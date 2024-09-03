package com.fdmgroup.forex.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.forex.services.PortfolioAssetService;

@RestController
@RequestMapping("/api/v1/portfolio-asset")
@CrossOrigin(origins = "*")
public class PortfolioAssetController {

    private PortfolioAssetService portfolioAssetService;

    public PortfolioAssetController(PortfolioAssetService portfolioAssetService) {
        super();
        this.portfolioAssetService = portfolioAssetService;
    }

}
