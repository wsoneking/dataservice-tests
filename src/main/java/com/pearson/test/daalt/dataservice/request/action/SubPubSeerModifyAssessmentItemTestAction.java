package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionType;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.request.message.AssessmentItemPossibleAnswers;
import com.pearson.test.daalt.dataservice.request.message.Message;

public class SubPubSeerModifyAssessmentItemTestAction extends ModifyAssessmentItemTestAction {

	public SubPubSeerModifyAssessmentItemTestAction(Quiz quiz) {
		messages = new ArrayList<>();
		this.quiz = quiz;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {
		checkCriticalObjects();
		
		System.out.println("Now executing ModifyAssessmentItemTestAction...\n");
		
		for (Question ques: quiz.getQuestions()) {
			//Assessment_Item_Possible_Answers message to publish the Question
			if (ques.getQuestionType().equals(QuestionType.MULTI_VALUE.value)) {
				Message assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers(QuestionType.MULTI_VALUE.value);
				assessmentItemPossibleAnswer.setProperty("Question", ques);
				messages.add(assessmentItemPossibleAnswer);
			} else if (ques.getQuestionType().equals(QuestionType.UNKNOWN_FORMAT.value)) {
				Message assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers(QuestionType.UNKNOWN_FORMAT.value);
				assessmentItemPossibleAnswer.setProperty("Question", ques);
				messages.add(assessmentItemPossibleAnswer);
			} else {
				Message assessmentItemPossibleAnswer = new AssessmentItemPossibleAnswers("General");
				assessmentItemPossibleAnswer.setProperty("Question", ques);
				messages.add(assessmentItemPossibleAnswer);
			}
		}
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
		}
	}
}

