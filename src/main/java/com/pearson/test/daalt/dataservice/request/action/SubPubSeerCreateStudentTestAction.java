package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.request.message.PersonMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.model.Student;

public class SubPubSeerCreateStudentTestAction extends CreateStudentTestAction {

	public SubPubSeerCreateStudentTestAction(Student student) {
		messages = new ArrayList<>();
		this.student = student;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing CreateStudentTestAction...\n");
		
		if(student.getGivenName() == null)
			student.setGivenName("FirstName");
		if(student.getFamilyName() == null)
			student.setFamilyName("LastName");
		
		//create and load a grid.daalt.transform.identityprofile.created message
		Message gridTransformIdmCreate = new PersonMessage();
		gridTransformIdmCreate.setProperty("User", student);
				
		messages.add(gridTransformIdmCreate);
		
		for (Message msg : messages) {
			msg.send( seerCount,subPubCount);
		}
	}

}
