package com.uber.eats.exception;

public class ForbiddenException extends Exception{

	 private static final long serialVersionUID = 1L;
	
	public ForbiddenException() {
		super();
	}
	
	public ForbiddenException(String message) {
		
		super(message);
		
	}
	
}
