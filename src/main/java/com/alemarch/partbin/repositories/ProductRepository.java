package com.alemarch.partbin.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alemarch.partbin.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	@EntityGraph(attributePaths = "category")
	@Query("select p from Product p where p.category.id = :id")
	List<Product> findByCategory(Long id);
}
