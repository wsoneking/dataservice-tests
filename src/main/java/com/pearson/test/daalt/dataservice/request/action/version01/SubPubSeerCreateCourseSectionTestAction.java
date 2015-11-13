package com.pearson.test.daalt.dataservice.request.action.version01;

import java.util.ArrayList;

import com.pearson.test.daalt.dataservice.request.message.version01.CourseSectionToLearningResource;
import com.pearson.test.daalt.dataservice.request.message.version01.GridTransformSectionMessage;
import com.pearson.test.daalt.dataservice.request.message.version01.Message;
import com.pearson.test.daalt.dataservice.request.message.version01.RevelLrMessage;
import com.pearson.test.daalt.dataservice.request.message.version01.RevelProductCourseMessage;
import com.pearson.test.daalt.dataservice.request.message.version01.RevelProductMessage;
import com.pearson.test.daalt.dataservice.model.CourseSection;

public class SubPubSeerCreateCourseSectionTestAction extends CreateCourseSectionTestAction {

	public SubPubSeerCreateCourseSectionTestAction(CourseSection courseSection) {
		messages = new ArrayList<>();
		this.courseSection = courseSection;
	}
	
	@Override
	public void execute(int seerCount, int subPubCount) throws Exception {		
		checkCriticalObjects();
		
		//		1. grid.daalt.transform.section.created
		Message gridTransformSectionCreateMessage = new GridTransformSectionMessage();
		gridTransformSectionCreateMessage.setProperty("CourseSection", courseSection);
		messages.add(gridTransformSectionCreateMessage);
		
		Message courseSectionLearningResourceMessage = new CourseSectionToLearningResource();
		courseSectionLearningResourceMessage.setProperty("CourseSection", courseSection);
		messages.add(courseSectionLearningResourceMessage);
		
		//		2. revel.course.lr.create
		Message revelCourseLrCreateMessage = new RevelLrMessage(/*hasParentResource*/ false);
		revelCourseLrCreateMessage.setProperty("CourseSection", courseSection);
		messages.add(revelCourseLrCreateMessage);
		
		// 		3. revel.product.course.create 
		Message revelProductCourseMessage = new RevelProductCourseMessage();
		revelProductCourseMessage.setProperty("CourseSection", courseSection);
		messages.add(revelProductCourseMessage);
		
		//		4. revel.product.create
		Message revelProductCreateMessage = new RevelProductMessage();
		revelProductCreateMessage.setProperty("CourseSection", courseSection);
		messages.add(revelProductCreateMessage);
		
		for (Message msg : messages) {
			msg.send(seerCount, subPubCount);
		}
	}
}
