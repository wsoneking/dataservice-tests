package com.pearson.test.daalt.dataservice.request.message;

public class MissingCriticalPropertiesException extends Exception {
	public MissingCriticalPropertiesException() {
		super("Failed to send message - missing critical properties.");
	}
	
	public MissingCriticalPropertiesException(String message) {
		super(message);
	}
	
	public MissingCriticalPropertiesException(String propertyName, Object propertyValue) {
		super("Failed to send message - missing critical property : " + propertyName);
	}
}
