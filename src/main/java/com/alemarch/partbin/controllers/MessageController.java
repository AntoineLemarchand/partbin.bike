package com.alemarch.partbin.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alemarch.partbin.entities.Message;

@RestController
public class MessageController {
  @RequestMapping("/hello")
  public Message sayHello() {
    return new Message("Hello World!");
  }
}
