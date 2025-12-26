package com.alemarch.partbin.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

	private final ImageStorageService imageStorageService;

	@Transactional
	public Iterable<ProductDto> getProducts(Map<String, Object> filters, SortParam sort) {
		CriteriaQuery<Product> cq = CriteriaQueryBuilder.build(entityManager, Product.class, filters, sort);
		List<Product> products = entityManager.createQuery(cq).getResultList();
		return products.stream()
			.map(productMapper::toDto)
			.collect(Collectors.toList());
	}

	@Transactional
	public Product getProductById(long id) {
		return productRepository.findById(id).orElse(null);
	}

	@Transactional
	public ProductDto createProduct(CreateProductDto productDto, User owner) {
		User managedOwner = userRepository.findById(owner.getId())
			.orElseThrow(() -> new RuntimeException("User not found with id: " + owner.getId()));

		Category category = categoryRepository.findById((byte) productDto.getCategoryId())
			.orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));

		Product product = Product.builder()
			.name(productDto.getName())
			.description(productDto.getDescription())
			.category(category)
			.owner(managedOwner)
			.build();

		Product newProduct = productRepository.save(product);
		managedOwner.getOwnedProducts().add(newProduct);

		userRepository.save(managedOwner);
		return productMapper.toDto(newProduct);
	}

	@Transactional
	public Iterable<ProductDto> getUserProducts(User user) {
		Map<String, Object> filters = new HashMap<>();
		filters.put("owner.id", user.getId());
		return getProducts(filters, null);
	}

	@Transactional
	public List<String> addImages(Long productId, List<MultipartFile> files, User user) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new RuntimeException("Product not found"));

		if (!product.getOwner().getId().equals(user.getId())) {
			throw new RuntimeException("Only the product owner can add images");
		}

		List<String> newImagePaths = imageStorageService.saveImages(files, productId);

		List<String> currentImages = product.getImagePaths();
		if (currentImages == null) {
			currentImages = new ArrayList<>();
		}
		currentImages.addAll(newImagePaths);
		product.setImagePaths(currentImages);

		return productRepository.save(product).getImagePaths();
	}

	@Transactional
	public void removeImage(Long productId, String imagePath, User user) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new RuntimeException("Product not found"));

		if (!product.getOwner().getId().equals(user.getId())) {
			throw new RuntimeException("Only the product owner can delete images");
		}

		List<String> images = product.getImagePaths();
		if (images != null) {
			images.remove(imagePath);
			product.setImagePaths(images);
			productRepository.save(product);
		}

		imageStorageService.deleteImage(imagePath);
	}
	
	@Transactional
	public void deleteProductAndImages(Long productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new RuntimeException("Product not found"));

		if (product.getImagePaths() != null) {
			imageStorageService.deleteProductImages(productId);
		}
		productRepository.delete(product);
	}
}
