package com.fdmgroup.forex.security;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.fdmgroup.forex.exceptions.InternalServerErrorException;
import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.Portfolio;
import com.fdmgroup.forex.models.User;
import com.fdmgroup.forex.models.dto.LoginResponseDTO;
import com.fdmgroup.forex.models.dto.UserDetailsDTO;
import com.fdmgroup.forex.repos.PortfolioRepo;
import com.fdmgroup.forex.repos.UserRepo;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
	private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

	private final TokenService tokenService;
	private final PortfolioRepo portfolioRepo;
	private final UserRepo userRepo;

	public AuthController(TokenService tokenService, PortfolioRepo portfolioRepo, UserRepo userRepo) {
		this.tokenService = tokenService;
		this.portfolioRepo = portfolioRepo;
		this.userRepo = userRepo;
	}

	@PostMapping("/login")
	public LoginResponseDTO token(Authentication authentication) throws BadRequestException {
		if (authentication == null) {
			LOG.debug("Token requested for an anonymous user");
			throw new BadRequestException("Authorization header is missing or invalid");
		}

		User user = userRepo.findByUsername(authentication.getName()).orElseThrow(
				() -> new RecordNotFoundException("User not found"));
		Portfolio portfolio = portfolioRepo.findByUser_Id(user.getId()).orElseThrow(
				() -> new InternalServerErrorException("Portfolio for the user not found. Please contact support."));
		UserDetailsDTO userDetailsDTO = new UserDetailsDTO(user, portfolio.getId().toString());

		LOG.debug("Token requested for user: '{}'", authentication.getName());
		String token = tokenService.generateToken(authentication);
		LOG.debug("Token granted for user '{}': {}", authentication.getName(), token);

		return new LoginResponseDTO(userDetailsDTO, token);
	}

}
