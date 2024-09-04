package com.fdmgroup.forex.exceptionhandlers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fdmgroup.forex.exceptions.InternalServerErrorException;
import com.fdmgroup.forex.exceptions.ResourceConflictException;
import com.fdmgroup.forex.response.ErrorResponse;
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
	 * Throws a 400 Bad Request if a request body doesn't pass @Valid validation
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		BindingResult result = ex.getBindingResult();
		List<ObjectError> errors = result.getAllErrors();

		String errorMessage = errors.stream()
				.map(ObjectError::getDefaultMessage)
				.collect(Collectors.joining("\n"));

		ErrorResponse responseBody = new ErrorResponse(status.value(), status.getReasonPhrase(), errorMessage);

		return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
	}

}
