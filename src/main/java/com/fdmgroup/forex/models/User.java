package com.fdmgroup.forex.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Model a user account on the Forex platform
 */
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@ManyToOne
	@JoinColumn(name = "preferred_currency_code")
	private Currency preferredCurrency;

	private String bankAccount;

	public User() {
	}

	public User(UUID id, String username, String email, String password, Currency preferredCurrency,
			String bankAccount) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.preferredCurrency = preferredCurrency;
		this.bankAccount = bankAccount;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Currency getPreferredCurrency() {
		return preferredCurrency;
	}

	public void setPreferredCurrency(Currency preferredCurrency) {
		this.preferredCurrency = preferredCurrency;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

}
