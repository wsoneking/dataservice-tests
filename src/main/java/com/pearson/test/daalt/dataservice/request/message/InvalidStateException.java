package com.pearson.test.daalt.dataservice.request.message;

public class InvalidStateException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidStateException() {
		super("Failed to execute test action due to invalid state.");
	}
	
	public InvalidStateException(String message) {
		super(message);
	}
}
