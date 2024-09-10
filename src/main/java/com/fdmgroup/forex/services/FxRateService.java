package com.fdmgroup.forex.services;

import java.io.*;
import java.net.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.forex.exceptions.*;
import com.fdmgroup.forex.models.*;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.repos.FxRateRepo;

@Service
public class FxRateService {

    @Value("${fxrates.api.usd.url}")
    private String fxApiUsdUrl;

    private FxRateRepo fxRateRepo;
    private CurrencyService currencyService;

    public FxRateService(FxRateRepo fxRateRepo, CurrencyService currencyService) {
        this.fxRateRepo = fxRateRepo;
        this.currencyService = currencyService;
    }

    public List<FxRate> findAllFxRates() {
        return fxRateRepo.findAll();
    }

    public List<FxRate> fetchAndUpdateFxRates() throws InternalServerErrorException {
        try {
            String response = fetchFxRatesFromAPI();
            Map<String,Double> usdRates = parseFxRatesResponse(response);
            return updateFxRatesFromUSDRates(usdRates);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error updating exchange rates via external API");
        }
    }

    private String fetchFxRatesFromAPI() throws IOException {
        URL obj = new URL(fxApiUsdUrl);
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
            throw new IOException("Failed to fetch exchange rates from API");
        }
    }

    private Map<String,Double> parseFxRatesResponse(String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = mapper.readValue(response, new TypeReference<Map<String, Object>>() {
        });

        @SuppressWarnings("unchecked")  // API response structure is known
        Map<String,Double> rates = (Map<String,Double>) data.get("rates");
        return rates;
    }

    private List<FxRate> updateFxRatesFromUSDRates(Map<String,Double> rates) {
        List<FxRate> fxRates = new ArrayList<>();
        for (String code : rates.keySet()) {
            double rate = rates.get(code);
            
            FxRate fxRate = createOrUpdateFxRate(code, rate);
            fxRates.add(fxRate);
        }
        return fxRates;
    }

    private FxRate createOrUpdateFxRate(String code, double rate) {
        try {
            FxRate fxRate = findFxRateByCurrencyId(code);
            fxRate.setRateToUSD(1 / rate);
            return fxRateRepo.save(fxRate);
        } catch (RecordNotFoundException e) {
            Currency currency = currencyService.findCurrencyById(code);
            FxRate fxRate = new FxRate(currency, 1 / rate);
            return fxRateRepo.save(fxRate);
        }
    }

    public FxRate findFxRateById(UUID id) throws RecordNotFoundException {
        Optional<FxRate> fxRateOptional = fxRateRepo.findById(id);
        return fxRateOptional.orElseThrow(() -> 
            new RecordNotFoundException("FxRate with id '" + id + "'' not found"));
    }

    public FxRate findFxRateByCurrencyId(String id) throws RecordNotFoundException {
        Optional<FxRate> fxRateOptional = fxRateRepo.findByCurrency_CurrencyCode(id);
        return fxRateOptional.orElseThrow(() -> 
            new RecordNotFoundException("FxRate for currency with code '" + id + "'' not found"));
    }

}
