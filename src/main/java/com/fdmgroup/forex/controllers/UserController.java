package com.fdmgroup.forex.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fdmgroup.forex.models.User;
import com.fdmgroup.forex.models.dto.RegisterUserDTO;
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

	@GetMapping("/user/{id}")
	public User getUserPublicInfo(@PathVariable UUID id) {
		return userService.findUserById(id);
	}

	@PostMapping("/register")
	public ResponseEntity<UserDetailsDTO> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
		UserDetailsDTO userDetailsDTO = userService.createUser(registerUserDTO);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.replaceQuery("")
				.replacePath("/api/v1/user/")
				.path("/{id}")
				.buildAndExpand(userDetailsDTO.getId())
				.toUri();
		return ResponseEntity.created(location).body(userDetailsDTO);
	}

}
