package com.pearson.test.daalt.dataservice.scenario.singleMsg;

import java.io.IOException;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.request.message.MultiValueQuestionUserAnsweredTincanMessage;
import com.pearson.test.daalt.dataservice.request.message.MultiValueQuestionUserAnsweredTincanMessage.MultiValueQuestionUserAnsweredTincanMessageSource;
import com.pearson.test.daalt.dataservice.request.message.MultiValueQuestionUserAnsweredTincanMessage.MultiValueQuestionUserAnsweredTincanMessageSource.Context.FinalAttemptExtension;
import com.pearson.test.daalt.dataservice.request.message.MultiValueQuestionUserAnsweredTincanMessage.MultiValueQuestionUserAnsweredTincanMessageSource.Context.Extension.StudentResponse;

/**
 *
 */

public class MultiValueQuestionUserAnsweredTincanMessageValidityTest extends BaseTestSingleMessage {

	String requiredField = "Course_Section_Source_System_Code";
	String optionalField = "Product_Source_System_Record_Id";
	
	@Test
	public void validMessage() throws Exception {

		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);
		
		// send the message 
		try {
			String messagePayloadAsJson = getMessage(uuid);
			// 1. valid message: pass
			kafkaHelper.publishTincanMessage(messagePayloadAsJson); 

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
			kafkaHelper.publishTincanMessage(missingField);

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
			kafkaHelper.publishTincanMessage(valueNull);
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
			kafkaHelper.publishTincanMessage(optionalMiss);
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
			kafkaHelper.publishTincanMessage(optionalNull);
		} catch (Exception e) {
			throw e;
		}
		// validation 
		SubpubRawValidation(uuid);
		SubpubErrorValidation(uuid);
	}
*/
/*	
	// FUTURE Set type wrong method is not general
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

		MultiValueQuestionUserAnsweredTincanMessage msg = new MultiValueQuestionUserAnsweredTincanMessage(false);
		MultiValueQuestionUserAnsweredTincanMessageSource msgSource = (MultiValueQuestionUserAnsweredTincanMessageSource) msg.messageSource;
		
		msgSource.actor.account.Person_Source_System_Record_Id = "" + UUID.randomUUID().toString();
		
		msgSource.context.extensions = msgSource.context.new FinalAttemptExtension();
		msgSource.context.extensions.Course_Section_Source_System_Record_Id = "SQE-CS-" + UUID.randomUUID().toString();
		msgSource.context.extensions.Person_Role_Code = "Student";
		msgSource.context.extensions.Assessment_Source_System_Record_Id = "SQE-assess-" + UUID.randomUUID().toString();
		msgSource.context.extensions.Assessment_Item_Source_System_Record_Id = "SQE-assessItem-" + UUID.randomUUID().toString();
		msgSource.context.extensions.Assessment_Item_Question_Type = "MultiValue";
		msgSource.context.extensions.Assessment_Item_Response_Code = "Correct";
		msgSource.context.extensions.Attempt_Number = 3;
		FinalAttemptExtension specificExtension = (FinalAttemptExtension) msgSource.context.extensions;
		specificExtension.Item_Response_Score = 90;
		
		StudentResponse resp = msgSource.context.extensions.new StudentResponse();
		resp.Answer_Id = "SQE-ans-" + UUID.randomUUID().toString();
		resp.Target_Id = "SQE-tar-" + UUID.randomUUID().toString();
		resp.Target_Sub_Question_Response_Code = "Correct";
		msgSource.context.extensions.Student_Response.add(resp);	
		
		String messagePayloadAsJson = new ObjectMapper().writeValueAsString(msgSource);
		System.out.println("messageAsJson: "+ messagePayloadAsJson);
		
		return messagePayloadAsJson;
	}
}
