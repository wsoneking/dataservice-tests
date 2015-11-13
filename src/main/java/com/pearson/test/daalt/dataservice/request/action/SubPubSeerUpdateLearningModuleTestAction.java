package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.pearson.test.daalt.dataservice.request.message.AssessmentItemPossibleAnswers;
import com.pearson.test.daalt.dataservice.request.message.CourseSectionLearningResourceMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningModuleMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningResourceRelationshipMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.AssessmentMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningModuleContentMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningResourceMessage;
import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.BasicQuiz;
import com.pearson.test.daalt.dataservice.model.Chapter;
import com.pearson.test.daalt.dataservice.model.ChapterSection;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.EmbeddedQuestion;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;

public class SubPubSeerUpdateLearningModuleTestAction extends UpdateLearningModuleTestAction {

	public SubPubSeerUpdateLearningModuleTestAction(Instructor instructor, 
			CourseSection courseSection, Assignment assignment) {
		messages = new ArrayList<>();
		this.instructor = instructor;
		this.courseSection = courseSection;
		this.assignment = assignment;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing UpdateLearningModuleTestAction...\n");

		//Learning_Module message to create the Assignment
		Message revelLmCreateMessage = new LearningModuleMessage();
		revelLmCreateMessage.setProperty("Transaction_Type_Code", "Update");
		revelLmCreateMessage.setProperty("CourseSection",  courseSection);
		revelLmCreateMessage.setProperty("Instructor",  instructor);
		revelLmCreateMessage.setProperty("Assignment", assignment);
		messages.add(revelLmCreateMessage);
				
//		for (Chapter chapter : assignment.getChapters()) {
//			boolean chapterHasSubType = chapter.getLearningResourceSubType() != null && !chapter.getLearningResourceSubType().isEmpty();
//			
//			//Learning_Module_Content message to add the Chapter learning resource to the Assignment
//			Message chapterMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ chapterHasSubType);
//			chapterMessage.setProperty("Transaction_Type_Code", "Update");
//			chapterMessage.setProperty("CourseSection", courseSection);
//			chapterMessage.setProperty("Assignment", assignment);
//			chapterMessage.setProperty("LearningResource", chapter);
//			messages.add(chapterMessage);
//			
//			Quiz chapterQuiz = chapter.getChapterQuiz();
//			if (chapterQuiz != null) {
//				boolean chapterQuizHasSubType = chapterQuiz.getLearningResourceSubType() != null && !chapterQuiz.getLearningResourceSubType().isEmpty();
//				
//				//Learning_Module_Content message to add the ChapterQuiz learning resource to the Assignment
//				Message chapterQuizMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ chapterQuizHasSubType);
//				chapterQuizMessage.setProperty("Transaction_Type_Code", "Update");
//				chapterQuizMessage.setProperty("CourseSection", courseSection);
//				chapterQuizMessage.setProperty("Assignment", assignment);
//				chapterQuizMessage.setProperty("LearningResource", chapterQuiz);
//				messages.add(chapterQuizMessage);
//
//				//iterate through nested quizzes
//				if (chapterQuiz.getNestedQuizzes() != null){
//					for (Quiz qz : chapterQuiz.getNestedQuizzes()) {
//						boolean qzHasSubType = qz.getLearningResourceSubType() != null && !qz.getLearningResourceSubType().isEmpty();
//						
//						//Learning_Module_Content message to add the NestedQuiz learning resource to the Assignment
//						Message nestedQuizMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ 
//								qzHasSubType);
//						nestedQuizMessage.setProperty("Transaction_Type_Code", "Update");
//						nestedQuizMessage.setProperty("CourseSection", courseSection);
//						nestedQuizMessage.setProperty("Assignment", assignment);
//						nestedQuizMessage.setProperty("LearningResource", qz);
//						messages.add(nestedQuizMessage);
//						
//					}
//				}
//			}
//			
//			//iterate through chapter sections
//			if (chapter.getChapterSections() != null){
//				for (ChapterSection chapSection : chapter.getChapterSections()) {
//					boolean chapSectionHasSubType = chapSection.getLearningResourceSubType() != null && !chapSection.getLearningResourceSubType().isEmpty();
//					
//					//Learning_Module_Content message to add the ChapterSection learning resource to the Assignment
//					Message chapterSectionMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ chapSectionHasSubType);
//					chapterSectionMessage.setProperty("Transaction_Type_Code", "Update");
//					chapterSectionMessage.setProperty("CourseSection", courseSection);
//					chapterSectionMessage.setProperty("Assignment", assignment);
//					chapterSectionMessage.setProperty("LearningResource", chapSection);
//					messages.add(chapterSectionMessage);
//					
//					Quiz chapterSectionQuiz = chapSection.getChapterSectionQuiz();
//					if (chapterSectionQuiz != null) {
//						boolean chapterSectionQuizHasSubType = chapterSectionQuiz.getLearningResourceSubType() != null && !chapterSectionQuiz.getLearningResourceSubType().isEmpty();
//
//						//Learning_Module_Content message to add the ChapterSectionQuiz learning resource to the Assignment
//						Message chapterSectionQuizMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ chapterSectionQuizHasSubType);
//						chapterSectionQuizMessage.setProperty("Transaction_Type_Code", "Update");
//						chapterSectionQuizMessage.setProperty("CourseSection", courseSection);
//						chapterSectionQuizMessage.setProperty("Assignment", assignment);
//						chapterSectionQuizMessage.setProperty("LearningResource", chapterSectionQuiz);
//						messages.add(chapterSectionQuizMessage);
//						
//						//Chapter Section Quiz cannot have nested assessments at this time
//					}
//				
//					if (chapSection.getPages() != null) {
//						for (Page page : chapSection.getPages()) {
//							boolean pageHasSubType = page.getLearningResourceSubType() != null && !page.getLearningResourceSubType().isEmpty();
//
//							//Learning_Module_Content message to add the Page learning resource to the Assignment
//							Message readingPageMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ pageHasSubType);
//							readingPageMessage.setProperty("Transaction_Type_Code", "Update");
//							readingPageMessage.setProperty("CourseSection", courseSection);
//							readingPageMessage.setProperty("Assignment", assignment);
//							readingPageMessage.setProperty("LearningResource", page);
//							messages.add(readingPageMessage);
//
//							// nested reading pages
//							if (page.getPages() != null) {
//								for (Page pg : page.getPages()){
//									boolean pgHasSubType = pg.getLearningResourceSubType() != null && !pg.getLearningResourceSubType().isEmpty();
//									
//									//Learning_Module_Content message to add the NestedPage learning resource to the Assignment
//									Message readingPgMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ pgHasSubType);
//									readingPgMessage.setProperty("Transaction_Type_Code", "Update");
//									readingPgMessage.setProperty("CourseSection", courseSection);
//									readingPgMessage.setProperty("Assignment", assignment);
//									readingPgMessage.setProperty("LearningResource", pg);
//									messages.add(readingPgMessage);
//								}
//							}
//							
//							if (page.getEmbeddedQuestions() != null) {
//								float seqNum = 0;
//								for (Question embeddedQuestion : page.getEmbeddedQuestions()) {
//									EmbeddedQuestion embeddedQuestionLR = page.getEmbeddedQuestionLR(embeddedQuestion, seqNum);
//									
//									boolean embeddedQuestionHasSubType = embeddedQuestionLR.getLearningResourceSubType() != null && !embeddedQuestionLR.getLearningResourceSubType().isEmpty();
//
//									//Learning_Module_Content message to add the EmbeddedQuestion learning resource to the Assignment
//									Message embeddedQuestionLMCMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ embeddedQuestionHasSubType);
//									embeddedQuestionLMCMessage.setProperty("Transaction_Type_Code", "Update");
//									embeddedQuestionLMCMessage.setProperty("CourseSection", courseSection);
//									embeddedQuestionLMCMessage.setProperty("Assignment", assignment);
//									embeddedQuestionLMCMessage.setProperty("LearningResource", embeddedQuestionLR);
//									messages.add(embeddedQuestionLMCMessage);
//									
//									seqNum++;
//								}
//							}
//						}	
//					}
//				}	
//			}
//		}
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
		}
	}
}
