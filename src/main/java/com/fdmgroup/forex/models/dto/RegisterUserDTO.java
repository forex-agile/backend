package com.fdmgroup.forex.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Model a user account on the Forex platform
 */
public class RegisterUserDTO {

	@NotNull(message = "Username must be present.")
	@NotBlank(message = "Username cannot be blank or contain only whitespace characters.")
	private String username;

	@NotNull(message = "Email must be present.")
	@Email(message = "Email must be a valid email address.")
	private String email;

	@NotNull(message = "Password must be present.")
	@NotBlank(message = "Password cannot be blank or contain only whitespace characters.")
	private String password;

	public RegisterUserDTO() {
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

}
