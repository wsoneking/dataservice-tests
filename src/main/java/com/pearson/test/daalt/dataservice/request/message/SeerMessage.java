package com.pearson.test.daalt.dataservice.request.message;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;

import com.pearson.seer.client.ClientConfiguration;
import com.pearson.seer.client.SeerClient;
import com.pearson.seer.client.exception.SeerReportingException;
import com.pearson.test.daalt.dataservice.TestEngine;

public abstract class SeerMessage implements Message {
	public MessageSource messageSource;
	public boolean directToKafka = true;
	public String kafkaBroker;
	public String seerTopic;
	public String seerServer;
	public String seerClient;
	
	public SeerMessage () {
		Properties property = new Properties();
		try {
			property.load(new FileInputStream(TestEngine.configFileLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
		directToKafka = TestEngine.getInstance().getDirectToKafka();
		kafkaBroker = TestEngine.getInstance().getKafkaBroker();
		seerTopic = TestEngine.getInstance().getSeerTopic();
		seerServer = property.getProperty(TestEngine.seerServerPropName);
		seerClient = property.getProperty(TestEngine.seerClientPropName);
	}
	
	@Override
	public void send(int seerCount, int subPubCount) throws JsonGenerationException, JsonMappingException, IOException, SeerReportingException {
		
		String messagePayloadAsJson = new ObjectMapper().writeValueAsString(messageSource);

		if (directToKafka) {

			Producer<String, String> subpubProducer;
			Properties props = new Properties();
	        props.put("metadata.broker.list", kafkaBroker);
	        props.put("serializer.class", "kafka.serializer.StringEncoder");
	        props.put("request.required.acks", "1");
	        subpubProducer = new Producer<String, String>(new ProducerConfig(props));
	        KeyedMessage<String, String> data = new KeyedMessage<String, String>(seerTopic, messagePayloadAsJson);
	        while(seerCount>0){
		        subpubProducer.send(data);
		        seerCount--;
	        }
	        subpubProducer.close();
	        
			System.out.println("Publishing message to " + seerTopic + " topic; The kafka broker is " + kafkaBroker + "." );
			System.out.println("Message Has Body: \n" + messagePayloadAsJson +"\n");
			
		} else {
			ClientConfiguration config = new ClientConfiguration(seerClient, "hRxQN4tBfy4S", seerServer);
			SeerClient client = new SeerClient(config);
	
			client.reportRawTincan(messagePayloadAsJson);  
			
			System.out.println("Publishing Seer message");
			System.out.println("Publishing message of type: "+ "Tincan");
			System.out.println("Message Has Body: \n" + messagePayloadAsJson +"\n");
		}
	}
}