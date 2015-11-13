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
import com.pearson.test.daalt.dataservice.model.BasicQuiz;
import com.pearson.test.daalt.dataservice.model.BasicStudent;
import com.pearson.test.daalt.dataservice.model.BasicTestData;
import com.pearson.test.daalt.dataservice.model.Chapter;
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
import com.pearson.test.daalt.dataservice.model.SubQuestion;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;

/*
 * LR belongs to both a book and a different collection.
 * a) Two course sections with two different books.
 * b) Both books have the same chapter with a chapter quiz.
 * c) Same student enrolled in both course section.
 * d) Student completes both assignments
 */

public class TwoDifferentBooksWithSameLearningResource extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
		TestDataRequest fourthRequest;
		TestData testData;
		Course course;
		CourseSection courseSection1;
		CourseSection courseSection2;
		Assignment assignmentToAdd;
		Instructor instr;
		Student student;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("TwoDifferentBooksWithSameLearningResource");
			
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
			
			//create course section 1
			course = new BasicCourse();
			courseSection1 = new BasicCourseSection();
			courseSection1.setInstructor(instr);
			//create Book
			Product courseSection1book = new BasicProduct();	
			courseSection1.addBook(courseSection1book);
			course.addCourseSection(courseSection1);
			testData.addCourse(course);
			//add action to create course section
			TestAction createCourseSection1 = testActionFactory.getCreateCourseSectionTestAction(courseSection1);
			firstRequest.addTestAction(createCourseSection1);
		
			// We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
			//add action to enroll instructor
			TestAction enrollInstrCourseSection1 = testActionFactory.getEnrollInstructorTestAction(instr, courseSection1);
			firstRequest.addTestAction(enrollInstrCourseSection1);
			
			//enroll student
			courseSection1.addStudent(student);
			//add action to enroll student
			TestAction enrollStudentCourseSection1 = testActionFactory.getEnrollStudentTestAction(student, courseSection1);
			firstRequest.addTestAction(enrollStudentCourseSection1);	
			
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
			assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_QUIZ_ONLY);
			
			System.out.println(assignmentToAdd.getStructure());
			
			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			assignmentToAdd.setDueDate(nowCal.getTime());
			assignmentToAdd.setTitle("DAALT SQE Test Assignment");
			courseSection1.addAssignment(assignmentToAdd);
			//add action to add assignment to course section
			TestAction addAssignmentCourseSection1 = testActionFactory.getAddAssignmentTestAction(instr, courseSection1, assignmentToAdd);
			firstRequest.addTestAction(addAssignmentCourseSection1);
			
			firstRequest.executeAllActions();	
			
			//create course section 2
			courseSection2 = new BasicCourseSection();
			courseSection2.setInstructor(instr);
			//create Book
			Product courseSection2book = new BasicProduct();	
			courseSection2.addBook(courseSection2book);
			course.addCourseSection(courseSection2);
			//add action to create course section 2
			TestAction createCourseSection2 = testActionFactory.getCreateCourseSectionTestAction(courseSection2);
			secondRequest.addTestAction(createCourseSection2);
		
			// We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
			//add action to enroll instructor
			TestAction enrollInstrCourseSection2 = testActionFactory.getEnrollInstructorTestAction(instr, courseSection2);
			secondRequest.addTestAction(enrollInstrCourseSection2);
			
			//enroll student
			courseSection2.addStudent(student);
			//add action to enroll student
			TestAction enrollStudentCourseSection2 = testActionFactory.getEnrollStudentTestAction(student, courseSection2);
			secondRequest.addTestAction(enrollStudentCourseSection2);	
			
		    Assignment assignmentToAdd2 = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_QUIZ_ONLY);
		    
		    //copy ids from Chapter in Assignment1 to Chapter in Assignment2 (same ids, different object)
		    Chapter chapterFromAssignment1 = assignmentToAdd.getChapters().get(0);
		    Chapter chapterFromAssignment2 = assignmentToAdd2.getChapters().get(0);
		    chapterFromAssignment2.setId(chapterFromAssignment1.getId());
		    
		    Quiz chapterQuizFromAssignment1 = chapterFromAssignment1.getChapterQuiz();
		    Quiz chapterQuizFromAssignment2 = chapterFromAssignment2.getChapterQuiz();
		    
		    chapterQuizFromAssignment2.setId(chapterQuizFromAssignment1.getId());
		    chapterQuizFromAssignment2.setLearningResourceId(chapterQuizFromAssignment1.getLearningResourceId());
		    chapterQuizFromAssignment2.setSeedDateTime(chapterQuizFromAssignment1.getAssessmentLastSeedDateTime());
		    
		    List<Question> questionList = chapterQuizFromAssignment1.getQuestions();
		    
		    for (int i=0; i<questionList.size(); i++) {
		    	Question questionFromAssginment1 = questionList.get(i);
		    	Question questionFromAssginment2 = chapterQuizFromAssignment2.directAccessQuestionList().get(i);
		    	questionFromAssginment2.setId(questionFromAssginment1.getId());
		    	questionFromAssginment2.setQuestionLastSeedDateTime(questionFromAssginment1.getQuestionLastSeedDateTime());
		    	questionFromAssginment2.setSequenceNumber(questionFromAssginment1.getSequenceNumber());
		    	questionFromAssginment2.setText(questionFromAssginment1.getText());
		    	for (int j=0; j<questionFromAssginment2.directAccessSubQuestionList().size(); j++) {
		    		SubQuestion subQuestionFromAssginment1 = questionFromAssginment1.directAccessSubQuestionList().get(j);
		    		SubQuestion subQuestionFromAssginment2 = questionFromAssginment2.directAccessSubQuestionList().get(j);
		    		subQuestionFromAssginment2.setId(subQuestionFromAssginment1.getId());
		    		subQuestionFromAssginment2.setText(subQuestionFromAssginment1.getText());
		    		for (int k=0; k<subQuestionFromAssginment2.getAnswers().size(); k++) {
			    		Answer answerFromAssginment1 = subQuestionFromAssginment1.getAnswers().get(k);
			    		Answer answerFromAssginment2 = subQuestionFromAssginment2.getAnswers().get(k);
			    		answerFromAssginment2.setId(answerFromAssginment1.getId());
			    		answerFromAssginment2.setText(answerFromAssginment1.getText());
			    	}
		    	}
		    }
			
			System.out.println(assignmentToAdd2.getStructure());

			nowCal.add(Calendar.DAY_OF_YEAR, 1);
			assignmentToAdd2.setDueDate(nowCal.getTime());
			assignmentToAdd2.setTitle("DAALT SQE Test Assignment");
			courseSection2.addAssignment(assignmentToAdd2);
			//add action to add assignment to course section
			TestAction addAssignmentCourseSection2 = testActionFactory.getAddAssignmentTestAction(instr, courseSection2, assignmentToAdd2);
			secondRequest.addTestAction(addAssignmentCourseSection2);
			
			secondRequest.executeAllActions();
	
			//Course Section 1 - complete Chapter Quiz
			int timeSpent = 60;
			Quiz chquiz1 = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			for(Question ques : chquiz1.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student, timeSpent, /*attemptNumber*/ 1, 
						answer, /*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques.getId() + " : " + timeSpent + " seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				//add action to courseSection1
				TestAction attemptQuestionCourseSection1TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection1, assignmentToAdd, chquiz1, ques, attempt, questionCompletionActivity);
				thirdRequest.addTestAction(attemptQuestionCourseSection1TestAction);
			}

			//Complete Quiz
			QuizCompletionActivity quizCompletionActivity = new QuizCompletionActivity(student);
			for(Question ques : chquiz1.getQuestions()){
				quizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chquiz1.addCompletionActivity(quizCompletionActivity);
			//add action to courseSection1
			TestAction completeChQuizCourseSection1TestAction = testActionFactory.getCompleteQuizTestAction(courseSection1, assignmentToAdd, chquiz1, quizCompletionActivity);
			thirdRequest.addTestAction(completeChQuizCourseSection1TestAction);
			
			thirdRequest.executeAllActions();
			
			// Chapter Quiz
			timeSpent = 60;
			Quiz chquiz2 = assignmentToAdd2.getChapters().get(0).getChapterQuiz();
			for(Question ques : chquiz2.getQuestions()){
				Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(ques, student, timeSpent, /*attemptNumber*/ 1, 
						answer, /*pointsPossible*/ ques.getPointsPossible(), /*isFinalAttempt*/ true);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques.getId() + " : " + timeSpent + " seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				//add action to courseSection2
				TestAction attemptQuestionCourseSection2TestAction = testActionFactory.getAttemptQuestionTestAction(courseSection2, assignmentToAdd2, chquiz2, ques, attempt, questionCompletionActivity);
				fourthRequest.addTestAction(attemptQuestionCourseSection2TestAction);
			}

			//Complete Quiz
			QuizCompletionActivity quizCompletionActivity2 = new QuizCompletionActivity(student);
			for(Question ques : chquiz2.getQuestions()){
				quizCompletionActivity2.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chquiz2.addCompletionActivity(quizCompletionActivity2);
			//add action to courseSection2
			TestAction completeChQuizCourseSection2TestAction = testActionFactory.getCompleteQuizTestAction(courseSection2, assignmentToAdd2, chquiz2, quizCompletionActivity2);
			fourthRequest.addTestAction(completeChQuizCourseSection2TestAction);
			
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


