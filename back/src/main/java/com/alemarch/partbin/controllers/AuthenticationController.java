package com.alemarch.partbin.controllers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alemarch.partbin.dtos.SignupRequest;
import com.alemarch.partbin.dtos.UserDto;
import com.alemarch.partbin.dtos.LoginRequest;
import com.alemarch.partbin.repositories.UserRepository;
import com.alemarch.partbin.services.JwtService;
import com.alemarch.partbin.entities.User;
import com.alemarch.partbin.mappers.UserMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	private final PasswordEncoder passwordEncoder;
	private final LogoutHandler logoutHandler;

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
		Authentication authenticationRequest =
			UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getEmail(), loginRequest.getPassword());
		Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

		SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
		HttpSession session = request.getSession();
		session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

		String token = jwtService.generateToken((UserDetails)authenticationResponse.getPrincipal());
		Cookie sessionID = new Cookie("partbin_sid", token);
		sessionID.setHttpOnly(true);
		sessionID.setPath("/");
		response.addCookie(sessionID);

		return new ResponseEntity<>(Map.of("message", "User signed in successfully!"), HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest, HttpServletRequest request) {
		if (!userRepository.findByEmail(signupRequest.getEmail()).isEmpty()) {
			return new ResponseEntity<>(Map.of("message", "Email already used!"), HttpStatus.BAD_REQUEST);
		}

		User user = new User();
		user.setDisplayName(signupRequest.getUsername());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
		user.setRoles(new HashSet<>());

		userRepository.save(user);
		return new ResponseEntity<>(Map.of("message", "User registered successfully)"), HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		logoutHandler.logout(request, response, authentication);

		request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
		request.getSession().invalidate();
		SecurityContextHolder.clearContext();
	
		Cookie cookie = new Cookie("partbin_sid", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		return new ResponseEntity<>(Map.of("message", "Logged out successfully"), HttpStatus.OK);
	}

	@GetMapping("/me")
	public ResponseEntity<?> me(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(401).body("Not authenticated");
		}

		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(userMapper.toDto(user));
	}


}
