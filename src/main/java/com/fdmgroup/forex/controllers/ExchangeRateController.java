package com.fdmgroup.forex.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.ExchangeRate;
import com.fdmgroup.forex.services.ExchangeRateService;

@RestController
@RequestMapping("api/v1/exchange-rate")
@CrossOrigin(origins = "*")
public class ExchangeRateController {

    private ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRateService.findAllExchangeRates();
        if (exchangeRates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(exchangeRates);
    }

    @GetMapping("/currency")
    public ResponseEntity<?> getExchangeRateByCurrency(@RequestBody Currency currency) {
        try {
            ExchangeRate exchangeRate = exchangeRateService.findExchangeRateByCurrency(currency);
            return ResponseEntity.ok(exchangeRate);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExchangeRate(@PathVariable UUID id) {
        try {
            ExchangeRate exchangeRate = exchangeRateService.findExchangeRateById(id);
            return ResponseEntity.ok(exchangeRate);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }

}
