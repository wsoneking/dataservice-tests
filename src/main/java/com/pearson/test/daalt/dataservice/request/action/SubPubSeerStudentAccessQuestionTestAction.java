package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Attempt;
import com.pearson.test.daalt.dataservice.model.AutoSaveActivity;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.LeaveQuestionActivity;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.TOTUserLoadsQuestionTincanMessage;


public class SubPubSeerStudentAccessQuestionTestAction  extends StudentAccessQuestionTestAction {



public SubPubSeerStudentAccessQuestionTestAction(CourseSection courseSection, Quiz quiz, Question question,
		Attempt attempt){
	messages = new ArrayList<>();
	this.courseSection = courseSection;
	this.quiz = quiz;
	this.question = question;
	this.attempt = attempt;
	

}


@Override
public void execute(int seerCount, int subPubCount) throws Exception {
	checkCriticalObjects();
	
	System.out.println("Now executing StudentAccessQuestionTestAction...\n");
	
	Message messageTOT = new TOTUserLoadsQuestionTincanMessage();
	messageTOT.setProperty("CourseSection", courseSection);
	messageTOT.setProperty("Quiz", quiz);
	messageTOT.setProperty("Question", question);
	messageTOT.setProperty("Attempt", attempt);
	messages.add(messageTOT);
	Thread.sleep(600); 	

	for (Message msg : messages) {
		msg.send(seerCount,subPubCount);
		Thread.sleep(500);
	}
}

}
