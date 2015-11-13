package com.pearson.test.daalt.dataservice.response.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class AttemptObject implements ModelWithContract {

	public int attemptNumber;
	public int attemptNumberStudentCount;
	public ResponseObject[] responses;
	public TargetSubQuestionObject[] targetSubQuestions;
	
	
	public int getAttemptNumber() {
		return attemptNumber;
	}

	public void setAttemptNumber(int attemptNumber) {
		this.attemptNumber = attemptNumber;
	}

	public int getAttemptNumberStudentCount() {
		return attemptNumberStudentCount;
	}

	public void setAttemptNumberStudentCount(int attemptNumberStudentCount) {
		this.attemptNumberStudentCount = attemptNumberStudentCount;
	}
	
	public ResponseObject[] getResponses() {
		if (responses == null) {
			responses = new ResponseObject[] {};
		}
		return responses;
	}

	public void setResponses(ResponseObject[] responses) {
		this.responses = responses;
	}
	
	@JsonIgnore
	public ResponseObject getResponseByCode(String responseCode) {
		ResponseObject toReturn = null;
		if (responses != null) {
			for (int i=0; i<responses.length; i++) {
				if (responses[i].getResponseCode().equalsIgnoreCase(responseCode)) {
					toReturn = responses[i];
				}
			}
		}
		return toReturn;
	}

	public TargetSubQuestionObject[] getTargetSubQuestions() {
		if(targetSubQuestions==null) {
			targetSubQuestions=new TargetSubQuestionObject[]{};
		}
		
		return targetSubQuestions;
	}
	
	public void setTargetSubQuestions(TargetSubQuestionObject[] targetSubQuestions) {
		this.targetSubQuestions = targetSubQuestions;
	}
	
	@JsonIgnore
	public TargetSubQuestionObject getTargetSubQuestionById(String targetSubQuestionId) {
		TargetSubQuestionObject toReturn =null;
		if(targetSubQuestions!=null) {
			for(TargetSubQuestionObject subQuestion : targetSubQuestions) {
				if (subQuestion.targetSubQuestionId.equalsIgnoreCase(targetSubQuestionId)) { 
					toReturn = subQuestion;
					break;
				}
			}
		}
		return toReturn;
	}

	
	@Override
	public String toString(){

		String out = null;
		out = "AttemptNumber - "+attemptNumber+"; ";
	
	return out;
		
	}

	@Override 
	@JsonIgnore
	public boolean isContractValid() {
		int[] positiveInts = new int[] {attemptNumber, attemptNumberStudentCount};
		
		boolean contractIsValid = true;
		
		for (int i : positiveInts) {
			if (i < 0) {
				contractIsValid = false;
			}
		}
		
		for (ResponseObject response : getResponses()) {
			if (!response.isContractValid()) {
				contractIsValid = false;
			}
		}
		
		return contractIsValid;
	} 

}
