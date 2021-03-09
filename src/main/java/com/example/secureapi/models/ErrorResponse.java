package com.example.secureapi.models;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

	private String error;
	private HttpStatus code;

	public ErrorResponse(String error, HttpStatus code) {
		this.error = error;
		this.code = code;
	}

	public HttpStatus getCode() {
		return code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setCode(HttpStatus code) {
		this.code = code;
	}

}
