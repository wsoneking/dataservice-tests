package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.request.message.PersonCourseSectionMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.PersonMessage;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Student;

public class SubPubSeerEnrollStudentTestAction extends EnrollStudentTestAction {
	
	public SubPubSeerEnrollStudentTestAction(Student student, CourseSection courseSection) {
		messages = new ArrayList<>();
		this.student = student;
		this.courseSection = courseSection;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing EnrollStudentTestAction...\n");
		
		// Send the Person message - June 17
		Message personMessage = new PersonMessage();
		personMessage.setProperty("Person",  student);
		messages.add(personMessage);
		
		
		// grid.daalt.transform.courseregistration.created
		Message gridTransformCourseregistrationCreateMessage = new PersonCourseSectionMessage();
		gridTransformCourseregistrationCreateMessage.setProperty("CourseSection",  courseSection);
		gridTransformCourseregistrationCreateMessage.setProperty("Person",  student);
		
		messages.add(gridTransformCourseregistrationCreateMessage);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
		}
	}
}
