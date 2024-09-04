package com.fdmgroup.forex.repos;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.FxRate;

@Repository
public interface FxRateRepo extends JpaRepository<FxRate, UUID> {

    Optional<FxRate> findByCurrency(Currency currency);

}
