package com.pearson.test.daalt.dataservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicCourse implements Course {
	private String id;
	private String externallyGeneratedId;
	private String courseStartDate;
	private String courseEndDate;
	private List<CourseSection> sections;
	
	public BasicCourse() {
		String randomUUID = UUID.randomUUID().toString();
		id = "SQE-C-" + randomUUID;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void addCourseSection(CourseSection section) {
		if (sections == null) {
			sections = new ArrayList<>();
		}
		sections.add(section);
	}

	@Override
	public List<CourseSection> getCourseSections() {
		return new ArrayList<>(sections);
	}

	@Override
	public CourseSection getDefaultCourseSection() {
		CourseSection toReturn = null;
		if (sections != null && !sections.isEmpty()) {
			toReturn = sections.get(0);
		}
		return toReturn;
	}

	@Override
	public CourseSection getCourseSectionById(String sectionId) {
		CourseSection toReturn = null;
		if (sections != null && !sections.isEmpty()) {
			for (CourseSection section : sections) {
				if (section.getId().equalsIgnoreCase(sectionId)) {
					toReturn = section;
				}
			}
		}
		return toReturn;
	}

	@Override
	public String getCourseStartDate() {
		return courseStartDate;
	}

	@Override
	public void setCourseStartDate(String date) {
		this.courseStartDate = date;
	}

	@Override
	public String getCourseEndDate() {
		return courseEndDate;
	}

	@Override
	public void setCourseEndDate(String date) {
		this.courseEndDate = date;
		
	}

	@Override
	public String getExternallyGeneratedId() {
		return externallyGeneratedId;
	}

	@Override
	public void setExternallyGeneratedId(String externallyGeneratedId) {
		this.externallyGeneratedId = externallyGeneratedId;
	}
}
