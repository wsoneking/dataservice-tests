package com.pearson.test.daalt.dataservice.validation;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

import com.pearson.test.daalt.dataservice.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Validation {

	private String name;

	private User actingUser;

	//old way of doing things should be removed, as all validations now use json
	private Map<String, String> parameters;

	//new way of doing things
	private String json;
	
	private boolean skipThisValidation = false;

	@Attribute(name = "name")
	public String getName() {
		return name;
	}

	@Attribute(name = "name")
	public void setName(String value) {
		name = value;
	}

	@Element(name = "ActingUser")
	public User getActingUser() {
		return actingUser;
	}

	@Element(name = "ActingUser")
	public void setActingUser(User value) {
		actingUser = value;
	}

	@ElementMap(name = "Parameters")
	public Map<String, String> getParameters() {
		if (parameters == null) {
			parameters = new HashMap<String, String>();
		}
		return parameters;
	}

	@ElementMap(name = "Parameters")
	public void setParameters(Map<String, String> value) {
		parameters = value;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public boolean getShouldSkipThisValidation() {
		return skipThisValidation;
	}

	public void setShouldSkipThisValidation(boolean skipThisValidation) {
		this.skipThisValidation = skipThisValidation;
	}
	
	public String getExpectedResultsPrintString() {
		return "not yet implemented";
	}
}
