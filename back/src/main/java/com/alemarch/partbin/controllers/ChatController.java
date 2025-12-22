package com.alemarch.partbin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alemarch.partbin.dtos.ChatDto;
import com.alemarch.partbin.dtos.MessageDto;
import com.alemarch.partbin.entities.User;
import com.alemarch.partbin.services.ChatService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@AllArgsConstructor
@RequestMapping("/chats")
public class ChatController {

	ChatService chatService;

	@GetMapping
	public ResponseEntity<Iterable<ChatDto>> getUserChats(Authentication authentication) {
		Iterable<ChatDto> chats = chatService.getUserChats((User)authentication.getPrincipal());
		return ResponseEntity.ok(chats);
	}

	@PostMapping("/message/{chatId}")
	public ResponseEntity<MessageDto> sendMessage(
			Authentication authentication,
			@RequestBody(required = true) String content,
			@PathVariable(required = true) long chatId
	) {
		long userId = ((User)authentication.getPrincipal()).getId();
		MessageDto newMessage = chatService.addMessage(userId, chatId, content);
		if (newMessage == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(newMessage);
	}

	@PostMapping("/join/{productId}")
	public ResponseEntity<ChatDto> joinChat(
			Authentication authentication,
			@PathVariable(required = true) Long productId
	) {
		User user = ((User)authentication.getPrincipal());
		ChatDto chat = chatService.upsertChat(user, productId);
		return ResponseEntity.ok(chat);
	}
}
