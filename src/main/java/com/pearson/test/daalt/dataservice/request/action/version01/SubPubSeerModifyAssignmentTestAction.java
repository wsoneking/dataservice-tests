package com.pearson.test.daalt.dataservice.request.action.version01;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.request.message.version01.Message;
import com.pearson.test.daalt.dataservice.request.message.version01.RevelLmContentMessage;
import com.pearson.test.daalt.dataservice.request.message.version01.RevelLmMessage;
import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Chapter;
import com.pearson.test.daalt.dataservice.model.ChapterSection;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Quiz;

public class SubPubSeerModifyAssignmentTestAction extends ModifyAssignmentTestAction {

	public SubPubSeerModifyAssignmentTestAction(Instructor instructor, 
			CourseSection courseSection, Assignment assignment, String verb) {
		messages = new ArrayList<>();
		this.instructor = instructor;
		this.courseSection = courseSection;
		this.assignment = assignment;
		this.verb = verb;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {	
		checkCriticalObjects();
		
		Message revelLmCreateMessage = new RevelLmMessage();
		revelLmCreateMessage.setProperty("Transaction_Type_Code",  verb);
		revelLmCreateMessage.setProperty("CourseSection",  courseSection);
		revelLmCreateMessage.setProperty("Instructor",  instructor);
		revelLmCreateMessage.setProperty("Assignment", assignment);
		messages.add(revelLmCreateMessage);
				
		//revel.lm.content.create - need a message for each learning resource in the assignment
		for (Chapter chapter : assignment.getChapters()) {
			//add message for chapter
			Message chapterMessage = new RevelLmContentMessage(/*contentIsQuiz*/ false, /*learningResourceHasSubtype*/ false);
			chapterMessage.setProperty("Transaction_Type_Code",  verb);
			chapterMessage.setProperty("CourseSection", courseSection);
			chapterMessage.setProperty("Assignment", assignment);
			chapterMessage.setProperty("LearningResource", chapter);
			messages.add(chapterMessage);
			
			//add message for chapter quiz
			Quiz chapterQuiz = chapter.getChapterQuiz();
			if (chapterQuiz != null) {
				Message chapterQuizMessage = new RevelLmContentMessage(/*contentIsQuiz*/ true, /*learningResourceHasSubtype*/ true);
				chapterQuizMessage.setProperty("Transaction_Type_Code",  verb);
				chapterQuizMessage.setProperty("CourseSection", courseSection);
				chapterQuizMessage.setProperty("Assignment", assignment);
				chapterQuizMessage.setProperty("LearningResource", chapterQuiz);
				messages.add(chapterQuizMessage);
				
				//iterate through nested quizzes
				if (chapterQuiz.getNestedQuizzes() != null){
					for (Quiz qz : chapterQuiz.getNestedQuizzes()) {
						Message nestedQuizMessage = new RevelLmContentMessage(/*contentIsQuiz*/ true, /*learningResourceHasSubtype*/ true);
						chapterQuizMessage.setProperty("Transaction_Type_Code",  verb);
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
					
					//add message for chapter section
					Message chapterSectionMessage = new RevelLmContentMessage(/*contentIsQuiz*/ false, /*learningResourceHasSubtype*/ false);
					chapterSectionMessage.setProperty("Transaction_Type_Code",  verb);
					chapterSectionMessage.setProperty("CourseSection", courseSection);
					chapterSectionMessage.setProperty("Assignment", assignment);
					chapterSectionMessage.setProperty("LearningResource", chapSection);
					messages.add(chapterSectionMessage);
					
					//add message for chapter section quiz
					Quiz chapterSectionQuiz = chapSection.getChapterSectionQuiz();
					if (chapterSectionQuiz != null) {
						Message chapterSectionQuizMessage = new RevelLmContentMessage(/*contentIsQuiz*/ true, /*learningResourceHasSubtype*/ true);
						chapterSectionQuizMessage.setProperty("Transaction_Type_Code",  verb);
						chapterSectionQuizMessage.setProperty("CourseSection", courseSection);
						chapterSectionQuizMessage.setProperty("Assignment", assignment);
						chapterSectionQuizMessage.setProperty("LearningResource", chapterSectionQuiz);
						messages.add(chapterSectionQuizMessage);
					}
					
					//iterate through pages
					if (chapSection.getPages() != null) {
						for (Page page : chapSection.getPages()) {
							//add message for reading page
							Message readingPageMessage = new RevelLmContentMessage(/*contentIsQuiz*/ false, /*learningResourceHasSubtype*/ false);
							readingPageMessage.setProperty("Transaction_Type_Code",  verb);
							readingPageMessage.setProperty("CourseSection", courseSection);
							readingPageMessage.setProperty("Assignment", assignment);
							readingPageMessage.setProperty("LearningResource", page);
							messages.add(readingPageMessage);

							// nested reading pages
							if (page.getPages() != null) {
								for (Page pg : page.getPages()){
									//add message for reading page
									Message readingPgMessage = new RevelLmContentMessage(/*contentIsQuiz*/ false, /*learningResourceHasSubtype*/ false);
									readingPageMessage.setProperty("Transaction_Type_Code",  verb);
									readingPgMessage.setProperty("CourseSection", courseSection);
									readingPgMessage.setProperty("Assignment", assignment);
									readingPgMessage.setProperty("LearningResource", pg);
									messages.add(readingPgMessage);
								}
							}
							
							//check for embedded quizzes and add appropriate messages
							if(page.getQuiz() != null){
								Quiz embeddedquiz = page.getQuiz();
								Message embeddedQuizMessage = new RevelLmContentMessage(/*contentIsQuiz*/ true, /*learningResourceHasSubtype*/ true);
								embeddedQuizMessage.setProperty("Transaction_Type_Code", verb);
								embeddedQuizMessage.setProperty("CourseSection", courseSection);
								embeddedQuizMessage.setProperty("Assignment", assignment);
								embeddedQuizMessage.setProperty("LearningResource", embeddedquiz);
								messages.add(embeddedQuizMessage);
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
