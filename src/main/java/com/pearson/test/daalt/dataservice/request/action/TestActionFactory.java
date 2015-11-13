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

public interface TestActionFactory {
	public CreateInstructorTestAction getCreateInstructorTestAction(Instructor instructor);
	public CreateStudentTestAction getCreateStudentTestAction(Student student);
	public CreateCourseSectionTestAction getCreateCourseSectionTestAction(CourseSection section);
	public ModifyCourseSectionTestAction getModifyCourseSectionTestAction(CourseSection section, String verb);
	public EnrollStudentTestAction getEnrollStudentTestAction(Student student, CourseSection section);
	public EnrollTATestAction getEnrollTATestAction(TA ta, CourseSection section);
	public EnrollInstructorTestAction getEnrollInstructorTestAction(Instructor instr, CourseSection section);
	public EnrollUserPreTransformTestAction getEnrollUserPreTransformTestAction(User usr, CourseSection section);
	public AddAssignmentTestAction getAddAssignmentTestAction(Instructor instructor, CourseSection section, Assignment assignment);
	public UpdateLearningModuleTestAction getUpdateLearningModuleTestAction(Instructor instructor, CourseSection section, Assignment assignment);
	public DeleteAssignmentContentTestAction getDeleteAssignmentContentTestAction(Instructor instructor, CourseSection section, Assignment assignment);
	public CreateAssignmentContentTestAction getCreateAssignmentContentTestAction(Instructor instructor, CourseSection section, Assignment assignment);
	public UpdateAssignmentContentTestAction getUpdateAssignmentContentTestAction(Instructor instructor, CourseSection section, Assignment assignment);
	public ReSendSeedDataTestAction getReSendSeedDataTestAction(CourseSection section, Assignment assignment);
	public ReadPageTestAction getReadPageTestAction(CourseSection section, Page page, LearningActivity learningActivity);
	public AttemptMultiValueQuestionTestAction getAttemptQuestionTestAction(CourseSection section, Assignment assignment, Quiz quiz, Question question, Attempt attempt, QuestionCompletionActivity questionCompletionActivity);
	public AttemptMultiValueQuestionAsPracticeTestAction getAttemptQuestionAsPracticeTestAction(CourseSection section, Assignment assignment, Quiz quiz, Question question, Attempt attempt, QuestionCompletionActivity questionCompletionActivity);
	public AttemptUnknownFormatQuestionTestAction getAttemptUnknownFormatQuestionTestAction(CourseSection section, Assignment assignment, Quiz quiz,Question question, Attempt attempt,QuestionCompletionActivity questionCompletionActivity);
	public CompleteQuizTestAction getCompleteQuizTestAction(CourseSection section, Assignment assignment, Quiz quiz, QuizCompletionActivity quizCompletionActivity);
	public DropCourseTestAction getDropCourseTestAction(Student student, CourseSection section);
	public DropCourseAndEnrollAgainButOutOfOrderTestAction getDropCourseAndErollAgainButMessageOutOfOrderTestAction(Student student, CourseSection section);
	public LeaveQuestionTestAction getLeaveQuestionTestAction(User user, CourseSection section, Quiz quiz, Question question, LeaveQuestionActivity leaveQuestionActivity, AutoSaveActivity autoSaveActivity);
	public SimulateDueDatePassingTestAction getSimulateDueDatePassingTestAction(Instructor instructor, CourseSection section, Assignment assignment);
	public InstructorAdjustsStudentGradeTestAction getInstructorAdjustsStudentGradeTestAction(CourseSection section, Assignment assignment, Quiz quiz, QuizCompletionActivity quizCompletionActivity);
	public UpdateScoreTestAction getUpdateScoreTestAction(CourseSection section, Assignment assignment, Quiz quiz, QuizCompletionActivity quizCompletionActivity);
	public DeletePublicationTestAction getDeletePublicationTestAction(Instructor instructor, CourseSection section, Assignment assignment);
	public WritingSpaceCompleteQuizTestAction getWritingSpaceCompleteQuizTestAction(CourseSection section, Assignment assignment, Question question, QuestionCompletionActivity questionCompletionActivity, Quiz quiz, QuizCompletionActivity quizCompletionActivity);
	public QuestionCompletionSimpleWritingTestAction getSimpleWritingCompleteQuizTestAction(CourseSection section, Assignment assignment, Question question, QuestionCompletionActivity questionCompletionActivity, Quiz quiz, QuizCompletionActivity quizCompletionActivity);
	public CreateAssignmentWithoutPossibleAnswersTestAction getAlternateAddAssignment(Instructor instructor,CourseSection courseSection,Assignment assignment,Question question);
	public ModifyAssessmentItemTestAction getModifyAssessmentItemTestAction(Quiz quiz);
	public UserUnLoadNonBookContentWithAssignmentTestAction getUserUnLoadNonBookContentWithAssignmentTestAction(CourseSection courseSection,
			Assignment assignment, LastActivityDate lastActivityDate);
	public UserLoadNonBookContentWithAssignmentTestAction getUserLoadNonBookContentWithAssignmentTestAction(
			CourseSection courseSection,Assignment assignment, LastActivityDate lastActivityDate);
	public UserLoadNonBookContentWithoutAssignmentTestAction getUserLoadNonBookContentWithoutAssignmentTestAction(CourseSection courseSection, 
			LastActivityDate lastActivityDate);
	public UserUnLoadNonBookContentWithoutAssignmentTestAction getUserUnLoadNonBookContentWithoutAssignmentTestAction(CourseSection courseSection,
			LastActivityDate lastActivityDate);
	public StudentSkippingAnAssignmentTestAction getStudentSkippingAnAssignmentTestAction(Instructor instructor, CourseSection courseSection,Assignment assignment);
	public StudentAccessQuestionTestAction getStudentAccessQuestionTestAction(CourseSection courseSection, Quiz quiz, Question question,Attempt attempt);
	public StudentAccessContentTestAction getStudentAccessContentTestAction(CourseSection courseSection, Page page, LearningActivity learningActivity);
	
	
}
