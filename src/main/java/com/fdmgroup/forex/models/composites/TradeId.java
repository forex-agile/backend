package com.fdmgroup.forex.models.composites;

import java.io.Serializable;
import java.util.*;

import com.fdmgroup.forex.models.Order;

public class TradeId implements Serializable {

    private UUID id;
    private Order order;

    public TradeId() {}

    public TradeId(UUID id, Order order) {
        this.id = id;
        this.order = order;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeId tradeId = (TradeId) o;
        return Objects.equals(id, tradeId.id) &&
               Objects.equals(order, tradeId.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order);
    }

}
