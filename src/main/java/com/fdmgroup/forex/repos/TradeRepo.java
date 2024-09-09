package com.fdmgroup.forex.repos;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.models.Trade;

@Repository
public interface TradeRepo extends JpaRepository<Trade, UUID> {

    List<Trade> findByOrder_Id(UUID id);
    
}
