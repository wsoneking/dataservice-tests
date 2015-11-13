package com.pearson.test.daalt.dataservice.model;

public enum CompletionActivityOriginCode {
	SYSTEM("System"), 
	STUDENT("Student");
	
	public String value;
	
	private CompletionActivityOriginCode(String value) {
		this.value = value;
	}
}
