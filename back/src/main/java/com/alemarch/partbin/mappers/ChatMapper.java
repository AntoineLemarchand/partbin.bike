package com.alemarch.partbin.mappers;

import org.mapstruct.Mapper;

import com.alemarch.partbin.dtos.ChatDto;
import com.alemarch.partbin.entities.Chat;

@Mapper(componentModel = "spring", uses = {MessageMapper.class, OwnerMapper.class} )
public interface ChatMapper {
	ChatDto toDto(Chat chat);
}
