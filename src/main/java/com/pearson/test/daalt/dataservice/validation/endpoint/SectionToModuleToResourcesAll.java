package com.pearson.test.daalt.dataservice.validation.endpoint;

/**
 * This is endpoint 1.9
 * https://{hostname}/v2/platforms/{platformId}/sections/{courseSectionId}/modules/{learningModuleId}/(resources/{learningResourceId})n/resources
 */

import java.io.IOException;
import java.util.Arrays;
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
import com.pearson.test.daalt.dataservice.response.model.DaaltLearningResourceCollectionObject;
import com.pearson.test.daalt.dataservice.response.model.LearningResourceObject;
import com.pearson.test.daalt.dataservice.response.model.LinkObject;
import com.pearson.test.daalt.dataservice.validation.DaaltValidation;
import com.pearson.test.daalt.dataservice.validation.ResponseCode;
import com.pearson.test.daalt.dataservice.validation.Validation;

public class SectionToModuleToResourcesAll extends DaaltValidation {

	private String route;
	private int expectedResponseCode;
	private LearningResourceObject[] expectedResources;
	private Map<String, LinkObject> expectedEnvelopeLevelLinks;
	private Integer offset;
	private Integer limit;
	private int itemCount;
	private String correlationId;
	
	public SectionToModuleToResourcesAll() {}
	
	@SuppressWarnings("unchecked")
	public SectionToModuleToResourcesAll(String testCaseNumber,
			Validation validation) {
		super(testCaseNumber, validation);
		
		route = parameters.get(ROUTE);
		expectedResponseCode = Integer.valueOf(parameters.get(EXPECTED_RESPONSE_CODE));
		Object obj = parameters.get(EXPECTED_RESOURCES);
		expectedResources =  (LearningResourceObject[]) obj;
		
	}
	
	public SectionToModuleToResourcesAll(String creationTestName, UserObject user, 
			String route, int expectedResponseCode, 
			LearningResourceObject[] expectedResources,
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

	public LearningResourceObject[] getExpectedResources() {
		return expectedResources;
	}

	public void setExpectedResources(LearningResourceObject[] expectedResources) {
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
//				.post().andFullUrl(TestEngine.getInstance().getApigeeTokenRoute())
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

		System.out.println("endpoint 1.9 : route: " + getEffectiveRoute());
		System.out.println("endpoint 1.9 : calling user id: " + getActingUserObject().getId());
		System.out.println("endpoint 1.9 : calling user username: " + getActingUserObject().getUsername());
		System.out.println("endpoint 1.9 : calling user password: " + getActingUserObject().getPassword());
		System.out.println("endpoint 1.9 : calling user correlation-id: " + correlationId);
		//validate value of responseCode
		boolean responseCodeCorrect = context.getActualReturnedStatus() == expectedResponseCode;
		validations.put("endpoint 1.9 : executed by user: " + getActingUserObject().getId() 
				+ " Validate actual response code " + context.getActualReturnedStatus() 
				+ " matches expected response code " + expectedResponseCode, responseCodeCorrect);
		
		if (expectedResponseCode == ResponseCode.OK.value && context.getActualReturnedStatus() == ResponseCode.OK.value) {
			System.out.println("endpoint 1.9 : response: " + context.getActualReturnedResponse());
			
			DaaltLearningResourceCollectionObject actualDataServiceResponse = context
					.getResponseAsDeserializedObject(DaaltLearningResourceCollectionObject.class);
			
			validations.put("endpoint 1.9 : Validate response is not null ", actualDataServiceResponse != null);
			
			//validate students collection
			List<LearningResourceObject> expectedResourceList = Arrays.asList(expectedResources);
			List<LearningResourceObject> actualResourceList = Arrays.asList(actualDataServiceResponse.getItems());
			validations.put("endpoint 1.9 : Validate actual size of resource collection " + actualResourceList.size()
					+ " matches expected size of resource collection " + expectedResourceList.size(), 
					actualResourceList.size() == expectedResourceList.size());			
			for (LearningResourceObject expectedResource : expectedResourceList) {
				LearningResourceObject actualResource = actualDataServiceResponse.getResourceById(expectedResource.getId());
				validations.put("endpoint 1.9 : Validate resource " + expectedResource.getId() + " exists in collection ", actualResource != null);
				if (actualResource != null) {
					validateResource(expectedResource, actualResource);
				}
			}
			

			//verify values of offset, limit, and itemCount
			if (offset == null) {
				validations.put("endpoint 1.9 : Validate actual offset " + actualDataServiceResponse.offset
						+ " matches expected offset 0 (default value)", 
						actualDataServiceResponse.offset == 0);
			} else {
				validations.put("endpoint 1.9 : Validate actual offset " + actualDataServiceResponse.offset
						+ " matches expected offset " + offset, 
						actualDataServiceResponse.offset == offset);
			}
			
			if (limit == null) {
				validations.put("endpoint 1.9 : Validate actual limit " + actualDataServiceResponse.limit
						+ " matches expected limit 100 (default value)", 
						actualDataServiceResponse.limit == 100);
			} else {
				validations.put("endpoint 1.9 : Validate actual limit " + actualDataServiceResponse.limit
						+ " matches expected limit " + limit, 
						actualDataServiceResponse.limit == limit);
			}
			
			validations.put("endpoint 1.9 : Validate actual itemCount " + actualDataServiceResponse.itemCount
					+ " matches expected itemCount " + itemCount, 
					actualDataServiceResponse.itemCount == itemCount);
			
			//TODO: verify contents of expectedEnvelopeLevelLinks == contents of actualDataServiceResponse.getLinks()
			//FROM KAT: Please don't verify _links at this time. It is not yet implemented
			
		}
		return validations;
	}
	
	private void validateResource(LearningResourceObject expectedResource, LearningResourceObject actualResource) {
		//don't need to validate : platformId, courseSectionId, learningModuleId, learningResourceId
		
		//validate learningResourceTitle - string
		if (expectedResource.learningResourceTitle != null) {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual learningResourceTitle is not null ", 
					actualResource.learningResourceTitle != null);
			if (actualResource.learningResourceTitle != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual learningResourceTitle: " + actualResource.learningResourceTitle
						+ " matches expected learningResourceTitle: " + expectedResource.learningResourceTitle, 
						actualResource.learningResourceTitle.compareTo(expectedResource.learningResourceTitle) == 0);
			}
		}
		
		//validate learningResourceType - string
		if (expectedResource.learningResourceType != null) {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual learningResourceType is not null ", 
					actualResource.learningResourceType != null);
			if (actualResource.learningResourceType != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual learningResourceType: " + actualResource.learningResourceType
						+ " matches expected learningResourceType: " + expectedResource.learningResourceType, 
						actualResource.learningResourceType.compareTo(expectedResource.learningResourceType) == 0);
			}
		}
		
		//validate learningResourceSubType - string
		if (expectedResource.learningResourceSubType != null) {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual learningResourceSubType is not null ", 
					actualResource.learningResourceSubType != null);
			if (actualResource.learningResourceSubType != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual learningResourceSubType: " + actualResource.learningResourceSubType
						+ " matches expected learningResourceSubType: " + expectedResource.learningResourceSubType, 
						actualResource.learningResourceSubType.compareTo(expectedResource.learningResourceSubType) == 0);
			}
		}
		
		
		//validate learningResourceSequence 
		validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId 
				+ ": Validate actual learningResourceSequence: " + actualResource.learningResourceSequence 
				+ " matches expected learningResourceSequence: " + expectedResource.learningResourceSequence, 
				actualResource.learningResourceSequence == expectedResource.learningResourceSequence);
		
		
		
		//validate pointsPossible - Float
		if (expectedResource.pointsPossible == null) {
//			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual pointsPossible is null ", 
//					actualResource.pointsPossible == null);
		} else {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual pointsPossible is not null ", 
					actualResource.pointsPossible != null);
			if (actualResource.pointsPossible != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual pointsPossible: " + actualResource.pointsPossible
						+ " matches expected pointsPossible: " + expectedResource.pointsPossible, 
						actualResource.pointsPossible .equals( expectedResource.pointsPossible));
			}
		}
		
		
		//validate practicePointsPossible - Float
		if (expectedResource.practicePointsPossible == null) {
//			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual practicePointsPossible is null ", 
//					actualResource.practicePointsPossible == null);
		} else {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual practicePointsPossible is not null ", 
					actualResource.practicePointsPossible != null);
			if (actualResource.practicePointsPossible != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual practicePointsPossible: " + actualResource.practicePointsPossible
						+ " matches expected practicePointsPossible: " + expectedResource.practicePointsPossible, 
						actualResource.practicePointsPossible .equals( expectedResource.practicePointsPossible));
			}
		}
		
		
		//validate hasChildrenFlag - boolean
		validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
				+ ": Validate actual hasChildrenFlag: " + actualResource.hasChildrenFlag
				+ " matches expected hasChildrenFlag: " + expectedResource.hasChildrenFlag, 
				actualResource.hasChildrenFlag == expectedResource.hasChildrenFlag);
		
		//validate classTotalPoints - Float
		if (expectedResource.classTotalPoints == null) {
//			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual classTotalPoints is null ", 
//					actualResource.classTotalPoints == null);
		} else {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual classTotalPoints is not null ", 
					actualResource.classTotalPoints != null);
			if (actualResource.classTotalPoints != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual classTotalPoints: " + actualResource.classTotalPoints
						+ " matches expected classTotalPoints: " + expectedResource.classTotalPoints, 
						actualResource.classTotalPoints .equals( expectedResource.classTotalPoints));
			}
		}
		
		//validate classAvgPoints - Float
		if (expectedResource.classAvgPoints == null) {
//			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual classAvgPoints is null ", 
//					actualResource.classAvgPoints == null);
		} else {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual classAvgPoints is not null ", 
					actualResource.classAvgPoints != null);
			if (actualResource.classAvgPoints != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual classAvgPoints: " + actualResource.classAvgPoints
						+ " matches expected classAvgPoints: " + expectedResource.classAvgPoints, 
						actualResource.classAvgPoints .equals( expectedResource.classAvgPoints));
			}
		}
		
		//validate classAvgPercent - Float
		if (expectedResource.classAvgPercent == null) {
//			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual classAvgPercent is null ", 
//					actualResource.classAvgPercent == null);
		} else {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual classAvgPercent is not null ", 
					actualResource.classAvgPercent != null);
			if (actualResource.classAvgPercent != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual classAvgPercent: " + actualResource.classAvgPercent
						+ " matches expected classAvgPercent: " + expectedResource.classAvgPercent, 
						actualResource.classAvgPercent .equals( expectedResource.classAvgPercent));
			}
		}
		
		//validate classTotalPracticePoints - Float
		if (expectedResource.classTotalPracticePoints == null) {
//			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual classTotalPracticePoints is null ", 
//					actualResource.classTotalPracticePoints == null);
		} else {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual classTotalPracticePoints is not null ", 
					actualResource.classTotalPracticePoints != null);
			if (actualResource.classTotalPracticePoints != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual classTotalPracticePoints: " + actualResource.classTotalPracticePoints
						+ " matches expected classTotalPracticePoints: " + expectedResource.classTotalPracticePoints, 
						actualResource.classTotalPracticePoints.equals(expectedResource.classTotalPracticePoints));
			}
		}
		
		//validate classAvgPracticePoints - Float
		if (expectedResource.classAvgPracticePoints == null) {
//			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
//					+ ": Validate actual classAvgPracticePoints is null ", 
//					actualResource.classAvgPracticePoints == null);
		} else {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual classAvgPracticePoints is not null ", 
					actualResource.classAvgPracticePoints != null);
			if (actualResource.classAvgPracticePoints != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual classAvgPracticePoints: " + actualResource.classAvgPracticePoints
						+ " matches expected classAvgPracticePoints: " + expectedResource.classAvgPracticePoints, 
						actualResource.classAvgPracticePoints .equals(expectedResource.classAvgPracticePoints));
			}
		}
		
		//validate avgTimeSpentAssessment - string
		if (expectedResource.avgTimeSpentAssessment != null) {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual avgTimeSpentAssessment is not null ", 
					actualResource.avgTimeSpentAssessment != null);
			if (actualResource.avgTimeSpentAssessment != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual avgTimeSpentAssessment: " + actualResource.avgTimeSpentAssessment
						+ " matches expected avgTimeSpentAssessment: " + expectedResource.avgTimeSpentAssessment, 
						actualResource.avgTimeSpentAssessment.compareTo(expectedResource.avgTimeSpentAssessment) == 0);
			}
		}
		
		//validate totalTimeSpentAssessment - string
		if (expectedResource.totalTimeSpentAssessment != null) {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual totalTimeSpentAssessment is not null ", 
					actualResource.totalTimeSpentAssessment != null);
			if (actualResource.avgTimeSpentTotal != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual totalTimeSpentAssessment: " + actualResource.totalTimeSpentAssessment
						+ " matches expected totalTimeSpentAssessment: " + expectedResource.totalTimeSpentAssessment, 
						actualResource.totalTimeSpentAssessment.compareTo(expectedResource.totalTimeSpentAssessment) == 0);
			}
		}
		
		//validate totalChildTimeSpentAssessment - string
		if (expectedResource.totalChildTimeSpentAssessment != null) {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual totalChildTimeSpentAssessment is not null ", 
					actualResource.totalChildTimeSpentAssessment != null);
			if (actualResource.totalChildTimeSpentAssessment != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual totalChildTimeSpentAssessment: " + actualResource.totalChildTimeSpentAssessment
						+ " matches expected totalChildTimeSpentAssessment: " + expectedResource.totalChildTimeSpentAssessment, 
						actualResource.totalChildTimeSpentAssessment.compareTo(expectedResource.totalChildTimeSpentAssessment) == 0);
			}
		}
		
		//validate avgTimeSpentLearning - string
		if (expectedResource.avgTimeSpentLearning != null) {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual avgTimeSpentLearning is not null ", 
					actualResource.avgTimeSpentLearning != null);
			if (actualResource.avgTimeSpentLearning != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual avgTimeSpentLearning: " + actualResource.avgTimeSpentLearning
						+ " matches expected avgTimeSpentLearning: " + expectedResource.avgTimeSpentLearning, 
						actualResource.avgTimeSpentLearning.compareTo(expectedResource.avgTimeSpentLearning) == 0);
			}
		}
		
		//validate totalTimeSpentLearning - string
		if (expectedResource.totalTimeSpentLearning != null) {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual totalTimeSpentLearning is not null ", 
					actualResource.totalTimeSpentLearning != null);
			if (actualResource.totalTimeSpentLearning != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual totalTimeSpentLearning: " + actualResource.totalTimeSpentLearning
						+ " matches expected totalTimeSpentLearning: " + expectedResource.totalTimeSpentLearning, 
						actualResource.totalTimeSpentLearning.compareTo(expectedResource.totalTimeSpentLearning) == 0);
			}
		}
		
		//validate totalChildTimeSpentLearning - string
		if (expectedResource.totalChildTimeSpentLearning != null) {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual totalChildTimeSpentLearning is not null ", 
					actualResource.totalChildTimeSpentLearning != null);
			if (actualResource.totalChildTimeSpentLearning != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual totalChildTimeSpentLearning: " + actualResource.totalChildTimeSpentLearning
						+ " matches expected totalChildTimeSpentLearning: " + expectedResource.totalChildTimeSpentLearning, 
						actualResource.totalChildTimeSpentLearning.compareTo(expectedResource.totalChildTimeSpentLearning) == 0);
			}
		}
		
		//validate timeSpentTotal - string
		if (expectedResource.timeSpentTotal != null) {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual timeSpentTotal is not null ", 
					actualResource.timeSpentTotal != null);
			if (actualResource.timeSpentTotal != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual timeSpentTotal: " + actualResource.timeSpentTotal
						+ " matches expected timeSpentTotal: " + expectedResource.timeSpentTotal, 
						actualResource.timeSpentTotal.compareTo(expectedResource.timeSpentTotal) == 0);
			}
		}
		
		//validate avgTimeSpentTotal - string
		if (expectedResource.avgTimeSpentTotal != null) {
			validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
					+ ": Validate actual avgTimeSpentTotal is not null ", 
					actualResource.avgTimeSpentTotal != null);
			if (actualResource.avgTimeSpentTotal != null) {
				validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
						+ ": Validate actual avgTimeSpentTotal: " + actualResource.avgTimeSpentTotal
						+ " matches expected avgTimeSpentTotal: " + expectedResource.avgTimeSpentTotal, 
						actualResource.avgTimeSpentTotal.compareTo(expectedResource.avgTimeSpentTotal) == 0);
			}
		}
		
		//validate courseSectionStudentCount - int
		validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
				+ ": Validate actual courseSectionStudentCount: " + actualResource.courseSectionStudentCount
				+ " matches expected courseSectionStudentCount: " + expectedResource.courseSectionStudentCount, 
				actualResource.courseSectionStudentCount == expectedResource.courseSectionStudentCount);
		
		//validate completedStudentCount - int
		validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
				+ ": Validate actual completedStudentCount: " + actualResource.completedStudentCount
				+ " matches expected completedStudentCount: " + expectedResource.completedStudentCount, 
				actualResource.completedStudentCount == expectedResource.completedStudentCount);
		
		//validate incompleteStudentCount - int
		validations.put("endpoint 1.9 : For resource " + actualResource.learningResourceId
				+ ": Validate actual incompleteStudentCount: " + actualResource.incompleteStudentCount
				+ " matches expected incompleteStudentCount: " + expectedResource.incompleteStudentCount, 
				actualResource.incompleteStudentCount == expectedResource.incompleteStudentCount);
		
	}

	@Override
	public String[] getRequiredParameters() {
		return new String[] {ROUTE, EXPECTED_RESPONSE_CODE, EXPECTED_RESOURCES};
	}
	
	@Override
	public String getExpectedResultsPrintString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("1.9 : SectionToModuleToResources");
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
		if (getExpectedResources() != null && getExpectedResponseCode()==200) {
			for (LearningResourceObject expectedResource : getExpectedResources()) {
				toReturn.append("\n......learningResourceId: " + expectedResource.learningResourceId);
				toReturn.append("\n......avgTimeSpentAssessment: " + expectedResource.avgTimeSpentAssessment);
				toReturn.append("\n......avgTimeSpentLearning: " + expectedResource.avgTimeSpentLearning);
				toReturn.append("\n......avgTimeSpentTotal: " + expectedResource.avgTimeSpentTotal);
				toReturn.append("\n......classAvgPercent: " + expectedResource.classAvgPercent);
				toReturn.append("\n......classAvgPoints: " + expectedResource.classAvgPoints);
				toReturn.append("\n......classTotalPoints: " + expectedResource.classTotalPoints);
				toReturn.append("\n......practicePointsPossible: " + expectedResource.practicePointsPossible);
				toReturn.append("\n......classTotalPracticePoints: " + expectedResource.classTotalPracticePoints);
				toReturn.append("\n......classAvgPracticePoints: " + expectedResource.classAvgPracticePoints);
				toReturn.append("\n......completedStudentCount: " + expectedResource.completedStudentCount);
				toReturn.append("\n......courseSectionId: " + expectedResource.courseSectionId);
				toReturn.append("\n......courseSectionStudentCount: " + expectedResource.courseSectionStudentCount);
				toReturn.append("\n......incompleteStudentCount: " + expectedResource.incompleteStudentCount);
				toReturn.append("\n......learningModuleId: " + expectedResource.learningModuleId);
				toReturn.append("\n......learningResourceSequence: " + expectedResource.learningResourceSequence);
				toReturn.append("\n......learningResourceSubType: " + expectedResource.learningResourceSubType);
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
				toReturn.append("\n.....................................................");
			}
		}
		return toReturn.toString();
	}
}
