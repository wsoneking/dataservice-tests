package com.pearson.test.daalt.dataservice.request.action;

public interface TestAction {
	public void execute(int seerCount, int subPubCount) throws Exception;
	public void checkCriticalObjects() throws InvalidStateException;
}
