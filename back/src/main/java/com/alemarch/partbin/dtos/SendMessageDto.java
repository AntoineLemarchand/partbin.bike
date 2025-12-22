package com.alemarch.partbin.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendMessageDto {

	@NotNull
	private Long productId;

	@NotBlank
	private String content;
}
