package com.pearson.test.daalt.dataservice.validation;

import java.lang.reflect.Constructor;

import org.codehaus.jackson.map.ObjectMapper;

public class ValidationFactory {

	public DaaltValidation getValidation(String testCaseNumber,
			Validation validation) throws Exception {
		String className = validation.getName().replace("class ", "");
		String name = validation.getName();
		if (validation.getName().contains(".")) {

			name = validation.getName().split("\\.")[validation.getName()
					.split("\\.").length - 1];
			System.out
					.println("(((((((((((((REDUCED CLASS NAME FOR VALIDATION TO "
							+ name);
		}

		Class<?> clazz = Class.forName(className.trim());
		if (validation.getParameters().size() == 0) {
			// its the json form of validator
			Object o = new ObjectMapper()
					.readValue(validation.getJson(), clazz);

			return (DaaltValidation) o;
		}

		Constructor<?> ctor = clazz.getConstructor(String.class,
				Validation.class);
		Object object = ctor.newInstance(testCaseNumber, validation);
		return (DaaltValidation) object;

	}
}
