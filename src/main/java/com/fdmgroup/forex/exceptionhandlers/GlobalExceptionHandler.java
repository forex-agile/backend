package com.fdmgroup.forex.exceptionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fdmgroup.forex.exceptions.ResourceConflictException;
import com.fdmgroup.forex.response.ResourceConflictResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceConflictException.class)
	public ResponseEntity<ResourceConflictResponse> handleResourceConflictException(ResourceConflictException ex) {
		ResourceConflictResponse response = new ResourceConflictResponse(HttpStatus.CONFLICT.value(),
				HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), ex.getField());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

}
