package com.pearson.test.daalt.dataservice.request.message.version01;

import java.util.UUID;

import com.pearson.test.daalt.dataservice.model.CourseSection;

public class GridTransformSectionMessage extends SubPubMessage {
	//grid.daalt.transform.section.created
	
	public GridTransformSectionMessage() {
		payload = new GridTransformSectionMsgPayload();
	}	
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		GridTransformSectionMsgPayload specificPayload = (GridTransformSectionMsgPayload) payload;
		switch(propertyName) {
		case "CourseSection": 
			CourseSection section =  (CourseSection)propertyValue;
			specificPayload.Course_Section_Source_System_Record_Id = section.getId();
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
		GridTransformSectionMsgPayload msgPayLoad = (GridTransformSectionMsgPayload) payload;
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
		if (msgPayLoad.Course_Source_System_Code == null) {
			return "MessagePayload.Course_Source_System_Code";
		}
		if (msgPayLoad.Course_Section_Code == null) {
			return "MessagePayload.Course_Section_Code";
		}
		if (msgPayLoad.Course_Section_Title == null) {
			return "MessagePayload.Course_Section_Title";
		}
		if (msgPayLoad.Course_Source_System_Record_Id == null) {
			return "MessagePayload.Course_Source_System_Record_Id";
		}
		return null;
	}

	protected class GridTransformSectionMsgPayload extends MessagePayload {
		//required - hard code
		public String Message_Type_Code = "Course_Section"; 
		//required - hard code
		public String Message_Version = "1.0"; 
		//required - hard code
		public String Course_Section_Source_System_Code = "Registrar"; 
		//required - caller must pass
		public String Course_Section_Source_System_Record_Id; 
		//required - hard code
		public String Course_Source_System_Code = "Registrar"; 
		//required - hard code for now - add override option later?
		public String Course_Section_Code = "TEST101"; 
		//required - hard code for now - add override option later?
		public String Course_Section_Title = "QA Default Course Title"; 
		//required - generate new each time - used where/how?
		public String Course_Source_System_Record_Id = "SQE-" + UUID.randomUUID().toString(); 
		
		//optional - generate new each time
		public String Course_Access_End_Date = getPastOrFutureTimeFormatted(90);
		//optional - generate new each time
		public String Course_Access_Start_Date = getPastOrFutureTimeFormatted(-2);		
		
/*		//optional : always null
		public String Course_Section_Description; 
		//optional : always null
		public float Call_Number;
		//optional : always null
		public String Section_Details;
		//optional : always null
		public float Time_Required_For_Completion;
		//optional : always null
		public String Ref_Time_Units_Code;
		//optional : always null
		public String Ref_Instruction_Language_Code;
		//optional : always null
		public String Virtual_Indicator;
		//optional : always null
		public String Organization_Calendar_Session_Id;
		//optional : always null
		public String Organization_Calendar_Session_Designator;
		//optional : always null
		public String Course_Meeting_Days_Of_Week_MTWTFSS;
		//optional : always null
		public String Open_Enrollment_Begin_Datetime;
		//optional : always null
		public String Open_Enrollment_End_Datetime;
		//optional : always null
		public String Course_Section_Meeting_Schedule;
*/		
	}
}
