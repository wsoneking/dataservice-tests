package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Chapter;
import com.pearson.test.daalt.dataservice.model.ChapterSection;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.EmbeddedQuestion;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.request.message.LearningModuleContentMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningModuleMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;

public class SubPubSeerCreateAssignmentContentTestAction extends CreateAssignmentContentTestAction {

	public SubPubSeerCreateAssignmentContentTestAction(Instructor instructor, CourseSection courseSection, Assignment assignment) {
		messages = new ArrayList<>();
		this.instructor = instructor;
		this.courseSection = courseSection;
		this.assignment = assignment;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing CreateAssignmentContentTestAction...\n");
		
		//Learning_Module message to create the Assignment
		Message revelLmCreateMessage = new LearningModuleMessage();
		revelLmCreateMessage.setProperty("Transaction_Type_Code", "Create");
		revelLmCreateMessage.setProperty("CourseSection",  courseSection);
		revelLmCreateMessage.setProperty("Instructor",  instructor);
		revelLmCreateMessage.setProperty("Assignment", assignment);
		messages.add(revelLmCreateMessage);
				
		for (Chapter chapter : assignment.getChapters()) {
			
			//Learning_Module_Content message to add the Chapter learning resource to the Assignment
			Message chapterMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ 
					(chapter.getLearningResourceSubType() != null && !chapter.getLearningResourceSubType().isEmpty()), /*contentIsQuiz*/ false, /*contentIsEmbeddedQuestion*/ false);
			chapterMessage.setProperty("Transaction_Type_Code", "Create");
			chapterMessage.setProperty("CourseSection", courseSection);
			chapterMessage.setProperty("Assignment", assignment);
			chapterMessage.setProperty("LearningResource", chapter);
			messages.add(chapterMessage);
			
			Quiz chapterQuiz = chapter.getChapterQuiz();
			if (chapterQuiz != null) {
				
				//Learning_Module_Content message to add the ChapterQuiz learning resource to the Assignment
				Message chapterQuizMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ 
						(chapterQuiz.getLearningResourceSubType() != null && !chapterQuiz.getLearningResourceSubType().isEmpty()), /*contentIsQuiz*/ true, /*contentIsEmbeddedQuestion*/ false);
				chapterMessage.setProperty("Transaction_Type_Code", "Create");
				chapterQuizMessage.setProperty("CourseSection", courseSection);
				chapterQuizMessage.setProperty("Assignment", assignment);
				chapterQuizMessage.setProperty("LearningResource", chapterQuiz);
				messages.add(chapterQuizMessage);

				//iterate through nested quizzes
				if (chapterQuiz.getNestedQuizzes() != null){
					for (Quiz qz : chapterQuiz.getNestedQuizzes()) {
						
						//Learning_Module_Content message to add the NestedQuiz learning resource to the Assignment
						Message nestedQuizMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ 
								(qz.getLearningResourceSubType() != null && !qz.getLearningResourceSubType().isEmpty()), /*contentIsQuiz*/ true, /*contentIsEmbeddedQuestion*/ false);
						chapterMessage.setProperty("Transaction_Type_Code", "Create");
						nestedQuizMessage.setProperty("CourseSection", courseSection);
						nestedQuizMessage.setProperty("Assignment", assignment);
						nestedQuizMessage.setProperty("LearningResource", qz);
						messages.add(nestedQuizMessage);						
					}
				}
			}
			
			//iterate through chapter sections
			if (chapter.getChapterSections() != null){
				for (ChapterSection chapSection : chapter.getChapterSections()) {
					
					//Learning_Module_Content message to add the ChapterSection learning resource to the Assignment
					Message chapterSectionMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ 
							(chapSection.getLearningResourceSubType() != null && !chapSection.getLearningResourceSubType().isEmpty()), /*contentIsQuiz*/ false, /*contentIsEmbeddedQuestion*/ false);
					chapterMessage.setProperty("Transaction_Type_Code", "Create");
					chapterSectionMessage.setProperty("CourseSection", courseSection);
					chapterSectionMessage.setProperty("Assignment", assignment);
					chapterSectionMessage.setProperty("LearningResource", chapSection);
					messages.add(chapterSectionMessage);
					
					Quiz chapterSectionQuiz = chapSection.getChapterSectionQuiz();
					if (chapterSectionQuiz != null) {
						
						//Learning_Module_Content message to add the ChapterSectionQuiz learning resource to the Assignment
						Message chapterSectionQuizMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ 
								(chapterSectionQuiz.getLearningResourceSubType() != null && !chapterSectionQuiz.getLearningResourceSubType().isEmpty()), /*contentIsQuiz*/ true, /*contentIsEmbeddedQuestion*/ false);
						chapterMessage.setProperty("Transaction_Type_Code", "Create");
						chapterSectionQuizMessage.setProperty("CourseSection", courseSection);
						chapterSectionQuizMessage.setProperty("Assignment", assignment);
						chapterSectionQuizMessage.setProperty("LearningResource", chapterSectionQuiz);
						messages.add(chapterSectionQuizMessage);
						
						//Chapter Section Quiz cannot have nested assessments at this time
					}
				
					if (chapSection.getPages() != null) {
						for (Page page : chapSection.getPages()) {
							
							//Learning_Module_Content message to add the Page learning resource to the Assignment
							Message readingPageMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ 
									(page.getLearningResourceSubType() != null && !page.getLearningResourceSubType().isEmpty()), /*contentIsQuiz*/ false, /*contentIsEmbeddedQuestion*/ false);
							chapterMessage.setProperty("Transaction_Type_Code", "Create");
							readingPageMessage.setProperty("CourseSection", courseSection);
							readingPageMessage.setProperty("Assignment", assignment);
							readingPageMessage.setProperty("LearningResource", page);
							messages.add(readingPageMessage);

							// nested reading pages
							if (page.getPages() != null) {
								for (Page pg : page.getPages()){
									
									//Learning_Module_Content message to add the NestedPage learning resource to the Assignment
									Message readingPgMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ 
											(pg.getLearningResourceSubType() != null && !pg.getLearningResourceSubType().isEmpty()), /*contentIsQuiz*/ false, /*contentIsEmbeddedQuestion*/ false);
									chapterMessage.setProperty("Transaction_Type_Code", "Create");
									readingPgMessage.setProperty("CourseSection", courseSection);
									readingPgMessage.setProperty("Assignment", assignment);
									readingPgMessage.setProperty("LearningResource", pg);
									messages.add(readingPgMessage);
								}
							}
							
							if (page.getEmbeddedQuestions() != null) {
								float seqNum = 0;
								for (Question embeddedQuestion : page.getEmbeddedQuestions()) {
									EmbeddedQuestion embeddedQuestionLR = page.getEmbeddedQuestionLR(embeddedQuestion, seqNum);
									
									//Learning_Module_Content message to add the EmbeddedQuestion learning resource to the Assignment
									Message embeddedQuestionLMCMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ 
											(embeddedQuestionLR.getLearningResourceSubType() != null && !embeddedQuestionLR.getLearningResourceSubType().isEmpty()), /*contentIsQuiz*/ false, /*contentIsEmbeddedQuestion*/ true);
									chapterMessage.setProperty("Transaction_Type_Code", "Create");
									embeddedQuestionLMCMessage.setProperty("CourseSection", courseSection);
									embeddedQuestionLMCMessage.setProperty("Assignment", assignment);
									embeddedQuestionLMCMessage.setProperty("LearningResource", embeddedQuestionLR);
									messages.add(embeddedQuestionLMCMessage);
									
									seqNum++;
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
