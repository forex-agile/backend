package com.fdmgroup.forex.exceptionhandlers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fdmgroup.forex.exceptions.InternalServerErrorException;
import com.fdmgroup.forex.exceptions.RecordNotFoundException;
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

	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<String> handleRecordNotFoundException(RecordNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ResourceConflictException.class)
	public ResponseEntity<ResourceConflictResponse> handleResourceConflictException(ResourceConflictException ex) {
		HttpStatus status = HttpStatus.CONFLICT;
		ResourceConflictResponse response = new ResourceConflictResponse(status.value(), status.getReasonPhrase(),
				ex.getMessage(), ex.getField());

		return new ResponseEntity<>(response, status);
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

		return new ResponseEntity<>(responseBody, status);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		Class<?> requiredType = ex.getRequiredType();
		if (requiredType.isEnum()) {
			String validValues = Arrays.stream(requiredType.getEnumConstants())
					.map(Object::toString)
					.collect(Collectors.joining(", "));
			String message = "Invalid value for '" + ex.getName() + "'. Valid values are: " + validValues;

			return new ResponseEntity<>(message, status);
		} else {

			return new ResponseEntity<>("Invalid value for " + ex.getName(), status);
		}
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
	}

}
