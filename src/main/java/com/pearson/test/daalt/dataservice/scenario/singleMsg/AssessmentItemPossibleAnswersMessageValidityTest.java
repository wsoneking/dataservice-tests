package com.pearson.test.daalt.dataservice.scenario.singleMsg;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.model.QuestionType;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemPossibleAnswers;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemPossibleAnswers.AssessmentItemPossibleAnswersMultiValueMsgPayload.MultiValueAnswerData.Target.TargetCorrectResponse;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemPossibleAnswers.AssessmentItemPossibleAnswersMultiValueMsgPayload;
/**
 *
 */


public class AssessmentItemPossibleAnswersMessageValidityTest extends BaseTestSingleMessage {

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

		AssessmentItemPossibleAnswers msg = new AssessmentItemPossibleAnswers(QuestionType.MULTI_VALUE.value);
		AssessmentItemPossibleAnswersMultiValueMsgPayload msgpayload = (AssessmentItemPossibleAnswersMultiValueMsgPayload) msg.payload;
		msgpayload.Assessment_Item_Question_Presentation_Format = "RadioButton";
		msgpayload.Assessment_Item_Question_Type = "MultiValue";
		msgpayload.Question_Text = "This is question text. ";
		msgpayload.Multi_Value_Answer_Data = msgpayload.new MultiValueAnswerData();
		com.pearson.test.daalt.dataservice.request.message.AssessmentItemPossibleAnswers.AssessmentItemPossibleAnswersMultiValueMsgPayload.MultiValueAnswerData.Answer ans 
			= msgpayload.Multi_Value_Answer_Data.new Answer();
		ans.Answer_Id = "SQE-answer-"+ UUID.randomUUID().toString();
		ans.Answer_Text = "This is answer text. ";
		msgpayload.Multi_Value_Answer_Data.Answers.add(ans);
		com.pearson.test.daalt.dataservice.request.message.AssessmentItemPossibleAnswers.AssessmentItemPossibleAnswersMultiValueMsgPayload.MultiValueAnswerData.Target target 
			= msgpayload.Multi_Value_Answer_Data.new Target();
		target.Target_Id = "SQE-target-" + UUID.randomUUID().toString();
		target.Target_Text = "Target text. ";
		TargetCorrectResponse resp = target.new TargetCorrectResponse();
		resp.Target_Correct_Response_Answer_Id = "SQE-TARGET-resp-" + UUID.randomUUID().toString();
		target.Target_Correct_Responses.add(resp);
		msgpayload.Multi_Value_Answer_Data.Targets.add(target);
		
		
		msgpayload.Assessment_Item_Source_System_Record_Id = "SQE-AssessItem-" + uuid;
		
		
		msg.checkCriticalProperties = false;
		String messagePayloadAsJson = new ObjectMapper().writeValueAsString(msgpayload);
		System.out.println("messageAsJson: "+ messagePayloadAsJson);
		
		return messagePayloadAsJson;
	}
}
