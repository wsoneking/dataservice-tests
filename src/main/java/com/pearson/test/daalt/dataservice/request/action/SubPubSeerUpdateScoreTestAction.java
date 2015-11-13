package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CompletionActivityOriginCode;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionPresentationFormat;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemCompletionMessage;
import com.pearson.test.daalt.dataservice.request.message.AssessmentPerformanceMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;

public class SubPubSeerUpdateScoreTestAction extends UpdateScoreTestAction{

	public SubPubSeerUpdateScoreTestAction(CourseSection courseSection, 
			Assignment assignment, Quiz quiz,
			QuizCompletionActivity quizCompletionActivity){
		messages = new ArrayList<>();
		this.courseSection = courseSection;
		this.assignment = assignment;
		this.quiz = quiz;
		this.quizCompletionActivity = quizCompletionActivity;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing UpdateScoreTestAction...\n");
		
		//NOTE: assumes "Student" completion - if we add a new test case 
		//where the system completes for the student and then the instructor updates the student's grade, this will need to be changed
		quizCompletionActivity.setAssignmentComplete(true);
		
		//revel.assessment.student.performance.create
		Message message = new AssessmentPerformanceMessage(quizCompletionActivity.getPerson(), QuestionPresentationFormat.RADIO_BUTTON);
		message.setProperty("Quiz", quiz);
		message.setProperty("QuizCompletionActivity", quizCompletionActivity);
		message.setProperty("CourseSection", courseSection);
		message.setProperty("Assignment", assignment);
		message.setProperty("Overriden", true);
		messages.add(message);
		
		for(Question question : quiz.getQuestions()){
			if(!question.studentUsedFinalAttempt(quizCompletionActivity.getPerson())){
				Message assessmentItemCompletionMessage = new AssessmentItemCompletionMessage();
				assessmentItemCompletionMessage.setProperty("CourseSection", courseSection);
				assessmentItemCompletionMessage.setProperty("Quiz", quiz);
				assessmentItemCompletionMessage.setProperty("Question", question);
				assessmentItemCompletionMessage.setProperty("Person", quizCompletionActivity.getPerson());
				assessmentItemCompletionMessage.setProperty("CompletionOriginator", CompletionActivityOriginCode.STUDENT.value);
				messages.add(assessmentItemCompletionMessage);
			}
		}
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
			Thread.sleep(500);
		}
	}

}

