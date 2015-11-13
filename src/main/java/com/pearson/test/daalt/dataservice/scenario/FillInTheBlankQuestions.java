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

public class FillInTheBlankQuestions extends BaseTestScenario {
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
			testData = new BasicTestData("FillInTheBlankQuestions");
			
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
			assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.ASSIGNMENT_WITH_FILL_IN_THE_BLANK);
			
			System.out.println(assignmentToAdd.getStructure());
			
			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			assignmentToAdd.setDueDate(nowCal.getTime());
			assignmentToAdd.setTitle("DAALT SQE Test Assignment");
			courseSection.addAssignment(assignmentToAdd);
			//add action to add assignment to course section
			// Chapter Quiz
			Quiz chquiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			Question unseededOtherQuestion = chquiz.getQuestions().get(2);
//			Question unseededOtherQuestion = chquiz.getQuestions().get(1);
			TestAction addAssignment = testActionFactory.getAlternateAddAssignment(instr, courseSection, assignmentToAdd, unseededOtherQuestion);
//			TestAction addAssignment = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
			firstRequest.addTestAction(addAssignment);
			
			firstRequest.executeAllActions();
			
			Question seededOtherQuestion = chquiz.getQuestions().get(1);
//			seededOtherQuestion.setIsQuestionSeeded(false);
//			seededOtherQuestion.setQuestionLastSeedDateTime(null);
			
			unseededOtherQuestion.setIsQuestionSeeded(false);
			unseededOtherQuestion.setQuestionLastSeedDateTime(null);
			
			//(standard) Radio Button Question attempt 1, correct, final 
			Question standardRadioButtonQuestion = chquiz.getQuestions().get(0);	
			MultiValueAttempt attemptQ6A1 = getMultiValueAttemptComplete(standardRadioButtonQuestion, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ standardRadioButtonQuestion.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			standardRadioButtonQuestion.addAttempt(attemptQ6A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + standardRadioButtonQuestion.getId() + " : 60 seconds");
			
			//(standard) Radio Button Question Completion Activity
			QuestionCompletionActivity chapQuizQuestion6CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ standardRadioButtonQuestion.getPointsPossible());
			standardRadioButtonQuestion.addCompletionActivity(chapQuizQuestion6CompletionActivity);
			TestAction ActionChQ6A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, standardRadioButtonQuestion, attemptQ6A1, chapQuizQuestion6CompletionActivity);
			secondRequest.addTestAction(ActionChQ6A1);
			
			//seeded "Other" Question attempt 1, correct, final 
			MultiValueAttempt attemptQ11A1 = getMultiValueAttemptComplete(seededOtherQuestion, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ seededOtherQuestion.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			seededOtherQuestion.addAttempt(attemptQ11A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + seededOtherQuestion.getId() + " : 60 seconds");
			
			//seeded "Other" Question Completion Activity
			QuestionCompletionActivity chapQuizQuestion11CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ seededOtherQuestion.getPointsPossible());
			seededOtherQuestion.addCompletionActivity(chapQuizQuestion11CompletionActivity);
			TestAction ActionChQ11A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, seededOtherQuestion, attemptQ11A1, chapQuizQuestion11CompletionActivity);
			secondRequest.addTestAction(ActionChQ11A1);
			
			//unseeded "Other" Question attempt 1, correct, final	
			MultiValueAttempt attemptQ12A1 = getMultiValueAttemptComplete(unseededOtherQuestion, student1, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ unseededOtherQuestion.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			unseededOtherQuestion.addAttempt(attemptQ12A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + unseededOtherQuestion.getId() + " : 60 seconds");
			
			//unseeded "Other" Question Completion Activity
			QuestionCompletionActivity chapQuizQuestion12CompletionActivity = new QuestionCompletionActivity(student1, /*score*/ unseededOtherQuestion.getPointsPossible());
			unseededOtherQuestion.addCompletionActivity(chapQuizQuestion12CompletionActivity);
			TestAction ActionChQ12A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, unseededOtherQuestion, attemptQ12A1, chapQuizQuestion12CompletionActivity);
			secondRequest.addTestAction(ActionChQ12A1);
			
			//Student 1 Assessment performance for Chapter quiz
			QuizCompletionActivity chQuizCompletionActivity = new QuizCompletionActivity(student1);
			chQuizCompletionActivity.addQuestionPerf(standardRadioButtonQuestion.getId(), /*pointsEarned*/ standardRadioButtonQuestion.getPointsPossible());
			chQuizCompletionActivity.addQuestionPerf(seededOtherQuestion.getId(), /*pointsEarned*/ seededOtherQuestion.getPointsPossible());
			chQuizCompletionActivity.addQuestionPerf(unseededOtherQuestion.getId(), /*pointsEarned*/ unseededOtherQuestion.getPointsPossible());
			chquiz.addCompletionActivity(chQuizCompletionActivity);
			TestAction completeChQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chquiz, chQuizCompletionActivity);
			secondRequest.addTestAction(completeChQuizTestAction);
			
			secondRequest.executeAllActions();
			
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
						
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
			
			//seeded "Other" Question attempt 1, correct, final	
			MultiValueAttempt attemptQ11Stud2A1 = getMultiValueAttemptComplete(seededOtherQuestion, student2, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ seededOtherQuestion.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			seededOtherQuestion.addAttempt(attemptQ11Stud2A1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + seededOtherQuestion.getId() + " : 60 seconds");
			
			//seeded "Other" Question Completion Activity
			QuestionCompletionActivity chapQuizQuestion11Stud2CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ seededOtherQuestion.getPointsPossible());
			seededOtherQuestion.addCompletionActivity(chapQuizQuestion11Stud2CompletionActivity);
			TestAction actionChQ11Stud2A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, seededOtherQuestion, attemptQ11Stud2A1, chapQuizQuestion11Stud2CompletionActivity);
			thirdRequest.addTestAction(actionChQ11Stud2A1);
			
			//unseeded "Other" Question attempt 1, correct, final	
			MultiValueAttempt attemptQ12Stud2A1 = getMultiValueAttemptComplete(unseededOtherQuestion, student2, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
					/*pointsEarned*/ unseededOtherQuestion.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
			unseededOtherQuestion.addAttempt(attemptQ12Stud2A1);
			timeOnTaskOutput.append("\n...assessment time : " + student2.getPersonId() + " : " + unseededOtherQuestion.getId() + " : 60 seconds");
			
			//unseeded "Other" Question Completion Activity
			QuestionCompletionActivity chapQuizQuestion12Stud2CompletionActivity = new QuestionCompletionActivity(student2, /*score*/ unseededOtherQuestion.getPointsPossible());
			unseededOtherQuestion.addCompletionActivity(chapQuizQuestion12Stud2CompletionActivity);
			TestAction actionChQ12Stud2A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, unseededOtherQuestion, attemptQ12Stud2A1, chapQuizQuestion12Stud2CompletionActivity);
			thirdRequest.addTestAction(actionChQ12Stud2A1);
			
			// Assessment performance for Chapter quiz
			QuizCompletionActivity S2ChQuizCompletionActivity = new QuizCompletionActivity(student2);
			S2ChQuizCompletionActivity.addQuestionPerf(standardRadioButtonQuestion.getId(), /*pointsEarned*/ standardRadioButtonQuestion.getPointsPossible()-1);
			S2ChQuizCompletionActivity.addQuestionPerf(seededOtherQuestion.getId(), /*pointsEarned*/ seededOtherQuestion.getPointsPossible());
			S2ChQuizCompletionActivity.addQuestionPerf(unseededOtherQuestion.getId(), /*pointsEarned*/ unseededOtherQuestion.getPointsPossible());
			chquiz.addCompletionActivity(S2ChQuizCompletionActivity);
			TestAction S2completeChQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chquiz, S2ChQuizCompletionActivity);
			thirdRequest.addTestAction(S2completeChQuizTestAction);

			thirdRequest.executeAllActions();
			
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
