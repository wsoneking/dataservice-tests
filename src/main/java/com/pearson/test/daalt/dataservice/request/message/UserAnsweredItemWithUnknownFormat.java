package com.pearson.test.daalt.dataservice.request.message;

import java.util.UUID;

import com.google.gson.JsonObject;
import com.pearson.test.daalt.dataservice.model.Attempt;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.UnknownFormatStudentResponseObject;

public class UserAnsweredItemWithUnknownFormat extends SeerMessage {
	
	public UserAnsweredItemWithUnknownFormat(boolean isFinalAttempt) {
		messageSource = new UserAnsweredItemWithUnknownFormatMsgSource();
		UserAnsweredItemWithUnknownFormatMsgSource specificMessageSource = (UserAnsweredItemWithUnknownFormatMsgSource) messageSource;
		if(isFinalAttempt) {
			specificMessageSource.context.extensions = specificMessageSource.context.new finalAttemptExtension();
		} else {
			specificMessageSource.context.extensions = specificMessageSource.context.new Extension();
		}
		
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException, InvalidStateException {
		UserAnsweredItemWithUnknownFormatMsgSource specificMessageSource = (UserAnsweredItemWithUnknownFormatMsgSource) messageSource;
		switch(propertyName) {
		case "PracticeFlag" :
			String type = (String) propertyValue;
			specificMessageSource.context.extensions.User_Work_Type = type;
			break;
		case "CourseSection" :
			CourseSection courseSection = (CourseSection) propertyValue;
			specificMessageSource.context.extensions.Course_Section_Source_System_Record_Id = courseSection.getId();
			break;
		case "Quiz" :
			Quiz quiz = (Quiz) propertyValue;
			specificMessageSource.context.extensions.Assessment_Source_System_Record_Id = quiz.getId();
			break;
		case "Question" :
			Question question = (Question) propertyValue;
			specificMessageSource.context.extensions.Assessment_Item_Source_System_Record_Id = question.getId();
			specificMessageSource.context.extensions.Assessment_Item_Question_Type = question.getQuestionType();
			specificMessageSource.context.extensions.Possible_Score = question.getPointsPossible();
			break;
		case "Attempt" :
			Attempt attempt = (Attempt) propertyValue;
			specificMessageSource.actor.account.Person_Source_System_Record_Id = attempt.getPersonId();
			specificMessageSource.context.extensions.Person_Role_Code = attempt.getPersonRole();
			specificMessageSource.context.extensions.Attempt_Number = attempt.getAttemptNumber();
			specificMessageSource.context.extensions.Assessment_Item_Response_Code = attempt.getAnswerCorrectness().value;
			specificMessageSource.context.extensions.Client_Side_Answered_Timestamp = attempt.getStringLastActivityDate();
			if(attempt.isFinalAttempt()){
				UserAnsweredItemWithUnknownFormatMsgSource.Context.finalAttemptExtension finalAttemptExtensions 
					= (UserAnsweredItemWithUnknownFormatMsgSource.Context.finalAttemptExtension) specificMessageSource.context.extensions;
				finalAttemptExtensions.Item_Response_Score = attempt.getPointsEarned();
				finalAttemptExtensions.Item_Response_Raw_Score = attempt.getPointsEarned();
			}
			break;
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : " + propertyName);
		}

	}

	@Override
	public String getMissingCriticalPropertyName() {
		UserAnsweredItemWithUnknownFormatMsgSource msgSource = (UserAnsweredItemWithUnknownFormatMsgSource) messageSource;
		if (msgSource.Message_Type_Code == null) {
			return "MessageSource.Message_Type_Code";
		}
		if (msgSource.Originating_System_Code == null) {
			return "MessageSource.Originating_System_Code";
		}
		if (msgSource.Message_Version == null) {
			return "MessageSource.Message_Version";
		}
		if (msgSource.Message_Transfer_Type == null) {
			return "MessageSource.Message_Transfer_Type";
		}
		if (msgSource.id == null) {
			return "MessageSource.id";
		}
		if (msgSource.actor == null) {
			return "MessageSource.actor";
		}
		if (msgSource.actor.objectType == null) {
			return "MessageSource.actor.objectType";
		}
		if (msgSource.actor.account == null) {
			return "MessageSource.actor.account";
		}
		if (msgSource.actor.account.homePage == null) {
			return "MessageSource.actor.account.homePage";
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
			return "MessageSource.object.definition.type";
		}
		if (msgSource.context == null) {
			return "MessageSource.context";
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
		if (msgSource.context.extensions.Assessment_Item_Question_Type == null) {
			return "MessageSource.context.extensions.Assessment_Item_Question_Type";
		}
		if (msgSource.context.extensions.Assessment_Item_Response_Code == null) {
			return "MessageSource.context.extensions.Assessment_Item_Response_Code";
		}
		if (msgSource.context.extensions.Attempt_Number == -1) {
			return "MessageSource.context.extensions.Attempt_Number";
		}
		if (msgSource.context.extensions.Student_Response == null) {
			return "MessageSource.context.extensions.Item_Response_Object";
		}
		if (msgSource.context.extensions instanceof UserAnsweredItemWithUnknownFormatMsgSource.Context.finalAttemptExtension) {
			UserAnsweredItemWithUnknownFormatMsgSource.Context.finalAttemptExtension finalAttemptExtension 
				= (UserAnsweredItemWithUnknownFormatMsgSource.Context.finalAttemptExtension) msgSource.context.extensions;
			if (finalAttemptExtension.Item_Response_Score == -1) {
				return "MessageSource.context.extensions.Item_Response_Score";
			}
			if (finalAttemptExtension.Item_Response_Raw_Score == -1) {
				return "MessageSource.context.extensions.Item_Response_Raw_Score";
			}
		}
		return null;
	}

	
	public class UserAnsweredItemWithUnknownFormatMsgSource extends MessageSource {
		
		//required : hard code
		public String Message_Type_Code = "User_Answered_Item_With_Unknown_Format";
		//required : hard code
		public String Originating_System_Code = "DAALTTest";
		//required : hard code
		public String Message_Version = "2.0";
		//required : hard code
		public String Message_Transfer_Type = "LiveStream";
		//required : generate unique
		public String id = "SQE-Tincan-" + UUID.randomUUID().toString();
		
		//required
		public Actor actor = new Actor();
		public class Actor {
			//required : hard code
			public String objectType = "Agent";
			
			//required : caller sets value
			public Account account = new Account();
			public class Account {
				//required : hard code
				public String homePage = "https://piapi.openclass.com";
				//optional : learningActivity.getPersonId() NOTE: can we change this back to required? Please???
				public String Person_Source_System_Record_Id;
			}
		}
		
		//required
		public Verb verb = new Verb();
		public class Verb {
			//required : hard code
			public String id = "http://adlnet.gov/expapi/verbs/answered";
			
			//required
			public Display display = new Display();
			public class Display {
				//required: hard code
				public String language = "en-US";
				//required: hard code
				public String verb = "answered";
			}
		}
		
		//required
		public Object object = new Object();
		public class Object {
			//required : hard-code
			public String objectType = "Activity";
			//required : generate unique
			public String id = "SQE-Tincan-Obj-" + UUID.randomUUID().toString();
			
			//required
			public Definition definition = new Definition();
			public class Definition{
				//required : hard-code
				public String type = "http://adlnet.gov/expapi/activities/question";
			}
		}
		
		//required
		public Context context = new Context();
		public class Context {
			//required :
			public Extension extensions = new Extension();
			public class Extension {
				//required: hard-code
				public String URL_Prefix_For_Context_Fields = "http://schema.pearson.com/daalt/";
				//required: hard-code 
				public String appId = "DAALT-SQE";
				//required: courseSection.getId()
				public String Course_Section_Source_System_Record_Id;
				//required: hard-code
				public String Course_Section_Source_System_Code = "Registrar";
				//optional:	learningActivity.getPersonRole()
				public String Person_Role_Code;
				//optional: hard-code NOTE: can we change this back to required? Please???
				public String Person_Source_System_Code = "PI";
				//required: get from quiz id
				public String Assessment_Source_System_Record_Id;
				//required: hard code
				public String Assessment_Source_System_Code = "PAF";
				//required: get from question
				public String Assessment_Item_Source_System_Record_Id;
				//required: hard code
				public String Assessment_Item_Source_System_Code = "PAF";
				//required: from question type
				public String Assessment_Item_Question_Type;
				//required: hard code
				public String User_Work_Type = "Credit";
				//required: attempt.getAttemptNumber()
				public float Attempt_Number = -1f;
				// required: attempt.getAnswerCorrectness().toString()
				public String Assessment_Item_Response_Code;
				//optional: hard code here
				public UnknownFormatStudentResponseObject Student_Response = new UnknownFormatStudentResponseObject();
				
//				//optional: 
//				public String Item_Response_Pass_Fail; 			// TODO we don't need this field, correect? It's only for Journal
				//optional:	question.getPointsPossible()
				public float Possible_Score = -1f;
//				//optional:
//				public float Item_Response_Adjusted_Score;		
//				//optional:
//				public boolean Hint_Requested_Flag; 			// TODO we don't need this field?
				//optional:
				public String Client_Side_Answered_Timestamp;	// TODO The Datetime when the Person finished work on the question. Same with transaction_time?
//				//optional:
//				public String User_Agent;					//TODO contains the user agent string for a given device which typically includes device type, browser engine, OS version, and other fields.
//				//optional:
//				public String Network_Connection_Type;		//TODO identifies what kind of network the device is connected to (Wifi/2G/3G).  
			}
			
			public class finalAttemptExtension extends Extension {
				
				// optional: attempt.getPointsEarned()
				public float Item_Response_Raw_Score = -1f;
				// optional: attempt.getPointsEarned()
				public float Item_Response_Score = -1f;
			}
		}
	}
}



