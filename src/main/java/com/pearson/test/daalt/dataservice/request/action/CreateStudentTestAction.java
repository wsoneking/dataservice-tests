package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.request.message.Message;
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
