package com.pearson.test.daalt.dataservice.request.message.version01;

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

public abstract class SeerMessage implements Message {
	public MessageSource messageSource;
	public boolean directlyToKafkaV1 = true;
	public String kafkaBroker;
	public String seerTopic;
	public String seerServer;
	public String seerClient;
	
	public SeerMessage () {
		Properties property = new Properties();
		try {
			property.load(new FileInputStream("config/config.cfg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		directlyToKafkaV1 = Boolean.parseBoolean(property.getProperty("directlyToKafkaV1"));
		kafkaBroker = property.getProperty("kafkaBrokerV1");
		seerTopic = property.getProperty("seerTopicV1");
		seerServer = property.getProperty("seerServerV1");
		seerClient = property.getProperty("seerClientV1");
	}
	
	@Override
	public void send(int seerCount, int subPubCount) throws JsonGenerationException, JsonMappingException, IOException, SeerReportingException {
		
		String messagePayloadAsJson = new ObjectMapper().writeValueAsString(messageSource);

		if (directlyToKafkaV1) {

			Producer<String, String> subpubProducer;
			Properties props = new Properties();
	        props.put("metadata.broker.list", kafkaBroker);		// broker  10.252.5.240,  10.252.1.99,  10.252.3.239
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
