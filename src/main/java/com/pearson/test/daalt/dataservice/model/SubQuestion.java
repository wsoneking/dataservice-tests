package com.pearson.test.daalt.dataservice.model;

import java.util.List;

public interface SubQuestion {

	public SuperAnswer getCorrectAnswer();
	public List<Answer> getAnswers();
	public void addAnswer(Answer answer);
	
	public SuperAnswer getIncompleteCorrectAnswer();
	public SuperAnswer getSingleIncorrectAnswer();
	public SuperAnswer getMultipleIncorrectAnswer();

	public SubQuestion copyMe();
	public String getId();
	public void setId(String id);
	public String getExternallyGeneratedId();
	public void setExternallyGeneratedId(String externallyGeneratedId);
	public String getText();
	public void setText(String text);
	public Float getSequenceNumber();
	public void setSequenceNumber(Float sequenceNumber);

	
}
