package com.pearson.test.daalt.dataservice.validation.endpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.pearson.ed.pi.authentication.systemclient.PiTokenContainer;
import com.pearson.qa.apex.dataobjects.UserObject;
import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.User;
import com.pearson.test.daalt.dataservice.mongodb.JSONUtils;
import com.pearson.test.daalt.dataservice.response.model.DaaltLearningResourceItemCollectionObject;
import com.pearson.test.daalt.dataservice.response.model.LearningResourceItemObject;
import com.pearson.test.daalt.dataservice.validation.DaaltValidation;
import com.pearson.test.daalt.dataservice.validation.ResponseCode;
import com.pearson.test.daalt.dataservice.validation.Validation;

public class SectionToModuleToResourceToItemsPagination extends DaaltValidation {

	private String route;
	private String correlationId;
	public SectionToModuleToResourceToItemsPagination() {}
	
	public SectionToModuleToResourceToItemsPagination(String testCaseNumber,
			Validation validation) {
		super(testCaseNumber, validation);
		
		route = parameters.get(ROUTE);
	}
	
	public SectionToModuleToResourceToItemsPagination(String creationTestName, UserObject user, 
			String route) throws JsonGenerationException, JsonMappingException, IOException {
		setCreationTestName(creationTestName);
		setRoute(route);
		
		//needed for all DaaltValidations
		setActingUser(new User(user));
		setJson(new ObjectMapper().writeValueAsString(this));
		setName(this.getClass().toString());
		setCorrelationId("SQE-correlationId-" + UUID.randomUUID().toString());
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	@Override
	public Map<String, Boolean> getValidations() throws Exception {
		Properties config = new Properties();
		config.put("com.pearson.ed.pi.auth.token.url", TestEngine.getInstance().getApigeeTokenRoute());
		config.put("com.pearson.ed.pi.auth.system.username", getActingUserObject().getUsername());
		config.put("com.pearson.ed.pi.auth.system.password", getActingUserObject().getPassword());
		PiTokenContainer.init(config);
		String token = PiTokenContainer.getSystemToken();
		PiTokenContainer.shutdown();
		
//		String requestBody = "{\"userName\":\"" +getActingUserObject().getUsername()+ "\", \"password\":\""+getActingUserObject().getPassword()+"\"}";
//		SimpleRestRequestContext tokenContext = createRequest().ofType()
//				.post().andFullUrl(apigeeTokenRoute)
//				.andNoAccessToken()
//				.and().withRequestBody(requestBody)
//				.executeAndReturnContext();
//		String token = (String) JSONUtils.readJSON(tokenContext.getActualReturnedResponse()).get(apigeeTokenKey);
		
		String oldBaseURL = TestEngine.getInstance().getOriginalBaseUrl();
		String newBaseURL = TestEngine.getInstance().getBaseUrl();

		if (TestEngine.getInstance().getEnvironmentSwitch()) {
			route = route.replace(oldBaseURL, newBaseURL);
			//TODO: replace baseURL in all _links
		}


		System.out.println("endpoint 1.11 pagination : route: " + route);
		
		SimpleRestRequestContext context;
		List<LearningResourceItemObject> previousItems = null;
		List<LearningResourceItemObject> currentItems = null;
		int loopCount = 0;
		while (loopCount < PAGINATION_LOOP_COUNT) {
			context = createRequest().ofType()
					.get().andFullUrl(route)
					.andXAuthorizationAccessTokenString(token)
					.and().withHeader("correlation-id", correlationId)
					.executeAndReturnContext();
			
			if (context.getActualReturnedStatus() == 200) {
				System.out.println("endpoint 1.11 pagination : loopCount=" + loopCount + " response: " + context.getActualReturnedResponse());
				
				validations.put("endpoint 1.11 pagination : loopCount=" + loopCount + " executed by user: " + getActingUserObject().getId() 
						+ " Validate actual response code " + context.getActualReturnedStatus() 
						+ " matches expected response code " + ResponseCode.OK.value, true);
				
				DaaltLearningResourceItemCollectionObject actualDataServiceResponse = context
						.getResponseAsDeserializedObject(DaaltLearningResourceItemCollectionObject.class);
				
				validations.put("endpoint 1.11 pagination : loopCount=" + loopCount + " Validate response is not null ", actualDataServiceResponse != null);
				
				if (previousItems == null) {
					previousItems = Arrays.asList(actualDataServiceResponse.getItems());
				} else {
					if (currentItems != null) {
						previousItems = new ArrayList<>(currentItems);
					}
					currentItems = Arrays.asList(actualDataServiceResponse.getItems());
					
					if (currentItems.size() == previousItems.size()) {
						validations.put("endpoint 1.11 pagination : loopCount=" + loopCount + " Validate current size of item collection " + currentItems.size()
								+ " matches previous size of item collection " + previousItems.size(), 
								true);
						
						boolean sameOrder = validatePagination(previousItems, currentItems);
						validations.put("endpoint 1.11 pagination : loopCount=" + loopCount + " Validate items are returned in the same order", sameOrder);
					} else {
						validations.put("endpoint 1.11 pagination : loopCount=" + loopCount + " Validate current size of item collection " + currentItems.size()
								+ " matches previous size of item collection " + previousItems.size(), 
								false);
					}
				}
			} else {
				validations.put("endpoint 1.11 pagination : loopCount=" + loopCount + "executed by user: " + getActingUserObject().getId() 
						+ " Validate actual response code " + context.getActualReturnedStatus() 
						+ " matches expected response code 200", false);
			}
			
			loopCount++;
		}
		
		return validations;
	}
	
	private boolean validatePagination(List<LearningResourceItemObject> previousItems, List<LearningResourceItemObject> currentItems) {
		boolean sameOrder = true;
		Iterator<LearningResourceItemObject> previousItemsIter = previousItems.iterator();
		Iterator<LearningResourceItemObject> currentItemsIter = currentItems.iterator();
		while (previousItemsIter.hasNext() && sameOrder) {
			sameOrder &= (previousItemsIter.next().itemId.compareTo(currentItemsIter.next().itemId) == 0);
		}
		return sameOrder;
	}

	@Override
	public String[] getRequiredParameters() {
		return new String[] {ROUTE};
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
}
