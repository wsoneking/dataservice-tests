package com.pearson.test.daalt.dataservice.model;

import java.util.UUID;

public class MultiValueAnswer implements Answer {
	private String id;
	private String externallyGeneratedId;
	private String text;
	private String studentEnteredText;
	private boolean correctAnswer;

	public MultiValueAnswer() {
		String randomUUID = UUID.randomUUID().toString();
		id = "SQE-MultiValueAns-" + randomUUID;
	}
	
	public MultiValueAnswer(String text) {
		this();
		this.text = text;
	}
	
	@Override
	public Answer copyMe() {
		Answer copy = new MultiValueAnswer();
		copy.setCorrectAnswer(correctAnswer);
		copy.setId(id);
		copy.setExternallyGeneratedId(externallyGeneratedId);
		copy.setText(text);
		return copy;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getExternallyGeneratedId() {
		return externallyGeneratedId;
	}

	@Override
	public void setExternallyGeneratedId(String externallyGeneratedId) {
		this.externallyGeneratedId = externallyGeneratedId;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String getStudentEnteredText() {
		return studentEnteredText;
	}

	@Override
	public void setStudentEnteredText(String studentEnteredText) {
		this.studentEnteredText = studentEnteredText;
	}

	@Override
	public boolean isCorrectAnswer() {
		return correctAnswer;
	}

	@Override
	public void setCorrectAnswer(boolean correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
}
