package com.pearson.test.daalt.dataservice.response.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

public class AssessmentObject implements ModelWithContract, ModelWithLinks, ModelWithId{

	public String assessmentId;
	public String assessmentType;
	public boolean assessmentSeeded;
	public String assessmentLastSeedDateTime;
	public String assessmentLastSeedType;
	public AssessmentItemObject[] assessmentItems;
	public Map<String, LinkObject> _links;
	
	@Override
	@JsonIgnore
	public boolean isContractValid() {
		String[] requiredStrings = new String[] { assessmentId,
				assessmentType, assessmentLastSeedDateTime,
				assessmentLastSeedType};
		
		boolean contractIsValid = true;

		for (String s : requiredStrings) {
			if (s == null || "".equals(s)) {
				contractIsValid = false;
			}
		}
		
		if (_links != null) {
			for (LinkObject link : _links.values()) {
				if (!link.isContractValid()) {
					contractIsValid = false;
				}
			}
		}
		
		for (AssessmentItemObject assessmentItem : assessmentItems) {
			if (!assessmentItem.isContractValid()) {
				contractIsValid = false;
			}
		}
		return contractIsValid;
	}

	@JsonIgnore
	public AssessmentItemObject getAssessmentItemByItemId(String itemId) {
		AssessmentItemObject toReturn = null;
		for (int i=0; i<assessmentItems.length; i++) {
			if (assessmentItems[i].itemId.compareTo(itemId) == 0) {
				toReturn = assessmentItems[i];
			}
		}
		return toReturn;
	}

	@Override
	@JsonIgnore
	public String getId() {
		return assessmentId;
	}

	@JsonIgnore
	public Map<String, LinkObject> getLinks() {
		return _links;
	}
	
	public void setLinks(Map<String, LinkObject> links) {
		this._links = links;
	}
}
