package com.pearson.test.daalt.dataservice.response.model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Reporter;

import com.pearson.qa.apex.builders.SimpleRestRequestBuilder;
import com.pearson.qa.apex.dataobjects.EnvironmentType;
import com.pearson.qa.apex.dataobjects.IAccessToken;
import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;

public class StudentLearningResourceObject implements ModelWithContract,
		ModelWithLinks, ModelComparable, ModelWithId, Comparator<StudentLearningResourceObject>, Comparable<StudentLearningResourceObject>  {

	private SimpleRestRequestBuilder.IBuilderPostStart createRequest() {
		return new SimpleRestRequestBuilder.ExpressionBuilder();
	}

	public String platformId;
	public String courseSectionId;
	public String learningModuleId;
	public String learningResourceId;
	public float learningResourceSequence;
	public String learningResourceTitle;
	public String learningResourceType;
	public String learningResourceSubType;	
	public Boolean hasChildrenFlag;
	public Float pointsPossible;
	public Float practicePointsPossible;
	public String studentId;
	public String studentFirstName;
	public String studentLastName;
	public Float studentPoints;
	public Boolean includesAdjustedPoints;
	public Float studentPracticePoints;
	public Float studentPercent;
	public String timeSpentTotal;
	public String totalTimeSpentAssessment;
	public String totalTimeSpentLearning;
	public String totalChildTimeSpentAssessment;
	public String totalChildTimeSpentLearning;
	public Float studentLateSubmissionPoints;
	
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

		float[] requiredFloats = new float[] { pointsPossible, studentPoints,
				studentPercent, practicePointsPossible, studentPracticePoints, studentLateSubmissionPoints};

		String[] requiredFields = new String[] { courseSectionId, learningResourceSubType,
				learningModuleId, learningResourceId, learningResourceTitle,
				learningResourceType, studentId, platformId, studentFirstName,
				studentLastName, timeSpentTotal, totalChildTimeSpentLearning, totalChildTimeSpentAssessment, totalTimeSpentLearning, totalTimeSpentAssessment };

		boolean contractIsValid = true;

		for (float d : requiredFloats) {
			if (d < 0) {
				contractIsValid = false;
			}
		} 

		for (String s : requiredFields) {
			if (s == null || "".equals(s)) {
				Reporter.log("One or more of the required fields is missing for learning resource with title "
						+ learningResourceTitle);
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
		StudentLearningResourceObject actualObject = null;
		try {
			actualObject = (StudentLearningResourceObject) compareObject;
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
		differences.addAll(ModelComparisonHelper
				.getDifferencesBetweenLinkCollections(_links,
						actualObject._links));

		return differences;
	}

	private List<Difference> getDifferencesBetweenProperties(
			StudentLearningResourceObject compareObject) {

		List<Difference> differences = new LinkedList<Difference>();

		if (!this.platformId.equals(compareObject.platformId)) {
			differences.add(new Difference("platformId does not match",
					this.platformId, compareObject.platformId));
		}

		if (this.studentPercent != compareObject.studentPercent) {
			differences.add(new Difference("studentScore does not match",
					String.valueOf(this.studentPercent), String
							.valueOf(compareObject.studentPercent)));
		}

		if (!this.learningResourceId.equals(compareObject.learningResourceId)) {
			differences.add(new Difference("learningResourceId does not match",
					String.valueOf(this.learningResourceId), String
							.valueOf(compareObject.learningResourceId)));
		}

		if (!this.learningModuleId.equals(compareObject.learningModuleId)) {
			differences.add(new Difference("learningModuleId does not match",
					this.learningModuleId, compareObject.learningModuleId));
		}

		if (!this.courseSectionId.equals(compareObject.courseSectionId)) {
			differences.add(new Difference("courseSectionId does not match",
					this.courseSectionId, compareObject.courseSectionId));
		}

		if (!this.learningResourceTitle
				.equals(compareObject.learningResourceTitle)) {
			differences.add(new Difference(
					"learningModuleTitle does not match",
					this.learningResourceTitle,
					compareObject.learningResourceTitle));
		}

		if (!this.studentId.equals(compareObject.studentId)) {
			differences.add(new Difference("studentId does not match",
					this.studentId, compareObject.studentId));
		}

		if (!this.learningResourceType
				.equals(compareObject.learningResourceType)) {
			differences.add(new Difference(
					"learningResourceType does not match",
					this.learningResourceType,
					compareObject.learningResourceType));
		}

		return differences;
	}

	@Override
	@JsonIgnore
	public String getId() {
		return learningResourceId;
	}

	@JsonIgnore
	public String getSelfLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("self")) {
				return link.getValue().href;
			}
		}

		return "Could not find self link for learning resource " + getId();
	}

	@JsonIgnore
	public String getResourcesLInk() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("childResources")) {
				return link.getValue().href;
			}
		}

		return "Could not find child resources link for learning resource "
				+ getId();
	}

	@JsonIgnore
	public String getItemsLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("items")) {
				return link.getValue().href;
			}
		}

		return "Could not find child resources link for learning resource "
				+ getId();
	}

	public boolean hasResources(IAccessToken token) throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("childResources")) {
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
			if (link.getKey().equals("childResources")) {
				SimpleRestRequestContext context = createRequest().ofType()
						.get().andFullUrl(link.getValue().href)
						.andAccessToken(token).executeAndReturnContext();

				// if (context.getActualReturnedStatus() != 200) {
				// throw new Exception(
				// "Could not retrieve any resources for module "
				// + getId());
				// }

				if (context.getActualReturnedStatus() == 200) {
					return context
							.getResponseAsDeserializedObject(DaaltStudentLearningResourceCollectionObject.class);
				} else {
					return null;
				}
			}
		}
		throw new Exception("Could not retrieve any resources for module "
				+ getId());
	}

	public boolean hasItems(IAccessToken token) throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("items")) {
				return createRequest().ofType().get()
						.andFullUrl(link.getValue().href).andAccessToken(token)
						.executeAndReturnStatus() == 200;
			}
		}
		return false;
	}

	public DaaltStudentLearningResourceItemCollectionObject getItems(
			IAccessToken token, EnvironmentType envType) throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("items")) {
				SimpleRestRequestContext context = createRequest().ofType()
						.get().andFullUrl(link.getValue().href)
						.andAccessToken(token).executeAndReturnContext();

				if (context.getActualReturnedStatus() != 200) {
					throw new Exception(
							"Could not retrieve any student resource items for resource "
									+ getId());
				}

				return context
						.getResponseAsDeserializedObject(DaaltStudentLearningResourceItemCollectionObject.class);
			}
		}
		throw new Exception(
				"Could not retrieve any student resource items for resource "
						+ getId());
	}

	public Map<String, String> getAllData(IAccessToken token) throws Exception {
		Map<String, String> output = new TreeMap<String, String>();

		output.put(getSelfLink(), new ObjectMapper().writeValueAsString(this));

		// Append allr esources data
		if (hasResources(token)) {

			output.put(getResourcesLInk(),
					new ObjectMapper().writeValueAsString(getResources(token)));

			for (StudentLearningResourceObject resource : getResources(token)
					.getItems()) {
				for (Entry<String, String> entry : resource.getAllData(token)
						.entrySet()) {
					output.put(entry.getKey(), entry.getValue());
				}
			}

		}

		if (hasItems(token)) {

			output.put(getItemsLink(),
					new ObjectMapper().writeValueAsString(getResources(token)));
		}

		return output;

	}
	

	@Override
	public int compareTo(StudentLearningResourceObject other) {
		int toReturn = 0;
		if (this.learningResourceSequence > other.learningResourceSequence) {
			toReturn = 1;
		} else if (this.learningResourceSequence < other.learningResourceSequence) {
			toReturn = -1;
		}
		return toReturn;
	}

	@Override
	public int compare(StudentLearningResourceObject thisLR, StudentLearningResourceObject thatLR) {
		int toReturn = 0;
		if (thisLR.learningResourceSequence > thatLR.learningResourceSequence) {
			toReturn = 1;
		} else if (thisLR.learningResourceSequence < thatLR.learningResourceSequence) {
			toReturn = -1;
		}
		return toReturn;
	}

}
