package com.pearson.test.daalt.dataservice.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;



public class BasicCourseSection implements CourseSection {
	private String id;
	private String externallyGeneratedId;
	private String title;
	private Instructor instructor;
	private List<Student> students;
	private List<TA> tas;
	private List<Assignment> assignments;
	private List<LastActivityDate> lastActivityDates;
	private String courseSectionCode;
	private String courseSectionDescription;
	private Product book;
	private boolean independentLearningResourceHierarchy;
	
	public BasicCourseSection () {
		String randomUUID = UUID.randomUUID().toString();
		id = "SQE-CS-" + randomUUID;
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
	public String getExternallyGeneratedId() {
		return externallyGeneratedId;
	}

	@Override
	public void setExternallyGeneratedId(String externallyGeneratedId) {
		this.externallyGeneratedId = externallyGeneratedId;
	}

	@Override
	public String getBookId() {
		return book.getId();
	}
	
	@Override
	public boolean isIndependentLearningResourceHierarchy() {
		return independentLearningResourceHierarchy;
	}

	@Override
	public void setIndependentLearningResourceHierarchy(
			boolean independentLearningResourceHierarchy) {
		this.independentLearningResourceHierarchy = independentLearningResourceHierarchy;
	}
	
	@Override
	public String getLearningResourceContextId() {
		return independentLearningResourceHierarchy ? getId() : getBookId();
	}
	
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void addAssignment(Assignment assignment) {
		if (assignments == null) {
			assignments = new ArrayList<>();
		}
		assignments.add(assignment);
	}
	
	@Override
	public List<Assignment> getAssignments() {
		List<Assignment> toReturn = new ArrayList<>();
		if (assignments != null) {
			toReturn = new ArrayList<>(assignments);
		}
		return toReturn;
	}
	
	@Override
	public Assignment getFirstAssignment() {
		Assignment toReturn = null;
		if (assignments != null && !assignments.isEmpty()) {
			toReturn = assignments.get(0);
		}
		return toReturn;
	}
	
	@Override
	public Assignment getAssignmentById(String assignmentId) {
		Assignment toReturn = null;
		if (assignments != null) {
			for (Assignment assignment : assignments) {
				if (assignment.getId().equalsIgnoreCase(assignmentId)) {
					toReturn = assignment;
				}
			}
		}
		return toReturn;
	}

	@Override
	public Instructor getInstructor() {
		return instructor;
	}

	@Override
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	@Override
	public void addStudent(Student student) {
		if (students == null) {
			students = new ArrayList<>();
		}
		students.add(student);
	}

	@Override
	public List<Student> getEnrolledStudents() {
		List<Student> toReturn = new ArrayList<>();
		if (students != null && !students.isEmpty()) {
			Collections.sort(students);
			toReturn = new ArrayList<>(students);
		}
		return toReturn;
	}
	
	@Override
	public int getEnrolledStudentCount() {
		return students == null ? 0 : students.size();
	}

	@Override
	public Student getStudentById(String personId) {
		Student toReturn = null;
		if (students != null) {
			for (Student stud : students) {
				if (stud.getPersonId().equalsIgnoreCase(personId)) {
					toReturn = stud;
				}
			}
		}
		return toReturn;
	}

	@Override
	public Float getPointsPossible() {
		Float pointsPossible = 0f;
		if (assignments != null) {
			for (Assignment assignment : assignments) {
				pointsPossible += assignment.getPointsPossible();
			}
		}
		return pointsPossible;
	}
	
	@Override
	public float getPointsEarnedFinal(User student) {
		float pointsEarned = 0f;
		if (assignments != null) {
			for (Assignment assignment : assignments) {
				pointsEarned += assignment.getPointsEarnedFinal(student);
			}
		}
		return pointsEarned;
	}

	@Override
	public String getCourseSectionCode() {
		return courseSectionCode;
	}

	@Override
	public void setCourseSectionCode(String code) {
		this.courseSectionCode = code;
	}

	@Override
	public String getCourseSectionDescription() {
		if(courseSectionDescription == null)
			return "Automatically created course section description";
		
		return courseSectionDescription;
	}

	@Override
	public void setCourseSectionDescription(String description) {
		this.courseSectionDescription = description;
		
	}

	@Override
	public void addBook(Product book) {
		this.book = book;
	}

	@Override
	public void addTA(TA ta) {
		if (tas == null) {
			tas = new ArrayList<>();
		}
		tas.add(ta);
	}

	@Override
	public boolean removeStudent(Student student) {
		return students.remove(student);
	}

	@Override
	public long getLearningTime(User student) {
		long learningTime = 0;
		if (assignments != null) {
			for (Assignment assign : assignments) {
				learningTime += assign.getLearningTime(student);
			}
		}
		return learningTime;
	}

	@Override
	public long getTimeSpentAssessing(User student) {
		long timeSpentAssessing = 0;
		if (assignments != null) {
			for (Assignment assign : assignments) {
				timeSpentAssessing += assign.getAssessmentTime(student);
			}
		}
		return timeSpentAssessing;
	}
	

	@Override
	public Assignment getMostRecentAssignmentForTrend(User student) {
		Assignment toReturn = null;
		if (assignments != null && !assignments.isEmpty()) {
			for(int index = assignments.size()-1; index >= 0; index-- ) {
				if(assignments.get(index).isDueDatePassed() && assignments.get(index).studentCompletedAssignment(student)) {
					toReturn = assignments.get(index);
					break;
				}
			}
		
		}
		
		return toReturn;
	}


	@Override
	public Assignment getMostRecentAssignment(User student) {
		Assignment toReturn = null;
		Assignment tempAssign =null;
		if (assignments != null) {
			for (int i=0; i < assignments.size(); i++) {
				for(int a = 1; a<assignments.size();a++){
					if(assignments.get(a-1).getLastActivityDate(student).compareTo(assignments.get(a).getLastActivityDate(student))< 1){
						tempAssign = assignments.get(a-1);
						assignments.set(a-1,assignments.get(a));
						assignments.set(a, tempAssign);
					}
				}
			}
		} 
		toReturn = assignments.get(assignments.size()-1);
		return toReturn;
	}

	@Override
	public List<LastActivityDate> getLastActivityDates() {
		List<LastActivityDate> toReturn = new ArrayList<>();
		if (assignments != null) {
			toReturn = new ArrayList<>(lastActivityDates);
		}
		return toReturn;
	}

	@Override
	public void addLastActivityDate(LastActivityDate lastActivityDate) {
		if (lastActivityDates == null) {
			lastActivityDates = new ArrayList<>();
		}
		lastActivityDates.add(lastActivityDate);	
	}
	


	@Override
	public Long getLastActivityDate(User student) {
		Long lastActivityDate = null;
		if (assignments != null) {
			for (Assignment assign : assignments) {
				Long assignmentLastActivityDate = assign.getLastActivityDate(student);
				if (assignmentLastActivityDate != null  && (lastActivityDate == null || assignmentLastActivityDate > lastActivityDate)){
					lastActivityDate = assignmentLastActivityDate;
				}
			}
		}
		
		if (lastActivityDates != null) {
			Long lastActivityDateObjectLastActivityDate = null;
			for (LastActivityDate lastActivityDateObject : lastActivityDates) {
				if (lastActivityDateObject.getPersonId().compareTo(student.getPersonId()) == 0) {
					lastActivityDateObjectLastActivityDate = lastActivityDateObject.getLastActivityDate();
					if (lastActivityDateObjectLastActivityDate != null && (lastActivityDate == null || lastActivityDateObjectLastActivityDate > lastActivityDate)) {
						lastActivityDate = lastActivityDateObject.getLastActivityDate();
					}
				}
			}
		}
		
		return lastActivityDate;
	}

	@Override
	public LastActivityDate getMostRecentLastActivityDateObject() {
		LastActivityDate toReturn = null;
		if (lastActivityDates != null && lastActivityDates.isEmpty()) {
			toReturn = (lastActivityDates.size() == 1) ? lastActivityDates.get(0):lastActivityDates.get(lastActivityDates.size()-1);
		}
		return toReturn;
	}
	
}
		

