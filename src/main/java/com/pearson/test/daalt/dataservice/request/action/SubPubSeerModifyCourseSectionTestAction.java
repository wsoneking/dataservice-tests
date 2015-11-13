package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.ProductToCourseSectionPLTMessage;

public class SubPubSeerModifyCourseSectionTestAction extends ModifyCourseSectionTestAction {

	public SubPubSeerModifyCourseSectionTestAction(CourseSection courseSection, String verb) {
		messages = new ArrayList<>();
		this.courseSection = courseSection;
		this.verb = verb;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {		
		checkCriticalObjects();
		
		System.out.println("Now executing ModifyCourseSectionTestAction...\n");
		
		// 		2. revel.product.course.create 
		Message revelProductCourseMessage = new ProductToCourseSectionPLTMessage();
		revelProductCourseMessage.setProperty("CourseSection", courseSection);
		revelProductCourseMessage.setProperty("Transaction_Type_Code", verb);
		messages.add(revelProductCourseMessage);
		
		for (Message msg : messages) {
			msg.send(seerCount,subPubCount);
		}
	}
}
