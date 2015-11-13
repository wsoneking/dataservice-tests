package com.pearson.test.daalt.dataservice.request.action.version01;

import java.util.List;

import com.pearson.test.daalt.dataservice.request.action.InvalidStateException;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.request.message.version01.Message;
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
