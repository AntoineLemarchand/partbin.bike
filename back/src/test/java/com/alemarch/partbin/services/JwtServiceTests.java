package com.alemarch.partbin.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.SecureRandom;
import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.JwtException;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTests {
	@Mock
	UserDetails userDetails;

	@Mock
	UserDetails otherUserDetails;
	
	JwtService service;

	private String secretKey;
	private byte[] keyBytes;

	@BeforeEach
	void setUp() {
		SecureRandom random = new SecureRandom();
		keyBytes = new byte[32];
		random.nextBytes(keyBytes);;
		secretKey = Base64.getEncoder().encodeToString(keyBytes);
	}


	@Test
	void shouldThrowOnExpiredToken() {
		Mockito.when(userDetails.getUsername()).thenReturn("testUser");
		JwtService jwtService = new JwtService(secretKey, -10000L);
		String token = jwtService.generateToken(userDetails);

		assertThrows(JwtException.class, () -> jwtService.isTokenValid(token, userDetails));
	}

	@Test
	void shouldThrowOnInvalidToken() {
		JwtService jwtService = new JwtService(secretKey, 99999L);

		assertThrows(JwtException.class, () -> jwtService.isTokenValid("i want to work at acme", userDetails));
	}

	@Test
	void shouldThrowOnMismatchedToken() {
		Mockito.when(userDetails.getUsername()).thenReturn("testUser");
		Mockito.when(otherUserDetails.getUsername()).thenReturn("anotherTestUser");
		JwtService jwtService = new JwtService(secretKey, 99999L);
		String token = jwtService.generateToken(otherUserDetails);

		assertFalse(jwtService.isTokenValid(token, userDetails));
	}

	@Test
	void shouldPassOnValidToken() {
		Mockito.when(userDetails.getUsername()).thenReturn("testUser");
		JwtService jwtService = new JwtService(secretKey, 99999L);
		String token = jwtService.generateToken(userDetails);

		assertTrue(jwtService.isTokenValid(token, userDetails));
	}
}
