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
import com.pearson.test.daalt.dataservice.response.model.LearningResourceObject;
import com.pearson.test.daalt.dataservice.validation.endpoint.SectionToModuleToResourcesAll;

/**
 * endpoint 1.9
 */
public class SectionToModuleToResourcesValidationEngine extends DaaltDataServiceValidationEngine {
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
			
			Student stud = null;
			if (courseSection.getEnrolledStudentCount() > 0) {
				stud = courseSection.getEnrolledStudents().get(0);
			}

			for (Assignment assignment : courseSection.getAssignments()) {
				for (Chapter chapter : assignment.getChapters()) {
					List<LearningResourceObject> resourcesUnderChapter = new ArrayList<>();
					
					Quiz chapterQuiz = chapter.getChapterQuiz();
					if (chapterQuiz != null) {
						LearningResourceObject chapterQuizResource 
							= getResourceObject(courseSection, assignment, chapterQuiz);
						resourcesUnderChapter.add(chapterQuizResource);
						
						List<LearningResourceObject> resourcesUnderChapterQuiz = new ArrayList<>();
						if (chapterQuiz.getNestedQuizzes() != null) {
							for (Quiz nestedQuiz : chapterQuiz.getNestedQuizzes()) {
								LearningResourceObject nestedQuizResource 
									= getResourceObject(courseSection, assignment, nestedQuiz);
								resourcesUnderChapterQuiz.add(nestedQuizResource);
							}
						}
						
						//section/module/chapter/chapterQuiz/resourcesUnderChapterQuiz
						route = new StringBuilder();
						route.append(baseUrl).append("/platforms/").append(platformCode)
						.append("/sections/").append(courseSection.getId())
						.append("/modules/").append(assignment.getId())
						.append("/resources/").append(chapter.getLearningResourceId())
						.append("/resources/").append(chapterQuiz.getLearningResourceId())
						.append("/resources");
						
						addValidations(data.getTestScenarioName(), instructorUser, 
								route.toString(), resourcesUnderChapterQuiz, stud);
					}

					if (chapter.getChapterSections() != null ){
						for (ChapterSection chapterSection : chapter.getChapterSections()) {
							List<LearningResourceObject> resourcesUnderChapterSection = new ArrayList<>();
							
							LearningResourceObject chapterSectionResource 
								= getResourceObject(courseSection, assignment, chapterSection);
							resourcesUnderChapter.add(chapterSectionResource);
							
							Quiz chapterSectionQuiz = chapterSection.getChapterSectionQuiz();
							if (chapterSectionQuiz != null) {
								LearningResourceObject chapterSectionQuizResource 
									= getResourceObject(courseSection, assignment, chapterSectionQuiz);
								resourcesUnderChapterSection.add(chapterSectionQuizResource);
							}
							
							if (chapterSection.getPages() != null) {
								for (Page page : chapterSection.getPages()) {
									List<LearningResourceObject> resourcesUnderPage = new ArrayList<>();
									LearningResourceObject pageResource 
										= getResourceObject(courseSection, assignment, page);
									resourcesUnderChapterSection.add(pageResource);
									
									// embedded questions
									if (page.getEmbeddedQuestions() != null) {
										float seqNum = 0;
										for (Question embeddedQuestion : page.getEmbeddedQuestions()) {
											EmbeddedQuestion embeddedQuestionLR = page.getEmbeddedQuestionLR(embeddedQuestion, seqNum);
											
											LearningResourceObject embeddedQuestionResource 
												= getResourceObject(courseSection, assignment, embeddedQuestionLR);
											resourcesUnderPage.add(embeddedQuestionResource);
											
											seqNum++;
										}
									}
									
									if (page.getPages() != null) {
										for (Page nestedPage : page.getPages()) {
											LearningResourceObject nestedPageResource 
												= getResourceObject(courseSection, assignment, nestedPage);
											resourcesUnderPage.add(nestedPageResource);
										}
									}
									
									//section/module/chapter/chapterSection/readingpage/resourcesUnderReadingpage
									route = new StringBuilder();
									route.append(baseUrl).append("/platforms/").append(platformCode)
									.append("/sections/").append(courseSection.getId())
									.append("/modules/").append(assignment.getId())
									.append("/resources/").append(chapter.getLearningResourceId())
									.append("/resources/").append(chapterSection.getLearningResourceId())
									.append("/resources/").append(page.getLearningResourceId())
									.append("/resources");
									
									addValidations(data.getTestScenarioName(), instructorUser, 
											route.toString(), resourcesUnderPage, stud);								
								}
							}
							
							//section/module/chapter/chapterSection/resourcesUnderChapterSection
							route = new StringBuilder();
							route.append(baseUrl).append("/platforms/").append(platformCode)
							.append("/sections/").append(courseSection.getId())
							.append("/modules/").append(assignment.getId())
							.append("/resources/").append(chapter.getLearningResourceId())
							.append("/resources/").append(chapterSection.getLearningResourceId())
							.append("/resources");
							
							addValidations(data.getTestScenarioName(), instructorUser, 
									route.toString(), resourcesUnderChapterSection, stud);
							
						}
					}
					
					//section/module/chapter/resourcesUnderChapter
					route = new StringBuilder();
					route.append(baseUrl).append("/platforms/").append(platformCode)
						.append("/sections/").append(courseSection.getId())
						.append("/modules/").append(assignment.getId())
						.append("/resources/").append(chapter.getLearningResourceId())
						.append("/resources");
					
					addValidations(data.getTestScenarioName(), instructorUser, 
							route.toString(), resourcesUnderChapter, stud);
				}
			}
			
			if (courseSection.getAssignments().isEmpty()) {
				route = new StringBuilder();
				route.append(baseUrl).append("/platforms/").append(platformCode)
				.append("/sections/").append(courseSection.getId())
				.append("/modules/").append("SQE-dummy-learning-module-id")
				.append("/resources/").append("SQE-dummy-learning-resource-id")
				.append("/resources");
				
				addValidations(data.getTestScenarioName(), instructorUser, 
						route.toString(), null, null);
			}
		}
		
		return validationList;
	}
	
	private LearningResourceObject getResourceObject(CourseSection courseSection, 
			Assignment assignment, LearningResource resource) throws InvalidTestDataException {
		if (courseSection.getInstructor() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToResources validations - courseSection.instructor is null");
		} else if (courseSection.getInstructor().getUserName() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToResources validations - instructor.userName is null");
		} else if (courseSection.getInstructor().getPassword() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToResources validations - instructor.password is null");
		} else if (courseSection.getInstructor().getPersonId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToResources validations - instructor.personId is null");
		} else if (platformCode == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToResources validations - platformId is null");
		} else if (courseSection.getId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToResources validations - courseSectionId is null");
		} else if (assignment.getId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToResources validations - assignmentId is null");
		} else if (resource.getLearningResourceId() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToResources validations - resource.learningResourceId is null");
		} else if (resource.getLearningResourceSequenceNumber() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToResources validations - resource.learningResourceSequenceNumber is null");
		} else if (resource.getLearningResourceTitle() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToResources validations - resource.learningResourceTitle is null");
		} else if (resource.getLearningResourceType() == null) {
			throw new InvalidTestDataException("Failed to generate SectionToModuleToResources validations - resource.learningResourceType is null");
		}
		
		LearningResourceObject resourceObj = new LearningResourceObject();
		resourceObj.platformId = platformCode;
		resourceObj.courseSectionId = courseSection.getId();
		resourceObj.learningModuleId = assignment.getId();
		resourceObj.learningResourceId = resource.getLearningResourceId();
		resourceObj.learningResourceSequence = resource.getLearningResourceSequenceNumber();
		resourceObj.learningResourceTitle = resource.getLearningResourceTitle();
		resourceObj.learningResourceType = resource.getLearningResourceType();
		resourceObj.learningResourceSubType = resource.getLearningResourceSubType();
		resourceObj.hasChildrenFlag = resource.getChildResources() != null && !resource.getChildResources().isEmpty();
		float pointsPossible = resource.getAggregatedPointsPossible();
		float pointsEarned = resource.getTotalPointsEarnedFinal();
		int courseSectionStudentCount = courseSection.getEnrolledStudentCount();
		resourceObj.courseSectionStudentCount = courseSectionStudentCount;
		resourceObj.completedStudentCount = 0;
		for (Student enrolledStud : courseSection.getEnrolledStudents()) {
			if (resource.studentCompleted(enrolledStud)) {
				resourceObj.completedStudentCount++;
			}
		} 
		resourceObj.incompleteStudentCount = resourceObj.courseSectionStudentCount - resourceObj.completedStudentCount;
		float classAverage = courseSectionStudentCount > 0 ? pointsEarned/(float) courseSectionStudentCount : 0;
		
	
		//  change the value to null  5/6/2015
		resourceObj.pointsPossible = pointsPossible;
		resourceObj.classTotalPoints = pointsEarned;
		resourceObj.classAvgPoints = classAverage;
		resourceObj.classAvgPercent = pointsPossible > 0 ? classAverage/pointsPossible*100 : 0;
		
		float classTotalPracticePoints = resource.getTotalPracticePointsEarnedFinal();
		resourceObj.practicePointsPossible = resource.getAggregatedPracticePointsPossible();
		resourceObj.classTotalPracticePoints = classTotalPracticePoints;
		resourceObj.classAvgPracticePoints = courseSectionStudentCount > 0 ? classTotalPracticePoints/(float) courseSectionStudentCount : 0;
	
//		if (!resource.hasCredit()) {
//			resourceObj.pointsPossible = null;
//			resourceObj.classTotalPoints = null;
//			resourceObj.classAvgPoints = null;
//			resourceObj.classAvgPercent = null;
//		}
//		
//		if (!resource.hasPractice()) {
//			resourceObj.practicePointsPossible = null;
//			resourceObj.classTotalPracticePoints = null;
//			resourceObj.classAvgPracticePoints = null;
//		}
		// end 5/6/2015
		
		//TODO: next release there will probably be a new set of requirements for practice vs. credit
		if (resourceObj.pointsPossible == 0) {
			resourceObj.pointsPossible = null;
			resourceObj.classTotalPoints = null;
			resourceObj.classAvgPoints = null;
			resourceObj.classAvgPercent = null;
		}
		
		if (resourceObj.practicePointsPossible == 0) {
			resourceObj.practicePointsPossible = null;
			resourceObj.classTotalPracticePoints = null;
			resourceObj.classAvgPracticePoints = null;
		}
		
		long assessmentTimeSeconds = resource.getTotalAssessmentTime();
		long learningTimeSeconds = resource.getTotalLearningTime();
		resourceObj.totalTimeSpentAssessment = formatTimeOnTask(assessmentTimeSeconds*1000);
		resourceObj.totalTimeSpentLearning = formatTimeOnTask(learningTimeSeconds*1000);
		float averageTimeAssessmentSeconds = courseSectionStudentCount > 0 
				? (float) assessmentTimeSeconds/(float) courseSectionStudentCount : 0;
		float averageTimeLearningSeconds = courseSectionStudentCount > 0 
				? (float) learningTimeSeconds/(float) courseSectionStudentCount : 0;
		resourceObj.avgTimeSpentAssessment = formatTimeOnTask((long) (averageTimeAssessmentSeconds*1000));
		resourceObj.avgTimeSpentLearning = formatTimeOnTask((long) (averageTimeLearningSeconds*1000));
		resourceObj.avgTimeSpentTotal = formatTimeOnTask((long) ((averageTimeAssessmentSeconds + averageTimeLearningSeconds)*1000));
		resourceObj.timeSpentTotal = formatTimeOnTask((assessmentTimeSeconds + learningTimeSeconds)*1000);
		long childAssessmentTimeSeconds = resource.getTotalChildAssessmentTime();
		long childLearningTimeSeconds = resource.getTotalChildLearningTime();
		resourceObj.totalChildTimeSpentAssessment = formatTimeOnTask(childAssessmentTimeSeconds*1000);
		resourceObj.totalChildTimeSpentLearning = formatTimeOnTask(childLearningTimeSeconds*1000);
		return resourceObj;
	}
	
	private void addValidations(String scenarioName, UserObject instructorUser, 
			String route, List<LearningResourceObject> resourceList, Student stud) 
					throws JsonGenerationException, JsonMappingException, IOException {
		
		LearningResourceObject[] resourceArray = null;
		
		/*
		 * FROM KAT: "stud" could be null and "resourceList" could be null or empty, 
	     * and in that case, the expectedResponseCode needs to be NOT_FOUND instead of OK.
	     * I put that logic back. Please do not remove it.
		 */
		int expectedResponseCode = ResponseCode.OK.value;
		if (stud == null || resourceList == null || resourceList.isEmpty()) {
			expectedResponseCode = ResponseCode.NOT_FOUND.value;
		} else if (resourceList != null && !resourceList.isEmpty()) {
			/*
			 * FROM KAT: Need to sort this list in order of learning resource sequence
			 * so that index in the list is in the same order as what the endpoint will return
			 * (I fixed it by adding a Comparator to LearningResourceObject)
			 */
			Collections.sort(resourceList);
			resourceArray = getArrayFromList(resourceList);
		}

		/*
		 * FROM KAT: Please don't add any _links at this time. 
		 * _links are not yet implemented for existing endpoints.
		 * This will be a future effort.
		 */
//		LinkObject selfLink = new LinkObject();
//		selfLink.href = route.toString();
//		Map<String, LinkObject> expectedEnvelopeLevelLinks = new HashMap<>();
//		expectedEnvelopeLevelLinks.put(LinkType.SELF.value, selfLink);
		
		//FROM KAT: "resourceArray" can be null : we need to code for this case
		int LRCount = resourceArray == null ? 0 : resourceArray.length;
		if (resourceArray != null) {
			int i = 0;
			//one call that limits the returned collection to only this learningResource
			for(LearningResourceObject learningResourceObject : resourceArray) {
				
				/*
				 * FROM KAT: Please don't add any _links at this time. 
				 * _links are not yet implemented for existing endpoints.
				 * This will be a future effort.
				 */
	//			Map<String, LinkObject> thisLRExpectedEnvelopeLevelLinks = new HashMap<>();
	//			LinkObject singleLRSelfLink = new LinkObject();
	//			singleLRSelfLink.href = route;
	//			thisLRExpectedEnvelopeLevelLinks.put(LinkType.SELF.value, singleLRSelfLink);
				
	//			if (LRCount > 1) {
	//				if (i > 0) {
	//					//add "first" link
	//					StringBuilder singleLRFirstRoute = new StringBuilder();
	//					singleLRFirstRoute.append(route)
	//						.append("?offset=0&limit=1");
	//					LinkObject singleLRFirstLink = new LinkObject();
	//					singleLRFirstLink.href = singleLRFirstRoute.toString();
	//					thisLRExpectedEnvelopeLevelLinks.put(LinkType.FIRST.value, singleLRFirstLink);
	//					
	//					//add "previous" link
	//					StringBuilder singleLRPreviousRoute = new StringBuilder();
	//					singleLRPreviousRoute.append(route)
	//						.append("?offset=").append(i-1).append("&limit=1");
	//					LinkObject singleLRPreviousLink = new LinkObject();
	//					singleLRPreviousLink.href = singleLRPreviousRoute.toString();
	//					thisLRExpectedEnvelopeLevelLinks.put(LinkType.PREVIOUS.value, singleLRPreviousLink);
	//				}
	//				
	//				if (i < LRCount-1) {
	//					//add "next" link
	//					StringBuilder singleLRNextRoute = new StringBuilder();
	//					singleLRNextRoute.append(route)
	//						.append("?offset=").append(i+1).append("&limit=1");
	//					LinkObject singleLRNextLink = new LinkObject();
	//					singleLRNextLink.href = singleLRNextRoute.toString();
	//					thisLRExpectedEnvelopeLevelLinks.put(LinkType.NEXT.value, singleLRNextLink);
	//					
	//					//add "last" link
	//					StringBuilder singleLRLastRoute = new StringBuilder();
	//					singleLRLastRoute.append(route)
	//						.append("?offset=").append(LRCount-1).append("&limit=1");
	//					LinkObject singleLRLastLink = new LinkObject();
	//					singleLRLastLink.href = singleLRLastRoute.toString();
	//					thisLRExpectedEnvelopeLevelLinks.put(LinkType.LAST.value, singleLRLastLink);							
	//				}
	//			}
				
				//FROM KAT: the expectedResponseCode be OK instead of UNATHORIZED here (I fixed it)
				LearningResourceObject[] thisLRcollection = new LearningResourceObject[1];
				thisLRcollection[0] = learningResourceObject;
				validationList.add(new SectionToModuleToResourcesAll(scenarioName, instructorUser, 
						route, /*expectedResponseCode*/ ResponseCode.OK.value, thisLRcollection,
						/*expectedItemLevelLinks*/ null, 
						/*offset*/ i, /*limit*/ 1, /*itemCount*/ LRCount));
				i++;
			}
			
			if (LRCount > 1) {
				//one call that does not limit the returned collection (offset=0 & limit>LRCount)
				validationList.add(new SectionToModuleToResourcesAll(scenarioName, instructorUser, 
						route, /*expectedResponseCode*/ ResponseCode.OK.value, 
						resourceArray, /*expectedEnvelopeLevelLinks*/ null, 
						/*offset*/ 0, /*limit*/ LRCount+1, /*itemCount*/ LRCount));
			}
			
			if (stud != null) {
				//students are not authorized to call endpoint 1.9 - expect a 401 response
				UserObject studUser = new UserObject(stud.getUserName(), stud.getPassword(), 
						UserType.Student, EnvironmentType.Staging);
				studUser.setId(stud.getPersonId());
				
				validationList.add(new SectionToModuleToResourcesAll(scenarioName, studUser, 
						route, /*expectedResponseCode*/ ResponseCode.UNATHORIZED.value, 
						/*expectedLR*/ null, /*expectedEnvelopeLevelLinks*/ null, 
						/*offset*/ null, /*limit*/ null, /*itemCount*/ LRCount));
			} 
			
			/*
			 * FROM KAT: If "stud" is null or if "resourceList" is null or empty,
			 * we should skip all of these BAD_REQUEST calls.
			 * It will get too difficult to read the test results quickly if we add too many negative validations.
			 * 
			 * Also, this requirement is not yet implemented for 1.9, so we should repress these validations for now.
			 * (leave commented so 1.9 will continue to pass)
			 */
			
//			//call endpoint with offset > max_index - expect a 400 response
//			validationList.add(new SectionToModuleToResourcesAll(scenarioName, instructorUser, 
//					route, /*expectedResponseCode*/ ResponseCode.BAD_REQUEST.value, 
//					/*expectedLR*/ null, /*expectedEnvelopeLevelLinks*/ null, 
//					/*offset*/ LRCount, /*limit*/ LRCount, /*itemCount*/ 0));
//			
//			//call endpoint with offset = -1 - expect a 400 response
//			validationList.add(new SectionToModuleToResourcesAll(scenarioName, instructorUser, 
//					route, /*expectedResponseCode*/ ResponseCode.BAD_REQUEST.value, 
//					/*expectedLR*/ null, /*expectedEnvelopeLevelLinks*/ null, 
//					/*offset*/ -1, /*limit*/ LRCount, /*itemCount*/ 0));
//			
//			//call endpoint with limit = -1 - expect a 400 response
//			validationList.add(new SectionToModuleToResourcesAll(scenarioName, instructorUser, 
//					route, /*expectedResponseCode*/ ResponseCode.BAD_REQUEST.value, 
//					/*expectedLR*/ null, /*expectedEnvelopeLevelLinks*/ null, 
//					/*offset*/ 0, /*limit*/ -1, /*itemCount*/ 0));
//			
//			//call endpoint with limit = 0 - expect a 400 response
//			validationList.add(new SectionToModuleToResourcesAll(scenarioName, instructorUser, 
//					route, /*expectedResponseCode*/ ResponseCode.BAD_REQUEST.value, 
//					/*expectedLR*/ null, /*expectedEnvelopeLevelLinks*/ null, 
//					/*offset*/ 0, /*limit*/ 0, /*itemCount*/ 0));
		}
		
		/*
		 * FROM KAT: If "stud" is null or if "resourceList" is null or empty,
		 * expectedResponseCode should be NOT_FOUND instead of OK (I fixed it)
		 */
		//one call that does not limit the returned collection (no offset or limit parameters)
		validationList.add(new SectionToModuleToResourcesAll(scenarioName, instructorUser, 
				route.toString(), expectedResponseCode, 
				resourceArray, /*expectedEnvelopeLevelLinks*/ null, 
				/*offset*/ null, /*limit*/ null, /*itemCount*/ LRCount));
	}
	
	private LearningResourceObject[] getArrayFromList(List<LearningResourceObject> resourceList) {
		LearningResourceObject[] resourceArray = new LearningResourceObject[resourceList.size()];
		int i=0;
		for (LearningResourceObject resource : resourceList) {
			resourceArray[i] = resource;
			i++;
		}
		return resourceArray;
	}
	
}