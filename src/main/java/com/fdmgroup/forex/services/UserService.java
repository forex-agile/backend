package com.fdmgroup.forex.services;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fdmgroup.forex.exceptions.InternalServerErrorException;
import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.exceptions.ResourceConflictException;
import com.fdmgroup.forex.models.Portfolio;
import com.fdmgroup.forex.models.Role;
import com.fdmgroup.forex.models.User;
import com.fdmgroup.forex.repos.PortfolioRepo;
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
	private PortfolioRepo portfolioRepo;
	private PasswordEncoder pwdEncoder;

	public UserService(UserRepo userRepo, RoleRepo roleRepo, PortfolioRepo portfolioRepo, PasswordEncoder pwdEncoder) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.portfolioRepo = portfolioRepo;
		this.pwdEncoder = pwdEncoder;
	}

	public User findUserById(UUID id) {
		return userRepo.findById(id).orElseThrow(() -> new RecordNotFoundException("User not found."));
	}

	@Transactional
	public User createUser(User user) {
		if (hasConflictUsername(user.getUsername()))
			throw new ResourceConflictException("Username already exists.", "username");
		if (hasConflictEmail(user.getEmail()))
			throw new ResourceConflictException("Email already exists.", "email");

		Role role = roleRepo.findByRole("USER").orElseThrow(
				() -> new InternalServerErrorException("Could not assign role to new user"));

		user.setPassword(pwdEncoder.encode(user.getPassword()));
		user.setRole(role);

		user =  userRepo.save(user);

		Portfolio portfolio = new Portfolio(user);
		portfolioRepo.save(portfolio);

		return user;
	}

	private boolean hasConflictUsername(String username) {
		return userRepo.findByUsername(username).isPresent();
	}

	private boolean hasConflictEmail(String email) {
		return userRepo.findByEmail(email).isPresent();
	}

}
