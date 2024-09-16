package com.fdmgroup.forex.controllers;

import java.util.*;

import org.springframework.http.HttpStatus;
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
	public ResponseEntity<FundTransfer> createFundTransfer(@RequestBody FundTransfer fundTransfer) {
	    FundTransfer savedFundTransfer = fundTransferService.transferFund(fundTransfer);
	    return ResponseEntity.status(HttpStatus.CREATED).body(savedFundTransfer);
	}
	
	@GetMapping("/{portfolioId}")
	public ResponseEntity<List<FundTransfer>> getAllFundTransfersByPortfolioId(@PathVariable UUID portfolioId ) {
		List<FundTransfer> fundTransfers = fundTransferService.findAllTransfersByPortfolioId(portfolioId);
		return ResponseEntity.ok(fundTransfers);
	}
	
}
