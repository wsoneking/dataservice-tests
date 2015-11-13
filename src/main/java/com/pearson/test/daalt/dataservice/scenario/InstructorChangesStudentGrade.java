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
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;

/**
 * student completes the assignment - send tincans and Assessment_Performance
 * Send second Assessment_Performance with different score
 */

public class InstructorChangesStudentGrade extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
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
			thirdRequest = new BasicTestDataRequest();
			testData = new BasicTestData("InstructorChangesStudentGrade");
			
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
			
			//enroll student1
			courseSection.addStudent(student);
			//add action to enroll student1
			TestAction enrollStudent1 = testActionFactory.getEnrollStudentTestAction(student, courseSection);
			firstRequest.addTestAction(enrollStudent1);
			
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
			assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_QUIZ_ONLY);

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
			
			//Chapter Quiz
			Quiz chapQuiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			for(Question ques : chapQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student, /*timeSpent*/ 30, /*attemptNumber*/ 1, 
						answer, /*pointsEarned*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques.getId() + " : 30 seconds");
				
				//Question completion activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, ques, attempt, questionCompletionActivity);
				secondRequest.addTestAction(attemptQuestionTestAction);
				
			}
			
			//First Complete Quiz
			QuizCompletionActivity chapQuizCompletionActivity1 = new QuizCompletionActivity(student);
			for(Question ques : chapQuiz.getQuestions()){
				chapQuizCompletionActivity1.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapQuiz.addCompletionActivity(chapQuizCompletionActivity1);
			TestAction completeChapQuizTestAction1 = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, chapQuizCompletionActivity1);
			secondRequest.addTestAction(completeChapQuizTestAction1);
			
			secondRequest.executeAllActions();
			
			//Instructor changes grade
			//Second Complete Quiz
			QuizCompletionActivity chapQuizCompletionActivity2 = new QuizCompletionActivity(student);
			for(Question ques : chapQuiz.getQuestions()){
				chapQuizCompletionActivity2.addQuestionPerf(ques.getId(), /*pointsEarned*/ 1f);
			}
			chapQuizCompletionActivity2.setIsAdjusted(true);
			chapQuiz.addCompletionActivity(chapQuizCompletionActivity2);
			chapQuiz.removeCompletionActivity(chapQuizCompletionActivity1);
			TestAction assessmentPerformanceChapQuizTestAction = testActionFactory.getInstructorAdjustsStudentGradeTestAction(courseSection, assignmentToAdd, chapQuiz, chapQuizCompletionActivity2);
			thirdRequest.addTestAction(assessmentPerformanceChapQuizTestAction);
			
			thirdRequest.executeAllActions();
			
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


