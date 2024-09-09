package com.fdmgroup.forex.models.dto;

import com.fdmgroup.forex.models.User;

/**
 * LoginResponseDTO
 */
public class LoginResponseDTO {

	private final User user;
	private final String token;

	public LoginResponseDTO(User user, String token) {
		this.user = user;
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public String getToken() {
		return token;
	}

}
