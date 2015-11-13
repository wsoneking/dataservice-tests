package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Attempt;
import com.pearson.test.daalt.dataservice.model.CompletionActivityOriginCode;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemCompletionMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.MultiValueQuestionUserAnsweredTincanMessage;
import com.pearson.test.daalt.dataservice.request.message.TOTUserLoadsQuestionTincanMessage;
import com.pearson.test.daalt.dataservice.request.message.TOTUserUnloadsQuestionTincanMessage;

public class SubPubSeerAttemptMultiValueQuestionTestAction extends AttemptMultiValueQuestionTestAction {
	
	public SubPubSeerAttemptMultiValueQuestionTestAction(CourseSection courseSection,
			Assignment assignment, Quiz quiz, Question question, 
			Attempt attempt, QuestionCompletionActivity questionCompletionActivity){
		messages = new ArrayList<>();
		this.courseSection = courseSection;
		this.assignment = assignment;
		this.quiz = quiz;
		this.question = question;
		this.attempt = attempt;
		this.questionCompletionActivity = questionCompletionActivity;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing AttemptMultiValueQuestionTestAction...\n");
		
		boolean finalAttempt = attempt.isFinalAttempt() ? true : false;
		
		Message message = new MultiValueQuestionUserAnsweredTincanMessage(finalAttempt);
		message.setProperty("CourseSection", courseSection);
		message.setProperty("Quiz", quiz);
		message.setProperty("Question", question);
		message.setProperty("Attempt", attempt);
		messages.add(message);
		

		Message messageTOTload = new TOTUserLoadsQuestionTincanMessage();
		messageTOTload.setProperty("CourseSection", courseSection);
		messageTOTload.setProperty("Quiz", quiz);
		messageTOTload.setProperty("Question", question);
		messageTOTload.setProperty("Attempt", attempt);
		messages.add(messageTOTload);
		
		Thread.sleep(600); 			// 
		
		Message messageTOT = new TOTUserUnloadsQuestionTincanMessage(/*isAttempt*/ true);
		messageTOT.setProperty("CourseSection", courseSection);
		messageTOT.setProperty("Quiz", quiz);
		messageTOT.setProperty("Question", question);
		messageTOT.setProperty("Attempt", attempt);
		messages.add(messageTOT);
		
	
		//if last attempt, send Assessment_Item_Completion
		if(finalAttempt){
			Message assessmentItemCompletionMessage = new AssessmentItemCompletionMessage();
			assessmentItemCompletionMessage.setProperty("CourseSection", courseSection);
			assessmentItemCompletionMessage.setProperty("Quiz", quiz);
			assessmentItemCompletionMessage.setProperty("Question", question);
			assessmentItemCompletionMessage.setProperty("Person", questionCompletionActivity.getPerson());
			assessmentItemCompletionMessage.setProperty("CompletionOriginator", CompletionActivityOriginCode.STUDENT.value);
			messages.add(assessmentItemCompletionMessage);
		}

		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
			Thread.sleep(500);
		}
	}

}
