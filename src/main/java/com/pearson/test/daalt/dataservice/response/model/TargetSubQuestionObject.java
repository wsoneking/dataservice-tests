package com.pearson.test.daalt.dataservice.response.model;


import org.codehaus.jackson.annotate.JsonIgnore;


public class TargetSubQuestionObject {
	public String targetSubQuestionId;
	public String targetSubQuestionText;
	public TargetSubQuestionResponseObject[] targetSubQuestionResponses;
	public TargetSubQuestionAnswerObject[] targetSubQuestionAnswers;
	
	@JsonIgnore
	public TargetSubQuestionResponseObject getResponseByResponseCode(String subQuestionResponseCode){
		TargetSubQuestionResponseObject response=null;
		if (targetSubQuestionResponses != null) {
			for(TargetSubQuestionResponseObject targetSubQuestionResponse : targetSubQuestionResponses){
				if(targetSubQuestionResponse.targetSubQuestionResponseCode.compareTo(subQuestionResponseCode)==0){
					 response=targetSubQuestionResponse;
					 break;
				}
			}
		}
		return response;
	}
	
	@JsonIgnore
	public TargetSubQuestionAnswerObject getAnswerByAnswerId(String targetSubQuestionAnswerId ){
		TargetSubQuestionAnswerObject response=null;
			for(TargetSubQuestionAnswerObject targetSubQuestionAnswer : targetSubQuestionAnswers){
				if(targetSubQuestionAnswer.targetSubQuestionAnswerId.compareTo(targetSubQuestionAnswerId)==0){
					response=targetSubQuestionAnswer;
					break;
			}
		}
		return response;
	
	}
	
	
	public TargetSubQuestionResponseObject targetSubQuestionResponses() {
		
		return null;
	}
	
}
