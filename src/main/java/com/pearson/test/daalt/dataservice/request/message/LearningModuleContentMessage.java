package com.pearson.test.daalt.dataservice.request.message;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.EmbeddedQuestion;
import com.pearson.test.daalt.dataservice.model.LearningResource;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.request.message.version01.RevelLmContentMessage.RevelAssessmentLmContentMsgPayload;
import com.pearson.test.daalt.dataservice.request.message.version01.RevelLmContentMessage.RevelLmContentMsgPayload;

public class LearningModuleContentMessage extends SubPubMessage {
	//revel.lm.content.create
	private boolean contentIsQuiz;
	private boolean contentIsEmbeddedQuestion;
	private boolean learningResourceHasSubtype;

	public LearningModuleContentMessage(boolean learningResourceHasSubtype, boolean contentIsQuiz, boolean contentIsEmbeddedQuestion) {
		this.contentIsQuiz = contentIsQuiz;
		this.contentIsEmbeddedQuestion = contentIsEmbeddedQuestion;
		this.learningResourceHasSubtype = learningResourceHasSubtype;
		if (contentIsEmbeddedQuestion) {
			if (learningResourceHasSubtype) {
				payload = new AssessmentItemLearningModuleContentSubtypeMsgPayload();
			} else {
				payload = new AssessmentItemLearningModuleContentMsgPayload();
			}
		} else if (contentIsQuiz) {
			if (learningResourceHasSubtype) {
				payload = new AssessmentLearningModuleContentSubtypeMsgPayload();
			} else {
				payload = new AssessmentLearningModuleContentMsgPayload();
			}
		} else {
			if (learningResourceHasSubtype) {
				payload = new LearningModuleContentSubtypeMsgPayload();
			} else {
				payload = new LearningModuleContentMsgPayload();
			}
		}
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException {
		LearningModuleContentMsgPayload specificPayload = (LearningModuleContentMsgPayload) payload;
		switch(propertyName) {
			case "Transaction_Type_Code":
				specificPayload.Transaction_Type_Code = propertyValue.toString();
				break;
			case "CourseSection": 
				CourseSection section =  (CourseSection) propertyValue;
				specificPayload.Course_Section_Source_System_Record_Id = section.getId();
				specificPayload.Learning_Resource_Relationship_Context_Id = section.getLearningResourceContextId();
				break;
			case "Assignment": 
				Assignment assignment =  (Assignment) propertyValue;
				specificPayload.Learning_Module_Source_System_Record_Id = assignment.getId();
				specificPayload.Assignment_Due_Datetime = assignment.getDueDateAsString();
				specificPayload.Assignment_Max_Points = assignment.getPointsPossible();
				break;
			case "LearningResource": 
				LearningResource learningResource =  (LearningResource) propertyValue;
				specificPayload.Learning_Resource_Source_System_Record_Id = learningResource.getLearningResourceId();
				specificPayload.Sequence_Number = learningResource.getLearningResourceSequenceNumber();
				specificPayload.Ref_Learning_Resource_Type_Code = learningResource.getLearningResourceType();
				if (learningResourceHasSubtype) {
					if (contentIsEmbeddedQuestion) {
						AssessmentItemLearningModuleContentSubtypeMsgPayload subtypePayload = (AssessmentItemLearningModuleContentSubtypeMsgPayload) payload;
						subtypePayload.Ref_Learning_Resource_Subtype_Code = learningResource.getLearningResourceSubType();
					} else if (contentIsQuiz) {
						AssessmentLearningModuleContentSubtypeMsgPayload subtypePayload = (AssessmentLearningModuleContentSubtypeMsgPayload) payload;
						subtypePayload.Ref_Learning_Resource_Subtype_Code = learningResource.getLearningResourceSubType();
					} else {
						LearningModuleContentSubtypeMsgPayload subtypePayload = (LearningModuleContentSubtypeMsgPayload) payload;
						subtypePayload.Ref_Learning_Resource_Subtype_Code = learningResource.getLearningResourceSubType();
					}
				}
				if (learningResource.isPractice()) {
					//PracticePointsPossible or pointsPossible is indicated by Include_Points_In_Student_Grade_Flag field, not any other boolean field - Leo
					specificPayload.Possible_Points = learningResource.getPracticePointsPossible(); 
					specificPayload.Include_Points_In_Student_Grade_Flag = false;
					specificPayload.Scored_Flag = false; //TODO: next release, this should be false if pointsPossible = null
					specificPayload.Add_To_Grade_Book_Flag = false;
				} else {
					specificPayload.Possible_Points = learningResource.getPointsPossible();
				}
				if (contentIsQuiz) {
					Quiz quiz = (Quiz) propertyValue;
					if (learningResourceHasSubtype) {
						AssessmentLearningModuleContentSubtypeMsgPayload assessmentPayload = (AssessmentLearningModuleContentSubtypeMsgPayload) payload;
						assessmentPayload.Assessment_Source_System_Record_Id = quiz.getId();
					} else {
						AssessmentLearningModuleContentMsgPayload assessmentPayload = (AssessmentLearningModuleContentMsgPayload) payload;
						assessmentPayload.Assessment_Source_System_Record_Id = quiz.getId();
					}
				} else if (contentIsEmbeddedQuestion) {
					EmbeddedQuestion question = (EmbeddedQuestion) propertyValue;
					if (learningResourceHasSubtype) {
						AssessmentItemLearningModuleContentSubtypeMsgPayload assessmentItemPayload = (AssessmentItemLearningModuleContentSubtypeMsgPayload) payload;
						assessmentItemPayload.Assessment_Source_System_Record_Id = question.getAssessmentId();
						assessmentItemPayload.Assessment_Item_Source_System_Record_Id = question.getItemId();
					} else {
						AssessmentItemLearningModuleContentMsgPayload assessmentItemPayload = (AssessmentItemLearningModuleContentMsgPayload) payload;
						assessmentItemPayload.Assessment_Source_System_Record_Id = question.getAssessmentId();
						assessmentItemPayload.Assessment_Item_Source_System_Record_Id = question.getItemId();
					}
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
		LearningModuleContentMsgPayload specificPayload = (LearningModuleContentMsgPayload) payload;
		if (specificPayload.Message_Type_Code == null) {
			return "LearningModuleContentMsgPayload.Message_Type_Code";
		}
		if (specificPayload.Message_Version == null) {
			return "LearningModuleContentMsgPayload.Message_Version";
		}
		if (specificPayload.Learning_Module_Source_System_Code == null) {
			return "LearningModuleContentMsgPayload.Learning_Module_Source_System_Code";
		}
		if (specificPayload.Learning_Resource_Source_System_Code == null) {
			return "LearningModuleContentMsgPayload.Learning_Resource_Source_System_Code";
		}
		if (specificPayload.Course_Section_Source_System_Code == null) {
			return "LearningModuleContentMsgPayload.Course_Section_Source_System_Code";
		}
		if (specificPayload.Learning_Module_Source_System_Record_Id == null) {
			return "LearningModuleContentMsgPayload.Learning_Module_Source_System_Record_Id";
		}
		if (specificPayload.Learning_Resource_Source_System_Record_Id == null) {
			return "LearningModuleContentMsgPayload.Learning_Resource_Source_System_Record_Id";
		}
		if (specificPayload.Course_Section_Source_System_Record_Id == null) {
			return "LearningModuleContentMsgPayload.Course_Section_Source_System_Record_Id";
		}
		if (specificPayload.Possible_Points == null) {
			return "LearningModuleContentMsgPayload.Possible_Points";
		}
		if (contentIsQuiz) {
			if (learningResourceHasSubtype) {
				AssessmentLearningModuleContentSubtypeMsgPayload assessmentPayload = (AssessmentLearningModuleContentSubtypeMsgPayload) payload;
				if (assessmentPayload.Assessment_Source_System_Code == null) {
					return "AssessmentLearningModuleContentSubtypeMsgPayload.Assessment_Source_System_Code";
				}
				if (assessmentPayload.Assessment_Source_System_Record_Id == null) {
					return "AssessmentLearningModuleContentSubtypeMsgPayload.Assessment_Source_System_Record_Id";
				}
				if (assessmentPayload.Ref_Learning_Resource_Subtype_Code == null) {
					return "AssessmentLearningModuleContentSubtypeMsgPayload.Ref_Learning_Resource_Subtype_Code";
				}
			} else {
				AssessmentLearningModuleContentMsgPayload assessmentPayload = (AssessmentLearningModuleContentMsgPayload) payload;
				if (assessmentPayload.Assessment_Source_System_Code == null) {
					return "AssessmentLearningModuleContentSubtypeMsgPayload.Assessment_Source_System_Code";
				}
				if (assessmentPayload.Assessment_Source_System_Record_Id == null) {
					return "AssessmentLearningModuleContentSubtypeMsgPayload.Assessment_Source_System_Record_Id";
				}
			}
		} else if (contentIsEmbeddedQuestion) {
			if (learningResourceHasSubtype) {
				AssessmentItemLearningModuleContentSubtypeMsgPayload assessmentPayload = (AssessmentItemLearningModuleContentSubtypeMsgPayload) payload;
				if (assessmentPayload.Assessment_Source_System_Code == null) {
					return "AssessmentItemLearningModuleContentSubtypeMsgPayload.Assessment_Source_System_Code";
				}
				if (assessmentPayload.Assessment_Source_System_Record_Id == null) {
					return "AssessmentItemLearningModuleContentSubtypeMsgPayload.Assessment_Source_System_Record_Id";
				}
				if (assessmentPayload.Assessment_Item_Source_System_Code == null) {
					return "AssessmentItemLearningModuleContentSubtypeMsgPayload.Assessment_Item_Source_System_Code";
				}
				if (assessmentPayload.Assessment_Item_Source_System_Record_Id == null) {
					return "AssessmentItemLearningModuleContentSubtypeMsgPayload.Assessment_Item_Source_System_Record_Id";
				}
				if (assessmentPayload.Ref_Learning_Resource_Subtype_Code == null) {
					return "AssessmentItemLearningModuleContentSubtypeMsgPayload.Ref_Learning_Resource_Subtype_Code";
				}
			} else {
				AssessmentItemLearningModuleContentMsgPayload assessmentPayload = (AssessmentItemLearningModuleContentMsgPayload) payload;
				if (assessmentPayload.Assessment_Source_System_Code == null) {
					return "AssessmentItemLearningModuleContentMsgPayload.Assessment_Source_System_Code";
				}
				if (assessmentPayload.Assessment_Source_System_Record_Id == null) {
					return "AssessmentItemLearningModuleContentMsgPayload.Assessment_Source_System_Record_Id";
				}
				if (assessmentPayload.Assessment_Item_Source_System_Code == null) {
					return "AssessmentItemLearningModuleContentMsgPayload.Assessment_Item_Source_System_Code";
				}
				if (assessmentPayload.Assessment_Item_Source_System_Record_Id == null) {
					return "AssessmentItemLearningModuleContentMsgPayload.Assessment_Item_Source_System_Record_Id";
				}
			}
		}
		return null;
	}

	public class LearningModuleContentMsgPayload extends MessagePayload {
		//required : hard code, never allow override
		public String Message_Type_Code = "Learning_Module_Content";
		//required : hard code, never allow override
		public String Message_Version = "2.0";
		//optional : hard code, never allow override
		public String Originating_System_Code = "DAALTTest";
		//required : hard code, never allow override
		public String Learning_Module_Source_System_Code = "Revel";
		//required : hard code, never allow override
		public String Learning_Resource_Source_System_Code = "EPS";
		//required : hard code, never allow override
		public String Course_Section_Source_System_Code = "Registrar";
		//optional : generate new every time, value should be now
		public String Creation_Datetime = getCurrentTimeFormatted();
		
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Learning_Module_Source_System_Record_Id;
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Learning_Resource_Source_System_Record_Id;
		//required : must be unique, generated fresh when object created, object passed to TestAction
		public String Course_Section_Source_System_Record_Id;
		//required : defined along with object, object passed to TestAction
		public Float Sequence_Number = -1f;
		//required : hard code, introduce override to allow scenarios tailored for Collections
		public String Ref_Relationship_Context_Code = "Book";
		//required : courseSection.getBookId()
		public String Learning_Resource_Relationship_Context_Id;
		//optional : defined along with object, object passed to TestAction
		public Float Possible_Points = 0f;
		//optional : defined along with object, object passed to TestAction
		public String Assignment_Due_Datetime;
		//optional : defined along with object, object passed to TestAction
		public Float Assignment_Max_Points = 0f;
		//optional : TRUE if learning resource is assessment, null otherwise
		public Boolean Scored_Flag = true;
		//optional : TRUE if learning resource is assessment, null otherwise
		public Boolean Add_To_Grade_Book_Flag = true;
		//optional : TRUE if learning resource is assessment, null otherwise
		public Boolean Include_Points_In_Student_Grade_Flag = true;
		//optional : defined along with object, object passed to TestAction
		public String Ref_Learning_Resource_Type_Code;	
		
//		//optional : not used by data service endpoints version 2.0
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
		
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
	
	public class LearningModuleContentSubtypeMsgPayload extends LearningModuleContentMsgPayload {
		//optional : learningResource.getLearningResourceSubType()
		public String Ref_Learning_Resource_Subtype_Code;	
	}

	public class AssessmentLearningModuleContentMsgPayload extends LearningModuleContentMsgPayload {
		//optional : default null, hard code when Assessment_Source_System_Record_Id is not null, never allow override
		public String Assessment_Source_System_Code = "PAF";
		//optional : must be unique, generated fresh when object created, object passed to TestAction
		public String Assessment_Source_System_Record_Id;
	}
	
	public class AssessmentLearningModuleContentSubtypeMsgPayload extends AssessmentLearningModuleContentMsgPayload {
		//optional : defined along with object, object passed to TestAction
		public String Ref_Learning_Resource_Subtype_Code;
	}
	
	public class AssessmentItemLearningModuleContentMsgPayload extends AssessmentLearningModuleContentMsgPayload {
		//optional : default null, hard code when Assessment_Item_Source_System_Record_Id is not null, never allow override
		public String Assessment_Item_Source_System_Code = "PAF";
		//optional : must be unique, generated fresh when object created, object passed to TestAction
		public String Assessment_Item_Source_System_Record_Id;
	}
	
	public class AssessmentItemLearningModuleContentSubtypeMsgPayload extends AssessmentItemLearningModuleContentMsgPayload {
		//optional : defined along with object, object passed to TestAction
		public String Ref_Learning_Resource_Subtype_Code;
	}
}
