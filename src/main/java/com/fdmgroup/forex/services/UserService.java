package com.fdmgroup.forex.services;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fdmgroup.forex.models.User;
import com.fdmgroup.forex.repos.UserRepo;

/**
 * Service class for {@link com.fdmgroup.forex.models.User User} entity
 */
public class UserService {

	private UserRepo userRepo;
	private PasswordEncoder pwdEncoder;

	public UserService(UserRepo userRepo, PasswordEncoder pwdEncoder) {
		this.userRepo = userRepo;
		this.pwdEncoder = pwdEncoder;
	}

	public void createUser(User user) {
		user.setPassword(pwdEncoder.encode(user.getPassword()));
		userRepo.save(user);
	}

}
