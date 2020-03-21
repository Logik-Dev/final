package project.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import project.models.entities.User;
import project.services.UserService;
import project.utils.JwtUtil;

@Configuration
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			final String authorizationHeader = request.getHeader("Authorization");
			String email = null;
			String jwt = null;

			// Si la valeur du header Authorization commence ben par Bearer extraire le
			// token et l'email qu'il contient
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				email = jwtUtil.extractEmail(jwt);
			}

			// Si on bien un email et aucune authentification dans le contexte récuperer
			// l'utilisateur et vérifier la validité du token
			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				User user = userService.findByEmail(email);
				if (jwtUtil.validateToken(jwt, user)) {
					// Tout est ok on authentifie l'utilisateur
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							user, null, user.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		} finally {
			filterChain.doFilter(request, response);
		}

	}
}
