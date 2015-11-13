package com.pearson.test.daalt.dataservice.utility;

import java.util.Map;

import org.testng.annotations.Test;

import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;
import com.pearson.test.daalt.dataservice.mongodb.JSONUtils;
import com.pearson.test.daalt.dataservice.validation.DaaltValidation;

public class PiTokenNew extends DaaltValidation {
	
	@Test
	public void executeAdhocRoute() throws Exception {
		
		String route112 = "https://daalt-analytics.stg-prsn.com/analytics/platforms/DAALT-SQE/sections/SQE-CS-95cd5492-2c95-42b5-ad65-e2b4402f0fe8/modules/SQE-Assign-37ccf8c4-4ef1-4907-a7a3-d55806914aeb/students";

		String userName = "tusha9.educator";
		String passWord = "Password27";
		String firstPost = "username="+userName+"&password="+passWord+"&grant_type=password&client_id=j2G2uKZ3Ca5DCPyPSj2XwuSOPEacHsr5&login_success_url=www.google.com";
	 
		SimpleRestRequestContext tokenContext1 = createRequest().ofType().post().andFullUrl("http://int-piapi.stg-openclass.com/webflow-int/login/webcredentials")
				.andNoAccessToken().and().withHeader("Content-Type", "application/x-www-form-urlencoded").and().withRequestBody(firstPost)
				.executeAndReturnContext();
		String responseBody1 = tokenContext1.getActualReturnedResponse().replaceAll("\t", "").replaceAll(" ", "").replaceAll("\n", "");
		@SuppressWarnings("unchecked")
		String token1 =  (String)( (Map<String, Object>) JSONUtils.readJSON(responseBody1).get("data") ).get("access_token");
//		System.out.println(token1);
		
		SimpleRestRequestContext tokenContext2 = createRequest().ofType().get().andFullUrl("http://int-piapi.stg-openclass.com/v1/pi_return_pi_token")
				.andNoAccessToken().and().withHeader("Authorization", "Bearer "+token1).executeAndReturnContext();
		String responseBody2 = tokenContext2.getActualReturnedResponse().replaceAll("\t", "").replaceAll(" ", "").replaceAll("\n", "");
//		System.out.println(responseBody2);
		String token2 = (String) JSONUtils.readJSON(responseBody2).get("pi_token");
		System.out.println("Final Token is:"+ token2);
				

	
		if (route112 != null) {
			SimpleRestRequestContext context = createRequest().ofType()
					.get().andFullUrl(route112)
					.andXAuthorizationAccessTokenString(token2)
					.executeAndReturnContext();
			
			System.out.println("1.12 response code: " + context.getActualReturnedStatus());
			if (context.getActualReturnedStatus() == 200) {
				System.out.println("1.12 response: " + context.getActualReturnedResponse());
			}
		}
	
		
	}
	
	

	@Override
	public Map<String, Boolean> getValidations() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getRequiredParameters() {
		// TODO Auto-generated method stub
		return null;
	}
}
