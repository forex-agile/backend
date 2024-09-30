package com.fdmgroup.forex.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fdmgroup.forex.models.Order;
import com.fdmgroup.forex.models.Trade;
import com.fdmgroup.forex.models.dto.TradeHistoryResponseDTO;
import com.fdmgroup.forex.repos.TradeRepo;

@Service
public class TradeService {

    private TradeRepo tradeRepo;

    public TradeService(TradeRepo tradeRepo) {
        this.tradeRepo = tradeRepo;
    }

    public Trade createTrade(UUID id, Order order, double baseFxAmount, double quoteFxAmount) {
        validateArgs(id, order, baseFxAmount, quoteFxAmount);
        Trade trade = new Trade(id, order, baseFxAmount, quoteFxAmount);
        return tradeRepo.save(trade);
    }

    private void validateArgs(UUID id, Order order, double baseFxAmount, double quoteFxAmount) throws IllegalArgumentException {
        if (!(id instanceof UUID)) {
            throw new IllegalArgumentException("Trade ID must be a valid UUID: id=" + id);
        }
        if (order == null) {
            throw new IllegalArgumentException("Trade must derive from a valid order");
        }
        if (baseFxAmount <= 0 || quoteFxAmount <= 0) {
            throw new IllegalArgumentException("Trade amounts must be positive: base=" + baseFxAmount + ", quote=" + quoteFxAmount);
        }
    }

    public List<TradeHistoryResponseDTO> findTradesByPortfolioId(UUID id) {
        return tradeRepo.findByOrder_Portfolio_Id(id).stream().map(TradeHistoryResponseDTO::new).toList();
        
    }

}
