package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CompletionActivityOriginCode;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionCompletionActivity;
import com.pearson.test.daalt.dataservice.model.QuestionPresentationFormat;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemCompletionMessage;
import com.pearson.test.daalt.dataservice.request.message.AssessmentPerformanceMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningModulePerformanceMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.SimpleWritingQuestionUserAnsweredTincanMessage;
import com.pearson.test.daalt.dataservice.request.message.TOTUserUnloadsQuestionTincanMessage;

public class SubPubSeerQuestionCompletionSimpleWritingTestAction extends QuestionCompletionSimpleWritingTestAction {

	public SubPubSeerQuestionCompletionSimpleWritingTestAction(CourseSection courseSection, Assignment assignment,
																Quiz quiz, Question question, QuestionCompletionActivity questionCompletionActivity,
																QuizCompletionActivity quizCompletionActivity) {
		messages = new ArrayList<>();
		this.courseSection = courseSection;
		this.assignment = assignment;
		this.quiz = quiz;
		this.question = question;
		this.questionCompletionActivity = questionCompletionActivity;
		this.quizCompletionActivity = quizCompletionActivity;
	}

	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing QuestionCompletionSimpleWritingTestAction...\n");
		
		User student = questionCompletionActivity.getPerson();

		Message message;		
		if (question.getQuestionPresentationFormat().equals(QuestionPresentationFormat.JOURNAL.value)) {
			message = new SimpleWritingQuestionUserAnsweredTincanMessage(true);
		} else {
			message = new SimpleWritingQuestionUserAnsweredTincanMessage(false);
		}		
		message.setProperty("CourseSection", courseSection);
		message.setProperty("Quiz", quiz);
		message.setProperty("Question", question);
		message.setProperty("QuestionCompletionActivity", questionCompletionActivity);
		messages.add(message);		

		Message messageTOT = new TOTUserUnloadsQuestionTincanMessage(/* isAttempt */false);
		messageTOT.setProperty("CourseSection", courseSection);
		messageTOT.setProperty("Quiz", quiz);
		messageTOT.setProperty("Question", question);
		messageTOT.setProperty("QuestionCompletionActivity", questionCompletionActivity);
		messages.add(messageTOT);

		Message assessmentItemCompletionMessage = new AssessmentItemCompletionMessage();
		assessmentItemCompletionMessage.setProperty("CourseSection", courseSection);
		assessmentItemCompletionMessage.setProperty("Quiz", quiz);
		assessmentItemCompletionMessage.setProperty("Question", question);
		assessmentItemCompletionMessage.setProperty("Person", quizCompletionActivity.getPerson());
		assessmentItemCompletionMessage.setProperty("CompletionOriginator", CompletionActivityOriginCode.STUDENT.value);
		messages.add(assessmentItemCompletionMessage);

		quizCompletionActivity.setAssignmentComplete(assignment.studentCompletedAssignment(student));
		Message assessmentPerformanceMessage = new AssessmentPerformanceMessage(student,
																				question.getQuestionPresentationFormatAsEnum());
		assessmentPerformanceMessage.setProperty("CourseSection", courseSection);
		assessmentPerformanceMessage.setProperty("Assignment", assignment);
		assessmentPerformanceMessage.setProperty("Quiz", quiz);
		assessmentPerformanceMessage.setProperty("QuizCompletionActivity", quizCompletionActivity);
		messages.add(assessmentPerformanceMessage);

//		if(assignment.studentCompletedAssignment(student)) {
//			Message lmPerfMessage = new LearningModulePerformanceMessage(student);
//			lmPerfMessage.setProperty("Transaction_Type_Code", "Create");
//			lmPerfMessage.setProperty("CompletionOriginator", CompletionActivityOriginCode.STUDENT.value);
//			lmPerfMessage.setProperty("CourseSection", courseSection);
//			lmPerfMessage.setProperty("Assignment", assignment);
//			messages.add(lmPerfMessage);
//		}

		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
			Thread.sleep(500);
		}
	}
}
