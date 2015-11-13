package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.request.message.Message;

public abstract class WritingSpaceCompleteQuizTestAction implements TestAction{
	
	protected List<Message> messages;
	
	protected CourseSection courseSection;
	protected Assignment assignment;
	protected Question question;
	protected QuestionCompletionActivity questionCompletionActivity;
	protected Quiz quiz;
	protected QuizCompletionActivity quizCompletionActivity;
	
	
	@Override
	public void checkCriticalObjects() throws InvalidStateException {
		
		if (courseSection == null) {
			throw new InvalidStateException("Failed to execute WritingSpaceCompleteQuizTestAction - "
					+ "CourseSection not specified");
		}
		if (assignment == null) {
			throw new InvalidStateException("Failed to execute WritingSpaceCompleteQuizTestAction - "
					+ "assignment not specified");
		}
		if (question == null) {
			throw new InvalidStateException("Failed to execute WritingSpaceCompleteQuizTestAction - "
					+ "question not specified");
		}
		if (questionCompletionActivity == null) {
			throw new InvalidStateException("Failed to execute WritingSpaceCompleteQuizTestAction - "
					+ "questionCompletionActivity not specified");
		}
		if (quiz == null) {
			throw new InvalidStateException("Failed to execute WritingSpaceCompleteQuizTestAction - "
					+ "quiz not specified");
		}
		if (quizCompletionActivity == null) {
			throw new InvalidStateException("Failed to execute WritingSpaceCompleteQuizTestAction - "
					+ "quizCompletionActivity not specified");
		}
	}
}
