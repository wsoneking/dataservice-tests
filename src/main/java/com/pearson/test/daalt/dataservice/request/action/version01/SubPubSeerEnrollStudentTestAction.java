package com.pearson.test.daalt.dataservice.request.action.version01;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.request.message.version01.GridTransformCourseregistrationMessage;
import com.pearson.test.daalt.dataservice.request.message.version01.GridTransformPersonIdentityMessage;
import com.pearson.test.daalt.dataservice.request.message.version01.Message;
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

		// Send the Person message - June 17
		Message personMessage = new GridTransformPersonIdentityMessage();
		personMessage.setProperty("Person",  student);
		messages.add(personMessage);
		
		// grid.daalt.transform.courseregistration.created
		Message gridTransformCourseregistrationCreateMessage = new GridTransformCourseregistrationMessage();
		gridTransformCourseregistrationCreateMessage.setProperty("CourseSection",  courseSection);
		gridTransformCourseregistrationCreateMessage.setProperty("Person",  student);
		
		messages.add(gridTransformCourseregistrationCreateMessage);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
		}
	}
	
	
}
