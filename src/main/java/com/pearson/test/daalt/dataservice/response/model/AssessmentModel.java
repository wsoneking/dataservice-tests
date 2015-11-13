package com.pearson.test.daalt.dataservice.response.model;

public class AssessmentModel {

	private String assessmentId;
	private String title;
	private Integer points;
	private ActivityModel[] activities;
	private Boolean update;

	private String bookCategory;
	public String getBookCategory() {
		return bookCategory;
	}

	public void setBookCategory(String bookCategory) {
		this.bookCategory = bookCategory;
	}
	
	public String getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(String assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public ActivityModel[] getActivities() {
		return activities;
	}

	public void setActivities(ActivityModel[] activities) {
		this.activities = activities;
	}

	public Boolean getUpdate() {
		return update;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}

}
