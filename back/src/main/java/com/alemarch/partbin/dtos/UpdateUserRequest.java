package com.alemarch.partbin.dtos;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UpdateUserRequest {
	private String username;

	@Email
	private String email;

	private String password;
}
