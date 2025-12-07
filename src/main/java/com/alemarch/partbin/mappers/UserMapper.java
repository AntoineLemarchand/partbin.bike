package com.alemarch.partbin.mappers;

import org.mapstruct.Mapper;

import com.alemarch.partbin.dtos.UserDto;
import com.alemarch.partbin.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserDto toDto(User user);
}
