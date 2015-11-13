package com.pearson.test.daalt.dataservice.validation.endpoint;

/**
 * This is endpoint 1.11
 * https://{hostname}/v2/platforms/{platformId}/sections/{courseSectionId}/modules/{learningModuleId}/(resources/{learningResourceId})n/items
 */

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.pearson.ed.pi.authentication.systemclient.PiTokenContainer;
import com.pearson.qa.apex.dataobjects.UserObject;
import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.User;
import com.pearson.test.daalt.dataservice.mongodb.JSONUtils;
import com.pearson.test.daalt.dataservice.response.model.AttemptObject;
import com.pearson.test.daalt.dataservice.response.model.DaaltLearningResourceItemCollectionObject;
import com.pearson.test.daalt.dataservice.response.model.LearningResourceItemObject;
import com.pearson.test.daalt.dataservice.response.model.LinkObject;
import com.pearson.test.daalt.dataservice.response.model.ResponseObject;
import com.pearson.test.daalt.dataservice.response.model.TargetSubQuestionAnswerObject;
import com.pearson.test.daalt.dataservice.response.model.TargetSubQuestionAnswerResponseObject;
import com.pearson.test.daalt.dataservice.response.model.TargetSubQuestionObject;
import com.pearson.test.daalt.dataservice.response.model.TargetSubQuestionResponseObject;
import com.pearson.test.daalt.dataservice.validation.DaaltValidation;
import com.pearson.test.daalt.dataservice.validation.ResponseCode;
import com.pearson.test.daalt.dataservice.validation.Validation;

public class SectionToModuleToResourceToItemsAll extends DaaltValidation {

	private String route;
	private int expectedResponseCode;
	private LearningResourceItemObject[] expectedItems;
	private Map<String, LinkObject> expectedEnvelopeLevelLinks;
	private Integer offset;
	private Integer limit;
	private int itemCount;
	private String correlationId;
	public SectionToModuleToResourceToItemsAll() {}
	
	@SuppressWarnings("unchecked")
	public SectionToModuleToResourceToItemsAll(String testCaseNumber,
			Validation validation) {
		super(testCaseNumber, validation);
		
		route = parameters.get(ROUTE);
		expectedResponseCode = Integer.valueOf(parameters.get(EXPECTED_RESPONSE_CODE));
		Object obj = parameters.get(EXPECTED_ITEMS);
		expectedItems =  (LearningResourceItemObject[]) obj;
	}
	
	public SectionToModuleToResourceToItemsAll(String creationTestName, UserObject user, 
			String route, int expectedResponseCode, 
			LearningResourceItemObject[] expectedItems,
			Map<String, LinkObject> expectedEnvelopeLevelLinks, 
			Integer offset, Integer limit, int itemCount) throws JsonGenerationException, JsonMappingException, IOException {
		setCreationTestName(creationTestName);
		setRoute(route);
		setExpectedResponseCode(expectedResponseCode);
		setExpectedItems(expectedItems);
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

	public LearningResourceItemObject[] getExpectedItems() {
		return expectedItems;
	}

	public void setExpectedItems(LearningResourceItemObject[] expectedItems) {
		this.expectedItems= expectedItems;
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
//		String testVar = TestEngine.getInstance().getTestVar();
//		System.out.println("Value found by 1.11 validator class: " + testVar);
		
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

		System.out.println("endpoint 1.11 : route: " + getEffectiveRoute());
		System.out.println("endpoint 1.11 : calling user id: " + getActingUserObject().getId());
		System.out.println("endpoint 1.11 : calling user username: " + getActingUserObject().getUsername());
		System.out.println("endpoint 1.11 : calling user password: " + getActingUserObject().getPassword());
		System.out.println("endpoint 1.11 : calling user correlation-id: " + correlationId);
		//validate value of responseCode
		boolean responseCodeCorrect = context.getActualReturnedStatus() == expectedResponseCode;
		validations.put("endpoint 1.11 : executed by user: " + getActingUserObject().getId() 
				+ " Validate actual response code " + context.getActualReturnedStatus() 
				+ " matches expected response code " + expectedResponseCode, responseCodeCorrect);
		
		if (expectedResponseCode == ResponseCode.OK.value && context.getActualReturnedStatus() == ResponseCode.OK.value) {
			System.out.println("endpoint 1.11 : response: " + context.getActualReturnedResponse());
			
			DaaltLearningResourceItemCollectionObject actualDataServiceResponse = context
					.getResponseAsDeserializedObject(DaaltLearningResourceItemCollectionObject.class);
			
			validations.put("endpoint 1.11 : Validate response is not null ", actualDataServiceResponse != null);
			
			//validate items collection
			List<LearningResourceItemObject> expectedItemList = Arrays.asList(expectedItems);
			List<LearningResourceItemObject> actualItems = Arrays.asList(actualDataServiceResponse.getItems());
			validations.put("endpoint 1.11 : Validate actual size of item collection " + actualItems.size()
					+ " matches expected size of item collection " + expectedItemList.size(), 
					actualItems.size() == expectedItemList.size());
			for (LearningResourceItemObject expectedItem : expectedItemList) {
				LearningResourceItemObject actualItem = actualDataServiceResponse.getItemByItemId(expectedItem.getId());
				validations.put("endpoint 1.11 : Validate item " + expectedItem.getId() + " exists in collection ", actualItem != null);
				if (actualItem != null) {
					validateItem(expectedItem, actualItem);
				}
			}
			
			//verify values of offset, limit, and itemCount
			if (offset == null) {
				validations.put("endpoint 1.11 : Validate actual offset " + actualDataServiceResponse.offset
						+ " matches expected offset 0 (default value)", 
						actualDataServiceResponse.offset == 0);
			} else {
				validations.put("endpoint 1.11 : Validate actual offset " + actualDataServiceResponse.offset
						+ " matches expected offset " + offset, 
						actualDataServiceResponse.offset == offset);
			}
			
			if (limit == null) {
				validations.put("endpoint 1.11 : Validate actual limit " + actualDataServiceResponse.limit
						+ " matches expected limit 100 (default value)", 
						actualDataServiceResponse.limit == 100);
			} else {
				validations.put("endpoint 1.11 : Validate actual limit " + actualDataServiceResponse.limit
						+ " matches expected limit " + limit, 
						actualDataServiceResponse.limit == limit);
			}
			
			validations.put("endpoint 1.11 : Validate actual itemCount " + actualDataServiceResponse.itemCount
					+ " matches expected itemCount " + itemCount, 
					actualDataServiceResponse.itemCount == itemCount);
			
			//TODO: verify contents of expectedEnvelopeLevelLinks == contents of actualDataServiceResponse.getLinks()
			
		}			
		return validations;
	}
	
	private void validateItem(LearningResourceItemObject expectedItem, LearningResourceItemObject actualItem) {
		//don't need to validate : platformId, courseSectionId, learningModuleId, learningResourceId, itemId
		
		//validate assessmentId - String
		if (expectedItem.assessmentId != null) {
			validations.put("endpoint 1.11 : For item " + actualItem.itemId
					+ ": Validate actual assessmentId is not null ", 
					actualItem.assessmentId != null);
			if (actualItem.assessmentId != null) {
				validations.put("endpoint 1.11 : For item " + actualItem.itemId 
						+ ": Validate actual assessmentId: " + actualItem.assessmentId
						+ " matches expected assessmentId: " + expectedItem.assessmentId, 
						actualItem.assessmentId.compareTo(expectedItem.assessmentId) == 0);
			}
		}
		
		//validate itemSequence - int
		validations.put("endpoint 1.11 : For item " + actualItem.itemId 
				+ ": Validate actual itemSequence: " + actualItem.itemSequence
				+ " matches expected itemSequence: " + expectedItem.itemSequence, 
				actualItem.itemSequence == expectedItem.itemSequence);
		
		//validate questionType - String 
		if (expectedItem.questionType != null) {
			validations.put("endpoint 1.11 : For item " + actualItem.itemId
					+ ": Validate actual questionType is not null ", 
					actualItem.questionType != null);
			if (actualItem.questionType != null) {
				validations.put("endpoint 1.11 : For item " + actualItem.itemId
						+ ": Validate actual questionType: " + actualItem.questionType
						+ " matches expected questionType: " + expectedItem.questionType, 
						actualItem.questionType.compareTo(expectedItem.questionType) == 0);
			}
		}
		
		//validate questionPresentationFormat - String
		if (expectedItem.questionPresentationFormat != null) {
			validations.put("endpoint 1.11 : For item " + actualItem.itemId
					+ ": Validate actual questionPresentationFormat is not null ", 
					actualItem.questionPresentationFormat != null);
			if (actualItem.questionPresentationFormat != null) {
				validations.put("endpoint 1.11 : For item " + actualItem.itemId
						+ ": Validate actual questionPresentationFormat: " + actualItem.questionPresentationFormat
						+ " matches expected questionPresentationFormat: " + expectedItem.questionPresentationFormat, 
						actualItem.questionPresentationFormat.compareTo(expectedItem.questionPresentationFormat) == 0);
			}
		}				
		
		//validate questionText - String
		if (expectedItem.questionText != null) {
			validations.put("endpoint 1.11 : For item " + actualItem.itemId
					+ ": Validate actual questionText is not null ", 
					actualItem.questionText != null);
			if (actualItem.questionText != null) {
				validations.put("endpoint 1.11 : For item " + actualItem.itemId
						+ ": Validate actual questionText: " + actualItem.questionText
						+ " matches expected questionText: " + expectedItem.questionText, 
						actualItem.questionText.compareTo(expectedItem.questionText) == 0);
			}
		}
		
		//validate pointsPossible - float
		validations.put("endpoint 1.11 : For item " + actualItem.itemId 
				+ ": Validate actual pointsPossible: " + actualItem.pointsPossible
				+ " matches expected pointsPossible: " + expectedItem.pointsPossible,
				actualItem.pointsPossible == expectedItem.pointsPossible);
		
		//validate totalItemResponseScore - float
		validations.put("endpoint 1.11 : For item " + actualItem.itemId 
				+ ": Validate actual totalItemResponseScore: " + actualItem.totalItemResponseScore
				+ " matches expected totalItemResponseScore: " + expectedItem.totalItemResponseScore,
				actualItem.totalItemResponseScore == expectedItem.totalItemResponseScore);
				
		//validate avgItemResponseScore - float
		validations.put("endpoint 1.11 : For item " + actualItem.itemId 
				+ ": Validate actual avgItemResponseScore: " + actualItem.avgItemResponseScore
				+ " matches expected avgItemResponseScore: " + expectedItem.avgItemResponseScore,
				actualItem.avgItemResponseScore == expectedItem.avgItemResponseScore);
		
		//validate courseSectionStudentCount - int
		validations.put("endpoint 1.11 : For item " + actualItem.itemId 
				+ ": Validate actual courseSectionStudentCount: " + actualItem.courseSectionStudentCount
				+ " matches expected courseSectionStudentCount: " + expectedItem.courseSectionStudentCount,
				actualItem.courseSectionStudentCount == expectedItem.courseSectionStudentCount);	
		
		//validate assessmentItemCompletedStudentCount - int
		validations.put("endpoint 1.11 : For item " + actualItem.itemId 
				+ ": Validate actual assessmentItemCompletedStudentCount: " + actualItem.assessmentItemCompletedStudentCount
				+ " matches expected assessmentItemCompletedStudentCount: " + expectedItem.assessmentItemCompletedStudentCount,
				actualItem.assessmentItemCompletedStudentCount == expectedItem.assessmentItemCompletedStudentCount);
		
		//validate correctStudentCount - int
		validations.put("endpoint 1.11 : For item " + actualItem.itemId 
				+ ": Validate actual correctStudentCount: " + actualItem.correctStudentCount
				+ " matches expected correctStudentCount: " + expectedItem.correctStudentCount,
				actualItem.correctStudentCount == expectedItem.correctStudentCount);
		
		//validate correctStudentPercent - float
		validations.put("endpoint 1.11 : For item " + actualItem.itemId 
				+ ": Validate actual correctStudentPercent: " + actualItem.correctStudentPercent
				+ " matches expected correctStudentPercent: " + expectedItem.correctStudentPercent,
				actualItem.correctStudentPercent == expectedItem.correctStudentPercent);
		
		//validate incorrectStudentCount - int
		validations.put("endpoint 1.11 : For item " + actualItem.itemId 
				+ ": Validate actual incorrectStudentCount: " + actualItem.incorrectStudentCount
				+ " matches expected incorrectStudentCount: " + expectedItem.incorrectStudentCount,
				actualItem.incorrectStudentCount == expectedItem.incorrectStudentCount);
		
		//validate noAttemptStudentCount - int
		validations.put("endpoint 1.11 : For item " + actualItem.itemId 
				+ ": Validate actual noAttemptStudentCount: " + actualItem.noAttemptStudentCount
				+ " matches expected noAttemptStudentCount: " + expectedItem.noAttemptStudentCount,
				actualItem.noAttemptStudentCount == expectedItem.noAttemptStudentCount);		
		
		//validate totalTimeSpentAssessing - String
		if (expectedItem.totalTimeSpentAssessing != null) {
			validations.put("endpoint 1.11 : For item " + actualItem.itemId
					+ ": Validate actual totalTimeSpentAssessing is not null ", 
					actualItem.totalTimeSpentAssessing != null);
			if (actualItem.totalTimeSpentAssessing != null) {
				validations.put("endpoint 1.11 : For item " + actualItem.itemId
						+ ": Validate actual totalTimeSpentAssessing: " + actualItem.totalTimeSpentAssessing
						+ " matches expected totalTimeSpentAssessing: " + expectedItem.totalTimeSpentAssessing, 
						actualItem.totalTimeSpentAssessing.compareTo(expectedItem.totalTimeSpentAssessing) == 0);
			}
		}
		
		//validate medianTimeSpentAssessing - String
		if (expectedItem.medianTimeSpentAssessing != null) {
			validations.put("endpoint 1.11 : For item " + actualItem.itemId
					+ ": Validate actual medianTimeSpentAssessing is not null ", 
					actualItem.medianTimeSpentAssessing != null);
			if (actualItem.medianTimeSpentAssessing != null) {
				validations.put("endpoint 1.11 : For item " + actualItem.itemId
						+ ": Validate actual medianTimeSpentAssessing: " + actualItem.medianTimeSpentAssessing
						+ " matches expected medianTimeSpentAssessing: " + expectedItem.medianTimeSpentAssessing, 
						actualItem.medianTimeSpentAssessing.compareTo(expectedItem.medianTimeSpentAssessing) == 0);
			}
		}
		
		//validate avgTimeSpentAssessing - String
		if (expectedItem.avgTimeSpentAssessing != null) {
			validations.put("endpoint 1.11 : For item " + actualItem.itemId
					+ ": Validate actual avgTimeSpentAssessing is not null ", 
					actualItem.avgTimeSpentAssessing != null);
			if (actualItem.avgTimeSpentAssessing != null) {
				validations.put("endpoint 1.11 : For item " + actualItem.itemId
						+ ": Validate actual avgTimeSpentAssessing: " + actualItem.avgTimeSpentAssessing
						+ " matches expected avgTimeSpentAssessing: " + expectedItem.avgTimeSpentAssessing, 
						actualItem.avgTimeSpentAssessing.compareTo(expectedItem.avgTimeSpentAssessing) == 0);
			}
		}
		
		//validate attempts sub-collection
		List<AttemptObject> expectedAttempts = Arrays.asList(expectedItem.attempts);
		List<AttemptObject> actualAttempts = Arrays.asList(actualItem.attempts);
		validations.put("endpoint 1.11 : For item " + actualItem.itemId 
				+ ": Validate actual size of attempts sub-collection " + actualAttempts.size()
				+ " matches expected size of attempts sub-collection " + expectedAttempts.size(), 
				actualAttempts.size() == expectedAttempts.size());
		for (AttemptObject expectedAttempt : expectedAttempts) {
			AttemptObject actualAttempt = actualItem.getAttemptByIndex(expectedAttempt.attemptNumber);
			validations.put("endpoint 1.11 : For item " + actualItem.itemId 
					+ ": Validate attempt number " + expectedAttempt.attemptNumber 
					+ " exists in collection ", actualAttempt != null);
			if (actualAttempt != null) {
				validateAttempt(expectedAttempt, actualAttempt, actualItem.itemId);
			}
		}
	}
	
	private void validateAttempt(AttemptObject expectedAttempt, AttemptObject actualAttempt, String itemId) {
		//no need to validate attemptNumber
		
		//validate attemptNumberStudentCount - int
		validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + actualAttempt.attemptNumber
				+ ": Validate actual attemptNumberStudentCount: " + actualAttempt.attemptNumberStudentCount
				+ " matches expected attemptNumberStudentCount: " + expectedAttempt.attemptNumberStudentCount,
				actualAttempt.attemptNumberStudentCount == expectedAttempt.attemptNumberStudentCount);
		
		//validate responses sub-collection
		List<ResponseObject> expectedResponses = Arrays.asList(expectedAttempt.responses);
		List<ResponseObject> actualResponses = Arrays.asList(actualAttempt.responses);
		validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + actualAttempt.attemptNumber
				+ ": Validate actual size of responses sub-collection " + actualResponses.size()
				+ " matches expected size of responses sub-collection " + expectedResponses.size(), 
				actualResponses.size() == expectedResponses.size());
		for (ResponseObject expectedResponse : expectedResponses) {
			ResponseObject actualResponse = actualAttempt.getResponseByCode(expectedResponse.getResponseCode());
			validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + actualAttempt.attemptNumber
				+ ": Validate response with code " + expectedResponse.getResponseCode()
				+ " exists in collection ", actualResponse != null);
			if (actualResponse != null) {
				validateResponse(expectedResponse, actualResponse, itemId, actualAttempt.attemptNumber);
			}
		}
		
		//validate targetSubQuestions sub-collection which creates a subQuestion object for each attempt
		List<TargetSubQuestionObject> expectedTargetSubQuestions = Arrays.asList(expectedAttempt.getTargetSubQuestions());
		List<TargetSubQuestionObject> actualTargetSubQuestions = Arrays.asList(actualAttempt.getTargetSubQuestions());
		validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + actualAttempt.attemptNumber
				+ ": Validate actual size of targets sub-collection " + actualTargetSubQuestions.size()
				+ " matches expected size of targets sub-collection " + expectedTargetSubQuestions.size(), 
				actualTargetSubQuestions.size() == expectedTargetSubQuestions.size());
		for(TargetSubQuestionObject expectedTargetSubQuestion : expectedTargetSubQuestions){
			TargetSubQuestionObject actualTargetSubQuestion = actualAttempt.getTargetSubQuestionById(expectedTargetSubQuestion.targetSubQuestionId);
			validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + actualAttempt.attemptNumber
				+ ": Validate target sub question with code " + expectedTargetSubQuestion.targetSubQuestionId
				+ " exists in collection ",  actualTargetSubQuestion != null);
			if ( actualTargetSubQuestion != null) {
				validateTargetSubQuestion(expectedTargetSubQuestion, actualTargetSubQuestion, itemId, actualAttempt.attemptNumber);
			}
			
		}
			
	}

			
	private void validateResponse(ResponseObject expectedResponse, ResponseObject actualResponse, String itemId, int attemptNumber) {
		//no need to validate the response code
		
		//validate responseStudentCount - int
		validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber + ", response with code: " + actualResponse.responseCode
				+ ": Validate actual responseStudentCount: " + actualResponse.responseStudentCount
				+ " matches expected responseStudentCount: " + expectedResponse.responseStudentCount,
				actualResponse.responseStudentCount == expectedResponse.responseStudentCount);
		
		//validate responsePercent - float
		validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber + ", response with code: " + actualResponse.responseCode
				+ ": Validate actual responsePercent: " + actualResponse.responsePercent
				+ " matches expected responsePercent: " + expectedResponse.responsePercent,
				actualResponse.responsePercent == expectedResponse.responsePercent);
	}
	
	
	private void validateTargetSubQuestion(TargetSubQuestionObject expectedTargetSubQuestion, TargetSubQuestionObject actualTargetSubQuestion, String itemId, int attemptNumber){
		//no need to validate the targetSubQuestionId
		
		//validate targetSubQuestionText - String
		if (expectedTargetSubQuestion.targetSubQuestionText != null) {
			validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber + ", response with code: " +actualTargetSubQuestion.targetSubQuestionId
					+ ": Validate actual targetSubQuestionText is not null ", 
					actualTargetSubQuestion.targetSubQuestionText != null);
			if (actualTargetSubQuestion.targetSubQuestionText != null) {
				validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber + ", response with code: " +actualTargetSubQuestion.targetSubQuestionId
					+ ": Validate actual targetSubQuestionText: " + actualTargetSubQuestion.targetSubQuestionText
					+ " matches expected targetSubQuestionText: " + expectedTargetSubQuestion.targetSubQuestionText, 
					actualTargetSubQuestion.targetSubQuestionText.compareTo(expectedTargetSubQuestion.targetSubQuestionText) == 0);
			}
		}
		
		//validate targetSubQuestionResponses sub-collection 
		List<TargetSubQuestionResponseObject> expectedTargetSubQuestionResponses = Arrays.asList(expectedTargetSubQuestion.targetSubQuestionResponses);
		List<TargetSubQuestionResponseObject> actualTargetSubQuestionResponses=Arrays.asList(actualTargetSubQuestion.targetSubQuestionResponses);
		
		validations.put("endpoint 1.11 : For item " + itemId + ", targetSubQuestionId " + actualTargetSubQuestion.targetSubQuestionId
				+ ": Validate actual size of targetSubQuestionResponses " + actualTargetSubQuestionResponses.size()
				+ " matches expected size of targetSubQuestionResponses " + expectedTargetSubQuestionResponses.size(), 
				actualTargetSubQuestionResponses.size() == expectedTargetSubQuestionResponses.size());
		
		for(TargetSubQuestionResponseObject expectedTargetSubQuestionResponse : expectedTargetSubQuestionResponses){
			TargetSubQuestionResponseObject actualTargetSubQuestionResponse=  actualTargetSubQuestion.getResponseByResponseCode(expectedTargetSubQuestionResponse.targetSubQuestionResponseCode);
			validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber
					+ ": Validate targetSubQuestionResponse with correctness value " + expectedTargetSubQuestionResponse.targetSubQuestionResponseCode
					+ " exists in collection ",  actualTargetSubQuestionResponse != null);
			if ( actualTargetSubQuestionResponse != null) {
				validateTargetSubQuestionResponse(expectedTargetSubQuestionResponse, actualTargetSubQuestionResponse, itemId, attemptNumber);
			}
		}
		
		//validate targetSubQuestionAnswer sub-collection 
		List<TargetSubQuestionAnswerObject> expectedTargetSubQuestionAnswers = Arrays.asList(expectedTargetSubQuestion.targetSubQuestionAnswers);
		List<TargetSubQuestionAnswerObject> actualTargetSubQuestionAnswers = Arrays.asList(actualTargetSubQuestion.targetSubQuestionAnswers);
	
		validations.put("endpoint 1.11 : For item " + itemId + ", sub Question Id " + actualTargetSubQuestion.targetSubQuestionId
				+ ": Validate actual size of targetSubQuestionAnswers " + actualTargetSubQuestionAnswers.size()
				+ " matches expected size of targetSubQuestionAnswers " +  expectedTargetSubQuestionAnswers.size(), 
				actualTargetSubQuestionAnswers.size() ==  expectedTargetSubQuestionAnswers.size());
	
		for(TargetSubQuestionAnswerObject expectedTargetSubQuestionAnswer : expectedTargetSubQuestionAnswers){
			TargetSubQuestionAnswerObject actualTargetSubQuestionAnswer=  actualTargetSubQuestion.getAnswerByAnswerId(expectedTargetSubQuestionAnswer.targetSubQuestionAnswerId);
			validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber
					+ ": Validate targetSubQuestionAnswer with " + expectedTargetSubQuestionAnswer.targetSubQuestionAnswerId
					+ " exists in collection ",  actualTargetSubQuestion != null);
			if ( actualTargetSubQuestionAnswer != null) {
				validateTargetSubQuestionAnswer( expectedTargetSubQuestionAnswer,actualTargetSubQuestionAnswer, itemId, attemptNumber);
			}
		}
		
	}
	
	
	
	private void validateTargetSubQuestionResponse(TargetSubQuestionResponseObject expectedTargetSubQuestionResponse, TargetSubQuestionResponseObject actualTargetSubQuestionResponse, String itemId, int attemptNumber){
		//no need to validate targetSubQuestionResponseCode
		
		//validate targetSubQuestionResponseStudentCount -int
		validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber + ", response with code: " +actualTargetSubQuestionResponse.targetSubQuestionResponseCode
			+ ": Validate actual targetSubQuestionResponseStudentCount: " + actualTargetSubQuestionResponse.targetSubQuestionResponseStudentCount
			+ " matches expected targetSubQuestionResponseStudentCount: " + expectedTargetSubQuestionResponse.targetSubQuestionResponseStudentCount,
			actualTargetSubQuestionResponse.targetSubQuestionResponseStudentCount == expectedTargetSubQuestionResponse.targetSubQuestionResponseStudentCount);
		
		//validate targetSubQuestionResponsePercent - float
		validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber + ", response with code: " + actualTargetSubQuestionResponse.targetSubQuestionResponseCode
			+ ": Validate actual targetSubQuestionResponsePercent: " +  actualTargetSubQuestionResponse.targetSubQuestionResponsePercent
			+ " matches expected targetSubQuestionResponsePercent: " + expectedTargetSubQuestionResponse.targetSubQuestionResponsePercent,
			actualTargetSubQuestionResponse.targetSubQuestionResponsePercent == expectedTargetSubQuestionResponse.targetSubQuestionResponsePercent);
	}


	private void validateTargetSubQuestionAnswer(TargetSubQuestionAnswerObject expectedTargetSubQuestionAnswer,TargetSubQuestionAnswerObject actualTargetSubQuestionAnswer, String itemId, int attemptNumber){
		//no need to validate targetSubQuestionAnswerId - String
		
		//validate targetSubQuestionAnswerText - String
		if (expectedTargetSubQuestionAnswer.targetSubQuestionAnswerText != null) {
			validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber + ", response with code: " +actualTargetSubQuestionAnswer.targetSubQuestionAnswerId
					+ ": Validate actual targetSubQuestionAnswerText is not null ", 
					actualTargetSubQuestionAnswer.targetSubQuestionAnswerText != null);
			if (actualTargetSubQuestionAnswer.targetSubQuestionAnswerText != null) {
				validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber + ", response with code: " +actualTargetSubQuestionAnswer.targetSubQuestionAnswerId
					+ ": Validate actual targetSubQuestionAnswerText: " + actualTargetSubQuestionAnswer.targetSubQuestionAnswerText
					+ " matches expected targetSubQuestionAnswerText: " + expectedTargetSubQuestionAnswer.targetSubQuestionAnswerText, 
					actualTargetSubQuestionAnswer.targetSubQuestionAnswerText.compareTo(expectedTargetSubQuestionAnswer.targetSubQuestionAnswerText) == 0);
			}
		}
		//validate targetSubQuestionAnswerCorrectFlag - boolean
		
		validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber + ", response with code: " + actualTargetSubQuestionAnswer.targetSubQuestionAnswerCorrectFlag
			+ ": Validate actual targetSubQuestionAnswerCorrectFlag: " +  actualTargetSubQuestionAnswer.targetSubQuestionAnswerCorrectFlag
			+ " matches expected targetSubQuestionAnswerCorrectFlag: " + expectedTargetSubQuestionAnswer.targetSubQuestionAnswerCorrectFlag,
			actualTargetSubQuestionAnswer.targetSubQuestionAnswerCorrectFlag == expectedTargetSubQuestionAnswer.targetSubQuestionAnswerCorrectFlag);
		
		
		//validate targetSubQuestionAnswerResponse sub-collection 
		List<TargetSubQuestionAnswerResponseObject> expectedTargetSubQuestionAnswersResponses = Arrays.asList(expectedTargetSubQuestionAnswer.targetSubQuestionAnswerResponses);
		List<TargetSubQuestionAnswerResponseObject> actualTargetSubQuestionAnswersResponses = Arrays.asList(actualTargetSubQuestionAnswer.targetSubQuestionAnswerResponses);
	
		validations.put("endpoint 1.11 : For item " + itemId + ", sub Question answer Id " + actualTargetSubQuestionAnswer.targetSubQuestionAnswerId
				+ ": Validate actual size of targetSubQuestionAnswersResponses " + actualTargetSubQuestionAnswersResponses.size()
				+ " matches expected size of targetSubQuestionAnswersResponses " +  expectedTargetSubQuestionAnswersResponses.size(), 
				actualTargetSubQuestionAnswersResponses.size() ==  expectedTargetSubQuestionAnswersResponses.size());
	
		for(TargetSubQuestionAnswerResponseObject expectedTargetSubQuestionAnswerResponse : expectedTargetSubQuestionAnswersResponses){
			TargetSubQuestionAnswerResponseObject actualTargetSubQuestionAnswerResponse =  actualTargetSubQuestionAnswer.getResponseByResponseCode(expectedTargetSubQuestionAnswerResponse.targetSubQuestionAnswerResponseCode);
			validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber
					+ ": Validate targetSubQuestionAnswer with " + expectedTargetSubQuestionAnswer.targetSubQuestionAnswerId
					+ " exists in collection ",  actualTargetSubQuestionAnswer != null);
			if ( actualTargetSubQuestionAnswerResponse != null) {
				validateTargetSubQuestionAnswerResponse( expectedTargetSubQuestionAnswerResponse,actualTargetSubQuestionAnswerResponse, itemId, attemptNumber);
			}
		}
		
	
	}
	
	private void validateTargetSubQuestionAnswerResponse(TargetSubQuestionAnswerResponseObject expectedTargetSubQuestionAnswerResponse,TargetSubQuestionAnswerResponseObject actualTargetSubQuestionAnswerResponse, String itemId, int attemptNumber){
		
			//validate targetSubQuestionAnswerStudentCount - int
		validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber + ",targetSubQuestionAnswerResponseCode: " +actualTargetSubQuestionAnswerResponse.targetSubQuestionAnswerResponseCode
			+ ": Validate actual targetSubQuestionAnswerStudentCount: " + actualTargetSubQuestionAnswerResponse.targetSubQuestionAnswerResponseStudentCount
			+ " matches expected targetSubQuestionAnswerStudentCount: " + expectedTargetSubQuestionAnswerResponse.targetSubQuestionAnswerResponseStudentCount,
			 actualTargetSubQuestionAnswerResponse.targetSubQuestionAnswerResponseStudentCount ==  expectedTargetSubQuestionAnswerResponse.targetSubQuestionAnswerResponseStudentCount);
		
		//validate targetSubQuestionAnswerStudentPercent - float
		validations.put("endpoint 1.11 : For item " + itemId + ", attempt " + attemptNumber + ", targetSubQuestionAnswerResponseCode : " +actualTargetSubQuestionAnswerResponse.targetSubQuestionAnswerResponseCode
				+ ": Validate actual targetSubQuestionAnswerStudentPercent: " + actualTargetSubQuestionAnswerResponse.targetSubQuestionAnswerResponsePercent
				+ " matches expected targetSubQuestionAnswerStudentPercent : " + expectedTargetSubQuestionAnswerResponse.targetSubQuestionAnswerResponsePercent,
				actualTargetSubQuestionAnswerResponse.targetSubQuestionAnswerResponsePercent == expectedTargetSubQuestionAnswerResponse.targetSubQuestionAnswerResponsePercent);
	
	}
		
	
	

	@Override
	public String[] getRequiredParameters() {
		return new String[] {ROUTE, EXPECTED_RESPONSE_CODE, EXPECTED_ITEMS};
	}
	
	
	@Override
	@JsonIgnore
	public String getExpectedResultsPrintString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("1.11 : SectionToModuleToResourceToItems");
		toReturn.append("\n...route: " + getEffectiveRoute());
		try {
			toReturn.append("\n...calling user id: " + getActingUserObject().getId());
			toReturn.append("\n...calling user username: " + getActingUserObject().getUsername());
			toReturn.append("\n...calling user password: " + getActingUserObject().getPassword());
		} catch (Exception e) {
			System.out.println("ORLY???");
		}
		toReturn.append("\n...expected offset: " + (getOffset() == null ? 0 : getOffset()));
		toReturn.append("\n...expected limit: " + (getLimit() == null ? 100 : getLimit()));
		toReturn.append("\n...expected itemCount: " + getItemCount());
		toReturn.append("\n...expected response code: " + getExpectedResponseCode());
		
		if (getExpectedItems() != null && getExpectedResponseCode()==200) {
			for (LearningResourceItemObject expectedItem : getExpectedItems()) {
				toReturn.append("\n......itemId: " + expectedItem.itemId);
				toReturn.append("\n......assessmentId: " + expectedItem.assessmentId);
				toReturn.append("\n......assessmentItemCompletedStudentCount: " + expectedItem.assessmentItemCompletedStudentCount);
				toReturn.append("\n......avgItemResponseScore: " + expectedItem.avgItemResponseScore);
				toReturn.append("\n......avgTimeSpentAssessing: " + expectedItem.avgTimeSpentAssessing);
				toReturn.append("\n......correctStudentCount: " + expectedItem.correctStudentCount);
				toReturn.append("\n......correctStudentPercent: " + expectedItem.correctStudentPercent);
				toReturn.append("\n......courseSectionId: " + expectedItem.courseSectionId);
				toReturn.append("\n......courseSectionStudentCount: " + expectedItem.courseSectionStudentCount);
				toReturn.append("\n......incorrectStudentCount: " + expectedItem.incorrectStudentCount);
				toReturn.append("\n......itemSequence: " + expectedItem.itemSequence);
				toReturn.append("\n......learningModuleId: " + expectedItem.learningModuleId);
				toReturn.append("\n......learningResourceId: " + expectedItem.learningResourceId);
				toReturn.append("\n......medianTimeSpentAssessing: " + expectedItem.medianTimeSpentAssessing);
				toReturn.append("\n......noAttemptStudentCount: " + expectedItem.noAttemptStudentCount);
				toReturn.append("\n......platformId: " + expectedItem.platformId);
				toReturn.append("\n......pointsPossible: " + expectedItem.pointsPossible);
				toReturn.append("\n......questionPresentationFormat: " + expectedItem.questionPresentationFormat);
				toReturn.append("\n......questionText: " + expectedItem.questionText);
				toReturn.append("\n......questionType: " + expectedItem.questionType);
				toReturn.append("\n......totalItemResponseScore: " + expectedItem.totalItemResponseScore);
				toReturn.append("\n......totalTimeSpentAssessing: " + expectedItem.totalTimeSpentAssessing);
				toReturn.append("\n.....................................................");
				if (expectedItem.attempts != null) {
					for (AttemptObject attempt : expectedItem.attempts) {
						toReturn.append("\n.........attemptNumber: " + attempt.attemptNumber);
						toReturn.append("\n.........attemptNumberStudentCount: " + attempt.attemptNumberStudentCount);
						toReturn.append("\n.....................................................");
						if (attempt.responses != null) {
							for (ResponseObject resp : attempt.responses) {
								toReturn.append("\n............responseCode: " + resp.responseCode);
								toReturn.append("\n............responsePercent: " + resp.responsePercent);
								toReturn.append("\n............responseStudentCount: " + resp.responseStudentCount);
								toReturn.append("\n.....................................................");
							}
						}
						if (attempt.targetSubQuestions != null) {
							for (TargetSubQuestionObject subQuestion : attempt.targetSubQuestions) {
								toReturn.append("\n............targetSubQuestionId: " + subQuestion.targetSubQuestionId);
								toReturn.append("\n............targetSubQuestionText: " + subQuestion.targetSubQuestionText);
								toReturn.append("\n.....................................................");
								if (subQuestion.targetSubQuestionAnswers != null) {
									for (TargetSubQuestionAnswerObject subAnswer : subQuestion.targetSubQuestionAnswers) {
										toReturn.append("\n...............targetSubQuestionAnswerId: " + subAnswer.targetSubQuestionAnswerId);
										toReturn.append("\n...............targetSubQuestionAnswerText: " + subAnswer.targetSubQuestionAnswerText);
										toReturn.append("\n...............targetSubQuestionAnswerCorrectFlag: " + subAnswer.targetSubQuestionAnswerCorrectFlag);
										if (subAnswer.targetSubQuestionAnswerResponses != null) {
											for (TargetSubQuestionAnswerResponseObject answerResponse : subAnswer.targetSubQuestionAnswerResponses){
												toReturn.append("\n..................targetSubQuestionAnswerResponseCode: " + answerResponse.targetSubQuestionAnswerResponseCode);
												toReturn.append("\n..................targetSubQuestionAnswerResponseStudentCount: " + answerResponse.targetSubQuestionAnswerResponseStudentCount);
												toReturn.append("\n..................targetSubQuestionAnswerResponseStudentPercent: " + answerResponse.targetSubQuestionAnswerResponsePercent);
												toReturn.append("\n.....................................................");
											}
										}
									}
								}
								if (subQuestion.targetSubQuestionResponses != null) {
									for (TargetSubQuestionResponseObject subResponse : subQuestion.targetSubQuestionResponses) {
										toReturn.append("\n...............targetSubQuestionResponseCode: " + subResponse.targetSubQuestionResponseCode);
										toReturn.append("\n...............targetSubQuestionResponsePercent: " + subResponse.targetSubQuestionResponsePercent);
										toReturn.append("\n...............targetSubQuestionResponseStudentCount: " + subResponse.targetSubQuestionResponseStudentCount);
										toReturn.append("\n.....................................................");
									}
								}
							}
						}
					}
				}
			}
		}
		return toReturn.toString();
	}

	
}
