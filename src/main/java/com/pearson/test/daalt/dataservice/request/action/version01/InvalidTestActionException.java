package com.pearson.test.daalt.dataservice.request.action.version01;

public class InvalidTestActionException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidTestActionException() {
		super("Failed to construct test data request - invalid Test Action.");
	}
	
	public InvalidTestActionException(String message) {
		super(message);
	}
}
