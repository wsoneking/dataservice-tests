package com.pearson.test.daalt.dataservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MultiValueMultipleHotSpotQuestion extends MultiValueQuestion {

	private List<SubQuestion> subQuestions;
	private boolean isPractice;
	
	public MultiValueMultipleHotSpotQuestion() {
		String randomUUID = UUID.randomUUID().toString();
		this.id = "SQE-" + QuestionPresentationFormat.MULTIPLE_HOT_SPOT.value + "-" + randomUUID;
		isSeeded = true;
		isPractice = false;
		seedDateTime = getCurrentTimeFormatted();
	}
	
	public MultiValueMultipleHotSpotQuestion(String text, float pointsPossible, float sequenceNumber) {
		this();
		this.text = text;
		this.pointsPossible = pointsPossible;
		this.sequenceNumber = sequenceNumber;
	}
	
	@Override
	public Question copyMe() {
		Question copy = new MultiValueMultipleHotSpotQuestion();
		copy.setAssessmentId(assessmentId);
		copy.setId(id);
		copy.setExternallyGeneratedId(externallyGeneratedId);
		copy.setText(text);
		copy.setPointsPossible(pointsPossible);
		copy.setSequenceNumber(sequenceNumber);
		copy.setIsQuestionSeeded(isSeeded);
		copy.setQuestionLastSeedDateTime(seedDateTime);
		if (subQuestions != null) {
			for (SubQuestion target : subQuestions) {
				SubQuestion copyTarget = target.copyMe();
				copy.addSubQuestion(copyTarget);
			}
		}
		return copy;
	}
	
	@Override
	public void setQuestionLastSeedDateTime(String lastSeedDateTime) {
		this.seedDateTime = lastSeedDateTime;
	}
	
	@Override
	public String getQuestionPresentationFormat() {
		return QuestionPresentationFormat.MULTIPLE_HOT_SPOT.value;
	}
	
	@Override
	public QuestionPresentationFormat getQuestionPresentationFormatAsEnum() {
		return QuestionPresentationFormat.MULTIPLE_HOT_SPOT;
	}
	
	@Override
	public List<SubQuestion> getSubQuestions() {
		return subQuestions == null ? null : new ArrayList<>(subQuestions);
	}
	
	@Override
	public List<SubQuestion> directAccessSubQuestionList() {
		return subQuestions;
	}
	
	@Override
	public void addSubQuestion(SubQuestion subquestion) {
		if (subQuestions == null) {
			subQuestions = new ArrayList<>();
		}
		subQuestions.add(subquestion);
	}
	
	@Override
	public boolean removeSubQuestion(SubQuestion subQuestion) {
		return subQuestions.remove(subQuestion);
	}
	
	@Override
	public void addAnswer(Answer answer) {
		if (subQuestions == null) {
			subQuestions = new ArrayList<>();
		}
		for(SubQuestion subQ : subQuestions) {
			subQ.addAnswer(answer);
		}
	}
	
	@Override
	public boolean isPractice() {
		return isPractice;
	}

	@Override
	public void setPractice(boolean bool) {
		isPractice = bool;
	}

}
