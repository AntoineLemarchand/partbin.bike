package com.alemarch.partbin.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.alemarch.partbin.dtos.OwnerDto;
import com.alemarch.partbin.entities.User;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
	@Mapping(source = "displayName", target = "name")
	OwnerDto toDto(User owner);
}

