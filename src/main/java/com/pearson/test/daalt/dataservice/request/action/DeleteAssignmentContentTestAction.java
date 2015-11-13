package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;

public abstract class DeleteAssignmentContentTestAction implements TestAction {
	protected List<Message> messages;
	
	protected Instructor instructor;
	protected CourseSection courseSection;
	protected Assignment assignment;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		//check for critical objects
		if (instructor == null) {
			throw new InvalidStateException("Failed to execute DeleteAssignmentContentTestAction - "
					+ "Instructor not specified");
		}
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute DeleteAssignmentContentTestAction - "
					+ "CourseSection not specified");
		}
		if (assignment == null) {
			throw new InvalidStateException("Failed to execute DeleteAssignmentContentTestAction - "
					+ "Assignment not specified");
		}
	}
}
