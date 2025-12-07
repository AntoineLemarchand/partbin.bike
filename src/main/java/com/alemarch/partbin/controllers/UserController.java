package com.alemarch.partbin.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alemarch.partbin.entities.User;
import com.alemarch.partbin.repositories.UserRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class UserController {
  private final UserRepository userRepository;

  @GetMapping("/users")
  public Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }
}
