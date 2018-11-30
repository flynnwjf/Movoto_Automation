package com.movoto.scripts.agent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONParserForAutomationNG {
	
    String parseString;
    JSONParser jsonParser;
    JSONObject jsonObject;
	public JSONParserForAutomationNG(String jsonString)
	{
	  parseString=jsonString;
	  jsonParser=new JSONParser();
	  try
	  {
		  parse();
	  }catch(ParseException pe)
	  {
		  System.out.println("JSONParserForAutomationNG:=="+pe.getMessage());
	  }
	}
	
	public void parse() throws ParseException
	{
		Object obj=jsonParser.parse(parseString);
		jsonObject=(JSONObject)obj;
		
	}
	public String getValueFromJSON(String key)
	{
		return (String) jsonObject.get(key);
	}
	public String getValueFromJSON(int i,String arrayStr,String key)
	{
		JSONArray jsonArray=(JSONArray)jsonObject.get(arrayStr);
		System.out.println();
		return String.valueOf(((JSONObject)(jsonArray.get(i))).get(key));
	}
	/*public static void main(String args[]) throws IOException, ParseException
	{
		
		
		JSONParser jsonParser=new JSONParser();
		Object obj=jsonParser.parse(reader);
		JSONObject jsonObject=(JSONObject)obj;
		
		JSONArray jsonArray=(JSONArray)jsonObject.get("listings");
		jsonObject=(JSONObject)jsonArray.get(3);
		
		System.out.println(jsonArray);
		for(int i=0;i<jsonArray.size();i++)
		{
		jsonObject=(JSONObject)jsonArray.get(i);
		System.out.println("JSONOBJECT is ============"+jsonArray.get(i));
		System.out.println("BATH ROOM =========="+jsonObject.get("bathroomsTotal"));
		}
	}*/
}
