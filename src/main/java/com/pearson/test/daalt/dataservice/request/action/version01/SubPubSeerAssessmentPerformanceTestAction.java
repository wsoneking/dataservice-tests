package com.pearson.test.daalt.dataservice.request.action.version01;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.request.message.version01.RevelAssessmentStudentPerformanceMessage;
import com.pearson.test.daalt.dataservice.request.message.version01.Message;

public class SubPubSeerAssessmentPerformanceTestAction extends AssessmentPerformanceTestAction {

	public SubPubSeerAssessmentPerformanceTestAction(CourseSection courseSection, 
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
		
		//revel.assessment.student.performance.create
		Message message = new RevelAssessmentStudentPerformanceMessage();
		message.setProperty("QuizCompletionActivity", quizCompletionActivity);
		message.setProperty("Quiz", quiz);
		message.setProperty("CourseSection", courseSection);
		message.setProperty("Assignment", assignment);
		messages.add(message);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
		}
	}

}
