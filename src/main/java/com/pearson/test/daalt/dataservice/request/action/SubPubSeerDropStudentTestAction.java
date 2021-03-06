package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.request.message.PersonCourseSectionMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;

public class SubPubSeerDropStudentTestAction extends DropCourseTestAction {

	public SubPubSeerDropStudentTestAction(Student student, CourseSection courseSection) {
		messages = new ArrayList<>();
		this.student = student;
		this.courseSection = courseSection;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing DropStudentTestAction...\n");
		
		Message gridTransformCourseregistrationCreateMessage = new PersonCourseSectionMessage();
		gridTransformCourseregistrationCreateMessage.setProperty("CourseSection", courseSection);
		gridTransformCourseregistrationCreateMessage.setProperty("Person", student);
		gridTransformCourseregistrationCreateMessage.setProperty("Transaction_Type_Code", "Delete");
		
		messages.add(gridTransformCourseregistrationCreateMessage);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
		}
	}

}
