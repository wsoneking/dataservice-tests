package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.AssignmentFactory;
import com.pearson.test.daalt.dataservice.model.AutoSaveActivity;
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
import com.pearson.test.daalt.dataservice.model.WritingSpaceQuestionAutoSave;
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;

/*
 * Student completes Chapter1 ChapterQuiz Writing Space Question (Draft 1)
 * Auto Save event for Chapter2 ChapterQuiz Writing Space Question (Draft 2) - Send TOT tincan
 * Student completes Chapter2 ChapterQuiz Writing Space Question (Draft 2)
 */

public class WritingSpaceAutoSaveScenario extends BaseTestScenario {
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
		Student student;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("WritingSpaceAutoSaveScenario");
			
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
			assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.TWO_QUIZZES_WITH_WRITING_SPACE_QUESTION);
			
			System.out.println(assignmentToAdd.getStructure());
			
			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone(TestEngine.timeZoneUTC));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			assignmentToAdd.setTitle("DAALT SQE Test Assignment");
			assignmentToAdd.setDueDate(nowCal.getTime());
			courseSection.addAssignment(assignmentToAdd);
			//add action to add assignment to course section
			TestAction addAssignment = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
			firstRequest.addTestAction(addAssignment);
			
			//execute first request
			firstRequest.executeAllActions();

			//BEGIN: Student completes Writing Space - Draft 1
			//Chapter1 Quiz
			Quiz chap1Quiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			Question chap1QuizQues = chap1Quiz.getQuestions().get(0);
			
			//Chapter1Quiz Question Completion Activity
			int timeSpent = 60;
			QuestionCompletionActivity chap1QuizQuestionCompletionActivity = new QuestionCompletionActivity(student, 0f);
			chap1QuizQuestionCompletionActivity.setTimeOnQuestion(timeSpent);
			chap1QuizQues.addCompletionActivity(chap1QuizQuestionCompletionActivity);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + chap1QuizQues.getId() + " : " + timeSpent + " seconds");
			
			//Chapter1 Quiz Completion Activity
			QuizCompletionActivity chap1QuizCompletionActivity = new QuizCompletionActivity(student);
			chap1QuizCompletionActivity.addQuestionPerf(chap1QuizQues.getId(), 0f);
			chap1Quiz.addCompletionActivity(chap1QuizCompletionActivity);
			TestAction chap1WritingSpaceCompleteQuizTestAction = testActionFactory.getWritingSpaceCompleteQuizTestAction(courseSection, assignmentToAdd, chap1QuizQues, chap1QuizQuestionCompletionActivity, chap1Quiz, chap1QuizCompletionActivity);
			secondRequest.addTestAction(chap1WritingSpaceCompleteQuizTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " +" last activity date: "+chap1QuizQuestionCompletionActivity.getLastActivityDate());
			//execute second request
			secondRequest.executeAllActions();

			//BEGIN: Student completes Writing Space - Draft 2
			//Chapter2 Quiz
			Quiz chap2Quiz = assignmentToAdd.getChapters().get(1).getChapterQuiz();
			Question chap2QuizQues = chap2Quiz.getQuestions().get(0);
			
			//Chapter2Quiz AutoSaveActivity
			timeSpent = 30;
			AutoSaveActivity chap2AutoSaveActivity = new WritingSpaceQuestionAutoSave();
			chap2AutoSaveActivity.setUser(student);
			chap2AutoSaveActivity.setTimeSpent(timeSpent);
			chap2QuizQues.addAutoSaveActivity(chap2AutoSaveActivity);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + chap2QuizQues.getId() + " : " + timeSpent + " seconds");
			//add AutoSaveTestAction
			TestAction chap2QuizAutoSaveTestAction = testActionFactory.getLeaveQuestionTestAction(student, courseSection, chap2Quiz, chap2QuizQues, null, chap2AutoSaveActivity);
			thirdRequest.addTestAction(chap2QuizAutoSaveTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " +" last activity date: "+chap2AutoSaveActivity.getLastActivityDate());
			
			//execute third request
			thirdRequest.executeAllActions();
			
			//Chapter2Quiz Question Completion Activity
			timeSpent = 20;
			QuestionCompletionActivity chap2QuizQuestionCompletionActivity = new QuestionCompletionActivity(student, chap2QuizQues.getPointsPossible());
			chap2QuizQuestionCompletionActivity.setTimeOnQuestion(timeSpent);
			chap2QuizQues.addCompletionActivity(chap2QuizQuestionCompletionActivity);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + chap2QuizQues.getId() + " : " + timeSpent + " seconds");
			
			//Chapter2 Quiz Completion Activity
			QuizCompletionActivity chap2QuizCompletionActivity = new QuizCompletionActivity(student);
			chap2QuizCompletionActivity.addQuestionPerf(chap2QuizQues.getId(), chap2QuizQues.getPointsPossible());
			chap2Quiz.addCompletionActivity(chap2QuizCompletionActivity);
			TestAction chap2WritingSpaceCompleteQuizTestAction = testActionFactory.getWritingSpaceCompleteQuizTestAction(courseSection, assignmentToAdd, chap2QuizQues, chap2QuizQuestionCompletionActivity, chap2Quiz, chap2QuizCompletionActivity);
			fourthRequest.addTestAction(chap2WritingSpaceCompleteQuizTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " +" last activity date: "+chap2QuizQuestionCompletionActivity.getLastActivityDate());
			//execute fourth request
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

