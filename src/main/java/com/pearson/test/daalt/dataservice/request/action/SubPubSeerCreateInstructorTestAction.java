package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.request.message.PersonMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.model.Instructor;

public class SubPubSeerCreateInstructorTestAction extends CreateInstructorTestAction {

	public SubPubSeerCreateInstructorTestAction(Instructor instructor) {
		messages = new ArrayList<>();
		this.instructor = instructor;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {	
		checkCriticalObjects();
		
		System.out.println("Now executing CreateInstructorTestAction...\n");
		
		if(instructor.getGivenName() == null)
			instructor.setGivenName("FirstName");
		if(instructor.getFamilyName() == null)
			instructor.setFamilyName("LastName");
		
		//create and load a grid.daalt.transform.identityprofile.created message
		Message gridTransformIdmCreate = new PersonMessage();
		gridTransformIdmCreate.setProperty("User", instructor);
				
		messages.add(gridTransformIdmCreate);
				
		for (Message msg : messages) {
			msg.send( seerCount,subPubCount);
		}
	}
}
