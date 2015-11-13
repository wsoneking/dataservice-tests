package com.pearson.test.daalt.dataservice.response.model;

import java.util.List;

public interface ModelComparable {

	public List<Difference> getDifferenceBetweenObjects(
			ModelComparable compareObject);

}
