package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.testng.Reporter;

public class DaaltCourseSectionStudentCollectionObject extends
		DaaltCollectionObject implements ModelWithContract, ModelWithPagination {


	public EmbeddedCourseSectionStudentCollectionObject _embedded;

	public CourseSectionStudentObject[] getItems() {
		if (_embedded == null || _embedded.pageItems == null) {
			return new CourseSectionStudentObject[] {};
		}
		return _embedded.pageItems;
	}

	public List<ModelWithLinks> getItemsAsModelsWithLinks() {
		List<ModelWithLinks> newItems = new LinkedList<ModelWithLinks>();

		for (CourseSectionStudentObject item : getItems()) {
			newItems.add((ModelWithLinks) item);
		}
		return newItems;
	}

	@Override
	public boolean isContractValid() {

		return isOffsetItemCountAndLimitCorrect() && isContractValidForItems()
				&& isOffsetItemCountAndLimitCorrect();

	}

	public boolean hasItembyPlatformID(String expectedPlatformId) {
		for (CourseSectionStudentObject item : getItems()) {
			if (item.platformId.equals(expectedPlatformId)) {

				Reporter.log("The platform ID inside the JSON response is :"
						+ item.platformId);
				return true;
			}
		}

		return false;
	}

	public boolean hasItemByCourseSectionID(String expectedSectionId) {
		for (CourseSectionStudentObject item : getItems()) {
			if (item.courseSectionId.equals(expectedSectionId)) {
				Reporter.log("The Section ID inside the JSON response is:"
						+ item.courseSectionId);
				return true;
			}
		}

		return false;
	}


	public boolean hasItemByStudentId(String expectedStudentId) {
		for (CourseSectionStudentObject item : getItems()) {
			if (item.studentId.equals(expectedStudentId)) {

				Reporter.log("The Student ID inside the JSON response is :"
						+ item.studentId);
				return true;
			}
		}

		return false;

	}



	public String getTimeSpentLearningBySectionId(String courseSectionId) {

		String timeSpentLearning = null;
		for (CourseSectionStudentObject item : getItems()) {
			if (item.courseSectionId.equals(courseSectionId)) {

				timeSpentLearning = String.valueOf(item.timeSpentLearning)
						.trim();
				Reporter.log("The timeSpentLearning for the Section ID : "
						+ courseSectionId + " is :" + timeSpentLearning);
			}
		}

		return timeSpentLearning;
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
		for (CourseSectionStudentObject item : getItems()) {
			if (!item.isContractValid()) {
				Reporter.log("The contract for one of the learning resource objects in the learning resource collection is invalid");

				result = false;
			}
		}

		return result;

	}
	
	@JsonIgnore
	public CourseSectionStudentObject getStudentWithId(String studentId) {

		CourseSectionStudentObject returnStudent = null;
		for (CourseSectionStudentObject student : getItems()) {
			if (student.getId().equals(studentId)) {
				returnStudent = student;
			}
		}

		return returnStudent;
	}

}