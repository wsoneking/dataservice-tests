package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Chapter;
import com.pearson.test.daalt.dataservice.model.ChapterSection;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionType;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemPossibleAnswers;
import com.pearson.test.daalt.dataservice.request.message.CourseSectionLearningResourceMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningResourceRelationshipMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.LearningResourceMessage;

public class SubPubSeerReSendSeedDataTestAction extends ReSendSeedDataTestAction {

	public SubPubSeerReSendSeedDataTestAction(CourseSection courseSection, Assignment assignment) {
		messages = new ArrayList<>();
		this.courseSection = courseSection;
		this.assignment = assignment;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing ReSendSeedDataTestAction...\n");
		
		//Course_Section_To_Learning_Resource message to connect the book to the CourseSection as a LearningResource
//		Message bookToCourseSectionMessage = new CourseSectionLearningResourceMessage();
//		bookToCourseSectionMessage.setProperty("CourseSection", courseSection);
//		messages.add(bookToCourseSectionMessage);
		
		//Learning_Resource message to create the Book as a learning resource
		Message bookRevelLrCreateMessage = new LearningResourceMessage(/*contentIsEmbeddedQuestion*/ false, /*learningResourceHasSubtype*/ false);
		bookRevelLrCreateMessage.setProperty("CourseSection", courseSection);
		messages.add(bookRevelLrCreateMessage);
				
		for (Chapter chapter : assignment.getChapters()) {
			boolean chapterHasSubType = chapter.getLearningResourceSubType() != null && !chapter.getLearningResourceSubType().isEmpty();
			
			//Learning_Resource message to create the Chapter as a learning resource
			Message chapterRevelLrCreateMessage = new LearningResourceMessage(/*contentIsEmbeddedQuestion*/ false, 
					/*learningResourceHasSubtype*/ chapterHasSubType);
			chapterRevelLrCreateMessage.setProperty("LearningResource", chapter);
			messages.add(chapterRevelLrCreateMessage);
			
			//Learning_Resource_Relationship message to establish the Book --> Chapter parent/child relationship
			Message bookToChapterMessage = new LearningResourceRelationshipMessage(/*parentLearningResourceIsBook*/ true);
			//if parent learning resource is a book, do not set ParentLearningResource property
			bookToChapterMessage.setProperty("CourseSection", courseSection);
			bookToChapterMessage.setProperty("LearningResource", chapter);
			messages.add(bookToChapterMessage);
			
			Quiz chapterQuiz = chapter.getChapterQuiz();
			if (chapterQuiz != null) {
				boolean chapterQuizHasSubType = chapterQuiz.getLearningResourceSubType() != null && !chapterQuiz.getLearningResourceSubType().isEmpty();
				
				//Learning_Resource message to create the ChapterQuiz as a learning resource
				Message chapterQuizRevelLrCreateMessage = new LearningResourceMessage(/*contentIsEmbeddedQuestion*/ false, 
						/*learningResourceHasSubtype*/ chapterQuizHasSubType);
				chapterQuizRevelLrCreateMessage.setProperty("LearningResource", chapterQuiz);
				messages.add(chapterQuizRevelLrCreateMessage);
				
				//Learning_Resource_Relationship message to establish the Chapter --> ChapterQuiz parent/child relationship
				Message chapterToChapterQuizMessage = new LearningResourceRelationshipMessage(/*parentLearningResourceIsBook*/ false);
				chapterToChapterQuizMessage.setProperty("CourseSection", courseSection);
				chapterToChapterQuizMessage.setProperty("ParentLearningResource", chapter);
				chapterToChapterQuizMessage.setProperty("LearningResource", chapterQuiz);
				messages.add(chapterToChapterQuizMessage);
				
				for (Question ques: chapterQuiz.getQuestions()) {
					
					//Assessment_Item_Possible_Answers message to publish the Question
					Message assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers(QuestionType.MULTI_VALUE.value);
					assessmentItemPossibleAnswer.setProperty("Question", ques);
					messages.add(assessmentItemPossibleAnswer);
				}

				//iterate through nested quizzes
				if (chapterQuiz.getNestedQuizzes() != null){
					for (Quiz qz : chapterQuiz.getNestedQuizzes()) {
						boolean qzHasSubType = qz.getLearningResourceSubType() != null && !qz.getLearningResourceSubType().isEmpty();
						
						//Learning_Resource message to create the NestedQuiz as a learning resource
						Message nestedQuizRevelLrCreateMessage = new LearningResourceMessage(/*contentIsEmbeddedQuestion*/ false, 
								/*learningResourceHasSubtype*/ qzHasSubType);
						nestedQuizRevelLrCreateMessage.setProperty("LearningResource", qz);
						messages.add(nestedQuizRevelLrCreateMessage);
						
						//Learning_Resource_Relationship message to establish the ChapterQuiz --> NestedQuiz parent/child relationship
						Message chapterQuizToNestedQuizMessage = new LearningResourceRelationshipMessage(/*parentLearningResourceIsBook*/ false);
						chapterQuizToNestedQuizMessage.setProperty("CourseSection", courseSection);
						chapterQuizToNestedQuizMessage.setProperty("ParentLearningResource", chapterQuiz);
						chapterQuizToNestedQuizMessage.setProperty("LearningResource", qz);
						messages.add(chapterQuizToNestedQuizMessage);
						
						for (Question ques: qz.getQuestions()) {
							
							//Assessment_Item_Possible_Answers message to publish the Question
							Message assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers(QuestionType.MULTI_VALUE.value);
							assessmentItemPossibleAnswer.setProperty("Question", ques);
							messages.add(assessmentItemPossibleAnswer);
						}
					}
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
						
						for (Question ques: chapterSectionQuiz.getQuestions()) {
							
							//Assessment_Item_Possible_Answers message to publish the Question
							Message assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers(QuestionType.MULTI_VALUE.value);
							assessmentItemPossibleAnswer.setProperty("Question", ques);
							messages.add(assessmentItemPossibleAnswer);
						}
						
						//Chapter Section Quiz cannot have nested assessments at this time
					}
				
					if (chapSection.getPages() != null) {
						for (Page page : chapSection.getPages()) {
							boolean pageHasSubType = page.getLearningResourceSubType() != null && !page.getLearningResourceSubType().isEmpty();
							
							//Learning_Resource message to create the Page as a learning resource
							Message pageRevelLrCreateMessage = new LearningResourceMessage(/*contentIsEmbeddedQuestion*/ false, 
									/*learningResourceHasSubtype*/ pageHasSubType);
							pageRevelLrCreateMessage.setProperty("LearningResource", page);
							messages.add(pageRevelLrCreateMessage);
							
							//Learning_Resource_Relationship message to establish the ChapterSection --> Page parent/child relationship
							Message chapterSectionToPageMessage = new LearningResourceRelationshipMessage(/*parentLearningResourceIsBook*/ false);
							chapterSectionToPageMessage.setProperty("CourseSection", courseSection);
							chapterSectionToPageMessage.setProperty("ParentLearningResource", chapSection);
							chapterSectionToPageMessage.setProperty("LearningResource", page);
							messages.add(chapterSectionToPageMessage);

							// nested reading pages
							if (page.getPages() != null) {
								for (Page pg : page.getPages()){
									boolean pgHasSubType = pg.getLearningResourceSubType() != null && !pg.getLearningResourceSubType().isEmpty();
									
									//Learning_Resource message to create the NestedPage as a learning resource
									Message pgRevelLrCreateMessage = new LearningResourceMessage(/*contentIsEmbeddedQuestion*/ false, 
											/*learningResourceHasSubtype*/ pgHasSubType);
									pgRevelLrCreateMessage.setProperty("LearningResource", pg);
									messages.add(pgRevelLrCreateMessage);
									
									//Learning_Resource_Relationship message to establish the Page --> NestedPage parent/child relationship
									Message pageToNestedPageMessage = new LearningResourceRelationshipMessage(/*parentLearningResourceIsBook*/ false);
									pageToNestedPageMessage.setProperty("CourseSection", courseSection);
									pageToNestedPageMessage.setProperty("ParentLearningResource", page);
									pageToNestedPageMessage.setProperty("LearningResource", pg);
									messages.add(pageToNestedPageMessage);
								}
							}
							
							if (page.getQuiz() != null) {
								Quiz embeddedQuiz = page.getQuiz();
								boolean embeddedQuizHasSubType = embeddedQuiz.getLearningResourceSubType() != null && !embeddedQuiz.getLearningResourceSubType().isEmpty();
								
								//Learning_Resource message to create the EmbeddedQuiz as a learning resource
								Message pgQuizRevelLrCreateMessage = new LearningResourceMessage(/*contentIsEmbeddedQuestion*/ true, 
										/*learningResourceHasSubtype*/ embeddedQuizHasSubType);
								pgQuizRevelLrCreateMessage.setProperty("LearningResource", embeddedQuiz);
								pgQuizRevelLrCreateMessage.setProperty("Page", page);
								messages.add(pgQuizRevelLrCreateMessage);
								
								//Learning_Resource_Relationship message to establish the Page --> EmbeddedQuiz parent/child relationship
								Message pageToEmbeddedQuizMessage = new LearningResourceRelationshipMessage(/*parentLearningResourceIsBook*/ false);
								pageToEmbeddedQuizMessage.setProperty("CourseSection", courseSection);
								pageToEmbeddedQuizMessage.setProperty("ParentLearningResource", page);
								pageToEmbeddedQuizMessage.setProperty("LearningResource", embeddedQuiz);
								messages.add(pageToEmbeddedQuizMessage);
								
								for (Question ques: embeddedQuiz.getQuestions()) {
									
									//Assessment_Item_Possible_Answers message to publish the Question
									Message assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers(QuestionType.MULTI_VALUE.value);
									assessmentItemPossibleAnswer.setProperty("Question", ques);
									messages.add(assessmentItemPossibleAnswer);
								}
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
