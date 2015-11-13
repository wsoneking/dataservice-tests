package com.pearson.test.daalt.dataservice.request.message;

import java.util.ArrayList;
import java.util.List;

import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;

public class AssessmentMessage extends SubPubMessage {
	//PUBLISH
	
	public AssessmentMessage() {
		payload = new AssessmentMsgPayload();
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		AssessmentMsgPayload specificPayload = (AssessmentMsgPayload) payload;
		switch(propertyName) {
			case "Quiz" :
				Quiz quiz = (Quiz) propertyValue;
				specificPayload.Assessment_Source_System_Record_Id = quiz.getId();
				if(quiz.getAssessmentLastSeedDateTime() != null){
					specificPayload.Transaction_Datetime = quiz.getAssessmentLastSeedDateTime();
				}
				specificPayload.Assessment_Items = new ArrayList<>();
				for (Question question : quiz.getQuestions()) {
					AssessmentMsgPayload.AssessmentItem assessmentItem = specificPayload.new AssessmentItem();
					assessmentItem.Assessment_Item_Source_System_Record_Id = question.getId();
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
		AssessmentMsgPayload specificPayload = (AssessmentMsgPayload) payload;
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
		if(specificPayload.Originating_System_Code == null){
			return "PafPublishMsgPayload.Originating_System_Code";
		}
		if(specificPayload.Assessment_Items == null){
			return "PafPublishMsgPayload.Assessment_Items";
		}
		for (AssessmentMsgPayload.AssessmentItem item : specificPayload.Assessment_Items) {
			if(item.Assessment_Item_Source_System_Code == null){
				return "PafPublishMsgPayload.AssessmentItem.Assessment_Item_Source_System_Code";
			}
			if(item.Assessment_Item_Source_System_Record_Id == null){
				return "PafPublishMsgPayload.AssessmentItem.Assessment_Item_Source_System_Record_Id";
			}
			if(item.Sequence_Number == null){
				return "PafPublishMsgPayload.AssessmentItem.Sequence_Number";
			}
		}
		return null;
	}
	
	public  class AssessmentMsgPayload extends MessagePayload {
		//required : hard code
		public String Message_Type_Code = "Assessment";
		//required : hard code
		public String Originating_System_Code = "DAALTTest";
		//required : hard code
//		public String Message_Version = "2.1"; 
		public String Message_Version = "2.0"; 
		//required : hard code
		public String Assessment_Source_System_Code = "PAF"; 
		//required : caller sets value
		public String Assessment_Source_System_Record_Id; 
		//required : hard code
		public String Ref_Assessment_Type_Code = "Quiz"; 
		 
		//optional : caller sets value
		public List<AssessmentItem> Assessment_Items; 
		
		public class AssessmentItem {
			//required : caller sets value
			public String Assessment_Item_Source_System_Record_Id;
			//required : hard code
			public String Assessment_Item_Source_System_Code = "PAF";
			//required : caller sets value
			public Float Sequence_Number;
		}
		
//		//optional:hardcode
//		public boolean Pooled_Assessment_Flag = true;	
//		//optional : hardcode
//		public int Number_Of_Pool_Items_Assigned_To_Student = 1;
//		optional 
// 		public boolean Randomize_Items_Flag =true;
//		//optional : hardcode
//		public int Assessment_Maximum_Attempts_Allowed = 2;
//		//optional : hardcode
//		public String Adaptive_Assessment_Type ="NonAdaptive";
//		//optional : hardcode
//		public String Master_Item_Source_System_Code = "Mastering";
//		//optional : not used by data service endpoints version 2.1
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
	}
}
