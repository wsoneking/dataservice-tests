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

import com.pearson.test.daalt.config.DefaultConfig;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.model.BasicStudent;
import com.pearson.test.daalt.dataservice.model.QuestionPresentationFormat;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.request.message.AssessmentPerformanceMessage;
import com.pearson.test.daalt.dataservice.request.message.AssessmentPerformanceMessage.AssessmentPerformanceMsgMultiValuePayload;
import com.pearson.test.daalt.dataservice.request.message.AssessmentPerformanceMessage.AssessmentPerformanceMsgPayload;
import com.pearson.test.daalt.dataservice.request.message.AssessmentPerformanceMessage.AssessmentPerformanceMsgPayload.AssessmentItem;
import com.pearson.test.daalt.dataservice.request.message.LearningModuleMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningModuleMessage.LearningModuleMsgPayload;
/**
 *
 */


public class AssessmentPerformanceMessageValidityTest extends BaseTestSingleMessage {

	String requiredField = "Originating_System_Code";
	String optionalField = "Assignment_Completion_Datetime";
	
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

		Student student = new BasicStudent("JJM1422050327356a@mailinator.com", "Password27", "ffffffff54c2c421e4b0f10ebd0752f4", "First","Last");
		AssessmentPerformanceMessage msg = new AssessmentPerformanceMessage(student, QuestionPresentationFormat.RADIO_BUTTON);
		AssessmentPerformanceMsgMultiValuePayload msgpayload = (AssessmentPerformanceMsgMultiValuePayload) msg.payload;
		
		msgpayload.Course_Section_Source_System_Record_Id = "SQE-CS-" + UUID.randomUUID().toString();

		msgpayload.Course_Points_Possible = 10f;
		msgpayload.Course_Raw_Score = 5f;
		msgpayload.Assignment_Source_System_Record_Id = "SQE-Assign-" + UUID.randomUUID().toString();
		msgpayload.Assignment_Points_Possible = 8f;
		msgpayload.Assignment_Raw_Score = 6f;
		msgpayload.Assignment_Completed = true;
		msgpayload.Assessment_Possible_Points = 4f;
		msgpayload.Student_Source_System_Record_Id = "ffffffff54c2c421e4b0f10ebd0752f4";
		msgpayload.Assessment_Raw_Score = 2f;
		msgpayload.Points_Earned_Original_Score = 2f;
		AssessmentPerformanceMsgMultiValuePayload.AssessmentItem assessmentItem = msgpayload.new AssessmentItem();
		assessmentItem.Assessment_Item_Question_Presentation_Format = "RadioButton";
		assessmentItem.Assessment_Item_Question_Type = "MultiValue";
		assessmentItem.Assessment_Item_Source_System_Record_Id = "SQE-assItem-" + UUID.randomUUID().toString();
		assessmentItem.Correct_Indicator = true;
		assessmentItem.Score = 1f;
		
		
		msg.checkCriticalProperties = false;
		String messagePayloadAsJson = new ObjectMapper().writeValueAsString(msgpayload);
		System.out.println("messageAsJson: "+ messagePayloadAsJson);
		
		return messagePayloadAsJson;
	}
	
}
