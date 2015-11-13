package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.testng.Reporter;

public class DaaltLearningResourceItemCollectionObject extends
		DaaltCollectionObject implements ModelWithContract, ModelWithPagination {

	public EmbeddedLearningResourceItemCollectionObject _embedded;

	public DaaltLearningResourceItemCollectionObject() {

	}

	public List<ModelWithLinks> getItemsAsModelsWithLinks() {
		List<ModelWithLinks> newItems = new LinkedList<ModelWithLinks>();

		for (LearningResourceItemObject item : getItems()) {
			newItems.add((ModelWithLinks) item);
		}
		return newItems;
	}

	@JsonIgnore
	public LearningResourceItemObject[] getItems() {
		if (_embedded == null || _embedded.pageItems == null) {
			return new LearningResourceItemObject[] {};
		}
		return _embedded.pageItems;
	}

	@Override
	public boolean isContractValid() {
		return isOffsetItemCountAndLimitCorrect() && isContractValidForItems();
	}

	public boolean hasItemByPlatformId(String expectedPlatformId) {
		for (LearningResourceItemObject item : getItems()) {
			if (item.platformId.equals(expectedPlatformId)) {

				Reporter.log("The platform ID inside the JSON response is :"
						+ item.platformId);
				return true;
			}
		}

		return false;
	}

	public boolean hasItemByCourseSectionId(String expectedCourseSectionID) {
		for (LearningResourceItemObject item : getItems()) {
			if (item.courseSectionId.equals(expectedCourseSectionID)) {
				Reporter.log("The Course Section ID inside the JSON response is:"
						+ item.courseSectionId);
				return true;
			}
		}

		return false;
	}

	public boolean hasItemByLearningModuleId(String expectedLearningModuleId) {
		for (LearningResourceItemObject item : getItems()) {
			if (item.learningModuleId.equals(expectedLearningModuleId)) {

				Reporter.log("The Module ID inside the JSON response is :"
						+ item.learningModuleId);
				return true;
			}
		}

		return false;
	}

	public boolean hasItemByLearningResourceId(String expectedLearningResourceId) {
		for (LearningResourceItemObject item : getItems()) {
			if (item.learningResourceId.equals(expectedLearningResourceId)) {

				Reporter.log("The Resource ID inside the JSON response is :"
						+ item.learningResourceId);
				return true;
			}
		}

		return false;
	}

	@JsonIgnore
	public String getItemBodyTextByResourceId(String learningResourceId) {

		String itemBodyText = null;
		for (LearningResourceItemObject item : getItems()) {
			if (item.learningResourceId.equals(learningResourceId)) {

				itemBodyText = String.valueOf(item.questionText).trim();
				Reporter.log("The itemBodyText for the Resource ID : "
						+ learningResourceId + " is :" + itemBodyText);
			}
		}

		return itemBodyText;
	}

	@JsonIgnore
	public String getItemIdByResourceId(String learningResourceId) {

		String itemId = null;
		for (LearningResourceItemObject item : getItems()) {
			if (item.learningResourceId.equals(learningResourceId)) {

				itemId = String.valueOf(item.itemId).trim();
				Reporter.log("The itemId for the Resource ID : "
						+ learningResourceId + " is :" + itemId);
			}
		}

		return itemId;
	}
	
	//returns item by specified itemid
	@JsonIgnore
	public LearningResourceItemObject getItemByItemId(String itemId) {

		LearningResourceItemObject item = null;
		for (LearningResourceItemObject itemInList : getItems()) {
			if (itemInList.itemId.equals(itemId)) {

				item = itemInList;
			}
		}
		return item;
	}

	@JsonIgnore
	public String getItemSequenceByResourceId(String learningResourceId) {

		String itemSequence = null;
		for (LearningResourceItemObject item : getItems()) {
			if (item.learningResourceId.equals(learningResourceId)) {

				itemSequence = String.valueOf(item.itemSequence).trim();
				Reporter.log("The itemSequence for the Resource ID : "
						+ learningResourceId + " is :" + itemSequence);
			}
		}

		return itemSequence;
	}

	// Contract functions
	@JsonIgnore
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

	@JsonIgnore
	public boolean isContractValidForItems() {
		boolean result = true;
		for (LearningResourceItemObject item : getItems()) {
			if (!item.isContractValid()) {
				Reporter.log("The contract for one of the learning resource objects in the learning resource collection is invalid");

				result = false;
			}
		}

		return result;

	}

}
