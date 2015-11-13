package com.pearson.test.daalt.dataservice.response.model;

import java.util.Map;

public class StudentLearningResourceItemObject implements ModelWithContract,
		ModelWithLinks, ModelWithId {

	public String courseSectionId;
	public String itemBodyText;
	public String itemId;
	public float itemSequence;
	public String learningModuleId;
	public String learningResourceId;
	public String learningResourceTitle;
	public String learningResourceType;
	public float learningResourceSequence;
	public float timeSpent;
	public String platformId;
	public Map<String, LinkObject> _links;
	public AttemptObject[] attempts;
	public String studentId;
	public float studentPercent;
	public float pointsPossible;
	public float studentPoints;
	public String studentFirstName;
	public String studentLastName;

	@Override
	public boolean isContractValid() {
		String[] requiredFields = new String[] { courseSectionId, itemBodyText,
				itemId, learningModuleId, learningResourceId, platformId,
				studentId, studentFirstName, studentLastName };

		float[] requiredFloats = new float[] {

		pointsPossible, studentPoints, studentPercent, itemSequence };

		boolean contractIsValid = true;

		for (float d : requiredFloats) {

			if (d < 0) {
				contractIsValid = false;
			}
		}

		for (String s : requiredFields) {
			if (s == null || s.equals("")) {
				contractIsValid = false;
			}
		}

		for (LinkObject link : _links.values()) {
			if (!link.isContractValid()) {
				contractIsValid = false;
			}
		}

		if (attempts != null) {
			for (AttemptObject attempt : attempts) {
				if (!attempt.isContractValid()) {
					contractIsValid = false;
				}
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
	public String getId() {
		return itemId;
	}





}
