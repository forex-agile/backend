package com.fdmgroup.forex.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/currency/{id}")
    public ResponseEntity<FxRate> getFxRateByCurrencyId(@PathVariable String id) {
        FxRate fxRate = fxRateService.findFxRateByCurrencyId(id);
        return ResponseEntity.ok(fxRate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FxRate> getFxRate(@PathVariable UUID id) {
        FxRate fxRate = fxRateService.findFxRateById(id);
        return ResponseEntity.ok(fxRate);
    }

    @PutMapping
    private ResponseEntity<List<FxRate>> updateFxRates() {
        List<FxRate> fxRates = fxRateService.fetchAndUpdateFxRates();
        if (fxRates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(fxRates);
    }

}
