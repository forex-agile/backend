package com.fdmgroup.forex.security;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.User;
import com.fdmgroup.forex.models.dto.LoginResponseDTO;
import com.fdmgroup.forex.repos.UserRepo;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
	private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

	private final TokenService tokenService;
	private final UserRepo userRepo;

	public AuthController(TokenService tokenService, UserRepo userRepo) {
		this.tokenService = tokenService;
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

		LOG.debug("Token requested for user: '{}'", authentication.getName());
		String token = tokenService.generateToken(authentication);
		LOG.debug("Token granted for user '{}': {}", authentication.getName(), token);

		return new LoginResponseDTO(user, token);
	}

}
