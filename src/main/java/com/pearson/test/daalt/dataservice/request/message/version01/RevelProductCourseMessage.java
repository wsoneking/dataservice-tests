package com.pearson.test.daalt.dataservice.request.message.version01;

import com.pearson.test.daalt.dataservice.model.CourseSection;

public class RevelProductCourseMessage extends SubPubMessage {
	//revel.product.course.create (/.delete/.update)
	
	public RevelProductCourseMessage() {
		payload = new RevelProductCourseMsgPayload();
	}
 
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		RevelProductCourseMsgPayload specificPayload = (RevelProductCourseMsgPayload) payload;
		switch(propertyName) {
		case "CourseSection": 
			CourseSection section =  (CourseSection)propertyValue;
			specificPayload.Course_Section_Source_System_Record_Id = section.getId();
			specificPayload.Product_Source_System_Record_Id = section.getBookId();
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
		RevelProductCourseMsgPayload msgPayLoad = (RevelProductCourseMsgPayload) payload;
		if (msgPayLoad.Course_Section_Source_System_Record_Id == null) {
			return "MessagePayload.Course_Section_Source_System_Record_Id";
		}
		if (msgPayLoad.Product_Source_System_Record_Id == null) {
			return "MessagePayload.Product_Source_System_Record_Id";
		}
		
		return null;
	}
	
	protected class RevelProductCourseMsgPayload extends MessagePayload {
		
		//required : hard code
		public String Message_Type_Code = "Product_To_Course_Section_PLT"; 
		//required : hard code
		public String Message_Version = "1.0"; 
		
		//required : hard code, never allow override
		public String Course_Section_Source_System_Code = "Registrar";
		
		//required : it's the book Id
		public String Product_Source_System_Record_Id;
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Course_Section_Source_System_Record_Id;
		
		//required : default value, hard code here, allow override
		public String Product_Source_System_Code = "EPS";
		// required : default value, hard code here, allow override
		public String Delivery_Platform_Code = "DAALT-SQE";
		
		//optional:
//		public String Product_To_Course_Section_Source_System_Code;
		//optional:
//		public String Product_To_Course_Section_Source_System_Record_Id;
		
		
		//optional:  always null
//		public String Eff_From_Datetime;
		//optional:  always null
//		public String Eff_Thru_Datetime;
		
		
		
	}

	/*
	    \"Message_Type_Code\": \"Product_To_Course_Section_PLT\",
	    \"Message_Version\": \"1.0\",
	    \"Transaction_Type_Code\": \"Create\",
	    \"Transaction_Datetime\": \"2015-01-08T17: 17: 49.525Z\",
	    \"Product_Source_System_Code\": \"Revel\",
	    \"Product_Source_System_Record_Id\": \"bd1838a0-747e-11e4-90a2-11cfd4ddafc5\",
	    \"Course_Section_Source_System_Code\": \"Registrar\",
	    \"Course_Section_Source_System_Record_Id\": \"54aebbb8e4b0739288263222\",
	    \"Delivery_Platform_Code\": \"Revel\"
	*/
}
