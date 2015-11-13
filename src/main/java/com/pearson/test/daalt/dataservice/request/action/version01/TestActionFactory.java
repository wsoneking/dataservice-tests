package com.pearson.test.daalt.dataservice.request.action.version01;

import com.pearson.test.daalt.dataservice.model.Assignment;
import com.pearson.test.daalt.dataservice.model.Attempt;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Instructor;
import com.pearson.test.daalt.dataservice.model.LearningActivity;
import com.pearson.test.daalt.dataservice.model.Page;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.Quiz;
import com.pearson.test.daalt.dataservice.model.QuizCompletionActivity;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.model.TA;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.action.CreateAssignmentWithoutPossibleAnswersTestAction;

public interface TestActionFactory {
	public CreateInstructorTestAction getCreateInstructorTestAction(Instructor instructor);
	public CreateStudentTestAction getCreateStudentTestAction(Student student);
	public CreateCourseSectionTestAction getCreateCourseSectionTestAction(CourseSection section);
	public EnrollStudentTestAction getEnrollStudentTestAction(Student student, CourseSection section);
	public EnrollTATestAction getEnrollTATestAction(TA ta, CourseSection section);
	public EnrollInstructorTestAction getEnrollInstructorTestAction(Instructor instr, CourseSection section);
	public AddAssignmentTestAction getAddAssignmentTestAction(Instructor instructor, CourseSection section, Assignment assignment);
	public ModifyAssignmentTestAction getModifyAssignmentTestAction(Instructor instructor, CourseSection section, Assignment assignment, String verb);
	public ReadPageTestAction getReadPageTestAction(User user, CourseSection section, Assignment assignment, Page page, LearningActivity learningActivity);
	public AttemptQuestionTestAction getAttemptQuestionTestAction(CourseSection section, Assignment assignment, Quiz quiz, Question question, Attempt attempt);
	public CompleteQuizTestAction getCompleteQuizTestAction(CourseSection section, Assignment assignment, Quiz quiz, QuizCompletionActivity quizCompletionActivity);
	public AssessmentPerformanceTestAction getAssessmentPerformanceTestAction(CourseSection section, Assignment assignment, Quiz quiz, QuizCompletionActivity quizCompletionActivity);
	public DropCourseTestAction getDropCourseTestAction(Student student, CourseSection section);
	public CreateAssignmentWithoutPossibleAnswersTestAction getAlternateAddAssignment(Instructor instructor,CourseSection courseSection,Assignment assignment,Question question);
}
