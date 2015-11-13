package com.pearson.test.daalt.dataservice.model;


import java.util.List;
import java.util.UUID;

public class SimpleWritingSharedWritingQuestion extends SimpleWritingQuestion {

	private float pointsPossible;
	private boolean isPractice;

	public SimpleWritingSharedWritingQuestion() {
		String randomUUID = UUID.randomUUID().toString();
		this.id = "SQE-" + QuestionPresentationFormat.SHARED_WRITING.value + "-" + randomUUID;
		isSeeded = true;
		isPractice = false;
		seedDateTime = getCurrentTimeFormatted();
	}

	public SimpleWritingSharedWritingQuestion(String text, float pointsPossible, float sequenceNumber) {
		this();
		this.text = text;
		this.pointsPossible= pointsPossible;
		this.sequenceNumber = sequenceNumber;
	}
	
	@Override
	public Question copyMe() {
		Question copy = new SimpleWritingSharedWritingQuestion();
		copy.setAssessmentId(assessmentId);
		copy.setId(id);
		copy.setExternallyGeneratedId(externallyGeneratedId);
		copy.setText(text);
		copy.setSequenceNumber(sequenceNumber);
		copy.setIsQuestionSeeded(isSeeded);
		copy.setQuestionLastSeedDateTime(seedDateTime);
		return copy;
	}

	@Override
	public void setQuestionLastSeedDateTime(String lastSeedDateTime) {
		this.seedDateTime = lastSeedDateTime;
	}
	
	@Override
	public String getQuestionPresentationFormat() {
		return QuestionPresentationFormat.SHARED_WRITING.value;
	}
	
	@Override
	public QuestionPresentationFormat getQuestionPresentationFormatAsEnum() {
		return QuestionPresentationFormat.SHARED_WRITING;
	}

	@Override
	public List<SubQuestion> getSubQuestions() {
		List<SubQuestion> toReturn = null;
		return toReturn;
	}

	@Override
	public void addSubQuestion(SubQuestion subquestion) {
	
	}
	
	@Override
	public boolean removeSubQuestion(SubQuestion subQuestion) {
		// not applicable
		return false;
	}
	
	@Override
	public void addAnswer(Answer answer) {
		
	}
	
	@Override
	public float getPointsPossible() {
		return pointsPossible;
	}

	@Override
	public void setPointsPossible(Float pointsPossible) {
		this.pointsPossible = pointsPossible;
	}
	
	@Override
	public float getPointsEarnedFinal(User student) {
		float pointsEarned = 0;
		if (getCompletionActivities() != null) {
			for (QuestionCompletionActivity activity : getCompletionActivities()) {
				if (activity.getPersonId().compareTo(student.getPersonId()) == 0) {
					pointsEarned += activity.getScore();
				}
			}
		}
		return pointsEarned;
	}

	@Override
	public float getTotalPointsEarnedFinal() {
		float pointsEarned = 0;
		if (getCompletionActivities() != null) {
			for (QuestionCompletionActivity activity : getCompletionActivities()) {
				pointsEarned += activity.getScore();
			}
		}
		return pointsEarned;
	}
	
	@Override
	public boolean isPractice() {
		return isPractice;
	}

	@Override
	public void setPractice(boolean bool) {
		isPractice = bool;
	}
	
	@Override
	public Float getStudentLateSubmissionPoints(User student) {
		// For release 2.1 only the end point 1.11 uses non multi-value item types & 1.11 doesn't report studentLateSubmissionPoints
		return null;
	}


}




