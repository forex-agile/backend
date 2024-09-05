package com.fdmgroup.forex.models;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "orders")
public abstract class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OpenOrder openOrder;

    @ManyToOne()
    @JoinColumn(name = "FK_User_ID", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
	private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
	private OrderStatus orderStatus;

    @Column(nullable = false, updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Instant creationDate;

    @Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Instant expiryDate;

    @ManyToOne()
    @JoinColumn(name = "FK_From_Currency_Code", nullable = false)
    private Currency baseFx;

    @ManyToOne()
    @JoinColumn(name = "FK_To_Currency_Code", nullable = false)
    private Currency quoteFx;

    private double quoteAmount;

    protected Order() {

    }

    public Order(OpenOrder openOrder, User user, Instant expiryDate, OrderStatus orderStatus, OrderType orderType, Currency baseFx, Currency quoteFx, double quoteAmount) {
        this.openOrder = openOrder;
        this.user = user;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.baseFx = baseFx;
        this.quoteFx = quoteFx;
        this.quoteAmount = quoteAmount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OpenOrder getOpenOrder() {
        return openOrder;
    }

    public void setOpenOrder(OpenOrder openOrder) {
        this.openOrder = openOrder;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Currency getBaseFx() {
        return baseFx;
    }

    public void setBaseFx(Currency baseFx) {
        this.baseFx = baseFx;
    }

    public Currency getQuoteFx() {
        return quoteFx;
    }

    public void setQuoteFx(Currency quoteFx) {
        this.quoteFx = quoteFx;
    }

    public double getQuoteAmount() {
        return quoteAmount;
    }

    public void setQuoteAmount(double quoteAmount) {
        this.quoteAmount = quoteAmount;
    }

}
