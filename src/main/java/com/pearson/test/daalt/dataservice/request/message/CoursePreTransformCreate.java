package com.pearson.test.daalt.dataservice.request.message;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.pearson.seer.client.exception.SeerReportingException;


public class CoursePreTransformCreate extends SubPubTransformerMessage {
	// grid.registrar.course.created

	public CoursePreTransformCreate() {
		payload = new CourseTransformMsgPayload();
		super.SetSubscriptionString("grid.registrar.courseregistration.created");
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
	
	
	
	public class CourseTransformMsgPayload extends TransformerMessagePayload {
		public Context context = new Context();
		public class Context {
			public String courseType = "Revel";
		}
		public Object object = new Object ();
		public class Object {
			public String createdAt = getPastOrFutureTimeFormatted(-1);
			public String updatedAt = getPastOrFutureTimeFormatted(0);
			public String id;
			public String courseTitle;
			public String description;
			public String courseAvatar;
			public String courseContentRef;
			public String courseType;
			public String sourceCourseId;
			public String masterType;
			public String courseVersion;
			public String courseCredit;
			public String[] externalIds;			
		}
		public String occurredAt;
	}


}


/*
{
    "context": {
        "courseType": "Revel"
    },
    "object": {
        "createdAt": "2014-03-28T16:19:15.776Z",
        "updatedAt": "2014-03-28T16:19:15.776Z",
        "id": "5335a103e4b08626a4ecc2c5",
        "courseTitle": "Introduction to Calculus 1",
        "description": "This course aims to provide basic concepts of Mathematics.",
        "courseAvatar": "http://content/calcusus101.png",
        "courseContentRef": "/MITCOURSES/0823508123508234086",
        "courseType": "Revel",
        "sourceCourseId": "029352039858220985",
        "masterType": "publisher",
        "courseVersion": "1.0",
        "courseCredits": "3.0",
        "externalIds": ["customId.12435"]
    },
    "occurredAt": "2014-03-28T16:19:15.780Z"
}

*/