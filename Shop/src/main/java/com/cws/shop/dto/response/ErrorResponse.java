package com.cws.shop.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
	
	private boolean success;
	private String message;
	private Map<String, String> errors;
	private LocalDateTime timestamp;
	
	public ErrorResponse(boolean success, String message, Map<String, String> errors, LocalDateTime timestamp) {
		this.success = success;
		this.message = message;
		this.errors = errors;
		this.timestamp = timestamp;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	
}
