package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.request.message.Message;

public abstract class DropCourseAndEnrollAgainButOutOfOrderTestAction implements TestAction  {
	protected List<Message> messages;
	
	protected CourseSection courseSection;
	protected Student student;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		//check for critical objects
		if (student == null) {
			throw new InvalidStateException("Failed to execute DropCourseTestAction - "
					+ "student not specified");
		}
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute DropCourseTestAction - "
					+ "CourseSection not specified");
		}
		
	}

}
