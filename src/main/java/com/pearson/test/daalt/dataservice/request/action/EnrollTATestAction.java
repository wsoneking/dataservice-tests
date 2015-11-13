package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.request.message.Message;
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
			throw new InvalidStateException("Failed to execute EnrollTATestAction - "
					+ "TA not specified");
		}
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute EnrollTATestAction - "
					+ "CourseSection not specified");
		}
		
	}
}
