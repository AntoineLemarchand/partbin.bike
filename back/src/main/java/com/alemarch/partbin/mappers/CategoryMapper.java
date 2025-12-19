package com.alemarch.partbin.mappers;

import org.mapstruct.Mapper;

import com.alemarch.partbin.dtos.UserDto;
import com.alemarch.partbin.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
	UserDto toDto(Category category);
}
