package com.miniplm.security;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import javax.security.auth.message.AuthException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.miniplm.entity.Token;
import com.miniplm.entity.ZAccount;
import com.miniplm.repository.TokenRepository;
import com.miniplm.service.JwtService;
import com.miniplm.service.UserService;
import com.miniplm.utils.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final static String AUTH_HEADER = "authorization";
	private final static String AUTH_HEADER_TYPE = "Bearer";
	@Value("${jwt.tokenValid}")
	private boolean tokenValid;
	private final JwtUtils jwtUtils;
	private final UserService userService;
	private final TokenRepository tokenRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader(AUTH_HEADER);
		String jwt = "";
		if (request.getRequestURI().equals("/api/v1/auth") || request.getRequestURI().equals("/")) {
			// 登入時不用檢查Token
			filterChain.doFilter(request, response);
			return;
		}
		if (authHeader == null || !authHeader.startsWith(AUTH_HEADER_TYPE)) {
			filterChain.doFilter(request, response);
			return;
		}

		jwt = authHeader.substring(7);
		String userName = "";
		try {
			userName = jwtUtils.extractUsername(jwt);
		}catch(ExpiredJwtException e) {
			log.error("JWT Token Expired");
			filterChain.doFilter(request, response);
			return;
		}
		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails user = userService.loadUserByUsername(userName);
			boolean isTokenValid = true;
			if (tokenValid) {
				isTokenValid = tokenRepository.findByToken(jwt).map(t -> !t.isExpired() && !t.isRevoked())
					.orElse(false);
			}

			if (jwtUtils.isTokenValid(jwt, user) && isTokenValid) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						user, null, user.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);	//將驗證後的authentication設回Context內
			}
		}
		filterChain.doFilter(request, response);
	}

}
