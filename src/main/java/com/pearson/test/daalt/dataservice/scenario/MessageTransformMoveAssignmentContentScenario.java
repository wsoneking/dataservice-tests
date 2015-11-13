package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

public class MessageTransformMoveAssignmentContentScenario extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;
	private com.pearson.test.daalt.dataservice.request.action.version01.TestActionFactory testActionFactoryV1;

	@Test
	public void loadTestData() throws Exception {
		testActionFactoryV1 = new com.pearson.test.daalt.dataservice.request.action.version01.SubPubSeerTestActionFactory();
		
		TestDataRequest zeroRequest;
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestDataRequest thirdRequest;
		TestDataRequest fourthRequest;
		TestDataRequest fifthRequest;
		TestDataRequest sixthRequest;
		TestDataRequest seventhRequest;
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
			zeroRequest = new BasicTestDataRequest();
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			fifthRequest = new BasicTestDataRequest();
			sixthRequest = new BasicTestDataRequest();
			seventhRequest = new BasicTestDataRequest();
			testData = new BasicTestData("MessageTransformMoveAssignmentContentScenario");
			
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
			courseSection.addStudent(student1);
			//add action to enroll student
			TestAction enrollStudent1 = testActionFactoryV1.getEnrollStudentTestAction(student1, courseSection);
			firstRequest.addTestAction(enrollStudent1);	
			
			//enroll student 2
			courseSection.addStudent(student2);
			//add action to enroll student
			TestAction enrollStudent2 = testActionFactoryV1.getEnrollStudentTestAction(student2, courseSection);
			firstRequest.addTestAction(enrollStudent2);	
			
			
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			
			////---- create learning time in the first chapter - part of first assignment
			
			assignmentToAdd1 = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_CH_SECTION_QUIZ_AND_READING);
			assignmentToAdd1.setAllTargetIdsTrue();
			
			System.out.println("assignment1 structure:\n"+assignmentToAdd1.getStructure());

			Calendar nowCal = createDueDate();

			assignmentToAdd1.setDueDate(nowCal.getTime());
			
			assignmentToAdd1.setTitle("DAALT SQE Test Assignment");
			courseSection.addAssignment(assignmentToAdd1);
			//add action to add assignment to course section
			TestAction addAssignment = testActionFactoryV1.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd1);
			firstRequest.addTestAction(addAssignment);
			
			firstRequest.executeAllActions();
	
     		//Read Page
			LearningActivity learningActivity = new LearningActivity(student1, 100);
			Page page = assignmentToAdd1.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			page.addLearningActivity(learningActivity);
			TestAction readPageTestAction = testActionFactoryV1.getReadPageTestAction(student1, courseSection, assignmentToAdd1, page, learningActivity);
			secondRequest.addTestAction(readPageTestAction);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page.getLearningResourceId() + " : 100 seconds");
			
			secondRequest.executeAllActions();
			
			////---- create learning time in the second chapter - part of second assignment
			
			assignmentToAdd2 = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CHAPTER_CH_SECTION_QUIZ_AND_READING_X2);
			assignmentToAdd2.setAllTargetIdsTrue();
			
			System.out.println("assignment2 structure:\n"+assignmentToAdd2.getStructure());
			assignmentToAdd2.setDueDate(nowCal.getTime());
			assignmentToAdd2.setTitle("DAALT SQE Test Assignment");
			courseSection.addAssignment(assignmentToAdd2);
			//add action to add assignment to course section
			TestAction addAssignment2 = testActionFactoryV1.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd2);
			thirdRequest.addTestAction(addAssignment2);
	
     		//Read Page
			LearningActivity learningActivity2 = new LearningActivity(student1, 100);
			Page page2 = assignmentToAdd2.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			page2.addLearningActivity(learningActivity2);
			TestAction readPageTestAction2 = testActionFactoryV1.getReadPageTestAction(student1, courseSection, assignmentToAdd2, page2, learningActivity2);
			thirdRequest.addTestAction(readPageTestAction2);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page2.getLearningResourceId() + " : 100 seconds");
			
			thirdRequest.executeAllActions();
			
			////---- create learning time in the third chapter - part of second assignment
	
     		//Read Page
			LearningActivity learningActivity3 = new LearningActivity(student1, /*timeSpentReadingSeconds*/ 100);
			Page page3 = assignmentToAdd2.getChapters().get(1).getChapterSections().get(0).getPages().get(0);
			page3.addLearningActivity(learningActivity3);
			TestAction readPageTestAction3 = testActionFactoryV1.getReadPageTestAction(student1, courseSection, assignmentToAdd2, page3, learningActivity3);
			fourthRequest.addTestAction(readPageTestAction3);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page3.getLearningResourceId() + " : 100 seconds");
			
			fourthRequest.executeAllActions();
			
			//delete old assignment content
			TestAction deleteAssignment1Content = testActionFactoryV1.getModifyAssignmentTestAction(instr, courseSection, assignmentToAdd1, "Delete");
			fifthRequest.addTestAction(deleteAssignment1Content);
			TestAction deleteAssignment2Content = testActionFactoryV1.getModifyAssignmentTestAction(instr, courseSection, assignmentToAdd2, "Delete");
			fifthRequest.addTestAction(deleteAssignment2Content);
			
			fifthRequest.executeAllActions();
			
			////---- Modify the assignment - move third chapter from second assignment to first assignment
			Chapter toMoveChap = assignmentToAdd2.getChapters().get(1);
			assignmentToAdd1.addChapter(toMoveChap);
			assignmentToAdd2.removeChapter(toMoveChap);
			
			System.out.println("The assignment structure after move the chapter.");
			System.out.println("assignment1 structure:\n"+assignmentToAdd1.getStructure());
			System.out.println("assignment2 structure:\n"+assignmentToAdd2.getStructure());
			
			//create new assignment content
			TestAction updateAssignment1 = testActionFactoryV1.getModifyAssignmentTestAction(instr, courseSection, assignmentToAdd1, "Create");
			sixthRequest.addTestAction(updateAssignment1);
			TestAction updateAssignment2 = testActionFactoryV1.getModifyAssignmentTestAction(instr, courseSection, assignmentToAdd2, "Create");
			sixthRequest.addTestAction(updateAssignment2);
			
			sixthRequest.executeAllActions();
			
			TestAction reSendAssignment1SeedData = testActionFactory.getReSendSeedDataTestAction(courseSection, assignmentToAdd1);
			seventhRequest.addTestAction(reSendAssignment1SeedData);
			TestAction reSendAssignment2SeedData = testActionFactory.getReSendSeedDataTestAction(courseSection, assignmentToAdd2);
			seventhRequest.addTestAction(reSendAssignment2SeedData);
			
			seventhRequest.executeAllActions();
			
			assignmentToAdd1.setDueDatePassed(true);
			assignmentToAdd2.setDueDatePassed(true);
			
			
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
