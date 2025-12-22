package com.alemarch.partbin.entities;

import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "chats",
uniqueConstraints = { @UniqueConstraint( name = "unique_user_product_chat", columnNames = {"user_id", "product_id"})
})
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "product_id")
	private Product product;

	@OneToMany(mappedBy = "id", orphanRemoval = true)
	private Set<Message> messages;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id")
	private User user;
}
