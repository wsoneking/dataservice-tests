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
import com.pearson.test.daalt.dataservice.model.BasicTA;
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
import com.pearson.test.daalt.dataservice.model.TA;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;
		/**
		 * The course enrolled one student and a TA. 
		 * Both the first student and the TA take the Chapter Quiz and Chapter Section Quiz and do reading. 
		 */
		
public class TADoReadingAndTakeTwoQuizzes extends BaseTestScenario {
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
		TA ta1;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");

		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("TADoReadingAndTakeTwoQuizzes");
			
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
			
			// add TA 1
			ta1 = new BasicTA(getUserConfig().getValue(TestEngine.ta01UsernamePropName),
					getUserConfig().getValue(TestEngine.ta01PasswordPropName),
					getUserConfig().getValue(TestEngine.ta01IdPropName),
					getUserConfig().getValue(TestEngine.ta01FirstName),
					getUserConfig().getValue(TestEngine.ta01LastName));
			testData.addTA(ta1);
			
			
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
			
			//enroll ta 1
			courseSection.addTA(ta1);
			//add action to enroll student
			TestAction enrollta = testActionFactory.getEnrollTATestAction(ta1, courseSection);
			firstRequest.addTestAction(enrollta);	
			
			
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
			int timeSpent = 100;
			LearningActivity learningActivity = new LearningActivity(student1, timeSpent);
			Page page = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			page.addLearningActivity(learningActivity);
			TestAction readPageTestAction = testActionFactory.getReadPageTestAction(courseSection, page, learningActivity);
			secondRequest.addTestAction(readPageTestAction);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page.getLearningResourceId() + " : " + timeSpent + " seconds");
	
			// Chapter Quiz
			// Question 1 attempt 1, correct, final 
			timeSpent = 60;
			Quiz chquiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			Question chQues1 = chquiz.getQuestions().get(0);
			Answer chapQuizQues1CorrectAnswer = chQues1.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			MultiValueAttempt attemptQ1A1 
				= getMultiValueRadioButtonAttempt(chQues1, student1, timeSpent, /*attemptNumber*/ 1, 
						chapQuizQues1CorrectAnswer, chQues1.getPointsPossible(), /*isFinalAttempt*/ true);
			chQues1.addAttempt(attemptQ1A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chQues1.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Quiz - Question 1
			QuestionCompletionActivity chapQuizQuestion1CompletionActivity = new QuestionCompletionActivity(student1, chQues1.getPointsPossible());
			chQues1.addCompletionActivity(chapQuizQuestion1CompletionActivity);
			TestAction ActionChQ1A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, chQues1, attemptQ1A1, chapQuizQuestion1CompletionActivity);
			secondRequest.addTestAction(ActionChQ1A1);

			// Question 2 attempt 1, correct, final 
			timeSpent = 60;
			Question chQues2 = chquiz.getQuestions().get(1);
			Answer chapQuizQues2CorrectAnswer = chQues2.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);	
			MultiValueAttempt attemptQ2A1 
				= getMultiValueRadioButtonAttempt(chQues2, student1, timeSpent, /*attemptNumber*/ 1, 
						chapQuizQues2CorrectAnswer, chQues2.getPointsPossible(), /*isFinalAttempt*/ true);
			chQues2.addAttempt(attemptQ2A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chQues2.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Quiz - Question 2
			QuestionCompletionActivity chapQuizQuestion2CompletionActivity = new QuestionCompletionActivity(student1, chQues2.getPointsPossible());
			chQues2.addCompletionActivity(chapQuizQuestion2CompletionActivity);
			TestAction ActionChQ2A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, chQues2, attemptQ2A1, chapQuizQuestion2CompletionActivity);
			secondRequest.addTestAction(ActionChQ2A1);
			
			// Question 3 attempt 1, correct, final 
			timeSpent = 60;
			Question chQues3 = chquiz.getQuestions().get(2);
			Answer chapQuizQues3CorrectAnswer = chQues3.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);			
			MultiValueAttempt attemptQ3A1 
				= getMultiValueRadioButtonAttempt(chQues3, student1, timeSpent, /*attemptNumber*/ 1, 
						chapQuizQues3CorrectAnswer, chQues3.getPointsPossible(), /*isFinalAttempt*/ true);
			chQues3.addAttempt(attemptQ3A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + chQues3.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Quiz - Question 3
			QuestionCompletionActivity chapQuizQuestion3CompletionActivity = new QuestionCompletionActivity(student1, chQues3.getPointsPossible());
			chQues3.addCompletionActivity(chapQuizQuestion3CompletionActivity);
			TestAction ActionChQ3A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, chQues3, attemptQ3A1, chapQuizQuestion3CompletionActivity);
			secondRequest.addTestAction(ActionChQ3A1);
			
			
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
			timeSpent = 60;
			Quiz sequiz = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			Question seQues1 = sequiz.getQuestions().get(0);
			Answer sectQuizQues1CorrectAnswer = seQues1.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			MultiValueAttempt SEattemptQ1A1 
				= getMultiValueRadioButtonAttempt(seQues1, student1, timeSpent, /*attemptNumber*/ 1, sectQuizQues1CorrectAnswer, 
						seQues1.getPointsPossible(), /*isFinalAttempt*/ true);
			seQues1.addAttempt(SEattemptQ1A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + seQues1.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Section Quiz - Question 1
			QuestionCompletionActivity chapSecQuizQuestion1CompletionActivity = new QuestionCompletionActivity(student1, seQues1.getPointsPossible());
			seQues1.addCompletionActivity(chapSecQuizQuestion1CompletionActivity);
			TestAction ActionSEQ1A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, seQues1, SEattemptQ1A1, chapSecQuizQuestion1CompletionActivity);
			thirdRequest.addTestAction(ActionSEQ1A1);

			// Question 2 attempt 1, correct, final 
			timeSpent = 60;
			Question seQues2 = sequiz.getQuestions().get(1);
			Answer sectQuizQues2CorrectAnswer = seQues2.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);	
			MultiValueAttempt SEattemptQ2A1 
				= getMultiValueRadioButtonAttempt(seQues2, student1, timeSpent, /*attemptNumber*/ 1, 
						sectQuizQues2CorrectAnswer, seQues2.getPointsPossible(), /*isFinalAttempt*/ true);
			seQues2.addAttempt(SEattemptQ2A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + seQues2.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Section Quiz - Question 2
			QuestionCompletionActivity chapSecQuizQuestion2CompletionActivity = new QuestionCompletionActivity(student1, seQues2.getPointsPossible());
			seQues2.addCompletionActivity(chapSecQuizQuestion2CompletionActivity);
			TestAction ActionSEQ2A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, seQues2, SEattemptQ2A1, chapSecQuizQuestion2CompletionActivity);
			thirdRequest.addTestAction(ActionSEQ2A1);
			
			// Question 3 attempt 1, correct, final 
			timeSpent = 60;
			Question seQues3 = sequiz.getQuestions().get(2);
			Answer sectQuizQues3CorrectAnswer = seQues3.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);			
			MultiValueAttempt SEattemptQ3A1 
				= getMultiValueRadioButtonAttempt(seQues3, student1, timeSpent, /*attemptNumber*/ 1, 
						sectQuizQues3CorrectAnswer, seQues3.getPointsPossible(), /*isFinalAttempt*/ true);
			seQues3.addAttempt(SEattemptQ3A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + seQues3.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Section Quiz - Question 3
			QuestionCompletionActivity chapSecQuizQuestion3CompletionActivity = new QuestionCompletionActivity(student1, seQues3.getPointsPossible());
			seQues3.addCompletionActivity(chapSecQuizQuestion3CompletionActivity);
			TestAction ActionSEQ3A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, seQues3, SEattemptQ3A1, chapSecQuizQuestion3CompletionActivity);
			thirdRequest.addTestAction(ActionSEQ3A1);
			
			// Assessment performance for section quiz
			QuizCompletionActivity SEQuizCompletionActivity = new QuizCompletionActivity(student1);
			SEQuizCompletionActivity.addQuestionPerf(seQues1.getId(), seQues1.getPointsPossible());
			SEQuizCompletionActivity.addQuestionPerf(seQues2.getId(), seQues2.getPointsPossible());
			SEQuizCompletionActivity.addQuestionPerf(seQues3.getId(), seQues3.getPointsPossible());
			sequiz.addCompletionActivity(SEQuizCompletionActivity);
			TestAction completeSEQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, sequiz, SEQuizCompletionActivity);
			thirdRequest.addTestAction(completeSEQuizTestAction);
			
			thirdRequest.executeAllActions();
						
			
			////---- TA 1
			//Read Page
			timeSpent = 100;
			LearningActivity learningActivityTA = new LearningActivity(ta1, timeSpent);
			Page pageTA = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			pageTA.addLearningActivity(learningActivityTA);
			TestAction readPageTestActionTA = testActionFactory.getReadPageTestAction(courseSection, pageTA, learningActivityTA);
			fourthRequest.addTestAction(readPageTestActionTA);
			timeOnTaskOutput.append("\n...learning time (TA): " + ta1.getPersonId() + " : " + pageTA.getLearningResourceId() + " : " + timeSpent + " seconds");
	
			// Chapter Quiz
			// Question 1 attempt 1, correct, final 
			timeSpent = 60;
			Quiz chquizTA = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			Question chQues1TA = chquizTA.getQuestions().get(0);
			Answer chapQuizQues1CorrectAnswerTA = chQues1TA.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			MultiValueAttempt attemptQ1A1TA 
				= getMultiValueRadioButtonAttempt(chQues1TA, ta1, timeSpent, /*attemptNumber*/ 1, chapQuizQues1CorrectAnswerTA, 
						chQues1TA.getPointsPossible(), /*isFinalAttempt*/ true);
			chQues1TA.addAttempt(attemptQ1A1TA);
			timeOnTaskOutput.append("\n...assessment time (TA): " + ta1.getPersonId() + " : " + chQues1TA.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Quiz - Question 1
			QuestionCompletionActivity chapQuizQuestion1CompletionActivityTA = new QuestionCompletionActivity(ta1, chQues1TA.getPointsPossible());
			chQues1TA.addCompletionActivity(chapQuizQuestion1CompletionActivityTA);
			TestAction ActionChQ1A1TA = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquizTA, chQues1TA, attemptQ1A1TA, chapQuizQuestion1CompletionActivityTA);
			fourthRequest.addTestAction(ActionChQ1A1TA);

			// Question 2 attempt 1, correct, final
			timeSpent = 60;
			Question chQues2TA = chquizTA.getQuestions().get(1);
			Answer chapQuizQues2CorrectAnswerTA = chQues2TA.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);	
			MultiValueAttempt attemptQ2A1TA 
				= getMultiValueRadioButtonAttempt(chQues2TA, ta1, timeSpent, /*attemptNumber*/ 1, 
						chapQuizQues2CorrectAnswerTA, chQues2TA.getPointsPossible(), /*isFinalAttempt*/ true);
			chQues2TA.addAttempt(attemptQ2A1TA);
			timeOnTaskOutput.append("\n...assessment time (TA): " + ta1.getPersonId() + " : " + chQues2TA.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Quiz - Question 2
			QuestionCompletionActivity chapQuizQuestion2CompletionActivityTA = new QuestionCompletionActivity(ta1, chQues2TA.getPointsPossible());
			chQues2TA.addCompletionActivity(chapQuizQuestion2CompletionActivityTA);
			TestAction ActionChQ2A1TA = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquizTA, chQues2TA, attemptQ2A1TA, chapQuizQuestion2CompletionActivityTA);
			fourthRequest.addTestAction(ActionChQ2A1TA);
			
			// Question 3 attempt 1, correct, final 
			timeSpent = 60;
			Question chQues3TA = chquizTA.getQuestions().get(2);
			Answer chapQuizQues3CorrectAnswerTA = chQues3TA.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);			
			MultiValueAttempt attemptQ3A1TA 
				= getMultiValueRadioButtonAttempt(chQues3TA, ta1, timeSpent, /*attemptNumber*/ 1, 
						chapQuizQues3CorrectAnswerTA, chQues3TA.getPointsPossible(), /*isFinalAttempt*/ true);
			chQues3TA.addAttempt(attemptQ3A1TA);
			timeOnTaskOutput.append("\n...assessment time (TA): " + ta1.getPersonId() + " : " + chQues3TA.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Quiz - Question 3
			QuestionCompletionActivity chapQuizQuestion3CompletionActivityTA = new QuestionCompletionActivity(ta1, chQues3TA.getPointsPossible());
			chQues3TA.addCompletionActivity(chapQuizQuestion3CompletionActivityTA);
			TestAction ActionChQ3A1TA = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquizTA, chQues3TA, attemptQ3A1TA, chapQuizQuestion3CompletionActivityTA);
			fourthRequest.addTestAction(ActionChQ3A1TA);
			
			// Assessment performance for Chapter quiz
			QuizCompletionActivity ChQuizCompletionActivityTA = new QuizCompletionActivity(ta1);
			ChQuizCompletionActivityTA.addQuestionPerf(chQues1TA.getId(), chQues1TA.getPointsPossible());
			ChQuizCompletionActivityTA.addQuestionPerf(chQues2TA.getId(), chQues2TA.getPointsPossible());
			ChQuizCompletionActivityTA.addQuestionPerf(chQues3TA.getId(), chQues3TA.getPointsPossible());
			chquizTA.addCompletionActivity(ChQuizCompletionActivityTA);
			TestAction completeChQuizTestActionTA = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chquizTA, ChQuizCompletionActivityTA);
			fourthRequest.addTestAction(completeChQuizTestActionTA);
			
			fourthRequest.executeAllActions();
			
			
			// Chapter Section Quiz
			// Question 1 attempt 1, correct, final 
			timeSpent = 60;
			Quiz sequizTA = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			Question seQues1TA = sequizTA.getQuestions().get(0);
			Answer sectQuizQues1CorrectAnswerTA = seQues1TA.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			MultiValueAttempt SEattemptQ1A1TA 
				= getMultiValueRadioButtonAttempt(seQues1TA, ta1, timeSpent, /*attemptNumber*/ 1, sectQuizQues1CorrectAnswerTA, 
						seQues1TA.getPointsPossible(), /*isFinalAttempt*/ true);
			seQues1TA.addAttempt(SEattemptQ1A1TA);
			timeOnTaskOutput.append("\n...assessment time (TA): " + ta1.getPersonId() + " : " + seQues1TA.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Section Quiz - Question 1
			QuestionCompletionActivity chapSecQuizQuestion1CompletionActivityTA = new QuestionCompletionActivity(ta1, seQues1TA.getPointsPossible());
			seQues1TA.addCompletionActivity(chapSecQuizQuestion1CompletionActivityTA);
			TestAction ActionSEQ1A1TA = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequizTA, seQues1TA, SEattemptQ1A1TA, chapSecQuizQuestion1CompletionActivityTA);
			fourthRequest.addTestAction(ActionSEQ1A1TA);

			// Question 2 attempt 1, correct, final
			timeSpent = 60;
			Question seQues2TA = sequizTA.getQuestions().get(1);
			Answer sectQuizQues2CorrectAnswerTA = seQues2TA.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);	
			MultiValueAttempt SEattemptQ2A1TA 
				= getMultiValueRadioButtonAttempt(seQues2TA, ta1, timeSpent, /*attemptNumber*/ 1, 
						sectQuizQues2CorrectAnswerTA, seQues2TA.getPointsPossible(), /*isFinalAttempt*/ true);
			seQues2TA.addAttempt(SEattemptQ2A1TA);
			timeOnTaskOutput.append("\n...assessment time (TA): " + ta1.getPersonId() + " : " + seQues2TA.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Section Quiz - Question 2
			QuestionCompletionActivity chapSecQuizQuestion2CompletionActivityTA = new QuestionCompletionActivity(ta1, seQues2TA.getPointsPossible());
			seQues2TA.addCompletionActivity(chapSecQuizQuestion2CompletionActivityTA);
			TestAction ActionSEQ2A1TA = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequizTA, seQues2TA, SEattemptQ2A1TA, chapSecQuizQuestion2CompletionActivityTA);
			fourthRequest.addTestAction(ActionSEQ2A1TA);
			
			// Question 3 attempt 1, correct, final 
			timeSpent = 60;
			Question seQues3TA = sequizTA.getQuestions().get(2);
			Answer sectQuizQues3CorrectAnswerTA = seQues3TA.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);			
			MultiValueAttempt SEattemptQ3A1TA 
				= getMultiValueRadioButtonAttempt(seQues3TA, ta1, timeSpent, /*attemptNumber*/ 1, 
						sectQuizQues3CorrectAnswerTA, seQues3TA.getPointsPossible(), /*isFinalAttempt*/ true);
			seQues3TA.addAttempt(SEattemptQ3A1TA);
			timeOnTaskOutput.append("\n...assessment time (TA): " + ta1.getPersonId() + " : " + seQues3TA.getId() + " : " + timeSpent + " seconds");
			
			//Question Completion Activity - Chapter Section Quiz - Question 3
			QuestionCompletionActivity chapSecQuizQuestion3CompletionActivityTA = new QuestionCompletionActivity(ta1, seQues3TA.getPointsPossible());
			seQues3TA.addCompletionActivity(chapSecQuizQuestion3CompletionActivityTA);
			TestAction ActionSEQ3A1TA = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequizTA, seQues3TA, SEattemptQ3A1TA, chapSecQuizQuestion3CompletionActivityTA);
			fourthRequest.addTestAction(ActionSEQ3A1TA);
			
			// Assessment performance for section quiz
			QuizCompletionActivity SEQuizCompletionActivityTA = new QuizCompletionActivity(ta1);
			SEQuizCompletionActivityTA.addQuestionPerf(seQues1TA.getId(), seQues1TA.getPointsPossible());
			SEQuizCompletionActivityTA.addQuestionPerf(seQues2TA.getId(), seQues2TA.getPointsPossible());
			SEQuizCompletionActivityTA.addQuestionPerf(seQues3TA.getId(), seQues3TA.getPointsPossible());
			sequizTA.addCompletionActivity(SEQuizCompletionActivityTA);
			TestAction completeSEQuizTestActionTA = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, sequizTA, SEQuizCompletionActivityTA);
			fourthRequest.addTestAction(completeSEQuizTestActionTA);

			//TA  - remove read page
			pageTA.removeLearningActivity(learningActivityTA);
			
			//TA - remove Chapter Quiz attempts
			for(Question ques : chquizTA.getQuestions()){
				if(ques.studentAttemptedQuestion(ta1)){
					List<Attempt> attempts = ques.getAttemptsForUser(ta1);
					for(Attempt attempt : attempts){
						ques.removeAttempt(attempt);
					}
				}
				//remove Question completion activity
				ques.removeStudentCompletionActivity(ta1);
			}
			//TA  - remove Chapter Section Quiz attempts
			for(Question ques : sequizTA.getQuestions()){
				if(ques.studentAttemptedQuestion(ta1)){
					List<Attempt> attempts = ques.getAttemptsForUser(ta1);
					for(Attempt attempt : attempts){
						ques.removeAttempt(attempt);
					}
				}
				//remove Question completion activity
				ques.removeStudentCompletionActivity(ta1);
			}
			// TA  - remove completionActivity
			sequizTA.removeCompletionActivity(SEQuizCompletionActivityTA);
			chquizTA.removeCompletionActivity(ChQuizCompletionActivityTA);
			
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

