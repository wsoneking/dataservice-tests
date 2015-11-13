package com.pearson.test.daalt.dataservice.request.message;

import com.pearson.test.daalt.dataservice.model.User;

public class PersonMessage extends SubPubMessage {
	//grid.daalt.transform.identityprofile.created
	
	public PersonMessage() {
		payload = new PersonMsgPayload();
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		PersonMsgPayload specificPayload = (PersonMsgPayload) payload;
		switch(propertyName) {
			case "Person" :
				User user = (User) propertyValue;
				specificPayload.Given_Name = user.getGivenName();
				specificPayload.Family_Name = user.getFamilyName();
				specificPayload.Source_System_Record_Id = user.getPersonId();
				break;
			default : 
				throw new UnknownPropertyException("Failed to build message due to unknown property : "
						+ propertyName);
		}
	}
	
	@Override
	public String getMissingCriticalPropertyName() {
		if (payload.Transaction_Type_Code == null) {
			return "MessagePayload.Transaction_Type_Code";
		}
		if(payload.Transaction_Datetime == null){
			return "MessagePayload.Transaction_Datetime";
		}
		PersonMsgPayload specificPayload = (PersonMsgPayload) payload;
		if(specificPayload.Given_Name == null){
			return "GridTransformPersonIdentityMsgPayload.Given_Name";
		}
		if(specificPayload.Message_Type_Code == null){
			return "GridTransformPersonIdentityMsgPayload.Message_Type_Code";
		}
		if(specificPayload.Message_Version == null){
			return "GridTransformPersonIdentityMsgPayload.Message_Version";
		}
		if(specificPayload.Source_System_Code == null){
			return "GridTransformPersonIdentityMsgPayload.Source_System_Code";
		}
		if(specificPayload.Family_Name == null){
			return "GridTransformPersonIdentityMsgPayload.Family_Name";
		}
		return null;
	}
	
	public class PersonMsgPayload extends MessagePayload {
		
		//required : always hard code
		public String Message_Type_Code = "Person";
		//required : always hard code
		public String Originating_System_Code = "DAALTTest";
		//required : hard code
		public String Message_Version = "2.0";
		//required : caller sets value or use default
		public String Given_Name;
		//required : caller sets value or use default
		public String Family_Name;
		//required : caller sets value
		public String Source_System_Record_Id;
		//required : hard code
		public String Source_System_Code = "PI";
		
/*		//optional : always null
		public String Middle_Name;
		//optional : always null
		public String Title;
		//optional : always null
		public String Generation_Code;
		//optional : always null
		public String Birthdate;
		//optional : always null
		public String Ref_Sex_Code;
		//optional : always null
		public String Created_Datetime;
		//optional : always null
		public String Preference_Language_Code;
		//optional : always null
		public String Preference_Time_Zone;
		//optional : always null
		public List<Person_Identifier> Person_Identifier;
		//optional : always null
		public List<Person_Address> Person_Address;
		//optional : always null
		public List<Person_Email_Address> Person_Email_Address;
		//optional : always null
		public List<Person_Telphone_Number> Person_Telphone_Number;
		
		public class Person_Identifier{
			//required : caller sets value
			public String Ref_Person_Identification_System_Code;
			//required : caller sets value
			public String Ref_Person_Identifier_Type_Code;
			//required : caller sets value
			public String Identifier;
		}
		
		public class Person_Address{
			//optional : always null
			public String Apartment_Room_Or_Suite_Number;
			//optional : always null
			public String City;
			//required : caller sets value
			public String Ref_Person_Location_Type_Code;
			//optional : always null
			public String Postal_Code;
			//optional : always null
			public String Ref_State_Code;
			//optional : always null
			public String Street_Number_And_Name;
		}
		
		public class Person_Email_Address{
			//required : caller sets value
			public String Email_Address;
			//required : caller sets value
			public boolean Is_Primary_Flag;
			//required : caller sets value
			public String Ref_Email_Type_Code;
		}
		
		public class Person_Telphone_Number{
			//required : caller sets value
			public String Ref_Person_Telphone_Number_Type_Code;
			//required : caller sets value
			public String Telephone_Number;
		}
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
