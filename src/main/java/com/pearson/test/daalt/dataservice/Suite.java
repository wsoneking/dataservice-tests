package com.pearson.test.daalt.dataservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.testng.Reporter;

@Root(name = "Suite")
public class Suite {

	private String id;
	private String suiteName;
	private Boolean didCreationTestsComplete = true;
	private String date;
	private Date timestamp;

	

	private List<TestCase> testCases;
	
	public Suite() {
		setDate();
		setTimestamp();
	}

	@Attribute(name = "id")
	public String getId() {
		return id;
	}

	@Attribute(name = "id")
	public void setId(String id) {
		this.id = id;
	}

	@ElementList(name = "TestCases")
	public List<TestCase> getTestCases() {
		if (testCases == null) {
			testCases = new LinkedList<TestCase>();
		}
		return testCases;
	}

	@ElementList(name = "TestCases")
	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public Boolean getDidCreationTestsComplete() {
		return didCreationTestsComplete;
	}

	public void setDidCreationTestsComplete(Boolean didCreationTestsComplete) {
		if (didCreationTestsComplete == false) {
			Reporter.log("<p>Error occurred upstream in loading, or creating data</p>");
		}
		this.didCreationTestsComplete = didCreationTestsComplete;
	}

	public String getDate() {
		return date;
	}

	public void setDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS z");
		this.date = dateFormat.format(new Date());
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp() {
		this.timestamp = new Date();
	}

}
