package com.alemarch.partbin.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageDto {

	@NotNull
	private long id;

	@NotNull
	@NotBlank
	private String content;

	@NotNull
	private LocalDateTime sentOn;
	
	@NotNull
	private UserDto sender;

	@NotNull
	private long chatId;
}
