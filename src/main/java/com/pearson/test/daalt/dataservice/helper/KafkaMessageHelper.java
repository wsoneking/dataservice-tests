package com.pearson.test.daalt.dataservice.helper;

import com.pearson.clients.subpub.Message;
import com.pearson.clients.subpub.PublishResponse;
import com.pearson.qa.common.kafkaconsumer.SimpleKafkaConsumer;
import com.pearson.seer.client.ClientConfiguration;
import com.pearson.seer.client.SeerClient;
import com.pearson.seer.client.exception.SeerReportingException;
import com.pearson.test.daalt.dataservice.request.action.SubPubMessageBusHelper;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class KafkaMessageHelper {

	Logger logger = LoggerFactory.getLogger(KafkaMessageHelper.class);
	 public SimpleKafkaConsumer kafkaConsumer;
	Properties kafkaProps;

	public KafkaMessageHelper() throws IOException {

		String kafkaPropFileLoc = System.getProperty("kafkapropfile");
		if (StringUtils.isBlank(kafkaPropFileLoc)) {
			kafkaPropFileLoc = "staging-error-kafka.properties";
		}

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(kafkaPropFileLoc);
		kafkaProps = new Properties();
		kafkaProps.load(inputStream);
	}

	public void startKafkaConsumerWithUniqueStringHandler(String topic, String uniqueString) throws IOException {
		stopKafkaConsumer();
		this.kafkaConsumer = new SimpleKafkaConsumer(kafkaProps, topic, new UniqueStringMessageHandlerFactory(uniqueString));
		startKafkaConsumer();
	}

	public void startKafkaConsumer() {
		this.kafkaConsumer.startGroup(3);
	}

	@AfterClass
	public void stopKafkaConsumer() {
		if (this.kafkaConsumer != null) {
			this.kafkaConsumer.shutdown();
		}
	}

	public Message getSubPubMessage(String payload, String messagetype) {
		Message subPubMessage = new Message();

		subPubMessage.setClient("DaaltSQE");
		subPubMessage.setClientString("DaaltSQE");
		subPubMessage.setSystem("DaaltSQE");
		subPubMessage.setSubSystem("DaaltSQE");
		subPubMessage.setMessageType(messagetype);
		subPubMessage.setPayloadString(payload);
		subPubMessage.setPayloadContentType(MediaType.APPLICATION_JSON);
		subPubMessage.setDurable(false);

		return subPubMessage;
	}

	public PublishResponse publishMessage(String payload, String messagetype) throws InterruptedException {
		Message message = getSubPubMessage(payload, messagetype);

		System.out.println("Publishing Subpub message of type: " + message.getMessageType());
		System.out.println("Subpub Message Has Body \n" + message.getPayloadString());

		PublishResponse response = new SubPubMessageBusHelper().getMessageBus().publish(message);
		Assert.assertNotNull(response, "Subpub Publish: the response from publishing to subpub cannot be null.");
		Assert.assertNull(response.getException(), "Subpub Publish: the response contains an exception");

		System.out.println("The message id is: " + response.getMessageId() + "\n");
		System.out.println("wait for 3 seconds to let the message send out.");
		Thread.sleep(1000*3);
		return response;
	}
	
	public PublishResponse publishMessage(String payload) throws InterruptedException {
		Message message = getSubPubMessage(payload, "daalt.qa");

		System.out.println("Publishing Subpub message of type: " + message.getMessageType());
		System.out.println("Subpub Message Has Body \n" + message.getPayloadString());

		PublishResponse response = new SubPubMessageBusHelper().getMessageBus().publish(message);
		Assert.assertNotNull(response, "Subpub Publish: the response from publishing to subpub cannot be null.");
		Assert.assertNull(response.getException(), "Subpub Publish: the response contains an exception");

		System.out.println("The message id is: " + response.getMessageId() + "\n");
		System.out.println("wait for 3 seconds to let the message send out.");
		Thread.sleep(1000*3);
		return response;
	}
	
	public void publishTincanMessage(String messageSource) throws InterruptedException, JsonGenerationException, JsonMappingException, IOException, SeerReportingException {
		
		String messagePayloadAsJson =  messageSource; // new ObjectMapper().writeValueAsString(messageSource);

		ClientConfiguration config = new ClientConfiguration("daalt-sqe", "hRxQN4tBfy4S", /* "https://seer-beacon.ecollege.com" */"https://seer-beacon.qaprod.ecollege.com");
		SeerClient client = new SeerClient(config);

		client.reportRawTincan(messagePayloadAsJson);  
		logger.info("Subpub Message Has Body \n" + messageSource);

		logger.info("wait for 3 seconds to let the message send out.");
		Thread.sleep(1000*3);
	}

	public List<String> waitForMessages(int messageCount,
	                                    ConcurrentHashMap<Object, Object> hashMap,
	                                    int waitTimeoutSeconds) throws InterruptedException {
		List<String> messages = new ArrayList<>();

		if (hashMap == null) {
			return messages;
		}

		long timeout = System.currentTimeMillis() + (waitTimeoutSeconds * 1000);

		while (timeout > System.currentTimeMillis()) {
			int size = hashMap.size();
			logger.info(String.format("%d out of %d messages in map.", size, messageCount));
			if (size >= messageCount) {
				break;
			}
			logger.info(String.format("Waiting for more messages ... %d", timeout - System.currentTimeMillis()));
			Thread.sleep(100);
		}

		for (Object key : hashMap.keySet()) {
			String message = (String) hashMap.get(key);
			messages.add(message);
		}

		return messages;
	}

}
