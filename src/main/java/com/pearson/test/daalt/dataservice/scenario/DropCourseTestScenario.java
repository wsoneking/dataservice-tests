package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.User;
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

/**
 * Student1 and Student 2 complete the assignment
 * Student1 and Student 2 drops the course
 * Student2 enrolls again. 
 */

public class DropCourseTestScenario extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
		TestDataRequest fourthRequest;
		TestDataRequest fifthRequest;
		TestDataRequest sixthRequest;
		TestDataRequest seventhRequest;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignment;
		Instructor instr;
		Student student1;
		Student student2;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		int timeSpent;
		
		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			fifthRequest = new BasicTestDataRequest();
			sixthRequest = new BasicTestDataRequest();
			seventhRequest = new BasicTestDataRequest();
			testData = new BasicTestData("DropCourseTestScenario");
			
			// add instructor
			User instrFromConfig = getEngine().getInstructor();
			instr = new BasicInstructor(instrFromConfig.getUserName(), 
					instrFromConfig.getPassword(), 
					instrFromConfig.getId(),
					instrFromConfig.getFirstName(),
					instrFromConfig.getLastName());
			testData.addInstructor(instr);
			
			// add student 1
			User student01FromConfig = getEngine().getStudent01();
			student1 = new BasicStudent(student01FromConfig.getUserName(), 
					student01FromConfig.getPassword(), 
					student01FromConfig.getId(),
					student01FromConfig.getFirstName(),
					student01FromConfig.getLastName());
			testData.addStudent(student1);
			
			// add student 2
			User student02FromConfig = getEngine().getStudent02();
			student2 = new BasicStudent(student02FromConfig.getUserName(), 
					student02FromConfig.getPassword(), 
					student02FromConfig.getId(),
					student02FromConfig.getFirstName(),
					student02FromConfig.getLastName());
			testData.addStudent(student2);
			
			
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
			courseSection.addStudent(student1);
			//add action to enroll student1
			TestAction enrollStudent1 = testActionFactory.getEnrollStudentTestAction(student1, courseSection);
			firstRequest.addTestAction(enrollStudent1);
			//enroll student2
			courseSection.addStudent(student2);
			//add action to enroll student2
			TestAction enrollStudent2 = testActionFactory.getEnrollStudentTestAction(student2, courseSection);
			firstRequest.addTestAction(enrollStudent2);
			
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
			assignment = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CH_SECTION_QUIZ_AND_CH_QUIZ_AND_READING);

			System.out.println(assignment.getStructure());
			
			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			assignment.setDueDate(nowCal.getTime());
			assignment.setTitle("DAALT SQE Test Assignment");
			courseSection.addAssignment(assignment);
			//add action to add assignment to course section
			TestAction addAssignment = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignment);
			firstRequest.addTestAction(addAssignment);
			
			firstRequest.executeAllActions();
	
			//BEGIN : Student1
			//Student1 - Read Page
			timeSpent = 120;
			LearningActivity stud1LearningActivity = new LearningActivity(student1, timeSpent);
			Page stud1Page = assignment.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			stud1Page.addLearningActivity(stud1LearningActivity);
			TestAction stud1ReadPageTestAction = testActionFactory.getReadPageTestAction(courseSection, stud1Page, stud1LearningActivity);
			secondRequest.addTestAction(stud1ReadPageTestAction);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + stud1Page.getLearningResourceId() + " : " + timeSpent + " seconds");
	
			//Student 1 - Chapter Quiz
			Quiz chapQuiz = assignment.getChapters().get(0).getChapterQuiz();
			for(Question ques : chapQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				timeSpent = 30;
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student1, timeSpent, /*attemptNumber*/ 1, 
						answer, /*pointsEarned*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + ques.getId() + " : " + timeSpent + " seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student1, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignment, chapQuiz, ques, attempt, questionCompletionActivity);
				secondRequest.addTestAction(attemptQuestionTestAction);
		
			}
			
			//Student 1 - Complete Quiz
			QuizCompletionActivity stud1ChapQuizCompletionActivity = new QuizCompletionActivity(student1);
			for(Question ques : chapQuiz.getQuestions()){
				stud1ChapQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapQuiz.addCompletionActivity(stud1ChapQuizCompletionActivity);
			TestAction stud1CompleteChapQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignment, chapQuiz, stud1ChapQuizCompletionActivity);
			secondRequest.addTestAction(stud1CompleteChapQuizTestAction);
			
			secondRequest.executeAllActions();
			
			
			//Student1 - Chapter Section Quiz
			Quiz chapSecQuiz = assignment.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			for(Question ques : chapSecQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				timeSpent = 40;
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student1, timeSpent, /*attemptNumber*/ 1, 
						answer, /*pointsEarned*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + ques.getId() + " : " + timeSpent + " seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student1, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignment, chapSecQuiz, ques, attempt, questionCompletionActivity);
				thirdRequest.addTestAction(attemptQuestionTestAction);
				
				
			}
			
			//Student1 - Complete Quiz
			QuizCompletionActivity stud1ChapSecQuizCompletionActivity = new QuizCompletionActivity(student1);
			for(Question ques : chapSecQuiz.getQuestions()){
				stud1ChapSecQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapSecQuiz.addCompletionActivity(stud1ChapSecQuizCompletionActivity);
			TestAction stud1CompleteChapSecQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignment, chapSecQuiz, stud1ChapSecQuizCompletionActivity);
			thirdRequest.addTestAction(stud1CompleteChapSecQuizTestAction);
			
			thirdRequest.executeAllActions();
			
			//BEGIN : Student 2
			//Student2 - Read Page
			timeSpent = 180;
			LearningActivity stud2LearningActivity = new LearningActivity(student2, timeSpent);
			Page stud2Page = assignment.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			stud2Page.addLearningActivity(stud2LearningActivity);
			TestAction stud2ReadPageTestAction = testActionFactory.getReadPageTestAction(courseSection, stud2Page, stud2LearningActivity);
			fourthRequest.addTestAction(stud2ReadPageTestAction);
			timeOnTaskOutput.append("\n...learning time : " + student2.getPersonId() + " : " + stud2Page.getLearningResourceId() + " : " + timeSpent + " seconds");
	
			//Student 2 - Chapter Quiz
			for(Question ques : chapQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				timeSpent = 50;
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student2, timeSpent, /*attemptNumber*/ 1, 
						answer, /*pointsEarned*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + ques.getId() + " : " + timeSpent + " seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student2, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignment, chapQuiz, ques, attempt, questionCompletionActivity);
				fourthRequest.addTestAction(attemptQuestionTestAction);
				
			}

			//Student2 - Complete Quiz
			QuizCompletionActivity stud2ChapQuizCompletionActivity = new QuizCompletionActivity(student2);
			for(Question ques : chapQuiz.getQuestions()){
				stud2ChapQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapQuiz.addCompletionActivity(stud2ChapQuizCompletionActivity);
			TestAction stud2CompleteChapQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignment, chapQuiz, stud2ChapQuizCompletionActivity);
			fourthRequest.addTestAction(stud2CompleteChapQuizTestAction);
			
			fourthRequest.executeAllActions();
			
			
			//Student2 - Chapter Section Quiz
			for(Question ques : chapSecQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				timeSpent = 60;
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student2, timeSpent, /*attemptNumber*/ 1, 
						answer, /*pointsEarned*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + ques.getId() + " : " + timeSpent + " seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student2, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignment, chapSecQuiz, ques, attempt, questionCompletionActivity);
				fifthRequest.addTestAction(attemptQuestionTestAction);
				
			}
		
			//Studen2 - Complete Quiz
			QuizCompletionActivity stud2ChapSecQuizCompletionActivity = new QuizCompletionActivity(student2);
			for(Question ques : chapSecQuiz.getQuestions()){
				stud2ChapSecQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapSecQuiz.addCompletionActivity(stud2ChapSecQuizCompletionActivity);
			TestAction stud2CompleteChapSecQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignment, chapSecQuiz, stud2ChapSecQuizCompletionActivity);
			fifthRequest.addTestAction(stud2CompleteChapSecQuizTestAction);
			
			fifthRequest.executeAllActions();
			
			//Student1 - Drop Course
			courseSection.removeStudent(student1);
			TestAction stud1DropCourseTestAction = testActionFactory.getDropCourseTestAction(student1, courseSection);
			sixthRequest.addTestAction(stud1DropCourseTestAction);
			
			//Student1 - remove read page
			stud1Page.removeLearningActivity(stud1LearningActivity);
			
			//Student1 - remove Chapter Quiz attempts
			for(Question ques : chapQuiz.getQuestions()){
				if(ques.studentAttemptedQuestion(student1)){
					List<Attempt> attempts = ques.getAttemptsForUser(student1);
					for(Attempt attempt : attempts){
						ques.removeAttempt(attempt);
					}
				}
				//remove question completion activity
				ques.removeStudentCompletionActivity(student1);
			}
			
			//Student1 - remove Chapter Section Quiz attempts
			for(Question ques : chapSecQuiz.getQuestions()){
				if(ques.studentAttemptedQuestion(student1)){
					List<Attempt> attempts = ques.getAttemptsForUser(student1);
					for(Attempt attempt : attempts){
						ques.removeAttempt(attempt);
					}
				}
				//remove question completion activity
				ques.removeStudentCompletionActivity(student1);
			}
			
			//Student1 - remove Quiz completion activity
			chapQuiz.removeCompletionActivity(stud1ChapQuizCompletionActivity);
			chapSecQuiz.removeCompletionActivity(stud1ChapSecQuizCompletionActivity);
			
			sixthRequest.executeAllActions();
			
			//Student2 - Drop Course
			courseSection.removeStudent(student2);
			TestAction stud2DropCourseTestAction = testActionFactory.getDropCourseTestAction(student2, courseSection);
			seventhRequest.addTestAction(stud2DropCourseTestAction);
			
			//Student2 - Enroll Again
			courseSection.addStudent(student2);
			TestAction stud2EnrollTestAction = testActionFactory.getEnrollStudentTestAction(student2, courseSection);
			seventhRequest.addTestAction(stud2EnrollTestAction);

			seventhRequest.executeAllActions();
			
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

