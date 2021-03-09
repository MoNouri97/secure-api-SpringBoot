package com.example.secureapi.jwt;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.secureapi.models.AuthRequest;
import com.example.secureapi.models.AuthResponse;
import com.example.secureapi.models.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;

public class JwtAuthFilter extends UsernamePasswordAuthenticationFilter {

	private final JwtConfig jwtConfig;
	private final JwtSecret jwtSecret;
	private final AuthenticationManager authenticationManager;

	@Autowired
	public JwtAuthFilter(
			AuthenticationManager authenticationManager,
			JwtConfig jwtConfig,
			JwtSecret jwtSecret
	)
	{
		this.jwtConfig = jwtConfig;
		this.jwtSecret = jwtSecret;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(
			HttpServletRequest request,
			HttpServletResponse response
	) throws AuthenticationException
	{
		try {
			// map the request to UsernamePasswordAuthRequest.class
			AuthRequest reqValue =
					new ObjectMapper().readValue(request.getInputStream(), AuthRequest.class);
			// authenticate the request
			Authentication authentication =
					new UsernamePasswordAuthenticationToken(reqValue.getUsername(), reqValue.getPassword());
			return authenticationManager.authenticate(authentication);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain,
			Authentication authResult
	) throws IOException,
			ServletException
	{
		// build token
		String token =
				Jwts.builder()
						.setSubject(authResult.getName())
						.claim("authorities", authResult.getAuthorities())
						.setIssuedAt(new java.util.Date())
						.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
						.signWith(jwtSecret.getJwtSecret())
						.compact();

		String authHeader = jwtConfig.getTokenPrefix() + token;
		response.addHeader(jwtConfig.getAuthorizationHeader(), authHeader);
		AuthResponse authResponse = new AuthResponse(authHeader, authResult.getName());

		new ObjectMapper().writeValue(response.getWriter(), authResponse);
	}

	@Override
	protected void unsuccessfulAuthentication(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException failed
	) throws IOException,
			ServletException
	{
		ErrorResponse errorResponse = new ErrorResponse(failed.getMessage(), HttpStatus.UNAUTHORIZED);
		response.setStatus(errorResponse.getCode().value());
		new ObjectMapper().writeValue(response.getWriter(), errorResponse);
	}

}
