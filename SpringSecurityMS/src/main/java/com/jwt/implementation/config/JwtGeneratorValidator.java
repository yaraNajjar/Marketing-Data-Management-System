package com.jwt.implementation.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jwt.implementation.service.DefaultUserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtGeneratorValidator {

	@Autowired
	DefaultUserService userService;

	private final String SECRET = "45b201c221fffeb30f9f6b2400708a63065d9e7a898f5b5822d0321814c931a2";

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Claims extractUserRole(String token) {
		return extractAllClaims(token);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(Authentication authentication, long userId) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, authentication, userId);
	}

	private String createToken(Map<String, Object> claims, Authentication authentication, long userId) {
		// String role = "ROLE_USER,ROLE_ADMIN";
		String role = authentication.getAuthorities().stream().map(r -> r.getAuthority())
				.collect(Collectors.joining(","));
		return Jwts.builder().claim("accountId", userId).claim("role", role).setSubject(authentication.getName())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(60 * 24)))
				.signWith(SignatureAlgorithm.HS256, SECRET).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public UsernamePasswordAuthenticationToken getAuthenticationToken(final String token,
			final Authentication existingAuth, final UserDetails userDetails) {

		Claims claims = extractAllClaims(token);

		final Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get("role").toString().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
	}

}
