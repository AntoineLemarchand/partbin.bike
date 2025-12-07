package com.alemarch.partbin.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alemarch.partbin.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	@EntityGraph(attributePaths = {"tags", "addresses"})
	Optional<User> findByEmail(String email);

	@EntityGraph(attributePaths = "addresses")
	@Query("select u from User u")
	List<User> findAllWithTags();
}
