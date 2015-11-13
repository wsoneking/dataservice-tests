package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.testng.Reporter;

public class DaaltLearningModuleStudentCollectionObject extends
		DaaltCollectionObject implements ModelWithContract, ModelWithPagination {


	public EmbeddedLearningModuleStudentCollectionObject _embedded;

	public LearningModuleStudentObject[] getItems() {
		if (_embedded == null || _embedded.pageItems == null) {
			return new LearningModuleStudentObject[] {};
		}
		return _embedded.pageItems;
	}

	public List<ModelWithLinks> getItemsAsModelsWithLinks() {
		List<ModelWithLinks> newItems = new LinkedList<ModelWithLinks>();

		for (LearningModuleStudentObject item : getItems()) {
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
		for (LearningModuleStudentObject item : getItems()) {
			if (item.platformId.equals(expectedPlatformId)) {

				Reporter.log("The platform ID inside the JSON response is :"
						+ item.platformId);
				return true;
			}
		}

		return false;
	}

	public boolean hasItemByCourseSectionID(String expectedSectionId) {
		for (LearningModuleStudentObject item : getItems()) {
			if (item.courseSectionId.equals(expectedSectionId)) {
				Reporter.log("The Section ID inside the JSON response is:"
						+ item.courseSectionId);
				return true;
			}
		}

		return false;
	}

	public boolean hasItemByLearningModuleId(String expectedLearningModuleId) {
		for (LearningModuleStudentObject item : getItems()) {
			if (item.learningModuleId.equals(expectedLearningModuleId)) {

				Reporter.log("The Module ID inside the JSON response is :"
						+ item.learningModuleId);
				return true;
			}
		}

		return false;
	}

	public boolean hasItemByStudentId(String expectedStudentId) {
		for (LearningModuleStudentObject item : getItems()) {
			if (item.studentId.equals(expectedStudentId)) {

				Reporter.log("The Student ID inside the JSON response is :"
						+ item.studentId);
				return true;
			}
		}

		return false;

	}

	public String getLearningModuleSequenceByModuleId(String learningModuleId) {

		String learningModuleSequence = null;
		for (LearningModuleStudentObject item : getItems()) {
			if (item.learningModuleId.equals(learningModuleId)) {

				learningModuleSequence = String.valueOf(
						item.learningModuleSequence).trim();
				Reporter.log("The learningModuleSequence for the Module ID : "
						+ learningModuleId + " is :" + learningModuleSequence);
			}
		}

		return learningModuleSequence;
	}

	public String getLearningModuleTitleByModuleId(String learningModuleId) {

		String learningModuleTitle = null;
		for (LearningModuleStudentObject item : getItems()) {
			if (item.learningModuleId.equals(learningModuleId)) {

				learningModuleTitle = String.valueOf(item.learningModuleTitle)
						.trim();
				Reporter.log("The learningModuleTitle for the Module ID : "
						+ learningModuleId + " is :" + learningModuleTitle);
			}
		}

		return learningModuleTitle;
	}

	public String getStudentScoreByModuleId(String learningModuleId) {

		String studentScore = null;
		for (LearningModuleStudentObject item : getItems()) {
			if (item.learningModuleId.equals(learningModuleId)) {

				studentScore = String.valueOf(item.studentPercent).trim();
				Reporter.log("The studentScore for the Module ID : "
						+ learningModuleId + " is :" + studentScore);
			}
		}

		return studentScore;
	}

	public String getTimeSpentAssessmentByModuleId(String learningModuleId) {

		String timeSpentAssessment = null;
		for (LearningModuleStudentObject item : getItems()) {
			if (item.learningModuleId.equals(learningModuleId)) {

				timeSpentAssessment = String.valueOf(item.timeSpentAssessment)
						.trim();
				Reporter.log("The timeSpentAssessment for the Module ID : "
						+ learningModuleId + " is :" + timeSpentAssessment);
			}
		}

		return timeSpentAssessment;
	}

	public String getTimeSpentLearningByModuleId(String learningModuleId) {

		String timeSpentLearning = null;
		for (LearningModuleStudentObject item : getItems()) {
			if (item.learningModuleId.equals(learningModuleId)) {

				timeSpentLearning = String.valueOf(item.timeSpentLearning)
						.trim();
				Reporter.log("The timeSpentLearning for the Module ID : "
						+ learningModuleId + " is :" + timeSpentLearning);
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
		for (LearningModuleStudentObject item : getItems()) {
			if (!item.isContractValid()) {
				Reporter.log("The contract for one of the learning resource objects in the learning resource collection is invalid");

				result = false;
			}
		}

		return result;

	}
	
	@JsonIgnore
	public LearningModuleStudentObject getStudentWithId(String studentId) {

		LearningModuleStudentObject returnStudent = null;
		for (LearningModuleStudentObject student : getItems()) {
			if (student.getId().equals(studentId)) {
				returnStudent = student;
			}
		}

		return returnStudent;
	}

}