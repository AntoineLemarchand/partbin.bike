package com.alemarch.partbin.controllers;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alemarch.partbin.dtos.SendMessageDto;
import com.alemarch.partbin.entities.Chat;
import com.alemarch.partbin.entities.Message;
import com.alemarch.partbin.entities.User;
import com.alemarch.partbin.services.ChatService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatController {

	ChatService chatService;

	@MessageMapping("/message")
	@SendTo("/ws/chat")
	public ResponseEntity<Message> sendMessage(
			Authentication authentication,
			@RequestBody SendMessageDto message
	) {
		long userId = ((User)authentication.getPrincipal()).getId();
		Message newMessage = chatService.addMessage(userId, message);
		if (newMessage == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(newMessage);
	}

	@PostMapping("/join/{productId}")
	public ResponseEntity<Chat> joinChat(
			Authentication authentication,
			@PathVariable(required = true) Long productId
	) {
		User user = ((User)authentication.getPrincipal());
		Chat chat = chatService.upsertChat(user, productId);
		return ResponseEntity.ok(chat);
	}
}
