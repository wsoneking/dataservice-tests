package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LastActivityDate;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.UserUnloadsNonBookContentWithAssignmentTinCanMessage;

public class SubPubSeerUserUnloadNonBookContentWithAssignmentTestAction extends UserUnLoadNonBookContentWithAssignmentTestAction{
	public SubPubSeerUserUnloadNonBookContentWithAssignmentTestAction(CourseSection courseSection,
			Assignment assignment, LastActivityDate lastActivityDate ){
		messages = new ArrayList<>();
		this.courseSection = courseSection;
		this.assignment = assignment;
		this.lastActivityDate = lastActivityDate;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing UserUnLoadNonBookContentWithAssignmentTestAction...\n");
		
		Message message = new UserUnloadsNonBookContentWithAssignmentTinCanMessage();
		message.setProperty("CourseSection", courseSection);
		message.setProperty("Assignment", assignment);
		message.setProperty("LastActivityDate", lastActivityDate);
		
		messages.add(message);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
			Thread.sleep(500);
		}
	}
}
