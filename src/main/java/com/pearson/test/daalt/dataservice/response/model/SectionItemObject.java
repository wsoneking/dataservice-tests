package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.codehaus.jackson.map.ObjectMapper;
import com.pearson.qa.apex.builders.SimpleRestRequestBuilder;
import com.pearson.qa.apex.dataobjects.IAccessToken;
import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;

public class SectionItemObject implements ModelWithContract, ModelWithLinks,
		ModelComparable, ModelWithId {

	private SimpleRestRequestBuilder.IBuilderPostStart createRequest() {
		return new SimpleRestRequestBuilder.ExpressionBuilder();
	}

	public Map<String, LinkObject> _links;
	public String platformId;
	public String courseCode;
	public String courseTitle;
	public String courseSectionId;
	public String courseSectionCode;
	public float avgTimeSpentLearning;
	public float avgTimeSpentAssessment;
	public float studentCount;
	public float classAvgPercent;

	public Map<String, LinkObject> getLinks() {
		return _links;
	}

	public void setLinks(Map<String, LinkObject> links) {
		this._links = links;
	}

	public boolean isContractValid() {

		boolean contractIsValid = true;

		float[] floatValues = new float[] { avgTimeSpentAssessment,
				avgTimeSpentLearning, classAvgPercent, studentCount };

		String[] requiredFields = new String[] { platformId, courseSectionCode,
				courseTitle, courseSectionId };

		if (studentCount < 0) {

			contractIsValid = false;

		}

		for (float d : floatValues) {
			if (d < 0) {
				contractIsValid = false;
			}
		}

		for (String s : requiredFields) {
			if (s == null || "".equals(s)) {
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
		SectionItemObject actualObject = null;
		try {
			actualObject = (SectionItemObject) compareObject;
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
			SectionItemObject compareObject) {

		List<Difference> differences = new LinkedList<Difference>();

		if (!this.platformId.equals(compareObject.platformId)) {
			differences.add(new Difference("platformId does not match",
					this.platformId, compareObject.platformId));
		}

		if (!this.courseSectionCode.equals(compareObject.courseSectionCode)) {
			differences.add(new Difference("courseSectionCode does not match",
					this.courseCode, compareObject.courseCode));
		}

		if (!this.courseTitle.equals(compareObject.courseTitle)) {
			differences.add(new Difference("courseTitle does not match",
					this.courseTitle, compareObject.courseTitle));
		}

		if (!this.courseSectionId.equals(compareObject.courseSectionId)) {
			differences.add(new Difference("courseSectionId does not match",
					this.courseSectionId, compareObject.courseSectionId));
		}

		if (this.classAvgPercent != compareObject.classAvgPercent) {
			differences.add(new Difference("classAvgPercent does not match",
					this.classAvgPercent, compareObject.classAvgPercent));
		}

		if (this.avgTimeSpentLearning != compareObject.avgTimeSpentLearning) {
			differences.add(new Difference(
					"avgTimeSpentLearning does not match",
					this.avgTimeSpentLearning,
					compareObject.avgTimeSpentLearning));
		}

		return differences;
	}

	@Override
	public String getId() {
		return courseSectionId;
	}

	public boolean hasModules(IAccessToken token) throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("modules")) {
				return createRequest().ofType().get()
						.andFullUrl(link.getValue().href).andAccessToken(token)
						.executeAndReturnStatus() == 200;
			}
		}
		return false;

	}

	public DaaltModuleCollectionObject getModules(IAccessToken token)
			throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("modules")) {
				SimpleRestRequestContext context = createRequest().ofType()
						.get().andFullUrl(link.getValue().href)
						.andAccessToken(token).executeAndReturnContext();

				if (context.getActualReturnedStatus() != 200) {
					throw new Exception(
							"Could not retrieve any modules for section "
									+ getId());
				}

				return context
						.getResponseAsDeserializedObject(DaaltModuleCollectionObject.class);
			}
		}

		throw new Exception("Could not retrieve any modules for section "
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

	public String getSelfLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("self")) {
				return link.getValue().href;
			}
		}

		return "Could not find self  link for section object " + getId();
	}

	public String getStudentsLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("students")) {
				return link.getValue().href;
			}
		}

		return "Could not find students link for section object " + getId();
	}

	public String getModulesLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("modules")) {
				return link.getValue().href;
			}
		}

		return "Could not find modules link for section object " + getId();
	}

	public SectionItemObject getSelf(IAccessToken token) throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("self")) {
				SimpleRestRequestContext context = createRequest().ofType()
						.get().andFullUrl(link.getValue().href)
						.andAccessToken(token).executeAndReturnContext();

				if (context.getActualReturnedStatus() != 200) {
					throw new Exception(
							"Could not retrieve self link for section "
									+ getId());
				}

				return context
						.getResponseAsDeserializedObject(SectionItemObject.class);
			}
		}

		throw new Exception("Could not retrieve self link for section "
				+ getId());
	}

	public DaaltSectionStudentCollectionObject getsStudents(IAccessToken token)
			throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("students")) {
				SimpleRestRequestContext context = createRequest().ofType()
						.get().andFullUrl(link.getValue().href)
						.andAccessToken(token).executeAndReturnContext();

				if (context.getActualReturnedStatus() != 200) {
					throw new Exception(
							"Could not retrieve any students for section "
									+ getId());
				}

				return context
						.getResponseAsDeserializedObject(DaaltSectionStudentCollectionObject.class);
			}
		}

		throw new Exception("Could not retrieve any students for section "
				+ getId());

	}

	public Map<String, String> getAllData(IAccessToken token) throws Exception {
		Map<String, String> output = new TreeMap<String, String>();

		// append self
		output.put(getSelfLink(), new ObjectMapper().writeValueAsString(this));

		// Append all module data
		if (hasModules(token)) {

			output.put(getModulesLink(),
					new ObjectMapper().writeValueAsString(getModules(token)));

			for (ModuleObject module : getModules(token).getItems()) {
				for (Entry<String, String> entry : module.getAllData(token)
						.entrySet()) {
					output.put(entry.getKey(), entry.getValue());
				}
			}

		}

		// append all student data
		if (hasStudents(token)) {

			output.put(getStudentsLink(),
					new ObjectMapper().writeValueAsString(getsStudents(token)));

			for (SectionStudentObject student : getsStudents(token).getItems()) {
				for (Entry<String, String> entry : student.getAllData(token)
						.entrySet()) {
					output.put(entry.getKey(), entry.getValue());
				}
			}
		}

		return output;
		// Append all student data

	}

}
