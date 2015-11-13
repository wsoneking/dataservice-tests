package com.pearson.test.daalt.dataservice.validation;

public class InvalidTestDataException extends Exception {
	public InvalidTestDataException() {
		super("Failed to construct validations - test data invalid.");
	}
	
	public InvalidTestDataException(String message) {
		super(message);
	}
}
