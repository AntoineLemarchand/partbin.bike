package com.alemarch.partbin.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alemarch.partbin.dtos.CategoryDto;
import com.alemarch.partbin.entities.Category;
import com.alemarch.partbin.mappers.CategoryMapper;
import com.alemarch.partbin.repositories.CategoryRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

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
			List<CategoryDto> categories = categoryRepository.findAll().stream()
				.map(categoryMapper::toDto)
				.collect(Collectors.toList());
			return ResponseEntity.ok(categories);
		}
	}

}
