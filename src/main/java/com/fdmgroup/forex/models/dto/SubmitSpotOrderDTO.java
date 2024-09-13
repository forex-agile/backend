package com.fdmgroup.forex.models.dto;

import java.util.Date;

import com.fdmgroup.forex.enums.OrderSide;
import com.fdmgroup.forex.enums.OrderType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * SubmitSpotOrderDTO
 */
public class SubmitSpotOrderDTO {

	@NotNull(message = "Order type is required.")
	private OrderType orderType;

	@NotNull(message = "Order side is required.")
	private OrderSide orderSide;

	@NotNull(message = "Base currency is required.")
	@NotBlank(message = "Base currency is required.")
	private String baseFx;

	@NotNull(message = "Quote currency is required.")
	@NotBlank(message = "Quote currency is required.")
	private String quoteFx;

	@NotNull(message = "Total amount is required.")
	@Positive(message = "Total amount must be a positive number.")
	private Double total;

	@Positive(message = "Limit must be a positive number.")
	private Double limit;

	@NotNull(message = "Expiry date is required.")
	private Date expiryDate;

	public SubmitSpotOrderDTO() {
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public OrderSide getOrderSide() {
		return orderSide;
	}

	public void setOrderSide(OrderSide orderSide) {
		this.orderSide = orderSide;
	}

	public String getBaseFx() {
		return baseFx;
	}

	public void setBaseFx(String baseFx) {
		this.baseFx = baseFx;
	}

	public String getQuoteFx() {
		return quoteFx;
	}

	public void setQuoteFx(String quoteFx) {
		this.quoteFx = quoteFx;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getLimit() {
		return limit;
	}

	public void setLimit(Double limit) {
		this.limit = limit;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

}
