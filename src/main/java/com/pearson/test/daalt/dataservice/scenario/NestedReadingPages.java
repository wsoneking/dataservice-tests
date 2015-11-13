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
		 *   chapter
                chapter section
                    page
                        page
                        page
                        
             Nested Reading page. Enroll one student. 
		 */
		
public class NestedReadingPages extends BaseTestScenario {
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
		Student student1;
		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");

		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			thirdRequest = new BasicTestDataRequest();
			fourthRequest = new BasicTestDataRequest();
			testData = new BasicTestData("NestedReadingPages");
			
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
			
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.NESTED_READING_PAGES_ONLY);
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
	
			////---- Student 1
     		//Read Page
			int timeSpent = 10;
			LearningActivity learningActivity = new LearningActivity(student1, timeSpent);
			Page page = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0);
			page.addLearningActivity(learningActivity);
			TestAction readPageTestAction = testActionFactory.getReadPageTestAction(courseSection, page, learningActivity);
			secondRequest.addTestAction(readPageTestAction);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page.getLearningResourceId() + " : " + timeSpent + " seconds");
			
			//Read Page 2 
			timeSpent = 12;
			LearningActivity learningActivity2 = new LearningActivity(student1, timeSpent);
			Page page2 = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0).getPages().get(0);
			page2.addLearningActivity(learningActivity2);
			TestAction readPageTestAction2 = testActionFactory.getReadPageTestAction(courseSection, page2, learningActivity2);
			secondRequest.addTestAction(readPageTestAction2);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page2.getLearningResourceId() + " : " + timeSpent + " seconds");
			
			//Read Page 3
			timeSpent = 14;
			LearningActivity learningActivity3 = new LearningActivity(student1, timeSpent);
			Page page3 = assignmentToAdd.getChapters().get(0).getChapterSections().get(0).getPages().get(0).getPages().get(1);
			page3.addLearningActivity(learningActivity3);
			TestAction readPageTestAction3 = testActionFactory.getReadPageTestAction(courseSection, page3, learningActivity3);
			secondRequest.addTestAction(readPageTestAction3);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page3.getLearningResourceId() + " : " + timeSpent + " seconds");
					
			secondRequest.executeAllActions();
			
			//Read Page
			timeSpent = 15;
			LearningActivity learningActivity4 = new LearningActivity(student1, timeSpent);
			page.addLearningActivity(learningActivity4);
			TestAction readPageTestAction4 = testActionFactory.getReadPageTestAction(courseSection, page, learningActivity4);
			thirdRequest.addTestAction(readPageTestAction4);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page.getLearningResourceId() + " : " + timeSpent + " seconds");
			
			//Read Page 2 
			timeSpent = 17;
			LearningActivity learningActivity5 = new LearningActivity(student1, timeSpent);
			page2.addLearningActivity(learningActivity5);
			TestAction readPageTestAction5 = testActionFactory.getReadPageTestAction(courseSection, page2, learningActivity5);
			thirdRequest.addTestAction(readPageTestAction5);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page2.getLearningResourceId() + " : " + timeSpent + " seconds");
			
			//Read Page 3
			timeSpent = 19;
			LearningActivity learningActivity6 = new LearningActivity(student1, timeSpent);
			page3.addLearningActivity(learningActivity6);
			TestAction readPageTestAction6 = testActionFactory.getReadPageTestAction(courseSection, page3, learningActivity6);
			thirdRequest.addTestAction(readPageTestAction6);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page3.getLearningResourceId() + " : " + timeSpent + " seconds");
					
			thirdRequest.executeAllActions();
			
			//Read Page
			timeSpent = 20;
			LearningActivity learningActivity7 = new LearningActivity(student1, timeSpent);
			page.addLearningActivity(learningActivity7);
			TestAction readPageTestAction7 = testActionFactory.getReadPageTestAction(courseSection, page, learningActivity7);
			fourthRequest.addTestAction(readPageTestAction7);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page.getLearningResourceId() + " : " + timeSpent + " seconds");
			
			//Read Page 2 
			timeSpent = 22;
			LearningActivity learningActivity8 = new LearningActivity(student1, timeSpent);
			page2.addLearningActivity(learningActivity8);
			TestAction readPageTestAction8 = testActionFactory.getReadPageTestAction(courseSection, page2, learningActivity8);
			fourthRequest.addTestAction(readPageTestAction8);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page2.getLearningResourceId() + " : " + timeSpent + " seconds");
			
			//Read Page 3
			timeSpent = 24;
			LearningActivity learningActivity9 = new LearningActivity(student1, timeSpent);
			page3.addLearningActivity(learningActivity9);
			TestAction readPageTestAction9 = testActionFactory.getReadPageTestAction(courseSection, page3, learningActivity9);
			fourthRequest.addTestAction(readPageTestAction9);
			timeOnTaskOutput.append("\n...learning time : " + student1.getPersonId() + " : " + page3.getLearningResourceId() + " : " + timeSpent + " seconds");
					
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

