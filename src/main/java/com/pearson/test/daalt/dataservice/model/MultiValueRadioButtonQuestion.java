package com.pearson.test.daalt.dataservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MultiValueRadioButtonQuestion extends MultiValueQuestion {

	protected SubQuestion subQuestion;
	private boolean isPractice;
	
	public MultiValueRadioButtonQuestion() {
		String randomUUID = UUID.randomUUID().toString();
		this.id = "SQE-" + QuestionPresentationFormat.RADIO_BUTTON.value + "-" + randomUUID;
		isSeeded = true;
		isPractice = false;
		seedDateTime = getCurrentTimeFormatted();
	}
	
	public MultiValueRadioButtonQuestion(String text, float pointsPossible, float sequenceNumber) {
		this();
		this.text = text;
		this.pointsPossible = pointsPossible;
		this.sequenceNumber = sequenceNumber;
	}
	
	@Override
	public Question copyMe() {
		Question copy = new MultiValueRadioButtonQuestion();
		copy.setAssessmentId(assessmentId);
		copy.setId(id);
		copy.setExternallyGeneratedId(externallyGeneratedId);
		copy.setText(text);
		copy.setPointsPossible(pointsPossible);
		copy.setSequenceNumber(sequenceNumber);
		copy.setIsQuestionSeeded(isSeeded);
		copy.setQuestionLastSeedDateTime(seedDateTime);
		if (subQuestion != null) {
			SubQuestion copyTarget = subQuestion.copyMe();
			copy.addSubQuestion(copyTarget);
		}
		return copy;
	}
	
	@Override
	public void setQuestionLastSeedDateTime(String lastSeedDateTime) {
		this.seedDateTime = lastSeedDateTime;
	}
	
	@Override
	public String getQuestionPresentationFormat() {
		return QuestionPresentationFormat.RADIO_BUTTON.value;
	}
	
	@Override
	public QuestionPresentationFormat getQuestionPresentationFormatAsEnum() {
		return QuestionPresentationFormat.RADIO_BUTTON;
	}
	
	@Override
	public List<SubQuestion> getSubQuestions() {
		List<SubQuestion> toReturn = null;
		if (subQuestion != null) {
			toReturn = new ArrayList<>();
			toReturn.add(subQuestion);
		}
		return toReturn;
	}
	
	@Override
	public List<SubQuestion> directAccessSubQuestionList() {
		return getSubQuestions();
	}
	
	@Override
	public void addSubQuestion(SubQuestion subquestion) {
		this.subQuestion = subquestion;
	}
	
	@Override
	public boolean removeSubQuestion(SubQuestion subQuestion) {
		boolean result = false;
		if(subQuestion != null){
			subQuestion = null;
			result = true;
		}
		return result;
	}
	
	@Override
	public void addAnswer(Answer answer) {
		if(subQuestion != null) {
			subQuestion.addAnswer(answer);
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
