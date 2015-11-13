package com.pearson.test.daalt.dataservice.request.message;

import java.util.ArrayList;
import java.util.List;

import com.pearson.test.daalt.dataservice.model.CourseSection;

public class CourseSectionLearningResourceMessage extends SubPubMessage {
	//revel.course.lr.create

	public CourseSectionLearningResourceMessage() {
		payload = new CourseSectionLearningResourceMsgPayload();
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException {
		CourseSectionLearningResourceMsgPayload specificPayload = (CourseSectionLearningResourceMsgPayload) payload;
		switch(propertyName) {
		case "CourseSection": 
			CourseSection section = (CourseSection) propertyValue;
			specificPayload.Course_Section_Source_System_Record_Id = section.getId(); 
			specificPayload.Learning_Resource_Relationship_Context_Id = section.getBookId();
			specificPayload.Learning_Resource_Source_System_Record_Id = section.getBookId();
			break;
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : " + propertyName);
		}
	}
	
	@Override
	public String getMissingCriticalPropertyName() {
		if (payload.Transaction_Type_Code == null) {
			return "CourseSectionLearningResourceMsgPayload.Transaction_Type_Code";
		}
		CourseSectionLearningResourceMsgPayload specificPayload = (CourseSectionLearningResourceMsgPayload) payload;
		if (specificPayload.Message_Type_Code == null) {
			return "CourseSectionLearningResourceMsgPayload.Message_Type_Code";
		}
		if (specificPayload.Originating_System_Code == null) {
			return "CourseSectionLearningResourceMsgPayload.Originating_System_Code";
		}
		if (specificPayload.Message_Version == null) {
			return "CourseSectionLearningResourceMsgPayload.Message_Version";
		}
		if (specificPayload.Course_Section_Source_System_Code == null) {
			return "CourseSectionLearningResourceMsgPayload.Course_Section_Source_System_Code";
		}
		if (specificPayload.Course_Section_Source_System_Record_Id == null) {
			return "CourseSectionLearningResourceMsgPayload.Course_Section_Source_System_Record_Id";
		}
		if (specificPayload.Ref_Relationship_Context_Code == null) {
			return "CourseSectionLearningResourceMsgPayload.Ref_Relationship_Context_Code";
		}
		if (specificPayload.Learning_Resource_Relationship_Context_Id == null) {
			return "CourseSectionLearningResourceMsgPayload.Learning_Resource_Relationship_Context_Id";
		}
		if (specificPayload.Learning_Resource_Source_System_Code == null) {
			return "CourseSectionLearningResourceMsgPayload.Learning_Resource_Source_System_Code";
		}
		if (specificPayload.Learning_Resource_Source_System_Record_Id == null) {
			return "CourseSectionLearningResourceMsgPayload.Learning_Resource_Source_System_Record_Id";
		}
		return null;
	}
	
	protected class CourseSectionLearningResourceMsgPayload extends MessagePayload {
		//required : hard code, never allow override
		public String Message_Type_Code = "Course_Section_To_Learning_Resource";
		//required : hard code, never allow override
		public String Originating_System_Code = "DAALTTest";
		//required : hard code
		public String Message_Version = "2.0";
		//required : hard code, never allow override
		public String Course_Section_Source_System_Code = "Registrar";
		//required : courseSection.getId()
		public String Course_Section_Source_System_Record_Id;
		//required : hard code
		public String Ref_Relationship_Context_Code = "Book";
		//required : courseSection.getBookId()
		public String Learning_Resource_Relationship_Context_Id;
		//required : hard code, never allow override
		public String Learning_Resource_Source_System_Code = "EPS";
		//required : courseSection.getBookId() - modify this strategy to allow for attaching something other than a book to the course section
		public String Learning_Resource_Source_System_Record_Id;
		
//		//optional : always null
//		public String Course_Source_System_Code;

//		//optional : always null
//		public String Course_Source_System_Record_Id;	
		
//		//optional : not used by data service endpoints version 2.0
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
	}
}
