package com.pearson.test.daalt.dataservice.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.pearson.qa.apex.dataobjects.EnvironmentType;
import com.pearson.qa.apex.dataobjects.UserObject;
import com.pearson.qa.apex.dataobjects.UserType;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.response.model.LearningModuleStudentObject;
import com.pearson.test.daalt.dataservice.validation.endpoint.SectionToModuleToStudentsAll;


/**
 * endpoint 1.12
 */
public class SectionToModuleToStudentsValidationEngine extends DaaltDataServiceValidationEngine {
	public List<Validation> getValidations(TestData data) throws InvalidTestDataException, JsonGenerationException, JsonMappingException, IOException {
		List<Validation> validationList = new ArrayList<>();
		String baseUrl = TestEngine.getInstance().getBaseUrl();
		StringBuilder route = new StringBuilder();
		
		for (CourseSection courseSection : data.getAllCourseSections()) {
			UserObject instructorUser = new UserObject(courseSection.getInstructor().getUserName(), 
					courseSection.getInstructor().getPassword(), 
					UserType.Professor, EnvironmentType.Staging);
			instructorUser.setId(courseSection.getInstructor().getPersonId());

			for (Assignment assignment : courseSection.getAssignments()) {
				route = new StringBuilder();
				route.append(baseUrl).append("/platforms/").append(platformCode)
					.append("/sections/").append(courseSection.getId())
					.append("/modules/").append(assignment.getId())
					.append("/students");
				if (!courseSection.getEnrolledStudents().isEmpty()) {
					UserObject studentUser = null;
					LearningModuleStudentObject[] expectedStudentCollection = new LearningModuleStudentObject[courseSection.getEnrolledStudents().size()];
					int itemCount = courseSection.getEnrolledStudents().size();
					int i=0;
					
					List<Student> listStud = courseSection.getEnrolledStudents();
					
					
					for (Student stud : listStud) {
						studentUser = new UserObject(stud.getUserName(), stud.getPassword(), 
								UserType.Student, EnvironmentType.Staging);
						studentUser.setId(stud.getPersonId());
						
						Float pointsPossible = assignment.getPointsPossible();
						Float pointsEarned = assignment.getPointsEarnedFinal(stud);
						long assessmentTimeSeconds = assignment.getAssessmentTime(stud);
						long learningTimeSeconds = assignment.getLearningTime(stud);
						LearningModuleStudentObject studObj = getStudentObject(stud, courseSection, assignment,
								pointsPossible, pointsEarned, assessmentTimeSeconds, learningTimeSeconds);
						expectedStudentCollection[i] = studObj;
						
						StringBuilder singleStudentRoute = new StringBuilder();
						singleStudentRoute.append(baseUrl).append("/platforms/").append(platformCode)
							.append("/sections/").append(courseSection.getId())
							.append("/modules/").append(assignment.getId())
							.append("/students");
							
						LearningModuleStudentObject[] thisStudentCollection = new LearningModuleStudentObject[1];
						thisStudentCollection[0] = studObj;
						validationList.add(new SectionToModuleToStudentsAll(data.getTestScenarioName(), instructorUser, 
								singleStudentRoute.toString(), /*expectedResponseCode*/ ResponseCode.OK.value, 
								thisStudentCollection, /*expectedItemLevelLinks*/  null, 
								/*offset*/ i, /*limit*/ 1, /*itemCount*/ itemCount));
						i++;
					}
					validationList.add(new SectionToModuleToStudentsAll(data.getTestScenarioName(), instructorUser, 
							route.toString(), /*expectedResponseCode*/ ResponseCode.OK.value, 
							expectedStudentCollection, /*expectedItemLevelLinks*/ null, 
							/*offset*/ 0, /*limit*/ itemCount+1, /*itemCount*/ itemCount));
					
					//one call that does not limit the returned collection (no offset or limit parameters)
					validationList.add(new SectionToModuleToStudentsAll(data.getTestScenarioName(), instructorUser, 
							route.toString(), ResponseCode.OK.value, 
							expectedStudentCollection, /*expectedEnvelopeLevelLinks*/ null, 
							/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
					
					if (studentUser != null) {
						//students are not authorized to call endpoint 1.12
						validationList.add(new SectionToModuleToStudentsAll(data.getTestScenarioName(), studentUser, 
								route.toString(), /*expectedResponseCode*/ ResponseCode.UNATHORIZED.value, expectedStudentCollection, 
								/*expectedEnvelopeLevelLinks*/ null, 
								/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
					}
				} else {
					validationList.add(new SectionToModuleToStudentsAll(data.getTestScenarioName(), instructorUser, 
							route.toString(), /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value, null, 
							/*expectedEnvelopeLevelLinks*/ null, 
							/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
				}
			}
			
			if (courseSection.getAssignments().isEmpty()) {
				route = new StringBuilder();
				route.append(baseUrl).append("/platforms/").append(platformCode)
					.append("/sections/").append(courseSection.getId())
					.append("/modules/").append("SQE-dummy-learning-module-id")
					.append("/students");
				
				validationList.add(new SectionToModuleToStudentsAll(data.getTestScenarioName(), instructorUser, 
						route.toString(), /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value, null, 
						/*expectedEnvelopeLevelLinks*/ null, 
						/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
			}
		}
		
		return validationList;
	}
	
	private LearningModuleStudentObject getStudentObject(User stud, 
			CourseSection courseSection, Assignment assignment,
			Float pointsPossible, Float pointsEarned, long assessmentTimeSeconds, long learningTimeSeconds) throws InvalidTestDataException {
		if (courseSection.getInstructor() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToStudents validations - courseSection.instructor is null");
		} else if (courseSection.getInstructor().getUserName() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToStudents validations - instructor.userName is null");
		} else if (courseSection.getInstructor().getPassword() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToStudents validations - instructor.password is null");
		} else if (courseSection.getInstructor().getPersonId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToStudents validations - instructor.personId is null");
		} else if (platformCode == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToStudents validations - platformId is null");
		} else if (courseSection.getId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToStudents validations - courseSectionId is null");
		} else if (assignment.getId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToStudents validations - learningModuleId is null");
		} else if (assignment.getSequenceNumber() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToStudents validations - learningModuleSequence is null");
		} else if (stud.getPersonId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToStudents validations - studentId is null");
		}
		
		LearningModuleStudentObject studObj = new LearningModuleStudentObject();
		studObj.platformId = platformCode;
		studObj.courseSectionId = courseSection.getId();
		studObj.learningModuleId = assignment.getId();
		studObj.learningModuleTitle = assignment.getTitle();
		studObj.learningModuleSequence = assignment.getSequenceNumber();
		studObj.studentId = stud.getPersonId();
		studObj.studentFirstName = stud.getGivenName();
		studObj.studentLastName = stud.getFamilyName();
		
		studObj.pointsPossible = pointsPossible;
		studObj.studentPoints = pointsEarned;
		studObj.studentPercent = (pointsPossible == 0) ? 0 : (pointsEarned/pointsPossible)*100;
		
		studObj.studentPracticePoints = assignment.getPracticePointsEarnedFinal(stud);
		studObj.practicePointsPossible = assignment.getPracticePointsPossible();
		
//		if (!assignment.hasCredit()) {
//			studObj.pointsPossible = null;
//			studObj.studentPoints = null;
//			studObj.studentPercent = null;
//		}
//		
//		if (!assignment.hasPractice()) {
//			studObj.practicePointsPossible = null;
//			studObj.studentPracticePoints = null;
//		}
		
		//TODO: next release there will probably be a new set of requirements for practice vs. credit
		if (studObj.pointsPossible == 0) {
			studObj.pointsPossible = 0f;
			studObj.studentPoints = null;
			studObj.studentPercent = 0f;
		}
		
		if (studObj.practicePointsPossible == 0) {
			studObj.practicePointsPossible = 0f;
			studObj.studentPracticePoints = 0f;
		}
		
		studObj.studentTrending = getTrending(courseSection, assignment, stud);
		studObj.timeSpentAssessment = formatTimeOnTask(assessmentTimeSeconds*1000);
		studObj.timeSpentLearning = formatTimeOnTask(learningTimeSeconds*1000);
		studObj.timeSpentTotal = formatTimeOnTask((assessmentTimeSeconds + learningTimeSeconds)*1000);
		return studObj;
	}

	private Float getTrending(CourseSection courseSection, Assignment assignment, User stud) { 
		float trend = 0;
		
		if(!assignment.isDueDatePassed() || !assignment.studentCompletedAssignment(stud)){
			return null;
		}
		
		
		List<Assignment> allAssignments = courseSection.getAssignments();
		int assignmentCount = allAssignments.size();
		int indexOfTargetAssignment = 0;
		for (int i=0; i < assignmentCount; i++) {
			if (allAssignments.get(i).getId().equals(assignment.getId())) {
				indexOfTargetAssignment = i;
				break;
			}
		}
		
		int maxIndex = indexOfTargetAssignment-1;
		Assignment maxAssignment = null;
		
		while ( maxIndex >= 0) {
			
			maxAssignment = allAssignments.get(maxIndex);
			if (maxAssignment.isDueDatePassed() && maxAssignment.studentCompletedAssignment(stud)) {
				break;
				
			}
			maxIndex--;
		}
		int count = 0;
		int index = maxIndex;
		List<Assignment> upToFivePreviousAssignments = new ArrayList<>();
		while (index >= 0 && upToFivePreviousAssignments.size() < 5) {
			if (allAssignments.get(index).isDueDatePassed() && allAssignments.get(index).studentCompletedAssignment(stud) && count <=5) {
				upToFivePreviousAssignments.add(allAssignments.get(index));
				count ++;
			}
			index--;
		}
			
		float totalPoints = 0;
		for (Assignment ass : upToFivePreviousAssignments) {
			totalPoints += ass.getPointsEarnedFinal(stud);
		}
		float avePrevisouPoints = upToFivePreviousAssignments.size() > 0 ? totalPoints / upToFivePreviousAssignments.size() : 0;
		
		trend = assignment.getPointsEarnedFinal(stud) - avePrevisouPoints;
		
		
		return trend;
	}
}