package com.pearson.test.daalt.dataservice.model;

import java.util.ArrayList;
import java.util.List;

public class BasicTestData implements
	TestData {
	private String testScenarioName;
	
	private Course course;
	private List<Instructor> instructorList;
	private List<Student> studList;
	private List<TA> taList;
	
	public BasicTestData(String testScenarioName) {
		this.testScenarioName = testScenarioName;
	}
	
	@Override
	public String getTestScenarioName() {
		return testScenarioName;
	}
	
	@Override
	public Instructor getInstructorById(String personId) {
		Instructor toReturn = null;
		if (instructorList != null) {
			for (Instructor instr : instructorList) {
				if (instr.getPersonId().equalsIgnoreCase(personId)) {
					toReturn = instr;
				}
			}
		}
		return toReturn;
	}
	
	@Override
	public void addInstructor(Instructor instructor) {
		if (instructorList == null) {
			instructorList = new ArrayList<>();
		}
		instructorList.add(instructor);
	}
	
	@Override
	public Student getStudentById(String studId) {
		Student toReturn = null;
		if (studList != null) {
			for (Student stud : studList) {
				if (stud.getPersonId().equalsIgnoreCase(studId)) {
					toReturn = stud;
				}
			}
		}
		return toReturn;
	}
	
	@Override
	public void addStudent(Student stud) {
		if (studList == null) {
			studList = new ArrayList<>();
		}
		studList.add(stud);
	}
	
	@Override
	public void addCourse(Course course) {
		this.course = course;
	}
	
	@Override
	public void addCourseSectionToCourse(String courseId, CourseSection section) {
		this.course.addCourseSection(section);
	}
	
	@Override
	public void addAssignmentToCourseSection(String courseSectionId, Assignment assignment) {
		this.course.getCourseSectionById(courseSectionId).addAssignment(assignment);
	}
	
	@Override
	public CourseSection getDefaultCourseSection() {
		return course.getDefaultCourseSection();
	}
	
	@Override
	public Course getDefaultCourse() {
		return course;
	}
	
	@Override
	public List<CourseSection> getAllCourseSections() {
		List<CourseSection> toReturn = null;
		if (course != null && course.getCourseSections() != null) {
			toReturn = course.getCourseSections();
		}
		return toReturn;
	}
	
	@Override
	public CourseSection getCourseSectionById(String courseSectionId) {
		CourseSection toReturn = null;
		if (course != null && course.getCourseSections() != null) {
			for (CourseSection section : course.getCourseSections()) {
				if (section.getId().equalsIgnoreCase(courseSectionId)) {
					toReturn = section;
				}
			}
		}
		return toReturn;
	}
	
	@Override
	public Assignment getAssignmentById(String assignmentId) {
		Assignment toReturn = null;
		if (course != null && course.getCourseSections() != null) {
			for (CourseSection section : course.getCourseSections()) {
				for (Assignment assignment : section.getAssignments()) {
					if (assignment.getId().equalsIgnoreCase(assignmentId)) {
						toReturn = assignment;
					}
				}
			}
		}
		return toReturn;
	}

	@Override
	public void addTA(TA ta) {
		if (taList == null) {
			taList = new ArrayList<>();
		}
		taList.add(ta);
		
	}


}
