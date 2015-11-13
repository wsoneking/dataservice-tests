package com.pearson.test.daalt.dataservice.scenario.singleMsg;

import java.io.IOException;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.request.message.LearningResourceMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningResourceMessage.LearningResourceMsgPayload;
import com.pearson.test.daalt.dataservice.request.message.LearningResourceMessage.LearningResourceSubtypeMsgPayload;

public class LearningResourceMessageValidityTest extends BaseTestSingleMessage {

	String requiredField = "Learning_Resource_Source_System_Record_Id";
	String optionalField = "Ref_Learning_Resource_Subtype_Code";
	
	@Test
	public void validMessage() throws Exception {

		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);
		
		// send the message 
		try {
			String messagePayloadAsJson = getLearningResourceMessage(uuid);
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
			String messagePayloadAsJson = getLearningResourceMessage(uuid);
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
			String messagePayloadAsJson = getLearningResourceMessage(uuid);
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


	@Test
	public void optionalMiss() throws Exception {
		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);
		// send the message 
		try {
			String messagePayloadAsJson = getLearningResourceMessage(uuid);
			// 5. optional property missing: pass
			String optionalMiss = removeThisField(messagePayloadAsJson,optionalField);
			System.out.println("New message  : "+ optionalMiss);
			kafkaHelper.publishMessage(optionalMiss);
		} catch (Exception e) {
			throw e;
		}
		// validation 
		SubpubRawValidation(uuid);
		SubpubValidValidation(uuid);
	}

	
	@Test
	public void optionalNull() throws Exception {
		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);

		// send the message 
		try {
			String messagePayloadAsJson = getLearningResourceMessage(uuid);
			// 6. optional property shown, but value is null: error
			String optionalNull = setThisFieldNull(messagePayloadAsJson,optionalField);
			System.out.println("New message  : "+ optionalNull);
			kafkaHelper.publishMessage(optionalNull);
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
			String messagePayloadAsJson = getLearningResourceMessage(uuid);
			// 4. required property's value type wrong: error
			String typeWrong = messagePayloadAsJson.replace("\"Ref_Learning_Resource_Subtype_Code\":\"Quiz\",", "\"Ref_Learning_Resource_Subtype_Code\":100.0,");
			System.out.println("New message  : "+ typeWrong);
			kafkaHelper.publishMessage(typeWrong);
		} catch (Exception e) {
			throw e;
		}
		// validation 
		SubpubRawValidation(uuid);
		SubpubErrorValidation(uuid);
	}
	
	public String getLearningResourceMessage(String uuid) throws JsonGenerationException, JsonMappingException, IOException {

		LearningResourceMessage msg = new LearningResourceMessage(/*contentIsEmbeddedQuestion*/ false, /*learningResourceHasSubtype*/ true);
		LearningResourceSubtypeMsgPayload msgpayload = (LearningResourceSubtypeMsgPayload) msg.payload;

		msgpayload.Learning_Resource_Source_System_Record_Id = "SQE-LR-"+UUID.randomUUID().toString();
		msgpayload.Ref_Learning_Resource_Type_Code = "Assessment";
		msgpayload.Ref_Learning_Resource_Subtype_Code = "Quiz";
		
		msg.checkCriticalProperties = false;
		String messagePayloadAsJson = new ObjectMapper().writeValueAsString(msgpayload);
		System.out.println("messageAsJson: "+ messagePayloadAsJson);
		
		return messagePayloadAsJson;
	}
}
