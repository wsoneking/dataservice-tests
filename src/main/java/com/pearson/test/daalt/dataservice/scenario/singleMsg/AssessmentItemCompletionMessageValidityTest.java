package com.pearson.test.daalt.dataservice.scenario.singleMsg;

import java.io.IOException;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.request.message.AssessmentItemCompletionMessage;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemCompletionMessage.AssessmentItemCompletionMsgPayload;
/**
 * 
 * @author Jin
 *		update the hosts file with the following lines
		10.252.5.145 daalt-kafka-ver1-01
		10.252.5.147 daalt-kafka-ver1-02
		10.252.5.63 daalt-kafka-ver1-03
 *
 */


public class AssessmentItemCompletionMessageValidityTest extends BaseTestSingleMessage {

	String requiredField = "Person_Role_Code";
	String optionalField = "Possible_Points";
	
	@Test
	public void validMessage() throws Exception {

		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);
		
		// send the message 
		try {
			String messagePayloadAsJson = getMessage(uuid);
			// 1. valid message: pass
			kafkaHelper.publishMessage(messagePayloadAsJson);

		} catch (Exception e) {
			throw e;
		}

		// validation 
		SubpubRawValidation(uuid);
		SubpubValidValidation(uuid);
	}
/*	
	@Test
	public void missingField() throws Exception {

		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);
		
		// send the message 
		try {
			String messagePayloadAsJson = getMessage(uuid);
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
			String messagePayloadAsJson = getMessage(uuid);
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
			String messagePayloadAsJson = getMessage(uuid);
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
			String messagePayloadAsJson = getMessage(uuid);
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

	
	// TODO Set type wrong method is not general
	@Test
	public void typeWrong() throws Exception {
		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);

		// send the message 
		try {
			String messagePayloadAsJson = getMessage(uuid);
			// 4. required property's value type wrong: error
			String typeWrong = messagePayloadAsJson.replace("\"Possible_Points\":100.0,", "\"Possible_Points\":\"100.0\",");
			System.out.println("New message  : "+ typeWrong);
			kafkaHelper.publishMessage(typeWrong);
		} catch (Exception e) {
			throw e;
		}
		// validation 
		SubpubRawValidation(uuid);
		SubpubErrorValidation(uuid);
	}
*/
	
	
	public String getMessage(String uuid) throws JsonGenerationException, JsonMappingException, IOException {

		AssessmentItemCompletionMessage msg = new AssessmentItemCompletionMessage();
		AssessmentItemCompletionMsgPayload msgpayload = (AssessmentItemCompletionMsgPayload) msg.payload;
		msgpayload.Course_Section_Source_System_Record_Id = "SQE-CS-"+uuid;
		msgpayload.Person_Source_System_Record_Id = "SQE-Person-" + UUID.randomUUID().toString();
		msgpayload.Assessment_Source_System_Record_Id = "SQE-Assess-" + UUID.randomUUID().toString();
		msgpayload.Assessment_Item_Source_System_Record_Id = "SQE-AssessItem-" + UUID.randomUUID().toString();
		msgpayload.Ref_Assessment_Item_Completion_Source_Code = "Student";
		msgpayload.Possible_Points = 100f;
		
		msg.checkCriticalProperties = false;
		String messagePayloadAsJson = new ObjectMapper().writeValueAsString(msgpayload);
		System.out.println("messageAsJson: "+ messagePayloadAsJson);
		
		return messagePayloadAsJson;
	}
}
