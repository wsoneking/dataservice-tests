package com.pearson.test.daalt.dataservice.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.testng.annotations.Test;

import com.pearson.qa.apex.dataobjects.EnvironmentType;
import com.pearson.qa.apex.dataobjects.UserObject;
import com.pearson.qa.apex.dataobjects.UserType;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.User;
import com.pearson.test.daalt.dataservice.model.BasicInstructor;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.request.message.AdHocSeerMessage;
import com.pearson.test.daalt.dataservice.request.message.AdHocSubPubMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.response.model.AssessmentItemObject;
import com.pearson.test.daalt.dataservice.response.model.AssessmentObject;
import com.pearson.test.daalt.dataservice.response.model.AttemptObject;
import com.pearson.test.daalt.dataservice.response.model.LearningModuleStudentObject;
import com.pearson.test.daalt.dataservice.response.model.LearningResourceItemObject;
import com.pearson.test.daalt.dataservice.response.model.LearningResourceObject;
import com.pearson.test.daalt.dataservice.response.model.ResponseObject;
import com.pearson.test.daalt.dataservice.response.model.StudentLearningResourceObject;
import com.pearson.test.daalt.dataservice.response.model.StudentModuleObject;
import com.pearson.test.daalt.dataservice.response.model.TargetSubQuestionAnswerObject;
import com.pearson.test.daalt.dataservice.response.model.TargetSubQuestionAnswerResponseObject;
import com.pearson.test.daalt.dataservice.response.model.TargetSubQuestionObject;
import com.pearson.test.daalt.dataservice.response.model.TargetSubQuestionResponseObject;
import com.pearson.test.daalt.dataservice.validation.ResponseCode;
import com.pearson.test.daalt.dataservice.validation.Validation;
import com.pearson.test.daalt.dataservice.validation.endpoint.AssessmentsAll;
import com.pearson.test.daalt.dataservice.validation.endpoint.SectionToModuleToResourceToItemsAll;
import com.pearson.test.daalt.dataservice.validation.endpoint.SectionToModuleToResourcesAll;
import com.pearson.test.daalt.dataservice.validation.endpoint.SectionToModuleToStudentsAll;
import com.pearson.test.daalt.dataservice.validation.endpoint.SectionToStudentToModuleToResourcesAll;
import com.pearson.test.daalt.dataservice.validation.endpoint.SectionToStudentToModulesAll;

public class AdHocScenario extends BaseTestScenario {
	
//NOTE: Be sure to change the testRun number manually each time you run this scenario. 
	//public void send(int seerCount, int subPubCount) method is called with hard coded parameters
	private int testRun = 38;
	
	protected String baseUrl = "https://daalt-analytics.stg-prsn.com/v2/analytics";
	

	private String instructorId ="ffffffff53b4ea66e4b0279a23cb161a";
	private String studentId = "ffffffff54c2c421e4b0f10ebd0752f4";
	private String bookId = "SQEBookFake0" + testRun;
	private String courseSectionId = "SQECSFake0" + testRun;
	private String assignmentId = "SQEAssignFake0" + testRun;
	private String chapterId = "SQEChapterFake0" + testRun;
	private String platformId = "DAALT-SQE";
	private String chapterQuizId = "SQEQuizFake0" + testRun;
	private String assessmentLRId = "SQE-Assess-LR-Fake-0" + testRun;
	private String assessmentItemId01 = "SQEAssessItemFake010" + testRun;
	private String assessmentItemTargetId01 = "SQE-Assess-Item-Target-Fake01-0" + testRun;
	private String assessmentItemAns01Id01 = "SQE-Assess-Item-Answer01-Fake01-0" + testRun;


	@Test
	public void loadBasicTestData() throws Exception {
		User instrFromConfig = getEngine().getInstructor();
		Instructor instr = new BasicInstructor(instrFromConfig.getUserName(), 
				instrFromConfig.getPassword(), 
				instrFromConfig.getId(),
				instrFromConfig.getFirstName(),
				instrFromConfig.getLastName());
		UserObject instructorUser = new UserObject(instr.getUserName(), 
				instr.getPassword(), 
				UserType.Professor, EnvironmentType.Staging);
		instructorUser.setId(instr.getPersonId());

		StringBuilder timeOnTaskOutput = new StringBuilder("\nTime on task summary...");
		
		//build and send messages
		
		//send Person message? (optional)
		
		//send Product_To_Course_Section_PLT 
		
		Message productToCourseSectionPLT = new AdHocSubPubMessage ("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:20.946Z\","
				+ "\"Message_Type_Code\":\"Product_To_Course_Section_PLT\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Product_Source_System_Code\":\"EPS\","
				+ "\"Product_Source_System_Record_Id\":\""+bookId+"\","
				+ "\"Course_Section_Source_System_Code\":\"Registrar\","
				+ "\"Course_Section_Source_System_Record_Id\":\""+courseSectionId+"\","
				+ "\"Delivery_Platform_Code\":\"DAALT-SQE\","
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:21.074Z\"}");
		productToCourseSectionPLT.send(1,1);
		
		Message instrPersonCourseSectionMsg = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:21.974Z\","
				+ "\"Message_Type_Code\":\"Person_Course_Section\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Course_Section_Source_System_Code\":\"Registrar\","
				+ "\"Person_Source_System_Code\":\"PI\","
				+ "\"Course_Section_Source_System_Record_Id\":\""+courseSectionId+"\","
				+ "\"Person_Source_System_Record_Id\":\""+instructorId+"\","
				+ "\"Person_Role_Code\":\"Instructor\","
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:21.975Z\"}");
		instrPersonCourseSectionMsg.send(1,1);
		
		Message StudentPersonCourseSectionMsg = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:22.561Z\","
				+ "\"Message_Type_Code\":\"Person_Course_Section\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Course_Section_Source_System_Code\":\"Registrar\","
				+ "\"Person_Source_System_Code\":\"PI\","
				+ "\"Course_Section_Source_System_Record_Id\":\""+courseSectionId+"\","
				+ "\"Person_Source_System_Record_Id\":\""+studentId+"\","
				+ "\"Person_Role_Code\":\"Student\","
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:22.561Z\"}");
		StudentPersonCourseSectionMsg.send(1,1);
		
		Message assessmentMsg = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:20.933Z\","
				+ "\"Message_Type_Code\":\"Assessment\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Assessment_Source_System_Code\":\"PAF\","
				+ "\"Assessment_Source_System_Record_Id\":\""+chapterQuizId +"\","
				+ "\"Ref_Assessment_Type_Code\":\"Quiz\","
				+ "\"Assessment_Items\":[{\"Assessment_Item_Source_System_Record_Id\":\""+assessmentItemId01+"\","
				+ "\"Assessment_Item_Source_System_Code\":\"PAF\","
				+ "\"Sequence_Number\":0.0}],"
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:33.670Z\"}");
		assessmentMsg.send(1,1);
		
		Message learningResourceBookMsg = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:23.147Z\","
				+ "\"Message_Type_Code\":\"Learning_Resource\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Ref_Learning_Resource_Type_Code\":\"Title\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Learning_Resource_Source_System_Record_Id\":\""+bookId+"\","
				+ "\"Learning_Resource_Source_System_Code\":\"Revel\","
				+ "\"Title\":\"Title \","
				+ "\"Ref_Learning_Resource_Creation_Status_Code\":\"Completed\","
				+ "\"Delivery_Platform_Code\":\"DAALT-SQE\","
				+ "\"Ref_Language_Code\":\"en-US\","
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:23.161Z\"}");
		learningResourceBookMsg.send(1,1);
		
		Message learningResourceChapterMsg = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:23.150Z\","
				+ "\"Message_Type_Code\":\"Learning_Resource\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Ref_Learning_Resource_Type_Code\":\"Chapter\","
				+ "\"Message_Version\":\"2.0\",\"Learning_Resource_Source_System_Record_Id\":\""+chapterId+"\","
				+ "\"Learning_Resource_Source_System_Code\":\"Revel\","
				+ "\"Title\":\"Chapter Title\","
				+ "\"Ref_Learning_Resource_Creation_Status_Code\":\"Completed\","
				+ "\"Delivery_Platform_Code\":\"DAALT-SQE\","
				+ "\"Ref_Language_Code\":\"en-US\","
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:24.914Z\"}");
		learningResourceChapterMsg.send(1,1);
		
		Message learningReosurceAsessmentMsg = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:23.153Z\","
				+ "\"Message_Type_Code\":\"Learning_Resource\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Ref_Learning_Resource_Type_Code\":\"Assessment\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Learning_Resource_Source_System_Record_Id\":\""+assessmentLRId+"\","
				+ "\"Learning_Resource_Source_System_Code\":\"Revel\","
				+ "\"Title\":\"Quiz Title\","
				+ "\"Ref_Learning_Resource_Creation_Status_Code\":\"Completed\","
				+ "\"Delivery_Platform_Code\":\"DAALT-SQE\","
				+ "\"Ref_Language_Code\":\"en-US\","
				+ "\"Ref_Learning_Resource_Subtype_Code\":\"ChapterTest\","
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:26.667Z\"}");
		learningReosurceAsessmentMsg.send(1,1);
		
		
		//send Course_Section_To_Learning_Resource message (optional?)
		Message courseSectionToLearningResourceMsg = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-25T12:18:52.225Z\","
				+ "\"Message_Type_Code\":\"Course_Section_To_Learning_Resource\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Course_Section_Source_System_Code\":\"Registrar\","
				+ "\"Course_Section_Source_System_Record_Id\":\""+courseSectionId+"\","
				+ "\"Ref_Relationship_Context_Code\":\"Book\","
				+ "\"Learning_Resource_Relationship_Context_Id\":\""+bookId+"\","
				+ "\"Learning_Resource_Source_System_Code\":\"EPS\","
				+ "\"Learning_Resource_Source_System_Record_Id\":\""+bookId+"\","
				+ "\"currentTimeFormatted\":\"2015-06-25T12:18:52.851Z\"}");
		courseSectionToLearningResourceMsg.send(1,1);	
		
		
		//send Assessment_Item_Possible_Answers message for each question
		Message assessmentItemPossibleAnswersQuestionMsg = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:20.931Z\","
				+ "\"Message_Type_Code\":\"Assessment_Item_Possible_Answers\","
				+ "\"Originating_System_Code\":\"Brix\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Assessment_Item_Source_System_Code\":\"PAF\","
				+ "\"Assessment_Item_Source_System_Record_Id\":\""+assessmentItemId01+"\","
				+ "\"Question_Text\":\"Chapter Quiz - Question 01\","
				+ "\"Assessment_Item_Question_Type\":\"MultiValue\","
				+ "\"Assessment_Item_Question_Presentation_Format\":\"RadioButton\","
				+ "\"Multi_Value_Answer_Data\":{\"Answers\":[{\"Answer_Id\":\""+assessmentItemAns01Id01+"\","
				+ "\"Answer_Text\":\"Chapter Quiz - Question 01 - Answer 01\"}],"
				+ "\"Targets\":[{\"Target_Id\":\""+assessmentItemTargetId01+"\","
				+ "\"Target_Text\":\"Chapter Quiz - Question 01\","
				+ "\"Target_Correct_Responses\":[{\"Target_Correct_Response_Answer_Id\":\""+assessmentItemAns01Id01+"\"}]}]},"
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:36.590Z\"}");
		assessmentItemPossibleAnswersQuestionMsg.send(1,1);
		
		//send Learning_Module message
		Message learningModuleCreateMessage = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:23.149Z\","
				+ "\"Message_Type_Code\":\"Learning_Module\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Creation_Datetime\":\"2015-06-10T11:33:23.149Z\","
				+ "\"Identifying_Code\":\"2015-06-10T11:33:23.149Z\","
				+ "\"Learning_Module_Source_System_Code\":\"Revel\","
				+ "\"Learning_Module_Source_System_Record_Id\":\""+assignmentId+"\","
				+ "\"Course_Section_Source_System_Code\":\"Registrar\","
				+ "\"Course_Section_Source_System_Record_Id\":\""+courseSectionId+"\","
				+ "\"Instructor_Source_System_Code\":\"PI\","
				+ "\"Instructor_Source_System_Record_Id\":\""+instructorId+"\","
				+ "\"Sequence_Number\":1.43482159E12,"
				+ "\"Title\":\"DAALT SQE Test Assignment\","
				+ "\"Include_In_Trending_Flag\":false,"
				+ "\"Activity_Due_Datetime\":\"2015-06-20T11:33:20.934-0600\","
				+ "\"Release_Datetime\":\"2015-06-10T11:33:23.149Z\","
				+ "\"Possible_Points\":18.0,"
				+ "\"Ref_Learning_Module_Type_Code\":\"Assignment\","
				+ "\"Description\":\"This module covers the main schools of psychology\","
				+ "\"Learning_Module_Version\":\"1.2\","
				+ "\"Maximum_Time_Allowed\":10.0,"
				+ "\"Ref_Time_Units_Code\":\"Hour\",\"Weight\":20.0,"
				+ "\"Ref_Language_Code\":\"en-US\","
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:24.330Z\"}");
		learningModuleCreateMessage.send(1,1);
		
	
		
		
		Message learningResourceRelationshipChapterToChapterQuizMsg = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:23.153Z\","
				+ "\"Message_Type_Code\":\"Learning_Resource_Relationship\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Ref_Relationship_Context_Code\":\"Book\","
				+ "\"Learning_Resource_Relationship_Context_Id\":\""+bookId+"\","
				+ "\"Parent_Source_System_Code\":\"Revel\","
				+ "\"Parent_Source_System_Record_Id\":\""+chapterId+"\","
				+ "\"Ref_Learning_Resource_Relationship_Type_Code\":\"IsPartOf\","
				+ "\"Child_Source_System_Code\":\"Revel\","
				+ "\"Sequence_Number\":1.0,"
				+ "\"Child_Source_System_Record_Id\":\""+assessmentLRId+"\","
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:27.250Z\"}");
		learningResourceRelationshipChapterToChapterQuizMsg.send(0,1);
		
		Message learningResourceRelationshipBookToChapterMsg = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:23.151Z\","
				+ "\"Message_Type_Code\":\"Learning_Resource_Relationship\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Ref_Relationship_Context_Code\":\"Book\","
				+ "\"Learning_Resource_Relationship_Context_Id\":\""+bookId+"\","
				+ "\"Parent_Source_System_Code\":\"EPS\","
				+ "\"Parent_Source_System_Record_Id\":\""+bookId+"\","
				+ "\"Ref_Learning_Resource_Relationship_Type_Code\":\"IsPartOf\","
				+ "\"Child_Source_System_Code\":\"Revel\","
				+ "\"Sequence_Number\":0.0,"
				+ "\"Child_Source_System_Record_Id\":\""+chapterId+"\","
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:25.497Z\"}");
		learningResourceRelationshipBookToChapterMsg.send(1,1);
		
		Message learningModuleContentChapterMsg =new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:23.152Z\","
				+ "\"Message_Type_Code\":\"Learning_Module_Content\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Learning_Module_Source_System_Code\":\"Revel\","
				+ "\"Learning_Resource_Source_System_Code\":\"EPS\","
				+ "\"Course_Section_Source_System_Code\":\"Registrar\","
				+ "\"Creation_Datetime\":\"2015-06-10T11:33:23.152Z\","
				+ "\"Learning_Module_Source_System_Record_Id\":\""+assignmentId+"\","
				+ "\"Learning_Resource_Source_System_Record_Id\":\""+chapterId+"\","
				+ "\"Course_Section_Source_System_Record_Id\":\""+courseSectionId+"\","
				+ "\"Sequence_Number\":0.0,"
				+ "\"Ref_Relationship_Context_Code\":\"Book\","
				+ "\"Learning_Resource_Relationship_Context_Id\":\""+bookId+"\","
				+ "\"Possible_Points\":0.0,\"Assignment_Due_Datetime\":\"2015-06-20T11:33:20.934-0600\","
				+ "\"Assignment_Max_Points\":18.0,"
				+ "\"Scored_Flag\":false,"
				+ "\"Add_To_Grade_Book_Flag\":false,"
				+ "\"Include_Points_In_Student_Grade_Flag\":false,"
				+ "\"Ref_Learning_Resource_Type_Code\":\"Chapter\","
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:26.083Z\"}");
		learningModuleContentChapterMsg.send(1,1);
		
		Message learningModuleContentAssesmentMsg = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:23.153Z\","
				+ "\"Message_Type_Code\":\"Learning_Module_Content\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Learning_Module_Source_System_Code\":\"Revel\","
				+ "\"Learning_Resource_Source_System_Code\":\"EPS\","
				+ "\"Course_Section_Source_System_Code\":\"Registrar\","
				+ "\"Creation_Datetime\":\"2015-06-10T11:33:23.153Z\","
				+ "\"Learning_Module_Source_System_Record_Id\":\""+assignmentId+"\","
				+ "\"Learning_Resource_Source_System_Record_Id\":\""+assessmentLRId+"\","
				+ "\"Course_Section_Source_System_Record_Id\":\""+courseSectionId+"\","
				+ "\"Sequence_Number\":1.0,"
				+ "\"Ref_Relationship_Context_Code\":\"Book\","
				+ "\"Learning_Resource_Relationship_Context_Id\":\""+bookId+"\","
				+ "\"Possible_Points\":9.0,"
				+ "\"Assignment_Due_Datetime\":\"2015-06-20T11:33:20.934-0600\","
				+ "\"Assignment_Max_Points\":18.0,"
				+ "\"Scored_Flag\":true,"
				+ "\"Add_To_Grade_Book_Flag\":true,"
				+ "\"Include_Points_In_Student_Grade_Flag\":true,"
				+ "\"Ref_Learning_Resource_Type_Code\":\"Assessment\","
				+ "\"Assessment_Source_System_Code\":\"PAF\","
				+ "\"Assessment_Source_System_Record_Id\":\""+chapterQuizId+"\","
				+ "\"Ref_Learning_Resource_Subtype_Code\":\"ChapterTest\","
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:27.833Z\"}");
		learningModuleContentAssesmentMsg.send(1,1);
	
		//send Multi_Value_Question_User_Answered message(s)
		String randomUUID = UUID.randomUUID().toString();
		String messageId = "SQE-msg-" + randomUUID;
		Message multiValueQuestionUserAnsweredMessage = new AdHocSeerMessage("{\"Transaction_Datetime\":\"2015-06-10T11:33:39.433Z\","
				+ "\"Message_Type_Code\":\"Multi_Value_Question_User_Answered\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Message_Transfer_Type\":\"LiveStream\","
				+ "\"id\":\""+messageId+"\","
				+ "\"actor\":{\"objectType\":\"Agent\",\"account\":{\"homePage\":\"https://piapi.openclass.com\",\"Person_Source_System_Record_Id\":\""+studentId+"\"}},"
				+ "\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/answered\",\"display\":{\"language\":\"en-US\",\"verb\":\"answered\"}},"
				+ "\"object\":{\"objectType\":\"Activity\","
					+ "\"id\":\"SQE-ObjectId-d166af8b-3fda-455e-8243-5933c044e784\","
					+ "\"definition\":{\"type\":\"http://adlnet.gov/expapi/activities/question\"}},"
				+ "\"context\":{\"extensions\":{\"URL_Prefix_For_Context_Fields\":\"http://schema.pearson.com/daalt/\","
				+ "\"appId\":\"DAALT-SQE\","
				+ "\"Course_Section_Source_System_Record_Id\":\""+courseSectionId+"\","
				+ "\"Course_Section_Source_System_Code\":\"Registrar\","
				+ "\"Person_Role_Code\":\"Student\","
				+ "\"Person_Source_System_Code\":\"PI\","
				+ "\"Assessment_Source_System_Record_Id\":\""+chapterQuizId+"\","
				+ "\"Assessment_Source_System_Code\":\"PAF\","
				+ "\"Assessment_Item_Source_System_Record_Id\":\""+assessmentItemId01+"\","
				+ "\"Assessment_Item_Source_System_Code\":\"PAF\","
				+ "\"Assessment_Item_Question_Type\":\"MultiValue\","
				+ "\"User_Work_Type\":\"Credit\","
				+ "\"Assessment_Item_Response_Code\":\"Correct\","
				+ "\"Attempt_Number\":1.0,"
				+ "\"Student_Response\":[{\"Target_Id\":\""+assessmentItemTargetId01+"\","
				+ "\"Answer_Id\":\""+assessmentItemAns01Id01+"\","
				+ "\"Target_Sub_Question_Response_Code\":\"Correct\"}],"
				+ "\"Client_Side_Accessed_Timestamp\":\"2015-06-10T11:33:39.434Z\","
				+ "\"Client_Side_Answered_Timestamp\":\"2015-06-10T11:33:39.434Z\","
				+ "\"Item_Response_Score\":3.0}},"
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:39.441Z\"}");
		multiValueQuestionUserAnsweredMessage.send(1,1);
		
		//send Time_On_Task_User_Unloads_Question message(s)
		randomUUID = UUID.randomUUID().toString();
		messageId = "SQE-msg-" + randomUUID;
		String randomUUID02 = UUID.randomUUID().toString();
		String totId = "SQE-ToT-" + randomUUID02;
		Message timeOnTaskUserUnloadsQuestion = new AdHocSeerMessage("{\"Transaction_Datetime\":\"2015-06-10T11:33:39.436Z\","
				+ "\"Message_Type_Code\":\"Time_On_Task_User_Unloads_Question\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Message_Transfer_Type\":\"LiveStream\","
				+ "\"id\":\""+messageId+"\","
				+ "\"actor\":{\"objectType\":\"Agent\",\"account\":{\"homePage\":\"https://piapi.openclass.com\","
					+ "\"Person_Source_System_Record_Id\":\""+studentId+"\"}},"
				+ "\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/answered\","
				+ "\"display\":{\"language\":\"en-US\",\"verb\":\"answered\"}},"
				+ "\"object\":{\"objectType\":\"Activity\","
				+ "\"id\":\"SQE-Tincan-Obj-614e9bf6-f52f-414c-9a8f-8eea37a72447\","
					+ "\"definition\":{\"type\":\"http://adlnet.gov/expapi/activities/question\"}},"
						+ "\"context\":{\"extensions\":{\"URL_Prefix_For_Context_Fields\":\"http://schema.pearson.com/daalt\","
						+ "\"appId\":\"DAALT-SQE\","
						+ "\"User_Activity_Causing_Unload\":\"User_Answers_Question\","
						+ "\"Course_Section_Source_System_Record_Id\":\""+courseSectionId+"\","
						+ "\"Course_Section_Source_System_Code\":\"Registrar\","
						+ "\"Person_Role_Code\":\"Student\","
						+ "\"Person_Source_System_Code\":\"PI\","
						+ "\"Assessment_Source_System_Record_Id\":\""+chapterQuizId+"\","
						+ "\"Assessment_Source_System_Code\":\"PAF\","
						+ "\"Assessment_Item_Source_System_Record_Id\":\""+assessmentItemId01+"\","
						+ "\"Assessment_Item_Source_System_Code\":\"PAF\","
						+ "\"Assessment_Item_Question_Type\":\"MultiValue\","
						+ "\"User_Work_Type\":\"Credit\","
						+ "\"Time_On_Task_UUID\":\""+totId+"\","
						+ "\"Duration_In_Seconds\":10.0,"
						+ "\"Client_Side_Load_Timestamp\":\"2015-06-10T11:33:39.438Z\","
						+ "\"Client_Side_Unload_Timestamp\":\"2015-06-10T11:33:39.438Z\","
						+ "\"Attempt_Number\":1.0}},"
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:40.525Z\"}");
		timeOnTaskUserUnloadsQuestion.send(1,1);
		
		//send Assessment_Item_Completion message(s)
		Message assessmentItemCompletionMessage = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:39.438Z\","
				+ "\"Message_Type_Code\":\"Assessment_Item_Completion\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Course_Section_Source_System_Code\":\"Registrar\","
				+ "\"Course_Section_Source_System_Record_Id\":\""+courseSectionId+"\","
				+ "\"Person_Role_Code\":\"Student\","
				+ "\"Person_Source_System_Code\":\"PI\","
				+ "\"Person_Source_System_Record_Id\":\""+studentId+"\","
				+ "\"Assessment_Source_System_Code\":\"PAF\","
				+ "\"Assessment_Source_System_Record_Id\":\""+chapterQuizId+"\","
				+ "\"Assessment_Item_Source_System_Code\":\"PAF\","
				+ "\"Assessment_Item_Source_System_Record_Id\":\""+assessmentItemId01+"\","
				+ "\"Assessment_Item_Completion_Code\":\"Complete\","
				+ "\"Assessment_Item_Completion_Datetime\":\"2015-06-10T11:33:39.439Z\","
				+ "\"Ref_Assessment_Item_Completion_Source_Code\":\"Student\","
				+ "\"Possible_Points\":3.0,"
				+ "\"currentTimeFormatted\":\"2015-06-10T11:33:41.611Z\"}");
		assessmentItemCompletionMessage.send(1,1);
		
		//send Assessment_Performance message
		Message assessmentPerformanceMessage = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Create\","
				+ "\"Transaction_Datetime\":\"2015-06-10T11:33:49.283Z\","
				+ "\"Message_Type_Code\":\"Assessment_Performance\","
				+ "\"Originating_System_Code\":\"DAALTTest\","
				+ "\"Message_Version\":\"2.0\","
				+ "\"Student_Source_System_Code\":\"PI\","
				+ "\"Student_Source_System_Record_Id\":\""+studentId+"\","
				+ "\"Course_Section_Source_System_Code\":\"Registrar\","
				+ "\"Course_Section_Source_System_Record_Id\":\""+courseSectionId+"\","
				+ "\"Course_Points_Possible\":18.0,"
				+ "\"Course_Raw_Score\":9.0,"
				+ "\"Assignment_Source_System_Code\":\"Revel\","
				+ "\"Assignment_Source_System_Record_Id\":\""+assignmentId+"\","
				+ "\"Assignment_Points_Possible\":18.0,"
				+ "\"Assignment_Raw_Score\":9.0,"
				+ "\"Assignment_Completed\":false,"
				+ "\"Assignment_Completion_Datetime\":\"2015-06-10T11:33:49.283Z\","
				+ "\"Ref_Assessment_Completion_Source_Code\":\"Student\","
				+ "\"Assessment_Source_System_Code\":\"PAF\","
				+ "\"Assessment_Source_System_Record_Id\":\""+chapterQuizId+"\","
				+ "\"Assessment_Type_Code\":\"Quiz\","
				+ "\"Assessment_Raw_Score\":9.0,"
				+ "\"Points_Earned_Original_Score\":9.0,"
				+ "\"Include_Points_In_Student_Grade_Flag\":true,"
				+ "\"Assessment_Possible_Points\":9.0,"
				+ "\"Assessment_Completion_Datetime\":\"2015-06-10T11:33:49.283Z \","
				+ "\"Score_Overridden_Flag\":false,"
				+ "\"Assessment_Items\":[{\"Assessment_Item_Source_System_Code\":\"PAF\","
						+ "\"Assessment_Item_Source_System_Record_Id\":\""+assessmentItemId01+"\","
						+ "\"Assessment_Item_Question_Type\":\"MultiValue\","
						+ "\"Correct_Indicator\":true,"
						+ "\"Assessment_Item_Question_Presentation_Format\":\"RadioButton\","
						+ "\"Attempts\":1,"
						+ "\"Possible_Score\":3.0,"
						+ "\"Score\":3.0}],"
						+ "\"currentTimeFormatted\":\"2015-06-10T11:33:49.293Z\"}");
		assessmentPerformanceMessage.send(1,1);
		
		//send Learning_Module message with Transaction_Type_Code = Update" & Include_In_Trending_Flag = true
//		Message learningModuleMessageUpdate = new AdHocSubPubMessage("{\"Transaction_Type_Code\":\"Update\","
//				+ "\"Transaction_Datetime\":\"2015-06-10T11:34:00.449Z\","
//				+ "\"Message_Type_Code\":\"Learning_Module\","
//				+ "\"Originating_System_Code\":\"DAALTTest\","
//				+ "\"Message_Version\":\"2.0\","
//				+ "\"Creation_Datetime\":\"2015-06-10T11:34:00.449Z\","
//				+ "\"Identifying_Code\":\"2015-06-10T11:34:00.449Z\","
//				+ "\"Learning_Module_Source_System_Code\":\"Revel\","
//				+ "\"Learning_Module_Source_System_Record_Id\":\""+assignmentId+"\","
//				+ "\"Course_Section_Source_System_Code\":\"Registrar\","
//				+ "\"Course_Section_Source_System_Record_Id\":\""+courseSectionId+"\","
//				+ "\"Instructor_Source_System_Code\":\"PI\","
//				+ "\"Instructor_Source_System_Record_Id\":\""+instructorId+"\","
//				+ "\"Sequence_Number\":1.43482159E12,"
//				+ "\"Title\":\"DAALT SQE Test Assignment\","
//				+ "\"Include_In_Trending_Flag\":true,"
//				+ "\"Activity_Due_Datetime\":\"2015-06-20T11:33:20.934-0600\","
//				+ "\"Release_Datetime\":\"2015-06-10T11:34:00.449Z\","
//				+ "\"Possible_Points\":18.0,"
//				+ "\"Ref_Learning_Module_Type_Code\":\"Assignment\","
//				+ "\"Description\":\"This module covers the main schools of psychology\","
//				+ "\"Learning_Module_Version\":\"1.2\","
//				+ "\"Maximum_Time_Allowed\":10.0,"
//				+ "\"Ref_Time_Units_Code\":\"Hour\","
//				+ "\"Weight\":20.0,"
//				+ "\"Ref_Language_Code\":\"en-US\","
//				+ "\"currentTimeFormatted\":\"2015-06-10T11:34:00.450Z\"}");
//		learningModuleMessageUpdate.send(0,1);
		
		//build validations for 3.4
		List<Validation> validationList = new ArrayList<>();
		StringBuilder route = new StringBuilder();
		
		route = new StringBuilder();
		AssessmentObject assessment = new AssessmentObject();
		assessment.assessmentId = chapterQuizId;
		assessment.assessmentType = "Quiz";
		assessment.assessmentSeeded = Boolean.TRUE;
		assessment.assessmentLastSeedDateTime = "2015-06-10T11:33:20.929Z";
		assessment.assessmentLastSeedType = "Create";		
		
		List<AssessmentItemObject> assessmentItems = new ArrayList<>();
		for(int i=0; i<1; i++){
			AssessmentItemObject assessmentItem = new AssessmentItemObject();
			assessmentItem.questionType = "MultiValue";
			assessmentItem.questionPresentationFormat = "RadioButton";
			assessmentItem.questionText = "my text";
			assessmentItem.itemLastSeedDateTime = "2015-06-10T11:33:20.929Z";
			assessmentItem.itemLastSeedType = "Create";
			assessmentItem.itemId = "who cares?";
			assessmentItem.itemSequence = 0f;
			assessmentItem.itemSeeded = Boolean.TRUE;
			assessmentItems.add(assessmentItem);
		}
		assessment.assessmentItems = new AssessmentItemObject[assessmentItems.size()];
		int i = 0;
		for(AssessmentItemObject assessmentItemObj : assessmentItems){
			assessment.assessmentItems[i] = assessmentItemObj;
			i++;
		}
		route.append(baseUrl).append("/validate").append("/assessments/").append(chapterQuizId);
		validationList.add(new AssessmentsAll("AdHocScenario", instructorUser, 
				route.toString(), /*expectedResponseCode*/ ResponseCode.OK.value, assessment));
		
		
		//build validations for 1.9
	
		LearningResourceObject[] resourceArray =  new LearningResourceObject[1];
		LearningResourceObject learningReosurce =new LearningResourceObject();
		resourceArray[0] = learningReosurce;
		
		resourceArray[0].platformId =  platformId;
		
		resourceArray[0].learningResourceId = assessmentLRId;
		resourceArray[0].learningResourceSequence = 4.0f;
		resourceArray[0].learningResourceTitle = "Quiz Title";
		resourceArray[0].learningResourceType = "Assessment";
		resourceArray[0].learningResourceSubType = "Quiz";
		resourceArray[0].hasChildrenFlag = Boolean.TRUE;
		resourceArray[0].courseSectionStudentCount = 1;
		resourceArray[0].completedStudentCount = 0;
		resourceArray[0].incompleteStudentCount = 0;
		resourceArray[0].pointsPossible =  9.0f;
		resourceArray[0].classTotalPoints = 70.00f;
		resourceArray[0].classAvgPoints = 50.55f;
		resourceArray[0].classAvgPercent = 80.00f;
	
		resourceArray[0].practicePointsPossible = null;
		resourceArray[0].classTotalPracticePoints = null;
		resourceArray[0].classAvgPracticePoints = null;
	
		
	
		resourceArray[0].totalTimeSpentAssessment ="00:00:54.0";
		resourceArray[0].totalTimeSpentLearning = "00:00:54.0";
		
		resourceArray[0].avgTimeSpentAssessment = "00:00:00.0";
		resourceArray[0].avgTimeSpentLearning = "00:00:00.0";
		resourceArray[0].avgTimeSpentTotal = "00:00:00.0";
		resourceArray[0].timeSpentTotal = "00:00:54.0";
		
		resourceArray[0].totalChildTimeSpentAssessment ="00:00:00.0";
		resourceArray[0].totalChildTimeSpentLearning = "00:00:00.0";

		
		//section/module/chapter/resourcesUnderChapter
		route = new StringBuilder();
		
		route.append(baseUrl).append("/platforms/").append(platformId)
		.append("/sections/").append(courseSectionId)
		.append("/modules/").append(assignmentId)
		.append("/resources/").append(chapterId)
		.append("/resources");
		
		validationList.add(new SectionToModuleToResourcesAll("AdHocScenario", instructorUser, 
				route.toString(), ResponseCode.OK.value, resourceArray, /*expectedEnvelopeLevelLinks*/ null, 
				/*offset*/ null, /*limit*/ null, /*itemCount*/ resourceArray.length));
		
		//build validations for 1.11
		LearningResourceItemObject[] itemArray =  new LearningResourceItemObject[1];
		LearningResourceItemObject item =new LearningResourceItemObject();
		itemArray[0] = item;
	
		
		item.itemId = assessmentItemId01;
		item.platformId = platformId;
		item.courseSectionId = courseSectionId;
		item.learningModuleId = assignmentId;
		item.learningResourceId = assessmentLRId;
		item.assessmentId = chapterQuizId;
		item.courseSectionStudentCount =2;
		item.assessmentItemCompletedStudentCount = 4;
		item.correctStudentCount = 2;
		item.correctStudentPercent = 25.0f;
		item.incorrectStudentCount = 2;
		item.noAttemptStudentCount = 2;
		item.totalItemResponseScore = 32.0f;
		item.avgItemResponseScore = 0;
		item.totalTimeSpentAssessing = "00:00:20";		
		item.avgTimeSpentAssessing = "00:00:30";
		item.medianTimeSpentAssessing = "00:00:10";
		
		item.attempts = new AttemptObject[1];
		AttemptObject itemAttempt = new AttemptObject();
		item.attempts[0] = itemAttempt;
		
		itemAttempt.attemptNumber =1;
		itemAttempt.attemptNumberStudentCount = 1;
		itemAttempt.targetSubQuestions = new TargetSubQuestionObject[1];
	
		 	
		TargetSubQuestionObject targetSubQObj = new TargetSubQuestionObject();
		targetSubQObj.targetSubQuestionId = assessmentItemTargetId01;
		targetSubQObj.targetSubQuestionText = "Sub Question";
		targetSubQObj.targetSubQuestionResponses = new TargetSubQuestionResponseObject[2];
			
		targetSubQObj.targetSubQuestionResponses[0] = new TargetSubQuestionResponseObject();
		targetSubQObj.targetSubQuestionResponses[0].targetSubQuestionResponseCode = "Correct";
		targetSubQObj.targetSubQuestionResponses[0].targetSubQuestionResponseStudentCount = 1;
		targetSubQObj.targetSubQuestionResponses[0].targetSubQuestionResponsePercent = 100;
		targetSubQObj.targetSubQuestionResponses[1] = new TargetSubQuestionResponseObject();
		targetSubQObj.targetSubQuestionResponses[1].targetSubQuestionResponseCode = "Incorrect";
		targetSubQObj.targetSubQuestionResponses[1].targetSubQuestionResponseStudentCount = 0;
		targetSubQObj.targetSubQuestionResponses[1].targetSubQuestionResponsePercent = 0;
	
		targetSubQObj.targetSubQuestionAnswers= new TargetSubQuestionAnswerObject[1];
		
		TargetSubQuestionAnswerObject targetSubQuestionAnswer = new TargetSubQuestionAnswerObject();
		targetSubQObj.targetSubQuestionAnswers[0] = targetSubQuestionAnswer;
		targetSubQObj.targetSubQuestionAnswers[0].targetSubQuestionAnswerId = assessmentItemAns01Id01;
		targetSubQObj.targetSubQuestionAnswers[0].targetSubQuestionAnswerText = "target sub question answer text";
		targetSubQObj.targetSubQuestionAnswers[0].targetSubQuestionAnswerCorrectFlag = true;
		targetSubQObj.targetSubQuestionAnswers[0].targetSubQuestionAnswerResponses = new TargetSubQuestionAnswerResponseObject[2];
		
		targetSubQObj.targetSubQuestionAnswers[0].targetSubQuestionAnswerResponses[0] = new TargetSubQuestionAnswerResponseObject();
		targetSubQObj.targetSubQuestionAnswers[0].targetSubQuestionAnswerResponses[0].targetSubQuestionAnswerResponseCode = "Correct";
		targetSubQObj.targetSubQuestionAnswers[0].targetSubQuestionAnswerResponses[0].targetSubQuestionAnswerResponseStudentCount = 1;
		targetSubQObj.targetSubQuestionAnswers[0].targetSubQuestionAnswerResponses[0].targetSubQuestionAnswerResponsePercent = 100;
		
		targetSubQObj.targetSubQuestionAnswers[0].targetSubQuestionAnswerResponses[1] = new TargetSubQuestionAnswerResponseObject();
		targetSubQObj.targetSubQuestionAnswers[0].targetSubQuestionAnswerResponses[1].targetSubQuestionAnswerResponseCode = "Incorrect";
		targetSubQObj.targetSubQuestionAnswers[0].targetSubQuestionAnswerResponses[1].targetSubQuestionAnswerResponseStudentCount = 0;
		targetSubQObj.targetSubQuestionAnswers[0].targetSubQuestionAnswerResponses[1].targetSubQuestionAnswerResponsePercent = 0;
	
		itemAttempt.targetSubQuestions[0] = targetSubQObj;

		ResponseObject  currentResponse = new ResponseObject();
		currentResponse.responseCode = "CORRECT";
		currentResponse.responseStudentCount=0;
		currentResponse.responsePercent = 0;

		itemAttempt.responses = new ResponseObject[1];
		itemAttempt.responses[0] = currentResponse;
		
		//direct call : chapterQuiz
		
		route = new StringBuilder();
		route.append(baseUrl).append("/platforms/").append(platformId)
			.append("/sections/").append(courseSectionId).append("/modules/").append(assignmentId)
			.append("/resources/").append(assessmentLRId).append("/items");
		validationList.add(new SectionToModuleToResourceToItemsAll("AdHocScenario", instructorUser, 
				route.toString(),
				ResponseCode.OK.value, itemArray, /*expectedEnvelopeLevelLinks*/ null, 
				/*offset*/ null, /*limit*/ null, /*itemCount*/ itemArray.length));
			
			
		// build validations for 1.12
		LearningModuleStudentObject studObj = new LearningModuleStudentObject();
		studObj.platformId = platformId;
		studObj.courseSectionId = courseSectionId;
		studObj.learningModuleId = assignmentId;
		studObj.learningModuleTitle = "Assignment Title";
		studObj.learningModuleSequence = 0.0f;
		studObj.studentId = studentId;
		studObj.studentFirstName = "student first name";
		studObj.studentLastName = "student last name";
		studObj.pointsPossible = 100.00f;
		studObj.studentPoints = 90.00f;
		studObj.studentPercent = 70.00f;
		studObj.studentPracticePoints = 80.00f;
		studObj.practicePointsPossible = 100.00f;
		studObj.studentTrending = 25.0f;
		studObj.timeSpentAssessment = "00:00:20";
		studObj.timeSpentLearning = "00:00:20";
		studObj.timeSpentTotal = "00:00:40";
		LearningModuleStudentObject[] studentArray =  new LearningModuleStudentObject[1];
		studentArray[0] = studObj;
		route = new StringBuilder();
		route.append(baseUrl).append("/platforms/").append(platformId)
			.append("/sections/").append(courseSectionId)
			.append("/modules/").append(assignmentId)
			.append("/students");
		validationList.add(new SectionToModuleToStudentsAll("AdHocScenario", instructorUser, 
				route.toString(), /*expectedResponseCode*/ResponseCode.OK.value, studentArray,
				/*expectedEnvelopeLevelLinks*/ null, 
				/*offset*/ null, /*limit*/ null, /*itemCount*/ studentArray.length));
		
		// build validations for 2.3
		StudentModuleObject moduleObj = new StudentModuleObject();
		StudentModuleObject[] moduleArray =  new StudentModuleObject[1];
		moduleArray[0] = moduleObj;
		moduleObj.platformId = platformId;
		moduleObj.courseSectionId = courseSectionId;
		moduleObj.studentId = studentId;
		moduleObj.learningModuleId = assignmentId;
		moduleObj.studentFirstName = "first name";
		moduleObj.studentLastName = "FamilyName";
		moduleObj.learningModuleTitle = "Module Title";
		moduleObj.learningModuleSequence = 0.00f;
		moduleObj.pointsPossible = 100.00f;
		moduleObj.studentPoints = 80.00f;
		moduleObj.studentPercent = 65.00f;
		moduleObj.practicePointsPossible = 80.00f;
		moduleObj.studentPracticePoints = 60.00f;
		moduleObj.timeSpentAssessment = "00:00:20";
		moduleObj.timeSpentLearning = "00:00:20";
		moduleObj.timeSpentTotal = "00:00:40";
		
	
		route = new StringBuilder();
		route.append(baseUrl).append("/platforms/").append(platformId)
		.append("/sections/").append(courseSectionId)
		.append("/students/").append(studentId)
		.append("/modules");
		validationList.add(new SectionToStudentToModulesAll("AdHocScenario", instructorUser, 
				route.toString(),/*expectedResponseCode*/ResponseCode.OK.value, moduleArray,
				/*expectedItemLevelLinks*/ null, 
				/*offset*/ null, /*limit*/ null, /*itemCount*/ moduleArray.length));
		// build validations for 2.7
		
		StudentLearningResourceObject resourceObj = new StudentLearningResourceObject();
		StudentLearningResourceObject[] studentModuleArray =  new StudentLearningResourceObject[1];
		studentModuleArray[0] = resourceObj;
		
		
		resourceObj.platformId = platformId;
		resourceObj.courseSectionId = courseSectionId;
		resourceObj.learningModuleId = assignmentId;
		resourceObj.learningResourceId = assessmentLRId;
		resourceObj.learningResourceSequence = 0.00f;
		resourceObj.learningResourceTitle = "Chapter Quiz";
		resourceObj.learningResourceType = "ChapterQuiz";
		resourceObj.learningResourceSubType = "Quiz";
		resourceObj.practicePointsPossible = 90.00f;
		resourceObj.studentPracticePoints = 80.00f;
		resourceObj.pointsPossible = 100.00f;
		resourceObj.studentPoints = 65.00f;
		resourceObj.studentPercent = 70.00f;
		studObj.studentPracticePoints = 70.00f;
		studObj.practicePointsPossible = 100.00f;
		studObj.studentTrending = 3.00f;
		studObj.timeSpentAssessment = "00:00:20";
		studObj.timeSpentLearning = "00:00:20";
		studObj.timeSpentTotal = "00:00:40";
		resourceObj.includesAdjustedPoints = true;
		resourceObj.hasChildrenFlag = true;
		resourceObj.studentId =studentId;
		resourceObj.studentFirstName ="Given name";
		resourceObj.studentLastName = "Last Name";
		
		resourceObj.totalTimeSpentAssessment ="00.30.00";
		resourceObj.totalTimeSpentLearning ="00.40.00";
		resourceObj.timeSpentTotal = "00.70.00";
		resourceObj.totalChildTimeSpentAssessment = "00.40.00";
		resourceObj.totalChildTimeSpentLearning = "00.40.00";
		
		//section/thisStudent/module/chapter/chapterQuiz/resourcesUnderChapterQuiz
		
		route = new StringBuilder();
		route.append(baseUrl).append("/platforms/").append(platformId)
			.append("/sections/").append(courseSectionId)
			.append("/students/").append(studentId)
			.append("/modules/").append(assignmentId)
			.append("/resources/").append(chapterId)
			.append("/resources");
		
		validationList.add(new SectionToStudentToModuleToResourcesAll("AdHocScenario", instructorUser, 
					route.toString(), /*expectedResponseCode*/ ResponseCode.OK.value, studentModuleArray,
					/*expectedItemLevelLinks*/ null, 
					/*offset*/ null, /*limit*/ null, /*itemCount*/ studentModuleArray.length));
			
		for (Validation val : validationList) {
			getCurrentTestCase().getValidations().add(val);
			if (Boolean.parseBoolean(getConfig().getValue(TestEngine.printExpectedOutputPropName))) {
				System.out.println(val.getExpectedResultsPrintString());
			}
			
			
		}
	}
}
