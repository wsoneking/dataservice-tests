package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.request.message.Message;

public abstract class ModifyAssessmentItemTestAction implements TestAction {
	protected List<Message> messages;
	
	protected Quiz quiz;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		//check for critical objects
		if (quiz == null) {
			throw new InvalidStateException("Failed to execute AssessmentItemPossibleAnswersTestAction - "
					+ "Quiz not specified");
		}
	}
}
