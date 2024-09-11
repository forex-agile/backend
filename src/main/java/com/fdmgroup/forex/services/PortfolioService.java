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
		this.portfolioRepo = portfolioRepo;
	}

	public Portfolio findPortfolioById(UUID id) throws RecordNotFoundException {
		Optional<Portfolio> portfolioOptional = portfolioRepo.findById(id);
		return portfolioOptional
				.orElseThrow(() -> new RecordNotFoundException("Portfolio with ID '" + id + "' not found"));
	}

	public Portfolio findPortfolioByUserId(UUID id) throws RecordNotFoundException {
		Optional<Portfolio> portfolioOptional = portfolioRepo.findByUser_Id(id);
		return portfolioOptional
				.orElseThrow(() -> new RecordNotFoundException("Portfolio with user ID '" + id + "' not found"));
	}

}
