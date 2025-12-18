package com.alemarch.partbin.controllers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alemarch.partbin.dtos.CreateProductDto;
import com.alemarch.partbin.dtos.ProductDto;
import com.alemarch.partbin.services.ProductService;

import com.alemarch.partbin.dtos.SortParam;
import com.alemarch.partbin.entities.Product;
import com.alemarch.partbin.entities.User;


import lombok.AllArgsConstructor;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
	private final ProductService productService;

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

	@PostMapping
	public ResponseEntity<String> createProduct(
			Authentication authentication,
			@RequestBody(required = true) CreateProductDto product
	) {
		User owner = (User) authentication.getPrincipal();
		productService.createProduct(product, owner);
		return ResponseEntity.ok("Product added successfully");
	}
}
