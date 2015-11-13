package com.pearson.test.daalt.dataservice.scenario.singleMsg;

import java.io.IOException;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.model.BasicStudent;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.request.message.InvalidStateException;
import com.pearson.test.daalt.dataservice.request.message.PersonPreTransformCreate;
import com.pearson.test.daalt.dataservice.request.message.UnknownPropertyException;

public class PersonPreTransformSingleMsg extends BaseTestSingleMessage {

	String requiredField = "Course_Section_Source_System_Record_Id";
	
	@Test
	public void validMessage() throws Exception {

		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);
		
		// send the message 
		try {
			String messagePayloadAsJson = getPersonPreTransformMessage(uuid);
			kafkaHelper.publishMessage(messagePayloadAsJson, "Idm.IdentityProfile.Created");

		} catch (Exception e) {
			throw e;
		}

	}

	
	public String getPersonPreTransformMessage(String uuid) throws JsonGenerationException, JsonMappingException, IOException {

		PersonPreTransformCreate msg = new PersonPreTransformCreate();
		Student student = new BasicStudent("Username001","Password27","userId-"+uuid,"FirstName","LastName");
		try {
			msg.setProperty("Person", student);
		} catch (UnknownPropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String messagePayloadAsJson = new ObjectMapper().writeValueAsString(msg.payload);
		System.out.println("messageAsJson: "+ messagePayloadAsJson);
		
		return messagePayloadAsJson;
	}
}
