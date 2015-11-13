package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.testng.Reporter;

public class DaaltStudentLearningResourceCollectionObject extends
		DaaltCollectionObject implements ModelWithContract, ModelWithLinks,
		ModelWithPagination {

	public EmbeddedStudentLearningResourceCollectionObject _embedded;

	public DaaltStudentLearningResourceCollectionObject() {

	}

	public StudentLearningResourceObject[] getItems() {

		if (_embedded == null || _embedded.pageItems == null) {
			_embedded.pageItems = new StudentLearningResourceObject[] {};
		}
		return _embedded.pageItems;
	}

	public List<ModelWithLinks> getItemsAsModelsWithLinks() {
		List<ModelWithLinks> newItems = new LinkedList<ModelWithLinks>();

		for (StudentLearningResourceObject item : getItems()) {
			newItems.add((ModelWithLinks) item);
		}
		return newItems;
	}

	@Override
	@JsonIgnore
	public boolean isContractValid() {

		return isOffsetItemCountAndLimitCorrect() && isContractValidForItems()
				&& isLinkContractValid();

	}

	public boolean hasItemByCourseSection(String expectedCourseSectionId,
			String learningResourceType) {
		boolean b = false;
		for (StudentLearningResourceObject item : getItems()) {
			if (item.courseSectionId.equals(expectedCourseSectionId)) {
				if (item.learningResourceType.contains(learningResourceType)) {
					b = true;
					Reporter.log("The Section ID inside the JSON response for '"
							+ learningResourceType
							+ "' type resource is:"
							+ item.courseSectionId);
					return b;

				}
			}
		}

		return false;
	}

	public boolean hasItemByPlatform(String expectedPlatformId,
			String learningResourceType) {
		boolean b = false;
		for (StudentLearningResourceObject item : getItems()) {
			if (item.platformId.equals(expectedPlatformId)) {
				if (item.learningResourceType.contains(learningResourceType)) {
					b = true;
					Reporter.log("The Platform ID inside the JSON response for '"
							+ learningResourceType
							+ "' type resource is:"
							+ item.platformId);
					return b;

				}
			}
		}

		return false;
	}

	public boolean hasItemByResourceId(String expectedResourceId,
			String learningResourceType) {
		boolean b = false;
		for (StudentLearningResourceObject item : getItems()) {
			if (item.learningResourceId.equals(expectedResourceId)) {
				if (item.learningResourceType.contains(learningResourceType)) {
					b = true;
					Reporter.log("The Resource ID inside the JSON response for '"
							+ learningResourceType
							+ "' type resource is:"
							+ item.learningResourceId);
					return b;

				}
			}
		}

		return false;
	}

	public boolean hasItemByLearningModuleId(String expectedModuleId,
			String learningResourceType) {
		boolean b = false;
		for (StudentLearningResourceObject item : getItems()) {
			if (item.learningModuleId.equals(expectedModuleId)) {
				if (item.learningResourceType.contains(learningResourceType)) {
					b = true;
					Reporter.log("The module ID inside the JSON response for '"
							+ learningResourceType + "' type resource is:"
							+ item.learningModuleId);
					return b;

				}
			}
		}

		return false;
	}

	public boolean hasItemByStudentId(String expectedStudentId,
			String learningResourceType) {
		boolean b = false;
		for (StudentLearningResourceObject item : getItems()) {
			if (item.studentId.equals(expectedStudentId)) {
				if (item.learningResourceType.contains(learningResourceType)) {
					b = true;
					Reporter.log("The Student ID inside the JSON response for '"
							+ learningResourceType
							+ "' type resource is:"
							+ item.studentId);
					return b;

				}
			}
		}

		return false;
	}

	public String getlearningResourceTypeByResourceID(String learningResourceId) {

		String learningResourceType = null;

		for (StudentLearningResourceObject item : getItems()) {
			if (item.learningResourceId.equals(learningResourceId)) {
				learningResourceType = String
						.valueOf(item.learningResourceType);
				Reporter.log("The Resource Type  for the resource ID  : "
						+ learningResourceId + " is :" + learningResourceType);
			}
		}

		return learningResourceType;
	}

	public String getLearningResourceTitleByResourceID(
			String learningResourceId, String learningResourceType) {

		String learningResourceTitle = null;

		for (StudentLearningResourceObject item : getItems()) {
			if (item.learningResourceId.equals(learningResourceId)) {

				if (item.learningResourceType.contains(learningResourceType)) {
					learningResourceTitle = String
							.valueOf(item.learningResourceTitle.trim());

					Reporter.log("The Resource Title for '"
							+ learningResourceType + "' type resource  : "
							+ learningResourceId + " is :"
							+ learningResourceTitle);
				}

			}
		}

		return learningResourceTitle;
	}

	public String getStudentScoreByResourceID(String learningResourceId,
			String learningResourceType) {

		String studentScore = null;

		for (StudentLearningResourceObject item : getItems()) {
			if (item.learningResourceId.equals(learningResourceId)) {

				if (item.learningResourceType.contains(learningResourceType)) {
					studentScore = String.valueOf(item.studentPercent);

					Reporter.log("The Student Score for '"
							+ learningResourceType + "' type resource  : "
							+ learningResourceId + " is :" + studentScore);
				}

			}
		}

		return studentScore;
	}

	// Contract functions
	public boolean isOffsetItemCountAndLimitCorrect() {

		boolean result = true;

		// offset and limit and item count can't be negative
		if (offset < 0 || limit < 0 || itemCount < 0) {
			result = false;
		}

		// number of items should never exceed limit
		if (getItems().length > limit) {
			result = false;
		}

		// number of items should never exceed item count
		if (getItems().length > itemCount) {
			result = false;
		}

		return result;

	}

	public boolean isContractValidForItems() {
		boolean result = true;
		for (StudentLearningResourceObject item : getItems()) {
			if (!item.isContractValid()) {
				Reporter.log("The contract for one of the learning resource objects in the learning resource collection is invalid");

				result = false;
			}
		}

		return result;

	}

	public StudentLearningResourceObject getResourceById(String resourceId) {

		for (StudentLearningResourceObject resource : getItems()) {
			if (resource.getId().equals(resourceId)) {
				return resource;
			}
		}
		return null;

	}

}
