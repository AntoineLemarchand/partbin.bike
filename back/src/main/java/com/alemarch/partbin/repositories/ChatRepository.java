package com.alemarch.partbin.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alemarch.partbin.entities.Chat;
import com.alemarch.partbin.entities.Product;


public interface ChatRepository extends JpaRepository<Chat, Long> {
	@EntityGraph
	@Query("SELECT c FROM Chat c WHERE c.user.id = :userId AND c.product.id = :productId")
	Optional<Chat> findByProductAndUser(@Param("userId") long userId, @Param("productId") long productId);

	@EntityGraph
	@Query("SELECT c from Chat c WHERE c.product.id = :productId")
	Iterable<Chat> findByProduct(Product product);

	@EntityGraph
	@Query("SELECT c from Chat c WHERE c.user.id = :userId")
	Iterable<Chat> findByUser(long userId);
}
