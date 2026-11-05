package com.jwt.implementation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

	private String username;
	private String password;
	private String email;
	private List<String> roles;
	private boolean deleted;
}
