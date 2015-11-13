package com.pearson.test.daalt.dataservice.request.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.pearson.test.daalt.dataservice.model.Answer;
import com.pearson.test.daalt.dataservice.model.Attempt;
import com.pearson.test.daalt.dataservice.model.AttemptResponseCode;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionPresentationFormat;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.SubAttempt;
import com.pearson.test.daalt.dataservice.model.SubQuestion;

public class MultiValueQuestionUserAnsweredTincanMessage extends SeerMessage {
	
	public MultiValueQuestionUserAnsweredTincanMessage(boolean isFinalAttempt) {
		messageSource = new MultiValueQuestionUserAnsweredTincanMessageSource();
		MultiValueQuestionUserAnsweredTincanMessageSource specificMessageSource = (MultiValueQuestionUserAnsweredTincanMessageSource) messageSource;
		if (isFinalAttempt) {
			specificMessageSource.context.extensions = specificMessageSource.context.new FinalAttemptExtension();
		} else {
			specificMessageSource.context.extensions = specificMessageSource.context.new Extension();
		}
	}
	
	@Override
	public void setProperty(String propertyName, java.lang.Object propertyValue)
			throws UnknownPropertyException {
		MultiValueQuestionUserAnsweredTincanMessageSource specificMessageSource = (MultiValueQuestionUserAnsweredTincanMessageSource) messageSource;
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
			break;
		case "Attempt" :
			Attempt attempt = (Attempt) propertyValue;
			specificMessageSource.actor.account.Person_Source_System_Record_Id = attempt.getPersonId();
			specificMessageSource.context.extensions.Person_Role_Code = attempt.getPersonRole();
			specificMessageSource.context.extensions.Attempt_Number = attempt.getAttemptNumber();
			specificMessageSource.context.extensions.Client_Side_Answered_Timestamp = attempt.getStringLastActivityDate();
			if (attempt.getSubAttempts() != null) {
				for (SubAttempt subAtt : attempt.getSubAttempts()) {
					for (Answer answer : subAtt.getAnswers()){
						SubQuestion subQuestion = subAtt.getSubQuestion();
						MultiValueQuestionUserAnsweredTincanMessageSource.Context.Extension.StudentResponse studentResponse 
							= specificMessageSource.context.extensions.new StudentResponse();
						if (attempt.isForFillInTheBlankOrNumeric()) {
							studentResponse.Target_Id = answer.getStudentEnteredText();
						} else {
							studentResponse.Target_Id = subQuestion.getId();
						}
						studentResponse.Answer_Id = answer.getId();
						if (subQuestion.getCorrectAnswer().contains(answer)){
							studentResponse.Target_Sub_Question_Response_Code = AttemptResponseCode.CORRECT.value;		
						} else {
							studentResponse.Target_Sub_Question_Response_Code = AttemptResponseCode.INCORRECT.value;
						}
						specificMessageSource.context.extensions.Student_Response.add(studentResponse);
					}
				}
			}
			specificMessageSource.context.extensions.Assessment_Item_Response_Code = attempt.getAnswerCorrectness().value;
			if(attempt.isFinalAttempt()){
				MultiValueQuestionUserAnsweredTincanMessageSource.Context.FinalAttemptExtension finalAttemptExtensions 
					= (MultiValueQuestionUserAnsweredTincanMessageSource.Context.FinalAttemptExtension) specificMessageSource.context.extensions;
				finalAttemptExtensions.Item_Response_Score = attempt.getPointsEarned();
			}
			break;
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : " + propertyName);
		}
		
	}

	@Override
	public String getMissingCriticalPropertyName() {
		MultiValueQuestionUserAnsweredTincanMessageSource msgSource = (MultiValueQuestionUserAnsweredTincanMessageSource) messageSource;
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
		if (msgSource.context.extensions instanceof MultiValueQuestionUserAnsweredTincanMessageSource.Context.FinalAttemptExtension) {
			MultiValueQuestionUserAnsweredTincanMessageSource.Context.FinalAttemptExtension finalAttemptExtension 
				= (MultiValueQuestionUserAnsweredTincanMessageSource.Context.FinalAttemptExtension) msgSource.context.extensions;
			if (finalAttemptExtension.Item_Response_Score == -1) {
				return "MessageSource.context.extensions.Item_Response_Score";
			}
		}
		if (msgSource.context.extensions.Client_Side_Accessed_Timestamp == null) {
			return "MessageSource.context.extensions.Client_Side_Accessed_Timestamp";
		}
		if (msgSource.context.extensions.Student_Response == null) {
			return "MessageSource.context.extensions.Student_Response";
		}
		for (MultiValueQuestionUserAnsweredTincanMessageSource.Context.Extension.StudentResponse response : msgSource.context.extensions.Student_Response) {
			if (response.Target_Id == null) {
				return "MessageSource.context.extensions.Student_Response.Target_Id";
			}
			if (response.Answer_Id == null) {
				return "MessageSource.context.extensions.Student_Response.Answer_Id";
			}
			if (response.Target_Sub_Question_Response_Code == null) {
				return "MessageSource.context.extensions.Student_Response.Target_Sub_Question_Response_Code";
			}
		}
		return null;
	}

	public class MultiValueQuestionUserAnsweredTincanMessageSource extends MessageSource {
		//required : hard code
		public String Message_Type_Code = "Multi_Value_Question_User_Answered";
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
			public class Account{
				//required : hard code
				public String homePage = "https://piapi.openclass.com";
				//required : attempt.getPersonId()
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
			//required : hard code
			public String objectType = "Activity";
			//required : generate unique
			public String id = "SQE-ObjectId-" + UUID.randomUUID().toString();
			
			//required
			public Definition definition = new Definition();
			public class Definition {
				//required : hard code
				public String type = "http://adlnet.gov/expapi/activities/question";
			}
		}
		
//		//optional : don't populate for version 2.0
//		public Result result = new Result();
//		public class Result {
//			//optional
//			public String response;
//		}
		
		//required
		public Context context = new Context();	
		public class Context {
			//required
			public Extension extensions;
			public class Extension {
				// required: hard code
				public String URL_Prefix_For_Context_Fields = "http://schema.pearson.com/daalt/";
				// required: hard code
				public String appId = "DAALT-SQE";
				// required: courseSection.getId()
				public String Course_Section_Source_System_Record_Id;  
				// required: hard code
				public String Course_Section_Source_System_Code = "Registrar";
				// required: attempt.getPersonRole()
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
				// required: question.getQuestionType()
				public String Assessment_Item_Question_Type;
				//optional: hard-code for version 2.0
				public String User_Work_Type = "Credit";
				// required: attempt.getAnswerCorrectness().toString()
				public String Assessment_Item_Response_Code;
				// required: attempt.getAttemptNumber()
				public float Attempt_Number = -1f;
				
				// required
				public List<StudentResponse> Student_Response = new ArrayList<StudentResponse>();
				public class StudentResponse {
					// required: subQuestion.getId()
					public String Target_Id;
					// required: answer.getId()
					public String Answer_Id;
					// required: answer contained in correct SuperAnswer ? "Correct" : "Incorrect"
					public String Target_Sub_Question_Response_Code;
				}
				
				// required: default to current time - not used by endpoints for version 2.0
				public String Client_Side_Accessed_Timestamp = getFormattedDateString(new Date());
				// optional: default to current time - not used by endpoints for version 2.0
				public String Client_Side_Answered_Timestamp;
				
//				//optional - not used by data services version 2.0
//				public String User_Agent;
//				//optional - not used by data services version 2.0
//				public String Network_Connection_Type;
			}
			
			public class FinalAttemptExtension extends Extension {
				// optional: attempt.getPointsEarned()
				public float Item_Response_Score = -1f;
			}
		}
		
//		//optional : not used by data service endpoints version 2.0
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
	}
}

