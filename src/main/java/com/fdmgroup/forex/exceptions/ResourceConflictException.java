package com.fdmgroup.forex.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceConflictException
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceConflictException extends RuntimeException {

	private final String field;

	public ResourceConflictException(String message, String conflictField) {
		super(message);
		this.field = conflictField;
	}

	public String getField() {
		return field;
	}

}
