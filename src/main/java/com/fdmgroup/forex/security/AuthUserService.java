package com.fdmgroup.forex.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

}
