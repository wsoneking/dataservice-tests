package com.pearson.test.daalt.dataservice.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BasicAssignment implements Assignment {
	private static final String DUE_DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; //TODO: fix me
	
	private String id;
	private String externallyGeneratedId;
	private Float sequenceNumber;
	private String title;
	private List<Chapter> chapters;
	private Date dueDate;
	private boolean dueDatePassed;
	
	public BasicAssignment() {
		title = "Basic Assignment title";
		String randomUUID = UUID.randomUUID().toString();
		id = "SQE-Assign-" + randomUUID;
		dueDatePassed = false;
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
	public Float getSequenceNumber() {
		return sequenceNumber;
	}
	
//	@Override
//	public void setSequenceNumber(Float sequenceNumber) {
//		this.sequenceNumber = sequenceNumber;
//	}

	@Override
	public String getTitle() {
		return title;
	}	

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void addChapter(Chapter chapter) {
		if (chapters == null) {
			chapters = new ArrayList<>();
		}
		chapters.add(chapter);
	}

	@Override
	public List<Chapter> getChapters() {
		return chapters == null ? null : new ArrayList<>(chapters);
	}

	@Override
	public boolean containsChapter(String chapterId) {
		boolean containsChapter = false;
		if (chapters != null) {
			for (Chapter chapter : chapters) {
				if (chapter.getId().equalsIgnoreCase(chapterId)) {
					containsChapter = true;
				}
			}
		}
		return containsChapter;
	}

	@Override
	public float getPointsPossible() {
		float pointsPossible = 0f;
		if (chapters != null) {
			for (Chapter chapter : chapters) {
				pointsPossible += chapter.getAggregatedPointsPossible();
			}
		}
		return pointsPossible;
	}

	@Override
	public String getDueDateAsString() {
		return new SimpleDateFormat(DUE_DATE_FORMAT_STRING).format(dueDate);
	}

	@Override
	public Date getDueDate() {
		return dueDate;
	}

	@Override
	public void setDueDate(Date date) {
		this.dueDate = date;
		this.sequenceNumber = (float) date.getTime();			// TODO This fake sequenceNumber will cause failure for sorting. Offset in 2.3 has intermittent error. 
	}
	
	@Override
	public boolean isDueDatePassed() {
		return dueDatePassed;
	}

	@Override
	public void setDueDatePassed(boolean dueDatePassed) {
		this.dueDatePassed = dueDatePassed;
	}

	@Override
	public boolean studentCompletedAssignment(User stud) {
		boolean assigmentComplete = true;
		if (chapters != null) {
			for (Chapter chapter : chapters) {
				assigmentComplete &= chapter.studentCompletedChapter(stud);
			}
		}
		return assigmentComplete;
	}
	
	@Override
	public float getPointsEarnedFinal(User student) {
		float pointsEarned = 0f;
		if (chapters != null) {
			for (Chapter chapter : chapters) {
				pointsEarned += chapter.getPointsEarnedFinal(student);
			}
		}
		return pointsEarned;
	}

	@Override
	public long getAssessmentTime(User student) {
		long assessmentTime = 0;
		if (chapters != null) {
			for (Chapter chapter : chapters) {
				assessmentTime += chapter.getAssessmentTime(student);
			}
		}
		return assessmentTime;
	}

	@Override
	public long getLearningTime(User student) {
		long learningTime = 0;
		if (chapters != null) {
			for (Chapter chapter : chapters) {
				learningTime += chapter.getLearningTime(student);
			}
		}
		return learningTime;
	}

	@Override
	public String getStructure() {
		StringBuilder structure = new StringBuilder();
		structure.append("Assignment structure...")
			.append("\n").append("...Assignment id : ").append(id);
		if (chapters != null) {
			for (Chapter chap : chapters) {
				structure.append("\n").append("......Chapter id : ").append(chap.getLearningResourceId());
				Quiz chapQuiz = chap.getChapterQuiz();
				if (chapQuiz != null) {
					structure.append("\n").append(".........Chapter Quiz assessment id : ").append(chapQuiz.getId());
					structure.append("\n").append(".........Chapter Quiz learning resource id : ").append(chapQuiz.getLearningResourceId());
					for (Question question : chapQuiz.getQuestions()) {
						structure.append("\n").append("............Chapter Quiz Question id : ").append(question.getId());
						if(question.getSubQuestions() != null) {
							for (SubQuestion subq : question.getSubQuestions()) {
								structure.append("\n").append("...............SubQuestion id : ").append(subq.getId());
								for (Answer answer : subq.getAnswers()) {
									structure.append("\n").append("..................Answer id : ").append(answer.getId());
									if (answer.isCorrectAnswer()) {
										structure.append(" - correct");
									}
								}
							}
						}
					}
					if (chapQuiz.getNestedQuizzes() != null) {
						for (Quiz nestedQuiz : chapQuiz.getNestedQuizzes()) {
							structure.append("\n").append("............Nested Quiz assessment id : ").append(nestedQuiz.getId());
							structure.append("\n").append("............Nested Quiz learning resource id : ").append(nestedQuiz.getLearningResourceId());
							for (Question question : nestedQuiz.getQuestions()) {
								structure.append("\n").append("...............Nested Quiz Question id : ").append(question.getId());
								if(question.getSubQuestions() != null) {
									for (SubQuestion subq : question.getSubQuestions()) {
										structure.append("\n").append("..................SubQuestion id : ").append(subq.getId());
										for (Answer answer : subq.getAnswers()) {
											structure.append("\n").append(".....................Answer id : ").append(answer.getId());
											if (answer.isCorrectAnswer()) {
												structure.append(" - correct");
											}
										}
									}
								}
							}
						}
					}
				}
				if (chap.getChapterSections() != null) {
					for (ChapterSection chapSec : chap.getChapterSections()) {
						structure.append("\n").append(".........Chapter Section id : ").append(chapSec.getLearningResourceId());
						Quiz chapSecQuiz = chapSec.getChapterSectionQuiz();
						if (chapSecQuiz != null) {
							structure.append("\n").append("............Chapter Section Quiz assessment id : ").append(chapSecQuiz.getId());
							structure.append("\n").append("............Chapter Section Quiz learning resource id : ").append(chapSecQuiz.getLearningResourceId());
							for (Question question : chapSecQuiz.getQuestions()) {
								structure.append("\n").append("...............Chapter Section Quiz Question id : ").append(question.getId());
								if(question.getSubQuestions() != null) {
									for (SubQuestion subq : question.getSubQuestions()) {
										structure.append("\n").append("..................SubQuestion id : ").append(subq.getId());
										for (Answer answer : subq.getAnswers()) {
											structure.append("\n").append(".....................Answer id : ").append(answer.getId());
											if (answer.isCorrectAnswer()) {
												structure.append(" - correct");
											}
										}
									}
								}
							}
						}
						if (chapSec.getPages() != null) {
							for (Page page : chapSec.getPages()) {
								structure.append("\n").append("............Page id : ").append(page.getLearningResourceId());
								if(page.getPages() != null){
									for (Page pg : page.getPages()){
										structure.append("\n").append("...............Page id : ").append(pg.getLearningResourceId());
									}
								}
								if (page.getEmbeddedQuestions() != null) {
									for (Question question : page.getEmbeddedQuestions()) {
										structure.append("\n").append("...............Embedded Question assessment id : ").append(question.getAssessmentId());
										structure.append("\n").append("..................Embedded Question id : ").append(question.getId());
										if(question.getSubQuestions() != null) {
											for (SubQuestion subq : question.getSubQuestions()) {
												structure.append("\n").append("..................SubQuestion id : ").append(subq.getId());
												for (Answer answer : subq.getAnswers()) {
													structure.append("\n").append(".....................Answer id : ").append(answer.getId());
													if (answer.isCorrectAnswer()) {
														structure.append(" - correct");
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return structure.toString();
	}
	
	@Override
	public List<LearningResource> getAllLearningResources() {
		List<LearningResource> resourceList = new ArrayList<>();
		if (chapters != null) {
			for (Chapter chap : chapters) {
				resourceList.add(chap);
				Quiz chapQuiz = chap.getChapterQuiz();
				if (chapQuiz != null) {
					resourceList.add(chapQuiz);
					if (chapQuiz.getNestedQuizzes() != null) {
						for (Quiz nestedQuiz : chapQuiz.getNestedQuizzes()) {
							resourceList.add(nestedQuiz);
						}
					}
				}
				if (chap.getChapterSections() != null) {
					for (ChapterSection chapSec : chap.getChapterSections()) {
						resourceList.add(chapSec);
						if (chapSec.getChapterSectionQuiz() != null) {
							resourceList.add(chapSec.getChapterSectionQuiz());
						}
						if (chapSec.getPages() != null) {
							for (Page page : chapSec.getPages()) {
								resourceList.add(page);
								if(page.getPages() != null){
									for (Page pg : page.getPages()){
										resourceList.add(pg);
									}
								}
								if(page.getQuiz() != null){
									resourceList.add(page.getQuiz());
								}
								if (page.getEmbeddedQuestions() != null) {
									int seqNum = 0;
									for (Question question : page.getEmbeddedQuestions()) {
										resourceList.add(page.getEmbeddedQuestionLR(question, seqNum));
										seqNum++;
									}
								}
							}
						}
					}
				}
			}
		}
		return resourceList;
	}
	
	@Override
	public LearningResource getDeepestLearningResource() {
		LearningResource deepestLeaf = null;
		int deepestLevelFound = 0;
		if (chapters != null) {
			for (Chapter chap : chapters) {
				if (deepestLevelFound < 1) {
					deepestLeaf = chap;
					deepestLevelFound = 1;
				}
				Quiz chapQuiz = chap.getChapterQuiz();
				if (chapQuiz != null) {
					if (deepestLevelFound < 2) {
						deepestLeaf = chapQuiz;
						deepestLevelFound = 2;
					}
					if (chapQuiz.getNestedQuizzes() != null) {
						for (Quiz nestedQuiz : chapQuiz.getNestedQuizzes()) {
							if (deepestLevelFound < 3) {
								deepestLeaf = nestedQuiz;
								deepestLevelFound = 3;
							}
						}
					}
				}
				if (chap.getChapterSections() != null) {
					for (ChapterSection chapSec : chap.getChapterSections()) {
						if (deepestLevelFound < 2) {
							deepestLeaf = chapSec;
							deepestLevelFound = 2;
						}
						if (chapSec.getChapterSectionQuiz() != null) {
							if (deepestLevelFound < 3) {
								deepestLeaf = chapSec.getChapterSectionQuiz();
								deepestLevelFound = 3;
							}
						}
						if (chapSec.getPages() != null) {
							for (Page page : chapSec.getPages()) {
								if (deepestLevelFound < 3) {
									deepestLeaf = page;
									deepestLevelFound = 3;
								}
								if(page.getPages() != null){
									for (Page pg : page.getPages()){
										if (deepestLevelFound < 4) {
											deepestLeaf = page;
											deepestLevelFound = 4;
										}
									}
								}
								if(page.getQuiz() != null){
									if (deepestLevelFound < 4) {
										deepestLeaf = page.getQuiz();
										deepestLevelFound = 4;
									}
								}
								if (page.getEmbeddedQuestions() != null) {
									int seqNum = 0;
									for (Question question : page.getEmbeddedQuestions()) {
										if (deepestLevelFound < 4) {
											deepestLeaf = page.getEmbeddedQuestionLR(question, seqNum);
											deepestLevelFound = 4;
										}
										seqNum++;
									}
								}
							}
						}
					}
				}
			}
		}
		return deepestLeaf;
	}

	@Override
	public boolean removeChapter(Chapter chapter) {
		boolean result = false;
		for(Chapter chap : new ArrayList<Chapter>(chapters)){
			if(chap.equals(chapter)){
				result = chapters.remove(chap);
			}
		}
		return result;
	}

	@Override
	public float getPracticePointsPossible() {
		float pointsPossible = 0f;
		if (chapters != null) {
			for (Chapter chapter : chapters) {
				pointsPossible += chapter.getAggregatedPracticePointsPossible();
			}
		}
		return pointsPossible;
	}

	@Override
	public float getPracticePointsEarnedFinal(User student) {
		float pointsEarned = 0f;
		if (chapters != null) {
			for (Chapter chapter : chapters) {
				pointsEarned += chapter.getPracticePointsEarnedFinal(student);
			}
		}
		return pointsEarned;
	}

	@Override
	public boolean hasPractice() {
		boolean toReturn = false;
		
		if (chapters != null) {
			for (Chapter chap : chapters) {
				if (chap.hasPractice()) {
					toReturn = true;
				}
			}
		}

		return toReturn;
	}

	@Override
	public boolean hasCredit() {
		boolean toReturn = false;
		
		if (chapters != null) {
			for (Chapter chap : chapters) {
				if (chap.hasCredit()) {
					toReturn = true;
				}
			}
		}

		return toReturn;
	}

	@Override
	public void setAllTargetIdsTrue() {
		if (chapters != null) {
			for (Chapter chap : chapters) {
				Quiz chapQuiz = chap.getChapterQuiz();
				if (chapQuiz != null) {
					for (Question question : chapQuiz.getQuestions()) {
						for (SubQuestion subQuestion : question.getSubQuestions()) {
							subQuestion.setId("true");
						}
					}
					if (chapQuiz.getNestedQuizzes() != null) {
						for (Quiz nestedQuiz : chapQuiz.getNestedQuizzes()) {
							for (Question question : nestedQuiz.getQuestions()) {
								for (SubQuestion subQuestion : question.getSubQuestions()) {
									subQuestion.setId("true");
								}
							}
						}
					}
				}
				if (chap.getChapterSections() != null) {
					for (ChapterSection chapSec : chap.getChapterSections()) {
						if (chapSec.getChapterSectionQuiz() != null) {
							for (Question question : chapSec.getChapterSectionQuiz().getQuestions()) {
								for (SubQuestion subQuestion : question.getSubQuestions()) {
									subQuestion.setId("true");
								}
							}
						}
						if (chapSec.getPages() != null) {
							for (Page page : chapSec.getPages()) {
								if(page.getQuiz() != null){
									for (Question question : page.getQuiz().getQuestions()) {
										for (SubQuestion subQuestion : question.getSubQuestions()) {
											subQuestion.setId("true");
										}
									}
								}
								if (page.getEmbeddedQuestions() != null) {
									for (Question question : page.getEmbeddedQuestions()) {
										for (SubQuestion subQuestion : question.getSubQuestions()) {
											subQuestion.setId("true");
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Long getLastActivityDate(User student) {
		Long lastActivityDate = null;
		if (chapters != null) {
			for (Chapter chapter : chapters) {
				Long chapterLastActivityDate =  chapter.getLastActivityDate(student);
				if (chapterLastActivityDate != null && (lastActivityDate == null || chapterLastActivityDate > lastActivityDate)) {
					lastActivityDate = chapterLastActivityDate;
				}
			}
		}
		return lastActivityDate;
	}
}
