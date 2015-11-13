package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;
import org.testng.Reporter;

public class DaaltStudentLearningResourceItemCollectionObject extends
		DaaltCollectionObject implements ModelWithContract, ModelWithPagination {

	public EmbeddedStudentLearningResourceItemCollectionObject _embedded;

	public DaaltStudentLearningResourceItemCollectionObject() {

	}

	public List<ModelWithLinks> getItemsAsModelsWithLinks() {
		List<ModelWithLinks> newItems = new LinkedList<ModelWithLinks>();

		for (StudentLearningResourceItemObject item : getItems()) {
			newItems.add((ModelWithLinks) item);
		}
		return newItems;
	}

	public StudentLearningResourceItemObject[] getItems() {
		if (_embedded == null || _embedded.pageItems == null) {
			_embedded.pageItems = new StudentLearningResourceItemObject[] {};
		}
		return _embedded.pageItems;
	}

	@Override
	public boolean isContractValid() {

		return isOffsetItemCountAndLimitCorrect() && isContractValidForItems()
				&& isLinkContractValid();

	}

	public boolean hasItemByPlatformId(String expectedPlatformId) {
		for (StudentLearningResourceItemObject item : getItems()) {
			if (item.platformId.equals(expectedPlatformId)) {

				Reporter.log("The platform ID inside the JSON response is :"
						+ item.platformId);
				return true;
			}
		}

		return false;
	}

	public boolean hasItemByCourseSectionId(String expectedCourseSectionID) {
		for (StudentLearningResourceItemObject item : getItems()) {
			if (item.courseSectionId.equals(expectedCourseSectionID)) {
				Reporter.log("The Course Section ID inside the JSON response is:"
						+ item.courseSectionId);
				return true;
			}
		}

		return false;
	}

	public boolean hasItemByLearningModuleId(String expectedLearningModuleId) {
		for (StudentLearningResourceItemObject item : getItems()) {
			if (item.learningModuleId.equals(expectedLearningModuleId)) {

				Reporter.log("The Module ID inside the JSON response is :"
						+ item.learningModuleId);
				return true;
			}
		}

		return false;
	}

	public boolean hasItemByLearningResourceId(String expectedLearningResourceId) {
		for (StudentLearningResourceItemObject item : getItems()) {
			if (item.learningResourceId.equals(expectedLearningResourceId)) {

				Reporter.log("The Resource ID inside the JSON response is :"
						+ item.learningResourceId);
				return true;
			}
		}

		return false;
	}

	public boolean hasItemByStudentId(String expectedStudentId) {
		for (StudentLearningResourceItemObject item : getItems()) {
			if (item.studentId.equals(expectedStudentId)) {

				Reporter.log("The Student ID inside the JSON response is :"
						+ item.studentId);
				return true;
			}
		}

		return false;
	}

	public String getItemBodyTextByResourceId(String learningResourceId) {

		String itemBodyText = null;
		for (StudentLearningResourceItemObject item : getItems()) {
			if (item.learningResourceId.equals(learningResourceId)) {

				itemBodyText = String.valueOf(item.itemBodyText).trim();
				Reporter.log("The itemBodyText for the Resource ID : "
						+ learningResourceId + " is :" + itemBodyText);
			}
		}

		return itemBodyText;
	}

	public String getItemIdByResourceId(String learningResourceId) {

		String itemId = null;
		for (StudentLearningResourceItemObject item : getItems()) {
			if (item.learningResourceId.equals(learningResourceId)) {

				itemId = String.valueOf(item.itemId).trim();
				Reporter.log("The itemId for the Resource ID : "
						+ learningResourceId + " is :" + itemId);
			}
		}

		return itemId;
	}

	public String getItemSequenceByResourceId(String learningResourceId) {

		String itemSequence = null;
		for (StudentLearningResourceItemObject item : getItems()) {
			if (item.learningResourceId.equals(learningResourceId)) {

				itemSequence = String.valueOf(item.itemSequence).trim();
				Reporter.log("The itemSequence for the Resource ID : "
						+ learningResourceId + " is :" + itemSequence);
			}
		}

		return itemSequence;
	}

	public String getStudentScoreByResourceId(String learningResourceId) {

		String studentScore = null;
		for (StudentLearningResourceItemObject item : getItems()) {
			if (item.learningResourceId.equals(learningResourceId)) {

				studentScore = String.valueOf(item.studentPercent).trim();
				Reporter.log("The Student Score for the Resource ID : "
						+ learningResourceId + " is :" + studentScore);
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
		for (StudentLearningResourceItemObject item : getItems()) {
			if (!item.isContractValid()) {
				Reporter.log("The contract for one of the learning resource objects in the learning resource collection is invalid");

				result = false;
			}
		}

		return result;

	}

}
