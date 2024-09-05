package com.fdmgroup.forex.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fdmgroup.forex.exceptions.InsufficientFundsException;
import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Portfolio;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.Asset;
import com.fdmgroup.forex.repos.AssetRepo;

public class AssetService {

    private AssetRepo assetRepo;

    public AssetService(AssetRepo assetRepo) {
        this.assetRepo = assetRepo;
    }

    public Asset createAsset(Portfolio portfolio, Currency currency) {
        return createAsset(portfolio, currency, 0.0);
    }

    public Asset createAsset(Portfolio portfolio, Currency currency, double balance) {
        Asset Asset = new Asset(portfolio, currency, balance);
        return assetRepo.save(Asset);
    }

    public Asset findAssetById(UUID id) throws RecordNotFoundException {
        Optional<Asset> assetOptional = assetRepo.findById(id);
        return assetOptional.orElseThrow(() -> 
            new RecordNotFoundException("Portfolio asset with ID '" + id + "'' not found"));
    }

    public List<Asset> findAssetByPortfolioId(UUID id) throws RecordNotFoundException {
        List<Asset> assets = assetRepo.findByPortfolio_Id(id);
        return assets;
    }

    public void depositAsset(Portfolio portfolio, Currency currency, double deposit) {
        try {
            Asset Asset = findAssetByPortfolioAndCurrency(portfolio, currency);
            Asset.setBalance(Asset.getBalance() + deposit);
            assetRepo.save(Asset);
        
        } catch (RecordNotFoundException e) {
            createAsset(portfolio, currency, deposit);
        }
    }

    public void withdrawAsset(Portfolio portfolio, Currency currency, double withdrawal) throws InsufficientFundsException, RecordNotFoundException {
        Asset Asset = findAssetByPortfolioAndCurrency(portfolio, currency);
        double balance = Asset.getBalance();
        if (withdrawal > balance) {
            throw new InsufficientFundsException("Withdrawal amount cannot exceed balance: balance=" + balance + ", withdrawal=" + withdrawal);
        } else {
            Asset.setBalance(balance - withdrawal);
        }
        assetRepo.save(Asset);
    }

    private Asset findAssetByPortfolioAndCurrency(Portfolio portfolio, Currency currency) throws RecordNotFoundException {
        Optional<Asset> assetOptional = assetRepo.findByPortfolioAndCurrency(portfolio, currency);
        return assetOptional.orElseThrow(() -> 
            new RecordNotFoundException("Portfolio asset with portfolio ID '" + portfolio.getId() + "' and currency code '" + currency.getCurrencyCode() + "' not found"));
    }

}
