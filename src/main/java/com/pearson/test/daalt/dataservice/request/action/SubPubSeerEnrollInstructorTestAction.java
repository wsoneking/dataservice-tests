package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.request.message.PersonCourseSectionMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.PersonMessage;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;

public class SubPubSeerEnrollInstructorTestAction extends EnrollInstructorTestAction {
	
	public SubPubSeerEnrollInstructorTestAction(Instructor instr, CourseSection courseSection) {
		messages = new ArrayList<>();
		this.instr = instr;
		this.courseSection = courseSection;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing EnrollInstructorTestAction...\n");

		// Send the Person message - June 17
		Message personMessage = new PersonMessage();
		personMessage.setProperty("Person",  instr);
		messages.add(personMessage);
		
		// grid.daalt.transform.courseregistration.created
		Message gridTransformCourseregistrationCreateMessage = new PersonCourseSectionMessage();
		gridTransformCourseregistrationCreateMessage.setProperty("CourseSection",  courseSection);
		gridTransformCourseregistrationCreateMessage.setProperty("Person",  instr);
		
		messages.add(gridTransformCourseregistrationCreateMessage);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
		}
	}
}
