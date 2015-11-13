package com.pearson.test.daalt.dataservice.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import com.pearson.test.daalt.dataservice.response.model.StudentModuleObject;
import com.pearson.test.daalt.dataservice.validation.endpoint.SectionToStudentToModulesAll;

/**
 * endpoint 2.3
 */
public class SectionToStudentToModulesValidationEngine extends DaaltDataServiceValidationEngine {
	private List<Validation> validationList;
	
	public List<Validation> getValidations(TestData data) throws InvalidTestDataException, JsonGenerationException, JsonMappingException, IOException {
		validationList = new ArrayList<>();
		String baseUrl = TestEngine.getInstance().getBaseUrl();
		StringBuilder route = new StringBuilder();
		
		for (CourseSection courseSection : data.getAllCourseSections()) {
			UserObject instructorUser = new UserObject(courseSection.getInstructor().getUserName(), 
					courseSection.getInstructor().getPassword(), 
					UserType.Professor, EnvironmentType.Staging);
			instructorUser.setId(courseSection.getInstructor().getPersonId());

			for (Student stud : courseSection.getEnrolledStudents()) {
				List<StudentModuleObject> studentModuleList = new ArrayList<>();
				for (Assignment assignment : courseSection.getAssignments()) {
					StudentModuleObject module = getModuleObject(stud, courseSection, assignment);
					studentModuleList.add(module);
				}
				route = new StringBuilder();
				route.append(baseUrl).append("/platforms/").append(platformCode)
				.append("/sections/").append(courseSection.getId())
				.append("/students/").append(stud.getPersonId())
				.append("/modules");
				
				addValidations(data.getTestScenarioName(), instructorUser, 
						route.toString(), studentModuleList, 
						stud, courseSection.getEnrolledStudents());
			}
		}
		
		return validationList;
	}
	
	private StudentModuleObject getModuleObject(Student stud, CourseSection courseSection, 
			Assignment assignment) throws InvalidTestDataException {
		if (courseSection.getInstructor() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModules validations - courseSection.instructor is null");
		} else if (courseSection.getInstructor().getUserName() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModules validations - instructor.userName is null");
		} else if (courseSection.getInstructor().getPassword() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModules validations - instructor.password is null");
		} else if (courseSection.getInstructor().getPersonId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModules validations - instructor.personId is null");
		} else if (platformCode == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModules validations - platformId is null");
		} else if (courseSection.getId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModules validations - courseSectionId is null");
		} else if (assignment.getId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModules validations - assignmentId is null");
		} else if (stud.getPersonId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModules validations - student.personId is null");
		}
		
		StudentModuleObject moduleObj = new StudentModuleObject();
		moduleObj.platformId = platformCode;
		moduleObj.courseSectionId = courseSection.getId();
		moduleObj.studentId = stud.getPersonId();
		moduleObj.learningModuleId = assignment.getId();
		moduleObj.studentFirstName = stud.getGivenName();
		moduleObj.studentLastName = stud.getFamilyName();
		moduleObj.learningModuleTitle = assignment.getTitle();
		moduleObj.learningModuleSequence = assignment.getSequenceNumber();
		float pointsPossible = assignment.getPointsPossible();
		float pointsEarned = assignment.getPointsEarnedFinal(stud);
		
		moduleObj.pointsPossible = pointsPossible;
		moduleObj.studentPoints = pointsEarned;
		moduleObj.studentPercent = pointsPossible > 0 ? (pointsEarned/pointsPossible)*100 : 0;
		
		moduleObj.practicePointsPossible = assignment.getPracticePointsPossible();
		moduleObj.studentPracticePoints = assignment.getPracticePointsEarnedFinal(stud);
		
		//  change the value to null  5/6/2015
//		if (!assignment.hasCredit()) {
//			moduleObj.pointsPossible = null;
//			moduleObj.studentPoints = null;
//			moduleObj.studentPercent = null;
//		}
//		
//		if (!assignment.hasPractice()) {
//			moduleObj.practicePointsPossible = null;
//			moduleObj.studentPracticePoints = null;
//		}
		// end 5/6/2015
		
		//TODO: next release there will probably be a new set of requirements for practice vs. credit
		if (moduleObj.pointsPossible == 0) {
			moduleObj.pointsPossible = null;
			moduleObj.studentPoints = null;
			moduleObj.studentPercent = 0f;
		}
		
		if (moduleObj.practicePointsPossible == 0) {
			moduleObj.practicePointsPossible = 0f;
			moduleObj.studentPracticePoints = 0f;
		}
		
		long assessmentTimeSeconds = assignment.getAssessmentTime(stud);
		long learningTimeSeconds = assignment.getLearningTime(stud);
		moduleObj.timeSpentAssessment = formatTimeOnTask(assessmentTimeSeconds*1000);
		moduleObj.timeSpentLearning = formatTimeOnTask(learningTimeSeconds*1000);
		moduleObj.timeSpentTotal = formatTimeOnTask((assessmentTimeSeconds + learningTimeSeconds)*1000);
		return moduleObj;
	}
	
	private void addValidations(String scenarioName, UserObject instructorUser, 
			String route, List<StudentModuleObject> moduleList, Student stud, List<Student> studList) 
					throws JsonGenerationException, JsonMappingException, IOException {
		
		int expectedResponseCode = ResponseCode.OK.value;
		if (moduleList.isEmpty()) {
			expectedResponseCode = ResponseCode.NOT_FOUND.value;
		}
		
		StudentModuleObject[] moduleArray = null;
		Collections.sort(moduleList);
		moduleArray = getArrayFromList(moduleList);
		int moduleCount = moduleArray == null ? 0 : moduleArray.length;
		int i = 0;
		//one call that limits the returned collection to only this learningResource
//		for(StudentModuleObject moduleObject : moduleArray) {								// in BasicAssignment, the module sequence number has defect. 
//			StudentModuleObject[] thisModulecollection = new StudentModuleObject[1];
//			thisModulecollection[0] = moduleObject;
//			validationList.add(new SectionToStudentToModulesAll(scenarioName, instructorUser, 
//					route, /*expectedResponseCode*/ ResponseCode.OK.value, thisModulecollection,
//					/*expectedItemLevelLinks*/ null, 
//					/*offset*/ i, /*limit*/ 1, /*itemCount*/ moduleCount));
//			i++;
//		}
		
		//instructor calls endpoint 2.3 to view this student's data - permitted
		validationList.add(new SectionToStudentToModulesAll(scenarioName, instructorUser, 
				route, expectedResponseCode, moduleArray,
				/*expectedItemLevelLinks*/ null, 
				/*offset*/ 0, /*limit*/ moduleCount+1, /*itemCount*/ moduleCount));
		
		for (Student enrolledStud : studList) {
			UserObject studentUser = new UserObject(enrolledStud.getUserName(), enrolledStud.getPassword(), 
					UserType.Student, EnvironmentType.Staging);
			studentUser.setId(enrolledStud.getPersonId());
			
			if (enrolledStud.getPersonId().compareTo(stud.getPersonId()) == 0) {
				//this student calls endpoint 2.3 to view this student's data - permitted
				validationList.add(new SectionToStudentToModulesAll(scenarioName, studentUser, 
						route, expectedResponseCode, moduleArray,
						/*expectedItemLevelLinks*/ null, 
						/*offset*/ null, /*limit*/ null, /*itemCount*/ moduleCount));
			} else {
				//another student calls endpoint 2.3 to view this student's data - not permitted
				validationList.add(new SectionToStudentToModulesAll(scenarioName, studentUser, 
						route, /*expectedResponseCode*/ ResponseCode.UNATHORIZED.value, moduleArray,
						/*expectedItemLevelLinks*/ null, 
						/*offset*/ null, /*limit*/ null, /*itemCount*/ moduleCount));
			}
		}
	}
	
	private StudentModuleObject[] getArrayFromList(List<StudentModuleObject> moduleList) {
		StudentModuleObject[] moduleArray = new StudentModuleObject[moduleList.size()];
		int i=0;
		for (StudentModuleObject module : moduleList) {
			moduleArray[i] = module;
			i++;
		}
		return moduleArray;
	}
}
