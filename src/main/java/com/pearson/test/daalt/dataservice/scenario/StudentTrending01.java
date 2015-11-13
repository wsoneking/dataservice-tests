package com.pearson.test.daalt.dataservice.scenario;

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
import com.pearson.test.daalt.dataservice.model.CompletionActivityOriginCode;
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
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;

import com.pearson.test.daalt.dataservice.validation.SectionToModuleToStudentsValidationEngine;

import com.pearson.test.daalt.dataservice.validation.SectionToStudentsValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;
		/**
		 * The courseSection contains 8 assignment, each assignment has only one chapter quiz, which has 3 questions. And each question.pointsPossible = 10;
		 * Student Leaves the Assignment with the index=4 and the System AC it. 
		 * Assignment_Index		earnedPoints		previousAverage		Trending
		 * 		0					3					--					3
		 * 		1					6					3					3
		 * 		2					9					4.5					4.5
		 * 		3					12					6					6
		 * 		4					0 (AC)				7.5					-7.5
		 * 		5					18					6					12
		 * 		6					21					9					12
		 * 		7					24					12					12
		 * 
		 * 
		 */
public class StudentTrending01 extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Instructor instructor;
		Student student;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			firstRequest = new BasicTestDataRequest();
			testData = new BasicTestData("StudentTrending01");
			
			// add instructor
			com.pearson.test.daalt.dataservice.User instrFromConfig = getEngine().getInstructor();
			instructor = new BasicInstructor(instrFromConfig.getUserName(), 
					instrFromConfig.getPassword(), 
					instrFromConfig.getId(),
					instrFromConfig.getFirstName(),
					instrFromConfig.getLastName());
			testData.addInstructor(instructor);
			
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
			courseSection.setInstructor(instructor);
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
			TestAction enrollInstr = testActionFactory.getEnrollInstructorTestAction(instructor, courseSection);
			firstRequest.addTestAction(enrollInstr);
			
			//enroll student 1
			courseSection.addStudent(student);
			//add action to enroll student
			TestAction enrollStudent1 = testActionFactory.getEnrollStudentTestAction(student, courseSection);
			firstRequest.addTestAction(enrollStudent1);	
			
			firstRequest.executeAllActions();
			
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			
			// create 8 assignment in this courseSection. 
			Quiz chapQuiz5 = null;
			int timeSpent = 30;
			for (int i=0; i<8; i++) {
				TestDataRequest request = new BasicTestDataRequest();
				
				float pointEarnEachQuestion = 1+i;
				Assignment assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_QUIZ_ONLY);
				System.out.println(assignmentToAdd.getStructure());
				Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				nowCal.add(Calendar.DAY_OF_YEAR, (10+i));
				assignmentToAdd.setDueDate(nowCal.getTime());
				assignmentToAdd.setTitle("DAALT SQE Test Assignment");
				courseSection.addAssignment(assignmentToAdd);
				//add action to add assignment to course section
				TestAction addAssignment = testActionFactory.getAddAssignmentTestAction(instructor, courseSection, assignmentToAdd);
				request.addTestAction(addAssignment);
			
				Quiz chapQuiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
				timeSpent = 30;
				if (i != 4) {
					for(Question question : chapQuiz.getQuestions()){
						Answer answer = question.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
						MultiValueAttempt attempt 
						= getMultiValueRadioButtonAttempt(question, student, timeSpent, /*attemptNumber*/ 1, 
								answer, /*pointsPossible*/ pointEarnEachQuestion, /*isFinalAttempt*/ true);
						question.addAttempt(attempt);
						timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + question.getId() + " : " + timeSpent + " seconds");
						
						//Question Completion Activity
						QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, pointEarnEachQuestion);
						question.addCompletionActivity(questionCompletionActivity);
						TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, question, attempt, questionCompletionActivity);
						request.addTestAction(attemptQuestionTestAction);
				
					}
					
					//Student 1 - Complete Quiz
					QuizCompletionActivity stud1ChapQuizCompletionActivity = new QuizCompletionActivity(student);
					for(Question question : chapQuiz.getQuestions()){
						stud1ChapQuizCompletionActivity.addQuestionPerf(question.getId(), pointEarnEachQuestion);
					}
					chapQuiz.addCompletionActivity(stud1ChapQuizCompletionActivity);
					TestAction stud1CompleteChapQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, stud1ChapQuizCompletionActivity);
					request.addTestAction(stud1CompleteChapQuizTestAction);
				} else {
					chapQuiz5 = assignmentToAdd.getChapters().get(0).getChapterQuiz();
				}
				
				assignmentToAdd.setDueDatePassed(true);
				TestAction passDueDateTestAction = testActionFactory.getSimulateDueDatePassingTestAction(instructor, courseSection, assignmentToAdd);
				request.addTestAction(passDueDateTestAction);
				request.executeAllActions();
			}
			
			//System completes the Assignment 5
			User system = new BasicStudent(null, null, student.getPersonId(), null, null);
			system.setPersonRole(CompletionActivityOriginCode.SYSTEM.value);
			QuizCompletionActivity chapQuizCompletionActivity = new QuizCompletionActivity(system);
			for(Question question : chapQuiz5.getQuestions()) {
				chapQuizCompletionActivity.addQuestionPerf(question.getId(), /*pointsEarned*/ 0f);
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(system, 0f);
				question.addCompletionActivity(questionCompletionActivity);
			}
			chapQuiz5.addCompletionActivity(chapQuizCompletionActivity);
		} catch (Exception e) {
			getEngine().getSuite().setDidCreationTestsComplete(false);
			throw e;
		}
		
//		DaaltDataServiceValidationEngine validationEngine = new DaaltDataServiceValidationEngine();
//		List<Validation> validationList = validationEngine.getValidationsForTestData(testData);
		
		//1.12 validation engine
		SectionToModuleToStudentsValidationEngine trendingValidationEngine = new SectionToModuleToStudentsValidationEngine();
		List<Validation> validationList = trendingValidationEngine.getValidations(testData);
		
		//2.1 validation engine
		SectionToStudentsValidationEngine trendingValidationEngine1 = new SectionToStudentsValidationEngine();
		validationList.addAll(trendingValidationEngine1.getValidations(testData));
		
		for (Validation validation : validationList) {
			getCurrentTestCase().getValidations().add(validation);
			if (getEngine().isPrintExpectedOutput()) {
				System.out.println(validation.getExpectedResultsPrintString());
			}
		}
		
		if (getEngine().isPrintToT()) {
			timeOnTaskOutput.append("\n");
			System.out.println(timeOnTaskOutput.toString());
		}
	}
}
