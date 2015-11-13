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

public class SectionStudentObject implements ModelWithContract, ModelWithLinks,
		ModelComparable, ModelWithId {

	private SimpleRestRequestBuilder.IBuilderPostStart createRequest() {
		return new SimpleRestRequestBuilder.ExpressionBuilder();
	}

	public String courseSectionId;
	public String firstName;
	public String lastName;
	public String lastActivityDate;
	public Map<String, LinkObject> _links;
	public String platformId;
	public String studentId;
	public float studentPercent;
	public float studentPoints;
	public float studentPointsAttempted;
	public float studentPointsPossible;
	public float trending;
	
	

	@Override
	public boolean isContractValid() {
		String[] requiredFields = new String[] { courseSectionId, firstName,
				lastName, platformId, studentId };

		float[] floatValues = new float[] { studentPointsAttempted,
				studentPoints, studentPercent };

		boolean contractIsValid = true;

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

		for (LinkObject link : _links.values()) {
			if (!link.isContractValid()) {
				contractIsValid = false;
			}
		}

		/*
		 * if(!((studentPoints / studentPointsAttempted*100)== studentPercent))
		 * { contractIsValid=false; }
		 */

		return contractIsValid;

	}

	public Map<String, LinkObject> getLinks() {
		return _links;
	}

	public void setLinks(Map<String, LinkObject> links) {
		this._links = links;
	}

	@Override
	public List<Difference> getDifferenceBetweenObjects(
			ModelComparable compareObject) {
		List<Difference> differences = new LinkedList<Difference>();

		// If its not the same type of object stop
		SectionStudentObject actualObject = null;
		try {
			actualObject = (SectionStudentObject) compareObject;
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
			SectionStudentObject compareObject) {

		List<Difference> differences = new LinkedList<Difference>();

		if (!this.platformId.equals(compareObject.platformId)) {
			differences.add(new Difference("platformId does not match",
					this.platformId, compareObject.platformId));
		}

		if (!this.firstName.equals(compareObject.firstName)) {
			differences.add(new Difference("firstName does not match",
					this.firstName, compareObject.firstName));
		}

		if (!this.lastName.equals(compareObject.lastName)) {
			differences.add(new Difference("lastName does not match",
					this.lastName, compareObject.lastName));
		}

		if (!this.courseSectionId.equals(compareObject.courseSectionId)) {
			differences.add(new Difference("courseSectionId does not match",
					this.courseSectionId, compareObject.courseSectionId));
		}

		if (!this.studentId.equals(compareObject.studentId)) {
			differences.add(new Difference("studentId does not match",
					this.studentId, compareObject.studentId));
		}

		if (this.studentPercent != compareObject.studentPercent) {
			differences.add(new Difference("studentScore does not match",
					String.valueOf(this.studentPercent), String
							.valueOf(compareObject.studentPercent)));
		}

		return differences;
	}

	@Override
	public String getId() {
		return studentId;
	}

	public String getSelfLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("self")) {
				return link.getValue().href;
			}
		}

		throw new Exception("Could not find self link!");
	}

	public String getModulesLink() throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("modules")) {
				return link.getValue().href;
			}
		}

		throw new Exception("Could not find self link!");
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

	public DaaltStudentModuleCollectionObject getModules(IAccessToken token)
			throws Exception {
		for (Entry<String, LinkObject> link : getLinks().entrySet()) {
			if (link.getKey().equals("modules")) {
				SimpleRestRequestContext context = createRequest().ofType()
						.get().andFullUrl(link.getValue().href)
						.andAccessToken(token).executeAndReturnContext();

				if (context.getActualReturnedStatus() != 200) {
					throw new Exception(
							"Could not retrieve any student modules for section "
									+ getId());
				}

				return context
						.getResponseAsDeserializedObject(DaaltStudentModuleCollectionObject.class);
			}
		}
		throw new Exception(
				"Could not retrieve any student modules for section " + getId());

	}

	public Map<String, String> getAllData(IAccessToken token) throws Exception {
		Map<String, String> output = new TreeMap<String, String>();

		output.put(getSelfLink(), new ObjectMapper().writeValueAsString(this));

		// Append allmodule data
		if (hasModules(token)) {

			output.put(getModulesLink(),
					new ObjectMapper().writeValueAsString(getModules(token)));

			for (StudentModuleObject module : getModules(token).getItems()) {
				for (Entry<String, String> entry : module.getAllData(token)
						.entrySet()) {
					output.put(entry.getKey(), entry.getValue());
				}
			}

		}
		return output;

	}

}
