package com.fdmgroup.forex.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.repos.CurrencyRepo;

@Service
public class CurrencyService {

    private CurrencyRepo currencyRepo;

    public CurrencyService(CurrencyRepo currencyRepo) {
        super();
        this.currencyRepo = currencyRepo;
    }

    public Currency findById(String id) throws RecordNotFoundException {
        Optional<Currency> currencyOptional = currencyRepo.findById(id);
        return currencyOptional.orElseThrow(() -> 
            new RecordNotFoundException("Currency with id '" + id + "'' not found"));
    }

}
