package com.fdmgroup.forex.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.FxRate;
import com.fdmgroup.forex.repos.CurrencyRepo;
import com.fdmgroup.forex.repos.FxRateRepo;

@Service
public class FxRateService {

    private CurrencyRepo currencyRepo;
    private FxRateRepo fxRateRepo;

    public FxRateService(CurrencyRepo currencyRepo, FxRateRepo fxRateRepo) {
        this.currencyRepo = currencyRepo;
        this.fxRateRepo = fxRateRepo;
    }

    public List<FxRate> findAllFxRates() {
        return fxRateRepo.findAll();
    }

    public FxRate findFxRateById(UUID id) throws RecordNotFoundException {
        Optional<FxRate> fxRateOptional = fxRateRepo.findById(id);
        return fxRateOptional.orElseThrow(() -> 
            new RecordNotFoundException("FxRate with id '" + id + "'' not found"));
    }

    public FxRate findFxRateByCurrencyCode(String currencyCode) throws RecordNotFoundException {
        Currency currency = currencyRepo.findById(currencyCode).orElseThrow(() -> 
                    new RecordNotFoundException("Currency with code '" + currencyCode + "'' not found"));
        Optional<FxRate> fxRateOptional = fxRateRepo.findByCurrency(currency);
        return fxRateOptional.orElseThrow(() -> 
            new RecordNotFoundException("FxRate for currency with code '" + currencyCode + "' not found"));
    }

}
