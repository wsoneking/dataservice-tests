package com.pearson.test.daalt.dataservice.model;

public interface AutoSaveActivity {

	public void setUser(User user);
	public void setTimeSpent(long timeSpent);
	public User getPerson();
	public String getPersonId();
	public String getPersonRole();
	public long getTimeSpent();
	public long getLastActivityDate();
	public String getStringLastActivityDate();
}
