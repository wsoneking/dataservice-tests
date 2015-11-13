package com.pearson.test.daalt.dataservice.request.action.version01;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.action.InvalidStateException;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.request.message.version01.Message;

public abstract class ReadPageTestAction implements TestAction{
	protected List<Message> messages;

	protected User user;
	protected CourseSection courseSection;
	protected Assignment assignment;
	protected LearningActivity learningActivity;
	protected Page page;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		if (user == null) {
			throw new InvalidStateException("Failed to execute ReadPageTestAction - "
					+ "user not specified");
		}
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute ReadPageTestAction - "
					+ "CourseSection not specified");
		}
		if (assignment == null) {
			throw new InvalidStateException("Failed to execute ReadPageTestAction - "
					+ "assignment not specified");
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
