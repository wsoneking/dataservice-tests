package com.pearson.test.daalt.dataservice.model;

public class BasicTA implements User, TA {

	private String userName;
	private String password;
	private String personId;
	private String givenName;
	private String middleName;
	private String familyName;
	public String personRole;
	
	public BasicTA(String userName, String password, String personId, String firstName, String lastName) {
		this.userName = userName;
		this.password = password;
		this.personId = personId;
		this.givenName = firstName;
		this.familyName = lastName;
	}
	
	@Override
	public String getUserName() {
		return userName;
	}

	@Override
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String getPersonId() {
		return personId;
	}
	
	@Override
	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@Override
	public String getPersonRole() {
		return "TeachingAssistant";
	}

	@Override
	public String getGivenName() {
		return givenName;
	}

	@Override
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	
	@Override
	public String getMiddleName() {
		return middleName;
	}

	@Override
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	@Override
	public String getFamilyName() {
		return familyName;
	}	

	@Override
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	@Override
	public void setPersonRole(String personRole) {
		this.personRole = personRole;
	}
}
