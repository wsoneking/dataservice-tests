package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.AssignmentFactory;
import com.pearson.test.daalt.dataservice.model.AttemptResponseCode;
import com.pearson.test.daalt.dataservice.model.BasicCourse;
import com.pearson.test.daalt.dataservice.model.BasicCourseSection;
import com.pearson.test.daalt.dataservice.model.BasicInstructor;
import com.pearson.test.daalt.dataservice.model.BasicProduct;
import com.pearson.test.daalt.dataservice.model.BasicStudent;
import com.pearson.test.daalt.dataservice.model.BasicTestData;
import com.pearson.test.daalt.dataservice.model.Course;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.Product;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.model.UnknownFormatAttempt;
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;

/**
 * One chapter quiz contains two generic question (unknown type and unknown format)
 */
public class GenericQuestionScenario extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestDataRequest thirdRequest;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignmentToAdd;
		Instructor instr;
		Student student;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			firstRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			testData = new BasicTestData("GenericQuestionScenario");
			
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
			student = new BasicStudent(student01FromConfig.getUserName(), 
					student01FromConfig.getPassword(), 
					student01FromConfig.getId(),
					student01FromConfig.getFirstName(),
					student01FromConfig.getLastName());
			testData.addStudent(student);
			
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
			
			//enroll student
			courseSection.addStudent(student);
			//add action to enroll student
			TestAction enrollStudent = testActionFactory.getEnrollStudentTestAction(student, courseSection);
			firstRequest.addTestAction(enrollStudent);	
			
			//add assignment to course section
			
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_QUIZ_ONLY_GENERIC);
			
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

			// Chapter Quiz
			Quiz chquiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			
			Question ques1 = chquiz.getQuestions().get(0);
			UnknownFormatAttempt attempt = new UnknownFormatAttempt();
			attempt.setUser(student);
			attempt.setTimeSpent(5);
			attempt.setAttemptNumber(1);
			attempt.setPointsEarned(ques1.getPointsPossible());
			attempt.setAnswerCorrectness(AttemptResponseCode.CORRECT);
			attempt.setFinalAttempt(true);
			
			ques1.addAttempt(attempt);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques1.getId() + " : " + 5 + " seconds");
			
			//Question Completion Activity
			QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, ques1.getPointsPossible());
			ques1.addCompletionActivity(questionCompletionActivity);
			TestAction attemptQuestionTestAction = testActionFactory.getAttemptUnknownFormatQuestionTestAction(courseSection, assignmentToAdd, chquiz, ques1, attempt, questionCompletionActivity);
			thirdRequest.addTestAction(attemptQuestionTestAction);
			
			
			Question ques2 = chquiz.getQuestions().get(1);
			UnknownFormatAttempt attempt21 = new UnknownFormatAttempt();
			attempt21.setUser(student);
			attempt21.setTimeSpent(11);
			attempt21.setAttemptNumber(1);
			attempt21.setPointsEarned(0);
			attempt21.setAnswerCorrectness(AttemptResponseCode.INCORRECT);
			attempt21.setFinalAttempt(false);
			ques2.addAttempt(attempt21);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques2.getId() + " : " + 11 + " seconds");
			TestAction attemptQuestionTestAction21 = testActionFactory.getAttemptUnknownFormatQuestionTestAction(courseSection, assignmentToAdd, chquiz, ques2, attempt21, null);
			thirdRequest.addTestAction(attemptQuestionTestAction21);
			
			UnknownFormatAttempt attempt22 = new UnknownFormatAttempt();
			attempt22.setUser(student);
			attempt22.setTimeSpent(20);
			attempt22.setAttemptNumber(2);
			attempt22.setPointsEarned(ques2.getPointsPossible() - 1);
			attempt22.setAnswerCorrectness(AttemptResponseCode.CORRECT);
			attempt22.setFinalAttempt(true);
			ques2.addAttempt(attempt22);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques2.getId() + " : " + 20 + " seconds");
			
			//Question Completion Activity
			QuestionCompletionActivity questionCompletionActivity2 = new QuestionCompletionActivity(student, ques2.getPointsPossible()-1);
			ques2.addCompletionActivity(questionCompletionActivity2);
			TestAction attemptQuestionTestAction22 = testActionFactory.getAttemptUnknownFormatQuestionTestAction(courseSection, assignmentToAdd, chquiz, ques2, attempt22, questionCompletionActivity2);
			thirdRequest.addTestAction(attemptQuestionTestAction22);
			
			

			Question ques3 = chquiz.getQuestions().get(2);
			UnknownFormatAttempt attempt31 = new UnknownFormatAttempt();
			attempt31.setUser(student);
			attempt31.setTimeSpent(40);
			attempt31.setAttemptNumber(1);
			attempt31.setPointsEarned(0);
			attempt31.setAnswerCorrectness(AttemptResponseCode.INCORRECT);
			attempt31.setFinalAttempt(true);
			ques3.addAttempt(attempt31);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques3.getId() + " : " + 40 + " seconds");
			
			//Question Completion Activity
			QuestionCompletionActivity questionCompletionActivity3 = new QuestionCompletionActivity(student, 0);
			ques3.addCompletionActivity(questionCompletionActivity3);
			TestAction attemptQuestionTestAction31 = testActionFactory.getAttemptUnknownFormatQuestionTestAction(courseSection, assignmentToAdd, chquiz, ques3, attempt31, questionCompletionActivity3);
			thirdRequest.addTestAction(attemptQuestionTestAction31);
			

			//Complete Quiz
			QuizCompletionActivity quizCompletionActivity = new QuizCompletionActivity(student);
			quizCompletionActivity.addQuestionPerf(ques1.getId(), ques1.getPointsEarnedFinal(student));
			quizCompletionActivity.addQuestionPerf(ques2.getId(), ques2.getPointsEarnedFinal(student));
			quizCompletionActivity.addQuestionPerf(ques3.getId(), ques3.getPointsEarnedFinal(student));
			chquiz.addCompletionActivity(quizCompletionActivity);
			TestAction completeChQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chquiz, quizCompletionActivity);
			thirdRequest.addTestAction(completeChQuizTestAction);
			
			
			
			
			assignmentToAdd.setDueDatePassed(true);
			TestAction passDueDateTestAction = testActionFactory.getSimulateDueDatePassingTestAction(instr, courseSection, assignmentToAdd);
			thirdRequest.addTestAction(passDueDateTestAction);
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

