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

public class MessageTransformNestedQuizzesScenario extends BaseTestScenario{
	private AssignmentFactory assignmentFactory;
	private com.pearson.test.daalt.dataservice.request.action.version01.TestActionFactory testActionFactoryV1;
	@Test
	public void loadBasicTestData() throws Exception {
		testActionFactoryV1 = new com.pearson.test.daalt.dataservice.request.action.version01.SubPubSeerTestActionFactory();
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
		TestDataRequest fourthRequest;
		TestDataRequest fifthRequest;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignmentToAdd;
		Instructor instr;
		Student student1;

		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			fifthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("NestedQuizzes");
			
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
			student1 = new BasicStudent(student01FromConfig.getUserName(), 
					student01FromConfig.getPassword(), 
					student01FromConfig.getId(),
					student01FromConfig.getFirstName(),
					student01FromConfig.getLastName());
			testData.addStudent(student1);
			

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
			firstRequest.addTestAction(createCourseSection);
		
			// We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
			//add action to enroll instructor
			TestAction enrollInstr = testActionFactoryV1.getEnrollInstructorTestAction(instr, courseSection);
			firstRequest.addTestAction(enrollInstr);
			
			//enroll student 1
			courseSection.addStudent(student1);
			//add action to enroll student
			TestAction enrollStudent1 = testActionFactoryV1.getEnrollStudentTestAction(student1, courseSection);
			firstRequest.addTestAction(enrollStudent1);	
			
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.NESTED_QUIZZES_UNDER_PRACTICE);  //NESTED_QUIZZES_UNDER_PRACTICE
			System.out.println(assignmentToAdd.getStructure());
			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			assignmentToAdd.setDueDate(nowCal.getTime());
			//assignmentToAdd.setSequenceNumber(0f);
			assignmentToAdd.setTitle("DAALT SQE Test Assignment");
			courseSection.addAssignment(assignmentToAdd);
			//add action to add assignment to course section
			TestAction addAssignment = testActionFactoryV1.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
			firstRequest.addTestAction(addAssignment);
			
			firstRequest.executeAllActions();
	
			////---- Student 1
			//Student 1 - Chapter Quiz
			Quiz chapQuiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			for(Question ques : chapQuiz.getQuestions()){
				Answer correctAnswer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
					= getMultiValueRadioButtonAttempt(ques, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
							correctAnswer, ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				
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
			
			secondRequest.executeAllActions();
			
			
			//Student 1 - nested Quiz 1
			Quiz nestedQuiz1 = assignmentToAdd.getChapters().get(0).getChapterQuiz().getNestedQuizzes().get(0);
			for(Question ques : nestedQuiz1.getQuestions()){
				Answer correctAnswer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
					= getMultiValueRadioButtonAttempt(ques, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
							correctAnswer, ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student1, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, nestedQuiz1, ques, attempt);
				thirdRequest.addTestAction(attemptQuestionTestAction);
				
				
			}
			//Student 1 - Complete Quiz
			QuizCompletionActivity nestedQuiz1CompletionActivity = new QuizCompletionActivity(student1);
			for(Question ques : nestedQuiz1.getQuestions()){
				nestedQuiz1CompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			nestedQuiz1.addCompletionActivity(nestedQuiz1CompletionActivity);
			TestAction nestedQuiz1TestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, nestedQuiz1, nestedQuiz1CompletionActivity);
			thirdRequest.addTestAction(nestedQuiz1TestAction);
			
			thirdRequest.executeAllActions();
			
			
			//Student 1 - nested Quiz 2
			Quiz nestedQuiz2 = assignmentToAdd.getChapters().get(0).getChapterQuiz().getNestedQuizzes().get(1);
			for(Question ques : nestedQuiz2.getQuestions()){
				Answer correctAnswer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
					= getMultiValueRadioButtonAttempt(ques, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
							correctAnswer, ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student1, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, nestedQuiz2, ques, attempt);
				fourthRequest.addTestAction(attemptQuestionTestAction);
				
	
			}
			//Student 1 - Complete Quiz
			QuizCompletionActivity nestedQuiz2CompletionActivity = new QuizCompletionActivity(student1);
			for(Question ques : nestedQuiz2.getQuestions()){
				nestedQuiz2CompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			nestedQuiz2.addCompletionActivity(nestedQuiz2CompletionActivity);
			TestAction nestedQuiz2TestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, nestedQuiz2, nestedQuiz2CompletionActivity);
			fourthRequest.addTestAction(nestedQuiz2TestAction);
			
			fourthRequest.executeAllActions();
			
			TestAction reSendSeedData = testActionFactory.getReSendSeedDataTestAction(courseSection, assignmentToAdd);
			fifthRequest.addTestAction(reSendSeedData);

			fifthRequest.executeAllActions();
			
		} catch (Exception e) {
			getEngine().getSuite().setDidCreationTestsComplete(false);
			throw e;
		}
		
		
		//pass TestData to validation engine
		DaaltDataServiceValidationEngine validationEngine = new DaaltDataServiceValidationEngine();
		List<Validation> validationList = validationEngine.getValidationsForTestData(testData);
		for (Validation val : validationList) {
			System.out.println(val.getExpectedResultsPrintString());
			getCurrentTestCase().getValidations().add(val);
		}
	}
}
