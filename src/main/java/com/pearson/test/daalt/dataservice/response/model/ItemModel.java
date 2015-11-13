package com.pearson.test.daalt.dataservice.response.model;

public class ItemModel {
	private String title;
	private String url;
	private String thumbnail;
	private boolean locked;
	private int level;
	private String item_id;
	private String parent_item_id;
	private String type;
	private String sub_type;
	private String item_href;
	private int seq_nbr;
	private ItemModel[] children;
	private AssessmentModel[] assessments;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}
	
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getItem_href() {
		return item_href;
	}
	
	public void setItem_href(String item_href) {
		this.item_href = item_href;
	}
	
	public int getSeq_nbr() {
		return seq_nbr;
	}
	
	public void setSeq_nbr(int seq_nbr) {
		this.seq_nbr = seq_nbr;
	}
	
	public ItemModel[] getChildren() {
		return children;
	}
	
	public void setChildren(ItemModel[] children) {
		this.children = children;
	}
	
	public AssessmentModel[] getAssessments() {
		return assessments;
	}
	
	public void setAssessments(AssessmentModel[] assessments) {
		this.assessments = assessments;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public String getParent_item_id() {
		return parent_item_id;
	}

	public void setParent_item_id(String parent_item_id) {
		this.parent_item_id = parent_item_id;
	}

	public String getSub_type() {
		return sub_type;
	}

	public void setSub_type(String sub_type) {
		this.sub_type = sub_type;
	} 
	
	
}
