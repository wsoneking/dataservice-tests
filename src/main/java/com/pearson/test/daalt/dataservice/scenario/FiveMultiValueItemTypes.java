package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.TestEngine;
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
		/**
		 * Quiz description:
		 * 	1. Binning
				Multi-subquestion and subquestion has multi-correct answers
					- 2 subquestions, each subquestion has two correct answers. 
			2. Categorizing		// not exist anymore
				Multi-subquestion, but each subquestion has only one correct answer
					- 2 subquestions, each subquestion has one correct answer.
			3. HotSpot
				only one subquestion, which has only one correct answer 
					- 1 subquestion, which has only one correct answer.
			4. MultipleHotSpot
				Multi-subquestion, but each subquestion has only one correct answer 
					- 2 subquestions, each subquestion has one correct answer.
			5. MultiSelect
				Only one subquestion, which has multi-correct answers
					- 1 subquestion, each subquestion has two correct answers. 
			6. RadioButton
				Only one subquestion, which has only one correct answer. And the text of question and text of subquestion are the same. 
					- 1 subquestion, which has only one correct answer.
		

		 * 
		 * Scenario description: 
			Two students. 
		    One student complete all the questions correctly with one attempt. score: 3*6 = 18
		    The other completes the questions with two attempts.  score: 2*6 = 12
			  For the second incorrect attempt (In this design, all three incorrect answer methods are called): 
				1. Binning: first subquestion correctly, but second subquestion IncompleteCorrectAnswer*.
				2. Categorizing: first subquestion correctly, second SingleIncorrectAnswer**. 
				3. HotSpot: SingleIncorrectAnswer.
				4. MultipleHotSpot: first subquestion correctly, second SingleIncorrectAnswer.
				5. MultiSelect: MultipleIncorrectAnswer***.
				6. RadioButton: SingleIncorrectAnswer.
			For the first attempt, the student answers all the question correctly. 
			
		 */
public class FiveMultiValueItemTypes extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
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
			testData = new BasicTestData("FiveMultiValueItemTypes");
			
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
		
			// We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
			//add action to enroll instructor
			TestAction enrollInstr = testActionFactory.getEnrollInstructorTestAction(instr, courseSection);
			firstRequest.addTestAction(enrollInstr);
			
			//enroll student 1
			courseSection.addStudent(student1);
			//add action to enroll student
			TestAction enrollStudent1 = testActionFactory.getEnrollStudentTestAction(student1, courseSection);
			firstRequest.addTestAction(enrollStudent1);	
			
			//enroll student 2
			courseSection.addStudent(student2);
			//add action to enroll student
			TestAction enrollStudent2 = testActionFactory.getEnrollStudentTestAction(student2, courseSection);
			firstRequest.addTestAction(enrollStudent2);
			
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.ONE_QUIZ_WITH_SIX_NEW_TYPES_OF_QUESTIONS);
			
			System.out.println(assignmentToAdd.getStructure());
			
			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			assignmentToAdd.setDueDate(nowCal.getTime());
			assignmentToAdd.setTitle("DAALT SQE Test Assignment");
			courseSection.addAssignment(assignmentToAdd);
			//add action to add assignment to course section
			// Chapter Quiz
			Quiz chquiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			TestAction addAssignment = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
			firstRequest.addTestAction(addAssignment);
			
			firstRequest.executeAllActions();
			
			//Binning Question attempt 1, correct, final 
			Question binningQuestion = chquiz.getQuestions().get(0);
			MultiValueAttempt attemptQ1A1 = getMultiValueAttemptComplete(binningQuestion, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ binningQuestion.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			binningQuestion.addAttempt(attemptQ1A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + binningQuestion.getId() + " : 60 seconds");
			
			//Binning Question Completion Activity
			QuestionCompletionActivity chapQuizQuestion1CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ binningQuestion.getPointsPossible());
			binningQuestion.addCompletionActivity(chapQuizQuestion1CompletionActivity);
			TestAction ActionChQ1A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, binningQuestion, attemptQ1A1, chapQuizQuestion1CompletionActivity);
			secondRequest.addTestAction(ActionChQ1A1);
			
			//Hotspot Question attempt 1, correct, final 
			Question hotspotQuestion = chquiz.getQuestions().get(1);	
			MultiValueAttempt attemptQ3A1 = getMultiValueAttemptComplete(hotspotQuestion, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ hotspotQuestion.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			hotspotQuestion.addAttempt(attemptQ3A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + hotspotQuestion.getId() + " : 60 seconds");
			
			//Hotspot Question Completion Activity
			QuestionCompletionActivity chapQuizQuestion3CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ hotspotQuestion.getPointsPossible());
			hotspotQuestion.addCompletionActivity(chapQuizQuestion3CompletionActivity);
			TestAction ActionChQ3A3 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, hotspotQuestion, attemptQ3A1, chapQuizQuestion3CompletionActivity);
			secondRequest.addTestAction(ActionChQ3A3);
			
			//Multiple Hotspot Question attempt 1, correct, final 
			Question multipleHotspotQuestion = chquiz.getQuestions().get(2);	
			MultiValueAttempt attemptQ4A1 = getMultiValueAttemptComplete(multipleHotspotQuestion, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ multipleHotspotQuestion.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			multipleHotspotQuestion.addAttempt(attemptQ4A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + multipleHotspotQuestion.getId() + " : 60 seconds");
			
			//Multiple Hotspot Question Completion Activity
			QuestionCompletionActivity chapQuizQuestion4CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ multipleHotspotQuestion.getPointsPossible());
			multipleHotspotQuestion.addCompletionActivity(chapQuizQuestion4CompletionActivity);
			TestAction ActionChQ4A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, multipleHotspotQuestion, attemptQ4A1, chapQuizQuestion4CompletionActivity);
			secondRequest.addTestAction(ActionChQ4A1);
			
			//(standard) Multi-Select Question attempt 1, correct, final 
			Question standardMultiSelectQuestion = chquiz.getQuestions().get(3);	
			MultiValueAttempt attemptQ5A1 = getMultiValueAttemptComplete(standardMultiSelectQuestion, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ standardMultiSelectQuestion.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			standardMultiSelectQuestion.addAttempt(attemptQ5A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + standardMultiSelectQuestion.getId() + " : 60 seconds");
			
			//(standard) Multi-Select Question Completion Activity
			QuestionCompletionActivity chapQuizQuestion5CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ standardMultiSelectQuestion.getPointsPossible());
			standardMultiSelectQuestion.addCompletionActivity(chapQuizQuestion5CompletionActivity);
			TestAction ActionChQ5A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, standardMultiSelectQuestion, attemptQ5A1, chapQuizQuestion5CompletionActivity);
			secondRequest.addTestAction(ActionChQ5A1);
						
			//(standard) Radio Button Question attempt 1, correct, final 
			Question standardRadioButtonQuestion = chquiz.getQuestions().get(4);	
			MultiValueAttempt attemptQ6A1 = getMultiValueAttemptComplete(standardRadioButtonQuestion, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ standardRadioButtonQuestion.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			standardRadioButtonQuestion.addAttempt(attemptQ6A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + standardRadioButtonQuestion.getId() + " : 60 seconds");
			
			//(standard) Radio Button Question Completion Activity
			QuestionCompletionActivity chapQuizQuestion6CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ standardRadioButtonQuestion.getPointsPossible());
			standardRadioButtonQuestion.addCompletionActivity(chapQuizQuestion6CompletionActivity);
			TestAction ActionChQ6A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, standardRadioButtonQuestion, attemptQ6A1, chapQuizQuestion6CompletionActivity);
			secondRequest.addTestAction(ActionChQ6A1);
			
			//(funky) Radio Button Question 1 attempt 1, correct, final 
			Question funkyRadioButtonQuestion01 = chquiz.getQuestions().get(5);	
			MultiValueAttempt attemptQ7A1 = getMultiValueAttemptComplete(funkyRadioButtonQuestion01, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ funkyRadioButtonQuestion01.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			funkyRadioButtonQuestion01.addAttempt(attemptQ7A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + funkyRadioButtonQuestion01.getId() + " : 60 seconds");
			
			//(funky) Radio Button Question 1 Completion Activity
			QuestionCompletionActivity chapQuizQuestion7CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ funkyRadioButtonQuestion01.getPointsPossible());
			funkyRadioButtonQuestion01.addCompletionActivity(chapQuizQuestion7CompletionActivity);
			TestAction ActionChQ7A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, funkyRadioButtonQuestion01, attemptQ7A1, chapQuizQuestion7CompletionActivity);
			secondRequest.addTestAction(ActionChQ7A1);
			
			//(funky) Radio Button Question 2 attempt 1, correct, final 
			Question funkyRadioButtonQuestion02 = chquiz.getQuestions().get(6);	
			MultiValueAttempt attemptQ8A1 = getMultiValueAttemptComplete(funkyRadioButtonQuestion02, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ funkyRadioButtonQuestion02.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			funkyRadioButtonQuestion02.addAttempt(attemptQ8A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + funkyRadioButtonQuestion02.getId() + " : 60 seconds");
			
			//(funky) Radio Button Question 2 Completion Activity
			QuestionCompletionActivity chapQuizQuestion8CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ funkyRadioButtonQuestion02.getPointsPossible());
			funkyRadioButtonQuestion02.addCompletionActivity(chapQuizQuestion8CompletionActivity);
			TestAction ActionChQ8A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, funkyRadioButtonQuestion02, attemptQ8A1, chapQuizQuestion8CompletionActivity);
			secondRequest.addTestAction(ActionChQ8A1);
			
			//(funky) Multi-Select Question 1 attempt 1, correct, final 
			Question funkyMultiSelectQuestion01 = chquiz.getQuestions().get(7);	
			MultiValueAttempt attemptQ9A1 = getMultiValueAttemptComplete(funkyMultiSelectQuestion01, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ funkyMultiSelectQuestion01.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			funkyMultiSelectQuestion01.addAttempt(attemptQ9A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + funkyMultiSelectQuestion01.getId() + " : 60 seconds");
			
			//(funky) Multi-Select Question 1 Completion Activity
			QuestionCompletionActivity chapQuizQuestion09CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ funkyMultiSelectQuestion01.getPointsPossible());
			funkyMultiSelectQuestion01.addCompletionActivity(chapQuizQuestion09CompletionActivity);
			TestAction ActionChQ9A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, funkyMultiSelectQuestion01, attemptQ9A1, chapQuizQuestion09CompletionActivity);
			secondRequest.addTestAction(ActionChQ9A1);
			
			//(funky) Multi-Select Question 2 attempt 1, correct, final 
			Question funkyMultiSelectQuestion02 = chquiz.getQuestions().get(8);	
			MultiValueAttempt attemptQ10A1 = getMultiValueAttemptComplete(funkyMultiSelectQuestion02, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ funkyMultiSelectQuestion02.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			funkyMultiSelectQuestion02.addAttempt(attemptQ10A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + funkyMultiSelectQuestion02.getId() + " : 60 seconds");
			
			//(funky) Multi-Select Question 2 Completion Activity
			QuestionCompletionActivity chapQuizQuestion10CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ funkyMultiSelectQuestion02.getPointsPossible());
			funkyMultiSelectQuestion02.addCompletionActivity(chapQuizQuestion10CompletionActivity);
			TestAction ActionChQ10A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, funkyMultiSelectQuestion02, attemptQ10A1, chapQuizQuestion10CompletionActivity);
			secondRequest.addTestAction(ActionChQ10A1);
			
			//Student 1 Assessment performance for Chapter quiz
			QuizCompletionActivity chQuizCompletionActivity = new QuizCompletionActivity(student1);
			chQuizCompletionActivity.addQuestionPerf(binningQuestion.getId(), /*pointsEarned*/ binningQuestion.getPointsPossible());
			chQuizCompletionActivity.addQuestionPerf(hotspotQuestion.getId(), /*pointsEarned*/ hotspotQuestion.getPointsPossible());
			chQuizCompletionActivity.addQuestionPerf(multipleHotspotQuestion.getId(), /*pointsEarned*/ multipleHotspotQuestion.getPointsPossible());
			chQuizCompletionActivity.addQuestionPerf(standardMultiSelectQuestion.getId(), /*pointsEarned*/ standardMultiSelectQuestion.getPointsPossible());
			chQuizCompletionActivity.addQuestionPerf(standardRadioButtonQuestion.getId(), /*pointsEarned*/ standardRadioButtonQuestion.getPointsPossible());
			chQuizCompletionActivity.addQuestionPerf(funkyRadioButtonQuestion01.getId(), /*pointsEarned*/ funkyRadioButtonQuestion01.getPointsPossible());
			chQuizCompletionActivity.addQuestionPerf(funkyRadioButtonQuestion02.getId(), /*pointsEarned*/ funkyRadioButtonQuestion02.getPointsPossible());
			chQuizCompletionActivity.addQuestionPerf(funkyMultiSelectQuestion01.getId(), /*pointsEarned*/ funkyMultiSelectQuestion01.getPointsPossible());
			chQuizCompletionActivity.addQuestionPerf(funkyMultiSelectQuestion02.getId(), /*pointsEarned*/ funkyMultiSelectQuestion02.getPointsPossible());
			chquiz.addCompletionActivity(chQuizCompletionActivity);
			TestAction completeChQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chquiz, chQuizCompletionActivity);
			secondRequest.addTestAction(completeChQuizTestAction);
			
			secondRequest.executeAllActions();
			
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
			
			// STUDENT 2
			//Binning Question attempt 1, incorrect, not final 
			MultiValueAttempt S2attemptQ1A1 = getMultiValueAttemptComplete(binningQuestion, student2, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ 0, /*isFinalAttempt*/ false, CorrectOrIncorrectLibrary.INCOMPLETECORRECTANSWERS_FIRST_SUB_CORRECT);
			binningQuestion.addAttempt(S2attemptQ1A1);
			TestAction S2ActionChQ1A1_1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, binningQuestion, S2attemptQ1A1, null);
			thirdRequest.addTestAction(S2ActionChQ1A1_1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + binningQuestion.getId() + " : 60 seconds");
			
			//Binning Question attempt 2, correct, final 
			MultiValueAttempt S2attemptQ1A2 = getMultiValueAttemptComplete(binningQuestion, student2, /*timeSpent*/ 60, /*attemptNumber*/ 2, 
					/*pointsEarned*/ binningQuestion.getPointsPossible()-1, /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			binningQuestion.addAttempt(S2attemptQ1A2);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + binningQuestion.getId() + " : 60 seconds");
			//Question Completion Activity
			QuestionCompletionActivity S2chapQuizQuestion1CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ binningQuestion.getPointsPossible()-1);
			binningQuestion.addCompletionActivity(S2chapQuizQuestion1CompletionActivity);
			TestAction S2ActionChQ1A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, binningQuestion, S2attemptQ1A2, S2chapQuizQuestion1CompletionActivity);
			thirdRequest.addTestAction(S2ActionChQ1A1);
			
			//Hotspot Question attempt 1, incorrect, not final 
			MultiValueAttempt S2attemptQ3A1 = getMultiValueAttemptComplete(hotspotQuestion, student2, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ 0, /*isFinalAttempt*/ false, CorrectOrIncorrectLibrary.SINGLEINCORRECTANSWER);
			hotspotQuestion.addAttempt(S2attemptQ3A1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + hotspotQuestion.getId() + " : 60 seconds");
			TestAction S2ActionChQ3A3_1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, hotspotQuestion, S2attemptQ3A1, null);
			thirdRequest.addTestAction(S2ActionChQ3A3_1);

			//Hotspot Question attempt 2, correct, final 
			MultiValueAttempt S2attemptQ3A2 = getMultiValueAttemptComplete(hotspotQuestion, student2, /*timeSpent*/ 60, /*attemptNumber*/ 2, 
					/*pointsEarned*/ hotspotQuestion.getPointsPossible()-1, /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			hotspotQuestion.addAttempt(S2attemptQ3A2);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + hotspotQuestion.getId() + " : 60 seconds");
			//Question Completion Activity
			QuestionCompletionActivity S2chapQuizQuestion3CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ hotspotQuestion.getPointsPossible()-1);
			hotspotQuestion.addCompletionActivity(S2chapQuizQuestion3CompletionActivity);
			TestAction S2ActionChQ3A3 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, hotspotQuestion, S2attemptQ3A2, S2chapQuizQuestion3CompletionActivity);
			thirdRequest.addTestAction(S2ActionChQ3A3);
			
			//Multiple Hotspot attempt 1, incorrect, not final 
			MultiValueAttempt S2attemptQ4A1 = getMultiValueAttemptComplete(multipleHotspotQuestion, student2, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ 0, /*isFinalAttempt*/ false, CorrectOrIncorrectLibrary.SINGLEINCORRECTANSWER);
			multipleHotspotQuestion.addAttempt(S2attemptQ4A1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + multipleHotspotQuestion.getId() + " : 60 seconds");
			TestAction S2ActionChQ4A1_1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, multipleHotspotQuestion, S2attemptQ4A1, null);
			thirdRequest.addTestAction(S2ActionChQ4A1_1);
			
			//Multiple Hotspot attempt 2, correct, final 
			MultiValueAttempt S2attemptQ4A2 = getMultiValueAttemptComplete(multipleHotspotQuestion, student2, /*timeSpent*/ 60, /*attemptNumber*/ 2, 
					/*pointsEarned*/ multipleHotspotQuestion.getPointsPossible()-1, /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			multipleHotspotQuestion.addAttempt(S2attemptQ4A2);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + multipleHotspotQuestion.getId() + " : 60 seconds");
			//Question Completion Activity
			QuestionCompletionActivity S2chapQuizQuestion4CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ multipleHotspotQuestion.getPointsPossible()-1);
			multipleHotspotQuestion.addCompletionActivity(S2chapQuizQuestion4CompletionActivity);
			TestAction S2ActionChQ4A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, multipleHotspotQuestion, S2attemptQ4A2, S2chapQuizQuestion4CompletionActivity);
			thirdRequest.addTestAction(S2ActionChQ4A1);
			
			//(standard) Multi-Select attempt 1, incorrect, not final 
			MultiValueAttempt S2attemptQ5A1 = getMultiValueAttemptComplete(standardMultiSelectQuestion, student2, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ 0, /*isFinalAttempt*/ false, CorrectOrIncorrectLibrary.MULTIPOLEINCORRECTANSWERS);
			standardMultiSelectQuestion.addAttempt(S2attemptQ5A1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + standardMultiSelectQuestion.getId() + " : 60 seconds");
			TestAction S2ActionChQ5A1_1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, standardMultiSelectQuestion, S2attemptQ5A1, null);
			thirdRequest.addTestAction(S2ActionChQ5A1_1);
			
			//(standard) Multi-Select attempt 2, correct, final 
			MultiValueAttempt S2attemptQ5A2 = getMultiValueAttemptComplete(standardMultiSelectQuestion, student2, /*timeSpent*/ 60, /*attemptNumber*/ 2, 
					/*pointsEarned*/ standardMultiSelectQuestion.getPointsPossible()-1, /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			standardMultiSelectQuestion.addAttempt(S2attemptQ5A2);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + standardMultiSelectQuestion.getId() + " : 60 seconds");
			//Question Completion Activity
			QuestionCompletionActivity S2chapQuizQuestion5CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ standardMultiSelectQuestion.getPointsPossible()-1);
			standardMultiSelectQuestion.addCompletionActivity(S2chapQuizQuestion5CompletionActivity);
			TestAction S2ActionChQ5A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, standardMultiSelectQuestion, S2attemptQ5A2, S2chapQuizQuestion5CompletionActivity);
			thirdRequest.addTestAction(S2ActionChQ5A1);
						
			//(standard) Radio Button attempt 1, incorrect, not final 
			MultiValueAttempt S2attemptQ6A1 = getMultiValueAttemptComplete(standardRadioButtonQuestion, student2, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ 0, /*isFinalAttempt*/ false, CorrectOrIncorrectLibrary.SINGLEINCORRECTANSWER);
			standardRadioButtonQuestion.addAttempt(S2attemptQ6A1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + standardRadioButtonQuestion.getId() + " : 60 seconds");
			TestAction S2ActionChQ6A1_1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, standardRadioButtonQuestion, S2attemptQ6A1, null);
			thirdRequest.addTestAction(S2ActionChQ6A1_1);		
			
			//(standard) Radio Button attempt 2, correct, final 
			MultiValueAttempt S2attemptQ6A2 = getMultiValueAttemptComplete(standardRadioButtonQuestion, student2, /*timeSpent*/ 60, /*attemptNumber*/ 2, 
					/*pointsEarned*/ standardRadioButtonQuestion.getPointsPossible()-1, /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			standardRadioButtonQuestion.addAttempt(S2attemptQ6A2);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + standardRadioButtonQuestion.getId() + " : 60 seconds");
			//Question Completion Activity
			QuestionCompletionActivity S2chapQuizQuestion6CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ standardRadioButtonQuestion.getPointsPossible()-1);
			standardRadioButtonQuestion.addCompletionActivity(S2chapQuizQuestion6CompletionActivity);
			TestAction S2ActionChQ6A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, standardRadioButtonQuestion, S2attemptQ6A2, S2chapQuizQuestion6CompletionActivity);
			thirdRequest.addTestAction(S2ActionChQ6A1);
			
			//(funky) Radio Button Question 1 attempt 1, incorrect, not final
			MultiValueAttempt attemptQ7Stud2A1 = getMultiValueAttemptComplete(funkyRadioButtonQuestion01, student2, /*timeSpent*/ 17, /*attemptNumber*/ 1, 
					/*pointsEarned*/ 0, /*isFinalAttempt*/ false, CorrectOrIncorrectLibrary.SINGLEINCORRECTANSWER);
			funkyRadioButtonQuestion01.addAttempt(attemptQ7Stud2A1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + funkyRadioButtonQuestion01.getId() + " : 17 seconds");
			TestAction actionChQ7Stud2A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, funkyRadioButtonQuestion01, attemptQ7Stud2A1, null);
			thirdRequest.addTestAction(actionChQ7Stud2A1);	
			
			//(funky) Radio Button Question 1 attempt 2, correct, final
			MultiValueAttempt attemptQ7Stud2A2 = getMultiValueAttemptComplete(funkyRadioButtonQuestion01, student2, /*timeSpent*/ 18, /*attemptNumber*/ 2, 
					/*pointsEarned*/ funkyRadioButtonQuestion01.getPointsPossible()-1, /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			funkyRadioButtonQuestion01.addAttempt(attemptQ7Stud2A2);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + funkyRadioButtonQuestion01.getId() + " : 18 seconds");
			
			//(funky) Radio Button Question 1 Completion Activity
			QuestionCompletionActivity chapQuizQuestion7Stud2CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ funkyRadioButtonQuestion01.getPointsPossible()-1);
			funkyRadioButtonQuestion01.addCompletionActivity(chapQuizQuestion7Stud2CompletionActivity);
			TestAction actionChQ7Stud2A2 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, funkyRadioButtonQuestion01, attemptQ7Stud2A2, chapQuizQuestion7Stud2CompletionActivity);
			thirdRequest.addTestAction(actionChQ7Stud2A2);
			
			//(funky) Radio Button Question 2 attempt 1, incorrect, not final
			MultiValueAttempt attemptQ8Stud2A1 = getMultiValueAttemptComplete(funkyRadioButtonQuestion02, student2, /*timeSpent*/ 19, /*attemptNumber*/ 1, 
					/*pointsEarned*/ 0, /*isFinalAttempt*/ false, CorrectOrIncorrectLibrary.SINGLEINCORRECTANSWER);
			funkyRadioButtonQuestion02.addAttempt(attemptQ8Stud2A1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + funkyRadioButtonQuestion02.getId() + " : 19 seconds");
			TestAction actionChQ8Stud2A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, funkyRadioButtonQuestion02, attemptQ8Stud2A1, null);
			thirdRequest.addTestAction(actionChQ8Stud2A1);	
			
			//(funky) Radio Button Question 2 attempt 2, correct, final
			MultiValueAttempt attemptQ8Stud2A2 = getMultiValueAttemptComplete(funkyRadioButtonQuestion02, student2, /*timeSpent*/ 20, /*attemptNumber*/ 2, 
					/*pointsEarned*/ funkyRadioButtonQuestion02.getPointsPossible()-1, /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			funkyRadioButtonQuestion02.addAttempt(attemptQ8Stud2A2);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + funkyRadioButtonQuestion02.getId() + " : 20 seconds");
			
			//(funky) Radio Button Question 2 Completion Activity
			QuestionCompletionActivity chapQuizQuestion8Stud2CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ funkyRadioButtonQuestion02.getPointsPossible()-1);
			funkyRadioButtonQuestion02.addCompletionActivity(chapQuizQuestion8Stud2CompletionActivity);
			TestAction actionChQ8Stud2A2 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, funkyRadioButtonQuestion02, attemptQ8Stud2A2, chapQuizQuestion8Stud2CompletionActivity);
			thirdRequest.addTestAction(actionChQ8Stud2A2);
			
			//(funky) Multi-Select Question 1 attempt 1, incorrect, not final 
			MultiValueAttempt attemptQ9Stud2A1 = getMultiValueAttemptComplete(funkyMultiSelectQuestion01, student2, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ 0, /*isFinalAttempt*/ false, CorrectOrIncorrectLibrary.MULTIPOLEINCORRECTANSWERS);
			funkyMultiSelectQuestion01.addAttempt(attemptQ9Stud2A1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + funkyMultiSelectQuestion01.getId() + " : 60 seconds");
			TestAction actionChQ9StudA1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, funkyMultiSelectQuestion01, attemptQ9Stud2A1, null);
			thirdRequest.addTestAction(actionChQ9StudA1);
			
			//(funky) Multi-Select Question 1 attempt 2, correct, final 
			MultiValueAttempt attemptQ9Stud2A2 = getMultiValueAttemptComplete(funkyMultiSelectQuestion01, student2, /*timeSpent*/ 60, /*attemptNumber*/ 2, 
					/*pointsEarned*/ funkyMultiSelectQuestion01.getPointsPossible()-1, /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			funkyMultiSelectQuestion01.addAttempt(attemptQ9Stud2A2);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + funkyMultiSelectQuestion01.getId() + " : 60 seconds");
			
			//(funky) Multi-Select Question 1 Completion Activity
			QuestionCompletionActivity chapQuizQuestion09Stud2CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ funkyMultiSelectQuestion01.getPointsPossible()-1);
			funkyMultiSelectQuestion01.addCompletionActivity(chapQuizQuestion09Stud2CompletionActivity);
			TestAction actionChQ9StudA2 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, funkyMultiSelectQuestion01, attemptQ9Stud2A2, chapQuizQuestion09Stud2CompletionActivity);
			thirdRequest.addTestAction(actionChQ9StudA2);
			
			//(funky) Multi-Select Question 2 attempt 1, incorrect, not final 
			MultiValueAttempt attemptQ10Stud2A1 = getMultiValueAttemptComplete(funkyMultiSelectQuestion02, student2, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ 0, /*isFinalAttempt*/ false, CorrectOrIncorrectLibrary.MULTIPOLEINCORRECTANSWERS);
			funkyMultiSelectQuestion02.addAttempt(attemptQ10Stud2A1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + funkyMultiSelectQuestion02.getId() + " : 60 seconds");
			TestAction actionChQ10Stud2A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, funkyMultiSelectQuestion02, attemptQ10Stud2A1, null);
			thirdRequest.addTestAction(actionChQ10Stud2A1);
			
			//(funky) Multi-Select Question 2 attempt 1, correct, final 
			MultiValueAttempt attemptQ10Stud2A2 = getMultiValueAttemptComplete(funkyMultiSelectQuestion02, student2, /*timeSpent*/ 60, /*attemptNumber*/ 2, 
					/*pointsEarned*/ funkyMultiSelectQuestion02.getPointsPossible()-1, /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			funkyMultiSelectQuestion02.addAttempt(attemptQ10Stud2A2);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + funkyMultiSelectQuestion02.getId() + " : 60 seconds");
			
			//(funky) Multi-Select Question 2 Completion Activity
			QuestionCompletionActivity chapQuizQuestion10Stud2CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ funkyMultiSelectQuestion02.getPointsPossible()-1);
			funkyMultiSelectQuestion02.addCompletionActivity(chapQuizQuestion10Stud2CompletionActivity);
			TestAction actionChQ10Stud2A2 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, funkyMultiSelectQuestion02, attemptQ10Stud2A2, chapQuizQuestion10Stud2CompletionActivity);
			thirdRequest.addTestAction(actionChQ10Stud2A2);
			
			// Assessment performance for Chapter quiz
			QuizCompletionActivity S2ChQuizCompletionActivity = new QuizCompletionActivity(student2);
			S2ChQuizCompletionActivity.addQuestionPerf(binningQuestion.getId(), /*pointsEarned*/ binningQuestion.getPointsPossible()-1);
			S2ChQuizCompletionActivity.addQuestionPerf(hotspotQuestion.getId(), /*pointsEarned*/ hotspotQuestion.getPointsPossible()-1);
			S2ChQuizCompletionActivity.addQuestionPerf(multipleHotspotQuestion.getId(), /*pointsEarned*/ multipleHotspotQuestion.getPointsPossible()-1);
			S2ChQuizCompletionActivity.addQuestionPerf(standardMultiSelectQuestion.getId(), /*pointsEarned*/ standardMultiSelectQuestion.getPointsPossible()-1);
			S2ChQuizCompletionActivity.addQuestionPerf(standardRadioButtonQuestion.getId(), /*pointsEarned*/ standardRadioButtonQuestion.getPointsPossible()-1);
			S2ChQuizCompletionActivity.addQuestionPerf(funkyRadioButtonQuestion01.getId(), /*pointsEarned*/ funkyRadioButtonQuestion01.getPointsPossible()-1);
			S2ChQuizCompletionActivity.addQuestionPerf(funkyRadioButtonQuestion02.getId(), /*pointsEarned*/ funkyRadioButtonQuestion02.getPointsPossible()-1);
			S2ChQuizCompletionActivity.addQuestionPerf(funkyMultiSelectQuestion01.getId(), /*pointsEarned*/ funkyMultiSelectQuestion01.getPointsPossible()-1);
			S2ChQuizCompletionActivity.addQuestionPerf(funkyMultiSelectQuestion02.getId(), /*pointsEarned*/ funkyMultiSelectQuestion02.getPointsPossible()-1);
			chquiz.addCompletionActivity(S2ChQuizCompletionActivity);
			TestAction S2completeChQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chquiz, S2ChQuizCompletionActivity);
			thirdRequest.addTestAction(S2completeChQuizTestAction);

			thirdRequest.executeAllActions();
			
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

