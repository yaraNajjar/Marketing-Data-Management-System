package com.jwt.implementation.controller;

import java.util.List;

//import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.implementation.config.JwtGeneratorValidator;
import com.jwt.implementation.model.User;
import com.jwt.implementation.model.UserDTO;
import com.jwt.implementation.repository.UserRepository;
import com.jwt.implementation.service.DefaultUserService;

@RestController
public class RestAppController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	JwtGeneratorValidator jwtGenVal;

	@Autowired
	BCryptPasswordEncoder bcCryptPasswordEncoder;

	@Autowired
	DefaultUserService userService;

	@PostMapping("/registration")
	public ResponseEntity<String> registerUser(@RequestBody UserDTO userDto) {
		if (userService.emailExists(userDto.getEmail())) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Email already exists: " + userDto.getEmail());
		}
		User users = userService.save(userDto);
		if (users == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Not able to save user.");
		else
			return ResponseEntity.ok("User saved successfully: " + users.getId());
	}

	@PostMapping("/genToken")
	public String generateJwtToken(@RequestBody UserDTO userDto) throws Exception {

		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		long userId = userService.loadUserIdByUsername((userDto.getUsername()));
		if (userService.isDeleted(userId)) {
			return "This user has been deleted.";
		}
		return jwtGenVal.generateToken(authentication, userId);
	}

	@PutMapping("/user/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public boolean updateUserById(@PathVariable long id, @RequestBody UserDTO user) {
		userService.updateUser(id, user);
		return true;
	}

	@DeleteMapping("/user/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public boolean deleteUserById(@PathVariable long id, @RequestHeader("Authorization") String authorizationHeader) {

		String token = extractToken(authorizationHeader);
		userService.deleteUser(id, token);
		return true;
	}

	private String extractToken(String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring(7); // Remove "Bearer " prefix
		}
		// Handle invalid or missing Authorization header
		throw new IllegalArgumentException("Invalid or missing Authorization header");
	}

	@GetMapping("/users")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public List<User> getAllUsers() {
		return userService.getAllActiveAndNonAdminUsers();
	}

	@GetMapping("/ROLE_ADMIN")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public boolean ROLE_ADMIN() {

		return true;

	}

	@GetMapping("/ROLE_USER")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public boolean ROLE_USER() {

		return true;
	}

	@GetMapping("/ROLE_CREATE_ACTION")
	@PreAuthorize("hasAuthority('ROLE_CREATE_ACTION')")
	public boolean ROLE_CREATE_ACTION() {
		return true;
	}

	@GetMapping("/ROLE_UPDATE_ACTION")
	@PreAuthorize("hasAuthority('ROLE_UPDATE_ACTION')")
	public boolean ROLE_UPDATE_ACTION() {
		return true;
	}

	@GetMapping("/ROLE_DELETE_ACTION")
	@PreAuthorize("hasAuthority('ROLE_DELETE_ACTION')")
	public boolean ROLE_DELETE_ACTION() {
		return true;
	}

	@GetMapping("/ROLE_CREATE_METRIC")
	@PreAuthorize("hasAuthority('ROLE_CREATE_METRIC')")
	public boolean ROLE_CREATE_METRIC() {
		return true;
	}

	@GetMapping("/ROLE_UPDATE_METRIC")
	@PreAuthorize("hasAuthority('ROLE_UPDATE_METRIC')")
	public boolean ROLE_UPDATE_METRIC() {
		return true;
	}

	@GetMapping("/ROLE_DELETE_METRIC")
	@PreAuthorize("hasAuthority('ROLE_DELETE_METRIC')")
	public boolean ROLE_DELETE_METRIC() {
		return true;
	}

	@GetMapping("/ROLE_TRIGGER")
	@PreAuthorize("hasAuthority('ROLE_TRIGGER')")
	public boolean ROLE_TRIGGER() {
		return true;
	}

}
