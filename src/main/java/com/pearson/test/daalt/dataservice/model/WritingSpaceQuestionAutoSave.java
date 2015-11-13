package com.pearson.test.daalt.dataservice.model;

import com.pearson.test.daalt.dataservice.helper.DateUtilities;

public class WritingSpaceQuestionAutoSave implements AutoSaveActivity {

	private User user;
	private long timeSpentSeconds;
	private long lastActivityDate;
	private boolean isCountForAssessing;
	
	public WritingSpaceQuestionAutoSave() {
		this.lastActivityDate = DateUtilities.getLongUTCDate();
		isCountForAssessing = true;
	}
	
	@Override
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public void setTimeSpent(long timeSpentSeconds) {
		this.timeSpentSeconds = timeSpentSeconds;
		
	}
	
	@Override
	public User getPerson() {
		return user;
	}
	
	@Override
	public String getPersonId() {
		return user.getPersonId();
	}
	
	@Override
	public String getPersonRole() {
		return user.getPersonRole();
	}
	
	@Override
	public long getTimeSpent() {
		return isCountForAssessing ? timeSpentSeconds : 0;
	}

	@Override
	public String getStringLastActivityDate(){
		return DateUtilities.getFormattedDateString(getLastActivityDate());
	}

	@Override
	public long getLastActivityDate() {
		return lastActivityDate;
	}
}
