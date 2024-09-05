package com.fdmgroup.forex.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.enums.TransferType;
import com.fdmgroup.forex.models.*;
import com.fdmgroup.forex.repos.FundTransferRepo;

import jakarta.transaction.Transactional;

@Service
public class FundTransferService {
	private FundTransferRepo fundTransferRepo;
	private CurrencyService currencyService;
	private PortfolioService portfolioService;
	private AssetService assetService;

	public FundTransferService(FundTransferRepo fundTransferRepo, CurrencyService currencyService, PortfolioService portfolioService, AssetService assetService) {
		this.fundTransferRepo = fundTransferRepo;
		this.currencyService = currencyService;
		this.portfolioService = portfolioService;
		this.assetService = assetService;
	}
	
    public List<FundTransfer> findAllTransfersByPortfolioId(UUID portfolioId) {
        return fundTransferRepo.findByPortfolio(portfolioService.findPortfolioById(portfolioId));
    }
	
    @Transactional
	public FundTransfer transferFund(FundTransfer fundTransfer) {
		validateAttributes(fundTransfer);
		fundTransfer.setTransferDate(new Date());
		if (fundTransfer.getTransferType().equals(TransferType.DEPOSIT)) {
			deposit(fundTransfer);
		} else {
			withdrawal(fundTransfer);
		}
		return fundTransferRepo.save(fundTransfer);
	}
	
	private void deposit(FundTransfer fundTransfer) {
		assetService.depositAsset(fundTransfer.getPortfolio(),fundTransfer.getCurrency(),fundTransfer.getAmount());
	}
	
	private void withdrawal(FundTransfer fundTransfer) {
		assetService.withdrawAsset(fundTransfer.getPortfolio(),fundTransfer.getCurrency(),fundTransfer.getAmount());
	}


	public void validateAttributes(FundTransfer fundTransfer) {
		validateCurrency(fundTransfer);
		validatePortfolio(fundTransfer);
	}

	private void validateCurrency(FundTransfer fundTransfer) {
		String currencyId = fundTransfer.getCurrency().getCurrencyCode();
		fundTransfer.setCurrency(currencyService.findCurrencyById(currencyId));
	}
	
	private void validatePortfolio(FundTransfer fundTransfer) {
		UUID portfolioId = fundTransfer.getPortfolio().getId();
		fundTransfer.setPortfolio(portfolioService.findPortfolioById(portfolioId));
	}
}
