package com.pearson.test.daalt.dataservice.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonObject;

public class WritingSpaceQuestion implements Question {

	private String id;
	private String externallyGeneratedId;
	private String text;
	private Float pointsPossible;
	private Float sequenceNumber;
	private String assessmentId;
	private List<QuestionCompletionActivity> completionActivities;
	private List<LeaveQuestionActivity> leaveQuestionActivities;
	private List<AutoSaveActivity> autoSaveActivities;
	private boolean isPractice;

	private boolean isSeeded;
	private String seedDateTime;
	
	public WritingSpaceQuestion(){
		String randomUUID = UUID.randomUUID().toString();
		this.id = "SQE-" + QuestionPresentationFormat.WRITING_SPACE.value + "-" + randomUUID;
		isSeeded = true;
		isPractice = false;
		seedDateTime = getCurrentTimeFormatted();
	}
	
	public WritingSpaceQuestion(String text, float pointsPossible, float sequenceNumber) {
		this();
		this.text = text;
		this.pointsPossible = pointsPossible;
		this.sequenceNumber = sequenceNumber;
	}
	
	@Override
	public Question copyMe() {
		Question copy = new WritingSpaceQuestion();
		copy.setAssessmentId(assessmentId);
		copy.setId(id);
		copy.setExternallyGeneratedId(externallyGeneratedId);
		copy.setText(text);
		copy.setSequenceNumber(sequenceNumber);
		copy.setIsQuestionSeeded(isSeeded);
		copy.setQuestionLastSeedDateTime(seedDateTime);
		copy.setPointsPossible(pointsPossible);
		return copy;
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
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public float getPointsPossible() {
		return pointsPossible;
	}

	@Override
	public void setPointsPossible(Float pointsPossible) {
		this.pointsPossible = pointsPossible;
	}

	@Override
	public float getSequenceNumber() {
		return sequenceNumber;
	}

	@Override
	public void setSequenceNumber(Float sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public void setAssessmentId(String assessmentId) {
		this.assessmentId = assessmentId;
	}

	@Override
	public String getAssessmentId() {
		return assessmentId;
	}
	
	@Override
	public void setIsQuestionSeeded(boolean isSeeded) {
		this.isSeeded = isSeeded;
	}

	@Override
	public boolean getIsQuestionSeeded() {
		return isSeeded;
	}
	
	@Override
	public String getQuestionLastSeedDateTime() {
		return seedDateTime;
	}
	
	@Override
	public void setQuestionLastSeedDateTime(String lastSeedDateTime) {
		this.seedDateTime = lastSeedDateTime;
	}
	
	@Override
	public String getQuestionType() {
		return QuestionType.WRITING_SPACE.value;
	}

	@Override
	public String getQuestionPresentationFormat() {
		return QuestionPresentationFormat.WRITING_SPACE.value;
	}
	
	@Override
	public QuestionPresentationFormat getQuestionPresentationFormatAsEnum() {
		return QuestionPresentationFormat.WRITING_SPACE;
	}

	@Override
	public boolean studentCompletedQuestion(User stud) {
		boolean complete = false;
		if(completionActivities != null){
			for (QuestionCompletionActivity activity : completionActivities) {
				if (activity.getPersonId().compareTo(stud.getPersonId()) == 0) {
					complete = true;
					break;
				}
			}
		}
		return complete;
	}

	@Override
	public boolean studentAnsweredQuestionCorrectly(User stud) {
		return studentCompletedQuestion(stud);
	}
	
	@Override
	public boolean studentAttemptedQuestion(User stud) {
		return studentCompletedQuestion(stud);
	}
	
	@Override
	public boolean studentUsedFinalAttempt(User user) {
		return studentAttemptedQuestion(user);
	}
	
	@Override
	public float getPointsEarnedFinal(User stud) {
		float pointsEarned = 0;
		if (completionActivities != null) {
			for (QuestionCompletionActivity activity : completionActivities) {
				if (activity.getPersonId().compareTo(stud.getPersonId()) == 0) {
					pointsEarned += activity.getScore();
				}
			}
		}
		return pointsEarned;
	}

	@Override
	public float getTotalPointsEarnedFinal() {
		float totalItemResponseScore = 0;
		if (completionActivities != null) {
			for (QuestionCompletionActivity activity : completionActivities) {
				totalItemResponseScore += activity.getScore();
			}
		}
		return totalItemResponseScore;
	}
	
	@Override
	public long getInProgressAssessmentTime(User student) {
		long assessmentTime = 0;
		if (completionActivities != null) {
			for (QuestionCompletionActivity activity : completionActivities) {
				if (activity.getPersonId().compareTo(student.getPersonId()) == 0) {
					assessmentTime += activity.getTimeOnQuestion();
				}
			}
		}
		if (leaveQuestionActivities != null) {
			for (LeaveQuestionActivity activity : leaveQuestionActivities) {
				if (activity.getPersonId().compareTo(student.getPersonId()) == 0) {
					assessmentTime += activity.getTimeSpent();
				}
			}
		}
		if (autoSaveActivities != null) {
			for (AutoSaveActivity activity : autoSaveActivities) {
				if (activity.getPersonId().compareTo(student.getPersonId()) == 0) {
					assessmentTime += activity.getTimeSpent();
				}
			}
		}
		return assessmentTime;
	}

	@Override
	public long getInProgressTotalAssessmentTime() {
		long assessmentTime = 0;
		if (completionActivities != null) {
			for (QuestionCompletionActivity activity : completionActivities) {
				assessmentTime += activity.getTimeOnQuestion();
			}
		}
		if (leaveQuestionActivities != null) {
			for (LeaveQuestionActivity activity : leaveQuestionActivities) {
				assessmentTime += activity.getTimeSpent();
			}
		}
		if (autoSaveActivities != null) {
			for (AutoSaveActivity activity : autoSaveActivities) {
				assessmentTime += activity.getTimeSpent();
			}
		}
		return assessmentTime;
	}

	@Override
	public long getCompletedAssessmentTime(User student) {
		long assessmentTime = 0;
		if (completionActivities != null) {
			for (QuestionCompletionActivity activity : completionActivities) {
				if (activity.getPersonId().compareTo(student.getPersonId()) == 0) {
					assessmentTime += activity.getTimeOnQuestion();
				}
			}
		}
		if (leaveQuestionActivities != null) {
			for (LeaveQuestionActivity activity : leaveQuestionActivities) {
				if (activity.getPersonId().compareTo(student.getPersonId()) == 0) {
					User stud = new BasicStudent(null, null, activity.getPersonId(), null, null);
					if (studentCompletedQuestion(stud)) {
						assessmentTime += activity.getTimeSpent();
					}
				}
			}
		}
		if (autoSaveActivities != null) {
			for (AutoSaveActivity activity : autoSaveActivities) {
				if (activity.getPersonId().compareTo(student.getPersonId()) == 0) {
					User stud = new BasicStudent(null, null, activity.getPersonId(), null, null);
					if (studentCompletedQuestion(stud)) {
						assessmentTime += activity.getTimeSpent();
					}
				}
			}
		}
		return assessmentTime;
	}

	@Override
	public long getCompletedTotalAssessmentTime() {
		long assessmentTime = 0;
		if (completionActivities != null) {
			for (QuestionCompletionActivity activity : completionActivities) {
				assessmentTime += activity.getTimeOnQuestion();
			}
		}
		if (leaveQuestionActivities != null) {
			for (LeaveQuestionActivity activity : leaveQuestionActivities) {
				User stud = new BasicStudent(null, null, activity.getPersonId(), null, null);
				if (studentCompletedQuestion(stud)) {
					assessmentTime += activity.getTimeSpent();
				}
			}
		}
		if (autoSaveActivities != null) {
			for (AutoSaveActivity activity : autoSaveActivities) {
				User stud = new BasicStudent(null, null, activity.getPersonId(), null, null);
				if (studentCompletedQuestion(stud)) {
					assessmentTime += activity.getTimeSpent();
				}
			}
		}
		return assessmentTime;
	}

	@Override
	public double getMedianAssessmentTime() {
		Map<String, Long> itemTimeSpentAssessingMap = new HashMap<>();
		double medianTimeSpentAssessing = 0;
		if (completionActivities != null) {
			for (QuestionCompletionActivity activity : completionActivities) {
				if (!itemTimeSpentAssessingMap.containsKey(activity.getPersonId())) {
					itemTimeSpentAssessingMap.put(activity.getPersonId(), 0L);
				}
				Long timeOnTask = itemTimeSpentAssessingMap.get(activity.getPersonId());
				timeOnTask += activity.getTimeOnQuestion();
				itemTimeSpentAssessingMap.put(activity.getPersonId(), timeOnTask);
			}
		}
		if (leaveQuestionActivities != null) {
			for (LeaveQuestionActivity activity : leaveQuestionActivities) {
				User stud = new BasicStudent(null, null, activity.getPersonId(), null, null);
				if (studentCompletedQuestion(stud)) {
					if (!itemTimeSpentAssessingMap.containsKey(activity.getPersonId())) {
						itemTimeSpentAssessingMap.put(activity.getPersonId(), 0L);
					}
					Long timeOnTask = itemTimeSpentAssessingMap.get(activity.getPersonId());
					timeOnTask += activity.getTimeSpent();
					itemTimeSpentAssessingMap.put(activity.getPersonId(), timeOnTask);
				}
			}
		}
		if (autoSaveActivities != null) {
			for (AutoSaveActivity activity : autoSaveActivities) {
				User stud = new BasicStudent(null, null, activity.getPersonId(), null, null);
				if (studentCompletedQuestion(stud)) {
					if (!itemTimeSpentAssessingMap.containsKey(activity.getPersonId())) {
						itemTimeSpentAssessingMap.put(activity.getPersonId(), 0L);
					}
					Long timeOnTask = itemTimeSpentAssessingMap.get(activity.getPersonId());
					timeOnTask += activity.getTimeSpent();
					itemTimeSpentAssessingMap.put(activity.getPersonId(), timeOnTask);
				}
			}
		}
		
		List<Long> itemTimeSpentAssessingList = new ArrayList<>();
		Iterator<String> mapIter = itemTimeSpentAssessingMap.keySet().iterator();
		while (mapIter.hasNext()) {
			Long timeOnTask = (Long) itemTimeSpentAssessingMap.get(mapIter.next());
			itemTimeSpentAssessingList.add(timeOnTask);
		}
		Collections.sort(itemTimeSpentAssessingList);
		if (itemTimeSpentAssessingList.size() == 0) {
			medianTimeSpentAssessing = 0;
		} else if (itemTimeSpentAssessingList.size() == 1) {
			medianTimeSpentAssessing = itemTimeSpentAssessingList.get(0);
		} else {
			int middle = itemTimeSpentAssessingList.size() / 2;
			if (itemTimeSpentAssessingList.size() % 2 == 1) {
				medianTimeSpentAssessing = itemTimeSpentAssessingList.get(middle);
			} else {
				medianTimeSpentAssessing = (itemTimeSpentAssessingList.get(middle - 1) + itemTimeSpentAssessingList
						.get(middle)) / 2.0;
			}
		}

		return medianTimeSpentAssessing;
	}

	@Override
	public List<QuestionCompletionActivity> getCompletionActivities() {
		return completionActivities == null ? null : new ArrayList<>(completionActivities);
	}

	@Override
	public void addCompletionActivity(QuestionCompletionActivity activity) {
		if (completionActivities == null) {
			completionActivities = new ArrayList<QuestionCompletionActivity>();
		}
		completionActivities.add(activity);

	}
	
	@Override
	public QuestionCompletionActivity getStudentQuestionCompletionActivity(User user) {
		QuestionCompletionActivity activity = null;
		if (completionActivities != null) {
			for (QuestionCompletionActivity questionCompletionActivity : completionActivities) {
				if (questionCompletionActivity.getPersonId().compareTo(user.getPersonId()) == 0) {
					activity = questionCompletionActivity;
				}
			}
		}
		return activity;
	}

	@Override
	public boolean removeCompletionActivity(QuestionCompletionActivity activity) {
		boolean result = false;
		List<QuestionCompletionActivity> toRemove = new ArrayList<>();
		for (QuestionCompletionActivity act : new ArrayList<QuestionCompletionActivity>(completionActivities)) {
			if (act.equals(activity)) {
				toRemove.add(activity);
			}
		}
		result = completionActivities.removeAll(toRemove);
		return result;
	}

	@Override
	public boolean removeStudentCompletionActivity(User user) {
		boolean result = false;
		if (completionActivities != null) {
			List<QuestionCompletionActivity> toRemove = new ArrayList<>();
			for (QuestionCompletionActivity activity : completionActivities) {
				if (activity.getPersonId().compareTo(user.getPersonId()) == 0) {
					toRemove.add(activity);
				}
			}
			result = completionActivities.removeAll(toRemove);
		}
		return result;
	}
	
	@Override
	public void addLeaveQuestion(LeaveQuestionActivity leaveQuestion) {
		if (leaveQuestionActivities == null) {
			leaveQuestionActivities = new ArrayList<>();
		}
		leaveQuestionActivities.add(leaveQuestion);
	}

	@Override
	public List<LeaveQuestionActivity> getLeaveQuestions() {
		return leaveQuestionActivities == null ? null : new ArrayList<>(leaveQuestionActivities);
	}

	@Override
	public List<LeaveQuestionActivity> getLeaveQuestionForUser(User user) {
		List<LeaveQuestionActivity> studLeaveAttempts = new ArrayList<>();
		if (leaveQuestionActivities != null) {
			for (LeaveQuestionActivity leaveQuestionActivity : leaveQuestionActivities) {
				if (leaveQuestionActivity.getPersonId().compareTo(user.getPersonId()) == 0) {
					studLeaveAttempts.add(leaveQuestionActivity);
				}
			}
		}
		return studLeaveAttempts;
	}

	@Override
	public long getTimeSpentWithoutAttempt(User user) {
		long assessmentTime = 0;
		if (leaveQuestionActivities != null) {
			for (LeaveQuestionActivity leaveQuestionActivity : leaveQuestionActivities) {
				if (leaveQuestionActivity.getPersonId().compareTo(user.getPersonId()) == 0) {
					assessmentTime += leaveQuestionActivity.getTimeSpent();
				}
			}
		}
		if (autoSaveActivities != null) {
			for (AutoSaveActivity activity : autoSaveActivities) {
				if (activity.getPersonId().compareTo(user.getPersonId()) == 0) {
					assessmentTime += activity.getTimeSpent();
				}
			}
		}
		return assessmentTime;
	}

	@Override
	public long getTimeSpentWithoutAttemptTotal() {
		long assessmentTime = 0;
		if (leaveQuestionActivities != null) {
			for (LeaveQuestionActivity leaveQuestionActivity : leaveQuestionActivities) {
				assessmentTime += leaveQuestionActivity.getTimeSpent();
			}
		}
		if (autoSaveActivities != null) {
			for (AutoSaveActivity activity : autoSaveActivities) {
				assessmentTime += activity.getTimeSpent();
			}
		}
		return assessmentTime;
	}

	@Override
	public void addAutoSaveActivity(AutoSaveActivity autoSaveActivity) {
		if (autoSaveActivities == null) {
			autoSaveActivities = new ArrayList<>();
		}
		autoSaveActivities.add(autoSaveActivity);	
	}

	@Override
	public List<AutoSaveActivity> getAutoSaveActivities() {
		return autoSaveActivities == null ? null : new ArrayList<>(autoSaveActivities);
	}
	
	@Override
	public List<AutoSaveActivity> getAutoSaveActivityForUser(User user) {
		List<AutoSaveActivity> studAutoSaveActivities = new ArrayList<>();
		if (autoSaveActivities != null) {
			for (AutoSaveActivity autoSaveActivity : autoSaveActivities) {
				if (autoSaveActivity.getPersonId().compareTo(user.getPersonId()) == 0) {
					studAutoSaveActivities.add(autoSaveActivity);
				}
			}
		}
		return studAutoSaveActivities;
	}
	
	public String getCurrentTimeFormatted() {
		String dateFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		return new SimpleDateFormat(dateFormatString).format(new Date());
	}
	
	@Override
	public void addAnswer(Answer answer) {
		// not applicable
	}

	@Override
	public List<Answer> getAnswers() {
		return null; //not applicable
	}
	
	@Override
	public List<Attempt> getAttempts() {
		//not applicable
		return null;
	}

	@Override
	public void addAttempt(Attempt attempt) {
		//not applicable
	}

	@Override
	public List<Attempt> getAttemptsForUser(User user) {
		//not applicable
		return null;
	}
	
	@Override
	public boolean removeAttempt(Attempt attempt) {
		// not applicable
		return false;
	}
	
	@Override
	public List<SubQuestion> getSubQuestions() {
		// not applicable
		return null;
	}
	
	@Override
	public List<SubQuestion> directAccessSubQuestionList() {
		return null;
	}

	@Override
	public void addSubQuestion(SubQuestion subquestion) {
		// not applicable
	}
	
	@Override
	public boolean removeSubQuestion(SubQuestion subQuestion) {
		// not applicable
		return false;
	}
	
	@Override
	public boolean isPractice() {
		return isPractice;
	}

	@Override
	public void setPractice(boolean bool) {
		isPractice = bool;
	}
	
	@Override
	public boolean studentCompletedQuestionNotBySystem(User stud) {
		boolean completed = false;
		if (completionActivities != null) {
			for (QuestionCompletionActivity activity : completionActivities) {
				if (activity.getPersonId().compareTo(stud.getPersonId()) == 0 && activity.getPersonRole().equalsIgnoreCase("System")) {
					completed = true;
				}
			}
		}
		return completed;
	}

	@Override
	public Float getStudentLateSubmissionPoints(User student) {
		// For release 2.1 only the end point 1.11 uses non multi-value item types & 1.11 doesn't report studentLateSubmissionPoints
		return null;
	}

	@Override
	public Long getLastActivityDate(User student) {
		Long lastActivityDate = null;
		if(completionActivities != null){
			for (QuestionCompletionActivity questionCompletionActivity : completionActivities) {
				if (questionCompletionActivity.getPersonId().compareTo(student.getPersonId()) == 0) {
					Long questionLastActivityDate = questionCompletionActivity.getLastActivityDate();
					if (questionLastActivityDate != null && (lastActivityDate == null || questionLastActivityDate > lastActivityDate)) {
						lastActivityDate = questionLastActivityDate;
					}
		
				}
			}
		}
		if (leaveQuestionActivities != null) {
			for (LeaveQuestionActivity leaveQuestionActivity : leaveQuestionActivities) {
				if (leaveQuestionActivity.getPersonId().compareTo(student.getPersonId()) == 0) {
					Long leaveQuestionLastActivityDate = leaveQuestionActivity.getLastActivityDate();
					if (leaveQuestionLastActivityDate != null && (lastActivityDate == null || leaveQuestionLastActivityDate > lastActivityDate)) {
						lastActivityDate = leaveQuestionLastActivityDate;
					}
				}
			}
		}
		if (autoSaveActivities != null) {
			for (AutoSaveActivity autoSaveActivity : autoSaveActivities) {
				if (autoSaveActivity.getPersonId().compareTo(student.getPersonId()) == 0) {
					Long autoSaveActivityLastActivityDate = autoSaveActivity.getLastActivityDate();
					if (autoSaveActivityLastActivityDate != null && (lastActivityDate == null || autoSaveActivityLastActivityDate > lastActivityDate)) {
						lastActivityDate = autoSaveActivityLastActivityDate;
					}
				}
			}
		}
		
		return lastActivityDate;
	}

	@Override
	public int compare(Question thisQuestion, Question thatQuestion) {
		return thisQuestion.getId().compareTo(thatQuestion.getId());
	}

	@Override
	public int compareTo(Question other) {
		return this.getId().compareTo(other.getId());
	}

	@Override
	public QuestionDefinitionObject getQuestionDefinitionObject() {		// Not implement for writing space
		return null;
	}

	@Override
	public AnswerDefinitionObject getAnswerDefinitionObject() {		// Not implement for writing space
		return null;
	}
}

