package com.fdmgroup.forex.exceptionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fdmgroup.forex.exceptions.InternalServerErrorException;
import com.fdmgroup.forex.exceptions.ResourceConflictException;
import com.fdmgroup.forex.response.ResourceConflictResponse;

/**
 * Class for defining responses for different thrown exceptions.
 *
 * Apart from building custom responses, this class is needed because OAuth
 * Resource Server filter chain will turn all responses to every failed request
 * into 401 unauthorized.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InternalServerErrorException.class)
	public ResponseEntity<String> handleInternalServerErrorException(InternalServerErrorException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceConflictException.class)
	public ResponseEntity<ResourceConflictResponse> handleResourceConflictException(ResourceConflictException ex) {
		ResourceConflictResponse response = new ResourceConflictResponse(HttpStatus.CONFLICT.value(),
				HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), ex.getField());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	/**
	 * Throws a 400 Bad Request with empty body if a request body doesn't
	 * pass @Valid validation
	 *
	 * Empty body because message may expose sensitive info about the backend
	 * architecture
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}

}
