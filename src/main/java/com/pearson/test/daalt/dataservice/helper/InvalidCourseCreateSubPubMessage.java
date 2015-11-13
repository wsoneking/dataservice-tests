package com.pearson.test.daalt.dataservice.helper;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * <b>InvalidCourseCreateSubPubMessage</b>
 * </p>
 * <p>
 *
 * </p>
 */
public class InvalidCourseCreateSubPubMessage extends KafkaMessageHelper {

	String uuid;

	public InvalidCourseCreateSubPubMessage() throws IOException {
		super();
		uuid = UUID.randomUUID().toString();
		// start listening to kafka
		startKafkaConsumerWithUniqueStringHandler("error_subpub", uuid);
	}

	@Test
	public void testInvalidCourseCreateSubPubMessage() throws InterruptedException, IOException {

		String invalidMessage = "{\n" +
            "    \"Transaction_Type_Code\": \"Create\",\n" +
            "    \"Transaction_Datetime\": \"2015-02-18T19:49:40.813Z\",\n" +
            "    \"Message_Type_Code\": \"Course_Section\",\n" +
//            "    \"Message_Version\": \"1.0\",\n" +
            "    \"Course_Section_Source_System_Code\": \"QA_Registrar\",\n" +
            "    \"Course_Section_Source_System_Record_Id\": \"SQE-CS-" + uuid + "\",\n" +
            "    \"Course_Source_System_Code\": \"QA_Registrar\",\n" +
            "    \"Course_Section_Code\": \"TEST101\",\n" +
            "    \"Course_Section_Title\": \"QA Default Course Title\",\n" +
            "    \"Course_Source_System_Record_Id\": \"SQE-c81c9344-8581-4df3-8fe5-d3b007c8cc49\",\n" +
            "    \"Course_Access_End_Date\": \"2015-05-19T19:49:40.813Z\",\n" +
            "    \"Course_Access_Start_Date\": \"2015-02-16T19:49:40.813Z\",\n" +
            "    \"currentTimeFormatted\": \"2015-02-18T19:49:41.392Z\"\n" +
            "}";

		logger.info("Message contains UUID: " + uuid);

		publishMessage(invalidMessage);

		List<String> messages = waitForMessages(1, kafkaConsumer.getProcessedMessageMap(), 30);

		Assert.assertTrue(messages.size() == 1);
	}

}
