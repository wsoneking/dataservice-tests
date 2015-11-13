package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.Date;
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
import com.pearson.test.daalt.dataservice.model.Chapter;
import com.pearson.test.daalt.dataservice.model.Course;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Product;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;

/**   
 *  course section contains two assignments, 
    after students do reading in the assignments, 
    move a chapter from one assignment to the other
    by sending Learning_Module and Learning_Module_Content messages with Transaction_Type_Code = "Delete"
    then sending Learning_Module and Learning_Module_Content messages with Transaction_Type_Code = "Create"
 */
public class MoveAssignmentContentViaDelete extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;

	@Test
	public void loadBasicTestData() throws Exception {
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
		TestDataRequest fourthRequest;
		TestDataRequest fifthRequest;
		TestDataRequest sixthRequest;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignmentToAdd1;
		Assignment assignmentToAdd2;
		Instructor instr;
		Student student1;
		Student student2;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");

		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			fifthRequest = new BasicTestDataRequest();
			sixthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("ContentMoveFromOneAssignmentToAnother");
			
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
			courseSection.addStudent(student1);
			//add action to enroll student
			TestAction enrollStudent1 = testActionFactory.getEnrollStudentTestAction(student1, courseSection);
			firstRequest.addTestAction(enrollStudent1);	
			
			//enroll student 2
			courseSection.addStudent(student2);
			//add action to enroll student
			TestAction enrollStudent2 = testActionFactory.getEnrollStudentTestAction(student2, courseSection);
			firstRequest.addTestAction(enrollStudent2);	
			
			
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			
			////---- Reading and taking the chapterSectionQuiz in the first assignment.
			
			assignmentToAdd1 = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_CH_SECTION_QUIZ_AND_READING);
			System.out.println("assignment1 structure:\n"+assignmentToAdd1.getStructure());
			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			assignmentToAdd1.setDueDate(nowCal.getTime());
			assignmentToAdd1.setTitle("DAALT SQE Test Assignment");
			courseSection.addAssignment(assignmentToAdd1);
			//add action to add assignment to course section
			TestAction addAssignment = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd1);
			firstRequest.addTestAction(addAssignment);
			
			firstRequest.executeAllActions();
	
     		//Read Page
			int timeSpent = 100;
			LearningActivity learningActivity = new LearningActivity(student1, timeSpent);
			Page page = assignmentToAdd1.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			page.addLearningActivity(learningActivity);
			TestAction readPageTestAction = testActionFactory.getReadPageTestAction(courseSection, page, learningActivity);
			secondRequest.addTestAction(readPageTestAction);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page.getLearningResourceId() + " : " + timeSpent + " seconds");
			
			secondRequest.executeAllActions();
						
			
			////---- Reading in the second assignment.
			
			assignmentToAdd2 = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_CH_SECTION_QUIZ_AND_READING_X2);
			System.out.println("assignment2 structure:\n"+assignmentToAdd2.getStructure());
			Calendar nowCal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			nowCal2.add(Calendar.DAY_OF_YEAR, 10);
			assignmentToAdd2.setDueDate(nowCal2.getTime());
			assignmentToAdd2.setTitle("DAALT SQE Test Assignment");
			courseSection.addAssignment(assignmentToAdd2);
			//add action to add assignment to course section
			TestAction addAssignment2 = testActionFactory.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd2);
			thirdRequest.addTestAction(addAssignment2);
	
     		//Read Page
			timeSpent = 110;
			LearningActivity learningActivity2 = new LearningActivity(student1, timeSpent);
			Page page2 = assignmentToAdd2.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			page2.addLearningActivity(learningActivity2);
			TestAction readPageTestAction2 = testActionFactory.getReadPageTestAction(courseSection, page2, learningActivity2);
			thirdRequest.addTestAction(readPageTestAction2);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page2.getLearningResourceId() + " : " + timeSpent + " seconds");
			
			thirdRequest.executeAllActions();
			
			////---- Reading in the second assignment.
	
     		//Read Page
			timeSpent = 103;
			LearningActivity learningActivity3 = new LearningActivity(student1, timeSpent);
			Page page3 = assignmentToAdd2.getChapters().get(1).getChapterSections().get(0).getPages().get(0);
			page3.addLearningActivity(learningActivity3);
			TestAction readPageTestAction3 = testActionFactory.getReadPageTestAction(courseSection, page3, learningActivity3);
			fourthRequest.addTestAction(readPageTestAction3);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page3.getLearningResourceId() + " : " + timeSpent + " seconds");
			
			fourthRequest.executeAllActions();
			
			//delete assignment content			
			TestAction deleteAssignment1Content = testActionFactory.getDeleteAssignmentContentTestAction(instr, courseSection, assignmentToAdd1);
			fifthRequest.addTestAction(deleteAssignment1Content);
			TestAction deleteAssignment2Content = testActionFactory.getDeleteAssignmentContentTestAction(instr, courseSection, assignmentToAdd2);
			fifthRequest.addTestAction(deleteAssignment2Content);

			fifthRequest.executeAllActions();
			
			////---- Modify the assignment
			
			// move one second chapter in assignment 2 into assignment 1. 
			Chapter toMoveChap = assignmentToAdd2.getChapters().get(1);
			assignmentToAdd1.addChapter(toMoveChap);
			assignmentToAdd2.removeChapter(toMoveChap);
			
			System.out.println("The assignment structure after move the chapter.");
			System.out.println("assignment1 structure:\n"+assignmentToAdd1.getStructure());
			System.out.println("assignment2 structure:\n"+assignmentToAdd2.getStructure());
					
			//// add TestAction for modified assignment
			
			//create new assignment content
			TestAction updateAssignment1 = testActionFactory.getCreateAssignmentContentTestAction(instr, courseSection, assignmentToAdd1);
			sixthRequest.addTestAction(updateAssignment1);
			TestAction updateAssignment2 = testActionFactory.getCreateAssignmentContentTestAction(instr, courseSection, assignmentToAdd2);
			sixthRequest.addTestAction(updateAssignment2);

			sixthRequest.executeAllActions();
			
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

