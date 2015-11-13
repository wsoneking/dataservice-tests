package com.pearson.test.daalt.dataservice.request.action.version01;

import java.util.List;

import com.pearson.test.daalt.dataservice.request.action.InvalidStateException;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.request.message.version01.Message;
import com.pearson.test.daalt.dataservice.model.Student;

public abstract class CreateStudentTestAction implements TestAction {
	protected List<Message> messages;
	
	protected Student student;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		if(student == null){
			throw new InvalidStateException("Failed to execute CreateStudentTestAction - "
					+ "Student not specified");
		}
	}
}
