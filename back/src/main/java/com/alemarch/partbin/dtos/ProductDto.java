package com.alemarch.partbin.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductDto {
	private long id;
	private String name;
	private String description;
	private BigDecimal price;
	private long categoryId;
}
