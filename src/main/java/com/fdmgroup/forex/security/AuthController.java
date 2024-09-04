package com.fdmgroup.forex.security;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
	private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

	private final TokenService tokenService;

	public AuthController(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@PostMapping("/login")
	public String token(Authentication authentication) throws BadRequestException {
		if (authentication == null) {
			LOG.debug("Token requested for an anonymous user");
			throw new BadRequestException("Authorization header is missing or invalid");
		}

		LOG.debug("Token requested for user: '{}'", authentication.getName());
		String token = tokenService.generateToken(authentication);
		LOG.debug("Token granted for user '{}': {}", authentication.getName(), token);
		return token;
	}

}
