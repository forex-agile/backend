package com.fdmgroup.forex.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.forex.models.dto.TradeHistoryResponseDTO;
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
    public ResponseEntity<List<TradeHistoryResponseDTO>> getTradesByPortfolioId(@PathVariable UUID id) {
        List<TradeHistoryResponseDTO> trades = tradeService.findTradesByPortfolioId(id);
        if (trades.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(trades);
    }
}
