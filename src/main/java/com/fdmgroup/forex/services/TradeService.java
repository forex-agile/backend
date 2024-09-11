package com.fdmgroup.forex.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.models.Trade;
import com.fdmgroup.forex.repos.TradeRepo;

@Service
public class TradeService {

    private TradeRepo tradeRepo;

    public TradeService(TradeRepo tradeRepo) {
        this.tradeRepo = tradeRepo;
    }

    public List<Trade> findTradesByPortfolioId(UUID id) {
        return tradeRepo.findByOrder_Portfolio_Id(id);
    }

}
