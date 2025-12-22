package com.alemarch.partbin.dtos;

import java.util.Set;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatDto {
	@NotNull
	private long id;

	@NotNull
	private ProductDto product;

	private Set<MessageDto> messages;

	@NotNull
	private UserDto user;
}
