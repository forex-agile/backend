package com.fdmgroup.forex.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.Portfolio;
import com.fdmgroup.forex.models.Asset;

public interface AssetRepo extends JpaRepository<Asset, UUID> {

    List<Asset> findByPortfolio_Id(UUID portfolioId);
    Optional<Asset> findByPortfolioAndCurrency(Portfolio portfolio, Currency currency);

}
