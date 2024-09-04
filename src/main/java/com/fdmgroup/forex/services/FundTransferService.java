package com.fdmgroup.forex.services;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.enums.TransferType;
import com.fdmgroup.forex.models.FundTransfer;
import com.fdmgroup.forex.repos.FundTransferRepo;

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
	
	public FundTransfer transferFund(FundTransfer fundTransfer) {
		getManagedAttributes(fundTransfer);
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
	
	private void withdrawal(FundTransfer fundTransfer) throws RecordNotFoundException, InsufficientFundsException {
		assetService.withdrawAsset(fundTransfer.getPortfolio(),fundTransfer.getCurrency(),fundTransfer.getAmount());
	}


	public void getManagedAttributes(FundTransfer fundTransfer) {
		getManagedCurrency(fundTransfer);
		getManagedPortfolio(fundTransfer);
	}

	private void getManagedCurrency(FundTransfer fundTransfer) {
		String currencyId = fundTransfer.getCurrency().getCurrencyCode();
		fundTransfer.setCurrency(currencyService.findCurrencyById(currencyId));
	}
	
	private void getManagedPortfolio(FundTransfer fundTransfer) {
		UUID portfolioId = fundTransfer.getPortfolio().getId();
		fundTransfer.setPortfolio(portfolioService.findPortfolioById(portfolioId));
	}
}
