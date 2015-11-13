package com.pearson.test.daalt.dataservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicChapter implements Chapter {
	private String id;
	private String externallyGeneratedId;
	private Float learningResourceSequenceNumber;
	private List<ChapterSection> chapterSections;
	private Quiz chapterQuiz;
	public boolean isPractice;

	public BasicChapter() {
		String randomUUID = UUID.randomUUID().toString();
		isPractice = true;
		id = "SQE-Chap-" + randomUUID;
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
		//not applicable - do nothing
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
	public String getLearningResourceTitle() {
		return "Chapter Title";
	}

	@Override
	public String getLearningResourceType() {
		return LearningResourceType.CHAPTER.value;
	}

	@Override
	public String getLearningResourceSubType() {
		return LearningResourceSubType.CHAPTER.value;
	}

	@Override
	public void addChapterSection(ChapterSection section) {
		if (chapterSections == null) {
			chapterSections = new ArrayList<>();
		}
		chapterSections.add(section);
	}

	@Override
	public List<ChapterSection> getChapterSections() {
		return chapterSections == null ? null : new ArrayList<>(chapterSections);
	}

	@Override
	public Quiz getChapterQuiz() {
		return chapterQuiz;
	}

	@Override
	public void setChapterQuiz(Quiz chapterQuiz) {
		this.chapterQuiz = chapterQuiz;
	}
	
	@Override
	public boolean studentCompletedChapter(User stud) {
		boolean chapterComplete = true;
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				chapterComplete &= chapterSection.studentCompletedChapterSection(stud);
			}
		}
		if (chapterQuiz != null) {
			chapterComplete &= chapterQuiz.studentCompletedQuiz(stud);
		}
		return chapterComplete;
	}
	
	@Override
	public boolean studentCompleted(User stud) {
		return studentCompletedChapter(stud);
	}

	@Override
	public float getAggregatedPointsPossible() {
		Float pointsPossible = 0f;
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				pointsPossible += chapterSection.getAggregatedPointsPossible();
			}
		}
		if (chapterQuiz != null) {
			pointsPossible += chapterQuiz.getAggregatedPointsPossible();
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
	public float getAggregatedPracticePointsPossible() {
		Float pointsPossible = 0f;
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				pointsPossible += chapterSection.getAggregatedPracticePointsPossible();
			}
		}
		if (chapterQuiz != null) {
			pointsPossible += chapterQuiz.getAggregatedPracticePointsPossible();
		}
		return pointsPossible;
	}
	
	@Override
	public float getPointsEarnedFinal(User student) {
		float pointsEarned = 0f;
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				pointsEarned += chapterSection.getPointsEarnedFinal(student);
			}
		}
		if (chapterQuiz != null) {
			pointsEarned += chapterQuiz.getPointsEarnedFinal(student);
		}
		return pointsEarned;
	}
	
	@Override
	public float getTotalPointsEarnedFinal() {
		float pointsEarned = 0f;
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				pointsEarned += chapterSection.getTotalPointsEarnedFinal();
			}
		}
		if (chapterQuiz != null) {
			pointsEarned += chapterQuiz.getTotalPointsEarnedFinal();
		}
		return pointsEarned;
	}
	
	@Override
	public float getPracticePointsEarnedFinal(User student) {
		float pointsEarned = 0f;
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				pointsEarned += chapterSection.getPracticePointsEarnedFinal(student);
			}
		}
		if (chapterQuiz != null) {
			pointsEarned += chapterQuiz.getPracticePointsEarnedFinal(student);
		}
		return pointsEarned;
	}

	@Override
	public float getTotalPracticePointsEarnedFinal() {
		float pointsEarned = 0f;
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				pointsEarned += chapterSection.getTotalPracticePointsEarnedFinal();
			}
		}
		if (chapterQuiz != null) {
			pointsEarned += chapterQuiz.getTotalPracticePointsEarnedFinal();
		}
		return pointsEarned;
	}

	@Override
	public long getAssessmentTime(User student) {
		long assessmentTime = 0;
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				assessmentTime += chapterSection.getAssessmentTime(student);
			}
		}
		if (chapterQuiz != null) {
			assessmentTime += chapterQuiz.getAssessmentTime(student);
		}
		return assessmentTime;
	}

	@Override
	public long getLearningTime(User student) {
		long learningTime = 0;
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				learningTime += chapterSection.getLearningTime(student);
			}
		}
		if (chapterQuiz != null) {
			learningTime += chapterQuiz.getLearningTime(student);
		}
		return learningTime;
	}

	@Override
	public List<LearningResource> getChildResources() {
		List<LearningResource> childResources = new ArrayList<>();
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				childResources.add(chapterSection);
			}
		}
		if (chapterQuiz != null) {
			childResources.add(chapterQuiz);
		}
		return childResources;
	}

	@Override
	public long getChildAssessmentTime(User student) {
		return getAssessmentTime(student);
	}

	@Override
	public long getChildLearningTime(User student) {
		return getLearningTime(student);
	}

	@Override
	public long getTotalAssessmentTime() {
		long assessmentTime = 0;
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				assessmentTime += chapterSection.getTotalAssessmentTime();
			}
		}
		if (chapterQuiz != null) {
			assessmentTime += chapterQuiz.getTotalAssessmentTime();
		}
		return assessmentTime;
	}

	@Override
	public long getTotalLearningTime() {
		long learningTime = 0;
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				learningTime += chapterSection.getTotalLearningTime();
			}
		}
		if (chapterQuiz != null) {
			learningTime += chapterQuiz.getTotalLearningTime();
		}
		return learningTime;
	}

	@Override
	public long getTotalChildAssessmentTime() {
		return getTotalAssessmentTime();
	}

	@Override
	public long getTotalChildLearningTime() {
		return getTotalLearningTime();
	}

	@Override
	public boolean isPractice() {
		return isPractice;
	}

	@Override
	public boolean isAdjusted() {
		if(chapterQuiz.isAdjusted()) {
			return true;
		}
		for (ChapterSection sec : chapterSections){
			if (sec.isAdjusted()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void setPractice(boolean bool) {
		isPractice = bool;
	}


	@Override
	public boolean hasPractice() {
		boolean toReturn = isPractice;
		
		if (chapterQuiz != null) {
			if (chapterQuiz.hasPractice()) {
				toReturn = true;
			}
		}
		if (chapterSections != null) {
			for (ChapterSection chapSec : chapterSections) {
				if (chapSec.hasPractice()) {
					toReturn = true;
				}
			}
		}
		return toReturn;
	}

	@Override
	public boolean hasCredit() {
		boolean toReturn = !isPractice;
		
		if (chapterQuiz != null) {
			if (chapterQuiz.hasCredit()) {
				toReturn = true;
			}
		}
		if (chapterSections != null) {
			for (ChapterSection chapSec : chapterSections) {
				if (chapSec.hasCredit()) {
					toReturn = true;
				}
			}
		}			
		
		return toReturn;
	}

	@Override
	public Float getStudentLateSubmissionPoints(User student) {
		Float studentLateSubmissionPoints = null;
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				Float chapterSectionLateSubmissionPoints = chapterSection.getStudentLateSubmissionPoints(student);
				if(chapterSectionLateSubmissionPoints != null) {
					if (studentLateSubmissionPoints == null) {
						studentLateSubmissionPoints = 0f;
					}
					studentLateSubmissionPoints += chapterSectionLateSubmissionPoints;
				}
			}
		}
		if (chapterQuiz != null) {
			Float chapterQuizLateSubmissionPoints = chapterQuiz.getStudentLateSubmissionPoints(student);
			if (studentLateSubmissionPoints == null) {
				studentLateSubmissionPoints = 0f;
			}
			studentLateSubmissionPoints += chapterQuizLateSubmissionPoints;
		}
		return studentLateSubmissionPoints;
	}

	@Override
	public Long getLastActivityDate(User student) {
		Long lastActivityDate = null;
		if (chapterSections != null) {
			for(ChapterSection chapterSection : chapterSections){
				Long chapterSectionLastActivityDate = chapterSection.getLastActivityDate(student);
				if (chapterSectionLastActivityDate != null && (lastActivityDate == null || chapterSectionLastActivityDate > lastActivityDate)) {
					lastActivityDate = chapterSectionLastActivityDate;
				}
			}
		}
		if (chapterQuiz != null) {
			Long chapterQuizLastActivityDate = chapterQuiz.getLastActivityDate(student);
			if (chapterQuizLastActivityDate != null && (lastActivityDate == null || chapterQuizLastActivityDate > lastActivityDate)) {
				lastActivityDate = chapterQuizLastActivityDate;
			}
		}
		return lastActivityDate;
	}
	
}
