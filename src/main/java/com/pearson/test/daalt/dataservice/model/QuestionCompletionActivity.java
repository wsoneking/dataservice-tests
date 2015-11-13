package com.pearson.test.daalt.dataservice.model;

import com.pearson.test.daalt.dataservice.helper.DateUtilities;

public class QuestionCompletionActivity {

	private User user;
	private float score;
	private long timeOnQuestion;
	private boolean isCorrect;
	private boolean isLate;
	private long lastActivityDate;
	private boolean isCountForAssessingTime;
	
	public QuestionCompletionActivity(User user, float score) {
		this.user = user;
		this.score = score;
		this.lastActivityDate = DateUtilities.getLongUTCDate();
		isCountForAssessingTime = true;
	}

	public User getPerson() {
		return user;
	}

	public String getPersonId() {
		return user.getPersonId();
	}

	public String getPersonRole() {
		return user.getPersonRole();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public long getTimeOnQuestion() {
		return isCountForAssessingTime ? timeOnQuestion : 0;
	}

	public void setTimeOnQuestion(long timeonQuestion) {
		this.timeOnQuestion = timeonQuestion;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public AttemptResponseCode getResponseCode() {
		isCorrect = false;
		AttemptResponseCode attemptResponseCode = AttemptResponseCode.INCORRECT;
		if (isCorrect == true) {
			return attemptResponseCode = AttemptResponseCode.CORRECT;
		} else {
			return attemptResponseCode;
		}
	}

	public JournalWritingPassFailCode getPassFailCode() {
		isCorrect = false;
		JournalWritingPassFailCode journalWritingPassFailCode = JournalWritingPassFailCode.FAIL;
		if (isCorrect == true) {
			return journalWritingPassFailCode = JournalWritingPassFailCode.PASS;
		} else {
			return journalWritingPassFailCode;
		}
	}

	public boolean isLate() {
		return isLate;
	}

	public void setLate(boolean isLate) {
		this.isLate = isLate;
	}
	
	public String getStringLastActivityDate() {
		return DateUtilities.getFormattedDateString(getLastActivityDate());
	}
	
	public long getLastActivityDate() {
		return lastActivityDate;
	}

	public void setCountForAssessingTime(boolean isCountForAssessingTime) {
		this.isCountForAssessingTime = isCountForAssessingTime;
	}

}
