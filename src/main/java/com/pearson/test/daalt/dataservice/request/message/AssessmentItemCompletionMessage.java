package com.pearson.test.daalt.dataservice.request.message;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.User;

public class AssessmentItemCompletionMessage extends SubPubMessage {

	public AssessmentItemCompletionMessage() {
		payload = new AssessmentItemCompletionMsgPayload();
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException {
		AssessmentItemCompletionMsgPayload specificPayload = (AssessmentItemCompletionMsgPayload) payload;
		switch (propertyName) {
		case "CourseSection" :
			CourseSection courseSection = (CourseSection) propertyValue;
			specificPayload.Course_Section_Source_System_Record_Id = courseSection.getId();
			break;
		case "Quiz" :
			Quiz quiz = (Quiz) propertyValue;
			specificPayload.Assessment_Source_System_Record_Id = quiz.getId();
			break;
		case "Question" :
			Question question = (Question) propertyValue;
			specificPayload.Assessment_Item_Source_System_Record_Id = question.getId();
			specificPayload.Possible_Points = question.getPointsPossible();
			break;
		case "Person" :
			User person = (User) propertyValue;
			specificPayload.Person_Source_System_Record_Id = person.getPersonId();
			break;
		case "CompletionOriginator" :
			specificPayload.Ref_Assessment_Item_Completion_Source_Code = propertyValue.toString();
			break;
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : "
							+ propertyName);
		}
		
	}

	@Override
	public String getMissingCriticalPropertyName() {
		if (payload.Transaction_Type_Code == null) {
			return "MessagePayload.Transaction_Type_Code";
		}
		AssessmentItemCompletionMsgPayload specificPayload = (AssessmentItemCompletionMsgPayload) payload;
		if (specificPayload.Message_Type_Code == null) {
			return "AssessmentItemCompletionMsgPayload.Message_Type_Code";
		}
		if (specificPayload.Message_Version == null){
			return "AssessmentItemCompletionMsgPayload.Message_Version";
		}
		if (specificPayload.Originating_System_Code == null){
			return "AssessmentItemCompletionMsgPayload.Originating_System_Code";
		}
		if (specificPayload.Course_Section_Source_System_Code == null){
			return "AssessmentItemCompletionMsgPayload.Course_Section_Source_System_Code";
		}
		if (specificPayload.Course_Section_Source_System_Record_Id == null){
			return "AssessmentItemCompletionMsgPayload.Course_Section_Source_System_Record_Id";
		}
		if (specificPayload.Person_Role_Code == null){
			return "AssessmentItemCompletionMsgPayload.Person_Role_Code";
		}
		if (specificPayload.Person_Source_System_Code == null){
			return "AssessmentItemCompletionMsgPayload.Person_Source_System_Code";
		}
		if (specificPayload.Person_Source_System_Record_Id == null){
			return "AssessmentItemCompletionMsgPayload.Person_Source_System_Record_Id";
		}
		if (specificPayload.Assessment_Source_System_Code == null){
			return "AssessmentItemCompletionMsgPayload.Assessment_Source_System_Code";
		}
		if (specificPayload.Assessment_Source_System_Record_Id == null){
			return "AssessmentItemCompletionMsgPayload.Assessment_Source_System_Record_Id";
		}
		if (specificPayload.Assessment_Item_Source_System_Code == null){
			return "AssessmentItemCompletionMsgPayload.Assessment_Item_Source_System_Code";
		}
		if (specificPayload.Assessment_Item_Source_System_Record_Id == null){
			return "AssessmentItemCompletionMsgPayload.Assessment_Item_Source_System_Record_Id";
		}
		if (specificPayload.Assessment_Item_Completion_Code == null){
			return "AssessmentItemCompletionMsgPayload.Assessment_Item_Completion_Code";
		}
		if (specificPayload.Assessment_Item_Completion_Datetime == null){
			return "AssessmentItemCompletionMsgPayload.Assessment_Item_Completion_Datetime";
		}
		if (specificPayload.Ref_Assessment_Item_Completion_Source_Code == null){
			return "AssessmentItemCompletionMsgPayload.Ref_Assessment_Item_Completion_Source_Code";
		}
		if (specificPayload.Possible_Points == null){
			return "AssessmentItemCompletionMsgPayload.Possible_Points";
		}
		return null;
	}
	
	public class AssessmentItemCompletionMsgPayload extends MessagePayload{
		//required : hard code
		public String Message_Type_Code = "Assessment_Item_Completion";
		//required : hard code
		public String Originating_System_Code = "DAALTTest";
		//required : hard code? need to figure out how to do message versioning
		//public String Message_Version = "2.1";
		public String Message_Version = "2.0";
		//required : hard code
		public String Course_Section_Source_System_Code = "Registrar";
		//required : caller sets value
		public String Course_Section_Source_System_Record_Id;
		//required : hard code
		public String Person_Role_Code = "Student";
		//required : hard code
		public String Person_Source_System_Code = "PI";
		//required : caller sets value
		public String Person_Source_System_Record_Id;
		//required : hard code
		public String Assessment_Source_System_Code = "PAF";
		//required : caller sets value
		public String Assessment_Source_System_Record_Id;
		//required : hard code
		public String Assessment_Item_Source_System_Code = "PAF";
		//required : caller sets value
		public String Assessment_Item_Source_System_Record_Id;
		//required : hard code
		public String Assessment_Item_Completion_Code = "Complete";
		//required : generate new every time, value should be now
		public String Assessment_Item_Completion_Datetime = getCurrentTimeFormatted();
		//required : caller sets value
		public String Ref_Assessment_Item_Completion_Source_Code;
		//optional : caller sets value
		public Float Possible_Points;
		
//		//optional : not used by data service endpoints version 2.1
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
	}
}
