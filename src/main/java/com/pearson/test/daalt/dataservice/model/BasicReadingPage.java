package com.pearson.test.daalt.dataservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicReadingPage implements Page {
	private String id;
	private String externallyGeneratedId;
	private Float learningResourceSequenceNumber;
	private Quiz quiz;
	private List<Question> embeddedQuestions;
	private List<LearningActivity> learningActivities;
	private List<Page> pages;
	private boolean isPractice;


	public BasicReadingPage() {
		String randomUUID = UUID.randomUUID().toString();
		isPractice = true;
		id = "SQE-Read-" + randomUUID;
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
		return id;
	}

	@Override
	public void setLearningResourceId(String learningResourceId) {
		// not applicable - do nothing
	}

	@Override
	public String getLearningResourceTitle() {
		return "Reading Page Title";
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
		return LearningResourceType.READING_PAGE.value;
	}

	@Override
	public String getLearningResourceSubType() {
		return LearningResourceSubType.READING_PAGE.value;
	}

	@Override
	public Quiz getQuiz() {
		return quiz;
	}

	@Override
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	@Override
	public List<Question> getEmbeddedQuestions() {
		return embeddedQuestions == null ? null : new ArrayList<>(embeddedQuestions);
	}

	@Override
	public void addEmbeddedQuestion(Question question) {
		if (embeddedQuestions == null) {
			embeddedQuestions = new ArrayList<>();
		}
		embeddedQuestions.add(question);
	}

	@Override
	public EmbeddedQuestion getEmbeddedQuestionLR(Question question, float learningResourceSeqNum) {
		EmbeddedQuestion embeddedQuestionLR = new EmbeddedQuestion();
		embeddedQuestionLR.setPractice(question.isPractice());
		embeddedQuestionLR.setAssessmentId(question.getAssessmentId());
		embeddedQuestionLR.setItemId(question.getId());
		embeddedQuestionLR.setText(question.getText());
		embeddedQuestionLR.setLearningResourceSequenceNumber(learningResourceSeqNum);
		embeddedQuestionLR.setParentLearningResourceId(this.getLearningResourceId());
		embeddedQuestionLR.setPointsPossible(question.getPointsPossible());
		embeddedQuestionLR.setPresentationFormat(question.getQuestionPresentationFormat());
		for (SubQuestion subQuestion : question.getSubQuestions()) {
			embeddedQuestionLR.addSubQuestion(subQuestion);
		}
		if (question.getAttempts() != null) {
			for (Attempt attempt : question.getAttempts()) {
				embeddedQuestionLR.addAttempt(attempt);
			}
		}
		if (question.getLeaveQuestions() != null) {
			for (LeaveQuestionActivity leaveQuestion : question.getLeaveQuestions()) {
				embeddedQuestionLR.addLeaveQuestion(leaveQuestion);
			}
		}
		if (question.getCompletionActivities() != null) {
			for (QuestionCompletionActivity completionAct : question.getCompletionActivities()) {
				embeddedQuestionLR.addCompletionActivity(completionAct);
			}
		}
		return embeddedQuestionLR;
	}

	@Override
	public boolean studentCompleted(User stud) {
		boolean pageComplete = true;
		if (quiz != null) {
			pageComplete &= quiz.studentCompletedQuiz(stud);
		}
		if (embeddedQuestions != null) {
			for (Question embeddedQuestion : embeddedQuestions) {
				pageComplete &= embeddedQuestion.studentCompletedQuestion(stud);
			}
		}
		return pageComplete;
	}

	@Override
	public List<LearningActivity> getLearningActivities() {
		return learningActivities == null ? null : new ArrayList<>(learningActivities);
	}

	@Override
	public List<Question> getSeededEmbeddedQuestions() {
		ArrayList<Question> toReturn = new ArrayList<Question>();
		if (embeddedQuestions != null) {
			for (Question q : embeddedQuestions) {
				if (q.getIsQuestionSeeded()) {
					toReturn.add(q);
				}
			}

		}
		return toReturn;
	}

	@Override
	public void addLearningActivity(LearningActivity activity) {
		if (learningActivities == null) {
			learningActivities = new ArrayList<>();
		}
		learningActivities.add(activity);
	}

	@Override
	public long getLearningTime(User student) {
		long learningTime = 0;
		if (learningActivities != null) {
			for (LearningActivity activity : learningActivities) {
				if (activity.getPersonId().compareTo(student.getPersonId()) == 0 && activity.isCountForLearningTime()) {
					learningTime += activity.getTimeSpent();
				}
			}
		}
		if (pages != null) {
			for (Page pg : pages) {
				learningTime += pg.getLearningTime(student);
			}
		}
		return learningTime;
	}

	@Override
	public float getAggregatedPointsPossible() {
		Float pointsPossible = 0f;
		if (quiz != null) {
			pointsPossible += quiz.getAggregatedPointsPossible();
		}
		if (embeddedQuestions != null) {
			for (Question embeddedQuestion : embeddedQuestions) {
				EmbeddedQuestion embeddedQuestionLR = getEmbeddedQuestionLR(embeddedQuestion, /* seqNum */0);
				pointsPossible += embeddedQuestionLR.getAggregatedPointsPossible();
			}
		}
		return pointsPossible;
	}

	@Override
	public float getAggregatedPracticePointsPossible() {
		Float pointsPossible = 0f;
		if (quiz != null) {
			pointsPossible += quiz.getAggregatedPracticePointsPossible();
		}
		if (embeddedQuestions != null) {
			for (Question embeddedQuestion : embeddedQuestions) {
				EmbeddedQuestion embeddedQuestionLR = getEmbeddedQuestionLR(embeddedQuestion, /* seqNum */0);
				pointsPossible += embeddedQuestionLR.getAggregatedPracticePointsPossible();
			}
		}
		return pointsPossible;
	}

	@Override
	public float getPointsPossible() {
		return 0;
	}

	@Override
	public float getPracticePointsPossible() {
		return 0;
	}

	@Override
	public float getPracticePointsEarnedFinal(User student) {
		float pointsEarned = 0;
		if (pages != null) {
			for (Page page : pages) {
				pointsEarned += page.getPracticePointsEarnedFinal(student);
			}
		}
		if (quiz != null) {
			pointsEarned += quiz.getPracticePointsEarnedFinal(student);
		}
		if (embeddedQuestions != null) {
			for (Question embeddedQuestion : embeddedQuestions) {
				EmbeddedQuestion embeddedQuestionLR = getEmbeddedQuestionLR(embeddedQuestion, /* seqNum */0);
				pointsEarned += embeddedQuestionLR.getPracticePointsEarnedFinal(student);
			}
		}
		return pointsEarned;
	}

	@Override
	public float getTotalPracticePointsEarnedFinal() {
		float pointsEarned = 0;
		if (pages != null) {
			for (Page page : pages) {
				pointsEarned += page.getTotalPracticePointsEarnedFinal();
			}
		}
		if (quiz != null) {
			pointsEarned += quiz.getTotalPracticePointsEarnedFinal();
		}
		if (embeddedQuestions != null) {
			for (Question embeddedQuestion : embeddedQuestions) {
				EmbeddedQuestion embeddedQuestionLR = getEmbeddedQuestionLR(embeddedQuestion, /* seqNum */0);
				pointsEarned += embeddedQuestionLR.getTotalPracticePointsEarnedFinal();
			}
		}
		return pointsEarned;
	}

	@Override
	public float getPointsEarnedFinal(User student) {
		float pointsEarned = 0;
		if (pages != null) {
			for (Page page : pages) {
				pointsEarned += page.getPointsEarnedFinal(student);
			}
		}
		if (quiz != null) {
			pointsEarned += quiz.getPointsEarnedFinal(student);
		}
		if (embeddedQuestions != null) {
			for (Question embeddedQuestion : embeddedQuestions) {
				EmbeddedQuestion embeddedQuestionLR = getEmbeddedQuestionLR(embeddedQuestion, /* seqNum */0);
				pointsEarned += embeddedQuestionLR.getPointsEarnedFinal(student);
			}
		}
		return pointsEarned;
	}

	@Override
	public float getTotalPointsEarnedFinal() {
		float pointsEarned = 0;
		if (pages != null) {
			for (Page page : pages) {
				pointsEarned += page.getTotalPointsEarnedFinal();
			}
		}
		if (quiz != null) {
			pointsEarned += quiz.getTotalPointsEarnedFinal();
		}
		if (embeddedQuestions != null) {
			for (Question embeddedQuestion : embeddedQuestions) {
				if(!(embeddedQuestion.isPractice())){
					pointsEarned += embeddedQuestion.getTotalPointsEarnedFinal();
				}
			}
		}
		return pointsEarned;
	}

	@Override
	public long getAssessmentTime(User student) {
		long assessmentTime = 0;
		if (pages != null) {
			for (Page page : pages) {
				assessmentTime += page.getAssessmentTime(student);
			}
		}
		if (quiz != null) {
			assessmentTime += quiz.getAssessmentTime(student);
		}
		if (embeddedQuestions != null) {
			for (Question embeddedQuestion : embeddedQuestions) {
				assessmentTime += embeddedQuestion.getInProgressAssessmentTime(student);
			}
		}
		return assessmentTime;
	}

	@Override
	public List<LearningResource> getChildResources() {
		List<LearningResource> childResources = new ArrayList<>();
		if (pages != null) {
			for (Page page : pages) {
				childResources.add(page);
			}
		}
		if (quiz != null) {
			childResources.add(quiz);
		}
		if (embeddedQuestions != null) {
			float seqNum = 0;
			for (Question embeddedQuestion : embeddedQuestions) {
				childResources.add(getEmbeddedQuestionLR(embeddedQuestion, seqNum));
				seqNum++;
			}
		}
		return childResources;
	}

	@Override
	public long getChildAssessmentTime(User student) {
		return getAssessmentTime(student);
	}

	@Override
	public long getChildLearningTime(User student) {
		long learningTime = 0;
		if (pages != null) {
			for (Page pg : pages) {
				learningTime += pg.getLearningTime(student);
			}
		}
		return learningTime;
	}

	@Override
	public long getTotalAssessmentTime() {
		long assessmentTime = 0;
		if (pages != null) {
			for (Page page : pages) {
				assessmentTime += page.getTotalAssessmentTime();
			}
		}
		if (quiz != null) {
			assessmentTime += quiz.getTotalAssessmentTime();
		}
		if (embeddedQuestions != null) {
			for (Question embeddedQuestion : embeddedQuestions) {
				assessmentTime += embeddedQuestion.getInProgressTotalAssessmentTime();
			}
		}
		return assessmentTime;
	}

	@Override
	public long getTotalLearningTime() {
		long learningTime = 0;
		if (learningActivities != null) {
			for (LearningActivity activity : learningActivities) {
				learningTime += activity.getTimeSpent();
			}
		}
		if (pages != null) {
			for (Page page : pages) {
				learningTime += page.getTotalLearningTime();
			}
		}
		return learningTime;
	}

	@Override
	public long getTotalChildAssessmentTime() {
		return getTotalAssessmentTime();
	}

	@Override
	public long getTotalChildLearningTime() {
		long learningTime = 0;
		if (pages != null) {
			for (Page page : pages) {
				learningTime += page.getTotalLearningTime();
			}
		}
		return learningTime;
	}

	@Override
	public boolean removeLearningActivity(LearningActivity activity) {
		return learningActivities.remove(activity);
	}

	@Override
	public boolean studentCompletedPages(User user) {
		boolean pageComplete = true;
		if (quiz != null) {
			pageComplete &= quiz.studentCompletedQuiz(user);
		}
		if (embeddedQuestions != null) {
			for (Question embeddedQuestion : embeddedQuestions) {
				pageComplete &= embeddedQuestion.studentCompletedQuestion(user);
			}
		}
		return pageComplete;
	}

	@Override
	public void addPage(Page page) {
		if (pages == null) {
			pages = new ArrayList<>();
		}
		pages.add(page);
	}

	@Override
	public List<Page> getPages() {
		return pages == null ? null : new ArrayList<>(pages);
	}

	@Override
	public boolean isPractice() {
		return isPractice;
	}

	@Override
	public boolean isAdjusted() {
		return false;
	}

	@Override
	public void setPractice(boolean bool) {
		isPractice = bool;
	}

	@Override
	public boolean hasPractice() {
		boolean toReturn = isPractice;
		if (quiz != null) {
			if (quiz.hasPractice()) {
				toReturn = true;
			}
		}

		if (embeddedQuestions != null) {
			for (Question question : embeddedQuestions) {
				if (question.isPractice()) {
					toReturn = true;
				}
			}
		}

		if (pages != null) {
			for (Page page : pages) {
				if (page.hasPractice()) {
					toReturn = true;
				}
			}
		}
		return toReturn;
	}

	@Override
	public boolean hasCredit() {
		boolean toReturn = !isPractice;
		if (quiz != null) {
			if (!quiz.hasPractice()) {
				toReturn = true;
			}
		}

		if (embeddedQuestions != null) {
			for (Question question : embeddedQuestions) {
				if (!question.isPractice()) {
					toReturn = true;
				}
			}
		}

		if (pages != null) {
			for (Page page : pages) {
				if (page.hasCredit()) {
					toReturn = true;
				}
			}
		}
		return toReturn;
	}

	@Override
	public Float getStudentLateSubmissionPoints(User student) {
		Float studentLateSubmissionPoints = null;
		if (quiz != null) {
			if (studentLateSubmissionPoints == null) {
				studentLateSubmissionPoints = 0.0f;
			}
			studentLateSubmissionPoints += quiz.getStudentLateSubmissionPoints(student);
		}
		if (embeddedQuestions != null) {
			for (Question q : embeddedQuestions) {
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
		return studentLateSubmissionPoints;
	}


	@Override
	public Long getLastActivityDate(User student) {
		Long lastActivityDate = null;
		if (learningActivities != null) {
			Long learningActivityLastActivityDate = null;
			for (LearningActivity activity : learningActivities) {
				if (activity.getPersonId().compareTo(student.getPersonId()) == 0) {
					learningActivityLastActivityDate = activity.getLastActivityDate();
					if(learningActivityLastActivityDate != null && (lastActivityDate == null || learningActivityLastActivityDate > lastActivityDate)) {
					
							lastActivityDate = activity.getLastActivityDate();
						}
				}
				
			}
		}
		if (pages != null) {
			for (Page page : pages) {
				Long pageLastActivityDate = page.getLastActivityDate(student);
				if(pageLastActivityDate != null && (lastActivityDate == null || pageLastActivityDate > lastActivityDate ) ){
					lastActivityDate = pageLastActivityDate;
				}
				
			}
		}
		if (quiz != null) {
			Long quizLastActivityDate = quiz.getLastActivityDate(student);
			if(quizLastActivityDate != null &&(lastActivityDate == null || quizLastActivityDate > lastActivityDate ) ){
				lastActivityDate = quizLastActivityDate;
			}
		}
		if (embeddedQuestions != null) {
			for (Question q : embeddedQuestions) {
				Long questionLastActivityDate = q.getLastActivityDate(student);
				if(questionLastActivityDate != null  && (lastActivityDate == null || questionLastActivityDate > lastActivityDate) ){
					lastActivityDate = questionLastActivityDate;
				}
			}
		}

	
		return lastActivityDate;
	}

}
