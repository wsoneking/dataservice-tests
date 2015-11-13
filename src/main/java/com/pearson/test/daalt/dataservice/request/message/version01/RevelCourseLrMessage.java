package com.pearson.test.daalt.dataservice.request.message.version01;

import com.pearson.test.daalt.dataservice.model.CourseSection;

public class RevelCourseLrMessage extends SubPubMessage {
	//revel.course.lr.create

	public RevelCourseLrMessage() {
		payload = new RevelCourseLrMsgPayload();
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		RevelCourseLrMsgPayload specificPayload = (RevelCourseLrMsgPayload) payload;
		switch(propertyName) {
		case "CourseSection": 
			CourseSection section =  (CourseSection)propertyValue;
			specificPayload.Course_Section_Source_System_Record_Id = section.getId(); 
			specificPayload.Learning_Resource_Source_System_Code = "Revel";
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
		RevelCourseLrMsgPayload specificPayload = (RevelCourseLrMsgPayload) payload;
		if (specificPayload.Message_Type_Code == null) {
			return "RevelLmContentMsgPayload.Message_Type_Code";
		}
		if (specificPayload.Message_Version == null) {
			return "RevelLmContentMsgPayload.Message_Version";
		}
		if (specificPayload.Learning_Resource_Source_System_Code == null) {
			return "RevelLmContentMsgPayload.Learning_Resource_Source_System_Code";
		}
		if (specificPayload.Course_Section_Source_System_Code == null) {
			return "RevelLmContentMsgPayload.Course_Section_Source_System_Code";
		}
		if (specificPayload.Learning_Resource_Source_System_Record_Id == null) {
			return "RevelLmContentMsgPayload.Learning_Resource_Source_System_Record_Id";
		}
		if (specificPayload.Course_Section_Source_System_Record_Id == null) {
			return "RevelLmContentMsgPayload.Course_Section_Source_System_Record_Id";
		}
		return null;
	}
	
	protected class RevelCourseLrMsgPayload extends MessagePayload {
		
		//FUTURE: these two will be moved into Learning_Resource_Hierarchy in new schema.
		//required : hard code
		public String Learning_Resource_Source_System_Code = "EPS";
		//required : get from object
		public String Learning_Resource_Source_System_Record_Id;
		
		//required : hard code, never allow override
		public String Message_Type_Code = "Course_Section_To_Learning_Resource";
		//required : hard code? need to figure out how to do message versioning
		public String Message_Version = "1.0";
		//required : hard code, never allow override
		public String Course_Section_Source_System_Code = "Registrar";

		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Course_Section_Source_System_Record_Id;
		
		// optional; it will be removed in new schema.
		public String Course_Section_Code = "SQE 54AEBBB8E4B0739";

		// The message below is for new schema
/*		//required : enum ["Collection","CollectionModule","Book","Video"]
		public String Ref_Relationship_Context_Code;	//FUTURE it's required, but can't find in real msg. It's new. 
		//required : FUTURE it's required, but can't find in real msg. It's new. "Identifies the Learning Resource that makes the Relationship Context unique.  See Ref_Relationship_Context_Code.
		public String Learning_Resource_Relationship_Context_Id;
		//required : FUTURE it's required, but can't find in real msg.
		public String Target_Learning_Resource_Source_System_Record_Id;
		
		//required FUTURE
		protected class Learning_Resource_Hierarchy{
			public String Learning_Resource_Source_System_Record_Id;
			public String Learning_Resource_Source_System_Code;
			public String Ref_Learning_Resource_Type_Code;
			public String Ref_Learning_Resource_Subtype_Code;
		}
		
		// I don't find it in schema. So comment it. https://devops-tools.pearson.com/stash/projects/DAALT_REF/repos/schemas/browse/subpub/json/Course_Section_To_Learning_Resource.schema.json?at=refs%2Fheads%2Fdev
		//public String Course_Section_Code;
*/	
		
		//optional : always null
//		public String Course_Source_System_Code;
		//optional : always null
//		public String Course_Source_System_Record_Id;
		
	}


	/*
		\"Message_Type_Code\": \"Course_Section_To_Learning_Resource\",
		\"Message_Version\": \"1.0\",
		\"Transaction_Type_Code\": \"Create\",
		\"Course_Section_Code\": \"54AEBBB8E4B0739\",
		\"Course_Section_Source_System_Code\": \"Registrar\",
		\"Course_Section_Source_System_Record_Id\": \"54aebbb8e4b0739288263222\",
		\"Learning_Resource_Source_System_Code\": \"EPS\",
		\"Learning_Resource_Source_System_Record_Id\": \"bd1838a0-747e-11e4-90a2-11cfd4ddafc5\",
		\"Transaction_Datetime\": \"2015-01-08T17: 17: 49.473Z\"
	*/
}
