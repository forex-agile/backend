package com.fdmgroup.forex.models.composites;

import java.io.Serializable;
import java.util.*;

public class TradeId implements Serializable {

    private UUID id;
    private UUID orderId;

    public TradeId() {}

    public TradeId(UUID id, UUID orderId) {
        this.id = id;
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeId tradeId = (TradeId) o;
        return Objects.equals(id, tradeId.id) &&
               Objects.equals(orderId, tradeId.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId);
    }

}
