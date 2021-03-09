package com.example.secureapi.jwt;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtSecret {
	private final JwtConfig jwtConfig;

	@Autowired
	public JwtSecret(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	@Bean
	public SecretKey getJwtSecret() {
		return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
	}

}
