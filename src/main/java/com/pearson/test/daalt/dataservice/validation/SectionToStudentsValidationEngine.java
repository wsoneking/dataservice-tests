package com.pearson.test.daalt.dataservice.validation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
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
import com.pearson.test.daalt.dataservice.response.model.CourseSectionStudentObject;
import com.pearson.test.daalt.dataservice.response.model.LinkObject;
import com.pearson.test.daalt.dataservice.validation.endpoint.SectionToStudentsAll;

/**
 * endpoint 2.1
 */
public class SectionToStudentsValidationEngine extends DaaltDataServiceValidationEngine {
	private String baseUrl;
	
	public List<Validation> getValidations(TestData data) throws InvalidTestDataException, JsonGenerationException, JsonMappingException, IOException {
		List<Validation> validationList = new ArrayList<>();
		baseUrl = TestEngine.getInstance().getBaseUrl();
		StringBuilder route = new StringBuilder();
		
		for (CourseSection courseSection : data.getAllCourseSections()) {
			UserObject instructorUser = new UserObject(courseSection.getInstructor().getUserName(), 
					courseSection.getInstructor().getPassword(), 
					UserType.Professor, EnvironmentType.Staging);
			instructorUser.setId(courseSection.getInstructor().getPersonId());

			route = new StringBuilder();
			route.append(baseUrl).append("/platforms/").append(platformCode)
				.append("/sections/").append(courseSection.getId())
				.append("/students");
			LinkObject selfLink = new LinkObject();
			selfLink.href = route.toString();
			
			if (!courseSection.getEnrolledStudents().isEmpty()) {
				int studentCount = courseSection.getEnrolledStudents().size();
				
				UserObject studentUser = null;
				CourseSectionStudentObject[] expectedStudentCollection = new CourseSectionStudentObject[studentCount];
				Map<String, LinkObject> expectedEnvelopeLevelLinks = new HashMap<>();
				expectedEnvelopeLevelLinks.put(LinkType.SELF.value, selfLink);
				int i=0;
				for (Student stud : courseSection.getEnrolledStudents()) {
					//add student to unlimited collection
					studentUser = new UserObject(stud.getUserName(), stud.getPassword(), 
							UserType.Student, EnvironmentType.Staging);
					studentUser.setId(stud.getPersonId());
					CourseSectionStudentObject studObj = getStudentObject(stud, courseSection);
					expectedStudentCollection[i] = studObj;
					
					/*
					 * one call that limits the returned collection to only this student
					 * 
					 * TODO: this approach assumes that the endpoint will return students 
					 * in the same order as courseSection.getEnrolledStudents() - will this work?
					 */
					StringBuilder singleStudentRoute = new StringBuilder();
					singleStudentRoute.append(baseUrl).append("/platforms/").append(platformCode)
						.append("/sections/").append(courseSection.getId()).append("/students");
					
					Map<String, LinkObject> thisStudentExpectedEnvelopeLevelLinks = new HashMap<>();
					LinkObject singleStudentSelfLink = new LinkObject();
					singleStudentSelfLink.href = singleStudentRoute.toString();
					thisStudentExpectedEnvelopeLevelLinks.put(LinkType.SELF.value, singleStudentSelfLink);
					
					if (studentCount > 1) {
						if (i > 0) {
							//add "first" link
							StringBuilder singleStudentFirstRoute = new StringBuilder();
							singleStudentFirstRoute.append(baseUrl).append("/platforms/").append(platformCode)
								.append("/sections/").append(courseSection.getId())
								.append("/students?offset=0&limit=1");
							LinkObject singleStudentFirstLink = new LinkObject();
							singleStudentFirstLink.href = singleStudentFirstRoute.toString();
							thisStudentExpectedEnvelopeLevelLinks.put(LinkType.FIRST.value, singleStudentFirstLink);
							
							//add "previous" link
							StringBuilder singleStudentPreviousRoute = new StringBuilder();
							singleStudentPreviousRoute.append(baseUrl).append("/platforms/").append(platformCode)
								.append("/sections/").append(courseSection.getId())
								.append("/students?offset=").append(i-1).append("&limit=1");
							LinkObject singleStudentPreviousLink = new LinkObject();
							singleStudentPreviousLink.href = singleStudentPreviousRoute.toString();
							thisStudentExpectedEnvelopeLevelLinks.put(LinkType.PREVIOUS.value, singleStudentPreviousLink);
						}
						
						if (i < studentCount-1) {
							//add "next" link
							StringBuilder singleStudentNextRoute = new StringBuilder();
							singleStudentNextRoute.append(baseUrl).append("/platforms/").append(platformCode)
								.append("/sections/").append(courseSection.getId())
								.append("/students?offset=").append(i+1).append("&limit=1");
							LinkObject singleStudentNextLink = new LinkObject();
							singleStudentNextLink.href = singleStudentNextRoute.toString();
							thisStudentExpectedEnvelopeLevelLinks.put(LinkType.NEXT.value, singleStudentNextLink);
							
							//add "last" link
							StringBuilder singleStudentLastRoute = new StringBuilder();
							singleStudentLastRoute.append(baseUrl).append("/platforms/").append(platformCode)
								.append("/sections/").append(courseSection.getId())
								.append("/students?offset=").append(studentCount-1).append("&limit=1");
							LinkObject singleStudentLastLink = new LinkObject();
							singleStudentLastLink.href = singleStudentLastRoute.toString();
							thisStudentExpectedEnvelopeLevelLinks.put(LinkType.LAST.value, singleStudentLastLink);							
						}
					}		

					CourseSectionStudentObject[] thisStudentCollection = new CourseSectionStudentObject[1];
					thisStudentCollection[0] = studObj;
					validationList.add(new SectionToStudentsAll(data.getTestScenarioName(), instructorUser, 
							singleStudentRoute.toString(), /*expectedResponseCode*/ /*ResponseCode.OK.value*/ ResponseCode.NOT_IMPLEMENTED.value, 
							thisStudentCollection, thisStudentExpectedEnvelopeLevelLinks, 
							/*offset*/ i, /*limit*/ 1, /*itemCount*/ studentCount));
					
					i++;					
				}
				
				//one call that does not limit the returned collection (no offset or limit parameters)
				validationList.add(new SectionToStudentsAll(data.getTestScenarioName(), instructorUser, 
						route.toString(), /*expectedResponseCode*/ /*ResponseCode.OK.value*/ ResponseCode.NOT_IMPLEMENTED.value, 
						expectedStudentCollection, expectedEnvelopeLevelLinks, 
						/*offset*/ null, /*limit*/ null, /*itemCount*/ studentCount));
				
				if (studentCount > 1) {
					//one call that does not limit the returned collection (offset=0 & limit>studentCount)
					StringBuilder unlimitedRoute = new StringBuilder();
					unlimitedRoute.append(baseUrl).append("/platforms/").append(platformCode)
						.append("/sections/").append(courseSection.getId()).append("/students");
					validationList.add(new SectionToStudentsAll(data.getTestScenarioName(), instructorUser, 
							unlimitedRoute.toString(), /*expectedResponseCode*/ /*ResponseCode.OK.value*/ ResponseCode.NOT_IMPLEMENTED.value, 
							expectedStudentCollection, expectedEnvelopeLevelLinks, 
							/*offset*/ 0, /*limit*/ studentCount+1, /*itemCount*/ studentCount));
				}
				
				if (studentUser != null) {
					//students are not authorized to call endpoint 2.1 - expect a 401 response
					validationList.add(new SectionToStudentsAll(data.getTestScenarioName(), studentUser, 
							route.toString(), /*expectedResponseCode*/ /*ResponseCode.UNATHORIZED.value*/ ResponseCode.NOT_IMPLEMENTED.value, 
							/*expectedStudents*/ null, /*expectedEnvelopeLevelLinks*/ null, 
							/*offset*/ null, /*limit*/ null, /*itemCount*/ studentCount));
				} 
				
				StringBuilder badRequestRoute = new StringBuilder();
				
				//call endpoint with offset > max_index - expect a 400 response
				badRequestRoute.append(baseUrl).append("/platforms/").append(platformCode)
					.append("/sections/").append(courseSection.getId()).append("/students");
				validationList.add(new SectionToStudentsAll(data.getTestScenarioName(), instructorUser, 
						badRequestRoute.toString(), /*expectedResponseCode*/ /*ResponseCode.BAD_REQUEST.value*/ ResponseCode.NOT_IMPLEMENTED.value, 
						/*expectedStudents*/ null, /*expectedEnvelopeLevelLinks*/ null, 
						/*offset*/ studentCount, /*limit*/ studentCount, /*itemCount*/ 0));
				
				//call endpoint with offset = -1 - expect a 400 response
				badRequestRoute = new StringBuilder();
				badRequestRoute.append(baseUrl).append("/platforms/").append(platformCode)
					.append("/sections/").append(courseSection.getId()).append("/students");
				validationList.add(new SectionToStudentsAll(data.getTestScenarioName(), instructorUser, 
						badRequestRoute.toString(), /*expectedResponseCode*/ /*ResponseCode.BAD_REQUEST.value*/ ResponseCode.NOT_IMPLEMENTED.value, 
						/*expectedStudents*/ null, /*expectedEnvelopeLevelLinks*/ null, 
						/*offset*/ -1, /*limit*/ studentCount, /*itemCount*/ 0));
				
				//call endpoint with limit = -1 - expect a 400 response
				badRequestRoute = new StringBuilder();
				badRequestRoute.append(baseUrl).append("/platforms/").append(platformCode)
					.append("/sections/").append(courseSection.getId()).append("/students");
				validationList.add(new SectionToStudentsAll(data.getTestScenarioName(), instructorUser, 
						badRequestRoute.toString(), /*expectedResponseCode*/ /*ResponseCode.BAD_REQUEST.value*/ ResponseCode.NOT_IMPLEMENTED.value, 
						/*expectedStudents*/ null, /*expectedEnvelopeLevelLinks*/ null, 
						/*offset*/ 0, /*limit*/ -1, /*itemCount*/ 0));
				
				//call endpoint with limit = 0 - expect a 400 response
				badRequestRoute = new StringBuilder();
				badRequestRoute.append(baseUrl).append("/platforms/").append(platformCode)
					.append("/sections/").append(courseSection.getId()).append("/students");
				validationList.add(new SectionToStudentsAll(data.getTestScenarioName(), instructorUser, 
						badRequestRoute.toString(), /*expectedResponseCode*/ /*ResponseCode.BAD_REQUEST.value*/ ResponseCode.NOT_IMPLEMENTED.value, 
						/*expectedStudents*/ null, /*expectedEnvelopeLevelLinks*/ null, 
						/*offset*/ 0, /*limit*/ 0, /*itemCount*/ 0));
				
			} else {
				//no students enrolled in the course section = expect a 404 response
				validationList.add(new SectionToStudentsAll(data.getTestScenarioName(), instructorUser, 
						route.toString(), /*expectedResponseCode*/ /*ResponseCode.NOT_FOUND.value*/ ResponseCode.NOT_IMPLEMENTED.value, 
						/*expectedStudents*/ null, /*expectedEnvelopeLevelLinks*/ null, 
						/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
			}
		}
		
		return validationList;
	}
	
	private CourseSectionStudentObject getStudentObject(User stud, 
			CourseSection courseSection) throws InvalidTestDataException {
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
		} else if (stud.getPersonId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToStudents validations - studentId is null");
		}
		
		CourseSectionStudentObject studObj = new CourseSectionStudentObject();
		studObj.platformId = platformCode;
		studObj.courseSectionId = courseSection.getId();
		studObj.studentId = stud.getPersonId();
		long learningTimeSeconds = courseSection.getLearningTime(stud);
		long timeSpentAssessing = courseSection.getTimeSpentAssessing(stud);
		studObj.timeSpentLearning = formatTimeOnTask(learningTimeSeconds*1000);
		studObj.timeSpentAssessment = formatTimeOnTask(timeSpentAssessing*1000);
		studObj.timeSpentTotal = formatTimeOnTask((timeSpentAssessing + learningTimeSeconds)*1000);
		studObj.studentTrending = getTrending(courseSection, stud);
		Long lastActivityDate = courseSection.getLastActivityDate(stud);
		if (lastActivityDate != null) {
			studObj.lastActivityDate = getFormattedDateString(lastActivityDate);
		}
		studObj._links = new HashMap<>();
		
		//add "self" link (route to call endpoint 2.2 & get back this student)
		//TODO: still an open question whether/how to suppress until endpoint 2.2 is implemented
//		StringBuilder selfLinkRoute = new StringBuilder();
//		selfLinkRoute.append(baseUrl).append("/platforms/").append(platformCode)
//			.append("/sections/").append(courseSection.getId())
//			.append("/students/").append(stud.getPersonId());
//		LinkObject selfLink = new LinkObject();
//		selfLink.href = selfLinkRoute.toString();
//		studObj._links.put(LinkType.SELF.value, selfLink);
		
		//add "modules" link (route to call endpoint 2.3 & get back modules for this student)
		StringBuilder modulesLinkRoute = new StringBuilder();
		modulesLinkRoute.append(baseUrl).append("/platforms/").append(platformCode)
			.append("/sections/").append(courseSection.getId())
			.append("/students/").append(stud.getPersonId())
			.append("/modules");
		LinkObject modulesLink = new LinkObject();
		modulesLink.href = modulesLinkRoute.toString();
		studObj._links.put(LinkType.MODULES.value, modulesLink);
		
		return studObj;
	}

	private Float getTrending(CourseSection courseSection, User stud) { 
		//get the most recent assignment in the CourseSection
		Assignment assignment = courseSection.getMostRecentAssignmentForTrend(stud);
		List<Assignment> allAssignments = courseSection.getAssignments();
		int assignmentCount = allAssignments.size();
		
		int index = 0;
		if (assignment != null) {
			for (int i=0; i < assignmentCount; i++) {
				if (allAssignments.get(i).getId().equals(assignment.getId())) {
					index = i;
					break;
				}
			}
		}
		index--;
		
		List<Assignment> upToFivePreviousAssignments = new ArrayList<>();
		
		while (index >= 0 && upToFivePreviousAssignments.size() < 5) {
			if (allAssignments.get(index).isDueDatePassed() && allAssignments.get(index).studentCompletedAssignment(stud)) {
				upToFivePreviousAssignments.add(allAssignments.get(index));
			}
			index--;
		}
			
		float totalPoints = 0;
		for (Assignment ass : upToFivePreviousAssignments) {
			totalPoints += ass.getPointsEarnedFinal(stud);
		}
		float avePreviousPoints = upToFivePreviousAssignments.size() > 0 ? totalPoints / upToFivePreviousAssignments.size() : 0;
		
		return (assignment != null) ? assignment.getPointsEarnedFinal(stud) - avePreviousPoints : null;
	}
	
	@JsonIgnore
	public String getFormattedDateString(Long millis) {
	String formattedString = null;
		if(millis != null){
		String dateFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		
		Date date = new Date(millis);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatString);
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    formattedString = sdf.format(date);
	}
		return formattedString;
	}
	
}