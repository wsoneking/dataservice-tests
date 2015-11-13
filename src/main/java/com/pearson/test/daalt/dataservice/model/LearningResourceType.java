package com.pearson.test.daalt.dataservice.model;

public enum LearningResourceType {
	BOOK("Title"),
	CHAPTER("Chapter"), 
	CHAPTER_SECTION("Module"), 
	CHAPTER_QUIZ("Assessment"), 
	CHAPTER_SECTION_QUIZ("Assessment"), 
	READING_PAGE("Section"), 
	EMBEDDED_QUESTION("Assessment");
	
	public String value;
	
	private LearningResourceType(String value) {
		this.value = value;
	}
}
