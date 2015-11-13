package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.request.message.Message;

public abstract class ReSendSeedDataTestAction implements TestAction {
	protected List<Message> messages;
	
	protected CourseSection courseSection;
	protected Assignment assignment;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		//check for critical objects
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute AddAssignmentTestAction - "
					+ "CourseSection not specified");
		}
		if (assignment == null) {
			throw new InvalidStateException("Failed to execute AddAssignmentTestAction - "
					+ "Assignment not specified");
		}
	}
}
