package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CompletionActivityOriginCode;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.QuestionPresentationFormat;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.request.message.AssessmentPerformanceMessage;
import com.pearson.test.daalt.dataservice.request.message.LearningModulePerformanceMessage;
import com.pearson.test.daalt.dataservice.request.message.Message;

public class SubPubSeerInstructorAdjustsStudentGradeTestAction extends InstructorAdjustsStudentGradeTestAction {

	public SubPubSeerInstructorAdjustsStudentGradeTestAction(CourseSection courseSection, 
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
		
		System.out.println("Now executing InstructorAdjustsStudentGradeTestAction...\n");
		
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
		
//		Message lmPerfMessage = new LearningModulePerformanceMessage(quizCompletionActivity.getPerson());
//		lmPerfMessage.setProperty("Transaction_Type_Code", "Update");
//		//NOTE: defaulting to "Student" completion - if we add a new test case 
//		//where the system completes for the student and then the instructor updates the student's grade, this will need to be changed
//		lmPerfMessage.setProperty("CompletionOriginator", CompletionActivityOriginCode.STUDENT.value); 
//		lmPerfMessage.setProperty("CourseSection", courseSection);
//		lmPerfMessage.setProperty("Assignment", assignment);
//		messages.add(lmPerfMessage);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
		}
	}

}
