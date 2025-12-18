package com.alemarch.partbin.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.alemarch.partbin.dtos.ProductDto;
import com.alemarch.partbin.entities.Product;
import com.alemarch.partbin.entities.User;
import com.alemarch.partbin.entities.UserWishlist;
import com.alemarch.partbin.mappers.ProductMapper;
import com.alemarch.partbin.repositories.ProductRepository;
import com.alemarch.partbin.repositories.UserRepository;
import com.alemarch.partbin.repositories.UserWishlistRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class WishlistService {
	private final UserWishlistRepository wishlistRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	@Transactional
	public List<ProductDto> getUserWishlist(User user) {
		User managedUser = userRepository.findById(user.getId())
			.orElseThrow(() -> new RuntimeException("User not found"));

		List<UserWishlist> wishlist = wishlistRepository.findAll();
		return wishlist.stream()
			.filter(w -> w.getUser().getId().equals(managedUser.getId()))
			.map(UserWishlist::getProduct)
			.map(productMapper::toDto)
			.collect(Collectors.toList());
	}

	@Transactional
	public void addToWishlist(User user, Long productId) {
		User managedUser = userRepository.findById(user.getId())
			.orElseThrow(() -> new RuntimeException("User not found"));
		
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new RuntimeException("Product not found"));

		boolean alreadyInWishlist = wishlistRepository.findAll().stream()
			.anyMatch(w -> w.getUser().getId().equals(managedUser.getId()) && 
							w.getProduct().getId().equals(productId));

		if (!alreadyInWishlist) {
			UserWishlist wishlistItem = UserWishlist.builder()
				.user(managedUser)
				.product(product)
				.build();
			wishlistRepository.save(wishlistItem);
		}
	}

	@Transactional
	public void removeFromWishlist(User user, Long productId) {
		User managedUser = userRepository.findById(user.getId())
			.orElseThrow(() -> new RuntimeException("User not found"));

		wishlistRepository.findAll().stream()
			.filter(w -> w.getUser().getId().equals(managedUser.getId()) && 
						   w.getProduct().getId().equals(productId))
			.findFirst()
			.ifPresent(wishlistRepository::delete);
	}
}