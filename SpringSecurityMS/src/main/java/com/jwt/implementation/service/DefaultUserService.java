package com.jwt.implementation.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.jwt.implementation.model.User;
import com.jwt.implementation.model.UserDTO;

public interface DefaultUserService extends UserDetailsService {
	User save(UserDTO userRegisteredDTO);

	long loadUserIdByUsername(String string);

	void deleteUser(long id, String token);


	void updateUser(long id, UserDTO userDetails);

	public List<User> getAllUsers();

	boolean isDeleted(long userId);

	public boolean emailExists(String email);

	public List<User> getAllActiveAndNonAdminUsers();
}
