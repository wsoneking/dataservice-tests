package com.pearson.test.daalt.dataservice.model;

import java.util.List;

import com.google.gson.JsonObject;

public interface Attempt {
	public String getId();
	public void setId(String id);
	public String getExternallyGeneratedId();
	public void setExternallyGeneratedId(String externallyGeneratedId);
	public User getPerson();
	public String getPersonId();
	public String getPersonRole();
	public int getAttemptNumber();
	public boolean isFinalAttempt();
	public AttemptResponseCode getAnswerCorrectness();
	public long getTimeSpent();
	public float getPointsEarned();
	public void setPointsEarned(float points);
	public String getStringLastActivityDate();
	public long getLastActivityDate();
	public void setUser(User user);
	public void setAttemptNumber(int attemptNumber);
	public void setFinalAttempt(boolean finalAttempt);
	public void setTimeSpent(long timeSpentAnsweringSeconds);
	public List<SubAttempt> getSubAttempts();
	public void addSubAttempt(SubAttempt subAttempt);
	public void setCountForAssessingTime(boolean isCountForAssessingTime);
	public JsonObject getItemResponseObject();
	public void setItemResponseObject(JsonObject itemResponseObject);
	public boolean isForFillInTheBlankOrNumeric();
	public void setForFillInTheBlankOrNumeric(boolean isFibNumeric);
}
