package com.alemarch.partbin.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.alemarch.partbin.dtos.CreateProductDto;
import com.alemarch.partbin.dtos.ProductDto;
import com.alemarch.partbin.entities.Product;

@Mapper(componentModel = "spring", uses = {OwnerMapper.class})
public interface ProductMapper {
	@Mapping(source = "category.id", target = "categoryId")
	ProductDto toDto(Product product);

	@Mapping(source = "categoryId", target = "category.id")
	Product toEntity(ProductDto productDto);

	@Mapping(source = "categoryId", target = "category.id")
	Product toEntity(CreateProductDto productDto);
}
