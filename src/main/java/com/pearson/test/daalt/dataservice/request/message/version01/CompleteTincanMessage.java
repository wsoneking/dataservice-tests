package com.pearson.test.daalt.dataservice.request.message.version01;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.LearningActivity;

public class CompleteTincanMessage extends SeerMessage{
	
	public CompleteTincanMessage(){
		messageSource = new CompleteTincanMessageSource();
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException {
		CompleteTincanMessageSource specificMessageSource = (CompleteTincanMessageSource) messageSource;
		
		switch(propertyName) {
		case "CourseSection" :
			CourseSection courseSection = (CourseSection) propertyValue;
			if(specificMessageSource.context == null){
				specificMessageSource.context = specificMessageSource.new Context();
			}
			if(specificMessageSource.context.extensions == null){
				specificMessageSource.context.extensions = new HashMap<String, java.lang.Object>();
			}
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.COURSE_SECTION_ID, courseSection.getId());	
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.COURSE_SECTION_ID_TYPE, "Registrar"); 
			break;
		case "Assignment" :
			Assignment assignment = (Assignment) propertyValue;
			if(specificMessageSource.context == null){
				specificMessageSource.context = specificMessageSource.new Context();
			}
			if(specificMessageSource.context.extensions == null){
				specificMessageSource.context.extensions = new HashMap<String, java.lang.Object>();
			}
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.ASSIGNMENT_ID, assignment.getId());
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.ASSIGNMENT_TYPE, "Revel");
			break;
		case "Page" :
			Page page = (Page) propertyValue;
			if(specificMessageSource.context == null){
				specificMessageSource.context = specificMessageSource.new Context();
			}
			if(specificMessageSource.context.extensions == null){
				specificMessageSource.context.extensions = new HashMap<String, java.lang.Object>();
			}
			if(specificMessageSource.verb == null){
				specificMessageSource.verb = specificMessageSource.new Verb();
			}
			if(specificMessageSource.object == null){
				specificMessageSource.object = specificMessageSource.new Object();
			}
			if(specificMessageSource.object.definition == null){
				specificMessageSource.object.definition = specificMessageSource.object.new Definition();
			}
			specificMessageSource.verb.id = "http://activitystrea.ms/schema/1.0/complete";
			specificMessageSource.object.id = "http://schema.pearson.com/daalt/contentId/" + page.getLearningResourceId();
			specificMessageSource.object.objectType = "Activity";
			specificMessageSource.object.definition.type = "http://adlnet.gov/expapi/activities/media";
			
//			specificMessageSource.context.extensions.put(SeerExtensionsKeys.APP_ID, "DAALT-SQE");
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.APP_ID, "revel");
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.CONTENT_ID, page.getLearningResourceId());
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.CONTAINER_CONTENT_ID, page.getLearningResourceId());
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.CONTENT_ID_TYPE, "EPS");
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.CONTAINER_CONTENT_ID_TYPE, "EPS");
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.PERSON_ID_TYPE, "PI");
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.TIME_ON_TASK_UUID, UUID.randomUUID().toString());
			specificMessageSource.context.extensions.put("source", "js_tincan");
			break;
		case "Activity" :
			LearningActivity learningActivity = (LearningActivity) propertyValue;
			if(specificMessageSource.context == null){
				specificMessageSource.context = specificMessageSource.new Context();
			}
			if(specificMessageSource.context.extensions == null){
				specificMessageSource.context.extensions = new HashMap<String, java.lang.Object>();
			}
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.ACTOR_ROLE_KEY, learningActivity.getPersonRole());
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.DURATION_IN_SECONDS, learningActivity.getTimeSpent());		

			if(specificMessageSource.actor == null){
				specificMessageSource.actor = specificMessageSource.new Actor();
			}
			if(specificMessageSource.actor.account == null){
				specificMessageSource.actor.account = specificMessageSource.actor.new Account();
			}
			specificMessageSource.actor.account.name = learningActivity.getPersonId();
			break;
			
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : " + propertyName);
		}
	}

	@Override
	public String getMissingCriticalPropertyName() {

		CompleteTincanMessageSource msgSource = (CompleteTincanMessageSource) messageSource;
		if (msgSource.actor == null) {
			return "MessageSource.actor";
		}
		if (msgSource.actor.account == null) {
			return "MessageSource.actor.account";
		}
		if (msgSource.actor.account.name == null) {
			return "MessageSource.actor.account.name";
		}
		if (msgSource.verb == null) {
			return "MessageSource.verb";
		}
		if (msgSource.verb.id == null) {
			return "MessageSource.verb.id";
		}
		if (msgSource.object == null) {
			return "MessageSource.object";
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
			return "MessageSource.context.extension";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.APP_ID)) {
			return "MessageSource.context.extension.APP_ID";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ACTOR_ROLE_KEY)) {
			return "MessageSource.context.extension.ACTOR_ROLE_KEY";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ASSIGNMENT_ID)) {
			return "MessageSource.context.extension.ASSIGNMENT_ID";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ASSIGNMENT_TYPE)) {
			return "MessageSource.context.extension.ASSIGNMENT_TYPE";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.CONTAINER_CONTENT_ID)) {
			return "MessageSource.context.extension.CONTAINER_CONTENT_ID";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.CONTAINER_CONTENT_ID_TYPE)) {
			return "MessageSource.context.extension.CONTAINER_CONTENT_ID_TYPE";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.CONTENT_ID)) {
			return "MessageSource.context.extension.CONTENT_ID";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.CONTENT_ID_TYPE)) {
			return "MessageSource.context.extension.CONTENT_ID_TYPE";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.COURSE_SECTION_ID)) {
			return "MessageSource.context.extension.COURSE_SECTION_ID";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.COURSE_SECTION_ID_TYPE)) {
			return "MessageSource.context.extension.COURSE_SECTION_ID_TYPE";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.DURATION_IN_SECONDS)) {
			return "MessageSource.context.extension.DURATION_IN_SECONDS";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.PERSON_ID_TYPE)) {
			return "MessageSource.context.extension.PERSON_ID_TYPE";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.TIME_ON_TASK_UUID)) {
			return "MessageSource.context.extension.TIME_ON_TASK_UUID";
		}
		return null;
	}

	public class CompleteTincanMessageSource extends MessageSource{

		//required : caller sets value
		public Actor actor;
		public class Actor{
			public String objectType = "Agent";				//required : hard code
			public Account account;
			public class Account{
				public String name;
				public String homePage = "https://piapi.openclass.com";			//required : hard code
			}
		}

		public Verb verb;
		public class Verb{
			public String id = "http://activitystrea.ms/schema/1.0/complete";
		}
		
		public Object object;
		public class Object{
			public String id;
			public String objectType;
			public Definition definition;
			public class Definition{
				public String type;
			}
		}

		public Context context = new Context();
		public class Context{
			public Map<String, java.lang.Object> extensions;
		}
	
		
	}
}
