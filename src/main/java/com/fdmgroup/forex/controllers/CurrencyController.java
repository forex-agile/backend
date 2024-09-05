package com.fdmgroup.forex.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.services.CurrencyService;

@RestController
@RequestMapping("/api/v1/currency")
@CrossOrigin(origins = "*")
public class CurrencyController {

    private CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        List<Currency> Currencies = currencyService.findAllCurrencies();
        if (Currencies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(Currencies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getCurrency(@PathVariable String id) {
        Currency currency = currencyService.findCurrencyById(id);
        return ResponseEntity.ok(currency);
    }
}
