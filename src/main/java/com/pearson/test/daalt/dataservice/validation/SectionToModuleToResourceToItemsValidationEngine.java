package com.pearson.test.daalt.dataservice.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.pearson.qa.apex.dataobjects.EnvironmentType;
import com.pearson.qa.apex.dataobjects.UserObject;
import com.pearson.qa.apex.dataobjects.UserType;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.model.Answer;
import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Attempt;
import com.pearson.test.daalt.dataservice.model.BasicQuiz;
import com.pearson.test.daalt.dataservice.model.Chapter;
import com.pearson.test.daalt.dataservice.model.ChapterSection;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.EmbeddedQuestion;
import com.pearson.test.daalt.dataservice.model.MultiValueQuestion;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionPresentationFormat;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.model.SubAttempt;
import com.pearson.test.daalt.dataservice.model.SubQuestion;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.model.UnknownFormatQuestion;
import com.pearson.test.daalt.dataservice.response.model.AttemptObject;
import com.pearson.test.daalt.dataservice.response.model.LearningResourceItemObject;
import com.pearson.test.daalt.dataservice.response.model.ResponseObject;
import com.pearson.test.daalt.dataservice.response.model.TargetSubQuestionAnswerObject;
import com.pearson.test.daalt.dataservice.response.model.TargetSubQuestionAnswerResponseObject;
import com.pearson.test.daalt.dataservice.response.model.TargetSubQuestionObject;
import com.pearson.test.daalt.dataservice.response.model.TargetSubQuestionResponseObject;
import com.pearson.test.daalt.dataservice.validation.endpoint.SectionToModuleToResourceToItemsAll;
import com.pearson.test.daalt.dataservice.validation.endpoint.SectionToModuleToResourceToItemsPagination;

/**
 * endpoint 1.11
 */
public class SectionToModuleToResourceToItemsValidationEngine extends DaaltDataServiceValidationEngine {
	public List<Validation> getValidations(TestData data) throws InvalidTestDataException, JsonGenerationException, JsonMappingException, IOException {
//		String testVar = TestEngine.getInstance().getTestVar();
//		System.out.println("Value found by 1.11 validation engine: " + testVar);		
		
		List<Validation> validationList = new ArrayList<>();
		String baseUrl = TestEngine.getInstance().getBaseUrl();
		StringBuilder route = new StringBuilder();
		
		for (CourseSection courseSection : data.getAllCourseSections()) {
			if (courseSection.getInstructor() == null) {
				throw new InvalidTestDataException("Failed to generate ItemAnalysis validations - courseSection.instructor is null");
			} else if (courseSection.getInstructor().getUserName() == null) {
				throw new InvalidTestDataException("Failed to generate ItemAnalysis validations - instructor.userName is null");
			} else if (courseSection.getInstructor().getPassword() == null) {
				throw new InvalidTestDataException("Failed to generate ItemAnalysis validations - instructor.password is null");
			} else if (courseSection.getInstructor().getPersonId() == null) {
				throw new InvalidTestDataException("Failed to generate ItemAnalysis validations - instructor.personId is null");
			}
			UserObject instructorUser = new UserObject(courseSection.getInstructor().getUserName(), 
					courseSection.getInstructor().getPassword(), 
					UserType.Professor, EnvironmentType.Staging);
			instructorUser.setId(courseSection.getInstructor().getPersonId());

			UserObject studentUser = null;
			if (!courseSection.getEnrolledStudents().isEmpty()) {
				studentUser = new UserObject(courseSection.getEnrolledStudents().get(0).getUserName(), 
						courseSection.getEnrolledStudents().get(0).getPassword(), 
						UserType.Student, EnvironmentType.Staging);
				studentUser.setId(courseSection.getEnrolledStudents().get(0).getPersonId());
			}
			int expectedResponseCode = studentUser == null ? ResponseCode.NOT_FOUND.value : ResponseCode.OK.value;
			
			for (Assignment assignment : courseSection.getAssignments()) {
				for (Chapter chapter : assignment.getChapters()) {
					route = new StringBuilder();
					route.append(baseUrl).append("/platforms/").append(platformCode)
						.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
						.append("/resources/").append(chapter.getLearningResourceId()).append("/items");
					validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
							route.toString(), /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value,
							/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
							/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
					
					Quiz chapterQuiz = chapter.getChapterQuiz();
					if (chapterQuiz != null) {
						//if the quiz contains any "seeded" questions (Assessment_Item_Possible_Answers message sent),
						//add ALL questions from the quiz to the collection, else expect 404 response
						if (chapterQuiz.getSeededQuestions().size() > 0) {
							LearningResourceItemObject[] expectedItemCollection = new LearningResourceItemObject[chapterQuiz.getQuestions().size()];
							int i = 0;
							for (Question question : chapterQuiz.getQuestions()) {
								if(question instanceof MultiValueQuestion || question instanceof UnknownFormatQuestion) {
									LearningResourceItemObject item = getItemForMultiValueQuestion(courseSection, assignment, chapterQuiz, question);
									expectedItemCollection[i] = item;
									i++;
								} else {
									LearningResourceItemObject item = getItemForNonMultiValueQuestion(courseSection, assignment, chapterQuiz, question);
									expectedItemCollection[i] = item;
									i++;
								}
							}
							
//							List<LearningResourceItemObject> itemList = new ArrayList<LearningResourceItemObject>();
//							for(int t=0; t<expectedItemCollection.length; t++) {
//								itemList.add(expectedItemCollection[t]);
//							}
//							Collections.sort(itemList); 
//							expectedItemCollection = getArrayFromList(itemList);
							
							if(expectedItemCollection.length > 0) {
								//direct call : chapterQuiz
								route = new StringBuilder();
								route.append(baseUrl).append("/platforms/").append(platformCode)
									.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
									.append("/resources/").append(chapterQuiz.getLearningResourceId()).append("/items");
								
								int itemCount = expectedItemCollection == null ? 0 : expectedItemCollection.length;
//								int ii = 0;
//								for(LearningResourceItemObject LRItemObject : expectedItemCollection) {
//									LearningResourceItemObject[] thisItemcollection = new LearningResourceItemObject[1];
//									thisItemcollection[0] = LRItemObject;
//									validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
//											route.toString(), /*expectedResponseCode*/ expectedResponseCode, thisItemcollection,
//											/*expectedItemLevelLinks*/ null, 
//											/*offset*/ ii, /*limit*/ 1, /*itemCount*/ ItemCount));
//									ii++;
//								}
								validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
										route.toString(), /*expectedResponseCode*/ expectedResponseCode, expectedItemCollection,
										/*expectedItemLevelLinks*/ null, 
										/*offset*/ 0, /*limit*/ itemCount+1, /*itemCount*/ itemCount));
								
								//one call that does not limit the returned collection (no offset or limit parameters)
								validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
										route.toString(), expectedResponseCode, 
										expectedItemCollection, /*expectedEnvelopeLevelLinks*/ null, 
										/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
								
								//FROM KAT: Yes, we need to keep this validation
								if (itemCount > 15) {
									validationList.add(new SectionToModuleToResourceToItemsPagination(data.getTestScenarioName(), instructorUser, 
											route.toString()));
								}
								
								if (studentUser != null) {
									//students are not authorized to call endpoint 1.11
									validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), studentUser, 
											route.toString(), /*expectedResponseCode*/ ResponseCode.UNATHORIZED.value,
											/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
											/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
								}
								
								//nested call : chapter --> chapterQuiz
								route = new StringBuilder();
								route.append(baseUrl).append("/platforms/").append(platformCode)
									.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
									.append("/resources/").append(chapter.getId()).append("/resources/").append(chapterQuiz.getLearningResourceId()).append("/items");
								
//								ii = 0;
//								for(LearningResourceItemObject LRItemObject : expectedItemCollection) {
//									LearningResourceItemObject[] thisItemcollection = new LearningResourceItemObject[1];
//									thisItemcollection[0] = LRItemObject;
//									validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
//											route.toString(), /*expectedResponseCode*/ expectedResponseCode, thisItemcollection,
//											/*expectedItemLevelLinks*/ null, 
//											/*offset*/ ii, /*limit*/ 1, /*itemCount*/ ItemCount));
//									ii++;
//								}
								validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
										route.toString(), /*expectedResponseCode*/ expectedResponseCode, expectedItemCollection,
										/*expectedItemLevelLinks*/ null, 
										/*offset*/ 0, /*limit*/ itemCount+1, /*itemCount*/ itemCount));
								
								//one call that does not limit the returned collection (no offset or limit parameters)
								validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
										route.toString(), expectedResponseCode, 
										expectedItemCollection, /*expectedEnvelopeLevelLinks*/ null, 
										/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
								
								//FROM KAT: Yes, we need to keep this validation
								if (itemCount > 15) {
									validationList.add(new SectionToModuleToResourceToItemsPagination(data.getTestScenarioName(), instructorUser, 
											route.toString()));
								}
								
								if (studentUser != null) {
									//students are not authorized to call endpoint 1.11
									validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), studentUser, 
											route.toString(), /*expectedResponseCode*/ ResponseCode.UNATHORIZED.value,
											/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
											/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
								}
							} 
						} else {
							//direct call : chapterQuiz
							route = new StringBuilder();
							route.append(baseUrl).append("/platforms/").append(platformCode)
								.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
								.append("/resources/").append(chapterQuiz.getLearningResourceId()).append("/items");
							validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
									route.toString(), ResponseCode.NOT_FOUND.value,
									/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
									/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
							
							//nested call : chapter --> chapterQuiz
							route = new StringBuilder();
							route.append(baseUrl).append("/platforms/").append(platformCode)
								.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
								.append("/resources/").append(chapter.getId()).append("/resources/").append(chapterQuiz.getLearningResourceId()).append("/items");
							validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
									route.toString(), ResponseCode.NOT_FOUND.value,
									/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
									/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
						}
						
						if (chapterQuiz.getNestedQuizzes() != null) {							
							for (Quiz nestedQuiz : chapterQuiz.getNestedQuizzes()) {
								//if the quiz contains any "seeded" questions (Assessment_Item_Possible_Answers message sent),
								//add ALL questions from the quiz to the collection, else expect 404 response
								if (nestedQuiz.getSeededQuestions().size() > 0) {
									LearningResourceItemObject[] expectedNestedItemCollection = new LearningResourceItemObject[nestedQuiz.getQuestions().size()]; 
									int j = 0;
									for (Question question : nestedQuiz.getQuestions()) { 
										if(question instanceof MultiValueQuestion || question instanceof UnknownFormatQuestion) {
											LearningResourceItemObject item = getItemForMultiValueQuestion(courseSection, assignment, nestedQuiz, question);
											expectedNestedItemCollection[j] = item;
											j++;
										} else {
											expectedNestedItemCollection = new LearningResourceItemObject[1];
											LearningResourceItemObject item = getItemForNonMultiValueQuestion(courseSection, assignment, nestedQuiz, question);
											expectedNestedItemCollection[j] = item;
											j++;
										}
									}
									

//									List<LearningResourceItemObject> itemList = new ArrayList<LearningResourceItemObject>();
//									for(int t=0; t<expectedNestedItemCollection.length; t++) {
//										itemList.add(expectedNestedItemCollection[t]);
//									}
//									Collections.sort(itemList); 
//									expectedNestedItemCollection = getArrayFromList(itemList);
									
									
									if(expectedNestedItemCollection.length > 0) {
										//direct call : nestedQuiz
										route = new StringBuilder();
										route.append(baseUrl).append("/platforms/").append(platformCode)
											.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
											.append("/resources/").append(nestedQuiz.getLearningResourceId()).append("/items");
										
										int itemCount = expectedNestedItemCollection == null ? 0 : expectedNestedItemCollection.length;
//										int ii = 0;
//										for(LearningResourceItemObject LRItemObject : expectedNestedItemCollection) {
//											LearningResourceItemObject[] thisItemcollection = new LearningResourceItemObject[1];
//											thisItemcollection[0] = LRItemObject;
//											validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
//													route.toString(), /*expectedResponseCode*/ expectedResponseCode, thisItemcollection,
//													/*expectedItemLevelLinks*/ null, 
//													/*offset*/ ii, /*limit*/ 1, /*itemCount*/ ItemCount));
//											ii++;
//										}
										validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
												route.toString(), /*expectedResponseCode*/ expectedResponseCode, expectedNestedItemCollection,
												/*expectedItemLevelLinks*/ null, 
												/*offset*/ 0, /*limit*/ itemCount+1, /*itemCount*/ itemCount));
										
										//one call that does not limit the returned collection (no offset or limit parameters)
										validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
												route.toString(), expectedResponseCode, 
												expectedNestedItemCollection, /*expectedEnvelopeLevelLinks*/ null, 
												/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
										
										//FROM KAT: Yes, we need to keep this validation
										if (itemCount > 15) {
											validationList.add(new SectionToModuleToResourceToItemsPagination(data.getTestScenarioName(), instructorUser, 
													route.toString()));
										}
										
										if (studentUser != null) {
											//students are not authorized to call endpoint 1.11
											validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), studentUser, 
													route.toString(), /*expectedResponseCode*/ ResponseCode.UNATHORIZED.value,
													/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
													/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
										}
										
										//nested call : chapter --> chapterQuiz --> nestedQuiz
										route = new StringBuilder();
										route.append(baseUrl).append("/platforms/").append(platformCode)
											.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
											.append("/resources/").append(chapter.getId()).append("/resources/").append(chapterQuiz.getLearningResourceId())
											.append("/resources/").append(nestedQuiz.getLearningResourceId()).append("/items");
//										ii = 0;
//										for(LearningResourceItemObject LRItemObject : expectedNestedItemCollection) {
//											LearningResourceItemObject[] thisItemcollection = new LearningResourceItemObject[1];
//											thisItemcollection[0] = LRItemObject;
//											validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
//													route.toString(), /*expectedResponseCode*/ expectedResponseCode, thisItemcollection,
//													/*expectedItemLevelLinks*/ null, 
//													/*offset*/ ii, /*limit*/ 1, /*itemCount*/ itemCount));
//											ii++;
//										}
										validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
												route.toString(), /*expectedResponseCode*/ expectedResponseCode, expectedNestedItemCollection,
												/*expectedItemLevelLinks*/ null, 
												/*offset*/ 0, /*limit*/ itemCount+1, /*itemCount*/ itemCount));
										
										//one call that does not limit the returned collection (no offset or limit parameters)
										validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
												route.toString(), expectedResponseCode, 
												expectedNestedItemCollection, /*expectedEnvelopeLevelLinks*/ null, 
												/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
										
										//FROM KAT: Yes, we need to keep this validation
										if (itemCount > 15) {
											validationList.add(new SectionToModuleToResourceToItemsPagination(data.getTestScenarioName(), instructorUser, 
													route.toString()));
										}
										
										if (studentUser != null) {
											//students are not authorized to call endpoint 1.11
											validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), studentUser, 
													route.toString(), /*expectedResponseCode*/ ResponseCode.UNATHORIZED.value,
													/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
													/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
										}
									}
								} else {
									//direct call : nestedQuiz
									route = new StringBuilder();
									route.append(baseUrl).append("/platforms/").append(platformCode)
										.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
										.append("/resources/").append(nestedQuiz.getLearningResourceId()).append("/items");
									validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
											route.toString(),  ResponseCode.NOT_FOUND.value,
											/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
											/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
									
									//nested call : chapter --> chapterQuiz --> nestedQuiz
									route = new StringBuilder();
									route.append(baseUrl).append("/platforms/").append(platformCode)
										.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
										.append("/resources/").append(chapter.getId()).append("/resources/").append(chapterQuiz.getLearningResourceId())
										.append("/resources/").append(nestedQuiz.getLearningResourceId()).append("/items");
									validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
											route.toString(),  ResponseCode.NOT_FOUND.value,
											/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
											/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
								}
							}
						}
					}
					if (chapter.getChapterSections() != null) {
						for (ChapterSection chapterSection : chapter.getChapterSections()) {
							//direct call : chapterSection
							route = new StringBuilder();
							route.append(baseUrl).append("/platforms/").append(platformCode)
								.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
								.append("/resources/").append(chapterSection.getLearningResourceId()).append("/items");
							validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
									route.toString(), /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value,
									/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
									/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
							
							//nested call : chapter --> chapterSection
							route = new StringBuilder();
							route.append(baseUrl).append("/platforms/").append(platformCode)
								.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
								.append("/resources/").append(chapter.getLearningResourceId())
								.append("/resources/").append(chapterSection.getLearningResourceId()).append("/items");
							validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
									route.toString(), /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value,
									/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
									/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
							
							Quiz chapterSectionQuiz = chapterSection.getChapterSectionQuiz();
							if (chapterSectionQuiz != null) {
								//if the quiz contains any "seeded" questions (Assessment_Item_Possible_Answers message sent),
								//add ALL questions from the quiz to the collection, else expect 404 response
								if (chapterSectionQuiz.getSeededQuestions().size() > 0) {
									LearningResourceItemObject[] expectedItemCollection = new LearningResourceItemObject[chapterSectionQuiz.getQuestions().size()];
									int i = 0;
									for (Question question : chapterSectionQuiz.getQuestions()) {
										if(question instanceof MultiValueQuestion || question instanceof UnknownFormatQuestion) {
											LearningResourceItemObject item = getItemForMultiValueQuestion(courseSection, assignment, 
													chapterSectionQuiz, question);
											expectedItemCollection[i] = item;
											i++;
										} else {
											expectedItemCollection = new LearningResourceItemObject[1];
											LearningResourceItemObject item = getItemForNonMultiValueQuestion(courseSection, assignment, 
													chapterSectionQuiz, question);
											expectedItemCollection[i] = item;
											i++;
										}
									}
									

//									List<LearningResourceItemObject> itemList = new ArrayList<LearningResourceItemObject>();
//									for(int t=0; t<expectedItemCollection.length; t++) {
//										itemList.add(expectedItemCollection[t]);
//									}
//									Collections.sort(itemList); 
//									expectedItemCollection = getArrayFromList(itemList);
									
									
									if(expectedItemCollection.length > 0) {
										//direct call : chapterSectionQuiz
										route = new StringBuilder();
										route.append(baseUrl).append("/platforms/").append(platformCode)
											.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
											.append("/resources/").append(chapterSectionQuiz.getLearningResourceId()).append("/items");
										
										int itemCount = expectedItemCollection == null ? 0 : expectedItemCollection.length;
//										int ii = 0;
//										for(LearningResourceItemObject LRItemObject : expectedItemCollection) {
//											LearningResourceItemObject[] thisItemcollection = new LearningResourceItemObject[1];
//											thisItemcollection[0] = LRItemObject;
//											validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
//													route.toString(), /*expectedResponseCode*/ expectedResponseCode, thisItemcollection,
//													/*expectedItemLevelLinks*/ null, 
//													/*offset*/ ii, /*limit*/ 1, /*itemCount*/ ItemCount));
//											ii++;
//										}
										validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
												route.toString(), /*expectedResponseCode*/ expectedResponseCode, expectedItemCollection,
												/*expectedItemLevelLinks*/ null, 
												/*offset*/ 0, /*limit*/ itemCount+1, /*itemCount*/ itemCount));
										
										//one call that does not limit the returned collection (no offset or limit parameters)
										validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
												route.toString(), expectedResponseCode, 
												expectedItemCollection, /*expectedEnvelopeLevelLinks*/ null, 
												/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
										
										//FROM KAT: Yes, we need to keep this validation
										if (itemCount > 15) {
											validationList.add(new SectionToModuleToResourceToItemsPagination(data.getTestScenarioName(), instructorUser, 
													route.toString()));
										}
										
										if (studentUser != null) {
											//students are not authorized to call endpoint 1.11
											validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), studentUser, 
													route.toString(), /*expectedResponseCode*/ ResponseCode.UNATHORIZED.value,
													/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
													/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
										}
										
										//nested call : chapter --> chapterSection --> chapterSectionQuiz
										route = new StringBuilder();
										route.append(baseUrl).append("/platforms/").append(platformCode)
											.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
											.append("/resources/").append(chapter.getId()).append("/resources/").append(chapterSection.getId())
											.append("/resources/").append(chapterSectionQuiz.getLearningResourceId()).append("/items");
//										ii = 0;
//										for(LearningResourceItemObject LRItemObject : expectedItemCollection) {
//											LearningResourceItemObject[] thisItemcollection = new LearningResourceItemObject[1];
//											thisItemcollection[0] = LRItemObject;
//											validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
//													route.toString(), /*expectedResponseCode*/ expectedResponseCode, thisItemcollection,
//													/*expectedItemLevelLinks*/ null, 
//													/*offset*/ ii, /*limit*/ 1, /*itemCount*/ itemCount));
//											ii++;
//										}
										validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
												route.toString(), /*expectedResponseCode*/ expectedResponseCode, expectedItemCollection,
												/*expectedItemLevelLinks*/ null, 
												/*offset*/ 0, /*limit*/ itemCount+1, /*itemCount*/ itemCount));
										
										//one call that does not limit the returned collection (no offset or limit parameters)
										validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
												route.toString(), expectedResponseCode, 
												expectedItemCollection, /*expectedEnvelopeLevelLinks*/ null, 
												/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
										
										//FROM KAT: Yes, we need to keep this validation
										if (itemCount > 15) {
											validationList.add(new SectionToModuleToResourceToItemsPagination(data.getTestScenarioName(), instructorUser, 
													route.toString()));
										}
										if (studentUser != null) {
											//students are not authorized to call endpoint 1.11
											validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), studentUser, 
													route.toString(), /*expectedResponseCode*/ ResponseCode.UNATHORIZED.value,
													/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
													/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
										}
									} 
								} else {
									//direct call : chapterSectionQuiz
									route = new StringBuilder();
									route.append(baseUrl).append("/platforms/").append(platformCode)
										.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
										.append("/resources/").append(chapterSectionQuiz.getLearningResourceId()).append("/items");
									validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
											route.toString(),  ResponseCode.NOT_FOUND.value,
											/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
											/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
									
									//nested call : chapter --> chapterSection --> chapterSectionQuiz
									route = new StringBuilder();
									route.append(baseUrl).append("/platforms/").append(platformCode)
										.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
										.append("/resources/").append(chapter.getId()).append("/resources/").append(chapterSection.getId())
										.append("/resources/").append(chapterSectionQuiz.getLearningResourceId()).append("/items");
									validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
											route.toString(),  ResponseCode.NOT_FOUND.value,
											/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
											/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
								}
							}
							
							if (chapterSection.getPages() != null) {
								//add embedded wrapping Quizzes to quizMap
								Map<String, Quiz> quizMap = new HashMap<>();
								for (Page page : chapterSection.getPages()){
									if(page.getEmbeddedQuestions() != null) {
										for (Question embeddedQuestion : page.getEmbeddedQuestions()) {
											if(!quizMap.containsKey(embeddedQuestion.getAssessmentId())) {
												Quiz newQuiz = new BasicQuiz();
												newQuiz.setId(embeddedQuestion.getAssessmentId());
												newQuiz.setSeedDateTime(embeddedQuestion.getQuestionLastSeedDateTime());
												quizMap.put(embeddedQuestion.getAssessmentId(), newQuiz);
											}
											Quiz wrappingQuiz = quizMap.get(embeddedQuestion.getAssessmentId());
											if (!wrappingQuiz.containsQuestionWithId(embeddedQuestion.getId())) {
												wrappingQuiz.addQuestion(embeddedQuestion);
											}
										}
									}
								}
								
								for (Page page : chapterSection.getPages()){
									//direct call : page
									route = new StringBuilder();
									route.append(baseUrl).append("/platforms/").append(platformCode)
										.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
										.append("/resources/").append(chapterSection.getLearningResourceId()).append("/items");
									validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
											route.toString(), /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value,
											/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
											/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
									
									//nested call : chapter --> chapterSection --> page
									route = new StringBuilder();
									route.append(baseUrl).append("/platforms/").append(platformCode)
										.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
										.append("/resources/").append(chapter.getLearningResourceId())
										.append("/resources/").append(chapterSection.getLearningResourceId())
										.append("/resources/").append(page.getLearningResourceId()).append("/items");
									validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
											route.toString(), /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value,
											/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
											/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
									
									if(page.getEmbeddedQuestions() != null) {
										for (Question embeddedQuestion : page.getEmbeddedQuestions()) {
											EmbeddedQuestion embeddedQuestionLR = page.getEmbeddedQuestionLR(embeddedQuestion, embeddedQuestion.getSequenceNumber());
											Quiz quiz = quizMap.get(embeddedQuestion.getAssessmentId());
											LearningResourceItemObject[] expectedItemCollection = new LearningResourceItemObject[quiz.getQuestions().size()];
											int i = 0;
											for (Question question : quiz.getQuestions()) {
												quiz.setLearningResourceId(embeddedQuestionLR.getLearningResourceId());
												if(question instanceof MultiValueQuestion || question instanceof UnknownFormatQuestion) {
													LearningResourceItemObject item = getItemForMultiValueQuestion(courseSection, assignment, 
															quiz, question);
													expectedItemCollection[i] = item;
													i++;
												} else {
													LearningResourceItemObject item = getItemForNonMultiValueQuestion(courseSection, assignment, 
															quiz, question);
													expectedItemCollection[i] = item;
													i++;
												}
											}
											
											if(expectedItemCollection.length > 0) {
												//direct call : embedded question
												route = new StringBuilder();
												route.append(baseUrl).append("/platforms/").append(platformCode)
													.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
													.append("/resources/").append(embeddedQuestionLR.getLearningResourceId()).append("/items");
												int itemCount = expectedItemCollection == null ? 0 : expectedItemCollection.length;
//												int ii = 0;
//												for(LearningResourceItemObject LRItemObject : expectedItemCollection) {
//													LearningResourceItemObject[] thisItemcollection = new LearningResourceItemObject[1];
//													thisItemcollection[0] = LRItemObject;
//													validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
//															route.toString(), /*expectedResponseCode*/ expectedResponseCode, thisItemcollection,
//															/*expectedItemLevelLinks*/ null, 
//															/*offset*/ ii, /*limit*/ 1, /*itemCount*/ ItemCount));
//													ii++;
//												}
												validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
														route.toString(), /*expectedResponseCode*/ expectedResponseCode, expectedItemCollection,
														/*expectedItemLevelLinks*/ null, 
														/*offset*/ 0, /*limit*/ itemCount+1, /*itemCount*/ itemCount));
												
												//one call that does not limit the returned collection (no offset or limit parameters)
												validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
														route.toString(), expectedResponseCode, 
														expectedItemCollection, /*expectedEnvelopeLevelLinks*/ null, 
														/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
												
												//FROM KAT: Yes, we need to keep this validation
												if (itemCount > 15) {
													validationList.add(new SectionToModuleToResourceToItemsPagination(data.getTestScenarioName(), instructorUser, 
															route.toString()));
												}
												if (studentUser != null) {
													//students are not authorized to call endpoint 1.11
													validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), studentUser, 
															route.toString(), /*expectedResponseCode*/ ResponseCode.UNATHORIZED.value,
															/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
															/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
												}
												
												//nested call : chapter --> chapterSection --> page --> embedded question
												route = new StringBuilder();
												route.append(baseUrl).append("/platforms/").append(platformCode)
													.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
													.append("/resources/").append(chapter.getId()).append("/resources/").append(chapterSection.getId())
													.append("/resources/").append(page.getId())
													.append("/resources/").append(embeddedQuestionLR.getLearningResourceId()).append("/items");
//												ii = 0;
//												for(LearningResourceItemObject LRItemObject : expectedItemCollection) {
//													LearningResourceItemObject[] thisItemcollection = new LearningResourceItemObject[1];
//													thisItemcollection[0] = LRItemObject;
//													validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
//															route.toString(), /*expectedResponseCode*/ expectedResponseCode, thisItemcollection,
//															/*expectedItemLevelLinks*/ null, 
//															/*offset*/ ii, /*limit*/ 1, /*itemCount*/ itemCount));
//													ii++;
//												}
												validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
														route.toString(), /*expectedResponseCode*/ expectedResponseCode, expectedItemCollection,
														/*expectedItemLevelLinks*/ null, 
														/*offset*/ 0, /*limit*/ itemCount+1, /*itemCount*/ itemCount));
												
												//one call that does not limit the returned collection (no offset or limit parameters)
												validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
														route.toString(), expectedResponseCode, 
														expectedItemCollection, /*expectedEnvelopeLevelLinks*/ null, 
														/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
												
												//FROM KAT: Yes, we need to keep this validation
												if (itemCount > 15) {
													validationList.add(new SectionToModuleToResourceToItemsPagination(data.getTestScenarioName(), instructorUser, 
															route.toString()));
												}
												if (studentUser != null) {
													//students are not authorized to call endpoint 1.11
													validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), studentUser, 
															route.toString(), /*expectedResponseCode*/ ResponseCode.UNATHORIZED.value,
															/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
															/*offset*/ null, /*limit*/ null, /*itemCount*/ itemCount));
												}
											}
										}
									}
									
									if (page.getPages() != null) {
										for (Page nestedPage : page.getPages()) {
											//direct call : nestedPage
											route = new StringBuilder();
											route.append(baseUrl).append("/platforms/").append(platformCode)
												.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
												.append("/resources/").append(nestedPage.getLearningResourceId()).append("/items");
											validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
													route.toString(), /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value,
													/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
													/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
											
											//nested call : chapter --> chapterSection --> page --> nestedPage
											route = new StringBuilder();
											route.append(baseUrl).append("/platforms/").append(platformCode)
												.append("/sections/").append(courseSection.getId()).append("/modules/").append(assignment.getId())
												.append("/resources/").append(chapter.getLearningResourceId())
												.append("/resources/").append(chapterSection.getLearningResourceId())
												.append("/resources/").append(page.getLearningResourceId())
												.append("/resources/").append(nestedPage.getLearningResourceId()).append("/items");
											validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
													route.toString(), /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value,
													/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
													/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
										}
									}
								}
							}
						}
					}
				}
			}
			
			if (courseSection.getAssignments().isEmpty()) {
				route = new StringBuilder();
				route.append(baseUrl).append("/platforms/").append(platformCode)
					.append("/sections/").append(courseSection.getId()).append("/modules/").append("SQE-dummy-learning-module-id")
					.append("/resources/").append("SQE-dummy-assessment-learning-resource-id").append("/items");
				validationList.add(new SectionToModuleToResourceToItemsAll(data.getTestScenarioName(), instructorUser, 
						route.toString(), /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value,
						/*expectedItem*/ null, /*expectedEnvelopeLevelLinks*/ null, 
						/*offset*/ null, /*limit*/ null, /*itemCount*/ 0));
			}
		}
		
		return validationList;
	}

	private LearningResourceItemObject getItemForMultiValueQuestion(CourseSection courseSection, Assignment assignment,
			Quiz quiz, Question question) throws InvalidTestDataException {
		if (platformCode == null) {
			throw new InvalidTestDataException(
					"Failed to generate SectionToModuleToResourceToItems validations - platformId is null");
		} else if (courseSection.getId() == null) {
			throw new InvalidTestDataException(
					"Failed to generate SectionToModuleToResourceToItems validations - courseSectionId is null");
		} else if (assignment.getId() == null) {
			throw new InvalidTestDataException(
					"Failed to generate SectionToModuleToResourceToItems validations - learningModuleId is null");
		} else if (quiz.getLearningResourceId() == null) {
			throw new InvalidTestDataException(
					"Failed to generate SectionToModuleToResourceToItems validations - learningResourceId is null");
		} else if (quiz.getId() == null) {
			throw new InvalidTestDataException(
					"Failed to generate SectionToModuleToResourceToItems validations - assessmentId is null");
		} else if (question.getId() == null) {
			throw new InvalidTestDataException(
					"Failed to generate SectionToModuleToResourceToItems validations - itemId is null");
		}		


		int studentsCompletedQuestionCount = 0;
		for (Student stud : courseSection.getEnrolledStudents()) {
			if (question.studentCompletedQuestion(stud)) {
				studentsCompletedQuestionCount++;
			}
		}
		LearningResourceItemObject item = new LearningResourceItemObject();
		item.platformId = platformCode;
		item.courseSectionId = courseSection.getId();
		item.learningModuleId = assignment.getId();
		item.learningResourceId = quiz.getLearningResourceId();
		item.assessmentId = quiz.getId();
		item.itemId = question.getId();
		item.itemSequence = (int) Math.floor(question.getSequenceNumber());
		item.questionType = question.getQuestionType();
		item.questionPresentationFormat = question.getQuestionPresentationFormat();
		item.questionText = question.getText();
		item.pointsPossible = studentsCompletedQuestionCount > 0 ? question.getPointsPossible() : 0;
		item.courseSectionStudentCount = courseSection.getEnrolledStudents().size();
		int correctStudentCount = 0;
		for (Student stud : courseSection.getEnrolledStudents()) {
			if (question.studentCompletedQuestion(stud) && question.studentAnsweredQuestionCorrectly(stud)) {
				correctStudentCount++;
			}
		}
		int noAttemptStudentCount = 0;
		for (Student stud : courseSection.getEnrolledStudents()) {
			if (!question.studentAttemptedQuestion(stud)) {
				noAttemptStudentCount++;
			}
		}
		int incorrectStudentCount = courseSection.getEnrolledStudentCount() - correctStudentCount - noAttemptStudentCount;
		
		item.assessmentItemCompletedStudentCount = studentsCompletedQuestionCount;
		
		item.correctStudentCount = correctStudentCount;
		item.correctStudentPercent = studentsCompletedQuestionCount > 0 ? ((float) correctStudentCount
				/ (float) studentsCompletedQuestionCount * 100) : 0;
		item.incorrectStudentCount = incorrectStudentCount;
		item.noAttemptStudentCount = noAttemptStudentCount;
		float totalItemResponseScore = question.getTotalPointsEarnedFinal();
		item.totalItemResponseScore = totalItemResponseScore;
		item.avgItemResponseScore = studentsCompletedQuestionCount > 0 ? (float) totalItemResponseScore
				/ (float) studentsCompletedQuestionCount : 0;

		long totalTimeSpentAssessing = question.getCompletedTotalAssessmentTime();
		item.totalTimeSpentAssessing = formatTimeOnTask(totalTimeSpentAssessing * 1000);
		float avgTimeSpentAssessing = studentsCompletedQuestionCount > 0 ? (float) totalTimeSpentAssessing
				/ (float) studentsCompletedQuestionCount : 0;

		item.avgTimeSpentAssessing = formatTimeOnTask((long) (avgTimeSpentAssessing * 1000));

		item.medianTimeSpentAssessing = formatTimeOnTask((long) (question.getMedianAssessmentTime() * 1000));

		List<AttemptObject> itemAttempts = buildAttemptsForQuestion(question);
		item.attempts = new AttemptObject[itemAttempts.size()];
		int nextAttemptIdx = 0;
		for (AttemptObject attemptObj : itemAttempts) {
			item.attempts[nextAttemptIdx] = attemptObj;
			nextAttemptIdx++;
		}
		
		if (question.getAutoSaveActivities() == null && question.getCompletionActivities() == null && question.getLeaveQuestions()==null) {
			// if there is no activity for this question, then all the student will be counted as noAttempt, even if we send ToT tincan messages.
			item.correctStudentCount = 0;
			item.incorrectStudentCount = 0;
			item.noAttemptStudentCount = courseSection.getEnrolledStudentCount();
			item.attempts = new AttemptObject[0];
		}
		
		return item;
	}
	
	private LearningResourceItemObject getItemForNonMultiValueQuestion(CourseSection courseSection, Assignment assignment,
			Quiz quiz, Question question) throws InvalidTestDataException {
		if (platformCode == null) {
			throw new InvalidTestDataException(
					"Failed to generate SectionToModuleToResourceToItems validations - platformId is null");
		} else if (courseSection.getId() == null) {
			throw new InvalidTestDataException(
					"Failed to generate SectionToModuleToResourceToItems validations - courseSectionId is null");
		} else if (assignment.getId() == null) {
			throw new InvalidTestDataException(
					"Failed to generate SectionToModuleToResourceToItems validations - learningModuleId is null");
		} else if (quiz.getLearningResourceId() == null) {
			throw new InvalidTestDataException(
					"Failed to generate SectionToModuleToResourceToItems validations - learningResourceId is null");
		} else if (quiz.getId() == null) {
			throw new InvalidTestDataException(
					"Failed to generate SectionToModuleToResourceToItems validations - assessmentId is null");
		} else if (question.getId() == null) {
			throw new InvalidTestDataException(
					"Failed to generate SectionToModuleToResourceToItems validations - itemId is null");
		}

		
		LearningResourceItemObject item = new LearningResourceItemObject();
		item.platformId = platformCode;
		item.courseSectionId = courseSection.getId();
		item.learningModuleId = assignment.getId();
		item.learningResourceId = quiz.getLearningResourceId();
		item.assessmentId = quiz.getId();
		item.itemId = question.getId();
		item.itemSequence = (int) Math.floor(question.getSequenceNumber());
		item.questionType = question.getQuestionType();
		item.questionPresentationFormat = question.getQuestionPresentationFormat();
		item.questionText = null;
		
		int studentsCompletedQuestionCount = 0;
		for (Student stud : courseSection.getEnrolledStudents()) {
			if (question.studentCompletedQuestion(stud)) {
				studentsCompletedQuestionCount++;
			}
		}
		item.pointsPossible = studentsCompletedQuestionCount > 0 ? question.getPointsPossible() : 0;
		item.courseSectionStudentCount = courseSection.getEnrolledStudents().size();
		item.assessmentItemCompletedStudentCount = studentsCompletedQuestionCount;
		item.correctStudentCount = 0;
		item.correctStudentPercent = 0;
		item.incorrectStudentCount = 0;
		item.noAttemptStudentCount = 1; // default value in 1.11 response
		item.totalItemResponseScore = 0;
		item.avgItemResponseScore = 0;
		
		long totalTimeSpentAssessing = question.getCompletedTotalAssessmentTime();
		item.totalTimeSpentAssessing = formatTimeOnTask(totalTimeSpentAssessing * 1000);
		float avgTimeSpentAssessing = studentsCompletedQuestionCount > 0 ? (float) totalTimeSpentAssessing
				/ (float) studentsCompletedQuestionCount : 0;

		item.avgTimeSpentAssessing = formatTimeOnTask((long) (avgTimeSpentAssessing * 1000));

		item.medianTimeSpentAssessing = formatTimeOnTask((long) (question.getMedianAssessmentTime() * 1000));
		
		item.attempts = new AttemptObject[0];
		
		return item;
	}

	private List<AttemptObject> buildAttemptsForQuestion(Question question) {
		List<AttemptObject> toReturn = new ArrayList<>();
		if (question.getAttempts() != null && question.getIsQuestionSeeded()) {
			for (Attempt attempt : question.getAttempts()) {
				AttemptObject currentItemAttempt = null;
				for (AttemptObject itemAttempt : toReturn) {
					if (itemAttempt.attemptNumber == attempt.getAttemptNumber()) {
						currentItemAttempt = itemAttempt;
					}
				}

				if (currentItemAttempt == null) {
					currentItemAttempt = new AttemptObject();
					currentItemAttempt.attemptNumber = attempt.getAttemptNumber();
					currentItemAttempt.attemptNumberStudentCount = 0;
					if (question.getQuestionPresentationFormat().equalsIgnoreCase(QuestionPresentationFormat.FILL_IN_THE_BLANK.value)
							|| question.getQuestionPresentationFormat().equalsIgnoreCase(QuestionPresentationFormat.NUMERIC.value)) {
						currentItemAttempt.targetSubQuestions = new TargetSubQuestionObject[0];
					} else {
						currentItemAttempt.targetSubQuestions = new TargetSubQuestionObject[question.getSubQuestions()
							.size()];
					}
					int index = 0;
					
					if (!question.getQuestionPresentationFormat().equalsIgnoreCase(QuestionPresentationFormat.FILL_IN_THE_BLANK.value)
							&& !question.getQuestionPresentationFormat().equalsIgnoreCase(QuestionPresentationFormat.NUMERIC.value)) {
						for (SubQuestion subQ : question.getSubQuestions()) {
							TargetSubQuestionObject targetSubQObj = new TargetSubQuestionObject();
							targetSubQObj.targetSubQuestionId = subQ.getId();
							targetSubQObj.targetSubQuestionText = subQ.getText();
							targetSubQObj.targetSubQuestionResponses = new TargetSubQuestionResponseObject[2];
							{
								targetSubQObj.targetSubQuestionResponses[0] = new TargetSubQuestionResponseObject();
								targetSubQObj.targetSubQuestionResponses[0].targetSubQuestionResponseCode = "Correct";
								targetSubQObj.targetSubQuestionResponses[0].targetSubQuestionResponseStudentCount = 0;
								targetSubQObj.targetSubQuestionResponses[0].targetSubQuestionResponsePercent = 0;
								targetSubQObj.targetSubQuestionResponses[1] = new TargetSubQuestionResponseObject();
								targetSubQObj.targetSubQuestionResponses[1].targetSubQuestionResponseCode = "Incorrect";
								targetSubQObj.targetSubQuestionResponses[1].targetSubQuestionResponseStudentCount = 0;
								targetSubQObj.targetSubQuestionResponses[1].targetSubQuestionResponsePercent = 0;
							}
							
							targetSubQObj.targetSubQuestionAnswers= new TargetSubQuestionAnswerObject[subQ.getAnswers().size()];
							int i = 0;
							for (Answer ans : subQ.getAnswers()) {
								TargetSubQuestionAnswerObject targetSubQuestionAnswer = new TargetSubQuestionAnswerObject();
								targetSubQObj.targetSubQuestionAnswers[i] = targetSubQuestionAnswer;
								targetSubQObj.targetSubQuestionAnswers[i].targetSubQuestionAnswerId = ans.getId();
								targetSubQObj.targetSubQuestionAnswers[i].targetSubQuestionAnswerText = ans.getText();
								targetSubQObj.targetSubQuestionAnswers[i].targetSubQuestionAnswerCorrectFlag = ans.isCorrectAnswer();
								targetSubQObj.targetSubQuestionAnswers[i].targetSubQuestionAnswerResponses = new TargetSubQuestionAnswerResponseObject[2];
								
								{		// DDS-1544 Since Response size is hard coded, I set the value directly instead of putting in a loop
									targetSubQObj.targetSubQuestionAnswers[i].targetSubQuestionAnswerResponses[0] = new TargetSubQuestionAnswerResponseObject();
									targetSubQObj.targetSubQuestionAnswers[i].targetSubQuestionAnswerResponses[0].targetSubQuestionAnswerResponseCode = "Correct";
									targetSubQObj.targetSubQuestionAnswers[i].targetSubQuestionAnswerResponses[0].targetSubQuestionAnswerResponseStudentCount = 0;
									targetSubQObj.targetSubQuestionAnswers[i].targetSubQuestionAnswerResponses[0].targetSubQuestionAnswerResponsePercent = 0;
									targetSubQObj.targetSubQuestionAnswers[i].targetSubQuestionAnswerResponses[1] = new TargetSubQuestionAnswerResponseObject();
									targetSubQObj.targetSubQuestionAnswers[i].targetSubQuestionAnswerResponses[1].targetSubQuestionAnswerResponseCode = "Incorrect";
									targetSubQObj.targetSubQuestionAnswers[i].targetSubQuestionAnswerResponses[1].targetSubQuestionAnswerResponseStudentCount = 0;
									targetSubQObj.targetSubQuestionAnswers[i].targetSubQuestionAnswerResponses[1].targetSubQuestionAnswerResponsePercent = 0;
								}
								
								i++;
							}
							currentItemAttempt.targetSubQuestions[index++] = targetSubQObj;
						}
					}
					
					toReturn.add(currentItemAttempt);
				}

				currentItemAttempt.attemptNumberStudentCount++;
				ResponseObject currentResponse = currentItemAttempt
						.getResponseByCode(attempt.getAnswerCorrectness().value);
				if (currentResponse == null) {
					currentResponse = new ResponseObject();
					currentResponse.responseCode = attempt.getAnswerCorrectness().value;
					currentResponse.responseStudentCount = 0;
					currentResponse.responsePercent = 0;
					if (currentItemAttempt.responses == null) {
						currentItemAttempt.responses = new ResponseObject[1];
						currentItemAttempt.responses[0] = currentResponse;
					} else {
						ResponseObject[] tempResp = new ResponseObject[currentItemAttempt.responses.length + 1];
						int i = 0;
						while (i < currentItemAttempt.responses.length) {
							tempResp[i] = currentItemAttempt.responses[i];
							i++;
						}
						tempResp[i] = currentResponse;
						currentItemAttempt.responses = tempResp;
					}
				}
			
				currentResponse.responseStudentCount++;
				for(int i=0; i<currentItemAttempt.responses.length; i++) {
					currentItemAttempt.responses[i].responsePercent
						= currentItemAttempt.attemptNumberStudentCount > 0 
							? (currentItemAttempt.responses[i].responseStudentCount / (float) currentItemAttempt.attemptNumberStudentCount * 100):
							0;
				}

				if (!question.getQuestionPresentationFormat().equalsIgnoreCase(QuestionPresentationFormat.FILL_IN_THE_BLANK.value)
						&& !question.getQuestionPresentationFormat().equalsIgnoreCase(QuestionPresentationFormat.NUMERIC.value)) {
					if (attempt.getSubAttempts() != null) {
						for (SubAttempt subAttempt : attempt.getSubAttempts()) {
							
							if(currentItemAttempt.getTargetSubQuestionById(subAttempt.getSubQuestion().getId()) != null) {
								TargetSubQuestionObject currentTargetSubQuestion = currentItemAttempt.getTargetSubQuestionById(subAttempt.getSubQuestion().getId());
								
								if (subAttempt.getAnswers() != null) {
									
									for (Answer ans : subAttempt.getAnswers()) {
										TargetSubQuestionAnswerObject currentTargetSubQuestionAnswer = currentTargetSubQuestion.getAnswerByAnswerId(ans.getId());
										if(currentTargetSubQuestionAnswer.targetSubQuestionAnswerId == ans.getId()){
											if(ans.isCorrectAnswer()) {
												currentTargetSubQuestionAnswer.targetSubQuestionAnswerResponses[0].targetSubQuestionAnswerResponseStudentCount ++;
											} else {
												currentTargetSubQuestionAnswer.targetSubQuestionAnswerResponses[1].targetSubQuestionAnswerResponseStudentCount ++;
											}
											currentTargetSubQuestionAnswer.targetSubQuestionAnswerResponses[0].targetSubQuestionAnswerResponsePercent
												= currentItemAttempt.attemptNumberStudentCount > 0 
												? (currentTargetSubQuestionAnswer.targetSubQuestionAnswerResponses[0].targetSubQuestionAnswerResponseStudentCount / 
														(float) currentItemAttempt.attemptNumberStudentCount * 100)	: 0;
											currentTargetSubQuestionAnswer.targetSubQuestionAnswerResponses[1].targetSubQuestionAnswerResponsePercent
												= currentItemAttempt.attemptNumberStudentCount > 0 
												? (currentTargetSubQuestionAnswer.targetSubQuestionAnswerResponses[1].targetSubQuestionAnswerResponseStudentCount / 
														(float) currentItemAttempt.attemptNumberStudentCount * 100)	: 0;
										} 
						
									}
									
									List<Answer> answersNotBeSelected = getListOfAnswersNotBeSelected(subAttempt.getAnswers(), subAttempt.getSubQuestion().getAnswers());
									for (Answer ans : answersNotBeSelected) {
										TargetSubQuestionAnswerObject currentTargetSubQuestionAnswer = currentTargetSubQuestion.getAnswerByAnswerId(ans.getId());
										if(currentTargetSubQuestionAnswer.targetSubQuestionAnswerId == ans.getId()){
											if(ans.isCorrectAnswer()) {
												currentTargetSubQuestionAnswer.targetSubQuestionAnswerResponses[1].targetSubQuestionAnswerResponseStudentCount ++;
											} else {
												currentTargetSubQuestionAnswer.targetSubQuestionAnswerResponses[0].targetSubQuestionAnswerResponseStudentCount ++;
											}
											currentTargetSubQuestionAnswer.targetSubQuestionAnswerResponses[1].targetSubQuestionAnswerResponsePercent
												= currentItemAttempt.attemptNumberStudentCount > 0 
												? (currentTargetSubQuestionAnswer.targetSubQuestionAnswerResponses[1].targetSubQuestionAnswerResponseStudentCount / 
														(float) currentItemAttempt.attemptNumberStudentCount * 100)	: 0;
											currentTargetSubQuestionAnswer.targetSubQuestionAnswerResponses[0].targetSubQuestionAnswerResponsePercent
												= currentItemAttempt.attemptNumberStudentCount > 0 
												? (currentTargetSubQuestionAnswer.targetSubQuestionAnswerResponses[0].targetSubQuestionAnswerResponseStudentCount / 
														(float) currentItemAttempt.attemptNumberStudentCount * 100)	: 0;
										} 
									}
								
								}
								
								
								if (currentTargetSubQuestion.targetSubQuestionResponses	!= null) {
									if (subAttempt.getAnswerCorrectness().value.equalsIgnoreCase("Correct") ) {
										currentTargetSubQuestion.targetSubQuestionResponses[0].targetSubQuestionResponseStudentCount ++;
									} else {
										currentTargetSubQuestion.targetSubQuestionResponses[1].targetSubQuestionResponseStudentCount ++;
									}
									
								}
								System.out.println();
									
								for(int i=0; i<currentTargetSubQuestion.targetSubQuestionResponses.length; i++) {
									currentTargetSubQuestion.targetSubQuestionResponses[i].targetSubQuestionResponsePercent
										= currentItemAttempt.attemptNumberStudentCount > 0 
											? (currentTargetSubQuestion.targetSubQuestionResponses[i].targetSubQuestionResponseStudentCount / (float) currentItemAttempt.attemptNumberStudentCount * 100):
											0;
								}
								
								// for test only
	//							System.out.println("\ntargetSubQuestionResponseCode  " + currentSubResponse.targetSubQuestionResponseCode
	//									+ "\ntargetSubQuestionResponseStudentCount = " + currentSubResponse.targetSubQuestionResponseStudentCount 
	//									+ "\nattemptNumberStudentCount = " + currentItemAttempt.attemptNumberStudentCount
	//									+ "\ntargetSubQuestionResponsePercent = " + currentSubResponse.targetSubQuestionResponsePercent);
							}
							
						}
					}
				}
			}
		}
		return toReturn;
	}

	private List<Answer> getListOfAnswersNotBeSelected(List<Answer> attemptAnswers, List<Answer> allAnswers) {
		List<Answer> toReturn = new ArrayList<>(allAnswers);
		for (Answer ans : allAnswers){
			for (Answer answer : attemptAnswers){
				if (answer.getId().compareToIgnoreCase(ans.getId()) == 0){
					toReturn.remove(answer);
					break;
				}
			}
		}
		return toReturn;
	}
	
	private LearningResourceItemObject[] getArrayFromList(List<LearningResourceItemObject> resourceList) {
		LearningResourceItemObject[] resourceItemArray = new LearningResourceItemObject[resourceList.size()];
		int i=0;
		for (LearningResourceItemObject resource : resourceList) { 
			resourceItemArray[i] = resource;
		}
		return resourceItemArray;
	}

}
