package com.alemarch.partbin.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alemarch.partbin.dtos.ProductDto;
import com.alemarch.partbin.mappers.ProductMapper;
import com.alemarch.partbin.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	@GetMapping
	public Iterable<ProductDto> getProducts(
			@RequestParam(required = false, name = "categoryId") Byte categoryId
	) {
		var products = (categoryId != null ?
			productRepository.findByCategory(categoryId) : 
			productRepository.findAll()
		);
		System.out.println(products);
		return products.stream()
			.map(productMapper::toDto)
			.toList();
	}
}
