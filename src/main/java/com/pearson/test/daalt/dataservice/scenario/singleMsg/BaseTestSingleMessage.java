package com.pearson.test.daalt.dataservice.scenario.singleMsg;

import java.io.IOException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import com.pearson.test.daalt.dataservice.helper.KafkaMessageHelper;


public class BaseTestSingleMessage {	

	KafkaMessageHelper kafkaHelper;
	
	@BeforeClass
	public void setKafkaHelper () throws IOException {
		kafkaHelper = new KafkaMessageHelper();
	}
	
	// Regex helper -- remove field
	public String removeThisField(String str, String fieldName){
		String pattern = "\""+fieldName+"[^,]*,";
		String toReturn = str.replaceFirst(pattern, "");
		return toReturn;
	}
	// Regex helper -- set null
	public String setThisFieldNull(String str, String fieldName){
		String pattern = "\""+fieldName+"[^,]*,";
		String toReturn = str.replaceFirst(pattern, "\""+fieldName+"\":null,");
		return toReturn;
	}
	// TODO Regex helper -- change value type  -- it's not easy to write
	
	
	public void SubpubRawValidation (String uuid) throws IOException, InterruptedException {
		// start listening to kafka
		kafkaHelper.startKafkaConsumerWithUniqueStringHandler("subpub_raw", uuid);
		// validation 
		List<String> messages = kafkaHelper.waitForMessages(1, kafkaHelper.kafkaConsumer.getProcessedMessageMap(), 30);
		Assert.assertTrue(messages.size() == 1);
		if(messages.size() == 1){
			System.out.println("\n*****This message is found in subpub_raw*****\n");
		} else {
			System.out.println("\n*****NOT FIND in subpub_raw*****\n");
		}
		kafkaHelper.stopKafkaConsumer();
	}
	
	public void SubpubErrorValidation (String uuid) throws IOException, InterruptedException {
		// start listening to kafka
		kafkaHelper.startKafkaConsumerWithUniqueStringHandler("error_subpub", uuid);
		// validation 
		List<String> messages = kafkaHelper.waitForMessages(1, kafkaHelper.kafkaConsumer.getProcessedMessageMap(), 30);
		Assert.assertTrue(messages.size() == 1);
		if(messages.size() == 1){
			System.out.println("\n*****This message is found in error_subpub*****\n");
		} else {
			System.out.println("\n*****NOT FIND in error_subpub*****\n");
		}
		kafkaHelper.stopKafkaConsumer();
	}
	
	public void SubpubValidValidation (String uuid) throws IOException, InterruptedException {
		// start listening to kafka
		kafkaHelper.startKafkaConsumerWithUniqueStringHandler("valid_subpub", uuid);
		// validation 
		List<String> messages = kafkaHelper.waitForMessages(1, kafkaHelper.kafkaConsumer.getProcessedMessageMap(), 30);
		Assert.assertTrue(messages.size() == 1);
		if(messages.size() == 1){
			System.out.println("\n*****This message is found in valid_subpub*****\n");
		} else {
			System.out.println("\n*****NOT FIND in valid_subpub*****\n");
		}
		kafkaHelper.stopKafkaConsumer();
	}
	
}
