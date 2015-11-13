package com.pearson.test.daalt.dataservice.utility;

import java.util.Map;

import org.testng.annotations.Test;

import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;
import com.pearson.test.daalt.dataservice.mongodb.JSONUtils;
import com.pearson.test.daalt.dataservice.validation.DaaltValidation;

public class PiTokenNewJWS extends DaaltValidation {
	
	@Test
	public void executeAdhocRoute() throws Exception {
		
//		String route112 = "https://daalt-analytics.stg-prsn.com/analytics/platforms/DAALT-SQE/sections/SQE-CS-95cd5492-2c95-42b5-ad65-e2b4402f0fe8/modules/SQE-Assign-37ccf8c4-4ef1-4907-a7a3-d55806914aeb/students";

		String userName = "tusha9.educator";
		String passWord = "Password27";
		String apigeeRoute = "http://int-piapi-internal.stg-openclass.com/tokens?useJwt=true";
	 

//		String requestBody = "{\"userName\":\"" + userName + "\", \"password\":\"" + passWord + "\"}";
		String requestBody = "{\"userName\":\"dds_perf_userp1002\",\"password\":\"Fleb7eab\"}";
		SimpleRestRequestContext tokenContext = createRequest().ofType()
				.post().andFullUrl(apigeeRoute)
				.andNoAccessToken()
				.and().withRequestBody(requestBody)
				.executeAndReturnContext();
		String token = (String) JSONUtils.readJSON(tokenContext.getActualReturnedResponse()).get(apigeeTokenKey);
		System.out.println("Token from Pi is: " + token);
		

//		if (route112 != null) {
//			SimpleRestRequestContext context = createRequest().ofType()
//					.get().andFullUrl(route112)
//					.andXAuthorizationAccessTokenString(token)
//					.executeAndReturnContext();
//			
//			System.out.println("1.12 response code: " + context.getActualReturnedStatus());
//			if (context.getActualReturnedStatus() == 200) {
//				System.out.println("1.12 response: " + context.getActualReturnedResponse());
//			}
//		}
	
		
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
