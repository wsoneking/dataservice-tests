package com.pearson.test.daalt.dataservice.validation.endpoint;

import java.util.Map;

import org.testng.annotations.Test;

import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.mongodb.JSONUtils;
import com.pearson.test.daalt.dataservice.validation.DaaltValidation;
import com.pearson.test.daalt.dataservice.validation.ResponseCode;

public class Sandbox extends DaaltValidation {
	/**
	 * Use me to call any endpoint
	 */
	@Test
	public void executeAdhocRoute() throws Exception {
		String route = " https://daalt-analytics.stg-prsn.com/v2/analytics/platforms/DAALT-SQE/sections/SQECSFake014/modules/SQEAssignFake014/resources/SQEQuizFake014/items";
		
		String requestBody = "{\"userName\":\"" + "ameya1.instr" + "\", \"password\":\"" + "Test1234" + "\"}";
		SimpleRestRequestContext tokenContext = createRequest().ofType()
				.post().andFullUrl(TestEngine.getInstance().getApigeeTokenRoute())
				.andNoAccessToken()
				.and().withRequestBody(requestBody)
				.executeAndReturnContext();
		String token = (String) JSONUtils.readJSON(tokenContext.getActualReturnedResponse()).get(apigeeTokenKey);
		System.out.println("Token is: " + token);
		
//		token = token.replace("piid", "client_id=Gs5nCGqoqwdiccJSkVYAivHK65PCbQ0m&type=at&piid");
//		System.out.println("New Token is: " + token);
		
		SimpleRestRequestContext context = createRequest().ofType()
				.get().andFullUrl(route)
				.andXAuthorizationAccessTokenString(token)
				.executeAndReturnContext();
		
		System.out.println("response code: " + context.getActualReturnedStatus());
		if (context.getActualReturnedStatus() == ResponseCode.OK.value) {
			System.out.println("response: " + context.getActualReturnedResponse());
		}
	}
	
	/**
	 * Use me to call endpoint 3.4
	 */
	@Test
	public void lookForSeedingData() throws Exception {
		String route = "https://daalt-analytics.stg-prsn.com/v2/analytics/validate/assessments/0f248794-3ae2-4fa9-bb61-bfb58d1a71a0";
		
		String requestBody = "{\"userName\":\"" + "tusha9.educator" + "\", \"password\":\"" + "Password27" + "\"}";
		SimpleRestRequestContext tokenContext = createRequest().ofType()
				.post().andFullUrl(TestEngine.getInstance().getApigeeTokenRoute())
				.andNoAccessToken()
				.and().withRequestBody(requestBody)
				.executeAndReturnContext();
		String token = (String) JSONUtils.readJSON(tokenContext.getActualReturnedResponse()).get(apigeeTokenKey);

		SimpleRestRequestContext context = createRequest().ofType()
				.get().andFullUrl(route)
				.andXAuthorizationAccessTokenString(token)
				.executeAndReturnContext();
		
		System.out.println("response code: " + context.getActualReturnedStatus());
		if (context.getActualReturnedStatus() == ResponseCode.OK.value) {
			System.out.println("response: " + context.getActualReturnedResponse());
		}
	}

	@Override
	public Map<String, Boolean> getValidations() throws Exception {
		//not applicable
		return null;
	}

	@Override
	public String[] getRequiredParameters() {
		//not applicable
		return null;
	}
}
