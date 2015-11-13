package com.pearson.test.daalt.dataservice.model;

public class InvalidAssignmentParametersException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidAssignmentParametersException() {
		super("Failed to construct test data request - invalid Assignment parameters.");
	}
	
	public InvalidAssignmentParametersException(String message) {
		super(message);
	}
}
