package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.User;
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
import com.pearson.test.daalt.dataservice.model.Course;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.MultiValueAttempt;
import com.pearson.test.daalt.dataservice.model.Page;
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

/**
 * two course sections use the same book
 * the book has a set of learning resources
 * 
 * the instructor arranges the learning resources into
 * one hierarchy for first course section
 * and a different hierarchy for second course section
 * 
 * one student enrolled in both course sections
 * student does reading on all pages
 * and answers every question correctly on the first attempt
 */
public class CourseSectionSpecificTableOfContents extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
		TestDataRequest fourthRequest;
		TestDataRequest fifthRequest;
		TestDataRequest sixthRequest;
		TestDataRequest seventhRequest;
		TestDataRequest eighthRequest;
		TestDataRequest ninthRequest;
		TestDataRequest tenthRequest;
		TestDataRequest eleventhRequest;
		TestDataRequest twelfthRequest;
		TestDataRequest thirteenthRequest;
		TestDataRequest fourteenthRequest;
		TestData testData;
		Course course;
		CourseSection courseSection01;
		Assignment assignment01;
		CourseSection courseSection02;
		Assignment assignment02;
		Instructor instr;
		Student student;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			fifthRequest = new BasicTestDataRequest();
			sixthRequest = new BasicTestDataRequest();
			seventhRequest = new BasicTestDataRequest();
			eighthRequest = new BasicTestDataRequest();
			ninthRequest = new BasicTestDataRequest();
			tenthRequest = new BasicTestDataRequest();
			eleventhRequest = new BasicTestDataRequest();
			twelfthRequest = new BasicTestDataRequest();
			thirteenthRequest = new BasicTestDataRequest();
			fourteenthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("CourseSectionSpecificTableOfContents");
			
			//add instructor
			User instrFromConfig = getEngine().getInstructor();
			instr = new BasicInstructor(instrFromConfig.getUserName(), 
					instrFromConfig.getPassword(), 
					instrFromConfig.getId(),
					instrFromConfig.getFirstName(),
					instrFromConfig.getLastName());
			testData.addInstructor(instr);
			
			//add student
			User student01FromConfig = getEngine().getStudent01();
			student = new BasicStudent(student01FromConfig.getUserName(), 
					student01FromConfig.getPassword(), 
					student01FromConfig.getId(),
					student01FromConfig.getFirstName(),
					student01FromConfig.getLastName());
			testData.addStudent(student);
			
			//create Book
			Product book = new BasicProduct();
			
			//create Course
			course = new BasicCourse();
			testData.addCourse(course);
			
			//create first course section
			courseSection01 = new BasicCourseSection();
			courseSection01.setInstructor(instr);
			courseSection01.addBook(book);
			courseSection01.setIndependentLearningResourceHierarchy(true);
			course.addCourseSection(courseSection01);
			//add action to create first course section
			TestAction createCourseSection01 = testActionFactory.getCreateCourseSectionTestAction(courseSection01);
			firstRequest.addTestAction(createCourseSection01);
		
			// We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
			//add action to enroll instructor in first course section
			TestAction enrollInstr01 = testActionFactory.getEnrollInstructorTestAction(instr, courseSection01);
			firstRequest.addTestAction(enrollInstr01);
			
			//enroll student
			courseSection01.addStudent(student);
			//add action to enroll student in first course section
			TestAction enrollStudent01 = testActionFactory.getEnrollStudentTestAction(student, courseSection01);
			firstRequest.addTestAction(enrollStudent01);	
			
			//create second course section
			courseSection02 = new BasicCourseSection();
			courseSection02.setInstructor(instr);
			courseSection02.addBook(book);
			courseSection02.setIndependentLearningResourceHierarchy(true);
			course.addCourseSection(courseSection02);
			//add action to create second course section
			TestAction createCourseSection02 = testActionFactory.getCreateCourseSectionTestAction(courseSection02);
			firstRequest.addTestAction(createCourseSection02);
		
			// We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
			//add action to enroll instructor in second course section
			TestAction enrollInstr02 = testActionFactory.getEnrollInstructorTestAction(instr, courseSection02);
			firstRequest.addTestAction(enrollInstr02);
			
			//enroll student
			courseSection02.addStudent(student);
			//add action to enroll student in second course section
			TestAction enrollStudent02 = testActionFactory.getEnrollStudentTestAction(student, courseSection02);
			firstRequest.addTestAction(enrollStudent02);	
			
			//fetch assignments from AssignmentFactory
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			List<Assignment> twoAssignments = assignmentFactory.getTwoAssignmentsWithSharedLearningResources(AssignmentFactory.AssignmentLibrary.THREE_CHAPTERS);
			assignment01 = twoAssignments.get(0);
			assignment02 = twoAssignments.get(1);
			
			System.out.println(assignment01.getStructure());
			System.out.println(assignment02.getStructure());
			
			//add first assignment to first course section
			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			assignment01.setDueDate(nowCal.getTime());
			assignment01.setTitle("DAALT SQE Test Assignment 01");
			courseSection01.addAssignment(assignment01);
			//add action to add assignment to course section
			TestAction addAssignment01 = testActionFactory.getAddAssignmentTestAction(instr, courseSection01, assignment01);
			firstRequest.addTestAction(addAssignment01);
			
			//add second assignment to second course section
			nowCal.add(Calendar.DAY_OF_YEAR, 12);
			assignment02.setDueDate(nowCal.getTime());
			assignment02.setTitle("DAALT SQE Test Assignment 02");
			courseSection02.addAssignment(assignment02);
			//add action to add assignment to course section
			TestAction addAssignment02 = testActionFactory.getAddAssignmentTestAction(instr, courseSection02, assignment02);
			firstRequest.addTestAction(addAssignment02);
			
			firstRequest.executeAllActions();
			
			int timeSpent = 10;
			
			//BEGIN : first assignment
			//student completes chapter01.assessment01 : chapters.get(0).chapterQuiz
			Quiz assign01Assessment01 = assignment01.getChapters().get(0).getChapterQuiz();
			for (Question question : assign01Assessment01.getQuestions()) {
				Answer correctAnswer = question.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt finalAttempt 
					= getMultiValueRadioButtonAttempt(question, student, /*timeSpent*/ timeSpent, /*attemptNumber*/ 1, 
						correctAnswer, /*pointsEarned*/ question.getPointsPossible(), /*isFinalAttempt*/ true);
				question.addAttempt(finalAttempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + question.getId() + " : " + timeSpent + " seconds");
				
				QuestionCompletionActivity questionCompletionActivity 
					= new QuestionCompletionActivity(student, /*pointsEarned*/ question.getPointsPossible());
				question.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection01, assignment01, assign01Assessment01, question, finalAttempt, questionCompletionActivity);
				secondRequest.addTestAction(attemptQuestionTestAction);
				timeSpent += 2;
			}
			QuizCompletionActivity quizCompletionActivity = new QuizCompletionActivity(student);
			for (Question question : assign01Assessment01.getQuestions()) {
				quizCompletionActivity.addQuestionPerf(question.getId(), /*pointsEarned*/ question.getPointsPossible());
			}
			assign01Assessment01.addCompletionActivity(quizCompletionActivity);
			TestAction completeAssessment01TestAction = testActionFactory.getCompleteQuizTestAction(courseSection01, assignment01, assign01Assessment01, quizCompletionActivity);
			secondRequest.addTestAction(completeAssessment01TestAction);
			
			secondRequest.executeAllActions();
			
			//student reads chapter01.chapterSection01.page01 : chapters.get(0).chapterSections.get(0).pages.get(0)
			timeSpent = 100;
			LearningActivity learningActivity = new LearningActivity(student, timeSpent);
			Page assign01Page01 = assignment01.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			assign01Page01.addLearningActivity(learningActivity);
			TestAction readPage01TestAction = testActionFactory.getReadPageTestAction(courseSection01, assign01Page01, learningActivity);
			thirdRequest.addTestAction(readPage01TestAction);
			timeOnTaskOutput.append("\n...learning time : " + student.getPersonId() + " : " + assign01Page01.getLearningResourceId() + " : " + timeSpent + " seconds");
			thirdRequest.executeAllActions();
			
			//student completes chapter01.chapterSection01.assessment02 : chapters.get(0).chapterSections.get(0).chapterSectionQuiz
			timeSpent = 8;
			Quiz assign01Assessment02 = assignment01.getChapters().get(0).getChapterSections().get(0).getChapterSectionQuiz();
			for (Question question : assign01Assessment02.getQuestions()) {
				Answer correctAnswer = question.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt finalAttempt 
					= getMultiValueRadioButtonAttempt(question, student, /*timeSpent*/ timeSpent, /*attemptNumber*/ 1, 
						correctAnswer, /*pointsEarned*/ question.getPointsPossible(), /*isFinalAttempt*/ true);
				question.addAttempt(finalAttempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + question.getId() + " : " + timeSpent + " seconds");
				
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, /*pointsEarned*/ question.getPointsPossible());
				question.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection01, assignment01, assign01Assessment02, question, finalAttempt, questionCompletionActivity);
				fourthRequest.addTestAction(attemptQuestionTestAction);
				timeSpent += 2;
			}
			QuizCompletionActivity quizCompletionActivity02 = new QuizCompletionActivity(student);
			for (Question question : assign01Assessment02.getQuestions()){
				quizCompletionActivity02.addQuestionPerf(question.getId(), /*pointsEarned*/ question.getPointsPossible());
			}
			assign01Assessment02.addCompletionActivity(quizCompletionActivity02);
			TestAction completeAssessment02TestAction2 = testActionFactory.getCompleteQuizTestAction(courseSection01, assignment01, assign01Assessment02, quizCompletionActivity02);
			fourthRequest.addTestAction(completeAssessment02TestAction2);

			fourthRequest.executeAllActions();
			
			//student completes chapter02.assessment03 : chapters.get(1).chapterQuiz
			Quiz assign01Assessment03 = assignment01.getChapters().get(1).getChapterQuiz();
			for (Question question : assign01Assessment03.getQuestions()) {
				Answer correctAnswer = question.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt finalAttempt 
					= getMultiValueRadioButtonAttempt(question, student, /*timeSpent*/ timeSpent, /*attemptNumber*/ 1, 
						correctAnswer, /*pointsEarned*/ question.getPointsPossible(), /*isFinalAttempt*/ true);
				question.addAttempt(finalAttempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + question.getId() + " : " + timeSpent + " seconds");
				
				QuestionCompletionActivity questionCompletionActivity 
					= new QuestionCompletionActivity(student, /*pointsEarned*/ question.getPointsPossible());
				question.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection01, assignment01, assign01Assessment03, question, finalAttempt, questionCompletionActivity);
				fifthRequest.addTestAction(attemptQuestionTestAction);
				timeSpent += 2;
			}
			QuizCompletionActivity quizCompletionActivity03 = new QuizCompletionActivity(student);
			for (Question question : assign01Assessment03.getQuestions()) {
				quizCompletionActivity03.addQuestionPerf(question.getId(), /*pointsEarned*/ question.getPointsPossible());
			}
			assign01Assessment03.addCompletionActivity(quizCompletionActivity03);
			TestAction completeAssessment03TestAction = testActionFactory.getCompleteQuizTestAction(courseSection01, assignment01, assign01Assessment03, quizCompletionActivity03);
			fifthRequest.addTestAction(completeAssessment03TestAction);
			
			fifthRequest.executeAllActions();
			
			//student reads chapter03.chapterSection02.page02 : chapters.get(2).chapterSections.get(0).pages.get(0)
			timeSpent = 110;
			LearningActivity learningActivity2 = new LearningActivity(student, timeSpent);
			Page assign01Page02 = assignment01.getChapters().get(2).getChapterSections().get(0).getPages().get(0);
			assign01Page02.addLearningActivity(learningActivity2);
			TestAction readPage02TestAction = testActionFactory.getReadPageTestAction(courseSection01, assign01Page02, learningActivity2);
			sixthRequest.addTestAction(readPage02TestAction);
			timeOnTaskOutput.append("\n...learning time : " + student.getPersonId() + " : " + assign01Page02.getLearningResourceId() + " : " + timeSpent + " seconds");
			sixthRequest.executeAllActions();
			
			//student reads chapter03.chapterSection02.page03 : chapters.get(2).chapterSections.get(0).pages.get(1)
			timeSpent = 120;
			LearningActivity learningActivity3 = new LearningActivity(student, timeSpent);
			Page assign01Page03 = assignment01.getChapters().get(2).getChapterSections().get(0).getPages().get(1);
			assign01Page03.addLearningActivity(learningActivity3);
			TestAction readPage03TestAction = testActionFactory.getReadPageTestAction(courseSection01, assign01Page03, learningActivity3);
			seventhRequest.addTestAction(readPage03TestAction);
			timeOnTaskOutput.append("\n...learning time : " + student.getPersonId() + " : " + assign01Page03.getLearningResourceId() + " : " + timeSpent + " seconds");
			seventhRequest.executeAllActions();
			
			//student completes chapter03.chapterSection02.page03.embeddedQuestion : chapters.get(2).chapterSections.get(0).pages.get(1).embeddedQuestions.get(0) 
			timeSpent = 40;
			Question assign01EmbeddedQuestion = assignment01.getChapters().get(2).getChapterSections().get(0).getPages().get(1).getEmbeddedQuestions().get(0);
			Quiz assign01WrappingQuiz = new BasicQuiz();
			assign01WrappingQuiz.setId(assign01EmbeddedQuestion.getAssessmentId());
			assign01WrappingQuiz.setSeedDateTime(assign01EmbeddedQuestion.getQuestionLastSeedDateTime());
			assign01WrappingQuiz.addQuestion(assign01EmbeddedQuestion);
			
			Answer embeddedCorrectAnswer = assign01EmbeddedQuestion.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);			
			MultiValueAttempt embeddedFinalAttempt 
				= getMultiValueRadioButtonAttempt(assign01EmbeddedQuestion, student, timeSpent, /*attemptNumber*/ 1, 
						embeddedCorrectAnswer, /*pointsEarned*/ assign01EmbeddedQuestion.getPointsPossible(), /*isFinalAttempt*/ true);
			assign01EmbeddedQuestion.addAttempt(embeddedFinalAttempt);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + assign01EmbeddedQuestion.getId() + " : " + timeSpent + " seconds");
			
			QuestionCompletionActivity embeddedQuestionCompletionActivity = new QuestionCompletionActivity(student, /*pointsEarned*/ assign01EmbeddedQuestion.getPointsPossible());
			assign01EmbeddedQuestion.addCompletionActivity(embeddedQuestionCompletionActivity);
			TestAction attemptEmbeddedQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection01, assignment01, assign01WrappingQuiz, assign01EmbeddedQuestion, embeddedFinalAttempt, embeddedQuestionCompletionActivity);
			eighthRequest.addTestAction(attemptEmbeddedQuestionTestAction);
			
			QuizCompletionActivity quizCompletionActivity04 = new QuizCompletionActivity(student);
			quizCompletionActivity04.addQuestionPerf(assign01EmbeddedQuestion.getId(), /*pointsEarned*/ assign01EmbeddedQuestion.getPointsPossible());
			assign01WrappingQuiz.addCompletionActivity(quizCompletionActivity04);
			TestAction completeWrappingQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection01, assignment01, assign01WrappingQuiz, quizCompletionActivity04);
			eighthRequest.addTestAction(completeWrappingQuizTestAction);
			
			eighthRequest.executeAllActions();
			//END : first assignment
			

			//BEGIN : second assignment
			//student reads chapter01.chapterSection02.page02 : chapters.get(0).chapterSections.get(0).pages.get(0)
			timeSpent = 103;
			LearningActivity learningActivity4 = new LearningActivity(student, timeSpent);
			Page assign02Page02 = assignment02.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			assign02Page02.addLearningActivity(learningActivity4);
			TestAction readPage02TestAction02 = testActionFactory.getReadPageTestAction(courseSection02, assign02Page02, learningActivity4);
			ninthRequest.addTestAction(readPage02TestAction02);
			timeOnTaskOutput.append("\n...learning time : " + student.getPersonId() + " : " + assign02Page02.getLearningResourceId() + " : " + timeSpent + " seconds");
			ninthRequest.executeAllActions();
			
			//student completes chapter01.chapterSection02.page02.embeddedQuestion : chapters.get(0).chapterSections.get(0).pages.get(0).embeddedQuestions.get(0)			
			timeSpent = 55;
			Question assign02EmbeddedQuestion = assignment02.getChapters().get(0).getChapterSections().get(0).getPages().get(0).getEmbeddedQuestions().get(0);
			Quiz assign02WrappingQuiz = new BasicQuiz();
			assign02WrappingQuiz.setId(assign02EmbeddedQuestion.getAssessmentId());
			assign02WrappingQuiz.setSeedDateTime(assign02EmbeddedQuestion.getQuestionLastSeedDateTime());
			assign02WrappingQuiz.addQuestion(assign02EmbeddedQuestion);
			
			Answer embeddedCorrectAnswer2 = assign02EmbeddedQuestion.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);			
			MultiValueAttempt embeddedFinalAttempt2 
				= getMultiValueRadioButtonAttempt(assign02EmbeddedQuestion, student, timeSpent, /*attemptNumber*/ 1, 
						embeddedCorrectAnswer2, /*pointsEarned*/ assign02EmbeddedQuestion.getPointsPossible(), /*isFinalAttempt*/ true);
			assign02EmbeddedQuestion.addAttempt(embeddedFinalAttempt2);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + assign02EmbeddedQuestion.getId() + " : " + timeSpent + " seconds");
			
			QuestionCompletionActivity embeddedQuestionCompletionActivity2 = new QuestionCompletionActivity(student, /*pointsEarned*/ assign02EmbeddedQuestion.getPointsPossible());
			assign02EmbeddedQuestion.addCompletionActivity(embeddedQuestionCompletionActivity2);
			TestAction attemptEmbeddedQuestionTestAction2 = testActionFactory.getAttemptQuestionTestAction(courseSection02, assignment02, assign02WrappingQuiz, assign02EmbeddedQuestion, embeddedFinalAttempt2, embeddedQuestionCompletionActivity2);
			tenthRequest.addTestAction(attemptEmbeddedQuestionTestAction2);
			
			QuizCompletionActivity quizCompletionActivity05 = new QuizCompletionActivity(student);
			quizCompletionActivity05.addQuestionPerf(assign02EmbeddedQuestion.getId(), /*pointsEarned*/ assign02EmbeddedQuestion.getPointsPossible());
			assign02WrappingQuiz.addCompletionActivity(quizCompletionActivity05);
			TestAction completeWrappingQuizTestAction2 = testActionFactory.getCompleteQuizTestAction(courseSection02, assignment02, assign02WrappingQuiz, quizCompletionActivity05);
			tenthRequest.addTestAction(completeWrappingQuizTestAction2);
			
			tenthRequest.executeAllActions();
			
			//student completes chapter02.assessment03 : chapters.get(1).chapterQuiz
			Quiz assign02Assessment03 = assignment02.getChapters().get(1).getChapterQuiz();
			for (Question question : assign02Assessment03.getQuestions()) {
				Answer correctAnswer = question.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt finalAttempt 
					= getMultiValueRadioButtonAttempt(question, student, /*timeSpent*/ timeSpent, /*attemptNumber*/ 1, 
						correctAnswer, /*pointsEarned*/ question.getPointsPossible(), /*isFinalAttempt*/ true);
				question.addAttempt(finalAttempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + question.getId() + " : " + timeSpent + " seconds");
				
				QuestionCompletionActivity questionCompletionActivity 
					= new QuestionCompletionActivity(student, /*pointsEarned*/ question.getPointsPossible());
				question.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection02, assignment02, assign02Assessment03, question, finalAttempt, questionCompletionActivity);
				eleventhRequest.addTestAction(attemptQuestionTestAction);
				timeSpent += 2;
			}
			QuizCompletionActivity quizCompletionActivity06 = new QuizCompletionActivity(student);
			for (Question question : assign02Assessment03.getQuestions()) {
				quizCompletionActivity06.addQuestionPerf(question.getId(), /*pointsEarned*/ question.getPointsPossible());
			}
			assign02Assessment03.addCompletionActivity(quizCompletionActivity06);
			TestAction completeAssessment03TestAction2 = testActionFactory.getCompleteQuizTestAction(courseSection02, assignment02, assign02Assessment03, quizCompletionActivity06);
			eleventhRequest.addTestAction(completeAssessment03TestAction2);
			
			eleventhRequest.executeAllActions();
			
			//student completes chapter03.assessment01 : chapters.get(2).chapterQuiz
			Quiz assign02Assessment01 = assignment02.getChapters().get(2).getChapterQuiz();
			for (Question question : assign02Assessment01.getQuestions()) {
				Answer correctAnswer = question.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt finalAttempt 
					= getMultiValueRadioButtonAttempt(question, student, /*timeSpent*/ timeSpent, /*attemptNumber*/ 1, 
						correctAnswer, /*pointsEarned*/ question.getPointsPossible(), /*isFinalAttempt*/ true);
				question.addAttempt(finalAttempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + question.getId() + " : " + timeSpent + " seconds");
				
				QuestionCompletionActivity questionCompletionActivity 
					= new QuestionCompletionActivity(student, /*pointsEarned*/ question.getPointsPossible());
				question.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection02, assignment02, assign02Assessment01, question, finalAttempt, questionCompletionActivity);
				twelfthRequest.addTestAction(attemptQuestionTestAction);
				timeSpent += 2;
			}
			QuizCompletionActivity quizCompletionActivity07 = new QuizCompletionActivity(student);
			for (Question question : assign02Assessment01.getQuestions()) {
				quizCompletionActivity07.addQuestionPerf(question.getId(), /*pointsEarned*/ question.getPointsPossible());
			}
			assign02Assessment01.addCompletionActivity(quizCompletionActivity07);
			TestAction completeAssessment01TestAction2 = testActionFactory.getCompleteQuizTestAction(courseSection02, assignment02, assign02Assessment01, quizCompletionActivity07);
			twelfthRequest.addTestAction(completeAssessment01TestAction2);
			
			twelfthRequest.executeAllActions();
			
			//student reads chapter03.chapterSection03.page01 : chapters.get(2).chapterSections.get(0).pages.get(0)
			timeSpent = 109;
			LearningActivity learningActivity5 = new LearningActivity(student, timeSpent);
			Page assign02Page01 = assignment02.getChapters().get(2).getChapterSections().get(0).getPages().get(0);
			assign02Page01.addLearningActivity(learningActivity5);
			TestAction readPage01TestAction02 = testActionFactory.getReadPageTestAction(courseSection02, assign02Page01, learningActivity5);
			thirteenthRequest.addTestAction(readPage01TestAction02);
			timeOnTaskOutput.append("\n...learning time : " + student.getPersonId() + " : " + assign02Page01.getLearningResourceId() + " : " + timeSpent + " seconds");
			thirteenthRequest.executeAllActions();
			
			//student completes chapter03.chapterSection03.assessment02 : chapters.get(2).chapterSections.get(0).chapterSectionQuiz
			timeSpent = 12;
			Quiz assign02Assessment02 = assignment02.getChapters().get(2).getChapterSections().get(0).getChapterSectionQuiz();
			for (Question question : assign02Assessment02.getQuestions()) {
				Answer correctAnswer = question.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
				MultiValueAttempt finalAttempt 
					= getMultiValueRadioButtonAttempt(question, student, /*timeSpent*/ timeSpent, /*attemptNumber*/ 1, 
						correctAnswer, /*pointsEarned*/ question.getPointsPossible(), /*isFinalAttempt*/ true);
				question.addAttempt(finalAttempt);
				timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + question.getId() + " : " + timeSpent + " seconds");
				
				QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student, /*pointsEarned*/ question.getPointsPossible());
				question.addCompletionActivity(questionCompletionActivity);
				TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection02, assignment02, assign02Assessment02, question, finalAttempt, questionCompletionActivity);
				fourteenthRequest.addTestAction(attemptQuestionTestAction);
				timeSpent += 2;
			}
			QuizCompletionActivity quizCompletionActivity08 = new QuizCompletionActivity(student);
			for (Question question : assign02Assessment02.getQuestions()){
				quizCompletionActivity08.addQuestionPerf(question.getId(), /*pointsEarned*/ question.getPointsPossible());
			}
			assign02Assessment02.addCompletionActivity(quizCompletionActivity08);
			TestAction completeAssessment02TestAction3 = testActionFactory.getCompleteQuizTestAction(courseSection02, assignment02, assign02Assessment02, quizCompletionActivity08);
			fourteenthRequest.addTestAction(completeAssessment02TestAction3);

			fourteenthRequest.executeAllActions();
			//END : second assignment
			
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
