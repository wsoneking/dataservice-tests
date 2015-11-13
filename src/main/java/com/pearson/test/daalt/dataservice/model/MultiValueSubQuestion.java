package com.pearson.test.daalt.dataservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MultiValueSubQuestion implements SubQuestion {

	private String id;
	private String externallyGeneratedId;
	private String text;
	public List<Answer> answers;
	private Float sequenceNumber;

	
	public MultiValueSubQuestion() {
		String randomUUID = UUID.randomUUID().toString();
		id = "SQE-SubQues-" + randomUUID;
	
	}
	
	public MultiValueSubQuestion(String text) {
		this();
		this.text = text;
	}
	
	@Override
	public SubQuestion copyMe() {
		SubQuestion copy = new MultiValueSubQuestion();
		copy.setId(id);
		copy.setExternallyGeneratedId(externallyGeneratedId);
		copy.setSequenceNumber(sequenceNumber);
		copy.setText(text);
		if (answers != null) {
			for (Answer answer : answers) {
				Answer answerCopy = answer.copyMe();
				copy.addAnswer(answerCopy);
			}
		}
		return copy;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getExternallyGeneratedId() {
		return externallyGeneratedId;
	}

	@Override
	public void setExternallyGeneratedId(String externallyGeneratedId) {
		this.externallyGeneratedId = externallyGeneratedId;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public List<Answer> getAnswers() {
		return answers;
	}

	@Override
	public void addAnswer(Answer answer) {
		if (answers == null) {
			answers = new ArrayList<>();
		}
		answers.add(answer);
	}
	
	@Override
	public SuperAnswer getCorrectAnswer() {
		SuperAnswer correctAnswer = new SuperAnswer();
		for (Answer ans : answers) {
			if (ans.isCorrectAnswer()) {
				correctAnswer.addAnswer(ans);
			}
		}
		return correctAnswer;
	}
	
	@Override
	public SuperAnswer getIncompleteCorrectAnswer() {
		SuperAnswer toReturn = getCorrectAnswer();
		if (toReturn.getAnswers().size() < 2) {
			toReturn = null;
		} else {
			toReturn.removeOneAnswer();
		}
		return toReturn;
	}
	
	@Override
	public SuperAnswer getSingleIncorrectAnswer() {
		SuperAnswer toReturn = new SuperAnswer();
		if (answers != null) {
			for(Answer ans : answers){
				if(!ans.isCorrectAnswer()){
					toReturn.addAnswer(ans);
					break;
				}
			}
		}
		return toReturn;
	}

	@Override
	public SuperAnswer getMultipleIncorrectAnswer() {
		SuperAnswer toReturn = getSingleIncorrectAnswer();
		if (toReturn.getAnswers() == null || toReturn.getAnswers().isEmpty()) {
			toReturn = null;
		} else {
			if (answers != null) {
				for(Answer ans : answers){
					if(ans.getId().compareTo(toReturn.getAnswers().get(0).getId()) != 0){
						toReturn.addAnswer(ans);
						break;
					}
				}
			}
		}
		return toReturn;
	}
	
	@Override
	public Float getSequenceNumber() {
		return sequenceNumber;
	}

	@Override
	public void setSequenceNumber(Float sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

}
