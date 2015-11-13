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

/*
 * Student completes Assignment1 ChapterQuiz Writing Space Question (Draft 1)
 * Student completes Assignment2 ChapterQuiz Writing Space Question (Draft 2)
 */

public class WritingSpaceDifferentAssignments extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignmentToAdd1;
		Assignment assignmentToAdd2;
		Instructor instr;
		Student student;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			testData = new BasicTestData("WritingSpaceDifferentAssignments");
			
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
			
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			assignmentToAdd1 = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.ONE_QUIZ_WITH_WRITING_SPACE_QUESTION);
			String assignment1QuestionId = assignmentToAdd1.getChapters().get(0).getChapterQuiz().getQuestions().get(0).getId();
			String assignment1QuestionLastSeedDateTime = assignmentToAdd1.getChapters().get(0).getChapterQuiz().getQuestions().get(0).getQuestionLastSeedDateTime();
			System.out.println(assignmentToAdd1.getStructure());
			
			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone(TestEngine.timeZoneUTC));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			assignmentToAdd1.setTitle("DAALT SQE Test Assignment");
			assignmentToAdd1.setDueDate(nowCal.getTime());
			courseSection.addAssignment(assignmentToAdd1);
			//add action to add assignment1 to course section
			TestAction addAssignment1 = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd1);
			firstRequest.addTestAction(addAssignment1);
			
			assignmentToAdd2 = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.ONE_QUIZ_WITH_WRITING_SPACE_QUESTION);
			//set Assignment2 ChapterQuiz Question id to same as Assignment1 ChapterQuiz Question id
			assignmentToAdd2.getChapters().get(0).getChapterQuiz().getQuestions().get(0).setId(assignment1QuestionId);
			//set Assignment2 ChapterQuiz Question lastSeedDateTime to same as Assignment1 ChapterQuiz Question lastSeedDateTime
			assignmentToAdd2.getChapters().get(0).getChapterQuiz().getQuestions().get(0).setQuestionLastSeedDateTime(assignment1QuestionLastSeedDateTime);
			
			System.out.println(assignmentToAdd2.getStructure());
			
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			assignmentToAdd2.setTitle("DAALT SQE Test Assignment");
			assignmentToAdd2.setDueDate(nowCal.getTime());
			courseSection.addAssignment(assignmentToAdd2);
			//add action to add assignment to course section
			TestAction addAssignment2 = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd2);
			firstRequest.addTestAction(addAssignment2);

			firstRequest.executeAllActions();
			
			//BEGIN: Student completes Assignment1 Writing Space - Draft 1
			//Assignment1 Chapter Quiz
			Quiz assignment1ChapQuiz = assignmentToAdd1.getChapters().get(0).getChapterQuiz();
			Question assignment1ChapQuizQues = assignment1ChapQuiz.getQuestions().get(0);
			
			//Assignment1 Question Completion Activity
			int timeSpent = 40;
			QuestionCompletionActivity assignment1ChapQuizQuestionCompletionActivity = new QuestionCompletionActivity(student, 0f);
			assignment1ChapQuizQuestionCompletionActivity.setTimeOnQuestion(timeSpent);
			assignment1ChapQuizQues.addCompletionActivity(assignment1ChapQuizQuestionCompletionActivity);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + assignment1ChapQuizQues.getId() + " : " + timeSpent + " seconds");
			
			//Assignment1 Quiz Completion Activity
			QuizCompletionActivity assigment1ChapQuizCompletionActivity = new QuizCompletionActivity(student);
			assigment1ChapQuizCompletionActivity.addQuestionPerf(assignment1ChapQuizQues.getId(), 0f);
			assignment1ChapQuiz.addCompletionActivity(assigment1ChapQuizCompletionActivity);
			TestAction assignment1WritingSpaceCompleteQuizTestAction = testActionFactory.getWritingSpaceCompleteQuizTestAction(courseSection, assignmentToAdd1, assignment1ChapQuizQues, assignment1ChapQuizQuestionCompletionActivity, assignment1ChapQuiz, assigment1ChapQuizCompletionActivity);
			secondRequest.addTestAction(assignment1WritingSpaceCompleteQuizTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " +" last activity date: "+assignment1ChapQuizQuestionCompletionActivity.getLastActivityDate());
			secondRequest.executeAllActions();
			
			//BEGIN: Student completes Assignment2 Writing Space - Draft 2
			//Assignment2 Chapter Quiz
			Quiz assignment2ChapQuiz = assignmentToAdd2.getChapters().get(0).getChapterQuiz();
			Question assignment2ChapQuizQues = assignment2ChapQuiz.getQuestions().get(0);
			
			//Assignment2 Question Completion Activity
			timeSpent = 60;
			QuestionCompletionActivity assignment2ChapQuizQuestionCompletionActivity = new QuestionCompletionActivity(student, assignment2ChapQuizQues.getPointsPossible());
			assignment2ChapQuizQuestionCompletionActivity.setTimeOnQuestion(timeSpent);
			assignment2ChapQuizQues.addCompletionActivity(assignment2ChapQuizQuestionCompletionActivity);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + assignment2ChapQuizQues.getId() + " : " + timeSpent + " seconds");
			
			//Assignment2 Quiz Completion Activity
			QuizCompletionActivity assigment2ChapQuizCompletionActivity = new QuizCompletionActivity(student);
			assigment2ChapQuizCompletionActivity.addQuestionPerf(assignment2ChapQuizQues.getId(), assignment2ChapQuizQues.getPointsPossible());
			assignment2ChapQuiz.addCompletionActivity(assigment2ChapQuizCompletionActivity);
			TestAction assignment2WritingSpaceCompleteQuizTestAction = testActionFactory.getWritingSpaceCompleteQuizTestAction(courseSection, assignmentToAdd2, assignment2ChapQuizQues, assignment2ChapQuizQuestionCompletionActivity, assignment2ChapQuiz, assigment2ChapQuizCompletionActivity);
			thirdRequest.addTestAction(assignment2WritingSpaceCompleteQuizTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " +" last activity date: "+assignment2ChapQuizQuestionCompletionActivity.getLastActivityDate());
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
