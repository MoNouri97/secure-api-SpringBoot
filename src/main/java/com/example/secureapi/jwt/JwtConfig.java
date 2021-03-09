package com.example.secureapi.jwt;

import com.google.common.net.HttpHeaders;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {
	private String secretKey;
	private String tokenPrefix;
	private Integer tokenExpirationDays;

	public JwtConfig() {
	}

	public String getAuthorizationHeader() {
		return HttpHeaders.AUTHORIZATION;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getTokenPrefix() {
		return tokenPrefix;
	}

	public void setTokenPrefix(String tokenPrefix) {
		this.tokenPrefix = tokenPrefix;
	}

	public Integer getTokenExpirationDays() {
		return tokenExpirationDays;
	}

	public void setTokenExpirationDays(Integer tokenExpirationDays) {
		this.tokenExpirationDays = tokenExpirationDays;
	}
}
