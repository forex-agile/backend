package com.fdmgroup.forex.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.ExchangeRate;
import com.fdmgroup.forex.repos.ExchangeRateRepo;

@Service
public class ExchangeRateService {

    private ExchangeRateRepo exchangeRateRepo;

    public ExchangeRateService(ExchangeRateRepo exchangeRateRepo) {
        this.exchangeRateRepo = exchangeRateRepo;
    }

    public List<ExchangeRate> findAllExchangeRates() {
        return exchangeRateRepo.findAll();
    }

    public ExchangeRate findById(UUID id) throws RecordNotFoundException {
        Optional<ExchangeRate> exchangeRateOptional = exchangeRateRepo.findById(id);
        return exchangeRateOptional.orElseThrow(() -> 
            new RecordNotFoundException("ExchangeRate with id '" + id + "'' not found"));
    }

    public ExchangeRate findByCurrency(Currency currency) throws RecordNotFoundException {
        Optional<ExchangeRate> exchangeRateOptional = exchangeRateRepo.findByCurrency(currency);
        return exchangeRateOptional.orElseThrow(() -> 
            new RecordNotFoundException("ExchangeRate for currency with code '" + currency.getCurrencyCode() + "'' not found"));
    }

}
