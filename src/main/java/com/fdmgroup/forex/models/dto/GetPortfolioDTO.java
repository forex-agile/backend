package com.fdmgroup.forex.models.dto;

import java.util.List;
import java.util.UUID;

import com.fdmgroup.forex.models.Asset;
import com.fdmgroup.forex.models.Portfolio;

/**
 * GetPortfolioDTO
 */
public class GetPortfolioDTO {

	private final UUID id;

	private final UserPublicInfoDTO user;

	private final List<Asset> assets;

	public GetPortfolioDTO(Portfolio portfolio) {
		this.id = portfolio.getId();
		this.user = new UserPublicInfoDTO(portfolio.getUser());
		this.assets = portfolio.getAssets();
	}

	public UUID getId() {
		return id;
	}

	public UserPublicInfoDTO getUser() {
		return user;
	}

	public List<Asset> getAssets() {
		return assets;
	}

}
