package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CompletionActivityOriginCode;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionPresentationFormat;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemCompletionMessage;
import com.pearson.test.daalt.dataservice.request.message.AssessmentPerformanceMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningModulePerformanceMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;

public class SubPubSeerCompleteQuizTestAction extends CompleteQuizTestAction {

	public SubPubSeerCompleteQuizTestAction(CourseSection courseSection, 
			Assignment assignment, Quiz quiz, QuizCompletionActivity quizCompletionActivity){
		messages = new ArrayList<>();
		this.courseSection = courseSection;
		this.assignment = assignment;
		this.quiz = quiz;
		this.quizCompletionActivity = quizCompletionActivity;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing CompleteQuizTestAction...\n");
		
		User student = quizCompletionActivity.getPerson();
		quizCompletionActivity.setAssignmentComplete(assignment.studentCompletedAssignment(student));
		
		//revel.assessment.student.performance.create
		Message message = new AssessmentPerformanceMessage(student, QuestionPresentationFormat.RADIO_BUTTON);
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
