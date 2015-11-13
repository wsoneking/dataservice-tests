package com.pearson.test.daalt.dataservice.request.message.version01;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.User;

public class GridTransformCourseregistrationMessage extends SubPubMessage {
	//grid.daalt.transform.courseregistration.created

	public GridTransformCourseregistrationMessage() {
		payload = new GridTransformCourseregistrationMsgPayload();
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		GridTransformCourseregistrationMsgPayload specificPayload = (GridTransformCourseregistrationMsgPayload) payload;
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
		GridTransformCourseregistrationMsgPayload msgPayLoad = (GridTransformCourseregistrationMsgPayload) payload;
		if (msgPayLoad.Message_Type_Code == null) {
			return "MessagePayload.Message_Type_Code";
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
	public class GridTransformCourseregistrationMsgPayload extends MessagePayload {
		//required : hard code, never allow override
		public String Message_Type_Code = "Person_Course_Section";
		//required : hard code, never allow override
		public String Message_Version = "1.0";
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
	}


	/*
	 * sample below
	 *     
	 *  \"Course_Section_Source_System_Record_Id\": \"54aebbb8e4b0739288263222\",
	    \"Person_Source_System_Record_Id\": \"ffffffff53b4ea66e4b0279a23cb161a\",
	    \"Person_Source_System_Code\": \"PI\",
	    \"Person_Role_Code\": \"Instructor\",
	    \"Transaction_Datetime\": \"2015-01-08T17: 17: 45.850Z\",
	    \"Message_Type_Code\": \"Person_Course_Section\",
	    \"Course_Section_Source_System_Code\": \"Registrar\",
	    \"Transaction_Type_Code\": \"Create\",
	    \"Message_Version\": \"1.0\"
	*/
}
