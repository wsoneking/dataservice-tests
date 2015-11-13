package com.pearson.test.daalt.dataservice.request.message.version01;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.pearson.seer.client.exception.SeerReportingException;

public interface Message {
	public void send(int seerCount, int subPubCount) throws MissingCriticalPropertiesException, MessageSendFailureException, JsonMappingException, JsonParseException, IOException, SeerReportingException;
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException;
	public String getMissingCriticalPropertyName();
}
