package com.pearson.test.daalt.dataservice.scenario.singleMsg;

import java.io.IOException;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.request.message.LearningResourceRelationshipMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningResourceRelationshipMessage.LearningResourceRelationshipMsgPayload;

public class LearningResourceRelationshipMessageValidityTest extends BaseTestSingleMessage {

	String requiredField = "Child_Source_System_Record_Id";
	
	@Test
	public void validMessage() throws Exception {

		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);
		
		// send the message 
		try {
			String messagePayloadAsJson = getLearningResourceRelationshipMessage(uuid);
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
			String messagePayloadAsJson = getLearningResourceRelationshipMessage(uuid);
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
			String messagePayloadAsJson = getLearningResourceRelationshipMessage(uuid);
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
			String messagePayloadAsJson = getLearningResourceRelationshipMessage(uuid);
			// 4. required property's value type wrong: error
			String typeWrong = messagePayloadAsJson.replace("\"Parent_Source_System_Code\":\"Revel\",", "\"Parent_Source_System_Code\":100.0,");
			System.out.println("New message  : "+ typeWrong);
			kafkaHelper.publishMessage(typeWrong);
		} catch (Exception e) {
			throw e;
		}
		// validation 
		SubpubRawValidation(uuid);
		SubpubErrorValidation(uuid);
	}
	
	public String getLearningResourceRelationshipMessage(String uuid) throws JsonGenerationException, JsonMappingException, IOException {

		LearningResourceRelationshipMessage msg = new LearningResourceRelationshipMessage(/*parentResourceIsBook*/ false);
		LearningResourceRelationshipMsgPayload msgpayload = (LearningResourceRelationshipMsgPayload) msg.payload;

		msgpayload.Learning_Resource_Relationship_Context_Id = "SQE-Book-"+UUID.randomUUID().toString();
		msgpayload.Parent_Source_System_Code = "Revel";
		msgpayload.Parent_Source_System_Record_Id = "SQE-PRL-"+UUID.randomUUID().toString();
		msgpayload.Sequence_Number = 0;
		msgpayload.Child_Source_System_Record_Id = "SQE-RL-"+UUID.randomUUID().toString();
		
		msg.checkCriticalProperties = false;
		String messagePayloadAsJson = new ObjectMapper().writeValueAsString(msgpayload);
		System.out.println("messageAsJson: "+ messagePayloadAsJson);
		
		return messagePayloadAsJson;
	}
}
