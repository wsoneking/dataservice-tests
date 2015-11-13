package com.pearson.test.daalt.dataservice.model;

import com.pearson.test.daalt.dataservice.helper.DateUtilities;

public class LearningActivity {
	private User user;
	private long timeSpentReadingSeconds;
	private long lastActivityDate;
	private boolean countForLearningTime;

	public LearningActivity(User user, long timeSpentReadingSeconds ) {
		this.user = user;
		this.timeSpentReadingSeconds = timeSpentReadingSeconds;
		this.lastActivityDate = DateUtilities.getLongUTCDate();
		this.countForLearningTime = true;
	}

	public String getPersonId() {
		return user.getPersonId();
	}
	
	public String getPersonRole() {
		return user.getPersonRole();
	}

	public long getTimeSpent() {
		return countForLearningTime ? timeSpentReadingSeconds : 0;
	}

	public Long getLastActivityDate() {
		return lastActivityDate;
	}
	
	public String getStringLastActivityDate(){
		return DateUtilities.getFormattedDateString(getLastActivityDate());
	}

	public boolean isCountForLearningTime() {
		return countForLearningTime;
	}

	public void setCountForLearningTime(boolean countForLearningTime) {
		this.countForLearningTime = countForLearningTime;
	}


	
}
