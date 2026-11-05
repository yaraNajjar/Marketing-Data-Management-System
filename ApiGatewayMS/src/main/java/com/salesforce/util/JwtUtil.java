package com.salesforce.util;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

	@Value("${token.signing.key}")
	private String jwtSigningKey;// = "45b201c221fffeb30f9f6b2400708a63065d9e7a898f5b5822d0321814c931a2";

	public void validateToken(final String token) {
		Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

//	public String extractUserName(String token) {
//		return extractClaim(token, Claims::getSubject);
//	}
//
//	@Override
//	public String generateToken(UserDetails userDetails) {
//		return generateToken(new HashMap<>(), userDetails);
//	}

//	@Override
//	public boolean isTokenValid(String token, UserDetails userDetails) {
//		final String userName = extractUserName(token);
//		return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
//	}
//
//	private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
//		final Claims claims = extractAllClaims(token);
//		return claimsResolvers.apply(claims);
//	}

//	private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
//		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
//				.setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
//				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
//	}

//	private boolean isTokenExpired(String token) {
//		return extractExpiration(token).before(new Date());
//	}
//
//	private Date extractExpiration(String token) {
//		return extractClaim(token, Claims::getExpiration);
//	}
//
//	private Claims extractAllClaims(String token) {
//		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
//	}
//
//	private Key getSignKey() {
//		byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
//		return Keys.hmacShaKeyFor(keyBytes);
//	}
}
