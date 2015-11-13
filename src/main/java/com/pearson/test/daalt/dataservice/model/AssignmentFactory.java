package com.pearson.test.daalt.dataservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AssignmentFactory {
	/**
	 * This approach to an AssignmentFactory will invite monolithic chaos, but at least it will be isolated,
	 * and we can make them re-usable monoliths.
	 * 
	 * FUTURE: Need a more dynamic approach to defining Assignments.
	 */
	public enum AssignmentLibrary {READING_ONLY, 
									CH_SECTION_QUIZ_AND_CH_QUIZ_AND_READING,
									CHAPTER_CH_SECTION_QUIZ_AND_READING_X2,
									CHAPTER_CH_SECTION_QUIZ_AND_READING,
									SECTION_READING_EMBEDDED_QUIZ,
									NESTED_READING_PAGES_ONLY,
									NESTED_QUIZZES,
									CHAPTER_QUIZ_ONLY,
									CHAPTER_QUIZ_ONLY_GENERIC,
									CHAPTER_QUIZ_GENERIC_MULTIVALUE_COMBINE,
									BIG_CHAPTER_QUIZ,
									ONE_QUIZ_WITH_SIX_NEW_TYPES_OF_QUESTIONS,
									ASSIGNMENT_WITH_FILL_IN_THE_BLANK,
									ONE_QUIZ_WITH_WRITING_SPACE_QUESTION,
									TWO_QUIZZES_WITH_WRITING_SPACE_QUESTION,
									ONE_QUIZ_WITH_SHARED_WRITING_QUESTION,
									ONE_QUIZ_WITH_JOURNAL_WRITING_QUESTION,
									NESTED_QUIZZES_UNDER_PRACTICE,
									CHAPTER_SECTION_QUIZ_WITHOUT_POSSIBLE_ANSWERS,
									REALLY_BIG_ASSIGNMENT,
									CH_SECTION_QUIZ_AND_CH_QUIZ_AND_READING_ONLY_PRACTICE,
									THREE_CHAPTERS, 
									CHAPTER_QUIZ_WITH_FILL_IN_THE_BLANK_AND_NUMERIC,
									CHAPTER_QUIZ_WITH_FILL_IN_THE_BLANK_AND_NUMERIC_ONLY};
							
	public Assignment getAssignmentByEnum(AssignmentLibrary selection) throws InvalidAssignmentParametersException {
		Assignment toReturn = null;
		switch (selection) {
			case READING_ONLY : toReturn = getReadingOnlyAssignment();
			break;
			case CH_SECTION_QUIZ_AND_CH_QUIZ_AND_READING : toReturn = getChSecQuizAndChQuizAndReading();
			break;
			case CHAPTER_CH_SECTION_QUIZ_AND_READING_X2 : toReturn = getChSecQuizAndReadingX2();
			break;
			case CHAPTER_CH_SECTION_QUIZ_AND_READING : toReturn = getChSecQuizAndReading();
			break;
			case SECTION_READING_EMBEDDED_QUIZ : toReturn = getSecReadingEmbeddedQuiz();
			break;
			case NESTED_READING_PAGES_ONLY : toReturn = getNestedReadingPagesOnly();
			break;
			case NESTED_QUIZZES : toReturn = getNestedQuizzes();
			break;
			case ONE_QUIZ_WITH_SIX_NEW_TYPES_OF_QUESTIONS : toReturn = getSixNewTypeOfQuestionsInChapQuiz();
			break;
			case ASSIGNMENT_WITH_FILL_IN_THE_BLANK : toReturn = getAssignmentWithFillInTheBlankQuestions();
			break;
			case BIG_CHAPTER_QUIZ : toReturn = getBigChapterQuiz();
			break;
			case CHAPTER_QUIZ_ONLY : toReturn = getChQuizOnly();
			break;
			case CHAPTER_QUIZ_ONLY_GENERIC : toReturn = getChQuizOnlyGeneric();
			break;
			case CHAPTER_QUIZ_GENERIC_MULTIVALUE_COMBINE : toReturn = getChQuizGenericMultiValueCombine();
			break;
			case ONE_QUIZ_WITH_WRITING_SPACE_QUESTION : toReturn = getOneQuizWithWritingSpaceQuestion();
			break;
			case TWO_QUIZZES_WITH_WRITING_SPACE_QUESTION : toReturn = getTwoQuizzesWithWritingSpaceQuestion();
			break;
			case ONE_QUIZ_WITH_SHARED_WRITING_QUESTION : toReturn = getOneQuizWithSimpleWritingSharedQuiz();
			break;
			case ONE_QUIZ_WITH_JOURNAL_WRITING_QUESTION : toReturn = getOneQuizWithSimpleWritingJournalQuiz();
			break;
			case NESTED_QUIZZES_UNDER_PRACTICE : toReturn = getNestedQuizzesUnderPractice();
			break;
			case CHAPTER_SECTION_QUIZ_WITHOUT_POSSIBLE_ANSWERS : toReturn = getAlternateAddAssignment();
			break;
			case REALLY_BIG_ASSIGNMENT : toReturn = getReallyBigAssignment();
			break;
			case CH_SECTION_QUIZ_AND_CH_QUIZ_AND_READING_ONLY_PRACTICE : toReturn = getChSecQuizAndChQuizAndReadingAllPractice();
			break;
			case CHAPTER_QUIZ_WITH_FILL_IN_THE_BLANK_AND_NUMERIC : toReturn = getQuizWithFillInTheBlankAndNumericQuestions();
			break;
			case CHAPTER_QUIZ_WITH_FILL_IN_THE_BLANK_AND_NUMERIC_ONLY : toReturn = getQuizWithFillInTheBlankAndNumericQuestionsOnly();
			break;
			default : throw new InvalidAssignmentParametersException();
		}
		return toReturn;
	}	
	
	public List<Assignment> getTwoAssignmentsWithSharedLearningResources(AssignmentLibrary selection) throws InvalidAssignmentParametersException {
		List<Assignment> toReturn = null;
		switch (selection) {
			case THREE_CHAPTERS : toReturn = getTwoAssignmentsWithThreeChapters();
			break;
			default : throw new InvalidAssignmentParametersException();
		}
		return toReturn;
	}	
	
	private List<Assignment> getTwoAssignmentsWithThreeChapters() {
		List<Assignment> toReturn = new ArrayList<>();
		
		//instantiate Assignment_01
		Assignment assignment01 = new BasicAssignment();
		
		//add Assignment_01 to list
		toReturn.add(assignment01);
		
		//instantiate Chapter_01
		Chapter chapter01 = new BasicChapter();
		chapter01.setLearningResourceSequenceNumber(0f);
		
		//add Chapter_01 to Assignment_01
		assignment01.addChapter(chapter01);
		
		//instantiate Assessment_01 & add Questions & Answers
		Quiz assessment01 = generateMultipleChoiceQuiz("Chapter Quiz", 3);
		assessment01.setLearningResourceSequenceNumber(1f);
		assessment01.setChapterQuiz(true);
		
		//add Assessment_01 to Chapter_01
		chapter01.setChapterQuiz(assessment01);
		
		//instantiate ChapterSection_01 & add Page_01 and Assessment_02
		ChapterSection chapterSection01 = new BasicChapterSection();
		chapterSection01.setLearningResourceSequenceNumber(2f);
		
		//add ChapterSection_01 to Chapter_01
		chapter01.addChapterSection(chapterSection01);
		
		//instantiate Page_01
		Page page01 = new BasicReadingPage();
		page01.setLearningResourceSequenceNumber(3f);
		
		//add Page_01 to ChapterSection_01
		chapterSection01.addPage(page01);
		
		//Instantiate Assessment_02 & add Questions
		Quiz assessment02 = generateMultipleChoiceQuiz("Chapter Section Quiz", 3);
		assessment02.setLearningResourceSequenceNumber(4f);
		
		//add Assessment_02 to ChapterSection_01
		chapterSection01.setChapterSectionQuiz(assessment02);
			
		//instantiate Chapter_02
		Chapter chapter02 = new BasicChapter();
		chapter02.setLearningResourceSequenceNumber(5f);
		
		//add Chapter_02 to Assignment_01
		assignment01.addChapter(chapter02);
		
		//instantiate Assessment_03 & add Questions & Answers
		Quiz assessment03 = generateMultipleChoiceQuiz("Chapter Quiz", 3);
		assessment03.setLearningResourceSequenceNumber(6f);
		assessment03.setChapterQuiz(true);
		
		//add Assessment_03 to Chapter_02
		chapter02.setChapterQuiz(assessment03);
		
		//instantiate Chapter_03
		Chapter chapter03 = new BasicChapter();
		chapter03.setLearningResourceSequenceNumber(7f);
		
		//add Chapter_03 to Assignment_01
		assignment01.addChapter(chapter03);
		
		//instantiate ChapterSection_02 & add Page_02 and Page_03
		ChapterSection chapterSection02 = new BasicChapterSection();
		chapterSection02.setLearningResourceSequenceNumber(8f);
		
		//add ChapterSection_02 to Chapter_03
		chapter03.addChapterSection(chapterSection02);
		
		//instantiate Page_02
		Page page02 = new BasicReadingPage();
		page02.setLearningResourceSequenceNumber(9f);
		
		//add Page_02 to ChapterSection_02
		chapterSection02.addPage(page02);
		
		//instantiate Page_03
		Page page03 = new BasicReadingPage();
		page03.setLearningResourceSequenceNumber(10f);
		
		//add Page_03 to ChapterSection_02
		chapterSection02.addPage(page03);
		
		//add Embedded_Question to Page_03
		String randomUUID = UUID.randomUUID().toString();
		String assessment04Id = "SQE-Assess-" + randomUUID;
		
		Question embeddedQuestion = getEmbeddedRadioButtonQuestion(/*sequenceNum*/ 0);
		embeddedQuestion.setAssessmentId(assessment04Id);
		page03.addEmbeddedQuestion(embeddedQuestion);
		
		//instantiate Assignment_02
		Assignment assignment02 = new BasicAssignment();
		
		//add Assignment_02 to list
		toReturn.add(assignment02);
		
		//add Chapter_01_Copy to Assignment_02
		Chapter chapter01Copy = new BasicChapter();
		chapter01Copy.setLearningResourceSequenceNumber(chapter01.getLearningResourceSequenceNumber());
		chapter01Copy.setId(chapter01.getId());
		assignment02.addChapter(chapter01Copy);
		
		//add ChapterSection_02_Copy to Chapter_01_Copy
		ChapterSection chapterSection02Copy = new BasicChapterSection();
		chapterSection02Copy.setLearningResourceSequenceNumber(chapterSection02.getLearningResourceSequenceNumber());
		chapterSection02Copy.setId(chapterSection02.getId());
		chapter01Copy.addChapterSection(chapterSection02Copy);
		
		//add Page_02_Copy to ChapterSection_02_Copy
		Page page02Copy = new BasicReadingPage();
		page02Copy.setLearningResourceSequenceNumber(page02.getLearningResourceSequenceNumber());
		page02Copy.setId(page02.getId());
		chapterSection02Copy.addPage(page02Copy);
		
		//add Embedded_Question_Copy to Page_02_Copy
		Question embeddedQuestionCopy = copyRadioButtonQuestion(embeddedQuestion);
		page02Copy.addEmbeddedQuestion(embeddedQuestionCopy);
		
		//add Chapter_02_Copy to Assignment_02
		Chapter chapter02Copy = new BasicChapter();
		chapter02Copy.setLearningResourceSequenceNumber(chapter02.getLearningResourceSequenceNumber());
		chapter02Copy.setId(chapter02.getId());
		assignment02.addChapter(chapter02Copy);
		
		//add Assessment_03_Copy to Chapter_02_Copy
		Quiz assessment03Copy = copyRadioButtonQuiz(assessment03);
		assessment03Copy.setChapterQuiz(true);
		chapter02Copy.setChapterQuiz(assessment03Copy);
		
		//add ChapterSection_01_Copy to Chapter_02_Copy
		ChapterSection chapterSection01Copy = new BasicChapterSection();
		chapterSection01Copy.setLearningResourceSequenceNumber(chapterSection01.getLearningResourceSequenceNumber());
		chapterSection01Copy.setId(chapterSection01.getId());
		chapter02Copy.addChapterSection(chapterSection01Copy);
		
		//add Chapter_03_Copy to Assignment_02
		Chapter chapter03Copy = new BasicChapter();
		chapter03Copy.setLearningResourceSequenceNumber(chapter03.getLearningResourceSequenceNumber());
		chapter03Copy.setId(chapter03.getId());
		assignment02.addChapter(chapter03Copy);
	    
		//add Assessment_01_Copy to Chapter_03_Copy
		Quiz assessment01Copy = copyRadioButtonQuiz(assessment01);
		assessment01Copy.setChapterQuiz(true);
		chapter03Copy.setChapterQuiz(assessment01Copy);
		
		//add ChapterSection_03 to Chapter_03_Copy
		ChapterSection chapterSection03 = new BasicChapterSection();
		chapterSection03.setLearningResourceSequenceNumber(11f);
		chapter03Copy.addChapterSection(chapterSection03);
		
		//add Assessment_02_Copy to ChapterSection_03
		Quiz assessment02Copy = copyRadioButtonQuiz(assessment02);
		chapterSection03.setChapterSectionQuiz(assessment02Copy);
		
		//add Page_01_Copy to ChapterSection_03
		Page page01Copy = new BasicReadingPage();
		page01Copy.setLearningResourceSequenceNumber(page01.getLearningResourceSequenceNumber());
		page01Copy.setId(page01.getId());
		chapterSection03.addPage(page01Copy);
		
		return toReturn;
	}
	
	//TODO: this method should be encapsulated - add to Quiz interface
	private Quiz copyRadioButtonQuiz(Quiz originalQuiz) {
		Quiz copyQuiz = new BasicQuiz();
		copyQuiz.setLearningResourceSequenceNumber(originalQuiz.getLearningResourceSequenceNumber());
		copyQuiz.setId(originalQuiz.getId());
		copyQuiz.setLearningResourceId(originalQuiz.getLearningResourceId());
		copyQuiz.setSeedDateTime(originalQuiz.getAssessmentLastSeedDateTime());
		for (Question originalQuestion : originalQuiz.getQuestions()) {
			copyQuiz.addQuestion(copyRadioButtonQuestion(originalQuestion));
		}
		return copyQuiz;
	}
	
	//TODO: this method should be encapsulated - add to Question interface
	private Question copyRadioButtonQuestion(Question originalQuestion) {
		Question copyQuestion = new MultiValueRadioButtonQuestion(originalQuestion.getText(), 
				originalQuestion.getPointsPossible(), originalQuestion.getSequenceNumber());
    	copyQuestion.setId(originalQuestion.getId());
    	copyQuestion.setAssessmentId(originalQuestion.getAssessmentId());
    	copyQuestion.setQuestionLastSeedDateTime(originalQuestion.getQuestionLastSeedDateTime());
    	for (int j=0; j<originalQuestion.directAccessSubQuestionList().size(); j++) {
    		SubQuestion originalSubQuestion = originalQuestion.directAccessSubQuestionList().get(j);
    		SubQuestion copySubQuestion = new MultiValueSubQuestion(originalQuestion.getText());
    		copyQuestion.addSubQuestion(copySubQuestion);
    		copySubQuestion.setId(originalSubQuestion.getId());
    		copySubQuestion.setText(originalSubQuestion.getText());
    		for (int k=0; k<originalSubQuestion.getAnswers().size(); k++) {
	    		Answer originalAnswer = originalSubQuestion.getAnswers().get(k);
	    		Answer copyAnswer = new MultiValueAnswer(originalAnswer.getText());
	    		copySubQuestion.addAnswer(copyAnswer);
	    		copyAnswer.setId(originalAnswer.getId());
	    		copyAnswer.setText(originalAnswer.getText());
	    		copyAnswer.setCorrectAnswer(originalAnswer.isCorrectAnswer());
	    	}
    	}
    	return copyQuestion;
	}

	private Assignment getReadingOnlyAssignment() {
		Assignment toReturn = new BasicAssignment(); //TODO: set a default due date?
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		ChapterSection chapterSection = new BasicChapterSection();
		chapterSection.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
		Page readingPage = new BasicReadingPage();
		readingPage.setLearningResourceSequenceNumber(2f); //TODO: check the validity of this
		chapterSection.addPage(readingPage);
		chapter.addChapterSection(chapterSection);
		toReturn.addChapter(chapter);
		return toReturn;
	}
	
	private Assignment getChQuizOnly() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment(); //TODO: set a default due date?
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);
		
		//instantiate ChapterQuiz & add Questions & Answers
		Quiz chapterQuiz = generateMultipleChoiceQuiz("Chapter Quiz", 10);
		chapterQuiz.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
		
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);
		
		
		return toReturn;
	}
	
	private Assignment getChQuizOnlyGeneric() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment();  
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f);  
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);
		
		//instantiate ChapterQuiz & add Questions & Answers
		Quiz chapterQuiz = new BasicQuiz();
		UnknownFormatQuestion question01 = new UnknownFormatQuestion("Question Text 1 ", 10, 0);
		UnknownFormatQuestion question02 = new UnknownFormatQuestion("Question Text 2 ", 10, 1);
		UnknownFormatQuestion question03 = new UnknownFormatQuestion("Question Text 3 ", 10, 2);
		
		chapterQuiz.addQuestion(question01);
		chapterQuiz.addQuestion(question02);
		chapterQuiz.addQuestion(question03);
		
		chapterQuiz.setLearningResourceSequenceNumber(1f);  
		
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);
		
		return toReturn;
	}
	
	private Assignment getChQuizGenericMultiValueCombine() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment();  
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f);  
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);
		
		//instantiate ChapterQuiz & add Questions & Answers
		Quiz chapterQuiz = new BasicQuiz();
		
		Question question01 = new MultiValueRadioButtonQuestion("Chapter Quiz - Question 01", /*pointsPossible*/ 10, /*sequenceNumber*/ 0);
		chapterQuiz.addQuestion(question01);
		SubQuestion question01Sub = new MultiValueSubQuestion(question01.getText());
		question01.addSubQuestion(question01Sub);
			Answer question01Answer01 = new MultiValueAnswer("Chapter Quiz - Question 01 - Answer 01");
			question01Answer01.setCorrectAnswer(true);
			question01Sub.addAnswer(question01Answer01);
			Answer question01Answer02 = new MultiValueAnswer("Chapter Quiz - Question 01 - Answer 02");
			question01Sub.addAnswer(question01Answer02);
			Answer question01Answer03 = new MultiValueAnswer("Chapter Quiz - Question 01 - Answer 03");
			question01Sub.addAnswer(question01Answer03);
		
		
		UnknownFormatQuestion question02 = new UnknownFormatQuestion("Question Text 2 ", 10, 1);
		chapterQuiz.addQuestion(question02);

		chapterQuiz.setLearningResourceSequenceNumber(1f);  
		
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);
		
		
		return toReturn;
	}
	
	private Assignment getChSecQuizAndChQuizAndReading() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment(); //TODO: set a default due date?
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);
		
		//instantiate ChapterQuiz & add Questions & Answers
		Quiz chapterQuiz = generateMultipleChoiceQuiz("Chapter Quiz", 3);
		chapterQuiz.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
		chapterQuiz.setChapterQuiz(true);
		
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);
		
		//instantiate ChapterSection & add ReadingPage and ChapterSectionQuiz
		ChapterSection chapterSection = new BasicChapterSection();
		chapterSection.setLearningResourceSequenceNumber(2f); //TODO: check the validity of this
			//instantiate ReadingPage
			Page readingPage = new BasicReadingPage();
			readingPage.setLearningResourceSequenceNumber(3f); //TODO: check the validity of this
			
			//add ReadingPage to ChapterSection
			chapterSection.addPage(readingPage);
			
			//Instantiate ChapterSectionQuiz & add Questions
			Quiz chapterSectionQuiz = generateMultipleChoiceQuiz("Chapter Section Quiz", 3);
			chapterSectionQuiz.setLearningResourceSequenceNumber(4f); //TODO: check the validity of this
			
			//add ChapterSectionQuiz to ChapterSection
			chapterSection.setChapterSectionQuiz(chapterSectionQuiz);
		
		//add ChapterSection to Chapter
		chapter.addChapterSection(chapterSection);
		
		return toReturn;
	}
	
	private Assignment getChSecQuizAndChQuizAndReadingAllPractice() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment(); //TODO: set a default due date?
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);
		
		//instantiate ChapterQuiz & add Questions & Answers
		Quiz chapterQuiz = generateMultipleChoiceQuiz("Chapter Quiz", 3);
		chapterQuiz.setPractice(true);
		for (Question q : chapterQuiz.getQuestions()) {
			q.setPractice(true);
		}
		chapterQuiz.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
		chapterQuiz.setChapterQuiz(true);
		
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);
		
		//instantiate ChapterSection & add ReadingPage and ChapterSectionQuiz
		ChapterSection chapterSection = new BasicChapterSection();
		chapterSection.setLearningResourceSequenceNumber(2f); //TODO: check the validity of this
			//instantiate ReadingPage
			Page readingPage = new BasicReadingPage();
			readingPage.setLearningResourceSequenceNumber(3f); //TODO: check the validity of this
			
			//add ReadingPage to ChapterSection
			chapterSection.addPage(readingPage);
			
			//Instantiate ChapterSectionQuiz & add Questions
			Quiz chapterSectionQuiz = generateMultipleChoiceQuiz("Chapter Section Quiz", 3);
			chapterSectionQuiz.setPractice(true);
			for (Question q : chapterSectionQuiz.getQuestions()) {
				q.setPractice(true);
			}
			chapterSectionQuiz.setLearningResourceSequenceNumber(4f); //TODO: check the validity of this
			
			//add ChapterSectionQuiz to ChapterSection
			chapterSection.setChapterSectionQuiz(chapterSectionQuiz);
		
		//add ChapterSection to Chapter
		chapter.addChapterSection(chapterSection);
		
		return toReturn;
	}

	private Assignment getChSecQuizAndReadingX2() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment(); //TODO: set a default due date?
		
		//instantiate Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);

		//instantiate ChapterSection & add ReadingPage and ChapterSectionQuiz
		ChapterSection chapterSection = new BasicChapterSection();
		chapterSection.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
			//instantiate ReadingPage
			Page readingPage = new BasicReadingPage();
			readingPage.setLearningResourceSequenceNumber(2f); //TODO: check the validity of this
			
			//add ReadingPage to ChapterSection
			chapterSection.addPage(readingPage);
			
			//Instantiate ChapterSectionQuiz & add Questions
			Quiz chapterSectionQuiz = generateMultipleChoiceQuiz("Chapter Section Quiz 1", 3);
			chapterSectionQuiz.setLearningResourceSequenceNumber(3f); //TODO: check the validity of this
			
			//add ChapterSectionQuiz to ChapterSection
			chapterSection.setChapterSectionQuiz(chapterSectionQuiz);
		
		//add ChapterSection to Chapter
		chapter.addChapterSection(chapterSection);
		
		//instantiate Chapter
		Chapter chapter2 = new BasicChapter();
		chapter2.setLearningResourceSequenceNumber(4f); //TODO: check the validity of this

		//add Chapter to Assignment
		toReturn.addChapter(chapter2);
		
		
		//instantiate ChapterSection & add ReadingPage and ChapterSectionQuiz 2
		ChapterSection chapterSection2 = new BasicChapterSection();
		chapterSection2.setLearningResourceSequenceNumber(5f); //TODO: check the validity of this
			//instantiate ReadingPage
			Page readingPage2 = new BasicReadingPage();
			readingPage2.setLearningResourceSequenceNumber(6f); //TODO: check the validity of this
			
			//add ReadingPage to ChapterSection
			chapterSection2.addPage(readingPage2);
			
			//Instantiate ChapterSectionQuiz & add Questions
			Quiz chapterSectionQuiz2 = generateMultipleChoiceQuiz("Chapter Section Quiz 2", 3);
			chapterSectionQuiz2.setLearningResourceSequenceNumber(7f); //TODO: check the validity of this
			
			//add ChapterSectionQuiz to ChapterSection
			chapterSection2.setChapterSectionQuiz(chapterSectionQuiz2);
		
		//add ChapterSection to Chapter
		chapter2.addChapterSection(chapterSection2);
		
		return toReturn;
	}
	

	private Assignment getChSecQuizAndReading() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment(); //TODO: set a default due date?
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);

		//instantiate ChapterSection & add ReadingPage and ChapterSectionQuiz
		ChapterSection chapterSection = new BasicChapterSection();
		chapterSection.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
			//instantiate ReadingPage
			Page readingPage = new BasicReadingPage();
			readingPage.setLearningResourceSequenceNumber(2f); //TODO: check the validity of this
			
			//add ReadingPage to ChapterSection
			chapterSection.addPage(readingPage);
			
			//Instantiate ChapterSectionQuiz & add Questions
			Quiz chapterSectionQuiz = generateMultipleChoiceQuiz("Chapter Section Quiz 1", 3);
			chapterSectionQuiz.setLearningResourceSequenceNumber(3f); //TODO: check the validity of this
			
			//add ChapterSectionQuiz to ChapterSection
			chapterSection.setChapterSectionQuiz(chapterSectionQuiz);
		
		//add ChapterSection to Chapter
		chapter.addChapterSection(chapterSection);
		
		return toReturn;
	}
	
	private Assignment getBigChapterQuiz() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment();
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f);
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);
		
		//instantiate ChapterQuiz & add Questions & Answers
		Quiz chapterQuiz = generateBigMultipleChoiceQuiz("Big Chapter Quiz");
		chapterQuiz.setLearningResourceSequenceNumber(1f);
		chapterQuiz.setChapterQuiz(true);
		
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);
		
		return toReturn;
	}
	
	private Assignment getReallyBigAssignment() {
		Assignment toReturn = new BasicAssignment();
		
		float seqNum = 0;
		for (int i=0; i<15; i++) {
			Chapter chapter = new BasicChapter();
			chapter.setLearningResourceSequenceNumber(seqNum);
			seqNum++;
			toReturn.addChapter(chapter);
			
			Quiz chapterQuiz = generateMultipleChoiceQuiz("Chapter " + i+1 + " Quiz", 3);
			chapterQuiz.setLearningResourceSequenceNumber(seqNum);
			seqNum++;

			chapter.setChapterQuiz(chapterQuiz);
			
			ChapterSection chapterSection = new BasicChapterSection();
			chapterSection.setLearningResourceSequenceNumber(seqNum);
			seqNum++;
			
			Page readingPage = new BasicReadingPage();
			readingPage.setLearningResourceSequenceNumber(seqNum);
			seqNum++;
			
			chapterSection.addPage(readingPage);
			
			Quiz chapterSectionQuiz = generateMultipleChoiceQuiz("Chapter Section " + i+1 + " Quiz", 3);
			chapterSectionQuiz.setLearningResourceSequenceNumber(seqNum);
			seqNum++;
			
			chapterSection.setChapterSectionQuiz(chapterSectionQuiz);
			
			chapter.addChapterSection(chapterSection);
		}
		
		return toReturn;
	}
	
	private Assignment getNestedQuizzes() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment(); //TODO: set a default due date?
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);
		
		//instantiate ChapterQuiz & add Questions & Answers
		Quiz chapterQuiz = generateMultipleChoiceQuiz("Chapter Quiz", 3);
		chapterQuiz.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
		chapterQuiz.setChapterQuiz(true);
		
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);
		
		
		//instantiate ChapterQuiz & add Questions & Answers
		Quiz nestedQuiz1 = generateMultipleChoiceQuiz("Nested Quiz 1", 3);
		nestedQuiz1.setLearningResourceSequenceNumber(2f); //TODO: check the validity of this
		//add ChapterQuiz to Chapter
		chapterQuiz.addNestedQuiz(nestedQuiz1);

		//instantiate ChapterQuiz & add Questions & Answers
		Quiz nestedQuiz2 = generateMultipleChoiceQuiz("Nested Quiz 2", 3);
		nestedQuiz2.setLearningResourceSequenceNumber(3f); //TODO: check the validity of this
		//add ChapterQuiz to Chapter
		chapterQuiz.addNestedQuiz(nestedQuiz2);
		
		return toReturn;
	}
	
	private Assignment getAlternateAddAssignment() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment(); //TODO: set a default due date?
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);

		//instantiate ChapterSection and add a ChaopterSectionQuiz
		ChapterSection chapterSection = new BasicChapterSection();
		chapterSection.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
						
		//Instantiate chapter section quiz and add 
		Quiz chapterQuiz = generateMultipleChoiceQuiz("Chapter Quiz", 4);
		chapterQuiz.setLearningResourceSequenceNumber(2f); //TODO: check the validity of this
		
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);
		
		//Instantiate ChapterSectionQuiz & add Questions
		Quiz chapterSectionQuiz = generateMultipleChoiceQuiz("Chapter Section Quiz 1", 3);
		chapterSectionQuiz.setLearningResourceSequenceNumber(3f); //TODO: check the validity of this
		
		//add ChapterSectionQuiz to ChapterSection
		chapterSection.setChapterSectionQuiz(chapterSectionQuiz);
	
		//add ChapterSection to Chapter
		chapter.addChapterSection(chapterSection);
		return toReturn;
	}
	
	private Assignment getNestedQuizzesUnderPractice() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment(); //TODO: set a default due date?
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);
		
		//instantiate ChapterQuiz & add Questions & Answers
		Quiz chapterQuiz = generateMultipleChoiceQuiz("Chapter Quiz", 4);
		chapterQuiz.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
		chapterQuiz.setChapterQuiz(true);
		
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);
		
		
		//instantiate ChapterQuiz & add Questions & Answers
		Quiz nestedQuiz1 = generateMultipleChoiceQuiz("Nested Quiz 1 - Practice", 4);
		nestedQuiz1.setPractice(true);
		for (Question q : nestedQuiz1.getQuestions()) {
			q.setPractice(true);
		}
		nestedQuiz1.setLearningResourceSequenceNumber(2f); //TODO: check the validity of this
		//add ChapterQuiz to Chapter
		chapterQuiz.addNestedQuiz(nestedQuiz1);

		//instantiate ChapterQuiz & add Questions & Answers
		Quiz nestedQuiz2 = generateMultipleChoiceQuiz("Nested Quiz 2 - Practice", 4);
		nestedQuiz2.setPractice(true);
		for (Question q : nestedQuiz2.getQuestions()) {
			q.setPractice(true);
		}
		nestedQuiz2.setLearningResourceSequenceNumber(3f); //TODO: check the validity of this
		//add ChapterQuiz to Chapter
		chapterQuiz.addNestedQuiz(nestedQuiz2);
		
		return toReturn;
	}

	private Assignment getSecReadingEmbeddedQuiz() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment();
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);

		//instantiate ChapterSection & add ReadingPage and ChapterSectionQuiz
		ChapterSection chapterSection = new BasicChapterSection();
		chapterSection.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
			//instantiate ReadingPage
			Page readingPage = new BasicReadingPage();
			readingPage.setLearningResourceSequenceNumber(2f); //TODO: check the validity of this
			
			//add ReadingPage to ChapterSection
			chapterSection.addPage(readingPage);
			
			//add embedded Questions to ReadingPage
			String randomUUID = UUID.randomUUID().toString();
			String assessmentId01 = "SQE-Assess-" + randomUUID;
			
			Question question01 = getEmbeddedRadioButtonQuestion(/*sequenceNum*/ 0);
			question01.setAssessmentId(assessmentId01);
			readingPage.addEmbeddedQuestion(question01);
			
			Question question02 = getEmbeddedRadioButtonQuestion(/*sequenceNum*/ 1);
			question02.setAssessmentId(assessmentId01);
			readingPage.addEmbeddedQuestion(question02);
			
			String randomUUID02 = UUID.randomUUID().toString();
			String assessmentId02 = "SQE-Assess-" + randomUUID02;
			
			Question question03 = getEmbeddedRadioButtonQuestion(/*sequenceNum*/ 3);
			question03.setPractice(true);
			question03.setAssessmentId(assessmentId02);
			readingPage.addEmbeddedQuestion(question03);
		
		//add ChapterSection to Chapter
		chapter.addChapterSection(chapterSection);
		
		return toReturn;
	}
	
	private Question getEmbeddedRadioButtonQuestion(float sequenceNum) {
		Question question = new MultiValueRadioButtonQuestion("Embedded - Question " + sequenceNum+1 , /*pointsPossible*/ 3, /*sequenceNumber*/ sequenceNum);
		SubQuestion subQuestion = new MultiValueSubQuestion(question.getText());
		question.addSubQuestion(subQuestion);
			Answer answer01 = new MultiValueAnswer("Embedded - Question " + sequenceNum+1 + " - Answer 01");
			subQuestion.addAnswer(answer01);
			Answer answer02 = new MultiValueAnswer("Embedded - Question " + sequenceNum+1 + " - Answer 02");
			answer02.setCorrectAnswer(true);
			subQuestion.addAnswer(answer02);
			Answer answer03 = new MultiValueAnswer("Embedded - Question " + sequenceNum+1 + " - Answer 03");
			subQuestion.addAnswer(answer03);
		return question;
	}
	
	private Assignment getNestedReadingPagesOnly() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment(); //TODO: set a default due date?
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);

		//instantiate ChapterSection & add ReadingPage and ChapterSectionQuiz
		ChapterSection chapterSection = new BasicChapterSection();
		chapterSection.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
			//instantiate ReadingPage
			Page readingPage = new BasicReadingPage();
			readingPage.setLearningResourceSequenceNumber(2f); //TODO: check the validity of this
			
			//add ReadingPage to ChapterSection
			chapterSection.addPage(readingPage);
			
			// initiate two more reading pages.
			Page readingPage2 = new BasicReadingPage();
			readingPage2.setLearningResourceSequenceNumber(3f); //TODO: check the validity of this
			
			Page readingPage3 = new BasicReadingPage();
			readingPage3.setLearningResourceSequenceNumber(4f); //TODO: check the validity of this
			
			readingPage.addPage(readingPage2);
			readingPage.addPage(readingPage3);
			
		
		//add ChapterSection to Chapter
		chapter.addChapterSection(chapterSection);
		
		return toReturn;
	}

	private Assignment getSixNewTypeOfQuestionsInChapQuiz() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment(); //TODO: set a default due date?
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);
		
		//instantiate ChapterQuiz & add Questions & Answers
		Quiz chapterQuiz = generateSixTypeMultiValueQuestionInQuiz("Chapter Quiz");
		chapterQuiz.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
		chapterQuiz.setChapterQuiz(true);
		
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);
		
		return toReturn;
	}
	
	private Quiz generateSixTypeMultiValueQuestionInQuiz(String quizTitle) {
		Quiz quiz = new BasicQuiz();
		Question question01 = new MultiValueBinningQuestion(quizTitle + " - Question 01", /*pointsPossible*/ 4, /*sequenceNumber*/ 0);
		quiz.addQuestion(question01);
		SubQuestion question01Sub1 = new MultiValueSubQuestion(question01.getText());
		question01.addSubQuestion(question01Sub1);
			Answer question01Answer01 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 01");
			question01Answer01.setCorrectAnswer(true);
			question01Sub1.addAnswer(question01Answer01);
			Answer question01Answer02 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 02");
			question01Answer02.setCorrectAnswer(true);
			question01Sub1.addAnswer(question01Answer02);
			Answer question01Answer03 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 03");
			question01Sub1.addAnswer(question01Answer03);
			Answer question01Answer04 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 04");
			question01Sub1.addAnswer(question01Answer04);
		SubQuestion question01Sub2 = new MultiValueSubQuestion(question01.getText());
		question01.addSubQuestion(question01Sub2);
			Answer question01Sub2Answer01 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 01");
			question01Sub2Answer01.setId(question01Answer01.getId());
			question01Sub2.addAnswer(question01Sub2Answer01);
			Answer question01Sub2Answer02 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 02");
			question01Sub2Answer02.setId(question01Answer02.getId());
			question01Sub2.addAnswer(question01Sub2Answer02);
			Answer question01Sub2Answer03 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 03");
			question01Sub2Answer03.setId(question01Answer03.getId());
			question01Sub2Answer03.setCorrectAnswer(true);
			question01Sub2.addAnswer(question01Sub2Answer03);
			Answer question01Sub2Answer04 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 04");
			question01Sub2Answer04.setId(question01Answer04.getId());
			question01Sub2Answer04.setCorrectAnswer(true);
			question01Sub2.addAnswer(question01Sub2Answer04);
			
		Question question03 = new MultiValueHotSpotQuestion(quizTitle + " - Question 03", /*pointsPossible*/ 3, /*sequenceNumber*/ 1);
		quiz.addQuestion(question03);
		SubQuestion question03Sub = new MultiValueSubQuestion(question03.getText());
		question03.addSubQuestion(question03Sub);
			Answer question03Answer01 = new MultiValueAnswer(quizTitle + " - Question 03 - Answer 01");
			question03Answer01.setCorrectAnswer(true);
			question03Sub.addAnswer(question03Answer01);
			Answer question03Answer02 = new MultiValueAnswer(quizTitle + " - Question 03 - Answer 02");
			question03Sub.addAnswer(question03Answer02);
			Answer question03Answer03 = new MultiValueAnswer(quizTitle + " - Question 03 - Answer 03");
			question03Sub.addAnswer(question03Answer03);
			Answer question03Answer04 = new MultiValueAnswer(quizTitle + " - Question 03 - Answer 04");
			question03Sub.addAnswer(question03Answer04);
			
		Question question04 = new MultiValueMultipleHotSpotQuestion(quizTitle + " - Question 04", /*pointsPossible*/ 3, /*sequenceNumber*/ 2);
		quiz.addQuestion(question04);
		SubQuestion question04Sub = new MultiValueSubQuestion(question04.getText());
		question04.addSubQuestion(question04Sub);
			Answer question04Answer01 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 01");
			question04Sub.addAnswer(question04Answer01);
			Answer question04Answer02 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 02");
			question04Sub.addAnswer(question04Answer02);
			Answer question04Answer03 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 03");
			question04Answer03.setCorrectAnswer(true);
			question04Sub.addAnswer(question04Answer03);
			Answer question04Answer04 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 04");
			question04Sub.addAnswer(question04Answer04);
		SubQuestion question04Sub2 = new MultiValueSubQuestion(question04.getText());
		question04.addSubQuestion(question04Sub2);
			Answer question04Sub2Answer01 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 01");
			question04Sub2Answer01.setId(question04Answer01.getId());
			question04Sub2.addAnswer(question04Sub2Answer01);
			Answer question04Sub2Answer02 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 02");
			question04Sub2Answer02.setId(question04Answer02.getId());
			question04Sub2Answer02.setCorrectAnswer(true);
			question04Sub2.addAnswer(question04Sub2Answer02);
			Answer question04Sub2Answer03 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 03");
			question04Sub2Answer03.setId(question04Answer03.getId());
			question04Sub2.addAnswer(question04Sub2Answer03);
			Answer question04Sub2Answer04 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 04");
			question04Sub2Answer04.setId(question04Answer04.getId());
			question04Sub2.addAnswer(question04Sub2Answer04);	
		SubQuestion question04Sub3 = new MultiValueSubQuestion(question04.getText());
		question04.addSubQuestion(question04Sub3);
			Answer question04Sub3Answer01 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 01");
			question04Sub3Answer01.setId(question04Answer01.getId());
			question04Sub3.addAnswer(question04Sub3Answer01);
			Answer question04Sub3Answer02 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 02");
			question04Sub3Answer02.setId(question04Answer02.getId());
			question04Sub3.addAnswer(question04Sub3Answer02);
			Answer question04Sub3Answer03 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 03");
			question04Sub3Answer03.setId(question04Answer03.getId());
			question04Sub3.addAnswer(question04Sub3Answer03);
			Answer question04Sub3Answer04 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 04");
			question04Sub3Answer04.setId(question04Answer04.getId());
			question04Sub3Answer04.setCorrectAnswer(true);
			question04Sub3.addAnswer(question04Sub3Answer04);	
			
		Question question05 = new MultiValueMultiSelectQuestion(quizTitle + " - Question 05", /*pointsPossible*/ 3, /*sequenceNumber*/ 3);
		quiz.addQuestion(question05);
		SubQuestion question05Sub = new MultiValueSubQuestion(question05.getText());
		question05.addSubQuestion(question05Sub);
			Answer question05Answer01 = new MultiValueAnswer(quizTitle + " - Question 05 - Answer 01");
			question05Answer01.setCorrectAnswer(true);
			question05Sub.addAnswer(question05Answer01);
			Answer question05Answer02 = new MultiValueAnswer(quizTitle + " - Question 05 - Answer 02");
			question05Sub.addAnswer(question05Answer02);
			Answer question05Answer03 = new MultiValueAnswer(quizTitle + " - Question 05 - Answer 03");
			question05Sub.addAnswer(question05Answer03);
			Answer question05Answer04 = new MultiValueAnswer(quizTitle + " - Question 05 - Answer 04");
			question05Answer04.setCorrectAnswer(true);
			question05Sub.addAnswer(question05Answer04);	
			
		Question question06 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 06", /*pointsPossible*/ 3, /*sequenceNumber*/ 4);
		quiz.addQuestion(question06);
		SubQuestion question06Sub = new MultiValueSubQuestion(question06.getText());
		question06.addSubQuestion(question06Sub);
			Answer question06Answer01 = new MultiValueAnswer(quizTitle + " - Question 06 - Answer 01");
			question06Sub.addAnswer(question06Answer01);
			Answer question06Answer02 = new MultiValueAnswer(quizTitle + " - Question 06 - Answer 02");
			question06Answer02.setCorrectAnswer(true);
			question06Sub.addAnswer(question06Answer02);
			Answer question06Answer03 = new MultiValueAnswer(quizTitle + " - Question 06 - Answer 03");
			question06Sub.addAnswer(question06Answer03);	
			Answer question06Answer04 = new MultiValueAnswer(quizTitle + " - Question 06 - Answer 04");
			question06Sub.addAnswer(question06Answer04);	
			
		//testing Brix craziness - two radio button questions that each have a single target with id = "Target"
		Question question07 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 07", /*pointsPossible*/ 3, /*sequenceNumber*/ 5);
		quiz.addQuestion(question07);
		SubQuestion question07Sub = new MultiValueSubQuestion(question07.getText());
		question07Sub.setId("Target");
		question07.addSubQuestion(question07Sub);
			Answer question07Answer01 = new MultiValueAnswer(quizTitle + " - Question 07 - Answer 01");
			question07Sub.addAnswer(question07Answer01);
			Answer question07Answer02 = new MultiValueAnswer(quizTitle + " - Question 07 - Answer 02");
			question07Answer02.setCorrectAnswer(true);
			question07Sub.addAnswer(question07Answer02);
			Answer question07Answer03 = new MultiValueAnswer(quizTitle + " - Question 07 - Answer 03");
			question07Sub.addAnswer(question07Answer03);	
			Answer question07Answer04 = new MultiValueAnswer(quizTitle + " - Question 07 - Answer 04");
			question07Sub.addAnswer(question07Answer04);
			
		Question question08 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 08", /*pointsPossible*/ 3, /*sequenceNumber*/ 6);
		quiz.addQuestion(question08);
		SubQuestion question08Sub = new MultiValueSubQuestion(question08.getText());
		question08Sub.setId("Target");
		question08.addSubQuestion(question08Sub);
			Answer question08Answer01 = new MultiValueAnswer(quizTitle + " - Question 08 - Answer 01");
			question08Answer01.setCorrectAnswer(true);
			question08Sub.addAnswer(question08Answer01);
			Answer question08Answer02 = new MultiValueAnswer(quizTitle + " - Question 08 - Answer 02");
			question08Sub.addAnswer(question08Answer02);
			Answer question08Answer03 = new MultiValueAnswer(quizTitle + " - Question 08 - Answer 03");
			question08Sub.addAnswer(question08Answer03);	
			Answer question08Answer04 = new MultiValueAnswer(quizTitle + " - Question 08 - Answer 04");
			question08Sub.addAnswer(question08Answer04);
			
	    //testing Brix craziness - two multi-select questions that each have two targets with ids "true" & "false"
		Question question09 = new MultiValueMultiSelectQuestion(quizTitle + " - Question 09", /*pointsPossible*/ 3, /*sequenceNumber*/ 7);
		quiz.addQuestion(question09);
		SubQuestion question09Sub1 = new MultiValueSubQuestion(question09.getText());
		question09Sub1.setId("true");
		question09.addSubQuestion(question09Sub1);
			Answer question09Answer01 = new MultiValueAnswer(quizTitle + " - Question 09 - Answer 01");
			question09Answer01.setCorrectAnswer(true);
			question09Sub1.addAnswer(question09Answer01);
			Answer question09Answer02 = new MultiValueAnswer(quizTitle + " - Question 09 - Answer 02");
			question09Sub1.addAnswer(question09Answer02);
			Answer question09Answer03 = new MultiValueAnswer(quizTitle + " - Question 09 - Answer 03");
			question09Sub1.addAnswer(question09Answer03);
			Answer question09Answer04 = new MultiValueAnswer(quizTitle + " - Question 09 - Answer 04");
			question09Answer04.setCorrectAnswer(true);
			question09Sub1.addAnswer(question09Answer04);
		SubQuestion question09Sub2 = new MultiValueSubQuestion(question09.getText());
		question09Sub2.setId("false");
		question09.addSubQuestion(question09Sub2);
			Answer question09Sub2Answer01 = new MultiValueAnswer(quizTitle + " - Question 09 - Answer 01");
			question09Sub2Answer01.setId(question09Answer01.getId());
			question09Sub2.addAnswer(question09Sub2Answer01);
			Answer question09Sub2Answer02 = new MultiValueAnswer(quizTitle + " - Question 09 - Answer 02");
			question09Sub2Answer02.setId(question09Answer02.getId());
			question09Sub2Answer02.setCorrectAnswer(true);
			question09Sub2.addAnswer(question09Sub2Answer02);
			Answer question09Sub2Answer03 = new MultiValueAnswer(quizTitle + " - Question 09 - Answer 03");
			question09Sub2Answer03.setId(question09Answer03.getId());
			question09Sub2Answer03.setCorrectAnswer(true);
			question09Sub2.addAnswer(question09Sub2Answer03);
			Answer question09Sub2Answer04 = new MultiValueAnswer(quizTitle + " - Question 09 - Answer 04");
			question09Sub2Answer04.setId(question09Answer04.getId());
			question09Sub2.addAnswer(question09Sub2Answer04);
			
		Question question10 = new MultiValueMultiSelectQuestion(quizTitle + " - Question 10", /*pointsPossible*/ 3, /*sequenceNumber*/ 8);
		quiz.addQuestion(question10);
		SubQuestion question10Sub1 = new MultiValueSubQuestion(question10.getText());
		question10Sub1.setId("true");
		question10.addSubQuestion(question10Sub1);
			Answer question10Answer01 = new MultiValueAnswer(quizTitle + " - Question 10 - Answer 01");
			question10Answer01.setCorrectAnswer(true);
			question10Sub1.addAnswer(question10Answer01);
			Answer question10Answer02 = new MultiValueAnswer(quizTitle + " - Question 10 - Answer 02");
			question10Sub1.addAnswer(question10Answer02);
			Answer question10Answer03 = new MultiValueAnswer(quizTitle + " - Question 10 - Answer 03");
			question10Sub1.addAnswer(question10Answer03);
			Answer question10Answer04 = new MultiValueAnswer(quizTitle + " - Question 10 - Answer 04");
			question10Answer04.setCorrectAnswer(true);
			question10Sub1.addAnswer(question10Answer04);
		SubQuestion question10Sub2 = new MultiValueSubQuestion(question10.getText());
		question10Sub2.setId("false");
		question10.addSubQuestion(question10Sub2);
			Answer question10Sub2Answer01 = new MultiValueAnswer(quizTitle + " - Question 10 - Answer 01");
			question10Sub2Answer01.setId(question10Answer01.getId());
			question10Sub2.addAnswer(question10Sub2Answer01);
			Answer question10Sub2Answer02 = new MultiValueAnswer(quizTitle + " - Question 10 - Answer 02");
			question10Sub2Answer02.setId(question10Answer02.getId());
			question10Sub2Answer02.setCorrectAnswer(true);
			question10Sub2.addAnswer(question10Sub2Answer02);
			Answer question10Sub2Answer03 = new MultiValueAnswer(quizTitle + " - Question 10 - Answer 03");
			question10Sub2Answer03.setId(question10Answer03.getId());
			question10Sub2Answer03.setCorrectAnswer(true);
			question10Sub2.addAnswer(question10Sub2Answer03);
			Answer question10Sub2Answer04 = new MultiValueAnswer(quizTitle + " - Question 10 - Answer 04");
			question10Sub2Answer04.setId(question10Answer04.getId());
			question10Sub2.addAnswer(question10Sub2Answer04);
			
		return quiz;
	}
	
	private Assignment getAssignmentWithFillInTheBlankQuestions() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment();
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f);
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);
		
		//instantiate ChapterQuiz & add Questions & Answers
		Quiz chapterQuiz = generateQuizWithFillInTheBlankQuestions("Chapter Quiz");
		chapterQuiz.setLearningResourceSequenceNumber(1f);
		chapterQuiz.setChapterQuiz(true);
		
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);
		
		return toReturn;
	}
	
	private Quiz generateQuizWithFillInTheBlankQuestions(String quizTitle) {
		Quiz quiz = new BasicQuiz();			
		Question question06 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 06", /*pointsPossible*/ 3, /*sequenceNumber*/ 4);
		quiz.addQuestion(question06);
		SubQuestion question06Sub = new MultiValueSubQuestion(question06.getText());
		question06.addSubQuestion(question06Sub);
			Answer question06Answer01 = new MultiValueAnswer(quizTitle + " - Question 06 - Answer 01");
			question06Sub.addAnswer(question06Answer01);
			Answer question06Answer02 = new MultiValueAnswer(quizTitle + " - Question 06 - Answer 02");
			question06Answer02.setCorrectAnswer(true);
			question06Sub.addAnswer(question06Answer02);
			Answer question06Answer03 = new MultiValueAnswer(quizTitle + " - Question 06 - Answer 03");
			question06Sub.addAnswer(question06Answer03);	
			Answer question06Answer04 = new MultiValueAnswer(quizTitle + " - Question 06 - Answer 04");
			question06Sub.addAnswer(question06Answer04);	
						
		Question question11 = new MultiValueOtherQuestion(quizTitle + " - Question 11", /*pointsPossible*/ 6, /*sequenceNumber*/ 9);
		quiz.addQuestion(question11);
		SubQuestion question11Sub1 = new MultiValueSubQuestion(question11.getText());
		question11.addSubQuestion(question11Sub1);
			Answer question11Answer01 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 01");
			question11Answer01.setCorrectAnswer(true);
			question11Sub1.addAnswer(question11Answer01);
			Answer question11Answer02 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 02");
			question11Sub1.addAnswer(question11Answer02);
			Answer question11Answer03 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 03");
			question11Sub1.addAnswer(question11Answer03);
			Answer question11Answer04 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 04");
			question11Sub1.addAnswer(question11Answer04);
		SubQuestion question11Sub2 = new MultiValueSubQuestion(question11.getText());
		question11.addSubQuestion(question11Sub2);
			Answer question11Sub2Answer01 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 01");
			question11Sub2Answer01.setId(question11Answer01.getId());
			question11Sub2.addAnswer(question11Sub2Answer01);
			Answer question11Sub2Answer02 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 02");
			question11Sub2Answer02.setId(question11Answer02.getId());
			question11Sub2Answer02.setCorrectAnswer(true);
			question11Sub2.addAnswer(question11Sub2Answer02);
			Answer question11Sub2Answer03 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 03");
			question11Sub2Answer03.setId(question11Answer03.getId());
			question11Sub2.addAnswer(question11Sub2Answer03);
			Answer question11Sub2Answer04 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 04");
			question11Sub2Answer04.setId(question11Answer04.getId());
			question11Sub2.addAnswer(question11Sub2Answer04);
		SubQuestion question11Sub3 = new MultiValueSubQuestion(question11.getText());
		question11.addSubQuestion(question11Sub3);
			Answer question11Sub3Answer01 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 01");
			question11Sub3Answer01.setId(question11Answer01.getId());
			question11Sub3.addAnswer(question11Sub3Answer01);
			Answer question11Sub3Answer02 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 02");
			question11Sub3Answer02.setId(question11Answer02.getId());
			question11Sub3.addAnswer(question11Sub3Answer02);
			Answer question11Sub3Answer03 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 03");
			question11Sub3Answer03.setId(question11Answer03.getId());
			question11Sub3Answer03.setCorrectAnswer(true);
			question11Sub3.addAnswer(question11Sub3Answer03);
			Answer question11Sub3Answer04 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 04");
			question11Sub3Answer04.setId(question11Answer04.getId());
			question11Sub3.addAnswer(question11Sub3Answer04);
			
		Question question12 = new MultiValueOtherQuestion(quizTitle + " - Question 12", /*pointsPossible*/ 6, /*sequenceNumber*/ 10);
		quiz.addQuestion(question12);
		SubQuestion question12Sub1 = new MultiValueSubQuestion(question12.getText());
		question12.addSubQuestion(question12Sub1);
			Answer question12Answer01 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 01");
			question12Answer01.setCorrectAnswer(true);
			question12Sub1.addAnswer(question12Answer01);
			Answer question12Answer02 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 02");
			question12Sub1.addAnswer(question12Answer02);
			Answer question12Answer03 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 03");
			question12Sub1.addAnswer(question12Answer03);
			Answer question12Answer04 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 04");
			question12Sub1.addAnswer(question12Answer04);
		SubQuestion question12Sub2 = new MultiValueSubQuestion(question12.getText());
		question12.addSubQuestion(question12Sub2);
			Answer question12Sub2Answer01 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 01");
			question12Sub2Answer01.setId(question12Answer01.getId());
			question12Sub2.addAnswer(question12Sub2Answer01);
			Answer question12Sub2Answer02 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 02");
			question12Sub2Answer02.setId(question12Answer02.getId());
			question12Sub2Answer02.setCorrectAnswer(true);
			question12Sub2.addAnswer(question12Sub2Answer02);
			Answer question12Sub2Answer03 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 03");
			question12Sub2Answer03.setId(question12Answer03.getId());
			question12Sub2.addAnswer(question12Sub2Answer03);
			Answer question12Sub2Answer04 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 04");
			question12Sub2Answer04.setId(question12Answer04.getId());
			question12Sub2.addAnswer(question12Sub2Answer04);
		SubQuestion question12Sub3 = new MultiValueSubQuestion(question12.getText());
		question12.addSubQuestion(question12Sub3);
			Answer question12Sub3Answer01 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 01");
			question12Sub3Answer01.setId(question12Answer01.getId());
			question12Sub3.addAnswer(question12Sub3Answer01);
			Answer question12Sub3Answer02 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 02");
			question12Sub3Answer02.setId(question12Answer02.getId());
			question12Sub3.addAnswer(question12Sub3Answer02);
			Answer question12Sub3Answer03 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 03");
			question12Sub3Answer03.setId(question12Answer03.getId());
			question12Sub3Answer03.setCorrectAnswer(true);
			question12Sub3.addAnswer(question12Sub3Answer03);
			Answer question12Sub3Answer04 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 04");
			question12Sub3Answer04.setId(question12Answer04.getId());
			question12Sub3.addAnswer(question12Sub3Answer04);
			
		return quiz;
	}

	private Quiz generateMultipleChoiceQuiz(String quizTitle, float pointsPossibleEachQuestion) {
		Quiz quiz = new BasicQuiz();
		Question question01 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 01", /*pointsPossible*/ pointsPossibleEachQuestion, /*sequenceNumber*/ 0);
		quiz.addQuestion(question01);
		SubQuestion question01Sub = new MultiValueSubQuestion(question01.getText());
		question01.addSubQuestion(question01Sub);
			Answer question01Answer01 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 01");
			question01Answer01.setCorrectAnswer(true);
			question01Sub.addAnswer(question01Answer01);
			Answer question01Answer02 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 02");
			question01Sub.addAnswer(question01Answer02);
			Answer question01Answer03 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 03");
			question01Sub.addAnswer(question01Answer03);
		Question question02 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 02", /*pointsPossible*/ pointsPossibleEachQuestion, /*sequenceNumber*/ 1);
		quiz.addQuestion(question02);
		SubQuestion question02Sub = new MultiValueSubQuestion(question02.getText());
		question02.addSubQuestion(question02Sub);
			Answer question02Answer01 = new MultiValueAnswer(quizTitle + " - Question 02 - Answer 01");
			question02Answer01.setCorrectAnswer(true);
			question02Sub.addAnswer(question02Answer01);
			Answer question02Answer02 = new MultiValueAnswer(quizTitle + " - Question 02 - Answer 02");
			question02Sub.addAnswer(question02Answer02);
			Answer question02Answer03 = new MultiValueAnswer(quizTitle + " - Question 02 - Answer 03");
			question02Sub.addAnswer(question02Answer03);
		Question question03 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 03", /*pointsPossible*/ pointsPossibleEachQuestion, /*sequenceNumber*/ 2);
		quiz.addQuestion(question03);
		SubQuestion question03Sub = new MultiValueSubQuestion(question03.getText());
		question03.addSubQuestion(question03Sub);
			Answer question03Answer01 = new MultiValueAnswer(quizTitle + " - Question 03 - Answer 01");
			question03Answer01.setCorrectAnswer(true);
			question03Sub.addAnswer(question03Answer01);
			Answer question03Answer02 = new MultiValueAnswer(quizTitle + " - Question 03 - Answer 02");
			question03Sub.addAnswer(question03Answer02);
			Answer question03Answer03 = new MultiValueAnswer(quizTitle + " - Question 03 - Answer 03");
			question03Sub.addAnswer(question03Answer03);
		return quiz;
	}
	
	private Quiz generateFillInTheBlankAndNumericQuiz(String quizTitle, float pointsPossible){
		Quiz quiz = new BasicQuiz();
		
		Question question01 = new MultiValueFillInTheBlankQuestion("Fill in the blanks(present tense of 'to be')", /*pointsPossible*/ 4, /*sequenceNumber*/ 0);
		quiz.addQuestion(question01);
		SubQuestion question01Sub01 = new MultiValueSubQuestion(question01.getText());
		question01.addSubQuestion(question01Sub01);
			Answer question01Answer01 = new MultiValueAnswer("doesn't matter what I put here");
			question01Answer01.setCorrectAnswer(true);
			question01Sub01.addAnswer(question01Answer01);
		SubQuestion question01Sub02 = new MultiValueSubQuestion(question01.getText());
		question01.addSubQuestion(question01Sub02);
			Answer question01Answer02 = new MultiValueAnswer("random text");
			question01Answer02.setCorrectAnswer(true);
			question01Sub02.addAnswer(question01Answer02);
			
		Question question02 = new MultiValueFillInTheBlankQuestion("Put the verbs into the simple past. When you finish, select Submit." ,/*pointsPossible*/ 2, /*sequenceNumber*/ 1);
		quiz.addQuestion(question02);
		SubQuestion question02Sub01 = new MultiValueSubQuestion(question02.getText());
		question02.addSubQuestion(question02Sub01);
			Answer question02Answer01 = new MultiValueAnswer("");
			question02Answer01.setCorrectAnswer(true);
			question02Sub01.addAnswer(question02Answer01);
			
		Question question03 = new MultiValueNumericQuestion("What is the magnitude of the acceleration a of the chair in meters per second squared?", /*pointsPossible*/ 6, /*sequenceNumber*/ 2);
		quiz.addQuestion(question03);
		SubQuestion question03Sub01 = new MultiValueSubQuestion(question03.getText());
		question03.addSubQuestion(question03Sub01);
			Answer question03Answer01 = new MultiValueAnswer("blue");
			question03Answer01.setCorrectAnswer(true);
			question03Sub01.addAnswer(question03Answer01);
		SubQuestion question03Sub02 = new MultiValueSubQuestion(question03.getText());
		question03.addSubQuestion(question03Sub02);
			Answer question03Answer02 = new MultiValueAnswer("not a valid numeric answer");
			question03Answer02.setCorrectAnswer(true);
			question03Sub02.addAnswer(question03Answer02);	
		SubQuestion question03Sub03 = new MultiValueSubQuestion(question03.getText());
		question03.addSubQuestion(question03Sub03);
			Answer question03Answer03 = new MultiValueAnswer("");
			question03Answer03.setCorrectAnswer(true);
			question03Sub03.addAnswer(question03Answer03);	
			
		Question question04 = new MultiValueRadioButtonQuestion(" What is the population of earth?", /*pointsPossible*/ 8, /*sequenceNumber*/ 3);
		quiz.addQuestion(question04);
		SubQuestion question04Sub01 = new MultiValueSubQuestion(question04.getText());
		question04.addSubQuestion(question04Sub01);
			Answer question04Answer01 = new MultiValueAnswer("9.125 billion");
			question04Sub01.addAnswer(question04Answer01);
			Answer question04Answer02 = new MultiValueAnswer("7.125 billion");
			question04Answer02.setCorrectAnswer(true);
			question04Sub01.addAnswer(question04Answer02);
			Answer question04Answer03 = new MultiValueAnswer("12.125 billion");
			question04Sub01.addAnswer(question04Answer03);	
			Answer question04Answer04 = new MultiValueAnswer("10.125 billion");
			question04Sub01.addAnswer(question04Answer04);	
			
		return quiz;
	}
	
	private Quiz generateFillInTheBlankAndNumericOnlyQuiz(String quizTitle, float pointsPossible){
		Quiz quiz = new BasicQuiz();
		
		Question question01 = new MultiValueFillInTheBlankQuestion("Fill in the blanks(present tense of 'to be')", /*pointsPossible*/ 4, /*sequenceNumber*/ 0);
		quiz.addQuestion(question01);
		SubQuestion question01Sub01 = new MultiValueSubQuestion(question01.getText());
		question01.addSubQuestion(question01Sub01);
			Answer question01Answer01 = new MultiValueAnswer("doesn't matter what I put here");
			question01Answer01.setCorrectAnswer(true);
			question01Sub01.addAnswer(question01Answer01);
		SubQuestion question01Sub02 = new MultiValueSubQuestion(question01.getText());
		question01.addSubQuestion(question01Sub02);
			Answer question01Answer02 = new MultiValueAnswer("random text");
			question01Answer02.setCorrectAnswer(true);
			question01Sub02.addAnswer(question01Answer02);
			
		Question question02 = new MultiValueFillInTheBlankQuestion("Put the verbs into the simple past. When you finish, select Submit." ,/*pointsPossible*/ 2, /*sequenceNumber*/ 1);
		quiz.addQuestion(question02);
		SubQuestion question02Sub01 = new MultiValueSubQuestion(question02.getText());
		question02.addSubQuestion(question02Sub01);
			Answer question02Answer01 = new MultiValueAnswer("");
			question02Answer01.setCorrectAnswer(true);
			question02Sub01.addAnswer(question02Answer01);
			
		Question question03 = new MultiValueNumericQuestion("What is the magnitude of the acceleration a of the chair in meters per second squared?", /*pointsPossible*/ 6, /*sequenceNumber*/ 2);
		quiz.addQuestion(question03);
		SubQuestion question03Sub01 = new MultiValueSubQuestion(question03.getText());
		question03.addSubQuestion(question03Sub01);
			Answer question03Answer01 = new MultiValueAnswer("blue");
			question03Answer01.setCorrectAnswer(true);
			question03Sub01.addAnswer(question03Answer01);
		SubQuestion question03Sub02 = new MultiValueSubQuestion(question03.getText());
		question03.addSubQuestion(question03Sub02);
			Answer question03Answer02 = new MultiValueAnswer("not a valid numeric answer");
			question03Answer02.setCorrectAnswer(true);
			question03Sub02.addAnswer(question03Answer02);	
		SubQuestion question03Sub03 = new MultiValueSubQuestion(question03.getText());
		question03.addSubQuestion(question03Sub03);
			Answer question03Answer03 = new MultiValueAnswer("");
			question03Answer03.setCorrectAnswer(true);
			question03Sub03.addAnswer(question03Answer03);	
		return quiz;
	}
	
	private Quiz generateBigMultipleChoiceQuiz(String quizTitle) {
		Quiz quiz = new BasicQuiz();
		Question question01 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 01", /*pointsPossible*/ 3, /*sequenceNumber*/ 0);
		quiz.addQuestion(question01);
		SubQuestion question01Sub = new MultiValueSubQuestion(question01.getText());
		question01.addSubQuestion(question01Sub);
			Answer question01Answer01 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 01");
			question01.addAnswer(question01Answer01);
			Answer question01Answer02 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 02");
			question01Answer02.setCorrectAnswer(true);
			question01.addAnswer(question01Answer02);
			Answer question01Answer03 = new MultiValueAnswer(quizTitle + " - Question 01 - Answer 03");
			question01.addAnswer(question01Answer03);
		Question question02 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 02", /*pointsPossible*/ 3, /*sequenceNumber*/ 1);
		quiz.addQuestion(question02);
		SubQuestion question02Sub = new MultiValueSubQuestion(question02.getText());
		question02.addSubQuestion(question02Sub);
			Answer question02Answer01 = new MultiValueAnswer(quizTitle + " - Question 02 - Answer 01");
			question02Sub.addAnswer(question02Answer01);
			Answer question02Answer02 = new MultiValueAnswer(quizTitle + " - Question 02 - Answer 02");
			question02Sub.addAnswer(question02Answer02);
			Answer question02Answer03 = new MultiValueAnswer(quizTitle + " - Question 02 - Answer 03");
			question02Answer03.setCorrectAnswer(true);
			question02Sub.addAnswer(question02Answer03);
		Question question03 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 03", /*pointsPossible*/ 3, /*sequenceNumber*/ 2);
		quiz.addQuestion(question03);
		SubQuestion question03Sub = new MultiValueSubQuestion(question03.getText());
		question03.addSubQuestion(question03Sub);
			Answer question03Answer01 = new MultiValueAnswer(quizTitle + " - Question 03 - Answer 01");
			question03Answer01.setCorrectAnswer(true);
			question03Sub.addAnswer(question03Answer01);
			Answer question03Answer02 = new MultiValueAnswer(quizTitle + " - Question 03 - Answer 02");
			question03Sub.addAnswer(question03Answer02);
			Answer question03Answer03 = new MultiValueAnswer(quizTitle + " - Question 03 - Answer 03");
			question03Sub.addAnswer(question03Answer03);
		Question question04 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 04", /*pointsPossible*/ 3, /*sequenceNumber*/ 3);
		quiz.addQuestion(question04);
		SubQuestion question04Sub = new MultiValueSubQuestion(question04.getText());
		question04.addSubQuestion(question04Sub);
			Answer question04Answer01 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 01");
			question04Sub.addAnswer(question04Answer01);
			Answer question04Answer02 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 02");
			question04Answer02.setCorrectAnswer(true);
			question04Sub.addAnswer(question04Answer02);
			Answer question04Answer03 = new MultiValueAnswer(quizTitle + " - Question 04 - Answer 03");
			question04Sub.addAnswer(question04Answer03);
		Question question05 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 05", /*pointsPossible*/ 3, /*sequenceNumber*/ 4);
		quiz.addQuestion(question05);
		SubQuestion question05Sub = new MultiValueSubQuestion(question05.getText());
		question05.addSubQuestion(question05Sub);
			Answer question05Answer01 = new MultiValueAnswer(quizTitle + " - Question 05 - Answer 01");
			question05Sub.addAnswer(question05Answer01);
			Answer question05Answer02 = new MultiValueAnswer(quizTitle + " - Question 05 - Answer 02");
			question05Answer02.setCorrectAnswer(true);
			question05Sub.addAnswer(question05Answer02);
			Answer question05Answer03 = new MultiValueAnswer(quizTitle + " - Question 05 - Answer 03");
			question05Sub.addAnswer(question05Answer03);
		Question question06 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 06", /*pointsPossible*/ 3, /*sequenceNumber*/ 5);
		quiz.addQuestion(question06);
		SubQuestion question06Sub = new MultiValueSubQuestion(question06.getText());
		question06.addSubQuestion(question06Sub);
			Answer question06Answer01 = new MultiValueAnswer(quizTitle + " - Question 06 - Answer 01");
			question06Sub.addAnswer(question06Answer01);
			Answer question06Answer02 = new MultiValueAnswer(quizTitle + " - Question 06 - Answer 02");
			question06Answer02.setCorrectAnswer(true);
			question06Sub.addAnswer(question06Answer02);
			Answer question06Answer03 = new MultiValueAnswer(quizTitle + " - Question 06 - Answer 03");
			question06Sub.addAnswer(question06Answer03);
		Question question07 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 07", /*pointsPossible*/ 3, /*sequenceNumber*/ 6);
		quiz.addQuestion(question07);
		SubQuestion question07Sub = new MultiValueSubQuestion(question07.getText());
		question07.addSubQuestion(question07Sub);
			Answer question07Answer01 = new MultiValueAnswer(quizTitle + " - Question 07 - Answer 01");
			question07Sub.addAnswer(question07Answer01);
			Answer question07Answer02 = new MultiValueAnswer(quizTitle + " - Question 07 - Answer 02");
			question07Answer02.setCorrectAnswer(true);
			question07Sub.addAnswer(question07Answer02);
			Answer question07Answer03 = new MultiValueAnswer(quizTitle + " - Question 07 - Answer 03");
			question07Sub.addAnswer(question07Answer03);
		Question question08 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 08", /*pointsPossible*/ 3, /*sequenceNumber*/ 7);
		quiz.addQuestion(question08);
		SubQuestion question08Sub = new MultiValueSubQuestion(question08.getText());
		question08.addSubQuestion(question08Sub);
			Answer question08Answer01 = new MultiValueAnswer(quizTitle + " - Question 08 - Answer 01");
			question08Sub.addAnswer(question08Answer01);
			Answer question08Answer02 = new MultiValueAnswer(quizTitle + " - Question 08 - Answer 02");
			question08Answer02.setCorrectAnswer(true);
			question08Sub.addAnswer(question08Answer02);
			Answer question08Answer03 = new MultiValueAnswer(quizTitle + " - Question 08 - Answer 03");
			question08Sub.addAnswer(question08Answer03);
		Question question09 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 09", /*pointsPossible*/ 3, /*sequenceNumber*/ 8);
		quiz.addQuestion(question09);
		SubQuestion question09Sub = new MultiValueSubQuestion(question09.getText());
		question09.addSubQuestion(question09Sub);
			Answer question09Answer01 = new MultiValueAnswer(quizTitle + " - Question 09 - Answer 01");
			question09Sub.addAnswer(question09Answer01);
			Answer question09Answer02 = new MultiValueAnswer(quizTitle + " - Question 09 - Answer 02");
			question09Answer02.setCorrectAnswer(true);
			question09Sub.addAnswer(question09Answer02);
			Answer question09Answer03 = new MultiValueAnswer(quizTitle + " - Question 09 - Answer 03");
			question09Sub.addAnswer(question09Answer03);
		Question question10 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 10", /*pointsPossible*/ 3, /*sequenceNumber*/ 9);
		quiz.addQuestion(question10);
		SubQuestion question10Sub = new MultiValueSubQuestion(question10.getText());
		question10.addSubQuestion(question10Sub);
			Answer question10Answer01 = new MultiValueAnswer(quizTitle + " - Question 10 - Answer 01");
			question10Sub.addAnswer(question10Answer01);
			Answer question10Answer02 = new MultiValueAnswer(quizTitle + " - Question 10 - Answer 02");
			question10Answer02.setCorrectAnswer(true);
			question10Sub.addAnswer(question10Answer02);
			Answer question10Answer03 = new MultiValueAnswer(quizTitle + " - Question 10 - Answer 03");
			question10Sub.addAnswer(question10Answer03);
		Question question11 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 11", /*pointsPossible*/ 3, /*sequenceNumber*/ 10);
		quiz.addQuestion(question11);
		SubQuestion question11Sub = new MultiValueSubQuestion(question11.getText());
		question11.addSubQuestion(question11Sub);
			Answer question11Answer01 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 01");
			question11Sub.addAnswer(question11Answer01);
			Answer question11Answer02 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 02");
			question11Answer02.setCorrectAnswer(true);
			question11Sub.addAnswer(question11Answer02);
			Answer question11Answer03 = new MultiValueAnswer(quizTitle + " - Question 11 - Answer 03");
			question11Sub.addAnswer(question11Answer03);
		Question question12 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 12", /*pointsPossible*/ 3, /*sequenceNumber*/ 11);
		quiz.addQuestion(question12);
		SubQuestion question12Sub = new MultiValueSubQuestion(question12.getText());
		question12.addSubQuestion(question12Sub);
			Answer question12Answer01 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 01");
			question12Sub.addAnswer(question12Answer01);
			Answer question12Answer02 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 02");
			question12Answer02.setCorrectAnswer(true);
			question12Sub.addAnswer(question12Answer02);
			Answer question12Answer03 = new MultiValueAnswer(quizTitle + " - Question 12 - Answer 03");
			question12Sub.addAnswer(question12Answer03);
		Question question13 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 13", /*pointsPossible*/ 3, /*sequenceNumber*/ 12);
		quiz.addQuestion(question13);
		SubQuestion question13Sub = new MultiValueSubQuestion(question13.getText());
		question13.addSubQuestion(question13Sub);
			Answer question13Answer01 = new MultiValueAnswer(quizTitle + " - Question 13 - Answer 01");
			question13Sub.addAnswer(question13Answer01);
			Answer question13Answer02 = new MultiValueAnswer(quizTitle + " - Question 13 - Answer 02");
			question13Answer02.setCorrectAnswer(true);
			question13Sub.addAnswer(question13Answer02);
			Answer question13Answer03 = new MultiValueAnswer(quizTitle + " - Question 13 - Answer 03");
			question13Sub.addAnswer(question13Answer03);
		Question question14 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 14", /*pointsPossible*/ 3, /*sequenceNumber*/ 13);
		quiz.addQuestion(question14);
		SubQuestion question14Sub = new MultiValueSubQuestion(question14.getText());
		question14.addSubQuestion(question14Sub);
			Answer question14Answer01 = new MultiValueAnswer(quizTitle + " - Question 14 - Answer 01");
			question14Sub.addAnswer(question14Answer01);
			Answer question14Answer02 = new MultiValueAnswer(quizTitle + " - Question 14 - Answer 02");
			question14Answer02.setCorrectAnswer(true);
			question14Sub.addAnswer(question14Answer02);
			Answer question14Answer03 = new MultiValueAnswer(quizTitle + " - Question 14 - Answer 03");
			question14Sub.addAnswer(question14Answer03);
		Question question15 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 15", /*pointsPossible*/ 3, /*sequenceNumber*/ 14);
		quiz.addQuestion(question15);
		SubQuestion question15Sub = new MultiValueSubQuestion(question15.getText());
		question15.addSubQuestion(question15Sub);
			Answer question15Answer01 = new MultiValueAnswer(quizTitle + " - Question 15 - Answer 01");
			question15Sub.addAnswer(question15Answer01);
			Answer question15Answer02 = new MultiValueAnswer(quizTitle + " - Question 15 - Answer 02");
			question15Answer02.setCorrectAnswer(true);
			question15Sub.addAnswer(question15Answer02);
			Answer question15Answer03 = new MultiValueAnswer(quizTitle + " - Question 15 - Answer 03");
			question15Sub.addAnswer(question15Answer03);
		Question question16 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 16", /*pointsPossible*/ 3, /*sequenceNumber*/ 15);
		quiz.addQuestion(question16);
		SubQuestion question16Sub = new MultiValueSubQuestion(question16.getText());
		question16.addSubQuestion(question16Sub);
			Answer question16Answer01 = new MultiValueAnswer(quizTitle + " - Question 16 - Answer 01");
			question16Sub.addAnswer(question16Answer01);
			Answer question16Answer02 = new MultiValueAnswer(quizTitle + " - Question 16 - Answer 02");
			question16Answer02.setCorrectAnswer(true);
			question16Sub.addAnswer(question16Answer02);
			Answer question16Answer03 = new MultiValueAnswer(quizTitle + " - Question 16 - Answer 03");
			question16Sub.addAnswer(question16Answer03);
		Question question17 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 17", /*pointsPossible*/ 3, /*sequenceNumber*/ 16);
		quiz.addQuestion(question17);
		SubQuestion question17Sub = new MultiValueSubQuestion(question17.getText());
		question17.addSubQuestion(question17Sub);
			Answer question17Answer01 = new MultiValueAnswer(quizTitle + " - Question 17 - Answer 01");
			question17Sub.addAnswer(question17Answer01);
			Answer question17Answer02 = new MultiValueAnswer(quizTitle + " - Question 17 - Answer 02");
			question17Answer02.setCorrectAnswer(true);
			question17Sub.addAnswer(question17Answer02);
			Answer question17Answer03 = new MultiValueAnswer(quizTitle + " - Question 17 - Answer 03");
			question17Sub.addAnswer(question17Answer03);
		Question question18 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 18", /*pointsPossible*/ 3, /*sequenceNumber*/ 17);
		quiz.addQuestion(question18);
		SubQuestion question18Sub = new MultiValueSubQuestion(question18.getText());
		question18.addSubQuestion(question18Sub);
			Answer question18Answer01 = new MultiValueAnswer(quizTitle + " - Question 18 - Answer 01");
			question18Sub.addAnswer(question18Answer01);
			Answer question18Answer02 = new MultiValueAnswer(quizTitle + " - Question 18 - Answer 02");
			question18Answer02.setCorrectAnswer(true);
			question18Sub.addAnswer(question18Answer02);
			Answer question18Answer03 = new MultiValueAnswer(quizTitle + " - Question 18 - Answer 03");
			question18Sub.addAnswer(question18Answer03);
		Question question19 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 19", /*pointsPossible*/ 3, /*sequenceNumber*/ 18);
		quiz.addQuestion(question19);
		SubQuestion question19Sub = new MultiValueSubQuestion(question19.getText());
		question19.addSubQuestion(question19Sub);
			Answer question19Answer01 = new MultiValueAnswer(quizTitle + " - Question 19 - Answer 01");
			question19Sub.addAnswer(question19Answer01);
			Answer question19Answer02 = new MultiValueAnswer(quizTitle + " - Question 19 - Answer 02");
			question19Answer02.setCorrectAnswer(true);
			question19Sub.addAnswer(question19Answer02);
			Answer question19Answer03 = new MultiValueAnswer(quizTitle + " - Question 19 - Answer 03");
			question19Sub.addAnswer(question19Answer03);
		Question question20 = new MultiValueRadioButtonQuestion(quizTitle + " - Question 20", /*pointsPossible*/ 3, /*sequenceNumber*/ 19);
		quiz.addQuestion(question20);
		SubQuestion question20Sub = new MultiValueSubQuestion(question20.getText());
		question20.addSubQuestion(question20Sub);
			Answer question20Answer01 = new MultiValueAnswer(quizTitle + " - Question 20 - Answer 01");
			question20Sub.addAnswer(question20Answer01);
			Answer question20Answer02 = new MultiValueAnswer(quizTitle + " - Question 20 - Answer 02");
			question20Answer02.setCorrectAnswer(true);
			question20Sub.addAnswer(question20Answer02);
			Answer question20Answer03 = new MultiValueAnswer(quizTitle + " - Question 20 - Answer 03");
			question20Sub.addAnswer(question20Answer03);
			
		return quiz;
	}
	
	private Assignment getOneQuizWithWritingSpaceQuestion(){
		
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment();
		
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		
		//add Chapter to Assignment
		toReturn.addChapter(chapter);
		
		//instantiate ChapterQuiz & add writing space Question
		Quiz chapterQuiz = generateWritingSpaceQuiz("WritingSpaceQuiz", 10);
		chapterQuiz.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);
		
		return toReturn;
	}
	
	private Assignment getQuizWithFillInTheBlankAndNumericQuestions() {
		Assignment toReturn = new BasicAssignment();
		//instantiate (only) Chapter1
		Chapter chapter1 = new BasicChapter();
		chapter1.setLearningResourceSequenceNumber(0f); 
		//add Chapter1 to Assignment
		toReturn.addChapter(chapter1);
		//instantiate ChapterQuiz & add writing space Question
		Quiz chapterQuiz = generateFillInTheBlankAndNumericQuiz("Fill In The Blank Two Questions And A Numeric Question Quiz", 27);
		chapterQuiz.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
		//add ChapterQuiz to Chapter
		chapter1.setChapterQuiz(chapterQuiz);
		return toReturn;
	}
	private Assignment getQuizWithFillInTheBlankAndNumericQuestionsOnly() {
		Assignment toReturn = new BasicAssignment();
		//instantiate (only) Chapter1
		Chapter chapter1 = new BasicChapter();
		chapter1.setLearningResourceSequenceNumber(0f); 
		//add Chapter1 to Assignment
		toReturn.addChapter(chapter1);
		//instantiate ChapterQuiz & add writing space Question
		Quiz chapterQuiz = generateFillInTheBlankAndNumericOnlyQuiz("Fill In The Blank Two Questions And A Numeric Questions Only Quiz", 27);
		chapterQuiz.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
		//add ChapterQuiz to Chapter
		chapter1.setChapterQuiz(chapterQuiz);
		return toReturn;
	}
	
	private Assignment getTwoQuizzesWithWritingSpaceQuestion(){
		
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment();
		
		//instantiate (only) Chapter1
		Chapter chapter1 = new BasicChapter();
		chapter1.setLearningResourceSequenceNumber(0f); //TODO: check the validity of this
		
		//add Chapter1 to Assignment
		toReturn.addChapter(chapter1);
		
		//instantiate Chapter1Quiz & add writing space Question
		Quiz chapter1Quiz = generateWritingSpaceQuiz("WritingSpaceQuizDraft", 10);
		chapter1Quiz.setLearningResourceSequenceNumber(1f); //TODO: check the validity of this
		//add Chapter1Quiz to Chapter1
		chapter1.setChapterQuiz(chapter1Quiz);
		
		//instantiate (only) Chapter2
		Chapter chapter2 = new BasicChapter();
		chapter2.setLearningResourceSequenceNumber(2f); //TODO: check the validity of this
		//add Chapter2 to Assignment
		toReturn.addChapter(chapter2);
		//instantiate Chapter2Quiz & add writing space Question
		Quiz chapter2Quiz = generateWritingSpaceQuiz("WritingSpaceQuizDraft", 10);
		//set Chapter2Quiz question id to same as Chapter1Quiz question id
		chapter2Quiz.getQuestions().get(0).setId(chapter1Quiz.getQuestions().get(0).getId()); 
		//set Chapter2Quiz question lastSeedDateTime to same as Chapter2Quiz question lastSeedDateTime
		chapter2Quiz.getQuestions().get(0).setQuestionLastSeedDateTime(chapter1Quiz.getQuestions().get(0).getQuestionLastSeedDateTime());
		chapter2Quiz.setLearningResourceSequenceNumber(3f); //TODO: check the validity of this
		//add Chapter2Quiz to Chapter2
		chapter2.setChapterQuiz(chapter2Quiz);
		
		return toReturn;
		
	}
	
	private Quiz generateWritingSpaceQuiz(String quizTitle, float pointsPossible){
		Quiz quiz = new BasicQuiz();
		Question question = new WritingSpaceQuestion(quizTitle, /*pointsPossible*/ pointsPossible, /*sequenceNumber*/ 0);
		quiz.addQuestion(question);
		return quiz;
	}
	private Assignment getOneQuizWithSimpleWritingSharedQuiz(){
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment();
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(1f);
		//add Chapter to Assignment
		toReturn.addChapter(chapter);
		//instantiate ChapterQuiz & add writing space Question
		Quiz chapterQuiz = generateSimpleWritingSharedQuiz("Simpe Writing Shared Writing Quiz", 10);
		chapterQuiz.setLearningResourceSequenceNumber(1f); //??????
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);	
		return toReturn;
	}
	
	private Assignment getOneQuizWithSimpleWritingJournalQuiz() {
		//instantiate Assignment
		Assignment toReturn = new BasicAssignment();
		//instantiate (only) Chapter
		Chapter chapter = new BasicChapter();
		chapter.setLearningResourceSequenceNumber(0f);
		//add Chapter to Assignment
		toReturn.addChapter(chapter);
		
		//instantiate ChapterQuiz & add writing space Question
		Quiz chapterQuiz = generateSimpleWritingJournalQuiz("Simpe Writing Journal Writing Quiz");
		chapterQuiz.setLearningResourceSequenceNumber(1f); //??????
		//add ChapterQuiz to Chapter
		chapter.setChapterQuiz(chapterQuiz);
				
		return toReturn;
	}
	
	
	private Quiz generateSimpleWritingSharedQuiz(String quizTitle, float pointsPossible){
		Quiz quiz = new BasicQuiz();
		Question question = new SimpleWritingSharedWritingQuestion(quizTitle, /*pointsPossible*/ pointsPossible, /*sequenceNumber*/ 0);
		quiz.addQuestion(question);
		return quiz;
	}
	
	private Quiz generateSimpleWritingJournalQuiz(String quizTitle){
		Quiz quiz = new BasicQuiz();
		Question question = new SimpleWritingJournalWritingQuestion(quizTitle, /* sequenceNumber */0);
		quiz.addQuestion(question);
		return quiz;
	}

}
	