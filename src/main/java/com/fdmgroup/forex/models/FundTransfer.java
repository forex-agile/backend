package com.fdmgroup.forex.models;

import java.util.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fdmgroup.forex.enums.TransferType;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

@Entity
public class FundTransfer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id; 
	
	@ManyToOne()
	@JoinColumn(name = "FK_Currency_ID")
	@Column(nullable = false)
	private Currency currency;
	
	@Column(nullable = false)
	@Positive(message = "Amount must be positive")
	private double amount;
	
	@Column(nullable = false)
	private TransferType transferType;
	
	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date transferDate;
	
	@ManyToOne()
	@JoinColumn(name = "FK_Portfolio_ID")
	@Column(nullable = false)
	private Portfolio portfolio;
	
	public FundTransfer() {}	
	
	public FundTransfer(Currency currency, double amount, TransferType transferType, Date transferDate, Portfolio portfolio) {
		this.currency = currency;
		this.amount = amount;
		this.transferType = transferType;
		this.transferDate = transferDate;
		this.portfolio = portfolio;
	}

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public TransferType getTransferType() {
		return transferType;
	}
	public void setTransferType(TransferType transferType) {
		this.transferType = transferType;
	}
	public Date getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	public Portfolio getPortfolio() {
		return portfolio;
	}
	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}
	
	
	
	
}
