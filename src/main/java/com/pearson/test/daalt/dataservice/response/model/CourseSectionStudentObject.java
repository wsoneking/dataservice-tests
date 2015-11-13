package com.pearson.test.daalt.dataservice.response.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

public class CourseSectionStudentObject implements ModelWithContract,
		ModelWithLinks, ModelWithId {

	public String platformId;
	public String courseSectionId;
	public String studentId;
	public String timeSpentLearning;
	public String timeSpentAssessment;
	public String timeSpentTotal;
	public Float studentTrending;
	public String lastActivityDate;
	
	public Map<String, LinkObject> _links;
	
	//FUTURE: public float trending;
	
	@Override
	@JsonIgnore
	public boolean isContractValid() {
		String[] requiredStrings = new String[] { courseSectionId,
				platformId, studentId, timeSpentLearning, timeSpentAssessment, lastActivityDate};

		float[] requiredFloats = new float[] {  };

		boolean contractIsValid = true;

		for (String s : requiredStrings) {
			if (s == null || "".equals(s)) {
				contractIsValid = false;
			}
		}

		for (float d : requiredFloats) {
			if (d < 0) {
				contractIsValid = false;
			}
		}

		for (LinkObject link : _links.values()) {
			if (!link.isContractValid()) {
				contractIsValid = false;
			}
		}

		return contractIsValid;
		
	}

	public Map<String, LinkObject> getLinks() {
		return _links;
	}

	public void setLinks(Map<String, LinkObject> links) {
		this._links = links;
	}

	@Override
	@JsonIgnore
	public String getId() {
		return studentId;
	}
}
