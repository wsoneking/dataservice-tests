package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LastActivityDate;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.UserLoadsNonBookContentWithAssignmentTincanMessage;

public class SubPubSeerUserLoadNonBookContentWithAssignmentTestAction extends UserLoadNonBookContentWithAssignmentTestAction {
	public SubPubSeerUserLoadNonBookContentWithAssignmentTestAction(CourseSection courseSection,
			Assignment assignment, LastActivityDate lastActivityDate){
		messages = new ArrayList<>();
		this.courseSection = courseSection;
		this.assignment = assignment;
		this.lastActivityDate = lastActivityDate;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now generate User Loads Non Book Content with Assignment with the Course_Section_Source_sytem_Record_Id...\n");
		Message messageUserLoadNonBookContentWithAssignment1 = new UserLoadsNonBookContentWithAssignmentTincanMessage(true);	
		messageUserLoadNonBookContentWithAssignment1.setProperty("CourseSection", courseSection);
		messageUserLoadNonBookContentWithAssignment1.setProperty("Assignment", assignment);
		messageUserLoadNonBookContentWithAssignment1.setProperty("LastActivityDate", lastActivityDate);
		messages.add(messageUserLoadNonBookContentWithAssignment1);
		
		Thread.sleep(2000); 
		
		System.out.println("Now generate User Loads Non Book Content with Assignment without the Course_Section_Source_sytem_Record_Id...\n");
		Message messageUserLoadNonBookContentWithAssignment2 = new UserLoadsNonBookContentWithAssignmentTincanMessage(false);	
		messageUserLoadNonBookContentWithAssignment2.setProperty("Assignment", assignment);
		messageUserLoadNonBookContentWithAssignment2.setProperty("LastActivityDate", lastActivityDate);
		messages.add(messageUserLoadNonBookContentWithAssignment2);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
			Thread.sleep(500);
		}
	}

}
