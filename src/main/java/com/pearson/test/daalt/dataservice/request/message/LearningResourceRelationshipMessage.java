package com.pearson.test.daalt.dataservice.request.message;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LearningResource;

public class LearningResourceRelationshipMessage extends SubPubMessage {
	private boolean parentLearningResourceIsBook;
	
	public LearningResourceRelationshipMessage(boolean parentLearningResourceIsBook){
		this.parentLearningResourceIsBook = parentLearningResourceIsBook;
		payload = new LearningResourceRelationshipMsgPayload();
		LearningResourceRelationshipMsgPayload specificPayload = (LearningResourceRelationshipMsgPayload) payload;
		specificPayload.Parent_Source_System_Code = parentLearningResourceIsBook ? "EPS" : "Revel";
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		LearningResourceRelationshipMsgPayload specificPayload = (LearningResourceRelationshipMsgPayload) payload;
		switch(propertyName) {
		case "CourseSection":
			CourseSection coursection = (CourseSection) propertyValue;
			specificPayload.Learning_Resource_Relationship_Context_Id = coursection.getLearningResourceContextId();
			if (parentLearningResourceIsBook) {
				specificPayload.Parent_Source_System_Record_Id = coursection.getBookId();
			}
			break;
		case "LearningResource": 
			LearningResource learningResource =  (LearningResource) propertyValue;
			specificPayload.Sequence_Number = learningResource.getLearningResourceSequenceNumber();
			specificPayload.Child_Source_System_Record_Id = learningResource.getLearningResourceId();
			break;
		case "ParentLearningResource": 
			LearningResource parentLearningResource =  (LearningResource) propertyValue;
			if (!parentLearningResourceIsBook) {
				specificPayload.Parent_Source_System_Record_Id = parentLearningResource.getLearningResourceId();
			}
			break;
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : " + propertyName);
		}
	}
	
	@Override
	public String getMissingCriticalPropertyName() {
		if (payload.Transaction_Type_Code == null) {
			return "LearningResourceRelationshipMsgPayload.Transaction_Type_Code";
		}
		if (payload.Transaction_Datetime == null) {
			return "LearningResourceRelationshipMsgPayload.Transaction_Datetime";
		}
		LearningResourceRelationshipMsgPayload specificPayload = (LearningResourceRelationshipMsgPayload) payload;
		if(specificPayload.Message_Type_Code == null){
			return "LearningResourceRelationshipMsgPayload.Message_Type_Code";
		}
		if(specificPayload.Originating_System_Code == null){
			return "LearningResourceRelationshipMsgPayload.Originating_System_Code";
		}
		if(specificPayload.Message_Version == null){
			return "LearningResourceRelationshipMsgPayload.Message_Version";
		}
		if(specificPayload.Ref_Relationship_Context_Code == null){
			return "LearningResourceRelationshipMsgPayload.Ref_Relationship_Context_Code";
		}
		if(specificPayload.Learning_Resource_Relationship_Context_Id == null){
			return "LearningResourceRelationshipMsgPayload.Learning_Resource_Relationship_Context_Id";
		}
		if(specificPayload.Parent_Source_System_Code == null){
			return "LearningResourceRelationshipMsgPayload.Parent_Source_System_Code";
		}
		if(specificPayload.Parent_Source_System_Record_Id == null){
			return "LearningResourceRelationshipMsgPayload.Parent_Source_System_Record_Id";
		}
		if(specificPayload.Ref_Learning_Resource_Relationship_Type_Code == null){
			return "LearningResourceRelationshipMsgPayload.Ref_Learning_Resource_Relationship_Type_Code";
		}
		if(specificPayload.Child_Source_System_Code == null){
			return "LearningResourceRelationshipMsgPayload.Child_Source_System_Code";
		}
		if(specificPayload.Sequence_Number == -1){
			return "LearningResourceRelationshipMsgPayload.Sequence_Number";
		}
		if(specificPayload.Child_Source_System_Record_Id == null){
			return "LearningResourceRelationshipMsgPayload.Child_Source_System_Record_Id";
		}
		return null;
	}

	public class LearningResourceRelationshipMsgPayload extends MessagePayload {
		//required : hard code
		public String Message_Type_Code = "Learning_Resource_Relationship"; 
		//optional : hard code, never allow override
		public String Originating_System_Code = "DAALTTest";
		//required : hard code
		public String Message_Version = "2.0";
		//required : hard code
		public String Ref_Relationship_Context_Code = "Book";
		//required : courseSection.getBookId()
		public String Learning_Resource_Relationship_Context_Id; 
		//required : "EPS" if book, "Revel" if not book
		public String Parent_Source_System_Code;
		//required : parentLearningResource.getLearningResourceId()
		public String Parent_Source_System_Record_Id;
		//required : hard code
		public String Ref_Learning_Resource_Relationship_Type_Code = "IsPartOf";
		//required : hard code
		public String Child_Source_System_Code = "Revel";
		//required : learningResource.getLearningResourceSequenceNumber()
		public float Sequence_Number = -1;
		//required : learningResource.getLearningResourceId()
		public String Child_Source_System_Record_Id;
		
//		//optional : not used by data service endpoints version 2.0
//		public String Eff_From_Date;
		
//		//optional : not used by data service endpoints version 2.0
//		public String Eff_Thru_Date;
		
//		//optional : not used by data service endpoints version 2.0
//		public String Explanatory_Notes;
		
//		//optional : not used by data service endpoints version 2.0
//		public String Person_Source_System_Code;
		
//		//optional : not used by data service endpoints version 2.0
//		public String Person_Source_System_Record_Id;
		
//		//optional : not used by data service endpoints version 2.0
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
	}
}
