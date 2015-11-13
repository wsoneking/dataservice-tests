package com.pearson.test.daalt.dataservice.request.message;

import java.util.Date;
import java.util.UUID;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuestionCompletionActivity;

public class SimpleWritingQuestionUserAnsweredTincanMessage extends SeerMessage {

	public SimpleWritingQuestionUserAnsweredTincanMessage(boolean isJournalWritingSubClass) {

		messageSource = new SimpleWritingQuestionUserAnsweredTincanMessageSource();
		SimpleWritingQuestionUserAnsweredTincanMessageSource specificMessageSource = (SimpleWritingQuestionUserAnsweredTincanMessageSource) messageSource;
		if (isJournalWritingSubClass) {
			specificMessageSource.context.extensions = specificMessageSource.context.new SimpleWritingQuestionUserAnsweredJournalWritingExtension();
		} else {
			specificMessageSource.context.extensions = specificMessageSource.context.new SimpleWritingQuestionUserAnsweredSharedWritingExtension();
		}

	}

	@Override
	public void setProperty(String propertyName, java.lang.Object propertyValue) throws UnknownPropertyException {
		SimpleWritingQuestionUserAnsweredTincanMessageSource specificMessageSource = (SimpleWritingQuestionUserAnsweredTincanMessageSource) messageSource;

		switch (propertyName) {
		case "CourseSection":
			CourseSection courseSection = (CourseSection) propertyValue;
			specificMessageSource.context.extensions.Course_Section_Source_System_Record_Id = courseSection.getId();
			break;
		case "Quiz":
			Quiz quiz = (Quiz) propertyValue;
			specificMessageSource.context.extensions.Assessment_Source_System_Record_Id = quiz.getId();
			break;
		case "Question":
			Question question = (Question) propertyValue;
			specificMessageSource.context.extensions.Assessment_Item_Source_System_Record_Id = question.getId();
			specificMessageSource.context.extensions.Assessment_Item_Question_Type = question.getQuestionType();
			break;
		case "QuestionCompletionActivity":
			QuestionCompletionActivity questionCompletionActivity = (QuestionCompletionActivity) propertyValue;
			specificMessageSource.actor.account.Person_Source_System_Record_Id = questionCompletionActivity
					.getPersonId();
			specificMessageSource.context.extensions.Person_Role_Code = questionCompletionActivity.getPersonRole();
			specificMessageSource.context.extensions.Client_Side_Answered_Timestamp = questionCompletionActivity.getStringLastActivityDate();

			if (specificMessageSource.context.extensions instanceof SimpleWritingQuestionUserAnsweredTincanMessageSource.Context.SimpleWritingQuestionUserAnsweredSharedWritingExtension) {

				SimpleWritingQuestionUserAnsweredTincanMessageSource.Context.SimpleWritingQuestionUserAnsweredSharedWritingExtension sharedWritingExtensions = (SimpleWritingQuestionUserAnsweredTincanMessageSource.Context.SimpleWritingQuestionUserAnsweredSharedWritingExtension) specificMessageSource.context.extensions;
				sharedWritingExtensions.Item_Response_Raw_Score = questionCompletionActivity.getScore();
				sharedWritingExtensions.Item_Response_Score = questionCompletionActivity.getScore();
			} else {
				SimpleWritingQuestionUserAnsweredTincanMessageSource.Context.SimpleWritingQuestionUserAnsweredJournalWritingExtension journalWritingExtensions = (SimpleWritingQuestionUserAnsweredTincanMessageSource.Context.SimpleWritingQuestionUserAnsweredJournalWritingExtension) specificMessageSource.context.extensions;
				journalWritingExtensions.Item_Response_Pass_Fail = questionCompletionActivity.getPassFailCode().value;
				journalWritingExtensions.Ref_Assessment_Item_Response_Status_Code = questionCompletionActivity.getResponseCode().value;
			}
			break;
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : " + propertyName);
		}

	}

	@Override
	public String getMissingCriticalPropertyName() {
		SimpleWritingQuestionUserAnsweredTincanMessageSource msgSource = (SimpleWritingQuestionUserAnsweredTincanMessageSource) messageSource;
		if (msgSource.Message_Type_Code == null) {
			return "MessageSource.Message_Type_Code";
		}
		if (msgSource.Originating_System_Code == null) {
			return "MessageSource.Originating_System_Code";
		}
		if (msgSource.Message_Version == null) {
			return "MessageSource.Message_Version";
		}
		if (msgSource.id == null) {
			return "MessageSource.id";
		}

		if (msgSource.actor == null) {
			return "MessageSource.actor";
		}
		if (msgSource.verb == null) {
			return "MessageSource.verb";
		}
		if (msgSource.object == null) {
			return "MessageSource.object";
		}
		if (msgSource.context == null) {
			return "MessageSource.context";
		}
		if (msgSource.actor.objectType == null) {
			return "MessageSource.actor.objectType";
		}
		if (msgSource.actor.account == null) {
			return "MessageSource.actor.account";
		}
		if (msgSource.actor.account.Person_Source_System_Record_Id == null) {
			return "MessageSource.actor.account.Person_Source_System_Record_Id";
		}
		if (msgSource.verb == null) {
			return "MessageSource.verb";
		}
		if (msgSource.verb.id == null) {
			return "MessageSource.verb.id";
		}
		if (msgSource.verb.display == null) {
			return "MessageSource.verb.display";
		}
		if (msgSource.verb.display.language == null) {
			return "MessageSource.verb.display.language";
		}
		if (msgSource.verb.display.verb == null) {
			return "MessageSource.verb.display.verb";
		}
		if (msgSource.object == null) {
			return "MessageSource.object";
		}
		if (msgSource.object.objectType == null) {
			return "MessageSource.object.objectType";
		}
		if (msgSource.object.id == null) {
			return "MessageSource.object.id";
		}
		if (msgSource.object.definition == null) {
			return "MessageSource.object.definition";
		}
		if (msgSource.object.definition.type == null) {
			return "MessageSource.object.definition..type";
		}
		if (msgSource.context.extensions == null) {
			return "MessageSource.context.extensions";
		}
		if (msgSource.context.extensions.URL_Prefix_For_Context_Fields == null) {
			return "MessageSource.context.extensions.URL_Prefix_For_Context_Fields";
		}
		if (msgSource.context.extensions.appId == null) {
			return "MessageSource.context.extensions.appId";
		}
		if (msgSource.context.extensions.Course_Section_Source_System_Record_Id == null) {
			return "MessageSource.context.extensions.Course_Section_Source_System_Record_Id";
		}

		if (msgSource.context.extensions.Course_Section_Source_System_Code == null) {
			return "MessageSource.context.extensions.Course_Section_Source_System_Code";
		}
		if (msgSource.context.extensions.Person_Role_Code == null) {
			return "MessageSource.context.extensions.Person_Role_Code";
		}
		if (msgSource.context.extensions.Person_Source_System_Code == null) {
			return "MessageSource.context.extensions.Person_Source_System_Code";
		}
		if (msgSource.context.extensions.Assessment_Source_System_Record_Id == null) {
			return "MessageSource.context.extensions.Assessment_Source_System_Record_Id";
		}
		if (msgSource.context.extensions.Assessment_Source_System_Code == null) {
			return "MessageSource.context.extensions.Assessment_Source_System_Code";
		}
		if (msgSource.context.extensions.Assessment_Item_Source_System_Record_Id == null) {
			return "MessageSource.context.extensions.Assessment_Item_Source_System_Record_Id";
		}
		if (msgSource.context.extensions.Assessment_Item_Source_System_Code == null) {
			return "MessageSource.context.extensions.Assessment_Item_Source_System_Code";
		}
		if (msgSource.context.extensions.User_Work_Type == null) {
			return "MessageSource.context.extensions.User_Work_Type";
		}
		if (msgSource.context.extensions.Assessment_Item_Question_Type == null) {
			return "MessageSource.context.extensions.Assessment_Item_Question_Type";
		}
		if (msgSource.context.extensions.Item_Response_Text == null) {
			return "MessageSource.context.extensions.Item_Response_Text";
		}
		if (msgSource.context.extensions.Client_Side_Accessed_Timestamp == null) {
			return "MessageSource.context.extensions.Client_Side_Accessed_Timestamp";
		}
		if (msgSource.context.extensions.Client_Side_Answered_Timestamp == null) {
			return "MessageSource.context.extensions.Client_Side_Answered_Timestamp";
		}

		if (msgSource.context.extensions instanceof SimpleWritingQuestionUserAnsweredTincanMessageSource.Context.SimpleWritingQuestionUserAnsweredSharedWritingExtension) {
			SimpleWritingQuestionUserAnsweredTincanMessageSource.Context.SimpleWritingQuestionUserAnsweredSharedWritingExtension simpleWritingSharedExtensions = (SimpleWritingQuestionUserAnsweredTincanMessageSource.Context.SimpleWritingQuestionUserAnsweredSharedWritingExtension) msgSource.context.extensions;

			if (simpleWritingSharedExtensions.Item_Response_Raw_Score == -1f) {
				return "MessageSource.context.extensions.Item_Response_Raw_Score";
			}
			if (simpleWritingSharedExtensions.Item_Response_Score == -1f) {
				return "MessageSource.context.extensions.Item_Response_Score";
			}
		}

		else {
			SimpleWritingQuestionUserAnsweredTincanMessageSource.Context.SimpleWritingQuestionUserAnsweredJournalWritingExtension simpleWritingJournalExtensions = (SimpleWritingQuestionUserAnsweredTincanMessageSource.Context.SimpleWritingQuestionUserAnsweredJournalWritingExtension) msgSource.context.extensions;

			if (simpleWritingJournalExtensions.Item_Response_Pass_Fail == null) {
				return "MessageSource.context.extensions.Item_Response_Pass_Fail";
			}
			if (simpleWritingJournalExtensions.Ref_Assessment_Item_Response_Status_Code == null) {
				return "MessageSource.context.extensions.Ref_Assessment_Item_Response_Status_Code";
			}

		}

		return null;
	}

	public class SimpleWritingQuestionUserAnsweredTincanMessageSource extends MessageSource {

		// required : hard code
		public String Message_Type_Code = "Simple_Writing_Question_User_Answered";
		// required : hard code
		public String Originating_System_Code = "DAALTTest";
		// required : hard code
		public String Message_Version = "2.0";
		// required : hard code
		public String Message_Transfer_Type = "LiveStream";
		// required : generate unique
		public String id = "SQE-Tincan-" + UUID.randomUUID().toString();

		public Actor actor = new Actor();

		public class Actor {
			// required : hard code
			public String objectType = "Agent";

			// required : caller sets value
			public Account account = new Account();

			public class Account {
				// required : questionCompletionActivity.getPersonId()
				public String Person_Source_System_Record_Id;
				// required : hard code
				public String homePage = "https://piapi.openclass.com";
			}
		}

		// required
		public Verb verb = new Verb();

		public class Verb {
			// required : hard code
			public String id = "http://adlnet.gov/expapi/verbs/answered";
			// required 
			public Display display = new Display();

			public class Display {
				// required: hard code
				public String language = "en-US";
				// required: hard code
				public String verb = "answered";
			}
		}

		// required
		public Object object = new Object();

		public class Object {

			// required : hard code
			public String objectType = "Activity";
			// required : generate unique
			public String id = "SQE-ObjectId-" + UUID.randomUUID().toString();
			
			// required 
			public Definition definition = new Definition();

			public class Definition {
				// required : hard code
				public String type = "http://adlnet.gov/expapi/activities/question";
			}
		}

		// public Result result;
		/*
		 * public class Result { //optional : caller sets value public String
		 * response; }
		 */
		
		// required
		public Context context = new Context();

		public class Context {
			// required 
			public Extension extensions;

			public class Extension {
				// required: hard code
				public String URL_Prefix_For_Context_Fields = "http://schema.pearson.com/daalt/";
				// required: hard code
				public String appId = "DAALT-SQE";
				// required: courseSection.getId()
				public String Course_Section_Source_System_Record_Id;
				// optional: hard code
				public String Course_Section_Source_System_Code = "Registrar";
				// required: questionCompletionActivity.getPersonRole()
				public String Person_Role_Code;
				// required: hard code
				public String Person_Source_System_Code = "PI";
				// required: quiz.getId()
				public String Assessment_Source_System_Record_Id;
				// required: hard code
				public String Assessment_Source_System_Code = "PAF";
				// required: question.getId()
				public String Assessment_Item_Source_System_Record_Id;
				// required: hard code
				public String Assessment_Item_Source_System_Code = "PAF";
				// optional: hard code for version 2.0
				public String User_Work_Type = "Credit";
				// required: question.getQuestionType()
				public String Assessment_Item_Question_Type;
				// optional; hard code:"Default student text"
				public String Item_Response_Text = "Default student text";
				// optional: default
				public String Client_Side_Accessed_Timestamp = getFormattedDateString(new Date());
				// optional: default
				public String Client_Side_Answered_Timestamp;
				
				// optional : not used by data services for version 2.0
				// public String Item_Response_Stored_At_URL;
				// optional : not used by data services for version 2.0
				/*
				 * public String User_Agent; public String public
				 * public String Network_Connection_Type;
				 */

			}

			public class SimpleWritingQuestionUserAnsweredJournalWritingExtension extends Extension {
				//These fields only exist if this tincan is for Journal Question
				
				//questionCompletionActivity.getResponseCode()
				public String Ref_Assessment_Item_Response_Status_Code;
				//questionCompletionActivity.getPassFailCode()
				public String Item_Response_Pass_Fail;
			}

			public class SimpleWritingQuestionUserAnsweredSharedWritingExtension extends Extension {
				//  These field is only exists if this tincan is for shared writing question ;
				//questionCompletionActivity.getScore()
				public float Item_Response_Raw_Score = -1f;
				//questionCompletionActivity.getScore()
				public float Item_Response_Score = -1f;
			
			}
		}

		// //optional : not used by data service endpoints version 2.0
		// public TransformationHistory Transformation_History = new
		// TransformationHistory();
		// public class TransformationHistory {
		// public String Transform_Datetime;
		// public String From_Version;
		// public String To_Version;
		// }
	}

}
