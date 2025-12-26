package com.alemarch.partbin.dtos;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateProductDto {
	private String name;
	private String description;
	private long categoryId;
	private Set<String> imagesUrl;
}
