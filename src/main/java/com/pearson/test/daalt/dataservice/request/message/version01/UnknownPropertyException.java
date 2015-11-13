package com.pearson.test.daalt.dataservice.request.message.version01;

public class UnknownPropertyException extends Exception {
	public UnknownPropertyException() {
		super("Failed to build message due to unknown property.");
	}
	
	public UnknownPropertyException(String message) {
		super(message);
	}
	
	public UnknownPropertyException(String propertyName, Object propertyValue) {
		super("Failed to build message due to unknown property : " + propertyName);
	}
}
