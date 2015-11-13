package com.pearson.test.daalt.dataservice.validation.endpoint;

/**
 * This is endpoint 2.1 
 * https://{hostname}/v2/platforms/{platformId}/sections/{courseSectionId}/students
 */

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
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
import com.pearson.test.daalt.dataservice.response.model.DaaltCourseSectionStudentCollectionObject;
import com.pearson.test.daalt.dataservice.response.model.CourseSectionStudentObject;
import com.pearson.test.daalt.dataservice.response.model.LinkObject;
import com.pearson.test.daalt.dataservice.validation.DaaltValidation;
import com.pearson.test.daalt.dataservice.validation.ResponseCode;
import com.pearson.test.daalt.dataservice.validation.Validation;

public class SectionToStudentsAll extends DaaltValidation {

	private String route;
	private int expectedResponseCode;
	private CourseSectionStudentObject[] expectedStudents;
	private Map<String, LinkObject> expectedEnvelopeLevelLinks;
	private Integer offset;
	private Integer limit;
	private int itemCount;
	private String correlationId;
	public SectionToStudentsAll() {}
	
	@SuppressWarnings("unchecked")
	public SectionToStudentsAll(String testCaseNumber,
			Validation validation) {
		super(testCaseNumber, validation);
		
		route = parameters.get(ROUTE);
		expectedResponseCode = Integer.valueOf(parameters.get(EXPECTED_RESPONSE_CODE));
		Object obj = parameters.get(EXPECTED_STUDENTS);
		expectedStudents = (CourseSectionStudentObject[]) obj;
	}
	
	public SectionToStudentsAll(String creationTestName, UserObject user, 
			String route, int expectedResponseCode, 
			CourseSectionStudentObject[] expectedStudents,
			Map<String, LinkObject> expectedEnvelopeLevelLinks, 
			Integer offset, Integer limit, int itemCount) throws JsonGenerationException, JsonMappingException, IOException {
		setCreationTestName(creationTestName);
		setRoute(route);
		setExpectedResponseCode(expectedResponseCode);
		setExpectedStudents(expectedStudents);
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

	public CourseSectionStudentObject[] getExpectedStudents() {
		return expectedStudents;
	}

	public void setExpectedStudents(CourseSectionStudentObject[] expectedStudents) {
		this.expectedStudents= expectedStudents;
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
		
		System.out.println("endpoint 2.1 : route: " + getEffectiveRoute());
		System.out.println("endpoint 2.1 : calling user id: " + getActingUserObject().getId());
		System.out.println("endpoint 2.1 : calling user username: " + getActingUserObject().getUsername());
		System.out.println("endpoint 2.1 : calling user password: " + getActingUserObject().getPassword());
		System.out.println("endpoint 2.1 : calling user correlation-id: " + correlationId);
		if (context.getActualReturnedStatus() == ResponseCode.OK.value) {
			System.out.println("endpoint 2.1 : response: " + context.getActualReturnedResponse());
		}

		//validate value of responseCode
		boolean responseCodeCorrect = context.getActualReturnedStatus() == expectedResponseCode;
		validations.put("endpoint 2.1 : executed by user: " + getActingUserObject().getId() 
				+ " Validate actual response code " + context.getActualReturnedStatus() 
				+ " matches expected response code " + expectedResponseCode, responseCodeCorrect);
		
		if (expectedResponseCode == ResponseCode.OK.value && context.getActualReturnedStatus() == ResponseCode.OK.value) {
			DaaltCourseSectionStudentCollectionObject actualDataServiceResponse = context
					.getResponseAsDeserializedObject(DaaltCourseSectionStudentCollectionObject.class);
			
			validations.put("endpoint 2.1 : Validate response is not null ", actualDataServiceResponse != null);
			
			//validate students collection
			List<CourseSectionStudentObject> expectedStudentList = Arrays.asList(expectedStudents);
			List<CourseSectionStudentObject> actualStudents = Arrays.asList(actualDataServiceResponse.getItems());
			validations.put("endpoint 2.1 : Validate actual size of student collection " + actualStudents.size()
					+ " matches expected size of student collection " + expectedStudentList.size(), 
					actualStudents.size() == expectedStudentList.size());
			for (CourseSectionStudentObject expectedStudent : expectedStudentList) {
				CourseSectionStudentObject actualStudent = actualDataServiceResponse.getStudentWithId(expectedStudent.getId());
				validations.put("endpoint 2.1 : Validate student " + expectedStudent.getId() + " exists in collection ", actualStudent != null);
				if (actualStudent != null) {
					validateStudent(expectedStudent, actualStudent);
				}
			}
			
			//verify values of offset, limit, and itemCount
			if (offset == null) {
				validations.put("endpoint 2.1 : Validate actual offset " + actualDataServiceResponse.offset
						+ " matches expected offset 0 (default value)", 
						actualDataServiceResponse.offset == 0);
			} else {
				validations.put("endpoint 2.1 : Validate actual offset " + actualDataServiceResponse.offset
						+ " matches expected offset " + offset, 
						actualDataServiceResponse.offset == offset);
			}
			
			if (limit == null) {
				validations.put("endpoint 2.1 : Validate actual limit " + actualDataServiceResponse.limit
						+ " matches expected limit 100 (default value)", 
						actualDataServiceResponse.limit == 100);
			} else {
				validations.put("endpoint 2.1 : Validate actual limit " + actualDataServiceResponse.limit
						+ " matches expected limit " + limit, 
						actualDataServiceResponse.limit == limit);
			}
			
			validations.put("endpoint 2.1 : Validate actual itemCount " + actualDataServiceResponse.itemCount
					+ " matches expected itemCount " + itemCount, 
					actualDataServiceResponse.itemCount == itemCount);
			
			//verify contents of expectedEnvelopeLevelLinks == contents of actualDataServiceResponse.getLinks()
			if (expectedEnvelopeLevelLinks == null) {
				validations.put("endpoint 2.1 : Validate actual envelope-level _links is null ", 
						actualDataServiceResponse.getLinks() == null);
			} else {
				Map<String, LinkObject> actualEnvelopeLevelLinks = actualDataServiceResponse.getLinks();
				validations.put("endpoint 2.1 : Validate actual envelope-level _links is not null ", 
						actualEnvelopeLevelLinks != null);
				
				if (actualEnvelopeLevelLinks != null) {
					validations.put("endpoint 2.1 : Validate actual envelope-level _links collection size " + actualEnvelopeLevelLinks.size() 
							+ " matched expected envelope-level _links collection size " + expectedEnvelopeLevelLinks.size(),
							actualEnvelopeLevelLinks.size() == expectedEnvelopeLevelLinks.size());
					
					Iterator<Map.Entry<String, LinkObject>> expectedLinksIter = expectedEnvelopeLevelLinks.entrySet().iterator();
					while (expectedLinksIter.hasNext()) {
						Map.Entry<String, LinkObject> currentEntry = (Map.Entry<String, LinkObject>) expectedLinksIter.next();
						String currentKey = currentEntry.getKey();
						LinkObject expectedLink = currentEntry.getValue();
						LinkObject actualLink = actualEnvelopeLevelLinks.get(currentKey);
						if (expectedLink == null) {
							validations.put("endpoint 2.1 : Validate actualLink " + currentKey + " is null ", 
									actualLink == null);
						} else {
							validations.put("endpoint 2.1 : Validate actualLink " + currentKey + " is not null ", 
									actualLink != null);
							if (actualLink != null) {
								validations.put("endpoint 2.1 : Validate actualLink " + currentKey + " " + actualLink.href
										+ " matches expectedLink: " + expectedLink.href, 
										actualLink.equals(expectedLink));
							}
						}
					}
				}
			}
			
		}			
		return validations;
	}
	
	private void validateStudent(CourseSectionStudentObject expectedStudent, CourseSectionStudentObject actualStudent) {
		//don't need to validate : platformId, courseSectionId, learningModuleId, studentId

		//validate timeSpentLearning;
		if (expectedStudent.timeSpentLearning != null) {
			validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
					+ ": Validate actual timeSpentLearning is not null ", 
					actualStudent.timeSpentLearning != null);
			if (actualStudent.timeSpentLearning != null) {
				validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
						+ ": Validate actual timeSpentLearning: " + actualStudent.timeSpentLearning
						+ " matches expected timeSpentLearning: " + expectedStudent.timeSpentLearning, 
						actualStudent.timeSpentLearning.compareTo(expectedStudent.timeSpentLearning) == 0);
			}
		}
		
		//validate timeSpentAssessing
		if (expectedStudent.timeSpentAssessment != null) {
			validations.put("endpoint 2.1 : For student " + actualStudent.studentId
					+ ": Validate actual timeSpentAssessment is not null ", 
					actualStudent.timeSpentAssessment != null);
			if (actualStudent.timeSpentAssessment != null) {
				validations.put("endpoint 2.1 : For student " + actualStudent.studentId
						+ ": Validate actual timeSpentAssessment: " + actualStudent.timeSpentAssessment
						+ " matches expected timeSpentAssessment: " + expectedStudent.timeSpentAssessment, 
						actualStudent.timeSpentAssessment.compareTo(expectedStudent.timeSpentAssessment) == 0);
			}
		}
		
		//validate timeSpentTotal
		if (expectedStudent.timeSpentTotal != null) {
			validations.put("endpoint 2.1 : for student " + actualStudent.studentId
					+ ": validate actual timeSpentTotal is not null ", 
					expectedStudent.timeSpentTotal != null);
			if (actualStudent.timeSpentTotal != null) {
				validations.put("endpoint 2.1 : for student " + actualStudent.studentId
						+ ": validate actual timeSpentTotal: " + actualStudent.timeSpentTotal
						+ " matches expected timeSpentTotal: " + expectedStudent.timeSpentTotal, 
						actualStudent.timeSpentTotal.compareTo(expectedStudent.timeSpentTotal) == 0);
			}
		}
		
		// validate trending;
		if (expectedStudent.studentTrending == null) {
			validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
					+ ": Validate actual studentTrending is null ", 
					actualStudent.studentTrending == null);
		} else {
			validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
					+ ": Validate actual studentTrending is not null ", 
					actualStudent.studentTrending != null);
			if (actualStudent.studentTrending != null) {
				validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
						+ ": Validate actual studentTrending: " + actualStudent.studentTrending
						+ " matches expected studentTrending: " + expectedStudent.studentTrending, 
						trendValueWithinTolerances(actualStudent.studentTrending, expectedStudent.studentTrending));
			}
		}

		// validate lastActivityDate;
		if (expectedStudent.lastActivityDate == null) {
			validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
					+ ": Validate actual lastActivityDate is null ", 
					actualStudent.lastActivityDate == null);
		} else {
			validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
					+ ": Validate actual lastActivityDate is not null ", 
					actualStudent.lastActivityDate != null);
			if (actualStudent.lastActivityDate != null) {
				validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
						+ ": Validate actual lastActivityDate: " + actualStudent.lastActivityDate
						+ " matches expected lastActivityDate: " + expectedStudent.lastActivityDate, 
						(actualStudent.lastActivityDate.compareTo(expectedStudent.lastActivityDate) == 0));
			}
		}

		//verify that contents of expectedStudent._links == contents of actualStudent._links
		if (expectedStudent._links == null) {
			validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
					+ ": Validate actual item-level _links is null ", 
					actualStudent._links == null);
		} else {
			validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
					+ ": Validate actual item-level _links is not null ", 
					actualStudent._links != null);
			if (actualStudent._links != null) {
				validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
						+ ": Validate actual item-level _links collection size " + actualStudent._links.size() 
						+ " matched expected item-level _links collection size " + expectedStudent._links.size(),
						actualStudent._links.size() == expectedStudent._links.size());
				
				Iterator<Map.Entry<String, LinkObject>> expectedLinksIter = expectedStudent._links.entrySet().iterator();
				while (expectedLinksIter.hasNext()) {
					Map.Entry<String, LinkObject> currentEntry = (Map.Entry<String, LinkObject>) expectedLinksIter.next();
					String currentKey = currentEntry.getKey();
					LinkObject expectedLink = currentEntry.getValue();
					LinkObject actualLink = actualStudent._links.get(currentKey);
					if (expectedLink == null) {
						validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
								+ ": Validate actualLink " + currentKey + " is null ", 
								actualLink == null);
					} else {
						validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
								+ ": Validate actualLink " + currentKey + " is not null ", 
								actualLink != null);
						if (actualLink != null) {
							validations.put("endpoint 2.1 : For student " + actualStudent.studentId 
									+ ": Validate actualLink " + currentKey + " " + actualLink.href
									+ " matches expectedLink: " + expectedLink.href, 
									actualLink.equals(expectedLink));
						}
					}
				}
			}
		}
	}

	@Override
	public String[] getRequiredParameters() {
		return new String[] {ROUTE, EXPECTED_RESPONSE_CODE, EXPECTED_STUDENTS};
	}
	
	@Override
	public String getExpectedResultsPrintString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("2.1 : SectionToStudents");
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
		if (expectedEnvelopeLevelLinks != null) {
			toReturn.append("\n...expected envelope-level _links:");
			Iterator<Map.Entry<String, LinkObject>> expectedLinksIter = expectedEnvelopeLevelLinks.entrySet().iterator();
			while (expectedLinksIter.hasNext()) {
				Map.Entry<String, LinkObject> currentEntry = (Map.Entry<String, LinkObject>) expectedLinksIter.next();
				toReturn.append("\n......key: " + currentEntry.getKey());
				toReturn.append("\n......value: " + currentEntry.getValue().href);
				toReturn.append("\n.....................................................");
			}
		}
		if (expectedStudents != null && getExpectedResponseCode()==200) {
			for (CourseSectionStudentObject expectedStudent : expectedStudents) {
				toReturn.append("\n......platformId: " + expectedStudent.platformId);
				toReturn.append("\n......courseSectionId: " + expectedStudent.courseSectionId);
				toReturn.append("\n......studentId: " + expectedStudent.studentId);
				toReturn.append("\n......timeSpentLearning: " + expectedStudent.timeSpentLearning);
				toReturn.append("\n......timeSpentAssessment: " + expectedStudent.timeSpentAssessment);
				toReturn.append("\n......timeSpentTotal: " + expectedStudent.timeSpentTotal);
				toReturn.append("\n......studentTrending: " + expectedStudent.studentTrending);
				toReturn.append("\n......lastActivityDate: " + expectedStudent.lastActivityDate);
				if (expectedStudent._links != null) {
					toReturn.append("\n......expected item-level _links:");
					Iterator<Map.Entry<String, LinkObject>> expectedLinksIter = expectedStudent._links.entrySet().iterator();
					while (expectedLinksIter.hasNext()) {
						Map.Entry<String, LinkObject> currentEntry = (Map.Entry<String, LinkObject>) expectedLinksIter.next();
						toReturn.append("\n.........key: " + currentEntry.getKey());
						toReturn.append("\n.........value: " + currentEntry.getValue().href);
					}
				}
				toReturn.append("\n.....................................................");
			}
		}
		return toReturn.toString();
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
}
