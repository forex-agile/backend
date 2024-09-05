package com.fdmgroup.forex.models;

import java.time.Instant;

import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;

import jakarta.persistence.Entity;

@Entity
public class LimitOrder extends Order {

    private double baseLimit;

    public LimitOrder() {}

    public LimitOrder(OpenOrder openOrder, User user, Instant expiryDate, Currency baseFx, Currency quoteFx, double quoteAmount, double baseLimit) {
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
        this.baseLimit = baseLimit;
    }

    public double getBaseLimit() {
        return baseLimit;
    }

    public void setBaseLimit(double baseLimit) {
        this.baseLimit = baseLimit;
    }

}
