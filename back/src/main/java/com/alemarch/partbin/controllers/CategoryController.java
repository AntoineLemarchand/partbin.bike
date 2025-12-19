package com.alemarch.partbin.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alemarch.partbin.entities.Category;
import com.alemarch.partbin.repositories.CategoryRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryRepository categoryRepository;

	@GetMapping
	public ResponseEntity<?> getCategories(
			@RequestParam(required = false, name = "id") Byte id
	) {
		if (id != null) {
			Optional<Category> category = categoryRepository.findById(id);
			if (category.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(category.get());
		} else {
			return ResponseEntity.ok(categoryRepository.findAll());
		}
	}

}
