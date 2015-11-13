package com.pearson.test.daalt.dataservice.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MultiValueNumericQuestion extends MultiValueQuestion{
	private List<SubQuestion> subQuestions;
	private boolean isPractice;
	
	public MultiValueNumericQuestion() {
		String randomUUID = UUID.randomUUID().toString();
		this.id = "SQE-" + QuestionPresentationFormat.NUMERIC.value + "-" + randomUUID;
		isSeeded = true;
		isPractice = false;
		seedDateTime = getCurrentTimeFormatted();
		subQuestions = new ArrayList<>();
	}
	
	public MultiValueNumericQuestion(String text, float pointsPossible, float sequenceNumber) {
		this();
		this.text = text;
		this.pointsPossible = pointsPossible;
		this.sequenceNumber = sequenceNumber;
		subQuestions = new ArrayList<>();
	}
	
	@Override
	public Question copyMe() {
		Question copy = new MultiValueNumericQuestion();
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
		return QuestionPresentationFormat.NUMERIC.value;
	}
	
	@Override
	public QuestionPresentationFormat getQuestionPresentationFormatAsEnum() {
		return QuestionPresentationFormat.NUMERIC;
	}
	
	@Override
	public List<SubQuestion> getSubQuestions() {
		return (subQuestions == null || subQuestions.isEmpty()) ? null : new ArrayList<>(subQuestions);
	}
	
	@Override
	public List<SubQuestion> directAccessSubQuestionList() {
		return subQuestions;
	}
	
	@Override
	public void addSubQuestion(SubQuestion subquestion) {
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
