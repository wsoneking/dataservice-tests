package com.pearson.test.daalt.dataservice.utility;

import com.pearson.qa.apex.builders.SimpleRestRequestBuilder;
import com.pearson.qa.apex.dataobjects.DummyAccessToken;
import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;

public class SearchSeerMsg {

	
	private static SimpleRestRequestBuilder.IBuilderPostStart createRequest() {
		return new SimpleRestRequestBuilder.ExpressionBuilder();
	}

	public static void main(String[] args) throws Exception {

		String courseSectionId = "SQE-CS-e90ccbdf-c2e2-4cdf-b505-729ebff0dc1b";
		
		String[] idpieces = courseSectionId.toLowerCase().split("-");
		int length = idpieces.length;
		
		
		
		
//		String url = "https://10.199.16.186:9200/seer/tincan/search-raw.json";
//		String url = "https://seer-es-report.stg-prsn.com/_search?pretty=false&size=1000";
//		String url = "https://seer-es-report.stg-prsn.com/seer/activity/search-raw.json";
		
		String url = "http://10.199.16.186:9200/_search?pretty=false&size=1000";
		String body = "{ \"query\" : { \"bool\" : { \"must\" : [ { \"term\" : { \"context.extensions.appId\" : \"revel\" } },"
				+ "{ \"term\" : { \"http://schema.pearson.com/daalt/courseSectionId\" : \"" + idpieces[length-1] + "\" } },"
				+ "{ \"term\" : { \"http://schema.pearson.com/daalt/courseSectionId\" : \""+ idpieces[length-2] +"\" } },"
				+ "{ \"term\" : { \"http://schema.pearson.com/daalt/courseSectionId\" : \""+ idpieces[length-3] +"\" } },"
				+ "{ \"term\" : { \"http://schema.pearson.com/daalt/courseSectionId\" : \""+ idpieces[length-4] +"\" } }] } } }"; 
		
		SimpleRestRequestContext context = createRequest()
				.ofType()
				.post()
				.andFullUrl(url)
				.andAccessToken(new DummyAccessToken("report-pw", "awesome!town"))
				.and().withRequestBody(body)
				.executeAndReturnContext();

		if (context.getActualReturnedStatus() == 200) {
			System.out.println(context.getActualReturnedResponse());
		} else {
			throw new Exception("Elastisearch query for app Id=Revel failed. " + context);
		}
		
	}

}
