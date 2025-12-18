package com.alemarch.partbin.repositories;

import com.alemarch.partbin.entities.UserWishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWishlistRepository extends JpaRepository<UserWishlist, Long> {
}