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
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.SectionToModuleToStudentsValidationEngine;
import com.pearson.test.daalt.dataservice.validation.SectionToStudentToModulesValidationEngine;
import com.pearson.test.daalt.dataservice.validation.SectionToStudentsValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;
		/**
		 * The courseSection contains 8 assignment, each assignment has only one chapter quiz, which has 3 questions. And each question.pointsPossible = 10;
		 * 
		 * Assignment_Index		earnedPoints		previousAverage		Trending
		 * 		0					30					--					30
		 * 		1					27					30					-3
		 * 		2					24					28.5				-4.5
		 * 		3					21					27					-6.0 
		 * 		4					--					25.5				null
		 * 		5					15					25.5				-10.5
		 * 		6					12					23.4				-11.4
		 * 		7					9					19.8				-10.8
		 * 
		 * 
		 */
public class StudentTrending02 extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;
	

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Instructor instr;
		Student student;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			firstRequest = new BasicTestDataRequest();
			testData = new BasicTestData("StudentTrending02");
			
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
			
			//enroll student 1
			courseSection.addStudent(student);
			//add action to enroll student
			TestAction enrollStudent1 = testActionFactory.getEnrollStudentTestAction(student, courseSection);
			firstRequest.addTestAction(enrollStudent1);	
			
			firstRequest.executeAllActions();
			
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			
			// create 6 assignment in this courseSection.
			int timeSpent = 20;
		
			for (int i=0; i<8; i++) {
				TestDataRequest request = new BasicTestDataRequest();
				
				float pointEarnEachQuestion = 10-i;
				Assignment assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_QUIZ_ONLY);
				System.out.println(assignmentToAdd.getStructure());
				Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				nowCal.add(Calendar.DAY_OF_YEAR, (10+i));
				assignmentToAdd.setDueDate(nowCal.getTime());
				assignmentToAdd.setTitle("DAALT SQE Test Assignment");
				courseSection.addAssignment(assignmentToAdd);
				//add action to add assignment to course section
				TestAction addAssignment = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
				request.addTestAction(addAssignment);
				
				if (i!= 4) {
					Quiz chapQuiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
					for (Question ques : chapQuiz.getQuestions()) {
						timeSpent = 20;
						Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
						MultiValueAttempt attempt 
						= getMultiValueRadioButtonAttempt(ques, student, timeSpent, /*attemptNumber*/ 1, 
								answer, /*pointsPossible*/ pointEarnEachQuestion, /*isFinalAttempt*/ true);
						ques.addAttempt(attempt);
						timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques.getId() + " : " + timeSpent + " seconds");
						//Question Completion Activity
						QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, pointEarnEachQuestion);
						ques.addCompletionActivity(questionCompletionActivity);
						TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, ques, attempt, questionCompletionActivity);
						request.addTestAction(attemptQuestionTestAction);
					}
					
					//Student 1 - Complete Quiz
					QuizCompletionActivity stud1ChapQuizCompletionActivity = new QuizCompletionActivity(student);
					for (Question ques : chapQuiz.getQuestions()){
						stud1ChapQuizCompletionActivity.addQuestionPerf(ques.getId(), pointEarnEachQuestion);
					}
					chapQuiz.addCompletionActivity(stud1ChapQuizCompletionActivity);
					TestAction stud1CompleteChapQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, stud1ChapQuizCompletionActivity);
					request.addTestAction(stud1CompleteChapQuizTestAction);
					assignmentToAdd.setDueDatePassed(true);
					TestAction passDueDateTestAction = testActionFactory.getSimulateDueDatePassingTestAction(instr, courseSection, assignmentToAdd);
					request.addTestAction(passDueDateTestAction);
					
				} else {
					assignmentToAdd.setDueDatePassed(true);
					TestAction studentSkippingAnAssignmentTestAction = testActionFactory.getStudentSkippingAnAssignmentTestAction(instr, courseSection, assignmentToAdd);
					request.addTestAction(studentSkippingAnAssignmentTestAction);
				}
				request.executeAllActions();
			}
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
		
		//2.3 validation engine
		SectionToStudentToModulesValidationEngine moduleValidationEngine = new SectionToStudentToModulesValidationEngine();
		validationList.addAll(moduleValidationEngine.getValidations(testData));
		
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

