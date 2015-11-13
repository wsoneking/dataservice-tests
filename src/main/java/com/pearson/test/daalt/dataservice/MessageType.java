package com.pearson.test.daalt.dataservice;

public enum MessageType {
	ASSESSMENT("Assessment", "PUBLISH"),
	ASSESSMENT_ITEM_TYPE("Assessment_Item_Type", "brix.assessment-item-type.create"),
	ASSESSMENT_PERFORMANCE("Assessment_Performance", "revel.assessment.student.performance.create"),
	COURSE_SECTION("Course_Section", "grid.daalt.transform.section.created"),
	COURSE_SECTION_TO_LEARNING_RESOURCE("Course_Section_To_Learning_Resource", "revel.course.lr.create"),
	LEARNING_MODULE("Learning_Module", "revel.lm.create"),
	LEARNING_MODULE_CONTENT("Learning_Module_Content", "revel.lm.content.create"),
	LEARNING_RESOURCE("Learning_Resource", "revel.lr.create"),
	MULTIPLE_CHOICE_QUESTION_STUDENT_ANSWERED("Multiple_Choice_Question_Student_Answered", ""),
	PERSON("Person", "grid.daalt.transform.identityprofile.created"),
	PERSON_COURSE_SECTION("Person_Course_Section", "grid.daalt.transform.courseregistration.created"),
	PRODUCT("Product", "revel.product.create"),
	PRODUCT_TO_COURSE_SECTION_PLT("Product_To_Course_Section_PLT", "revel.product.course.create"),
	STUDENT_UNLOADS_CONTENT("Student_Unloads_Content", "");
	
	private String name;
	private String subscriptionString;
	
	private MessageType(String name, String subscriptionString) {
		this.name = name;
		this.subscriptionString = subscriptionString;
	}

	public String getName() {
		return name;
	}

	public String getSubscriptionString() {
		return subscriptionString;
	}
}
