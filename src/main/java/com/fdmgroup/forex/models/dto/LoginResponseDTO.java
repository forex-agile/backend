package com.fdmgroup.forex.models.dto;

/**
 * LoginResponseDTO
 */
public class LoginResponseDTO {

	private final UserDetailsDTO user;
	private final String token;

	public LoginResponseDTO(UserDetailsDTO userDetailsDTO, String token) {
		this.user = userDetailsDTO;
		this.token = token;
	}

	public UserDetailsDTO getUser() {
		return user;
	}

	public String getToken() {
		return token;
	}

}
