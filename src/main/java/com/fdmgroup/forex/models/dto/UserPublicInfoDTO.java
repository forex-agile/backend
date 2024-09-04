package com.fdmgroup.forex.models.dto;

import java.util.UUID;

import com.fdmgroup.forex.models.User;

/**
 * Data transfer object for user public information
 */
public class UserPublicInfoDTO {

	private UUID id;
	private String username;

	public UserPublicInfoDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
	}

	public UUID getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

}
