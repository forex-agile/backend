package com.fdmgroup.forex.models.dto;

import java.util.Date;
import java.util.UUID;

import com.fdmgroup.forex.enums.*;
import com.fdmgroup.forex.models.Order;

/**
 * OrderResponseDTO
 */
public class OrderResponseDTO {

	private UUID id;
	private UUID portfolioId;
	private UUID userId;
	private OrderType orderType;
	private OrderSide orderSide;
	private OrderStatus orderStatus;
	private Date creationDate;
	private Date expiryDate;
	private String baseFx; // currency code
	private String quoteFx; // currency code
	private double total;
	private double residual;
	private double limit;

	public OrderResponseDTO(Order order) {
		this.id = order.getId();
		this.portfolioId = order.getPortfolio().getId();
		this.orderType = order.getOrderType();
		this.orderSide = order.getOrderSide();
		this.orderStatus = order.getOrderStatus();
		this.creationDate = order.getCreationDate();
		this.expiryDate = order.getExpiryDate();
		this.baseFx = order.getBaseFx().getCurrencyCode();
		this.quoteFx = order.getQuoteFx().getCurrencyCode();
		this.total = order.getTotal();
		this.residual = order.getResidual();
		this.limit = order.getLimit();
	}

	public OrderResponseDTO(Order order, UUID userId) {
		this(order);
		this.userId = userId;
	}

	public UUID getId() {
		return id;
	}

	public UUID getPortfolioId() {
		return portfolioId;
	}

	public UUID getUserId() {
		return userId;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public OrderSide getOrderSide() {
		return orderSide;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public String getBaseFx() {
		return baseFx;
	}

	public String getQuoteFx() {
		return quoteFx;
	}

	public double getTotal() {
		return total;
	}

	public double getResidual() {
		return residual;
	}

	public double getLimit() {
		return limit;
	}

}
