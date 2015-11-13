package com.pearson.test.daalt.dataservice.utility;

import java.util.Properties;

import org.testng.annotations.Test;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class SingleMessageSendToKafkaTool {

	@Test
	public void run () {
		
		
		String messagePayloadAsJson = "{\"Transaction_Datetime\":\"2015-06-30T22:34:17.881Z\",\"Message_Type_Code\":\"Multi_Value_Question_User_Answered\",\"Originating_System_Code\":\"DAALTTest\",\"Message_Version\":\"2.0\",\"Message_Transfer_Type\":\"LiveStream\",\"id\":\"SQE-Tincan-e4e2d1b8-818a-4342-8057-5f30672cac2a\",\"actor\":{\"objectType\":\"Agent\",\"account\":{\"homePage\":\"https://piapi.openclass.com\",\"Person_Source_System_Record_Id\":\"ffffffff54c2c421e4b0f10ebd0752f4\"}},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/answered\",\"display\":{\"language\":\"en-US\",\"verb\":\"answered\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"SQE-ObjectId-c2a057d9-6791-4166-9820-438a1c8e238e\",\"definition\":{\"type\":\"http://adlnet.gov/expapi/activities/question\"}},\"context\":{\"extensions\":{\"URL_Prefix_For_Context_Fields\":\"http://schema.pearson.com/daalt/\",\"appId\":\"DAALT-SQE\",\"Course_Section_Source_System_Record_Id\":\"SQE-CS-a761bf2c-fdfc-4e65-8bcf-9b4c1900963f\",\"Course_Section_Source_System_Code\":\"Registrar\",\"Person_Role_Code\":\"Student\",\"Person_Source_System_Code\":\"PI\",\"Assessment_Source_System_Record_Id\":\"SQE-Assess-9a729b14-8bec-48da-85b6-78bffb8f2ee0\",\"Assessment_Source_System_Code\":\"PAF\",\"Assessment_Item_Source_System_Record_Id\":\"SQE-Binning-90a0c0cf-35fb-445d-92f3-fd9acdba5d6c\",\"Assessment_Item_Source_System_Code\":\"PAF\",\"Assessment_Item_Question_Type\":\"MultiValue\",\"User_Work_Type\":\"Credit\",\"Assessment_Item_Response_Code\":\"Correct\",\"Attempt_Number\":1.0,\"Student_Response\":[{\"Target_Id\":\"SQE-SubQues-2ddbbf8c-221e-4945-93e0-be92c58287e7\",\"Answer_Id\":\"SQE-MultiValueAns-16915e06-8272-4b46-9877-43db44fbf194\",\"Target_Sub_Question_Response_Code\":\"Correct\"},{\"Target_Id\":\"SQE-SubQues-2ddbbf8c-221e-4945-93e0-be92c58287e7\",\"Answer_Id\":\"SQE-MultiValueAns-d2ca08d9-915e-42d5-b91f-5e50e6dec58d\",\"Target_Sub_Question_Response_Code\":\"Correct\"},{\"Target_Id\":\"SQE-SubQues-e6143355-89ab-457c-9a4d-895b3cd75631\",\"Answer_Id\":\"SQE-MultiValueAns-cb1e7c78-3890-4d0c-b8f1-949e587a35a2\",\"Target_Sub_Question_Response_Code\":\"Correct\"},{\"Target_Id\":\"SQE-SubQues-e6143355-89ab-457c-9a4d-895b3cd75631\",\"Answer_Id\":\"SQE-MultiValueAns-6be80eba-f2e6-4310-ab47-a994aa0a392a\",\"Target_Sub_Question_Response_Code\":\"Correct\"}],\"Client_Side_Accessed_Timestamp\":\"2015-06-30T16:34:17.886Z\",\"Client_Side_Answered_Timestamp\":\"2015-06-30T16:34:17.886Z\",\"Item_Response_Score\":4.0}},\"currentTimeFormatted\":\"2015-06-30T16:34:17.904Z\"}";
		
		Producer<String, String> subpubProducer;
		Properties props = new Properties();
        props.put("metadata.broker.list", "10.252.5.240:9092");		// broker  10.252.5.240,  10.252.1.99,  10.252.3.239
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
        subpubProducer = new Producer<String, String>(new ProducerConfig(props));
        KeyedMessage<String, String> data = new KeyedMessage<String, String>("tincan_raw2.0", messagePayloadAsJson);
        subpubProducer.send(data);
        subpubProducer.close();
		
		System.out.println("Message Has Body: \n" + messagePayloadAsJson +"\n");
		
	}
	
	

}
