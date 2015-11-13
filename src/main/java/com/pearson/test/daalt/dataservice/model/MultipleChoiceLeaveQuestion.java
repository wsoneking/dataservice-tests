package com.pearson.test.daalt.dataservice.model;

import com.pearson.test.daalt.dataservice.helper.DateUtilities;

public class MultipleChoiceLeaveQuestion implements LeaveQuestionActivity{

	private User user;
	private long timeSpentWithoutAttemptingSeconds;
	private long lastActivityDate;
	private boolean isCountForAssessing;
	
	public MultipleChoiceLeaveQuestion() {
		 this.lastActivityDate = DateUtilities.getLongUTCDate();
		 isCountForAssessing = true;
	}
	
	@Override
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public void setTimeSpent(long timeSpentWithoutAttemptingSeconds) {
		this.timeSpentWithoutAttemptingSeconds = timeSpentWithoutAttemptingSeconds;
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
		return isCountForAssessing ? timeSpentWithoutAttemptingSeconds : 0;
	}

	@Override
	public long getLastActivityDate() {
		return lastActivityDate;
	}
	
	@Override
	public String getStringLastActivityDate(){
		return DateUtilities.getFormattedDateString(getLastActivityDate());
	}
}
