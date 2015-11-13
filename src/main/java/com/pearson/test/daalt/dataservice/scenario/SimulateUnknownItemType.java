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

/*
 * student completes assignment containing two assessments (chapter quiz & chapter section quiz)
 * when publishing the assessment content, for the first question in the chapter quiz,
 * Assessment_Item_Possible_Answers message will not be sent
 */

public class SimulateUnknownItemType extends BaseTestScenario {

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
			testData = new BasicTestData("SimulateUnknownItemType");

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

			// We need to enroll the instructor to the course, or we will get
			// 401 when the instructor make the call.
			// add action to enroll instructor
			TestAction enrollInstr = testActionFactory.getEnrollInstructorTestAction(instr, courseSection);
			firstRequest.addTestAction(enrollInstr);

			// enroll student 1
			courseSection.addStudent(student);
			// add action to enroll student
			TestAction enrollStudent1 = testActionFactory.getEnrollStudentTestAction(student, courseSection);
			firstRequest.addTestAction(enrollStudent1);

			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			assignmentToAdd = assignmentFactory
					.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_SECTION_QUIZ_WITHOUT_POSSIBLE_ANSWERS);
			System.out.println(assignmentToAdd.getStructure());
			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			assignmentToAdd.setDueDate(nowCal.getTime());
			assignmentToAdd.setTitle("DAALT SQE Test Assignment");
			// add action to add assignment to course section
			courseSection.addAssignment(assignmentToAdd);

			Quiz chapQuiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			// select the first question to which the student would not have a
			// AssessmentItemPossibelAnswers will not get populated
			Question question = chapQuiz.getQuestions().get(0);
			TestAction addAssignment = testActionFactory.getAlternateAddAssignment(instr, courseSection, assignmentToAdd, question);
			firstRequest.addTestAction(addAssignment);
			firstRequest.executeAllActions();
			
			question.setIsQuestionSeeded(false);
			question.setQuestionLastSeedDateTime(null);

			// Student - completes Chapter Quiz Questions correctly

			int timeSpent = 30;
			for (Question ques : chapQuiz.getQuestions()) {
				MultiValueAttempt attempt = getMultiValueAttemptComplete(ques, student, timeSpent, /* attemptNumber */1,
																		/* pointsEarned */ques.getPointsPossible(),/* isFinalAttempt */true,CorrectOrIncorrectLibrary.CORRECTLY);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques.getId() + " : " + timeSpent + " seconds");

				// Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student,
						ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection,
						assignmentToAdd, chapQuiz, ques, attempt, questionCompletionActivity);
				secondRequest.addTestAction(attemptQuestionTestAction);
			}
			
			secondRequest.executeAllActions();

			// Student - Complete Quiz
			QuizCompletionActivity stud1ChapQuizCompletionActivity = new QuizCompletionActivity(student);
			for (Question ques : chapQuiz.getQuestions()) {
				stud1ChapQuizCompletionActivity.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			chapQuiz.addCompletionActivity(stud1ChapQuizCompletionActivity);
			TestAction studCompleteChapQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection,
					assignmentToAdd, chapQuiz, stud1ChapQuizCompletionActivity);
			thirdRequest.addTestAction(studCompleteChapQuizTestAction);
			thirdRequest.executeAllActions();
			
			// Section Quiz
			timeSpent = 30;
			Quiz sequiz = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			for(Question ques : sequiz.getQuestions()){
				MultiValueAttempt attempt = getMultiValueAttemptComplete(ques, student, timeSpent, /* attemptNumber */1,
							/* pointsPossible */ques.getPointsPossible(),/* isFinalAttempt */true,CorrectOrIncorrectLibrary.CORRECTLY);
				ques.addAttempt(attempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + ques.getId() + " : " + timeSpent + " seconds");
				
				//Question Completion Activity
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, ques.getPointsPossible());
				ques.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, sequiz, ques, attempt, questionCompletionActivity);
				fourthRequest.addTestAction(attemptQuestionTestAction);
							
			}
					
			QuizCompletionActivity quizCompletionActivity2 = new QuizCompletionActivity(student);
			for(Question ques : sequiz.getQuestions()){
				quizCompletionActivity2.addQuestionPerf(ques.getId(), ques.getPointsPossible());
			}
			sequiz.addCompletionActivity(quizCompletionActivity2);
			TestAction completeChQuizTestAction2 = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, sequiz, quizCompletionActivity2);
			fourthRequest.addTestAction(completeChQuizTestAction2);
			
			fourthRequest.executeAllActions();
						
		} catch (Exception e) {
			getEngine().getSuite().setDidCreationTestsComplete(false);
			throw e;
		}

		// pass TestData to validation engine
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
