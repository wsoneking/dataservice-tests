package com.pearson.test.daalt.dataservice.scenario.singleMsg;

import java.io.IOException;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.request.message.PersonCourseSectionMessage;
import com.pearson.test.daalt.dataservice.request.message.PersonCourseSectionMessage.PersonCourseSectionMsgPayload;

public class PersonCourseSectionMessageValidityTest extends BaseTestSingleMessage {

	String requiredField = "Course_Section_Source_System_Record_Id";
	
	@Test
	public void validMessage() throws Exception {

		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);
		
		// send the message 
		try {
			String messagePayloadAsJson = getPersonCourseSectionMessage(uuid);
			// 1. valid message: pass
			kafkaHelper.publishMessage(messagePayloadAsJson);

		} catch (Exception e) {
			throw e;
		}

		// validation 
		SubpubRawValidation(uuid);
		SubpubValidValidation(uuid);
	}
	
	@Test
	public void missingField() throws Exception {

		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);
		
		// send the message 
		try {
			String messagePayloadAsJson = getPersonCourseSectionMessage(uuid);
			// 2. missing a required field: error
			String missingField = removeThisField(messagePayloadAsJson,requiredField);
			System.out.println("New message  : "+ missingField);
			kafkaHelper.publishMessage(missingField);

		} catch (Exception e) {
			throw e;
		}

		// validation 
		SubpubRawValidation(uuid);
		SubpubErrorValidation(uuid);
	}

	@Test
	public void valueNull() throws Exception {

		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);
		
		// send the message 
		try {
			String messagePayloadAsJson = getPersonCourseSectionMessage(uuid);
			// 3. required property's value is null: error
			String valueNull = setThisFieldNull(messagePayloadAsJson,requiredField);
			System.out.println("New message  : "+ valueNull);
			kafkaHelper.publishMessage(valueNull);
		} catch (Exception e) {
			throw e;
		}
		// validation 
		SubpubRawValidation(uuid);
		SubpubErrorValidation(uuid);
	}
	
	//TODO: introduce some generic way to send invalid values for multiple fields
	@Test
	public void typeWrong() throws Exception {
		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);

		// send the message 
		try {
			String messagePayloadAsJson = getPersonCourseSectionMessage(uuid);
			// 4. required property's value type wrong: error
			String typeWrong = messagePayloadAsJson.replace("\"Person_Role_Code\":\"Student\",", "\"Person_Role_Code\":3,");
			System.out.println("New message  : "+ typeWrong);
			kafkaHelper.publishMessage(typeWrong);
		} catch (Exception e) {
			throw e;
		}
		// validation 
		SubpubRawValidation(uuid);
		SubpubErrorValidation(uuid);
	}
	
	public String getPersonCourseSectionMessage(String uuid) throws JsonGenerationException, JsonMappingException, IOException {

		PersonCourseSectionMessage msg = new PersonCourseSectionMessage();
		PersonCourseSectionMsgPayload msgpayload = (PersonCourseSectionMsgPayload) msg.payload;
		
		msgpayload.Course_Section_Source_System_Record_Id = "SQE-CS-"+uuid;
		msgpayload.Person_Source_System_Record_Id = "SQE-Person-"+UUID.randomUUID().toString();
		msgpayload.Person_Role_Code = "Student";
		
		msg.checkCriticalProperties = false;
		String messagePayloadAsJson = new ObjectMapper().writeValueAsString(msgpayload);
		System.out.println("messageAsJson: "+ messagePayloadAsJson);
		
		return messagePayloadAsJson;
	}
}
