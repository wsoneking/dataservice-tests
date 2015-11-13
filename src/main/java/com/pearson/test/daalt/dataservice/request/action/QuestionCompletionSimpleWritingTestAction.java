package com.pearson.test.daalt.dataservice.request.action;

import java.util.List;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Course;
import com.pearson.test.daalt.dataservice.model.CourseSection;

import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.request.message.Message;

public abstract class QuestionCompletionSimpleWritingTestAction implements TestAction {
	protected List<Message> messages;

	protected Course course;
	protected CourseSection courseSection;
	protected Assignment assignment;
	protected Quiz quiz;
	protected Question question;
	protected QuestionCompletionActivity questionCompletionActivity;
	protected QuizCompletionActivity quizCompletionActivity;

	public void checkCriticalObjects() throws InvalidStateException {

		if (courseSection == null) {
			throw new InvalidStateException(
					"Failed to execute Question Completion Simple Writing Question Test Action - "
							+ "CourseSection not specified");
		}
		if (assignment == null) {
			throw new InvalidStateException(
					"Failed to execute Question Completion Simple Writing Question Test Action - "
							+ "assignment not specified");
		}
		if (quiz == null) {
			throw new InvalidStateException(
					"Failed to execute Question Completion Simple Writing Question Test Action - "
							+ "quiz not specified");
		}
		if (question == null) {
			throw new InvalidStateException(
					"Failed to execute Question Completion Simple Writing Question Test Action - "
							+ "question not specified");
		}

		if (questionCompletionActivity == null) {
			throw new InvalidStateException(
					"Failed to execute Question Completion Simple Writing Question Test Action  - "
							+ "questionCompletionActivity not specified");
		}
		
		if (quizCompletionActivity == null) {
			throw new InvalidStateException(
					"Failed to execute Question Completion Simple Writing Question Test Action  - "
							+ "quizCompletionActivity not specified");
		}
		
	}
}
