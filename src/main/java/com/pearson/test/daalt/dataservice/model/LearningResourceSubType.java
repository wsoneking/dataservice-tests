package com.pearson.test.daalt.dataservice.model;

public enum LearningResourceSubType {
	CHAPTER(null), 
	CHAPTER_SECTION(null), 
	CHAPTER_QUIZ("ChapterTest"), 
	CHAPTER_SECTION_QUIZ("Quiz"), 
	READING_PAGE(null), 
	EMBEDDED_QUESTION("EmbeddedItem");
	
	public String value;
	
	private LearningResourceSubType(String value) {
		this.value = value;
	}
}
