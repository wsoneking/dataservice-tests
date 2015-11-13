package com.pearson.test.daalt.dataservice.request.message.version01;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LearningResource;
import com.pearson.test.daalt.dataservice.model.Quiz;

public class RevelLmContentMessage extends SubPubMessage {
	//revel.lm.content.create
	private boolean contentIsQuiz;
	private boolean learningResourceHasSubtype;

	public RevelLmContentMessage(boolean contentIsQuiz, boolean learningResourceHasSubtype) {
		this.contentIsQuiz = contentIsQuiz;
		this.learningResourceHasSubtype = learningResourceHasSubtype;
		if (contentIsQuiz && learningResourceHasSubtype) {
			payload = new RevelAssessmentLmContentMsgWithSubtypePayload();
		} else if (contentIsQuiz && !learningResourceHasSubtype) {
			payload = new RevelAssessmentLmContentMsgPayload();
		} else if (!contentIsQuiz && learningResourceHasSubtype) {
			payload = new RevelLmContentMsgWithSubtypePayload();
		} else {
			payload = new RevelLmContentMsgPayload();
		}
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException {
		RevelLmContentMsgPayload specificPayload = (RevelLmContentMsgPayload) payload;
		switch(propertyName) {
			case "Transaction_Type_Code":
				specificPayload.Transaction_Type_Code = propertyValue.toString();
				break;
			case "CourseSection": 
				CourseSection section =  (CourseSection) propertyValue;
				specificPayload.Course_Section_Source_System_Record_Id = section.getId();
				break;
			case "Assignment": 
				Assignment assignment =  (Assignment) propertyValue;
				specificPayload.Learning_Module_Source_System_Record_Id = assignment.getId();
				specificPayload.Assignment_Due_Datetime = assignment.getDueDateAsString();
				specificPayload.Assignment_Max_Points = assignment.getPointsPossible();
				break;
			case "LearningResource": 
				LearningResource learningResource =  (LearningResource) propertyValue;
				RevelAssessmentLmContentMsgWithSubtypePayload assessmentSubtypePayload;
				RevelAssessmentLmContentMsgPayload assessmentPayload;
				RevelLmContentMsgWithSubtypePayload subtypePayload;
				if (contentIsQuiz && learningResourceHasSubtype) {
					assessmentSubtypePayload = (RevelAssessmentLmContentMsgWithSubtypePayload) payload;
					assessmentSubtypePayload.Ref_Learning_Resource_Subtype_Code = learningResource.getLearningResourceSubType();
					Quiz quiz = (Quiz) propertyValue;
					assessmentSubtypePayload.Assessment_Source_System_Code = "PAF";
					assessmentSubtypePayload.Assessment_Source_System_Record_Id = quiz.getId();
				} else if (contentIsQuiz && !learningResourceHasSubtype) {
					assessmentPayload = (RevelAssessmentLmContentMsgPayload) payload;
					Quiz quiz = (Quiz) propertyValue;
					assessmentPayload.Assessment_Source_System_Code = "PAF";
					assessmentPayload.Assessment_Source_System_Record_Id = quiz.getId();
				} else if (!contentIsQuiz && learningResourceHasSubtype) {
					subtypePayload = (RevelLmContentMsgWithSubtypePayload) payload;
					subtypePayload.Ref_Learning_Resource_Subtype_Code = learningResource.getLearningResourceSubType();
				}
				specificPayload.Learning_Resource_Source_System_Code = "Revel";
				specificPayload.Learning_Resource_Source_System_Record_Id = learningResource.getLearningResourceId();
				specificPayload.Sequence_Number = learningResource.getLearningResourceSequenceNumber();
				specificPayload.Ref_Learning_Resource_Type_Code = learningResource.getLearningResourceType();
				specificPayload.Possible_Points = learningResource.getPointsPossible();
				if (learningResource.isPractice()) {
					specificPayload.Scored_Flag = Boolean.FALSE;
					specificPayload.Add_To_Grade_Book_Flag = Boolean.FALSE;
				}
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
		RevelLmContentMsgPayload specificPayload = (RevelLmContentMsgPayload) payload;
		if (specificPayload.Message_Type_Code == null) {
			return "RevelLmContentMsgPayload.Message_Type_Code";
		}
		if (specificPayload.Message_Version == null) {
			return "RevelLmContentMsgPayload.Message_Version";
		}
		if (specificPayload.Learning_Module_Source_System_Code == null) {
			return "RevelLmContentMsgPayload.Learning_Module_Source_System_Code";
		}
		if (specificPayload.Learning_Resource_Source_System_Code == null) {
			return "RevelLmContentMsgPayload.Learning_Resource_Source_System_Code";
		}
		if (specificPayload.Course_Section_Source_System_Code == null) {
			return "RevelLmContentMsgPayload.Course_Section_Source_System_Code";
		}
		if (specificPayload.Learning_Module_Source_System_Record_Id == null) {
			return "RevelLmContentMsgPayload.Learning_Module_Source_System_Record_Id";
		}
		if (specificPayload.Learning_Resource_Source_System_Record_Id == null) {
			return "RevelLmContentMsgPayload.Learning_Resource_Source_System_Record_Id";
		}
		if (specificPayload.Course_Section_Source_System_Record_Id == null) {
			return "RevelLmContentMsgPayload.Course_Section_Source_System_Record_Id";
		}
		if (specificPayload.Possible_Points == null) {
			return "RevelLmContentMsgPayload.Possible_Points";
		}
		if (contentIsQuiz) {
			RevelAssessmentLmContentMsgPayload assessmentPayload = (RevelAssessmentLmContentMsgPayload) payload;
			if (assessmentPayload.Assessment_Source_System_Code == null) {
				return "RevelLmContentMsgPayload.Assessment_Source_System_Code";
			}
			if (assessmentPayload.Assessment_Source_System_Record_Id == null) {
				return "RevelLmContentMsgPayload.Assessment_Source_System_Record_Id";
			}
		}
		return null;
	}

	public class RevelLmContentMsgPayload extends MessagePayload {
		//required : hard code, never allow override
		public String Message_Type_Code = "Learning_Module_Content";
		//required : hard code? need to figure out how to do message versioning
		public String Message_Version = "1.0";
		//required : hard code, never allow override
		public String Learning_Module_Source_System_Code = "Revel";
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Learning_Module_Source_System_Record_Id;
		//required : hard code, never allow override
		public String Learning_Resource_Source_System_Code = "EPS";
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Learning_Resource_Source_System_Record_Id;
		//required : hard code, never allow override
		public String Course_Section_Source_System_Code = "Registrar";
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Course_Section_Source_System_Record_Id;
		//required : defined along with object, object passed to TestAction
		public Float Sequence_Number = -1f;
		//optional : defined along with object, object passed to TestAction
		public String Assignment_Due_Datetime;
		//optional : defined along with object, object passed to TestAction
		public Float Assignment_Max_Points = 0f;
		//optional : TRUE if learning resource is assessment, null otherwise
		public Boolean Scored_Flag = true;
		//optional : TRUE if learning resource is assessment, null otherwise
		public Boolean Add_To_Grade_Book_Flag = true;
		//optional : defined along with object, object passed to TestAction
		public Float Possible_Points = 0f;
		//optional : generate new every time, value should be now
		public String Creation_Datetime = getCurrentTimeFormatted();
		//optional : defined along with object, object passed to TestAction
		public String Ref_Learning_Resource_Type_Code;
		
/*		
		//optional : always null - assuming this is different from Assignment_Due_Datetime
		public String Assignment_Datetime;		
		//optional : always null
		public Boolean Required_Flag;
		//optional : always null
		public Float Maximum_Time_Allowed;
		//optional : always null
		public String Ref_Time_Units_Code;
		//optional : always null
		public Float Maximum_Attempts_Allowed;
		//optional : always null
		public Float Weight;
		//optional : always null - does not appear in workflow diagrams
		public String Learning_Module_Content_Source_System_Code;
		//optional : always null - does not appear in workflow diagrams
		public String Learning_Module_Content_Source_System_Record_Id;
	*/	
	}
	
	public class RevelLmContentMsgWithSubtypePayload extends RevelLmContentMsgPayload {
		//optional : defined along with object, object passed to TestAction
		public String Ref_Learning_Resource_Subtype_Code;
	}

	public class RevelAssessmentLmContentMsgPayload extends RevelLmContentMsgPayload {
		//optional : default null, hard code when Assessment_Source_System_Record_Id is not null, never allow override
		public String Assessment_Source_System_Code;
		//optional : must be unique, generated fresh when object created, object passed to TestAction
		public String Assessment_Source_System_Record_Id;
	}
	
	public class RevelAssessmentLmContentMsgWithSubtypePayload extends RevelAssessmentLmContentMsgPayload {
		//optional : defined along with object, object passed to TestAction
		public String Ref_Learning_Resource_Subtype_Code;
	}
}
