package com.pearson.test.daalt.dataservice.validation;

public enum ResponseCode {
	OK(200), NO_CONTENT(204), BAD_REQUEST(400), UNATHORIZED(401), NOT_FOUND(404), INTERNAL_SERVER_ERROR(500), NOT_IMPLEMENTED(501);
	
	public int value;
	
	private ResponseCode(int value) {
		this.value = value;
	}
}
