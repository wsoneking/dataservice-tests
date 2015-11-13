package com.pearson.test.daalt.dataservice.scenario;

import java.util.ArrayList;
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
import com.pearson.test.daalt.dataservice.model.SubAttempt;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;

public class FillInTheBlankAndNumericOnly extends BaseTestScenario {
	private static final String STUD01_QUESTION01_ATTEMPT01_ANSWER01_TEXT = "is";
	private static final String STUD01_QUESTION01_ATTEMPT01_ANSWER02_TEXT = "are";
	private static final String STUD01_QUESTION02_ATTEMPT01_ANSWER01_TEXT = "went";
	private static final String STUD01_QUESTION03_ATTEMPT01_ANSWER01_TEXT = "9.8";
	private static final String STUD01_QUESTION03_ATTEMPT01_ANSWER02_TEXT = "1.0";
	private static final String STUD01_QUESTION03_ATTEMPT01_ANSWER03_TEXT =  "-1";
	
	private static final String STUD02_QUESTION01_ATTEMPT01_ANSWER01_TEXT = "am";
	private static final String STUD02_QUESTION01_ATTEMPT01_ANSWER02_TEXT = "were";
	private static final String STUD02_QUESTION02_ATTEMPT01_ANSWER01_TEXT = "dunno";
	private static final String STUD02_QUESTION03_ATTEMPT01_ANSWER01_TEXT = "9.799";
	private static final String STUD02_QUESTION03_ATTEMPT01_ANSWER02_TEXT = "1";
	private static final String STUD02_QUESTION03_ATTEMPT01_ANSWER03_TEXT =  "0";
	
	private static final String STUD02_QUESTION01_ATTEMPT02_ANSWER01_TEXT = "am";
	private static final String STUD02_QUESTION01_ATTEMPT02_ANSWER02_TEXT = "are";
	private static final String STUD02_QUESTION02_ATTEMPT02_ANSWER01_TEXT = "went";
	private static final String STUD02_QUESTION03_ATTEMPT02_ANSWER01_TEXT = "9.799";
	private static final String STUD02_QUESTION03_ATTEMPT02_ANSWER02_TEXT = "1";
	private static final String STUD02_QUESTION03_ATTEMPT02_ANSWER03_TEXT =  "0";

	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
		TestDataRequest fourthRequest;
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
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("FillInTheBlankAndNumericOnly");
			
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
			assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_QUIZ_WITH_FILL_IN_THE_BLANK_AND_NUMERIC_ONLY);

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
			
			Quiz chapterQuiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			int timeSpent = 60;
			
			//BEGIN student1 activity
			
			//student1 answers FillInTheBlank_Question_01 correctly on the first attempt
			timeSpent = 60;
			Question chapterQuizQuestion01 = chapterQuiz.getQuestions().get(0);
			List<String> stud01Question01Attempt01AnswerList = new ArrayList<>();
			stud01Question01Attempt01AnswerList.add(STUD01_QUESTION01_ATTEMPT01_ANSWER01_TEXT);
			stud01Question01Attempt01AnswerList.add(STUD01_QUESTION01_ATTEMPT01_ANSWER02_TEXT);
			MultiValueAttempt stud01Question01Attempt01 = getCorrectFillInTheBlankOrNumericAttempt(chapterQuizQuestion01, student1, timeSpent, /*attemptNumber*/ 1, 
					/*pointsEarned*/ chapterQuizQuestion01.getPointsPossible(), /*isFinalAttempt*/ true, stud01Question01Attempt01AnswerList);
			chapterQuizQuestion01.addAttempt(stud01Question01Attempt01);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapterQuizQuestion01.getId() + " : " + timeSpent + " seconds");
			QuestionCompletionActivity question01Stud01CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ chapterQuizQuestion01.getPointsPossible());
			chapterQuizQuestion01.addCompletionActivity(question01Stud01CompletionActivity);
			TestAction stud01Question01Attempt01Action = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapterQuiz, chapterQuizQuestion01, stud01Question01Attempt01, question01Stud01CompletionActivity);
			secondRequest.addTestAction(stud01Question01Attempt01Action);
			
			//student1 answers FillInTheBlank_Question_02 correctly on the first attempt
			timeSpent = 20;
			Question chapterQuizQuestion02 = chapterQuiz.getQuestions().get(1);
			List<String> stud01Question02Attempt01AnswerList = new ArrayList<>();
			stud01Question02Attempt01AnswerList.add(STUD01_QUESTION02_ATTEMPT01_ANSWER01_TEXT);
			MultiValueAttempt stud01Question02Attempt01 = getCorrectFillInTheBlankOrNumericAttempt(chapterQuizQuestion02, student1, timeSpent, /*attemptNumber*/ 1, 
					/*pointsEarned*/ chapterQuizQuestion02.getPointsPossible(), /*isFinalAttempt*/ true, stud01Question02Attempt01AnswerList);
			chapterQuizQuestion02.addAttempt(stud01Question02Attempt01);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapterQuizQuestion02.getId() + " : " + timeSpent + " seconds");
			QuestionCompletionActivity question02Stud01CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ chapterQuizQuestion02.getPointsPossible());
			chapterQuizQuestion02.addCompletionActivity(question02Stud01CompletionActivity);
			TestAction stud01Question02Attempt01Action = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapterQuiz, chapterQuizQuestion02, stud01Question02Attempt01, question02Stud01CompletionActivity);
			secondRequest.addTestAction(stud01Question02Attempt01Action);
			
			//student1 answers Numeric_Question_01 correctly on the first attempt
			timeSpent = 33;
			Question chapterQuizQuestion03 = chapterQuiz.getQuestions().get(2);
			List<String> stud01Question03Attempt01AnswerList = new ArrayList<>();
			stud01Question03Attempt01AnswerList.add(STUD01_QUESTION03_ATTEMPT01_ANSWER01_TEXT);
			stud01Question03Attempt01AnswerList.add(STUD01_QUESTION03_ATTEMPT01_ANSWER02_TEXT);
			stud01Question03Attempt01AnswerList.add(STUD01_QUESTION03_ATTEMPT01_ANSWER03_TEXT);
			MultiValueAttempt stud01Question03Attempt01 = getCorrectFillInTheBlankOrNumericAttempt(chapterQuizQuestion03, student1, timeSpent, /*attemptNumber*/ 1, 
					/*pointsEarned*/ chapterQuizQuestion03.getPointsPossible(), /*isFinalAttempt*/ true, stud01Question03Attempt01AnswerList);
			chapterQuizQuestion03.addAttempt(stud01Question03Attempt01);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chapterQuizQuestion03.getId() + " : " + timeSpent + " seconds");
			QuestionCompletionActivity question03Stud01CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ chapterQuizQuestion03.getPointsPossible());
			chapterQuizQuestion03.addCompletionActivity(question03Stud01CompletionActivity);
			TestAction stud01Question03Attempt01Action = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapterQuiz, chapterQuizQuestion03, stud01Question03Attempt01, question03Stud01CompletionActivity);
			secondRequest.addTestAction(stud01Question03Attempt01Action);
			
			//student1 completes the chapter quiz
			QuizCompletionActivity stud1ChapQuizCompletionActivity = new QuizCompletionActivity(student1);
			stud1ChapQuizCompletionActivity.addQuestionPerf(chapterQuizQuestion01.getId(), chapterQuizQuestion01.getPointsPossible());
			stud1ChapQuizCompletionActivity.addQuestionPerf(chapterQuizQuestion02.getId(), chapterQuizQuestion02.getPointsPossible());
			stud1ChapQuizCompletionActivity.addQuestionPerf(chapterQuizQuestion03.getId(), chapterQuizQuestion03.getPointsPossible());
			chapterQuiz.addCompletionActivity(stud1ChapQuizCompletionActivity);
			TestAction stud1CompleteChapQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapterQuiz, stud1ChapQuizCompletionActivity);
			secondRequest.addTestAction(stud1CompleteChapQuizTestAction);
			
			secondRequest.executeAllActions();
			//END student1 activity
			
			//BEGIN student2 activity
			//student2 first attempt for FillInTheBlank_Question_01 (incorrect)
			timeSpent = 20;
			List<String> stud02Question01Attempt01AnswerList = new ArrayList<>();
			stud02Question01Attempt01AnswerList.add(STUD02_QUESTION01_ATTEMPT01_ANSWER01_TEXT);
			stud02Question01Attempt01AnswerList.add(STUD02_QUESTION01_ATTEMPT01_ANSWER02_TEXT);
			MultiValueAttempt stud02Question01Attempt01 = getCorrectFillInTheBlankOrNumericAttempt(chapterQuizQuestion01, student2, timeSpent, /*attemptNumber*/ 1, 
					/*pointsEarned*/ 0, /*isFinalAttempt*/ false, stud02Question01Attempt01AnswerList);
			SubAttempt stud02Question01Attempt01SubAttempt02 = stud02Question01Attempt01.getSubAttempts().get(1);
			Answer stud02Question01Attempt01SubAttempt02Answer = stud02Question01Attempt01SubAttempt02.getAnswers().get(0);
			stud02Question01Attempt01SubAttempt02Answer.setCorrectAnswer(false);
			stud02Question01Attempt01SubAttempt02Answer.setId("randomString");
			chapterQuizQuestion01.addAttempt(stud02Question01Attempt01);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapterQuizQuestion01.getId() + " : " + timeSpent + " seconds");
			TestAction stud02Question01Attempt01Action = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapterQuiz, chapterQuizQuestion01, stud02Question01Attempt01, null);
			thirdRequest.addTestAction(stud02Question01Attempt01Action);
			
			//student2 second attempt for FillInTheBlank_Question_01 (correct & final)
			timeSpent = 29;
			List<String> stud02Question01Attempt02AnswerList = new ArrayList<>();
			stud02Question01Attempt02AnswerList.add(STUD02_QUESTION01_ATTEMPT02_ANSWER01_TEXT);
			stud02Question01Attempt02AnswerList.add(STUD02_QUESTION01_ATTEMPT02_ANSWER02_TEXT);
			MultiValueAttempt stud02Question01Attempt02 = getCorrectFillInTheBlankOrNumericAttempt(chapterQuizQuestion01, student2, timeSpent, /*attemptNumber*/ 2, 
					/*pointsEarned*/ 3, /*isFinalAttempt*/ true, stud02Question01Attempt02AnswerList);
			chapterQuizQuestion01.addAttempt(stud02Question01Attempt02);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapterQuizQuestion01.getId() + " : " + timeSpent + " seconds");
			QuestionCompletionActivity question01Stud02CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ 3);
			chapterQuizQuestion01.addCompletionActivity(question01Stud02CompletionActivity);
			TestAction stud02Question01Attempt02Action = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapterQuiz, chapterQuizQuestion01, stud02Question01Attempt02, question01Stud02CompletionActivity);
			thirdRequest.addTestAction(stud02Question01Attempt02Action);
			
			//TODO: student2 first attempt for FillInTheBlank_Question_02 (incorrect & not final)
			timeSpent = 11;
			List<String> stud02Question02Attempt01AnswerList = new ArrayList<>();
			stud02Question02Attempt01AnswerList.add(STUD02_QUESTION02_ATTEMPT01_ANSWER01_TEXT);
		
			MultiValueAttempt stud02Question02Attempt01 = getCorrectFillInTheBlankOrNumericAttempt(chapterQuizQuestion02, student2, timeSpent, /*attemptNumber*/ 1, 
					/*pointsEarned*/ 0, /*isFinalAttempt*/ false, stud02Question02Attempt01AnswerList);
			SubAttempt stud02Question02Attempt01SubAttempt01 = stud02Question02Attempt01.getSubAttempts().get(0);
			Answer stud02Question02Attempt01SubAttempt01Answer = stud02Question02Attempt01SubAttempt01.getAnswers().get(0);
			stud02Question02Attempt01SubAttempt01Answer.setCorrectAnswer(false);
			stud02Question02Attempt01SubAttempt01Answer.setId("randomString");
			chapterQuizQuestion02.addAttempt(stud02Question02Attempt01);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapterQuizQuestion02.getId() + " : " + timeSpent + " seconds");
			TestAction stud02Question02Attempt01Action = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapterQuiz, chapterQuizQuestion02, stud02Question02Attempt01, null);
			thirdRequest.addTestAction(stud02Question02Attempt01Action);
			
			//student2 second attempt for FillInTheBlank_Question_02 (incorrect & final)
			timeSpent = 10;
			List<String> stud02Question02Attempt02AnswerList = new ArrayList<>();
			stud02Question02Attempt02AnswerList.add(STUD02_QUESTION02_ATTEMPT02_ANSWER01_TEXT);
		
			MultiValueAttempt stud02Question02Attempt02 = getCorrectFillInTheBlankOrNumericAttempt(chapterQuizQuestion02, student2, timeSpent, /*attemptNumber*/ 2, 
					/*pointsEarned*/ 0, /*isFinalAttempt*/ true, stud02Question02Attempt02AnswerList);
			SubAttempt stud02Question02Attempt02SubAttempt01 = stud02Question02Attempt02.getSubAttempts().get(0);
			Answer stud02Question02Attempt02SubAttempt02Answer = stud02Question02Attempt02SubAttempt01.getAnswers().get(0);
			stud02Question02Attempt02SubAttempt02Answer.setCorrectAnswer(false);
			stud02Question02Attempt02SubAttempt02Answer.setId("wasn't");
			chapterQuizQuestion02.addAttempt(stud02Question02Attempt02);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapterQuizQuestion02.getId() + " : " + timeSpent + " seconds");
			QuestionCompletionActivity question02Stud02CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ 0);
			chapterQuizQuestion02.addCompletionActivity(question02Stud02CompletionActivity);
			TestAction stud02Question02Attempt02Action = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapterQuiz, chapterQuizQuestion02, stud02Question02Attempt02, question02Stud02CompletionActivity);
			thirdRequest.addTestAction(stud02Question02Attempt02Action);
			
			//TODO: student2 first attempt for Numeric_Question_01 (incorrect)
			timeSpent = 36;
			List<String> stud02Question03Attempt01AnswerList = new ArrayList<>();
			stud02Question03Attempt01AnswerList.add(STUD02_QUESTION03_ATTEMPT01_ANSWER01_TEXT);
			stud02Question03Attempt01AnswerList.add(STUD02_QUESTION03_ATTEMPT01_ANSWER02_TEXT);
			stud02Question03Attempt01AnswerList.add(STUD02_QUESTION03_ATTEMPT01_ANSWER03_TEXT);
			MultiValueAttempt stud02Question03Attempt01 = getCorrectFillInTheBlankOrNumericAttempt(chapterQuizQuestion03, student2, timeSpent, /*attemptNumber*/ 1, 
					/*pointsEarned*/ 4, /*isFinalAttempt*/ false, stud02Question03Attempt01AnswerList);
			SubAttempt stud02Question03Attempt01SubAttempt03 = stud02Question03Attempt01.getSubAttempts().get(2);
			Answer stud02Question03Attempt01SubAttempt03Answer = stud02Question03Attempt01SubAttempt03.getAnswers().get(0);
			stud02Question03Attempt01SubAttempt03Answer.setCorrectAnswer(false);
			stud02Question03Attempt01SubAttempt03Answer.setId("2.3456");
			chapterQuizQuestion03.addAttempt(stud02Question03Attempt01);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapterQuizQuestion03.getId() + " : " + timeSpent + " seconds");
			TestAction stud02Question03Attempt01Action = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapterQuiz, chapterQuizQuestion03, stud02Question03Attempt01, null);
			thirdRequest.addTestAction(stud02Question03Attempt01Action);

			//TODO: student2 second attempt for Numeric_Question_01 (correct & final)
			
			timeSpent = 33;
			List<String> stud02Question03Attempt02AnswerList = new ArrayList<>();
			stud02Question03Attempt02AnswerList.add(STUD02_QUESTION03_ATTEMPT02_ANSWER01_TEXT);
			stud02Question03Attempt02AnswerList.add(STUD02_QUESTION03_ATTEMPT02_ANSWER02_TEXT);
			stud02Question03Attempt02AnswerList.add(STUD02_QUESTION03_ATTEMPT02_ANSWER03_TEXT);
			MultiValueAttempt stud02Question03Attempt02 = getCorrectFillInTheBlankOrNumericAttempt(chapterQuizQuestion03, student2, timeSpent, /*attemptNumber*/ 2, 
					/*pointsEarned*/ chapterQuizQuestion03.getPointsPossible(), /*isFinalAttempt*/ true, stud02Question03Attempt02AnswerList);
			chapterQuizQuestion03.addAttempt(stud02Question03Attempt02);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + chapterQuizQuestion03.getId() + " : " + timeSpent + " seconds");
			QuestionCompletionActivity question03Stud02CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ 5);
			chapterQuizQuestion03.addCompletionActivity(question03Stud02CompletionActivity);
			TestAction stud02Question03Attempt02Action = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapterQuiz, chapterQuizQuestion03, stud02Question03Attempt02, question03Stud02CompletionActivity);
			thirdRequest.addTestAction(stud02Question03Attempt02Action);
			//Student 02 Completes the chapterQuiz
			QuizCompletionActivity stud2ChapQuizCompletionActivity = new QuizCompletionActivity(student2);
			stud2ChapQuizCompletionActivity.addQuestionPerf(chapterQuizQuestion01.getId(),3);
			stud2ChapQuizCompletionActivity.addQuestionPerf(chapterQuizQuestion02.getId(),0);
			stud2ChapQuizCompletionActivity.addQuestionPerf(chapterQuizQuestion03.getId(),5);
			chapterQuiz.addCompletionActivity(stud2ChapQuizCompletionActivity);
			TestAction stud2CompleteChapQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapterQuiz, stud2ChapQuizCompletionActivity);
			thirdRequest.addTestAction(stud2CompleteChapQuizTestAction);
			
			thirdRequest.executeAllActions();
			//END student2 activity
			
			//TODO: simulate due date passing (so studentTrending is not null)
			assignmentToAdd.setDueDatePassed(true);
			TestAction passDueDateTestAction = testActionFactory.getSimulateDueDatePassingTestAction(instr, courseSection, assignmentToAdd);
			fourthRequest.addTestAction(passDueDateTestAction);
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
