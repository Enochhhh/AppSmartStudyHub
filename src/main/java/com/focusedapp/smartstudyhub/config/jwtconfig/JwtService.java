package com.focusedapp.smartstudyhub.config.jwtconfig;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	// This Signing Key is used to create the signature of the token and is used to verify that the token sent by user is valid
	private static final String SECRET_KEY = "bXVzaWNwb2xpY2VtYW5nZXRyZXN1bHRmaXJtb3JhbmdlcGxhbm5pbmdwaXBlc29ydG0=";
	
	public String extractUserName(String token) {
		return Stream.of(extractClaim(token, Claims::getSubject).split("-"))
				.map(String::trim)
				.collect(Collectors.toList()).get(1);
		
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String generateToken(JwtUser user) {
		return generateToken(new HashMap<>(), user);
	}
	
	public String generateToken(Map<String, Object> extraClaims, JwtUser user) {
		StringBuilder subject = new StringBuilder();
		subject.append(user.getUser().getId().toString().concat("-"));
		subject.append(user.getUser().getUserName().concat("-"));
		subject.append(user.getUser().getEmail().concat("-"));
		subject.append(user.getUser().getLastName().concat(" "));
		subject.append(user.getUser().getFirstName());
		
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(subject.toString())
				.claim("Authority", user.getAuthorities())
				.claim("Role", user.getUser().getRole())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(8)))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public Boolean isTokenValid(String token, UserDetails userDetails) {		
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	
	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
}
