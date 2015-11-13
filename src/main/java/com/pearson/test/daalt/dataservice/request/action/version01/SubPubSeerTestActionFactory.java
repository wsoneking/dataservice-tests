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
import com.pearson.test.daalt.dataservice.request.action.SubPubSeerCreateAssignmentWithoutPossibleAnswersTestAction;

public class SubPubSeerTestActionFactory implements TestActionFactory {
	@Override
	public AddAssignmentTestAction getAddAssignmentTestAction(
			Instructor instructor, CourseSection section, Assignment assignment) {
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
	public EnrollStudentTestAction getEnrollStudentTestAction(Student student,
			CourseSection section) {
		return new SubPubSeerEnrollStudentTestAction(student, section);
	}

	@Override
	public EnrollInstructorTestAction getEnrollInstructorTestAction(
			Instructor instr, CourseSection section) {
		return new SubPubSeerEnrollInstructorTestAction(instr, section);
	}

	@Override
	public ReadPageTestAction getReadPageTestAction(User user, CourseSection section, Assignment assignment,
			Page page, LearningActivity learningActivity) {
		return new SubPubSeerReadPageTestAction(user, section, assignment, page, learningActivity);
	}

	@Override
	public AttemptQuestionTestAction getAttemptQuestionTestAction(CourseSection section, Assignment assignment, 
			Quiz quiz, Question question, Attempt attempt) {
		return new SubPubSeerAttemptQuestionTestAction(section, assignment, quiz, question, attempt);
	}

	@Override
	public CompleteQuizTestAction getCompleteQuizTestAction(
			CourseSection section, Assignment assignment, Quiz quiz,
			QuizCompletionActivity quizCompletionActivity) {
		return new SubPubSeerCompleteQuizTestAction(section, assignment, quiz, quizCompletionActivity);
	}
	
	@Override
	public AssessmentPerformanceTestAction getAssessmentPerformanceTestAction(
			CourseSection section, Assignment assignment, Quiz quiz,
			QuizCompletionActivity quizCompletionActivity) {
		return new SubPubSeerAssessmentPerformanceTestAction(section, assignment, quiz, quizCompletionActivity);
	}

	@Override
	public DropCourseTestAction getDropCourseTestAction(Student student,
			CourseSection section) {
		return new SubPubSeerDropStudentTestAction(student, section);
	}
		
	public ModifyAssignmentTestAction getModifyAssignmentTestAction(
			Instructor instructor, CourseSection section,
			Assignment assignment, String verb) {
		return new SubPubSeerModifyAssignmentTestAction(instructor, section, assignment, verb);
	}

	@Override
	public EnrollTATestAction getEnrollTATestAction(TA ta, CourseSection section) {
		return new SubPubSeerEnrollTATestAction(ta, section);
	}

	@Override
	public CreateAssignmentWithoutPossibleAnswersTestAction getAlternateAddAssignment(Instructor instructor,
			CourseSection courseSection, Assignment assignment, Question question) {
		return new SubPubSeerCreateAssignmentWithoutPossibleAnswersTestAction(instructor, courseSection, assignment,
				question);
	}
	
}
