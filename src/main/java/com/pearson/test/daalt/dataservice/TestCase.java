package com.pearson.test.daalt.dataservice;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.pearson.qa.apex.dataobjects.UserObject;
import com.pearson.qa.apex.dataobjects.UserType;
import com.pearson.test.daalt.dataservice.validation.Validation;

@Root(name = "TestCase")
public class TestCase {

	private String id;
	private User actingUser;
	private List<Validation> validations;

	@Attribute(name = "id")
	public String getId() {
		return id;
	}

	@Attribute(name = "id")
	public void setId(String id) {
		this.id = id;
	}

	@Element(name = "ActingUser")
	public User getActingUser() {
		return actingUser;
	}

	@Element(name = "ActingUser")
	@JsonProperty("actingUser")
	public void setActingUser(User actingUser) {
		this.actingUser = actingUser;
	}

	@JsonIgnore
	public void setActingUser(UserObject actingUser) {
		this.actingUser = new User(actingUser);
	}

	@JsonIgnore
	public UserObject getActingUserAsUserObject() {
		return new UserObject(getActingUser().getUserName(), getActingUser()
				.getPassword(), UserType.Undefined, TestEngine.getInstance()
				.getTestEnvType());
	}

	@ElementList(name = "Validations")
	public List<Validation> getValidations() {
		if (validations == null) {
			validations = new LinkedList<Validation>();
		}
		return validations;
	}

	@ElementList(name = "Validations")
	public void setValidations(List<Validation> validations) {
		this.validations = validations;
	}

}
