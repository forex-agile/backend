package com.fdmgroup.forex.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.FxRate;
import com.fdmgroup.forex.repos.FxRateRepo;

@Service
public class FxRateService {

    private FxRateRepo fxRateRepo;

    public FxRateService(FxRateRepo fxRateRepo) {
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

    public FxRate findFxRateByCurrency(Currency currency) throws RecordNotFoundException {
        Optional<FxRate> fxRateOptional = fxRateRepo.findByCurrency(currency);
        return fxRateOptional.orElseThrow(() -> 
            new RecordNotFoundException("FxRate for currency with code '" + currency.getCurrencyCode() + "'' not found"));
    }

}
