package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;

public abstract class EnrollInstructorTestAction implements TestAction {
	protected List<Message> messages;
	
	protected CourseSection courseSection;
	protected Instructor instr;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		//check for critical objects
		if (instr == null) {
			throw new InvalidStateException("Failed to execute EnrollInstructorTestAction - "
					+ "instr not specified");
		}
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute EnrollInstructorTestAction - "
					+ "CourseSection not specified");
		}
		
	}
}
