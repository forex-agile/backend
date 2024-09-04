package com.fdmgroup.forex.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Portfolio;
import com.fdmgroup.forex.services.PortfolioService;

@RestController
@RequestMapping("/api/v1/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

    private PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        super();
        this.portfolioService = portfolioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPortfolio(@PathVariable UUID id) {
        try {
            Portfolio portfolio = portfolioService.findById(id);
            return ResponseEntity.ok(portfolio);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getPortfolioByUserId(@PathVariable UUID id) {
        try {
            Portfolio portfolio = portfolioService.findByUserId(id);
            return ResponseEntity.ok(portfolio);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }

}
