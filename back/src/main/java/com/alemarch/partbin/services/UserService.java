package com.alemarch.partbin.services;

import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.alemarch.partbin.dtos.ProductDto;
import com.alemarch.partbin.dtos.SortParam;
import com.alemarch.partbin.dtos.UserDto;
import com.alemarch.partbin.entities.Product;
import com.alemarch.partbin.entities.User;
import com.alemarch.partbin.mappers.ProductMapper;
import com.alemarch.partbin.mappers.UserMapper;
import com.alemarch.partbin.repositories.ProductRepository;
import com.alemarch.partbin.utils.CriteriaQueryBuilder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;

@NoArgsConstructor
@Service
public class UserService {

	private EntityManager entityManager;

	private ProductRepository productRepository;
	private ProductMapper productMapper;

	private UserMapper userMapper;

	public Iterable<UserDto> getUsers(Map<String, Object> filters, SortParam sort) {
		CriteriaQuery<User> cq = CriteriaQueryBuilder.build(entityManager, User.class, filters, sort);
		List<User> users = entityManager.createQuery(cq).getResultList();
		return users.stream()
			.map(userMapper::toDto)
			.collect(Collectors.toList());
	}

	public Iterable<ProductDto> getWishlist(User user) {
		return user.getFavoriteProducts().stream()
			.map(productMapper::toDto)
			.toList();
	}

	public boolean addToWishlist(User user, long productId) {
		Product product = productRepository.findById(productId).orElse(null);
		if (product == null) {
			return false;
		}
		user.addFavoriteProduct(product);
		return true;
	}
}
