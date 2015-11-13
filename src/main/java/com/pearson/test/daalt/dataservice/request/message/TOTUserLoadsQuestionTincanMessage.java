package com.pearson.test.daalt.dataservice.request.message;

import java.util.Date;
import java.util.UUID;

import com.pearson.test.daalt.dataservice.model.Attempt;
import com.pearson.test.daalt.dataservice.model.AutoSaveActivity;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.LeaveQuestionActivity;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Quiz;


/**
 * 
 * @author UWANGJI
 *
 * https://devops-tools.pearson.com/stash/projects/DAALT_REF/repos/schemas/browse/src/main/resources/seer/tincan/Time_On_Task_User_Loads_Question.Schema.json?at=refs%2Ftags%2Fv2.0.8
 *
 */

public class TOTUserLoadsQuestionTincanMessage extends SeerMessage{	

	public TOTUserLoadsQuestionTincanMessage(){
		messageSource = new TOTUserLoadsQuestionTincanMessageSource();
		TOTUserLoadsQuestionTincanMessageSource specificMessageSource = (TOTUserLoadsQuestionTincanMessageSource) messageSource;
		specificMessageSource.context.extensions = specificMessageSource.context.new Extension();
	}
	
	@Override
	public void setProperty(String propertyName, java.lang.Object propertyValue)
			throws UnknownPropertyException {
		TOTUserLoadsQuestionTincanMessageSource specificMessageSource = (TOTUserLoadsQuestionTincanMessageSource) messageSource;
		switch(propertyName) {
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
			break;
		case "Attempt" :
			Attempt attempt = (Attempt) propertyValue;
			specificMessageSource.actor.account.Person_Source_System_Record_Id = attempt.getPersonId();
			specificMessageSource.context.extensions.Person_Role_Code = attempt.getPersonRole();
			specificMessageSource.context.extensions.Client_Side_Load_Timestamp = attempt.getStringLastActivityDate();
			break;
		case "QuestionCompletionActivity" :
			QuestionCompletionActivity completeQuestionActivity = (QuestionCompletionActivity) propertyValue;
			specificMessageSource.actor.account.Person_Source_System_Record_Id = completeQuestionActivity.getPersonId();
			specificMessageSource.context.extensions.Person_Role_Code = completeQuestionActivity.getPersonRole();
			specificMessageSource.context.extensions.Client_Side_Load_Timestamp = completeQuestionActivity.getStringLastActivityDate();
			break;
		case "LeaveQuestionActivity" :
			LeaveQuestionActivity leaveQuestionActivity = (LeaveQuestionActivity) propertyValue;
			specificMessageSource.actor.account.Person_Source_System_Record_Id = leaveQuestionActivity.getPersonId();
			specificMessageSource.context.extensions.Person_Role_Code = leaveQuestionActivity.getPersonRole();
			specificMessageSource.context.extensions.Client_Side_Load_Timestamp = leaveQuestionActivity.getStringLastActivityDate();
			break;
		case "AutoSaveActivity" :
			AutoSaveActivity autoSaveActivity = (AutoSaveActivity) propertyValue;
			specificMessageSource.actor.account.Person_Source_System_Record_Id = autoSaveActivity.getPersonId();
			specificMessageSource.context.extensions.Person_Role_Code = autoSaveActivity.getPersonRole();
			specificMessageSource.context.extensions.Client_Side_Load_Timestamp = autoSaveActivity.getStringLastActivityDate();
			break;			
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : " + propertyName);
		}
		
	}

	@Override
	public String getMissingCriticalPropertyName() {
		TOTUserLoadsQuestionTincanMessageSource msgSource = (TOTUserLoadsQuestionTincanMessageSource) messageSource;
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
		if (msgSource.context.extensions.Time_On_Task_UUID == null) {
			return "MessageSource.context.extensions.Time_On_Task_UUID";
		}
		if (msgSource.context.extensions.Client_Side_Load_Timestamp == null) {
			return "MessageSource.context.extensions.Client_Side_Load_Timestamp";
		}
		return null;
	}

	public class TOTUserLoadsQuestionTincanMessageSource extends MessageSource {
		//required : hard code
		public String Message_Type_Code = "Time_On_Task_User_Loads_Question";
		//required : hard code
		public String Originating_System_Code = "DAALTTest";
		//required : hard code
		public String Message_Version = "2.0";			// TODO or 2.1
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
				//required : 	attempt.getPersonId(), leaveQuestionActivity.getPersonId(), 
				//				completeQuestionActivity.getPersonId(), or autosaveActivity.getPersonId()
				public String Person_Source_System_Record_Id;
			}
		}
		
		//required
		public Verb verb = new Verb();
		public class Verb{
			//required : 	"http://adlnet.gov/expapi/verbs/answered" if attempt or completeQuestionActivity
            //				"http://activitystrea.ms/schema/1.0/access" if leaveQuestionActivity
            //				"http://activitystrea.ms/schema/1.0/autosave" if autoSaveActivity
			public String id = "http://activitystrea.ms/schema/1.0/access";
			
			//required
			public Display display = new Display();
			public class Display {
				//required: hard code
				public String language = "en-US";
				//required: "answered" if attempt or completeQuestionActivity
                //			"accessed" if leaveQuestionActivity
                //			"autosaved" if autoSaveActivity
				public String verb = "accessed";
			}
		}
		
		//required
		public Object object = new Object();
		public class Object{
			//required : hard-code
			public String objectType = "Activity";
			//required : generate unique
			public String id = "SQE-Tincan-Obj-" + UUID.randomUUID().toString();
			
			//required : caller sets value
			public Definition definition = new Definition();
			public class Definition{
				//required : hard-code
				public String type = "http://adlnet.gov/expapi/activities/question";
			}
		}
		
//		//optional : don't populate for version 2.0
//		public Result result = new Result();
//		public class Result {
//			//optional : caller sets value
//			public String response;
//		}
		
		//required
		public Context context = new Context();
		public class Context {
			//required :
			public Extension extensions;
			public class Extension {
				//required: hard-code
				public String URL_Prefix_For_Context_Fields = "http://schema.pearson.com/daalt/";
				//required: hard-code 
				public String appId = "DAALT-SQE";
				//required: courseSection.getId()
				public String Course_Section_Source_System_Record_Id;
				//required: hard-code
				public String Course_Section_Source_System_Code = "Registrar";
				//required:	attempt.getPersonRole(), leaveQuestionActivity.getPersonRole(), 
                //			completeQuestionActivity.getPersonRole(), or autosaveActivity.getPersonRole()
				public String Person_Role_Code;
				//required: hard-code
				public String Person_Source_System_Code = "PI";
				//required: quiz.getId()
				public String Assessment_Source_System_Record_Id; 
				//required: hard-code
				public String Assessment_Source_System_Code = "PAF";
				//required: question.getId()
				public String Assessment_Item_Source_System_Record_Id;  
				//required: hard-code
				public String Assessment_Item_Source_System_Code = "PAF";
				//required: generate unique
				public String Time_On_Task_UUID = "SQE-ToT-" + UUID.randomUUID().toString();
				//required: default to current time - not used by endpoints for version 2.0
				public String Client_Side_Load_Timestamp;
					
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

