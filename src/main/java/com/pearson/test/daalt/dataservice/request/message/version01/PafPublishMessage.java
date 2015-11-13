package com.pearson.test.daalt.dataservice.request.message.version01;

import java.util.ArrayList;
import java.util.List;

import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;

public class PafPublishMessage extends SubPubMessage {
	//PUBLISH
	
	public PafPublishMessage() {
		payload = new PafPublishMsgPayload();
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		PafPublishMsgPayload specificPayload = (PafPublishMsgPayload) payload;
		switch(propertyName) {
			case "Quiz" :
				Quiz quiz = (Quiz) propertyValue;
				specificPayload.Assessment_Source_System_Record_Id = quiz.getId();
				if(quiz.getAssessmentLastSeedDateTime() != null){
					specificPayload.Transaction_Datetime = quiz.getAssessmentLastSeedDateTime();
				}
				specificPayload.Assessment_Items = new ArrayList<>();
				for (Question question : quiz.getQuestions()) {
					PafPublishMsgPayload.AssessmentItem assessmentItem = specificPayload.new AssessmentItem();
					assessmentItem.Assessment_Item_Source_System_Record_Id = question.getId();
					assessmentItem.Assessment_Item_Source_System_Code= "PAF";
					assessmentItem.Sequence_Number = question.getSequenceNumber();
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
		PafPublishMsgPayload specificPayload = (PafPublishMsgPayload) payload;
		if(specificPayload.Assessment_Source_System_Code == null){
			return "PafPublishMsgPayload.Assessment_Source_System_Code";
		}
		if(specificPayload.Assessment_Source_System_Record_Id == null){
			return "PafPublishMsgPayload.Assessment_Source_System_Record_Id";
		}
		if(specificPayload.Ref_Assessment_Type_Code == null){
			return "PafPublishMsgPayload.Ref_Assessment_Type_Code";
		}
		if(specificPayload.Message_Type_Code == null){
			return "PafPublishMsgPayload.Message_Type_Code";
		}
		if(specificPayload.Message_Version == null){
			return "PafPublishMsgPayload.Message_Version";
		}
		if(specificPayload.Assessment_Items == null){
			return "PafPublishMsgPayload.Assessment_Items";
		}
		for (com.pearson.test.daalt.dataservice.request.message.version01.PafPublishMessage.PafPublishMsgPayload.AssessmentItem item : specificPayload.Assessment_Items) {
			if(item.Assessment_Item_Source_System_Code == null){
				return "PafPublishMsgPayload.Assessment_Items.Assessment_Item_Source_System_Code";
			}
			if(item.Assessment_Item_Source_System_Record_Id == null){
				return "PafPublishMsgPayload.Assessment_Items.Assessment_Item_Source_System_Record_Id";
			}
			if(item.Sequence_Number == null){
				return "PafPublishMsgPayload.Assessment_Items.Sequence_Number";
			}
		}
		return null;
	}
	
	public  class PafPublishMsgPayload extends MessagePayload {
		//required : hard code
		public String Assessment_Source_System_Code = "PAF"; 
		//required : caller sets value
		public String Assessment_Source_System_Record_Id; 
		//required : hard code
		public String Ref_Assessment_Type_Code = "Quiz"; 
		//required : hard code
		public String Message_Type_Code = "Assessment"; 
		//required : hard code
		public String Message_Version = "1.0"; 
		
		//optional : caller sets value
		public List<AssessmentItem> Assessment_Items; 
		
		public class AssessmentItem {
			//required : caller sets value
			public String Assessment_Item_Source_System_Record_Id;
			//required : hard code
			public String Assessment_Item_Source_System_Code;
			//required : caller sets value
			public Float Sequence_Number;
		}
	}
}
