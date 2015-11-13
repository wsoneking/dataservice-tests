package com.pearson.test.daalt.dataservice.scenario;

import java.util.Calendar;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.pearson.qa.apex.dataobjects.UserObject;
import com.pearson.qa.apex.dataobjects.UserType;
import com.pearson.test.daalt.dataservice.BaseTestUtility;
import com.pearson.test.daalt.dataservice.TestCase;
import com.pearson.test.daalt.dataservice.TestEngine;
import com.pearson.test.daalt.dataservice.model.Answer;
import com.pearson.test.daalt.dataservice.model.MultiValueAnswer;
import com.pearson.test.daalt.dataservice.model.MultiValueAttempt;
import com.pearson.test.daalt.dataservice.model.MultiValueSubAttempt;
import com.pearson.test.daalt.dataservice.model.Question;
import com.pearson.test.daalt.dataservice.model.SubQuestion;
import com.pearson.test.daalt.dataservice.model.User;
import com.pearson.test.daalt.dataservice.request.action.SubPubSeerTestActionFactory;
import com.pearson.test.daalt.dataservice.request.action.TestActionFactory;

public class BaseTestScenario extends BaseTestUtility {	
	private TestCase currentTestCase;
	private UserObject defaultProf;
	@JsonIgnore
	protected String dateFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	protected TestActionFactory testActionFactory;
	
	@BeforeMethod(alwaysRun = true)
	public void initializeTestBasic() {
		System.out.println("Initializing Tests");
		testActionFactory = new SubPubSeerTestActionFactory(); 
		getEngine().incrementCurrentTest();
		TestCase newTest = new TestCase();
		newTest.setId(String.valueOf(getEngine().getCurrentTest()));
		setCurrentTestCase(newTest);
		getCurrentTestCase().setActingUser(getDefaultProf());

	}

	@AfterMethod(alwaysRun = true)
	public void addTestBasic() throws Exception {
		System.out.println("AddingTest To Be Validated");
		if (getCurrentTestCase().getActingUser() == null) {
			getCurrentTestCase().setActingUser(getDefaultProf());
		}

		getEngine().getSuite().getTestCases().add(getCurrentTestCase());
		getEngine().saveSuite();
	}
	
	public TestCase getCurrentTestCase() {
		return currentTestCase;
	}

	public void setCurrentTestCase(TestCase currentTest) {
		this.currentTestCase = currentTest;
	}
	
	public UserObject getDefaultProf() {
		if (defaultProf == null) {
			com.pearson.test.daalt.dataservice.User instrFromConfig = getEngine().getInstructor();
			defaultProf = new UserObject(instrFromConfig.getUserName(), 
					instrFromConfig.getPassword(),
					UserType.Professor, getEngine().getTestEnvType());
			defaultProf.setId(instrFromConfig.getId());
		}
		return defaultProf;
	}
	
	public MultiValueAttempt getMultiValueRadioButtonAttempt(Question question, User user, long timeSpent, int attemptNumber, 
			Answer answer, float pointsEarned, boolean isFinalAttempt){
		MultiValueAttempt attempt = new MultiValueAttempt();
		MultiValueSubAttempt subAttempt = new MultiValueSubAttempt();
		subAttempt.setSubQuestion(question.getSubQuestions().get(0));
		attempt.addSubAttempt(subAttempt);
		subAttempt.addAnswer(answer);
		attempt.setUser(user);
		attempt.setTimeSpent(timeSpent);
		attempt.setAttemptNumber(attemptNumber);
		attempt.setPointsEarned(pointsEarned);
		attempt.setFinalAttempt(isFinalAttempt);
		return attempt;
	}
	
	public MultiValueAttempt getCorrectFillInTheBlankOrNumericAttempt(Question question, User user, long timeSpent, int attemptNumber, 
			float pointsEarned, boolean isFinalAttempt, List<String> studentAnswers){
		MultiValueAttempt attempt = new MultiValueAttempt();
		attempt.setForFillInTheBlankOrNumeric(true);
		attempt.setUser(user);
		attempt.setTimeSpent(timeSpent);
		attempt.setAttemptNumber(attemptNumber);
		attempt.setPointsEarned(pointsEarned);
		attempt.setFinalAttempt(isFinalAttempt);
		
		List<SubQuestion> subQuestions = question.directAccessSubQuestionList();
		int index = 0;
		for (SubQuestion subQuestion : subQuestions) {
			Answer subQuestionCorrectAnswer = subQuestion.getAnswers().get(0);
			MultiValueSubAttempt subAttempt = new MultiValueSubAttempt();
			subAttempt.setSubQuestion(subQuestion);
			attempt.addSubAttempt(subAttempt);
			Answer subQuestionAnswer = new MultiValueAnswer("n/a");
			subQuestionAnswer.setStudentEnteredText(studentAnswers.get(index));
			subQuestionAnswer.setId(subQuestionCorrectAnswer.getId());
			subQuestionAnswer.setCorrectAnswer(subQuestionCorrectAnswer.isCorrectAnswer());
			subAttempt.addAnswer(subQuestionAnswer);
			index++;
		}
		return attempt;
	}	
	
	public MultiValueAttempt getMultiValueAttemptComplete(Question question, User user, long timeSpent, int attemptNumber, 
			float pointsEarned, boolean isFinalAttempt, CorrectOrIncorrectLibrary CorrectOrIncorrect){
		MultiValueAttempt attempt = new MultiValueAttempt();
		
		attempt.setUser(user);
		attempt.setTimeSpent(timeSpent);
		attempt.setAttemptNumber(attemptNumber);
		attempt.setPointsEarned(pointsEarned);
		attempt.setFinalAttempt(isFinalAttempt);
		
		switch(CorrectOrIncorrect) {
			case CORRECTLY:
				for (SubQuestion subQ : question.getSubQuestions()){
					MultiValueSubAttempt subAttempt = new MultiValueSubAttempt();
					subAttempt.setSubQuestion(subQ);
					for (Answer ans : subQ.getCorrectAnswer().getAnswers()){
						subAttempt.addAnswer(ans);
					}
					attempt.addSubAttempt(subAttempt);
				}
				break;
			case INCOMPLETECORRECTANSWERS:
				for (SubQuestion subQ : question.getSubQuestions()){
					MultiValueSubAttempt subAttempt = new MultiValueSubAttempt();
					subAttempt.setSubQuestion(subQ);
					for (Answer ans : subQ.getIncompleteCorrectAnswer().getAnswers()){
						subAttempt.addAnswer(ans);
					}
					attempt.addSubAttempt(subAttempt);
				}
				break;
			case INCOMPLETECORRECTANSWERS_FIRST_SUB_CORRECT:
				int i = 1;
				for (SubQuestion subQ : question.getSubQuestions()){
					MultiValueSubAttempt subAttempt = new MultiValueSubAttempt();
					subAttempt.setSubQuestion(subQ);
					if (i == 1) {
						for (Answer ans : subQ.getCorrectAnswer().getAnswers()){
							subAttempt.addAnswer(ans);
						}
						i ++;
					} else {
						for (Answer ans : subQ.getIncompleteCorrectAnswer().getAnswers()){
							subAttempt.addAnswer(ans);
						}
					}
					attempt.addSubAttempt(subAttempt);
				}
				break;
			case SINGLEINCORRECTANSWER:
				for (SubQuestion subQ : question.getSubQuestions()){
					MultiValueSubAttempt subAttempt = new MultiValueSubAttempt();
					subAttempt.setSubQuestion(subQ);
					for (Answer ans : subQ.getSingleIncorrectAnswer().getAnswers()){
						subAttempt.addAnswer(ans);
					}
					attempt.addSubAttempt(subAttempt);
				}
				break;
			case SINGLEINCORRECTANSWER_FIRST_SUB_CORRECT:
				int j = 1;
				for (SubQuestion subQ : question.getSubQuestions()){
					MultiValueSubAttempt subAttempt = new MultiValueSubAttempt();
					subAttempt.setSubQuestion(subQ);
					if (j == 1) {
						for (Answer ans : subQ.getCorrectAnswer().getAnswers()){
							subAttempt.addAnswer(ans);
						}
						j ++;
					} else {
						for (Answer ans : subQ.getSingleIncorrectAnswer().getAnswers()){
							subAttempt.addAnswer(ans);
						}
					}
					attempt.addSubAttempt(subAttempt);
				}
				break;
			case MULTIPOLEINCORRECTANSWERS:
				for (SubQuestion subQ : question.getSubQuestions()){
					MultiValueSubAttempt subAttempt = new MultiValueSubAttempt();
					subAttempt.setSubQuestion(subQ);
					for (Answer ans : subQ.getMultipleIncorrectAnswer().getAnswers()){
						subAttempt.addAnswer(ans);
					}
					attempt.addSubAttempt(subAttempt);
				}
				break;
		}
		
		return attempt;
	}
	
	public Calendar createDueDate() {

		Calendar nowCal = Calendar.getInstance();
		nowCal.set(Calendar.YEAR, Integer.parseInt(getConfig().getValue(TestEngine.DueDate_YEAR)));
		nowCal.set(Calendar.MONTH, Integer.parseInt(getConfig().getValue(TestEngine.DueDate_MONTH)));
		nowCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(getConfig().getValue(TestEngine.DueDate_DAY)));
		nowCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getConfig().getValue(TestEngine.DueDate_Hour)));
		nowCal.set(Calendar.MINUTE, Integer.parseInt(getConfig().getValue(TestEngine.DueDate_Minute)));
		nowCal.set(Calendar.SECOND, Integer.parseInt(getConfig().getValue(TestEngine.DueDate_Second)));
		nowCal.set(Calendar.MILLISECOND, Integer.parseInt(getConfig().getValue(TestEngine.DueDate_Millisecond)));
//		nowCal.set(Calendar.ZONE_OFFSET, Integer.parseInt(getConfig().getValue(TestEngine.DueDate_Zone)));
		
		return nowCal;
	}
	
	public enum CorrectOrIncorrectLibrary {
		CORRECTLY,
		INCOMPLETECORRECTANSWERS,
		SINGLEINCORRECTANSWER,
		MULTIPOLEINCORRECTANSWERS,
		INCOMPLETECORRECTANSWERS_FIRST_SUB_CORRECT,
		SINGLEINCORRECTANSWER_FIRST_SUB_CORRECT
	}
	
//	public String getFormattedDateString(Date date) {
//		return new SimpleDateFormat(dateFormatString).format(date);
//	}
//		
}
