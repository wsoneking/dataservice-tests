package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.request.message.Message;

public abstract class StudentAccessContentTestAction implements TestAction {

	protected List<Message> messages;

	protected CourseSection courseSection;
	protected Page page;
	protected LearningActivity learningActivity;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute UserUnLoadNonBookContentWithAssignmentTestAction - "
					+ "CourseSection not specified");
		}
		if (page == null) {
			throw new InvalidStateException("Failed to execute UserUnLoadNonBookContentWithAssignmentTestAction - "
					+ "page not specified");
		}
		if (learningActivity == null) {
			throw new InvalidStateException("Failed to execute UserUnLoadNonBookContentWithAssignmentTestAction - "
					+ "learningActivity not specified");
		}
}
}
