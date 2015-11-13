package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.model.CourseSection;

public abstract class ModifyCourseSectionTestAction implements TestAction {
	protected List<Message> messages;
	
	protected CourseSection courseSection;
	protected String verb;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute CreateCourseSectionTestAction - "
					+ "CourseSection not specified");
		}
		if (verb == null) {
			throw new InvalidStateException("Failed to execute CreateCourseSectionTestAction - "
					+ "verb not specified");
		}
	}
}
