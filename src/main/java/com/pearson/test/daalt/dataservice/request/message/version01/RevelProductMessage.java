package com.pearson.test.daalt.dataservice.request.message.version01;

import com.pearson.test.daalt.dataservice.model.CourseSection;

public class RevelProductMessage extends SubPubMessage {
	//revel.product.create
	
	public RevelProductMessage(){
		payload = new RevelProductMsgPayload();
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException {
		RevelProductMsgPayload specificPayload = (RevelProductMsgPayload) payload;
		switch(propertyName) {
		case "CourseSection" :
			CourseSection courseSection = (CourseSection) propertyValue;
			specificPayload.Product_Code = courseSection.getBookId();
			specificPayload.Product_Source_System_Record_Id = courseSection.getBookId();
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
		if (payload.Transaction_Datetime == null) {
			return "MessagePayload.Transaction_Datetime";
		}
		RevelProductMsgPayload specificPayload = (RevelProductMsgPayload) payload;
		if(specificPayload.Message_Type_Code == null){
			return "RevelProductMsgPayload.Message_Type_Code";
		}
		if(specificPayload.Message_Version == null){
			return "RevelProductMsgPayload.Message_Version";
		}
		if(specificPayload.Delivery_Platform_Code == null){
			return "RevelProductMsgPayload.Delivery_Platform_Code";
		}
		if(specificPayload.Product_Code == null){
			return "RevelProductMsgPayload.Product_Code";
		}
		if(specificPayload.Product_Name == null){
			return "RevelProductMsgPayload.Product_Name";
		}
		if(specificPayload.Product_Source_System_Code == null){
			return "RevelProductMsgPayload.Product_Source_System_Code";
		}
		if(specificPayload.Product_Source_System_Record_Id == null){
			return "RevelProductMsgPayload.Product_Source_System_Record_Id";
		}
		if(specificPayload.Product_Description == null){
			return "RevelProductMsgPayload.Product_Description";
		}
		return null;
	}
	
	protected class RevelProductMsgPayload extends MessagePayload {
		//required : hard code
		public String Message_Type_Code = "Product";
		//required : hard code
		public String Message_Version = "1.0";
		//required : hard code
		public String Delivery_Platform_Code = "DAALT-SQE";
		//required : caller sets value
		public String Product_Code;
		//required : hard code for now, introduce override to allow for different products
		public String Product_Name = "QA_DiscoveringtheLifeSpan";
		//required : hard code
		public String Product_Source_System_Code = "Revel";
		//required : caller sets value
		public String Product_Source_System_Record_Id;
		
		//optional : hard code for now, introduce override to allow for different products
		public String Product_Description = "QA_ThatisnottheanswertothequestionIasked";
	}
}
