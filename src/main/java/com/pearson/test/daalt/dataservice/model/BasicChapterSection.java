package com.pearson.test.daalt.dataservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicChapterSection implements ChapterSection {
	private String id;
	private String externallyGeneratedId;
	private Float learningResourceSequenceNumber;
	private List<Page> pages;
	private Quiz chapterSectionQuiz;
	public boolean isPractice;

	public BasicChapterSection() {
		String randomUUID = UUID.randomUUID().toString();
		isPractice = true;
		id = "SQE-ChapSec-" + randomUUID;
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
	public Float getLearningResourceSequenceNumber() {
		return learningResourceSequenceNumber;
	}

	@Override
	public void setLearningResourceSequenceNumber(Float learningResourceSequenceNumber) {
		this.learningResourceSequenceNumber = learningResourceSequenceNumber;
	}

	@Override
	public String getLearningResourceTitle() {
		return "Chapter Section Title";
	}

	@Override
	public String getLearningResourceType() {
		return LearningResourceType.CHAPTER_SECTION.value;
	}

	@Override
	public String getLearningResourceSubType() {
		return LearningResourceSubType.CHAPTER_SECTION.value;
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
	public Quiz getChapterSectionQuiz() {
		return chapterSectionQuiz;
	}

	@Override
	public void setChapterSectionQuiz(Quiz chapterSectionQuiz) {
		this.chapterSectionQuiz = chapterSectionQuiz;
	}

	@Override
	public float getAggregatedPointsPossible() {
		Float pointsPossible = 0f;
		if (pages != null) {
			for (Page pg : pages) {
				pointsPossible += pg.getAggregatedPointsPossible();
			}
		}
		if (chapterSectionQuiz != null) {
			pointsPossible += chapterSectionQuiz.getAggregatedPointsPossible();
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
		if (pages != null) {
			for (Page pg : pages) {
				pointsPossible += pg.getAggregatedPracticePointsPossible();
			}
		}
		if (chapterSectionQuiz != null) {
			pointsPossible += chapterSectionQuiz.getAggregatedPracticePointsPossible();
		}
		return pointsPossible;
	}

	@Override
	public boolean studentCompletedChapterSection(User stud) {
		boolean chapterSectionComplete = true;
		if (pages != null) {
			for (Page pg : pages) {
				chapterSectionComplete &= pg.studentCompletedPages(stud);
			}
		}
		if (chapterSectionQuiz != null) {
			chapterSectionComplete &= chapterSectionQuiz.studentCompletedQuiz(stud);
		}
		return chapterSectionComplete;
	}

	@Override
	public boolean studentCompleted(User stud) {
		return studentCompletedChapterSection(stud);
	}

	@Override
	public float getPointsEarnedFinal(User student) {
		float pointsEarned = 0;
		if (pages != null) {
			for (Page page : pages) {
				pointsEarned += page.getPointsEarnedFinal(student);
			}
		}
		if (chapterSectionQuiz != null) {
			pointsEarned += chapterSectionQuiz.getPointsEarnedFinal(student);
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
		if (chapterSectionQuiz != null) {
			pointsEarned += chapterSectionQuiz.getTotalPointsEarnedFinal();
		}
		return pointsEarned;
	}

	@Override
	public float getPracticePointsEarnedFinal(User student) {
		float pointsEarned = 0;
		if (pages != null) {
			for (Page page : pages) {
				pointsEarned += page.getPracticePointsEarnedFinal(student);
			}
		}
		if (chapterSectionQuiz != null) {
			pointsEarned += chapterSectionQuiz.getPracticePointsEarnedFinal(student);
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
		if (chapterSectionQuiz != null) {
			pointsEarned += chapterSectionQuiz.getTotalPracticePointsEarnedFinal();
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
		if (chapterSectionQuiz != null) {
			assessmentTime += chapterSectionQuiz.getAssessmentTime(student);
		}
		return assessmentTime;
	}

	@Override
	public long getLearningTime(User student) {
		long learningTime = 0;
		if (pages != null) {
			for (Page page : pages) {
				learningTime += page.getLearningTime(student);
			}
		}
		if (chapterSectionQuiz != null) {
			learningTime += chapterSectionQuiz.getLearningTime(student);
		}
		return learningTime;
	}

	@Override
	public List<LearningResource> getChildResources() {
		List<LearningResource> childResources = new ArrayList<>();
		if (pages != null) {
			for (Page page : pages) {
				childResources.add(page);
			}
		}
		if (chapterSectionQuiz != null) {
			childResources.add(chapterSectionQuiz);
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
		if (pages != null) {
			for (Page page : pages) {
				assessmentTime += page.getTotalAssessmentTime();
			}
		}
		if (chapterSectionQuiz != null) {
			assessmentTime += chapterSectionQuiz.getTotalAssessmentTime();
		}
		return assessmentTime;
	}

	@Override
	public long getTotalLearningTime() {
		long learningTime = 0;
		if (pages != null) {
			for (Page page : pages) {
				learningTime += page.getTotalLearningTime();
			}
		}
		if (chapterSectionQuiz != null) {
			learningTime += chapterSectionQuiz.getTotalLearningTime();
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
		if (chapterSectionQuiz != null && chapterSectionQuiz.isAdjusted()) {
			return true;
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
		if (chapterSectionQuiz != null) {
			if (chapterSectionQuiz.hasPractice()) {
				toReturn = true;
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
		if (chapterSectionQuiz != null) {
			if (chapterSectionQuiz.hasCredit()) {
				toReturn = true;
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
	public List<Question> getSeededQuestions() {
		ArrayList<Question> toReturn = new ArrayList<Question>();
		if (chapterSectionQuiz != null) {
			for (Question q : chapterSectionQuiz.getQuestions()) {
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
		if (pages != null) {
			for(Page page : pages){
				Float pageLateSubmissionPoints = page.getStudentLateSubmissionPoints(student);
				if(pageLateSubmissionPoints != null) {
					if (studentLateSubmissionPoints == null) {
						studentLateSubmissionPoints = 0f;
					}
					studentLateSubmissionPoints += pageLateSubmissionPoints;
				}
			}
		}
		if (chapterSectionQuiz != null) {
			Float chapterSectionQuizLateSubmissionPoints = chapterSectionQuiz.getStudentLateSubmissionPoints(student);
			if (chapterSectionQuizLateSubmissionPoints != null) {
				if (studentLateSubmissionPoints == null) {
					studentLateSubmissionPoints = 0f;
				}
				studentLateSubmissionPoints += chapterSectionQuizLateSubmissionPoints;
			}
		}
		return studentLateSubmissionPoints;
	}

	@Override
	public Long getLastActivityDate(User student) {
		Long lastActivityDate = null;
		if (pages != null) {
			for(Page page : pages){
				Long pageLastActivityDate = page.getLastActivityDate(student);
				if (pageLastActivityDate != null  && (lastActivityDate == null || pageLastActivityDate > lastActivityDate)) {
					lastActivityDate = pageLastActivityDate;
				}
			}
		}
		if (chapterSectionQuiz != null) {
			Long chapterSectionQuizLastActivityDate = chapterSectionQuiz.getLastActivityDate(student);
			if (chapterSectionQuizLastActivityDate != null  && (lastActivityDate == null || chapterSectionQuizLastActivityDate > lastActivityDate)) {
				lastActivityDate = chapterSectionQuizLastActivityDate;
			}
		}
		return lastActivityDate;
	}
}
