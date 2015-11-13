package com.pearson.test.daalt.dataservice.request.action;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.request.message.Message;
import com.pearson.test.daalt.dataservice.request.message.ProductToCourseSectionPLTMessage;
import com.pearson.test.daalt.dataservice.model.CourseSection;

public class SubPubSeerCreateCourseSectionTestAction extends CreateCourseSectionTestAction {

	public SubPubSeerCreateCourseSectionTestAction(CourseSection courseSection) {
		messages = new ArrayList<>();
		this.courseSection = courseSection;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {		
		checkCriticalObjects();
		
		System.out.println("Now executing CreateCourseSectionTestAction...\n");
		
		// 		2. revel.product.course.create 
		Message revelProductCourseMessage = new ProductToCourseSectionPLTMessage();
		revelProductCourseMessage.setProperty("CourseSection", courseSection);
		messages.add(revelProductCourseMessage);
		
		for (Message msg : messages) {
			msg.send(seerCount, subPubCount);
		}
	}
}
