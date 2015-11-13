package com.pearson.test.daalt.dataservice.model;

import java.util.List;

public interface TestData {
	public String getTestScenarioName();
	
	public Instructor getInstructorById(String personId);
	public void addInstructor(Instructor instructor);
	public Student getStudentById(String studId);
	public void addStudent(Student stud);
	public void addTA(TA ta);
	public void addCourse(Course course);
	public void addCourseSectionToCourse(String courseId, CourseSection section);
	public void addAssignmentToCourseSection(String courseSectionId, Assignment assignment);
	
	public List<CourseSection> getAllCourseSections();
	public CourseSection getDefaultCourseSection();
	public CourseSection getCourseSectionById(String courseSectionId);
	public Assignment getAssignmentById(String assignmentId);
	public Course getDefaultCourse();
}
