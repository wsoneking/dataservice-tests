package com.pearson.test.daalt.dataservice.model;

public enum QuestionPresentationFormat {
	RADIO_BUTTON("RadioButton"),
	HOT_SPOT("HotSpot"), 
	MULTI_SELECT("MultiSelect"),
	MULTIPLE_HOT_SPOT("MultipleHotSpot"),
	BINNING("Binning"), 
	CATEGORIZING("Categorizing"), 
	JOURNAL("Journal"),
	SHARED_WRITING("SharedWriting"),
	FILL_IN_THE_BLANK("FillInTheBlank"), 
	NUMERIC("Numeric"),
	UNKNOWN_FORMAT("UnknownFormat"),
	WRITING_SPACE("WritingSpace");

	
	public String value;
	
	private QuestionPresentationFormat(String value) {
		this.value = value;
	}
	
	public static QuestionPresentationFormat getEnumFromStringValue(String val) {
		QuestionPresentationFormat toReturn = null;
		switch(val) {
			case "RadioButton":
				toReturn = RADIO_BUTTON;
				break;
			case "HotSpot":
				toReturn = HOT_SPOT;
				break;
			case "MultiSelect":
				toReturn = MULTI_SELECT;
				break;
			case "MultipleHotSpot":
				toReturn = MULTIPLE_HOT_SPOT;
				break;
			case "Binning":
				toReturn = BINNING;
				break;
			case "Categorizing":
				toReturn = CATEGORIZING;
				break;
			case "Journal":
				toReturn = JOURNAL;
				break;
			case "SharedWriting":
				toReturn = SHARED_WRITING;
				break;
			case "UnknownFormat":
				toReturn = UNKNOWN_FORMAT;
				break;
			case "WritingSpace":
				toReturn = WRITING_SPACE;
				break;
			case "FillInTheBlank":
				toReturn = FILL_IN_THE_BLANK;
				break;
			case "Numeric":
				toReturn = NUMERIC;
				break;
		}
		return toReturn;
	}
}
