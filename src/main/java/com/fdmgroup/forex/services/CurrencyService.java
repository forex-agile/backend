package com.fdmgroup.forex.services;

import java.io.*;
import java.net.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.forex.exceptions.InternalServerErrorException;
import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.repos.CurrencyRepo;

@Service
public class CurrencyService {

    @Value("${fxrates.api.base.url}")
    private String fxAPIBaseURL;

    @Value("${fxrates.api.key}")
    private String fxAPIKey;

    private CurrencyRepo currencyRepo;
    private List<String> obsoleteCurrencyCodes;

    public CurrencyService(CurrencyRepo currencyRepo) {
        this.currencyRepo = currencyRepo;
        List<String> obsoleteCurrencyCodes = new ArrayList<>();
        obsoleteCurrencyCodes.add("SLL");
        this.obsoleteCurrencyCodes = obsoleteCurrencyCodes;
    }

    public List<Currency> fetchAndCreateCurrencies() throws InternalServerErrorException {
        try {
            String response = fetchCurrencyCodesFromAPI();
            List<List<String>> supportedCodes = parseCurrencyCodesResponse(response);
            return createCurrenciesFromCodes(supportedCodes);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error fetching from external currency API");
        }
    }

    private String fetchCurrencyCodesFromAPI() throws IOException {
        String url = fxAPIBaseURL + fxAPIKey + "/codes";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                response.append(inputLine);
            }
            input.close();
            return response.toString();
        } else {
            throw new IOException("Failed to fetch currency codes from API");
        }
    }

    private List<List<String>> parseCurrencyCodesResponse(String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = mapper.readValue(response, new TypeReference<Map<String, Object>>() {
        });

        @SuppressWarnings("unchecked")  // API response structure is known
        List<List<String>> supportedCodes = (List<List<String>>) data.get("supported_codes");
        return supportedCodes;
    }

    private List<Currency> createCurrenciesFromCodes(List<List<String>> supportedCodes) {
        List<Currency> currencies = new ArrayList<>();
        for (List<String> codeNamePair : supportedCodes) {
            String code = codeNamePair.get(0);
            String name = codeNamePair.get(1);
            
            if (obsoleteCurrencyCodes.contains(code)) {
                continue;
            }
            
            Currency currency = createCurrency(code, name);
            currencies.add(currency);
        }
        return currencies;
    }

    public Currency createCurrency(String currencyCode, String currencyName) {
        Currency currency = new Currency(currencyCode, currencyName);
        return currencyRepo.save(currency);
    }

    public List<Currency> findAllCurrencies() {
        return currencyRepo.findAll();
    }

    public Currency findCurrencyById(String id) throws RecordNotFoundException {
        Optional<Currency> currencyOptional = currencyRepo.findById(id);
        return currencyOptional
                .orElseThrow(() -> new RecordNotFoundException("Currency with id '" + id + "'' not found"));
    }

}
