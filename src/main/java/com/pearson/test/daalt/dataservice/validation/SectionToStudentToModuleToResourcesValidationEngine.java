package com.pearson.test.daalt.dataservice.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.pearson.qa.apex.dataobjects.EnvironmentType;
import com.pearson.qa.apex.dataobjects.UserObject;
import com.pearson.qa.apex.dataobjects.UserType;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Chapter;
import com.pearson.test.daalt.dataservice.model.ChapterSection;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.EmbeddedQuestion;
import com.pearson.test.daalt.dataservice.model.LearningResource;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.response.model.StudentLearningResourceObject;
import com.pearson.test.daalt.dataservice.validation.endpoint.SectionToStudentToModuleToResourcesAll;

/**
 * endpoint 2.7
 */
public class SectionToStudentToModuleToResourcesValidationEngine extends DaaltDataServiceValidationEngine {
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

			if (!courseSection.getEnrolledStudents().isEmpty()) {
				for (Assignment assignment : courseSection.getAssignments()) {
					for (Chapter chapter : assignment.getChapters()) {
						Map<String, List<StudentLearningResourceObject>> resourcesUnderChapter = new HashMap<>();
						
						Quiz chapterQuiz = chapter.getChapterQuiz();
						if (chapterQuiz != null) {
							
							for (Student stud : courseSection.getEnrolledStudents()) {
								StudentLearningResourceObject chapterQuizResource = getResourceObject(stud, 
										courseSection, assignment, chapterQuiz);
								resourcesUnderChapter = addResourceToCollection(resourcesUnderChapter, 
										stud.getPersonId(), chapterQuizResource);
							}
							
							if (chapterQuiz.getNestedQuizzes() != null) {
								Map<String, List<StudentLearningResourceObject>> resourcesUnderChapterQuiz = new HashMap<>();
								for (Quiz nestedQuiz : chapterQuiz.getNestedQuizzes()) {
									for (Student stud : courseSection.getEnrolledStudents()) {
										StudentLearningResourceObject nestedQuizResource = getResourceObject(stud, 
												courseSection, assignment, nestedQuiz);
										resourcesUnderChapterQuiz = addResourceToCollection(resourcesUnderChapterQuiz, 
												stud.getPersonId(), nestedQuizResource);
									}
								}
								
								//section/thisStudent/module/chapter/chapterQuiz/resourcesUnderChapterQuiz
								for (Student stud : courseSection.getEnrolledStudents()) {
									route = new StringBuilder();
									route.append(baseUrl).append("/platforms/").append(platformCode)
									.append("/sections/").append(courseSection.getId())
									.append("/students/").append(stud.getPersonId())
									.append("/modules/").append(assignment.getId())
									.append("/resources/").append(chapter.getLearningResourceId())
									.append("/resources/").append(chapterQuiz.getLearningResourceId())
									.append("/resources");
									
									addValidations(data.getTestScenarioName(), instructorUser, 
											route.toString(), resourcesUnderChapterQuiz.get(stud.getPersonId()), 
											stud, courseSection.getEnrolledStudents());
								}
							}							
						}
						if (chapter.getChapterSections() != null ){
							for (ChapterSection chapterSection : chapter.getChapterSections()) {
								Map<String, List<StudentLearningResourceObject>> resourcesUnderChapterSection = new HashMap<>();
								Quiz chapterSectionQuiz = chapterSection.getChapterSectionQuiz();
								
								for (Student stud : courseSection.getEnrolledStudents()) {								
									StudentLearningResourceObject chapterSectionResource = getResourceObject(stud, 
											courseSection, assignment, chapterSection);
									resourcesUnderChapter = addResourceToCollection(resourcesUnderChapter, 
											stud.getPersonId(), chapterSectionResource);
								}
								
								if (chapterSectionQuiz != null) {
									for (Student stud : courseSection.getEnrolledStudents()) {
										StudentLearningResourceObject chapterSectionQuizResource = getResourceObject(stud, 
												courseSection, assignment, chapterSectionQuiz);
										resourcesUnderChapterSection = addResourceToCollection(resourcesUnderChapterSection, 
												stud.getPersonId(), chapterSectionQuizResource);
									}
								}
								
								if (chapterSection.getPages() != null){
									for (Page page : chapterSection.getPages()) {
										Map<String, List<StudentLearningResourceObject>> resourcesUnderPage = new HashMap<>();
										
										for (Student stud : courseSection.getEnrolledStudents()) {
											StudentLearningResourceObject pageResource = getResourceObject(stud, 
													courseSection, assignment, page);
											resourcesUnderChapterSection = addResourceToCollection(resourcesUnderChapterSection, 
													stud.getPersonId(), pageResource);
										}

										//embedded questions
										if (page.getEmbeddedQuestions() != null) {
											for (Student stud : courseSection.getEnrolledStudents()) {
												float seqNum = 0;
												for (Question embeddedQuestion : page.getEmbeddedQuestions()) {
													EmbeddedQuestion embeddedQuestionLR = page.getEmbeddedQuestionLR(embeddedQuestion, seqNum);
													
													StudentLearningResourceObject embeddedQuestionResource = getResourceObject(stud, 
															courseSection, assignment, embeddedQuestionLR);
													resourcesUnderPage = addResourceToCollection(resourcesUnderPage, 
															stud.getPersonId(), embeddedQuestionResource);
													
													seqNum++;
												}
											}
										}
										
										//nested pages
										if (page.getPages() != null) {
											for (Page nestedPage : page.getPages()) {
												for (Student stud : courseSection.getEnrolledStudents()) {
													StudentLearningResourceObject nestedPageResource = getResourceObject(stud, 
															courseSection, assignment, nestedPage);
													resourcesUnderPage = addResourceToCollection(resourcesUnderPage, 
															stud.getPersonId(), nestedPageResource);
												}
											}
										}
										
										if (page.getEmbeddedQuestions() != null || (page.getPages() != null && !page.getPages().isEmpty())) {
											//section/thisStudent/module/chapter/chapterSection/page/resourcesUnderpage
											for (Student stud : courseSection.getEnrolledStudents()) {
												route = new StringBuilder();
												route.append(baseUrl).append("/platforms/").append(platformCode)
												.append("/sections/").append(courseSection.getId())
												.append("/students/").append(stud.getPersonId())
												.append("/modules/").append(assignment.getId())
												.append("/resources/").append(chapter.getLearningResourceId())
												.append("/resources/").append(chapterSection.getLearningResourceId())
												.append("/resources/").append(page.getLearningResourceId())
												.append("/resources");
												
												addValidations(data.getTestScenarioName(), instructorUser, 
														route.toString(), resourcesUnderPage.get(stud.getPersonId()), 
														stud, courseSection.getEnrolledStudents());
											}
										} else {
											for (Student stud : courseSection.getEnrolledStudents()) {
												route = new StringBuilder();
												route.append(baseUrl).append("/platforms/").append(platformCode)
												.append("/sections/").append(courseSection.getId())
												.append("/students/").append(stud.getPersonId())
												.append("/modules/").append(assignment.getId())
												.append("/resources/").append(chapter.getLearningResourceId())
												.append("/resources/").append(chapterSection.getLearningResourceId())
												.append("/resources/").append(page.getLearningResourceId())
												.append("/resources");

												validationList.add(new SectionToStudentToModuleToResourcesAll(data.getTestScenarioName(), instructorUser, 
														route.toString(), /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value, null,
														/*expectedItemLevelLinks*/ null, 
														/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
											}
										}
									}
								}
								
								
								//section/thisStudent/module/chapter/chapterSection/resourcesUnderChapterSection
								for (Student stud : courseSection.getEnrolledStudents()) {
									route = new StringBuilder();
									route.append(baseUrl).append("/platforms/").append(platformCode)
									.append("/sections/").append(courseSection.getId())
									.append("/students/").append(stud.getPersonId())
									.append("/modules/").append(assignment.getId())
									.append("/resources/").append(chapter.getLearningResourceId())
									.append("/resources/").append(chapterSection.getLearningResourceId())
									.append("/resources");
									
									addValidations(data.getTestScenarioName(), instructorUser, 
											route.toString(), resourcesUnderChapterSection.get(stud.getPersonId()), 
											stud, courseSection.getEnrolledStudents());
								}
							}
						}
						
						//section/thisStudent/module/chapter/resourcesUnderChapter
						for (Student stud : courseSection.getEnrolledStudents()) {
							route = new StringBuilder();
							route.append(baseUrl).append("/platforms/").append(platformCode)
								.append("/sections/").append(courseSection.getId())
								.append("/students/").append(stud.getPersonId())
								.append("/modules/").append(assignment.getId())
								.append("/resources/").append(chapter.getLearningResourceId())
								.append("/resources");
							
							addValidations(data.getTestScenarioName(), instructorUser, 
									route.toString(), resourcesUnderChapter.get(stud.getPersonId()), 
									stud, courseSection.getEnrolledStudents());
						}
					}
				}
				
				if (courseSection.getAssignments().isEmpty()) {
					for (Student stud : courseSection.getEnrolledStudents()) {
						route = new StringBuilder();
						route.append(baseUrl).append("/platforms/").append(platformCode)
						.append("/sections/").append(courseSection.getId())
						.append("/students/").append(stud.getPersonId())
						.append("/modules/").append("SQE-fake-learning-module-id")
						.append("/resources/").append("SQE-fake-learning-resource-id")
						.append("/resources");
	
						validationList.add(new SectionToStudentToModuleToResourcesAll(data.getTestScenarioName(), instructorUser, 
								route.toString(), /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value, null,
								/*expectedItemLevelLinks*/ null, 
								/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
					}
				}
			}
		}
		
		return validationList;
	}
	
	private StudentLearningResourceObject getResourceObject(Student stud, CourseSection courseSection, 
			Assignment assignment, LearningResource resource) throws InvalidTestDataException {
		if (courseSection.getInstructor() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModuleToResources validations - courseSection.instructor is null");
		} else if (courseSection.getInstructor().getUserName() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModuleToResources validations - instructor.userName is null");
		} else if (courseSection.getInstructor().getPassword() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModuleToResources validations - instructor.password is null");
		} else if (courseSection.getInstructor().getPersonId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModuleToResources validations - instructor.personId is null");
		} else if (platformCode == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModuleToResources validations - platformId is null");
		} else if (courseSection.getId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModuleToResources validations - courseSectionId is null");
		} else if (assignment.getId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModuleToResources validations - assignmentId is null");
		} else if (resource.getLearningResourceId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModuleToResources validations - resource.learningResourceId is null");
		} else if (resource.getLearningResourceSequenceNumber() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModuleToResources validations - resource.learningResourceSequenceNumber is null");
		} else if (resource.getLearningResourceTitle() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModuleToResources validations - resource.learningResourceTitle is null");
		} else if (resource.getLearningResourceType() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModuleToResources validations - resource.learningResourceType is null");
		} else if (stud.getPersonId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToStudentToModuleToResources validations - student.personId is null");
		}
		
		StudentLearningResourceObject resourceObj = new StudentLearningResourceObject();
		resourceObj.platformId = platformCode;
		resourceObj.courseSectionId = courseSection.getId();
		resourceObj.learningModuleId = assignment.getId();
		resourceObj.learningResourceId = resource.getLearningResourceId();
		resourceObj.learningResourceSequence = resource.getLearningResourceSequenceNumber();
		resourceObj.learningResourceTitle = resource.getLearningResourceTitle();
		resourceObj.learningResourceType = resource.getLearningResourceType();
		resourceObj.learningResourceSubType = resource.getLearningResourceSubType();
		float pointsPossible = resource.getAggregatedPointsPossible();
		float pointsEarned = resource.getPointsEarnedFinal(stud);
		float practicePointsPossible = resource.getAggregatedPracticePointsPossible();
		float practicePointsEarned = resource.getPracticePointsEarnedFinal(stud);
		
		resourceObj.practicePointsPossible = practicePointsPossible;
		resourceObj.studentPracticePoints = practicePointsEarned;
		resourceObj.pointsPossible = pointsPossible;
		resourceObj.studentPoints = pointsEarned;
		resourceObj.studentPercent = pointsPossible > 0 ? (pointsEarned/pointsPossible)*100 : 0;
		resourceObj.studentLateSubmissionPoints = resource.getStudentLateSubmissionPoints(stud);
		
//		if (!resource.hasCredit()) {
//			resourceObj.pointsPossible = null;
//			resourceObj.studentPoints = null;
//			resourceObj.studentPercent = null;
//		}
//		if (!resource.hasPractice()) {
//			resourceObj.practicePointsPossible = null;
//			resourceObj.studentPracticePoints = null;
//		}
		
		//TODO: next release there will probably be a new set of requirements for practice vs. credit
		if (resourceObj.pointsPossible == 0) {
			resourceObj.pointsPossible = null;
			resourceObj.studentPoints = null;
			resourceObj.studentPercent = null;
		}
		
		if (resourceObj.practicePointsPossible == 0) {
			resourceObj.practicePointsPossible = null;
			resourceObj.studentPracticePoints = null;
		}
		
		resourceObj.includesAdjustedPoints = resource.isAdjusted();
		resourceObj.hasChildrenFlag = resource.getChildResources() != null && !resource.getChildResources().isEmpty();
		resourceObj.studentId = stud.getPersonId();
		resourceObj.studentFirstName = stud.getGivenName();
		resourceObj.studentLastName = stud.getFamilyName();
		
		long assessmentTimeSeconds = resource.getAssessmentTime(stud);
		long learningTimeSeconds = resource.getLearningTime(stud);
		resourceObj.totalTimeSpentAssessment = formatTimeOnTask(assessmentTimeSeconds*1000);
		resourceObj.totalTimeSpentLearning = formatTimeOnTask(learningTimeSeconds*1000);
		resourceObj.timeSpentTotal = formatTimeOnTask((assessmentTimeSeconds + learningTimeSeconds)*1000);
		resourceObj.totalChildTimeSpentAssessment = formatTimeOnTask(resource.getChildAssessmentTime(stud)*1000);
		resourceObj.totalChildTimeSpentLearning = formatTimeOnTask(resource.getChildLearningTime(stud)*1000);
		return resourceObj;
	}
	
	private void addValidations(String scenarioName, UserObject instructorUser, 
			String route, List<StudentLearningResourceObject> resourceList, Student stud, List<Student> studList) 
					throws JsonGenerationException, JsonMappingException, IOException {
		
		if (resourceList != null && !resourceList.isEmpty()) {
			StudentLearningResourceObject[] resourceArray = null;

			Collections.sort(resourceList);
			resourceArray = getArrayFromList(resourceList);
			int LRCount = resourceArray == null ? 0 : resourceArray.length;
			int i = 0;
			//one call that limits the returned collection to only this learningResource
//			for(StudentLearningResourceObject learningResourceObject : resourceArray) {					// FUTURE: out of scope for DDS 2.0, LRS not being populated per Lakshmi. 
//				StudentLearningResourceObject[] thisLRcollection = new StudentLearningResourceObject[1];
//				thisLRcollection[0] = learningResourceObject;
//				validationList.add(new SectionToStudentToModuleToResourcesAll(scenarioName, instructorUser, 
//						route, /*expectedResponseCode*/ ResponseCode.OK.value, thisLRcollection,
//						/*expectedItemLevelLinks*/ null, 
//						/*offset*/ i, /*limit*/ 1, /*itemCount*/ LRCount));
//				i++;
//			}
			
			//instructor calls endpoint 2.7 to view this student's data - permitted
			validationList.add(new SectionToStudentToModuleToResourcesAll(scenarioName, instructorUser, 
					route, /*expectedResponseCode*/ ResponseCode.OK.value, resourceArray,
					/*expectedItemLevelLinks*/ null, 
					/*offset*/ 0, /*limit*/ LRCount + 1, /*itemCount*/ LRCount));
			
			for (Student enrolledStud : studList) {
				UserObject studentUser = new UserObject(enrolledStud.getUserName(), enrolledStud.getPassword(), 
						UserType.Student, EnvironmentType.Staging);
				studentUser.setId(enrolledStud.getPersonId());
				
				if (enrolledStud.getPersonId().compareTo(stud.getPersonId()) == 0) {
					//this student calls endpoint 2.7 to view this student's data - permitted
					validationList.add(new SectionToStudentToModuleToResourcesAll(scenarioName, studentUser, 
							route, /*expectedResponseCode*/ ResponseCode.OK.value, resourceArray,
							/*expectedItemLevelLinks*/ null, 
							/*offset*/ null, /*limit*/ null, /*itemCount*/ LRCount));
				} else {
					//another student calls endpoint 2.7 to view this student's data - not permitted
					validationList.add(new SectionToStudentToModuleToResourcesAll(scenarioName, studentUser, 
							route, /*expectedResponseCode*/ ResponseCode.UNATHORIZED.value, resourceArray,
							/*expectedItemLevelLinks*/ null, 
							/*offset*/ null, /*limit*/ null, /*itemCount*/ LRCount));
				}
			}
		} else {
			//no child learning resources will be returned by the endpoint - expect 404
			validationList.add(new SectionToStudentToModuleToResourcesAll(scenarioName, instructorUser, 
					route, /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value, null,
					/*expectedItemLevelLinks*/ null, 
					/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
		}
	}
	
	private Map<String, List<StudentLearningResourceObject>> addResourceToCollection(Map<String, List<StudentLearningResourceObject>> collection, 
			String key, StudentLearningResourceObject resourceObj) {
		if (!collection.containsKey(key)) {
			collection.put(key, new ArrayList<StudentLearningResourceObject>());
		}
		collection.get(key).add(resourceObj);
		return collection;
	}
	
	private StudentLearningResourceObject[] getArrayFromList(List<StudentLearningResourceObject> resourceList) {
		StudentLearningResourceObject[] resourceArray = new StudentLearningResourceObject[resourceList.size()];
		int i=0;
		for (StudentLearningResourceObject resource : resourceList) {
			resourceArray[i] = resource;
			i++;
		}
		return resourceArray;
	}
}
