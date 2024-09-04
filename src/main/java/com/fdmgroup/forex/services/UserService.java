package com.fdmgroup.forex.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fdmgroup.forex.exceptions.InternalServerError;
import com.fdmgroup.forex.models.Role;
import com.fdmgroup.forex.models.User;
import com.fdmgroup.forex.repos.RoleRepo;
import com.fdmgroup.forex.repos.UserRepo;

import jakarta.transaction.Transactional;

/**
 * Provide services related to user
 */
@Service
public class UserService {

	private UserRepo userRepo;
	private RoleRepo roleRepo;
	private PasswordEncoder pwdEncoder;

	public UserService(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder pwdEncoder) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.pwdEncoder = pwdEncoder;
	}

	@Transactional
	public User createUser(User user) throws InternalServerError {
		Role role = roleRepo.findByRole("USER").orElseThrow(
				() -> new InternalServerError("Internal Server Error: could not assign role"));
		user.setPassword(pwdEncoder.encode(user.getPassword()));
		user.setRole(role);
		return userRepo.save(user);
	}

}
