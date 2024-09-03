package com.fdmgroup.forex.controllers;

import org.springframework.web.bind.annotation.*;

import com.fdmgroup.forex.services.FundTransferService;

@RestController
@RequestMapping("/api/v1/fund_transfer")
@CrossOrigin(origins = "*")
public class FundTransferController {
	private FundTransferService fundTransferService;
	
	public FundTransferController(FundTransferService fundTransferService) {
		super();
		this.fundTransferService = fundTransferService;
	}
	
}
