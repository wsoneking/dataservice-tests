package com.pearson.test.daalt.dataservice.model;

import java.util.UUID;

public class BasicProduct implements Product {

	private String id;
	private String externallyGeneratedId;;
	
	public BasicProduct() {
		String randomUUID = UUID.randomUUID().toString();
		id = "SQE-book-" + randomUUID;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getExternallyGeneratedId() {
		return externallyGeneratedId;
	}

	@Override
	public void setExternallyGeneratedId(String externallyGeneratedId) {
		this.externallyGeneratedId = externallyGeneratedId;
		
	}
	

}
