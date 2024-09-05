package com.fdmgroup.forex.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fdmgroup.forex.models.FundTransfer;
import com.fdmgroup.forex.services.FundTransferService;

@RestController
@RequestMapping("/api/v1/fund-transfer")
@CrossOrigin(origins = "*")
public class FundTransferController {
	private FundTransferService fundTransferService;
	
	public FundTransferController(FundTransferService fundTransferService) {
		this.fundTransferService = fundTransferService;
	}
	
	@PostMapping("")
	public ResponseEntity<FundTransfer> createFundTransfer(FundTransfer fundTransfer) {
		FundTransfer savedFundTransfer = fundTransferService.transferFund(fundTransfer);
		return ResponseEntity.ok(savedFundTransfer);
	}
	
	@GetMapping("/{portfolioId}")
	public ResponseEntity<List<FundTransfer>> getAllFundTransfersByPortfolioId(@PathVariable UUID portfolioId ) {
		List<FundTransfer> fundTransfers = fundTransferService.findAllTransfersByPortfolioId(portfolioId);
		return ResponseEntity.ok(fundTransfers);
	}
	
}
