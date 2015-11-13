package com.pearson.test.daalt.dataservice.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;

public abstract class MultiValueQuestion implements Question {
	protected String id;
	protected String externallyGeneratedId;
	protected String text;
	protected Float pointsPossible;
	private List<QuestionCompletionActivity> completionActivities;

	protected Float sequenceNumber;
	protected String assessmentId;
	protected List<Attempt> attempts;
	protected List<LeaveQuestionActivity> leaveQuestionActivities;
	
	protected boolean isSeeded;
	protected String seedDateTime;
	
	@Override
	public QuestionDefinitionObject getQuestionDefinitionObject() {			// Not implement for multivalue
		return null;
	}

	@Override
	public AnswerDefinitionObject getAnswerDefinitionObject() {			// Not implement for multivalue
		return null;
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
	public List<Answer> getAnswers() {
		Set<Answer> answers = new HashSet<>();
		Set<String> answerIds = new HashSet<>();
		for (SubQuestion que : this.getSubQuestions()){
			for (Answer ans : que.getAnswers()){
				if (!answerIds.contains(ans.getId())) {
					answers.add(ans);
					answerIds.add(ans.getId());
				}
			}
		}
		List<Answer> toReturns = new ArrayList<Answer>();
		toReturns.addAll(answers);
		return toReturns;
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
	public String getAssessmentId() {
		return assessmentId;
	}

	@Override
	public void setAssessmentId(String assessmentId) {
		this.assessmentId = assessmentId;
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
	public boolean studentCompletedQuestionNotBySystem(User stud) {
		boolean complete = false;
		if(completionActivities != null){
			for (QuestionCompletionActivity activity : completionActivities) {
				if (activity.getPersonId().compareTo(stud.getPersonId()) == 0 && !(activity.getPersonRole().equalsIgnoreCase("System"))) {
					complete = true;
					break;
				}
			}
		}
		return complete;
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
	public float getPointsEarnedFinal(User student) {
		float pointsEarned = 0;
		if (attempts != null) {
			for (Attempt attempt : attempts) {
				if(attempt.getPersonId().compareTo(student.getPersonId()) == 0 && attempt.isFinalAttempt()){
					pointsEarned = attempt.getPointsEarned();
					break;
				}
			}
		}
		return pointsEarned;
	}

	@Override
	public boolean removeAttempt(Attempt attempt) {
		return attempts.remove(attempt);
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
	public float getTotalPointsEarnedFinal() {
		float totalItemResponseScore = 0;
		if(attempts != null){
			for (Attempt attempt : attempts) {
				if(attempt.isFinalAttempt()){
					totalItemResponseScore += attempt.getPointsEarned();
				}
			}
		}
		return totalItemResponseScore;
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
		return QuestionType.MULTI_VALUE.value;
	}

	@Override
	public List<QuestionCompletionActivity> getCompletionActivities() {
		return completionActivities == null ? null : new ArrayList<>(completionActivities);
	}

	@Override
	public void addCompletionActivity(QuestionCompletionActivity activity) {
		if (completionActivities == null) {
			completionActivities = new ArrayList<>();
		}
		completionActivities.add(activity);
		
	}
	
	@Override
	public QuestionCompletionActivity getStudentQuestionCompletionActivity(User user){
		QuestionCompletionActivity activity = null;
		if(completionActivities != null){
			for(QuestionCompletionActivity questionCompletionActivity : completionActivities){
				if(questionCompletionActivity.getPersonId().compareTo(user.getPersonId()) == 0){
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
		for(QuestionCompletionActivity act : new ArrayList<QuestionCompletionActivity>(completionActivities)){
			if(act.equals(activity)){
				toRemove.add(activity);
			}
		}
		result = completionActivities.removeAll(toRemove);
		return result;
	}

	@Override
	public boolean removeStudentCompletionActivity(User user) {
		boolean result = false;
		if(completionActivities != null){
			List<QuestionCompletionActivity> toRemove = new ArrayList<>();
			for(QuestionCompletionActivity activity : completionActivities){
				if (activity.getPersonId().compareTo(user.getPersonId()) == 0) {
					toRemove.add(activity);
				}
			}
			result = completionActivities.removeAll(toRemove);
		}
		return result;
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
	
	public String getCurrentTimeFormatted() {
		String dateFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		return new SimpleDateFormat(dateFormatString).format(new Date());
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
	public Float getStudentLateSubmissionPoints(User student) {
		Float studentLateSubmissionPoints = null;
		if (getCompletionActivities() != null) {
			for(QuestionCompletionActivity questionCompletionActivity : getCompletionActivities()) {
				if(questionCompletionActivity.isLate() && questionCompletionActivity.getPersonId().compareTo(student.getPersonId()) == 0) {
					studentLateSubmissionPoints = getPointsEarnedFinal(student);
				}
			}
		}
		return studentLateSubmissionPoints;
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
	
}
