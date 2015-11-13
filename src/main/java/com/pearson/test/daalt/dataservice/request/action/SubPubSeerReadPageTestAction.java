package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.request.message.UserLoadsContentTincanMessage;
import com.pearson.test.daalt.dataservice.request.message.UserUnloadsContentTincanMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;

public class SubPubSeerReadPageTestAction extends ReadPageTestAction{

	public SubPubSeerReadPageTestAction(CourseSection courseSection, 
			Page page, LearningActivity learningActivity){
		messages = new ArrayList<>();
		this.courseSection = courseSection;
		this.page = page;
		this.learningActivity = learningActivity;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing ReadPageTestAction...\n");

		Message message1 = new UserLoadsContentTincanMessage();
		message1.setProperty("CourseSection", courseSection);
		message1.setProperty("Page", page);
		message1.setProperty("LearningActivity", learningActivity);

		messages.add(message1);
		
		Thread.sleep(500);  	// Load and Unload have 0.5 sec difference. 
		
		Message message2 = new UserUnloadsContentTincanMessage();
		message2.setProperty("CourseSection", courseSection);
		message2.setProperty("Page", page);
		message2.setProperty("LearningActivity", learningActivity);
		
		messages.add(message2);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
			Thread.sleep(500);
		}
	}

}
