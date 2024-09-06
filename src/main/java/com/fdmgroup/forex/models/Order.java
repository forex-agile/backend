package com.fdmgroup.forex.models;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.fdmgroup.forex.enums.OrderSide;
import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "FK_User_ID", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
	private OrderSide orderSide;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
	private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
	private OrderType orderType;

    @Column(nullable = false, updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

    @Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDate;

    @ManyToOne()
    @JoinColumn(name = "FK_Base_Currency_Code", nullable = false, updatable = false)
    private Currency baseFx;

    @ManyToOne()
    @JoinColumn(name = "FK_Quote_Currency_Code", nullable = false, updatable = false)
    private Currency quoteFx;

    @Column(nullable = false, updatable = false)
    private double total;

    @Column(nullable = false)
    private double residual;

    @Column(nullable = true, updatable = false)
    private double limit;

    protected Order() {
    }

    public Order(
        User user, OrderType orderType, OrderSide orderSide, OrderStatus orderStatus, Date expiryDate, 
        Currency baseFx, Currency quoteFx, double total, double residual
    ) {
        this.user = user;
        this.orderType = orderType;
        this.orderSide = orderSide;
        this.orderStatus = orderStatus;
        this.expiryDate = expiryDate;
        this.baseFx = baseFx;
        this.quoteFx = quoteFx;
        this.total = total;
        this.residual = residual;
    }

    public Order(
        User user, OrderType orderType, OrderSide orderSide, OrderStatus orderStatus, Date expiryDate, 
        Currency baseFx, Currency quoteFx, double total, double residual, double limit
    ) {
        this.user = user;
        this.orderType = orderType;
        this.orderSide = orderSide;
        this.orderStatus = orderStatus;
        this.expiryDate = expiryDate;
        this.baseFx = baseFx;
        this.quoteFx = quoteFx;
        this.total = total;
        this.residual = residual;
        this.limit = limit;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        if (id != null) {
            this.id = id;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
        }
    }

    public OrderSide getOrderSide() {
        return orderSide;
    }

    public void setOrderSide(OrderSide orderSide) {
        if (this.orderSide == null && orderSide != null) {
            this.orderSide = orderSide;
        }
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == null && orderStatus != null) {
            this.orderStatus = orderStatus;
        }
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        if (this.orderType == null && orderType != null) {
            this.orderType = orderType;
        }
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        if (this.expiryDate == null && expiryDate != null) {
            this.expiryDate = expiryDate;
        }
    }

    public Currency getBaseFx() {
        return baseFx;
    }

    public void setBaseFx(Currency baseFx) {
        if (this.baseFx == null && baseFx != null) {
            this.baseFx = baseFx;
        }
    }

    public Currency getQuoteFx() {
        return quoteFx;
    }

    public void setQuoteFx(Currency quoteFx) {
        if (this.quoteFx == null && quoteFx != null) {
            this.quoteFx = quoteFx;
        }
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getResidual() {
        return residual;
    }

    public void setResidual(double residual) {
        this.residual = residual;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

}
