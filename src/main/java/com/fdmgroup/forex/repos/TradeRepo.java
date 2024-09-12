package com.fdmgroup.forex.repos;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.forex.models.Trade;
import com.fdmgroup.forex.models.composites.TradeId;

@Repository
public interface TradeRepo extends JpaRepository<Trade, TradeId> {

    List<Trade> findByOrder_Portfolio_Id(UUID id);
    
}
