package com.pearson.test.daalt.dataservice.model;

public interface Answer {
	public Answer copyMe();
	public String getId();
	public void setId(String id);
	public String getExternallyGeneratedId();
	public void setExternallyGeneratedId(String externallyGeneratedId);
	public String getText();
	public void setText(String text);
	public String getStudentEnteredText();
	public void setStudentEnteredText(String StudentEnteredText);
	public boolean isCorrectAnswer();
	public void setCorrectAnswer(boolean correctAnswer);
}
