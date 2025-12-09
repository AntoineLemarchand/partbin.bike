package com.alemarch.partbin.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alemarch.partbin.dtos.ProductDto;
import com.alemarch.partbin.dtos.SortParam;
import com.alemarch.partbin.entities.Product;
import com.alemarch.partbin.mappers.ProductMapper;
import com.alemarch.partbin.utils.CriteriaQueryBuilder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class ProductService {
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private ProductMapper productMapper;

	public Iterable<ProductDto> getProducts(Map<String, Object> filters, SortParam sort) {
		CriteriaQuery<Product> cq = CriteriaQueryBuilder.build(entityManager, Product.class, filters, sort);
		List<Product> products = entityManager.createQuery(cq).getResultList();
		return products.stream()
			.map(productMapper::toDto)
			.collect(Collectors.toList());
	}

}
