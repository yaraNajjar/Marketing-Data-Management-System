package com.jwt.implementation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.implementation.model.User;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);

	User findById(long id);

	Optional<User> findByEmail(String email);
}
