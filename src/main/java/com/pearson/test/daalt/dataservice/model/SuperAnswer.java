package com.pearson.test.daalt.dataservice.model;

import java.util.ArrayList;
import java.util.List;

public class SuperAnswer {
	private List<Answer> answers;

	public List<Answer> getAnswers() {
		return answers;
	}
	
	public void addAnswer(Answer answer) {
		if (answers == null) {
			answers = new ArrayList<>();
		}
		answers.add(answer);
	}
	
	public void removeOneAnswer() {
		if (answers != null && !answers.isEmpty()) {
			answers.remove(0);
		}
	}
	
	public boolean contains(Answer answer) {
		boolean contains = false;
		if (answers != null && !answers.isEmpty()) {
			for (Answer localAnswer : answers) {
				if (localAnswer.getId().equalsIgnoreCase(answer.getId())) {
					contains = true;
				}
			}
		}
		return contains;
	}
}
