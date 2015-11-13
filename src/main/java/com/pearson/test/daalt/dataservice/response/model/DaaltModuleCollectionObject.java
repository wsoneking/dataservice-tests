package com.pearson.test.daalt.dataservice.response.model;

import java.util.LinkedList;
import java.util.List;
import org.testng.Reporter;

public class DaaltModuleCollectionObject extends DaaltCollectionObject
		implements ModelWithContract, ModelWithLinks, ModelWithPagination {

	public EmbeddedModuleCollectionObject _embedded;

	public DaaltModuleCollectionObject() {

	}

	public ModuleObject[] getItems() {
		if (_embedded == null || _embedded.pageItems == null) {
			return new ModuleObject[] {};
		}
		return _embedded.pageItems;
	}

	public List<ModelWithLinks> getItemsAsModelsWithLinks() {
		List<ModelWithLinks> newItems = new LinkedList<ModelWithLinks>();

		for (ModuleObject item : getItems()) {
			newItems.add((ModelWithLinks) item);
		}
		return newItems;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public boolean isContractValid() {

		return isOffsetItemCountAndLimitCorrect() && isContractValidForItems()
				&& isOffsetItemCountAndLimitCorrect();

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
		for (ModuleObject item : getItems()) {
			if (!item.isContractValid()) {
				Reporter.log("The contract for one of the learning resource objects in the learning resource collection is invalid");

				result = false;
			}
		}

		return result;

	}

}
