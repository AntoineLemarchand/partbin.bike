package com.alemarch.partbin.controllers;

import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alemarch.partbin.entities.User;

import com.alemarch.partbin.dtos.UserDto;
import com.alemarch.partbin.dtos.ProductDto;
import com.alemarch.partbin.dtos.SortParam;
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

	// TODO restrict this to admin role
	@GetMapping
	public ResponseEntity<Iterable<UserDto>> getUsers(
			@RequestParam(required = false, name = "filter") Map<String, Object> filter,
			@RequestParam(required = false, name = "filter") SortParam sort
	) {
		return ResponseEntity.ok(userService.getUsers(filter, sort));
	}

	@PostMapping
	public ResponseEntity<UserDto> updateUser(Authentication authentication, @RequestBody UpdateUserRequest newValues) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(userService.updateUser(user, newValues));
	}

	@GetMapping("/wishlist")
	public ResponseEntity<Iterable<ProductDto>> getWishlist(Authentication authentication) {
		return ResponseEntity.ok(userService.getWishlist((User)authentication.getPrincipal()));
	}

	@PostMapping("/wishlist")
	public ResponseEntity<String> addToWishlist(Authentication authentication, @RequestBody long productId) {
		User user = (User) authentication.getPrincipal();
		boolean inserted = userService.addToWishlist(user, productId);
		if (!inserted) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().build();
	}
}
