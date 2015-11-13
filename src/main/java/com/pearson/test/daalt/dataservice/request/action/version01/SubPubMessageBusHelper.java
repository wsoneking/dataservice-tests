package com.pearson.test.daalt.dataservice.request.action.version01;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.pearson.clients.subpub.SubPubMessageBus;
import com.pearson.clients.subpub.SubPubPrincipal;

public class SubPubMessageBusHelper {
	private String subpubPrincipalId;
	private String key;
	private String subPubUrl;
	
	private SubPubMessageBus messageBus = null;
	
	public SubPubMessageBusHelper() {
		Properties property = new Properties();
		try {
			property.load(new FileInputStream("config/config.cfg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		subpubPrincipalId = property.getProperty("subpubPrincipalIdV1");
		key = property.getProperty("subpubKeyV1");
		subPubUrl = property.getProperty("subPubUrlV1");
	}
	
	public SubPubMessageBus getMessageBus() {
		if (messageBus == null) {
			String host = this.subPubUrl.replace("https://", "").replace(
					"http://", "");
			int port = 80;
			SubPubPrincipal principal = new SubPubPrincipal();
			principal.setKey(key);
			principal.setPrincipalId(this.subpubPrincipalId);
			messageBus = new SubPubMessageBus(host, port, "", principal);
		}
		return messageBus;
	}
	
}
