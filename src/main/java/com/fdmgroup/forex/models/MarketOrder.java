package com.fdmgroup.forex.models;

import java.time.Instant;

import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;

import jakarta.persistence.Entity;

@Entity
public class MarketOrder extends Order {

    public MarketOrder() {}

    public MarketOrder(OpenOrder openOrder, User user, Instant expiryDate, Currency baseFx, Currency quoteFx, double quoteAmount) {
        super(
            openOrder, 
            user,
            expiryDate,
            OrderStatus.ACTIVE,
            OrderType.MARKET, 
            baseFx,
            quoteFx,
            quoteAmount
        );
    }

}
