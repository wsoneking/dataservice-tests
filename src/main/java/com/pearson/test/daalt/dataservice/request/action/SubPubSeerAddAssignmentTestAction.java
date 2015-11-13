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
import com.pearson.test.daalt.dataservice.model.MultiValueOtherQuestion;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionPresentationFormat;
import com.pearson.test.daalt.dataservice.model.QuestionType;
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
	public void execute(int subpubCount, int seerCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing AddAssignmentTestAction...\n");
		
		Map<String, Quiz> quizMap = new HashMap<>();
		
		//Learning_Resource message to create the Book as a learning resource
		Message bookRevelLrCreateMessage = new LearningResourceMessage(/*contentIsEmbeddedQuestion*/ false, /*learningResourceHasSubtype*/ false);
		bookRevelLrCreateMessage.setProperty("CourseSection", courseSection);
		messages.add(bookRevelLrCreateMessage);
		
		//Course_Section_To_Learning_Resource message to connect the book to the CourseSection as a LearningResource
		Message bookToCourseSectionMessage = new CourseSectionLearningResourceMessage();
		bookToCourseSectionMessage.setProperty("CourseSection", courseSection);
		messages.add(bookToCourseSectionMessage);
		
		//Learning_Module message to create the Assignment
		Message revelLmCreateMessage = new LearningModuleMessage();
		//do not need to explicitly set Transaction_Type_Code because default value is "create"
		revelLmCreateMessage.setProperty("CourseSection",  courseSection);
		revelLmCreateMessage.setProperty("Instructor",  instructor);
		revelLmCreateMessage.setProperty("Assignment", assignment);
		messages.add(revelLmCreateMessage);
				
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
			
			//Learning_Module_Content message to add the Chapter learning resource to the Assignment
			Message chapterMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ chapterHasSubType, /*contentIsQuiz*/ false, /*contentIsEmbeddedQuestion*/ false);
			chapterMessage.setProperty("CourseSection", courseSection);
			chapterMessage.setProperty("Assignment", assignment);
			chapterMessage.setProperty("LearningResource", chapter);
			messages.add(chapterMessage);
			
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
				
				//Learning_Module_Content message to add the ChapterQuiz learning resource to the Assignment
				Message chapterQuizMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ chapterQuizHasSubType, /*contentIsQuiz*/ true, /*contentIsEmbeddedQuestion*/ false);
				chapterQuizMessage.setProperty("CourseSection", courseSection);
				chapterQuizMessage.setProperty("Assignment", assignment);
				chapterQuizMessage.setProperty("LearningResource", chapterQuiz);
				messages.add(chapterQuizMessage);
				
				quizMap.put(chapterQuiz.getId(), chapterQuiz);

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
						
						//Learning_Module_Content message to add the NestedQuiz learning resource to the Assignment
						Message nestedQuizMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ 
								qzHasSubType, /*contentIsQuiz*/ true, /*contentIsEmbeddedQuestion*/ false);
						nestedQuizMessage.setProperty("CourseSection", courseSection);
						nestedQuizMessage.setProperty("Assignment", assignment);
						nestedQuizMessage.setProperty("LearningResource", qz);
						messages.add(nestedQuizMessage);
						
						quizMap.put(qz.getId(), qz);
						
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
						
						quizMap.put(chapterSectionQuiz.getId(), chapterSectionQuiz);
						
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
							
							//Learning_Module_Content message to add the Page learning resource to the Assignment
							Message readingPageMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ pageHasSubType, /*contentIsQuiz*/ false, /*contentIsEmbeddedQuestion*/ false);
							readingPageMessage.setProperty("CourseSection", courseSection);
							readingPageMessage.setProperty("Assignment", assignment);
							readingPageMessage.setProperty("LearningResource", page);
							messages.add(readingPageMessage);

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
									
									//Learning_Module_Content message to add the NestedPage learning resource to the Assignment
									Message readingPgMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ pgHasSubType, /*contentIsQuiz*/ false, /*contentIsEmbeddedQuestion*/ false);
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
									
									boolean embeddedQuestionHasSubType = embeddedQuestionLR.getLearningResourceSubType() != null && !embeddedQuestionLR.getLearningResourceSubType().isEmpty();
									
									//Learning_Resource message to create the EmbeddedQuestion as a learning resource
									Message embeddedQuestionLRMessage = new LearningResourceMessage(/*contentIsEmbeddedQuestion*/ true, 
											/*learningResourceHasSubtype*/ embeddedQuestionHasSubType);
									embeddedQuestionLRMessage.setProperty("LearningResource", embeddedQuestionLR);
									embeddedQuestionLRMessage.setProperty("Page", page);
									messages.add(embeddedQuestionLRMessage);
									
									//Learning_Resource_Relationship message to establish the Page --> EmbeddedQuestion parent/child relationship
									Message pageToEmbeddedQuestionMessage = new LearningResourceRelationshipMessage(/*parentLearningResourceIsBook*/ false);
									pageToEmbeddedQuestionMessage.setProperty("CourseSection", courseSection);
									pageToEmbeddedQuestionMessage.setProperty("ParentLearningResource", page);
									pageToEmbeddedQuestionMessage.setProperty("LearningResource", embeddedQuestionLR);
									messages.add(pageToEmbeddedQuestionMessage);
									
									//Learning_Module_Content message to add the EmbeddedQuestion learning resource to the Assignment
									Message embeddedQuestionLMCMessage = new LearningModuleContentMessage(/*learningResourceHasSubtype*/ embeddedQuestionHasSubType, /*contentIsQuiz*/ false, /*contentIsEmbeddedQuestion*/ true);
									embeddedQuestionLMCMessage.setProperty("CourseSection", courseSection);
									embeddedQuestionLMCMessage.setProperty("Assignment", assignment);
									embeddedQuestionLMCMessage.setProperty("LearningResource", embeddedQuestionLR);
									messages.add(embeddedQuestionLMCMessage);
									
									if(!quizMap.containsKey(embeddedQuestion.getAssessmentId())) {
										Quiz newQuiz = new BasicQuiz();
										newQuiz.setId(embeddedQuestion.getAssessmentId());
										newQuiz.setSeedDateTime(embeddedQuestion.getQuestionLastSeedDateTime());
										quizMap.put(embeddedQuestion.getAssessmentId(), newQuiz);
									}
									Quiz wrappingQuiz = quizMap.get(embeddedQuestion.getAssessmentId());
									if (!wrappingQuiz.containsQuestionWithId(embeddedQuestion.getId())) {
										wrappingQuiz.addQuestion(embeddedQuestion);
									}
									
									seqNum++;
								}
							}
						}	
					}
				}	
			}
		}
		
		//iterate through quiz map and send publication messages
		Iterator<String> quizMapIter = quizMap.keySet().iterator();
		while (quizMapIter.hasNext()) {
			Quiz quiz = quizMap.get(quizMapIter.next());
			
			//Assessment message to publish the Quiz
			Message chapterSectionQuizPafPublishMessage = new AssessmentMessage();
			chapterSectionQuizPafPublishMessage.setProperty("Quiz", quiz);
			messages.add(chapterSectionQuizPafPublishMessage);
			
			for (Question ques: quiz.getQuestions()) {
				Message assessmentItemPossibleAnswer;
				
				//Assessment_Item_Possible_Answers message to publish the Question
				if (ques.getQuestionPresentationFormat().equalsIgnoreCase(QuestionPresentationFormat.FILL_IN_THE_BLANK.value)) {
					assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers(QuestionPresentationFormat.FILL_IN_THE_BLANK.value);
				} else if (ques.getQuestionPresentationFormat().equalsIgnoreCase(QuestionPresentationFormat.NUMERIC.value)) {
					assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers(QuestionPresentationFormat.NUMERIC.value);
				} else if (ques.getQuestionType().equals(QuestionType.MULTI_VALUE.value) && !(ques instanceof MultiValueOtherQuestion)) {
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
		
		for (Message msg : messages) {
			msg.send(subpubCount,seerCount);
		}
	}
}
