package com.fdmgroup.forex.models;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

public class OpenOrder {

    @Id
    @JoinColumn(name = "FK_Order_ID", nullable = false)
    private Order order;

    private double unfulfilledAmount;

    public OpenOrder(Order order, double unfulfilledAmount) {
        this.order = order;
        this.unfulfilledAmount = unfulfilledAmount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public double getUnfulfilledAmount() {
        return unfulfilledAmount;
    }

    public void setUnfulfilledAmount(double unfulfilledAmount) {
        this.unfulfilledAmount = unfulfilledAmount;
    }

}
