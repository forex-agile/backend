package com.fdmgroup.forex.response;

/**
 * Response body for error responses
 */
public class ErrorResponse {

	private final int status;
	private final String error;
	private final String message;

	public ErrorResponse(int status, String error, String message) {
		this.status = status;
		this.error = error;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

}
