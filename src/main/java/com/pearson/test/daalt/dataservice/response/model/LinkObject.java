package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.testng.Reporter;

public class LinkObject implements ModelWithContract, ModelComparable {

	public String rel;
	public String href;

	@Override
	@JsonIgnore
	public boolean isContractValid() {

		Reporter.log("The href of the link is :" + href);
		Reporter.log("The value of rel is :" + rel);

		boolean contractIsValid = true;
		String[] requiredFields = new String[] { href };

		for (String s : requiredFields) {
			if (s == null || "".equals(s)) {
				contractIsValid = false;
			}
		}

		return contractIsValid;
	}
	
	@Override
	public boolean equals(Object other) {
		boolean equal = true;
		if (!(other instanceof LinkObject)) {
			equal = false;
		} else {
			LinkObject otherLink = (LinkObject) other;
			equal = this.href.equals(otherLink.href);
		}
		return equal;
	}

	@Override
	public List<Difference> getDifferenceBetweenObjects(
			ModelComparable compareObject) {
		List<Difference> differences = new LinkedList<Difference>();

		// If its not the same type of object stop
		LinkObject actualObject = null;
		try {
			actualObject = (LinkObject) compareObject;
		} catch (Exception e) {
			differences.add(new Difference("Objects are not the same type",
					"Expected comparison object to be "
							+ this.getClass().toString(), compareObject
							.getClass().toString()));
			return differences;
		}

		differences.addAll(getDifferencesBetweenProperties(actualObject));
		return differences;
	}

	private List<Difference> getDifferencesBetweenProperties(
			LinkObject compareObject) {

		List<Difference> differences = new LinkedList<Difference>();



		if (!this.href.equals(compareObject.href)) {
			differences.add(new Difference("href does not match", this.href,
					compareObject.href));
		}

		return differences;
	}

}
