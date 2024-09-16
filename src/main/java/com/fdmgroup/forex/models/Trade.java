
package com.fdmgroup.forex.models;

import java.util.*;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import com.fdmgroup.forex.models.composites.TradeId;

@Entity
@Table(name = "trades")
@IdClass(TradeId.class)
public class Trade {

    @Id
    private UUID id;

    @Id
    @ManyToOne()
    @JoinColumn(name = "FK_Order_ID", nullable = false)
    private Order order;

    @Column(nullable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date executionDate;

    private double baseFxAmount;
    private double quoteFxAmount;

    public Trade() {}

    public Trade(UUID id, Order order, double baseFxAmount, double quoteFxAmount) {
        this.id = id;
        this.order = order;
        this.baseFxAmount = baseFxAmount;
        this.quoteFxAmount = quoteFxAmount;
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

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public double getBaseFxAmount() {
        return baseFxAmount;
    }

    public void setBaseFxAmount(double baseFxAmount) {
        if (baseFxAmount > 0) {
            this.baseFxAmount = baseFxAmount;
        }
    }

    public double getQuoteFxAmount() {
        return quoteFxAmount;
    }

    public void setQuoteFxAmount(double quoteFxAmount) {
        if (quoteFxAmount > 0) {
            this.quoteFxAmount = quoteFxAmount;
        }
    }

}
