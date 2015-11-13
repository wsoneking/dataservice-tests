package com.pearson.test.daalt.dataservice.request.message.version01;

import java.util.ArrayList;
import java.util.List;

import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;

public class BrixAssessmentItemTypeMessage extends SubPubMessage {
	//brix.assessment-item-type.create
	
	public BrixAssessmentItemTypeMessage() {
		payload = new BrixAssessmentItemTypeMsgPayload();
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		BrixAssessmentItemTypeMsgPayload specificPayload = (BrixAssessmentItemTypeMsgPayload) payload;
		switch(propertyName) {
			case "Quiz" :
				Quiz quiz = (Quiz) propertyValue;
				specificPayload.Assessment_Items = new ArrayList<>();
				for (Question question : quiz.getQuestions()) {
					BrixAssessmentItemTypeMsgPayload.AssessmentItem assessmentItem = specificPayload.new AssessmentItem();
					assessmentItem.Assessment_Item_Source_System_Record_Id = question.getId();
					assessmentItem.Assessment_Item_Type_Code = "MultipleChoice_4";
					assessmentItem.Answers = new ArrayList<>();
					for(com.pearson.test.daalt.dataservice.model.Answer ans : question.getAnswers()){
						BrixAssessmentItemTypeMsgPayload.AssessmentItem.Answer answer = assessmentItem.new Answer();
						answer.Answer_Source_System_Record_Id = ans.getId();
						assessmentItem.Answers.add(answer);
					}
					specificPayload.Assessment_Items.add(assessmentItem);
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
		if (payload.Transaction_Datetime == null) {
			return "MessagePayload.Transaction_Datetime";
		}
		BrixAssessmentItemTypeMsgPayload specificPayload = (BrixAssessmentItemTypeMsgPayload) payload;
		if(specificPayload.Message_Type_Code == null){
			return "BrixAssessmentItemTypeMsgPayload.Message_Type_Code";
		}
		if(specificPayload.Message_Version == null){
			return "BrixAssessmentItemTypeMsgPayload.Message_Version";
		}
		if(specificPayload.Answer_Source_System_Code == null){
			return "BrixAssessmentItemTypeMsgPayload.Answer_Source_System_Code";
		}
		if(specificPayload.Assessment_Items == null){
			return "BrixAssessmentItemTypeMsgPayload.Assessment_Items";
		}
		for (com.pearson.test.daalt.dataservice.request.message.version01.BrixAssessmentItemTypeMessage.BrixAssessmentItemTypeMsgPayload.AssessmentItem item : specificPayload.Assessment_Items) {
			if(item.Assessment_Item_Source_System_Code == null){
				return "BrixAssessmentItemTypeMsgPayload.Assessment_Items.Assessment_Item_Source_System_Code";
			}
			if(item.Assessment_Item_Source_System_Record_Id == null){
				return "BrixAssessmentItemTypeMsgPayload.Assessment_Items.Assessment_Item_Source_System_Record_Id";
			}
			if(item.Answers == null){
				return "BrixAssessmentItemTypeMsgPayload.Assessment_Items.Answers";
			}
			for (com.pearson.test.daalt.dataservice.request.message.version01.BrixAssessmentItemTypeMessage.BrixAssessmentItemTypeMsgPayload.AssessmentItem.Answer ans : item.Answers) {
				if(ans.Answer_Source_System_Record_Id == null){
					return "BrixAssessmentItemTypeMsgPayload.Assessment_Items.Answer.Answer_Source_System_Record_Id";
				}
			}
		}
		return null;
	}
	
	public class BrixAssessmentItemTypeMsgPayload extends MessagePayload {
		//required : hard code
		public String Message_Type_Code = "Assessment_Item_Type"; 
		//required : hard code
		public String Message_Version = "1.0"; 
		//required : hard code 
		public String Answer_Source_System_Code = "Brix"; 
		//required : caller sets value
		public List<AssessmentItem> Assessment_Items; 
		
		public class AssessmentItem {
			//required : caller sets value
			public String Assessment_Item_Source_System_Record_Id;
			//required : hard code - defined when setting  
			public String Assessment_Item_Source_System_Code = "PAF";
			//required : default value - introduce override to allow for new item types
			public String Assessment_Item_Type_Code;
			//required : caller sets value
			public List<Answer> Answers;
			
			public class Answer {
				//required : caller sets value
				public String Answer_Source_System_Record_Id;
			}
		}
	}
}
