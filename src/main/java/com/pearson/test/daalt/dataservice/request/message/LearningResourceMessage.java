package com.pearson.test.daalt.dataservice.request.message;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LearningResource;
import com.pearson.test.daalt.dataservice.model.LearningResourceType;
import com.pearson.test.daalt.dataservice.model.Page;

//revel.lr.create
public class LearningResourceMessage extends SubPubMessage {
	private boolean contentIsEmbeddedQuestion;
	
	public LearningResourceMessage(boolean contentIsEmbeddedQuestion, boolean learningResourceHasSubtype){
		this.contentIsEmbeddedQuestion = contentIsEmbeddedQuestion;
		if (contentIsEmbeddedQuestion) {
			payload = new LearningResourceEmbeddedQuestionMsgPayload();
		} else if (learningResourceHasSubtype){
			payload = new LearningResourceSubtypeMsgPayload();
		} else {
			payload = new LearningResourceMsgPayload();
		}
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		LearningResourceMsgPayload specificPayload = (LearningResourceMsgPayload) payload;
		
		switch(propertyName) {
		case "CourseSection":
			CourseSection coursection = (CourseSection) propertyValue;
			specificPayload.Learning_Resource_Source_System_Record_Id = coursection.getBookId();
			specificPayload.Ref_Learning_Resource_Type_Code = LearningResourceType.BOOK.value;
			break;
		case "LearningResource":
			LearningResource learningResource =  (LearningResource) propertyValue;
			specificPayload.Learning_Resource_Source_System_Record_Id = learningResource.getLearningResourceId();
			specificPayload.Ref_Learning_Resource_Type_Code = learningResource.getLearningResourceType();
			specificPayload.Title = learningResource.getLearningResourceTitle();
			if (payload instanceof LearningResourceSubtypeMsgPayload) {
				LearningResourceSubtypeMsgPayload subtypePayload = (LearningResourceSubtypeMsgPayload) payload;
				subtypePayload.Ref_Learning_Resource_Subtype_Code = learningResource.getLearningResourceSubType();
			}
			break;
		case "Page" :
			Page page = (Page) propertyValue;
			LearningResourceEmbeddedQuestionMsgPayload embeddedPayload = (LearningResourceEmbeddedQuestionMsgPayload) payload;
			embeddedPayload.Container_Content_Source_System_Id = page.getLearningResourceId();
			break;
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : " + propertyName);
		}
	}
	
	@Override
	public String getMissingCriticalPropertyName() {
		if (payload.Transaction_Type_Code == null) {
			return "LearningResourceMsgPayload.Transaction_Type_Code";
		}
		if (payload.Transaction_Datetime == null) {
			return "LearningResourceMsgPayload.Transaction_Datetime";
		}
		LearningResourceMsgPayload specificPayload = (LearningResourceMsgPayload) payload;
		if(specificPayload.Message_Type_Code == null){
			return "LearningResourceMsgPayload.Message_Type_Code";
		}
		if(specificPayload.Originating_System_Code == null){
			return "LearningResourceMsgPayload.Originating_System_Code";
		}
		if(specificPayload.Ref_Learning_Resource_Type_Code == null){
			return "LearningResourceMsgPayload.Ref_Learning_Resource_Type_Code";
		}
		if(specificPayload.Message_Version == null){
			return "LearningResourceMsgPayload.Message_Version";
		}
		if(specificPayload.Learning_Resource_Source_System_Code == null){
			return "LearningResourceMsgPayload.Learning_Resource_Source_System_Code";
		}
		if(specificPayload.Learning_Resource_Source_System_Record_Id == null){
			return "LearningResourceMsgPayload.Learning_Resource_Source_System_Record_Id";
		}
		if(specificPayload.Title == null){
			return "LearningResourceMsgPayload.Title";
		}
		if(specificPayload.Ref_Learning_Resource_Creation_Status_Code == null){
			return "LearningResourceMsgPayload.Ref_Learning_Resource_Creation_Status_Code";
		}
		if(specificPayload.Delivery_Platform_Code == null){
			return "LearningResourceMsgPayload.Delivery_Platform_Code";
		}
		if (contentIsEmbeddedQuestion) {
			LearningResourceEmbeddedQuestionMsgPayload embeddedPayload = (LearningResourceEmbeddedQuestionMsgPayload) payload;
			if(embeddedPayload.Container_Content_Source_System_Id == null){
				return "LearningResourceMsgPayload.Container_Content_Source_System_Id";
			}
		}
		return null;
	}

	public class LearningResourceMsgPayload extends MessagePayload {
		//required : hard code
		public String Message_Type_Code = "Learning_Resource"; 
		//optional : hard code, never allow override
		public String Originating_System_Code = "DAALTTest";
		//required : learningResource.getType()
		public String Ref_Learning_Resource_Type_Code;
		//required : hard code
		public String Message_Version = "2.0";
		//required : learningResource.getId()
		public String Learning_Resource_Source_System_Record_Id; 
		//required : hard code
		public String Learning_Resource_Source_System_Code = "Revel"; 
		//required : learningResource.getLearningResourceTitle()
		public String Title = "Title";
		//optional : hard code
		public String Ref_Learning_Resource_Creation_Status_Code = "Completed";
		//optional : hard code
		public String Delivery_Platform_Code = "DAALT-SQE";
		//optional : default value, introduce override to allow for internationalization
		public String Ref_Language_Code = "en-US"; 		
		
//		//optional : not used by data service endpoints version 2.0
//		public PrintableName Printable_Name = new PrintableName();
//		public class PrintableName {
//			public String Tier_Name;
//			public String Tier_Code;
//		}
		
//		//optional : not used by data service endpoints version 2.0
//		public String Version;
		
//		//optional : not used by data service endpoints version 2.0
//		public String Ref_Learning_Resource_Media_Type_Code;
		
//		//optional : not used by data service endpoints version 2.0
//		public String Description;
		
//		//optional : not used by data service endpoints version 2.0
//		public List<LearningResourceDatetime> Learning_Resource_Datetimes = new ArrayList<>();
//		public class LearningResourceDatetime {
//			public String Datetime_Type;
//			public String Datetime_Value;
//		}
		
//		//optional : not used by data service endpoints version 2.0
//		public List<LearningResourceContributor> Learning_Resource_Contributors = new ArrayList<>();
//		public class LearningResourceContributor {
//			public String Person_Role_Code;
//			public FreeformData Freeform_Data;
//			public StructuredData Structured_Data;
//			
//			public class FreeformData {
//				public String Person_Name;
//			}
//			public class StructuredData {
//				public String Person_Source_System_Code;
//				public String Person_Source_System_Record_Id;
//				public String Organization_Source_System_Code;
//				public String Organization_Source_System_Record_Id;
//			}
//		}
		
//		//optional : not used by data service endpoints version 2.0
//		public List<LearningResourceOrganization> Learning_Resource_Organizations = new ArrayList<>();
//		public class LearningResourceOrganization {
//			public String Organization_Role_Code;
//			public FreeformData Freeform_Data;
//			public StructuredData Structured_Data;
//			
//			public class FreeformData {
//				public String Organization_Name;
//			}
//			public class StructuredData {
//				public String Organization_Source_System_Code;
//				public String Organization_Source_System_Record_Id;
//			}
//		}
		
//		//optional : not used by data service endpoints version 2.0
//		public List<LearningResourceURL> Learning_Resource_URLs = new ArrayList<>();
//		public class LearningResourceURL {
//			public String URL_Type_Code;
//			public String URL;
//		}
		
//		//optional : not used by data service endpoints version 2.0
//		public List<LearningResourceIdentifier> Learning_Resource_Identifiers = new ArrayList<>();
//		public class LearningResourceIdentifier {
//			public String Ref_Learning_Resource_Identifier_Type_Code;
//			public String Learning_Resource_Identifier;
//		}
		
//		//optional : not used by data service endpoints version 2.0
//		public List<LearningResourceDiscipline> Learning_Resource_Discipline = new ArrayList<>();
//		public class LearningResourceDiscipline {
//			public float Discipline_Weight;
//			public String Discipline_Source_System_Code;
//			public String Discipline_Source_System_Record_Id;
//			public String Discipline_Name;
//		}
		
//		//optional : not used by data service endpoints version 2.0
//		public List<LearningResourceSubject> Learning_Resource_Subject = new ArrayList<>();
//		public class LearningResourceSubject {
//			public String Subject_Source_System_Code;
//			public String Subject_Source_System_Record_Id;
//		}
		
//		//optional : not used by data service endpoints version 2.0
//		public List<LearningResourceKeyword> Learning_Resource_Keywords = new ArrayList<>();
//		public class LearningResourceKeyword {
//			public String Ref_Language_Code;
//			public String Keyword;
//		}
		
//		//optional : not used by data service endpoints version 2.0
//		public List<LearningResourceIntendedEndUserRole> Learning_Resource_Intended_End_User_Roles = new ArrayList<>();
//		public class LearningResourceIntendedEndUserRole {
//			public String End_User_Role_Code;
//		}
		
//		//optional : not used by data service endpoints version 2.0
//		public boolean Is_Precurated_Flag;
		
//		//optional : not used by data service endpoints version 2.0
//		public String Public_Private_Sharing_Code;
		
//		//optional : not used by data service endpoints version 2.0
//		public float Resource_Count;
		
//		//optional : not used by data service endpoints version 2.0
//		public float Chapter_Count;
		
//		//optional : not used by data service endpoints version 2.0
//		public float Page_Count;
		
//		//optional : not used by data service endpoints version 2.0
//		public String Ref_Learning_Resource_Content_Model_Type_Code;
		
//		//optional : not used by data service endpoints version 2.0
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
		
	}
	
	public class LearningResourceSubtypeMsgPayload extends LearningResourceMsgPayload {
		//optional : learningResource.getSubType()
		public String Ref_Learning_Resource_Subtype_Code;
	}
	
	public class LearningResourceEmbeddedQuestionMsgPayload extends LearningResourceSubtypeMsgPayload {
		//optional : if LearningResource is EmbeddedQuestion, use page.getLearningResourceId()
		public String Container_Content_Source_System_Id;
	}
}
