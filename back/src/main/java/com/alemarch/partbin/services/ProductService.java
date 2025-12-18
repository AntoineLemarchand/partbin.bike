package com.alemarch.partbin.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.alemarch.partbin.dtos.CreateProductDto;
import com.alemarch.partbin.dtos.ProductDto;
import com.alemarch.partbin.dtos.SortParam;
import com.alemarch.partbin.entities.Category;
import com.alemarch.partbin.entities.Product;
import com.alemarch.partbin.entities.User;
import com.alemarch.partbin.mappers.ProductMapper;
import com.alemarch.partbin.repositories.CategoryRepository;
import com.alemarch.partbin.repositories.ProductRepository;
import com.alemarch.partbin.repositories.UserRepository;
import com.alemarch.partbin.utils.CriteriaQueryBuilder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProductService {
	@PersistenceContext
	private EntityManager entityManager;
	private ProductRepository productRepository;
	private CategoryRepository categoryRepository;
	private ProductMapper productMapper;
	private UserRepository userRepository;

	@Transactional
	public Iterable<ProductDto> getProducts(Map<String, Object> filters, SortParam sort) {
		CriteriaQuery<Product> cq = CriteriaQueryBuilder.build(entityManager, Product.class, filters, sort);
		List<Product> products = entityManager.createQuery(cq).getResultList();
		return products.stream()
			.map(productMapper::toDto)
			.collect(Collectors.toList());
	}

	@Transactional
	public Product createProduct(CreateProductDto productDto, User owner) {
		User managedOwner = userRepository.findById(owner.getId())
			.orElseThrow(() -> new RuntimeException("User not found with id: " + owner.getId()));

		Category category = categoryRepository.findById((byte) productDto.getCategoryId())
			.orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));

		Product product = Product.builder()
			.name(productDto.getName())
			.description(productDto.getDescription())
			.category(category)
			.owner(managedOwner) // Use managed owner
			.build();

		return productRepository.save(product);
	}

	@Transactional
	public Iterable<ProductDto> getUserProducts(User user) {
		Map<String, Object> filters = new HashMap<>();
		filters.put("owner.id", user.getId());
		return getProducts(filters, null);
	}
}
