package com.alemarch.partbin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alemarch.partbin.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Byte> {
}