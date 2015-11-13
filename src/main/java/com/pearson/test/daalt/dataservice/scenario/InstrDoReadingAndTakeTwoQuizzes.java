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
		 * The course enrolled one student and an instructor. 
		 * Both the first student and the instr take the Chapter Quiz and Chapter Section Quiz and do reading. 
		 */
		
public class InstrDoReadingAndTakeTwoQuizzes extends BaseTestScenario {
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
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");

		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("InstrDoReadingAndTakeTwoQuizzes");
			
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
			TestAction createCourseSection = testActionFactory.getCreateCourseSectionTestAction(courseSection);
			firstRequest.addTestAction(createCourseSection);
		
			// We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
			//add action to enroll instructor
			TestAction enrollInstr = testActionFactory.getEnrollInstructorTestAction(instr, courseSection);
			firstRequest.addTestAction(enrollInstr);
			
			//enroll student 1
			courseSection.addStudent(student1);
			//add action to enroll student
			TestAction enrollStudent1 = testActionFactory.getEnrollStudentTestAction(student1, courseSection);
			firstRequest.addTestAction(enrollStudent1);	
			
			
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
	
			
			////---- Student 1
     		//Read Page
			LearningActivity learningActivity = new LearningActivity(student1, 100);
			Page page = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			page.addLearningActivity(learningActivity);
			TestAction readPageTestAction = testActionFactory.getReadPageTestAction(courseSection, page, learningActivity);
			secondRequest.addTestAction(readPageTestAction);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page.getLearningResourceId() + " : 100 seconds");
	
			// Chapter Quiz
			// Question 1 attempt 1, correct, final 
			Quiz chquiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			Question chQues1 = chquiz.getQuestions().get(0);
			Answer chapQuizQues1CorrectAnswer = chQues1.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			MultiValueAttempt attemptQ1A1 
				= getMultiValueRadioButtonAttempt(chQues1, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						chapQuizQues1CorrectAnswer,  /*pointsEarned*/ chQues1.getPointsPossible(), /*isFinalAttempt*/ true);
			chQues1.addAttempt(attemptQ1A1);
			
			//Question completion activity- Chapter Quiz - Question 1
			QuestionCompletionActivity chapQuizQuestion1CompletionActivity = new QuestionCompletionActivity(student1, chQues1.getPointsPossible());
			chQues1.addCompletionActivity(chapQuizQuestion1CompletionActivity);
			TestAction ActionChQ1A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, chQues1, attemptQ1A1, chapQuizQuestion1CompletionActivity);
			secondRequest.addTestAction(ActionChQ1A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chQues1.getId() + " : 60 seconds");

			// Question 2 attempt 1, correct, final 
			Question chQues2 = chquiz.getQuestions().get(1);
			Answer chapQuizQues2CorrectAnswer = chQues2.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			MultiValueAttempt attemptQ2A1 
				= getMultiValueRadioButtonAttempt(chQues2, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						chapQuizQues2CorrectAnswer,  /*pointsEarned*/ chQues2.getPointsPossible(), /*isFinalAttempt*/ true);
			chQues2.addAttempt(attemptQ2A1);
			
			//Question completion activity- Chapter Quiz - Question 2
			QuestionCompletionActivity chapQuizQuestion2CompletionActivity = new QuestionCompletionActivity(student1, chQues2.getPointsPossible());
			chQues2.addCompletionActivity(chapQuizQuestion2CompletionActivity);
			TestAction ActionChQ2A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, chQues2, attemptQ2A1, chapQuizQuestion2CompletionActivity);
			secondRequest.addTestAction(ActionChQ2A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chQues2.getId() + " : 60 seconds");
			
			// Question 3 attempt 1, correct, final 
			Question chQues3 = chquiz.getQuestions().get(2);
			Answer chapQuizQues3CorrectAnswer = chQues3.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			MultiValueAttempt attemptQ3A1 
				= getMultiValueRadioButtonAttempt(chQues3, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						chapQuizQues3CorrectAnswer,  /*pointsEarned*/ chQues3.getPointsPossible(), /*isFinalAttempt*/ true);
			chQues3.addAttempt(attemptQ3A1);
			
			//Question completion activity- Chapter Quiz - Question 3
			QuestionCompletionActivity chapQuizQuestion3CompletionActivity = new QuestionCompletionActivity(student1, chQues3.getPointsPossible());
			chQues3.addCompletionActivity(chapQuizQuestion3CompletionActivity);
			TestAction ActionChQ3A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, chQues3, attemptQ3A1, chapQuizQuestion3CompletionActivity);
			secondRequest.addTestAction(ActionChQ3A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chQues3.getId() + " : 60 seconds");
			
			// Assessment performance for Chapter quiz
			QuizCompletionActivity ChQuizCompletionActivity = new QuizCompletionActivity(student1);
			ChQuizCompletionActivity.addQuestionPerf(chQues1.getId(), chQues1.getPointsPossible());
			ChQuizCompletionActivity.addQuestionPerf(chQues2.getId(), chQues2.getPointsPossible());
			ChQuizCompletionActivity.addQuestionPerf(chQues3.getId(), chQues3.getPointsPossible());
			chquiz.addCompletionActivity(ChQuizCompletionActivity);
			TestAction completeChQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chquiz, ChQuizCompletionActivity);
			secondRequest.addTestAction(completeChQuizTestAction);
			
			secondRequest.executeAllActions();
			
			
			// Chapter Section Quiz
			// Question 1 attempt 1, correct, final 
			Quiz sequiz = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			Question seQues1 = sequiz.getQuestions().get(0);
			Answer sectQuizQues1CorrectAnswer = seQues1.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			MultiValueAttempt SEattemptQ1A1 
				= getMultiValueRadioButtonAttempt(seQues1, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						sectQuizQues1CorrectAnswer,  /*pointsEarned*/ seQues1.getPointsPossible(), /*isFinalAttempt*/ true);
			seQues1.addAttempt(SEattemptQ1A1);
			
			//Question completion activity - Chapter Section Quiz - Question 1
			QuestionCompletionActivity chapSecQuizQuestion1CompletionActivity = new QuestionCompletionActivity(student1, seQues1.getPointsPossible());
			seQues1.addCompletionActivity(chapSecQuizQuestion1CompletionActivity);
			TestAction ActionSEQ1A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, seQues1, SEattemptQ1A1, chapSecQuizQuestion1CompletionActivity);
			thirdRequest.addTestAction(ActionSEQ1A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + seQues1.getId() + " : 60 seconds");

			// Question 2 attempt 1, correct, final 
			Question seQues2 = sequiz.getQuestions().get(1);
			Answer sectQuizQues2CorrectAnswer = seQues2.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			MultiValueAttempt SEattemptQ2A1 
				= getMultiValueRadioButtonAttempt(seQues2, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						sectQuizQues2CorrectAnswer,  /*pointsEarned*/ seQues2.getPointsPossible(), /*isFinalAttempt*/ true);
			seQues2.addAttempt(SEattemptQ2A1);
			
			//Question completion activity - Chapter Section Quiz - Question 2
			QuestionCompletionActivity chapSecQuizQuestion2CompletionActivity = new QuestionCompletionActivity(student1, seQues2.getPointsPossible());
			seQues2.addCompletionActivity(chapSecQuizQuestion2CompletionActivity);
			TestAction ActionSEQ2A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, seQues2, SEattemptQ2A1, chapSecQuizQuestion2CompletionActivity);
			thirdRequest.addTestAction(ActionSEQ2A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + seQues2.getId() + " : 60 seconds");
			
			// Question 3 attempt 1, correct, final 
			Question seQues3 = sequiz.getQuestions().get(2);
			Answer sectQuizQues3CorrectAnswer = seQues3.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);		
			MultiValueAttempt SEattemptQ3A1 
				= getMultiValueRadioButtonAttempt(seQues3, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						sectQuizQues3CorrectAnswer,  /*pointsEarned*/ seQues3.getPointsPossible(), /*isFinalAttempt*/ true);
			seQues3.addAttempt(SEattemptQ3A1);
			
			//Question completion activity - Chapter Section Quiz - Question 3
			QuestionCompletionActivity chapSecQuizQuestion3CompletionActivity = new QuestionCompletionActivity(student1, seQues3.getPointsPossible());
			seQues3.addCompletionActivity(chapSecQuizQuestion3CompletionActivity);
			TestAction ActionSEQ3A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, seQues3, SEattemptQ3A1, chapSecQuizQuestion3CompletionActivity);
			thirdRequest.addTestAction(ActionSEQ3A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + seQues3.getId() + " : 60 seconds");
			
			// Assessment performance for section quiz
			QuizCompletionActivity SEQuizCompletionActivity = new QuizCompletionActivity(student1);
			SEQuizCompletionActivity.addQuestionPerf(seQues1.getId(), seQues1.getPointsPossible());
			SEQuizCompletionActivity.addQuestionPerf(seQues2.getId(), seQues2.getPointsPossible());
			SEQuizCompletionActivity.addQuestionPerf(seQues3.getId(), seQues3.getPointsPossible());
			sequiz.addCompletionActivity(SEQuizCompletionActivity);
			TestAction completeSEQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, sequiz, SEQuizCompletionActivity);
			thirdRequest.addTestAction(completeSEQuizTestAction);
			
			thirdRequest.executeAllActions();
						
			
			////---- Instr 1
			//Read Page
			LearningActivity learningActivityInstr = new LearningActivity(instr, 100);
			Page pageInstr = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			pageInstr.addLearningActivity(learningActivityInstr);
			TestAction readPageTestActionInstr = testActionFactory.getReadPageTestAction(courseSection, pageInstr, learningActivityInstr);
			fourthRequest.addTestAction(readPageTestActionInstr);
			timeOnTaskOutput.append("\n...learning time : " + instr.getPersonId() + " (instructor) : " + pageInstr.getLearningResourceId() + " : 100 seconds");
	
			// Chapter Quiz
			// Question 1 attempt 1, correct, final 
			Quiz chquizInstr = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			Question chQues1Instr = chquizInstr.getQuestions().get(0);
			Answer chapQuizQues1CorrectAnswerInstr = chQues1Instr.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			MultiValueAttempt attemptQ1A1Instr 
				= getMultiValueRadioButtonAttempt(chQues1Instr, instr, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						chapQuizQues1CorrectAnswerInstr,  /*pointsEarned*/ chQues1Instr.getPointsPossible(), /*isFinalAttempt*/ true);
			chQues1Instr.addAttempt(attemptQ1A1Instr);
			
			//Question completion activity- Chapter Quiz - Question 1
			QuestionCompletionActivity chapQuizQuestion1CompletionActivityInstr = new QuestionCompletionActivity(instr, chQues1Instr.getPointsPossible());
			chQues1Instr.addCompletionActivity(chapQuizQuestion1CompletionActivityInstr);
			TestAction ActionChQ1A1Instr = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquizInstr, chQues1Instr, attemptQ1A1Instr, chapQuizQuestion1CompletionActivityInstr);
			fourthRequest.addTestAction(ActionChQ1A1Instr);
			timeOnTaskOutput.append("\n...assessment time : " + instr.getPersonId() + " (instructor) : " + chQues1Instr.getId() + " : 60 seconds");

			// Question 2 attempt 1, correct, final 
			Question chQues2Instr = chquizInstr.getQuestions().get(1);
			Answer chapQuizQues2CorrectAnswerInstr = chQues2Instr.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);	
			MultiValueAttempt attemptQ2A1Instr 
				= getMultiValueRadioButtonAttempt(chQues2Instr, instr, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						chapQuizQues2CorrectAnswerInstr,  /*pointsEarned*/ chQues2Instr.getPointsPossible(), /*isFinalAttempt*/ true);
			chQues2Instr.addAttempt(attemptQ2A1Instr);
			
			//Question completion activity- Chapter Quiz - Question 2
			QuestionCompletionActivity chapQuizQuestion2CompletionActivityInstr = new QuestionCompletionActivity(instr, chQues2Instr.getPointsPossible());
			chQues2Instr.addCompletionActivity(chapQuizQuestion2CompletionActivityInstr);
			TestAction ActionChQ2A1Instr = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquizInstr, chQues2Instr, attemptQ2A1Instr, chapQuizQuestion2CompletionActivityInstr);
			fourthRequest.addTestAction(ActionChQ2A1Instr);
			timeOnTaskOutput.append("\n...assessment time : " + instr.getPersonId() + " (instructor) : " + chQues2Instr.getId() + " : 60 seconds");
			
			// Question 3 attempt 1, correct, final 
			Question chQues3Instr = chquizInstr.getQuestions().get(2);
			Answer chapQuizQues3CorrectAnswerInstr = chQues3Instr.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);			
			MultiValueAttempt attemptQ3A1Instr 
				= getMultiValueRadioButtonAttempt(chQues3Instr, instr, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						chapQuizQues3CorrectAnswerInstr,  /*pointsEarned*/ chQues3Instr.getPointsPossible(), /*isFinalAttempt*/ true);
			chQues3Instr.addAttempt(attemptQ3A1Instr);
			
			//Question completion activity- Chapter Quiz - Question 3
			QuestionCompletionActivity chapQuizQuestion3CompletionActivityInstr = new QuestionCompletionActivity(instr, chQues3Instr.getPointsPossible());
			chQues3Instr.addCompletionActivity(chapQuizQuestion3CompletionActivityInstr);
			TestAction ActionChQ3A1Instr = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquizInstr, chQues3Instr, attemptQ3A1Instr, chapQuizQuestion3CompletionActivityInstr);
			fourthRequest.addTestAction(ActionChQ3A1Instr);
			timeOnTaskOutput.append("\n...assessment time : " + instr.getPersonId() + " (instructor) : " + chQues3Instr.getId() + " : 60 seconds");
			
			// Assessment performance for Chapter quiz
			QuizCompletionActivity ChQuizCompletionActivityInstr = new QuizCompletionActivity(instr);
			ChQuizCompletionActivityInstr.addQuestionPerf(chQues1Instr.getId(), chQues1Instr.getPointsPossible());
			ChQuizCompletionActivityInstr.addQuestionPerf(chQues2Instr.getId(), chQues2Instr.getPointsPossible());
			ChQuizCompletionActivityInstr.addQuestionPerf(chQues3Instr.getId(), chQues3Instr.getPointsPossible());
			chquizInstr.addCompletionActivity(ChQuizCompletionActivityInstr);
			TestAction completeChQuizTestActionInstr = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chquizInstr, ChQuizCompletionActivityInstr);
			fourthRequest.addTestAction(completeChQuizTestActionInstr);
			
			fourthRequest.executeAllActions();
			
			
			// Chapter Section Quiz
			// Question 1 attempt 1, correct, final 
			Quiz sequizInstr = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			Question seQues1Instr = sequizInstr.getQuestions().get(0);
			Answer sectQuizQues1CorrectAnswerInstr = seQues1Instr.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			MultiValueAttempt SEattemptQ1A1Instr 
				= getMultiValueRadioButtonAttempt(seQues1Instr, instr, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						sectQuizQues1CorrectAnswerInstr,  /*pointsEarned*/ seQues1Instr.getPointsPossible(), /*isFinalAttempt*/ true);
			seQues1Instr.addAttempt(SEattemptQ1A1Instr);
			
			//Question completion activity- Chapter Section Quiz - Question 1
			QuestionCompletionActivity chapSecQuizQuestion1CompletionActivityInstr = new QuestionCompletionActivity(instr, seQues1Instr.getPointsPossible());
			seQues1Instr.addCompletionActivity(chapSecQuizQuestion1CompletionActivityInstr);
			TestAction ActionSEQ1A1Instr = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequizInstr, seQues1Instr, SEattemptQ1A1Instr, chapSecQuizQuestion1CompletionActivityInstr);
			fourthRequest.addTestAction(ActionSEQ1A1Instr);
			timeOnTaskOutput.append("\n...assessment time : " + instr.getPersonId() + " (instructor) : " + seQues1Instr.getId() + " : 60 seconds");

			// Question 2 attempt 1, correct, final 
			Question seQues2Instr = sequizInstr.getQuestions().get(1);
			Answer sectQuizQues2CorrectAnswerInstr = seQues2Instr.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);	
			MultiValueAttempt SEattemptQ2A1Instr 
				= getMultiValueRadioButtonAttempt(seQues2Instr, instr, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						sectQuizQues2CorrectAnswerInstr,  /*pointsEarned*/ seQues2Instr.getPointsPossible(), /*isFinalAttempt*/ true);
			seQues2Instr.addAttempt(SEattemptQ2A1Instr);
			
			//Question completion activity- Chapter Section Quiz - Question 2
			QuestionCompletionActivity chapSecQuizQuestion2CompletionActivityInstr = new QuestionCompletionActivity(instr, seQues2Instr.getPointsPossible());
			seQues2Instr.addCompletionActivity(chapSecQuizQuestion2CompletionActivityInstr);
			TestAction ActionSEQ2A1Instr = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequizInstr, seQues2Instr, SEattemptQ2A1Instr, chapSecQuizQuestion2CompletionActivityInstr);
			fourthRequest.addTestAction(ActionSEQ2A1Instr);
			timeOnTaskOutput.append("\n...assessment time : " + instr.getPersonId() + " (instructor) : " + seQues2Instr.getId() + " : 60 seconds");
			
			// Question 3 attempt 1, correct, final 
			Question seQues3Instr = sequizInstr.getQuestions().get(2);
			Answer sectQuizQues3CorrectAnswerInstr = seQues3Instr.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);		
			MultiValueAttempt SEattemptQ3A1Instr 
				= getMultiValueRadioButtonAttempt(seQues3Instr, instr, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						sectQuizQues3CorrectAnswerInstr, /*pointsEarned*/ seQues3Instr.getPointsPossible(), /*isFinalAttempt*/ true);
			seQues3Instr.addAttempt(SEattemptQ3A1Instr);
			
			//Question completion activity- Chapter Section Quiz - Question 3
			QuestionCompletionActivity chapSecQuizQuestion3CompletionActivityInstr = new QuestionCompletionActivity(instr, seQues3Instr.getPointsPossible());
			seQues3Instr.addCompletionActivity(chapSecQuizQuestion3CompletionActivityInstr);
			TestAction ActionSEQ3A1Instr = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequizInstr, seQues3Instr, SEattemptQ3A1Instr, chapSecQuizQuestion3CompletionActivityInstr);
			fourthRequest.addTestAction(ActionSEQ3A1Instr);
			timeOnTaskOutput.append("\n...assessment time : " + instr.getPersonId() + " (instructor) : " + seQues3Instr.getId() + " : 60 seconds");
			
			// Assessment performance for section quiz
			QuizCompletionActivity SEQuizCompletionActivityInstr = new QuizCompletionActivity(instr);
			SEQuizCompletionActivityInstr.addQuestionPerf(seQues1Instr.getId(), seQues1Instr.getPointsPossible());
			SEQuizCompletionActivityInstr.addQuestionPerf(seQues2Instr.getId(), seQues2Instr.getPointsPossible());
			SEQuizCompletionActivityInstr.addQuestionPerf(seQues3Instr.getId(), seQues3Instr.getPointsPossible());
			sequizInstr.addCompletionActivity(SEQuizCompletionActivityInstr);
			TestAction completeSEQuizTestActionInstr = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, sequizInstr, SEQuizCompletionActivityInstr);
			fourthRequest.addTestAction(completeSEQuizTestActionInstr);
				
			
			//Instr  - remove read page
			pageInstr.removeLearningActivity(learningActivityInstr);
			
			//Instr - remove Chapter Quiz attempts
			for(Question ques : chquizInstr.getQuestions()){
				if(ques.studentAttemptedQuestion(instr)){
					List<Attempt> attempts = ques.getAttemptsForUser(instr);
					for(Attempt attempt : attempts){
						ques.removeAttempt(attempt);
					}
				}
				//remove question completion activity
				ques.removeStudentCompletionActivity(instr);
			}
			//Instr  - remove Chapter Section Quiz attempts
			for(Question ques : sequizInstr.getQuestions()){
				if(ques.studentAttemptedQuestion(instr)){
					List<Attempt> attempts = ques.getAttemptsForUser(instr);
					for(Attempt attempt : attempts){
						ques.removeAttempt(attempt);
					}
				}
				//remove question completion activity
				ques.removeStudentCompletionActivity(instr);
			}
			// Instr  - remove completionActivity
			sequizInstr.removeCompletionActivity(SEQuizCompletionActivityInstr);
			chquizInstr.removeCompletionActivity(ChQuizCompletionActivityInstr);
			
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

