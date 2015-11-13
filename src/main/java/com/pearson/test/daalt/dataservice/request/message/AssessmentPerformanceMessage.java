package com.pearson.test.daalt.dataservice.request.message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.JournalWritingPassFailCode;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionPresentationFormat;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.model.User;

public class AssessmentPerformanceMessage extends SubPubMessage {
	//revel.assessment.student.performance.create
	private User student;
	private QuestionPresentationFormat questionPresentationFormat;

	public AssessmentPerformanceMessage(User student, QuestionPresentationFormat questionPresentationFormat) {
		this.student = student;
		this.questionPresentationFormat = questionPresentationFormat;
		switch (questionPresentationFormat) {
			case UNKNOWN_FORMAT :
				payload = new AssessmentPerformanceMsgMultiValuePayload();		// Unknown format question's assessment performance message are same with multivalue.
				break;
			case BINNING :
				payload = new AssessmentPerformanceMsgMultiValuePayload();
				break;
			case CATEGORIZING :
				payload = new AssessmentPerformanceMsgMultiValuePayload();
				break;
			case HOT_SPOT :
				payload = new AssessmentPerformanceMsgMultiValuePayload();
				break;
			case MULTI_SELECT :
				payload = new AssessmentPerformanceMsgMultiValuePayload();
				break;
			case MULTIPLE_HOT_SPOT :
				payload = new AssessmentPerformanceMsgMultiValuePayload();
				break;
			case RADIO_BUTTON :
				payload = new AssessmentPerformanceMsgMultiValuePayload();
				break;
			case JOURNAL:
				payload = new AssessmentPerformanceMsgJournalPayload();
				break;
			case SHARED_WRITING:
				payload = new AssessmentPerformanceMsgSharedWritingPayload();
				break;
			case WRITING_SPACE :
				payload = new AssessmentPerformanceMsgWritingSpacePayload();
				break;
			case FILL_IN_THE_BLANK :
				payload = new AssessmentPerformanceMsgMultiValuePayload();
				break;
			case NUMERIC :
				payload = new AssessmentPerformanceMsgMultiValuePayload();
				break;
		}
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException, InvalidStateException {
		AssessmentPerformanceMsgPayload specificPayload = (AssessmentPerformanceMsgPayload) payload;
		AssessmentPerformanceMsgMultiValuePayload multiValuePayload = null;
		AssessmentPerformanceMsgJournalPayload journalPayload = null;
		AssessmentPerformanceMsgSharedWritingPayload sharedWritingPayload = null;
		AssessmentPerformanceMsgWritingSpacePayload writingSpacePayload = null;
		switch (questionPresentationFormat) {
			case UNKNOWN_FORMAT:
			case BINNING :
				multiValuePayload = (AssessmentPerformanceMsgMultiValuePayload) payload;
				break;
			case CATEGORIZING :
				multiValuePayload = (AssessmentPerformanceMsgMultiValuePayload) payload;
				break;
			case HOT_SPOT :
				multiValuePayload = (AssessmentPerformanceMsgMultiValuePayload) payload;
				break;
			case MULTI_SELECT :
				multiValuePayload = (AssessmentPerformanceMsgMultiValuePayload) payload;
				break;
			case MULTIPLE_HOT_SPOT :
				multiValuePayload = (AssessmentPerformanceMsgMultiValuePayload) payload;
				break;
			case RADIO_BUTTON :
				multiValuePayload = (AssessmentPerformanceMsgMultiValuePayload) payload;
				break;
			case JOURNAL:
				journalPayload = (AssessmentPerformanceMsgJournalPayload) payload;
				break;
			case SHARED_WRITING :
				sharedWritingPayload = (AssessmentPerformanceMsgSharedWritingPayload) payload;
				break;
			case WRITING_SPACE :
				writingSpacePayload = (AssessmentPerformanceMsgWritingSpacePayload) payload;
				break;
			case FILL_IN_THE_BLANK :
				multiValuePayload = (AssessmentPerformanceMsgMultiValuePayload) payload;
				break;
			case NUMERIC :
				multiValuePayload = (AssessmentPerformanceMsgMultiValuePayload) payload;
				break;
		}

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
			specificPayload.Assessment_Possible_Points = quiz.getPointsPossible();
//			specificPayload.Student_Late_Submission_Points = quiz.getStudentLateSubmissionPoints(student);
			
			for(Question question : quiz.getQuestions()){
				if (payload instanceof AssessmentPerformanceMsgMultiValuePayload){
					AssessmentPerformanceMsgMultiValuePayload.AssessmentItem assessmentItem = getItemWithIdMultiValue(question.getId());
					if (assessmentItem == null) {
						assessmentItem = multiValuePayload.new AssessmentItem();
						assessmentItem.Assessment_Item_Source_System_Record_Id = question.getId();
						assessmentItem.Assessment_Item_Question_Type = question.getQuestionType();
						assessmentItem.Assessment_Item_Question_Presentation_Format = question.getQuestionPresentationFormat();
						assessmentItem.Possible_Score = question.getPointsPossible();
						assessmentItem.Score = question.getPointsEarnedFinal(student);
						assessmentItem.Attempts = question.getAttemptsForUser(student).size();
						multiValuePayload.Assessment_Items.add(assessmentItem);
					}
				} else if (payload instanceof AssessmentPerformanceMsgJournalPayload){		
					AssessmentPerformanceMsgJournalPayload.AssessmentItem assessmentItem = getItemWithIdJournal(question.getId());
					if (assessmentItem == null) {
						assessmentItem = journalPayload.new AssessmentItem();
						assessmentItem.Assessment_Item_Source_System_Record_Id = question.getId();
						assessmentItem.Assessment_Item_Question_Type = question.getQuestionType();
						assessmentItem.Assessment_Item_Question_Presentation_Format = question.getQuestionPresentationFormat();
						journalPayload.Assessment_Items.add(assessmentItem);
					}
				} else if (payload instanceof AssessmentPerformanceMsgWritingSpacePayload){		
					AssessmentPerformanceMsgWritingSpacePayload.AssessmentItem assessmentItem = getItemWithIdWritingSpace(question.getId());
					if (assessmentItem == null) {
						assessmentItem = writingSpacePayload.new AssessmentItem();
						assessmentItem.Assessment_Item_Source_System_Record_Id = question.getId();
						assessmentItem.Assessment_Item_Question_Type = question.getQuestionType();
						assessmentItem.Assessment_Item_Question_Presentation_Format = question.getQuestionPresentationFormat();
						assessmentItem.Possible_Score = question.getPointsPossible();
						assessmentItem.Score = question.getPointsEarnedFinal(student);
						writingSpacePayload.Assessment_Items.add(assessmentItem);
					}
				} else if (payload instanceof AssessmentPerformanceMsgSharedWritingPayload){		
					AssessmentPerformanceMsgSharedWritingPayload.AssessmentItem assessmentItem = getItemWithIdSharedWriting(question.getId());
					if (assessmentItem == null) {
						assessmentItem = sharedWritingPayload.new AssessmentItem();
						assessmentItem.Assessment_Item_Source_System_Record_Id = question.getId();
						assessmentItem.Assessment_Item_Question_Type = question.getQuestionType();
						assessmentItem.Assessment_Item_Question_Presentation_Format = question.getQuestionPresentationFormat();
						assessmentItem.Possible_Score = question.getPointsPossible();
						assessmentItem.Score = question.getPointsEarnedFinal(student);
						sharedWritingPayload.Assessment_Items.add(assessmentItem);
					}
				}
				
			}
			break;
		case "QuizCompletionActivity" :
			if (specificPayload.Assessment_Source_System_Record_Id == null) {
				throw new InvalidStateException("Must set Quiz property before setting QuizCompletionActivity property");
			}
			
			QuizCompletionActivity activity = (QuizCompletionActivity) propertyValue;
			specificPayload.Student_Source_System_Record_Id = activity.getPersonId();
			specificPayload.Assessment_Raw_Score = activity.getPointsEarned();
			specificPayload.Points_Earned_Original_Score = activity.getPointsEarned();
			specificPayload.Assignment_Completed = activity.isAssignmentComplete();
			specificPayload.Ref_Assessment_Completion_Source_Code = activity.getPersonRole(); 
			
			Map<String, Float> questionPerfMap = activity.getQuestionPerfs();
			Iterator<String> questionPerfIter = questionPerfMap.keySet().iterator();
			while (questionPerfIter.hasNext()) {
				String questionId = (String) questionPerfIter.next();
				if (payload instanceof AssessmentPerformanceMsgMultiValuePayload){
					AssessmentPerformanceMsgMultiValuePayload.AssessmentItem assessmentItem = getItemWithIdMultiValue(questionId);
					assessmentItem.Correct_Indicator = (questionPerfMap.get(questionId) > 0);
				} else if (payload instanceof AssessmentPerformanceMsgJournalPayload){		
					AssessmentPerformanceMsgJournalPayload.AssessmentItem assessmentItem = getItemWithIdJournal(questionId);
					assessmentItem.Correct_Indicator = (questionPerfMap.get(questionId) > 0);
					if(questionPerfMap.get(questionId) > 0){
						assessmentItem.Pass_Fail_Indicator = JournalWritingPassFailCode.PASS.value;
					} else{
						assessmentItem.Pass_Fail_Indicator = JournalWritingPassFailCode.FAIL.value;
					}
				} else if (payload instanceof AssessmentPerformanceMsgWritingSpacePayload){		
					AssessmentPerformanceMsgWritingSpacePayload.AssessmentItem assessmentItem = getItemWithIdWritingSpace(questionId);
					assessmentItem.Correct_Indicator = (questionPerfMap.get(questionId) > 0);
				} else if (payload instanceof AssessmentPerformanceMsgSharedWritingPayload){		
					AssessmentPerformanceMsgSharedWritingPayload.AssessmentItem assessmentItem = getItemWithIdSharedWriting(questionId);
					assessmentItem.Correct_Indicator = (questionPerfMap.get(questionId) > 0);
				}
				
			}
			break;
	
		
		case "Overriden" :
			boolean over_riden = (boolean) propertyValue;
			specificPayload.Score_Overridden_Flag = over_riden;
			break;
			
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : "
							+ propertyName);
		}

	}
	
	
	private AssessmentPerformanceMsgMultiValuePayload.AssessmentItem getItemWithIdMultiValue(String itemId) {
		AssessmentPerformanceMsgMultiValuePayload.AssessmentItem toReturn = null;
		AssessmentPerformanceMsgMultiValuePayload specificPayload = (AssessmentPerformanceMsgMultiValuePayload) payload;
		if (specificPayload.Assessment_Items != null) {
			for (AssessmentPerformanceMsgMultiValuePayload.AssessmentItem item : specificPayload.Assessment_Items) {
				if (item.Assessment_Item_Source_System_Record_Id.compareTo(itemId) == 0) {
					toReturn = item;
					break;
				}
			}
		}
		return toReturn;
	}
	private AssessmentPerformanceMsgJournalPayload.AssessmentItem getItemWithIdJournal(String itemId) {
		AssessmentPerformanceMsgJournalPayload.AssessmentItem toReturn = null;
		AssessmentPerformanceMsgJournalPayload specificPayload = (AssessmentPerformanceMsgJournalPayload) payload;
		if (specificPayload.Assessment_Items != null) {
			for (AssessmentPerformanceMsgJournalPayload.AssessmentItem item : specificPayload.Assessment_Items) {
				if (item.Assessment_Item_Source_System_Record_Id.compareTo(itemId) == 0) {
					toReturn = item;
					break;
				}
			}
		}
		return toReturn;
	}
	private AssessmentPerformanceMsgWritingSpacePayload.AssessmentItem getItemWithIdWritingSpace(String itemId) {
		AssessmentPerformanceMsgWritingSpacePayload.AssessmentItem toReturn = null;
		AssessmentPerformanceMsgWritingSpacePayload specificPayload = (AssessmentPerformanceMsgWritingSpacePayload) payload;
		if (specificPayload.Assessment_Items != null) {
			for (AssessmentPerformanceMsgWritingSpacePayload.AssessmentItem item : specificPayload.Assessment_Items) {
				if (item.Assessment_Item_Source_System_Record_Id.compareTo(itemId) == 0) {
					toReturn = item;
					break;
				}
			}
		}
		return toReturn;
	}
	private AssessmentPerformanceMsgSharedWritingPayload.AssessmentItem getItemWithIdSharedWriting(String itemId) {
		AssessmentPerformanceMsgSharedWritingPayload.AssessmentItem toReturn = null;
		AssessmentPerformanceMsgSharedWritingPayload specificPayload = (AssessmentPerformanceMsgSharedWritingPayload) payload;
		if (specificPayload.Assessment_Items != null) {
			for (AssessmentPerformanceMsgSharedWritingPayload.AssessmentItem item : specificPayload.Assessment_Items) {
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
		AssessmentPerformanceMsgPayload specificPayload = (AssessmentPerformanceMsgPayload) payload;
		if (specificPayload.Message_Type_Code == null){
			return "AssessmentPerformanceMsgPayload.Message_Type_Code";
		}
		if (specificPayload.Message_Version == null){
			return "AssessmentPerformanceMsgPayload.Message_Version";
		}
		if (specificPayload.Originating_System_Code == null){
			return "AssessmentPerformanceMsgPayload.Originating_System_Code";
		}
		if (specificPayload.Student_Source_System_Code == null){
			return "AssessmentPerformanceMsgPayload.Student_Source_System_Code";
		}
		if (specificPayload.Student_Source_System_Record_Id == null){
			return "AssessmentPerformanceMsgPayload.Student_Source_System_Record_Id";
		}
		if (specificPayload.Course_Section_Source_System_Code == null){
			return "AssessmentPerformanceMsgPayload.Course_Section_Source_System_Code";
		}
		if (specificPayload.Course_Section_Source_System_Record_Id == null){
			return "AssessmentPerformanceMsgPayload.Course_Section_Source_System_Record_Id";
		}
		if (specificPayload.Assignment_Source_System_Code == null){
			return "AssessmentPerformanceMsgPayload.Assignment_Source_System_Code";
		}
		if (specificPayload.Course_Points_Possible == null){
			return "AssessmentPerformanceMsgPayload.Course_Points_Possible";
		}
		if (specificPayload.Course_Raw_Score == null){
			return "AssessmentPerformanceMsgPayload.Course_Raw_Score";
		}
		if (specificPayload.Assignment_Source_System_Record_Id == null){
			return "AssessmentPerformanceMsgPayload.Assignment_Source_System_Record_Id";
		}
		if (specificPayload.Assignment_Points_Possible == null){
			return "AssessmentPerformanceMsgPayload.Assignment_Points_Possible";
		}
		if (specificPayload.Assessment_Possible_Points == null){
			return "AssessmentPerformanceMsgPayload.Assessment_Possible_Points";
		}
		if (specificPayload.Assignment_Raw_Score == null){
			return "AssessmentPerformanceMsgPayload.Assignment_Raw_Score";
		}
		if (specificPayload.Assessment_Source_System_Record_Id == null){
			return "AssessmentPerformanceMsgPayload.Assessment_Source_System_Record_Id";
		}
		if (specificPayload.Assessment_Raw_Score == null){
			return "AssessmentPerformanceMsgPayload.Assessment_Raw_Score";
		}
		if (specificPayload.Points_Earned_Original_Score == null){
			return "AssessmentPerformanceMsgPayload.Points_Earned_Original_Score";
		}
		if (specificPayload.Ref_Assessment_Completion_Source_Code == null){
			return "AssessmentPerformanceMsgPayload.Ref_Assessment_Completion_Source_Code";
		}
		if (specificPayload.Assessment_Source_System_Code == null){
			return "AssessmentPerformanceMsgPayload.Assessment_Source_System_Code";
		}
		if (specificPayload.Assessment_Type_Code == null){
			return "AssessmentPerformanceMsgPayload.Assessment_Type_Code";
		}
		if (specificPayload.Assessment_Items == null){
			return "AssessmentPerformanceMsgPayload.Assessment_Items";
		}
		
		for (AssessmentPerformanceMsgPayload.AssessmentItem item : specificPayload.Assessment_Items) {
			if (item.Assessment_Item_Source_System_Code == null){
				return "AssessmentPerformanceMsgPayload.Assessment_Item.Assessment_Item_Source_System_Code";
			}
			if (item.Assessment_Item_Source_System_Record_Id == null){
				return "AssessmentPerformanceMsgPayload.Assessment_Item.Assessment_Item_Source_System_Record_Id";
			}
			if (item.Assessment_Item_Question_Type == null){
				return "AssessmentPerformanceMsgPayload.Assessment_Item.Assessment_Item_Question_Type";
			}
		}
		
		if (payload instanceof AssessmentPerformanceMsgMultiValuePayload ) {
			AssessmentPerformanceMsgMultiValuePayload specificPayload1 = (AssessmentPerformanceMsgMultiValuePayload) payload;
			for (AssessmentPerformanceMsgMultiValuePayload.AssessmentItem item : specificPayload1.Assessment_Items) {
				if (item.Possible_Score == null){
					return "AssessmentPerformanceMsgPayload.Assessment_Item.Possible_Score";
				}
				if (item.Score == null){
					return "AssessmentPerformanceMsgPayload.Assessment_Item.Score";
				}
				if (item.Attempts == -1){
					return "AssessmentPerformanceMsgPayload.Assessment_Item.Attempts";
				}
			}
		} else if (payload instanceof AssessmentPerformanceMsgWritingSpacePayload ) {
			AssessmentPerformanceMsgWritingSpacePayload specificPayload2 = (AssessmentPerformanceMsgWritingSpacePayload) payload;
			for (AssessmentPerformanceMsgWritingSpacePayload.AssessmentItem item : specificPayload2.Assessment_Items) {
				if (item.Possible_Score == null){
					return "AssessmentPerformanceMsgPayload.Assessment_Item.Possible_Score";
				}
				if (item.Score == null){
					return "AssessmentPerformanceMsgPayload.Assessment_Item.Score";
				}
			}
		} else if (payload instanceof AssessmentPerformanceMsgJournalPayload ) {
			AssessmentPerformanceMsgJournalPayload specificPayload3 = (AssessmentPerformanceMsgJournalPayload) payload;
			for (AssessmentPerformanceMsgJournalPayload.AssessmentItem item : specificPayload3.Assessment_Items) {
				if (item.Pass_Fail_Indicator == null){
					return "AssessmentPerformanceMsgPayload.Assessment_Item.Pass_Fail_Indicator";
				}
			}
		} else if (payload instanceof AssessmentPerformanceMsgSharedWritingPayload ) {
			AssessmentPerformanceMsgSharedWritingPayload specificPayload4 = (AssessmentPerformanceMsgSharedWritingPayload) payload;
			for (AssessmentPerformanceMsgSharedWritingPayload.AssessmentItem item : specificPayload4.Assessment_Items) {
				if (item.Possible_Score == null){
					return "AssessmentPerformanceMsgPayload.Assessment_Item.Possible_Score";
				}
				if (item.Score == null){
					return "AssessmentPerformanceMsgPayload.Assessment_Item.Score";
				}
			}
		}
		return null;
	}
	
	public class AssessmentPerformanceMsgPayload extends MessagePayload {
		//required : hard code
		public String Message_Type_Code = "Assessment_Performance";
		//required : hard code
		public String Originating_System_Code = "DAALTTest";
		//required : hard code 
//		public String Message_Version = "2.1";
		public String Message_Version = "2.0";
		//required : hard code
		public String Student_Source_System_Code = "PI";
		//required : caller sets value - comes from QuizCompletionActivity
		public String Student_Source_System_Record_Id;
		//required : hard code
		public String Course_Section_Source_System_Code = "Registrar";
		//required : caller sets value - comes from CourseSection
		public String Course_Section_Source_System_Record_Id;
		//optional : caller sets value - comes from CourseSection
		public Float Course_Points_Possible;
		//optional : caller sets value - comes from CourseSection
		public Float Course_Raw_Score;
		//required : hard code
		public String Assignment_Source_System_Code = "Revel";
		//required : caller sets value - comes from Assignment
		public String Assignment_Source_System_Record_Id;
		//required : caller sets value - comes from Assignment
		public Float Assignment_Points_Possible;
		//required : caller sets value - comes from Assignment
		public Float Assignment_Raw_Score;
		//required : hard-code - replaced by Learning_Module_Performance in V2.0.9
		public boolean Assignment_Completed = false;
		//optional : generate new every time, value should be now
		public String Assignment_Completion_Datetime = new SimpleDateFormat(dateFormatString).format(new Date());
//		//optional : quiz.getStudentLateSubmissionPoints()
//		public Float Student_Late_Submission_Points;
		//required : hard code
		public String Ref_Assessment_Completion_Source_Code = "Student";
		//required : hard code
		public String Assessment_Source_System_Code = "PAF";
		//required : caller sets value - comes from Quiz
		public String Assessment_Source_System_Record_Id;
		//required :hard code
		public String Assessment_Type_Code = "Quiz";
		//required : caller sets value - comes from QuizCompletionActivity
		public Float Assessment_Raw_Score;
		//required : caller sets value - comes from QuizCompletionActivity
		public Float Points_Earned_Original_Score;
		

		//optional : default is true, caller sets value
		public boolean Include_Points_In_Student_Grade_Flag = true;
		
		//optional : caller sets value
		public Float Assessment_Possible_Points ;
		//optional : generate new every time, value should be now
		public String Assessment_Completion_Datetime = new SimpleDateFormat(dateFormatString).format(new Date());
		
		
		//optional : caller sets value
		public boolean Score_Overridden_Flag = false;

		//optional : generate new every time, value should be now

		
		//optional : not used by data service endpoints version 2.1
		/*public String Score_Overridden_On_Datetime;
		public String Score_Update_Reason_Text;
		public String Instructor_Source_System_Code = "PI";
		public String Instructor_Source_System_Record_Id;*/
		
//		//optional : not used by data service endpoints version 2.1
//		public Float Difficulty_Of_Assigned_Problems;
//		//optional : not used by data service endpoints version 2.1
//		public Float Difficulty_Of_Answered_Problems;

		
		//optional : caller sets value
		public List<AssessmentItem> Assessment_Items = new ArrayList<AssessmentItem>();
		public class AssessmentItem {
			
			//required : hard-code
			public String Assessment_Item_Source_System_Code = "PAF";
			//required : caller sets value - comes from Quiz
			public String Assessment_Item_Source_System_Record_Id;
			//required : hard code - introduce override to allow for new item types
			public String Assessment_Item_Question_Type;
			//optional : caller sets value - comes from QuizCompletionActivity
			public boolean Correct_Indicator;		// true/false
			//optional : hard code - introduce override to allow for new item types
			public String Assessment_Item_Question_Presentation_Format;

		}
		
//		//optional : not used by data service endpoints version 2.0
//		public TransformationHistory Transformation_History = new TransformationHistory();
//		public class TransformationHistory {
//			public String Transform_Datetime;
//			public String From_Version;
//			public String To_Version;
//		}
	}

	public class AssessmentPerformanceMsgMultiValuePayload extends AssessmentPerformanceMsgPayload {
		public List<AssessmentItem> Assessment_Items = new ArrayList<AssessmentItem>();
		public class AssessmentItem extends AssessmentPerformanceMsgPayload.AssessmentItem {
			//required : caller sets value - comes from Quiz --> Question
			public int Attempts = -1;
			//optional : caller sets value - comes from Quiz --> Question
			public Float Possible_Score;
			//optional : caller sets value - comes from QuizCompletionActivity
			public Float Score;
		}
		
	}
	
	public class AssessmentPerformanceMsgWritingSpacePayload extends AssessmentPerformanceMsgPayload {
		public List<AssessmentItem> Assessment_Items = new ArrayList<AssessmentItem>();
		public class AssessmentItem extends AssessmentPerformanceMsgPayload.AssessmentItem {
			//optional : caller sets value - comes from Quiz --> Question
			public Float Possible_Score;
			//optional : caller sets value - comes from QuizCompletionActivity
			public Float Score;
		}
	}
	
	public class AssessmentPerformanceMsgJournalPayload extends AssessmentPerformanceMsgPayload {
		public List<AssessmentItem> Assessment_Items = new ArrayList<AssessmentItem>();
		public class AssessmentItem extends AssessmentPerformanceMsgPayload.AssessmentItem {
			//required : caller sets value	This is added for Revel 2.1.
			public String Pass_Fail_Indicator = JournalWritingPassFailCode.FAIL.value;		// NotApplicable, Pass, Fail
		}
	}
	
	public class AssessmentPerformanceMsgSharedWritingPayload extends AssessmentPerformanceMsgPayload {
		public List<AssessmentItem> Assessment_Items = new ArrayList<AssessmentItem>();
		public class AssessmentItem extends AssessmentPerformanceMsgPayload.AssessmentItem {
			//optional : caller sets value - comes from Quiz --> Question
			public Float Possible_Score;
			//optional : caller sets value - comes from QuizCompletionActivity
			public Float Score;
		}
	}
}
