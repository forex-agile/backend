package com.fdmgroup.forex.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fdmgroup.forex.models.User;
import com.fdmgroup.forex.models.dto.UserDetailsDTO;
import com.fdmgroup.forex.services.UserService;

import jakarta.validation.Valid;

/**
 * Controller class to handle user related requests
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<UserDetailsDTO> register(@Valid @RequestBody User user) {
		user = userService.createUser(user);
		UserDetailsDTO userDetailsDTO = new UserDetailsDTO(user);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.replaceQuery("")
				.replacePath("/api/v1/user/")
				.path("/{id}")
				.buildAndExpand(user.getId().toString())
				.toUri();
		return ResponseEntity.created(location).body(userDetailsDTO);
	}

}
