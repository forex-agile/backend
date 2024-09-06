package com.fdmgroup.forex.repos;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.models.Portfolio;

@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, UUID> {

    Optional<Portfolio> findByUser_UserId(UUID id);

}
