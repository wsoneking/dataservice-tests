package com.pearson.test.daalt.dataservice.validation.endpoint;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.pearson.qa.apex.dataobjects.UserObject;
import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.User;
import com.pearson.test.daalt.dataservice.mongodb.JSONUtils;
import com.pearson.test.daalt.dataservice.response.model.AssessmentItemObject;
import com.pearson.test.daalt.dataservice.response.model.AssessmentObject;
import com.pearson.test.daalt.dataservice.response.model.DaaltAssessmentObject;
import com.pearson.test.daalt.dataservice.validation.DaaltValidation;
import com.pearson.test.daalt.dataservice.validation.ResponseCode;
import com.pearson.test.daalt.dataservice.validation.Validation;

public class AssessmentsAll extends DaaltValidation {

	private String route;
	private int expectedResponseCode;
	private AssessmentObject expectedAssessment;
	
	public AssessmentsAll() {}
	
	@SuppressWarnings("unchecked")
	public AssessmentsAll(String testCaseNumber,
			Validation validation) {
		super(testCaseNumber, validation);
		
		route = parameters.get(ROUTE);
		expectedResponseCode = Integer.valueOf(parameters.get(EXPECTED_RESPONSE_CODE));
		Object obj = parameters.get(EXPECTED_ASSESSMENT);
		expectedAssessment =  (AssessmentObject) obj;
	}
	
	public AssessmentsAll(String creationTestName, UserObject user, 
			String route, int expectedResponseCode, 
			AssessmentObject expectedAssessment) throws JsonGenerationException, JsonMappingException, IOException {
		setCreationTestName(creationTestName);
		setRoute(route);
		setExpectedResponseCode(expectedResponseCode);
		setExpectedAssessment(expectedAssessment);
		
		//needed for all DaaltValidations
		setActingUser(new User(user));
		setJson(new ObjectMapper().writeValueAsString(this));
		setName(this.getClass().toString());
	}
	
	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public int getExpectedResponseCode() {
		return expectedResponseCode;
	}

	public void setExpectedResponseCode(int expectedResponseCode) {
		this.expectedResponseCode = expectedResponseCode;
	}

	public AssessmentObject getExpectedAssessment() {
		return expectedAssessment;
	}

	public void setExpectedAssessment(AssessmentObject expectedAssessment) {
		this.expectedAssessment = expectedAssessment;
	}
	
	@Override
	public Map<String, Boolean> getValidations() throws Exception {
		String requestBody = "{\"userName\":\"" +getActingUserObject().getUsername()+ "\", \"password\":\""+getActingUserObject().getPassword()+"\"}";
		SimpleRestRequestContext tokenContext = createRequest().ofType()
				.post().andFullUrl(TestEngine.getInstance().getApigeeTokenRoute())
				.andNoAccessToken()
				.and().withRequestBody(requestBody)
				.executeAndReturnContext();
		String token = (String) JSONUtils.readJSON(tokenContext.getActualReturnedResponse()).get(apigeeTokenKey);
		
		String oldBaseURL = TestEngine.getInstance().getOriginalBaseUrl();
		String newBaseURL = TestEngine.getInstance().getBaseUrl();

		if (TestEngine.getInstance().getEnvironmentSwitch()) {
			route = route.replace(oldBaseURL, newBaseURL);
			//TODO: replace baseURL in all _links
		}
		
		SimpleRestRequestContext context = createRequest().ofType()
				.get().andFullUrl(route)
				.andXAuthorizationAccessTokenString(token)
				.executeAndReturnContext();
		
		System.out.println("endpoint 3.4 : route: " + route);
		System.out.println("endpoint 3.4 : calling user id: " + getActingUserObject().getId());
		System.out.println("endpoint 3.4 : calling user username: " + getActingUserObject().getUsername());
		System.out.println("endpoint 3.4 : calling user password: " + getActingUserObject().getPassword());
		
		//validate value of responseCode
		boolean responseCodeCorrect = context.getActualReturnedStatus() == expectedResponseCode;
		validations.put("endpoint 3.4 : executed by user: " + getActingUserObject().getId() 
				+ " Validate actual response code " + context.getActualReturnedStatus() 
				+ " matches expected response code " + expectedResponseCode, responseCodeCorrect);
				
		if (expectedResponseCode == ResponseCode.OK.value && context.getActualReturnedStatus() == ResponseCode.OK.value) {
			System.out.println("endpoint 3.4 : response: " + context.getActualReturnedResponse());
			
			DaaltAssessmentObject actualDataServiceResponse = context
					.getResponseAsDeserializedObject(DaaltAssessmentObject.class);
			
			validations.put("endpoint 3.4 : Validate response is not null ", actualDataServiceResponse != null);
			
			//validate assessment
			AssessmentObject actualAssessment = actualDataServiceResponse.getItems()[0];
			validateAssessment(expectedAssessment, actualAssessment);
			
		}
		
		return validations;
	}

	private void validateAssessment(AssessmentObject expectedAssessment, AssessmentObject actualAssessment) {
		
		//validate assessmentId - string
		if (expectedAssessment.assessmentId != null) {
			validations.put("endpoint 3.4 : For item " + actualAssessment.assessmentId
					+ ": Validate actual assessmentId is not null ", 
					actualAssessment.assessmentId != null);
			if (actualAssessment.assessmentId != null) {
				validations.put("endpoint 3.4 : For assessment " + actualAssessment.assessmentId
						+ ": Validate actual assessmentId: " + actualAssessment.assessmentId
						+ " matches expected assessmentId: " + expectedAssessment.assessmentId, 
						actualAssessment.assessmentId.compareTo(expectedAssessment.assessmentId) == 0);
			}
		}
		
		//validate assessmentType - string
		if(expectedAssessment.assessmentType != null){
			validations.put("endpoint 3.4 : For item " + actualAssessment.assessmentId
					+ ": Validate actual assessmentType is not null ", 
					actualAssessment.assessmentType != null);
			if(actualAssessment.assessmentType != null){
				validations.put("endpoint 3.4 : For assessment " + actualAssessment.assessmentId
						+ ": Validate actual assessmentType: " + actualAssessment.assessmentType
						+ " matches expected assessmentType: " + expectedAssessment.assessmentType, 
						actualAssessment.assessmentType.compareTo(expectedAssessment.assessmentType) == 0);
			}
		}
		
		
		//validate assessmentSeeded - boolean
		validations.put("endpoint 3.4 : For assessment " + actualAssessment.assessmentId
				+ ": Validate actual assessmentSeeded: " + actualAssessment.assessmentSeeded
				+ " matches expected assessmentSeeded: " + expectedAssessment.assessmentSeeded, 
				actualAssessment.assessmentSeeded == expectedAssessment.assessmentSeeded);
		
		//validate assessmentLastSeedDateTime - string
		if(expectedAssessment.assessmentLastSeedDateTime != null){
			validations.put("endpoint 3.4 : For item " + actualAssessment.assessmentId
					+ ": Validate actual assessmentLastSeedDateTime is not null ", 
					actualAssessment.assessmentLastSeedDateTime != null);
			if(actualAssessment.assessmentLastSeedDateTime != null){
				validations.put("endpoint 3.4 : For assessment " + actualAssessment.assessmentId
						+ ": Validate actual assessmentLastSeedDateTime: " + actualAssessment.assessmentLastSeedDateTime
						+ " matches expected assessmentLastSeedDateTime: " + expectedAssessment.assessmentLastSeedDateTime, 
						actualAssessment.assessmentLastSeedDateTime.compareTo(expectedAssessment.assessmentLastSeedDateTime) == 0);
			}
		}
		
		//validate assessmentLastSeedType - string
		if(expectedAssessment.assessmentLastSeedType != null){
			validations.put("endpoint 3.4 : For item " + actualAssessment.assessmentId
					+ ": Validate actual assessmentLastSeedType is not null ", 
					actualAssessment.assessmentLastSeedType != null);
			if(actualAssessment.assessmentLastSeedType != null){
				validations.put("endpoint 3.4 : For assessment " + actualAssessment.assessmentId
						+ ": Validate actual assessmentLastSeedType: " + actualAssessment.assessmentLastSeedType
						+ " matches expected assessmentLastSeedType: " + expectedAssessment.assessmentLastSeedType, 
						actualAssessment.assessmentLastSeedType.compareTo(expectedAssessment.assessmentLastSeedType) == 0);
			}
		}
		
		//validate assessmentItem sub-collection
		List<AssessmentItemObject> expectedAssessmentItems = Arrays.asList(expectedAssessment.assessmentItems);
		List<AssessmentItemObject> actualAssessmentItems = Arrays.asList(actualAssessment.assessmentItems);
		validations.put("endpoint 3.4 : For assessment " + actualAssessment.assessmentId
				+ ": Validate actual size of assessmentItem sub-collection " + actualAssessmentItems.size()
				+ " matches expected size of assessmentItem sub-collection " + expectedAssessmentItems.size(), 
				actualAssessmentItems.size() == expectedAssessmentItems.size());
		for(AssessmentItemObject expectedAssessmentItem : expectedAssessmentItems){
			AssessmentItemObject actualAssessmentItem = actualAssessment.getAssessmentItemByItemId(expectedAssessmentItem.itemId);
			validations.put("endpoint 3.4 : For assessment " + actualAssessment.assessmentId
					+ ": Validate assessmentItem itemId " + expectedAssessmentItem.itemId
					+ " exists in collection ", actualAssessmentItem != null);
			if (actualAssessmentItem != null) {
				validateAssessmentItem(expectedAssessmentItem, actualAssessmentItem, actualAssessment.assessmentId);
			}
		}
	}
	
	private void validateAssessmentItem(AssessmentItemObject expectedAssessmentItem, AssessmentItemObject actualAssessmentItem, String assessmentId) {
	
		//validate itemId - string
		if(expectedAssessmentItem.itemId != null){
			validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
					+ ": Validate actual itemId is not null ", 
					actualAssessmentItem.itemId != null);
			if(actualAssessmentItem.itemId != null){
				validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
						+ ": Validate actual itemId: " + actualAssessmentItem.itemId
						+ " matches expected itemId: " + expectedAssessmentItem.itemId,
						actualAssessmentItem.itemId.compareTo(expectedAssessmentItem.itemId) == 0);
			}
		}
		
		//validate itemSequence - float
		validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
				+ ": Validate actual itemSequence: " + actualAssessmentItem.itemSequence
				+ " matches expected itemSequence: " + expectedAssessmentItem.itemSequence,
				actualAssessmentItem.itemSequence == expectedAssessmentItem.itemSequence);
		
		//validate questionType - string
		if(expectedAssessmentItem.questionType != null){
			validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
					+ ": Validate actual questionType is not null ", 
					actualAssessmentItem.questionType != null);
			if(actualAssessmentItem.questionType != null){
				validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
						+ ": Validate actual questionType: " + actualAssessmentItem.questionType
						+ " matches expected questionType: " + expectedAssessmentItem.questionType,
						actualAssessmentItem.questionType.compareTo(expectedAssessmentItem.questionType) == 0);
			}
		}
		
		//validate questionPresentationFormat - string
		if(expectedAssessmentItem.questionPresentationFormat != null){
			validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
					+ ": Validate actual questionPresentationFormat is not null ", 
					actualAssessmentItem.questionPresentationFormat != null);
			if(actualAssessmentItem.questionPresentationFormat != null){
				validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
						+ ": Validate actual questionPresentationFormat: " + actualAssessmentItem.questionPresentationFormat
						+ " matches expected questionPresentationFormat: " + expectedAssessmentItem.questionPresentationFormat,
						actualAssessmentItem.questionPresentationFormat.compareTo(expectedAssessmentItem.questionPresentationFormat) == 0);
			}
		}
		
		//validate questionText - string
		if(expectedAssessmentItem.questionText != null){
			validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
					+ ": Validate actual questionText is not null ", 
					actualAssessmentItem.questionText != null);
			if(actualAssessmentItem.questionText != null){
				validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
						+ ": Validate actual questionText: " + actualAssessmentItem.questionText
						+ " matches expected questionText: " + expectedAssessmentItem.questionText,
						actualAssessmentItem.questionText.compareTo(expectedAssessmentItem.questionText) == 0);
			}
		}
		
		//validate itemSeeded - boolean
		validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
				+ ": Validate actual itemSeeded: " + actualAssessmentItem.itemSeeded
				+ " matches expected itemSeeded: " + expectedAssessmentItem.itemSeeded,
				actualAssessmentItem.itemSeeded == expectedAssessmentItem.itemSeeded);
		
		//validate itemLastSeedDateTime - string
		if(expectedAssessmentItem.itemLastSeedDateTime != null){
			validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
					+ ": Validate actual itemLastSeedDateTime is not null ", 
					actualAssessmentItem.itemLastSeedDateTime != null);
			if(actualAssessmentItem.itemLastSeedDateTime != null){
				validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
						+ ": Validate actual itemLastSeedDateTime: " + actualAssessmentItem.itemLastSeedDateTime
						+ " matches expected itemLastSeedDateTime: " + expectedAssessmentItem.itemLastSeedDateTime,
						actualAssessmentItem.itemLastSeedDateTime.compareTo(expectedAssessmentItem.itemLastSeedDateTime) == 0);
			}
		}
		
		//validate itemLastSeedType - string
		if(expectedAssessmentItem.itemLastSeedType != null){
			validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
					+ ": Validate actual itemLastSeedType is not null ", 
					actualAssessmentItem.itemLastSeedType != null);
			if(actualAssessmentItem.itemLastSeedType != null){
				validations.put("endpoint 3.4 : For assessment " + assessmentId + ", assessmentItem " + actualAssessmentItem.itemId
						+ ": Validate actual itemLastSeedType: " + actualAssessmentItem.itemLastSeedType
						+ " matches expected itemLastSeedType: " + expectedAssessmentItem.itemLastSeedType,
						actualAssessmentItem.itemLastSeedType.compareTo(expectedAssessmentItem.itemLastSeedType) == 0);
			}
		}
	}
	
	@Override
	public String[] getRequiredParameters() {
		return new String[] {ROUTE, EXPECTED_RESPONSE_CODE, EXPECTED_ASSESSMENT};
	}
	
	@Override
	public String getExpectedResultsPrintString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("3.4 : AssessmentSeeding");
		toReturn.append("\n...route: " + getRoute());
		try {
			toReturn.append("\n...calling user id: " + getActingUserObject().getId());
			toReturn.append("\n...calling user username: " + getActingUserObject().getUsername());
			toReturn.append("\n...calling user password: " + getActingUserObject().getPassword());
		} catch (Exception e) {
			System.out.println("ORLY???");
		}
		toReturn.append("\n...expected response code: " + getExpectedResponseCode());
		if (expectedAssessment != null && getExpectedResponseCode()==200) {
			toReturn.append("\n......assessmentId: " + expectedAssessment.assessmentId);
			toReturn.append("\n......assessmentType: " + expectedAssessment.assessmentType);
			toReturn.append("\n......assessmentSeeded: " + expectedAssessment.assessmentSeeded);
			toReturn.append("\n......assessmentLastSeedDateTime: " + expectedAssessment.assessmentLastSeedDateTime);
			toReturn.append("\n......assessmentLastSeedType: " + expectedAssessment.assessmentLastSeedType);
			if (expectedAssessment.assessmentItems != null) {
				for (AssessmentItemObject item : expectedAssessment.assessmentItems) {
					toReturn.append("\n.........itemId: " + item.itemId);
					toReturn.append("\n.........itemSequence: " + item.itemSequence);
					toReturn.append("\n.........questionType: " + item.questionType);
					toReturn.append("\n.........questionPresentationFormat: " + item.questionPresentationFormat);
					toReturn.append("\n.........questionText: " + item.questionText);
					toReturn.append("\n.........itemSeeded: " + item.itemSeeded);
					toReturn.append("\n.........itemLastSeedDateTime: " + item.itemLastSeedDateTime);
					toReturn.append("\n.........itemLastSeedType: " + item.itemLastSeedType);
					toReturn.append("\n.....................................................");
				}
			}
		}
		return toReturn.toString();
	}
}
