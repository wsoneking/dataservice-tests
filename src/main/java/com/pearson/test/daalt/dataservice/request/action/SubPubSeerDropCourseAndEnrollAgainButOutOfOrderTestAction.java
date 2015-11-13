package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.PersonCourseSectionMessage;

public class SubPubSeerDropCourseAndEnrollAgainButOutOfOrderTestAction extends
		DropCourseAndEnrollAgainButOutOfOrderTestAction {

	public SubPubSeerDropCourseAndEnrollAgainButOutOfOrderTestAction(Student student, CourseSection courseSection) {
		messages = new ArrayList<>();
		this.student = student;
		this.courseSection = courseSection;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now generate DropStudentTestAction messages...\n");
		courseSection.removeStudent(student);
		Message gridTransformCourseregistrationDeleteMessage = new PersonCourseSectionMessage();
		gridTransformCourseregistrationDeleteMessage.setProperty("CourseSection", courseSection);
		gridTransformCourseregistrationDeleteMessage.setProperty("Person", student);
		gridTransformCourseregistrationDeleteMessage.setProperty("Transaction_Type_Code", "Delete");
		
		Thread.sleep(1000); 
		
		System.out.println("Now generate EnrollStudentTestAction messages...\n");
		courseSection.addStudent(student);
		Message gridTransformCourseregistrationCreateMessage = new PersonCourseSectionMessage();
		gridTransformCourseregistrationCreateMessage.setProperty("CourseSection",  courseSection);
		gridTransformCourseregistrationCreateMessage.setProperty("Person",  student);
				
		System.out.println("Now send EnrollStudentTestAction messages...\n");
		gridTransformCourseregistrationCreateMessage.send(seerCount, subPubCount);
		
		System.out.println("Now send DropStudentTestAction messages...\n");
		gridTransformCourseregistrationDeleteMessage.send(seerCount, subPubCount);
	}

}
