package com.alemarch.partbin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alemarch.partbin.entities.User;

import com.alemarch.partbin.dtos.UserDto;
import com.alemarch.partbin.dtos.UpdateUserRequest;
import com.alemarch.partbin.services.UserService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final UserService userService;

	@PostMapping
	public ResponseEntity<UserDto> updateUser(Authentication authentication, @RequestBody UpdateUserRequest newValues) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(userService.updateUser(user, newValues));
	}
}
