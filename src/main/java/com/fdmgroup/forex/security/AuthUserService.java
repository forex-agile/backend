package com.fdmgroup.forex.security;

import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.fdmgroup.forex.models.User;
import com.fdmgroup.forex.repos.UserRepo;

/**
 * AuthUserService
 */
@Service
public class AuthUserService implements UserDetailsService {

	private UserRepo userRepo;

	public AuthUserService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = this.userRepo.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException(username));
		return new AuthUser(user);
	}

	public User getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = ((Jwt) authentication.getPrincipal()).getClaims().get("userId").toString();
		User user = this.userRepo.findById(UUID.fromString(userId)).orElseThrow(() -> new AccessDeniedException(
				"Your authentication token is storing invalid user data, please login again."
						+ " If problem persists, contact support."));
		return user;
	}

}
