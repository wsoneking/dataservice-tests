package com.pearson.test.daalt.dataservice.request.message.version01;

public class MessageSendFailureException extends Exception {
	public MessageSendFailureException() {
		super("Failed to send message.");
	}
	
	public MessageSendFailureException(String message) {
		super(message);
	}
}