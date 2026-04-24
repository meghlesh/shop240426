package com.cws.shop.exception;

public class UnauthorizedActionException extends RuntimeException{
	public UnauthorizedActionException(String message) {
        super(message);
    }
}
