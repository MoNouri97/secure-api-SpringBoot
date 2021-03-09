package com.example.secureapi.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JwtVerifier extends OncePerRequestFilter {
	private final JwtConfig jwtConfig;
	private final JwtSecret jwtSecret;

	public JwtVerifier(JwtConfig jwtConfig, JwtSecret jwtSecret) {
		this.jwtConfig = jwtConfig;
		this.jwtSecret = jwtSecret;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException,
			IOException
	{
		String authHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
		if (Strings.isNullOrEmpty(authHeader) || !authHeader.startsWith(jwtConfig.getTokenPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			String token = authHeader.replace(jwtConfig.getTokenPrefix(), "");
			// parse token
			Jws<Claims> claimsJws =
					Jwts.parserBuilder()
							.setSigningKey(jwtSecret.getJwtSecret())
							.build()
							.parseClaimsJws(token);

			// username
			Claims body = claimsJws.getBody();
			String username = body.getSubject();

			// authorities List must be mapped tp Set
			var authorities = (List<Map<String, String>>) body.get("authorities");
			Set<SimpleGrantedAuthority> simpleAuthorities =
					authorities	.stream()
											.map(m -> new SimpleGrantedAuthority(m.get("authority")))
											.collect(Collectors.toSet());

			Authentication authentication =
					new UsernamePasswordAuthenticationToken(username, null, simpleAuthorities);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (JwtException e) {
			throw new IllegalStateException("token cannot be trusted !!!");
		}

		filterChain.doFilter(request, response);

	}

}
