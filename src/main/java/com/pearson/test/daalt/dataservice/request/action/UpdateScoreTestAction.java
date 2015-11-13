package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.request.message.Message;

public abstract class UpdateScoreTestAction implements TestAction{
	protected List<Message> messages;
	
	protected CourseSection courseSection;
	protected Assignment assignment;
	protected Quiz quiz;
	protected QuizCompletionActivity quizCompletionActivity;
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute UpdateScoreTestAction - "
					+ "CourseSection not specified");
		}
		if (assignment == null) {
			throw new InvalidStateException("Failed to execute UpdateScoreTestAction - "
					+ "assignment not specified");
		}
		if (quiz == null) {
			throw new InvalidStateException("Failed to execute UpdateScoreTestAction - "
					+ "quiz not specified");
		}
		if (quizCompletionActivity == null) {
			throw new InvalidStateException("Failed to execute UpdateScoreTestAction - "
					+ "quizCompletionActivity not specified");
		}
	}
}
