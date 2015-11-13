package com.pearson.test.daalt.dataservice.model;

import java.util.List;

public interface Chapter extends LearningResource {
//	public Chapter copyMe();
	public String getId();
	public void setId(String id);
	public String getExternallyGeneratedId();
	public void setExternallyGeneratedId(String externallyGeneratedId);
	public void addChapterSection(ChapterSection section);
	public List<ChapterSection> getChapterSections();
	public Quiz getChapterQuiz();
	public void setChapterQuiz(Quiz chapterQuiz);
	public float getAggregatedPointsPossible();
	
	public boolean studentCompletedChapter(User stud);
	public float getPointsEarnedFinal(User student);
	public long getAssessmentTime(User student);
	public long getLearningTime(User student);
}
