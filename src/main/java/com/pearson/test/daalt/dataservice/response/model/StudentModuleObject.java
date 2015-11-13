package com.pearson.test.daalt.dataservice.response.model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;

import com.pearson.qa.apex.builders.SimpleRestRequestBuilder;
import com.pearson.qa.apex.dataobjects.IAccessToken;
import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;

public class StudentModuleObject implements ModelWithContract, ModelWithLinks,
		ModelComparable, ModelWithId, Comparator<StudentModuleObject>, Comparable<StudentModuleObject>  {

	private SimpleRestRequestBuilder.IBuilderPostStart createRequest() {
		return new SimpleRestRequestBuilder.ExpressionBuilder();
	}

	public String platformId;
	public String courseSectionId;
	public String studentId;
	public String studentFirstName;
	public String studentLastName;
	public String learningModuleId;
	public float learningModuleSequence;
	public String learningModuleTitle;
	public Float pointsPossible;
	public Float practicePointsPossible;
	public Float studentPoints;
	public Float studentPracticePoints;
	public Float studentPercent;
	public String timeSpentAssessment;
	public String timeSpentLearning;
	public String timeSpentTotal;
	
	public Map<String, LinkObject> _links;

	public Map<String, LinkObject> getLinks() {
		return _links;
	}

	public void setLinks(Map<String, LinkObject> links) {
		this._links = links;
	}

	@Override
	@JsonIgnore
	public boolean isContractValid() {
		String[] requiredFields = new String[] { courseSectionId,
				learningModuleId, learningModuleTitle,
				platformId, studentId, studentFirstName, studentLastName, timeSpentAssessment,
				timeSpentLearning };

		float[] requiredFloats = new float[] {learningModuleSequence, studentPercent, studentPoints,
				pointsPossible };

		boolean contractIsValid = true;

		for (String s : requiredFields) {
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

	@Override
	public List<Difference> getDifferenceBetweenObjects(
			ModelComparable compareObject) {
		List<Difference> differences = new LinkedList<Difference>();

		// If its not the same type of object stop
		StudentModuleObject actualObject = null;
		try {
			actualObject = (StudentModuleObject) compareObject;
		} catch (Exception e) {
			differences.add(new Difference("Objects are not the same type",
					"Expected comparison object to be "
							+ this.getClass().toString(), compareObject
							.getClass().toString()));
			return differences;
		}

		// add any property differences
		differences.addAll(getDifferencesBetweenProperties(actualObject));

		// add any link differences
		// add any link differences
		differences.addAll(ModelComparisonHelper
				.getDifferencesBetweenLinkCollections(_links,
						actualObject._links));

		return differences;

	}

	private List<Difference> getDifferencesBetweenProperties(
			StudentModuleObject compareObject) {

		List<Difference> differences = new LinkedList<Difference>();

		if (!this.platformId.equals(compareObject.platformId)) {
			differences.add(new Difference("platformId does not match",
					this.platformId, compareObject.platformId));
		}

		if (this.timeSpentAssessment != compareObject.timeSpentAssessment) {
			differences.add(new Difference(
					"timeSpentAssessment does not match", String
							.valueOf(this.timeSpentAssessment), String
							.valueOf(compareObject.timeSpentAssessment)));
		}

		if (this.timeSpentLearning != compareObject.timeSpentLearning) {
			differences.add(new Difference(
					"avgTimeSpentLearning does not match", String
							.valueOf(this.timeSpentLearning), String
							.valueOf(compareObject.timeSpentLearning)));
		}

		if (this.studentPercent != compareObject.studentPercent) {
			differences.add(new Difference("studentScore does not match",
					String.valueOf(this.studentPercent), String
							.valueOf(compareObject.studentPercent)));
		}
		
		if (this.learningModuleSequence != compareObject.learningModuleSequence) {
			differences.add(new Difference("learningModuleSequence does not match",
					String.valueOf(this.learningModuleSequence), String
							.valueOf(compareObject.learningModuleSequence)));
		}

		if (!this.learningModuleId.equals(compareObject.learningModuleId)) {
			differences.add(new Difference("learningModuleId does not match",
					this.learningModuleId, compareObject.learningModuleId));
		}

		if (!this.courseSectionId.equals(compareObject.courseSectionId)) {
			differences.add(new Difference("courseSectionId does not match",
					this.courseSectionId, compareObject.courseSectionId));
		}

		if (!this.studentId.equals(compareObject.studentId)) {
			differences.add(new Difference("studentId does not match",
					this.studentId, compareObject.studentId));
		}

		if (!this.learningModuleTitle.equals(compareObject.learningModuleTitle)) {
			differences
					.add(new Difference("learningModuleTitle does not match",
							this.learningModuleTitle,
							compareObject.learningModuleTitle));
		}

		return differences;
	}

	@Override
	@JsonIgnore
	public String getId() {
		return learningModuleId;
	}

	@JsonIgnore
	public String getSelfLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("self")) {
				return link.getValue().href;
			}
		}

		throw new Exception("Could not find self link!");
	}

	@JsonIgnore
	public String getResourcesLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("resources")) {
				return link.getValue().href;
			}
		}

		return "Could not find resources link! for Student Module Object!";
	}

	public boolean hasResources(IAccessToken token) throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("resources")) {
				return createRequest().ofType().get()
						.andFullUrl(link.getValue().href).andAccessToken(token)
						.executeAndReturnStatus() == 200;
			}
		}
		return false;
	}

	public DaaltStudentLearningResourceCollectionObject getResources(
			IAccessToken token) throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("resources")) {
				SimpleRestRequestContext context = createRequest()
						.ofType().get().andFullUrl(link.getValue().href)
						.andAccessToken(token).executeAndReturnContext();

				if (context.getActualReturnedStatus() != 200) {
					throw new Exception(
							"Could not retrieve any resources for module "
									+ getId());
				}

				return context
						.getResponseAsDeserializedObject(DaaltStudentLearningResourceCollectionObject.class);
			}
		}
		throw new Exception("Could not retrieve any resources for module "
				+ getId());
	}

	public Map<String, String> getAllData(IAccessToken token) throws Exception {
		Map<String, String> output = new TreeMap<String, String>();

		output.put(getSelfLink(), new ObjectMapper().writeValueAsString(this));

		// Append allr esources data
		if (hasResources(token)) {

			output.put(getResourcesLink(),
					new ObjectMapper().writeValueAsString(getResources(token)));

			for (StudentLearningResourceObject resource : getResources(token)
					.getItems()) {
				for (Entry<String, String> entry : resource.getAllData(token)
						.entrySet()) {
					output.put(entry.getKey(), entry.getValue());
				}
			}

		}

		return output;

	}

	@Override
	public int compareTo(StudentModuleObject other) {
		int toReturn = 0;
		if (this.learningModuleSequence > other.learningModuleSequence) {
			toReturn = 1;
		} else if (this.learningModuleSequence < other.learningModuleSequence) {
			toReturn = -1;
		}
		return toReturn;
	}

	@Override
	public int compare(StudentModuleObject thisLR, StudentModuleObject thatLR) {
		int toReturn = 0;
		if (thisLR.learningModuleSequence > thatLR.learningModuleSequence) {
			toReturn = 1;
		} else if (thisLR.learningModuleSequence < thatLR.learningModuleSequence) {
			toReturn = -1;
		}
		return toReturn;
	}
}
