package com.movoto.tests;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.movoto.context.ConfigurationManager;
import com.movoto.fixtures.FixtureLibrary;

import groovy.json.JsonException;

public class ClickTest {
	
	private FixtureLibrary library;

	@BeforeTest
	public void startTest(final ITestContext testContext) {
	    System.out.println(testContext.getName());
		library = new ConfigurationManager().createContext(testContext.getName());
	    //library = FixtureLibraryFactory.getLibrary(testContext.getName());
	}

	@Test
	public void clickTest() {
		JSONParser parser = new JSONParser();
		try {		 
			Object obj = parser.parse(new FileReader("data/N.json"));
	        JSONObject jsonObject = (JSONObject) obj;
	        System.out.println("Origin: " + jsonObject);
	        Map<String, Object> retMap = new HashMap<>();	            
	        if(jsonObject!=null){
	            retMap = toMap(jsonObject);
	        }        
	        System.out.println("Change: " + retMap);
		} catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}
	
	/*static String splitCamelCase(String s) {
		   return s.replaceAll(
		      String.format("%s|%s|%s",
		         "(?<=[A-Z])(?=[A-Z][a-z])",
		         "(?<=[^A-Z])(?=[A-Z])",
		         "(?<=[A-Za-z])(?=[^A-Za-z])"
		      ),
		      " "
		   );
		}*/
	
	public static Map<String, Object> toMap(JSONObject object) throws JsonException {
	    Map<String, Object> map = new HashMap<>();
	    Iterator<String> keysItr = object.keySet().iterator();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = object.get(key);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }
	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}

	public static List<Object> toList(JSONArray array) throws JsonException {
	    List<Object> list = new ArrayList<>();
	    for(int i = 0; i < array.size(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}

}
