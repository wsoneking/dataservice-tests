package com.pearson.test.daalt.dataservice.response.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

public class AssessmentItemObject implements ModelWithContract{

	public String itemId;
	public float itemSequence;
	public String questionType;
	public String questionPresentationFormat;
	public String questionText;
	public boolean itemSeeded;
	public String itemLastSeedDateTime;
	public String itemLastSeedType;
	
	public Map<String, LinkObject> _links;
	
	@Override
	@JsonIgnore
	public boolean isContractValid() {
		
		String[] requiredStrings = new String[] { itemId, questionType, questionPresentationFormat,
				questionText, itemLastSeedDateTime, itemLastSeedType};
		
		float[] requiredFloats = new float[] {  itemSequence };
		
		boolean contractIsValid = true;
		
		for (String s : requiredStrings) {
			if (s == null || "".equals(s)) {
				contractIsValid = false;
			}
		}
		
		for (float d : requiredFloats) {
			if (d < 0) {
				contractIsValid = false;
			}
		}
		
		return contractIsValid;
	}
	
}
