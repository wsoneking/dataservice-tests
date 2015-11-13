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
import com.pearson.test.daalt.dataservice.model.LeaveQuestionActivity;
import com.pearson.test.daalt.dataservice.model.MultipleChoiceLeaveQuestion;
import com.pearson.test.daalt.dataservice.model.Product;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;

/* one student enrolled in the course section
* course section contains two assignments
* 		first assignment contains an assessment with a "Journal" question
* 		second assignment contains an assessment with a "SharedWriting" question
* student Leaves the first question and the System completes the assignment*/

public class StudentLeavesWritingQuestions extends BaseTestScenario {

	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
		
		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignmentJournalAdd;
		Assignment assignmentSimpleWritingAdd;
		Instructor instr;
		Student student;

		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			testData = new BasicTestData("StudentLeavesWritingQuestions");

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

			// create course section
			course = new BasicCourse();
			courseSection = new BasicCourseSection();
			courseSection.setInstructor(instr);
			// create Book
			Product book = new BasicProduct();
			courseSection.addBook(book);
			course.addCourseSection(courseSection);
			testData.addCourse(course);
			// add action to create course section
			TestAction createCourseSection = testActionFactory.getCreateCourseSectionTestAction(courseSection);
			firstRequest.addTestAction(createCourseSection);
			TestAction enrollInstr = testActionFactory.getEnrollInstructorTestAction(instr, courseSection);
			firstRequest.addTestAction(enrollInstr);

			// enroll student
			courseSection.addStudent(student);
			// add action to enroll student
			TestAction enrollStudent = testActionFactory.getEnrollStudentTestAction(student, courseSection);
			firstRequest.addTestAction(enrollStudent);

			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			assignmentJournalAdd = assignmentFactory
					.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.ONE_QUIZ_WITH_JOURNAL_WRITING_QUESTION);

			System.out.println(assignmentJournalAdd.getStructure());

			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone(TestEngine.timeZoneUTC));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			//assignmentJournalAdd.setSequenceNumber(0f);
			assignmentJournalAdd.setTitle("DAALT SQE Test Assignment");
			assignmentJournalAdd.setDueDate(nowCal.getTime());
			courseSection.addAssignment(assignmentJournalAdd);
			// add action to add assignment1 to course section
			TestAction addAssignment1 = testActionFactory.getAddAssignmentTestAction(instr, courseSection,
					assignmentJournalAdd);
			firstRequest.addTestAction(addAssignment1);

			assignmentSimpleWritingAdd = assignmentFactory
					.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.ONE_QUIZ_WITH_SHARED_WRITING_QUESTION);
			
			System.out.println(assignmentSimpleWritingAdd.getStructure());
			
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			//assignmentSimpleWritingAdd.setSequenceNumber(1f);
			assignmentSimpleWritingAdd.setTitle("DAALT SQE Test Assignment");
			assignmentSimpleWritingAdd.setDueDate(nowCal.getTime());
			courseSection.addAssignment(assignmentSimpleWritingAdd);
			// add action to add assignment to course section
			TestAction addAssignment2 = testActionFactory.getAddAssignmentTestAction(instr, courseSection,
					assignmentSimpleWritingAdd);
			firstRequest.addTestAction(addAssignment2);
		
			// BEGIN: Student leaves Assignment1 Simple Writing Journal
			// Assignment1 Chapter Quiz
			Quiz assignment1ChapQuiz = assignmentJournalAdd.getChapters().get(0).getChapterQuiz();
			Question assignment1ChapQuizQues = assignment1ChapQuiz.getQuestions().get(0);
			LeaveQuestionActivity leaveQues = new MultipleChoiceLeaveQuestion();
			leaveQues.setUser(student);
			leaveQues.setTimeSpent(40);
			assignment1ChapQuizQues.addLeaveQuestion(leaveQues);
			TestAction leaveChapQuizQuestionTestAction = testActionFactory.getLeaveQuestionTestAction(student, courseSection, assignment1ChapQuiz, assignment1ChapQuizQues, leaveQues, null);
			firstRequest.addTestAction(leaveChapQuizQuestionTestAction);
			firstRequest.executeAllActions();
			//System - Complete Chapter Quiz
			//System sets score to zero
			User system = new BasicStudent(null, null, student.getPersonId(), null, null);
			system.setPersonRole("System");
			
			
			// Assignment1 Question Completion Activity
			QuestionCompletionActivity assignment1ChapQuizQuestionCompletionActivity = new QuestionCompletionActivity(
					system, /*score*/ 0f);
			assignment1ChapQuizQues.addCompletionActivity(assignment1ChapQuizQuestionCompletionActivity);

			// Assignment1 Quiz Completion Activity
			QuizCompletionActivity assigment1ChapQuizCompletionActivity = new QuizCompletionActivity(system);
			assigment1ChapQuizCompletionActivity.addQuestionPerf(assignment1ChapQuizQues.getId(),/*score*/ 0f);
			assignment1ChapQuiz.addCompletionActivity(assigment1ChapQuizCompletionActivity);
			TestAction assignment1SimpleWritingCompleteQuizTestAction = testActionFactory.getSimpleWritingCompleteQuizTestAction
					(courseSection, assignmentJournalAdd, assignment1ChapQuizQues,
							assignment1ChapQuizQuestionCompletionActivity, assignment1ChapQuiz,
							assigment1ChapQuizCompletionActivity);
			secondRequest.addTestAction(assignment1SimpleWritingCompleteQuizTestAction);
			secondRequest.executeAllActions();

			// BEGIN: Student completes Assignment2 Simple Writing SharedWriting
			// Assignment2 Chapter Quiz
			Quiz assignment2ChapQuiz = assignmentSimpleWritingAdd.getChapters().get(0).getChapterQuiz();
			Question assignment2ChapQuizQues = assignment2ChapQuiz.getQuestions().get(0);

			// Assignment2 Question Completion Activity
			QuestionCompletionActivity assignment2ChapQuizQuestionCompletionActivity = new QuestionCompletionActivity(
					system, assignment2ChapQuizQues.getPointsPossible());
			assignment2ChapQuizQues.addCompletionActivity(assignment2ChapQuizQuestionCompletionActivity);

			// Assignment2 Quiz Completion Activity
			QuizCompletionActivity assigment2ChapQuizCompletionActivity = new QuizCompletionActivity(system);
			assigment2ChapQuizCompletionActivity.addQuestionPerf(assignment2ChapQuizQues.getId(),
					assignment2ChapQuizQues.getPointsPossible());
			assignment2ChapQuiz.addCompletionActivity(assigment2ChapQuizCompletionActivity);
			TestAction assignment2SharedWritingCompleteQuizTestAction = testActionFactory
					.getSimpleWritingCompleteQuizTestAction(courseSection, assignmentSimpleWritingAdd, assignment2ChapQuizQues,
							assignment2ChapQuizQuestionCompletionActivity, assignment2ChapQuiz,
							assigment2ChapQuizCompletionActivity);
			thirdRequest.addTestAction(assignment2SharedWritingCompleteQuizTestAction);

			thirdRequest.executeAllActions();
			
			assignmentJournalAdd.setDueDatePassed(true);
			TestAction passDueDateTestAction1 = testActionFactory.getSimulateDueDatePassingTestAction(instr, courseSection, assignmentJournalAdd);
			thirdRequest.addTestAction(passDueDateTestAction1);
			assignmentSimpleWritingAdd.setDueDatePassed(true);
			TestAction passDueDateTestAction2 = testActionFactory.getSimulateDueDatePassingTestAction(instr, courseSection, assignmentSimpleWritingAdd);
			thirdRequest.addTestAction(passDueDateTestAction2);
			
			thirdRequest.executeAllActions();
			
		} catch (Exception e) {
			getEngine().getSuite().setDidCreationTestsComplete(false);
			throw e;
		}

		// pass TestData to validation engine
		DaaltDataServiceValidationEngine validationEngine = new DaaltDataServiceValidationEngine();
		List<Validation> validationList = validationEngine.getValidationsForTestData(testData);
		for (Validation val : validationList) {
			System.out.println(val.getExpectedResultsPrintString());
			getCurrentTestCase().getValidations().add(val);
		}
	}
}
