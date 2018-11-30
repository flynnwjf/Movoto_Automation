package com.movoto.utils;

import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONParserForAutomationNG {
	
    JSONParser jsonParser;
    JSONObject jsonObject;
    
	public JSONParserForAutomationNG(String filePath){
		try{
			jsonParser = new JSONParser();
			jsonObject = (JSONObject)jsonParser.parse(new FileReader(filePath));
			//parse用于从一个字符串中解析出json对象
		}
		catch(ParseException pe)
		{
			System.out.println("JSONParserForAutomationNG:JSONParserForAutomationNG() method ParserException ->" + pe.getMessage());
		}
		catch(Exception exc)
		{
			System.out.println("JSONParserForAutomationNG:JSONParserForAutomationNG() method Exception ->" + exc.getMessage());
		}
	}
	
	public JSONObject getNode(String nodeName){
		JSONObject node = null;
		try{
		    if(jsonObject != null){
		    	node = (JSONObject)jsonObject.get(nodeName);
		    }
	    }
		catch(Exception exc){
	    	System.out.println("JSONParserForAutomationNG:getNode(String nodeName) Exception ->" + exc.getMessage());
	    }
		return node; 		   
	}
	
	public String getValueFromJSON(String nodeName,String key){
		JSONObject node = null;
		String value = null;
		try{
			if(jsonObject != null)
			{
				node = (JSONObject)jsonObject.get(nodeName);
				if(node != null)
					value = node.get(key).toString();
			}
		}
		catch(Exception exc){
			System.out.println("Exception in JSONParserForAutomationNG:getValueFromJSON(String nodeName,String key)->" + exc.getMessage());
		}
		return(value);
	}
	
	public JSONArray getJSONArray(String nodeName,String key){
		JSONObject node = null;
		try{
			node = getNode(nodeName);
			if(node != null)
			{
				return ((JSONArray)node.get(key));
			}
		}
		catch(Exception exc){
			System.out.println("Exception in JSONParserForAutomationNG:getJSONArray(String nodeName,String key)->" + exc.getMessage());
		}
	    return null;
	}
	
	public String elementAt(String nodeName,String key,String element,int i){
		JSONObject node = null, elementNode = null;
		JSONArray arrObj = null;
		String value = null;
		try{
			node = getNode(nodeName);
			if(node != null){
				arrObj = (JSONArray)node.get(key);
				if(arrObj != null)
				{
					elementNode = ((JSONObject)arrObj.get(i));
					if(elementNode != null)
						value = elementNode.get(element).toString();
				}
			}
		}
		catch(Exception exc){
			System.out.println("Exception in JSONParserForAutomationNG:getNode(String nodeName)elementAt(String nodeName,String key,String element,int i)->" + exc.getMessage());	
		}
		return value;
	}
	
	public JSONArray getArrayFromJSON(String key)
	{
		return (JSONArray)jsonObject.get(key);
	}
	

}
