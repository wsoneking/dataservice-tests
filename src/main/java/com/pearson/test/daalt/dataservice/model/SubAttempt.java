package com.pearson.test.daalt.dataservice.model;

import java.util.List;

public interface SubAttempt {

	public String getId();
	public void setId(String id);
	public String getExternallyGeneratedId();
	public void setExternallyGeneratedId(String externallyGeneratedId);
	public AttemptResponseCode getAnswerCorrectness(); 
	public List<Answer> getAnswers();
	public void addAnswer(Answer ans);
	public SubQuestion getSubQuestion();
	public void setSubQuestion(SubQuestion subQuestion);

	
}