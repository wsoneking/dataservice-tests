package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;
import org.testng.Reporter;

public class DaaltLearningResourceCollectionObject extends
		DaaltCollectionObject implements ModelWithContract, ModelWithLinks,
		ModelWithPagination {

	public EmbeddedLearningResourceCollectionObject _embedded;
	public String qualifiedChildClassName = "com.pearson.test.daalt.models.LearningResourceObject";
	public List<String> secondaryId = null;

	public DaaltLearningResourceCollectionObject() {

	}

	public LearningResourceObject[] getItems() {
		if (_embedded == null || _embedded.pageItems == null) {
			return new LearningResourceObject[] {};
		}

		return _embedded.pageItems;
	}

	public List<ModelWithLinks> getItemsAsModelsWithLinks() {
		List<ModelWithLinks> newItems = new LinkedList<ModelWithLinks>();

		for (LearningResourceObject item : getItems()) {
			newItems.add((ModelWithLinks) item);
		}
		return newItems;
	}

	@Override
	public boolean isContractValid() {

		return isOffsetItemCountAndLimitCorrect() && isContractValidForItems()
				&& isOffsetItemCountAndLimitCorrect();

	}

	public String getClassAvgByResourceID(String learningResourceId,
			String learningResourceType) {

		String classAverage = "";

		for (LearningResourceObject item : getItems()) {
			if (item.learningResourceId.equals(learningResourceId)) {

				if (item.learningResourceType.contains(learningResourceType)) {
					classAverage = String.valueOf(item.classAvgPercent).trim();

					Reporter.log("The Class Average for '"
							+ learningResourceType
							+ "' type Learning resource  : "
							+ learningResourceId + " is :" + classAverage);
				}
			}
		}

		return classAverage;
	}

	public String getLearningResourceTitleByResourceID(
			String learningResourceId, String learningResourceType) {

		String learningResourceTitle = null;

		for (LearningResourceObject item : getItems()) {
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

	public String getlearningResourceTypeByResourceID(String learningResourceId) {

		String learningResourceType = null;

		for (LearningResourceObject item : getItems()) {
			if (item.learningResourceId.equals(learningResourceId)) {
				learningResourceType = String
						.valueOf(item.learningResourceType);
				Reporter.log("The Resource Type  for the resource ID  : "
						+ learningResourceId + " is :" + learningResourceType);
			}
		}

		return learningResourceType;
	}

	public String getAverageTimeSpentByResourceID(String learningResourceId,
			String learningResourceType) {

		String avgTimeSpent = "";

		for (LearningResourceObject item : getItems())

		{

			// Reporter.log("The resourceID is :"+learningResourceId);

			if (item.learningResourceId.equals(learningResourceId))

			{
				/*
				 * Reporter.log("The user is inside first IF");
				 * Reporter.log("The returned resourcetype is : "
				 * +item.learningResourceType);
				 * Reporter.log("The user passed resourceType is : "
				 * +learningResourceType);
				 */
				if (item.learningResourceType.contains(learningResourceType))

				{
					Reporter.log("The user is inside first IF");
					avgTimeSpent = (String.valueOf(item.avgTimeSpentTotal)).trim();
					Reporter.log("The Average time spent for '"
							+ learningResourceType + "' type resource : "
							+ learningResourceId + " is :" + avgTimeSpent);
					break;
				}
			}
		}
		return avgTimeSpent;
	}

	public boolean hasPlatformByResource(String expectedPlatformId,
			String learningResourceType) {
		boolean b = false;
		for (LearningResourceObject item : getItems()) {

			// Reporter.log("The platform ID for this object is "+item.platformId);

			if (item.platformId.equals(expectedPlatformId)) {

				// Reporter.log("Inside the ResourceObject");

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

	public boolean hasSectionByResource(String expectedSectionId,
			String learningResourceType) {
		boolean b = false;
		for (LearningResourceObject item : getItems()) {
			if (item.courseSectionId.equals(expectedSectionId)) {
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

	public boolean hasModuleByResource(String expectedModuleId,
			String learningResourceType) {
		boolean b = false;
		for (LearningResourceObject item : getItems()) {
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

	public boolean hasItemByResourceID(String expectedResourceId,
			String learningResourceType) {
		boolean b = false;
		for (LearningResourceObject item : getItems()) {
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
		for (LearningResourceObject item : getItems()) {
			if (!item.isContractValid()) {
				Reporter.log("The contract for one of the learning resource objects in the learning resource collection is invalid");

				result = false;
			}
		}

		return result;

	}

	public LearningResourceObject getResourceById(String resourceId) {
		for (LearningResourceObject resource : getItems()) {
			if (resource.getId().equals(resourceId)) {
				return resource;
			}
		}

		return null;
	}
}
