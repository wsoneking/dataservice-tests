package com.pearson.test.daalt.dataservice.model;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.pearson.test.daalt.dataservice.helper.DateUtilities;



public class UnknownFormatAttempt implements Attempt  {
	private String id;
	private String externallyGeneratedId;
	private User user;
	private List<SubAttempt> subAttempts;
	private int attemptNumber;
	private boolean finalAttempt;
	private long timeSpentAnsweringSeconds;
	private float pointsEarned;
	private long lastActivityDate;
	private boolean isCountForAssessingTime;
	private boolean isCountForPointsEarned;
	private JsonObject itemResponseObject = null;
	private AttemptResponseCode correctness = AttemptResponseCode.INCORRECT;
	
	public UnknownFormatAttempt() {
		String randomUUID = UUID.randomUUID().toString();
		id = "SQE-UnknownAttpt-" + randomUUID;
		this.lastActivityDate = DateUtilities.getLongUTCDate();
		isCountForAssessingTime = true;
		isCountForPointsEarned = true;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getExternallyGeneratedId() {
		return externallyGeneratedId;
	}

	@Override
	public void setExternallyGeneratedId(String externallyGeneratedId) {
		this.externallyGeneratedId = externallyGeneratedId;
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
	public User getPerson() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int getAttemptNumber() {
		return attemptNumber;
	}

	@Override
	public void setAttemptNumber(int attemptNumber) {
		this.attemptNumber = attemptNumber;
	}

	@Override
	public boolean isFinalAttempt() {
		return finalAttempt;
	}
	
	@Override
	public void setFinalAttempt(boolean finalAttempt) {
		this.finalAttempt = finalAttempt;
	}

	@Override
	public AttemptResponseCode getAnswerCorrectness() {
		return correctness;
	}
	
	public void setAnswerCorrectness(AttemptResponseCode value) {
		correctness = value;
	}

	@Override
	public long getTimeSpent() {
		return isCountForAssessingTime ? timeSpentAnsweringSeconds : 0;
	}
	
	@Override
	public void setTimeSpent(long timeSpentAnswering) {
		this.timeSpentAnsweringSeconds = timeSpentAnswering;
	}

	@Override
	public float getPointsEarned() {
		return isCountForPointsEarned ? pointsEarned : 0 ;
	}

	@Override
	public void setPointsEarned(float points) {
		this.pointsEarned = points;
	}

	@Override
	public List<SubAttempt> getSubAttempts() {
		return subAttempts == null ? null : new ArrayList<>(subAttempts);
	}

	@Override
	public void addSubAttempt(SubAttempt subAttempt) {
		if (subAttempts == null) {
			subAttempts = new ArrayList<>();
		}
		subAttempts.add(subAttempt);
	}

	@Override
	public long getLastActivityDate() {
		return lastActivityDate;
	}
	
	@Override
	public String getStringLastActivityDate() {
		return DateUtilities.getFormattedDateString(getLastActivityDate());
	}

	@Override
	public void setCountForAssessingTime(boolean isCountForAssessingTime) {
		this.isCountForAssessingTime = isCountForAssessingTime;
	}

	public void setCountForPointsEarned(boolean isCountForPointsEarned) {
		this.isCountForPointsEarned = isCountForPointsEarned;
	}

	@Override
	public JsonObject getItemResponseObject() {
		return itemResponseObject;
	}

	@Override
	public void setItemResponseObject(JsonObject itemResponseObject) {
		this.itemResponseObject = itemResponseObject;
	}

	@Override
	public boolean isForFillInTheBlankOrNumeric() {
		return false;
	}

	@Override
	public void setForFillInTheBlankOrNumeric(boolean isFibNumeric) {
		//not applicable - do nothing
	}
	
}
