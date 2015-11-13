package com.pearson.test.daalt.dataservice.request.message;

import java.util.UUID;

import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.User;

public class PersonCourseSectionPreTransformCreate extends SubPubTransformerMessage {
	// grid.registrar.courseregistration.created
	
	public PersonCourseSectionPreTransformCreate() {
		payload = new PersonCourseSectionTransformMsgPayload();
		super.SetSubscriptionString("grid.registrar.courseregistration.created");
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws UnknownPropertyException, InvalidStateException {
		PersonCourseSectionTransformMsgPayload specificPayload = (PersonCourseSectionTransformMsgPayload) payload;
		switch(propertyName) {
		case "CourseSection": 
			CourseSection section =  (CourseSection)propertyValue;
			specificPayload.object.sectionId = section.getId();
			break;
		case "Person":
			User user = (User) propertyValue;
			specificPayload.object.userId = user.getPersonId();
			if (user.getPersonRole().equals("Instructor")){
				specificPayload.object.groupType = "instructor";
			} else if (user.getPersonRole().equals("TeachingAssistant")) {
				specificPayload.object.groupType = "ta";
			} else if (user.getPersonRole().equals("Student")) {
				specificPayload.object.groupType = "student";
			} else {
				System.out.println("User Role error.");
			}
			break;
		default:
			throw new UnknownPropertyException("Failed to build message due to unknown property : " + propertyName);
		}
	}
	
	@Override
	public String getMissingCriticalPropertyName() {
		PersonCourseSectionTransformMsgPayload msgPayLoad = (PersonCourseSectionTransformMsgPayload) payload;
		if (msgPayLoad.object.userId == null) {
			return "MessagePayload.userId";
		}
		if (msgPayLoad.object.sectionId == null) {
			return "MessagePayload.sectionId";
		}
		if (msgPayLoad.object.groupType == null) {
			return "MessagePayload.Message_Version";
		}
		return null;
	}
	
	
	public class PersonCourseSectionTransformMsgPayload extends TransformerMessagePayload {
		public Context context = new Context();
		public class Context {
			public String str ="";
		}
		public Object object = new Object ();
		public class Object {
			public String userId;
			public String sectionId;
			public String status = "pending";
			public String authgroupid = "AuthGroupId - " + UUID.randomUUID();
			public String groupType;
		}
		public String occurredAt = getPastOrFutureTimeFormatted(0);
	}



}

/*
{
    "context": {},
    "object": {
        "userId": "ffffffff53359f97e4b06ef6494c7952",
        "sectionId": "53234b5ce4b06bb120844da8",
        "status": "pending",
        "authgroupid": "550e8400-e29b-41d4-a716-446655440000",
        "groupType": "Instructor"
    },
    "occurredAt": "2014-03-28T16:40:51.574Z"
}

*/
