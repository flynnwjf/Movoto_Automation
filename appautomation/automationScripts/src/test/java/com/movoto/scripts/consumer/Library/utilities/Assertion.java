package com.movoto.scripts.consumer.Library.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;
import com.movoto.scripts.consumer.Library.utilities.utilities.MyExclusionStrategy;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public final class Assertion {
	
	public static Gson Gson;
	public Assertion()
	{
		MyExclusionStrategy myExclusionStrategy = new MyExclusionStrategy(WebElement.class);
		Gson = new GsonBuilder().setExclusionStrategies(myExclusionStrategy).create();
	}
	
	public static String areEqual(Object expectedResult,Object actualResult) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		Object expectJsonObject = JsonPath.read(Gson.toJson(expectedResult),"$");
		Object actualJsonObject = JsonPath.read(Gson.toJson(actualResult),"$");
		String className =expectedResult.getClass().getTypeName();
		if (className.contains("List")) className = ((List<Object>)expectedResult).get(0).getClass().getName();
		return areEqualMap(expectJsonObject, actualJsonObject);
	}
	
	
	public static String areEqualMap(Object expectJsonObject,Object actualJsonObject) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException 
	{
		Object comparedResult = areEqualMap(expectJsonObject,actualJsonObject,"");
		String comparedResultType = comparedResult.getClass().getTypeName();
		if (comparedResultType.contains("JSONArray"))
		{
			return ((JSONArray)comparedResult).toJSONString();
		}
		else if (comparedResultType.contains("LinkedHashMap"))
		{
			return (new JSONObject((LinkedHashMap)comparedResult)).toJSONString();
		}
		else
		{
			return comparedResult.toString();
		}
	}
	
	public static Object areEqualMap(Object expectJsonObject,Object actualJsonObject,String index4Array) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException 
	{
		LinkedHashMap comparedResult = new LinkedHashMap();
		
		String classType= expectJsonObject.getClass().getTypeName();

		//if (className.contains(".")) className = Arrays.asList(className.split("\\.")).get(className.split("\\.").length-1);
		
		
		if (classType.contains("JSONArray"))
		{
			JSONArray expectJsonArray = (JSONArray)expectJsonObject;
			JSONArray actualJsonArrray =(JSONArray)actualJsonObject;
		
			int expectCount = expectJsonArray.size();
			int actualCount = actualJsonArrray.size();
			Object failureLogWithTagName = new Object();
			if (expectCount!=actualCount) 
			{
				 failureLogWithTagName = Contains4JsonArrays(expectJsonArray,actualJsonArrray);
			}
			else
			{
				failureLogWithTagName = Comapre4JsonArrays(expectJsonArray,actualJsonArrray);;
			}
			if (index4Array=="")
			{
				String failureLogWithTagNameClassType = failureLogWithTagName.getClass().getTypeName();
				if (failureLogWithTagNameClassType.contains("JSONArray")) return ((JSONArray)failureLogWithTagName).toJSONString();
				else return failureLogWithTagName.toString();
			}
			else
			{
				return failureLogWithTagName;
			}
		}
		else
		{
			LinkedHashMap comparedJson = Comapre4Jsons((LinkedHashMap)expectJsonObject,(LinkedHashMap)actualJsonObject);
			if (!comparedJson.isEmpty() && index4Array!="") 
				{
				comparedJson.put("index4Array", String.format("element %s in array failed",index4Array));
				}
			//JSONObject returnedJSONObject = new JSONObject(comparedJson);
			//String returnedJSONString = returnedJSONObject.toJSONString();
			return comparedJson;
		}
		
		
	}
	
	public static Object Comapre4JsonArrays(JSONArray expectJsonObject,JSONArray actualJsonObject) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		LinkedHashMap failureLogWithTagName = new LinkedHashMap();
	
		JSONArray logValueArray = new JSONArray();
	
		JSONArray expectJsonArray = (JSONArray)expectJsonObject;
		JSONArray actualJsonArrray =(JSONArray)actualJsonObject;
		int expectCount = expectJsonArray.size();
		int actualCount = actualJsonArrray.size();
		if (expectCount!=actualCount) 
		{
			logValueArray.add(RecordFailure(String.valueOf(expectCount),String.valueOf(actualCount)));
			failureLogWithTagName.put(String.format("Count"), logValueArray);
			//return failureLogWithTagName;
		}
		//else 
		//{
			JSONArray failureLogArray =new JSONArray();
			for (int i=0;i<expectCount;i++)
			{
				//LinkedHashMap returnedMap = new LinkedHashMap();
				Object returnedMap = new Object();
				returnedMap = areEqualMap(expectJsonArray.get(i), actualJsonArrray.get(i),String.valueOf(i));
				
				if (!(returnedMap.toString().equals("{}")||returnedMap.toString().equals("[]"))) 
				{
					if (((LinkedHashMap)returnedMap).containsKey("index4Array") && !((LinkedHashMap)returnedMap).get("index4Array").toString().contains("array failed"))
					{
						JSONArray leftActualJsonArrray = new JSONArray();
						leftActualJsonArrray.addAll(actualJsonArrray.subList(i, expectCount));
						String foundInActual = Contains4Json((LinkedHashMap)expectJsonArray.get(i),leftActualJsonArrray);
						if (!foundInActual.equals("false")) 
						{
							int indexfoundInActual = Integer.valueOf(foundInActual)+i;
							/*LinkedHashMap faultActual = new LinkedHashMap();
							LinkedHashMap realActual = new LinkedHashMap();
							faultActual = (LinkedHashMap)actualJsonArrray.get(i);
							realActual = (LinkedHashMap)actualJsonArrray.get(indexfoundInActual);
							actualJsonArrray.set(i, realActual);
							actualJsonArrray.set(indexfoundInActual, realActual);*/
							((LinkedHashMap)returnedMap).put("index4Array", String.format("element's expect index is %s, actual index is %s", String.valueOf(i),String.valueOf(indexfoundInActual)));
							JSONArray leftExpectJsonArrray = new JSONArray();
							leftExpectJsonArrray.addAll(expectJsonArray.subList(i+1, expectCount));
							String foundInExpect = Contains4Json((LinkedHashMap)actualJsonArrray.get(i),leftExpectJsonArrray);
							if (!foundInExpect.equals("false")) 
							{
								((LinkedHashMap)returnedMap).put("foundInExpect4ThisActual",String.format("this actual element is displayed other expect element %s", String.valueOf(Integer.valueOf(foundInExpect)+i)));
							}
							else
							{
								((LinkedHashMap)returnedMap).put("extraOnUI",actualJsonArrray.get(i));
								actualJsonArrray.remove(actualJsonArrray.get(i));
								
							}
						}
						else 
						{
							actualJsonArrray.removeAll(leftActualJsonArrray);
							actualJsonArrray.add(new LinkedHashMap());
							actualJsonArrray.addAll(leftActualJsonArrray);
							returnedMap = new LinkedHashMap();
							((LinkedHashMap)returnedMap).put("index4Array", String.format("Missing display for element %s", String.valueOf(i)));
							((LinkedHashMap)returnedMap).put("expect",expectJsonArray.get(i));
						}
						
					}
					
						
					failureLogArray.add(returnedMap);
				}
			}
			
			if (expectCount<actualJsonArrray.size() && failureLogArray.size()>0 && ((LinkedHashMap)failureLogArray.get(0)).containsKey("index4Array")) {
				LinkedHashMap addtionalActual = new LinkedHashMap();
				JSONArray addtionalActualJSONArray = new JSONArray();
				addtionalActualJSONArray.addAll(actualJsonArrray.subList(expectCount+1, actualJsonArrray.size()));
				addtionalActual.put("extraActual", addtionalActualJSONArray);
				failureLogArray.add(addtionalActual);
			}
			return failureLogArray;
		//}
		
	}
	
	
	public static Object Contains4JsonArrays(JSONArray expectJsonObject,JSONArray actualJsonObject) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		LinkedHashMap failureLogWithTagName = new LinkedHashMap();
	
		JSONArray logValueArray = new JSONArray();
	
		JSONArray expectJsonArray = (JSONArray)expectJsonObject;
		JSONArray actualJsonArrray =(JSONArray)actualJsonObject;
		JSONArray expectJsonArrayContainer = (JSONArray) expectJsonArray.clone();//new JSONArray();
		JSONArray actualJsonArrayContainer = (JSONArray) actualJsonArrray.clone();//new JSONArray();

		int expectCount = expectJsonArray.size();
		int actualCount = actualJsonArrray.size();
		int minCount=Integer.min(expectCount,actualCount);
		JSONArray failureLogArray =new JSONArray();
		
		for (int i=0;i<expectCount;i++)
		{
			//LinkedHashMap returnedMap = new LinkedHashMap();
			Object returnedMap = new Object();
			String mapInArray = Contains4Json((LinkedHashMap)expectJsonArray.get(i),actualJsonArrray);
			if (!mapInArray.equals("false"))
			{
				int indexFoundInArray=Integer.valueOf(mapInArray);
				expectJsonArrayContainer.remove(expectJsonArray.get(i));
				actualJsonArrayContainer.remove(actualJsonArrray.get(indexFoundInArray));
			}
			
		}
		
		logValueArray.add(RecordFailure(String.valueOf(expectCount),String.valueOf(actualCount)));
		failureLogWithTagName.put(String.format("Count"), logValueArray);
		
		failureLogWithTagName.put("expectResult_notEqual", expectJsonArrayContainer);
		failureLogWithTagName.put("actualResult_notEqual", actualJsonArrayContainer);
		return failureLogWithTagName;
		
	}
	
	public static String Contains4Json(LinkedHashMap expectJsonMap,JSONArray actualJsonArrray) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		String findIndex="false";
		for (int i=0;i<actualJsonArrray.size();i++)
		{
			Object returnedMap = areEqualMap(expectJsonMap, actualJsonArrray.get(i),String.valueOf(i));
			if ((returnedMap.toString().equals("{}")||returnedMap.toString().equals("||"))) {
				return String.valueOf(i);
			}
		}
		return findIndex;
	}
	public static LinkedHashMap Comapre4Jsons(String expectJsonObject,String actualJsonObject)
	{
		return new LinkedHashMap();
	}
	public static LinkedHashMap Comapre4Jsons(LinkedHashMap expectJsonObject,LinkedHashMap actualJsonObject)
	{
		LinkedHashMap expectJson = (LinkedHashMap)expectJsonObject;
		LinkedHashMap actualJson = (LinkedHashMap)actualJsonObject;
		LinkedHashMap comparedJson = (LinkedHashMap)expectJson.clone();
		expectJson.forEach((expectKey,expectValue)->{
			String expectValueType = expectValue.getClass().getTypeName();
			if (expectValueType.equals("java.lang.String"))
			{
				String actualValue = actualJson.get(expectKey).toString();
				if (!expectValue.toString().equals(actualValue)) comparedJson.put(expectKey, RecordFailure(expectValue.toString(),actualValue));
				else comparedJson.remove(expectKey);
			}
			else if (expectValueType.equals("java.lang.Integer"))
			{
				String actualValueString = String.valueOf((Integer)actualJson.get(expectKey));
				String expectValueString = String.valueOf((Integer)expectValue);
				if (!expectValueString.equals(actualValueString)) comparedJson.put(expectKey, RecordFailure(expectValue.toString(),actualValueString));
				else comparedJson.remove(expectKey);
			}
			else
			{
				Object returnedMap = new Object();
				try {
					returnedMap = areEqualMap(expectValue, actualJson.get(expectKey),expectKey.toString());
				} catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException
						| InstantiationException | NoSuchFieldException | SecurityException
						| InvocationTargetException | NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!(returnedMap.toString().equals("[]")||returnedMap.toString().equals("{}"))) comparedJson.put(expectKey, returnedMap);
				else 
				{
					comparedJson.remove(expectKey);
				}
			}
		});
		return comparedJson;
		
	}
	/*
	public static LinkedHashMap Comapre4Jsons(LinkedHashMap expectJsonObject,LinkedHashMap actualJsonObject,String index4Array)
	{
		LinkedHashMap comparedJson = Comapre4Jsons( expectJsonObject, actualJsonObject);
		comparedJson.put("indexInArray", index4Array);
		return comparedJson;
	}*/
	public static LinkedHashMap RecordFailure(String expect,String actual)
	{
		LinkedHashMap map = new LinkedHashMap();
		map.put("expect", expect);
		map.put("actual", actual);
		return map;
	}
	
}
