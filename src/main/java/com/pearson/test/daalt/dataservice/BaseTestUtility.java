package com.pearson.test.daalt.dataservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.pearson.qa.apex.dataobjects.EnvironmentType;
import com.pearson.qa.apex.helpers.SSLUtilities;
import com.pearson.qa.apex.validations.ValidationCollectionObject;
import com.pearson.test.daalt.config.DefaultConfig;

public class BaseTestUtility {
	public static final String userCollectionPropName = "userCollection";
	public static final String devEnvironmentName = "dev";
	public static final String stgEnvironmentName = "stg";
	public static final String prdEnvironmentName = "prd";
	
	public static final String instr_username_stg = "Instructor_username_stg";
	public static final String instr_password_stg = "Instructor_password_stg";
	public static final String instr_id_stg = "Instructor_personId_stg";
	public static final String instr_firstName_stg = "Instructor_FirstName_stg";
	public static final String instr_lastName_stg = "Instructor_LastName_stg";
	public static final String student01_username_stg = "Student_1_username_stg";
	public static final String student01_password_stg = "Student_1_password_stg";
	public static final String student01_id_stg = "Student_1_personId_stg";
	public static final String student01_firstName_stg = "Student_1_FirstName_stg";
	public static final String student01_lastName_stg = "Student_1_LastName_stg";
	public static final String student02_username_stg = "Student_2_username_stg";
	public static final String student02_password_stg = "Student_2_password_stg";
	public static final String student02_id_stg = "Student_2_personId_stg";
	public static final String student02_firstName_stg = "Student_2_FirstName_stg";
	public static final String student02_lastName_stg = "Student_2_LastName_stg";
	public static final String instr_username_prod = "Instructor_username_prod";
	public static final String instr_password_prod = "Instructor_password_prod";
	public static final String instr_id_prod = "Instructor_personId_prod";
	public static final String instr_firstName_prod = "Instructor_FirstName_prod";
	public static final String instr_lastName_prod= "Instructor_LastName_prod";
	public static final String student01_username_prod = "Student_1_username_prod";
	public static final String student01_password_prod = "Student_1_password_prod";
	public static final String student01_id_prod = "Student_1_personId_prod";
	public static final String student01_firstName_prod = "Student_1_FirstName_prod";
	public static final String student01_lastName_prod = "Student_1_LastName__prod";
	public static final String student02_username_prod = "Student_2_username_prod";
	public static final String student02_password_prod = "Student_2_password_prod";
	public static final String student02_id_prod = "Student_2_personId_prod";
	public static final String student02_firstName_prod = "Student_2_FirstName_prod";
	public static final String student02_lastName_prod = "Student_2_LastName_prod";

	private TestEngine engine;
	private ValidationCollectionObject validate;
	
	@Parameters({TestEngine.environmentPropName,
					userCollectionPropName,
					TestEngine.waitTimePropName, 
					TestEngine.testVarPropName, 
					TestEngine.printToTPropName, 
					TestEngine.printExpectedOutputPropName, 
					TestEngine.apigeeTokenRoutePropName, 
					TestEngine.environmentSwitchPropName, 
					TestEngine.originalBaseUrlPropName, 
					TestEngine.baseUrlPropName, 
					TestEngine.directToKafkaPropName, 
					TestEngine.kafkaBrokerPropName, 
					TestEngine.seerTopicPropName, 
					TestEngine.subpubTopicPropName})
	@BeforeSuite(alwaysRun = true)
	public void setEnvironment(ITestContext ctx, 
			@Optional String environment,
			@Optional String userCollection,
			@Optional String waitTimeSeconds, 
			@Optional String testVar, 
			@Optional String printToT, 
			@Optional String printExpectedOutput, 
			@Optional String apigeeTokenRoute, 
			@Optional String environmentSwitch, 
			@Optional String originalBaseUrl, 
			@Optional String baseUrl, 
			@Optional String directToKafka, 
			@Optional String kafkaBroker, 
			@Optional String seerTopic, 
			@Optional String subpubTopic) throws Exception {

		System.out.println("(((((((((((((((((((SUITE NAME "
				+ ctx.getSuite().getName());
		if (environment != null && !environment.equals("")) {
			System.out.println("Received environment string of " + environment);
			// override the config env type with the string passed in
			getConfig().setEnvironmentType(getConfig().getEnvironmentType(environment));
			getConfig().setRunningLocally(false);
			getUserConfig().setEnvironmentType(getConfig().getEnvironmentType(environment));
			getUserConfig().setRunningLocally(false);
		} else {
			getConfig().setRunningLocally(true);
			getUserConfig().setRunningLocally(true);
		}

		User instructor = new User();
		User student01 = new User();
		User student02 = new User();
		if ((userCollection == null) || ("".equals(userCollection))) {
			userCollection = getConfig().getValue(userCollectionPropName);
		}
		switch (userCollection) {
			case prdEnvironmentName:
				instructor.setId(getUserConfig().getValue(instr_id_prod));
				instructor.setUserName(getUserConfig().getValue(instr_username_prod));
				instructor.setPassword(getUserConfig().getValue(instr_password_prod));
				instructor.setFirstName(getUserConfig().getValue(instr_firstName_prod));
				instructor.setLastName(getUserConfig().getValue(instr_lastName_prod));
				
				student01.setId(getUserConfig().getValue(student01_id_prod));
				student01.setUserName(getUserConfig().getValue(student01_username_prod));
				student01.setPassword(getUserConfig().getValue(student01_password_prod));
				student01.setFirstName(getUserConfig().getValue(student01_firstName_prod));
				student01.setLastName(getUserConfig().getValue(student01_lastName_prod));
				
				student02.setId(getUserConfig().getValue(student02_id_stg));
				student02.setUserName(getUserConfig().getValue(student02_username_prod));
				student02.setPassword(getUserConfig().getValue(student02_password_prod));
				student02.setFirstName(getUserConfig().getValue(student02_firstName_prod));
				student02.setLastName(getUserConfig().getValue(student02_lastName_prod));
			break;
			default:
				instructor.setId(getUserConfig().getValue(instr_id_stg));
				instructor.setUserName(getUserConfig().getValue(instr_username_stg));
				instructor.setPassword(getUserConfig().getValue(instr_password_stg));
				instructor.setFirstName(getUserConfig().getValue(instr_firstName_stg));
				instructor.setLastName(getUserConfig().getValue(instr_lastName_stg));
				
				student01.setId(getUserConfig().getValue(student01_id_stg));
				student01.setUserName(getUserConfig().getValue(student01_username_stg));
				student01.setPassword(getUserConfig().getValue(student01_password_stg));
				student01.setFirstName(getUserConfig().getValue(student01_firstName_stg));
				student01.setLastName(getUserConfig().getValue(student01_lastName_stg));
				
				student02.setId(getUserConfig().getValue(student02_id_stg));
				student02.setUserName(getUserConfig().getValue(student02_username_stg));
				student02.setPassword(getUserConfig().getValue(student02_password_stg));
				student02.setFirstName(getUserConfig().getValue(student02_firstName_stg));
				student02.setLastName(getUserConfig().getValue(student02_lastName_stg));
		}
		getEngine().setInstructor(instructor);
		getEngine().setStudent01(student01);
		getEngine().setStudent02(student02);

		if ((waitTimeSeconds != null) && (!"".equals(waitTimeSeconds))) {
			getEngine().setWaitTimeSeconds(Integer.valueOf(waitTimeSeconds));
		} else {
			getEngine().setWaitTimeSeconds(
					Integer.valueOf(getConfig().getValue(TestEngine.waitTimePropName)));
		}
		
		if ((testVar != null) && (!"".equals(testVar))) {
			getEngine().setTestVar(testVar);
		} else {
			getEngine().setTestVar(getConfig().getValue(TestEngine.testVarPropName));
		}
		
		if ((printToT != null) && (!"".equals(printToT))) {
			getEngine().setPrintToT(Boolean.valueOf(printToT));
		} else {
			getEngine().setPrintToT(Boolean.valueOf(getConfig().getValue(TestEngine.printToTPropName)));
		}
		
		if ((printExpectedOutput != null) && (!"".equals(printExpectedOutput))) {
			getEngine().setPrintExpectedOutput(Boolean.valueOf(printExpectedOutput));
		} else {
			getEngine().setPrintExpectedOutput(Boolean.valueOf(getConfig().getValue(TestEngine.printExpectedOutputPropName)));
		}
		
		if ((apigeeTokenRoute != null) && (!"".equals(apigeeTokenRoute))) {
			getEngine().setApigeeTokenRoute(apigeeTokenRoute);
		} else {
			getEngine().setApigeeTokenRoute(getConfig().getValue(TestEngine.apigeeTokenRoutePropName));
		}
		
		if ((environmentSwitch != null) && (!"".equals(environmentSwitch))) {
			getEngine().setEnvironmentSwitch(Boolean.valueOf(environmentSwitch));
		} else {
			getEngine().setEnvironmentSwitch(Boolean.valueOf(getConfig().getValue(TestEngine.environmentSwitchPropName)));
		}
		
		if ((originalBaseUrl != null) && (!"".equals(originalBaseUrl))) {
			getEngine().setOriginalBaseUrl(originalBaseUrl);
		} else {
			getEngine().setOriginalBaseUrl(getConfig().getValue(TestEngine.originalBaseUrlPropName));
		}

		if ((baseUrl != null) && (!"".equals(baseUrl))) {
			getEngine().setBaseUrl(baseUrl);
		} else {
			getEngine().setBaseUrl(getConfig().getValue(TestEngine.baseUrlPropName));
		}
		
		if ((directToKafka != null) && (!"".equals(directToKafka))) {
			getEngine().setDirectToKafka(Boolean.valueOf(directToKafka));
		} else {
			getEngine().setDirectToKafka(Boolean.valueOf(getConfig().getValue(TestEngine.directToKafkaPropName)));
		}
		
		if ((kafkaBroker != null) && (!"".equals(kafkaBroker))) {
			getEngine().setKafkaBroker(kafkaBroker);
		} else {
			getEngine().setKafkaBroker(getConfig().getValue(TestEngine.kafkaBrokerPropName));
		}
		
		if ((seerTopic != null) && (!"".equals(seerTopic))) {
			getEngine().setSeerTopic(seerTopic);
		} else {
			getEngine().setSeerTopic(getConfig().getValue(TestEngine.seerTopicPropName));
		}
		
		if ((subpubTopic != null) && (!"".equals(subpubTopic))) {
			getEngine().setSubpubTopic(subpubTopic);
		} else {
			getEngine().setSubpubTopic(getConfig().getValue(TestEngine.subpubTopicPropName));
		}
		
		// I don't think this next part does anything important anymore
		getEngine().setTestEnvType(getEngine().config.getEnvironmentType());
		if (getEngine().getTestEnvType() == EnvironmentType.Dev) {
			getEngine().setDataEnvType(EnvironmentType.Staging);
		} else {
			getEngine().setDataEnvType(getEngine().getTestEnvType());
		}

		getEngine().getSuite().setSuiteName(ctx.getSuite().getName());
	}
	
	@AfterSuite(alwaysRun = true)
	public void tearDownTestDataFile() throws Exception {
		getEngine().saveSuite();
	}
	
	@BeforeMethod(alwaysRun = true)
	public void initializeTestNgReporter() {
		Reporter.log("<ul>\n");
	}
	
	@BeforeMethod(alwaysRun = true)
	public void initializeNewTestDataFile() throws Exception {
		// Generate Suite Execution Number
		if (TestEngine.getInstance().getSuite().getId() == null) {
			String suiteId = getTimeStamp();

			// Initialize Suite For This Test
			getEngine().getSuite().setId(suiteId);
		}
	}
	
	public static String getTimeStamp() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmmssSSSS");
		return df.format(date);
	}

	@BeforeMethod(alwaysRun = true)
	public void setupProxy() {

		Properties sysProperties = System.getProperties();
		sysProperties.put("http.proxyHost", "127.0.0.1");
		sysProperties.put("http.proxyPort", "8888");
		sysProperties.put("https.proxyHost", "127.0.0.1");
		sysProperties.put("https.proxyPort", "8888");

		SSLUtilities.trustAllHttpsCertificates();
	}
	
	@AfterMethod(alwaysRun = true)
	public void completeTestNgReporter() {
		Reporter.log("</ul>\n");
	}

	@AfterMethod(alwaysRun = true)
	public void outputAndResetValidations() {
		System.out.println(validate());
		validate().clear();
	}
	
	public ValidationCollectionObject validate() {
		if (validate == null) {
			validate = new ValidationCollectionObject();
		}
		return validate;
	}

	public boolean verifySuccess() {
		return validate().getFailedCount()==0;
	}
	
	public String getValidationMessage() {
		return validate().getFailedValidations().toString();
	}
	
	public void assertSuccess() {
		Assert.assertTrue(validate().getFailedCount() == 0, validate()
				.toString());
	}

	public void addValidations(Map<String, Boolean> validations) {
		for (Entry<String, Boolean> validation : validations.entrySet()) {
			if (validation.getValue() == false) {
				validate().addFailedValidation(validation.getKey());
			} else {
				validate().addSuccessfulValidation(validation.getKey());
			}
		}
	}
	
	public TestEngine getEngine() {
		if (engine == null) {
			engine = TestEngine.getInstance();
		}
		return engine;
	}
	
	public DefaultConfig getConfig() {
		return getEngine().config;
	}
	
	public DefaultConfig getUserConfig() {
		return getEngine().userconfig;
	}
}
