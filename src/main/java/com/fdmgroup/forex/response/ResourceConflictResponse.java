package com.fdmgroup.forex.response;

/**
 * Response body for a 409 Conflict response
 */
public class ResourceConflictResponse extends ErrorResponse {

	/**
	 * Indicates the field that caused the conflict
	 */
	private final String field;

	public ResourceConflictResponse(int status, String error, String message, String field) {
		super(status, error, message);
		this.field = field;
	}

	public String getField() {
		return field;
	}

}
