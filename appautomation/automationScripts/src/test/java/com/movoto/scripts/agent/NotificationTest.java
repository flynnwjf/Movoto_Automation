package com.movoto.scripts.agent;

import org.testng.annotations.Test;

import com.movoto.context.ConfigurationManager;
import com.movoto.fixtures.FixtureLibrary;
import com.movoto.scripts.BaseTest;
import com.movoto.scripts.CommonScenarios;
import com.movoto.scripts.CommonScenariosManager;

import groovy.json.JsonException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;

public class NotificationTest  {
	
	protected FixtureLibrary library;
	protected CommonScenarios scenarios;
	protected ITestContext thiscontext;

	@BeforeTest
	public void setup(ITestContext context) {
		this.thiscontext = context;
		Reporter.log("Platform: " + context.getName(), true);
		library = new ConfigurationManager().createContext(context.getName());
		context.setAttribute("LIBRARY", library);
	}
	
	@BeforeMethod
	public void beforeMehtod(Method method){
		library.setCurrentTestMethod(method.getName());
		scenarios = CommonScenariosManager.getCommonScenarios(thiscontext, library);
	}

	@Test
	public void test() {
		Map<String, Object> data = apiTest();
		library.setRequestHeader("Authorization", authorizaion);
		library.setRequestHeader("Content-Type", "application/json");
		library.setRequestHeader("x-userid", "ReDevSandboxSalesforce");
		String response = library.HTTPPost("http://alpaca.san-mateo.movoto.net:4035/dispatchservice/notifications", data);
		System.out.println(response);
		Object message = library.getValueFromJson("$.sentNotifications[0].notificationInfo.notificationMessage", response);
		System.out.println(message);
	}
	
	public Map<String, Object> apiTest() {
		JSONParser parser = new JSONParser();
		 try {
			 
	            Object obj = parser.parse(new FileReader(
	                    "data/N0.json"));
	 
	            JSONObject jsonObject = (JSONObject) obj;
	            System.out.println(jsonObject);
	            Map<String, Object> retMap = new HashMap<>();
	            
	            if(jsonObject!=null){
	            	retMap = toMap(jsonObject);
	            	return retMap;
	            }
	            
	            System.out.println(retMap);
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return null;
	}

	
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

	String authorizaion = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtb3ZvdG9hdXRoIiwic3ViIjoiUmVEZXZTYW5kYm94U2FsZXNmb3JjZSIsImlhdCI6MTQ1OTg1MTk3MDkxOSwiZXhwIjoxNDU5ODUzMTcwOTE5LCJ0eXBlIjoiYWNjZXNzIiwicm9sZSI6IiIsInNjb3BlcyI6WyJVc2VyIE1hbmFnZW1lbnQiLCJGYXN0IENvbm5lY3QiLCJOb3RpZmljYXRpb24gTWFuYWdlbWVudCIsIlZpZXcgVXBkYXRlIE5vdGlmaWNhdGlvbnMiXX0.qt8ph50gs97IexjyrcCyCHle6UNv5agWa5uHexWnnNU";

}
