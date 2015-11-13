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
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;

/**
 *Chapter Quiz Question1 - Student1 and Student2 answer correctly in one attempt.
 *Chapter Quiz Question2 - Student1 answer correctly in one attempt. Student2 answer correctly in two attempts.
 *Chapter Quiz Question3 - Student1 answer correctly in two attempts. Student2 answer incorrectly in three attempts.
 *Chapter Section Quiz Question1 - Student1 and Student2 answer correctly in three attempts. 
 *Chapter Section Quiz Question2 - Student1 and Student2 answer incorrectly in three attempts.
 *Chapter Section Quiz Question3 - Student1 answer incorrectly in three attempts. Student2 does not attempt.
 */

public class ItemAnalysisBigComplexTestScenario extends BaseTestScenario {
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
		Student student1;
		Student student2;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			firstRequest = new BasicTestDataRequest();
			firstRequest.setSeerMessageCount(2);
			firstRequest.setSubPubMessageCount(2);
			secondRequest = new BasicTestDataRequest();
			secondRequest.setSeerMessageCount(2);
			secondRequest.setSubPubMessageCount(2);
			thirdRequest = new BasicTestDataRequest();
			thirdRequest.setSeerMessageCount(2);
			thirdRequest.setSubPubMessageCount(2);
			fourthRequest = new BasicTestDataRequest();
			fourthRequest.setSeerMessageCount(2);
			fourthRequest.setSubPubMessageCount(2);
			fifthRequest = new BasicTestDataRequest();
			fifthRequest.setSeerMessageCount(2);
			fifthRequest.setSubPubMessageCount(2);
			sixthRequest = new BasicTestDataRequest();
			sixthRequest.setSeerMessageCount(2);
			sixthRequest.setSubPubMessageCount(2);
			testData = new BasicTestData("ItemAnalysisBigComplexTestScenario");
			
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
	
			//Read Page - Student1 reads Chapter Section
			LearningActivity stud1LearningActivity = new LearningActivity(student1, 120);
			Page stud1Page = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			stud1Page.addLearningActivity(stud1LearningActivity);
			TestAction stud1ReadPageTestAction = testActionFactory.getReadPageTestAction(courseSection, stud1Page, stud1LearningActivity);
			secondRequest.addTestAction(stud1ReadPageTestAction);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + stud1Page.getLearningResourceId() + " : 120 seconds");
			
			//Chapter Quiz
			Quiz chapQuiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			//Chapter Section Quiz
			Quiz chapSecQuiz = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			
			//BEGIN: Student 1 - Chapter Quiz
			//Chapter Quiz Question1 - Student1 answer correctly in one attempt
			Question chapQuizQues1 = chapQuiz.getQuestions().get(0);
			Answer chapQuizQues1CorrectAnswer = chapQuizQues1.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			//Student1 takes one attempt to answer question1 correctly - Final Attempt
			MultiValueAttempt stud1ChapQuizQues1Attempt1 
				= getMultiValueRadioButtonAttempt(chapQuizQues1, student1, /*timeSpent*/ 30, /*attemptNumber*/ 1, chapQuizQues1CorrectAnswer, 
						chapQuizQues1.getPointsPossible(), /*isFinalAttempt*/ true);
			//add action to add student1 attempt1 to question1
			chapQuizQues1.addAttempt(stud1ChapQuizQues1Attempt1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapQuizQues1.getId() + " : 30 seconds");
			
			//Student 1 -Question Completion Activity - Chapter Quiz - Question 1
			QuestionCompletionActivity stud1ChapQuizQuestion1CompletionActivity = new QuestionCompletionActivity(student1, chapQuizQues1.getPointsPossible());
			chapQuizQues1.addCompletionActivity(stud1ChapQuizQuestion1CompletionActivity);
			TestAction stud1Attempt1ChapQuizQuestion1TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, chapQuizQues1, stud1ChapQuizQues1Attempt1, stud1ChapQuizQuestion1CompletionActivity);
			secondRequest.addTestAction(stud1Attempt1ChapQuizQuestion1TestAction);

			//Chapter Quiz Question2 - Student1 answer correctly in one attempt
			Question chapQuizQues2 = chapQuiz.getQuestions().get(1);
			Answer chapQuizQues2CorrectAnswer = chapQuizQues2.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			//Student1 takes one attempt to answer question2 correctly - Final Attempt
			MultiValueAttempt stud1ChapQuizQues2Attempt1 = getMultiValueRadioButtonAttempt(chapQuizQues2, student1, /*timeSpent*/ 35, /*attemptNumber*/ 1, chapQuizQues2CorrectAnswer, chapQuizQues2.getPointsPossible(), /*isFinalAttempt*/ true);
			//add action to add student1 attempt1 to question2
			chapQuizQues2.addAttempt(stud1ChapQuizQues2Attempt1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapQuizQues2.getId() + " : 35 seconds");
			
			//Student 1 -Question Completion Activity - Chapter Quiz - Question 2
			QuestionCompletionActivity stud1ChapQuizQuestion2CompletionActivity = new QuestionCompletionActivity(student1, chapQuizQues2.getPointsPossible());
			chapQuizQues2.addCompletionActivity(stud1ChapQuizQuestion2CompletionActivity);
			TestAction stud1Attempt1ChapQuizQuestion2TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, chapQuizQues2, stud1ChapQuizQues2Attempt1, stud1ChapQuizQuestion2CompletionActivity);
			secondRequest.addTestAction(stud1Attempt1ChapQuizQuestion2TestAction);
			
			//Chapter Quiz Question3 - Student1 answer correctly in two attempts
			Question chapQuizQues3 = chapQuiz.getQuestions().get(2);
			Answer chapQuizQues3CorrectAnswer = chapQuizQues3.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			Answer chapQuizQues3IncorrectAnswer = chapQuizQues3.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);
			//Student1 takes two attempts to answer question3 correctly
			//First attempt to answer incorrectly
			MultiValueAttempt stud1ChapQuizQues3Attempt1 = getMultiValueRadioButtonAttempt(chapQuizQues3, student1, /*timeSpent*/ 20, /*attemptNumber*/ 1, chapQuizQues3IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			//add action to add student1 attempt1 to question3
			chapQuizQues3.addAttempt(stud1ChapQuizQues3Attempt1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapQuizQues3.getId() + " : 20 seconds");
			TestAction stud1Attempt1ChapQuizQuestion3TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, chapQuizQues3, stud1ChapQuizQues3Attempt1, null);
			secondRequest.addTestAction(stud1Attempt1ChapQuizQuestion3TestAction);
			//Second attempt to answer correctly - Final getAttempt
			MultiValueAttempt stud1ChapQuizQues3Attempt2 = getMultiValueRadioButtonAttempt(chapQuizQues3, student1, /*timeSpent*/  25, /*attemptNumber*/ 2, chapQuizQues3CorrectAnswer, 2f, /*isFinalAttempt*/ true);
			chapQuizQues3.addAttempt(stud1ChapQuizQues3Attempt2);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapQuizQues3.getId() + " : 25 seconds");
			
			//Student 1 -Question Completion Activity - Chapter Quiz - Question 3
			QuestionCompletionActivity stud1ChapQuizQuestion3CompletionActivity = new QuestionCompletionActivity(student1, 2f);
			chapQuizQues3.addCompletionActivity(stud1ChapQuizQuestion3CompletionActivity);
			//add action to add student1 attempt2 to question3
			TestAction stud1Attempt2ChapQuizQuestion3TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, chapQuizQues3, stud1ChapQuizQues3Attempt2, stud1ChapQuizQuestion3CompletionActivity);
			secondRequest.addTestAction(stud1Attempt2ChapQuizQuestion3TestAction);
			
			//instatiate Chapter Quiz completion object for student1
			QuizCompletionActivity stud1ChapQuizCompletionActivity = new QuizCompletionActivity(student1);
			// add points earned by student1 to performancePerQuestion
			stud1ChapQuizCompletionActivity.addQuestionPerf(chapQuizQues1.getId(), chapQuizQues1.getPointsPossible());
			// add points earned by student1 performancePerQuestion
			stud1ChapQuizCompletionActivity.addQuestionPerf(chapQuizQues2.getId(), chapQuizQues2.getPointsPossible());
			// add points earned by student1 performancePerQuestion
			stud1ChapQuizCompletionActivity.addQuestionPerf(chapQuizQues3.getId(), 2f);
			//Complete Chapter Quiz for student1
			chapQuiz.addCompletionActivity(stud1ChapQuizCompletionActivity);
			TestAction stud1CompleteChapQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, stud1ChapQuizCompletionActivity);
			secondRequest.addTestAction(stud1CompleteChapQuizTestAction);	
			
			secondRequest.executeAllActions();
			
			//BEGIN: Student 1 - Chapter Section Quiz
			//Chapter Section Quiz Question1 - Student1 answer correctly in three attempts
			Question chapSecQuizQues1 = chapSecQuiz.getQuestions().get(0);
			Answer chapSecQuizQues1CorrectAnswer = chapSecQuizQues1.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			Answer chapSecQuizQues1IncorrectAnswer = chapSecQuizQues1.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);
			//Student1 takes three attempts to answer question1 correctly
			//First attempt to answer incorrectly
			MultiValueAttempt stud1ChapSecQuizQues1Attempt1 = getMultiValueRadioButtonAttempt(chapSecQuizQues1, student1, /*timeSpent*/ 40, /*attemptNumber*/ 1, chapSecQuizQues1IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			chapSecQuizQues1.addAttempt(stud1ChapSecQuizQues1Attempt1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapSecQuizQues1.getId() + " : 40 seconds");
			//add action to add student1 attempt1 to question1
			TestAction stud1Attempt1ChapSecQuizQuestion1TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues1, stud1ChapSecQuizQues1Attempt1, null);
			thirdRequest.addTestAction(stud1Attempt1ChapSecQuizQuestion1TestAction);
			//Second attempt to answer incorrectly
			MultiValueAttempt stud1ChapSecQuizQues1Attempt2 = getMultiValueRadioButtonAttempt(chapSecQuizQues1, student1, /*timeSpent*/ 25, /*attemptNumber*/ 2, chapSecQuizQues1IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			chapSecQuizQues1.addAttempt(stud1ChapSecQuizQues1Attempt2);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapSecQuizQues1.getId() + " : 25 seconds");
			//add action to add student1 attempt2 to question1
			TestAction stud1Attempt2ChapSecQuizQuestion1TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues1, stud1ChapSecQuizQues1Attempt2, null);
			thirdRequest.addTestAction(stud1Attempt2ChapSecQuizQuestion1TestAction);
			//Third attempt to answer correctly - Final Attempt
			MultiValueAttempt stud1ChapSecQuizQues1Attempt3 = getMultiValueRadioButtonAttempt(chapSecQuizQues1, student1,/*timeSpent*/ 20, /*attemptNumber*/ 3, chapSecQuizQues1CorrectAnswer, 1f, /*isFinalAttempt*/ true);
			chapSecQuizQues1.addAttempt(stud1ChapSecQuizQues1Attempt3);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapSecQuizQues1.getId() + " : 20 seconds");
			//Student 1 - Question Completion Activity - Chapter Section Quiz - Question 1
			QuestionCompletionActivity stud1ChapSecQuizQuestion1CompletionActivity = new QuestionCompletionActivity(student1, 1f);
			chapSecQuizQues1.addCompletionActivity(stud1ChapSecQuizQuestion1CompletionActivity);
			//add action to add student1 attempt3 to question1
			TestAction stud1Attempt3ChapSecQuizQuestion1TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues1, stud1ChapSecQuizQues1Attempt3, stud1ChapSecQuizQuestion1CompletionActivity);
			thirdRequest.addTestAction(stud1Attempt3ChapSecQuizQuestion1TestAction);			
			
			//Chapter Section Quiz Question2 - Student1 answer incorrectly in three attempts
			Question chapSecQuizQues2 = chapSecQuiz.getQuestions().get(1);
			Answer chapSecQuizQues2IncorrectAnswer = chapSecQuizQues2.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);
			//Student1 takes three attempts to answer question2 incorrectly
			//First attempt to answer incorrectly
			MultiValueAttempt stud1ChapSecQuizQues2Attempt1 = getMultiValueRadioButtonAttempt(chapSecQuizQues2, student1, /*timeSpent*/ 20, /*attemptNumber*/ 1, chapSecQuizQues2IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			chapSecQuizQues2.addAttempt(stud1ChapSecQuizQues2Attempt1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapSecQuizQues2.getId() + " : 20 seconds");
			//add action to add student1 attempt1 to question2
			TestAction stud1Attempt1ChapSecQuizQuestion2TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues2, stud1ChapSecQuizQues2Attempt1, null);
			thirdRequest.addTestAction(stud1Attempt1ChapSecQuizQuestion2TestAction);
			//Second attempt to answer incorrectly
			MultiValueAttempt stud1ChapSecQuizQues2Attempt2 = getMultiValueRadioButtonAttempt(chapSecQuizQues2, student1, /*timeSpent*/ 25, /*attemptNumber*/ 2, chapSecQuizQues2IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			chapSecQuizQues2.addAttempt(stud1ChapSecQuizQues2Attempt2);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapSecQuizQues2.getId() + " : 25 seconds");
			//add action to add student1 attempt2 to question2
			TestAction stud1Attempt2ChapSecQuizQuestion2TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues2, stud1ChapSecQuizQues2Attempt2, null);
			thirdRequest.addTestAction(stud1Attempt2ChapSecQuizQuestion2TestAction);
			//Third attempt to answer incorrectly - Final Attempt
			MultiValueAttempt stud1ChapSecQuizQues2Attempt3 = getMultiValueRadioButtonAttempt(chapSecQuizQues2, student1, /*timeSpent*/ 35, /*attemptNumber*/ 3, chapSecQuizQues2IncorrectAnswer, 0f, /*isFinalAttempt*/ true);
			chapSecQuizQues2.addAttempt(stud1ChapSecQuizQues2Attempt3);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapSecQuizQues2.getId() + " : 35 seconds");
			//Student 1 - Question Completion Activity - Chapter Section Quiz - Question 2
			QuestionCompletionActivity stud1ChapSecQuizQuestion2CompletionActivity = new QuestionCompletionActivity(student1, 0f);
			chapSecQuizQues2.addCompletionActivity(stud1ChapSecQuizQuestion2CompletionActivity);
			//add action to add student1 attempt3 to question2
			TestAction stud1Attempt3ChapSecQuizQuestion2TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues2, stud1ChapSecQuizQues2Attempt3, stud1ChapSecQuizQuestion2CompletionActivity);
			thirdRequest.addTestAction(stud1Attempt3ChapSecQuizQuestion2TestAction);
			
			//Chapter Section Quiz Question3 - Student1 answer incorrectly in three attempts
			Question chapSecQuizQues3 = chapSecQuiz.getQuestions().get(2);
			Answer chapSecQuizQues3IncorrectAnswer = chapSecQuizQues3.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);
			//Student1 takes three attempt to answer question3 incorrectly
			//First Attempt to answer incorrectly
			MultiValueAttempt stud1ChapSecQuizQues3Attempt1 = getMultiValueRadioButtonAttempt(chapSecQuizQues3, student1, /*timeSpent*/ 20, /*attemptNumber*/ 1, chapSecQuizQues3IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			chapSecQuizQues3.addAttempt(stud1ChapSecQuizQues3Attempt1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapSecQuizQues3.getId() + " : 20 seconds");
			//add action to add student1 attempt1 to question3
			TestAction stud1Attempt1ChapSecQuizQuestion3TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues3, stud1ChapSecQuizQues3Attempt1, null);
			thirdRequest.addTestAction(stud1Attempt1ChapSecQuizQuestion3TestAction);
			//Second Attempt to answer incorrectly
			MultiValueAttempt stud1ChapSecQuizQues3Attempt2 = getMultiValueRadioButtonAttempt(chapSecQuizQues3, student1, /*timeSpent*/ 25, /*attemptNumber*/ 2, chapSecQuizQues3IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			chapSecQuizQues3.addAttempt(stud1ChapSecQuizQues3Attempt2);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapSecQuizQues3.getId() + " : 25 seconds");
			//add action to add student1 attempt2 to question3
			TestAction stud1Attempt2ChapSecQuizQuestion3TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues3, stud1ChapSecQuizQues3Attempt2, null);
			thirdRequest.addTestAction(stud1Attempt2ChapSecQuizQuestion3TestAction);
			//Third attempt to answer incorrectly - Final Attempt
			MultiValueAttempt stud1ChapSecQuizQues3Attempt3 = getMultiValueRadioButtonAttempt(chapSecQuizQues3, student1, /*timeSpent*/ 35, /*attemptNumber*/ 3, chapSecQuizQues3IncorrectAnswer, 0f, /*isFinalAttempt*/ true);
			chapSecQuizQues3.addAttempt(stud1ChapSecQuizQues3Attempt3);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapSecQuizQues3.getId() + " : 35 seconds");
			//Student 1 - Question Completion Activity - Chapter Section Quiz - Question 3
			QuestionCompletionActivity stud1ChapSecQuizQuestion3CompletionActivity = new QuestionCompletionActivity(student1, 0f);
			chapSecQuizQues3.addCompletionActivity(stud1ChapSecQuizQuestion3CompletionActivity);
			//add action to add student1 attempt3 to question2
			TestAction stud1Attempt3ChapSecQuizQuestion3TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues3, stud1ChapSecQuizQues3Attempt3, stud1ChapSecQuizQuestion3CompletionActivity);
			thirdRequest.addTestAction(stud1Attempt3ChapSecQuizQuestion3TestAction);	
			
			//Complete Chapter Section Quiz for student1
			QuizCompletionActivity stud1ChapSecQuizCompletionActivity = new QuizCompletionActivity(student1);
			// add points earned by student1 performancePerQuestion
			stud1ChapSecQuizCompletionActivity.addQuestionPerf(chapSecQuizQues1.getId(), 1f);
			// add points earned by student1 performancePerQuestion
			stud1ChapSecQuizCompletionActivity.addQuestionPerf(chapSecQuizQues2.getId(), 0f);
			// add points earned by student1 performancePerQuestion
			stud1ChapSecQuizCompletionActivity.addQuestionPerf(chapSecQuizQues3.getId(), 0f);
			chapSecQuiz.addCompletionActivity(stud1ChapSecQuizCompletionActivity);
			TestAction stud1CompleteChapSecQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapSecQuiz, stud1ChapSecQuizCompletionActivity);
			thirdRequest.addTestAction(stud1CompleteChapSecQuizTestAction);
			
			thirdRequest.executeAllActions();
			
			//Read Page - Student2 reads Chapter Section
			LearningActivity stud2LearningActivity = new LearningActivity(student2, 180);
			Page stud2Page = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			stud2Page.addLearningActivity(stud2LearningActivity);
			TestAction stud2ReadPageTestAction = testActionFactory.getReadPageTestAction(courseSection, stud2Page, stud2LearningActivity);
			fourthRequest.addTestAction(stud2ReadPageTestAction);
			timeOnTaskOutput.append("\n...learning time : " + student2.getPersonId() + " : " + stud2Page.getLearningResourceId() + " : 180 seconds");
			
			//BEGIN: Student 2 - Chapter Quiz
			//Student2 takes one attempt to answer question1 correctly - Final Attempt
			MultiValueAttempt stud2ChapQuizQues1Attempt1 = getMultiValueRadioButtonAttempt(chapQuizQues1, student2, /*timeSpent*/ 40, /*attemptNumber*/1, chapQuizQues1CorrectAnswer, chapQuizQues1.getPointsPossible(), /*isFinalAttempt*/ true);
			//add action to add student2 attempt1 to question1
			chapQuizQues1.addAttempt(stud2ChapQuizQues1Attempt1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapQuizQues1.getId() + " : 40 seconds");
			//Student 2 -Question Completion Activity - Chapter Quiz - Question 1
			QuestionCompletionActivity stud2ChapQuizQuestion1CompletionActivity = new QuestionCompletionActivity(student2, chapQuizQues1.getPointsPossible());
			chapQuizQues1.addCompletionActivity(stud2ChapQuizQuestion1CompletionActivity);
			TestAction stud2Attempt1ChapQuizQuestion1TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, chapQuizQues1, stud2ChapQuizQues1Attempt1, stud2ChapQuizQuestion1CompletionActivity);
			fourthRequest.addTestAction(stud2Attempt1ChapQuizQuestion1TestAction);
			
			//Student2 takes two attempts to answer question2 correctly
			Answer chapQuizQues2IncorrectAnswer = chapQuizQues2.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);
			//First attempt to answer incorrectly
			MultiValueAttempt stud2ChapQuizQues2Attempt1 = getMultiValueRadioButtonAttempt(chapQuizQues2, student2, /*timeSpent*/ 45, /*attemptNumber*/ 1, chapQuizQues2IncorrectAnswer, 0f, /*isFinalAttempt*/ false); 
			//add action to add student2 attempt1 to question2
			chapQuizQues2.addAttempt(stud2ChapQuizQues2Attempt1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapQuizQues2.getId() + " : 45 seconds");
			TestAction stud2Attempt1ChapQuizQuestion2TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, chapQuizQues2, stud2ChapQuizQues2Attempt1, null);
			fourthRequest.addTestAction(stud2Attempt1ChapQuizQuestion2TestAction);
			//Second attempt to answer correctly - Final Attempt
			MultiValueAttempt stud2ChapQuizQues2Attempt2 = getMultiValueRadioButtonAttempt(chapQuizQues2, student2, /*timeSpent*/ 55, /*attemptNumber*/ 2, chapQuizQues2CorrectAnswer, 2f, /*isFinalAttempt*/ true);
			chapQuizQues2.addAttempt(stud2ChapQuizQues2Attempt2);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapQuizQues2.getId() + " : 55 seconds");
			//Student 2 -Question Completion Activity - Chapter Quiz - Question 2
			QuestionCompletionActivity stud2ChapQuizQuestion2CompletionActivity = new QuestionCompletionActivity(student2, 2f);
			chapQuizQues2.addCompletionActivity(stud2ChapQuizQuestion2CompletionActivity);
			//add action to add student2 attempt2 to question2
			TestAction stud2Attempt2ChapQuizQuestion2TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, chapQuizQues2, stud2ChapQuizQues2Attempt2, stud2ChapQuizQuestion2CompletionActivity);
			fourthRequest.addTestAction(stud2Attempt2ChapQuizQuestion2TestAction);
			
			//Student2 takes three attempts to answer question3 incorrectly
			//First attempt to answer incorrectly
			MultiValueAttempt stud2ChapQuizQues3Attempt1 = getMultiValueRadioButtonAttempt(chapQuizQues3, student2, /*timeSpent*/ 45, /*attemptNumber*/ 1, chapQuizQues3IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			chapQuizQues3.addAttempt(stud2ChapQuizQues3Attempt1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapQuizQues3.getId() + " : 45 seconds");
			//add action to add student2 attempt1 to question3
			TestAction stud2Attempt1ChapQuizQuestion3TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, chapQuizQues3, stud2ChapQuizQues3Attempt1, null);
			fourthRequest.addTestAction(stud2Attempt1ChapQuizQuestion3TestAction);
			//Second attempt to answer incorrectly
			MultiValueAttempt stud2ChapQuizQues3Attempt2 = getMultiValueRadioButtonAttempt(chapQuizQues3, student2, /*timeSpent*/ 60, /*attemptNumber*/ 2, chapQuizQues3IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			chapQuizQues3.addAttempt(stud2ChapQuizQues3Attempt2);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapQuizQues3.getId() + " : 60 seconds");
			//add action to add student2 attempt2 to question3
			TestAction stud2Attempt2ChapQuizQuestion3TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, chapQuizQues3, stud2ChapQuizQues3Attempt2, null);
			fourthRequest.addTestAction(stud2Attempt2ChapQuizQuestion3TestAction);
			//Third attempt to answer incorrectly - Final Attempt
			MultiValueAttempt stud2ChapQuizQues3Attempt3 = getMultiValueRadioButtonAttempt(chapQuizQues3, student2, /*timeSpent*/ 32, /*attemptNumber*/ 3, chapQuizQues3IncorrectAnswer, 0f, /*isFinalAttempt*/ true);
			chapQuizQues3.addAttempt(stud2ChapQuizQues3Attempt3);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapQuizQues3.getId() + " : 32 seconds");
			//Student 2 -Question Completion Activity - Chapter Quiz - Question 3
			QuestionCompletionActivity stud2ChapQuizQuestion3CompletionActivity = new QuestionCompletionActivity(student2, 0f);
			chapQuizQues3.addCompletionActivity(stud2ChapQuizQuestion3CompletionActivity);
			//add action to add student2 attempt3 to question3
			TestAction stud2Attempt3ChapQuizQuestion3TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, chapQuizQues3, stud2ChapQuizQues3Attempt3, stud2ChapQuizQuestion3CompletionActivity);
			fourthRequest.addTestAction(stud2Attempt3ChapQuizQuestion3TestAction);
			
			//instatiate Chapter Quiz completion object for student2
			QuizCompletionActivity stud2ChapQuizCompletionActivity = new QuizCompletionActivity(student2);
			// add points earned by student2 to performancePerQuestion
			stud2ChapQuizCompletionActivity.addQuestionPerf(chapQuizQues1.getId(), chapQuizQues1.getPointsPossible());
			// add points earned by student2 to performancePerQuestion
			stud2ChapQuizCompletionActivity.addQuestionPerf(chapQuizQues2.getId(), 2f);
			// add points earned by student2 to performancePerQuestion
			stud2ChapQuizCompletionActivity.addQuestionPerf(chapQuizQues3.getId(), 0f);
			//Complete Chapter Quiz for student2
			chapQuiz.addCompletionActivity(stud2ChapQuizCompletionActivity);
			TestAction stud2CompleteChapQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, stud2ChapQuizCompletionActivity);
			fourthRequest.addTestAction(stud2CompleteChapQuizTestAction);		
			
			fourthRequest.executeAllActions();
			
			//BEGIN: Student 2 - Chapter Section Quiz
			//Student2 takes three attempts to answer question1 correctly
			//First attempt to answer incorrectly
			MultiValueAttempt stud2ChapSecQuizQues1Attempt1 = getMultiValueRadioButtonAttempt(chapSecQuizQues1, student2, /*timeSpent*/ 50, /*attemptNumber*/ 1, chapSecQuizQues1IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			chapSecQuizQues1.addAttempt(stud2ChapSecQuizQues1Attempt1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapSecQuizQues1.getId() + " : 50 seconds");
			//add action to add student2 attempt1 to question1
			TestAction stud2Attempt1ChapSecQuizQuestion1TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues1, stud2ChapSecQuizQues1Attempt1, null);
			fifthRequest.addTestAction(stud2Attempt1ChapSecQuizQuestion1TestAction);
			//Second attempt to answer incorrectly
			MultiValueAttempt stud2ChapSecQuizQues1Attempt2 = getMultiValueRadioButtonAttempt(chapSecQuizQues1, student2, /*timeSpent*/ 40, /*attemptNumber*/ 2, chapSecQuizQues1IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			chapSecQuizQues1.addAttempt(stud2ChapSecQuizQues1Attempt2);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapSecQuizQues1.getId() + " : 40 seconds");
			//add action to add student2 attempt2 to question1
			TestAction stud2Attempt2ChapSecQuizQuestion1TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues1, stud2ChapSecQuizQues1Attempt2, null);
			fifthRequest.addTestAction(stud2Attempt2ChapSecQuizQuestion1TestAction);
			//Third attempt to answer correctly - Final Attempt
			MultiValueAttempt stud2ChapSecQuizQues1Attempt3 = getMultiValueRadioButtonAttempt(chapSecQuizQues1, student2, /*timeSpent*/ 25, /*attemptNumber*/ 3, chapSecQuizQues1CorrectAnswer, 1f, /*isFinalAttempt*/ true);
			chapSecQuizQues1.addAttempt(stud2ChapSecQuizQues1Attempt3);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapSecQuizQues1.getId() + " : 25 seconds");
			//Student 2 - Question Completion Activity - Chapter Section Quiz - Question 1
			QuestionCompletionActivity stud2ChapSecQuizQuestion1CompletionActivity = new QuestionCompletionActivity(student2, 1f);
			chapSecQuizQues1.addCompletionActivity(stud2ChapSecQuizQuestion1CompletionActivity);
			//add action to add student2 attempt3 to question1
			TestAction stud2Attempt3ChapSecQuizQuestion1TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues1, stud2ChapSecQuizQues1Attempt3, stud2ChapSecQuizQuestion1CompletionActivity);
			fifthRequest.addTestAction(stud2Attempt3ChapSecQuizQuestion1TestAction);
			
			//Student2 takes three attempts to answer question2 incorrectly
			//First attempt to answer incorrectly
			MultiValueAttempt stud2ChapSecQuizQues2Attempt1 = getMultiValueRadioButtonAttempt(chapSecQuizQues2, student2, /*timeSpent*/ 55, /*attemptNumber*/ 1, chapSecQuizQues2IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			chapSecQuizQues2.addAttempt(stud2ChapSecQuizQues2Attempt1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapSecQuizQues2.getId() + " : 55 seconds");
			//add action to add student2 attempt1 to question2
			TestAction stud2Attempt1ChapSecQuizQuestion2TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues2, stud2ChapSecQuizQues2Attempt1, null);
			fifthRequest.addTestAction(stud2Attempt1ChapSecQuizQuestion2TestAction);
			//Second attempt to answer incorrectly
			MultiValueAttempt stud2ChapSecQuizQues2Attempt2 = getMultiValueRadioButtonAttempt(chapSecQuizQues2, student2, /*timeSpent*/ 30, /*attemptNumber*/ 2, chapSecQuizQues2IncorrectAnswer, 0f, /*isFinalAttempt*/ false);
			chapSecQuizQues2.addAttempt(stud2ChapSecQuizQues2Attempt2);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapSecQuizQues2.getId() + " : 30 seconds");
			//add action to add student2 attempt2 to question2
			TestAction stud2Attempt2ChapSecQuizQuestion2TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues2, stud2ChapSecQuizQues2Attempt2, null);
			fifthRequest.addTestAction(stud2Attempt2ChapSecQuizQuestion2TestAction);
			//Third attempt to answer incorrectly - Final Attempt
			MultiValueAttempt stud2ChapSecQuizQues2Attempt3 = getMultiValueRadioButtonAttempt(chapSecQuizQues2, student2, /*timeSpent*/ 20, /*attemptNumber*/ 3, chapSecQuizQues2IncorrectAnswer, 0f, /*isFinalAttempt*/ true);
			chapSecQuizQues2.addAttempt(stud2ChapSecQuizQues2Attempt3);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapSecQuizQues2.getId() + " : 20 seconds");
			//Student 2 - Question Completion Activity - Chapter Section Quiz - Question 2
			QuestionCompletionActivity stud2ChapSecQuizQuestion2CompletionActivity = new QuestionCompletionActivity(student2, 0f);
			chapSecQuizQues2.addCompletionActivity(stud2ChapSecQuizQuestion2CompletionActivity);
			//add action to add student2 attempt3 to question2
			TestAction stud2Attempt3ChapSecQuizQuestion2TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapSecQuiz, chapSecQuizQues2, stud2ChapSecQuizQues2Attempt3, stud2ChapSecQuizQuestion2CompletionActivity);
			fifthRequest.addTestAction(stud2Attempt3ChapSecQuizQuestion2TestAction);	
			
			fifthRequest.executeAllActions();
			
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

