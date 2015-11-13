package com.pearson.test.daalt.dataservice.request.action.version01;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Attempt;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.request.action.InvalidStateException;
import com.pearson.test.daalt.dataservice.request.action.TestAction;
import com.pearson.test.daalt.dataservice.request.message.version01.Message;

public abstract class AttemptQuestionTestAction implements TestAction {
	protected List<Message> messages;
	
	protected CourseSection courseSection;
	protected Assignment assignment;
	protected Quiz quiz;
	protected Question question;
	protected Attempt attempt;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute AttemptQuestionTestAction - "
					+ "CourseSection not specified");
		}
		if (assignment == null) {
			throw new InvalidStateException("Failed to execute AttemptQuestionTestAction - "
					+ "assignment not specified");
		}
		if (quiz == null) {
			throw new InvalidStateException("Failed to execute AttemptQuestionTestAction - "
					+ "quiz not specified");
		}
		if (question == null) {
			throw new InvalidStateException("Failed to execute AttemptQuestionTestAction - "
					+ "question not specified");
		}
		if (attempt == null) {
			throw new InvalidStateException("Failed to execute AttemptQuestionTestAction - "
					+ "attempt not specified");
		}
	}
}
