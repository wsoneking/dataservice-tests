package com.pearson.test.daalt.dataservice.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QuizCompletionActivity {
	private User user;
	private Map<String, Float> performancePerQuestion;
	public boolean isAdjusted;
	private boolean assignmentComplete;
	
	public QuizCompletionActivity(User user) {
		this.user = user;
		isAdjusted = false;
		performancePerQuestion = new HashMap<>();
	}
	
	public void setIsAdjusted(boolean boo) {
		isAdjusted = boo;
	}
	
	public User getPerson() {
		return user;
	}
	
	public String getPersonId() {
		return user.getPersonId();
	}
	
	public String getPersonRole() {
		return user.getPersonRole();
	}
	
	public boolean isAssignmentComplete() {
		return assignmentComplete;
	}

	public void setAssignmentComplete(boolean assignmentComplete) {
		this.assignmentComplete = assignmentComplete;
	}

	public float getPointsEarned() {
		float pointsEarned = 0;
		if (performancePerQuestion != null) {
			Iterator<String> questionPerfIter = performancePerQuestion.keySet().iterator();
			while (questionPerfIter.hasNext()) {
				pointsEarned += performancePerQuestion.get(questionPerfIter.next());
			}
		}
		return pointsEarned;
	}
	
	public Map<String, Float> getQuestionPerfs() {
		return performancePerQuestion;
	}

	public void addQuestionPerf(String questionId, float pointsEarned) {
		if (performancePerQuestion == null) {
			performancePerQuestion = new HashMap<>();
		}
		performancePerQuestion.put(questionId, pointsEarned);
	}
}
