package com.alemarch.partbin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alemarch.partbin.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Long> { }
