package com.alemarch.partbin.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alemarch.partbin.dtos.ProductDto;
import com.alemarch.partbin.dtos.SortParam;
import com.alemarch.partbin.dtos.UpdateUserRequest;
import com.alemarch.partbin.dtos.UserDto;
import com.alemarch.partbin.entities.Product;
import com.alemarch.partbin.entities.User;
import com.alemarch.partbin.mappers.ProductMapper;
import com.alemarch.partbin.mappers.UserMapper;
import com.alemarch.partbin.repositories.ProductRepository;
import com.alemarch.partbin.repositories.UserRepository;
import com.alemarch.partbin.utils.CriteriaQueryBuilder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;

@Service
public class UserService {

	@PersistenceContext
	private EntityManager entityManager;

	private ProductRepository productRepository;
	private UserRepository userRepository;
	private ProductMapper productMapper;
	private UserMapper userMapper;

	@Transactional
	public Iterable<UserDto> getUsers(Map<String, Object> filters, SortParam sort) {
		CriteriaQuery<User> cq = CriteriaQueryBuilder.build(entityManager, User.class, filters, sort);
		List<User> users = entityManager.createQuery(cq).getResultList();
		return users.stream()
			.map(userMapper::toDto)
			.collect(Collectors.toList());
	}

	@Transactional
	public UserDto updateUser(User user, UpdateUserRequest values) {
		if (values.getEmail() != null && !values.getEmail().isEmpty()) {
			user.setEmail(values.getEmail());
		}
		if (values.getUsername() != null && !values.getUsername().isEmpty()) {
			user.setDisplayName(values.getUsername());
		}
		entityManager.merge(user);
		return userMapper.toDto(user);
	}

	@Transactional
	public List<ProductDto> getWishlist(User user) {
		User managedUser = userRepository.findById(user.getId())
			.orElseThrow(() -> new RuntimeException("User not found"));

		Set<Product> wishlist = managedUser.getFavoriteProducts();
		return wishlist.stream()
			.map(productMapper::toDto)
			.collect(Collectors.toList());
	}

	@Transactional
	public void addToWishlist(User user, Long productId) {
		User managedUser = userRepository.findById(user.getId())
			.orElseThrow(() -> new RuntimeException("User not found"));
		
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new RuntimeException("Product not found"));

		boolean alreadyInWishlist = managedUser.getFavoriteProducts().stream()
			.anyMatch(p -> p.getId().equals(productId));

		if (!alreadyInWishlist) {
			managedUser.getFavoriteProducts().add(product);
			userRepository.save(managedUser);
		}
	}

	@Transactional
	public void removeFromWishlist(User user, Long productId) {
		User managedUser = userRepository.findById(user.getId())
			.orElseThrow(() -> new RuntimeException("User not found"));

		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new RuntimeException("Product not found"));

		managedUser.getFavoriteProducts().remove(product);
		userRepository.save(managedUser);
	}
}
