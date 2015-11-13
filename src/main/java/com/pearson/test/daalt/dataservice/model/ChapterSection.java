package com.pearson.test.daalt.dataservice.model;

import java.util.List;

public interface ChapterSection extends LearningResource {
//	public ChapterSection copyMe();
	public String getId();
	public void setId(String id);
	public String getExternallyGeneratedId();
	public void setExternallyGeneratedId(String externallyGeneratedId);
	public void addPage(Page page);
	public List<Page> getPages();
	public Quiz getChapterSectionQuiz();
	public List<Question> getSeededQuestions();

	public void setChapterSectionQuiz(Quiz chapterSectionQuiz);
	public float getAggregatedPointsPossible();
	
	public boolean studentCompletedChapterSection(User stud);
	public float getPointsEarnedFinal(User student);
	public long getAssessmentTime(User student);
	public long getLearningTime(User student);
}
