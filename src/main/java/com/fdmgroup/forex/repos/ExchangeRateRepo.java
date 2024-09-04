package com.fdmgroup.forex.repos;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.ExchangeRate;

@Repository
public interface ExchangeRateRepo extends JpaRepository<ExchangeRate, UUID> {

    Optional<ExchangeRate> findByCurrency(Currency currency);

}
