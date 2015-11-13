package com.pearson.test.daalt.dataservice.response.model;

public class Difference {

	private String differenceName;
	private String expectedValue;
	private String actualValue;

	public Difference(String name, String expectedValue, String actualValue) {
		setDifferenceName(name);
		setExpectedValue(expectedValue);
		setActualValue(actualValue);
	}
	
	public Difference(String name, int expectedValue, int actualValue) {
		setDifferenceName(name);
		setExpectedValue(String.valueOf(expectedValue));
		setActualValue(String.valueOf(actualValue));
	}
	
	public Difference(String name, double expectedValue, double actualValue) {
		setDifferenceName(name);
		setExpectedValue(String.valueOf(expectedValue));
		setActualValue(String.valueOf(actualValue));
	}
	
	public Difference(String name, boolean expectedValue, boolean actualValue) {
		setDifferenceName(name);
		setExpectedValue(String.valueOf(expectedValue));
		setActualValue(String.valueOf(actualValue));
	}

	public String getDifferenceName() {
		return differenceName;
	}

	public void setDifferenceName(String differenceName) {
		this.differenceName = differenceName;
	}

	public String getExpectedValue() {
		return expectedValue;
	}

	public void setExpectedValue(String expectedValue) {
		this.expectedValue = expectedValue;
	}

	public String getActualValue() {
		return actualValue;
	}

	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}

	public String toString() {
		return String.format(
				"difference in %s found, expected value %s actual value %s",
				getDifferenceName(), getExpectedValue(), getActualValue());
	}
}
