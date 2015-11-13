package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.model.CourseSection;

public abstract class CreateCourseSectionTestAction implements TestAction {
	protected List<Message> messages;
	
	protected CourseSection courseSection;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute CreateCourseSectionTestAction - "
					+ "CourseSection not specified");
		}
	}
}
