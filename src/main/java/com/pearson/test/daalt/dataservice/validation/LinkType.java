package com.pearson.test.daalt.dataservice.validation;

public enum LinkType {
	SELF("self"), LAST("last"), FIRST("first"), PREVIOUS("previous"), NEXT("next"), MODULES("modules");

	public String value;
	
	private LinkType(String value) {
		this.value = value;
	}
}
