package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;


import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Chapter;
import com.pearson.test.daalt.dataservice.model.ChapterSection;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.MultiValueOtherQuestion;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionType;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemPossibleAnswers;
import com.pearson.test.daalt.dataservice.request.message.AssessmentMessage;
import com.pearson.test.daalt.dataservice.request.message.CourseSectionLearningResourceMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningModuleContentMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningModuleMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningResourceMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningResourceRelationshipMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;

public class SubPubSeerCreateAssignmentWithoutPossibleAnswersTestAction extends
		CreateAssignmentWithoutPossibleAnswersTestAction {

	public SubPubSeerCreateAssignmentWithoutPossibleAnswersTestAction(Instructor instructor,
			CourseSection courseSection, Assignment assignment, Question question) {
		messages = new ArrayList<>();
		this.instructor = instructor;
		this.courseSection = courseSection;
		this.assignment = assignment;
		this.question = question;
	}

	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing CreateAssignmentWithoutPossibleAnswersTestAction...\n");

		// Learning_Resource message to create the Book as a learning resource
		Message bookRevelLrCreateMessage = new LearningResourceMessage(/* contentIsEmbeddedQuestion */false, /* learningResourceHasSubtype */
		false);
		bookRevelLrCreateMessage.setProperty("CourseSection", courseSection);
		messages.add(bookRevelLrCreateMessage);
		
		//Course_Section_To_Learning_Resource message to connect the book to the CourseSection as a LearningResource
		Message bookToCourseSectionMessage = new CourseSectionLearningResourceMessage();
		bookToCourseSectionMessage.setProperty("CourseSection", courseSection);
		messages.add(bookToCourseSectionMessage);

		// Learning_Module message to create the Assignment
		Message revelLmCreateMessage = new LearningModuleMessage();
		// do not need to explicitly set Transaction_Type_Code because default
		// value is "create"
		revelLmCreateMessage.setProperty("CourseSection", courseSection);
		revelLmCreateMessage.setProperty("Instructor", instructor);
		revelLmCreateMessage.setProperty("Assignment", assignment);
		messages.add(revelLmCreateMessage);

		for (Chapter chapter : assignment.getChapters()) {

			boolean chapterHasSubType = chapter.getLearningResourceSubType() != null
					&& !chapter.getLearningResourceSubType().isEmpty();
			
			// Learning_Resource message to create the Chapter as a learning
			// resource
			Message chapterRevelLrCreateMessage = new LearningResourceMessage(/* contentIsEmbeddedQuestion */false,
			/* learningResourceHasSubtype */chapterHasSubType);
			chapterRevelLrCreateMessage.setProperty("LearningResource", chapter);
			messages.add(chapterRevelLrCreateMessage);

			// Learning_Resource_Relationship message to establish the Book -->
			// Chapter parent/child relationship
			Message bookToChapterMessage = new LearningResourceRelationshipMessage(
			/* parentLearningResourceIsBook */true);
			// if parent learning resource is a book, do not set
			// ParentLearningResource property
			bookToChapterMessage.setProperty("CourseSection", courseSection);
			bookToChapterMessage.setProperty("LearningResource", chapter);
			messages.add(bookToChapterMessage);

			// Learning_Module_Content message to add the Chapter learning
			// resource to the Assignment
			Message chapterMessage = new LearningModuleContentMessage(/* learningResourceHasSubtype */chapterHasSubType, /*contentIsQuiz*/ false, /*contentIsEmbeddedQuestion*/ false);
			chapterMessage.setProperty("CourseSection", courseSection);
			chapterMessage.setProperty("Assignment", assignment);
			chapterMessage.setProperty("LearningResource", chapter);
			messages.add(chapterMessage);

			Quiz chapterQuiz = chapter.getChapterQuiz();

			boolean chapterQuizHasSubType = chapterQuiz.getLearningResourceSubType() != null
					&& !chapterQuiz.getLearningResourceSubType().isEmpty();
			
			// Learning_Resource message to create the ChapterQuiz as a
			// learning resource
			Message chapterQuizRevelLrCreateMessage = new LearningResourceMessage(
			/* contentIsEmbeddedQuestion */false,
			/* learningResourceHasSubtype */chapterQuizHasSubType);
			chapterQuizRevelLrCreateMessage.setProperty("LearningResource", chapterQuiz);
			messages.add(chapterQuizRevelLrCreateMessage);

			// Learning_Resource_Relationship message to establish the
			// Chapter --> ChapterQuiz parent/child relationship
			Message chapterToChapterQuizMessage = new LearningResourceRelationshipMessage(
			/* parentLearningResourceIsBook */false);
			chapterToChapterQuizMessage.setProperty("CourseSection", courseSection);
			chapterToChapterQuizMessage.setProperty("ParentLearningResource", chapter);
			chapterToChapterQuizMessage.setProperty("LearningResource", chapterQuiz);
			messages.add(chapterToChapterQuizMessage);

			// Learning_Module_Content message to add the ChapterQuiz
			// learning resource to the Assignment
			Message chapterQuizMessage = new LearningModuleContentMessage(
			/* learningResourceHasSubtype */chapterQuizHasSubType, /*contentIsQuiz*/ true, /*contentIsEmbeddedQuestion*/ false);
			chapterQuizMessage.setProperty("CourseSection", courseSection);
			chapterQuizMessage.setProperty("Assignment", assignment);
			chapterQuizMessage.setProperty("LearningResource", chapterQuiz);
			messages.add(chapterQuizMessage);

			// Assessment message to publish the Quiz
			// The message would not publish a Assessment_Item_Possible_Answers
			// message to publish current Question
			Message chapterQuizPafPublishMessage = new AssessmentMessage();
			chapterQuizPafPublishMessage.setProperty("Quiz", chapterQuiz);
			messages.add(chapterQuizPafPublishMessage);

			for (Question ques : chapterQuiz.getQuestions()) {
				if (!(ques.getId().compareTo(this.question.getId())==0)) {
					Message assessmentItemPossibleAnswer;
					
					//Assessment_Item_Possible_Answers message to publish the Question
					if(ques.getQuestionType().equals(QuestionType.MULTI_VALUE.value) && !(ques instanceof MultiValueOtherQuestion)){
						assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers(QuestionType.MULTI_VALUE.value);
					} else if (ques.getQuestionType().equals(QuestionType.UNKNOWN_FORMAT.value)) {
						assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers(QuestionType.UNKNOWN_FORMAT.value);
					} else {
						assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers("General");
					}
					
					assessmentItemPossibleAnswer.setProperty("Question", ques);
					messages.add(assessmentItemPossibleAnswer);
				}
			}

			//iterate through chapter sections
			if (chapter.getChapterSections() != null){
				for (ChapterSection chapSection : chapter.getChapterSections()) {
					boolean chapSectionHasSubType = chapSection.getLearningResourceSubType() != null && !chapSection.getLearningResourceSubType().isEmpty();
					//Learning_Resource message to create the ChapterSection as a learning resource
					Message chapterSectionRevelLrCreateMessage = new LearningResourceMessage(/*contentIsEmbeddedQuestion*/ false, 
							/*learningResourceHasSubtype*/ chapSectionHasSubType);
					chapterSectionRevelLrCreateMessage.setProperty("LearningResource", chapSection);
					messages.add(chapterSectionRevelLrCreateMessage);
					
					//Learning_Resource_Relationship message to establish the Chapter --> ChapterSection parent/child relationship
					Message chapterToChapterSectionMessage = new LearningResourceRelationshipMessage(/*parentLearningResourceIsBook*/ false);
					chapterToChapterSectionMessage.setProperty("CourseSection", courseSection);
					chapterToChapterSectionMessage.setProperty("ParentLearningResource", chapter);
					chapterToChapterSectionMessage.setProperty("LearningResource", chapSection);
					messages.add(chapterToChapterSectionMessage);
					
					//Learning_Module_Content message to add the ChapterSection learning resource to the Assignment
					Message chapterSectionMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ chapSectionHasSubType, /*contentIsQuiz*/ false, /*contentIsEmbeddedQuestion*/ false);
					chapterSectionMessage.setProperty("CourseSection", courseSection);
					chapterSectionMessage.setProperty("Assignment", assignment);
					chapterSectionMessage.setProperty("LearningResource", chapSection);
					messages.add(chapterSectionMessage);
					
					
					Quiz chapterSectionQuiz = chapSection.getChapterSectionQuiz();
					if (chapterSectionQuiz != null) {
						boolean chapterSectionQuizHasSubType = chapterSectionQuiz.getLearningResourceSubType() != null && !chapterSectionQuiz.getLearningResourceSubType().isEmpty();
						
						//Learning_Resource message to create the ChapterSectionQuiz as a learning resource
						Message chapterSectionQuizRevelLrCreateMessage = new LearningResourceMessage(/*contentIsEmbeddedQuestion*/ false, 
								/*learningResourceHasSubtype*/ chapterSectionQuizHasSubType);
						chapterSectionQuizRevelLrCreateMessage.setProperty("LearningResource", chapterSectionQuiz);
						messages.add(chapterSectionQuizRevelLrCreateMessage);
						
						//Learning_Resource_Relationship message to establish the ChapterSection --> ChapterSectionQuiz parent/child relationship
						Message chapterSectionToChapterSectionQuizMessage = new LearningResourceRelationshipMessage(/*parentLearningResourceIsBook*/ false);
						chapterSectionToChapterSectionQuizMessage.setProperty("CourseSection", courseSection);
						chapterSectionToChapterSectionQuizMessage.setProperty("ParentLearningResource", chapSection);
						chapterSectionToChapterSectionQuizMessage.setProperty("LearningResource", chapterSectionQuiz);
						messages.add(chapterSectionToChapterSectionQuizMessage);
						
						//Learning_Module_Content message to add the ChapterSectionQuiz learning resource to the Assignment
						Message chapterSectionQuizMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ chapterSectionQuizHasSubType, /*contentIsQuiz*/ true, /*contentIsEmbeddedQuestion*/ false);
						chapterSectionQuizMessage.setProperty("CourseSection", courseSection);
						chapterSectionQuizMessage.setProperty("Assignment", assignment);
						chapterSectionQuizMessage.setProperty("LearningResource", chapterSectionQuiz);
						messages.add(chapterSectionQuizMessage);
						
						// Assessment message to publish the Quiz
						// The message would not publish a Assessment_Item_Possible_Answers
						// message to publish current Question
						Message chapterSectionQuizPafPublishMessage = new AssessmentMessage();
						chapterSectionQuizPafPublishMessage.setProperty("Quiz", chapterSectionQuiz);
						messages.add(chapterSectionQuizPafPublishMessage);

						for (Question ques : chapterSectionQuiz.getQuestions()) {
							if (!(ques.getId().compareTo(this.question.getId())==0)) {
								Message assessmentItemPossibleAnswer;
								
								//Assessment_Item_Possible_Answers message to publish the Question
								if(ques.getQuestionType().equals(QuestionType.MULTI_VALUE.value) && !(ques instanceof MultiValueOtherQuestion)){
									assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers(QuestionType.MULTI_VALUE.value);
								} else if (ques.getQuestionType().equals(QuestionType.UNKNOWN_FORMAT.value)) {
									assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers(QuestionType.UNKNOWN_FORMAT.value);
								} else {
									assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers("General");
								}
								
								assessmentItemPossibleAnswer.setProperty("Question", ques);
								messages.add(assessmentItemPossibleAnswer);
							}
						}
					}
				}
			}	
		}
	
			
		for (Message msg : messages) {
				msg.send(seerCount,subPubCount);
			}
		}
	
	}

