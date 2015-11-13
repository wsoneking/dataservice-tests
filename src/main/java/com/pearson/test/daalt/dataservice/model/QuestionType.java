package com.pearson.test.daalt.dataservice.model;

public enum QuestionType {
	MULTI_VALUE("MultiValue"),
	SIMPLE_WRITING("SimpleWriting"), 
	WRITING_SPACE("WritingSpace"),
	NUMERIC("Numeric"),
	PROGRAMMING_EXERCISE("ProgrammingExercise"),
	UNKNOWN_FORMAT("UnknownFormat"),
	OTHER("Other");
	
	public String value;
	
	private QuestionType(String value) {
		this.value = value;
	}
}
