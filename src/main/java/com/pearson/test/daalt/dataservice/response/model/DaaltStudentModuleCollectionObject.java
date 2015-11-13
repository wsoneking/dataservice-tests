package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.testng.Reporter;

public class DaaltStudentModuleCollectionObject extends DaaltCollectionObject
		implements ModelWithContract, ModelWithLinks, ModelWithPagination {

	public EmbeddedStudentModuleCollectionObject _embedded;

	public DaaltStudentModuleCollectionObject() {

	}

	public StudentModuleObject[] getItems() {
		if (_embedded == null || _embedded.pageItems == null) {
			return new StudentModuleObject[] {};
		}
		return _embedded.pageItems;
	}

	public List<ModelWithLinks> getItemsAsModelsWithLinks() {
		List<ModelWithLinks> newItems = new LinkedList<ModelWithLinks>();

		for (StudentModuleObject item : getItems()) {
			newItems.add((ModelWithLinks) item);
		}
		return newItems;
	}

	@Override
	public boolean isContractValid() {

		return isOffsetItemCountAndLimitCorrect() && isContractValidForItems()
				&& isLinkContractValid();

	}

	// Contract functions
	public boolean isOffsetItemCountAndLimitCorrect() {

		boolean result = true;

		// offset and limit and item count can't be negative
		if (offset < 0 || limit < 0 || itemCount < 0) {
			result = false;
		}

		// number of items should never exceed limit
		if (getItems().length > limit) {
			result = false;
		}

		// number of items should never exceed item count
		if (getItems().length > itemCount) {
			result = false;
		}

		return result;

	}

	public boolean isContractValidForItems() {
		boolean result = true;
		for (StudentModuleObject item : getItems()) {
			if (!item.isContractValid()) {
				Reporter.log("The contract for one of the learning resource objects in the learning resource collection is invalid");

				result = false;
			}
		}

		return result;

	}	
	
	@JsonIgnore
	public StudentModuleObject getModuleWithId(String moduleId) {

		StudentModuleObject returnModule = null;
		for (StudentModuleObject module : getItems()) {
			if (module.getId().equals(moduleId)) {
				returnModule = module;
			}
		}

		return returnModule;
	}
}
