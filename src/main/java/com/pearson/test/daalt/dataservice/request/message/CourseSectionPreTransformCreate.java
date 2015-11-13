package com.pearson.test.daalt.dataservice.request.message;


public class CourseSectionPreTransformCreate extends SubPubTransformerMessage {
	// grid.registrar.course.created

	public CourseSectionPreTransformCreate() {
		payload = new CourseSectionTransformMsgPayload();
		super.SetSubscriptionString("grid.registrar.section.created");
	}
	
	
	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException, InvalidStateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMissingCriticalPropertyName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public class CourseSectionTransformMsgPayload extends TransformerMessagePayload {
		public Context context = new Context();
		public class Context {}
		public Object object = new Object ();
		public class Object {
			public String createdAt = getPastOrFutureTimeFormatted(-1);
			public String updatedAt = getPastOrFutureTimeFormatted(0);
			public String id;
			public String courseId;
			public String sectionCode;
			public String sectionDetails;
			public String sectionDescription;
			public String startDate;
			public String endDate;
			public String openenrollmentbegins;
			public String openenrollmentends;
			public String[] sectionKeys;
			public int registrationCount;
			public boolean isClassFull;
		}
		public String occurredAt;
	}

}


/*
{
    "context": {},
    "object": {
        "createdAt": "2014-03-28T16:24:58.008Z",
        "updatedAt": "2014-03-28T16:24:58.008Z",
        "id": "5335a25ae4b08626a4ecc2c9",
        "courseId": "5335a259e4b08626a4ecc2c8",
        "sectionCode": "C32247",
        "sectionTitle": "Internet Era",
        "sectionDetails": "A course about history of internet",
        "sectionDescription": "description",
        "startDate": "2011-04-15T00:00:00.000Z",
        "endDate": "2011-08-15T00:00:00.000Z",
        "openenrollmentbegins": "2011-04-15T00:00:00.000Z",
        "openenrollmentends": "2011-04-20T00:00:00.000Z",
        "sectionKeys": [],
        "registrationCount": 0,
        "isClassFull": false
    },
    "occurredAt": "2014-03-28T16:24:58.015Z"
}


*/