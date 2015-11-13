package com.pearson.test.daalt.dataservice.model;

import java.util.List;

public class EmbeddedQuestion extends MultiValueQuestion implements LearningResource {

	private String assessmentId;
	private String itemId;
	private String parentLearningResourceId;
	private Float learningResourceSequenceNumber;
	private QuestionPresentationFormat presentationFormat;
	private boolean isPractice;
	private List<QuestionCompletionActivity> completionActivities;
	
	public EmbeddedQuestion(){
		isPractice = false;
	}
	
	@Override
	public Question copyMe() {
		EmbeddedQuestion copy = new EmbeddedQuestion();
		copy.setAssessmentId(assessmentId);
		copy.setId(itemId);
		copy.setItemId(itemId);
		copy.setExternallyGeneratedId(externallyGeneratedId);
		copy.setIsQuestionSeeded(isSeeded);
		copy.setQuestionLastSeedDateTime(seedDateTime);
		copy.setSequenceNumber(sequenceNumber);
		copy.setText(text);
		copy.setPointsPossible(pointsPossible);
		copy.setPresentationFormat(presentationFormat.value);		
		return copy;
	}
	
	public String getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(String assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getParentLearningResourceId() {
		return parentLearningResourceId;
	}

	public void setParentLearningResourceId(String parentLearningResourceId) {
		this.parentLearningResourceId = parentLearningResourceId;
	}

	@Override
	public String getLearningResourceId() {
		return parentLearningResourceId + "+" + itemId;
	}
	
	@Override
	public void setLearningResourceId(String learningResourceId) {
		//not applicable - do nothing
	}

	@Override
	public Float getLearningResourceSequenceNumber() {
		return learningResourceSequenceNumber;
	}

	@Override
	public void setLearningResourceSequenceNumber(
			Float learningResourceSequenceNumber) {
		this.learningResourceSequenceNumber = learningResourceSequenceNumber;
	}

	@Override
	public String getLearningResourceTitle() {
		return "Embedded Item Title";
	}

	@Override
	public String getLearningResourceType() {
		return LearningResourceType.EMBEDDED_QUESTION.value;
	}

	@Override
	public String getLearningResourceSubType() {
		return LearningResourceSubType.EMBEDDED_QUESTION.value;
	}

	@Override
	public List<LearningResource> getChildResources() {
		return null;
	}
	
	@Override
	public float getAggregatedPointsPossible() {
		return getPointsPossible();
	}
	
	@Override
	public float getAggregatedPracticePointsPossible() {
		return getPracticePointsPossible();
	}
	
	@Override
	public float getPointsPossible() {
		return isPractice ? 0 : super.getPointsPossible();
	}
	
	@Override
	public float getPracticePointsPossible() {
		return isPractice ? super.getPointsPossible() : 0;
	}

	@Override
	public float getPracticePointsEarnedFinal(User student) {
		return isPractice ? super.getPointsEarnedFinal(student) : 0;
	}

	@Override
	public float getTotalPracticePointsEarnedFinal() {
		return isPractice ? super.getTotalPointsEarnedFinal() : 0;
	}

	@Override
	public boolean studentCompleted(User stud) {
		return super.studentCompletedQuestion(stud);
	}

	@Override
	public float getPointsEarnedFinal(User student) {
		return isPractice ? 0 : super.getPointsEarnedFinal(student);
	}

	@Override
	public long getAssessmentTime(User student) {
		return super.getInProgressAssessmentTime(student);
	}

	@Override
	public long getLearningTime(User student) {
		return 0;
	}

	@Override
	public long getChildAssessmentTime(User student) {
		return 0;
	}

	@Override
	public long getChildLearningTime(User student) {
		return 0;
	}

	@Override
	public long getTotalAssessmentTime() {
		return super.getInProgressTotalAssessmentTime();
	}

	@Override
	public long getTotalLearningTime() {
		return 0;
	}

	@Override
	public long getTotalChildAssessmentTime() {
		return 0;
	}

	@Override
	public long getTotalChildLearningTime() {
		return 0;
	}

	@Override
	public void addAnswer(Answer answer) {
		//not applicable - do nothing
	}

	@Override
	public List<SubQuestion> getSubQuestions() {
		//not applicable - never call this method
		return null;
	}
	
	@Override
	public List<SubQuestion> directAccessSubQuestionList() {
		return null;
	}

	@Override
	public void addSubQuestion(SubQuestion subquestion) {
		//not applicable - do nothing
	}
	
	@Override
	public boolean removeSubQuestion(SubQuestion subQuestion) {
		// not applicable
		return false;
	}
	
	@Override
	public String getQuestionPresentationFormat() {
		return presentationFormat.value; 
	}
	
	public void setPresentationFormat(String presentationFormat) {
		this.presentationFormat = QuestionPresentationFormat.getEnumFromStringValue(presentationFormat);
	}
	
	@Override
	public QuestionPresentationFormat getQuestionPresentationFormatAsEnum() {
		return presentationFormat;
	}

	@Override
	public boolean isPractice() {
		return isPractice;
	}

	@Override
	public boolean isAdjusted() {
		return false;
	}
	
	@Override
	public void setPractice(boolean bool) {
		isPractice = bool;
	}
	

	@Override
	public boolean hasPractice() {
		if (isPractice()){
			return true;
		}
		return false;
	}

	@Override
	public boolean hasCredit() {
		if (!isPractice()){
			return true;
		}
		return false;
	}

	@Override
	public Float getStudentLateSubmissionPoints(User student) {
		Float studentLateSubmissionPoints = null;
		if(completionActivities != null){
			for(QuestionCompletionActivity questionCompletionActivity : completionActivities){
				if(questionCompletionActivity.getPersonId().compareTo(student.getPersonId()) == 0 && questionCompletionActivity.isLate()){
					studentLateSubmissionPoints = super.getPointsEarnedFinal(student); 
				}
			}
		}
		return studentLateSubmissionPoints;
	}
}

