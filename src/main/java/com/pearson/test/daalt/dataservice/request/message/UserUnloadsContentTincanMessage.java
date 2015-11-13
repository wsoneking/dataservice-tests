package com.pearson.test.daalt.dataservice.request.message;

import java.util.Date;
import java.util.UUID;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.User;

public class UserUnloadsContentTincanMessage extends SeerMessage {

	public UserUnloadsContentTincanMessage(){
		messageSource = new CompleteTincanMessageSource();
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException {
		CompleteTincanMessageSource specificMessageSource = (CompleteTincanMessageSource) messageSource;
		switch(propertyName) {
		case "CourseSection" :
			CourseSection courseSection = (CourseSection) propertyValue;
			specificMessageSource.context.extensions.Course_Section_Source_System_Record_Id = courseSection.getId();
			break;
		case "Page" :
			Page page = (Page) propertyValue;
			specificMessageSource.context.extensions.Learning_Resource_Source_System_Record_Id = page.getLearningResourceId();
			break;
		case "LearningActivity" :
			LearningActivity learningActivity = (LearningActivity) propertyValue;
			specificMessageSource.actor.account.Person_Source_System_Record_Id = learningActivity.getPersonId();
			specificMessageSource.context.extensions.Person_Role_Code = learningActivity.getPersonRole();
			specificMessageSource.context.extensions.Duration_In_Seconds = learningActivity.getTimeSpent();
			specificMessageSource.context.extensions.Client_Side_Unload_Timestamp = learningActivity.getStringLastActivityDate();
			break;
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : " + propertyName);
		}
	}

	@Override
	public String getMissingCriticalPropertyName() {
		CompleteTincanMessageSource msgSource = (CompleteTincanMessageSource) messageSource;
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
		if (msgSource.context.extensions.Learning_Resource_Source_System_Record_Id == null) {
			return "MessageSource.context.extensions.Learning_Resource_Source_System_Record_Id";
		}
		if (msgSource.context.extensions.Time_On_Task_UUID == null) {
			return "MessageSource.context.extensions.Time_On_Task_UUID";
		}
		if (msgSource.context.extensions.Duration_In_Seconds == -1) {
			return "MessageSource.context.extensions.Duration_In_Seconds";
		}
		if (msgSource.context.extensions.Client_Side_Load_Timestamp == null) {
			return "MessageSource.context.extensions.Client_Side_Load_Timestamp";
		}
		if (msgSource.context.extensions.Client_Side_Unload_Timestamp == null) {
			return "MessageSource.context.extensions.Client_Side_Unload_Timestamp";
		}
		return null;
	}

	public class CompleteTincanMessageSource extends MessageSource {
		//required : hard code
		public String Message_Type_Code = "User_Unloads_Content";
		//required : hard code
		public String Originating_System_Code = "DAALTTest";
		//required : hard code
//		public String Message_Version = "2.1";
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
			public String id = "http://activitystrea.ms/schema/1.0/complete";
			
			//required
			public Display display = new Display();
			public class Display {
				//required: hard code
				public String language = "en-US";
				//required: hard code
				public String verb = "completed";
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
				public String type = "http://adlnet.gov/expapi/activities/media";
			}
		}
		
//		//optional : don't populate for version 2.1
//		public Result result = new Result();
//		public class Result {
//			//optional : caller sets value
//			public String response;
//		}
		
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
				//optional:	learningActivity.getPersonRole() NOTE: can we change this back to required? Please???
				public String Person_Role_Code;
				//optional: hard-code NOTE: can we change this back to required? Please???
				public String Person_Source_System_Code = "PI";
				//required: generate unique
				public String Time_On_Task_UUID = "SQE-ToT-" + UUID.randomUUID().toString();
				//required: learningActivity.getTimeSpent()
				public float Duration_In_Seconds = -1f;
				//required: default to current time - not used by endpoints for version 2.0
				public String Client_Side_Load_Timestamp = getFormattedDateString(new Date());
				//required: default to current time - not used by endpoints for version 2.0
				public String Client_Side_Unload_Timestamp; //TODO: fix me
				//optional: page.getLearningResourceId() NOTE: can we change this back to required? Please???
				public String Learning_Resource_Source_System_Record_Id; 
				
//				//optional - not used by data services version 2.1
//				public String User_Agent;
//				//optional - not used by data services version 2.1
//				public String Network_Connection_Type;
//				//optional - not used by data services version 2.1
//				public String Course_Source_System_Record_Id;
//				//optional - not used by data services version 2.1
//				public String Course_Source_System_Code;
//				//optional - not used by data services version 2.1
//				public String Content_Type_Code;
//				//optional - not used by data services version 2.1
//				public String Content_Subtype_Code;
//				//optional - not used by data services version 2.1
//				public String Expected_Outcome_Of_Page_Usage;
//				//optional - not used by data services version 2.1
//				public String Event_Causing_Page_Navigation;
//				//optional - not used by data services version 2.1
//				public String Page_User_Navigated_From;
//				//optional - not used by data services version 2.1
//				public String Page_User_Navigated_To;
//				//optional - not used by data services version 2.1
//				public String Page_Focus_Area_Code;
			}
		}
		
//		//optional : not used by data service endpoints version 2.1
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
	}
}
