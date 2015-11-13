package com.pearson.test.daalt.dataservice.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonObject;

public abstract class SimpleWritingQuestion implements Question {
	protected String id;
	protected String externallyGeneratedId;
	protected String text;
	private List<QuestionCompletionActivity> completionActivities;
	protected Float sequenceNumber;
	protected boolean isSeeded;
	protected String seedDateTime;
	protected String assessmentId;
	protected List<LeaveQuestionActivity> leaveQuestionActivities;

	@Override
	public QuestionDefinitionObject getQuestionDefinitionObject() {		// Not implement for simple writing
		return null;
	}

	@Override
	public AnswerDefinitionObject getAnswerDefinitionObject() {		// Not implement for simple writing
		return null;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public List<Attempt> getAttempts() {
		// not applicable
		return null;
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
	public List<Answer> getAnswers() {
		// not applicable
		return null;
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

	public String getCurrentTimeFormatted() {
		String dateFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		return new SimpleDateFormat(dateFormatString).format(new Date());
	}

	@Override
	public String getAssessmentId() {
		return assessmentId;
	}

	@Override
	public void setAssessmentId(String assessmentId) {
		this.assessmentId = assessmentId;
	}

	@Override
	public boolean studentCompletedQuestion(User stud) {
		boolean completed = false;
		if (completionActivities != null) {
			for (QuestionCompletionActivity activity : completionActivities) {
				if (activity.getPersonId().compareTo(stud.getPersonId()) == 0) {
					completed = true;
				}
			}
		}
		return completed;
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
	public boolean studentAttemptedQuestion(User stud) {
		return studentCompletedQuestion(stud);
	}

	@Override
	public boolean studentUsedFinalAttempt(User stud) {
		return studentAttemptedQuestion(stud);
	}

	@Override
	public boolean studentAnsweredQuestionCorrectly(User stud) {
		boolean answeredCorrectly = false;
		for (QuestionCompletionActivity activity : completionActivities) {
			if (activity.getPersonId().compareTo(stud.getPersonId()) == 0) {
				if (activity.getResponseCode() == AttemptResponseCode.CORRECT
						|| activity.getPassFailCode() == JournalWritingPassFailCode.PASS) {
					answeredCorrectly = true;
				}
			}
		}
		return answeredCorrectly;
	}

	@Override
	public void addAttempt(Attempt attempt) {
		// not applicable - do not implement
	}

	@Override
	public boolean removeAttempt(Attempt attempt) {
		// not applicable
		return false;
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
		return assessmentTime;
	}

	@Override
	public double getMedianAssessmentTime() {
		List<Long> itemTimeSpentAssessingList = new ArrayList<>();
		double medianTimeSpentAssessing = 0;
		if (completionActivities != null) {
			for (QuestionCompletionActivity activity : completionActivities) {
				itemTimeSpentAssessingList.add(activity.getTimeOnQuestion());
			}
		}
		if (leaveQuestionActivities != null) {
			for (LeaveQuestionActivity activity : leaveQuestionActivities) {
				User stud = new BasicStudent(null, null, activity.getPersonId(), null, null);
				if (studentCompletedQuestion(stud)) {
					itemTimeSpentAssessingList.add(activity.getTimeSpent());
				}
			}
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
		return assessmentTime;
	}

	@Override
	public String getQuestionType() {
		return QuestionType.SIMPLE_WRITING.value;
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
	public List<Attempt> getAttemptsForUser(User user) {
		// not applicable
		return null;
	}
	
	@Override
	public void addAutoSaveActivity(AutoSaveActivity autoSaveActivity) {
		// not applicable
	}

	@Override
	public List<AutoSaveActivity> getAutoSaveActivityForUser(User user) {
		// not applicable
		return null;
	}

	@Override
	public List<AutoSaveActivity> getAutoSaveActivities() {
		// not applicable
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
		
		return lastActivityDate;
	}
	
	@Override
	public List<SubQuestion> directAccessSubQuestionList() {
		return null;
	}
	
	@Override
	public int compare(Question thisQuestion, Question thatQuestion) {
		return thisQuestion.getId().compareTo(thatQuestion.getId());
	}

	@Override
	public int compareTo(Question other) {
		return this.getId().compareTo(other.getId());
	}

}
