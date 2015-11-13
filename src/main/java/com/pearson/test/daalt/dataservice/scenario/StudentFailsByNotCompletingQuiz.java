package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.model.Answer;
import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.AssignmentFactory;
import com.pearson.test.daalt.dataservice.model.BasicCourse;
import com.pearson.test.daalt.dataservice.model.BasicCourseSection;
import com.pearson.test.daalt.dataservice.model.BasicInstructor;
import com.pearson.test.daalt.dataservice.model.BasicProduct;
import com.pearson.test.daalt.dataservice.model.BasicStudent;
import com.pearson.test.daalt.dataservice.model.BasicTestData;
import com.pearson.test.daalt.dataservice.model.CompletionActivityOriginCode;
import com.pearson.test.daalt.dataservice.model.Course;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.MultiValueAttempt;
import com.pearson.test.daalt.dataservice.model.Product;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;

/**
 *Student makes some attempts to answer questions, but does not complete the assessment.
 *Send Assessment_Performance with zero score for remaining questions
 */

public class StudentFailsByNotCompletingQuiz extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest fourthRequest;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignmentToAdd;
		Instructor instr;
		Student student;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("StudentFailsByNotCompletingQuiz");
			
			// add instructor
			com.pearson.test.daalt.dataservice.User instrFromConfig = getEngine().getInstructor();
			instr = new BasicInstructor(instrFromConfig.getUserName(), 
					instrFromConfig.getPassword(), 
					instrFromConfig.getId(),
					instrFromConfig.getFirstName(),
					instrFromConfig.getLastName());
			testData.addInstructor(instr);
			
			// add student
			com.pearson.test.daalt.dataservice.User student01FromConfig = getEngine().getStudent01();
			student = new BasicStudent(student01FromConfig.getUserName(), 
					student01FromConfig.getPassword(), 
					student01FromConfig.getId(),
					student01FromConfig.getFirstName(),
					student01FromConfig.getLastName());
			testData.addStudent(student);
			
			//create course section
			course = new BasicCourse();
			courseSection = new BasicCourseSection();
			courseSection.setInstructor(instr);
			//create Book
			Product book = new BasicProduct();	
			courseSection.addBook(book);
			course.addCourseSection(courseSection);
			testData.addCourse(course);
			//add action to create course section
			TestAction createCourseSection = testActionFactory.getCreateCourseSectionTestAction(courseSection);
			firstRequest.addTestAction(createCourseSection);
		
			//We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
			//add action to enroll instructor
			TestAction enrollInstr = testActionFactory.getEnrollInstructorTestAction(instr, courseSection);
			firstRequest.addTestAction(enrollInstr);
			
			//enroll student
			courseSection.addStudent(student);
			//add action to enroll student
			TestAction enrollStudent = testActionFactory.getEnrollStudentTestAction(student, courseSection);
			firstRequest.addTestAction(enrollStudent);
			
			//add assignment to course section
			/*
			 * Using an AssignmentFactory.
			 * First implementation returns from a pre-defined selection of Assignments identified by Enum.
			 * Later, we can discuss possible ways of improving on the factory design.
			 * 
			 * Two other factories that would be useful: 
			 * 		Product (aka Book) - defines learning resource relationships
			 * 		AssignmentPerformance - defines the precise interactions between a student and an Assignment 
			 */
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CH_SECTION_QUIZ_AND_CH_QUIZ_AND_READING);

			System.out.println(assignmentToAdd.getStructure());
			
			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			assignmentToAdd.setDueDate(nowCal.getTime());
			assignmentToAdd.setTitle("DAALT SQE Test Assignment");
			courseSection.addAssignment(assignmentToAdd);
			//add action to add assignment to course section
			TestAction addAssignment = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
			firstRequest.addTestAction(addAssignment);
			
			firstRequest.executeAllActions();
			
			Quiz chapQuiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			
			//Chapter Quiz Question1 - Student answer incorrectly in one attempt - Do not Final Attempt
			int timeSpent = 30;
			Question chapQuizQues1 = chapQuiz.getQuestions().get(0);
			Answer chapQuizQues1IncorrectAnswer = chapQuizQues1.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);
			MultiValueAttempt chapQuizQues1Attempt1 
				= getMultiValueRadioButtonAttempt(chapQuizQues1, student, timeSpent, /*attemptNumber*/ 1, 
						chapQuizQues1IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			//add action to add student attempt1 to question1
			chapQuizQues1.addAttempt(chapQuizQues1Attempt1);
			TestAction attempt1ChapQuizQuestion1TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, 
					chapQuiz, chapQuizQues1, chapQuizQues1Attempt1, /*questionCompletionActivity*/ null);
			secondRequest.addTestAction(attempt1ChapQuizQuestion1TestAction);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + chapQuizQues1.getId() + " : " + timeSpent + " seconds");
			
			//Chapter Quiz Question2 - Student answer incorrectly in two attempts - Do not use Final Attempt
			Question chapQuizQues2 = chapQuiz.getQuestions().get(1);
			Answer chapQuizQues2IncorrectAnswer = chapQuizQues2.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);
			timeSpent = 20;
			MultiValueAttempt chapQuizQues2Attempt1 
				= getMultiValueRadioButtonAttempt(chapQuizQues2, student, timeSpent, /*attemptNumber*/ 1, 
						chapQuizQues2IncorrectAnswer, /*pointsEarned*/ 0f, /*isFinalAttempt*/ false);
			chapQuizQues2.addAttempt(chapQuizQues2Attempt1);
			TestAction attempt1ChapQuizQuestion2TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, 
					chapQuiz, chapQuizQues2, chapQuizQues2Attempt1, /*questionCompletionActivity*/ null);
			secondRequest.addTestAction(attempt1ChapQuizQuestion2TestAction);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + chapQuizQues2.getId() + " : " + timeSpent + " seconds");
			timeSpent = 25;
			MultiValueAttempt chapQuizQues2Attempt2 
				= getMultiValueRadioButtonAttempt(chapQuizQues2, student, timeSpent, /*attemptNumber*/ 2, 
						chapQuizQues2IncorrectAnswer, /*pointsEarned*/ 0f, /*isFinalAttempt*/ false);
			chapQuizQues2.addAttempt(chapQuizQues2Attempt2);
			TestAction attempt2ChapQuizQuestion2TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, 
					chapQuiz, chapQuizQues2, chapQuizQues2Attempt2, /*questionCompletionActivity*/ null);
			secondRequest.addTestAction(attempt2ChapQuizQuestion2TestAction);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + chapQuizQues2.getId() + " : " + timeSpent + " seconds");
			
			//Chapter Quiz Question3 - Student answer incorrectly in two attempts - Do not use final attempt
			Question chapQuizQues3 = chapQuiz.getQuestions().get(2);
			Answer chapQuizQues3IncorrectAnswer = chapQuizQues3.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);
			timeSpent = 60;
			MultiValueAttempt chapQuizQues3Attempt1 
				= getMultiValueRadioButtonAttempt(chapQuizQues3, student, timeSpent, /*attemptNumber*/ 1, 
						chapQuizQues3IncorrectAnswer, /*pointsEarned*/ 0f, /*isFinalAttempt*/ false);
			chapQuizQues3.addAttempt(chapQuizQues3Attempt1);
			TestAction attempt1ChapQuizQuestion3TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, 
					chapQuiz, chapQuizQues3, chapQuizQues3Attempt1, /*questionCompletionActivity*/ null);
			secondRequest.addTestAction(attempt1ChapQuizQuestion3TestAction);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + chapQuizQues3.getId() + " : " + timeSpent + " seconds");
			timeSpent = 30;
			MultiValueAttempt chapQuizQues3Attempt2 
				= getMultiValueRadioButtonAttempt(chapQuizQues3, student, timeSpent, /*attemptNumber*/ 2, 
						chapQuizQues3IncorrectAnswer, /*pointsEarned*/ 0f, /*isFinalAttempt*/ false);
			chapQuizQues3.addAttempt(chapQuizQues3Attempt2);
			TestAction attempt2ChapQuizQuestion3TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, 
					chapQuiz, chapQuizQues3, chapQuizQues3Attempt2, /*questionCompletionActivity*/ null);
			secondRequest.addTestAction(attempt2ChapQuizQuestion3TestAction);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + chapQuizQues3.getId() + " : " + timeSpent + " seconds");
			
			secondRequest.executeAllActions();
			
			assignmentToAdd.setDueDatePassed(true);
			TestAction passDueDateTestAction = testActionFactory.getSimulateDueDatePassingTestAction(instr, courseSection, assignmentToAdd);
			fourthRequest.addTestAction(passDueDateTestAction);
			
			fourthRequest.executeAllActions();
			
			//Student - Complete Chapter Quiz
			//System sets score to zero
			User system = new BasicStudent(/*userName*/ null, /*password*/ null, student.getPersonId(), /*firstName*/ null, /*lastName*/ null);
			system.setPersonRole(CompletionActivityOriginCode.SYSTEM.value);
			QuizCompletionActivity chapQuizCompletionActivity = new QuizCompletionActivity(system);
			for(Question ques : chapQuiz.getQuestions()){
				chapQuizCompletionActivity.addQuestionPerf(ques.getId(), /*pointsEarned*/ 0f);
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(system, 0f);
				ques.addCompletionActivity(questionCompletionActivity);
			}
			chapQuiz.addCompletionActivity(chapQuizCompletionActivity);
			
			//System - Complete Chapter Section Quiz
			Quiz chapSecQuiz = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			QuizCompletionActivity chapSecQuizCompletionActivity = new QuizCompletionActivity(system);
			for(Question ques : chapSecQuiz.getQuestions()){
				chapSecQuizCompletionActivity.addQuestionPerf(ques.getId(), /*pointsEarned*/ 0f);
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(system, 0f);
				ques.addCompletionActivity(questionCompletionActivity);
			}
			chapSecQuiz.addCompletionActivity(chapSecQuizCompletionActivity);
			
		} catch (Exception e) {
			getEngine().getSuite().setDidCreationTestsComplete(false);
			throw e;
		}
		
		//pass TestData to validation engine
		DaaltDataServiceValidationEngine validationEngine = new DaaltDataServiceValidationEngine();
		List<Validation> validationList = validationEngine.getValidationsForTestData(testData);
		for (Validation val : validationList) {
			getCurrentTestCase().getValidations().add(val);
			if (getEngine().isPrintExpectedOutput()) {
				System.out.println(val.getExpectedResultsPrintString());
			}
		}
		
		if (getEngine().isPrintToT()) {
			timeOnTaskOutput.append("\n");
			System.out.println(timeOnTaskOutput.toString());
		}
	}
}

