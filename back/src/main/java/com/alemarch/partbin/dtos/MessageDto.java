package com.alemarch.partbin.dtos;

import java.sql.Date;

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
	private Date sentOn;
	
	@NotNull
	private UserDto sender;

	@NotNull
	private long chatId;
}
