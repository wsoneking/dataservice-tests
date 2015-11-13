package com.pearson.test.daalt.dataservice.request.message;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.User;

public class PersonCourseSectionMessage extends SubPubMessage {
	//grid.daalt.transform.courseregistration.created

	public PersonCourseSectionMessage() {
		payload = new PersonCourseSectionMsgPayload();
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		PersonCourseSectionMsgPayload specificPayload = (PersonCourseSectionMsgPayload) payload;
		switch(propertyName) {
		case "CourseSection": 
			CourseSection section =  (CourseSection)propertyValue;
			specificPayload.Course_Section_Source_System_Record_Id = section.getId();
			break;
		case "Person":
			User user = (User) propertyValue;
			specificPayload.Person_Source_System_Record_Id = user.getPersonId();
			specificPayload.Person_Role_Code = user.getPersonRole();
			break;
		case "Transaction_Type_Code" :
			specificPayload.Transaction_Type_Code = propertyValue.toString();
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
		PersonCourseSectionMsgPayload msgPayLoad = (PersonCourseSectionMsgPayload) payload;
		if (msgPayLoad.Message_Type_Code == null) {
			return "MessagePayload.Message_Type_Code";
		}
		if (msgPayLoad.Originating_System_Code == null) {
			return "MessagePayload.Originating_System_Code";
		}
		if (msgPayLoad.Message_Version == null) {
			return "MessagePayload.Message_Version";
		}
		if (msgPayLoad.Course_Section_Source_System_Code == null) {
			return "MessagePayload.Course_Section_Source_System_Code";
		}
		if (msgPayLoad.Course_Section_Source_System_Record_Id == null) {
			return "MessagePayload.Course_Section_Source_System_Record_Id";
		}
		if (msgPayLoad.Person_Source_System_Code == null) {
			return "MessagePayload.Person_Source_System_Code";
		}
		if (msgPayLoad.Person_Source_System_Record_Id == null) {
			return "MessagePayload.Person_Source_System_Record_Id";
		}
		if (msgPayLoad.Person_Role_Code == null) {
			return "MessagePayload.Person_Role_Code";
		}
		return null;
	}
	
	public MessagePayload getPayloadObject(){
		return payload;
	}
	
	public class PersonCourseSectionMsgPayload extends MessagePayload {
		//required : hard code, never allow override
		public String Message_Type_Code = "Person_Course_Section";
		//required : hard code, never allow override
		public String Originating_System_Code = "DAALTTest";
		//required : hard code, never allow override
//		public String Message_Version = "2.1";
		public String Message_Version = "2.0";
		//required : hard code, never allow override
		public String Course_Section_Source_System_Code = "Registrar";
		//required : hard code, never allow override
		public String Person_Source_System_Code = "PI";
		
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Course_Section_Source_System_Record_Id;
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Person_Source_System_Record_Id;
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Person_Role_Code;
		
/*		//optional : always null
		public String Course_Section_Code;
		//optional : always null
		public String Enrollment_Datetime;
*/
		
//		//optional : not used by data service endpoints version 2.0
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
		
	}
}
