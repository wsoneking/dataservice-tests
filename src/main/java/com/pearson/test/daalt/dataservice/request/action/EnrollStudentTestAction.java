package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Student;

public abstract class EnrollStudentTestAction implements TestAction {
	protected List<Message> messages;
	
	protected CourseSection courseSection;
	protected Student student;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		//check for critical objects
		if (student == null) {
			throw new InvalidStateException("Failed to execute EnrollStudentTestAction - "
					+ "student not specified");
		}
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute EnrollStudentTestAction - "
					+ "CourseSection not specified");
		}
		
	}
}
