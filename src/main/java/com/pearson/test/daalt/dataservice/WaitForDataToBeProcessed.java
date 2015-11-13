package com.pearson.test.daalt.dataservice;

import org.testng.annotations.Test;

public class WaitForDataToBeProcessed {

	@Test(timeOut = 60000000)
	public void waitForData() throws InterruptedException {
		int numSeconds = TestEngine.getInstance().getWaitTimeSeconds();

		System.out.println("Waiting " + numSeconds
				+ " seconds for created data to become visible");

		int secondCounter = 0;

		while (secondCounter < numSeconds) {
			Thread.sleep(1000);
			secondCounter++;

			System.out.println(secondCounter + " seconds have passed "
					+ String.valueOf(numSeconds - secondCounter)
					+ " seconds to go");

		}

		System.out.println("Waiting Completed");
	}

}
