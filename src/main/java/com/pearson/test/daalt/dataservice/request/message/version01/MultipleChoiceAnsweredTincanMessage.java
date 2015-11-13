package com.pearson.test.daalt.dataservice.request.message.version01;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Attempt;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
public class MultipleChoiceAnsweredTincanMessage extends SeerMessage{
	
	public MultipleChoiceAnsweredTincanMessage(){
		messageSource = new MultipleChoiceCorrectAnsweredTincanMessageSource();
		
	}
	
	@Override
	public void setProperty(String propertyName, java.lang.Object propertyValue)
			throws UnknownPropertyException {
		MultipleChoiceCorrectAnsweredTincanMessageSource specificMessageSource = (MultipleChoiceCorrectAnsweredTincanMessageSource) messageSource;
		switch(propertyName) {
		case "CourseSection" :
			CourseSection courseSection = (CourseSection) propertyValue;
			if(specificMessageSource.context == null){
				specificMessageSource.context = specificMessageSource.new Context();
			}
			if(specificMessageSource.context.extensions == null){
				specificMessageSource.context.extensions = new HashMap<String, java.lang.Object>();
			}
//			specificMessageSource.context.extensions.put(SeerExtensionsKeys.APP_ID, "DAALT-SQE");
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.APP_ID, "revel");
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.COURSE_SECTION_ID, courseSection.getId());
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.COURSE_SECTION_ID_TYPE, "Registrar"); 
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.ACTOR_ROLE_KEY, "Student");
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.PERSON_ID_TYPE, "PI");
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
		case "Quiz" :
			Quiz quiz = (Quiz) propertyValue;
			if(specificMessageSource.context == null){
				specificMessageSource.context = specificMessageSource.new Context();
			}
			if(specificMessageSource.context.extensions == null){
				specificMessageSource.context.extensions = new HashMap<String, java.lang.Object>();
			}
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.ASSESSMENT_ID, quiz.getId());
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.ASSESSMENT_TYPE, "PAF");
			break;
		case "Question" :
			Question question = (Question) propertyValue;
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
			specificMessageSource.verb.id = "http://adlnet.gov/expapi/verbs/answered";
			specificMessageSource.object.id = "http://repo.paf.pearsoncmg.com/itemId/" + question.getId();
			specificMessageSource.object.objectType = "Activity";
			specificMessageSource.object.definition.type = "http://adlnet.gov/expapi/activities/question";
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.ASSESSMENT_ITEM_ID, question.getId());
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.ASSESSMENT_ITEM_TYPE, "PAF");
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.ASSESSMENT_ITEM_QUESTION_TYPE, "MultipleChoice");
			break;
		case "Attempt" :
			Attempt attempt = (Attempt) propertyValue;
			if(specificMessageSource.context == null){
				specificMessageSource.context = specificMessageSource.new Context();
			}
			if(specificMessageSource.context.extensions == null){
				specificMessageSource.context.extensions = new HashMap<String, java.lang.Object>();
			}
			if(specificMessageSource.actor == null){
				specificMessageSource.actor = specificMessageSource.new Actor();
			}
			if(specificMessageSource.actor.account == null){
				specificMessageSource.actor.account = specificMessageSource.actor.new Account();
			}
			specificMessageSource.actor.account.name = attempt.getPersonId();
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.ATTEMPT_NUMBER, attempt.getAttemptNumber());
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.DURATION_IN_SECONDS, attempt.getTimeSpent());
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.ANSWER_ID, attempt.getSubAttempts().get(0).getAnswers().get(0).getId());
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.ANSWER_ID_TYPE, "BRIX");			
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.CONTENT_ID, "");
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.CONTENT_ID_TYPE, "EPS");
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.CONTAINER_CONTENT_ID, "");
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.CONTAINER_CONTENT_ID_TYPE, "EPS");
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.TIME_ON_TASK_UUID, "SQE-TOT-" + UUID.randomUUID().toString());
			specificMessageSource.context.extensions.put(SeerExtensionsKeys.ITEM_RESPONSE_STATUS_CODE, attempt.getAnswerCorrectness());
			if(attempt.isFinalAttempt()){
				specificMessageSource.context.extensions.put(SeerExtensionsKeys.ITEM_RESPONSE_SCORE, attempt.getPointsEarned());
			}
			break;
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : " + propertyName);
		}
		
	}

	@Override
	public String getMissingCriticalPropertyName() {
		
		MultipleChoiceCorrectAnsweredTincanMessageSource msgSource = (MultipleChoiceCorrectAnsweredTincanMessageSource) messageSource;
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
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ANSWER_ID)) {
			return "MessageSource.context.extension.ANSWER_ID";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ANSWER_ID_TYPE)) {
			return "MessageSource.context.extension.ANSWER_ID_TYPE";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ASSESSMENT_ID)) {
			return "MessageSource.context.extension.ASSESSMENT_ID";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ASSESSMENT_ITEM_ID)) {
			return "MessageSource.context.extension.ASSESSMENT_ITEM_ID";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ASSESSMENT_ITEM_QUESTION_TYPE)) {
			return "MessageSource.context.extension.ASSESSMENT_ITEM_QUESTION_TYPE";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ASSESSMENT_ITEM_TYPE)) {
			return "MessageSource.context.extension.ASSESSMENT_ITEM_TYPE";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ASSESSMENT_TYPE)) {
			return "MessageSource.context.extension.ASSESSMENT_TYPE";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ASSIGNMENT_ID)) {
			return "MessageSource.context.extension.ASSIGNMENT_ID";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ASSIGNMENT_TYPE)) {
			return "MessageSource.context.extension.ASSIGNMENT_TYPE";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ATTEMPT_NUMBER)) {
			return "MessageSource.context.extension.ATTEMPT_NUMBER";
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
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ITEM_RESPONSE_SCORE)) {
			return "MessageSource.context.extension.ITEM_RESPONSE_SCORE";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.ITEM_RESPONSE_STATUS_CODE)) {
			return "MessageSource.context.extension.ITEM_RESPONSE_STATUS_CODE";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.PERSON_ID_TYPE)) {
			return "MessageSource.context.extension.PERSON_ID_TYPE";
		}
		if (!msgSource.context.extensions.containsKey(SeerExtensionsKeys.TIME_ON_TASK_UUID)) {
			return "MessageSource.context.extension.TIME_ON_TASK_UUID";
		}
		return null;
	}

	public class MultipleChoiceCorrectAnsweredTincanMessageSource extends MessageSource{
		
		
		//required : caller sets value
		public Actor actor;
		//required : caller sets value
		public Verb verb;
		//required : caller sets value
		public Object object;
		//required : caller sets value
		public Result result;
		//required : caller sets value
		public Context context;
		
		public class Actor{
			//required : caller sets value
			public Account account;
			//required : hard code
			public String objectType = "Agent";
			
			public class Account{
				//required : caller sets value
				public String name;
				//required : hard code
				public String homePage = "https://piapi.openclass.com";
			}
		}
		public class Verb{
			//required : caller sets value
			public String id;
		}
		public class Object{
			//required : caller sets value
			public String id;
			//required : caller sets value
			public String objectType;
			//required : caller sets value
			public Definition definition;
			
			public class Definition{
				//required : caller sets value
				public String type;
			}
		}
		public class Result {
			//required : caller sets value
			public String response;
		}
		public class Context{
			//required : caller sets value
			public Map<String, java.lang.Object> extensions;
		}
	}
}

