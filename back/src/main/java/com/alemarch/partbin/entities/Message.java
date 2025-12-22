package com.alemarch.partbin.entities;

import java.sql.Date;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "content")
	private String content;

	@Column(name = "sent_on")
	private Date sentOn;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User sender;

	@ManyToOne
	@JoinColumn(name = "chat_id")
	private Chat chat;

}
