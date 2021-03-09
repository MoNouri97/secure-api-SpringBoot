package com.example.secureapi.models;

import java.util.Date;

public class AuthResponse {
	private final String token;
	private final String userName;
	private final Date timestamp;

	public AuthResponse(String token, String userName) {
		this.token = token;
		this.userName = userName;
		this.timestamp = new Date();
	}

	public String getToken() {
		return token;
	}

	public String getUserName() {
		return userName;
	}

	public Date getTimestamp() {
		return timestamp;
	}
}
