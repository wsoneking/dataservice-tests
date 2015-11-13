package com.pearson.test.daalt.dataservice.request.action.version01;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.request.message.version01.BrixAssessmentItemTypeMessage;
import com.pearson.test.daalt.dataservice.request.message.version01.Message;
import com.pearson.test.daalt.dataservice.request.message.version01.PafPublishMessage;
import com.pearson.test.daalt.dataservice.request.message.version01.RevelLmContentMessage;
import com.pearson.test.daalt.dataservice.request.message.version01.RevelLmMessage;
import com.pearson.test.daalt.dataservice.request.message.version01.RevelLrMessage;
import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Chapter;
import com.pearson.test.daalt.dataservice.model.ChapterSection;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Quiz;

public class SubPubSeerAddAssignmentTestAction extends AddAssignmentTestAction {

	public SubPubSeerAddAssignmentTestAction(Instructor instructor, 
			CourseSection courseSection, Assignment assignment) {
		messages = new ArrayList<>();
		this.instructor = instructor;
		this.courseSection = courseSection;
		this.assignment = assignment;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {	
		checkCriticalObjects();
		
		//revel.lr.create - message for Learning Resource - Book
		Message bookRevelLrCreateMessage = new RevelLrMessage(/*hasParentResource*/ false);
		bookRevelLrCreateMessage.setProperty("CourseSection", courseSection);
		messages.add(bookRevelLrCreateMessage);
		
		//revel.lm.create - only one
		Message revelLmCreateMessage = new RevelLmMessage();
		//do not need to explicitly set Transaction_Type_Code because default value is "create"
		revelLmCreateMessage.setProperty("CourseSection",  courseSection);
		revelLmCreateMessage.setProperty("Instructor",  instructor);
		revelLmCreateMessage.setProperty("Assignment", assignment);
		messages.add(revelLmCreateMessage);
				
		//revel.lm.content.create - need a message for each learning resource in the assignment
		for (Chapter chapter : assignment.getChapters()) {
			
			//revel.lr.create - message for Learning Resource - Chapter
			Message chapterRevelLrCreateMessage = new RevelLrMessage(/*hasParentResource*/ true);
			chapterRevelLrCreateMessage.setProperty("LearningResource", chapter);
			chapterRevelLrCreateMessage.setProperty("Parent_Source_System_Record_Id", courseSection.getBookId());
			messages.add(chapterRevelLrCreateMessage);
			
			//add message for chapter
			Message chapterMessage = new RevelLmContentMessage(/*contentIsQuiz*/ false, /*learningResourceHasSubtype*/ false);
			chapterMessage.setProperty("CourseSection", courseSection);
			chapterMessage.setProperty("Assignment", assignment);
			chapterMessage.setProperty("LearningResource", chapter);
			messages.add(chapterMessage);
			
			//add message for chapter quiz
			Quiz chapterQuiz = chapter.getChapterQuiz();
			if (chapterQuiz != null) {
				
				//revel.lr.create - message for Learning Resource - Chapter Quiz
				Message chapterQuizRevelLrCreateMessage = new RevelLrMessage(/*hasParentResource*/ true);
				chapterQuizRevelLrCreateMessage.setProperty("LearningResource", chapterQuiz);
				chapterQuizRevelLrCreateMessage.setProperty("Parent_Source_System_Record_Id", chapter.getLearningResourceId());
				messages.add(chapterQuizRevelLrCreateMessage);
				
				Message chapterQuizMessage = new RevelLmContentMessage(/*contentIsQuiz*/ true, /*learningResourceHasSubtype*/ true);
				chapterQuizMessage.setProperty("CourseSection", courseSection);
				chapterQuizMessage.setProperty("Assignment", assignment);
				chapterQuizMessage.setProperty("LearningResource", chapterQuiz);
				messages.add(chapterQuizMessage);
				
				//PUBLISH
				Message chapterQuizPafPublishMessage = new PafPublishMessage();
				chapterQuizPafPublishMessage.setProperty("Quiz", chapterQuiz);
				messages.add(chapterQuizPafPublishMessage);
				
				//brix.assessment-item-type.create
				Message chapterQuizBrixAssessmentItemTypeCreateMessage = new BrixAssessmentItemTypeMessage();
				chapterQuizBrixAssessmentItemTypeCreateMessage.setProperty("Quiz", chapterQuiz);
				messages.add(chapterQuizBrixAssessmentItemTypeCreateMessage);
				

				//iterate through nested quizzes
				if (chapterQuiz.getNestedQuizzes() != null){
					for (Quiz qz : chapterQuiz.getNestedQuizzes()) {
						//revel.lr.create - message for Learning Resource - nested Quiz
						Message nestedQuizRevelLrCreateMessage = new RevelLrMessage(/*hasParentResource*/ true);
						nestedQuizRevelLrCreateMessage.setProperty("LearningResource", qz);
						nestedQuizRevelLrCreateMessage.setProperty("Parent_Source_System_Record_Id", chapterQuiz.getLearningResourceId());
						messages.add(nestedQuizRevelLrCreateMessage);
						
						Message nestedQuizMessage = new RevelLmContentMessage(/*contentIsQuiz*/ true, /*learningResourceHasSubtype*/ true);
						nestedQuizMessage.setProperty("CourseSection", courseSection);
						nestedQuizMessage.setProperty("Assignment", assignment);
						nestedQuizMessage.setProperty("LearningResource", qz);
						messages.add(nestedQuizMessage);
						
						//PUBLISH
						Message nestedQuizPafPublishMessage = new PafPublishMessage();
						nestedQuizPafPublishMessage.setProperty("Quiz", qz);
						messages.add(nestedQuizPafPublishMessage);
						
						//brix.assessment-item-type.create
						Message nestedQuizBrixAssessmentItemTypeCreateMessage = new BrixAssessmentItemTypeMessage();
						nestedQuizBrixAssessmentItemTypeCreateMessage.setProperty("Quiz", qz);
						messages.add(nestedQuizBrixAssessmentItemTypeCreateMessage);
						
					}
				}
			}
			
			//iterate through chapter sections
			if (chapter.getChapterSections() != null){
				for (ChapterSection chapSection : chapter.getChapterSections()) {
					
					//revel.lr.create - message for Learning Resource - Chapter Section
					Message chapterSectionRevelLrCreateMessage = new RevelLrMessage(/*hasParentResource*/ true);
					chapterSectionRevelLrCreateMessage.setProperty("LearningResource", chapSection);
					chapterSectionRevelLrCreateMessage.setProperty("Parent_Source_System_Record_Id", chapter.getLearningResourceId());
					messages.add(chapterSectionRevelLrCreateMessage);
					
					//add message for chapter section
					Message chapterSectionMessage = new RevelLmContentMessage(/*contentIsQuiz*/ false, /*learningResourceHasSubtype*/ false);
					chapterSectionMessage.setProperty("CourseSection", courseSection);
					chapterSectionMessage.setProperty("Assignment", assignment);
					chapterSectionMessage.setProperty("LearningResource", chapSection);
					messages.add(chapterSectionMessage);
					
					//add message for chapter section quiz
					Quiz chapterSectionQuiz = chapSection.getChapterSectionQuiz();
					if (chapterSectionQuiz != null) {
						
						//revel.lr.create - message for Learning Resource - Chapter Section Quiz
						Message chapterSectionQuizRevelLrCreateMessage = new RevelLrMessage(/*hasParentResource*/ true);
						chapterSectionQuizRevelLrCreateMessage.setProperty("LearningResource", chapterSectionQuiz);
						chapterSectionQuizRevelLrCreateMessage.setProperty("Parent_Source_System_Record_Id", chapSection.getLearningResourceId());
						messages.add(chapterSectionQuizRevelLrCreateMessage);
						
						Message chapterSectionQuizMessage = new RevelLmContentMessage(/*contentIsQuiz*/ true, /*learningResourceHasSubtype*/ true);
						chapterSectionQuizMessage.setProperty("CourseSection", courseSection);
						chapterSectionQuizMessage.setProperty("Assignment", assignment);
						chapterSectionQuizMessage.setProperty("LearningResource", chapterSectionQuiz);
						messages.add(chapterSectionQuizMessage);
						
						//PUBLISH
						Message chapterSectionQuizPafPublishMessage = new PafPublishMessage();
						chapterSectionQuizPafPublishMessage.setProperty("Quiz", chapterSectionQuiz);
						messages.add(chapterSectionQuizPafPublishMessage);
						
						//brix.assessment-item-type.create
						Message chapterSectionQuizBrixAssessmentItemTypeCreateMessage = new BrixAssessmentItemTypeMessage();
						chapterSectionQuizBrixAssessmentItemTypeCreateMessage.setProperty("Quiz", chapterSectionQuiz);
						messages.add(chapterSectionQuizBrixAssessmentItemTypeCreateMessage);
						
						//Chapter Section Quiz cannot have nested assessments at this time
					}
				
					//iterate through pages
					if (chapSection.getPages() != null) {
						for (Page page : chapSection.getPages()) {
							
							//revel.lr.create - message for Learning Resource - Page
							Message pageRevelLrCreateMessage = new RevelLrMessage(/*hasParentResource*/ true);
							pageRevelLrCreateMessage.setProperty("LearningResource", page);
							pageRevelLrCreateMessage.setProperty("Parent_Source_System_Record_Id", chapSection.getLearningResourceId());
							messages.add(pageRevelLrCreateMessage);
							
							//add message for reading page
							Message readingPageMessage = new RevelLmContentMessage(/*contentIsQuiz*/ false, /*learningResourceHasSubtype*/ false);
							readingPageMessage.setProperty("CourseSection", courseSection);
							readingPageMessage.setProperty("Assignment", assignment);
							readingPageMessage.setProperty("LearningResource", page);
							messages.add(readingPageMessage);
							
							// nested reading pages
							if (page.getPages() != null) {
								for (Page pg : page.getPages()){
									//revel.lr.create - message for Learning Resource - Page
									Message pgRevelLrCreateMessage = new RevelLrMessage(/*hasParentResource*/ true);
									pgRevelLrCreateMessage.setProperty("LearningResource", pg);
									pgRevelLrCreateMessage.setProperty("Parent_Source_System_Record_Id", page.getLearningResourceId());
									messages.add(pgRevelLrCreateMessage);
									
									//add message for reading page
									Message readingPgMessage = new RevelLmContentMessage(/*contentIsQuiz*/ false, /*learningResourceHasSubtype*/ false);
									readingPgMessage.setProperty("CourseSection", courseSection);
									readingPgMessage.setProperty("Assignment", assignment);
									readingPgMessage.setProperty("LearningResource", pg);
									messages.add(readingPgMessage);
								}
							}
							
							//check for embedded quizzes and add appropriate messages
							if(page.getQuiz() != null){
								Quiz embeddedquiz = page.getQuiz();
								//revel.lr.create - message for Learning Resource - Embedded Quiz
								Message embeddedQuizRevelLrCreateMessage = new RevelLrMessage(/*hasParentResource*/ true);
								embeddedQuizRevelLrCreateMessage.setProperty("LearningResource", embeddedquiz);
								embeddedQuizRevelLrCreateMessage.setProperty("Parent_Source_System_Record_Id", page.getLearningResourceId());
								messages.add(embeddedQuizRevelLrCreateMessage);
								
								Message embeddedQuizMessage = new RevelLmContentMessage(/*contentIsQuiz*/ true, /*learningResourceHasSubtype*/ true);
								embeddedQuizMessage.setProperty("CourseSection", courseSection);
								embeddedQuizMessage.setProperty("Assignment", assignment);
								embeddedQuizMessage.setProperty("LearningResource", embeddedquiz);
								messages.add(embeddedQuizMessage);
								
								//PUBLISH
								Message embeddedQuizPafPublishMessage = new PafPublishMessage();
								embeddedQuizPafPublishMessage.setProperty("Quiz", embeddedquiz);
								messages.add(embeddedQuizPafPublishMessage);
								
								//brix.assessment-item-type.create
								Message embeddedQuizBrixAssessmentItemTypeCreateMessage = new BrixAssessmentItemTypeMessage();
								embeddedQuizBrixAssessmentItemTypeCreateMessage.setProperty("Quiz", embeddedquiz);
								messages.add(embeddedQuizBrixAssessmentItemTypeCreateMessage);
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
