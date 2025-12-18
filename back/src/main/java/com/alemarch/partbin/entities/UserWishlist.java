package com.alemarch.partbin.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_wishlists")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWishlist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(name = "created_at")
	private java.time.LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		createdAt = java.time.LocalDateTime.now();
	}
}