package com.pearson.test.daalt.dataservice.response.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;

import com.pearson.qa.apex.dataobjects.EnvironmentType;
import com.pearson.qa.apex.dataobjects.IAccessToken;

public class LearningResourceItemObject implements ModelWithContract,
		ModelWithLinks, ModelWithId, Comparator<LearningResourceItemObject>, Comparable<LearningResourceItemObject>  {

	public String platformId;
	public String courseSectionId;
	public String learningModuleId;
	public String learningResourceId;
	public String assessmentId;
	public String itemId;
	public int itemSequence;
	public String questionType;
	public String questionPresentationFormat;
	public String questionText;
	public float pointsPossible;
	public float totalItemResponseScore;
	public float avgItemResponseScore;
	public int courseSectionStudentCount;
	public int assessmentItemCompletedStudentCount;
	public int correctStudentCount;
	public float correctStudentPercent;
	public int incorrectStudentCount;
	public int noAttemptStudentCount;
	public String totalTimeSpentAssessing;
	public String medianTimeSpentAssessing;
	public String avgTimeSpentAssessing;
	public AttemptObject[] attempts;
	
	public Map<String, LinkObject> _links;
	

	@Override
	@JsonIgnore
	public boolean isContractValid() {
		float[] floatValues = new float[] { correctStudentPercent,
				pointsPossible, correctStudentCount, incorrectStudentCount,
				itemSequence, noAttemptStudentCount, assessmentItemCompletedStudentCount, 
				totalItemResponseScore, avgItemResponseScore};

		String[] requiredFields = new String[] { courseSectionId,
				itemId, learningModuleId, learningResourceId, platformId, totalTimeSpentAssessing,
				medianTimeSpentAssessing, avgTimeSpentAssessing};

		boolean contractIsValid = true;

		for (float d : floatValues) {
			if (d < 0) {
				contractIsValid = false;
			}
		}

		for (String s : requiredFields) {
			if (s == null || s.equals("")) {
				contractIsValid = false;
			}
		}

		if (_links != null) {
			for (LinkObject link : _links.values()) {
				if (!link.isContractValid()) {
					contractIsValid = false;
				}
			}
		}

		for (AttemptObject attempt : attempts) {
			if (!attempt.isContractValid()) {
				contractIsValid = false;
			}
		}

		return contractIsValid;
	}

	@JsonIgnore
	public Map<String, LinkObject> getLinks() {
		return _links;
	}

	public void setLinks(Map<String, LinkObject> links) {
		this._links = links;
	}

	@Override
	@JsonIgnore
	public String getId() {
		return itemId;
	}

	@JsonIgnore
	public String getSelfLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("self")) {
				return link.getValue().href;
			}
		}

		return "Could not find self link for resource item " + getId();
	}
	
	@JsonIgnore
	public AttemptObject getAttemptByIndex(int attemptNumber) {
		AttemptObject toReturn = null;
		for (int i=0; i<attempts.length; i++) {
			if (attempts[i].attemptNumber == attemptNumber) {
				toReturn = attempts[i];
			}
		}
		return toReturn;
	}

	public Map<String, String> getAllData(IAccessToken token,
			EnvironmentType envType) throws Exception {
		Map<String, String> output = new TreeMap<String, String>();

		output.put(getSelfLink(), new ObjectMapper().writeValueAsString(this));

		return output;
		// Append all student data

	}
	
	@Override
	public int compareTo(LearningResourceItemObject other) {
		int toReturn = 0;
		if (this.itemSequence > other.itemSequence) {
			toReturn = 1;
		} else if (this.itemSequence < other.itemSequence) {
			toReturn = -1;
		}
		return toReturn;
	}

	@Override
	public int compare(LearningResourceItemObject o1, LearningResourceItemObject o2) {
		int toReturn = 0;
		if (o1.itemSequence > o2.itemSequence) {
			toReturn = 1;
		} else if (o1.itemSequence < o2.itemSequence) {
			toReturn = -1;
		}
		return toReturn;
	}


}
