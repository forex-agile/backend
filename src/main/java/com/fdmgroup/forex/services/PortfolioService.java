package com.fdmgroup.forex.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Portfolio;
import com.fdmgroup.forex.repos.PortfolioRepo;

@Service
public class PortfolioService {

    private PortfolioRepo portfolioRepo;

    public PortfolioService(PortfolioRepo portfolioRepo) {
        super();
        this.portfolioRepo = portfolioRepo;
    }

    public Portfolio findById(UUID id) throws RecordNotFoundException {
        Optional<Portfolio> portfolioOptional = portfolioRepo.findById(id);
        return portfolioOptional.orElseThrow(() -> 
            new RecordNotFoundException("portfolio with ID '" + id + "'' not found"));
    }

    public Portfolio findByUserId(UUID userId) throws RecordNotFoundException {
        Optional<Portfolio> portfolioOptional = portfolioRepo.findByUserId(userId);
        return portfolioOptional.orElseThrow(() -> 
            new RecordNotFoundException("portfolio with user ID '" + userId + "'' not found"));
    }

}
