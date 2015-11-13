package com.pearson.test.daalt.dataservice.model;

public enum JournalWritingPassFailCode {
	PASS("Pass"), FAIL("Fail");

	public String value;

	private JournalWritingPassFailCode(String value) {
		this.value = value;
	}
}
