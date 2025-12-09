package com.alemarch.partbin.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateProductDto {
	private String name;
	private String description;
	private long categoryId;
	private long ownerId;
}
