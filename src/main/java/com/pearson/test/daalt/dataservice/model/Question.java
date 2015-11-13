package com.pearson.test.daalt.dataservice.model;

import java.util.Comparator;
import java.util.List;

import com.google.gson.JsonObject;

public interface Question extends Comparator<Question>, Comparable<Question> {
	public Question copyMe();
	public String getId();
	public void setId(String id);
	public String getExternallyGeneratedId();
	public void setExternallyGeneratedId(String externallyGeneratedId);
	public String getText();
	public void setText(String text);
	public void addAnswer(Answer answer);
	public List<Answer> getAnswers();
	public float getPointsPossible();
	public void setPointsPossible(Float pointsPossible);
	public float getSequenceNumber();
	public void setSequenceNumber(Float sequenceNumber);
	public void setAssessmentId(String assessmentId);
	public String getAssessmentId();
	public List<Attempt> getAttempts();
	public void addAttempt(Attempt attempt);
	public List<Attempt> getAttemptsForUser(User user);
	public boolean studentAttemptedQuestion(User stud);
	public boolean studentCompletedQuestion(User stud);
	public boolean studentCompletedQuestionNotBySystem(User stud);
	public boolean studentAnsweredQuestionCorrectly(User stud);
	public boolean removeAttempt(Attempt attempt);
	public boolean studentUsedFinalAttempt(User user);
	public float getPointsEarnedFinal(User stud);
	
	//includes all assessment time with no regard to question completion events
	public long getInProgressAssessmentTime(User student);
	public long getInProgressTotalAssessmentTime();
	
	//includes assessment time only from questions with completion events
	public long getCompletedAssessmentTime(User student);
	public long getCompletedTotalAssessmentTime();
	
	public float getTotalPointsEarnedFinal();
	public double getMedianAssessmentTime();

	public void addLeaveQuestion(LeaveQuestionActivity leaveQuestion); 
	public List<LeaveQuestionActivity> getLeaveQuestionForUser(User user);
	public List<LeaveQuestionActivity> getLeaveQuestions();
	public long getTimeSpentWithoutAttempt(User user);
	public long getTimeSpentWithoutAttemptTotal();
	public List<SubQuestion> getSubQuestions();
	public List<SubQuestion> directAccessSubQuestionList();
	public void addSubQuestion(SubQuestion subquestion);
	public boolean removeSubQuestion(SubQuestion subQuestion);
	
	public String getQuestionType();
	public String getQuestionPresentationFormat();
	public QuestionPresentationFormat getQuestionPresentationFormatAsEnum();
	
	public List<QuestionCompletionActivity> getCompletionActivities();
	public void addCompletionActivity(QuestionCompletionActivity activity);
	public QuestionCompletionActivity getStudentQuestionCompletionActivity(User user);
	public boolean removeCompletionActivity(QuestionCompletionActivity activity);
	public boolean removeStudentCompletionActivity(User user); 
	
	public void setIsQuestionSeeded(boolean isSeeded);
	public boolean getIsQuestionSeeded();
	public void setQuestionLastSeedDateTime(String lastSeedDateTime);
	public String getQuestionLastSeedDateTime(); 

	
	public void addAutoSaveActivity(AutoSaveActivity autoSaveActivity);
	public List<AutoSaveActivity> getAutoSaveActivityForUser(User user);
	public List<AutoSaveActivity> getAutoSaveActivities();
	
	public boolean isPractice();
	public void setPractice(boolean bool);
	
	public Float getStudentLateSubmissionPoints(User student);
	public Long getLastActivityDate(User student);
	public QuestionDefinitionObject getQuestionDefinitionObject();
	public AnswerDefinitionObject getAnswerDefinitionObject();
}
