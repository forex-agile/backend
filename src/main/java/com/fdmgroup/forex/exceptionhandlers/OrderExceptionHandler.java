package com.fdmgroup.forex.exceptionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fdmgroup.forex.exceptions.order.NoMatchingOrderException;

/**
 * Class for handling exceptions thrown by the fund transfer service
 */
@RestControllerAdvice
public class OrderExceptionHandler {

	@ExceptionHandler(NoMatchingOrderException.class)
	public ResponseEntity<String> handleNoMatchingOrderException(NoMatchingOrderException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);
	}

}
