package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.model.Instructor;

public abstract class CreateInstructorTestAction implements TestAction {
	protected List<Message> messages;
	
	protected Instructor instructor;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		if(instructor == null){
			throw new InvalidStateException("Failed to execute CreateInstructorTestAction - "
					+ "Instructor not specified");
		}
	}
}
