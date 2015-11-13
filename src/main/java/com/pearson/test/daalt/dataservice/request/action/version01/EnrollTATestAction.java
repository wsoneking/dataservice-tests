package com.pearson.test.daalt.dataservice.request.action.version01;

import java.util.List;

import com.pearson.test.daalt.dataservice.request.action.InvalidStateException;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.request.message.version01.Message;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.TA;

public abstract class EnrollTATestAction implements TestAction {
	protected List<Message> messages;
	
	protected CourseSection courseSection;
	protected TA ta;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		//check for critical objects
		if (ta == null) {
			throw new InvalidStateException("Failed to execute AddAssignmentTestAction - "
					+ "TA not specified");
		}
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute AddAssignmentTestAction - "
					+ "CourseSection not specified");
		}
		
	}
}
