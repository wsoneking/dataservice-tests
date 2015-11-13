package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.User;

public abstract class EnrollUserPreTransformTestAction implements TestAction {
	protected List<Message> messages;
	
	protected CourseSection courseSection;
	protected User usr;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		//check for critical objects
		if (usr == null) {
			throw new InvalidStateException("Failed to execute EnrollUserPreTransformTestAction - "
					+ "instr not specified");
		}
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute EnrollUserPreTransformTestAction - "
					+ "CourseSection not specified");
		}
		
	}
}
