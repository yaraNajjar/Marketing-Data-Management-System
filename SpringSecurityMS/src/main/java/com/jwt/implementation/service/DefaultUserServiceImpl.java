package com.jwt.implementation.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import com.jwt.implementation.feign.FeignMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.implementation.feign.FeignAction;
import com.jwt.implementation.model.Role;
import com.jwt.implementation.model.User;
import com.jwt.implementation.model.UserDTO;
import com.jwt.implementation.repository.RoleRepository;
import com.jwt.implementation.repository.UserRepository;

@Service
public class DefaultUserServiceImpl implements DefaultUserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	 FeignAction feignAction;

	@Autowired
	 FeignMetric feignMetric;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(user.getRole()));
	}

	public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
	}

	@Override
	public User save(UserDTO userRegisteredDTO) {
		User user = new User();
		user.setEmail(userRegisteredDTO.getEmail());
		user.setUsername(userRegisteredDTO.getUsername());
		user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));

		userRegisteredDTO.getRoles().forEach(roleName -> {
			Role role = roleRepo.findByRole(roleName);
			user.setRole(role);
		});

		return userRepo.save(user);
	}

	public void updateUser(long id, UserDTO userDetails) {
		// Retrieve the user entity by ID
		User user = userRepo.findById(id);

		// Update the user's attributes
		user.setUsername(userDetails.getUsername());
		user.setEmail(userDetails.getEmail());
		user.setDeleted(userDetails.isDeleted());
		if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
		}


		Set<Role> userRoles = new HashSet<>();
		userDetails.getRoles().forEach(role -> {
			Role existingRole = roleRepo.findByRole(role);
			userRoles.add(existingRole);
		});
		user.setRoles(userRoles);


		userRepo.save(user);
	}



	public void deleteUser(long id, String token) {
		try {
			User user = userRepo.findById(id);
			Boolean resultFeignAction = feignAction.deleteUserAndActions(user.getId());
			Boolean resultFeignMetric = feignMetric.deleteUserAndMetrics(user.getId());
			if (resultFeignAction && resultFeignMetric) {
			userRepo.deleteById(user.getId());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	@Override
	public long loadUserIdByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		return user.getId();
	}

	@Override
	public boolean isDeleted(long userId) throws UsernameNotFoundException {

		User user = userRepo.findById(userId);
		return user.isDeleted();

	}

	@Override
	public boolean emailExists(String email) {
		return userRepo.findByEmail(email).isPresent();
	}

	@Override
	public List<User> getAllActiveAndNonAdminUsers() {
		return userRepo.findAll().stream()
				.filter(user -> !user.isDeleted() &&
						user.getRoles().stream().noneMatch(role -> role.getRole().equals("ROLE_ADMIN")))
				.collect(Collectors.toList());
	}
}
