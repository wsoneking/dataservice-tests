package com.pearson.test.daalt.dataservice.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.pearson.test.daalt.dataservice.model.TestData;
import com.pearson.test.daalt.dataservice.validation.Validation;

public class DaaltDataServiceValidationEngine {
	protected String platformCode = "DAALT-SQE";
	
	public List<Validation> getValidationsForTestData(TestData data) throws InvalidTestDataException, JsonGenerationException, JsonMappingException, IOException {
//		String testVar = TestEngine.getInstance().getTestVar();
//		System.out.println("Value found by top-level validation engine: " + testVar);
		
		List<Validation> validationList = new ArrayList<>();
		
		//FUTURE: endpoint 1.4
		
		//FUTURE: endpoint 1.5
		
		//FUTURE: endpoint 1.6

		//endpoint 1.9
		SectionToModuleToResourcesValidationEngine moduleResources = new SectionToModuleToResourcesValidationEngine();
		validationList.addAll(moduleResources.getValidations(data));
				
		//endpoint 1.11
		SectionToModuleToResourceToItemsValidationEngine itemAnalysis = new SectionToModuleToResourceToItemsValidationEngine();
		validationList.addAll(itemAnalysis.getValidations(data));
	
		//endpoint 1.12
		SectionToModuleToStudentsValidationEngine moduleStudents = new SectionToModuleToStudentsValidationEngine();
		validationList.addAll(moduleStudents.getValidations(data));

		//endpoint 2.1
		SectionToStudentsValidationEngine sectionStudents = new SectionToStudentsValidationEngine();
		validationList.addAll(sectionStudents.getValidations(data));
		
		//FUTURE: endpoint 2.2
		
		//endpoint 2.3
		SectionToStudentToModulesValidationEngine studentModules = new SectionToStudentToModulesValidationEngine();
		validationList.addAll(studentModules.getValidations(data));

		//endpoint 2.7
		SectionToStudentToModuleToResourcesValidationEngine studentModuleResources = new SectionToStudentToModuleToResourcesValidationEngine();
		validationList.addAll(studentModuleResources.getValidations(data));
		
		//endpoint 3.4
		AssessmentValidationEngine assessment = new AssessmentValidationEngine();
		validationList.addAll(assessment.getValidations(data));

		return validationList;
	}
	

	public String formatTimeOnTask(long timeOnTaskMillis) {
		long hoursDivider = 1000*60*60;
		long minutesDivider = 1000*60;
		long secondsDivider = 1000;
		
		long hours = (long) Math.floor(timeOnTaskMillis/hoursDivider);
		
		long minutes = timeOnTaskMillis;
		if (hours > 0) {
			minutes %= (hoursDivider);
		}
		minutes = (long) Math.floor(minutes/minutesDivider);
		
		long seconds = timeOnTaskMillis;
		if (minutes > 0) {
			seconds %= (minutesDivider);
		} else if (hours > 0) {
			seconds %= (hoursDivider);
		}
		seconds = (long) Math.floor(seconds/secondsDivider);
		
		long milliseconds = timeOnTaskMillis;
		if (seconds > 0) {
			milliseconds %= (secondsDivider);
		} else if (minutes > 0) {
			milliseconds %= (minutesDivider);
		} else if (hours > 0) {
			milliseconds %= (hoursDivider);
		}
		milliseconds = milliseconds/100;
		
		StringBuilder formattedTimeOnTask = new StringBuilder();
		formattedTimeOnTask.append(String.format("%02d", hours)).append(":")
			.append(String.format("%02d", minutes)).append(":")
			.append(String.format("%02d", seconds)).append(".")
			.append(String.format("%01d", milliseconds));

		return formattedTimeOnTask.toString();
	}
}
