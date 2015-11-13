package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.UserLoadsContentTincanMessage;

public class SubPubSeerStudentAccessContentTestAction extends StudentAccessContentTestAction {
	public SubPubSeerStudentAccessContentTestAction(CourseSection courseSection,
			 Page page, LearningActivity learningActivity){
		messages = new ArrayList<>();
		this.courseSection = courseSection;
		this.page = page;
		this.learningActivity = learningActivity;
		
	}

	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing StudentAccessQuestionTestAction...\n");
		
		Message messageULC = new UserLoadsContentTincanMessage();
		messageULC.setProperty("CourseSection", courseSection);
		messageULC.setProperty("Page", page);
		messageULC.setProperty("LearningActivity", learningActivity);
	
		messages.add(messageULC);
		Thread.sleep(600); 	

		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
			Thread.sleep(500);
		}
	}

}
