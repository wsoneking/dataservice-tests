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
import com.pearson.test.daalt.dataservice.model.Course;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.MultiValueAnswer;
import com.pearson.test.daalt.dataservice.model.MultiValueAttempt;
import com.pearson.test.daalt.dataservice.model.MultiValueMultipleHotSpotQuestion;
import com.pearson.test.daalt.dataservice.model.Product;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.model.SubQuestion;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;

/*
 * Instructor adds an assignment to course section (send publication messages, Learning_Module messages, Learning_Module_Content messages)
 * Assignment contains five multi-value questions.
 * Student begin answering questions in the assignment (send tincans, Assessment_Item_Completion messages, Assessment_Performance messages)
 * Modify the assessment items as follows...
 * question 1 - add a possible answer
 * question 2 - change possible points
 * question 3 - remove a target (sub-question)
 * question 4 - change correct answer
 * question 5 - remove a possible answer
 * Re-send Learning_Module_Content, Assessment and Assessment_Item_Possible_Answers messages for all questions.
 */

public class ItemModificationAfterStudentInteraction extends BaseTestScenario {
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
		Student student;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			testData = new BasicTestData("ItemModificationAfterStudentInteraction");
			
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
			/*
			 * Using an AssignmentFactory.
			 * First implementation returns from a pre-defined selection of Assignments identified by Enum.
			 * Later, we can discuss possible ways of improving on the factory design.
			 * 
			 * Two other factories that would be useful: 
			 * 		Product (aka Book) - defines learning resource relationships
			 * 		AssignmentPerformance - defines the precise interactions between a student and an Assignment 
			 */
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
			TestAction addAssignment = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
			firstRequest.addTestAction(addAssignment);
			
			firstRequest.executeAllActions();	

			Quiz chquiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			for(Question ques : chquiz.getQuestions()){
				
				MultiValueAttempt attempt = getMultiValueAttemptComplete(ques, student, /*timeSpent*/ 60, /*attemptNumber*/ 1, 
						/*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true, CorrectOrIncorrectLibrary.CORRECTLY);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques.getId() + " : 60 seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chquiz, ques, attempt, questionCompletionActivity);
				secondRequest.addTestAction(attemptQuestionTestAction);
				
			}
			
			//Complete Quiz - Quiz Completion Activity
			QuizCompletionActivity quizCompletionActivity = new QuizCompletionActivity(student);
			for(Question ques : chquiz.getQuestions()){
				quizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chquiz.addCompletionActivity(quizCompletionActivity);
			TestAction completeChQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chquiz, quizCompletionActivity);
			secondRequest.addTestAction(completeChQuizTestAction);
			
			secondRequest.executeAllActions();
			
			//Add a possible answer to each sub-question of question 1
			Question chQues1 = chquiz.getQuestions().get(0);
			SubQuestion chQues1SubQuestion1 = chQues1.getSubQuestions().get(0);
			Answer chQues1SubQuestion1Answer = new MultiValueAnswer(" - Question 01 - Sub1 - New Answer");
			chQues1SubQuestion1.addAnswer(chQues1SubQuestion1Answer);
			SubQuestion chQues1SubQuestion2 = chQues1.getSubQuestions().get(1);
			Answer chQues1SubQuestion2Answer = new MultiValueAnswer(" - Question 01 - Sub2 - New Answer");
			chQues1SubQuestion2Answer.setId(chQues1SubQuestion1Answer.getId());
			chQues1SubQuestion2.addAnswer(chQues1SubQuestion2Answer);
		
			//Change possible points of question 2
			Question chQues3 = chquiz.getQuestions().get(1);
			chQues3.setPointsPossible(4f);
				
			//Remove a target sub-question of question 3
			Question chQues4 = chquiz.getQuestions().get(2);
			MultiValueMultipleHotSpotQuestion multipleHotSpotQuestion = (MultiValueMultipleHotSpotQuestion) chQues4;
			multipleHotSpotQuestion.removeSubQuestion(multipleHotSpotQuestion.getSubQuestions().get(0));
			
			//Change correct answer of question 4
			Question chQues5 = chquiz.getQuestions().get(3);
			SubQuestion chQues5SubQuestion1 = chQues5.getSubQuestions().get(0);
			chQues5SubQuestion1.getAnswers().get(0).setCorrectAnswer(false);
			chQues5SubQuestion1.getAnswers().get(1).setCorrectAnswer(true);
			chQues5SubQuestion1.getAnswers().get(2).setCorrectAnswer(true);
			chQues5SubQuestion1.getAnswers().get(3).setCorrectAnswer(false);
			
			//Remove a possible answer of question 5
			Question chQues6 = chquiz.getQuestions().get(4);
			chQues6.getSubQuestions().get(0).getAnswers().remove(0);
			
			//Re-send Assessment_Item_Possible_Answers messages for all questions.
			TestAction assessmentItemPossibleAnswersTestAction = testActionFactory.getModifyAssessmentItemTestAction(chquiz);
			thirdRequest.addTestAction(assessmentItemPossibleAnswersTestAction);
			
			System.out.println(assignmentToAdd.getStructure());
			
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


