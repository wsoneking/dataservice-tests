package com.pearson.test.daalt.dataservice.validation;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.pearson.test.daalt.config.DefaultConfig;
import com.pearson.test.daalt.dataservice.BaseTestUtility;
import com.pearson.test.daalt.dataservice.Suite;
import com.pearson.test.daalt.dataservice.TestCase;
import com.pearson.test.daalt.dataservice.mongodb.MongoDB;

public class ValidateSuiteTests extends BaseTestUtility {


	@Test(dataProvider = "getExecutedTestCasesByDB")
	public void validateEverything(Object validation) throws Exception {

		DaaltValidation realValidation = (DaaltValidation) validation;

		if (realValidation.getShouldSkipThisValidation() == true) {
			throw new SkipException(
					"Validation "
							+ realValidation.getName()
							+ " skipped, likely because of an upstream error in the creation test");
		}
		if ( realValidation.isValidationSupported() == false ) {
			System.out.println("**********************************************"
					+ "\n Skipped because this Validation is not Supported\n "
					+ "**********************************************");
//			Thread.sleep(3000);
			throw new SkipException(
					"Validation "
							+ realValidation.getName()
							+ " skipped, likely because this validation is not supported");
		}
		addValidations(realValidation.getValidations());
		assertSuccess();

	}
	

	@DataProvider
	public Iterator<Object[]> getExecutedTestCases() throws Exception {

		ValidationFactory factory = new ValidationFactory();

		
		//don't use the file, and instead get the json from the DB
		File suiteFileJson = new File(
				"src/test/resources/testdata/currentsuite."
						+ getEngine().getSuite().getId() + ".json");

		// Deserialize our xml file
		// suiteFile.createNewFile();
		// Serializer serializer = new Persister();
		// Suite currentSuite = serializer.read(Suite.class, suiteFileXml);

		Suite currentSuite = new ObjectMapper().readValue(suiteFileJson,
				Suite.class);
		List<Object[]> validations = new LinkedList<Object[]>();

		for (TestCase tc : currentSuite.getTestCases()) {
			for (Validation val : tc.getValidations()) {
				validations.add(new Object[] { factory.getValidation(
						tc.getId(), val) });
			}
		}

		return validations.iterator();

	}
	
	
	
	@DataProvider
	public Iterator<Object[]> getExecutedTestCasesByDB() throws Exception {

		ValidationFactory factory = new ValidationFactory();

		// set up question database
		DefaultConfig config = new DefaultConfig("config/config.cfg");
		String dbUrl = config.getValue("dbUrl");
		String dbName = config.getValue("dbName");
		int dbPort = Integer.parseInt(config.getValue("dbPort"));
		String dbUser = config.getValue("dbUser");
		String dbPassword = config.getValue("dbPassword");
		String dbColl = config.getValue("dbCollectionTestEngine");
		
		System.out.println("**********finish connecting*********");
		
		MongoDB mgdb = new MongoDB(dbUrl, dbName, dbPort);
		mgdb.connectToDb(dbUser, dbPassword.toCharArray());
		DBCollection collection = mgdb.getDB().getCollection(dbColl);
		
		BasicDBObject keys = new BasicDBObject();
		keys.put("_id", 0);
		BasicDBObject query = new BasicDBObject();
		
		System.out.println("Suite Id is: "+ getEngine().getSuite().getId());
		query.put("id", getEngine().getSuite().getId());
			
		DBCursor cursor = collection.find(query, keys);
		String src = cursor.one().toString();
		
//		System.out.println("********Data is : " + src);
		
		Suite currentSuite =  new ObjectMapper().readValue(src, Suite.class);;
		List<Object[]> validations = new LinkedList<Object[]>();

		for (TestCase tc : currentSuite.getTestCases()) {
			for (Validation val : tc.getValidations()) {
				validations.add(new Object[] { factory.getValidation(
						tc.getId(), val) });
			}
		}

		return validations.iterator();

	}
	

	@DataProvider
	public Iterator<Object[]> getSpecificValidationsByDB() throws Exception {

		ValidationFactory factory = new ValidationFactory();

		DefaultConfig config = new DefaultConfig("config/config.cfg");
		String dbUrl = config.getValue("dbUrl");
		String dbName = config.getValue("dbName");
		int dbPort = Integer.parseInt(config.getValue("dbPort"));
		String dbColl = config.getValue("dbCollectionTestEngine");
		
		// create the connection and login as root
		MongoDB mgdb = new MongoDB(dbUrl, dbName, dbPort);
		mgdb.connectToDbWithoutAuth();
		DBCollection collection = mgdb.getDB().getCollection(dbColl);
		
//		System.out.println("**********finish connecting*********");
		
		BasicDBObject keys = new BasicDBObject();
		keys.put("_id", 0);
		BasicDBObject query = new BasicDBObject();
		query.put("id", getEngine().getSuite().getId());
		DBCursor cursor = collection.find(query, keys);
		String src = cursor.one().toString();
		
//		System.out.println("********Data is : " + src);
		
		Suite currentSuite =  new ObjectMapper().readValue(src, Suite.class);;
		List<Object[]> validations = new LinkedList<Object[]>();

		for (TestCase tc : currentSuite.getTestCases()) {
			for (Validation val : tc.getValidations()) {
				validations.add(new Object[] { factory.getValidation(
						tc.getId(), val) });
			}
		}

		return validations.iterator();

	}
	
}
