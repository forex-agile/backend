package com.fdmgroup.forex.controllers;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fdmgroup.forex.models.Trade;
import com.fdmgroup.forex.services.TradeService;


@RestController
@RequestMapping("api/v1/trade")
@CrossOrigin(origins = "*")
public class TradeController {

    private TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping("/portfolio/{id}")
    public ResponseEntity<List<Trade>> getTradesByPortfolioId(@RequestParam UUID id) {
        List<Trade> trades = tradeService.findTradesByPortfolioId(id);
        if (trades.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(trades);
    }
}
