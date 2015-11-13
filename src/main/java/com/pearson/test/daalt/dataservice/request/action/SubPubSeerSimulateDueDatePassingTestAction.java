package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.BasicQuiz;
import com.pearson.test.daalt.dataservice.model.BasicStudent;
import com.pearson.test.daalt.dataservice.model.Chapter;
import com.pearson.test.daalt.dataservice.model.ChapterSection;
import com.pearson.test.daalt.dataservice.model.CompletionActivityOriginCode;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionPresentationFormat;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemCompletionMessage;
import com.pearson.test.daalt.dataservice.request.message.AssessmentPerformanceMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningModuleMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningModulePerformanceMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;

public class SubPubSeerSimulateDueDatePassingTestAction extends SimulateDueDatePassingTestAction {

	public SubPubSeerSimulateDueDatePassingTestAction(Instructor instructor, CourseSection courseSection, 
			Assignment assignment){
		messages = new ArrayList<>();
		this.instructor = instructor;
		this.courseSection = courseSection;
		this.assignment = assignment;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing SimulateDueDatePassingTestAction...\n");
		
		User system = new BasicStudent (null, null, null, null, null);
		system.setPersonRole(CompletionActivityOriginCode.SYSTEM.value);
		
		//Learning_Module message to update the Assignment
		Message revelLmUpdateMessage = new LearningModuleMessage();
		revelLmUpdateMessage.setProperty("Transaction_Type_Code", "Update");
		revelLmUpdateMessage.setProperty("CourseSection",  courseSection);
		revelLmUpdateMessage.setProperty("Instructor",  instructor);
		revelLmUpdateMessage.setProperty("Assignment", assignment);
		messages.add(revelLmUpdateMessage);
		
		if (courseSection.getEnrolledStudents() != null) {
			//Learning_Module_Performance messages
//			for (User student : courseSection.getEnrolledStudents()) {
//				if (!assignment.studentCompletedAssignment(student)) {
//					Message lmPerfMessage = new LearningModulePerformanceMessage(student);
//					lmPerfMessage.setProperty("Transaction_Type_Code", "Create");
//					lmPerfMessage.setProperty("CompletionOriginator", CompletionActivityOriginCode.SYSTEM.value);
//					lmPerfMessage.setProperty("CourseSection", courseSection);
//					lmPerfMessage.setProperty("Assignment", assignment);
//					messages.add(lmPerfMessage);
//				}
//			}
			
			for (Chapter chapter : assignment.getChapters()) {
				Quiz chapterQuiz = chapter.getChapterQuiz();
				if (chapterQuiz != null) {
					for (User student : courseSection.getEnrolledStudents()) {
						if (!chapterQuiz.studentCompletedQuiz(student)) {
							system.setPersonId(student.getPersonId());
							QuizCompletionActivity chapQuizCompletionActivity = new QuizCompletionActivity(system);
							chapQuizCompletionActivity.setAssignmentComplete(true);
							for(Question ques : chapterQuiz.getQuestions()){
								chapQuizCompletionActivity.addQuestionPerf(ques.getId(), /*pointsEarned*/ 0f);
							}
							
							//Assessment_Performance messages for chapterQuiz
							Message assessPerfMessage = new AssessmentPerformanceMessage(student, QuestionPresentationFormat.RADIO_BUTTON);
							assessPerfMessage.setProperty("Quiz", chapterQuiz);
							assessPerfMessage.setProperty("QuizCompletionActivity", chapQuizCompletionActivity);
							assessPerfMessage.setProperty("CourseSection", courseSection);
							assessPerfMessage.setProperty("Assignment", assignment);
							messages.add(assessPerfMessage);
						}
						
						for (Question question : chapterQuiz.getQuestions()) {
							//Assessment_Item_Completion messages for chapterQuiz
							if (!question.studentCompletedQuestion(student)) {
								Message assessItemCompletionMessage = new AssessmentItemCompletionMessage();
								assessItemCompletionMessage.setProperty("CourseSection", courseSection);
								assessItemCompletionMessage.setProperty("Quiz", chapterQuiz);
								assessItemCompletionMessage.setProperty("Question", question);
								assessItemCompletionMessage.setProperty("Person", student);
								assessItemCompletionMessage.setProperty("CompletionOriginator", CompletionActivityOriginCode.SYSTEM.value);
								messages.add(assessItemCompletionMessage);
							}
						}
					}
	
					//iterate through nested quizzes
					if (chapterQuiz.getNestedQuizzes() != null){
						for (Quiz nestedQuiz : chapterQuiz.getNestedQuizzes()) {
							for (User student : courseSection.getEnrolledStudents()) {
								if (!nestedQuiz.studentCompletedQuiz(student)) {
									system.setPersonId(student.getPersonId());
									QuizCompletionActivity nestedQuizCompletionActivity = new QuizCompletionActivity(system);
									nestedQuizCompletionActivity.setAssignmentComplete(true);
									for(Question ques : nestedQuiz.getQuestions()){
										nestedQuizCompletionActivity.addQuestionPerf(ques.getId(), /*pointsEarned*/ 0f);
									}
									
									//Assessment_Performance messages for nestedQuiz
									Message assessPerfMessage = new AssessmentPerformanceMessage(student, QuestionPresentationFormat.RADIO_BUTTON);
									assessPerfMessage.setProperty("Quiz", nestedQuiz);
									assessPerfMessage.setProperty("QuizCompletionActivity", nestedQuizCompletionActivity);
									assessPerfMessage.setProperty("CourseSection", courseSection);
									assessPerfMessage.setProperty("Assignment", assignment);
									messages.add(assessPerfMessage);
								}
								
								for (Question question : nestedQuiz.getQuestions()) {
									//Assessment_Item_Completion messages for nestedQuiz
									if (!question.studentCompletedQuestion(student)) {
										Message assessItemCompletionMessage = new AssessmentItemCompletionMessage();
										assessItemCompletionMessage.setProperty("CourseSection", courseSection);
										assessItemCompletionMessage.setProperty("Quiz", nestedQuiz);
										assessItemCompletionMessage.setProperty("Question", question);
										assessItemCompletionMessage.setProperty("Person", student);
										assessItemCompletionMessage.setProperty("CompletionOriginator", CompletionActivityOriginCode.SYSTEM.value);
										messages.add(assessItemCompletionMessage);
									}
								}
							}
						}
					}
				}
				
				//iterate through chapter sections
				if (chapter.getChapterSections() != null){
					for (ChapterSection chapSection : chapter.getChapterSections()) {
						Quiz chapterSectionQuiz = chapSection.getChapterSectionQuiz();
						if (chapterSectionQuiz != null) {
							for (User student : courseSection.getEnrolledStudents()) {
								if (!chapterSectionQuiz.studentCompletedQuiz(student)) {
									system.setPersonId(student.getPersonId());
									QuizCompletionActivity chapSectionQuizCompletionActivity = new QuizCompletionActivity(system);
									chapSectionQuizCompletionActivity.setAssignmentComplete(true);
									for(Question ques : chapterSectionQuiz.getQuestions()){
										chapSectionQuizCompletionActivity.addQuestionPerf(ques.getId(), /*pointsEarned*/ 0f);
									}
									
									//Assessment_Performance messages for chapterSectionQuiz
									Message assessPerfMessage = new AssessmentPerformanceMessage(student, QuestionPresentationFormat.RADIO_BUTTON);
									assessPerfMessage.setProperty("Quiz", chapterSectionQuiz);
									assessPerfMessage.setProperty("QuizCompletionActivity", chapSectionQuizCompletionActivity);
									assessPerfMessage.setProperty("CourseSection", courseSection);
									assessPerfMessage.setProperty("Assignment", assignment);
									messages.add(assessPerfMessage);
								}
								
								for (Question question : chapterSectionQuiz.getQuestions()) {
									//Assessment_Item_Completion messages for chapterSectionQuiz
									if (!question.studentCompletedQuestion(student)) {
										Message assessItemCompletionMessage = new AssessmentItemCompletionMessage();
										assessItemCompletionMessage.setProperty("CourseSection", courseSection);
										assessItemCompletionMessage.setProperty("Quiz", chapterSectionQuiz);
										assessItemCompletionMessage.setProperty("Question", question);
										assessItemCompletionMessage.setProperty("Person", student);
										assessItemCompletionMessage.setProperty("CompletionOriginator", CompletionActivityOriginCode.SYSTEM.value);
										messages.add(assessItemCompletionMessage);
									}
								}
							}
							
							//Chapter Section Quiz cannot have nested assessments at this time
						}
					
						if (chapSection.getPages() != null) {
							for (Page page : chapSection.getPages()) {
								if (page.getEmbeddedQuestions() != null) {
									for (Question embeddedQuestion : page.getEmbeddedQuestions()) {
										Quiz wrappingQuiz = new BasicQuiz();
										wrappingQuiz.setId(embeddedQuestion.getAssessmentId());
										for (User student : courseSection.getEnrolledStudents()) {
											//Assessment_Item_Completion messages for embeddedQuestion
											if (!embeddedQuestion.studentCompletedQuestion(student)) {
												Message assessItemCompletionMessage = new AssessmentItemCompletionMessage();
												assessItemCompletionMessage.setProperty("CourseSection", courseSection);
												assessItemCompletionMessage.setProperty("Quiz", wrappingQuiz);
												assessItemCompletionMessage.setProperty("Question", embeddedQuestion);
												assessItemCompletionMessage.setProperty("Person", student);
												assessItemCompletionMessage.setProperty("CompletionOriginator", CompletionActivityOriginCode.SYSTEM.value);
												messages.add(assessItemCompletionMessage);
											}
										}
									}
								}
							}	
							
							//NOTE: not checking for completion at quiz level to send Assessment_Performance messages
							//for embedded questions at this time - enhance in future?
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