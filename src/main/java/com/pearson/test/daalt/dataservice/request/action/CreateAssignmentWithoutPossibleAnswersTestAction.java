package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.request.message.Message;

public abstract class CreateAssignmentWithoutPossibleAnswersTestAction implements TestAction {
	protected List<Message> messages;

	protected Instructor instructor;
	protected CourseSection courseSection;
	protected Assignment assignment;
	protected Question question;

	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		// check for critical objects
		if (instructor == null) {
			throw new InvalidStateException("Failed to execute AddAssignmentTestAction - " + "Instructor not specified");
		}
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute AddAssignmentTestAction - "
					+ "CourseSection not specified");
		}
		if (assignment == null) {
			throw new InvalidStateException("Failed to execute AddAssignmentTestAction - " + "Assignment not specified");
		}
		if (question == null) {
			throw new InvalidStateException("Failed to execute AddAssignmentTestAction - " + "Question not specified");
		}
	}

}
