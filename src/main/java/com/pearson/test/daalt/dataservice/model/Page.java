package com.pearson.test.daalt.dataservice.model;

import java.util.List;

public interface Page extends LearningResource {
//	public Page copyMe();
	public String getId();
	public void setId(String id);
	public String getExternallyGeneratedId();
	public void setExternallyGeneratedId(String externallyGeneratedId);
	public Quiz getQuiz();
	public void setQuiz(Quiz quiz);
	public void addEmbeddedQuestion(Question question);
	public List<Question> getEmbeddedQuestions();
	public List<Question> getSeededEmbeddedQuestions();
	public EmbeddedQuestion getEmbeddedQuestionLR(Question question, float learningResourceSeqNum);
	public List<LearningActivity> getLearningActivities();
	public void addLearningActivity(LearningActivity activity);
	public boolean removeLearningActivity(LearningActivity activity);

	public boolean studentCompletedPages(User user);
	public float getAggregatedPointsPossible();
	public long getLearningTime(User student);

	public long getAssessmentTime(User student);
	public void addPage(Page page);
	public List<Page> getPages();
	public Long getLastActivityDate(User student);
}
