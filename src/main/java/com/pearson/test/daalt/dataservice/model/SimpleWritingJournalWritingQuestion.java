package com.pearson.test.daalt.dataservice.model;

import java.util.List;
import java.util.UUID;

public class SimpleWritingJournalWritingQuestion extends SimpleWritingQuestion {
	
	private boolean isPractice;
	
	public SimpleWritingJournalWritingQuestion() {
		String randomUUID = UUID.randomUUID().toString();
		this.id = "SQE-" + QuestionPresentationFormat.JOURNAL.value + "-" + randomUUID;
		isSeeded = true;
		isPractice = false;
		seedDateTime = getCurrentTimeFormatted();
	}

	public SimpleWritingJournalWritingQuestion(String text, float sequenceNumber) {
		this();
		this.text = text;
		this.sequenceNumber = sequenceNumber;

	}
	
	@Override
	public Question copyMe() {
		Question copy = new SimpleWritingJournalWritingQuestion();
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
		return QuestionPresentationFormat.JOURNAL.value;
	}
	
	@Override
	public QuestionPresentationFormat getQuestionPresentationFormatAsEnum() {
		return QuestionPresentationFormat.JOURNAL;
	}

	@Override
	public List<SubQuestion> getSubQuestions() {
		return null;
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
		return 0;
	}

	@Override
	public void setPointsPossible(Float pointsPossible) {
		// not applicable
	}

	@Override
	public float getPointsEarnedFinal(User stud) {
		return 0;
	}

	@Override
	public float getTotalPointsEarnedFinal() {
		return 0;
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
