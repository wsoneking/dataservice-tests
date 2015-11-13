package com.pearson.test.daalt.dataservice.response.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

public class LearningModuleStudentObject implements ModelWithContract,
		ModelWithLinks, ModelWithId {

	public String platformId;
	public String courseSectionId;
	public String learningModuleId;
	public String learningModuleTitle;
	public Float learningModuleSequence;
	public Float pointsPossible;
	public String studentId;
	public String studentFirstName;
	public String studentLastName;
	
	public Float studentPoints;
	public Float studentPercent;

	public Float practicePointsPossible;
	public Float studentPracticePoints;		
	public Float studentTrending;
	
	public String timeSpentAssessment;
	public String timeSpentLearning;
	public String timeSpentTotal;
	
	public Map<String, LinkObject> _links;
	
	//FUTURE: public float trending;
	
	@Override
	@JsonIgnore
	public boolean isContractValid() {
		String[] requiredStrings = new String[] { courseSectionId,
				learningModuleId, learningModuleTitle,
				platformId, studentId, studentFirstName, studentLastName , timeSpentAssessment, timeSpentLearning, timeSpentTotal};

		float[] requiredFloats = new float[] {  pointsPossible, studentPoints,
				studentPercent, studentPracticePoints, studentTrending, practicePointsPossible, learningModuleSequence};

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
