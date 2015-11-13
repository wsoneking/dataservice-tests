package com.pearson.test.daalt.dataservice.mongodb;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.type.TypeReference;

public class JSONUtils {
	public static String createJSON(Object obj) throws IOException {
		return createJSON(obj, false);
	}
	
    public static String createJSON(Object obj, boolean prettyPrint) throws IOException {
    	ObjectWriter ow = new ObjectMapper().writer();
    	if(prettyPrint) {
    		ow = ow.withDefaultPrettyPrinter();
    	}
    	
    	try {
    		return ow.writeValueAsString(obj);
    	} catch(JsonProcessingException e) {
    		throw new RuntimeException(e);
    	}
    }
    
    public static Map<String, Object> readJSON(String json) {
    	JsonFactory factory = new JsonFactory();
    	ObjectMapper mapper = new ObjectMapper(factory);
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
        try {
        	return mapper.readValue(json, typeRef);
        } catch(Exception e) {
        	throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> readJSONList(String json) {
    	JsonFactory factory = new JsonFactory();
    	ObjectMapper mapper = new ObjectMapper(factory);
        TypeReference<ArrayList<HashMap<String,Object>>> typeRef = new TypeReference<ArrayList<HashMap<String,Object>>>() {};
        try {
        	return mapper.readValue(json, typeRef);
        } catch(Exception e) {
        	throw new RuntimeException(e);
        }
    }

    
    /*
     *JSON:
     *	{
     *		"A" : "B",
     *		"C" : {
     *			"D" : "E"
     *		}
     *	} 
     */
    @SuppressWarnings("unchecked")
	public static void main(String [] args) throws Exception {
    	String json = 
    			"{"
    			+ "\"A\" : \"B\","
    			+ "\"C\" : {"
    					+ "\"D\" : \"E\""
    				+ "}"
    			+ "}";
    	
    	Map<String, Object> map = readJSON(json);
    	
    	System.out.println((String)map.get("A"));
    	
    	Map<String, Object> c = (Map<String, Object>)map.get("C");
    	System.out.println((String)c.get("D"));
    }
}
