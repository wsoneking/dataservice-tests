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
import com.pearson.test.daalt.dataservice.request.action.SubPubMessageBusHelper;

public abstract class SubPubTransformerMessage extends com.pearson.clients.subpub.Message implements Message {
	public TransformerMessagePayload payload;
	public boolean checkCriticalProperties = true;
	private String subscriptionString;
	
	public SubPubTransformerMessage (){
		Properties property = new Properties();
		try {
			property.load(new FileInputStream("config/config.cfg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void SetSubscriptionString(String subscrip) {
		subscriptionString = subscrip;
	}
	public String getSubscriptionString() {
		return subscriptionString;
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
