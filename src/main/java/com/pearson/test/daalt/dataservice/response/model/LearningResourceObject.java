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
import com.pearson.qa.apex.dataobjects.IAccessToken;
import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;

public class LearningResourceObject implements ModelWithContract,
		ModelWithLinks, ModelComparable, ModelWithId, Comparator<LearningResourceObject>, Comparable<LearningResourceObject> {

	private SimpleRestRequestBuilder.IBuilderPostStart createRequest() {
		return new SimpleRestRequestBuilder.ExpressionBuilder();
	}

	public String platformId;
	public String courseSectionId;
	public String learningModuleId;
	public String learningResourceId;
	
	public String learningResourceTitle;
	public String learningResourceType;
	public String learningResourceSubType;
	public float learningResourceSequence;
	
	public Float pointsPossible;
	public Float practicePointsPossible;
	public boolean hasChildrenFlag;
	public Float classTotalPoints;
	public Float classAvgPoints;
	public Float classAvgPercent;
	public Float classTotalPracticePoints;
	public Float classAvgPracticePoints;
	public String avgTimeSpentAssessment;
	public String totalTimeSpentAssessment;
	public String totalChildTimeSpentAssessment;
	public String avgTimeSpentLearning;
	public String totalTimeSpentLearning;
	public String totalChildTimeSpentLearning;
	public String timeSpentTotal;
	public String avgTimeSpentTotal;
	public int courseSectionStudentCount;	
	public int completedStudentCount;
	public int incompleteStudentCount;
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

		float[] floatValues = new float[] {
				pointsPossible, practicePointsPossible, classAvgPoints, classAvgPercent, 
				classTotalPracticePoints, classAvgPracticePoints};

		String[] requiredFields = new String[] { courseSectionId,
				learningModuleId, learningResourceId, learningResourceTitle,
				learningResourceType, learningResourceSubType, platformId , 
				avgTimeSpentTotal, avgTimeSpentAssessment, 
				totalChildTimeSpentAssessment,totalTimeSpentAssessment, avgTimeSpentLearning,
				totalChildTimeSpentLearning, totalTimeSpentLearning,};

		boolean contractIsValid = true;

		for (float d : floatValues) {
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

		if (_links != null) {
			for (LinkObject link : _links.values()) {
				if (!link.isContractValid()) {
					contractIsValid = false;
				}
			}
		}

		return contractIsValid;
	}

	@Override
	public List<Difference> getDifferenceBetweenObjects(
			ModelComparable compareObject) {
		List<Difference> differences = new LinkedList<Difference>();

		// If its not the same type of object stop
		LearningResourceObject actualObject = null;
		try {
			actualObject = (LearningResourceObject) compareObject;
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
			LearningResourceObject compareObject) {

		List<Difference> differences = new LinkedList<Difference>();

		if (!this.platformId.equals(compareObject.platformId)) {
			differences.add(new Difference("platformId does not match",
					this.platformId, compareObject.platformId));
		}

		if (this.avgTimeSpentTotal != compareObject.avgTimeSpentTotal) {
			differences.add(new Difference("avgTimeSpentTotal does not match",
					String.valueOf(this.avgTimeSpentTotal), String
							.valueOf(compareObject.avgTimeSpentTotal)));
		}

		if (this.classAvgPercent != compareObject.classAvgPercent) {
			differences.add(new Difference("classAvg does not match", String
					.valueOf(this.classAvgPercent), String
					.valueOf(compareObject.classAvgPercent)));
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

	public DaaltLearningResourceCollectionObject getResources(IAccessToken token)
			throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("childResources")) {
				SimpleRestRequestContext context = createRequest().ofType()
						.get().andFullUrl(link.getValue().href)
						.andAccessToken(token).executeAndReturnContext();

				if (context.getActualReturnedStatus() != 200) {
					throw new Exception(
							"Could not retrieve any child resources for resource "
									+ getId());
				}

				return context
						.getResponseAsDeserializedObject(DaaltLearningResourceCollectionObject.class);
			}
		}
		throw new Exception(
				"Could not retrieve any child resources for resource "
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

	public DaaltLearningResourceItemCollectionObject getItems(IAccessToken token)
			throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("items")) {
				SimpleRestRequestContext context = createRequest().ofType()
						.get().andFullUrl(link.getValue().href)
						.andAccessToken(token).executeAndReturnContext();

				if (context.getActualReturnedStatus() != 200) {
					throw new Exception(
							"Could not retrieve any resource items for resource "
									+ getId());
				}

				return context
						.getResponseAsDeserializedObject(DaaltLearningResourceItemCollectionObject.class);
			}
		}
		throw new Exception(
				"Could not retrieve any resource items for resource " + getId());
	}

	@JsonIgnore
	public String getSelfLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("self")) {
				return link.getValue().href;
			}
		}

		return "Could not find self link for resource " + getId();
	}

	@JsonIgnore
	public String getResourcesLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("childResources")) {
				return link.getValue().href;
			}
		}

		return "Could not find child resources link for resource " + getId();
	}

	@JsonIgnore
	public String getItemsLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("items")) {
				return link.getValue().href;
			}
		}

		return "Could not find items link for resource " + getId();
	}

	public Map<String, String> getAllData(IAccessToken token) throws Exception {
		Map<String, String> output = new TreeMap<String, String>();

		output.put(getSelfLink(), new ObjectMapper().writeValueAsString(this));

		// resources
		if (hasResources(token)) {

			output.put(getResourcesLink(),
					new ObjectMapper().writeValueAsString(getResources(token)));

			for (LearningResourceObject resource : getResources(token)
					.getItems()) {
				for (Entry<String, String> entry : resource.getAllData(token)
						.entrySet()) {
					output.put("Child of: " + getId() + " " + entry.getKey(),
							entry.getValue());
				}
			}
		}

		if (hasItems(token)) {

			output.put(getItemsLink(),
					new ObjectMapper().writeValueAsString(getItems(token)));

		}

		return output;
		// Append all student data

	}

	@Override
	public int compareTo(LearningResourceObject other) {
		int toReturn = 0;
		if (this.learningResourceSequence > other.learningResourceSequence) {
			toReturn = 1;
		} else if (this.learningResourceSequence < other.learningResourceSequence) {
			toReturn = -1;
		}
		return toReturn;
	}

	@Override
	public int compare(LearningResourceObject thisLR, LearningResourceObject thatLR) {
		int toReturn = 0;
		if (thisLR.learningResourceSequence > thatLR.learningResourceSequence) {
			toReturn = 1;
		} else if (thisLR.learningResourceSequence < thatLR.learningResourceSequence) {
			toReturn = -1;
		}
		return toReturn;
	}

}
