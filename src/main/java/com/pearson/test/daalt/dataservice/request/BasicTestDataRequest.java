package com.pearson.test.daalt.dataservice.request;

import java.util.ArrayList;
import java.util.List;

import com.pearson.test.daalt.dataservice.request.action.TestAction;

public class BasicTestDataRequest implements TestDataRequest {
	private List<TestAction> testActionsList;
	private int subPubMessageCount = 4;
	private int seerMessageCount = 4;
	@Override
	public void addTestAction(TestAction action) {
		if(testActionsList == null) {
			testActionsList = new ArrayList<>();
		}
		testActionsList.add(action);
	}
	
	@Override
	public void executeAllActions() throws Exception {
		for (TestAction action : testActionsList) {
			action.execute(seerMessageCount,subPubMessageCount);
		}
	}

	public int getSubPubMessageCount() {
		return subPubMessageCount;
	}

	public void setSubPubMessageCount(int subPubMessageCount) {
		this.subPubMessageCount = subPubMessageCount;
	}

	public int getSeerMessageCount() {
		return seerMessageCount;
	}

	public void setSeerMessageCount(int seerMessageCount) {
		this.seerMessageCount = seerMessageCount;
	}
}
