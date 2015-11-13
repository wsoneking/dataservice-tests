package com.pearson.test.daalt.dataservice.response.model;

import java.util.Map;

import org.testng.Reporter;

public abstract class DaaltCollectionObject implements ModelWithLinks, ModelWithPagination {

	public int offset;
	public int itemCount;
	public int limit;
	public Map<String, LinkObject> _links;

	public boolean isLinkContractValid() {
		boolean result = true;
		for (LinkObject link : _links.values()) {
			if (!link.isContractValid()) {

				Reporter.log("The contract for one of the link objects in the learning resource collection is invalid");

				result = false;
			}
		}

		return result;
	}

	public Map<String, LinkObject> getLinks() {
		return _links;
	}

	public void setLinks(Map<String, LinkObject> links) {
		this._links = links;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
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

}
