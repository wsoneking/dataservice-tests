package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.AutoSaveActivity;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LeaveQuestionActivity;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.TOTUserUnloadsQuestionTincanMessage;

public class SubPubSeerLeaveQuestionTestAction extends LeaveQuestionTestAction{

	public SubPubSeerLeaveQuestionTestAction(User user, CourseSection courseSection, 
			Quiz quiz, Question question, LeaveQuestionActivity leaveQuestionActivity,
			AutoSaveActivity autoSaveActivity){
		messages = new ArrayList<>();
		this.user = user;
		this.courseSection = courseSection;
		this.quiz = quiz;
		this.question = question;
		this.leaveQuestionActivity = leaveQuestionActivity;
		this.autoSaveActivity = autoSaveActivity;

	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing LeaveQuestionTestAction...\n");
		
		Message messageTOT = new TOTUserUnloadsQuestionTincanMessage(/*isAttempt*/ false);
		messageTOT.setProperty("CourseSection", courseSection);
		messageTOT.setProperty("Quiz", quiz);
		messageTOT.setProperty("Question", question);
		if (leaveQuestionActivity != null) {
			messageTOT.setProperty("LeaveQuestionActivity", leaveQuestionActivity);
		} else if (autoSaveActivity != null) {
			messageTOT.setProperty("AutoSaveActivity", autoSaveActivity);
		}
		messages.add(messageTOT);
		
	
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
			Thread.sleep(500);
		}
	}

}

