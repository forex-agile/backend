
package com.fdmgroup.forex.models;

import java.util.*;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "FK_Order_ID", nullable = false, updatable = false)
    private Order order;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date executionDate;

    private double baseFxAmount;
    private double quoteFxAmount;

    public Trade() {}

    public Trade(Order order, double baseFxAmount, double quoteFxAmount) {
        this.order = order;
        this.baseFxAmount = baseFxAmount;
        this.quoteFxAmount = quoteFxAmount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        if (id != null) {
            this.id = id;
        }
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        if (order != null) {
            this.order = order;
        }
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
