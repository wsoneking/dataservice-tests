package com.pearson.test.daalt.dataservice.scenario;

import java.util.ArrayList;
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
import com.pearson.test.daalt.dataservice.model.LastActivityDate;
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
/* one student enrolled in the 6 course sections; each course section consists of a single assignment with a chapter quiz assessment 
*Student performs:
* 		load non book content with an assignment- this action involves sending two messages: i.e with/out Course_Section_Source_sytem_Record_Id
* 		unloads non book contents with an assignment
* 		completes a simple_writing questions: sharedWriting and JournalWriting
* 		access/load the chapterQuiz and the Time on task is not recorded
* 		load non book content without an assignment
* 		unloads non book contents without an assignment
* 		previews the assignment
* 		attempts a multi-value question
* */
public class NonBookContentActivities extends BaseTestScenario {
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
		TestDataRequest eightthRequest;
		TestDataRequest ninethRequest;
		TestDataRequest tenthRequest;
		TestData testData;
		Course course;
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
			eightthRequest = new BasicTestDataRequest();
			ninethRequest = new BasicTestDataRequest();
			tenthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("NonBookContentActvities");
			
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
			testData.addCourse(course);
			int dayInterval = 0;
			//create 8 courseSections
			List<CourseSection> courseSections = new ArrayList<CourseSection>();
			for (int i = 0; i < 9; i++) {
				CourseSection courseSection = new BasicCourseSection();
				courseSections.add(courseSection);	
				courseSection.setInstructor(instr);
				//create Book
				Product book = new BasicProduct();	
				courseSection.addBook(book);
				course.addCourseSection(courseSection);
				
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
				TestAction enrollStudent1 = testActionFactory.getEnrollStudentTestAction(student, courseSection);
				firstRequest.addTestAction(enrollStudent1);	
				
				if (assignmentFactory == null) {
					assignmentFactory = new AssignmentFactory();
				}
				Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				if (i != 2 && i != 3 && ( i != 4 && i != 5)) {
					//create an assignment
					Assignment assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CH_SECTION_QUIZ_AND_CH_QUIZ_AND_READING);
					System.out.println(assignmentToAdd.getStructure());
					nowCal.add(Calendar.DAY_OF_YEAR, (10+dayInterval));
					assignmentToAdd.setDueDate(nowCal.getTime());
					assignmentToAdd.setTitle("DAALT SQE Test Assignment");
					courseSection.addAssignment(assignmentToAdd);
					//add action to add assignment to course section
					TestAction addAssignment = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
					firstRequest.addTestAction(addAssignment);
				} 
				if( i == 2 && i != 3 &&( i != 4 && i !=5) ) {
					Assignment assignmentJournalAdd = assignmentFactory
							.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.ONE_QUIZ_WITH_JOURNAL_WRITING_QUESTION);

					System.out.println(assignmentJournalAdd.getStructure());
					nowCal.add(Calendar.DAY_OF_YEAR, 10);
					assignmentJournalAdd.setTitle("DAALT SQE Test Assignment");
					assignmentJournalAdd.setDueDate(nowCal.getTime());
					courseSection.addAssignment(assignmentJournalAdd);
					// add action to add assignment1 to course section
					TestAction addAssignment1 = testActionFactory.getAddAssignmentTestAction(instr, courseSection,
							assignmentJournalAdd);
					firstRequest.addTestAction(addAssignment1);
				}
				if(i !=2 &&  i == 3 &&  ( i != 4 && i !=5) ) {
					Assignment assignmentSimpleWritingAdd = assignmentFactory
							.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.ONE_QUIZ_WITH_SHARED_WRITING_QUESTION);
					
					System.out.println(assignmentSimpleWritingAdd.getStructure());
					
					nowCal.add(Calendar.DAY_OF_YEAR, 10);
					assignmentSimpleWritingAdd.setTitle("DAALT SQE Test Assignment");
					assignmentSimpleWritingAdd.setDueDate(nowCal.getTime());
					courseSection.addAssignment(assignmentSimpleWritingAdd);
					// add action to add assignment to course section
					TestAction addAssignment2 = testActionFactory.getAddAssignmentTestAction(instr, courseSection,
							assignmentSimpleWritingAdd);
					firstRequest.addTestAction(addAssignment2);
				
				}
				
				dayInterval++;
			}			
			
			firstRequest.executeAllActions();
			
     		//Load a non-book content with an assignment
			CourseSection courseSection1 = courseSections.get(0);
			Assignment assignment1 = courseSection1.getAssignments().get(0);
			LastActivityDate lastActivityDate1 = new LastActivityDate(student);
			courseSection1.addLastActivityDate(lastActivityDate1);
			TestAction loadNonBookContentWithAssignmentTestAction = testActionFactory.getUserLoadNonBookContentWithAssignmentTestAction(courseSection1, assignment1, lastActivityDate1);
			secondRequest.addTestAction(loadNonBookContentWithAssignmentTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " +" last activity date: "+ lastActivityDate1.getLastActivityDate());
			secondRequest.executeAllActions();
			
			//Unload a non-book content with an assignment
			CourseSection courseSection2 = courseSections.get(1);
			Assignment assignment2 = courseSection2.getAssignments().get(0);
			LastActivityDate lastActivityDate2 = new LastActivityDate(student);
			courseSection2.addLastActivityDate(lastActivityDate2);
			TestAction unLoadNonBookContentWithAssignmentTestAction = testActionFactory.getUserUnLoadNonBookContentWithAssignmentTestAction(courseSection2,assignment2, lastActivityDate2);
			thirdRequest.addTestAction(unLoadNonBookContentWithAssignmentTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " +" last activity date: "+ lastActivityDate2.getLastActivityDate());
			thirdRequest.executeAllActions();
			
			// Student completes  Simple Writing Journal Quiz question1
			CourseSection courseSection3 = courseSections.get(2);
			Assignment assignment3 = courseSection3.getAssignments().get(0);
			Quiz assignment3ChapQuiz = assignment3.getChapters().get(0).getChapterQuiz();
			Question assignment3ChapQuizQues = assignment3ChapQuiz.getQuestions().get(0);

			// Assignment3 Question Completion Activity
			int timeSpent = 10;
			QuestionCompletionActivity assignment3ChapQuizQuestionCompletionActivity = new QuestionCompletionActivity(
					student, /*score*/ 0f);
			assignment3ChapQuizQuestionCompletionActivity.setTimeOnQuestion(timeSpent);
			assignment3ChapQuizQues.addCompletionActivity(assignment3ChapQuizQuestionCompletionActivity);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + assignment3ChapQuizQues.getId() + " : " + timeSpent + " seconds");

			// Assignment3 Quiz Completion Activity
			QuizCompletionActivity assigment3ChapQuizCompletionActivity = new QuizCompletionActivity(student);
			assigment3ChapQuizCompletionActivity.addQuestionPerf(assignment3ChapQuizQues.getId(),/*score*/ 0f);
			assignment3ChapQuiz.addCompletionActivity(assigment3ChapQuizCompletionActivity);
			TestAction assignment3SimpleWritingCompleteQuizTestAction = testActionFactory.getSimpleWritingCompleteQuizTestAction
					(courseSection3, assignment3, assignment3ChapQuizQues,
							assignment3ChapQuizQuestionCompletionActivity, assignment3ChapQuiz,
							assigment3ChapQuizCompletionActivity);
			fourthRequest.addTestAction(assignment3SimpleWritingCompleteQuizTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " +" last activity date: "+assignment3ChapQuizQuestionCompletionActivity.getLastActivityDate());
			fourthRequest.executeAllActions();

			// Student completes Assignment4 Simple Writing SharedWriting
			// Assignment4 Chapter Quiz
			CourseSection courseSection4 = courseSections.get(3);
			Assignment assignment4 = courseSection4.getAssignments().get(0);
			Quiz assignment4ChapQuiz = assignment4.getChapters().get(0).getChapterQuiz();
			Question assignment4ChapQuizQues = assignment4ChapQuiz.getQuestions().get(0);
			timeSpent = 10;
			QuestionCompletionActivity assignment4ChapQuizQuestionCompletionActivity = new QuestionCompletionActivity(
					student, assignment4ChapQuizQues.getPointsPossible());
			assignment4ChapQuizQuestionCompletionActivity.setTimeOnQuestion(timeSpent);
			assignment4ChapQuizQues.addCompletionActivity(assignment4ChapQuizQuestionCompletionActivity);
			timeOnTaskOutput.append("\n...assessment time : " + student.getPersonId() + " : " + assignment4ChapQuizQues.getId() + " : " + timeSpent + " seconds");
			QuizCompletionActivity assigment4ChapQuizCompletionActivity = new QuizCompletionActivity(student);
			assigment4ChapQuizCompletionActivity.addQuestionPerf(assignment4ChapQuizQues.getId(),
					assignment4ChapQuizQues.getPointsPossible());
			assignment4ChapQuiz.addCompletionActivity(assigment4ChapQuizCompletionActivity);
			TestAction assignment4SharedWritingCompleteQuizTestAction = testActionFactory
					.getSimpleWritingCompleteQuizTestAction(courseSection4, assignment4, assignment4ChapQuizQues,assignment4ChapQuizQuestionCompletionActivity, assignment4ChapQuiz,assigment4ChapQuizCompletionActivity);
			fifthRequest.addTestAction(assignment4SharedWritingCompleteQuizTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " +" last activity date: "+assignment4ChapQuizQuestionCompletionActivity.getLastActivityDate());
			fifthRequest.executeAllActions();
			
			//Load a non-book content without an assignment
			CourseSection courseSection5 = courseSections.get(4);
			LastActivityDate lastActivityDate3 = new LastActivityDate(student);
			courseSection5.addLastActivityDate(lastActivityDate3);
			TestAction LoadNonBookContentWithoutAssignmentTestAction = testActionFactory.getUserLoadNonBookContentWithoutAssignmentTestAction(courseSection5, lastActivityDate3);	
			sixthRequest.addTestAction(LoadNonBookContentWithoutAssignmentTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " +" last activity date: "+ lastActivityDate3.getLastActivityDate());
			sixthRequest.executeAllActions();
			
			//UnLoad a non-book content without an assignment
			CourseSection courseSection6 = courseSections.get(5);
			LastActivityDate lastActivityDate4 = new LastActivityDate(student);
			courseSection6.addLastActivityDate(lastActivityDate4);
			TestAction unLoadNonBookContentWithoutAssignmentTestAction = testActionFactory.getUserUnLoadNonBookContentWithoutAssignmentTestAction(courseSection6, lastActivityDate4);
			seventhRequest.addTestAction(unLoadNonBookContentWithoutAssignmentTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " + " last activity date: "+ lastActivityDate4.getLastActivityDate());
			seventhRequest.executeAllActions();
			
			//Access the chapterQuiz
			timeSpent = 10;
			CourseSection courseSection7 = courseSections.get(6);
			Assignment assignment5 = courseSection7.getAssignments().get(0);
			Quiz quiz = assignment5.getChapters().get(0).getChapterQuiz();
			Question question =  quiz.getQuestions().get(0);
			Answer answer = question.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			MultiValueAttempt attempt 
				= getMultiValueRadioButtonAttempt(question, student, /*timeSpent*/ timeSpent, /*attemptNumber*/ 1, 
					answer, /*pointsPossible*/ question.getPointsPossible(), /*isFinalAttempt*/ true);
			attempt.setCountForAssessingTime(false);
			attempt.setCountForPointsEarned(false);
			question.addAttempt(attempt);
			TestAction studentAccessQuestionTestAction = testActionFactory.getStudentAccessQuestionTestAction(courseSection7, quiz, question, attempt);
			eightthRequest.addTestAction(studentAccessQuestionTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " + " last activity date: "+ attempt.getLastActivityDate());
			eightthRequest.executeAllActions();
			
			
			//attempts a multi-value question
			CourseSection courseSection8 = courseSections.get(7);
			Assignment assignment6 = courseSection8.getAssignments().get(0);
			Quiz chapQuiz = assignment6.getChapters().get(0).getChapterQuiz();
			Question ques =  chapQuiz.getQuestions().get(0);
			Answer ans = ques.getSubQuestions().get(0).getCorrectAnswer().getAnswers().get(0);
			timeSpent = 20;
			MultiValueAttempt multiValueAttempt 
				= getMultiValueRadioButtonAttempt(ques, student, /*timeSpent*/ timeSpent, /*attemptNumber*/ 1, 
						ans, /*pointsPossible*/ question.getPointsPossible(), /*isFinalAttempt*/ false);
			ques.addAttempt(multiValueAttempt);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " + " last activity date: "+ multiValueAttempt.getLastActivityDate());
			TestAction attemptQuestionTestAction = testActionFactory.getAttemptQuestionTestAction(courseSection8, assignment6, chapQuiz, ques, multiValueAttempt, null);
			ninethRequest.addTestAction(attemptQuestionTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId() + " : " + " last activity date: "+ multiValueAttempt.getLastActivityDate());
			ninethRequest.executeAllActions();
			
			// Access content
			CourseSection courseSection9 = courseSections.get(8);
			Assignment assignment7 = courseSection9.getAssignments().get(0);
			Page page2 = assignment7.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			timeSpent = 10;
			LearningActivity learningActivity2 = new LearningActivity(student, timeSpent);
			learningActivity2.setCountForLearningTime(false);
			page2.addLearningActivity(learningActivity2);
			TestAction studentAccessContentTestAction = testActionFactory.getStudentAccessContentTestAction(courseSection9, page2, learningActivity2);
			tenthRequest.addTestAction(studentAccessContentTestAction);
			timeOnTaskOutput.append("\n..." + student.getPersonId()+ " : " + " last activity date: "+ learningActivity2.getLastActivityDate());
			tenthRequest.executeAllActions();
			
		} catch (Exception e) {
			getEngine().getSuite().setDidCreationTestsComplete(false);
			throw e;
		}
		
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