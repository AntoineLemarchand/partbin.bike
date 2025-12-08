package com.alemarch.partbin.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "authorities")
	List<? extends GrantedAuthority> authorities;


	@ManyToMany
	@JoinTable(
	name = "wishlist",
	joinColumns = @JoinColumn(name = "user_id"),
	inverseJoinColumns = @JoinColumn(name = "product_id")
	)
	private final Set<Product> favoriteProducts = new HashSet<>();

	@ManyToMany
	@JoinTable(
	name = "owners",
	joinColumns = @JoinColumn(name = "user_id"),
	inverseJoinColumns = @JoinColumn(name = "product_id")
	)
	private final Set<Product> ownedProducts = new HashSet<>();

	public void addFavoriteProduct(Product product) {
		favoriteProducts.add(product);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"id = " + id + ", " +
			"name = " + username + ", " +
			"email = " + email + ")";
	}
}
