package com.pearson.test.daalt.dataservice.request.message.version01;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LearningResource;

public class RevelLrMessage extends SubPubMessage {
	//revel.lr.create
	boolean hasParentResource;
	
	public RevelLrMessage(boolean hasParentResource){
		this.hasParentResource = hasParentResource;
		if (hasParentResource) {
			payload = new RevelLrMsgWithParentPayload();
		} else {
			payload = new RevelLrMsgPayload();
		}
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		RevelLrMsgPayload specificPayload = (RevelLrMsgPayload) payload;
		switch(propertyName) {
		case "CourseSection":
			CourseSection coursection = (CourseSection) propertyValue;
			specificPayload.Source_System_Record_Id = coursection.getBookId();
			specificPayload.Ref_Learning_Resource_Type_Code = "Book";
			specificPayload.Title = "Default Book Title";
			break;
		case "LearningResource": 
			LearningResource learningResource =  (LearningResource) propertyValue;
			specificPayload.Source_System_Record_Id = learningResource.getLearningResourceId();
			specificPayload.Ref_Learning_Resource_Type_Code = learningResource.getLearningResourceType();
			specificPayload.Title = learningResource.getLearningResourceTitle();
			break;
		case "Parent_Source_System_Record_Id" :
			RevelLrMsgWithParentPayload hasParentPayload = (RevelLrMsgWithParentPayload) payload;
			hasParentPayload.Parent_Source_System_Record_Id = propertyValue.toString();
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
		RevelLrMsgPayload specificPayload = (RevelLrMsgPayload) payload;
		if(specificPayload.Message_Type_Code == null){
			return "RevelLrMsgPayload.Message_Type_Code";
		}
		if(specificPayload.Message_Version == null){
			return "RevelLrMsgPayload.Message_Version";
		}
		if(specificPayload.Title == null){
			return "RevelLrMsgPayload.Title";
		}
		if(specificPayload.Source_System_Code == null){
			return "RevelLrMsgPayload.Source_System_Code";
		}
		if(specificPayload.Source_System_Record_Id == null){
			return "RevelLrMsgPayload.Source_System_Record_Id";
		}
		if(specificPayload.Ref_Learning_Resource_Type_Code == null){
			return "RevelLrMsgPayload.Ref_Learning_Resource_Type_Code";
		}
		if(hasParentResource) {
			RevelLrMsgWithParentPayload hasParentPayload = (RevelLrMsgWithParentPayload) payload;
			if(hasParentPayload.Parent_Source_System_Record_Id == null) {
				return "RevelLrMsgWithParentPayload.Parent_Source_System_Record_Id";
			}
		}
		return null;
	}

	protected class RevelLrMsgPayload extends MessagePayload {
		//required : hard code
		public String Message_Type_Code = "Learning_Resource"; 
		//required : hard code
		public String Message_Version = "1.0"; 
		//required : caller sets value
		public String Title;
		//required : defined when setting property
		public String Ref_Learning_Resource_Type_Code;
		//optional : default value, introduce override to allow for other types of platform codes
		public String Delivery_Platform_Code = "DAALT-SQE"; 
		//optional : generate new every time, value should be now
		public String Creation_Datetime = new SimpleDateFormat(dateFormatString).format(new Date());
		//optional : default value, introduce override to allow for internationalization
		public String Ref_Language_Code = "Eng";
		//required : hard code
		public String Source_System_Code = "EPS"; 
		//required : caller sets value
		public String Source_System_Record_Id;

		
/*		//optional : always null
		public String Ref_Learning_Resource_Subtype_Code;
		//optional : always null
		public String Description;
		//optional : always null
		public String URL;
		//optional : always null
		public String Concept_Keyword;
		//optional : always null
		public String Subject_Name;
		//optional : always null
		public String Subject_Code;
		//optional : always null
		public String Subject_Code_System;
		//optional : always null
		public String Ref_Learning_Resource_Content_Model_Type_Code;
		//optional : always null
		public String Container_Content_Source_System_Id;
		//optional : always null
		public String Version;
		//optional : always null
		public String Creator;
		//optional : always null
		public String Publisher_Name;
		//optional : always null
		public String Published_Datetime;
		//optional : always null
		public String Copyright_Holder_Name;
		//optional : always null
		public String Copyright_Year;
		//optional : always null
		public String Use_Rights_URL;
		//optional : always null
		public String Is_Based_On_URL;
		//optional : always null
		public String Ref_Learning_Resource_Intended_End_User_Role_Code;
		//optional : always null
		public String Ref_Learning_Resource_Educational_Use_Code;
		//optional : always null
		public String Ref_Learning_Resource_Digital_Media_Type_Code;
		//optional : always null
		public String Ref_Learning_Resource_Interactivity_Type_Code;
		//optional : always null
		public long Minutes_Required;
		//optional : always null
		public long Max_Seconds_When_Missing_Page_Unload;
		//optional : always null
		public long Typical_Age_Range_Minimum;
		//optional : always null
		public long Typical_Age_Range_Maximum;
		//optional : always null
		public String Child_Source_System_Record_Id;
	*/
	}
	
	protected class RevelLrMsgWithParentPayload extends RevelLrMsgPayload {
		//optional : caller sets value - needed to get data back from endpoints
		public String Parent_Source_System_Record_Id;
	}
}
