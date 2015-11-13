package com.pearson.test.daalt.dataservice.request.message;

import com.pearson.test.daalt.dataservice.model.CourseSection;

public class ProductToCourseSectionPLTMessage extends SubPubMessage {
	//revel.product.course.create (/.delete/.update)
	
	public ProductToCourseSectionPLTMessage() {
		payload = new ProductToCourseSectionPLTMsgPayload();
	}
 
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		ProductToCourseSectionPLTMsgPayload specificPayload = (ProductToCourseSectionPLTMsgPayload) payload;
		switch(propertyName) {
		case "CourseSection": 
			CourseSection section =  (CourseSection)propertyValue;
			specificPayload.Course_Section_Source_System_Record_Id = section.getId();
			specificPayload.Product_Source_System_Record_Id = section.getBookId();
			break;
		case "Transaction_Type_Code": 
			String code =  (String) propertyValue;
			specificPayload.Transaction_Type_Code = code;
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
		ProductToCourseSectionPLTMsgPayload msgPayLoad = (ProductToCourseSectionPLTMsgPayload) payload;
		if (msgPayLoad.Course_Section_Source_System_Record_Id == null) {
			return "MessagePayload.Course_Section_Source_System_Record_Id";
		}
		if (msgPayLoad.Product_Source_System_Record_Id == null) {
			return "MessagePayload.Product_Source_System_Record_Id";
		}
		
		return null;
	}
	
	protected class ProductToCourseSectionPLTMsgPayload extends MessagePayload {
		
		//required : hard code
		public String Message_Type_Code = "Product_To_Course_Section_PLT"; 
		//required : hard code, never allow override
		public String Originating_System_Code = "DAALTTest";
		//required : hard code
//		public String Message_Version = "2.1";
		public String Message_Version = "2.0";
		
		//required : hard code
		public String Product_Source_System_Code = "EPS";
		//required : it's the book Id
		public String Product_Source_System_Record_Id;
		
		//required : hard code, never allow override
		public String Course_Section_Source_System_Code = "Registrar";
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Course_Section_Source_System_Record_Id;
		
		// required : hard code
		public String Delivery_Platform_Code = "DAALT-SQE";
		
		//optional:
//		public String Product_To_Course_Section_Source_System_Code;
		//optional:
//		public String Product_To_Course_Section_Source_System_Record_Id;
		
		
		//optional:  always null
//		public String Eff_From_Time;
		//optional:  always null
//		public String Eff_Thru_Datetime;
		
//		//optional : not used by data service endpoints version 2.0
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
		
		
	}
}
