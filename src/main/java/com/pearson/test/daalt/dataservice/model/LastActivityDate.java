package com.pearson.test.daalt.dataservice.model;

import com.pearson.test.daalt.dataservice.helper.DateUtilities;


public class LastActivityDate {
	private User user;
	private long lastActivityDate;
	
	public LastActivityDate(User user) {
		this.user = user;
		this.lastActivityDate = DateUtilities.getLongUTCDate();
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Long getLastActivityDate() {
		return lastActivityDate;
	}
	
	public String getPersonId() {
		return user.getPersonId();
	}
	
	public String getPersonRole() {
		return user.getPersonRole();
	}
	
	public String getStringLastActivityDate(){
		return DateUtilities.getFormattedDateString(getLastActivityDate());
	}
}
