package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.testng.Reporter;

public class DaaltAssessmentObject extends DaaltCollectionObject
		implements ModelWithContract, ModelWithPagination {
	
	public EmbeddedAssessmentObject _embedded;

	public DaaltAssessmentObject(){ }
	
	public List<ModelWithLinks> getItemsAsModelsWithLinks() {
		List<ModelWithLinks> newItems = new LinkedList<ModelWithLinks>();
		AssessmentObject assessment = getItems()[0];
		newItems.add((ModelWithLinks)assessment);
		return newItems;
	}

	public AssessmentObject[] getItems() {
		if (_embedded == null || _embedded.pageItems == null) {
			return new AssessmentObject[] {};
		}
		return _embedded.pageItems;
	}
	
	@Override
	public boolean isContractValid() {
		return isOffsetItemCountAndLimitCorrect() && isContractValidForItems();
	}
	
	// Contract functions
	@JsonIgnore
	public boolean isOffsetItemCountAndLimitCorrect() {

		boolean result = true;
		
		// item count can't be negative and greater than 1
		if (itemCount < 0 && itemCount > 1) {
			result = false;
		}
		return result;

	}
	
	@JsonIgnore
	public boolean isContractValidForItems() {
		boolean result = true;
		AssessmentObject assessment = getItems()[0];
		if (!assessment.isContractValid()) {
			Reporter.log("The contract for assessment object is invalid");
			result = false;
		}
		return result;
	}

}
