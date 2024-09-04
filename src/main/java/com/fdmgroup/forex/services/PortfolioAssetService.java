package com.fdmgroup.forex.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fdmgroup.forex.exceptions.InsufficientFundsException;
import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Portfolio;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.PortfolioAsset;
import com.fdmgroup.forex.repos.PortfolioAssetRepo;

public class PortfolioAssetService {

    private PortfolioAssetRepo portfolioAssetRepo;

    public PortfolioAssetService(PortfolioAssetRepo portfolioAssetRepo) {
        this.portfolioAssetRepo = portfolioAssetRepo;
    }

    public PortfolioAsset createPortfolioAsset(Portfolio portfolio, Currency currency) {
        return createPortfolioAsset(portfolio, currency, 0.0);
    }

    public PortfolioAsset createPortfolioAsset(Portfolio portfolio, Currency currency, double balance) {
        PortfolioAsset portfolioAsset = new PortfolioAsset(portfolio, currency, balance);
        return portfolioAssetRepo.save(portfolioAsset);
    }

    public PortfolioAsset findPortfolioAssetById(UUID id) throws RecordNotFoundException {
        Optional<PortfolioAsset> portfolioAssetOptional = portfolioAssetRepo.findById(id);
        return portfolioAssetOptional.orElseThrow(() -> 
            new RecordNotFoundException("Portfolio asset with ID '" + id + "'' not found"));
    }

    public List<PortfolioAsset> findPortfolioAssetByPortfolioId(UUID id) throws RecordNotFoundException {
        List<PortfolioAsset> portfolioAssets = portfolioAssetRepo.findByPortfolio_Id(id);
        return portfolioAssets;
    }

    public void depositAsset(Portfolio portfolio, Currency currency, double deposit) {
        try {
            PortfolioAsset portfolioAsset = findPortfolioAssetByPortfolioAndCurrency(portfolio, currency);
            portfolioAsset.setBalance(portfolioAsset.getBalance() + deposit);
            portfolioAssetRepo.save(portfolioAsset);
        
        } catch (RecordNotFoundException e) {
            createPortfolioAsset(portfolio, currency, deposit);
        }
    }

    public void withdrawAsset(Portfolio portfolio, Currency currency, double withdrawal) throws InsufficientFundsException, RecordNotFoundException {
        try {
            PortfolioAsset portfolioAsset = findPortfolioAssetByPortfolioAndCurrency(portfolio, currency);
            double balance = portfolioAsset.getBalance();
            if (withdrawal > balance) {
                throw new InsufficientFundsException("Withdrawal amount cannot exceed balance: balance=" + balance + ", withdrawal=" + withdrawal);
            } else {
                portfolioAsset.setBalance(balance - withdrawal);
            }
            portfolioAssetRepo.save(portfolioAsset);
        
        } catch (RecordNotFoundException e) {
            throw e;
        }
    }

    private PortfolioAsset findPortfolioAssetByPortfolioAndCurrency(Portfolio portfolio, Currency currency) throws RecordNotFoundException {
        Optional<PortfolioAsset> portfolioAssetOptional = portfolioAssetRepo.findByPortfolioAndCurrency(portfolio, currency);
        return portfolioAssetOptional.orElseThrow(() -> 
            new RecordNotFoundException("Portfolio asset with portfolio ID '" + portfolio.getId() + "' and currency code '" + currency.getCurrencyCode() + "' not found"));
    }

}
