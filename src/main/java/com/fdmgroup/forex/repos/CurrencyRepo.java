package com.fdmgroup.forex.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.models.Currency;

@Repository
public interface CurrencyRepo extends JpaRepository<Currency, String> {

    Optional<Currency> findByCurrencyName(String name);

}
