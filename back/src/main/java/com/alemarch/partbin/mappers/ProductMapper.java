package com.alemarch.partbin.mappers;

import org.mapstruct.Mapper;

import com.alemarch.partbin.dtos.CreateProductDto;
import com.alemarch.partbin.dtos.ProductDto;
import com.alemarch.partbin.entities.Product;

@Mapper(componentModel = "spring", uses = {OwnerMapper.class, CategoryMapper.class})
public interface ProductMapper {
	ProductDto toDto(Product product);

	Product toEntity(ProductDto productDto);

	Product toEntity(CreateProductDto productDto);
}
