package com.pearson.test.daalt.dataservice.request.message.version01;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.model.User;

public class RevelAssessmentStudentPerformanceMessage extends SubPubMessage {
	//revel.assessment.student.performance.create
	private User student;

	public RevelAssessmentStudentPerformanceMessage() {
		payload = new RevelAssessmentStudentPerformanceMsgPayload();
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException {
		RevelAssessmentStudentPerformanceMsgPayload specificPayload = (RevelAssessmentStudentPerformanceMsgPayload) payload;
		switch (propertyName) {
		case "CourseSection" :
			CourseSection courseSection = (CourseSection) propertyValue;
			specificPayload.Course_Section_Source_System_Record_Id = courseSection.getId();
			specificPayload.Course_Points_Possible = courseSection.getPointsPossible();
			specificPayload.Course_Raw_Score = courseSection.getPointsEarnedFinal(student);
			break;
		case "Assignment" :
			Assignment assignment = (Assignment) propertyValue;
			specificPayload.Assignment_Source_System_Record_Id = assignment.getId();
			specificPayload.Assignment_Points_Possible = assignment.getPointsPossible();
			specificPayload.Assignment_Raw_Score = assignment.getPointsEarnedFinal(student);
			break;
		case "Quiz" :
			Quiz quiz = (Quiz) propertyValue;
			specificPayload.Assessment_Source_System_Record_Id = quiz.getId();
			if (specificPayload.Assessment_Items == null) {
				specificPayload.Assessment_Items = new ArrayList<>();
			}
			for(Question question : quiz.getQuestions()){
				RevelAssessmentStudentPerformanceMsgPayload.AssessmentItem assessmentItem = getItemWithId(question.getId());
				if (assessmentItem == null) {
					assessmentItem = specificPayload.new AssessmentItem();
					assessmentItem.Assessment_Item_Source_System_Record_Id = question.getId();
					specificPayload.Assessment_Items.add(assessmentItem);
				}
				assessmentItem.Possible_Score = question.getPointsPossible();
				assessmentItem.Attempts = question.getAttemptsForUser(student).size();
			}
			break;
		case "QuizCompletionActivity" :
			QuizCompletionActivity activity = (QuizCompletionActivity) propertyValue;
			student = activity.getPerson();
			specificPayload.Student_Source_System_Record_Id = activity.getPersonId();
			specificPayload.Assessment_Raw_Score = activity.getPointsEarned();
			specificPayload.Points_Earned_Original_Score = activity.getPointsEarned();
			
			Map<String, Float> questionPerfMap = activity.getQuestionPerfs();
			Iterator<String> questionPerfIter = questionPerfMap.keySet().iterator();
			while (questionPerfIter.hasNext()) {
				String questionId = (String) questionPerfIter.next();
				RevelAssessmentStudentPerformanceMsgPayload.AssessmentItem assessmentItem = getItemWithId(questionId);
				if (assessmentItem == null) {
					assessmentItem = specificPayload.new AssessmentItem();
					assessmentItem.Assessment_Item_Source_System_Record_Id = questionId;
					specificPayload.Assessment_Items.add(assessmentItem);
				}
				assessmentItem.Correct_Indicator = (questionPerfMap.get(questionId) > 0);
				assessmentItem.Score = questionPerfMap.get(questionId);
			}
			break;
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : "
							+ propertyName);
		}

	}
	
	private RevelAssessmentStudentPerformanceMsgPayload.AssessmentItem getItemWithId(String itemId) {
		RevelAssessmentStudentPerformanceMsgPayload.AssessmentItem toReturn = null;
		RevelAssessmentStudentPerformanceMsgPayload specificPayload = (RevelAssessmentStudentPerformanceMsgPayload) payload;
		if (specificPayload.Assessment_Items != null) {
			for (RevelAssessmentStudentPerformanceMsgPayload.AssessmentItem item : specificPayload.Assessment_Items) {
				if (item.Assessment_Item_Source_System_Record_Id.compareTo(itemId) == 0) {
					toReturn = item;
					break;
				}
			}
		}
		return toReturn;
	}
	
	@Override
	public String getMissingCriticalPropertyName() {
		if (payload.Transaction_Type_Code == null) {
			return "MessagePayload.Transaction_Type_Code";
		}
		RevelAssessmentStudentPerformanceMsgPayload specificPayload = (RevelAssessmentStudentPerformanceMsgPayload) payload;
		if (specificPayload.Student_Source_System_Record_Id == null){
			return "RevelAssessmentStudentPerformanceMsgPayload.Student_Source_System_Record_Id";
		}
		if (specificPayload.Course_Section_Source_System_Record_Id == null){
			return "RevelAssessmentStudentPerformanceMsgPayload.Course_Section_Source_System_Record_Id";
		}
		if (specificPayload.Course_Section_Source_System_Record_Id == null){
			return "RevelAssessmentStudentPerformanceMsgPayload.Course_Section_Source_System_Record_Id";
		}
		if (specificPayload.Course_Points_Possible == null){
			return "RevelAssessmentStudentPerformanceMsgPayload.Course_Points_Possible";
		}
		if (specificPayload.Course_Raw_Score == null){
			return "RevelAssessmentStudentPerformanceMsgPayload.Course_Raw_Score";
		}
		if (specificPayload.Assignment_Source_System_Record_Id == null){
			return "RevelAssessmentStudentPerformanceMsgPayload.Assignment_Source_System_Record_Id";
		}
		if (specificPayload.Assignment_Points_Possible == null){
			return "RevelAssessmentStudentPerformanceMsgPayload.Assignment_Points_Possible";
		}
		if (specificPayload.Assignment_Raw_Score == null){
			return "RevelAssessmentStudentPerformanceMsgPayload.Assignment_Raw_Score";
		}
		if (specificPayload.Assessment_Source_System_Record_Id == null){
			return "RevelAssessmentStudentPerformanceMsgPayload.Assessment_Source_System_Record_Id";
		}
		if (specificPayload.Assessment_Raw_Score == null){
			return "RevelAssessmentStudentPerformanceMsgPayload.Assessment_Raw_Score";
		}
		if (specificPayload.Points_Earned_Original_Score == null){
			return "RevelAssessmentStudentPerformanceMsgPayload.Points_Earned_Original_Score";
		}
		if (specificPayload.Assessment_Items == null){
			return "RevelAssessmentStudentPerformanceMsgPayload.Assessment_Items";
		}
		for (RevelAssessmentStudentPerformanceMsgPayload.AssessmentItem item : specificPayload.Assessment_Items) {
			if (item.Assessment_Item_Source_System_Code == null){
				return "RevelAssessmentStudentPerformanceMsgPayload.Assessment_Item.Assessment_Item_Source_System_Code";
			}
			if (item.Assessment_Item_Source_System_Record_Id == null){
				return "RevelAssessmentStudentPerformanceMsgPayload.Assessment_Item.Assessment_Item_Source_System_Record_Id";
			}
			if (item.Possible_Score == null){
				return "RevelAssessmentStudentPerformanceMsgPayload.Assessment_Item.Possible_Score";
			}
			if (item.Score == null){
				return "RevelAssessmentStudentPerformanceMsgPayload.Assessment_Item.Score";
			}
		}
		return null;
	}
	
	public class RevelAssessmentStudentPerformanceMsgPayload extends MessagePayload {
		//required : hard code
		public String Message_Type_Code = "Assessment_Performance";
		//required : hard code
		public String Message_Version = "1.0";
		
		//optional : caller sets value
		public List<AssessmentItem> Assessment_Items = new ArrayList<AssessmentItem>();
		

		public class AssessmentItem{
			
			//required : hard-code
			public String Assessment_Item_Source_System_Code = "PAF";
			//required : caller sets value - comes from Quiz
			public String Assessment_Item_Source_System_Record_Id;
			//required : caller sets value - comes from QuizCompletionActivity
			public boolean Correct_Indicator;		// true/false
			//required : caller sets value - comes from Quiz --> Question
			public int Attempts;
			//optional : caller sets value - comes from Quiz --> Question
			public Float Possible_Score;
			//optional : caller sets value - comes from QuizCompletionActivity
			public Float Score;
			
			//required : caller sets value	This is added for Revel 2.1.
//			public String Pass_Fail_Indicator = "Pass";		// NotApplicable, Pass, Fail
		}
		
		//required : caller sets value - comes from QuizCompletionActivity
		public Float Assessment_Raw_Score;
		//required : hard code
		public String Assessment_Source_System_Code = "PAF";
		//required : caller sets value - comes from Quiz
		public String Assessment_Source_System_Record_Id;
		//required :hard code
		public String Assessment_Type_Code = "Quiz";
		//required : caller sets value - comes from Assignment
		public Float Assignment_Points_Possible;
		//required : caller sets value - comes from Assignment
		public Float Assignment_Raw_Score;
		//required : hard code
		public String Assignment_Source_System_Code = "Revel";
		//required : caller sets value - comes from Assignment
		public String Assignment_Source_System_Record_Id;
		//required : caller sets value - comes from CourseSection
		public Float Course_Points_Possible;
		//required : caller sets value - comes from CourseSection
		public Float Course_Raw_Score;
		//required : hard code
		public String Course_Section_Source_System_Code = "Registrar";
		//required : caller sets value - comes from CourseSection
		public String Course_Section_Source_System_Record_Id;
		//required : caller sets value - comes from QuizCompletionActivity
		public Float Points_Earned_Original_Score;
		//required : hard code
		public String Ref_Assessment_Completion_Source_Code = "Student";
		//required : hard code
		public String Student_Source_System_Code = "PI";
		//required : caller sets value - comes from QuizCompletionActivity
		public String Student_Source_System_Record_Id;
		
	/*	
		//optional : hard code
		public String Instructor_Source_System_Code = "QA_PI";
		//optional : caller sets value
		public String Instructor_Source_System_Record_Id;
		
	
		//optional : caller sets value
		public Float Points_Earned_After_Instructor_Update;
		
		//optional : always null
		public String Score_Overridden_Flag;
		//optional : always null
		public String Score_Overridden_On_Datetime;
		//optional : always null
		public String Score_Update_Reason_Text;
*/		
	}
}
