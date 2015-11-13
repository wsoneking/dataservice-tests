package com.pearson.test.daalt.dataservice;

import org.codehaus.jackson.map.ObjectMapper;




import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.pearson.qa.apex.builders.SimpleRestRequestBuilder;
import com.pearson.qa.apex.dataobjects.EnvironmentType;
import com.pearson.test.daalt.config.DefaultConfig;
import com.pearson.test.daalt.dataservice.mongodb.MongoDB;

public class TestEngine {
	public static final String configFileLocation = "config/config.cfg";
	public static final String userConfigFileLocation = "config/userconfig.cfg";
	
	//config properties
	public static final String environmentPropName = "environment";
	public static final String waitTimePropName = "waitTimeSeconds";
	public static final String testVarPropName = "testVar";
	public static final String envSwitchPropName = "environmentSwitch";
	public static final String printToTPropName = "printToT";
	public static final String printExpectedOutputPropName = "printExpectedOutput";
	public static final String apigeeTokenRoutePropName = "apigeeTokenRoute";
	public static final String environmentSwitchPropName = "environmentSwitch";
	public static final String originalBaseUrlPropName = "originalBaseUrl";
	public static final String baseUrlPropName = "baseUrl";
	public static final String directToKafkaPropName = "directToKafka";
	public static final String kafkaBrokerPropName = "kafkaBroker";
	public static final String subpubTopicPropName = "subpubTopic";
	public static final String seerTopicPropName = "seerTopic";
	public static final String seerServerPropName = "seerServer";
	public static final String seerClientPropName = "seerClient";
	public static final String subpubPrincipalPropName = "subpubPrincipal";
	public static final String subpubKeyPropName = "subpubKey";
	public static final String subpubUrlPropName = "subpubUrl";
	
	//userconfig properties
	public static final String student03UsernamePropName = "Student_13_username";
	public static final String student03PasswordPropName = "Student_13_password";
	public static final String student03IdPropName = "Student_13_personId";
	public static final String student03FirstName = "Student_13_FirstName";
	public static final String student03LastName = "Student_13_LastName";
	public static final String ta01UsernamePropName = "TA_1_username";
	public static final String ta01PasswordPropName = "TA_1_password";
	public static final String ta01IdPropName = "TA_1_personId";
	public static final String ta01FirstName = "TA_1_FirstName";
	public static final String ta01LastName = "TA_1_LastName";
	
	public static final String DueDate_YEAR = "Due_Date_Year";
	public static final String DueDate_MONTH = "Due_Date_Month";
	public static final String DueDate_DAY = "Due_Date_Day";	
	public static final String DueDate_Hour = "Due_Date_Hour";
	public static final String DueDate_Minute = "Due_Date_Minute";
	public static final String DueDate_Second = "Due_Date_Second";
	public static final String DueDate_Millisecond = "Due_Date_Millisecond";
	public static final String DueDate_Zone = "Due_Date_Zone";
	
	//other constants
	public static final String timeZoneUTC = "UTC";
	
	public DefaultConfig config;
	public DefaultConfig userconfig;
	
	private static TestEngine testEngine;
	private Suite suite;
	private int currentTest = 0;
	private EnvironmentType testEnvType;
	private EnvironmentType dataEnvType;
	public static String xmlPath;
	public static String jsonPath;
	private int waitTimeSeconds;
	private String testVar;
	private Boolean printToT;
	private Boolean printExpectedOutput;
	private Boolean environmentSwitch;
	private String originalBaseUrl;
	private String baseUrl;
	private Boolean directToKafka;
	private String kafkaBroker;
	private String seerTopic;
	private String subpubTopic;
	
	private String apigeeTokenRoute;
	private User instructor;
	private User student01;
	private User student02;

	private TestEngine() {
		try {
			config = new DefaultConfig(configFileLocation);
			userconfig = new DefaultConfig(userConfigFileLocation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static TestEngine getInstance() {
		if (testEngine == null) {
			testEngine = new TestEngine();
		}
		return testEngine;
	}
	
	public String getConfigParameter(String key) {
		String toReturn = System.getProperty(key);
		if (toReturn == null) {
			toReturn = config.getValue(key);
		}
		System.out.println("baseUrl="+toReturn);
		return toReturn;
	}
	
	public String getUserConfigParameter(String key) {
		return userconfig.getValue(key);
	}

	public SimpleRestRequestBuilder.IBuilderPostStart createRequest() {
		return new SimpleRestRequestBuilder.ExpressionBuilder();
	}

	public Suite getSuite() {
		if (suite == null) {
			suite = new Suite();
		}
		return suite;
	}

	public void saveSuite() throws Exception {
		if (getSuite().getId() == null) {
			System.out.println("Unable to save Suite no ID provided for suite");
		} else {


			// write to json file
			String json = new ObjectMapper().writeValueAsString(getSuite());

			// write into MongoDB Jin
			DefaultConfig config = new DefaultConfig(configFileLocation);
			String dbUrl = config.getValue("dbUrl");
			String dbName = config.getValue("dbName");
			int dbPort = Integer.parseInt(config.getValue("dbPort"));
			String dbColl = config.getValue("dbCollectionTestEngine");

			// create the connection and login as root
			MongoDB mgdb = new MongoDB(dbUrl, dbName, dbPort);
			mgdb.connectToDbWithoutAuth();
			DBCollection collection = mgdb.getDB().getCollection(dbColl);

			DBObject insertDBObject = (DBObject) JSON.parse(json);

			BasicDBObject query = new BasicDBObject();
			query.put("id", getSuite().getId());
			if (collection.findOne(query) == null) {		// this part need to be improved later - Jin
				collection.insert(insertDBObject);
			} else {
				collection.remove(query);
				collection.insert(insertDBObject);
			}

		}
	}

	public int getCurrentTest() {
		return currentTest;
	}

	public void incrementCurrentTest() {
		currentTest++;
	}

	public EnvironmentType getTestEnvType() {
		return testEnvType;
	}

	public void setTestEnvType(EnvironmentType testEnvType) {
		this.testEnvType = testEnvType;
	}

	public EnvironmentType getDataEnvType() {
		return dataEnvType;
	}

	public void setDataEnvType(EnvironmentType dataEnvType) {
		this.dataEnvType = dataEnvType;
	}

	public int getWaitTimeSeconds() {
		if (waitTimeSeconds == -1) {
			waitTimeSeconds = Integer.valueOf(config.getValue(waitTimePropName));
		}
		return waitTimeSeconds;
	}

	public void setWaitTimeSeconds(int debugWaitTimeSeconds) {
		this.waitTimeSeconds = debugWaitTimeSeconds;
	}

	public String getTestVar() {
		if (testVar == null || testVar.isEmpty()) {
			testVar = config.getValue("testVar");
		}
		return testVar;
	}

	public void setTestVar(String testVar) {
		this.testVar = testVar;
	}

	public boolean isPrintToT() {
		if (printToT == null) {
			printToT = Boolean.valueOf(config.getValue(printToTPropName));
		}
		return printToT;
	}

	public void setPrintToT(boolean printToT) {
		this.printToT = printToT;
	}

	public boolean isPrintExpectedOutput() {
		if (printExpectedOutput == null) {
			printExpectedOutput = Boolean.valueOf(config.getValue(printExpectedOutputPropName));
		}
		return printExpectedOutput;
	}

	public void setPrintExpectedOutput(boolean printExpectedOutput) {
		this.printExpectedOutput = printExpectedOutput;
	}
	
	public String getApigeeTokenRoute() {
		if (apigeeTokenRoute == null || apigeeTokenRoute.isEmpty()) {
			apigeeTokenRoute = config.getValue(apigeeTokenRoutePropName);
		}
		return apigeeTokenRoute;
	}

	public void setApigeeTokenRoute(String apigeeTokenRoute) {
		this.apigeeTokenRoute = apigeeTokenRoute;
	}

	public boolean getEnvironmentSwitch() {
		if (environmentSwitch == null) {
			environmentSwitch = Boolean.valueOf(config.getValue(environmentSwitchPropName));
		}
		return environmentSwitch;
	}

	public void setEnvironmentSwitch(boolean environmentSwitch) {
		this.environmentSwitch = environmentSwitch;
	}
	
	public String getOriginalBaseUrl() {
		if (originalBaseUrl == null || originalBaseUrl.isEmpty()) {
			originalBaseUrl = config.getValue(originalBaseUrlPropName);
		}
		return originalBaseUrl;
	}

	public void setOriginalBaseUrl(String originalBaseUrl) {
		this.originalBaseUrl = originalBaseUrl;
	}

	public String getBaseUrl() {
		if (baseUrl == null || baseUrl.isEmpty()) {
			baseUrl = config.getValue(baseUrlPropName);
		}
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public boolean getDirectToKafka() {
		if (directToKafka == null) {
			directToKafka = Boolean.valueOf(config.getValue(directToKafkaPropName));
		}
		return directToKafka;
	}

	public void setDirectToKafka(boolean directToKafka) {
		this.directToKafka = directToKafka;
	}

	public String getKafkaBroker() {
		if (kafkaBroker == null || kafkaBroker.isEmpty()) {
			kafkaBroker = config.getValue(kafkaBrokerPropName);
		}
		return kafkaBroker;
	}

	public void setKafkaBroker(String kafkaBroker) {
		this.kafkaBroker = kafkaBroker;
	}

	public String getSeerTopic() {
		if (seerTopic == null || seerTopic.isEmpty()) {
			seerTopic = config.getValue(seerTopicPropName);
		}
		return seerTopic;
	}

	public void setSeerTopic(String seerTopic) {
		this.seerTopic = seerTopic;
	}

	public String getSubpubTopic() {
		if (subpubTopic == null || subpubTopic.isEmpty()) {
			subpubTopic = config.getValue(subpubTopicPropName);
		}
		return subpubTopic;
	}

	public void setSubpubTopic(String subpubTopic) {
		this.subpubTopic = subpubTopic;
	}

	public User getInstructor() {
		return instructor;
	}

	public void setInstructor(User instructor) {
		this.instructor = instructor;
	}

	public User getStudent01() {
		return student01;
	}

	public void setStudent01(User student01) {
		this.student01 = student01;
	}

	public User getStudent02() {
		return student02;
	}

	public void setStudent02(User student02) {
		this.student02 = student02;
	}
}
