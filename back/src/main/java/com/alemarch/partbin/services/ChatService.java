package com.alemarch.partbin.services;

import java.sql.Date;

import org.springframework.stereotype.Service;

import com.alemarch.partbin.dtos.SendMessageDto;
import com.alemarch.partbin.entities.Chat;
import com.alemarch.partbin.entities.Message;
import com.alemarch.partbin.entities.Product;
import com.alemarch.partbin.entities.User;
import com.alemarch.partbin.repositories.ChatRepository;
import com.alemarch.partbin.repositories.MessageRepository;
import com.alemarch.partbin.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChatService {
	ChatRepository chatRepository;
	MessageRepository messageRepository;
	ProductRepository productRepository;

	public Message addMessage(long userId, SendMessageDto message) {
		Chat chat = chatRepository.findByProductAndUser(userId, message.getProductId()).orElse(null);
		if (chat == null) {
			return null;
		}
		Message newMessage = new Message();
		newMessage.setSentOn(new Date(System.currentTimeMillis()));
		newMessage.setContent(message.getContent());
		chat.getMessages().add(newMessage);

		newMessage = messageRepository.save(newMessage);
		chatRepository.save(chat);
		return newMessage;
	}

	public Chat upsertChat(User user, long productId) {
		Product product = productRepository.findById(productId).orElse(null);
		if (product == null) {
			return null;
		}
		return chatRepository.findByProductAndUser(user.getId(), productId)
			.orElseGet(() -> {
				Chat newChat = new Chat();
				newChat.setUser(user);
				newChat.setProduct(product);
				return chatRepository.save(newChat);
			});
	}
}
