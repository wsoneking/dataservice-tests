package com.pearson.test.daalt.dataservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MultiValueSubAttempt implements SubAttempt {
	
	private String id;
	private String externallyGeneratedId;
	private List<Answer> answers;
	private SubQuestion subQuestion;
	
	public MultiValueSubAttempt() {
		String randomUUID = UUID.randomUUID().toString();
		id = "SQE-MultiSubAttpt-" + randomUUID;
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
	public SubQuestion getSubQuestion() {
		return subQuestion;
	}

	@Override
	public void setSubQuestion(SubQuestion subQuestion) {
		this.subQuestion = subQuestion;
	}

	@Override
	public AttemptResponseCode getAnswerCorrectness() {
		SuperAnswer superanswer = subQuestion.getCorrectAnswer();
		
		AttemptResponseCode correctness = answers.size() == superanswer.getAnswers().size() ? AttemptResponseCode.CORRECT : AttemptResponseCode.INCORRECT;
		if(correctness == AttemptResponseCode.CORRECT){
			for (Answer superans : superanswer.getAnswers()) {
				boolean foundMatchingAnswer = false;
				for(Answer ans:answers){
					if(ans.getId().compareToIgnoreCase(superans.getId()) == 0) {
						foundMatchingAnswer = true;
						break;
					}
				}
				
				correctness = foundMatchingAnswer == false ? AttemptResponseCode.INCORRECT : AttemptResponseCode.CORRECT;
				if(correctness==AttemptResponseCode.INCORRECT){
					break;
				}
				
			}
		}
		
		
		return correctness;
	}

	@Override
	public List<Answer> getAnswers() {
		return answers == null ? null : new ArrayList<>(answers);
	}

	@Override
	public void addAnswer(Answer ans) {
		if (answers == null) {
			answers = new ArrayList<>();
		}
		answers.add(ans);
	}




}
