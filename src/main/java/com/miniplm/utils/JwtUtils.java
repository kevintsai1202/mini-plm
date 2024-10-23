package com.miniplm.utils;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;

@ConfigurationProperties(prefix = "jwt")
@Component
@Data
public class JwtUtils {
	
	private String secretKey;
	
	private int lifeTime;

//	public String generateToken(UserDetails zuser) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.SECOND, lifeTime);
//
//		Claims claims = Jwts.claims();
//		claims.setSubject("NPI System");
//		claims.setExpiration(calendar.getTime());
//		claims.put("username", zuser.getUsername());
////		claims.put("userid", zuser.getId());
////    claims.put("email", zuser.getEmail());
////    System.out.println(secretKey);
//		Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
//
//		String token = Jwts.builder().setClaims(claims).signWith(key).compact();
//
//		return token;
//	}
	
	public String createToken(String username) {
        // 有效時間（毫秒）
        long expirationMillis = Instant.now()
                .plusSeconds(lifeTime)
                .getEpochSecond()
                * 1000;

        // 設置標準內容與自定義內容
        Claims claims = Jwts.claims();
        claims.setSubject("NPI System");
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(expirationMillis));
        claims.put("username", username);
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        // 簽名後產生 token
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
    }

	public Map<String, Object> parseToken(String token) {
		if (token == null)
			throw new NullPointerException("token is null!");

		Key key = Keys.hmacShaKeyFor(this.secretKey.getBytes());

		JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();

		Claims claims = parser.parseClaimsJws(token).getBody();

//        return claims.entrySet().stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		return new HashMap<>(claims);
	}
	
	public Claims extractAllClaims(String token) {
		if (token == null)
			throw new NullPointerException("token is null!");

		Key key = Keys.hmacShaKeyFor(this.secretKey.getBytes());

		JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();

		Claims claims = parser.parseClaimsJws(token).getBody();

//        return claims.entrySet().stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		return claims;
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = (String) parseToken(token).get("username");
		if (isExpired(token)) return false;
		return (username.equals(userDetails.getUsername()));
	}

	public boolean isExpired(String token) {
		try {
			Key key = Keys.hmacShaKeyFor(this.secretKey.getBytes());
			JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
			Claims claims = parser.parseClaimsJws(token).getBody();
		}catch(ExpiredJwtException e) {
			System.err.println("isExpired");
			return true;
		}
		return false;
	}
	
	  public String extractUsername(String token) {
		  Claims claims = extractAllClaims(token);
		  return (String) claims.get("username");
	  }
}
