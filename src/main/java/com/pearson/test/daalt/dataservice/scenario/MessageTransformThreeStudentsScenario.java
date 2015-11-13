package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.List;

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

public class MessageTransformThreeStudentsScenario extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;
	private com.pearson.test.daalt.dataservice.request.action.version01.TestActionFactory testActionFactoryV1;

	@Test
	public void loadTestData() throws Exception {
		testActionFactoryV1 = new com.pearson.test.daalt.dataservice.request.action.version01.SubPubSeerTestActionFactory();
		
		TestDataRequest zeroRequest;
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
		TestDataRequest fourthRequest;
		TestDataRequest fifthRequest;
		TestDataRequest sixthRequest;
		TestDataRequest seventhRequest;
		TestDataRequest eigthRequest;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignmentToAdd;
		Instructor instr;
		Student student1;
		Student student2;
		Student student3;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			zeroRequest = new BasicTestDataRequest();
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			fifthRequest = new BasicTestDataRequest();
			sixthRequest = new BasicTestDataRequest();
			seventhRequest = new BasicTestDataRequest();
			eigthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("MessageTransformThreeStudentsScenario");
			
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
			
			//add student3 to test data
			student3 = new BasicStudent(getUserConfig().getValue(TestEngine.student03UsernamePropName),
					getUserConfig().getValue(TestEngine.student03PasswordPropName),
					getUserConfig().getValue(TestEngine.student03IdPropName),
					getUserConfig().getValue(TestEngine.student03FirstName),
					getUserConfig().getValue(TestEngine.student03LastName));
			testData.addStudent(student3);
			
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
			//enroll student3
			courseSection.addStudent(student3);
			//add action to enroll student3
			TestAction enrollStudent3 = testActionFactoryV1.getEnrollStudentTestAction(student3, courseSection);
			firstRequest.addTestAction(enrollStudent3);
			
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
			
			
			//Student1 - Chapter Quiz
			Quiz chapQuiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			for(Question ques : chapQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student1, /*timeSpent*/ 70, /*attemptNumber*/ 1, 
						answer, /*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + ques.getId() + " : 70 seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student1, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, ques, attempt);
				secondRequest.addTestAction(attemptQuestionTestAction);
				
				
			}
			//Student1 - Complete Chapter Quiz
			QuizCompletionActivity stud1ChapQuizCompletionActivity = new QuizCompletionActivity(student1);
			for(Question ques : chapQuiz.getQuestions()){
				stud1ChapQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapQuiz.addCompletionActivity(stud1ChapQuizCompletionActivity);
			TestAction stud1CompleteChapQuizTestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, stud1ChapQuizCompletionActivity);
			secondRequest.addTestAction(stud1CompleteChapQuizTestAction);
			
			secondRequest.executeAllActions();
			
			//Student1 - Chapter Section Quiz
			Quiz chapSecQuiz = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			for(Question ques : chapSecQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student1, /*timeSpent*/ 90, /*attemptNumber*/ 1, 
						answer, /*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + ques.getId() + " : 90 seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student1, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, ques, attempt);
				thirdRequest.addTestAction(attemptQuestionTestAction);
				
			}
			
			//Student1 - Complete Chapter Section Quiz
			QuizCompletionActivity stud1ChapSecQuizCompletionActivity = new QuizCompletionActivity(student1);
			for(Question ques : chapSecQuiz.getQuestions()){
				stud1ChapSecQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapSecQuiz.addCompletionActivity(stud1ChapSecQuizCompletionActivity);
			TestAction stud1CompleteChapSecQuizTestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapSecQuiz, stud1ChapSecQuizCompletionActivity);
			thirdRequest.addTestAction(stud1CompleteChapSecQuizTestAction);
			
			thirdRequest.executeAllActions();			
			
			//Student2 - Chapter Quiz
			for(Question ques : chapQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student2, /*timeSpent*/ 20, /*attemptNumber*/ 1, 
						answer, /*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + ques.getId() + " : 20 seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student2, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, ques, attempt);
				fourthRequest.addTestAction(attemptQuestionTestAction);
				
				
			}
			//Student2 - Complete Chapter Quiz
			QuizCompletionActivity stud2ChapQuizCompletionActivity = new QuizCompletionActivity(student2);
			for(Question ques : chapQuiz.getQuestions()){
				stud2ChapQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapQuiz.addCompletionActivity(stud2ChapQuizCompletionActivity);
			TestAction stud2CompleteChapQuizTestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, stud2ChapQuizCompletionActivity);
			fourthRequest.addTestAction(stud2CompleteChapQuizTestAction);
			
			fourthRequest.executeAllActions();
			
			//Student2 - Chapter Section Quiz
			for(Question ques : chapSecQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student2, /*timeSpent*/ 40, /*attemptNumber*/ 1, 
						answer, /*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + ques.getId() + " : 40 seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student2, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, ques, attempt);
				fifthRequest.addTestAction(attemptQuestionTestAction);
				
				
			}
			
			//Student2 - Complete Chapter Section Quiz
			QuizCompletionActivity stud2ChapSecQuizCompletionActivity = new QuizCompletionActivity(student2);
			for(Question ques : chapSecQuiz.getQuestions()){
				stud2ChapSecQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapSecQuiz.addCompletionActivity(stud2ChapSecQuizCompletionActivity);
			TestAction stud2CompleteChapSecQuizTestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapSecQuiz, stud2ChapSecQuizCompletionActivity);
			fifthRequest.addTestAction(stud2CompleteChapSecQuizTestAction);
			
			fifthRequest.executeAllActions();
			
			//Student3 - Chapter Quiz
			for(Question ques : chapQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student3, /*timeSpent*/ 30, /*attemptNumber*/ 1, 
						answer, /*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student3.getPersonId() + " : " + ques.getId() + " : 30 seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student3, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, ques, attempt);
				sixthRequest.addTestAction(attemptQuestionTestAction);
				
				
			}
			//Student3 - Complete Chapter Quiz
			QuizCompletionActivity stud3ChapQuizCompletionActivity = new QuizCompletionActivity(student3);
			for(Question ques : chapQuiz.getQuestions()){
				stud3ChapQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapQuiz.addCompletionActivity(stud3ChapQuizCompletionActivity);
			TestAction stud3CompleteChapQuizTestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, stud3ChapQuizCompletionActivity);
			sixthRequest.addTestAction(stud3CompleteChapQuizTestAction);
			
			sixthRequest.executeAllActions();
			
			//Student3 - Chapter Section Quiz
			for(Question ques : chapSecQuiz.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student3, /*timeSpent*/ 50, /*attemptNumber*/ 1, 
						answer, /*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student3.getPersonId() + " : " + ques.getId() + " : 50 seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student3, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, ques, attempt);
				seventhRequest.addTestAction(attemptQuestionTestAction);
				
				
			}
			
			//Student3 - Complete Chapter Section Quiz
			QuizCompletionActivity stud3ChapSecQuizCompletionActivity = new QuizCompletionActivity(student3);
			for(Question ques : chapSecQuiz.getQuestions()){
				stud3ChapSecQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapSecQuiz.addCompletionActivity(stud3ChapSecQuizCompletionActivity);
			TestAction stud3CompleteChapSecQuizTestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapSecQuiz, stud3ChapSecQuizCompletionActivity);
			seventhRequest.addTestAction(stud3CompleteChapSecQuizTestAction);
			
			seventhRequest.executeAllActions();
			
			TestAction reSendSeedData = testActionFactory.getReSendSeedDataTestAction(courseSection, assignmentToAdd);
			eigthRequest.addTestAction(reSendSeedData);

			eigthRequest.executeAllActions();
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
