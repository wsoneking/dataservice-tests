package com.pearson.test.daalt.dataservice.model;

public enum AttemptResponseCode {
	CORRECT("Correct"), INCORRECT("Incorrect");
	
	public String value;
	
	private AttemptResponseCode(String value) {
		this.value = value;
	}
}
