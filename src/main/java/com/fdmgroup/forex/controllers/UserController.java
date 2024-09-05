package com.fdmgroup.forex.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fdmgroup.forex.exceptions.RecordNotFoundException;
import com.fdmgroup.forex.models.User;
import com.fdmgroup.forex.models.dto.UserDetailsDTO;
import com.fdmgroup.forex.models.dto.UserPublicInfoDTO;
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
	public UserPublicInfoDTO getUserPublicInfo(@PathVariable UUID id) {
		User user = userService.findUserById(id).orElseThrow(() -> new RecordNotFoundException("User not found."));
		return new UserPublicInfoDTO(user);
	}

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
