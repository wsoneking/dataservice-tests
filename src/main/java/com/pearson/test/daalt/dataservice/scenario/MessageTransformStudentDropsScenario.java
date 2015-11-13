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

public class MessageTransformStudentDropsScenario extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;
	private com.pearson.test.daalt.dataservice.request.action.version01.TestActionFactory testActionFactoryV1;

	@Test
	public void loadBasicTestData() throws Exception {
		testActionFactoryV1 = new com.pearson.test.daalt.dataservice.request.action.version01.SubPubSeerTestActionFactory();
		
		TestDataRequest zeroRequest;
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
		Student student1;
		Student student2;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			zeroRequest = new BasicTestDataRequest();
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			fifthRequest = new BasicTestDataRequest();
			sixthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("MessageTransformStudentDropsScenario");
			
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
			student1 = new BasicStudent(student01FromConfig.getUserName(), 
					student01FromConfig.getPassword(), 
					student01FromConfig.getId(),
					student01FromConfig.getFirstName(),
					student01FromConfig.getLastName());
			testData.addStudent(student1);
			
			// add student 2
			com.pearson.test.daalt.dataservice.User student02FromConfig = getEngine().getStudent02();
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
			TestAction createCourseSection = testActionFactoryV1.getCreateCourseSectionTestAction(courseSection);
			zeroRequest.addTestAction(createCourseSection);
			
			zeroRequest.executeAllActions();
			
			//sleep to ensure that Course_Section_Learning_Resource arrives before Learning_Module_Content
			Thread.sleep(10000);
		
			//We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
			//add action to enroll instructor
			TestAction enrollInstr = testActionFactoryV1.getEnrollInstructorTestAction(instr, courseSection);
			firstRequest.addTestAction(enrollInstr);
			
			//enroll student1
			courseSection.addStudent(student1);
			//add action to enroll student1
			TestAction enrollStudent1 = testActionFactoryV1.getEnrollStudentTestAction(student1, courseSection);
			firstRequest.addTestAction(enrollStudent1);
			//enroll student2
			courseSection.addStudent(student2);
			//add action to enroll student2
			TestAction enrollStudent2 = testActionFactoryV1.getEnrollStudentTestAction(student2, courseSection);
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
			assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CH_SECTION_QUIZ_AND_CH_QUIZ_AND_READING);
			assignmentToAdd.setAllTargetIdsTrue();

			System.out.println(assignmentToAdd.getStructure());
			Calendar nowCal = createDueDate();

			assignmentToAdd.setDueDate(nowCal.getTime());
			
			assignmentToAdd.setTitle("DAALT SQE Test Assignment");
			courseSection.addAssignment(assignmentToAdd);
			//add action to add assignment to course section
			TestAction addAssignment = testActionFactoryV1.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
			firstRequest.addTestAction(addAssignment);
			
			firstRequest.executeAllActions();
	
			//BEGIN : Student1
			//Student1 - Read Page
			LearningActivity stud1LearningActivity = new LearningActivity(student1, /*timeSpentReadingSeconds*/ 120);
			Page stud1Page = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			stud1Page.addLearningActivity(stud1LearningActivity);
			TestAction stud1ReadPageTestAction = testActionFactoryV1.getReadPageTestAction(student1, courseSection, assignmentToAdd, stud1Page, stud1LearningActivity);
			secondRequest.addTestAction(stud1ReadPageTestAction);
			timeOnTaskOutput.append("\n...learning time (stud drops course): " + student1.getPersonId() + " : " + stud1Page.getLearningResourceId() + " : 120 seconds");
	
			//Student 1 - Chapter Quiz
			Quiz chapQuiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			for(Question ques : chapQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student1, /*timeSpent*/ 30, /*attemptNumber*/ 1, 
						answer, /*pointsEarned*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time (stud drops course): " + student1.getPersonId() + " : " + ques.getId() + " : 30 seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student1, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, ques, attempt);
				secondRequest.addTestAction(attemptQuestionTestAction);
		
			}
			
			//Student 1 - Complete Quiz
			QuizCompletionActivity stud1ChapQuizCompletionActivity = new QuizCompletionActivity(student1);
			for(Question ques : chapQuiz.getQuestions()){
				stud1ChapQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapQuiz.addCompletionActivity(stud1ChapQuizCompletionActivity);
			TestAction stud1CompleteChapQuizTestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, stud1ChapQuizCompletionActivity);
			secondRequest.addTestAction(stud1CompleteChapQuizTestAction);
			
			
			//Student1 - Chapter Section Quiz
			Quiz chapSecQuiz = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			for(Question ques : chapSecQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student1, /*timeSpent*/ 40, /*attemptNumber*/ 1, 
						answer, /*pointsEarned*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time (stud drops course): " + student1.getPersonId() + " : " + ques.getId() + " : 40 seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student1, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, ques, attempt);
				secondRequest.addTestAction(attemptQuestionTestAction);
				
				
			}
			
			//Student1 - Complete Quiz
			QuizCompletionActivity stud1ChapSecQuizCompletionActivity = new QuizCompletionActivity(student1);
			for(Question ques : chapSecQuiz.getQuestions()){
				stud1ChapSecQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapSecQuiz.addCompletionActivity(stud1ChapSecQuizCompletionActivity);
			TestAction stud1CompleteChapSecQuizTestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapSecQuiz, stud1ChapSecQuizCompletionActivity);
			secondRequest.addTestAction(stud1CompleteChapSecQuizTestAction);
			
			secondRequest.executeAllActions();
			
			//BEGIN : Student 2
			//Student2 - Read Page
			LearningActivity stud2LearningActivity = new LearningActivity(student2, /*timeSpentReadingSeconds*/ 180);
			Page stud2Page = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			stud2Page.addLearningActivity(stud2LearningActivity);
			TestAction stud2ReadPageTestAction = testActionFactoryV1.getReadPageTestAction(student2, courseSection, assignmentToAdd, stud2Page, stud2LearningActivity);
			thirdRequest.addTestAction(stud2ReadPageTestAction);
			timeOnTaskOutput.append("\n...learning time : " + student2.getPersonId() + " : " + stud2Page.getLearningResourceId() + " : 180 seconds");
	
			//Student 2 - Chapter Quiz
			for(Question ques : chapQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student2, /*timeSpent*/ 50, /*attemptNumber*/ 1, 
						answer, /*pointsEarned*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + ques.getId() + " : 50 seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student2, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, ques, attempt);
				thirdRequest.addTestAction(attemptQuestionTestAction);
				
			}

			//Student2 - Complete Quiz
			QuizCompletionActivity stud2ChapQuizCompletionActivity = new QuizCompletionActivity(student2);
			for(Question ques : chapQuiz.getQuestions()){
				stud2ChapQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapQuiz.addCompletionActivity(stud2ChapQuizCompletionActivity);
			TestAction stud2CompleteChapQuizTestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, stud2ChapQuizCompletionActivity);
			thirdRequest.addTestAction(stud2CompleteChapQuizTestAction);
			
			
			//Student2 - Chapter Section Quiz
			for(Question ques : chapSecQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student2, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						answer, /*pointsEarned*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + ques.getId() + " : 60 seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student2, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, ques, attempt);
				thirdRequest.addTestAction(attemptQuestionTestAction);
				
			}
		
			//Studen2 - Complete Quiz
			QuizCompletionActivity stud2ChapSecQuizCompletionActivity = new QuizCompletionActivity(student2);
			for(Question ques : chapSecQuiz.getQuestions()){
				stud2ChapSecQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapSecQuiz.addCompletionActivity(stud2ChapSecQuizCompletionActivity);
			TestAction stud2CompleteChapSecQuizTestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapSecQuiz, stud2ChapSecQuizCompletionActivity);
			thirdRequest.addTestAction(stud2CompleteChapSecQuizTestAction);
			
			thirdRequest.executeAllActions();
			
			//Student1 - Drop Course
			courseSection.removeStudent(student1);
			TestAction stud1DropCourseTestAction = testActionFactoryV1.getDropCourseTestAction(student1, courseSection);
			fourthRequest.addTestAction(stud1DropCourseTestAction);
			
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
			
			fourthRequest.executeAllActions();
			
			//Student2 - Drop Course
			courseSection.removeStudent(student2);
			TestAction stud2DropCourseTestAction = testActionFactoryV1.getDropCourseTestAction(student2, courseSection);
			fifthRequest.addTestAction(stud2DropCourseTestAction);
			
			//Student2 - Enroll Again
			courseSection.addStudent(student2);
			TestAction stud2EnrollTestAction = testActionFactoryV1.getEnrollStudentTestAction(student2, courseSection);
			fifthRequest.addTestAction(stud2EnrollTestAction);
			
			fifthRequest.executeAllActions();
			
			TestAction reSendSeedData = testActionFactory.getReSendSeedDataTestAction(courseSection, assignmentToAdd);
			sixthRequest.addTestAction(reSendSeedData);

			sixthRequest.executeAllActions();

			assignmentToAdd.setDueDatePassed(true);
			
			
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
