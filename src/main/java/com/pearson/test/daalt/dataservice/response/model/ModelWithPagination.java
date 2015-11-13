package com.pearson.test.daalt.dataservice.response.model;

import java.util.List;


public interface ModelWithPagination {
	
	public int getItemCount();
	public int getLimit();
	public int getOffset();
	public List<ModelWithLinks> getItemsAsModelsWithLinks();

}
