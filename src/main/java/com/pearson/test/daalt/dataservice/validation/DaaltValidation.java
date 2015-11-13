package com.pearson.test.daalt.dataservice.validation;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.pearson.qa.apex.builders.SimpleRestRequestBuilder;
import com.pearson.qa.apex.dataobjects.EnvironmentType;
import com.pearson.qa.apex.dataobjects.UserObject;
import com.pearson.qa.apex.dataobjects.UserType;
import com.pearson.test.daalt.config.DefaultConfig;
import com.pearson.test.daalt.dataservice.TestEngine;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class DaaltValidation extends Validation {
	public static final String configFileLocation = "config/config.cfg";
	public static final String apigeeTokenKey = "data";

//	public String apigeeTokenRoute;
	
	protected static String ROUTE = "route";
	protected static String EXPECTED_RESPONSE_CODE = "expectedResponseCode";

	protected static String EXPECTED_ITEMS = "expectedItems";
	protected static String EXPECTED_STUDENTS = "expectedStudents";
	protected static String EXPECTED_RESOURCES = "expectedResources";
	protected static String EXPECTED_MODULES = "expectedModules";
	protected static String EXPECTED_ASSESSMENT = "expectedAssessment";

	protected String testCaseNumber;
	protected EnvironmentType envType;
	protected TestEngine engine;
	private UserObject actingUser;
	protected Map<String, Boolean> validations = new HashMap<String, Boolean>();
	protected Map<String, String> parameters;
	protected String domain;
	protected final long TIME_SPENT_TOLERANCE_MILLIS = 1000*7;
	protected final long PAGINATION_LOOP_COUNT = 60;
	protected final float TREND_TOLERANCE = 0.001f;

	private String creationTestName;

	public DaaltValidation() {
		this.engine = TestEngine.getInstance();
		this.envType = engine.getTestEnvType();
		Properties property = new Properties();
		try {
			property.load(new FileInputStream(configFileLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
//		apigeeTokenRoute = property.getProperty("apigeeTokenRoute");
	}

	@JsonIgnore
	public boolean isValidationSupported() throws Exception {

		// is the current class name (split out on "." to get just the name")
		// compare to the list of classes in the config

		// if its in the list return true
		// if not return false
		String[] validatorsSupps = getConfig().getValue("validatorsSupported")
				.split(",");
		for (String s : validatorsSupps) {
			System.out.println(this.getClass().getName());	
			if (this.getClass().getName().contains(s))
		
				return true;
		}
		return false;
	}

	public DaaltValidation(String testCaseNumber, Validation validation) {
		this.testCaseNumber = testCaseNumber;
		parameters = validation.getParameters();
		this.engine = TestEngine.getInstance();
		this.envType = engine.getTestEnvType();
		actingUser = new UserObject(validation.getActingUser().getUserName(),
				validation.getActingUser().getPassword(), UserType.Undefined,
				this.envType);
	}

	@JsonIgnore
	public DefaultConfig getConfig() throws Exception {
		return engine.config;
	}
	
	@JsonIgnore
	public UserObject getActingUserObject() {
		if (actingUser == null) {
			actingUser = new UserObject(getActingUser().getUserName(),
					getActingUser().getPassword(), UserType.Undefined,
					this.envType);
			actingUser.setId(getActingUser().getId());
		}
		return actingUser;
	}

	@JsonIgnore
	public abstract Map<String, Boolean> getValidations() throws Exception;

	@JsonIgnore
	public abstract String[] getRequiredParameters();

	
	public void enforceRequiredParameters() throws Exception {
		for (String parameter : getRequiredParameters()) {
			if (!parameters.containsKey(parameter)) {
				throw new Exception("Validator is missing required parameter: "
						+ parameter + ".  Received parameters are: "
						+ parameters.toString());
			}
		}
	}

	public String getTestName() {
		return this.getClass().toString();
	}

	public String getCreationTestName() {
		if (creationTestName == null) {
			return "Unknown";
		}
		return creationTestName;
	}

	public void setCreationTestName(String creationTestName) {
		this.creationTestName = creationTestName;
	}

	public String toString() {

		String username = "Unknown";
		if (getActingUser() != null) {
			username = getActingUser().getUserName();
		}

		return "Validation: " + getTestName() + ", Created By Test: "
				+ getCreationTestName() + ", Executed by Acting User: "
				+ username + ": suite Number: "
				+ TestEngine.getInstance().getSuite().getId();
	}
	
	protected boolean trendValueWithinTolerances(float expected, float actual) {
		boolean withinTolerances = false;
		double diff = Math.abs(expected - actual);
		if (diff <= TREND_TOLERANCE) {
			withinTolerances = true;
		}
		return withinTolerances;
	}

	protected boolean valueWithinTolerances(double expected, double actual) {
		boolean withinTolerances = false;
		double diff = Math.abs(expected - actual);
		if (diff <= TIME_SPENT_TOLERANCE_MILLIS) {
			withinTolerances = true;
		}
		return withinTolerances;
	}
	
	protected boolean valueWithinExpectedLongString(long expected, String actual) throws ParseException {
		
		if(actual.equals("00:00:00.0")){			// this part means, if actual is 0F, then expected should be 0F, no tolerance allowed
			if(expected == 0F)
				return true;
			else
				return false;
		}
		
		boolean back = false;
		Date expH = new Date(expected - TimeZone.getDefault().getRawOffset() + TIME_SPENT_TOLERANCE_MILLIS);
		Date expL = new Date(expected - TimeZone.getDefault().getRawOffset() - TIME_SPENT_TOLERANCE_MILLIS);
		Date act = new SimpleDateFormat("HH:mm:ss.S", Locale.getDefault()).parse(actual);
		
		System.out.println("DATE EXPECTED LOW: "+expL);
		System.out.println("DATE EXPECTED HIGH: "+expH);
		System.out.println("DATE ACTUAL: "+act);
		
		if(act.before(expH) && act.after(expL))
			back = true;
		return back;
	}
	
	protected float getDeviationPercent(long expected, long actual) throws ParseException {
		long difference = actual - expected;
		float percentDeviation = 0;
		if(expected != 0){
			percentDeviation = ((difference*1000) / expected) * 100;
		}
		
		return percentDeviation;
	}
	
	public SimpleRestRequestBuilder.IBuilderPostStart createRequest() {
		return engine.createRequest();
	}
}
