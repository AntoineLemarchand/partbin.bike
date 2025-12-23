package com.alemarch.partbin.dtos;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ProductDto {
	private long id;
	private String name;
	private String description;
	private CategoryDto category;
	private OwnerDto owner;
	private Set<String> imagesUrl;
}
