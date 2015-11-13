package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.request.message.Message;

public abstract class ReadPageTestAction implements TestAction{
	protected List<Message> messages;

	protected CourseSection courseSection;
	protected LearningActivity learningActivity;
	protected Page page;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute ReadPageTestAction - "
					+ "CourseSection not specified");
		}
		if (page == null) {
			throw new InvalidStateException("Failed to execute ReadPageTestAction - "
					+ "page not specified");
		}
		if (learningActivity == null) {
			throw new InvalidStateException("Failed to execute ReadPageTestAction - "
					+ "learningActivity not specified");
		}
	}
}
