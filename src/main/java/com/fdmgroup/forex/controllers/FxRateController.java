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
import com.fdmgroup.forex.models.FxRate;
import com.fdmgroup.forex.services.FxRateService;

@RestController
@RequestMapping("api/v1/fx-rate")
@CrossOrigin(origins = "*")
public class FxRateController {

    private FxRateService fxRateService;

    public FxRateController(FxRateService fxRateService) {
        this.fxRateService = fxRateService;
    }

    @GetMapping
    public ResponseEntity<List<FxRate>> getAllFxRates() {
        List<FxRate> fxRates = fxRateService.findAllFxRates();
        if (fxRates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(fxRates);
    }

    @GetMapping("/currency")
    public ResponseEntity<?> getFxRateByCurrency(@RequestBody Currency currency) {
        try {
            FxRate fxRate = fxRateService.findFxRateByCurrency(currency);
            return ResponseEntity.ok(fxRate);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFxRate(@PathVariable UUID id) {
        try {
            FxRate fxRate = fxRateService.findFxRateById(id);
            return ResponseEntity.ok(fxRate);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }

}
