package com.pearson.test.daalt.dataservice.scenario.singleMsg;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.request.message.LearningModuleMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningModuleMessage.LearningModuleMsgPayload;
/**
 *
 */


public class LearningModuleMessageValidityTest extends BaseTestSingleMessage {

	String requiredField = "Originating_System_Code";
	String optionalField = "Assessment_Item_Question_Presentation_Format";
	
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
*/

/*	
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

		LearningModuleMessage msg = new LearningModuleMessage();
		LearningModuleMsgPayload msgpayload = (LearningModuleMsgPayload) msg.payload;
		
		msgpayload.Course_Section_Source_System_Record_Id = "SQE-CS-" + UUID.randomUUID().toString();
/*		
		msgpayload.Learning_Module_Keywords = new ArrayList<>();
		msgpayload.Learning_Module_Keywords.add(msgpayload.new LearningModuleKeyword());
		msgpayload.Learning_Module_URLs = new ArrayList<>();
		msgpayload.Learning_Module_URLs.add(msgpayload.new LearningModuleURL());
*/
		msgpayload.Instructor_Source_System_Record_Id = "SQE-Instr-" + UUID.randomUUID().toString();
		msgpayload.Title = "AssignmentTitle";
		msgpayload.Activity_Due_Datetime = getPastOrFutureTimeFormatted(8);
		msgpayload.Learning_Module_Source_System_Record_Id = "SQE-LM-" + UUID.randomUUID().toString();
		msgpayload.Possible_Points = 100f;
		
		
		msg.checkCriticalProperties = false;
		String messagePayloadAsJson = new ObjectMapper().writeValueAsString(msgpayload);
		System.out.println("messageAsJson: "+ messagePayloadAsJson);
		
		return messagePayloadAsJson;
	}
	
	protected String dateFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public String getPastOrFutureTimeFormatted(int daysToAdd) {
		Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		nowCal.add(Calendar.DAY_OF_YEAR, daysToAdd);
		return new SimpleDateFormat(dateFormatString).format(nowCal.getTime());
	}
}
