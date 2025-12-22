package com.alemarch.partbin.controllers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alemarch.partbin.dtos.CreateProductDto;
import com.alemarch.partbin.dtos.ProductDto;
import com.alemarch.partbin.services.ProductService;
import com.alemarch.partbin.services.UserService;

import com.alemarch.partbin.dtos.SortParam;
import com.alemarch.partbin.entities.Product;
import com.alemarch.partbin.entities.User;
import com.alemarch.partbin.mappers.ProductMapper;

import lombok.AllArgsConstructor;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
	private final ProductService productService;
	private final UserService userService;
	private final ProductMapper productMapper;

	@GetMapping
	public ResponseEntity<Iterable<ProductDto>> getProducts(
			@RequestParam(required = false, name = "filter") String filters,
			@RequestParam(required = false, name = "sort") SortParam sort
	) {
		Map<String, Object> filterMap = new HashMap<>();
		if (filters != null) {
			try {
				String decoded = URLDecoder.decode(filters, StandardCharsets.UTF_8);
				filterMap = new ObjectMapper().readValue(decoded, new TypeReference<Map<String, Object>>() {});
			} catch (Exception e) {
				return ResponseEntity.badRequest().build();
			}
		}
		return ResponseEntity.ok(productService.getProducts(filterMap, sort));
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProductById(
			@PathVariable(required = true) long productId
	) {
		Product product = productService.getProductById(productId);
		if (product == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(productMapper.toDto(product));
	}

	@PostMapping
	public ResponseEntity<String> createProduct(
			Authentication authentication,
			@RequestBody(required = true) CreateProductDto product
	) {
		User owner = (User) authentication.getPrincipal();
		productService.createProduct(product, owner);
		return ResponseEntity.ok("Product added successfully");
	}

	@GetMapping("/my")
	public ResponseEntity<Iterable<ProductDto>> getMyProducts(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(productService.getUserProducts(user));
	}

	@GetMapping("/wishlist")
	public ResponseEntity<List<ProductDto>> getMyWishlist(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(userService.getWishlist(user));
	}

	@PostMapping("/wishlist/{productId}")
	public ResponseEntity<String> addToWishlist(
			Authentication authentication,
			@PathVariable Long productId
	) {
		User user = (User) authentication.getPrincipal();
		userService.addToWishlist(user, productId);
		return ResponseEntity.ok("Added to wishlist");
	}

	@DeleteMapping("/wishlist/{productId}")
	public ResponseEntity<String> removeFromWishlist(
			Authentication authentication,
			@PathVariable Long productId
	) {
		User user = (User) authentication.getPrincipal();
		userService.removeFromWishlist(user, productId);
		return ResponseEntity.ok("Removed from wishlist");
	}
}
