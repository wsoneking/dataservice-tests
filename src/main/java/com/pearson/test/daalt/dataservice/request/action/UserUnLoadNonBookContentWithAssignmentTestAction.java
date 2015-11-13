package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LastActivityDate;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.request.message.Message;

public abstract class UserUnLoadNonBookContentWithAssignmentTestAction implements TestAction {

	protected List<Message> messages;

	protected CourseSection courseSection;
	protected Assignment assignment;
	protected LastActivityDate lastActivityDate;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute UserUnLoadNonBookContentWithAssignmentTestAction - "
					+ "CourseSection not specified");
		}
		if (assignment == null) {
			throw new InvalidStateException("Failed to execute UserUnLoadNonBookContentWithAssignmentTestAction - "
					+ "page not specified");
		}
		if (lastActivityDate == null) {
			throw new InvalidStateException("Failed to execute UserUnLoadNonBookContentWithAssignmentTestAction - "
					+ "lastActivityDate not specified");
		}
}

}