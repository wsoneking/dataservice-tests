package com.pearson.test.daalt.dataservice.model;

import java.util.List;

public interface CourseSection {
//	public CourseSection copyMe();
	public String getId();
	public void setId(String id);
	public String getExternallyGeneratedId();
	public void setExternallyGeneratedId(String externallyGeneratedId);
	public String getBookId();
	public String getLearningResourceContextId();
	public boolean isIndependentLearningResourceHierarchy();
	public void setIndependentLearningResourceHierarchy(boolean independentLearningResourceHierarchy);
	public String getTitle();
	public void setTitle(String title);
	public Instructor getInstructor();
	public void setInstructor(Instructor instructor);
	public void addStudent(Student student);
	public void addTA(TA ta);
	public int getEnrolledStudentCount();
	public List<Student> getEnrolledStudents();
	public Student getStudentById(String personId);
	public void addAssignment(Assignment assignment);
	public List<Assignment> getAssignments();
	public void addBook(Product book);
	public Assignment getFirstAssignment();
	public Assignment getAssignmentById(String assignmentId);
	public Float getPointsPossible();
	public float getPointsEarnedFinal(User student);
	public boolean removeStudent(Student student);
	public long getTimeSpentAssessing(User student);
	Assignment getMostRecentAssignmentForTrend(User student);
	public List<LastActivityDate> getLastActivityDates();
	
	
	
	// add based on the message contracts
	public String getCourseSectionCode();			// 
	public void setCourseSectionCode(String code);
	public String getCourseSectionDescription();
	public void setCourseSectionDescription(String description);
	
	public long getLearningTime(User student);
	public Long getLastActivityDate(User student);
	public Assignment getMostRecentAssignment(User student);
	public void addLastActivityDate(LastActivityDate lastActivityDate);
	
	public LastActivityDate getMostRecentLastActivityDateObject();

	
	
}
