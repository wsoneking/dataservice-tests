package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.Date;
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
import com.pearson.test.daalt.dataservice.model.Course;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.MultiValueAttempt;
import com.pearson.test.daalt.dataservice.model.Page;
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
 * create course section
 * enroll one student in course section
 * add assignment to course section (includes two quizzes and a reading page)
 * student does reading
 * student completes chap Quiz
 * Due date passes
 * Student answer chap section quiz as practice.
 * instructor extends due date - LMC with "Update"?
 * re-send tincans, AIC, and AP?
 * 
 */
public class ExtendDueDateWithResendingUpdateMessage extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
		TestDataRequest fourthRequest;
		TestDataRequest fifthRequest;
		TestDataRequest sixthRequest;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignmentToAdd;
		Instructor instr;
		Student student;
		
		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			fifthRequest = new BasicTestDataRequest();
			sixthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("ExtendDueDateWithResendingUpdateMessage");
			
			// add instructor
			com.pearson.test.daalt.dataservice.User instrFromConfig = getEngine().getInstructor();
			instr = new BasicInstructor(instrFromConfig.getUserName(), 
					instrFromConfig.getPassword(), 
					instrFromConfig.getId(),
					instrFromConfig.getFirstName(),
					instrFromConfig.getLastName());
			testData.addInstructor(instr);
			
			// add student 1
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
		
			// We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
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

     		//Read Page
			LearningActivity learningActivity = new LearningActivity(student, 100);
			Page page = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			page.addLearningActivity(learningActivity);
			TestAction readPageTestAction = testActionFactory.getReadPageTestAction(courseSection, page, learningActivity);
			secondRequest.addTestAction(readPageTestAction);
			
			secondRequest.executeAllActions();

			int timeSpent = 10;
	
			// Chapter Quiz
			Quiz chquiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			for(Question ques : chquiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student, /*timeSpent*/ timeSpent, /*attemptNumber*/ 1, 
						answer, /*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, ques, attempt, questionCompletionActivity);
				thirdRequest.addTestAction(attemptQuestionTestAction);
				timeSpent += 2;
				
			}

			//Complete Quiz
			QuizCompletionActivity quizCompletionActivity = new QuizCompletionActivity(student);
			for(Question ques : chquiz.getQuestions()){
				quizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chquiz.addCompletionActivity(quizCompletionActivity);
			TestAction completeChQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chquiz, quizCompletionActivity);
			thirdRequest.addTestAction(completeChQuizTestAction);
			thirdRequest.executeAllActions();
			
			

			// Section Quiz
			Quiz sequiz = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			
			//Student - Complete Chapter Section Quiz
			QuizCompletionActivity chapSecQuizCompletionActivity = new QuizCompletionActivity(student);
			for(Question ques : sequiz.getQuestions()){
				chapSecQuizCompletionActivity.addQuestionPerf(ques.getId(), /*pointsEarned*/ 0f);
				
				//Question Completion Activity
				//System sets score to zero
				User system = new BasicStudent(null, null, student.getPersonId(), null, null);
				system.setPersonRole("System");
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(system, 0f);
				ques.addCompletionActivity(questionCompletionActivity);
			}
			sequiz.addCompletionActivity(chapSecQuizCompletionActivity);
			TestAction stud1CompleteChapSecQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, sequiz, chapSecQuizCompletionActivity);
			fourthRequest.addTestAction(stud1CompleteChapSecQuizTestAction);
			fourthRequest.executeAllActions();
			
			// set practice
			sequiz.setPractice(true);		// it's useless. 

			timeSpent = 20;
			// take the quiz
			for(Question ques : sequiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student, /*timeSpent*/ timeSpent, /*attemptNumber*/ 1, 
						answer, /*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, ques, attempt, questionCompletionActivity);
				fifthRequest.addTestAction(attemptQuestionTestAction);
				timeSpent += 2;
			}
		
			QuizCompletionActivity quizCompletionActivity2 = new QuizCompletionActivity(student);
			for(Question ques : sequiz.getQuestions()){
				quizCompletionActivity2.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			sequiz.addCompletionActivity(quizCompletionActivity2);
			TestAction completeChQuizTestAction2 = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, sequiz, quizCompletionActivity2);
			fifthRequest.addTestAction(completeChQuizTestAction2);

			fifthRequest.executeAllActions();
			
			
			// update due date
			Calendar nowCal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			nowCal2.add(Calendar.DAY_OF_YEAR, 20);
			assignmentToAdd.setDueDate(nowCal2.getTime());
			
			TestAction updateDueDate = testActionFactory.getUpdateLearningModuleTestAction(instr, courseSection, assignmentToAdd);
			sixthRequest.addTestAction(updateDueDate);
			
			
			// re-send tincans, AIC and AP
			// set practice
			sequiz.setPractice(false);		// it's useless. 
			
			// take the quiz
			timeSpent = 20;
			for(Question ques : sequiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student, /*timeSpent*/ timeSpent, /*attemptNumber*/ 1, 
						answer, /*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionAsPracticeTestAction(courseSection, assignmentToAdd, sequiz, ques, attempt, questionCompletionActivity);
				sixthRequest.addTestAction(attemptQuestionTestAction);
				timeSpent += 2;
			}
		
			QuizCompletionActivity quizCompletionActivity2b = new QuizCompletionActivity(student);
			for(Question ques : sequiz.getQuestions()){
				quizCompletionActivity2b.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			sequiz.addCompletionActivity(quizCompletionActivity2b);
			TestAction completeChQuizTestAction2b = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, sequiz, quizCompletionActivity2b);
			sixthRequest.addTestAction(completeChQuizTestAction2b);
			
			sixthRequest.executeAllActions();	
			
			
			
		} catch (Exception e) {
			getEngine().getSuite().setDidCreationTestsComplete(false);
			throw e;
		}
		
		//pass TestData to validation engine
		DaaltDataServiceValidationEngine validationEngine = new DaaltDataServiceValidationEngine();
		List<Validation> validationList = validationEngine.getValidationsForTestData(testData);
		for (Validation val : validationList) {
			getCurrentTestCase().getValidations().add(val);
		}
	}
	
}

