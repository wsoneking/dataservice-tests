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
		 * The course enrolled two student. 
		 * The first student take the Chapter Quiz and Chapter Section Quiz. 
		 * The student do reading on the chapter section reading page.
		 * Chapter Quiz:  1. one attempt correct; 2. two attempts correct; 3. three attempts correct;
		 * Section Quiz:  1. one attempt incorrect; 2. two attempts incorrect; 3. three attempts incorrect. 
		 */
public class TwoStudentEnrollOneStudentFinishTwoQuiz extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest request;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignmentToAdd;
		Instructor instr;
		Student student1;
		Student student2;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			request = new BasicTestDataRequest();
			testData = new BasicTestData("TwoStudentEnrollOneStudentFinishTwoQuiz");
			
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
			request.addTestAction(createCourseSection);
		
			// We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
			//add action to enroll instructor
			TestAction enrollInstr = testActionFactory.getEnrollInstructorTestAction(instr, courseSection);
			request.addTestAction(enrollInstr);
			
			//enroll student 1
			courseSection.addStudent(student1);
			//add action to enroll student
			TestAction enrollStudent1 = testActionFactory.getEnrollStudentTestAction(student1, courseSection);
			request.addTestAction(enrollStudent1);	
			
			//enroll student 2
			courseSection.addStudent(student2);
			//add action to enroll student
			TestAction enrollStudent2 = testActionFactory.getEnrollStudentTestAction(student2, courseSection);
			request.addTestAction(enrollStudent2);	
			
			
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
			request.addTestAction(addAssignment);
	

     		//Read Page
			int timeSpent = 110;
			LearningActivity learningActivity = new LearningActivity(student1, timeSpent);
			Page page = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			page.addLearningActivity(learningActivity);
			TestAction readPageTestAction = testActionFactory.getReadPageTestAction(courseSection, page, learningActivity);
			request.addTestAction(readPageTestAction);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page.getLearningResourceId() + " : " + timeSpent + " seconds");
	
			// Chapter Quiz
			// Question 1 attempt 1, correct, final 
			timeSpent = 60;
			Quiz chquiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			Question chQues1 = chquiz.getQuestions().get(0);
			Answer chapQuizQues1CorrectAnswer = chQues1.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			MultiValueAttempt attemptQ1A1 
				= getMultiValueRadioButtonAttempt(chQues1, student1, timeSpent, /*attemptNumber*/ 1, chapQuizQues1CorrectAnswer, 
						chQues1.getPointsPossible(), /*isFinalAttempt*/ true);
			chQues1.addAttempt(attemptQ1A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chQues1.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Quiz - Question 1
			QuestionCompletionActivity chapQuizQuestion1CompletionActivity = new QuestionCompletionActivity(student1, chQues1.getPointsPossible());
			chQues1.addCompletionActivity(chapQuizQuestion1CompletionActivity);
			TestAction ActionChQ1A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, chQues1, attemptQ1A1, chapQuizQuestion1CompletionActivity);
			request.addTestAction(ActionChQ1A1);

			// Question 2 attempt 1, incorrect, not final 
			timeSpent = 60;
			Question chQues2 = chquiz.getQuestions().get(1);
			Answer chapQuizQues2IncorrectAnswer = chQues2.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);
			MultiValueAttempt attemptQ2A1 
				= getMultiValueRadioButtonAttempt(chQues2, student1, timeSpent, /*attemptNumber*/ 1, 
						chapQuizQues2IncorrectAnswer, /*pointsEarned*/ 0, /*isFinalAttempt*/ false);
			chQues2.addAttempt(attemptQ2A1);
			TestAction ActionChQ2A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, chQues2, attemptQ2A1, null);
			request.addTestAction(ActionChQ2A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chQues2.getId() + " : " + timeSpent + " seconds");
			
			// Question 2 attempt 2, correct, final 
			timeSpent = 60;
			Answer chapQuizQues2CorrectAnswer = chQues2.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);	
			MultiValueAttempt attemptQ2A2 
				= getMultiValueRadioButtonAttempt(chQues2, student1, timeSpent, /*attemptNumber*/ 2, 
						chapQuizQues2CorrectAnswer, /*pointsEarned*/ chQues2.getPointsPossible()-1, /*isFinalAttempt*/ true);
			chQues2.addAttempt(attemptQ2A2);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chQues2.getId() + " : " + timeSpent + " seconds");
			//Question Completion Activity - Chapter Quiz - Question 2
			QuestionCompletionActivity chapQuizQuestion2CompletionActivity = new QuestionCompletionActivity(student1, chQues2.getPointsPossible()-1);
			chQues2.addCompletionActivity(chapQuizQuestion2CompletionActivity);
			TestAction ActionChQ2A2 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, chQues2, attemptQ2A2, chapQuizQuestion2CompletionActivity);
			request.addTestAction(ActionChQ2A2);
			

			// Question 3 attempt 1, incorrect, not final 
			timeSpent = 60;
			Question chQues3 = chquiz.getQuestions().get(2);
			Answer chapQuizQues3IncorrectAnswer = chQues3.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);		
			MultiValueAttempt attemptQ3A1 
				= getMultiValueRadioButtonAttempt(chQues3, student1, timeSpent, /*attemptNumber*/ 1, 
						chapQuizQues3IncorrectAnswer, /*pointsEarned*/ 0, /*isFinalAttempt*/ false);
			chQues3.addAttempt(attemptQ3A1);
			TestAction ActionChQ3A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, chQues3, attemptQ3A1, null);
			request.addTestAction(ActionChQ3A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chQues3.getId() + " : " + timeSpent + " seconds");

			// Question 3 attempt 2, incorrect, not final 
			timeSpent = 60;
			MultiValueAttempt attemptQ3A2 
				= getMultiValueRadioButtonAttempt(chQues3, student1, timeSpent, /*attemptNumber*/ 2, 
						chapQuizQues3IncorrectAnswer, /*pointsEarned*/ 0, /*isFinalAttempt*/ false);
			chQues3.addAttempt(attemptQ3A2);
			TestAction ActionChQ3A2 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, chQues3, attemptQ3A2, null);
			request.addTestAction(ActionChQ3A2);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chQues3.getId() + " : " + timeSpent + " seconds");

			// Question 3 attempt 3, correct, final 
			timeSpent = 60;
			Answer chapQuizQues3CorrectAnswer = chQues3.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);	
			MultiValueAttempt attemptQ3A3 
				= getMultiValueRadioButtonAttempt(chQues3, student1, timeSpent, /*attemptNumber*/ 3, 
						chapQuizQues3CorrectAnswer, /*pointsEarned*/ chQues3.getPointsPossible()-2, /*isFinalAttempt*/ true);
			chQues3.addAttempt(attemptQ3A3);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chQues3.getId() + " : " + timeSpent + " seconds");
			//Question Completion Activity - Chapter Quiz - Question 3
			QuestionCompletionActivity chapQuizQuestion3CompletionActivity = new QuestionCompletionActivity(student1, chQues3.getPointsPossible()-2);
			chQues3.addCompletionActivity(chapQuizQuestion3CompletionActivity);
			TestAction ActionChQ3A3 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, chQues3, attemptQ3A3, chapQuizQuestion3CompletionActivity);
			request.addTestAction(ActionChQ3A3);
			
			
			
			
			// Assessment performance for Chapter quiz
			QuizCompletionActivity ChQuizCompletionActivity = new QuizCompletionActivity(student1);
			ChQuizCompletionActivity.addQuestionPerf(chQues1.getId(), /*pointsEarned*/ chQues1.getPointsPossible());
			ChQuizCompletionActivity.addQuestionPerf(chQues2.getId(), /*pointsEarned*/ chQues2.getPointsPossible()-1);
			ChQuizCompletionActivity.addQuestionPerf(chQues3.getId(), /*pointsEarned*/ chQues3.getPointsPossible()-2);
			chquiz.addCompletionActivity(ChQuizCompletionActivity);
			TestAction completeChQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chquiz, ChQuizCompletionActivity);
			request.addTestAction(completeChQuizTestAction);
			
			// Chapter Section Quiz
			// Question 1 attempt 1, incorrect, final 
			timeSpent = 60;
			Quiz sequiz = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			Question seQues1 = sequiz.getQuestions().get(0);
			Answer sectQuizQues1IncorrectAnswer = seQues1.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);
			MultiValueAttempt SecAttemptQ1A1 
				= getMultiValueRadioButtonAttempt(seQues1, student1, timeSpent, /*attemptNumber*/ 1, 
						sectQuizQues1IncorrectAnswer, /*pointsEarned*/ 0, /*isFinalAttempt*/ true);
			seQues1.addAttempt(SecAttemptQ1A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + seQues1.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Section Quiz - Question 1
			QuestionCompletionActivity chapSecQuizQuestion1CompletionActivity = new QuestionCompletionActivity(student1, 0f);
			seQues1.addCompletionActivity(chapSecQuizQuestion1CompletionActivity);
			TestAction ActionSEQ1A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, seQues1, SecAttemptQ1A1, chapSecQuizQuestion1CompletionActivity);
			request.addTestAction(ActionSEQ1A1);

			// Question 2 attempt 1, incorrect, not final
			timeSpent = 60;
			Question seQues2 = sequiz.getQuestions().get(1);
			Answer sectQuizQues2IncorrectAnswer = seQues2.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);
			MultiValueAttempt SecAttemptQ2A1 
				= getMultiValueRadioButtonAttempt(seQues2, student1, timeSpent, /*attemptNumber*/ 1, 
						sectQuizQues2IncorrectAnswer, /*pointsEarned*/ 0, /*isFinalAttempt*/ false);
			seQues2.addAttempt(SecAttemptQ2A1);
			TestAction ActionSEQ2A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, seQues2, SecAttemptQ2A1, null);
			request.addTestAction(ActionSEQ2A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + seQues2.getId() + " : " + timeSpent + " seconds");
			
			// Question 2 attempt 2, incorrect, final 
			timeSpent = 60;
			MultiValueAttempt SecAttemptQ2A2 
				= getMultiValueRadioButtonAttempt(seQues2, student1, timeSpent, /*attemptNumber*/ 2, 
						sectQuizQues2IncorrectAnswer, /*pointsEarned*/ 0, /*isFinalAttempt*/ true);
			seQues2.addAttempt(SecAttemptQ2A2);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + seQues2.getId() + " : " + timeSpent + " seconds");
			//Question Completion Activity - Chapter Section Quiz - Question 2
			QuestionCompletionActivity chapSecQuizQuestion2CompletionActivity = new QuestionCompletionActivity(student1, 0f);
			seQues2.addCompletionActivity(chapSecQuizQuestion2CompletionActivity);
			TestAction ActionSEQ2A2 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, seQues2, SecAttemptQ2A2, chapSecQuizQuestion2CompletionActivity);
			request.addTestAction(ActionSEQ2A2);
			

			// Question 3 attempt 1, incorrect, not final 
			timeSpent = 60;
			Question seQues3 = sequiz.getQuestions().get(2);
			Answer sectQuizQues3IncorrectAnswer = seQues3.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);
			MultiValueAttempt SecAttemptQ3A1 
				= getMultiValueRadioButtonAttempt(seQues3, student1, timeSpent, /*attemptNumber*/ 1, 
						sectQuizQues3IncorrectAnswer, /*pointsEarned*/ 0, /*isFinalAttempt*/ false);
			seQues3.addAttempt(SecAttemptQ3A1);
			TestAction ActionSEQ3A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, seQues3, SecAttemptQ3A1, null);
			request.addTestAction(ActionSEQ3A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + seQues3.getId() + " : " + timeSpent + " seconds");

			// Question 3 attempt 2, incorrect, not final 
			timeSpent = 60;
			MultiValueAttempt SecAttemptQ3A2 
				= getMultiValueRadioButtonAttempt(seQues3, student1, timeSpent, /*attemptNumber*/ 2, 
						sectQuizQues3IncorrectAnswer, /*pointsEarned*/ 0, /*isFinalAttempt*/ false);
			seQues3.addAttempt(SecAttemptQ3A2);
			TestAction ActionSEQ3A2 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, seQues3, SecAttemptQ3A2, null);
			request.addTestAction(ActionSEQ3A2);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + seQues3.getId() + " : " + timeSpent + " seconds");

			// Question 3 attempt 3, incorrect, final 
			timeSpent = 60;
			MultiValueAttempt SecAttemptQ3A3 
				= getMultiValueRadioButtonAttempt(seQues3, student1, timeSpent, /*attemptNumber*/ 3, 
						sectQuizQues3IncorrectAnswer, /*pointsEarned*/ 0, /*isFinalAttempt*/ true);
			seQues3.addAttempt(SecAttemptQ3A3);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + seQues3.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Section Quiz - Question 3
			QuestionCompletionActivity chapSecQuizQuestion3CompletionActivity = new QuestionCompletionActivity(student1, 0f);
			seQues3.addCompletionActivity(chapSecQuizQuestion3CompletionActivity);
			TestAction ActionSEQ3A3 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, seQues3, SecAttemptQ3A3, chapSecQuizQuestion3CompletionActivity);
			request.addTestAction(ActionSEQ3A3);
			
			
			//Complete Quiz
			QuizCompletionActivity SEQuizCompletionActivity = new QuizCompletionActivity(student1);
			SEQuizCompletionActivity.addQuestionPerf(seQues1.getId(), /*pointsEarned*/ 0);
			SEQuizCompletionActivity.addQuestionPerf(seQues2.getId(), /*pointsEarned*/ 0);
			SEQuizCompletionActivity.addQuestionPerf(seQues3.getId(), /*pointsEarned*/ 0);
			sequiz.addCompletionActivity(SEQuizCompletionActivity);
			TestAction completeSEQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, sequiz, SEQuizCompletionActivity);
			request.addTestAction(completeSEQuizTestAction);
						
			request.executeAllActions();
			
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

