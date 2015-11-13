package com.pearson.test.daalt.dataservice.model;

import java.util.List;

public interface LearningResource {
	public String getLearningResourceId();
	public void setLearningResourceId(String learningResourceId);
	public Float getLearningResourceSequenceNumber();
	public void setLearningResourceSequenceNumber(Float learningResourceSequenceNumber);
	
	public String getLearningResourceTitle();
	public String getLearningResourceType();
	public String getLearningResourceSubType();
	public List<LearningResource> getChildResources();
	public float getAggregatedPointsPossible();
	public float getAggregatedPracticePointsPossible();
	public float getPointsPossible();
	public float getPracticePointsPossible();
	
	public boolean studentCompleted(User stud);
	
	public float getPointsEarnedFinal(User student);
	public float getPracticePointsEarnedFinal(User student);
	public float getTotalPointsEarnedFinal();
	public float getTotalPracticePointsEarnedFinal();
	public Float getStudentLateSubmissionPoints(User student);
	public Long getLastActivityDate(User student);
	
	public long getLearningTime(User student);
	public long getChildLearningTime(User student);
	public long getTotalLearningTime();
	public long getTotalChildLearningTime();
	
	public long getAssessmentTime(User student);
	public long getChildAssessmentTime(User student);
	public long getTotalAssessmentTime();
	public long getTotalChildAssessmentTime();
	
	public boolean isAdjusted();
	
	public boolean isPractice();
	public void setPractice(boolean bool);
	
	
	public boolean hasPractice();
	public boolean hasCredit();
}
