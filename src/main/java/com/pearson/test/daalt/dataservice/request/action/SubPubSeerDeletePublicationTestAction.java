package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.BasicQuiz;
import com.pearson.test.daalt.dataservice.model.Chapter;
import com.pearson.test.daalt.dataservice.model.ChapterSection;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionType;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemPossibleAnswers;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.AssessmentMessage;

public class SubPubSeerDeletePublicationTestAction extends DeletePublicationTestAction {

	public SubPubSeerDeletePublicationTestAction(Instructor instructor, 
			CourseSection courseSection, Assignment assignment) {
		messages = new ArrayList<>();
		this.instructor = instructor;
		this.courseSection = courseSection;
		this.assignment = assignment;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing DeletePublicationTestAction...\n");
		
		Map<String, Quiz> quizMap = new HashMap<>();
				
		for (Chapter chapter : assignment.getChapters()) {
			
			Quiz chapterQuiz = chapter.getChapterQuiz();
			if (chapterQuiz != null) {
				
				quizMap.put(chapterQuiz.getId(), chapterQuiz);

				//iterate through nested quizzes
				if (chapterQuiz.getNestedQuizzes() != null){
					for (Quiz qz : chapterQuiz.getNestedQuizzes()) {
						quizMap.put(qz.getId(), qz);	
					}
				}
			}
			
			//iterate through chapter sections
			if (chapter.getChapterSections() != null){
				for (ChapterSection chapSection : chapter.getChapterSections()) {
					Quiz chapterSectionQuiz = chapSection.getChapterSectionQuiz();
					if (chapterSectionQuiz != null) {
						quizMap.put(chapterSectionQuiz.getId(), chapterSectionQuiz);
					}
				
					if (chapSection.getPages() != null) {
						for (Page page : chapSection.getPages()) {
							if (page.getEmbeddedQuestions() != null) {
								for (Question embeddedQuestion : page.getEmbeddedQuestions()) {
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
			
			//Assessment message
			Message pafPublishMessage = new AssessmentMessage();
			pafPublishMessage.setProperty("Quiz", quiz);
			pafPublishMessage.setProperty("Transaction_Type_Code", "Delete");
			messages.add(pafPublishMessage);
			
			for (Question ques: quiz.getQuestions()) {
				
				//Assessment_Item_Possible_Answers message
				Message assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers(QuestionType.MULTI_VALUE.value);
				assessmentItemPossibleAnswer.setProperty("Question", ques);
				assessmentItemPossibleAnswer.setProperty("Transaction_Type_Code", "Delete");
				messages.add(assessmentItemPossibleAnswer);
			}
		}
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
		}
	}
}
