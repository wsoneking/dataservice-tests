package com.pearson.test.daalt.dataservice.request.message;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.pearson.clients.subpub.PublishResponse;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.request.action.SubPubMessageBusHelper;

public abstract class SubPubMessage extends com.pearson.clients.subpub.Message implements Message {
	public MessagePayload payload;
	public boolean checkCriticalProperties = true;
	public boolean directToKafka = true;
	public String kafkaBroker;
	public String subpubTopic;
	
	public SubPubMessage (){
		Properties property = new Properties();
		try {
			property.load(new FileInputStream(TestEngine.configFileLocation));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		directToKafka = TestEngine.getInstance().getDirectToKafka();
		kafkaBroker = TestEngine.getInstance().getKafkaBroker();
		subpubTopic = TestEngine.getInstance().getSubpubTopic();
	}
	
	public String getSubscriptionString() {
		return "daalt.qa"; //all messages from DAALT SQE are sent under the same subscription
	}
	
	@Override
	public void send(int seerCount, int subPubCount) throws MissingCriticalPropertiesException, MessageSendFailureException, JsonMappingException, 
			JsonParseException, IOException {
		
		if(checkCriticalProperties){
			String missingCriticalPropertyName = getMissingCriticalPropertyName();
			if (missingCriticalPropertyName != null) {
				throw new MissingCriticalPropertiesException("Failed to send message - missing critical property : " + missingCriticalPropertyName);
			}
		}
		String messagePayloadAsJson = new ObjectMapper().writeValueAsString(payload);
		
		if (directToKafka) {
			Producer<String, String> subpubProducer;
			Properties props = new Properties();
	        props.put("metadata.broker.list", kafkaBroker);		// broker  10.252.5.240,  10.252.1.99,  10.252.3.239
	        props.put("serializer.class", "kafka.serializer.StringEncoder");
	        props.put("request.required.acks", "1");
	        subpubProducer = new Producer<String, String>(new ProducerConfig(props));
	        KeyedMessage<String, String> data = new KeyedMessage<String, String>(subpubTopic, messagePayloadAsJson);

	        while(subPubCount>0){
		        subpubProducer.send(data);
		        subPubCount--;
	        }
	       
	        	subpubProducer.close();
			
			System.out.println("Publishing message to " + subpubTopic + " topic; The kafka broker is " + kafkaBroker + "." );
			System.out.println("Message Has Body: \n" + messagePayloadAsJson +"\n");
			
		} else {
			
			System.out.println("Publishing SubPub message");
			this.setClient("DaaltSQE");
			this.setClientString("DaaltSQE");
			this.setSystem("DaaltSQE");
			this.setSubSystem("DaaltSQE");
			this.setMessageType(getSubscriptionString());
			this.setPayloadString(messagePayloadAsJson);
			this.setPayloadContentType(MediaType.APPLICATION_JSON);
			this.setDurable(false);
	
			System.out.println("Publishing message of type: "+ this.getMessageType());
			System.out.println("Message Has Body \n" + this.getPayloadString());
			PublishResponse response = new SubPubMessageBusHelper().getMessageBus().publish(this);
			if (response.getMessageId() == null) {
				throw new MessageSendFailureException();
			}
			System.out.println("The message id is: "+response.getMessageId()+"\n");
		}
	}
	

	
}
