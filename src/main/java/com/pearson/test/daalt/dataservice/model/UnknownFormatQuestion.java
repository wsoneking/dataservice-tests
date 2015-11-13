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

public class UnknownFormatQuestion implements Question {

	private String id;
	private String externallyGeneratedId;
	private String text;
	private Float pointsPossible;
	private Float sequenceNumber;
	private String assessmentId;
	protected List<Attempt> attempts;
	private List<QuestionCompletionActivity> completionActivities;
	private List<LeaveQuestionActivity> leaveQuestionActivities;
	private List<AutoSaveActivity> autoSaveActivities;
	private boolean isPractice;

	private boolean isSeeded;
	private String seedDateTime;
	
	private QuestionDefinitionObject questionDefinitionObject;
	private AnswerDefinitionObject answerDefinitionObject;
	
	public UnknownFormatQuestion(){
		String randomUUID = UUID.randomUUID().toString();
		this.id = "SQE-" + QuestionPresentationFormat.UNKNOWN_FORMAT.value + "-" + randomUUID;
		isSeeded = true;
		isPractice = false;
		seedDateTime = getCurrentTimeFormatted();
	}
	
	public UnknownFormatQuestion(String text, float pointsPossible, float sequenceNumber) {
		this();
		this.text = text;
		this.pointsPossible = pointsPossible;
		this.sequenceNumber = sequenceNumber;
		questionDefinitionObject = new QuestionDefinitionObject();
		answerDefinitionObject = new AnswerDefinitionObject();
	}
	
	@Override
	public Question copyMe() {
		UnknownFormatQuestion copy = new UnknownFormatQuestion();
		copy.setAssessmentId(assessmentId);
		copy.setId(id);
		copy.setExternallyGeneratedId(externallyGeneratedId);
		copy.setText(text);
		copy.setSequenceNumber(sequenceNumber);
		copy.setIsQuestionSeeded(isSeeded);
		copy.setQuestionLastSeedDateTime(seedDateTime);
		copy.setPointsPossible(pointsPossible);
		copy.setAnswerDefinitionObject(answerDefinitionObject);
		copy.setQuestionDefinitionObject(questionDefinitionObject);
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
		return QuestionType.UNKNOWN_FORMAT.value;
	}

	@Override
	public String getQuestionPresentationFormat() {
		return QuestionPresentationFormat.UNKNOWN_FORMAT.value;
	}
	
	@Override
	public QuestionPresentationFormat getQuestionPresentationFormatAsEnum() {
		return QuestionPresentationFormat.UNKNOWN_FORMAT;
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
		boolean answeredCorrectly = false;
		if (attempts != null) {
			for (Attempt attempt : attempts) {
				if (attempt.getPersonId().compareTo(stud.getPersonId()) == 0 && attempt.isFinalAttempt()) {
					answeredCorrectly = attempt.getAnswerCorrectness() == AttemptResponseCode.CORRECT;
				}
			}
		}
		return answeredCorrectly;
	}
	
	@Override
	public boolean studentAttemptedQuestion(User stud) {
		boolean attempted = false;
		if (attempts != null) {
			for (Attempt attempt : attempts) {
				if (attempt.getPersonId().compareTo(stud.getPersonId()) == 0) {
					attempted = true;
					break;
				}
			}
		}
		return attempted;
	}
	
	@Override
	public boolean studentUsedFinalAttempt(User stud){
		boolean usedFinalAttempt = false;
		if (attempts != null) {
			for (Attempt attempt : attempts) {
				if(attempt.getPersonId().compareTo(stud.getPersonId()) == 0 && attempt.isFinalAttempt()){
					usedFinalAttempt = true;
					break;
				}
			}
		}
		return usedFinalAttempt;
	}
	
	@Override
	public float getPointsEarnedFinal(User stud) {
		float pointsEarned = 0;
		if (attempts != null) {
			for (Attempt attempt : attempts) {
				if(attempt.getPersonId().compareTo(stud.getPersonId()) == 0 && attempt.isFinalAttempt()){
					pointsEarned = attempt.getPointsEarned();
					break;
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
		if (attempts != null) {
			for (Attempt attempt : attempts) {
				if (attempt.getPersonId().compareTo(student.getPersonId()) == 0) {
					assessmentTime += attempt.getTimeSpent();
				}
			}
		}
		if (leaveQuestionActivities != null) {
			for (LeaveQuestionActivity leaveQuestionActivity : leaveQuestionActivities) {
				if (leaveQuestionActivity.getPersonId().compareTo(student.getPersonId()) == 0) {
					assessmentTime += leaveQuestionActivity.getTimeSpent();
				}
			}
		}
		return assessmentTime;
	}

	@Override
	public long getInProgressTotalAssessmentTime() {
		long assessmentTime = 0;
		if (attempts != null) {
			for (Attempt attempt : attempts) {
				assessmentTime += attempt.getTimeSpent();
			}
		}
		if (leaveQuestionActivities != null) {
			for (LeaveQuestionActivity leaveQuestionActivity : leaveQuestionActivities) {
				assessmentTime += leaveQuestionActivity.getTimeSpent();
			}
		}
		return assessmentTime;
	}

	@Override
	public long getCompletedAssessmentTime(User student) {
		long assessmentTime = 0;
		if (studentCompletedQuestion(student)) {
			assessmentTime = getInProgressAssessmentTime(student);
		}
		return assessmentTime;
	}
	
	@Override
	public long getCompletedTotalAssessmentTime() {
		long assessmentTime = 0;
		if (attempts != null) {
			for (Attempt attempt : attempts) {
				if (studentCompletedQuestion(attempt.getPerson())) {
					assessmentTime += attempt.getTimeSpent();
				}
			}
		}
		if (leaveQuestionActivities != null) {
			for (LeaveQuestionActivity leaveQuestionActivity : leaveQuestionActivities) {
				if (studentCompletedQuestion(leaveQuestionActivity.getPerson())) {
					assessmentTime += leaveQuestionActivity.getTimeSpent();
				}
			}
		}
		return assessmentTime;
	}

	@Override
	public double getMedianAssessmentTime() {
		//TODO:For each student that has a final attempt on this question, include that student's assessment time in the list of 
		//possible assessment times from which we will select the median(including time spent without attempting)
		List<Long> itemTimeSpentAssessingList = new ArrayList<>();
		double medianTimeSpentAssessing = 0; 
		Map<String, List<Attempt>> attemptMap = new HashMap<>();
		Map<String, List<LeaveQuestionActivity>> activityMap = new HashMap<>();
		List<Attempt> studAttempts = new ArrayList<>();
		List<LeaveQuestionActivity> studActivities = new ArrayList<>();
		if(attempts != null){
			for(Attempt attempt : attempts){
				List<Attempt> atmpt = attemptMap.get(attempt.getPersonId());
				if(atmpt == null){
					studAttempts.add(attempt);
					attemptMap.put(attempt.getPersonId(), studAttempts);
				}else {
					atmpt.add(attempt);
					attemptMap.put(attempt.getPersonId(), atmpt);
				}
			}
			for (Map.Entry<String, List<Attempt>> entry : attemptMap.entrySet()) {
				long totalItemAssessmentTime = 0;
				for (Attempt attempt : attempts) {
					if (attempt.getPersonId().compareTo(entry.getKey()) == 0 && studentCompletedQuestion(attempt.getPerson())) {
						totalItemAssessmentTime += attempt.getTimeSpent();
					}
				}
				if(leaveQuestionActivities != null){
					for (LeaveQuestionActivity activity : leaveQuestionActivities) {
						if (activity.getPersonId().compareTo(entry.getKey()) == 0 && studentCompletedQuestion(activity.getPerson())) {
							totalItemAssessmentTime += activity.getTimeSpent();
						}
					}
				}
				itemTimeSpentAssessingList.add(totalItemAssessmentTime);
			}
			Collections.sort(itemTimeSpentAssessingList);
			if(itemTimeSpentAssessingList.size() == 0){
				medianTimeSpentAssessing = 0;
			}else if(itemTimeSpentAssessingList.size() == 1){
				medianTimeSpentAssessing = itemTimeSpentAssessingList.get(0);
			}else {
				int middle = itemTimeSpentAssessingList.size()/2;
				if (itemTimeSpentAssessingList.size() % 2 == 1) {
					medianTimeSpentAssessing =  itemTimeSpentAssessingList.get(middle);
			    }else {
			    	medianTimeSpentAssessing  = (itemTimeSpentAssessingList.get(middle-1) + itemTimeSpentAssessingList.get(middle)) / 2.0;
			    } 
			}
		} else if (leaveQuestionActivities != null) {		
			for(LeaveQuestionActivity activity : leaveQuestionActivities){
				List<LeaveQuestionActivity> act = activityMap.get(activity.getPersonId());
				if(act == null){
					studActivities.add(activity);
					activityMap.put(activity.getPersonId(), studActivities);
				}else {
					act.add(activity);
					activityMap.put(activity.getPersonId(), act);
				}
			}
			
			long totalItemAssessmentTime = 0;
			for (Map.Entry<String, List<LeaveQuestionActivity>> entry : activityMap.entrySet()) {
				for (LeaveQuestionActivity activity : leaveQuestionActivities) {
					if (activity.getPersonId().compareTo(entry.getKey()) == 0 && studentCompletedQuestion(activity.getPerson())) {
						totalItemAssessmentTime += activity.getTimeSpent();
					}
				}
			}

			itemTimeSpentAssessingList.add(totalItemAssessmentTime);
			Collections.sort(itemTimeSpentAssessingList);
			if(itemTimeSpentAssessingList.size() == 0){
				medianTimeSpentAssessing = 0;
			}else if(itemTimeSpentAssessingList.size() == 1){
				medianTimeSpentAssessing = itemTimeSpentAssessingList.get(0);
			}else {
				int middle = itemTimeSpentAssessingList.size()/2;
				if (itemTimeSpentAssessingList.size() % 2 == 1) {
					medianTimeSpentAssessing =  itemTimeSpentAssessingList.get(middle);
			    }else {
			    	medianTimeSpentAssessing  = (itemTimeSpentAssessingList.get(middle-1) + itemTimeSpentAssessingList.get(middle)) / 2.0;
			    } 
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
		return attempts == null ? null : new ArrayList<>(attempts);
	}

	@Override
	public void addAttempt(Attempt attempt) {
		if (attempts == null) {
			attempts = new ArrayList<>();
		}
		attempts.add(attempt);
	}

	@Override
	public List<Attempt> getAttemptsForUser(User user) {
		List<Attempt> studAttempts = new ArrayList<>();
		if (attempts != null) {
			for (Attempt attempt : attempts) {
				if (attempt.getPersonId().compareTo(user.getPersonId()) == 0) {
					studAttempts.add(attempt);
				}
			}
		}
		return studAttempts;
	}
	
	@Override
	public boolean removeAttempt(Attempt attempt) {
		// not applicable
		return false;
	}
	
	@Override
	public List<SubQuestion> getSubQuestions() {
		return new ArrayList<SubQuestion>();
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
		if (getAttempts() != null) {
			for (Attempt attempt : getAttempts()) {
				if (attempt.getPersonId().compareTo(student.getPersonId()) == 0) {
					Long attemptLastActivity = attempt.getLastActivityDate();
					if (attemptLastActivity != null && (lastActivityDate == null || attemptLastActivity > lastActivityDate)) {
						lastActivityDate = attemptLastActivity;
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
	public int compare(Question thisQuestion, Question thatQuestion) {
		return thisQuestion.getId().compareTo(thatQuestion.getId());
	}

	@Override
	public int compareTo(Question other) {
		return this.getId().compareTo(other.getId());
	}

	@Override
	public QuestionDefinitionObject getQuestionDefinitionObject() {
		return questionDefinitionObject;
	}
	
	public void setQuestionDefinitionObject(QuestionDefinitionObject object) {
		questionDefinitionObject = object;
	}

	@Override
	public AnswerDefinitionObject getAnswerDefinitionObject() {
		return answerDefinitionObject;
	}
	
	public void setAnswerDefinitionObject(AnswerDefinitionObject object) {
		answerDefinitionObject = object;
	}

	@Override
	public List<SubQuestion> directAccessSubQuestionList() {
		// TODO Auto-generated method stub
		return null;
	}
}

