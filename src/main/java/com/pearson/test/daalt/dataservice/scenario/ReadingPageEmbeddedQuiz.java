package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
		 * Chapter has chapter section, which has a reading page, which contains a quiz.
		 * Student 1 complete the quiz and reading for 1 min.
		 */
		
public class ReadingPageEmbeddedQuiz extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest request;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignmentToAdd;
		Instructor instr;
		Student student1;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");

		try {
			request = new BasicTestDataRequest();
			testData = new BasicTestData("ReadingPageEmbeddedQuiz");
			
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
			student1 = new BasicStudent(student01FromConfig.getUserName(), 
					student01FromConfig.getPassword(), 
					student01FromConfig.getId(),
					student01FromConfig.getFirstName(),
					student01FromConfig.getLastName());
			testData.addStudent(student1);
			
			
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
			request.addTestAction(createCourseSection);
		
			// We need to enroll the instructor to the course, or we will get 401 when the instructor make the call. 
			//add action to enroll instructor
			TestAction enrollInstr = testActionFactory.getEnrollInstructorTestAction(instr, courseSection);
			request.addTestAction(enrollInstr);
			
			//enroll student 1
			courseSection.addStudent(student1);
			//add action to enroll student
			TestAction enrollStudent1 = testActionFactory.getEnrollStudentTestAction(student1, courseSection);
			request.addTestAction(enrollStudent1);	
			
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.SECTION_READING_EMBEDDED_QUIZ);
			System.out.println(assignmentToAdd.getStructure());
			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			assignmentToAdd.setDueDate(nowCal.getTime());
			assignmentToAdd.setTitle("DAALT SQE Test Assignment");
			courseSection.addAssignment(assignmentToAdd);
			//add action to add assignment to course section
			TestAction addAssignment = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
			request.addTestAction(addAssignment);
	
			////---- Student 1
     		//Read Page
			int timeSpent = 10;
			LearningActivity learningActivity = new LearningActivity(student1, timeSpent);
			Page page = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			page.addLearningActivity(learningActivity);
			TestAction readPageTestAction = testActionFactory.getReadPageTestAction(courseSection, page, learningActivity);
			request.addTestAction(readPageTestAction);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page.getLearningResourceId() + " : " + timeSpent + " seconds");
			
			Map<String, Quiz> quizMap = new HashMap<>();
			
			//Embedded Question 1 attempt 1, correct, final
			timeSpent = 20;
			Question ques1 = page.getEmbeddedQuestions().get(0);
			Answer ques1CorrectAnswer = ques1.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);	
			MultiValueAttempt attemptQ1A1 
				= getMultiValueRadioButtonAttempt(ques1, student1, timeSpent, /*attemptNumber*/ 1, ques1CorrectAnswer, ques1.getPointsPossible(), /*isFinalAttempt*/ true);
			ques1.addAttempt(attemptQ1A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + ques1.getId() + " : " + timeSpent + " seconds");
			
			//Embedded Question 1 Completion Activity
			if(!quizMap.containsKey(ques1.getAssessmentId())) {
				Quiz newQuiz = new BasicQuiz();
				newQuiz.setId(ques1.getAssessmentId());
				newQuiz.setSeedDateTime(ques1.getQuestionLastSeedDateTime());
				quizMap.put(ques1.getAssessmentId(), newQuiz);
			}
			Quiz ques1WrappingQuiz = quizMap.get(ques1.getAssessmentId());
			if (!ques1WrappingQuiz.containsQuestionWithId(ques1.getId())) {
				ques1WrappingQuiz.addQuestion(ques1);
			}
			
			QuestionCompletionActivity question1CompletionActivity = new QuestionCompletionActivity(student1, ques1.getPointsPossible());
			ques1.addCompletionActivity(question1CompletionActivity);
			TestAction actionChQ1A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, ques1WrappingQuiz, ques1, attemptQ1A1, question1CompletionActivity);
			request.addTestAction(actionChQ1A1);

			//Embedded Question 2 attempt 1, correct, final 
			timeSpent = 60;
			Question ques2 = page.getEmbeddedQuestions().get(1);
			Answer ques2CorrectAnswer = ques2.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);	
			MultiValueAttempt attemptQ2A1 
				= getMultiValueRadioButtonAttempt(ques2, student1, timeSpent, /*attemptNumber*/ 1, ques2CorrectAnswer, ques2.getPointsPossible(), /*isFinalAttempt*/ true);
			ques2.addAttempt(attemptQ2A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + ques2.getId() + " : " + timeSpent + " seconds");
			
			//Embedded Question 2 Completion Activity
			if(!quizMap.containsKey(ques2.getAssessmentId())) {
				Quiz newQuiz = new BasicQuiz();
				newQuiz.setId(ques2.getAssessmentId());
				newQuiz.setSeedDateTime(ques2.getQuestionLastSeedDateTime());
				quizMap.put(ques2.getAssessmentId(), newQuiz);
			}
			Quiz ques2WrappingQuiz = quizMap.get(ques2.getAssessmentId());
			if (!ques2WrappingQuiz.containsQuestionWithId(ques2.getId())) {
				ques2WrappingQuiz.addQuestion(ques2);
			}

			QuestionCompletionActivity question2CompletionActivity = new QuestionCompletionActivity(student1, ques2.getPointsPossible());
			ques2.addCompletionActivity(question2CompletionActivity);
			TestAction actionQ2A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, ques2WrappingQuiz, ques2, attemptQ2A1, question2CompletionActivity);
			request.addTestAction(actionQ2A1);
			
			//Embedded Question 3 attempt 1, correct, final 
			timeSpent = 40;
			Question ques3 = page.getEmbeddedQuestions().get(2);
			Answer ques3CorrectAnswer = ques3.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);			
			MultiValueAttempt attemptQ3A1 
				= getMultiValueRadioButtonAttempt(ques3, student1, timeSpent, /*attemptNumber*/ 1, ques3CorrectAnswer, ques3.getPointsPossible(), /*isFinalAttempt*/ true);
			ques3.addAttempt(attemptQ3A1);
			timeOnTaskOutput.append("\n...assessment time : " + student1.getPersonId() + " : " + ques3.getId() + " : " + timeSpent + " seconds");
			
			//Embedded Question 3 Completion Activity
			if(!quizMap.containsKey(ques3.getAssessmentId())) {
				Quiz newQuiz = new BasicQuiz();
				newQuiz.setId(ques3.getAssessmentId());
				newQuiz.setSeedDateTime(ques3.getQuestionLastSeedDateTime());
				quizMap.put(ques3.getAssessmentId(), newQuiz);
			}
			Quiz ques3WrappingQuiz = quizMap.get(ques3.getAssessmentId());
			if (!ques3WrappingQuiz.containsQuestionWithId(ques3.getId())) {
				ques3WrappingQuiz.addQuestion(ques3);
			}

			QuestionCompletionActivity question3CompletionActivity = new QuestionCompletionActivity(student1, ques3.getPointsPossible());
			ques3.addCompletionActivity(question3CompletionActivity);
			TestAction actionQ3A1 = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, ques3WrappingQuiz, ques3, attemptQ3A1, question3CompletionActivity);
			request.addTestAction(actionQ3A1);
			
			//Quiz Completion for wrapping Quizzes
			Iterator<String> quizMapIter = quizMap.keySet().iterator();
			while (quizMapIter.hasNext()) {
				Quiz quiz = quizMap.get(quizMapIter.next());
				QuizCompletionActivity quizCompletionActivity = new QuizCompletionActivity(student1);
				for(Question question : quiz.getQuestions()) {
					quizCompletionActivity.addQuestionPerf(question.getId(), question.getPointsPossible());
				}
				quiz.addCompletionActivity(quizCompletionActivity);
				TestAction completeChQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, quiz, quizCompletionActivity);
				request.addTestAction(completeChQuizTestAction);
			}
			
			request.executeAllActions();
			
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

