package com.pearson.test.daalt.dataservice.validation.endpoint;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.pearson.test.daalt.dataservice.mongodb.JSONUtils;
import com.pearson.ed.pi.authentication.systemclient.PiTokenContainer;
import com.pearson.qa.apex.dataobjects.UserObject;
import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.User;
import com.pearson.test.daalt.dataservice.response.model.DaaltStudentModuleCollectionObject;
import com.pearson.test.daalt.dataservice.response.model.LinkObject;
import com.pearson.test.daalt.dataservice.response.model.StudentModuleObject;
import com.pearson.test.daalt.dataservice.validation.DaaltValidation;
import com.pearson.test.daalt.dataservice.validation.ResponseCode;
import com.pearson.test.daalt.dataservice.validation.Validation;

/**
 * 2.3
 */

public class SectionToStudentToModulesAll extends DaaltValidation {

	private String route;
	private int expectedResponseCode;
	private StudentModuleObject[] expectedModules;
	private String correlationId;
	private Map<String, LinkObject> expectedEnvelopeLevelLinks;
	private Integer offset;
	private Integer limit;
	private int itemCount;
	
	public SectionToStudentToModulesAll() {}
	
	@SuppressWarnings("unchecked")
	public SectionToStudentToModulesAll(String testCaseNumber,
			Validation validation) {
		super(testCaseNumber, validation);
		
		route = parameters.get(ROUTE);
		expectedResponseCode = Integer.valueOf(parameters.get(EXPECTED_RESPONSE_CODE));
		Object obj = parameters.get(EXPECTED_MODULES);
		expectedModules =  (StudentModuleObject[]) obj;
	}
	
	public SectionToStudentToModulesAll(String creationTestName, UserObject user, 
			String route, int expectedResponseCode, 
			StudentModuleObject[] expectedResources,
			Map<String, LinkObject> expectedEnvelopeLevelLinks, 
			Integer offset, Integer limit, int itemCount) throws JsonGenerationException, JsonMappingException, IOException {
		setCreationTestName(creationTestName);
		setRoute(route);
		setExpectedResponseCode(expectedResponseCode);
		setExpectedResources(expectedResources);
		setExpectedEnvelopeLevelLinks(expectedEnvelopeLevelLinks);
		setOffset(offset);
		setLimit(limit);
		setItemCount(itemCount);
		setCorrelationId("SQE-correlationId-" + UUID.randomUUID().toString());
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

	public StudentModuleObject[] getExpectedResources() {
		return expectedModules;
	}

	public void setExpectedResources(StudentModuleObject[] expectedResources) {
		this.expectedModules= expectedResources;
	}

	public int getExpectedResponseCode() {
		return expectedResponseCode;
	}

	public void setExpectedResponseCode(int expectedResponseCode) {
		this.expectedResponseCode = expectedResponseCode;
	}
	public String getEffectiveRoute() {
		StringBuilder effectiveRoute = new StringBuilder();
		effectiveRoute.append(route);
		
		if (offset != null && limit != null) {
			effectiveRoute.append("?offset=").append(offset).append("&limit=").append(limit);
		}
		
		return effectiveRoute.toString();
	}
	
	public Map<String, LinkObject> getExpectedEnvelopeLevelLinks() {
		return expectedEnvelopeLevelLinks;
	}

	public void setExpectedEnvelopeLevelLinks(
			Map<String, LinkObject> expectedEnvelopeLevelLinks) {
		this.expectedEnvelopeLevelLinks = expectedEnvelopeLevelLinks;
	}
	
	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
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

		SimpleRestRequestContext context = createRequest().ofType()
				.get().andFullUrl(getEffectiveRoute())
				.andXAuthorizationAccessTokenString(token).and().withHeader("correlation-id", correlationId)
				.executeAndReturnContext();
		
		System.out.println("endpoint 2.3 : route: " + getEffectiveRoute());
		System.out.println("endpoint 2.3 : calling user id: " + getActingUserObject().getId());
		System.out.println("endpoint 2.3 : calling user username: " + getActingUserObject().getUsername());
		System.out.println("endpoint 2.3 : calling user password: " + getActingUserObject().getPassword());
		System.out.println("endpoint 2.3 : calling user correlation-id: " + correlationId);
		//validate value of responseCode
		boolean responseCodeCorrect = context.getActualReturnedStatus() == expectedResponseCode;
		validations.put("endpoint 2.3 : executed by user: " + getActingUserObject().getId() 
				+ " Validate actual response code " + context.getActualReturnedStatus() 
				+ " matches expected response code " + expectedResponseCode, responseCodeCorrect);
		
		if (expectedResponseCode == ResponseCode.OK.value && context.getActualReturnedStatus() == ResponseCode.OK.value) {
			System.out.println("endpoint 2.3 : response: " + context.getActualReturnedResponse());
			
			DaaltStudentModuleCollectionObject actualDataServiceResponse = context
					.getResponseAsDeserializedObject(DaaltStudentModuleCollectionObject.class);
			
			validations.put("endpoint 2.3 : Validate response is not null ", actualDataServiceResponse != null);
			
			//validate students collection
			List<StudentModuleObject> expectedModuleList = Arrays.asList(expectedModules);
			List<StudentModuleObject> actualModuleList = Arrays.asList(actualDataServiceResponse.getItems());
			validations.put("endpoint 2.3 : Validate actual size of module collection " + actualModuleList.size()
					+ " matches expected size of module collection " + expectedModuleList.size(), 
					actualModuleList.size() == expectedModuleList.size());
			
			float previousSequence = -1;
			for (StudentModuleObject actualModule : actualModuleList) {
				if (previousSequence > -1) {
					validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
							+ ": Validate actual learningModuleSequence: " + actualModule.learningModuleSequence + " is in order ", 
							actualModule.learningModuleSequence > previousSequence);
					previousSequence = actualModule.learningModuleSequence;
				}
			}
			
			for (StudentModuleObject expectedModule : expectedModuleList) {
				StudentModuleObject actualModule = actualDataServiceResponse.getModuleWithId(expectedModule.getId());
				validations.put("endpoint 2.3 : Validate module " + expectedModule.getId() + " exists in collection ", actualModule != null);
				if (actualModule != null) {
					validateModule(expectedModule, actualModule);
				}
			}
			

			//verify values of offset, limit, and itemCount
			if (offset == null) {
				validations.put("endpoint 2.3 : Validate actual offset " + actualDataServiceResponse.offset
						+ " matches expected offset 0 (default value)", 
						actualDataServiceResponse.offset == 0);
			} else {
				validations.put("endpoint 2.3 : Validate actual offset " + actualDataServiceResponse.offset
						+ " matches expected offset " + offset, 
						actualDataServiceResponse.offset == offset);
			}
			
			if (limit == null) {
				validations.put("endpoint 2.3 : Validate actual limit " + actualDataServiceResponse.limit
						+ " matches expected limit 100 (default value)", 
						actualDataServiceResponse.limit == 100);
			} else {
				validations.put("endpoint 2.3 : Validate actual limit " + actualDataServiceResponse.limit
						+ " matches expected limit " + limit, 
						actualDataServiceResponse.limit == limit);
			}
			
			validations.put("endpoint 2.3 : Validate actual itemCount " + actualDataServiceResponse.itemCount
					+ " matches expected itemCount " + itemCount, 
					actualDataServiceResponse.itemCount == itemCount);
			
			//TODO: verify contents of expectedEnvelopeLevelLinks == contents of actualDataServiceResponse.getLinks()
			// Please don't verify _links at this time. It is not yet implemented
			
		}			
		return validations;
	}
	
	private void validateModule(StudentModuleObject expectedModule, StudentModuleObject actualModule) {
		//don't need to validate : platformId, courseSectionId, studentId, learningModuleId
		
		//validate studentFirstName;
		if (expectedModule.studentFirstName != null) {
			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
					+ ": Validate actual studentFirstName is not null ", 
					actualModule.studentFirstName != null);
			if (actualModule.studentFirstName != null) {
				validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
						+ ": Validate actual studentFirstName: " + actualModule.studentFirstName
						+ " matches expected studentFirstName: " + expectedModule.studentFirstName, 
						actualModule.studentFirstName.compareTo(expectedModule.studentFirstName) == 0);
			}
		}
		
		//validate studentLastName;
		if (expectedModule.studentLastName != null) {
			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
					+ ": Validate actual studentLastName is not null ", 
					actualModule.studentLastName != null);
			if (actualModule.studentLastName != null) {
				validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
						+ ": Validate actual studentLastName: " + actualModule.studentLastName
						+ " matches expected studentLastName: " + expectedModule.studentLastName, 
						actualModule.studentLastName.compareTo(expectedModule.studentLastName) == 0);
			}
		}
		
		//validate learningModuleSequence;
		validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
				+ ": Validate actual learningModuleSequence: " + actualModule.learningModuleSequence
				+ " matches expected learningModuleSequence: " + expectedModule.learningModuleSequence, 
				actualModule.learningModuleSequence == expectedModule.learningModuleSequence);
		
		//validate learningModuleTitle
		if (expectedModule.learningModuleTitle != null) {
			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
					+ ": Validate actual learningModuleTitle is not null ", 
					actualModule.learningModuleTitle != null);
			if (actualModule.learningModuleTitle != null) {
				validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
				+ ": Validate actual learningModuleTitle: " + actualModule.learningModuleTitle
				+ " matches expected learningModuleTitle: " + expectedModule.learningModuleTitle, 
				actualModule.learningModuleTitle.compareTo(expectedModule.learningModuleTitle) == 0);
			}
		}

		//validate pointsPossible; Float 
		if (expectedModule.pointsPossible == null) {
//			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
//					+ ": Validate actual pointsPossible is null ", 
//					actualModule.pointsPossible == null);
		} else {
			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
					+ ": Validate actual pointsPossible is not null ", 
					actualModule.pointsPossible != null);
			if (actualModule.pointsPossible != null) {
				validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
						+ ": Validate actual pointsPossible: " + actualModule.pointsPossible
						+ " matches expected pointsPossible: " + expectedModule.pointsPossible, 
						actualModule.pointsPossible .equals( expectedModule.pointsPossible));
			}
		}
		
		//validate practicePointsPossible; Float 
		if (expectedModule.practicePointsPossible == null) {
//			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
//					+ ": Validate actual practicePointsPossible is null ", 
//					actualModule.practicePointsPossible == null);
		} else {
			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
					+ ": Validate actual practicePointsPossible is not null ", 
					actualModule.practicePointsPossible != null);
			if (actualModule.practicePointsPossible != null) {
				validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
						+ ": Validate actual practicePointsPossible: " + actualModule.practicePointsPossible
						+ " matches expected practicePointsPossible: " + expectedModule.practicePointsPossible, 
						actualModule.practicePointsPossible .equals(expectedModule.practicePointsPossible));
			}
		}
		
		//validate studentPoints; Float 
		if (expectedModule.studentPoints == null) {
//			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
//					+ ": Validate actual studentPoints is null ", 
//					actualModule.studentPoints == null);
		} else {
			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
					+ ": Validate actual studentPoints is not null ", 
					actualModule.studentPoints != null);
			if (actualModule.studentPoints != null) {
				validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
						+ ": Validate actual studentPoints: " + actualModule.studentPoints
						+ " matches expected studentPoints: " + expectedModule.studentPoints, 
						actualModule.studentPoints .equals( expectedModule.studentPoints));
			}
		}
		
		//validate studentPracticePoints; Float 
		if (expectedModule.studentPracticePoints == null) {
//			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
//					+ ": Validate actual studentPracticePoints is null ", 
//					actualModule.studentPracticePoints == null);
		} else {
			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
					+ ": Validate actual studentPracticePoints is not null ", 
					actualModule.studentPracticePoints != null);
			if (actualModule.studentPracticePoints != null) {
				validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
						+ ": Validate actual studentPracticePoints: " + actualModule.studentPracticePoints
						+ " matches expected studentPracticePoints: " + expectedModule.studentPracticePoints, 
						actualModule.studentPracticePoints .equals( expectedModule.studentPracticePoints));
			}
		}
		
		//validate studentPercent; Float 
		if (expectedModule.studentPercent == null) {
//			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
//					+ ": Validate actual studentPercent is null ", 
//					actualModule.studentPercent == null);
		} else {
			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
					+ ": Validate actual studentPercent is not null ", 
					actualModule.studentPercent != null);
			if (actualModule.studentPercent != null) {
				validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
						+ ": Validate actual studentPercent: " + actualModule.studentPercent
						+ " matches expected studentPercent: " + expectedModule.studentPercent, 
						actualModule.studentPercent .equals( expectedModule.studentPercent));
			}
		}
				
		//validate timeSpentAssessment
		if (expectedModule.timeSpentAssessment != null) {
			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
					+ ": Validate actual timeSpentAssessment is not null ", 
					actualModule.timeSpentAssessment != null);
			if (actualModule.timeSpentAssessment != null) {
				validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
						+ ": Validate actual timeSpentAssessment: " + actualModule.timeSpentAssessment
						+ " matches expected timeSpentAssessment: " + expectedModule.timeSpentAssessment, 
						actualModule.timeSpentAssessment.compareTo(expectedModule.timeSpentAssessment) == 0);
			}
		}
		
		//validate timeSpentLearning
		if (expectedModule.timeSpentLearning != null) {
			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
					+ ": Validate actual timeSpentLearning is not null ", 
					actualModule.timeSpentLearning != null);
			if (actualModule.timeSpentLearning != null) {
				validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
						+ ": Validate actual timeSpentLearning: " + actualModule.timeSpentLearning
						+ " matches expected timeSpentLearning: " + expectedModule.timeSpentLearning, 
						actualModule.timeSpentLearning.compareTo(expectedModule.timeSpentLearning) == 0);
			}
		}
		
		//validate timeSpentTotal
		if (expectedModule.timeSpentTotal != null) {
			validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
					+ ": Validate actual timeSpentTotal is not null ", 
					actualModule.timeSpentTotal != null);
			if (actualModule.timeSpentTotal != null) {
				validations.put("endpoint 2.3 : For module " + actualModule.learningModuleId
						+ ": Validate actual timeSpentTotal: " + actualModule.timeSpentTotal
						+ " matches expected timeSpentTotal: " + expectedModule.timeSpentTotal, 
						actualModule.timeSpentTotal.compareTo(expectedModule.timeSpentTotal) == 0);
			}
		}
	}

	@Override
	public String[] getRequiredParameters() {
		return new String[] {ROUTE, EXPECTED_RESPONSE_CODE, EXPECTED_MODULES};
	}
	
	@Override
	public String getExpectedResultsPrintString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("2.3 : SectionToStudentToModules");
		toReturn.append("\n...route: " + getEffectiveRoute());
		try {
			toReturn.append("\n...calling user id: " + getActingUserObject().getId());
			toReturn.append("\n...calling user username: " + getActingUserObject().getUsername());
			toReturn.append("\n...calling user password: " + getActingUserObject().getPassword());
		} catch (Exception e) {
			System.out.println("ORLY???");
		}
		toReturn.append("\n...expected response code: " + getExpectedResponseCode());
		toReturn.append("\n...expected offset: " + (getOffset() == null ? 0 : getOffset()));
		toReturn.append("\n...expected limit: " + (getLimit() == null ? 100 : getLimit()));
		toReturn.append("\n...expected itemCount: " + getItemCount());
		toReturn.append("\n...expected response code: " + getExpectedResponseCode());
		if (expectedModules != null && getExpectedResponseCode()==200) {
			for (StudentModuleObject expectedModule : expectedModules) {
				toReturn.append("\n......courseSectionId: " + expectedModule.courseSectionId);
				toReturn.append("\n......learningModuleId: " + expectedModule.learningModuleId);
				toReturn.append("\n......learningModuleSequence: " + expectedModule.learningModuleSequence);
				toReturn.append("\n......learningModuleTitle: " + expectedModule.learningModuleTitle);
				toReturn.append("\n......platformId: " + expectedModule.platformId);
				toReturn.append("\n......studentId: " + expectedModule.studentId);
				toReturn.append("\n......studentFirstName: " + expectedModule.studentFirstName);
				toReturn.append("\n......studentLastName: " + expectedModule.studentLastName);
				toReturn.append("\n......studentPercent: " + expectedModule.studentPercent);
				toReturn.append("\n......studentPoints: " + expectedModule.studentPoints);
				toReturn.append("\n......studentPracticePoints: " + expectedModule.studentPracticePoints);
				toReturn.append("\n......timeSpentAssessment: " + expectedModule.timeSpentAssessment);
				toReturn.append("\n......timeSpentLearning: " + expectedModule.timeSpentLearning);
				toReturn.append("\n......timeSpentTotal: " + expectedModule.timeSpentTotal);
				toReturn.append("\n......pointsPossible: " + expectedModule.pointsPossible);
				toReturn.append("\n......practicePointsPossible: " + expectedModule.practicePointsPossible);
				toReturn.append("\n.....................................................");
			}
		}
		return toReturn.toString();
	}

}
