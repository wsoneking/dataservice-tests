package com.pearson.test.daalt.dataservice.request;

import com.pearson.test.daalt.dataservice.request.action.TestAction;

public interface TestDataRequest {
	void addTestAction(TestAction action);
	void executeAllActions() throws Exception;
	void setSeerMessageCount(int i);
	void setSubPubMessageCount(int i);
}
