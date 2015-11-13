package com.pearson.test.daalt.dataservice.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BasicQuiz implements Quiz {
	private String id;
	private String externallyGeneratedId;
	private String learningResourceId;
	private Float learningResourceSequenceNumber;
	private List<Question> questions;
	private List<QuizCompletionActivity> completionActivities;
	private List<Quiz> nestedQuizzes;
	private boolean isChapterQuiz;
	
	private boolean isSeeded;
	public boolean isPractice;
	
	public String seedDateTime;
	
	public BasicQuiz() {
		String randomUUID = UUID.randomUUID().toString();
		id = "SQE-Assess-" + randomUUID;
		String randomUUID2 = UUID.randomUUID().toString();
		learningResourceId = "SQE-Quiz-LR-" + randomUUID2;
		isSeeded = true;
		isPractice = false;
		seedDateTime = getCurrentTimeFormatted();
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
	public String getLearningResourceId() {
		return learningResourceId;
	}
	
	@Override
	public void setLearningResourceId(String learningResourceId) {
		this.learningResourceId = learningResourceId;
	}
	
	@Override
	public String getLearningResourceTitle() {
		return "Quiz Title";
	}
	
	@Override
	public Float getLearningResourceSequenceNumber() {
		return learningResourceSequenceNumber;
	}
	
	@Override
	public void setLearningResourceSequenceNumber(Float learningResourceSequenceNumber) {
		this.learningResourceSequenceNumber = learningResourceSequenceNumber;
	}
	
	@Override
	public String getLearningResourceType() {
		String toReturn = isChapterQuiz ? LearningResourceType.CHAPTER_QUIZ.value : LearningResourceType.CHAPTER_SECTION_QUIZ.value;
		return toReturn;
	}

	@Override
	public String getLearningResourceSubType() {
		String toReturn = isChapterQuiz ? LearningResourceSubType.CHAPTER_QUIZ.value : LearningResourceSubType.CHAPTER_SECTION_QUIZ.value;
		return toReturn;
	}

	@Override
	public void setChapterQuiz(boolean isChapterQuiz) {
		this.isChapterQuiz = isChapterQuiz;
	}

	@Override
	public void addQuestion(Question question) {
		if (questions == null) {
			questions = new ArrayList<>();
		}
		questions.add(question);
	}
	
	@Override
	public void removeQuestion(Question question) {
		int index = -1;
		if (questions != null) {
			for (int i=0; i<questions.size(); i++) {
				if (questions.get(i).getId().compareTo(question.getId()) == 0) {
					index = i;
					break;
				}
			}
		}
		if (index > -1) {
			questions.remove(index);
		}
	}

	@Override
	public List<Question> getQuestions() {
		List<Question> toReturn = new ArrayList<>();
		if (questions != null && !questions.isEmpty()) {
			Collections.sort(questions);
			toReturn = new ArrayList<>(questions);
		}
		return toReturn;
	}
	
	@Override
	public List<Question> directAccessQuestionList() {
		return questions;
	}
	
	@Override
	public int getNumberOfMultiValueQuestions() {
		int count = 0;
		if (questions != null) {
			for (Question question : questions) {
				if (question instanceof MultiValueQuestion) {
					count++;
				}
			}
		}
		return count;
	}
	
	@Override
	public boolean containsQuestionWithId(String questionId) {
		boolean result = false;
		if (questions != null) {
			for (Question question : questions) {
				if (question.getId().equalsIgnoreCase(questionId)) {
					result = true;
				}
			}
		}
		return result;
	}
	
	@Override
	public float getPointsPossible() {
		return isPractice ? 0 : getRawPointsPossible();
	}
	
	@Override
	public float getPracticePointsPossible() {
		return isPractice ? getRawPointsPossible() : 0;
	}

	@Override
	public float getAggregatedPointsPossible() {
		Float pointsPossible = getPointsPossible();
		if (nestedQuizzes != null) {
			for (Quiz nestedQuiz : nestedQuizzes) {
				pointsPossible += nestedQuiz.getAggregatedPointsPossible();
			}
		}
		return pointsPossible;
	}	
	
	@Override
	public float getAggregatedPracticePointsPossible() {
		Float pointsPossible = getPracticePointsPossible();
		if (nestedQuizzes != null) {
			for (Quiz nestedQuiz : nestedQuizzes) {
				pointsPossible += nestedQuiz.getAggregatedPracticePointsPossible();
			}
		}
		return pointsPossible;
	}
	
	private float getRawPointsPossible() {
		Float pointsPossible = 0f;
		if (questions != null) {
			for (Question question : questions) {
				pointsPossible += question.getPointsPossible();
			}
		}
		return pointsPossible;
	}
	
	@Override
	public boolean isChapterQuiz() {
		return isChapterQuiz;
	}

	@Override
	public boolean studentCompletedQuiz(User stud) {
		boolean complete = getQuizCompletionActivity(stud) == null ? false : true;
		if (nestedQuizzes != null) {
			for (Quiz nestedQuiz : nestedQuizzes) {
				complete &= nestedQuiz.studentCompletedQuiz(stud);
			}
		}
		return complete;
	}
	
	@Override
	public boolean studentCompleted(User stud) {
		return studentCompletedQuiz(stud);
	}

	@Override
	public List<QuizCompletionActivity> getCompletionActivities() {
		return completionActivities == null ? null : new ArrayList<>(completionActivities);
	}

	@Override
	public void addCompletionActivity(QuizCompletionActivity activity) {
		if (completionActivities == null) {
			completionActivities = new ArrayList<>();
		}
		completionActivities.add(activity);
	}
	
	@Override
	public float getPointsEarnedFinal(User student) {
		float pointsEarned = isPractice ? 0 : getRawPointsEarnedFinal(student);
		if (nestedQuizzes != null) {
			for (Quiz nestedQuiz : nestedQuizzes) {
				pointsEarned += nestedQuiz.getPointsEarnedFinal(student);
			}
		}
		return pointsEarned;
	}
	
	@Override
	public float getTotalPointsEarnedFinal() {
		float pointsEarned = isPractice ? 0 : getRawTotalPointsEarnedFinal();
		if (nestedQuizzes != null) {
			for (Quiz nestedQuiz : nestedQuizzes) {
				pointsEarned += nestedQuiz.getTotalPointsEarnedFinal();
			}
		}
		return pointsEarned;
	}
	
	@Override
	public float getPracticePointsEarnedFinal(User student) {
		float pointsEarned = isPractice ? getRawPointsEarnedFinal(student) : 0;
		if (nestedQuizzes != null) {
			for (Quiz nestedQuiz : nestedQuizzes) {
				pointsEarned += nestedQuiz.getPracticePointsEarnedFinal(student);
			}
		}
		return pointsEarned;
	}

	@Override
	public float getTotalPracticePointsEarnedFinal() {
		float pointsEarned = isPractice ? getRawTotalPointsEarnedFinal() : 0;
		if (nestedQuizzes != null) {
			for (Quiz nestedQuiz : nestedQuizzes) {
				pointsEarned += nestedQuiz.getTotalPracticePointsEarnedFinal();
			}
		}
		return pointsEarned;
	}
	
	private float getRawPointsEarnedFinal(User student) {
		float pointsEarned = 0;
		QuizCompletionActivity completionActivity = getQuizCompletionActivity(student);
		if (completionActivity != null) {
			pointsEarned = completionActivity.getPointsEarned();
		}
		return pointsEarned;
	}
	
	private float getRawTotalPointsEarnedFinal() {
		float pointsEarned = 0;
		if (completionActivities != null) {
			for (QuizCompletionActivity completionActivity : completionActivities) {
				pointsEarned += completionActivity.getPointsEarned();
			}
		}
		return pointsEarned;
	}
	
	@Override
	public long getAssessmentTime(User student) {
		long assessmentTime = 0;
		if (questions != null) {
			for(Question question : questions){
				assessmentTime += question.getInProgressAssessmentTime(student);
			}
		}
		if (nestedQuizzes != null) {
			for (Quiz nestedQuiz : nestedQuizzes) {
				assessmentTime += nestedQuiz.getAssessmentTime(student);
			}
		}
		return assessmentTime;
	}
	
	@Override
	public long getLearningTime(User student) {
		return 0;
	}
	
	private QuizCompletionActivity getQuizCompletionActivity(User stud) {
		QuizCompletionActivity completionActivity = null;
		if (completionActivities != null) {
			for (QuizCompletionActivity activity : completionActivities) {
				if (activity.getPersonId().compareTo(stud.getPersonId()) == 0) {
					completionActivity = activity;
					break;
				}
			}
		}
		return completionActivity;
	}

	@Override
	public List<LearningResource> getChildResources() {
		List<LearningResource> childResources = null;
		if (nestedQuizzes != null && !nestedQuizzes.isEmpty()) {
			childResources = new ArrayList<>();
			for (Quiz nestedQuiz : nestedQuizzes) {
				childResources.add(nestedQuiz);
			}
		}
		return childResources;
	}

	@Override
	public long getChildAssessmentTime(User student) {
		long assessmentTime = 0;
		if (nestedQuizzes != null) {
			for (Quiz nestedQuiz : nestedQuizzes) {
				assessmentTime += nestedQuiz.getAssessmentTime(student);
			}
		}
		return assessmentTime;
	}

	@Override
	public long getChildLearningTime(User student) {
		return 0;
	}

	@Override
	public long getTotalAssessmentTime() {
		long assessmentTime = 0;
		if (questions != null) {
			for(Question question : questions){
				assessmentTime += question.getInProgressTotalAssessmentTime();
			}
		}
		assessmentTime += getTotalChildAssessmentTime();
		return assessmentTime;
	}

	@Override
	public long getTotalLearningTime() {
		return 0;
	}

	@Override
	public long getTotalChildAssessmentTime() {
		long assessmentTime = 0;
		if (nestedQuizzes != null) {
			for (Quiz nestedQuiz : nestedQuizzes) {
				assessmentTime += nestedQuiz.getTotalAssessmentTime();
			}
		}
		return assessmentTime;
	}

	@Override
	public long getTotalChildLearningTime() {
		return 0;
	}

	@Override
	public boolean removeCompletionActivity(QuizCompletionActivity activity) {
		boolean result = false;
		for(QuizCompletionActivity act : new ArrayList<QuizCompletionActivity>(completionActivities)){
			if(act.equals(activity)){
				result = completionActivities.remove(act);
			}
		}
		return result;	
	}

	@Override
	public List<Quiz> getNestedQuizzes() {
		return nestedQuizzes == null ? null : new ArrayList<>(nestedQuizzes);
	}

	@Override
	public void addNestedQuiz(Quiz quiz) {
		if (nestedQuizzes == null) {
			nestedQuizzes = new ArrayList<>();
		}
		nestedQuizzes.add(quiz);
	}

	@Override
	public void setIsAssessmentSeeded(boolean isSeeded) {
		this.isSeeded = isSeeded;
	}

	@Override
	public boolean getIsAssessmentSeeded() {
		return isSeeded;
	}

	@Override
	public String getAssessmentLastSeedDateTime() {
		return seedDateTime;
	}
	
	public String getCurrentTimeFormatted() {
		String dateFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		return new SimpleDateFormat(dateFormatString).format(new Date());
	}

	@Override
	public boolean isPractice() {
		return isPractice;
	}

	@Override
	public boolean isAdjusted() {
		if (completionActivities != null) {
			for (QuizCompletionActivity act : completionActivities) {
				if (act.isAdjusted) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void setPractice(boolean bool) {
		isPractice = bool;
	}

	@Override
	public void setSeedDateTime(String dateTime) {
		this.seedDateTime = dateTime;
	}


	@Override
	public boolean hasPractice() {
		boolean toReturn = isPractice;
		if (questions != null) {
			for (Question q : questions) {
				if (q.isPractice()) {
					toReturn = true;
				}
			}
		}
		if (getNestedQuizzes() != null) {
			for (Quiz nestedQuiz : getNestedQuizzes()) {
				if (nestedQuiz.hasPractice()) {
					toReturn = true;
				}
			}
		}
		return toReturn;
	}

	@Override
	public boolean hasCredit() {
		boolean toReturn = !isPractice;
		if (questions != null) {
			for (Question q : questions) {
				if (!q.isPractice()) {
					toReturn = true;
				}
			}
		}
		if (getNestedQuizzes() != null) {
			for (Quiz nestedQuiz : getNestedQuizzes()) {
				if (nestedQuiz.hasCredit()) {
					toReturn = true;
				}
			}
		}
		return toReturn;
	}

	@Override
	public List<Question> getSeededQuestions() {
		ArrayList<Question> toReturn = new ArrayList<Question>();
		if(questions != null){
			for (Question q : questions) {
				if (q.getIsQuestionSeeded()) {
					toReturn.add(q);
				}
			}
		}
		return toReturn;
	}

	@Override
	public Float getStudentLateSubmissionPoints(User student) {
		Float studentLateSubmissionPoints = null;
		if (questions != null) {
			for (Question q : questions) {
				Float questionLateSubmissionPoints = q.getStudentLateSubmissionPoints(student);
				if (questionLateSubmissionPoints != null) {
					if (studentLateSubmissionPoints == null) {
						studentLateSubmissionPoints = 0.0f;
					}
					studentLateSubmissionPoints += questionLateSubmissionPoints;
					//not checking practice points at this time, as practice points are de-prioritized for DDS V2.1
				}
			}
		}
		if (getNestedQuizzes() != null) {
			for (Quiz nestedQuiz : getNestedQuizzes()) {
				if (studentLateSubmissionPoints == null) {
					studentLateSubmissionPoints = 0.0f;
				}
				Float nestedQuizLateSubmissionPoints = nestedQuiz.getStudentLateSubmissionPoints(student);
				if (nestedQuizLateSubmissionPoints != null) {
					studentLateSubmissionPoints += nestedQuiz.getStudentLateSubmissionPoints(student);
				}
			}
		}
		return studentLateSubmissionPoints;
	}

	@Override
	public Long getLastActivityDate(User student) {
		Long lastActivityDate = null;
		if (questions != null) {
			for (Question q : questions) {
				Long questionLastActivityDate = q.getLastActivityDate(student);
				if (questionLastActivityDate != null && (lastActivityDate == null || questionLastActivityDate > lastActivityDate)) {
					lastActivityDate = questionLastActivityDate;
				} 
			}
		}
		if (getNestedQuizzes() != null) {
			for (Quiz nestedQuiz : getNestedQuizzes()) {
				Long nestedQuizLastActivityDate = nestedQuiz.getLastActivityDate(student);
				if (nestedQuizLastActivityDate != null && (lastActivityDate == null || nestedQuizLastActivityDate > lastActivityDate)) {
					lastActivityDate = nestedQuizLastActivityDate;
				}
			}
		}
		return lastActivityDate;
	}

}

