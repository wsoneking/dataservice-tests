package com.pearson.test.daalt.dataservice.scenario.singleMsg;

import java.io.IOException;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import com.pearson.test.daalt.dataservice.model.BasicCourseSection;
import com.pearson.test.daalt.dataservice.model.BasicStudent;
import com.pearson.test.daalt.dataservice.model.CourseSection;
import com.pearson.test.daalt.dataservice.model.Student;
import com.pearson.test.daalt.dataservice.request.message.InvalidStateException;
import com.pearson.test.daalt.dataservice.request.message.PersonCourseSectionPreTransformCreate;
import com.pearson.test.daalt.dataservice.request.message.PersonPreTransformCreate;
import com.pearson.test.daalt.dataservice.request.message.UnknownPropertyException;

public class PersonCourseSectionPreTransformSingleMsg extends BaseTestSingleMessage {

	
	@Test
	public void validMessage() throws Exception {

		String uuid = UUID.randomUUID().toString();
		System.out.println("The UUID is: "+uuid);
		
		// send the message 
		try {
			String messagePayloadAsJson = getPersonCourseSectionPreTransformMessage(uuid);
			kafkaHelper.publishMessage(messagePayloadAsJson, "grid.registrar.courseregistration.created");

		} catch (Exception e) {
			throw e;
		}

	}

	
	public String getPersonCourseSectionPreTransformMessage(String uuid) throws JsonGenerationException, JsonMappingException, IOException {

		PersonCourseSectionPreTransformCreate msg = new PersonCourseSectionPreTransformCreate();
		Student student = new BasicStudent("Username001","Password27","userId-"+uuid,"FirstName","LastName");
		CourseSection cs = new BasicCourseSection();
		
		try {
			msg.setProperty("Person", student);
			msg.setProperty("CourseSection", cs);
		} catch (UnknownPropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String messagePayloadAsJson = new ObjectMapper().writeValueAsString(msg.payload);
		System.out.println("messageAsJson: "+ messagePayloadAsJson);
		
		return messagePayloadAsJson;
	}
}
