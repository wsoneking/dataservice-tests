package com.pearson.test.daalt.dataservice.request.message;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONValue;

import com.google.gson.JsonObject;
import com.pearson.test.daalt.dataservice.model.AnswerDefinitionObject;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionDefinitionObject;
public class AssessmentItemPossibleAnswers extends SubPubMessage {

	public AssessmentItemPossibleAnswers(String type) {
		switch(type) {
		case "MultiValue":
			payload = new AssessmentItemPossibleAnswersMultiValueMsgPayload();
			break;
		case "FillInTheBlank": 
			payload = new AssessmentItemPossibleAnswersFillInOrNumericMsgPayload();
			break;
		case "Numeric": 
			payload = new AssessmentItemPossibleAnswersFillInOrNumericMsgPayload();
			break;
		case "UnknownFormat":
			payload = new AssessmentItemPossibleAnswersUnknownMsgPayload();
			break;
		case "General":
			payload = new AssessmentItemPossibleAnswersMsgPayload();
			break;
		}
		
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException {
		AssessmentItemPossibleAnswersMsgPayload specificPayload = (AssessmentItemPossibleAnswersMsgPayload) payload;
		AssessmentItemPossibleAnswersMultiValueMsgPayload multiPayload = null;
		AssessmentItemPossibleAnswersUnknownMsgPayload unknownPayload = null;
		AssessmentItemPossibleAnswersFillInOrNumericMsgPayload fillInAndNumericPayload = null;
		if (payload instanceof AssessmentItemPossibleAnswersMultiValueMsgPayload ){
			multiPayload = (AssessmentItemPossibleAnswersMultiValueMsgPayload) payload;
		} else if (payload instanceof AssessmentItemPossibleAnswersUnknownMsgPayload ){
			unknownPayload = (AssessmentItemPossibleAnswersUnknownMsgPayload) payload;
		} else if (payload instanceof AssessmentItemPossibleAnswersFillInOrNumericMsgPayload) {
			fillInAndNumericPayload = (AssessmentItemPossibleAnswersFillInOrNumericMsgPayload) payload;
		}
		
		switch(propertyName) {
		case "Question" :
			Question question = (Question) propertyValue;
			if(question.getQuestionLastSeedDateTime() != null){
				specificPayload.Transaction_Datetime = question.getQuestionLastSeedDateTime();
			}
			specificPayload.Assessment_Item_Source_System_Record_Id = question.getId();
			specificPayload.Question_Text = question.getText();
			specificPayload.Assessment_Item_Question_Type = question.getQuestionType();
			specificPayload.Assessment_Item_Question_Presentation_Format = question.getQuestionPresentationFormat();
			
			
			if (payload instanceof AssessmentItemPossibleAnswersMultiValueMsgPayload) {
				multiPayload.Multi_Value_Answer_Data = multiPayload.new MultiValueAnswerData();
				multiPayload.Multi_Value_Answer_Data.Answers = new ArrayList<>();
				for (com.pearson.test.daalt.dataservice.model.Answer ans : question.getAnswers()){
					AssessmentItemPossibleAnswersMultiValueMsgPayload.MultiValueAnswerData.Answer answer = multiPayload.Multi_Value_Answer_Data.new Answer();
					answer.Answer_Id = ans.getId();
					answer.Answer_Text = ans.getText();
					multiPayload.Multi_Value_Answer_Data.Answers.add(answer);
				}
	
				multiPayload.Multi_Value_Answer_Data.Targets = new ArrayList<>();
				for (com.pearson.test.daalt.dataservice.model.SubQuestion que : question.getSubQuestions()) {
					AssessmentItemPossibleAnswersMultiValueMsgPayload.MultiValueAnswerData.Target target = multiPayload.Multi_Value_Answer_Data.new Target();
					target.Target_Id = que.getId(); 
					target.Target_Text = que.getText();
					
					target.Target_Correct_Responses = new ArrayList<>();
					for (com.pearson.test.daalt.dataservice.model.Answer ans : que.getAnswers()){
						if (ans.isCorrectAnswer()){
							AssessmentItemPossibleAnswersMultiValueMsgPayload.MultiValueAnswerData.Target.TargetCorrectResponse response = target.new TargetCorrectResponse();
							response.Target_Correct_Response_Answer_Id = ans.getId();
							target.Target_Correct_Responses.add(response);
						}
					}
					multiPayload.Multi_Value_Answer_Data.Targets.add(target);
				}
			}
			
			if (payload instanceof AssessmentItemPossibleAnswersUnknownMsgPayload) {
				unknownPayload.Question_Definition_Object = question.getQuestionDefinitionObject();
				unknownPayload.Answer_Definition_Object = question.getAnswerDefinitionObject();
			}
			
			if (payload instanceof AssessmentItemPossibleAnswersFillInOrNumericMsgPayload) {
				fillInAndNumericPayload.Multi_Value_Answer_Data = fillInAndNumericPayload.new MultiValueAnswerData();
				fillInAndNumericPayload.Multi_Value_Answer_Data.Answers = new ArrayList<>();
				for (com.pearson.test.daalt.dataservice.model.Answer ans : question.getAnswers()){
					AssessmentItemPossibleAnswersFillInOrNumericMsgPayload.MultiValueAnswerData.Answer answer = fillInAndNumericPayload.Multi_Value_Answer_Data.new Answer();
					answer.Answer_Id = ans.getId();
					answer.Answer_Text = ans.getText();
					fillInAndNumericPayload.Multi_Value_Answer_Data.Answers.add(answer);
				}
			}			
			break;
		case "Transaction_Type_Code" :
			specificPayload.Transaction_Type_Code = propertyValue.toString();
			break;
		default : 
			throw new UnknownPropertyException("Failed to build message due to unknown property : " + propertyName);
		}
	}

	@Override
	public String getMissingCriticalPropertyName() {
		if (payload.Transaction_Type_Code == null) {
			return "MessagePayload.Transaction_Type_Code";
		}
		AssessmentItemPossibleAnswersMsgPayload specificPayload = (AssessmentItemPossibleAnswersMsgPayload) payload;
		if (specificPayload.Message_Type_Code == null) {
			return "AssessmentItemCompletionMsgPayload.Message_Type_Code";
		}
		if (specificPayload.Message_Version == null){
			return "AssessmentItemCompletionMsgPayload.Message_Version";
		}
		if (specificPayload.Originating_System_Code == null){
			return "AssessmentItemCompletionMsgPayload.Originating_System_Code";
		}
		if (specificPayload.Assessment_Item_Source_System_Record_Id == null){
			return "AssessmentItemCompletionMsgPayload.Assessment_Item_Source_System_Record_Id";
		}
		if (specificPayload.Question_Text == null){
			return "AssessmentItemCompletionMsgPayload.Question_Text";
		}
		if (specificPayload.Assessment_Item_Question_Type == null){
			return "AssessmentItemCompletionMsgPayload.Assessment_Item_Question_Type";
		}
		if (specificPayload.Assessment_Item_Question_Presentation_Format == null){
			return "AssessmentItemCompletionMsgPayload.Assessment_Item_Question_Presentation_Format";
		}
		if (specificPayload.Assessment_Item_Source_System_Code == null){
			return "AssessmentItemCompletionMsgPayload.Assessment_Item_Source_System_Code";
		}
		if (payload instanceof AssessmentItemPossibleAnswersMultiValueMsgPayload) {
			AssessmentItemPossibleAnswersMultiValueMsgPayload specificPayload2 = (AssessmentItemPossibleAnswersMultiValueMsgPayload) payload;
			if (specificPayload2.Multi_Value_Answer_Data == null){
				return "AssessmentItemCompletionMsgPayload.multiValueAnswerData";
			}
			if (specificPayload2.Multi_Value_Answer_Data.Answers == null){
				return "AssessmentItemCompletionMsgPayload.multiValueAnswerData.answers";
			}
			if (specificPayload2.Multi_Value_Answer_Data.Targets == null){
				return "AssessmentItemCompletionMsgPayload.multiValueAnswerData.targets";
			}
		}
		if (payload instanceof AssessmentItemPossibleAnswersFillInOrNumericMsgPayload) {
			AssessmentItemPossibleAnswersFillInOrNumericMsgPayload specificPayload3 = (AssessmentItemPossibleAnswersFillInOrNumericMsgPayload) payload;
			if (specificPayload3.Multi_Value_Answer_Data == null){
				return "AssessmentItemCompletionMsgPayload.multiValueAnswerData";
			}
			if (specificPayload3.Multi_Value_Answer_Data.Answers == null){
				return "AssessmentItemCompletionMsgPayload.multiValueAnswerData.answers";
			}
		}
		return null;
	}

	
	public class AssessmentItemPossibleAnswersMsgPayload extends MessagePayload{
		
		//required : hard code
		public String Message_Type_Code = "Assessment_Item_Possible_Answers";
		//required : hard code
		public String Originating_System_Code = "Brix";	
		//required : hard code? 
		public String Message_Version = "2.0";
		//public String Message_Version = "2.1";
		//required : hard code
		public String Assessment_Item_Source_System_Code = "PAF"; 
//		//optional:
//		public String Operational_Environment_URI;		// TODO  This value is used primarily for troubleshooting. Remove it?
		//required : caller sets value
		public String Assessment_Item_Source_System_Record_Id;
		//required : caller sets value
		public String Question_Text;
		//required : caller can reset it
		public String Assessment_Item_Question_Type; 
		//optional : caller can reset it
		public String Assessment_Item_Question_Presentation_Format; 
//		//optional:
//		public Linkage Learning_Resource_Linkage = new Linkage();		// not need to use this for Revel 4.0 for December release.
//		
//		public class Linkage {
//			//required:
//			public String Learning_Resource_Source_System_Record_Id;
//			//required:
//			public String Learning_Resource_Source_System_Code;
//		}
		
		
		
		
//		//optional : hard code
//		public String Operational_Environment_URI = "http://schema.pearson.com/daalt/"; 
//		//optional : hard code
//		public String Question_Definition_Object = "UnknownFormat";
//		//optional : hard code
//		public String Answer_Definition_Object = "UnknownFormat";
		
//		optional :not used by data service endpoints version 2.1
//		public LearningResourceLinkage Learning_Resource_Linkage = new LearningResourceLinkage();
//		public class LearningResourceLinkage {
//			public String Learning_Resource_Source_System_Record_Id;
//			public String Learning_Resource_Source_System_Code;
//		}
		
//		optional : not used by data service endpoints version 2.1
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
	}
	
	public class AssessmentItemPossibleAnswersUnknownMsgPayload extends AssessmentItemPossibleAnswersMsgPayload {
		//optional:
		public QuestionDefinitionObject Question_Definition_Object;
		//optional:
		public AnswerDefinitionObject Answer_Definition_Object;
	}
	
	public class AssessmentItemPossibleAnswersFillInOrNumericMsgPayload extends AssessmentItemPossibleAnswersMsgPayload {
		public MultiValueAnswerData Multi_Value_Answer_Data;		// This field should NOT be populated if the question type is an 'UnknownFormat' 
		public class MultiValueAnswerData {
			//required : caller sets value
			public List<Answer> Answers = new ArrayList<Answer>();
			public class Answer {
				//required : caller sets value  
				public String Answer_Id;
				//required : caller sets value
				public String Answer_Text;
			}
		}
	}
	
	public class AssessmentItemPossibleAnswersMultiValueMsgPayload extends AssessmentItemPossibleAnswersMsgPayload {
		// optional: caller set
		public MultiValueAnswerData Multi_Value_Answer_Data;		// This field should NOT be populated if the question type is an 'UnknownFormat' 
		public class MultiValueAnswerData {

			//required : caller sets value
			public List<Answer> Answers = new ArrayList<Answer>();
			public class Answer {
				//required : caller sets value
				public String Answer_Id;
				//required : caller sets value
				public String Answer_Text;
			}

			//optional : caller sets value
			public List<Target> Targets = new ArrayList<Target>();// This field should NOT be populated if the question type is an 'Fill In The Blank' OR 'Numeric'
			public class Target {

				//required : caller sets value
				public String Target_Id;
				//required : caller sets value
				public String Target_Text;
				//required : caller sets value
				public List<TargetCorrectResponse> Target_Correct_Responses = new ArrayList<TargetCorrectResponse>();
				public class TargetCorrectResponse {
					//required : caller sets value
					public String Target_Correct_Response_Answer_Id;
				}
			}
		}
	}
}
