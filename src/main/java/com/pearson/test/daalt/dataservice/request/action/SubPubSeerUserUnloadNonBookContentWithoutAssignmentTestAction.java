package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LastActivityDate;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.UserUnloadsNonBookContentWithoutAssignmentTinCanMessage;

public class SubPubSeerUserUnloadNonBookContentWithoutAssignmentTestAction extends UserUnLoadNonBookContentWithoutAssignmentTestAction{
	public SubPubSeerUserUnloadNonBookContentWithoutAssignmentTestAction(CourseSection courseSection,
			LastActivityDate lastActivityDate){
		messages = new ArrayList<>();
		this.courseSection = courseSection;
		this.lastActivityDate = lastActivityDate;
		
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing UserUnLoadNonBookContentWithoutAssignmentTestAction...\n");
		
		Message message = new  UserUnloadsNonBookContentWithoutAssignmentTinCanMessage();
		message.setProperty("CourseSection", courseSection);
		message.setProperty("LastActivityDate", lastActivityDate);
	
		
		messages.add(message);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
			Thread.sleep(500);
		}
	}
}
