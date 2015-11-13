package com.pearson.test.daalt.dataservice.request.message.version01;

import com.pearson.test.daalt.dataservice.model.CourseSection;

public class CourseSectionToLearningResource extends SubPubMessage {
	public CourseSectionToLearningResource() {
		payload = new CourseSectionToLearningResourceMsgPayload();
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		CourseSectionToLearningResourceMsgPayload specificPayload = (CourseSectionToLearningResourceMsgPayload) payload;
		switch(propertyName) {
		case "CourseSection": 
			CourseSection section =  (CourseSection)propertyValue;
			specificPayload.Course_Section_Source_System_Record_Id = section.getId();
			specificPayload.Learning_Resource_Source_System_Record_Id = section.getBookId();
			break;
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : " + propertyName);
		}
	}
	
	@Override
	public String getMissingCriticalPropertyName() {
		if (payload.Transaction_Type_Code == null) {
			return "MessagePayload.Transaction_Type_Code";
		}
		CourseSectionToLearningResourceMsgPayload msgPayLoad = (CourseSectionToLearningResourceMsgPayload) payload;
		if (msgPayLoad.Message_Type_Code == null) {
			return "MessagePayload.Message_Type_Code";
		}
		if (msgPayLoad.Message_Version == null) {
			return "MessagePayload.Message_Version";
		}
		if (msgPayLoad.Learning_Resource_Source_System_Code == null) {
			return "MessagePayload.Learning_Resource_Source_System_Code";
		}
		if (msgPayLoad.Learning_Resource_Source_System_Record_Id == null) {
			return "MessagePayload.Learning_Resource_Source_System_Record_Id";
		}
		if (msgPayLoad.Course_Section_Source_System_Code == null) {
			return "MessagePayload.Course_Section_Source_System_Code";
		}
		if (msgPayLoad.Course_Section_Source_System_Record_Id == null) {
			return "MessagePayload.Course_Section_Source_System_Record_Id";
		}
		return null;
	}
	
	public MessagePayload getPayloadObject(){
		return payload;
	}
	
	public class CourseSectionToLearningResourceMsgPayload extends MessagePayload {
		//required : hard code, never allow override
		public String Message_Type_Code = "Course_Section_To_Learning_Resource";
		//required : hard code, never allow override
		public String Message_Version = "1.0";
		//required : hard code, never allow override
		public String Learning_Resource_Source_System_Code = "EPS";
		//required : hard code, never allow override
		public String Learning_Resource_Source_System_Record_Id;
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Course_Section_Source_System_Code = "Registrar";
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Course_Section_Source_System_Record_Id;
	}
}
