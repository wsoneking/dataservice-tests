package com.pearson.test.daalt.dataservice.request.message;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.User;

public class LearningModulePerformanceMessage extends SubPubMessage {
	private User student;

	public LearningModulePerformanceMessage(User student) {
		this.student = student;
		payload = new LearningModulePerformanceMessagePayload();
		LearningModulePerformanceMessagePayload specificPayload = (LearningModulePerformanceMessagePayload) payload;
		specificPayload.Student_Source_System_Record_Id = student.getPersonId();
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException {
		LearningModulePerformanceMessagePayload specificPayload = (LearningModulePerformanceMessagePayload) payload;
		switch (propertyName) {
		case "Transaction_Type_Code":
			specificPayload.Transaction_Type_Code = propertyValue.toString();
			break;
		case "CompletionOriginator" :
			specificPayload.Ref_Completion_Source_Code = propertyValue.toString();
			break;
		case "CourseSection" :
			CourseSection courseSection = (CourseSection) propertyValue;
			specificPayload.Course_Section_Source_System_Record_Id = courseSection.getId();
			break;
		case "Assignment" :
			Assignment assignment = (Assignment) propertyValue;
			specificPayload.Learning_Module_Source_System_Record_Id = assignment.getId();
			specificPayload.Learning_Module_Points_Possible = assignment.getPointsPossible();
			specificPayload.Learning_Module_Raw_Score = assignment.getPointsEarnedFinal(student);
			break;
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : "
							+ propertyName);
		}
	}

	@Override
	public String getMissingCriticalPropertyName() {
		if (payload.Transaction_Type_Code == null) {
			return "MessagePayload.Transaction_Type_Code";
		}
		LearningModulePerformanceMessagePayload specificPayload = (LearningModulePerformanceMessagePayload) payload;
		if (specificPayload.Message_Type_Code == null) {
			return "LearningModulePerformanceMessagePayload.Message_Type_Code";
		}
		if (specificPayload.Message_Version == null){
			return "LearningModulePerformanceMessagePayload.Message_Version";
		}
		if (specificPayload.Originating_System_Code == null){
			return "LearningModulePerformanceMessagePayload.Originating_System_Code";
		}
		if (specificPayload.Course_Section_Source_System_Code == null){
			return "LearningModulePerformanceMessagePayload.Course_Section_Source_System_Code";
		}
		if (specificPayload.Course_Section_Source_System_Record_Id == null){
			return "LearningModulePerformanceMessagePayload.Course_Section_Source_System_Record_Id";
		}
		if (specificPayload.Student_Source_System_Code == null){
			return "LearningModulePerformanceMessagePayload.Student_Source_System_Code";
		}
		if (specificPayload.Student_Source_System_Record_Id == null){
			return "LearningModulePerformanceMessagePayload.Student_Source_System_Record_Id";
		}
		if (specificPayload.Learning_Module_Source_System_Code == null){
			return "LearningModulePerformanceMessagePayload.Learning_Module_Source_System_Code";
		}
		if (specificPayload.Learning_Module_Source_System_Record_Id == null){
			return "LearningModulePerformanceMessagePayload.Learning_Module_Source_System_Record_Id";
		}
		if (specificPayload.Learning_Module_Completed_Flag == null){
			return "LearningModulePerformanceMessagePayload.Learning_Module_Completed_Flag";
		}
		if (specificPayload.Ref_Completion_Source_Code == null){
			return "LearningModulePerformanceMessagePayload.Ref_Completion_Source_Code";
		}
		if (specificPayload.Learning_Module_Points_Possible == null){
			return "LearningModulePerformanceMessagePayload.Learning_Module_Points_Possible";
		}
		if (specificPayload.Learning_Module_Raw_Score == null){
			return "LearningModulePerformanceMessagePayload.Learning_Module_Raw_Score";
		}
		return null;
	}
	
	public class LearningModulePerformanceMessagePayload extends MessagePayload{
		//required : hard code
		public String Message_Type_Code = "Learning_Module_Performance";
		//optional : hard code
		public String Originating_System_Code = "DAALTTest";
		//required : hard code
		public String Message_Version = "2.0";
		//required : hard code
		public String Student_Source_System_Code = "PI";
		//required : person.getPersonId()
		public String Student_Source_System_Record_Id;
		//required : hard code
		public String Course_Section_Source_System_Code = "Registrar";
		//required : courseSection.getId()
		public String Course_Section_Source_System_Record_Id;
		//required : hard code
		public String Learning_Module_Source_System_Code = "Revel";
		//required : assignment.getId()
		public String Learning_Module_Source_System_Record_Id;
		//required : hard code
		//NOTE: for DDS 2.1 there is no valid use case where this should be false
		public Boolean Learning_Module_Completed_Flag = true;
		//optional : generate new every time, value should be now
		public String Learning_Module_Completion_Datetime = getCurrentTimeFormatted();
		//optional : "System" if sent by SimulateDueDatePassingTestAction. "Student" in all other cases. 
		//NOTE: At the time of coding, this strategy is a best guess. Awaiting confirmation from Product.	
		public String Ref_Completion_Source_Code;
		//required : assignment.getPointsPossible()
		public Float Learning_Module_Points_Possible;
		//required : assignment.getPointsEarnedFinal(student) 
		//NOTE: double-check that this will return the correct value according to requirements in message schema
		public Float Learning_Module_Raw_Score;
		
//		//optional : not used by data service endpoints version 2.1
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
	}
}
