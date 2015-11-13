package com.pearson.test.daalt.dataservice.validation.endpoint;

/**
 * This is endpoint 1.12
 * https://{hostname}/v2/platforms/{platformId}/sections/{courseSectionId}/modules/{learningModuleId}/students
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

import com.pearson.test.daalt.dataservice.mongodb.JSONUtils;
import com.pearson.ed.pi.authentication.systemclient.PiTokenContainer;
import com.pearson.qa.apex.dataobjects.UserObject;
import com.pearson.qa.apex.ziggyfw.context.SimpleRestRequestContext;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.User;
import com.pearson.test.daalt.dataservice.response.model.DaaltLearningModuleStudentCollectionObject;
import com.pearson.test.daalt.dataservice.response.model.LearningModuleStudentObject;
import com.pearson.test.daalt.dataservice.response.model.LinkObject;
import com.pearson.test.daalt.dataservice.validation.DaaltValidation;
import com.pearson.test.daalt.dataservice.validation.ResponseCode;
import com.pearson.test.daalt.dataservice.validation.Validation;

public class SectionToModuleToStudentsAll extends DaaltValidation {

	private String route;
	private int expectedResponseCode;
	private LearningModuleStudentObject[] expectedStudents;
	private Map<String, LinkObject> expectedEnvelopeLevelLinks;
	private Integer offset;
	private Integer limit;
	private int itemCount;
	private String correlationId;
	public SectionToModuleToStudentsAll() {}
	
	@SuppressWarnings("unchecked")
	public SectionToModuleToStudentsAll(String testCaseNumber,
			Validation validation) {
		super(testCaseNumber, validation);
		
		route = parameters.get(ROUTE);
		expectedResponseCode = Integer.valueOf(parameters.get(EXPECTED_RESPONSE_CODE));
		Object obj = parameters.get(EXPECTED_STUDENTS);
		expectedStudents =  (LearningModuleStudentObject[]) obj;
	}
	
	public SectionToModuleToStudentsAll(String creationTestName, UserObject user, 
			String route, int expectedResponseCode, 
			LearningModuleStudentObject[] expectedStudents,
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

	public LearningModuleStudentObject[] getExpectedStudents() {
		return expectedStudents;
	}

	public void setExpectedStudents(LearningModuleStudentObject[] expectedStudents) {
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
		
		System.out.println("endpoint 1.12 : route: " + getEffectiveRoute());
		System.out.println("endpoint 1.12 : calling user id: " + getActingUserObject().getId());
		System.out.println("endpoint 1.12 : calling user username: " + getActingUserObject().getUsername());
		System.out.println("endpoint 1.12 : calling user password: " + getActingUserObject().getPassword());
		System.out.println("endpoint 1.12 : calling user correlation-id: " + correlationId);
		//validate value of responseCode
		boolean responseCodeCorrect = context.getActualReturnedStatus() == expectedResponseCode;
		validations.put("endpoint 1.12 : executed by user: " + getActingUserObject().getId() 
				+ " Validate actual response code " + context.getActualReturnedStatus() 
				+ " matches expected response code " + expectedResponseCode, responseCodeCorrect);
		
		if (expectedResponseCode == ResponseCode.OK.value && context.getActualReturnedStatus() == ResponseCode.OK.value) {
			System.out.println("endpoint 1.12 : response: " + context.getActualReturnedResponse());
			
			DaaltLearningModuleStudentCollectionObject actualDataServiceResponse = context
					.getResponseAsDeserializedObject(DaaltLearningModuleStudentCollectionObject.class);
			
			validations.put("endpoint 1.12 : Validate response is not null ", actualDataServiceResponse != null);
			
			//validate students collection
			List<LearningModuleStudentObject> expectedStudentList = Arrays.asList(expectedStudents);
			List<LearningModuleStudentObject> actualStudents = Arrays.asList(actualDataServiceResponse.getItems());
			validations.put("endpoint 1.12 : Validate actual size of student collection " + actualStudents.size()
					+ " matches expected size of student collection " + expectedStudentList.size(), 
					actualStudents.size() == expectedStudentList.size());
			for (LearningModuleStudentObject expectedStudent : expectedStudentList) {
				LearningModuleStudentObject actualStudent = actualDataServiceResponse.getStudentWithId(expectedStudent.getId());
				if (actualStudent != null || (offset == null && limit == null)) {
					validations.put("endpoint 1.12 : Validate student " + expectedStudent.getId() + " exists in collection ", actualStudent != null);
					if (actualStudent != null) {
						validateStudent(expectedStudent, actualStudent);
					}
				} else {
					System.out.println("endpoint 1.12 : skipping remaining validations because ignoring DDS-1957");
				}
			}
			
			//verify values of offset, limit, and itemCount
			if (offset == null) {
				validations.put("endpoint 1.12 : Validate actual offset " + actualDataServiceResponse.offset
						+ " matches expected offset 0 (default value)", 
						actualDataServiceResponse.offset == 0);
			} else {
				validations.put("endpoint 1.12 : Validate actual offset " + actualDataServiceResponse.offset
						+ " matches expected offset " + offset, 
						actualDataServiceResponse.offset == offset);
			}
			
			if (limit == null) {
				validations.put("endpoint 1.12 : Validate actual limit " + actualDataServiceResponse.limit
						+ " matches expected limit 100 (default value)", 
						actualDataServiceResponse.limit == 100);
			} else {
				validations.put("endpoint 1.12 : Validate actual limit " + actualDataServiceResponse.limit
						+ " matches expected limit " + limit, 
						actualDataServiceResponse.limit == limit);
			}
			
			validations.put("endpoint 1.12 : Validate actual itemCount " + actualDataServiceResponse.itemCount
					+ " matches expected itemCount " + itemCount, 
					actualDataServiceResponse.itemCount == itemCount);
			
			//TODO: verify contents of expectedEnvelopeLevelLinks == contents of actualDataServiceResponse.getLinks()
			//FROM KAT: Please don't verify _links at this time. It is not yet implemented
			
		}			
		return validations;
	}
	
	private void validateStudent(LearningModuleStudentObject expectedStudent, LearningModuleStudentObject actualStudent) {
		//don't need to validate : platformId, courseSectionId, learningModuleId, studentId
		
		//validate learningModuleTitle
		if (expectedStudent.learningModuleTitle != null) {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
					+ ": Validate actual learningModuleTitle is not null ", 
					actualStudent.learningModuleTitle != null);
			if (actualStudent.learningModuleTitle != null) {
				validations.put("endpoint 1.12 : For student " + actualStudent.studentId
						+ ": Validate actual learningModuleTitle: " + actualStudent.learningModuleTitle
						+ " matches expected learningModuleTitle: " + expectedStudent.learningModuleTitle, 
						actualStudent.learningModuleTitle.compareTo(expectedStudent.learningModuleTitle) == 0);
			}
		}
		
		//validate learningModuleSequence; Float
		if (expectedStudent.learningModuleSequence == null) {
			validations.put("endpoint 1.12 : For resource " + actualStudent.studentId
					+ ": Validate actual learningModuleSequence is null ", 
					actualStudent.learningModuleSequence == null);
		} else {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
					+ ": Validate actual learningModuleSequence is not null ", 
					actualStudent.learningModuleSequence != null);
			if (actualStudent.learningModuleSequence != null) {
				validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
						+ ": Validate actual learningModuleSequence: " + actualStudent.learningModuleSequence
						+ " matches expected learningModuleSequence: " + expectedStudent.learningModuleSequence, 
						actualStudent.learningModuleSequence.compareTo(expectedStudent.learningModuleSequence) == 0);
			}
		}
		
		//validate pointsPossible;  Float
		if (expectedStudent.pointsPossible == null) {
//			validations.put("endpoint 1.12 : For student " + actualStudent.studentId
//					+ ": Validate actual pointsPossible is null ", 
//					actualStudent.pointsPossible == null);
		} else {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId
					+ ": Validate actual pointsPossible is not null ", 
					actualStudent.pointsPossible != null);
			if (actualStudent.pointsPossible != null) {
				validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
						+ ": Validate actual pointsPossible: " + actualStudent.pointsPossible
						+ " matches expected pointsPossible: " + expectedStudent.pointsPossible, 
						actualStudent.pointsPossible .equals( expectedStudent.pointsPossible));
			}
		}
		
		//validate studentFirstName;
		if (expectedStudent.studentFirstName != null) {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
					+ ": Validate actual studentFirstName is not null ", 
					actualStudent.studentFirstName != null);
			if (actualStudent.studentFirstName != null) {
				validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
						+ ": Validate actual studentFirstName: " + actualStudent.studentFirstName
						+ " matches expected studentFirstName: " + expectedStudent.studentFirstName, 
						actualStudent.studentFirstName.compareTo(expectedStudent.studentFirstName) == 0);
			}
		}
		
		//validate studentLastName;
		if (expectedStudent.studentLastName != null) {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
					+ ": Validate actual studentLastName is not null ", 
					actualStudent.studentLastName != null);
			if (actualStudent.studentLastName != null) {
				validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
						+ ": Validate actual studentLastName: " + actualStudent.studentLastName
						+ " matches expected studentLastName: " + expectedStudent.studentLastName, 
						actualStudent.studentLastName.compareTo(expectedStudent.studentLastName) == 0);
			}
		}
		
		//validate studentPoints; Float
		if (expectedStudent.studentPoints == null) {
//			validations.put("endpoint 1.12 : For student " + actualStudent.studentId
//					+ ": Validate actual studentPoints is null ", 
//					actualStudent.studentPoints == null);
		} else {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId
					+ ": Validate actual studentPoints is not null ", 
					actualStudent.studentPoints != null);
			if (actualStudent.studentPoints != null) {
				validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
						+ ": Validate actual studentPoints: " + actualStudent.studentPoints
						+ " matches expected studentPoints: " + expectedStudent.studentPoints, 
						actualStudent.studentPoints .equals( expectedStudent.studentPoints));
			}
		}
		
		//validate studentPercent; Float
		if (expectedStudent.studentPercent == null) {
//			validations.put("endpoint 1.12 : For student " + actualStudent.studentId
//					+ ": Validate actual studentPercent is null ", 
//					actualStudent.studentPercent == null);
		} else {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId
					+ ": Validate actual studentPercent is not null ", 
					actualStudent.studentPercent != null);
			if (actualStudent.studentPercent != null) {
				validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
						+ ": Validate actual studentPercent: " + actualStudent.studentPercent
						+ " matches expected studentPercent: " + expectedStudent.studentPercent, 
						actualStudent.studentPercent .equals( expectedStudent.studentPercent));
			}
		}

		// validate studentPracticePoints;   Float
		if (expectedStudent.studentPracticePoints == null) {
//			validations.put("endpoint 1.12 : For student " + actualStudent.studentId
//					+ ": Validate actual studentPracticePoints is null ", 
//					actualStudent.studentPracticePoints == null);
		} else {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId
					+ ": Validate actual studentPracticePoints is not null ", 
					actualStudent.studentPracticePoints != null);
			if (actualStudent.studentPracticePoints != null) {
				validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
						+ ": Validate actual studentPoints: " + actualStudent.studentPracticePoints
						+ " matches expected studentPoints: " + expectedStudent.studentPracticePoints, 
						actualStudent.studentPracticePoints .equals( expectedStudent.studentPracticePoints));
			}
		}
		
		// validate practicePointsPossible;   Float
		if (expectedStudent.practicePointsPossible == null) {
//			validations.put("endpoint 1.12 : For student " + actualStudent.studentId
//					+ ": Validate actual practicePointsPossible is null ", 
//					actualStudent.practicePointsPossible == null);
		} else {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId
					+ ": Validate actual practicePointsPossible is not null ", 
					actualStudent.practicePointsPossible != null);
			if (actualStudent.practicePointsPossible != null) {
				validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
						+ ": Validate actual practicePointsPossible: " + actualStudent.practicePointsPossible
						+ " matches expected practicePointsPossible: " + expectedStudent.practicePointsPossible, 
						actualStudent.practicePointsPossible.equals(expectedStudent.practicePointsPossible));
			}
		}
		
		
		// validate trending;
		if (expectedStudent.studentTrending == null) {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
					+ ": Validate actual studentTrending is null ", 
					actualStudent.studentTrending == null);
		} else {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
					+ ": Validate actual studentTrending is not null ", 
					actualStudent.studentTrending != null);
			if (actualStudent.studentTrending != null) {
				validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
						+ ": Validate actual studentTrending: " + actualStudent.studentTrending
						+ " matches expected studentTrending: " + expectedStudent.studentTrending, 
						actualStudent.studentTrending.equals(expectedStudent.studentTrending));
			}
		}
		
		//validate timeSpentAssessment;
		if (expectedStudent.timeSpentAssessment != null) {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
					+ ": Validate actual timeSpentAssessment is not null ", 
					actualStudent.timeSpentAssessment != null);
			if (actualStudent.timeSpentAssessment != null) {
				validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
						+ ": Validate actual timeSpentAssessment: " + actualStudent.timeSpentAssessment
						+ " matches expected timeSpentAssessment: " + expectedStudent.timeSpentAssessment, 
						actualStudent.timeSpentAssessment.compareTo(expectedStudent.timeSpentAssessment) == 0);
			}
		}
		
		//validate timeSpentLearning;
		if (expectedStudent.timeSpentLearning != null) {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
					+ ": Validate actual timeSpentLearning is not null ", 
					actualStudent.timeSpentLearning != null);
			if (actualStudent.timeSpentLearning != null) {
				validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
						+ ": Validate actual timeSpentLearning: " + actualStudent.timeSpentLearning
						+ " matches expected timeSpentLearning: " + expectedStudent.timeSpentLearning, 
						actualStudent.timeSpentLearning.compareTo(expectedStudent.timeSpentLearning) == 0);
			}
		}
		
		//validate timeSpentTotal;
		if (expectedStudent.timeSpentTotal != null) {
			validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
					+ ": Validate actual timeSpentTotal is not null ", 
					actualStudent.timeSpentTotal != null);
			if (actualStudent.timeSpentTotal != null) {
				validations.put("endpoint 1.12 : For student " + actualStudent.studentId 
						+ ": Validate actual timeSpentTotal: " + actualStudent.timeSpentTotal
						+ " matches expected timeSpentTotal: " + expectedStudent.timeSpentTotal, 
						actualStudent.timeSpentTotal.compareTo(expectedStudent.timeSpentTotal) == 0);
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
		toReturn.append("1.12 : SectionToModuleToStudents");
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
		if (expectedStudents != null && getExpectedResponseCode()==200) {
			for (LearningModuleStudentObject expectedStudent : expectedStudents) {
				toReturn.append("\n......platformId: " + expectedStudent.platformId);
				toReturn.append("\n......courseSectionId: " + expectedStudent.courseSectionId);
				toReturn.append("\n......learningModuleId: " + expectedStudent.learningModuleId);
				toReturn.append("\n......learningModuleTitle: " + expectedStudent.learningModuleTitle);
				toReturn.append("\n......learningModuleSequence: " + expectedStudent.learningModuleSequence);
				toReturn.append("\n......pointsPossible: " + expectedStudent.pointsPossible);
				toReturn.append("\n......studentId: " + expectedStudent.studentId);
				toReturn.append("\n......studentFirstName: " + expectedStudent.studentFirstName);
				toReturn.append("\n......studentLastName: " + expectedStudent.studentLastName);
				toReturn.append("\n......studentPoints: " + expectedStudent.studentPoints);
				toReturn.append("\n......studentPercent: " + expectedStudent.studentPercent);
				toReturn.append("\n......practicePointsPossible: " + expectedStudent.practicePointsPossible);
				toReturn.append("\n......studentPracticePoints: " + expectedStudent.studentPracticePoints);
				toReturn.append("\n......studentTrending: " + expectedStudent.studentTrending);
				toReturn.append("\n......timeSpentAssessment: " + expectedStudent.timeSpentAssessment);
				toReturn.append("\n......timeSpentLearning: " + expectedStudent.timeSpentLearning);
				toReturn.append("\n......timeSpentTotal: " + expectedStudent.timeSpentTotal);
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
