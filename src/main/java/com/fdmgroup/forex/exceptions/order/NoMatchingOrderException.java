package com.fdmgroup.forex.exceptions.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * NoMatchingOrderException
 */
@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoMatchingOrderException extends OrderException {

	public NoMatchingOrderException(String message) {
		super(message);
	}

}
