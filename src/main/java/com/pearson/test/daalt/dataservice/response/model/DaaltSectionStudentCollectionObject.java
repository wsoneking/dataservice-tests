package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.testng.Reporter;

public class DaaltSectionStudentCollectionObject extends DaaltCollectionObject
		implements ModelWithContract, ModelWithPagination {

	public EmbeddedSectionStudentCollectionObject _embedded;

	
	public DaaltSectionStudentCollectionObject() {

	}

	public SectionStudentObject[] getItems() {
		if (_embedded == null || _embedded.pageItems == null) {
			return new SectionStudentObject[] {};
		}
		return _embedded.pageItems;
	}

	public List<ModelWithLinks> getItemsAsModelsWithLinks() {
		List<ModelWithLinks> newItems = new LinkedList<ModelWithLinks>();

		for (SectionStudentObject item : getItems()) {
			newItems.add((ModelWithLinks) item);
		}
		return newItems;
	}

	@Override
	public boolean isContractValid() {

		return isOffsetItemCountAndLimitCorrect() && isContractValidForItems()
				&& isOffsetItemCountAndLimitCorrect();

	}

	public boolean hasItemByPlatform(String expectedPlatformId) {
		for (SectionStudentObject item : getItems()) {
			if (item.platformId.equals(expectedPlatformId)) {

				Reporter.log("The platform ID inside the JSON response is :"
						+ item.platformId);
				return true;
			}
		}

		return false;
	}

	public boolean hasItemByCourseSection(String expectedCourseSectionId) {
		for (SectionStudentObject item : getItems()) {
			if (item.courseSectionId.equals(expectedCourseSectionId)) {

				Reporter.log("The Course Section ID inside the JSON response is :"
						+ item.courseSectionId);
				return true;
			}
		}

		return false;
	}

	public boolean hasItemByStudentId(String expectedStudentId) {
		for (SectionStudentObject item : getItems()) {
			if (item.studentId.equals(expectedStudentId)) {

				Reporter.log("The Student ID inside the JSON response is :"
						+ item.studentId);
				return true;
			}
		}

		return false;
	}

	public String getFirstNameByStudentID(String expectedStudentID) {
		String firstName = null;
		for (SectionStudentObject item : getItems()) {

			if (item.studentId.equals(expectedStudentID)) {

				firstName = item.firstName.trim();
				Reporter.log("The First Name for the Student ID : "
						+ expectedStudentID + " is :" + firstName);
			}
		}

		return firstName;
	}

	public String getLastNameByStudentID(String expectedStudentID) {
		String lastName = null;
		for (SectionStudentObject item : getItems()) {

			if (item.studentId.equals(expectedStudentID)) {

				lastName = item.lastName.trim();
				Reporter.log("The Last Name for the Student ID : "
						+ expectedStudentID + " is :" + lastName);
			}
		}

		return lastName;
	}

	public String getStudentScoreByStudentID(String expectedStudentID) {
		String studentScore = null;
		for (SectionStudentObject item : getItems()) {

			if (item.studentId.equals(expectedStudentID)) {

				studentScore = String.valueOf(item.studentPercent);
				Reporter.log("The Student Score for the Student ID : "
						+ expectedStudentID + " is :" + studentScore);
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
		for (SectionStudentObject item : getItems()) {
			if (!item.isContractValid()) {
				Reporter.log("The contract for one of the learning resource objects in the learning resource collection is invalid");

				result = false;
			}
		}

		return result;

	}

	@JsonIgnore
	public SectionStudentObject getStudentWithId(String studentId) {

		SectionStudentObject returnStudent = null;

		if (studentId != null) {
			for (SectionStudentObject student : getItems()) {
				if (studentId.equals(student.getId())) {
					returnStudent = student;
				}
			}
		}

		return returnStudent;

	}

}
