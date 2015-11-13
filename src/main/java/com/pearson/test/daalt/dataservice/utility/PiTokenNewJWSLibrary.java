package com.pearson.test.daalt.dataservice.utility;

import java.io.IOException;
import java.util.Properties;

import com.pearson.ed.pi.authentication.systemclient.PiTokenContainer;


public class PiTokenNewJWSLibrary {
	
	public static void main(String[] args) throws IOException {
		
		Properties config = new Properties();
		config.put("com.pearson.ed.pi.auth.token.url", "http://int-piapi-internal.stg-openclass.com/tokens");
		config.put("com.pearson.ed.pi.auth.system.username", "tusha9.educator");
		config.put("com.pearson.ed.pi.auth.system.password", "Password27");
		PiTokenContainer.init(config);
		String token = PiTokenContainer.getSystemToken();
		PiTokenContainer.shutdown();
		System.out.println(token);
		
	}

}
