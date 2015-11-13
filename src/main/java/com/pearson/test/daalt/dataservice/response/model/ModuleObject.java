package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.codehaus.jackson.map.ObjectMapper;
import com.pearson.qa.apex.builders.SimpleRestRequestBuilder;
import com.pearson.qa.apex.dataobjects.IAccessToken;
import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;

public class ModuleObject implements ModelWithContract, ModelWithLinks,
		ModelComparable, ModelWithId {

	private SimpleRestRequestBuilder.IBuilderPostStart createRequest() {
		return new SimpleRestRequestBuilder.ExpressionBuilder();
	}

	public float avgTimeSpentAssessment;
	public float avgTimeSpentLearning;
	public float classAvgPercent;
	public float classAvgPoints;
	public float completedStudentCount;
	public float incompleteStudentCount;
	public float pointsPossible;
	public String courseSectionId;
	public String learningModuleId;
	public String learningModuleSequence;
	public String learningModuleTitle;
	public String platformId;
	public String learningModuleDueDate;
	public Map<String, LinkObject> _links;

	public Map<String, LinkObject> getLinks() {
		return _links;
	}

	public void setLinks(Map<String, LinkObject> links) {
		this._links = links;
	}

	@Override
	public boolean isContractValid() {
		float[] floatValues = new float[] { avgTimeSpentAssessment,
				avgTimeSpentLearning, pointsPossible, classAvgPercent,
				classAvgPoints, completedStudentCount };
		String[] requiredStrings = new String[] { courseSectionId,
				learningModuleId, learningModuleTitle, platformId,
				learningModuleSequence };

		boolean contractIsValid = true;

		if (completedStudentCount < 0) {
			contractIsValid = false;
		}

		for (float d : floatValues) {
			if (d < 0) {
				contractIsValid = false;
			}
		}

		for (String s : requiredStrings) {
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

		return contractIsValid;

	}

	@Override
	public List<Difference> getDifferenceBetweenObjects(
			ModelComparable compareObject) {
		List<Difference> differences = new LinkedList<Difference>();

		// If its not the same type of object stop
		ModuleObject actualObject = null;
		try {
			actualObject = (ModuleObject) compareObject;
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
			ModuleObject compareObject) {

		List<Difference> differences = new LinkedList<Difference>();

		if (!this.platformId.equals(compareObject.platformId)) {
			differences.add(new Difference("platformId does not match",
					this.platformId, compareObject.platformId));
		}

		if (this.avgTimeSpentAssessment != compareObject.avgTimeSpentAssessment) {
			differences.add(new Difference(
					"avgTimeSpentAssessment does not match", String
							.valueOf(this.avgTimeSpentAssessment), String
							.valueOf(compareObject.avgTimeSpentAssessment)));
		}

		if (this.avgTimeSpentLearning != compareObject.avgTimeSpentLearning) {
			differences.add(new Difference(
					"avgTimeSpentLearning does not match", String
							.valueOf(this.avgTimeSpentLearning), String
							.valueOf(compareObject.avgTimeSpentLearning)));
		}

		if (this.classAvgPercent != compareObject.classAvgPercent) {
			differences.add(new Difference("classAvg does not match", String
					.valueOf(this.classAvgPercent), String
					.valueOf(compareObject.classAvgPercent)));
		}

		if (!this.learningModuleSequence
				.equals(compareObject.learningModuleSequence)) {
			differences.add(new Difference(
					"learningModuleSequence does not match", String
							.valueOf(this.learningModuleSequence), String
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

		if (!this.learningModuleTitle.equals(compareObject.learningModuleTitle)) {
			differences
					.add(new Difference("learningModuleTitle does not match",
							this.learningModuleTitle,
							compareObject.learningModuleTitle));
		}

		return differences;
	}

	@Override
	public String getId() {
		return learningModuleId;
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

	public DaaltLearningResourceCollectionObject getResources(IAccessToken token)
			throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("resources")) {
				SimpleRestRequestContext context = createRequest().ofType()
						.get().andFullUrl(link.getValue().href)
						.andAccessToken(token).executeAndReturnContext();

				if (context.getActualReturnedStatus() != 200) {
					throw new Exception(
							"Could not retrieve any resources for module "
									+ getId());
				}

				return context
						.getResponseAsDeserializedObject(DaaltLearningResourceCollectionObject.class);
			}
		}
		throw new Exception("Could not retrieve any resources for module "
				+ getId());
	}

	public boolean hasStudents(IAccessToken token) throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("students")) {
				return createRequest().ofType().get()
						.andFullUrl(link.getValue().href).andAccessToken(token)
						.executeAndReturnStatus() == 200;
			}
		}
		return false;
	}

	public DaaltLearningModuleStudentCollectionObject getStudents(
			IAccessToken token) throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("students")) {
				SimpleRestRequestContext context = createRequest().ofType()
						.get().andFullUrl(link.getValue().href)
						.andAccessToken(token).executeAndReturnContext();

				if (context.getActualReturnedStatus() != 200) {
					throw new Exception(
							"Could not retrieve any resources for module "
									+ getId());
				}

				return context
						.getResponseAsDeserializedObject(DaaltLearningModuleStudentCollectionObject.class);
			}
		}
		throw new Exception(
				"Could not retrieve any resources for module students "
						+ getId());
	}

	public String getSelfLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("self")) {
				return link.getValue().href;
			}
		}

		return "Could not find self link for module object " + getId();
	}

	public String getStudentsLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("students")) {
				return link.getValue().href;
			}
		}

		return "Could not find students link for module object " + getId();
	}

	public String getResourcesLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("resources")) {
				return link.getValue().href;
			}
		}

		return "Could not find resources link for module object " + getId();
	}

	public Map<String, String> getAllData(IAccessToken token) throws Exception {
		Map<String, String> output = new TreeMap<String, String>();

		// append self
		output.put(getSelfLink(), new ObjectMapper().writeValueAsString(this));

		// Append allr esources data
		if (hasResources(token)) {

			output.put(getResourcesLink(),
					new ObjectMapper().writeValueAsString(getResources(token)));

			for (LearningResourceObject resource : getResources(token)
					.getItems()) {
				for (Entry<String, String> entry : resource.getAllData(token)
						.entrySet()) {
					output.put(entry.getKey(), entry.getValue());
				}
			}

		}

		// Append all students data
		if (hasStudents(token)) {

			output.put(getStudentsLink(),
					new ObjectMapper().writeValueAsString(getStudents(token)));

		}

		return output;

	}
}
