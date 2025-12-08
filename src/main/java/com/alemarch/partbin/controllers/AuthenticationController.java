package com.alemarch.partbin.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alemarch.partbin.dtos.SignupRequest;
import com.alemarch.partbin.dtos.LoginRequest;
import com.alemarch.partbin.repositories.UserRepository;
import com.alemarch.partbin.entities.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;
	private final LogoutHandler logoutHandler;

	private final AuthenticationManager authenticationManager;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
		Authentication authenticationRequest =
			UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getEmail(), loginRequest.getPassword());
		Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

		SecurityContextHolder.getContext().setAuthentication(authenticationResponse);

		HttpSession session = request.getSession();
		session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
		return new ResponseEntity<>("User signed in successfully!", HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest, HttpServletRequest request) {
		if (!userRepository.findByEmail(signupRequest.getEmail()).isEmpty()) {
			return new ResponseEntity<>("Email already used!", HttpStatus.BAD_REQUEST);
		}

		User user = new User();
		user.setUsername(signupRequest.getName());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

		userRepository.save(user);
		return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		logoutHandler.logout(request, response, authentication);

		request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
		request.getSession().invalidate();
		SecurityContextHolder.clearContext();
		return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
	}

}
