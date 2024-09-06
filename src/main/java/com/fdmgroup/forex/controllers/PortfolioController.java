package com.fdmgroup.forex.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.forex.models.Portfolio;
import com.fdmgroup.forex.models.dto.GetPortfolioDTO;
import com.fdmgroup.forex.services.PortfolioService;

@RestController
@RequestMapping("/api/v1/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

	private PortfolioService portfolioService;

	public PortfolioController(PortfolioService portfolioService) {
		super();
		this.portfolioService = portfolioService;
	}

	@GetMapping("/username/{username}")
	public ResponseEntity<GetPortfolioDTO> getPortfolioByUsername(@PathVariable String username) {
		Portfolio portfolio = portfolioService.findPortfolioByUsername(username);
		return ResponseEntity.ok(new GetPortfolioDTO(portfolio));
	}

	@GetMapping("/{id}")
	public ResponseEntity<GetPortfolioDTO> getPortfolio(@PathVariable UUID id) {
		Portfolio portfolio = portfolioService.findPortfolioById(id);
		return ResponseEntity.ok(new GetPortfolioDTO(portfolio));
	}

}
