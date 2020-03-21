package project.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import project.models.entities.User;

@Service
public class JwtUtil {

	private final String SECRET_KEY = "ultrasecretagent007";

	public String generateToken(User user) {
		Map<String, Object> payload = new HashMap<>();
		payload.put("id", user.getId());
		String email = user.getEmail();
		return Jwts.builder().setClaims(payload).setSubject(email).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) 
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	public Boolean validateToken(String token, User user) {
		final String email = extractEmail(token);
		return (email.equals(user.getEmail()) && !isTokenExpired(token));
		
	}
	
	private Claims extractPayload(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	public Date extractExpiration(String token) {
		return extractPayload(token).getExpiration();
	}
	
	public String extractEmail(String token) {
		return extractPayload(token).getSubject();
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
}
