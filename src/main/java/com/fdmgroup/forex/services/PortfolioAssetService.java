package com.fdmgroup.forex.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.PortfolioAsset;
import com.fdmgroup.forex.repos.PortfolioAssetRepo;

public class PortfolioAssetService {

    private PortfolioAssetRepo portfolioAssetRepo;

    public PortfolioAssetService(PortfolioAssetRepo portfolioAssetRepo) {
        this.portfolioAssetRepo = portfolioAssetRepo;
    }

    public PortfolioAsset findById(UUID id) throws RecordNotFoundException {
        Optional<PortfolioAsset> portfolioAssetOptional = portfolioAssetRepo.findById(id);
        return portfolioAssetOptional.orElseThrow(() -> 
            new RecordNotFoundException("portfolio asset with ID '" + id + "'' not found"));
    }

    public List<PortfolioAsset> findByPortfolioId(UUID id) throws RecordNotFoundException {
        List<PortfolioAsset> portfolioAssets = portfolioAssetRepo.findByPortfolio_Id(id);
        return portfolioAssets;
    }

}
