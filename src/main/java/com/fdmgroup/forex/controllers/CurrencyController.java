package com.fdmgroup.forex.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.forex.services.CurrencyService;

@RestController
@RequestMapping("/api/v1/currency")
@CrossOrigin(origins = "*")
public class CurrencyController {

    private CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        super();
        this.currencyService = currencyService;
    }

}
