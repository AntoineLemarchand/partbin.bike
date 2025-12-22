package com.alemarch.partbin.services;

import java.sql.Date;

import org.springframework.stereotype.Service;

import com.alemarch.partbin.dtos.ChatDto;
import com.alemarch.partbin.dtos.MessageDto;
import com.alemarch.partbin.entities.Chat;
import com.alemarch.partbin.entities.Message;
import com.alemarch.partbin.entities.Product;
import com.alemarch.partbin.entities.User;
import com.alemarch.partbin.mappers.ChatMapper;
import com.alemarch.partbin.mappers.MessageMapper;
import com.alemarch.partbin.repositories.ChatRepository;
import com.alemarch.partbin.repositories.MessageRepository;
import com.alemarch.partbin.repositories.ProductRepository;
import com.alemarch.partbin.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChatService {
	ChatRepository chatRepository;
	MessageRepository messageRepository;
	ProductRepository productRepository;
	UserRepository userRepository;

	ChatMapper chatMapper;
	MessageMapper messageMapper;

	public MessageDto addMessage(long userId, long chatId, String content) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
		Chat chat = chatRepository.findById(chatId).orElse(null);
		if (chat == null) {
			return null;
		}
		Message newMessage = new Message();
		newMessage.setSentOn(new Date(System.currentTimeMillis()));
		newMessage.setContent(content);
		newMessage.setSender(user);
		newMessage.setChat(chat);
		chat.getMessages().add(newMessage);

		messageRepository.save(newMessage);
		chatRepository.save(chat);
		return messageMapper.toDto(newMessage);
	}

	public ChatDto upsertChat(User user, long productId) {
		User managedUser = userRepository.findById(user.getId())
			.orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));
		Product product = productRepository.findById(productId).orElse(null);
		if (product == null) {
			return null;
		}
		Chat chat = chatRepository.findByProductAndUser(user.getId(), productId)
			.orElseGet(() -> {
				Chat newChat = new Chat();
				newChat.setUser(managedUser);
				newChat.setProduct(product);
				return chatRepository.save(newChat);
			});
		return chatMapper.toDto(chat);
	}
}
