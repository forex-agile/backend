package com.fdmgroup.forex.models;

import java.util.Date;
import java.util.UUID;

import com.fdmgroup.forex.enums.TransferType;

import jakarta.persistence.*;

@Entity
public class FundTransfer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id; 
	@ManyToOne()
	@JoinColumn(name = "FK_Currency_ID")
	private Currency currency;
	private double amount;
	private TransferType transferType;
	private Date transferDate;
	@ManyToOne()
	@JoinColumn(name = "FK_User_ID")
	private User user;
	
	public FundTransfer() {}	
	
	public FundTransfer(Currency currency, double amount, TransferType transferType, Date transferDate, User user) {
		super();
		this.currency = currency;
		this.amount = amount;
		this.transferType = transferType;
		this.transferDate = transferDate;
		this.user = user;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	
}
