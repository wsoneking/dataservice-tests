package com.pearson.test.daalt.dataservice.response.model;

public class ChildAssetModel {

	private String item_id;
	private String item_href;
	private String asset_id;
	private String parent_asset_id;
	private String asset_type;
	private String asset_sub_type;
	private String title;
	private Float points;
	private String course_id;
	private String thumbnail;
	private String background_image;
	private ChildAssetModel[] children;	
	private Boolean is_required;
	private Integer duration_in_minutes;
	private Boolean visible;
	private AssessmentModel[] assessments;
	
	private String assignment_id;
	private Long due_date;
	private Boolean include;
	private Boolean excluded;
	private String revel_href;
	private Integer level;
	private Boolean locked;
	private Boolean isLocked;
	private Boolean merge_metadata_map;
	private String parent_item_id;
	private Integer seq_nbr;
	private String type;
	private String url;
	private TempMetaModel tempMeta;
	
	
	
	/*
	"level": 99,
    "item_id": "navpoint436",
    "parent_item_id": "navpoint434",
    "type": "unknown",
    "item_href": "d06310b0-f660-4a14-9b53-9d623ad85b84/300/file/pearson_ciccarelli_v11/OPS/text/chapter-06/quiz-1/quiz-assignment-02.xhtml",
    "seq_nbr": 436,
    "include": true,
    "tempMeta": {
        "origInclude": false
    },
    "due_date": 1406689200000
	 */


	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
	
	public void setCourse_id_recursive(String course_id) {
		this.course_id = course_id;
		if (children != null) {
			for (ChildAssetModel childAsset : children) {
				childAsset.setCourse_id_recursive(course_id);
			}
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getAsset_id() {
		return asset_id;
	}

	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}

	public String getAsset_type() {
		return asset_type;
	}

	public void setAsset_type(String asset_type) {
		this.asset_type = asset_type;
	}

	public String getAsset_sub_type() {
		return asset_sub_type;
	}

	public void setAsset_sub_type(String asset_sub_type) {
		this.asset_sub_type = asset_sub_type;
	}

	public String getAssignment_id() {
		return assignment_id;
	}

	public void setAssignment_id(String assignment_id) {
		this.assignment_id = assignment_id;
	}
	
	public Float getPoints() {
		return points;
	}

	public void setPoints(Float points) {
		this.points = points;
	}

	public Boolean getIs_required() {
		return is_required;
	}

	public void setIs_required(Boolean is_required) {
		this.is_required = is_required;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean isLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean isIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Boolean getMerge_metadata_map() {
		return merge_metadata_map;
	}

	public void setMerge_metadata_map(Boolean merge_metadata_map) {
		this.merge_metadata_map = merge_metadata_map;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getParent_item_id() {
		return parent_item_id;
	}

	public void setParent_item_id(String parent_item_id) {
		this.parent_item_id = parent_item_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSeq_nbr() {
		return seq_nbr;
	}

	public void setSeq_nbr(Integer seq_nbr) {
		this.seq_nbr = seq_nbr;
	}

	public Boolean getInclude() {
		return include;
	}

	public void setInclude(Boolean include) {
		this.include = include;
	}
	
	

	public Boolean getExcluded() {
		return excluded;
	}

	public void setExcluded(Boolean excluded) {
		this.excluded = excluded;
	}

	public TempMetaModel getTempMeta() {
		return tempMeta;
	}

	public void setTempMeta(TempMetaModel tempMeta) {
		this.tempMeta = tempMeta;
	}

	public Long getDue_date() {
		return due_date;
	}

	public void setDue_date(Long due_date) {
		this.due_date = due_date;
	}
	
	public void setDue_dateRecursive(Long due_date) {
		if (this.due_date != null) {
			this.due_date = due_date;
		}
		if (children != null) {
			for (ChildAssetModel childAsset : children) {
				childAsset.setDue_dateRecursive(due_date);
			}
		}
	}

	public ChildAssetModel[] getChildren() {
		return children;
	}

	public void setChildren(ChildAssetModel[] children) {
		this.children = children;
	}

	public String getBackground_image() {
		return background_image;
	}

	public void setBackground_image(String background_image) {
		this.background_image = background_image;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public String getItem_href() {
		return item_href;
	}

	public void setItem_href(String item_href) {
		this.item_href = item_href;
	}

	public String getRevel_href() {
		return revel_href;
	}

	public void setRevel_href(String revel_href) {
		this.revel_href = revel_href;
	}

	public String getParent_asset_id() {
		return parent_asset_id;
	}

	public void setParent_asset_id(String parent_asset_id) {
		this.parent_asset_id = parent_asset_id;
	}

	public Integer getDuration_in_minutes() {
		return duration_in_minutes;
	}

	public void setDuration_in_minutes(Integer duration_in_minutes) {
		this.duration_in_minutes = duration_in_minutes;
	}

	public AssessmentModel[] getAssessments() {
		return assessments;
	}

	public void setAssessments(AssessmentModel[] assessments) {
		this.assessments = assessments;
	}
}

