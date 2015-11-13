package com.pearson.test.daalt.dataservice.request.message;

import java.util.Date;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;

public class LearningModuleMessage extends SubPubMessage {
	//revel.lm.create
	
	public LearningModuleMessage() {
		payload = new LearningModuleMsgPayload();
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException {
		LearningModuleMsgPayload specificPayload = (LearningModuleMsgPayload) payload;
		switch(propertyName) {
		case "Transaction_Type_Code":
			specificPayload.Transaction_Type_Code = propertyValue.toString();
			break;
		case "CourseSection": 
			CourseSection section =  (CourseSection) propertyValue;
			specificPayload.Course_Section_Source_System_Record_Id = section.getId();
			break;
		case "Instructor":
			Instructor instructor = (Instructor) propertyValue;
			specificPayload.Instructor_Source_System_Record_Id = instructor.getPersonId();
			break;
		case "Assignment":
			Assignment assignment = (Assignment) propertyValue;
			specificPayload.Title = assignment.getTitle();
			specificPayload.Activity_Due_Datetime = assignment.getDueDateAsString();
			specificPayload.Learning_Module_Source_System_Record_Id = assignment.getId();
			specificPayload.Possible_Points = assignment.getPointsPossible();
			specificPayload.Sequence_Number = assignment.getSequenceNumber();
			specificPayload.Include_In_Trending_Flag = assignment.isDueDatePassed();
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
		LearningModuleMsgPayload specificPayload = (LearningModuleMsgPayload) payload;
		if (specificPayload.Message_Type_Code == null) {
			return "RevelLmMsgPayload.Message_Type_Code";
		}
		if (specificPayload.Originating_System_Code == null) {
			return "RevelLmMsgPayload.Originating_System_Code";
		}
		if (specificPayload.Message_Version == null) {
			return "RevelLmMsgPayload.Message_Version";
		}
		if (specificPayload.Learning_Module_Source_System_Code == null) {
			return "RevelLmMsgPayload.Learning_Module_Source_System_Code";
		}
		if (specificPayload.Instructor_Source_System_Code == null) {
			return "RevelLmMsgPayload.Instructor_Source_System_Code";
		}
		if (specificPayload.Course_Section_Source_System_Code == null) {
			return "RevelLmMsgPayload.Course_Section_Source_System_Code";
		}
		if (specificPayload.Creation_Datetime == null) {
			return "RevelLmMsgPayload.Creation_Datetime";
		}
		if (specificPayload.Learning_Module_Source_System_Record_Id == null) {
			return "RevelLmMsgPayload.Learning_Module_Source_System_Record_Id";
		}
		if (specificPayload.Instructor_Source_System_Record_Id == null) {
			return "RevelLmMsgPayload.Instructor_Source_System_Record_Id";
		}
		if (specificPayload.Course_Section_Source_System_Record_Id == null) {
			return "RevelLmMsgPayload.Course_Section_Source_System_Record_Id";
		}
		if (specificPayload.Activity_Due_Datetime == null) {
			return "RevelLmMsgPayload.Activity_Due_Datetime";
		}
		if (specificPayload.Possible_Points == null) {
			return "RevelLmMsgPayload.Possible_Points";
		}
		if (specificPayload.Title == null) {
			return "RevelLmMsgPayload.Title";
		}
		if (specificPayload.Ref_Learning_Module_Type_Code == null) {
			return "RevelLmMsgPayload.Ref_Learning_Module_Type_Code";
		}
		return null;
	}
	
	public class LearningModuleMsgPayload extends MessagePayload {
		// required : hard code, never allow override
		public String Message_Type_Code = "Learning_Module";
		// optional : hard code
		public String Originating_System_Code = "DAALTTest";	
		// required : hard code? need to figure out how to do message versioning
		public String Message_Version = "2.0";
		// required : hard code generate date here.
		public String Creation_Datetime = getFormattedDateString(new Date());
		// optional : hard code generate date here 
		public String Identifying_Code = getFormattedDateString(new Date());
		// required : hard code here
		public String Learning_Module_Source_System_Code = "Revel";
		// required : caller sets value
		public String Learning_Module_Source_System_Record_Id;
		// required : hard code  
		public String Course_Section_Source_System_Code = "Registrar";
		// required : caller sets value;
		public String Course_Section_Source_System_Record_Id;
		// required : hard code
		public String Instructor_Source_System_Code = "PI";
		// required : caller sets value. 
		public String Instructor_Source_System_Record_Id;
		// optional : Revel will populate this with the Timestamp of the Activity_Due_Datetime. This will enable the system to use this field as part of the Trending calculation."
		public float Sequence_Number ;
		// required : defined along with object, object passed to TestAction
		public String Title;
		// required : default to false. set to true if this is an "Update" message intended to simulate the due date passing
		public boolean Include_In_Trending_Flag = false;
		// required : hard code, generate due date
		public String Activity_Due_Datetime = getPastOrFutureTimeFormatted(5);
		// optional : hard code, generate
		public String Release_Datetime = getCurrentTimeFormatted();
		// optional : defined along with object, object passed to TestAction
		public Float Possible_Points;
		// required : default value, introduce override to allow for other types of learning modules  
		public String Ref_Learning_Module_Type_Code = "Assignment";
		// optional : hard code. 
		public String Description = "This module covers the main schools of psychology";
		// optional : // hard code here -Bob
		public String Learning_Module_Version = "1.2";		
		// optional : hard code, default
		public float Maximum_Time_Allowed = 10;
		// optional : hard code. 
		public String Ref_Time_Units_Code = "Hour";
		// optional : // hard code is OK -Bob
		public float Weight = 20;		
		// optional : hard code
		public String Ref_Language_Code = "en-US";
		
//		//optional : not used by data service endpoints version 2.0
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
		
/*		
		// optional :
		public List<LearningModuleKeyword> Learning_Module_Keywords;
		public class LearningModuleKeyword {
			// required : "enum": ["en-US","en-GB"]	 hard code
			public String Ref_Language_Code = "en-US";
			// required : hard code
			public String Keyword = "LearningModuleKeyword"; 
		}
		
		// optional :
		public List<LearningModuleURL> Learning_Module_URLs = new ArrayList<>();
		public class LearningModuleURL {
			// required : enum 
			public String URL_Type_Code = "RubricURL";
			// required : hard code		TODO can we hard code here?
			public String URL = "www.GreatRubrics.com";
		}
*/
	
/*
	
		// optional : You can leave blank for Revel. - Bob
		public String Based_On_Learning_Resource_Source_System_Record_Id;
	
 		// optional : You can leave blank for Revel. - Bob
		public List<LearningModuleSubject> Learning_Module_Subject;
		public class LearningModuleSubject {
			// required : hard code
			public String Subject_Source_System_Code = "Collection";
			// required : TODO "Source system record id records the GUID of the Subject record in the domain identified by Subject_Source_System_Code."
			public String Subject_Source_System_Record_Id;
		}
		// optional : You can leave blank for Revel. - Bob
		public List<BaseOnLRHierarchy> Based_On_Learning_Resource_Hierarchy = new ArrayList<>();
		public class BaseOnLRHierarchy {
			// required : TODO "Source system record id of the Learning Resource (which in this case is a 'Collection')"
			public String Learning_Resource_Source_System_Record_Id;
			// required : "enum": [ "EPS","Collections", "Other" ]
			public String Learning_Resource_Source_System_Code = "Collections";
			// required : "enum": ["Collection","CollectionModule", "Book","Title","Module","Section","Card","Assessment","Video"     ]
			public String Ref_Learning_Resource_Type_Code; // TODO
		}
*/
	}

}
