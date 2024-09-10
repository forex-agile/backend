package com.fdmgroup.forex.controllers;

import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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
        List<Currency> currencies = currencyService.findAllCurrencies();
        if (currencies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getCurrency(@PathVariable String id) {
        Currency currency = currencyService.findCurrencyById(id.toUpperCase());
        return ResponseEntity.ok(currency);
    }

    @PostMapping
    private ResponseEntity<List<Currency>> createCurrencies() {
        List<Currency> currencies = currencyService.fetchAndCreateCurrencies();
        if (currencies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(currencies);
    }
}
