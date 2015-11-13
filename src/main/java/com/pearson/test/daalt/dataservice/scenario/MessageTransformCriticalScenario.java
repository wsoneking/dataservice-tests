package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.model.Answer;
import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.AssignmentFactory;
import com.pearson.test.daalt.dataservice.model.Attempt;
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
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;

public class MessageTransformCriticalScenario extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;
	private com.pearson.test.daalt.dataservice.request.action.version01.TestActionFactory testActionFactoryV1;

	@Test
	public void messageTransformCriticalLoadData() throws Exception {
		testActionFactoryV1 = new com.pearson.test.daalt.dataservice.request.action.version01.SubPubSeerTestActionFactory();
		
		TestDataRequest zeroRequest;
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
		TestDataRequest fourthRequest;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignmentToAdd;
		Instructor instr;
		Student student;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		int timeSpent;
		
		try {
			zeroRequest = new BasicTestDataRequest();
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("MessageTransformCriticalLoadData");
			
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
			TestAction createCourseSection = testActionFactoryV1.getCreateCourseSectionTestAction(courseSection);
			zeroRequest.addTestAction(createCourseSection);
			
			zeroRequest.executeAllActions();
			
			//sleep to ensure that Course_Section_Learning_Resource arrives before Learning_Module_Content
			Thread.sleep(10000);
		
			// We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
			//add action to enroll instructor
			TestAction enrollInstr = testActionFactoryV1.getEnrollInstructorTestAction(instr, courseSection);
			firstRequest.addTestAction(enrollInstr);
			
			//enroll student
			courseSection.addStudent(student);
			//add action to enroll student
			TestAction enrollStudent = testActionFactoryV1.getEnrollStudentTestAction(student, courseSection);
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
			
			Calendar nowCal = createDueDate();
			
			assignmentToAdd.setDueDate(nowCal.getTime());
			
			assignmentToAdd.setTitle("DAALT SQE Test Assignment");
			assignmentToAdd.setAllTargetIdsTrue();
			courseSection.addAssignment(assignmentToAdd);
			//add action to add assignment to course section
			TestAction addAssignment = testActionFactoryV1.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
			firstRequest.addTestAction(addAssignment);
			
			System.out.println(assignmentToAdd.getStructure());
			
			firstRequest.executeAllActions();

     		//Read Page
			timeSpent = 100;
			LearningActivity learningActivity = new LearningActivity(student, timeSpent);
			Page page = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			page.addLearningActivity(learningActivity);
			TestAction readPageTestAction = testActionFactoryV1.getReadPageTestAction(student, courseSection, assignmentToAdd, page, learningActivity);
			secondRequest.addTestAction(readPageTestAction);
			timeOnTaskOutput.append("\n...learning time : " + student.getPersonId() + " : " + page.getLearningResourceId() + " : " + timeSpent + " seconds");

			timeSpent = 10;
			// Chapter Quiz
			Quiz chquiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			for(Question ques : chquiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				Attempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student, /*timeSpent*/ timeSpent, /*attemptNumber*/ 1, 
						answer, /*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques.getId() + " : " + timeSpent + " seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, ques, attempt);
				secondRequest.addTestAction(attemptQuestionTestAction);
				timeSpent += 2;
			}

			//Complete Quiz
			QuizCompletionActivity quizCompletionActivity = new QuizCompletionActivity(student);
			for(Question ques : chquiz.getQuestions()){
				quizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chquiz.addCompletionActivity(quizCompletionActivity);
			TestAction completeChQuizTestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, chquiz, quizCompletionActivity);
			secondRequest.addTestAction(completeChQuizTestAction);
			
			secondRequest.executeAllActions();
			
			
			// Section Quiz
			Quiz sequiz = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			for(Question ques : sequiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student, /*timeSpent*/ timeSpent, /*attemptNumber*/ 1, 
						answer, /*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques.getId() + " : " + timeSpent + " seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, ques, attempt);
				thirdRequest.addTestAction(attemptQuestionTestAction);
				timeSpent += 2;
			}
		
			QuizCompletionActivity quizCompletionActivity2 = new QuizCompletionActivity(student);
			for(Question ques : sequiz.getQuestions()){
				quizCompletionActivity2.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			sequiz.addCompletionActivity(quizCompletionActivity2);
			TestAction completeChQuizTestAction2 = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, sequiz, quizCompletionActivity2);
			thirdRequest.addTestAction(completeChQuizTestAction2);
			
			thirdRequest.executeAllActions();
			assignmentToAdd.setDueDatePassed(true);
			
			TestAction reSendSeedData = testActionFactory.getReSendSeedDataTestAction(courseSection, assignmentToAdd);
			fourthRequest.addTestAction(reSendSeedData);

			fourthRequest.executeAllActions();
			
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
