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
import com.pearson.test.daalt.dataservice.model.Product;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.request.BasicTestDataRequest;
import com.pearson.test.daalt.dataservice.request.TestDataRequest;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.validation.DaaltDataServiceValidationEngine;
import com.pearson.test.daalt.dataservice.validation.Validation;

public class MessageTransformItemAnalysisCriticalTestDataLoadScenario extends BaseTestScenario {
	private AssignmentFactory assignmentFactory;
	private com.pearson.test.daalt.dataservice.request.action.version01.TestActionFactory testActionFactoryV1;

	@Test
	public void loadBasicTestData() throws Exception {
		testActionFactoryV1 = new com.pearson.test.daalt.dataservice.request.action.version01.SubPubSeerTestActionFactory();
		TestDataRequest firstRequest;
		TestDataRequest secondRequest;

		TestData testData;
		Course course;
		CourseSection courseSection;
		Assignment assignmentToAdd;
		Instructor instr;
		Student student;

		try {
			firstRequest = new BasicTestDataRequest();
			secondRequest = new BasicTestDataRequest();
			testData = new BasicTestData("ItemAnalysisCriticalTestDataLoad");

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
			TestAction createCourseSection = testActionFactoryV1.getCreateCourseSectionTestAction(courseSection);
			firstRequest.addTestAction(createCourseSection);

			// We need to enroll the instructor to the course, or we will get
			// 401 when the instructor make the call.
			// add action to enroll instructor
			TestAction enrollInstr = testActionFactoryV1.getEnrollInstructorTestAction(instr, courseSection);
			firstRequest.addTestAction(enrollInstr);

			// enroll student
			courseSection.addStudent(student);
			// add action to enroll student
			TestAction enrollStudent = testActionFactoryV1.getEnrollStudentTestAction(student, courseSection);
			firstRequest.addTestAction(enrollStudent);

			if (assignmentFactory == null) {
				assignmentFactory = new AssignmentFactory();
			}
			assignmentToAdd = assignmentFactory
					.getAssignmentByEnum(AssignmentFactory.AssignmentLibrary.CH_SECTION_QUIZ_AND_CH_QUIZ_AND_READING);

			System.out.println(assignmentToAdd.getStructure());

			Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone(TestEngine.timeZoneUTC));
			nowCal.add(Calendar.DAY_OF_YEAR, 10);
			//assignmentToAdd.setSequenceNumber(0f);
			assignmentToAdd.setTitle("DAALT SQE Test Assignment");
			assignmentToAdd.setDueDate(nowCal.getTime());
			courseSection.addAssignment(assignmentToAdd);
			// add action to add assignment to course section
			TestAction addAssignment = testActionFactoryV1.getAddAssignmentTestAction(instr, courseSection,
					assignmentToAdd);
			firstRequest.addTestAction(addAssignment);

			firstRequest.executeAllActions();

			TestAction reSendSeedData = testActionFactory.getReSendSeedDataTestAction(courseSection, assignmentToAdd);
			secondRequest.addTestAction(reSendSeedData);

			secondRequest.executeAllActions();
		} catch (Exception e) {
			getEngine().getSuite().setDidCreationTestsComplete(false);
			throw e;
		}

		// pass TestData to validation engine
		DaaltDataServiceValidationEngine validationEngine = new DaaltDataServiceValidationEngine();
		List<Validation> validationList = validationEngine.getValidationsForTestData(testData);
		for (Validation val : validationList) {
			System.out.println(val.getExpectedResultsPrintString());
			getCurrentTestCase().getValidations().add(val);
		}
	}
}
