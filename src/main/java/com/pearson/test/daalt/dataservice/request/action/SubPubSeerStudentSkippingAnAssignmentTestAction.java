package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.BasicStudent;
import com.pearson.test.daalt.dataservice.model.CompletionActivityOriginCode;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.message.LearningModuleMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;

public class SubPubSeerStudentSkippingAnAssignmentTestAction extends StudentSkippingAnAssignmentTestAction{

	public SubPubSeerStudentSkippingAnAssignmentTestAction(Instructor instructor, CourseSection courseSection, 
			Assignment assignment){
		messages = new ArrayList<>();
		this.instructor = instructor;
		this.courseSection = courseSection;
		this.assignment = assignment;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing Student skipping test action...\n");
		
		User system = new BasicStudent (null, null, null, null, null);
		system.setPersonRole(CompletionActivityOriginCode.SYSTEM.value);
		
		//Learning_Module message to update the Assignment
		Message revelLmUpdateMessage = new LearningModuleMessage();
		revelLmUpdateMessage.setProperty("Transaction_Type_Code", "Update");
		revelLmUpdateMessage.setProperty("CourseSection",  courseSection);
		revelLmUpdateMessage.setProperty("Instructor",  instructor);
		revelLmUpdateMessage.setProperty("Assignment", assignment);
		messages.add(revelLmUpdateMessage);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
		}
	}

}
