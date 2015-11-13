package com.pearson.test.daalt.dataservice;

import org.simpleframework.xml.Element;

import com.pearson.qa.apex.dataobjects.UserObject;

public class User {

	private String userName;
	private String password;
	private String id;
	private String firstName;
	private String lastName;

	public User() {

	}

	public User(UserObject user) {
		userName = user.getUsername();
		password = user.getPassword();
		id = user.getId();
	}

	@Element(name = "UserName")
	public String getUserName() {
		return userName;
	}

	@Element(name = "UserName")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Element(name = "Password")
	public String getPassword() {
		return password;
	}

	@Element(name = "Password")
	public void setPassword(String password) {
		this.password = password;
	}

	@Element(name = "Id")
	public String getId() {
		return id;
	}

	@Element(name = "Id")
	public void setId(String id) {
		this.id = id;
	}
	
	@Element(name = "FirstName")
	public String getFirstName() {
		return firstName;
	}

	@Element(name = "FirstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Element(name = "LastName")
	public String getLastName() {
		return lastName;
	}

	@Element(name = "LastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
