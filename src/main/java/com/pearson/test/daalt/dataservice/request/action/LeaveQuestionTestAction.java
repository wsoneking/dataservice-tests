package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.AutoSaveActivity;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LeaveQuestionActivity;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.message.Message;

public abstract class LeaveQuestionTestAction implements TestAction{
	protected List<Message> messages;

	protected User user;
	protected CourseSection courseSection;
	protected Quiz quiz;
	protected Question question;
	protected LeaveQuestionActivity leaveQuestionActivity;
	protected AutoSaveActivity autoSaveActivity;

	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		if (user == null) {
			throw new InvalidStateException("Failed to execute LeaveQuestionTestAction - "
					+ "user not specified");
		}
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
		if (leaveQuestionActivity == null && autoSaveActivity == null) {
			throw new InvalidStateException("Failed to execute LeaveQuestionTestAction - "
					+ "leaveQuestionActivity and autoSaveActivity not specified");
		}
	}
}
