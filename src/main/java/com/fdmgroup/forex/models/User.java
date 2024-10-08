package com.fdmgroup.forex.models;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

/**
 * Model a user account on the Forex platform
 */
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(unique = true, nullable = false)
	@JsonIgnore
	private String email;

	@Column(nullable = false)
	@JsonIgnore
	private String password;

	@ManyToOne
	@JoinColumn(name = "FK_Preferred_Currency_Code", nullable = false)
	@JsonIgnore
	private Currency preferredCurrency;

	@JsonIgnore
	private String bankAccount;

	@ManyToOne
	@JoinColumn(name = "FK_Role_Id", nullable = false)
	@JsonIgnore
	private Role role;

	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date createdDate;

	@Column(nullable = false, updatable = false)
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
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
