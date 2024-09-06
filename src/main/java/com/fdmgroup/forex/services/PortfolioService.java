package com.fdmgroup.forex.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Portfolio;
import com.fdmgroup.forex.models.User;
import com.fdmgroup.forex.repos.PortfolioRepo;
import com.fdmgroup.forex.repos.UserRepo;

@Service
public class PortfolioService {

	private PortfolioRepo portfolioRepo;
	private UserRepo userRepo;

	public PortfolioService(PortfolioRepo portfolioRepo, UserRepo userRepo) {
		this.portfolioRepo = portfolioRepo;
		this.userRepo = userRepo;
	}

	public Portfolio findPortfolioById(UUID id) throws RecordNotFoundException {
		Optional<Portfolio> portfolioOptional = portfolioRepo.findById(id);
		return portfolioOptional
				.orElseThrow(() -> new RecordNotFoundException("Portfolio with ID '" + id + "'' not found"));
	}

	public Portfolio findPortfolioByUsername(String username) throws RecordNotFoundException {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new RecordNotFoundException("User with username '" + username + "' not found"));

		Optional<Portfolio> portfolioOptional = portfolioRepo.findByUser(user);
		return portfolioOptional.orElseThrow(
				() -> new RecordNotFoundException("Portfolio with user ID '" + user.getId() + "'' not found"));
	}

}
