package com.fdmgroup.forex.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.models.*;
import com.fdmgroup.forex.repos.TradeRepo;

@Service
public class TradeService {

    private TradeRepo tradeRepo;

    public TradeService(TradeRepo tradeRepo) {
        this.tradeRepo = tradeRepo;
    }

    public Trade createTrade(UUID id, Order order, double baseFxAmount, double quoteFxAmount) {
        Trade trade = new Trade(id, order, baseFxAmount, quoteFxAmount);
        return tradeRepo.save(trade);
    }

    public List<Trade> findTradesByPortfolioId(UUID id) {
        return tradeRepo.findByOrder_Portfolio_Id(id);
    }

}
