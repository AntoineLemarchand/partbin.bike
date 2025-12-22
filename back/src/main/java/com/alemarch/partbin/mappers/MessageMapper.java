package com.alemarch.partbin.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.alemarch.partbin.dtos.MessageDto;
import com.alemarch.partbin.entities.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {
	@Mapping(source = "chat.id", target = "chatId")
	MessageDto toDto(Message message);
}
