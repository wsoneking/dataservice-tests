package com.pearson.test.daalt.dataservice.model;

import java.util.List;

public interface Course {
//	public Course copyMe();
	public String getId();
	public void setId(String id);
	public String getExternallyGeneratedId();
	public void setExternallyGeneratedId(String externallyGeneratedId);
	public void addCourseSection(CourseSection section);
	public List<CourseSection> getCourseSections();
	public CourseSection getDefaultCourseSection();
	public CourseSection getCourseSectionById(String sectionId);

	public String getCourseStartDate();
	public void setCourseStartDate(String date);
	public String getCourseEndDate();
	public void setCourseEndDate(String date);
}
