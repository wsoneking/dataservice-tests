package com.pearson.test.daalt.dataservice.utility;

import java.util.Map;

import org.testng.annotations.Test;

import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.mongodb.JSONUtils;
import com.pearson.test.daalt.dataservice.validation.DaaltValidation;

public class PiTokenValidationTest extends DaaltValidation {
	
	@Test
	public void executeAdhocRoute() throws Exception {
		
		String route112 = "https://daalt-analytics.dev-prsn.com/v2/analytics/platforms/DAALT-SQE/sections/SQE-CS-436e1407-d7b0-4a08-a175-7490e9081f91/students?offset=0&limit=1";
//		String route23 = null;
//		String route27 = null;
//		String route19 = "https://daalt-analytics-temp.stg-prsn.com/v2/analytics/platforms/DAALT-SQE/sections/SQE-CS-2e963b7f-9d13-4e71-836d-ba2b8b4dc502/modules/SQE-Assign-029a297b-d858-4c6d-b80b-9762a3ddffad/resources/SQE-Chap-46da603d-dd81-4821-ab2a-9e9131372287/resources";
//		String route111 = null;
		
	

		String requestBody = "{\"userName\":\"" + "tusha9.educator" + "\", \"password\":\"" + "Password27" + "\"}";
		SimpleRestRequestContext tokenContext = createRequest().ofType()
				.post().andFullUrl(TestEngine.getInstance().getApigeeTokenRoute())
				.andNoAccessToken()
				.and().withRequestBody(requestBody)
				.executeAndReturnContext();
		String token = (String) JSONUtils.readJSON(tokenContext.getActualReturnedResponse()).get(apigeeTokenKey);
		System.out.println("Token from Pi is: " + token);
	
//		token = token.replace("piid", "client_id=Gs5nCGqoqwdiccJSkVYAivHK65PCbQ0m&piid");
//		System.out.println("New Token is: " + token);
	
//		String token = "1.0|idm|idm|client_id=Gs5nCGqoqwdiccJSkVYAivHK65PCbQ0m&piid=ffffffff53b4ea66e4b0279a23cb161a&sessid=9c83983d057f45eb991812fd5c538de7&type=at|2015-06-22T18:17:39+00:00|2015-06-22T21:17:39+00:00|366aab49cec659cc1ccdce77ce7e8374";
	
		if (route112 != null) {
			SimpleRestRequestContext context = createRequest().ofType()
					.get().andFullUrl(route112)
					.andXAuthorizationAccessTokenString(token)
					.executeAndReturnContext();
			
			System.out.println("1.12 response code: " + context.getActualReturnedStatus());
			if (context.getActualReturnedStatus() == 200) {
				System.out.println("1.12 response: " + context.getActualReturnedResponse());
			}
		}
		
/*			if (route111 != null) {
			SimpleRestRequestContext context = createRequest().ofType()
					.get().andFullUrl(route111)
					.andXAuthorizationAccessTokenString(token)
					.executeAndReturnContext();
			
			System.out.println("1.11 response code: " + context.getActualReturnedStatus());
			if (context.getActualReturnedStatus() == ResponseCode.OK.value) {
				System.out.println("1.11 response: " + context.getActualReturnedResponse());
			}
		}
		
		if (route19 != null) {
			SimpleRestRequestContext context = createRequest().ofType()
					.get().andFullUrl(route19)
					.andXAuthorizationAccessTokenString(token)
					.executeAndReturnContext();
			
			System.out.println("1.9 response code: " + context.getActualReturnedStatus());
			if (context.getActualReturnedStatus() == ResponseCode.OK.value) {
				System.out.println("1.9 response: " + context.getActualReturnedResponse());
			}
		}
		if (route23 != null) {
			SimpleRestRequestContext context = createRequest().ofType()
					.get().andFullUrl(route23)
					.andXAuthorizationAccessTokenString(token)
					.executeAndReturnContext();
			
			System.out.println("2.3 response code: " + context.getActualReturnedStatus());
			if (context.getActualReturnedStatus() == ResponseCode.OK.value) {
				System.out.println("2.3 response: " + context.getActualReturnedResponse());
			}
		}
		if (route27 != null) {
			SimpleRestRequestContext context = createRequest().ofType()
					.get().andFullUrl(route27)
					.andXAuthorizationAccessTokenString(token)
					.executeAndReturnContext();
			
			System.out.println("2.7 response code: " + context.getActualReturnedStatus());
			if (context.getActualReturnedStatus() == ResponseCode.OK.value) {
				System.out.println("2.7 response: " + context.getActualReturnedResponse());
			}
		}
		
		
	*/
		
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
