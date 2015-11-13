package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CompletionActivityOriginCode;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemCompletionMessage;
import com.pearson.test.daalt.dataservice.request.message.AssessmentPerformanceMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningModulePerformanceMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.TOTUserUnloadsQuestionTincanMessage;

public class SubPubSeerWritingSpaceCompleteQuizTestAction extends WritingSpaceCompleteQuizTestAction{

	public SubPubSeerWritingSpaceCompleteQuizTestAction(CourseSection courseSection, 
			Assignment assignment, Question question, QuestionCompletionActivity questionCompletionActivity, 
			Quiz quiz, QuizCompletionActivity quizCompletionActivity){
		messages = new ArrayList<>();
		this.courseSection = courseSection;
		this.assignment = assignment;
		this.question = question;
		this.questionCompletionActivity = questionCompletionActivity;
		this.quiz = quiz;
		this.quizCompletionActivity = quizCompletionActivity;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing WritingSpaceCompleteQuizTestAction...\n");
		
		User student = quizCompletionActivity.getPerson();
		
		Message messageTOT = new TOTUserUnloadsQuestionTincanMessage(/*isAttempt*/ false);
		messageTOT.setProperty("CourseSection", courseSection);
		messageTOT.setProperty("Quiz", quiz);
		messageTOT.setProperty("Question", question);
		messageTOT.setProperty("QuestionCompletionActivity", questionCompletionActivity);
		messages.add(messageTOT);
		
		//Assessment_Item_Completion message
		Message assessmentItemCompletionMessage = new AssessmentItemCompletionMessage();
		assessmentItemCompletionMessage.setProperty("CourseSection", courseSection);
		assessmentItemCompletionMessage.setProperty("Quiz", quiz);
		assessmentItemCompletionMessage.setProperty("Question", question);
		assessmentItemCompletionMessage.setProperty("Person", student);
		assessmentItemCompletionMessage.setProperty("CompletionOriginator", CompletionActivityOriginCode.STUDENT.value);
		messages.add(assessmentItemCompletionMessage);
		
		//revel.assessment.student.performance.create
		quizCompletionActivity.setAssignmentComplete(assignment.studentCompletedAssignment(student));
		Message message = new AssessmentPerformanceMessage(student, question.getQuestionPresentationFormatAsEnum());
		message.setProperty("Quiz", quiz);
		message.setProperty("QuizCompletionActivity", quizCompletionActivity);
		message.setProperty("CourseSection", courseSection);
		message.setProperty("Assignment", assignment);
		messages.add(message);
		
//		if(assignment.studentCompletedAssignment(student)) {
//			Message lmPerfMessage = new LearningModulePerformanceMessage(student);
//			lmPerfMessage.setProperty("Transaction_Type_Code", "Create");
//			lmPerfMessage.setProperty("CompletionOriginator", CompletionActivityOriginCode.STUDENT.value);
//			lmPerfMessage.setProperty("CourseSection", courseSection);
//			lmPerfMessage.setProperty("Assignment", assignment);
//			messages.add(lmPerfMessage);
//		}
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
		}
	}

}
