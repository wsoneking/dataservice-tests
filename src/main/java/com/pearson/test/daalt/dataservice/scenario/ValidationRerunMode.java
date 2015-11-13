package com.pearson.test.daalt.dataservice.scenario;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.mongodb.MongoDB;
import com.pearson.test.daalt.dataservice.validation.DaaltValidation;
import com.pearson.test.daalt.dataservice.validation.Validation;
import com.pearson.test.daalt.dataservice.validation.ValidationFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.pearson.test.daalt.config.DefaultConfig;
import com.pearson.test.daalt.dataservice.BaseTestUtility;
import com.pearson.test.daalt.dataservice.Suite;
import com.pearson.test.daalt.dataservice.TestCase;

public class ValidationRerunMode extends BaseTestUtility {



	private String specificValidationFileId = "201506230331130082";

	@Test(enabled = true, dataProvider = "getSpecificValidationsByDB")
	public void validateEverythingSpecificFile(Object validation)
			throws Exception {

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
			throw new SkipException(
					"Validation "
							+ realValidation.getName()
							+ " skipped, because this validation is not supported");
		}
		addValidations(realValidation.getValidations());
		assertSuccess();

	}

	
	@DataProvider
	public Iterator<Object[]> getSpecificValidationsByDB() throws Exception {

		ValidationFactory factory = new ValidationFactory();


		// set up question database
		DefaultConfig config = new DefaultConfig("config/config.cfg");
		String dbUrl = config.getValue("dbUrl");
		String dbName = config.getValue("dbName");
		int dbPort = Integer.parseInt(config.getValue("dbPort"));
		String dbColl = config.getValue("dbCollectionTestEngine");
		
		MongoDB mgdb = new MongoDB(dbUrl, dbName, dbPort);
		mgdb.connectToDbWithoutAuth();
		System.out.println("Connect to DB successfully. ");
		
		DBCollection collection = mgdb.getDB().getCollection(dbColl);
		BasicDBObject keys = new BasicDBObject();
		keys.put("_id", 0);
		BasicDBObject query = new BasicDBObject();
		query.put("id", specificValidationFileId);
		DBCursor cursor = collection.find(query, keys);
		
		if(cursor.one() == null)
			System.out.println("Can't find this suite with this id");
		String src = cursor.one().toString();
		
		System.out.println(src);

		Suite currentSuite = new ObjectMapper().readValue(src,
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
	

}
