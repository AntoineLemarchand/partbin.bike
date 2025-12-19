package com.alemarch.partbin.mappers;

import org.mapstruct.Mapper;

import com.alemarch.partbin.dtos.CategoryDto;
import com.alemarch.partbin.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
	CategoryDto toDto(Category category);
}
