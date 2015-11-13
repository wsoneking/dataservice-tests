package com.pearson.test.daalt.dataservice.request.action.version01;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Attempt;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.request.message.version01.Message;
import com.pearson.test.daalt.dataservice.request.message.version01.MultipleChoiceAnsweredTincanMessage;

public class SubPubSeerAttemptQuestionTestAction extends AttemptQuestionTestAction{

	public SubPubSeerAttemptQuestionTestAction(CourseSection courseSection, 
			Assignment assignment, Quiz quiz, Question question, Attempt attempt){
		messages = new ArrayList<>();
		this.courseSection = courseSection;
		this.assignment = assignment;
		this.quiz = quiz;
		this.question = question;
		this.attempt = attempt;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		Message message = new MultipleChoiceAnsweredTincanMessage();
		message.setProperty("CourseSection", courseSection);
		message.setProperty("Assignment", assignment);
		message.setProperty("Quiz", quiz);
		message.setProperty("Question", question);
		message.setProperty("Attempt", attempt);
		
		//FUTURE: if last attempt, send Assessment_Item_Completion
		
		messages.add(message);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
			Thread.sleep(500);
		}
	}

}
