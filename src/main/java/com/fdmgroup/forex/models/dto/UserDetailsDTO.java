package com.fdmgroup.forex.models.dto;

import java.util.Date;

import com.fdmgroup.forex.models.User;

/**
 * UserSignupDTO
 */
public class UserDetailsDTO {

	private String id;
	private String username;
	private String email;
	private String preferredCurrencyCode;
	private String bankAccountId;
	private String role;
	private Date createdDate;
	private Date modifiedDate;

	public UserDetailsDTO() {
	}

	public UserDetailsDTO(User user) {
		this.id = user.getId().toString();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.preferredCurrencyCode = user.getPreferredCurrency() == null ? null
				: user.getPreferredCurrency().getCurrencyCode();
		this.bankAccountId = user.getBankAccount();
		this.role = user.getRole() == null ? null : user.getRole().getRole();
		this.createdDate = user.getCreatedDate();
		this.modifiedDate = user.getModifiedDate();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getPreferredCurrencyCode() {
		return preferredCurrencyCode;
	}

	public void setPreferredCurrencyCode(String preferredCurrencyCode) {
		this.preferredCurrencyCode = preferredCurrencyCode;
	}

	public String getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(String bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
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
