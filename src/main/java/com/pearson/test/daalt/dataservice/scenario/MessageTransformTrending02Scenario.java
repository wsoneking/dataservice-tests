package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.List;

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

public class MessageTransformTrending02Scenario extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;
	private com.pearson.test.daalt.dataservice.request.action.version01.TestActionFactory testActionFactoryV1;

	@Test
	public void loadBasicTestData() throws Exception {
		testActionFactoryV1 = new com.pearson.test.daalt.dataservice.request.action.version01.SubPubSeerTestActionFactory();
		
		TestDataRequest zeroRequest;
		TestDataRequest firstRequest;
		TestDataRequest lastRequest;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Instructor instr;
		Student student;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			zeroRequest = new BasicTestDataRequest();
			firstRequest = new BasicTestDataRequest();
			lastRequest = new BasicTestDataRequest();
			testData = new BasicTestData("MessageTransformTrending02Scenario");
			
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
			TestAction createCourseSection = testActionFactoryV1.getCreateCourseSectionTestAction(courseSection);
			zeroRequest.addTestAction(createCourseSection);
			
			zeroRequest.executeAllActions();
			
			//sleep to ensure that Course_Section_Learning_Resource arrives before Learning_Module_Content
			Thread.sleep(10000);
		
			// We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
			//add action to enroll instructor
			TestAction enrollInstr = testActionFactoryV1.getEnrollInstructorTestAction(instr, courseSection);
			firstRequest.addTestAction(enrollInstr);
			
			//enroll student 1
			courseSection.addStudent(student);
			//add action to enroll student
			TestAction enrollStudent1 = testActionFactoryV1.getEnrollStudentTestAction(student, courseSection);
			firstRequest.addTestAction(enrollStudent1);	
			
			firstRequest.executeAllActions();			
			
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			
			// create 8 assignment in this courseSection. 
			int timeSpent = 30;
			Calendar nowCal = createDueDate();

			for (int i=0; i<8; i++) {
				TestDataRequest request = new BasicTestDataRequest();
				
				float pointEarnEachQuestion = 10 - i;
				Assignment assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_QUIZ_ONLY);
				assignmentToAdd.setAllTargetIdsTrue();
				
				System.out.println(assignmentToAdd.getStructure());
				
				nowCal.add(Calendar.DATE, 1);
				assignmentToAdd.setDueDate(nowCal.getTime());
				assignmentToAdd.setTitle("DAALT SQE Test Assignment");
				courseSection.addAssignment(assignmentToAdd);
				//add action to add assignment to course section
				TestAction addAssignment = testActionFactoryV1.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
				request.addTestAction(addAssignment);
			
				Quiz chapQuiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
				timeSpent = 30;
				if (i != 4) {
					for(Question ques : chapQuiz.getQuestions()){
						Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
						Attempt attempt 
						= getMultiValueRadioButtonAttempt(ques, student, timeSpent, /*attemptNumber*/ 1, 
								answer, /*pointsPossible*/ pointEarnEachQuestion, /*isFinalAttempt*/ true);
						ques.addAttempt(attempt);
						timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques.getId() + " : " + timeSpent + " seconds");
						
						//Question Completion Activity
						QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, pointEarnEachQuestion);
						ques.addCompletionActivity(questionCompletionActivity);
						TestAction attemptQuestionTestAction = testActionFactoryV1.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, ques, attempt);
						request.addTestAction(attemptQuestionTestAction);
				
					}
					
					//Student 1 - Complete Quiz
					QuizCompletionActivity stud1ChapQuizCompletionActivity = new QuizCompletionActivity(student);
					for(Question ques : chapQuiz.getQuestions()){
						stud1ChapQuizCompletionActivity.addQuestionPerf(ques.getId(), pointEarnEachQuestion);
					}
					chapQuiz.addCompletionActivity(stud1ChapQuizCompletionActivity);
					TestAction stud1CompleteChapQuizTestAction = testActionFactoryV1.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, stud1ChapQuizCompletionActivity);
					request.addTestAction(stud1CompleteChapQuizTestAction);

				}
				assignmentToAdd.setDueDatePassed(true);
				
				request.executeAllActions();
			}	
			
			for (Assignment assignment : courseSection.getAssignments()) {
				TestAction reSendSeedData = testActionFactory.getReSendSeedDataTestAction(courseSection, assignment);
				lastRequest.addTestAction(reSendSeedData);
			}

			lastRequest.executeAllActions();		
			
			
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
