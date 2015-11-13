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
import com.pearson.test.daalt.dataservice.response.model.DaaltStudentLearningResourceCollectionObject;
import com.pearson.test.daalt.dataservice.response.model.LinkObject;
import com.pearson.test.daalt.dataservice.response.model.StudentLearningResourceObject;
import com.pearson.test.daalt.dataservice.validation.DaaltValidation;
import com.pearson.test.daalt.dataservice.validation.ResponseCode;
import com.pearson.test.daalt.dataservice.validation.Validation;

/**
 * 2.7
 *
 */

public class SectionToStudentToModuleToResourcesAll extends DaaltValidation {

	private String route;
	private int expectedResponseCode;
	private StudentLearningResourceObject[] expectedResources;
	private Map<String, LinkObject> expectedEnvelopeLevelLinks;
	private Integer offset;
	private Integer limit;
	private int itemCount;
	private String correlationId;
	public SectionToStudentToModuleToResourcesAll() {}
	
	@SuppressWarnings("unchecked")
	public SectionToStudentToModuleToResourcesAll(String testCaseNumber,
			Validation validation) {
		super(testCaseNumber, validation);
		
		route = parameters.get(ROUTE);
		expectedResponseCode = Integer.valueOf(parameters.get(EXPECTED_RESPONSE_CODE));
		Object obj = parameters.get(EXPECTED_RESOURCES);
		expectedResources =  (StudentLearningResourceObject[]) obj;
	}
	
	public SectionToStudentToModuleToResourcesAll(String creationTestName, UserObject user, 
			String route, int expectedResponseCode, 
			StudentLearningResourceObject[] expectedResources,
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
	
	public String getEffectiveRoute() {
		StringBuilder effectiveRoute = new StringBuilder();
		effectiveRoute.append(route);
		
		if (offset != null && limit != null) {
			effectiveRoute.append("?offset=").append(offset).append("&limit=").append(limit);
		}
		
		return effectiveRoute.toString();
	}

	public StudentLearningResourceObject[] getExpectedResources() {
		return expectedResources;
	}

	public void setExpectedResources(StudentLearningResourceObject[] expectedResources) {
		this.expectedResources= expectedResources;
	}

	public int getExpectedResponseCode() {
		return expectedResponseCode;
	}

	public void setExpectedResponseCode(int expectedResponseCode) {
		this.expectedResponseCode = expectedResponseCode;
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
		
		System.out.println("endpoint 2.7 : route: " + getEffectiveRoute());
		System.out.println("endpoint 2.7 : calling user id: " + getActingUserObject().getId());
		System.out.println("endpoint 2.7 : calling user username: " + getActingUserObject().getUsername());
		System.out.println("endpoint 2.7 : calling user password: " + getActingUserObject().getPassword());
		System.out.println("endpoint 2.7 : calling user correlation-id: " + correlationId);
		//validate value of responseCode
		boolean responseCodeCorrect = context.getActualReturnedStatus() == expectedResponseCode;
		validations.put("endpoint 2.7 : executed by user: " + getActingUserObject().getId() 
				+ " Validate actual response code " + context.getActualReturnedStatus() 
				+ " matches expected response code " + expectedResponseCode, responseCodeCorrect);
		
		if (expectedResponseCode == ResponseCode.OK.value && context.getActualReturnedStatus() == ResponseCode.OK.value) {
			System.out.println("endpoint 2.7 : response: " + context.getActualReturnedResponse());
			
			DaaltStudentLearningResourceCollectionObject actualDataServiceResponse = context
					.getResponseAsDeserializedObject(DaaltStudentLearningResourceCollectionObject.class);
			
			validations.put("endpoint 2.7 : Validate response is not null ", actualDataServiceResponse != null);
			
			//validate students collection
			List<StudentLearningResourceObject> expectedResourceList = Arrays.asList(expectedResources);
			List<StudentLearningResourceObject> actualResourceList = Arrays.asList(actualDataServiceResponse.getItems());
			validations.put("endpoint 2.7 : Validate actual size of resource collection " + actualResourceList.size()
					+ " matches expected size of resource collection " + expectedResourceList.size(), 
					actualResourceList.size() == expectedResourceList.size());
			for (StudentLearningResourceObject expectedResource : expectedResourceList) {
				StudentLearningResourceObject actualResource = actualDataServiceResponse.getResourceById(expectedResource.getId());
				validations.put("endpoint 2.7 : Validate resource " + expectedResource.getId() + " exists in collection ", actualResource != null);
				if (actualResource != null) {
					validateResource(expectedResource, actualResource);
				}
			}
			

			//verify values of offset, limit, and itemCount
			if (offset == null) {
				validations.put("endpoint 2.7 : Validate actual offset " + actualDataServiceResponse.offset
						+ " matches expected offset 0 (default value)", 
						actualDataServiceResponse.offset == 0);
			} else {
				validations.put("endpoint 2.7 : Validate actual offset " + actualDataServiceResponse.offset
						+ " matches expected offset " + offset, 
						actualDataServiceResponse.offset == offset);
			}
			
			if (limit == null) {
				validations.put("endpoint 2.7 : Validate actual limit " + actualDataServiceResponse.limit
						+ " matches expected limit 100 (default value)", 
						actualDataServiceResponse.limit == 100);
			} else {
				validations.put("endpoint 2.7 : Validate actual limit " + actualDataServiceResponse.limit
						+ " matches expected limit " + limit, 
						actualDataServiceResponse.limit == limit);
			}
			
			validations.put("endpoint 2.7 : Validate actual itemCount " + actualDataServiceResponse.itemCount
					+ " matches expected itemCount " + itemCount, 
					actualDataServiceResponse.itemCount == itemCount);
			
			//TODO: verify contents of expectedEnvelopeLevelLinks == contents of actualDataServiceResponse.getLinks()
			// Please don't verify _links at this time. It is not yet implemented
		}			
		return validations;
	}
	
	private void validateResource(StudentLearningResourceObject expectedResource, StudentLearningResourceObject actualResource) {
		//don't need to validate : platformId, courseSectionId, studentId, learningModuleId, learningResourceId
		
		//validate learningResourceTitle
		if (expectedResource.learningResourceTitle != null) {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual learningResourceTitle is not null ", 
					actualResource.learningResourceTitle != null);
			if (actualResource.learningResourceTitle != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual learningResourceTitle: " + actualResource.learningResourceTitle
						+ " matches expected learningResourceTitle: " + expectedResource.learningResourceTitle, 
						actualResource.learningResourceTitle.compareTo(expectedResource.learningResourceTitle) == 0);
			}
		}
		
		// validate learningResourceSequence;
		validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
				+ ": Validate actual learningResourceSequence: " + actualResource.learningResourceSequence
				+ " matches expected learningResourceSequence: " + expectedResource.learningResourceSequence, 
				actualResource.learningResourceSequence == expectedResource.learningResourceSequence);
		
		//validate learningResourceType;
		if (expectedResource.learningResourceType != null) {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual learningResourceType is not null ", 
					actualResource.learningResourceType != null);
			if (actualResource.learningResourceType != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual learningResourceType: " + actualResource.learningResourceType
						+ " matches expected learningResourceType: " + expectedResource.learningResourceType, 
						actualResource.learningResourceType.compareTo(expectedResource.learningResourceType) == 0);
			}
		}
		
		//validate learningResourceSubType;
		if (expectedResource.learningResourceSubType != null) {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual learningResourceSubType is not null ", 
					actualResource.learningResourceSubType != null);
			if (actualResource.learningResourceSubType != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual learningResourceSubType: " + actualResource.learningResourceSubType
						+ " matches expected learningResourceSubType: " + expectedResource.learningResourceSubType, 
						actualResource.learningResourceSubType.compareTo(expectedResource.learningResourceSubType) == 0);
			}
		}
		
		//validate hasChildrenFlag;
		if (expectedResource.hasChildrenFlag != null) {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual hasChildrenFlag is not null ", 
					actualResource.hasChildrenFlag != null);
			if (actualResource.hasChildrenFlag != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual hasChildrenFlag: " + actualResource.hasChildrenFlag
						+ " matches expected hasChildrenFlag: " + expectedResource.hasChildrenFlag, 
						actualResource.hasChildrenFlag.compareTo(expectedResource.hasChildrenFlag) == 0);
			}
		}
		
		//validate studentFirstName;
		if (expectedResource.studentFirstName != null) {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual studentFirstName is not null ", 
					actualResource.studentFirstName != null);
			if (actualResource.studentFirstName != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual studentFirstName: " + actualResource.studentFirstName
						+ " matches expected studentFirstName: " + expectedResource.studentFirstName, 
						actualResource.studentFirstName.compareTo(expectedResource.studentFirstName) == 0);
			}
		}
		
		//validate studentLastName;
		if (expectedResource.studentLastName != null) {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual studentLastName is not null ", 
					actualResource.studentLastName != null);
			if (actualResource.studentLastName != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual studentLastName: " + actualResource.studentLastName
						+ " matches expected studentLastName: " + expectedResource.studentLastName, 
						actualResource.studentLastName.compareTo(expectedResource.studentLastName) == 0);
			}
		}
		
		// validate pointsPossible;Float 
		if (expectedResource.pointsPossible == null) {
//			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual pointsPossible is null ", 
//					actualResource.pointsPossible == null);
		} else {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual pointsPossible is not null ", 
					actualResource.pointsPossible != null);
			if (actualResource.pointsPossible != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual pointsPossible: " + actualResource.pointsPossible
						+ " matches expected pointsPossible: " + expectedResource.pointsPossible, 
						actualResource.pointsPossible .equals( expectedResource.pointsPossible) );
			}
		}

		// validate practicePointsPossible; Float 
		if (expectedResource.practicePointsPossible == null) {
//			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual practicePointsPossible is null ", 
//					actualResource.practicePointsPossible == null);
		} else {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual practicePointsPossible is not null ", 
					actualResource.practicePointsPossible != null);
			if (actualResource.practicePointsPossible != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual practicePointsPossible: " + actualResource.practicePointsPossible
						+ " matches expected practicePointsPossible: " + expectedResource.practicePointsPossible, 
						actualResource.practicePointsPossible .equals( expectedResource.practicePointsPossible) );
			}
		}
		
		// validate studentPracticePoints; Float 
		if (expectedResource.studentPracticePoints == null) {
//			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual studentPracticePoints is null ", 
//					actualResource.studentPracticePoints == null);
		} else {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual studentPracticePoints is not null ", 
					actualResource.studentPracticePoints != null);
			if (actualResource.studentPracticePoints != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual studentPracticePoints: " + actualResource.studentPracticePoints
						+ " matches expected studentPracticePoints: " + expectedResource.studentPracticePoints, 
						actualResource.studentPracticePoints .equals( expectedResource.studentPracticePoints));
			}
		}
		
		//validate includesAdjustedPoints;
		if (expectedResource.includesAdjustedPoints != null) {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual includesAdjustedPoints is not null ", 
					actualResource.includesAdjustedPoints != null);
			if (actualResource.includesAdjustedPoints != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual includesAdjustedPoints: " + actualResource.includesAdjustedPoints
						+ " matches expected includesAdjustedPoints: " + expectedResource.includesAdjustedPoints, 
						actualResource.includesAdjustedPoints.compareTo(expectedResource.includesAdjustedPoints) == 0);
			}
		}
		
		// validate studentPoints; Float 
		if (expectedResource.studentPoints == null) {
//			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual studentPoints is null ", 
//					actualResource.studentPoints == null);
		} else {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual studentPoints is not null ", 
					actualResource.studentPoints != null);
			if (actualResource.studentPoints != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual studentPoints: " + actualResource.studentPoints
						+ " matches expected studentPoints: " + expectedResource.studentPoints, 
						actualResource.studentPoints .equals( expectedResource.studentPoints));
			}
		}
		
		// validate studentPercent; Float 
		if (expectedResource.studentPercent == null) {
//			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual studentPercent is null ", 
//					actualResource.studentPercent == null);
		} else {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual studentPercent is not null ", 
					actualResource.studentPercent != null);
			if (actualResource.studentPercent != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual studentPercent: " + actualResource.studentPercent
						+ " matches expected studentPercent: " + expectedResource.studentPercent, 
						actualResource.studentPercent .equals( expectedResource.studentPercent));
			}
		}
		
		//validate totalTimeSpentAssessment;
		if (expectedResource.totalTimeSpentAssessment != null) {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual totalTimeSpentAssessment is not null ", 
					actualResource.totalTimeSpentAssessment != null);
			if (actualResource.totalTimeSpentAssessment != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual totalTimeSpentAssessment: " + actualResource.totalTimeSpentAssessment
						+ " matches expected totalTimeSpentAssessment: " + expectedResource.totalTimeSpentAssessment, 
						actualResource.totalTimeSpentAssessment.compareTo(expectedResource.totalTimeSpentAssessment) == 0);
			}
		}
				
		//validate totalTimeSpentLearning;
		if (expectedResource.totalTimeSpentLearning != null) {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual totalTimeSpentLearning is not null ", 
					actualResource.totalTimeSpentLearning != null);
			if (actualResource.totalTimeSpentLearning != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual totalTimeSpentLearning: " + actualResource.totalTimeSpentLearning
						+ " matches expected totalTimeSpentLearning: " + expectedResource.totalTimeSpentLearning, 
						actualResource.totalTimeSpentLearning.compareTo(expectedResource.totalTimeSpentLearning) == 0);
			}
		}
		
		//validate timeSpentTotal;
		if (expectedResource.timeSpentTotal != null) {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual timeSpentTotal is not null ", 
					actualResource.timeSpentTotal != null);
			if (actualResource.timeSpentTotal != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual timeSpentTotal: " + actualResource.timeSpentTotal
						+ " matches expected timeSpentTotal: " + expectedResource.timeSpentTotal, 
						actualResource.timeSpentTotal.compareTo(expectedResource.timeSpentTotal) == 0);
			}
		}
		
		//validate totalChildTimeSpentAssessment;
		if (expectedResource.totalChildTimeSpentAssessment != null) {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual totalChildTimeSpentAssessment is not null ", 
					actualResource.totalChildTimeSpentAssessment != null);
			if (actualResource.totalChildTimeSpentAssessment != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual totalChildTimeSpentAssessment: " + actualResource.totalChildTimeSpentAssessment
						+ " matches expected totalChildTimeSpentAssessment: " + expectedResource.totalChildTimeSpentAssessment, 
						actualResource.totalChildTimeSpentAssessment.compareTo(expectedResource.totalChildTimeSpentAssessment) == 0);
			}
		}
		
		//validate totalChildTimeSpentLearning;
		if (expectedResource.totalChildTimeSpentLearning != null) {
			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
					+ ": Validate actual totalChildTimeSpentLearning is not null ", 
					actualResource.totalChildTimeSpentLearning != null);
			if (actualResource.totalChildTimeSpentLearning != null) {
				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
						+ ": Validate actual totalChildTimeSpentLearning: " + actualResource.totalChildTimeSpentLearning
						+ " matches expected totalChildTimeSpentLearning: " + expectedResource.totalChildTimeSpentLearning, 
						actualResource.totalChildTimeSpentLearning.compareTo(expectedResource.totalChildTimeSpentLearning) == 0);
			}
		}
		
//		//validate studentLateSubmissionPoints;
//		if (expectedResource.studentLateSubmissionPoints == null) {
//			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual studentLateSubmissionPoints is null ", 
//					actualResource.studentLateSubmissionPoints == null);
//		} else if (expectedResource.studentLateSubmissionPoints != null) {
//			validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual studentLateSubmissionPoints is not null ", 
//					actualResource.studentLateSubmissionPoints != null);
//			if (actualResource.studentLateSubmissionPoints != null) {
//				validations.put("endpoint 2.7 : For resource " + actualResource.learningResourceId
//						+ ": Validate actual studentLateSubmissionPoints: " + actualResource.studentLateSubmissionPoints
//						+ " matches expected studentLateSubmissionPoints: " + expectedResource.studentLateSubmissionPoints, 
//						actualResource.studentLateSubmissionPoints.compareTo(expectedResource.studentLateSubmissionPoints) == 0);
//			}
//		}
	}

	@Override
	public String[] getRequiredParameters() {
		return new String[] {ROUTE, EXPECTED_RESPONSE_CODE, EXPECTED_RESOURCES};
	}
	
	@Override
	public String getExpectedResultsPrintString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("2.7 : SectionToStudentToModuleToResources");
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
		if (getExpectedResources() != null && getExpectedResponseCode()==200) {
			for (StudentLearningResourceObject expectedResource : getExpectedResources()) {
				toReturn.append("\n......learningResourceId: " + expectedResource.learningResourceId);
				toReturn.append("\n......studentFirstName: " + expectedResource.studentFirstName);
				toReturn.append("\n......studentLastName: " + expectedResource.studentLastName);
				toReturn.append("\n......studentId: " + expectedResource.studentId);
				toReturn.append("\n......studentPercent: " + expectedResource.studentPercent);
				toReturn.append("\n......studentPoints: " + expectedResource.studentPoints);
				toReturn.append("\n......courseSectionId: " + expectedResource.courseSectionId);
				toReturn.append("\n......learningModuleId: " + expectedResource.learningModuleId);
				toReturn.append("\n......learningResourceSequence: " + expectedResource.learningResourceSequence);
				toReturn.append("\n......learningResourceTitle: " + expectedResource.learningResourceTitle);
				toReturn.append("\n......learningResourceType: " + expectedResource.learningResourceType);
				toReturn.append("\n......platformId: " + expectedResource.platformId);
				toReturn.append("\n......pointsPossible: " + expectedResource.pointsPossible);
				toReturn.append("\n......timeSpentTotal: " + expectedResource.timeSpentTotal);
				toReturn.append("\n......totalChildTimeSpentAssessment: " + expectedResource.totalChildTimeSpentAssessment);
				toReturn.append("\n......totalChildTimeSpentLearning: " + expectedResource.totalChildTimeSpentLearning);
				toReturn.append("\n......totalTimeSpentAssessment: " + expectedResource.totalTimeSpentAssessment);
				toReturn.append("\n......totalTimeSpentLearning: " + expectedResource.totalTimeSpentLearning);
				toReturn.append("\n......hasChildrenFlag: " + expectedResource.hasChildrenFlag);
				toReturn.append("\n......learningResourceSubType: " + expectedResource.learningResourceSubType);
				toReturn.append("\n......practicePointsPossible: " + expectedResource.practicePointsPossible);
				toReturn.append("\n......includesAdjustedPoints: " + expectedResource.includesAdjustedPoints);
				toReturn.append("\n......studentPracticePoints: " + expectedResource.studentPracticePoints);
//				toReturn.append("\n......studentLateSubmissionPoints: " + expectedResource.studentLateSubmissionPoints);
				toReturn.append("\n.....................................................");
			}
		}
		return toReturn.toString();
	}

}
