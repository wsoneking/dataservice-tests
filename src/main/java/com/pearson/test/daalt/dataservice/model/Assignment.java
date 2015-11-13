package com.pearson.test.daalt.dataservice.model;

import java.util.Date;
import java.util.List;

public interface Assignment {
//	public Assignment copyMe();
	public String getId();
	public void setId(String id);
	public String getExternallyGeneratedId();
	public void setExternallyGeneratedId(String externallyGeneratedId);
	public void addChapter(Chapter chapter);
	public boolean removeChapter(Chapter chapter);
	public List<Chapter> getChapters();
	public boolean containsChapter(String chapterId);
	public float getPointsPossible();
	public float getPracticePointsPossible();
	public Float getSequenceNumber();
	public String getTitle();
	public void setTitle(String title);
	public String getStructure();
	public List<LearningResource> getAllLearningResources();
	public LearningResource getDeepestLearningResource();
	
	// add them base on contract. these properties will appear in every Learning_Module
	public String getDueDateAsString();
	public Date getDueDate();
	public void setDueDate(Date date);
	public boolean isDueDatePassed();
	public void setDueDatePassed(boolean dueDatePassed);
	
	public boolean studentCompletedAssignment(User stud);
	public float getPointsEarnedFinal(User student);
	public float getPracticePointsEarnedFinal(User student);
	public long getAssessmentTime(User student);
	public long getLearningTime(User student);
	
	public boolean hasPractice();
	public boolean hasCredit();

	/**
	 * Finds all targets (SubQuestions) in the assignment and sets their ids to "true"
	 * 
	 * THIS IS A HACK to be used ONLY for V1 -> V2 message transform.
	 */
	public void setAllTargetIdsTrue();
	public Long getLastActivityDate(User student);
}
