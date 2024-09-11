package com.fdmgroup.forex.services;

import java.io.*;
import java.time.LocalDateTime;
import java.math.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.forex.exceptions.*;
import com.fdmgroup.forex.models.*;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.repos.FxRateRepo;
import com.fdmgroup.forex.repos.FxRateUpdateTimeRepo;

@Service
public class FxRateService {

    @Value("${fxrates.api.usd.url}")
    private String fxApiUsdUrl;

    private FxRateRepo fxRateRepo;
    private APIService apiService;
    private CurrencyService currencyService;

    @Autowired
    private FxRateUpdateTimeRepo fxRateUpdateTimeRepo;

    public FxRateService(FxRateRepo fxRateRepo, FxRateUpdateTimeRepo fxRateUpdateTimeRepo, APIService apiService, CurrencyService currencyService) {
        this.fxRateRepo = fxRateRepo;
        this.fxRateRepo = fxRateRepo;
        this.apiService = apiService;
        this.currencyService = currencyService;
    }

    public List<FxRate> getUpdatedFxRates() {
        FxRateUpdateTime updateTime = findFxRateUpdateTime();

        if (needsUpdate(updateTime.getLastUpdateTime())) {
            List<FxRate> updatedRates = fetchAndUpdateFxRates();
            updateTime.setLastUpdateTime(LocalDateTime.now());
            fxRateUpdateTimeRepo.save(updateTime);
            return updatedRates;
        } else {
            return findAllFxRates();
        }
    }

    public FxRateUpdateTime findFxRateUpdateTime() {
        FxRateUpdateTime updateTime = fxRateUpdateTimeRepo.findById(1)
            .orElse(new FxRateUpdateTime());
        fxRateUpdateTimeRepo.save(updateTime);
        return updateTime;
    }

    private boolean needsUpdate(LocalDateTime lastUpdateTime) {
        return lastUpdateTime.isBefore(LocalDateTime.now().minusHours(24));
    }

    public List<FxRate> findAllFxRates() {
        return fxRateRepo.findAll();
    }

    public List<FxRate> fetchAndUpdateFxRates() throws InternalServerErrorException {
        try {
            String response = apiService.getFxRates();
            Map<String,Double> usdRates = parseFxRatesResponse(response);
            return updateFxRatesFromUSDRates(usdRates);

        } catch (Exception e) {
            throw new InternalServerErrorException("Error updating exchange rates via external API: " + e.getMessage());
        }
    }

    private Map<String,Double> parseFxRatesResponse(String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = mapper.readValue(response, new TypeReference<Map<String, Object>>() {});

        Object ratesObject = data.get("rates");
        if (!(ratesObject instanceof Map<?, ?>)) {
            throw new IllegalArgumentException("Expected 'rates' to be a Map, but got: " + ratesObject.getClass());
        }
    
        Map<?, ?> rawRates = (Map<?, ?>) ratesObject;
        Map<String, Double> rates = new HashMap<>();
    
        for (Map.Entry<?, ?> entry : rawRates.entrySet()) {
            if (!(entry.getKey() instanceof String)) {
                throw new IllegalArgumentException("Expected rate key to be a String, but got: " + entry.getKey().getClass());
            }
            String key = (String) entry.getKey();
            Object value = entry.getValue();
    
            if (value instanceof Integer intValue) {
                rates.put(key, intValue.doubleValue());
            } else if (value instanceof Double doubleValue) {
                rates.put(key, doubleValue);
            } else if (value instanceof Number numberValue) {
                rates.put(key, numberValue.doubleValue());
            } else {
                throw new IllegalArgumentException("Unexpected value type in rates map: " + value.getClass());
            }
        }
        return rates;
    }

    private List<FxRate> updateFxRatesFromUSDRates(Map<String,Double> rates) {
        List<FxRate> fxRates = new ArrayList<>();
        for (String code : rates.keySet()) {
            double rate = Double.valueOf(rates.get(code));
            try {
                FxRate fxRate = createOrUpdateFxRate(code, rate);
                fxRates.add(fxRate);
            } catch (RecordNotFoundException e) {

            }
        }
        return fxRates;
    }

    private FxRate createOrUpdateFxRate(String code, double rate) {
        BigDecimal bdRate = BigDecimal.valueOf(rate);
        double inverseRate = BigDecimal.ONE.divide(bdRate, 10, RoundingMode.HALF_UP).doubleValue();
        try {
            FxRate fxRate = findFxRateByCurrencyId(code);
            fxRate.setRateToUSD(inverseRate);
            return fxRateRepo.save(fxRate);
        } catch (RecordNotFoundException e) {
            Currency currency = currencyService.findCurrencyById(code);
            FxRate fxRate = new FxRate(currency, inverseRate);
            return fxRateRepo.save(fxRate);
        }
    }

    public FxRate findFxRateById(UUID id) throws RecordNotFoundException {
        Optional<FxRate> fxRateOptional = fxRateRepo.findById(id);
        return fxRateOptional.orElseThrow(() -> 
            new RecordNotFoundException("FxRate with id '" + id + "' not found"));
    }

    public FxRate findFxRateByCurrencyId(String id) throws RecordNotFoundException {
        Optional<FxRate> fxRateOptional = fxRateRepo.findByCurrency_CurrencyCode(id);
        return fxRateOptional.orElseThrow(() -> 
            new RecordNotFoundException("FxRate for currency with code '" + id + "' not found"));
    }

}
