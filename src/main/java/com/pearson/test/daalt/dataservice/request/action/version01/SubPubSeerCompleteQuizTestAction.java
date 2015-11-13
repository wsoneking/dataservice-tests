package com.pearson.test.daalt.dataservice.request.action.version01;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.request.message.version01.Message;
import com.pearson.test.daalt.dataservice.request.message.version01.RevelAssessmentStudentPerformanceMessage;

public class SubPubSeerCompleteQuizTestAction extends CompleteQuizTestAction {

	public SubPubSeerCompleteQuizTestAction(CourseSection courseSection, 
			Assignment assignment, Quiz quiz, QuizCompletionActivity quizCompletionActivity){
		messages = new ArrayList<>();
		this.quizCompletionActivity = quizCompletionActivity;
		this.courseSection = courseSection;
		this.assignment = assignment;
		this.quiz = quiz;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		//revel.assessment.student.performance.create
		Message message = new RevelAssessmentStudentPerformanceMessage();
		message.setProperty("QuizCompletionActivity", quizCompletionActivity);
		message.setProperty("CourseSection", courseSection);
		message.setProperty("Assignment", assignment);
		message.setProperty("Quiz", quiz);
		
		messages.add(message);
		
		for (Message msg : messages) {
			msg.send(seerCount, subPubCount);
		}
	}

}
