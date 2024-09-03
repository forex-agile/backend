package com.fdmgroup.forex.repos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.forex.models.PortfolioAsset;

public interface PortfolioAssetRepo extends JpaRepository<PortfolioAsset, UUID> {

    List<PortfolioAsset> findByPortfolio_Id(UUID portfolioId);

}
