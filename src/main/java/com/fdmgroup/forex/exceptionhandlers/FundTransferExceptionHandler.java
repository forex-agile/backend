package com.fdmgroup.forex.exceptionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fdmgroup.forex.exceptions.InsufficientFundsException;

/**
 * Class for handling exceptions thrown by the fund transfer service
 */
@RestControllerAdvice
public class FundTransferExceptionHandler {

	@ExceptionHandler(InsufficientFundsException.class)
	public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
	}

}
