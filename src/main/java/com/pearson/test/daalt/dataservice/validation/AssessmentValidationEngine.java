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
import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.BasicQuiz;
import com.pearson.test.daalt.dataservice.model.Chapter;
import com.pearson.test.daalt.dataservice.model.ChapterSection;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.EmbeddedQuestion;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.response.model.AssessmentItemObject;
import com.pearson.test.daalt.dataservice.response.model.AssessmentObject;
import com.pearson.test.daalt.dataservice.validation.endpoint.AssessmentsAll;

/**
 * endpoint 3.4
 */

public class AssessmentValidationEngine extends DaaltDataServiceValidationEngine {
	
	public List<Validation> getValidations(TestData data) throws InvalidTestDataException, JsonGenerationException, JsonMappingException, IOException {
		List<Validation> validationList = new ArrayList<>();
		String baseUrl = TestEngine.getInstance().getBaseUrl();
		StringBuilder route = new StringBuilder();
	
		for (CourseSection courseSection : data.getAllCourseSections()) {
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
			
			for (Assignment assignment : courseSection.getAssignments()) {
				for (Chapter chapter : assignment.getChapters()) {
					route = new StringBuilder();
					route.append(baseUrl).append("/validate").append("/assessments/").append(chapter.getLearningResourceId());
					validationList.add(new AssessmentsAll(data.getTestScenarioName(), instructorUser, 
							route.toString(), /*expectedResponseCode*/ ResponseCode.NOT_FOUND.value, null));
					
					Quiz chapterQuiz = chapter.getChapterQuiz();
					if (chapterQuiz != null) {
						AssessmentObject expectedAssessment = getAssessmentForQuiz(chapterQuiz);
						route = new StringBuilder();
						route.append(baseUrl).append("/validate").append("/assessments/").append(chapterQuiz.getId());
						validationList.add(new AssessmentsAll(data.getTestScenarioName(), instructorUser, 
								route.toString(), /*expectedResponseCode*/ ResponseCode.OK.value, expectedAssessment));
						
						if (studentUser != null) {
							validationList.add(new AssessmentsAll(data.getTestScenarioName(), studentUser, 
									route.toString(), /*expectedResponseCode*/ ResponseCode.OK.value, expectedAssessment));
						}
						
						if (chapterQuiz.getNestedQuizzes() != null) {
							for (Quiz nestedQuiz : chapterQuiz.getNestedQuizzes()) {
								AssessmentObject expectedNestedAssessment = getAssessmentForQuiz(nestedQuiz);
								route = new StringBuilder();
								route.append(baseUrl).append("/validate").append("/assessments/").append(nestedQuiz.getId());
								validationList.add(new AssessmentsAll(data.getTestScenarioName(), instructorUser, 
										route.toString(), /*expectedResponseCode*/ ResponseCode.OK.value, expectedNestedAssessment));
								
								if (studentUser != null) {
									validationList.add(new AssessmentsAll(data.getTestScenarioName(), studentUser, 
											route.toString(), /*expectedResponseCode*/ ResponseCode.OK.value, expectedNestedAssessment));
								}
							}
						}
					}
					if (chapter.getChapterSections() != null) {
						for (ChapterSection chapterSection : chapter.getChapterSections()) {
							Quiz chapterSectionQuiz = chapterSection.getChapterSectionQuiz();
							if (chapterSectionQuiz != null) {								
								AssessmentObject expectedChapterSectionAssessment = getAssessmentForQuiz(chapterSectionQuiz);
								route = new StringBuilder();
								route.append(baseUrl).append("/validate").append("/assessments/").append(chapterSectionQuiz.getId());
								validationList.add(new AssessmentsAll(data.getTestScenarioName(), instructorUser, 
										route.toString(), /*expectedResponseCode*/ ResponseCode.OK.value, expectedChapterSectionAssessment));
								
								if (studentUser != null) {
									validationList.add(new AssessmentsAll(data.getTestScenarioName(), studentUser, 
											route.toString(), /*expectedResponseCode*/ ResponseCode.OK.value, expectedChapterSectionAssessment));
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
									if(page.getEmbeddedQuestions() != null) {
										for (Question embeddedQuestion : page.getEmbeddedQuestions()) {
											Quiz quiz = quizMap.get(embeddedQuestion.getAssessmentId());
											AssessmentObject expectedAssessment = getAssessmentForQuiz(quiz);
											route = new StringBuilder();
											route.append(baseUrl).append("/validate").append("/assessments/").append(quiz.getId());
											validationList.add(new AssessmentsAll(data.getTestScenarioName(), instructorUser, 
													route.toString(), /*expectedResponseCode*/ ResponseCode.OK.value, expectedAssessment));
											
											if (studentUser != null) {
												validationList.add(new AssessmentsAll(data.getTestScenarioName(), studentUser, 
														route.toString(), /*expectedResponseCode*/ ResponseCode.OK.value, expectedAssessment));
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return validationList;
	}
	
	private AssessmentObject getAssessmentForQuiz(Quiz quiz) throws InvalidTestDataException {
		
		if (quiz.getId() == null) {
			throw new InvalidTestDataException("Failed to generate Assessment validations - assessmentId is null");
		}else if(quiz.getQuestions() != null){
			for(Question question : quiz.getQuestions()){
				if(question.getId() == null){
					throw new InvalidTestDataException("Failed to generate Assessment validations - itemId is null");
				}
			}
		}
		
		AssessmentObject assessment = new AssessmentObject();
		assessment.assessmentId = quiz.getId();
		assessment.assessmentType = "Quiz";
		assessment.assessmentSeeded = quiz.getIsAssessmentSeeded();
		assessment.assessmentLastSeedDateTime = quiz.getAssessmentLastSeedDateTime();
		if(quiz.getIsAssessmentSeeded() == true){
			assessment.assessmentLastSeedType = "Create";
		}else{
			assessment.assessmentLastSeedType = "Delete";
		}
		
		
		List<AssessmentItemObject> assessmentItems = new ArrayList<>();
		if(quiz.getQuestions() != null){
			for(Question question : quiz.getQuestions()){
				AssessmentItemObject assessmentItem = new AssessmentItemObject();
				if(question.getQuestionLastSeedDateTime()==null){
					assessmentItem.questionType ="";
					assessmentItem.questionPresentationFormat = "";
					assessmentItem.questionText = "";
					assessmentItem.itemLastSeedDateTime = "";
					assessmentItem.itemLastSeedType = "";
				} else {
					assessmentItem.questionType = question.getQuestionType();
					assessmentItem.questionPresentationFormat = question.getQuestionPresentationFormat();
					assessmentItem.questionText = question.getText();
					assessmentItem.itemLastSeedDateTime = question.getQuestionLastSeedDateTime();
					if(question.getIsQuestionSeeded() == true){
						assessmentItem.itemLastSeedType = "Create";
					}else{
						assessmentItem.itemLastSeedType = "Delete";
					}
				}
				assessmentItem.itemId = question.getId();
				assessmentItem.itemSequence = question.getSequenceNumber();
				assessmentItem.itemSeeded = question.getIsQuestionSeeded();
				assessmentItems.add(assessmentItem);
			}
			assessment.assessmentItems = new AssessmentItemObject[assessmentItems.size()];
			int i = 0;
			for(AssessmentItemObject assessmentItemObj : assessmentItems){
				assessment.assessmentItems[i] = assessmentItemObj;
				i++;
			}
		}
		return assessment;
	}
}
