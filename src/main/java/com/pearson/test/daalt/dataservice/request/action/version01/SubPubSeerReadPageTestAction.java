package com.pearson.test.daalt.dataservice.request.action.version01;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.message.version01.CompleteTincanMessage;
import com.pearson.test.daalt.dataservice.request.message.version01.Message;

public class SubPubSeerReadPageTestAction extends ReadPageTestAction{

	public SubPubSeerReadPageTestAction(User user, CourseSection courseSection, Assignment assignment, 
			Page page, LearningActivity learningActivity){
		messages = new ArrayList<>();
		this.user = user;
		this.courseSection = courseSection;
		this.assignment = assignment;
		this.page = page;
		this.learningActivity = learningActivity;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		Message message = new CompleteTincanMessage();
		message.setProperty("CourseSection", courseSection);
		message.setProperty("Assignment", assignment);
		message.setProperty("Page", page);
		message.setProperty("Activity", learningActivity);
		
		messages.add(message);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
			Thread.sleep(500);
		}
	}

}
