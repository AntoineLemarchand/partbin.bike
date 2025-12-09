package com.alemarch.partbin.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.SecureRandom;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTests {
	@Mock
	UserDetails userDetails;
	
	JwtService service;

	@Test
	void shouldReturnFalseOnExpiredToken() {
		SecureRandom random = new SecureRandom();
		byte[] keyBytes = new byte[32];
		random.nextBytes(keyBytes);;
		String secretKey = Base64.getEncoder().encodeToString(keyBytes);
		Mockito.when(userDetails.getUsername()).thenReturn("testUser");

		JwtService jwtService = new JwtService(secretKey, -10000L);
		String token = jwtService.generateToken(userDetails);

		assertThrows(Exception.class, () -> jwtService.isTokenValid(token, userDetails));
	}
}
