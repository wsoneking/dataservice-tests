package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.request.message.PersonCourseSectionMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.PersonCourseSectionPreTransformCreate;
import com.pearson.test.daalt.dataservice.request.message.PersonMessage;
import com.pearson.test.daalt.dataservice.request.message.PersonPreTransformCreate;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.User;

public class SubPubSeerEnrollUserPreTransformTestAction extends EnrollUserPreTransformTestAction {
	
	public SubPubSeerEnrollUserPreTransformTestAction(User user, CourseSection courseSection) {
		messages = new ArrayList<>();
		this.usr = user;
		this.courseSection = courseSection;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing EnrollUserPreTransformTestAction...\n");

		Message personMessage = new PersonPreTransformCreate();
		personMessage.setProperty("Person",  usr);
		messages.add(personMessage);
		
		Message gridPreTransformCourseregistrationCreateMessage = new PersonCourseSectionPreTransformCreate();
		gridPreTransformCourseregistrationCreateMessage.setProperty("CourseSection",  courseSection);
		gridPreTransformCourseregistrationCreateMessage.setProperty("Person",  usr);
		
		messages.add(gridPreTransformCourseregistrationCreateMessage);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
		}
	}
}
