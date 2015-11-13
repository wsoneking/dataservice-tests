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
		 * 
		 */
public class VerySimpleForTestCodeOnly extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest request;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Instructor instr;
		Student student1;
		Student student2;
		Student student3;

		
		try {
			request = new BasicTestDataRequest();
			testData = new BasicTestData("VerySimpleForTestCodeOnly");
			
			// add instructor
			com.pearson.test.daalt.dataservice.User instrFromConfig = getEngine().getInstructor();
			instr = new BasicInstructor(instrFromConfig.getUserName(), 
					instrFromConfig.getPassword(), 
					instrFromConfig.getId(),
					instrFromConfig.getFirstName(),
					instrFromConfig.getLastName());
			testData.addInstructor(instr);
			
			// add student 1
			com.pearson.test.daalt.dataservice.User student01FromConfig = getEngine().getStudent01();
			student1 = new BasicStudent(student01FromConfig.getUserName(), 
					student01FromConfig.getPassword(), 
					student01FromConfig.getId(),
					student01FromConfig.getFirstName(),
					student01FromConfig.getLastName());
			testData.addStudent(student1);
			
			// add student 2
			com.pearson.test.daalt.dataservice.User student02FromConfig = getEngine().getStudent02();
			student2 = new BasicStudent(student02FromConfig.getUserName(), 
					student02FromConfig.getPassword(), 
					student02FromConfig.getId(),
					student02FromConfig.getFirstName(),
					student02FromConfig.getLastName());
			testData.addStudent(student2);
			
			//add student3 to test data
			student3 = new BasicStudent(getUserConfig().getValue(TestEngine.student03UsernamePropName),
					getUserConfig().getValue(TestEngine.student03PasswordPropName),
					getUserConfig().getValue(TestEngine.student03IdPropName),
					getUserConfig().getValue(TestEngine.student03FirstName),
					getUserConfig().getValue(TestEngine.student03LastName));
			testData.addStudent(student3);
			
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
			

			//enroll student 2
			courseSection.addStudent(student2);
			//add action to enroll student
			TestAction enrollStudent2 = testActionFactory.getEnrollStudentTestAction(student2, courseSection);
			request.addTestAction(enrollStudent2);	
			

			//enroll student 3
			courseSection.addStudent(student3);
			//add action to enroll student
			TestAction enrollStudent3 = testActionFactory.getEnrollStudentTestAction(student3, courseSection);
			request.addTestAction(enrollStudent3);	
			
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			
			float pointEarnEachQuestion = 5;
			Assignment assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_QUIZ_ONLY);
			System.out.println(assignmentToAdd.getStructure());
			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			nowCal.add(Calendar.DAY_OF_YEAR, (10));
			assignmentToAdd.setDueDate(nowCal.getTime());
			assignmentToAdd.setTitle("DAALT SQE Test Assignment");
			courseSection.addAssignment(assignmentToAdd);
			//add action to add assignment to course section
			TestAction addAssignment = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
			request.addTestAction(addAssignment);
		
			Quiz chapQuiz = assignmentToAdd.getChapters().get(0).getChapterQuiz();
			
			// Student 1
			{
				
				for(Question ques : chapQuiz.getQuestions()){
					Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
					MultiValueAttempt attempt 
					= getMultiValueRadioButtonAttempt(ques, student1, /*timeSpent*/ 30, /*attemptNumber*/ 1, 
							answer, /*pointsPossible*/ pointEarnEachQuestion, /*isFinalAttempt*/ true);
					ques.addAttempt(attempt);
					
					//Question Completion Activity
					QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student1, pointEarnEachQuestion);
					ques.addCompletionActivity(questionCompletionActivity);
					TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, ques, attempt, questionCompletionActivity);
					request.addTestAction(attemptQuestionTestAction);
			
				}
				
				//Student 1 - Complete Quiz
				QuizCompletionActivity stud1ChapQuizCompletionActivity = new QuizCompletionActivity(student1);
				for(Question ques : chapQuiz.getQuestions()){
					stud1ChapQuizCompletionActivity.addQuestionPerf(ques.getId(), pointEarnEachQuestion);
				}
				chapQuiz.addCompletionActivity(stud1ChapQuizCompletionActivity);
				TestAction stud1CompleteChapQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, stud1ChapQuizCompletionActivity);
				request.addTestAction(stud1CompleteChapQuizTestAction);
			
			}
			
			// Student 2
			{
				
				for(Question ques : chapQuiz.getQuestions()){
					Answer answer = ques.getSubQuestions().get(0).getSingleIncorrectAnswer().getAnswers().get(0);
					MultiValueAttempt attempt 
					= getMultiValueRadioButtonAttempt(ques, student2, /*timeSpent*/ 30, /*attemptNumber*/ 1, 
							answer, /*pointsPossible*/ pointEarnEachQuestion-1, /*isFinalAttempt*/ true);
					ques.addAttempt(attempt);
					
					//Question Completion Activity
					QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student2, pointEarnEachQuestion-1);
					ques.addCompletionActivity(questionCompletionActivity);
					TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, ques, attempt, questionCompletionActivity);
					request.addTestAction(attemptQuestionTestAction);
			
				}
				
				//Student 1 - Complete Quiz
				QuizCompletionActivity stud1ChapQuizCompletionActivity = new QuizCompletionActivity(student2);
				for(Question ques : chapQuiz.getQuestions()){
					stud1ChapQuizCompletionActivity.addQuestionPerf(ques.getId(), pointEarnEachQuestion-1);
				}
				chapQuiz.addCompletionActivity(stud1ChapQuizCompletionActivity);
				TestAction stud1CompleteChapQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, stud1ChapQuizCompletionActivity);
				request.addTestAction(stud1CompleteChapQuizTestAction);
			
			}	
			// Student 3
			{
				for(Question ques : chapQuiz.getQuestions()){
					Answer answer = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
					MultiValueAttempt attempt 
					= getMultiValueRadioButtonAttempt(ques, student3, /*timeSpent*/ 30, /*attemptNumber*/ 1, 
							answer, /*pointsPossible*/ pointEarnEachQuestion, /*isFinalAttempt*/ true);
					ques.addAttempt(attempt);
					
					//Question Completion Activity
					QuestionCompletionActivity questionCompletionActivity = new QuestionCompletionActivity(student3, pointEarnEachQuestion);
					ques.addCompletionActivity(questionCompletionActivity);
					TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection, assignmentToAdd, chapQuiz, ques, attempt, questionCompletionActivity);
					request.addTestAction(attemptQuestionTestAction);
			
				}
				
				//Student 1 - Complete Quiz
				QuizCompletionActivity stud1ChapQuizCompletionActivity = new QuizCompletionActivity(student3);
				for(Question ques : chapQuiz.getQuestions()){
					stud1ChapQuizCompletionActivity.addQuestionPerf(ques.getId(), pointEarnEachQuestion);
				}
				chapQuiz.addCompletionActivity(stud1ChapQuizCompletionActivity);
				TestAction stud1CompleteChapQuizTestAction = testActionFactory.getCompleteQuizTestAction(courseSection, assignmentToAdd, chapQuiz, stud1ChapQuizCompletionActivity);
				request.addTestAction(stud1CompleteChapQuizTestAction);
			
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
			System.out.println(val.getExpectedResultsPrintString());
			getCurrentTestCase().getValidations().add(val);
		}
	}
}

