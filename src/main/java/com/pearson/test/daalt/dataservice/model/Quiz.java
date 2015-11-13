package com.pearson.test.daalt.dataservice.model;

import java.util.List;

public interface Quiz extends LearningResource {
	public String getId();
	public void setId(String id);
	public String getExternallyGeneratedId();
	public void setExternallyGeneratedId(String externallyGeneratedId);
	public void setChapterQuiz(boolean isChapterQuiz);
	public void addQuestion(Question question);
	public void removeQuestion(Question question);
	public List<Question> getQuestions();
	public List<Question> getSeededQuestions();
	public List<Question> directAccessQuestionList();
	public boolean containsQuestionWithId(String questionId);
	public float getAggregatedPointsPossible();
	public boolean isChapterQuiz();
	
	public List<QuizCompletionActivity> getCompletionActivities();
	public void addCompletionActivity(QuizCompletionActivity activity);
	public boolean removeCompletionActivity(QuizCompletionActivity activity);
	public boolean studentCompletedQuiz(User stud);
	
	public List<Quiz> getNestedQuizzes();
	public void addNestedQuiz(Quiz quiz);
	
	public float getPointsEarnedFinal(User student);
	public long getAssessmentTime(User student);

	public void setIsAssessmentSeeded(boolean isSeeded);
	public boolean getIsAssessmentSeeded();
	public String getAssessmentLastSeedDateTime();
	
	public int getNumberOfMultiValueQuestions();
	public void setSeedDateTime(String dateTime);
}
