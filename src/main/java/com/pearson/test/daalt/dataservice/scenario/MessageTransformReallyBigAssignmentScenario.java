package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
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
import com.pearson.test.daalt.dataservice.model.Course;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.Product;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.SectionToModuleToResourcesValidationEngine;
import com.pearson.test.daalt.dataservice.validation.SectionToStudentToModuleToResourcesValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;

public class MessageTransformReallyBigAssignmentScenario extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;
	private com.pearson.test.daalt.dataservice.request.action.version01.TestActionFactory testActionFactoryV1;

	@Test
	public void loadBasicTestData() throws Exception {
		testActionFactoryV1 =  new com.pearson.test.daalt.dataservice.request.action.version01.SubPubSeerTestActionFactory();
		
		TestDataRequest zeroRequest;
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;
		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignmentToAdd;
		Instructor instr;
		Student student1;
		Student student2;
		
		/*
		 * The course doesn't enrolled any student. 
		 */
		
		try {
			zeroRequest = new BasicTestDataRequest();
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			testData = new BasicTestData("MessageTransformReallyBigAssignmentScenario");
			
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
			
			//enroll student
			courseSection.addStudent(student1);
			//add action to enroll student
			TestAction enrollStudent = testActionFactory.getEnrollStudentTestAction(student1, courseSection);
			firstRequest.addTestAction(enrollStudent);
			
			
			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			assignmentToAdd = assignmentFactory.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.REALLY_BIG_ASSIGNMENT);
			assignmentToAdd.setAllTargetIdsTrue();
			
			System.out.println(assignmentToAdd.getStructure());

			Calendar nowCal = createDueDate();

			assignmentToAdd.setDueDate(nowCal.getTime());
			
			assignmentToAdd.setTitle("DAALT SQE Test Assignment");
			courseSection.addAssignment(assignmentToAdd);
			//add action to add assignment to course section
			TestAction addAssignment = testActionFactoryV1.getAddAssignmentTestAction(instr, courseSection, assignmentToAdd);
			firstRequest.addTestAction(addAssignment);
			
			firstRequest.executeAllActions();
			
			TestAction reSendSeedData = testActionFactory.getReSendSeedDataTestAction(courseSection, assignmentToAdd);
			secondRequest.addTestAction(reSendSeedData);

			secondRequest.executeAllActions();
			assignmentToAdd.setDueDatePassed(true);
			
		} catch (Exception e) {
			getEngine().getSuite().setDidCreationTestsComplete(false);
			throw e;
		}

//		DaaltDataServiceValidationEngine validationEngine = new DaaltDataServiceValidationEngine();
//		List<Validation> validationList = validationEngine.getValidationsForTestData(testData);
		
		SectionToModuleToResourcesValidationEngine firstValidationEngine = new SectionToModuleToResourcesValidationEngine();
		List<Validation> firstValidationList = firstValidationEngine.getValidations(testData);
		for (Validation val : firstValidationList) {
			getCurrentTestCase().getValidations().add(val);
		}
		
		SectionToStudentToModuleToResourcesValidationEngine secondValidationEngine = new SectionToStudentToModuleToResourcesValidationEngine();
		List<Validation> secondValidationList = secondValidationEngine.getValidations(testData);
		for (Validation val : secondValidationList) {
			getCurrentTestCase().getValidations().add(val);
		}
	}
}
