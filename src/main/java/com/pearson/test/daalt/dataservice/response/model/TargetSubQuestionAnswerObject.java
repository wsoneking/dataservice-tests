package com.pearson.test.daalt.dataservice.response.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class TargetSubQuestionAnswerObject {
	public boolean targetSubQuestionAnswerCorrectFlag;
	public String targetSubQuestionAnswerId;
	public String targetSubQuestionAnswerText;
	public TargetSubQuestionAnswerResponseObject[] targetSubQuestionAnswerResponses;

	@JsonIgnore
	public TargetSubQuestionAnswerResponseObject getResponseByResponseCode(String subQuestionAnswerResponseCode){
		TargetSubQuestionAnswerResponseObject response=null;
		if (targetSubQuestionAnswerResponses != null) {
			for(TargetSubQuestionAnswerResponseObject targetSubQuestionAnswerResponse : targetSubQuestionAnswerResponses){
				if(targetSubQuestionAnswerResponse.targetSubQuestionAnswerResponseCode.compareTo(subQuestionAnswerResponseCode)==0){
					 response=targetSubQuestionAnswerResponse;
					 break;
				}
			}
		}
		return response;
	}
	
}
