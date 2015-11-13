package com.pearson.test.daalt.dataservice.request.message;

import java.io.IOException;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.pearson.seer.client.ClientConfiguration;
import com.pearson.seer.client.SeerClient;
import com.pearson.seer.client.exception.SeerReportingException;

public class AdHocSeerMessage extends SeerMessage {
	private String messagePayloadAsJson;
	
	public AdHocSeerMessage(String messagePayloadAsJson) {
		this.messagePayloadAsJson = messagePayloadAsJson;
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException, InvalidStateException {
		//do nothing
	}
	
	@Override
	public String getMissingCriticalPropertyName() {
		return null;
	}
	
	@Override
	public void send(int seerCount, int subPubCount) throws JsonGenerationException, JsonMappingException, IOException, SeerReportingException {
		if (directToKafka) {
			Producer<String, String> subpubProducer;
			Properties props = new Properties();
	        props.put("metadata.broker.list", kafkaBroker);		// broker  10.252.5.240,  10.252.1.99,  10.252.3.239
	        props.put("serializer.class", "kafka.serializer.StringEncoder");
	        props.put("request.required.acks", "1");
	        subpubProducer = new Producer<String, String>(new ProducerConfig(props));
	        KeyedMessage<String, String> data = new KeyedMessage<String, String>(seerTopic, messagePayloadAsJson);
	        subpubProducer.send(data);
	        subpubProducer.close();
			
			System.out.println("Publishing message to " + seerTopic + " topic; The kafka broker is " + kafkaBroker + "." );
			System.out.println("Message Has Body: \n" + messagePayloadAsJson +"\n");
			
		} else {
			ClientConfiguration config = new ClientConfiguration("DaaltAutomatedTests", "hRxQN4tBfy4S", /* "https://seer-beacon.ecollege.com" */"https://seer-beacon.qaprod.ecollege.com");
			SeerClient client = new SeerClient(config);
	
			client.reportRawTincan(messagePayloadAsJson);  
			
			System.out.println("Publishing Seer message");
			System.out.println("Publishing message of type: "+ "Tincan");
			System.out.println("Message Has Body: \n" + messagePayloadAsJson +"\n");
		}
	}
}
