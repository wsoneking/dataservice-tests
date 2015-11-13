package com.pearson.test.daalt.dataservice.model;

public interface LeaveQuestionActivity {
	public void setUser(User user);
	public void setTimeSpent(long timeSpentWithoutAttempting);
	public User getPerson();
	public String getPersonId();
	public String getPersonRole();
	public long getTimeSpent();
	public long getLastActivityDate() ;
	String getStringLastActivityDate();
}