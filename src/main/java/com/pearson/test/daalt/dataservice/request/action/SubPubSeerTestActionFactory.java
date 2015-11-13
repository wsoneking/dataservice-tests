package com.pearson.test.daalt.dataservice.request.action;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Attempt;
import com.pearson.test.daalt.dataservice.model.AutoSaveActivity;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.LastActivityDate;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.LeaveQuestionActivity;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.QuestionCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.model.TA;
import com.pearson.test.daalt.dataservice.model.User;

public class SubPubSeerTestActionFactory implements TestActionFactory {
	@Override
	public AddAssignmentTestAction getAddAssignmentTestAction(Instructor instructor, CourseSection section,
			Assignment assignment) {
		return new SubPubSeerAddAssignmentTestAction(instructor, section, assignment);
	}

	@Override
	public CreateInstructorTestAction getCreateInstructorTestAction(Instructor instructor) {
		return new SubPubSeerCreateInstructorTestAction(instructor);
	}

	@Override
	public CreateStudentTestAction getCreateStudentTestAction(Student student) {
		return new SubPubSeerCreateStudentTestAction(student);
	}

	@Override
	public CreateCourseSectionTestAction getCreateCourseSectionTestAction(CourseSection section) {
		return new SubPubSeerCreateCourseSectionTestAction(section);
	}

	@Override
	public EnrollStudentTestAction getEnrollStudentTestAction(Student student, CourseSection section) {
		return new SubPubSeerEnrollStudentTestAction(student, section);
	}

	@Override
	public EnrollInstructorTestAction getEnrollInstructorTestAction(Instructor instr, CourseSection section) {
		return new SubPubSeerEnrollInstructorTestAction(instr, section);
	}

	@Override
	public EnrollUserPreTransformTestAction getEnrollUserPreTransformTestAction(User usr, CourseSection section) {
		return new SubPubSeerEnrollUserPreTransformTestAction(usr, section);
	}

	@Override
	public ReadPageTestAction getReadPageTestAction(CourseSection section, Page page, LearningActivity learningActivity) {
		return new SubPubSeerReadPageTestAction(section, page, learningActivity);
	}

	@Override
	public AttemptMultiValueQuestionTestAction getAttemptQuestionTestAction(CourseSection section,
			Assignment assignment, Quiz quiz, Question question, Attempt attempt,
			QuestionCompletionActivity questionCompletionActivity) {
		return new SubPubSeerAttemptMultiValueQuestionTestAction(section, assignment, quiz, question, attempt,
				questionCompletionActivity);
	}
	
	@Override
	public SubPubSeerAttemptMultiValueQuestionAsPracticeTestAction getAttemptQuestionAsPracticeTestAction(CourseSection section,
			Assignment assignment, Quiz quiz, Question question, Attempt attempt,
			QuestionCompletionActivity questionCompletionActivity) {
		return new SubPubSeerAttemptMultiValueQuestionAsPracticeTestAction(section, assignment, quiz, question, attempt,
				questionCompletionActivity);
	}
	
	@Override
	public SubPubSeerAttemptUnknownFormatQuestionTestAction getAttemptUnknownFormatQuestionTestAction(CourseSection section,
			Assignment assignment, Quiz quiz, Question question, Attempt attempt,
			QuestionCompletionActivity questionCompletionActivity) {
		return new SubPubSeerAttemptUnknownFormatQuestionTestAction(section, assignment, quiz, question, attempt,
				questionCompletionActivity);
	}

	@Override
	public CompleteQuizTestAction getCompleteQuizTestAction(CourseSection section, Assignment assignment, Quiz quiz,
			QuizCompletionActivity quizCompletionActivity) {
		return new SubPubSeerCompleteQuizTestAction(section, assignment, quiz, quizCompletionActivity);
	}

	@Override
	public DropCourseTestAction getDropCourseTestAction(Student student, CourseSection section) {
		return new SubPubSeerDropStudentTestAction(student, section);
	}

	@Override
	public DeleteAssignmentContentTestAction getDeleteAssignmentContentTestAction(Instructor instructor,
			CourseSection section, Assignment assignment) {
		return new SubPubSeerDeleteAssignmentContentTestAction(instructor, section, assignment);
	}

	@Override
	public CreateAssignmentContentTestAction getCreateAssignmentContentTestAction(Instructor instructor,
			CourseSection section, Assignment assignment) {
		return new SubPubSeerCreateAssignmentContentTestAction(instructor, section, assignment);
	}
	
	@Override
	public UpdateAssignmentContentTestAction getUpdateAssignmentContentTestAction(Instructor instructor,
			CourseSection section, Assignment assignment) {
		return new SubPubSeerUpdateAssignmentContentTestAction(instructor, section, assignment);
	}

	@Override
	public ReSendSeedDataTestAction getReSendSeedDataTestAction(CourseSection section, Assignment assignment) {
		return new SubPubSeerReSendSeedDataTestAction(section, assignment);
	}

	@Override
	public EnrollTATestAction getEnrollTATestAction(TA ta, CourseSection section) {
		return new SubPubSeerEnrollTATestAction(ta, section);
	}

	@Override
	public LeaveQuestionTestAction getLeaveQuestionTestAction(User user, CourseSection section, Quiz quiz,
			Question question, LeaveQuestionActivity leaveQuestionActivity, AutoSaveActivity autoSaveActivity) {
		return new SubPubSeerLeaveQuestionTestAction(user, section, quiz, question, leaveQuestionActivity,
				autoSaveActivity);
	}
	
	@Override
	public SimulateDueDatePassingTestAction getSimulateDueDatePassingTestAction(Instructor instructor,
			CourseSection section, Assignment assignment) {
		return new SubPubSeerSimulateDueDatePassingTestAction(instructor, section, assignment);
	}

	@Override
	public InstructorAdjustsStudentGradeTestAction getInstructorAdjustsStudentGradeTestAction(CourseSection section,
			Assignment assignment, Quiz quiz, QuizCompletionActivity quizCompletionActivity) {
		return new SubPubSeerInstructorAdjustsStudentGradeTestAction(section, assignment, quiz, quizCompletionActivity);
	}

	@Override
	public UpdateScoreTestAction getUpdateScoreTestAction(CourseSection section, Assignment assignment, Quiz quiz,
			QuizCompletionActivity quizCompletionActivity) {
		return new SubPubSeerUpdateScoreTestAction(section, assignment, quiz, quizCompletionActivity);
	}

	@Override
	public DeletePublicationTestAction getDeletePublicationTestAction(Instructor instructor, CourseSection section,
			Assignment assignment) {
		return new SubPubSeerDeletePublicationTestAction(instructor, section, assignment);
	}

	@Override
	public WritingSpaceCompleteQuizTestAction getWritingSpaceCompleteQuizTestAction(CourseSection section,
			Assignment assignment, Question question, QuestionCompletionActivity questionCompletionActivity, Quiz quiz,
			QuizCompletionActivity quizCompletionActivity) {
		return new SubPubSeerWritingSpaceCompleteQuizTestAction(section, assignment, question,
				questionCompletionActivity, quiz, quizCompletionActivity);
	}

	@Override
	public QuestionCompletionSimpleWritingTestAction getSimpleWritingCompleteQuizTestAction(CourseSection section,
			Assignment assignment, Question question, QuestionCompletionActivity questionCompletionActivity, Quiz quiz,
			QuizCompletionActivity quizCompletionActivity) {

		return new SubPubSeerQuestionCompletionSimpleWritingTestAction(section, assignment, quiz, question,
				questionCompletionActivity, quizCompletionActivity);
	}
	
	@Override
	public ModifyAssessmentItemTestAction getModifyAssessmentItemTestAction(Quiz quiz) {
		return new SubPubSeerModifyAssessmentItemTestAction(quiz);
	}

	@Override
	public ModifyCourseSectionTestAction getModifyCourseSectionTestAction(
			CourseSection section, String verb) {
		return new SubPubSeerModifyCourseSectionTestAction(section, verb);

	}

	@Override
	public CreateAssignmentWithoutPossibleAnswersTestAction getAlternateAddAssignment(Instructor instructor,
			CourseSection courseSection, Assignment assignment, Question question) {
		return new SubPubSeerCreateAssignmentWithoutPossibleAnswersTestAction(instructor, courseSection, assignment,
				question);
	}


	@Override
	public UpdateLearningModuleTestAction getUpdateLearningModuleTestAction(
			Instructor instructor, CourseSection section, Assignment assignment) {
		return new SubPubSeerUpdateLearningModuleTestAction(instructor, section, assignment);
	}
	@Override
	public UserLoadNonBookContentWithAssignmentTestAction getUserLoadNonBookContentWithAssignmentTestAction(CourseSection courseSection,
			Assignment assignment, LastActivityDate lastActivityDate) {

		return new SubPubSeerUserLoadNonBookContentWithAssignmentTestAction(courseSection,assignment, lastActivityDate);
	}

	@Override
	public UserUnLoadNonBookContentWithAssignmentTestAction getUserUnLoadNonBookContentWithAssignmentTestAction(
			CourseSection courseSection, Assignment assignment, LastActivityDate lastActivityDate) {
		return new SubPubSeerUserUnloadNonBookContentWithAssignmentTestAction(courseSection,
				 assignment,lastActivityDate);
	}

	@Override
	public UserLoadNonBookContentWithoutAssignmentTestAction getUserLoadNonBookContentWithoutAssignmentTestAction(
			CourseSection courseSection, LastActivityDate lastActivityDate) {
			return new SubPubSeerUserLoadNonBookContentWithoutAssignmentTestAction(courseSection,lastActivityDate);
	}

	@Override
	public UserUnLoadNonBookContentWithoutAssignmentTestAction getUserUnLoadNonBookContentWithoutAssignmentTestAction(
			CourseSection courseSection, LastActivityDate lastActivityDate) {
		return new SubPubSeerUserUnloadNonBookContentWithoutAssignmentTestAction(courseSection,lastActivityDate);
	}

	@Override
	public StudentSkippingAnAssignmentTestAction getStudentSkippingAnAssignmentTestAction(Instructor instructor,
			CourseSection courseSection, Assignment assignment) {
		return new SubPubSeerStudentSkippingAnAssignmentTestAction(instructor,
				courseSection, assignment) ;
	}



	@Override
	public StudentAccessContentTestAction getStudentAccessContentTestAction(CourseSection courseSection, Page page,
			LearningActivity learningActivity) {
		
		return new SubPubSeerStudentAccessContentTestAction(courseSection,page, learningActivity);
	}

	@Override
	public StudentAccessQuestionTestAction getStudentAccessQuestionTestAction(CourseSection courseSection, Quiz quiz,
			Question question, Attempt attempt) {
		
		return new SubPubSeerStudentAccessQuestionTestAction(courseSection,quiz,question,attempt);
	}

	@Override
	public DropCourseAndEnrollAgainButOutOfOrderTestAction getDropCourseAndErollAgainButMessageOutOfOrderTestAction(
			Student student, CourseSection section) {
		return new SubPubSeerDropCourseAndEnrollAgainButOutOfOrderTestAction(student, section);
	}





	

}
