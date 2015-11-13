package com.pearson.test.daalt.dataservice.response.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ResponseObject implements ModelWithContract {
	public String responseCode;
	public int responseStudentCount;
	public float responsePercent; 

	public float getResponsePercent() {
		return responsePercent;
	}

	public void setResponsePercent(float responsePercent) {
		this.responsePercent = responsePercent;
	}

	
	public String getResponseCode() {
		return responseCode;
	}


	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public int getResponseStudentCount() {
		return responseStudentCount;
	}

	public void setResponseStudentCount(int responseStudentCount) {
		this.responseStudentCount = responseStudentCount;
	}

	@Override
	@JsonIgnore
	public boolean isContractValid() {
		return (responseCode != null && !"".equals(responseCode)/* && studentResponseCount >= 0*/);
	}

}
