package com.fdmgroup.forex.models.dto;

import java.util.Date;
import java.util.UUID;

import com.fdmgroup.forex.enums.OrderSide;
import com.fdmgroup.forex.enums.OrderStatus;
import com.fdmgroup.forex.enums.OrderType;
import com.fdmgroup.forex.models.Currency;
import com.fdmgroup.forex.models.Order;
import com.fdmgroup.forex.models.Trade;

public class TradeHistoryResponseDTO {
	private UUID id;
	private UUID orderId;
	private UUID portfolioId;
	private UUID userId;
	private OrderSide orderSide;
	private OrderStatus orderStatus;
	private OrderType orderType;
	private Date creationDate;
	private Date expiryDate;
	private Currency baseFx;
	private Currency quoteFx;
	private double total;
	private double residual;
	private double limit;
	private Date executionDate;
	private double baseFxAmount;
	private double quoteFxAmount;

	public TradeHistoryResponseDTO(Trade trade) {
		this.id = trade.getId();
		this.orderId = trade.getOrder().getId();
		this.portfolioId = trade.getOrder().getPortfolio().getId();
		this.userId = trade.getOrder().getPortfolio().getUser().getId();
		this.orderSide = trade.getOrder().getOrderSide();
		this.orderStatus = trade.getOrder().getOrderStatus();
		this.orderType = trade.getOrder().getOrderType();
		this.creationDate = trade.getOrder().getCreationDate();
		this.expiryDate = trade.getOrder().getExpiryDate();
		this.baseFx = trade.getOrder().getBaseFx();
		this.quoteFx = trade.getOrder().getQuoteFx();
		this.total = trade.getOrder().getTotal();
		this.residual = trade.getOrder().getResidual();
		this.limit = trade.getOrder().getLimit();
		this.executionDate = trade.getExecutionDate();
		this.baseFxAmount = trade.getBaseFxAmount();
		this.quoteFxAmount = trade.getQuoteFxAmount();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
	}

	public UUID getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(UUID portfolioId) {
		this.portfolioId = portfolioId;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public OrderSide getOrderSide() {
		return orderSide;
	}

	public void setOrderSide(OrderSide orderSide) {
		this.orderSide = orderSide;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
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
		this.baseFxAmount = baseFxAmount;
	}

	public double getQuoteFxAmount() {
		return quoteFxAmount;
	}

	public void setQuoteFxAmount(double quoteFxAmount) {
		this.quoteFxAmount = quoteFxAmount;
	}
	
	

}
