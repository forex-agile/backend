package com.fdmgroup.forex.services;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.repos.FundTransferRepo;

@Service
public class FundTransferService {
	private FundTransferRepo fundTransferRepo;

	public FundTransferService(FundTransferRepo fundTransferRepo) {
		super();
		this.fundTransferRepo = fundTransferRepo;
	}
	
}
