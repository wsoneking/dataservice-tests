package com.pearson.test.daalt.dataservice.request.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.pearson.clients.subpub.SubPubMessageBus;
import com.pearson.clients.subpub.SubPubPrincipal;
import com.pearson.test.daalt.dataservice.TestEngine;

public class SubPubMessageBusHelper {
	private String subpubPrincipal;
	private String key;
	private String subPubUrl;
	
	private SubPubMessageBus messageBus = null;
	
	public SubPubMessageBusHelper() {
		Properties property = new Properties();
		try {
			property.load(new FileInputStream(TestEngine.configFileLocation));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		subpubPrincipal = property.getProperty(TestEngine.subpubPrincipalPropName);
		key = property.getProperty(TestEngine.subpubKeyPropName);
		subPubUrl = property.getProperty(TestEngine.subpubUrlPropName);
	}
	
	public SubPubMessageBus getMessageBus() {
		if (messageBus == null) {
			String host = this.subPubUrl.replace("https://", "").replace(
					"http://", "");
			int port = 80;
			SubPubPrincipal principal = new SubPubPrincipal();
			principal.setKey(key);
			principal.setPrincipalId(this.subpubPrincipal);
			messageBus = new SubPubMessageBus(host, port, "", principal);
		}
		return messageBus;
	}
	
	
}
