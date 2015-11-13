package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.Attempt;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.request.message.Message;


public abstract class StudentAccessQuestionTestAction implements TestAction {
	protected List<Message> messages;
	protected CourseSection courseSection;
	protected Quiz quiz;
	protected Question question;
	protected Attempt attempt;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
	
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute LeaveQuestionTestAction - "
					+ "CourseSection not specified");
		}
		if (quiz == null) {
			throw new InvalidStateException("Failed to execute LeaveQuestionTestAction - "
					+ "quiz not specified");
		}
		if (question == null) {
			throw new InvalidStateException("Failed to execute LeaveQuestionTestAction - "
					+ "question not specified");
		}
		if (attempt == null) {
			throw new InvalidStateException("Failed to execute learningActivity - "
					+ "attempt not specified");
		}
	}
}
