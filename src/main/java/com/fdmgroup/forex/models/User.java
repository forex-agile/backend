package com.fdmgroup.forex.models;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Model a user account on the Forex platform
 */
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotNull(message = "Username must be present.")
	@NotBlank(message = "Username cannot be blank or contain only whitespace characters.")
	@Column(unique = true, nullable = false)
	private String username;

	@NotNull(message = "Email must be present.")
	@Email(message = "Email must be a valid email address.")
	@Column(unique = true, nullable = false)
	private String email;

	@NotNull(message = "Password must be present.")
	@NotBlank(message = "Password cannot be blank or contain only whitespace characters.")
	@Column(nullable = false)
	private String password;

	@ManyToOne
	@JoinColumn(name = "FK_Preferred_Currency_Code")
	private Currency preferredCurrency;

	private String bankAccount;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(nullable = false, updatable = false)
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	public User() {
	}

	public User(UUID id, String username, String email, String password, Currency preferredCurrency,
			String bankAccount, Role role) {
		setId(id);
		setUsername(username);
		setEmail(email);
		setPassword(password);
		setPreferredCurrency(preferredCurrency);
		setBankAccount(bankAccount);
		setRole(role);
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
