package com.pearson.test.daalt.dataservice.request.message.version01;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;

public class RevelLmMessage extends SubPubMessage {
	//revel.lm.create
	
	public RevelLmMessage() {
		payload = new RevelLmMsgPayload();
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException {
		RevelLmMsgPayload specificPayload = (RevelLmMsgPayload) payload;
		switch(propertyName) {
		case "Transaction_Type_Code":
			specificPayload.Transaction_Type_Code = propertyValue.toString();
			break;
		case "CourseSection": 
			CourseSection section =  (CourseSection) propertyValue;
			specificPayload.Course_Section_Source_System_Record_Id = section.getId();
			break;
		case "Instructor":
			Instructor instructor = (Instructor) propertyValue;
			specificPayload.Instructor_Source_System_Record_Id = instructor.getPersonId();
			break;
		case "Assignment":
			Assignment assignment = (Assignment) propertyValue;
			specificPayload.Title = assignment.getTitle();
			specificPayload.Activity_Due_Datetime = assignment.getDueDateAsString();
			specificPayload.Learning_Module_Source_System_Record_Id = assignment.getId();
			specificPayload.Possible_Points = assignment.getPointsPossible();
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
		RevelLmMsgPayload specificPayload = (RevelLmMsgPayload) payload;
		if (specificPayload.Message_Type_Code == null) {
			return "RevelLmMsgPayload.Message_Type_Code";
		}
		if (specificPayload.Message_Version == null) {
			return "RevelLmMsgPayload.Message_Version";
		}
		if (specificPayload.Learning_Module_Source_System_Code == null) {
			return "RevelLmMsgPayload.Learning_Module_Source_System_Code";
		}
		if (specificPayload.Instructor_Source_System_Code == null) {
			return "RevelLmMsgPayload.Instructor_Source_System_Code";
		}
		if (specificPayload.Course_Section_Source_System_Code == null) {
			return "RevelLmMsgPayload.Course_Section_Source_System_Code";
		}
		if (specificPayload.Creation_Datetime == null) {
			return "RevelLmMsgPayload.Creation_Datetime";
		}
		if (specificPayload.Learning_Module_Source_System_Record_Id == null) {
			return "RevelLmMsgPayload.Learning_Module_Source_System_Record_Id";
		}
		if (specificPayload.Instructor_Source_System_Record_Id == null) {
			return "RevelLmMsgPayload.Instructor_Source_System_Record_Id";
		}
		if (specificPayload.Course_Section_Source_System_Record_Id == null) {
			return "RevelLmMsgPayload.Course_Section_Source_System_Record_Id";
		}
		if (specificPayload.Activity_Due_Datetime == null) {
			return "RevelLmMsgPayload.Activity_Due_Datetime";
		}
		if (specificPayload.Possible_Points == null) {
			return "RevelLmMsgPayload.Possible_Points";
		}
		if (specificPayload.Title == null) {
			return "RevelLmMsgPayload.Title";
		}
		if (specificPayload.Ref_Learning_Module_Type_Code == null) {
			return "RevelLmMsgPayload.Ref_Learning_Module_Type_Code";
		}
		return null;
	}
	
	public class RevelLmMsgPayload extends MessagePayload {
		//required : hard code, never allow override
		public String Message_Type_Code = "Learning_Module";
		//required : hard code? need to figure out how to do message versioning
		public String Message_Version = "1.0";
		//required : hard code, never allow override
		public final String Learning_Module_Source_System_Code = "Revel";
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Learning_Module_Source_System_Record_Id;
		//required : hard code, never allow override
		public String Instructor_Source_System_Code = "PI";
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Instructor_Source_System_Record_Id;
		//required : hard code, never allow override
		public String Course_Section_Source_System_Code = "Registrar";
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Course_Section_Source_System_Record_Id;
		//optional : default value, introduce override to allow for multiple assignments on the course section
		public Float Sequence_Number = 0f;
		//required : defined along with object, object passed to TestAction
		public String Title;
		//required : default value, introduce override to allow for other types of learning modules
		public String Ref_Learning_Module_Type_Code = "Assignment";
		//required : generate new every time, value should be now
		public String Creation_Datetime = getCurrentTimeFormatted();
		//required : defined along with object, object passed to TestAction
		public String Activity_Due_Datetime;
		//optional : default value = now, introduce override to allow for separate assignment creation / assignment release
		public String Release_Datetime = getCurrentTimeFormatted();
		//optional : defined along with object, object passed to TestAction
		public Float Possible_Points;
		//optional : default value, introduce override to allow for internationalization
		public String Ref_Language_Code = "Eng";
/*		
		//optional : always null, introduce override to allow for assignment modification test cases
		public String Learning_Module_Version_Info;
		
		//optional : always null
		public String Identifying_Code;
		//optional : always null
		public String Description;
		//optional : always null
		public String Prerequisite;
		//optional : always null
		public String Abstract;
		//optional : always null
		public String Subject_Keyword_List;
		//optional : always null
		public String Rubric_URL;
		//optional : always null
		public float Maximum_Time_Allowed;
		//optional : always null
		public String Ref_Time_Units_Code;
		//optional : always null
		public Float Weight;
		
	*/
	}
}
