package com.fdmgroup.forex.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.forex.models.Portfolio;
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

	@GetMapping("/user/{id}")
	@PreAuthorize("#id.toString() == authentication.principal.claims['userId']")
	public ResponseEntity<Portfolio> getPortfolioByUserId(@PathVariable UUID id) {
		Portfolio portfolio = portfolioService.findPortfolioByUserId(id);
		return ResponseEntity.ok(portfolio);
	}

	@GetMapping("/{id}")
	@PostAuthorize("returnObject.getBody().user.id == authentication.principal.claims['userId']")
	public ResponseEntity<Portfolio> getPortfolio(@PathVariable UUID id) {
		Portfolio portfolio = portfolioService.findPortfolioById(id);
		return ResponseEntity.ok(portfolio);
	}

}
