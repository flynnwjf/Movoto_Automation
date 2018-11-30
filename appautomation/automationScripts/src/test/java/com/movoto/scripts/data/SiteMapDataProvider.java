package com.movoto.scripts.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.stream.Collectors;
import java.util.*;

import org.apache.commons.collections.comparators.ComparableComparator;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.movoto.common.JSONManager;
import com.movoto.fixtures.FixtureLibrary;
import com.movoto.scripts.consumer.Library.*;
import com.movoto.scripts.consumer.Library.utilities.utilities;
import com.movoto.scripts.consumer.Library.utilities.utilities.FileManager;
import com.movoto.scripts.consumer.Library.utilities.utilities.JsonManager;
import com.movoto.scripts.consumer.Library.utilities.RestAPI;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;
public final class SiteMapDataProvider {

	private static JsonManager jsonManager = new utilities.JsonManager();
	private static String jsonFilePath;
	private static String  basicJsonPath;
	private static JsonContext expectedResultFormat;
	@DataProvider(name="SiteMap")
  	public static Object SiteMap(Class<?> tClazz) throws Exception {
  		//FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
  		//library.setCurrentTestMethod(method.getName());
  		String jsonFilePath = "data/SiteMap/SiteMap.json";///Users/vivian/Documents/Automation/20161225/appautomation/automationScripts/src/test/java/com/movoto/scripts/consumer/
  		
  		return new utilities.JsonManager().JsonDeserializer(jsonFilePath,tClazz);
     
	}
	
	@DataProvider(name="SiteMap")
  	public static Object SiteMap(Class<?> tClazz,String testData,String testState) throws Exception {
  		String apiResponse = GetSiteMapAPIInfo(testData,testState);
  		LinkedHashMap expectedResultWithAPIPath = (LinkedHashMap)JsonPath.read(testData, "$.ExpectedResults");
  		
  	    Object expectedResult = BuildExpectedResult(JsonPath.read(apiResponse,"$"),expectedResultWithAPIPath,tClazz);

  		return expectedResult;

	}
	
	@DataProvider(name="SiteMapArray")
  	public static Object SiteMapArray(Type tClazz) throws Exception {
  		//FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
  		//library.setCurrentTestMethod(method.getName());
  		String jsonFilePath = "data/ConsumerWebSite/SiteMap_list.json";///Users/vivian/Documents/temp/Automation/appautomation/automationScripts/
  		Object returnObject = new utilities.JsonManager().JsonDeserializerArrayByFilePath(jsonFilePath,tClazz);
  		return returnObject;
     
	}
	public static String GetSiteMapAPIInfo(String testData,String testState) throws Exception
	{
		LinkedHashMap testDataMap = JsonPath.read(testData, "$");
		String pageName = testDataMap.get("pageName").toString();
		basicJsonPath = String.format("$.siteMapPages[?(@.pageName=='%s')]",pageName);
		String apiInfo = testData.replace("{one of testStates}", testState);
		String apiResponse = BuildAPI(JsonPath.read(apiInfo, "$.API"));
		return apiResponse;
	}
	@DataProvider(name="SiteMapArray")
  	public static <T> List<T> SiteMapArray(Class<T> tClass,Type tClazz,String testData,String testState) throws Exception {  		
	
		String apiResponse = GetSiteMapAPIInfo(testData,testState);
		
		//System.out.println(apiResponse);
  		List<LinkedHashMap> expectedResultWithAPIPath = (List<LinkedHashMap>)JsonPath.read(testData, "$.ExpectedResults");
  		
  		List<T> expectedResult = (List<T>)BuildExpectedResult(JsonPath.read(apiResponse,"$"),expectedResultWithAPIPath,tClazz);

  		return expectedResult;
     
	}
	
	public static Object BuildExpectedResult(Object apiResponse,Object object,Type clazzType)
	{
		Object expectedResultMap = BuildExpectedFromAPI(apiResponse,object);
		String expectedResultMapClassType = expectedResultMap.getClass().getTypeName();
		try {
			return jsonManager.JsonDeserializerArray(((JSONArray)expectedResultMap).toJSONString(), clazzType);
		} catch (JsonSyntaxException | JsonIOException | InstantiationException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object BuildExpectedResult(Object apiResponse,Object object,Class<?> clazz)
	{
		Object expectedResultMap = BuildExpectedFromAPI(apiResponse,object);
		String expectedResultMapClassType = expectedResultMap.getClass().getTypeName();
		try {
			return jsonManager.JsonDeserializer(((JSONObject)expectedResultMap).toJSONString(), clazz);
		} catch (JsonSyntaxException | JsonIOException | InstantiationException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object BuildExpectedFromAPI(Object apiResponse,Object object)
	{

		List<Object> buildResultList=new ArrayList<Object>(Arrays.asList(object));
		if (object.toString().contains(",$."))
		{
			Object apiMap=apiResponse;//JsonPath.read(apiResponse.toString(), "$");
			String apiMapType = apiMap.getClass().getTypeName();
			if (apiMapType.contains("JSONArray"))
			{
				List<LinkedHashMap> apiJSONArray = (List<LinkedHashMap>)apiMap;
				List<LinkedHashMap> resultFormatList = (List<LinkedHashMap>)object;
				//List<List<LinkedHashMap>> result = new ArrayList<List<LinkedHashMap>>(Arrays.asList((List<LinkedHashMap>)object));
				JSONArray result = new JSONArray();
				resultFormatList.forEach(resultFormat ->{
					apiJSONArray.forEach(apiJSON ->
					{
						JSONObject feedBack = FillInExpectedResult(resultFormat,apiJSON);
						result.add(feedBack);
					});
				});

				buildResultList.set(0, result);
			}
			else
			{
				LinkedHashMap apiJSON = (LinkedHashMap)apiMap;
				LinkedHashMap resultFormat = (LinkedHashMap)object;
				buildResultList.set(0, FillInExpectedResult(resultFormat,apiJSON));	
			}
		}
		return buildResultList.get(0);
		
	}
	
	public static JSONObject FillInExpectedResult(LinkedHashMap resultFormat,LinkedHashMap apiJSON)
	{
		LinkedHashMap currentResultFormat = (LinkedHashMap)resultFormat;
		LinkedHashMap returnedResult = (LinkedHashMap)resultFormat.clone();
		
		currentResultFormat.forEach((resultFormatKey,resultFormatValue) ->{
			if (resultFormatValue.toString().contains(",$.") )
			{
				String resultFormatValueClassType = resultFormatValue.getClass().getTypeName();
				if (resultFormatValueClassType.equals("java.lang.String"))
				{
					returnedResult.put(resultFormatKey, MapAPIWithExpected((LinkedHashMap)apiJSON,resultFormatValue.toString()));
				}
				else
				{
					String jsonPath;
					if (resultFormatValueClassType.contains("JSONArray"))
					{
						jsonPath = GetParentJPathOfJsonValueString((LinkedHashMap)((JSONArray)resultFormatValue).get(0));
					}
					else
					{
						jsonPath = GetParentJPathOfJsonValueString((LinkedHashMap)resultFormatValue);
					}
					String keyInAPI = jsonPath.split("\\.")[jsonPath.split("\\.").length-1];
					Object apiElement =apiJSON;
					if (!keyInAPI.equals("$")) apiElement = apiJSON.get(keyInAPI);
					Object returned =BuildExpectedFromAPI(apiElement,resultFormatValue);
					returnedResult.put(resultFormatKey, returned);
				}
			}
		});
		return new JSONObject(returnedResult);
	}
	public static String MapAPIWithExpected(LinkedHashMap apiResponse,String sFormat)
	{
		String result="";
		String[] resultList=sFormat.split(",\\$\\.");
		String[] sFormatList=resultList[0].split("\\%s");
		for (int i=1;i<resultList.length;i++)
		{
			String key = resultList[i].split("\\.")[resultList[i].split("\\.").length-1];
			
			result =result+sFormatList[i-1]+apiResponse.get(key).toString();
		}
		if (resultList.length == sFormatList.length) result = result+sFormatList[resultList.length-1];
		return result;
	}
	

	public static String GetParentJPathOfJsonValueString(LinkedHashMap map)
	{

		List<String> parentJPath= new ArrayList<String>(Arrays.asList(""));
		map.values().stream().anyMatch(v ->{
			if (v.getClass().getTypeName().contains("String") && v.toString().contains(",$."))
			{
				String entireJsonPath = v.toString().split(",")[1];
				String parentJsonPath = entireJsonPath.substring(0, entireJsonPath.lastIndexOf("."));
				parentJPath.set(0, parentJsonPath);
				return true;
			}
			return false;
		});
		return parentJPath.get(0);
	}
	
	public static String RebuildAPI(String initailAPIresponse,Object api) throws Exception
	{
		LinkedHashMap initialAPIMap = (LinkedHashMap)api;
		List<LinkedHashMap> initialAPIParasMap = new ArrayList<LinkedHashMap>();
		if (initialAPIMap.containsKey("Parameters4URI")) initialAPIParasMap.add((LinkedHashMap)initialAPIMap.get("Parameters4URI"));
		LinkedHashMap initialAPIInfo = RestAPI.GetAPIInfo(initialAPIMap.get("apiName").toString(),initialAPIMap.get("Parameters4URI"));
		
		List<String> returnAPIResponse = new ArrayList<String>(Arrays.asList(initailAPIresponse));
		
		if (initialAPIInfo.containsKey("RebuiltByAPIs"))
		{
			JSONArray rebuildAPIInfos = (JSONArray)initialAPIInfo.get("RebuiltByAPIs");
			rebuildAPIInfos.forEach(rebuildAPIInfo ->{
				LinkedHashMap rebuildAPIInfoMap = (LinkedHashMap)rebuildAPIInfo;
				rebuildAPIInfoMap.put("Parameters4URI",initialAPIParasMap.get(0));
				try {
					String rebuildAPIInitialResponse =BuildAPI(rebuildAPIInfoMap);
					if (!rebuildAPIInitialResponse.equals("")&&rebuildAPIInfoMap.containsKey("RebuildMethod")) 
					{
						Arrays.asList(SiteMapDataProvider.class.getMethods()).forEach(m -> 
						{
							String methodName = rebuildAPIInfoMap.get("RebuildMethod").toString();
							if (m.getName().equals(methodName))
							{

								try 
								{
									if (methodName.equals("AddBoroughs")) 
									{
										returnAPIResponse.set(0,m.invoke(new SiteMapDataProvider(), returnAPIResponse.get(0).toString(),rebuildAPIInitialResponse).toString());
									}
									else if (methodName.equals("AddListingCount"))
									{
										returnAPIResponse.set(0,m.invoke(new SiteMapDataProvider(), returnAPIResponse.get(0).toString(),rebuildAPIInitialResponse).toString());
									}
									
								}
								 catch (IllegalAccessException | InvocationTargetException|IllegalArgumentException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
								}
							}
						});			
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
		}
		return returnAPIResponse.get(0);
		
	}
	

	public static String AddListingCount(String initialAPIresponse,String listingCoutAPIResponse) throws FileNotFoundException
	{
		
		JSONArray initialAPIresponseJSONArray = JsonPath.read(initialAPIresponse, "$");
		
		LinkedHashMap listingCoutAPIResponseMap = (LinkedHashMap)JsonPath.read(listingCoutAPIResponse, "$");
		initialAPIresponseJSONArray.forEach(initialAPIresponseJSON ->
		{
			LinkedHashMap initialAPIresponseJSONMap = (LinkedHashMap)initialAPIresponseJSON;
			initialAPIresponseJSONMap.put("listingCount", listingCoutAPIResponseMap.get(initialAPIresponseJSONMap.get("id").toString()));
		});
		
		return initialAPIresponseJSONArray.toJSONString();
	}
	
	public static String AddSchoolsCountExcluding0AndCounty(String initialAPIresponse) throws Exception
	{
		JSONArray initialAPIresponseJSONArray = JsonPath.read(initialAPIresponse, "$");
		JSONObject postBody4SchoolsCountAPI = new JSONObject();
		JSONArray cityIDs = new JSONArray();
		List<Object> ids = initialAPIresponseJSONArray.stream().map(item -> ((LinkedHashMap)item).get("id")).collect(Collectors.toList());
		ids.forEach(id ->cityIDs.add((Integer)id));
		
		postBody4SchoolsCountAPI.put("cityCodes", cityIDs);
		LinkedHashMap searchedJSONObject=RestAPI.GetAPIInfo("SchoolsCount4StateCities",null);
		String schoolsCoutAPIResponse=RestAPI.GetResponse(searchedJSONObject.get("URL").toString(), (LinkedHashMap)(searchedJSONObject.get("Headers")),postBody4SchoolsCountAPI);
		LinkedHashMap schoolsCoutAPIResponseMap = (LinkedHashMap)JsonPath.read(schoolsCoutAPIResponse, "$");
		//List<JSONArray> modifiedAPIresponseJSONArrayList = new ArrayList<JSONArray>(Arrays.asList(modifieinitialAPIresponseJSONArraydAPIresponseJSONArray));
		JSONArray modifiedAPIresponseJSONArray = new JSONArray();
		initialAPIresponseJSONArray.forEach(initialAPIresponseJSON ->
		{
			LinkedHashMap initialAPIresponseJSONMap = (LinkedHashMap)initialAPIresponseJSON;
			String cityID = initialAPIresponseJSONMap.get("id").toString();
			if (schoolsCoutAPIResponseMap.containsKey(cityID) && !initialAPIresponseJSONMap.get("sitemapCitySchoolPageUrl").toString().toLowerCase().contains("-county/") ) {
				//modifiedAPIresponseJSONArrayList.set(0, modifiedAPIresponseJSONArrayList.get(0).remove(initialAPIresponseJSON));
				modifiedAPIresponseJSONArray.add(initialAPIresponseJSONMap);
			}
		});
		return modifiedAPIresponseJSONArray.toJSONString();
	}
	
	
	public static String BuildAPI(Object api) throws Exception
	{
		LinkedHashMap apiMap = (LinkedHashMap)api;
		String initailAPIResponse =GetAPI(apiMap);
		
		List<Object>  builtAPI= new ArrayList<Object>(Arrays.asList(initailAPIResponse));
		String rebuiltAPIResponse;
		if (!initailAPIResponse.equals(""))
		{
			rebuiltAPIResponse = RebuildAPI(initailAPIResponse,apiMap);
		}
		else return "";
		
		builtAPI.set(0, rebuiltAPIResponse);
		if (apiMap.containsKey("BuildMethod")) 
		{
			List<String> buildMethods = (List<String>) apiMap.get("BuildMethod");

			List<String> apiResponseGroupByLetters = new ArrayList<String>();
			buildMethods.forEach(buildMethod ->{
				Arrays.asList(SiteMapDataProvider.class.getMethods()).forEach(m -> 
				{
					if (m.getName().equals(buildMethod))
					{
						try {
							
							if (buildMethod.contains("GroupByLetters")) 
								{
									if (buildMethod.equals("GroupByLetters")) 
									{
										apiResponseGroupByLetters.add((String)m.invoke(new SiteMapDataProvider(), builtAPI.get(0),"abbreviation"));
										builtAPI.set(0,apiResponseGroupByLetters.get(0));
									}
									if (buildMethod.equals("GroupByLettersExcludingCounty")) 
									{
										builtAPI.set(0,m.invoke(new SiteMapDataProvider(),apiResponseGroupByLetters.get(0),"cityStatisticsPageUrl"));
									}
								}
							else if (buildMethod.equals("PlaceIntoAGroupAndShowNoneHideState"))  builtAPI.set(0,m.invoke(new SiteMapDataProvider(), builtAPI.get(0)));
							else if (buildMethod.equals("ListingCityWithCountNotZero4NoneCounty"))  builtAPI.set(0,m.invoke(new SiteMapDataProvider(), builtAPI.get(0)));
							else if (buildMethod.equals("GroupByZipcodesExcludingCounty")) builtAPI.set(0,m.invoke(new SiteMapDataProvider(), builtAPI.get(0),"abbreviation"));
							else if (buildMethod.equals("AddSchoolsCountExcluding0AndCounty")) builtAPI.set(0,m.invoke(new SiteMapDataProvider(), builtAPI.get(0)));
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});	
			});
					
		}
		return builtAPI.get(0).toString();
	}
	
	public static String ListingCityWithCountNotZero4NoneCounty(String initialAPIResponse) throws FileNotFoundException
	{
		
		String modifiedAPIResponse = initialAPIResponse;
		JSONArray initialAPIResponseArray = JsonPath.read(initialAPIResponse, "$");
		
		
		JSONArray modifiedAPIResponseArray = new JSONArray();
		List<LinkedHashMap> jsonList4Remove = new ArrayList<LinkedHashMap>();
		for (Object initialAPIResponseJson:initialAPIResponseArray)
		{
			
			LinkedHashMap initialAPIResponseJsonMap = ((LinkedHashMap)initialAPIResponseJson);
			if ((!initialAPIResponseJsonMap.get("sitemapCityPropertyPageUrl").toString().toLowerCase().endsWith("-county/"))&&(!initialAPIResponseJsonMap.get("listingCount").toString().equals("0")))
			{
				modifiedAPIResponseArray.add(initialAPIResponseJsonMap);
			}
		}
		
		int count4ModifiedAPIResponseArray = modifiedAPIResponseArray.size();
		if (count4ModifiedAPIResponseArray>90)
		{
			
			int maxSizeWithDuplicateCity = 180;
			
			JSONArray sortedByListingCountWithTopCitiesResponseArray = new JSONArray();
			sortedByListingCountWithTopCitiesResponseArray.addAll(SortJSONArray(modifiedAPIResponseArray,"listingCount",true).subList(0, maxSizeWithDuplicateCity));
			
			for (int i=0;i<maxSizeWithDuplicateCity-1;i++)
			{
				if (i<90)
				{
					
					LinkedHashMap modifiedAPI = (LinkedHashMap)sortedByListingCountWithTopCitiesResponseArray.get(i);
					String currentCityName = modifiedAPI.get("abbreviation").toString();
					String currentCityListingCount = modifiedAPI.get("listingCount").toString();
					for (int j=i+1;j<maxSizeWithDuplicateCity-1;j++)
					{
						LinkedHashMap nextModifiedAPI = (LinkedHashMap)sortedByListingCountWithTopCitiesResponseArray.get(j);
						String nextCityName = nextModifiedAPI.get("abbreviation").toString();
						String nextCityListingCount = nextModifiedAPI.get("listingCount").toString();
						if (currentCityListingCount.equals(nextCityListingCount))
						{
							if (currentCityName.equals(nextCityName))
							{
								sortedByListingCountWithTopCitiesResponseArray.remove(i);
								i--;
							}
							
						}
						else
						{
							break;
						}
					}	
				}
				else
				{
					break;
				}
			}
			modifiedAPIResponseArray = new JSONArray();
			modifiedAPIResponseArray.addAll(sortedByListingCountWithTopCitiesResponseArray.subList(0, 90));
		}
	    modifiedAPIResponseArray = SortJSONArray(modifiedAPIResponseArray,"abbreviation");
		
		
		//if (jsonList4Remove.size()>0) jsonList4Remove.forEach(json4Remove->modifiedAPIResponseArray.remove(json4Remove));
		JSONObject modifiedAPIMap = new JSONObject();
		modifiedAPIMap.put("items", modifiedAPIResponseArray);
		modifiedAPIMap.put("stateName", ((LinkedHashMap)modifiedAPIResponseArray.get(0)).get("stateName").toString());
		//FileManager.WriteFileContent("data/ConsumerWebSite/temp.json",modifiedAPIResponseArray.toJSONString());
		return modifiedAPIMap.toJSONString();
	}
	
	public static String PlaceIntoAGroupAndShowNoneHideState(String jsonString)
	{
		JSONArray jsonArr = JsonPath.read(jsonString, "$");
		List<LinkedHashMap> json4Remove = new ArrayList<LinkedHashMap>();
		jsonArr.forEach(json -> {
			LinkedHashMap jsonMap = (LinkedHashMap)json;
			if (jsonMap.get("hide").toString().equals("1"))
			{
				json4Remove.add(jsonMap);
			}
			});
		if (json4Remove.size()>0) json4Remove.forEach(json ->jsonArr.remove(json));
		return "[{\"items\":"+jsonArr.toJSONString()+"}]";

	}
	
	public static JSONArray SortJSONArray(JSONArray jsonArray,String groupByKey) throws FileNotFoundException
	{
		return SortJSONArray(jsonArray,groupByKey,false);
	}
	
	public static JSONArray SortJSONArray(JSONArray jsonArray,String groupByKey,boolean reversed) throws FileNotFoundException
	{
		JSONArray jsonArr = new JSONArray();
		jsonArr = jsonArray;
	    JSONArray sortedJsonArray = new JSONArray();
	    
	    
	    List<LinkedHashMap> jsonValues = new ArrayList<LinkedHashMap>();
	    for (int i = 0; i < jsonArr.size(); i++) {
	    	LinkedHashMap jsonHashMap = new LinkedHashMap();
	    	if (jsonArr.get(i).getClass().toString().contains("JSONObject")) jsonHashMap=(LinkedHashMap)(JsonPath.read(((JSONObject)jsonArr.get(i)).toJSONString(),"$"));
	    	else jsonHashMap=(LinkedHashMap)jsonArr.get(i);
	    	jsonValues.add((jsonHashMap));
	    }
	    Collections.sort( jsonValues, new Comparator<LinkedHashMap>() {
	        //You can change "Name" with "ID" if you want to sort by ID

	        public int compare(LinkedHashMap a, LinkedHashMap b) {
	            String valA = new String();
	            String valB = new String();
	            
                valA =a.get(groupByKey).toString();
                valB = b.get(groupByKey).toString();
                if (a.get(groupByKey).getClass().toString().toLowerCase().contains("int"))
                {

    	            Integer intA=new Integer(valA);
    	            Integer intB=new Integer(valB);
                	if (reversed==true)
		            {
		            	return intB.compareTo(intA);
		            }
		            else
		            {
		            	return intA.compareTo(intB);
		            }
                }
	            else
	            {
		            if (reversed==true)
		            {
		            	return valB.compareTo(valA);
		            }
		            else
		            {
		            	return valA.compareTo(valB);
		            }
	            }
	            //if you want to change the sort order, simply use the following:
	            //return -valA.compareTo(valB);
	        }
	    });

	    for (int i = 0; i < jsonArr.size(); i++) {
	    	JSONObject jsonObject = new JSONObject();
	    	if (jsonArr.get(0).getClass().toString().contains("JSONObject")) sortedJsonArray.add(new JSONObject(jsonValues.get(i)));
	    	else sortedJsonArray.add(jsonValues.get(i));
	    }
	    return sortedJsonArray;
	}
	
	public static String GroupByZipcodesExcludingCounty(String json,String groupByKey) throws FileNotFoundException
	{
		JSONArray groups= new JSONArray();
		try
		{
		    JSONArray jsonValues = SortJSONArray(JsonPath.read(json,"$"),groupByKey);
		    int index=0;	
		    Map<String,JSONArray> jsonValuesGroupBykey = new HashMap<String,JSONArray>();
		    
    		Object stateName=null;
	    	for (int i=0;i< jsonValues.size();i++)
	    	{
	    		JSONArray jsonArray_itemsValue = new JSONArray();
	    		LinkedHashMap tempj = new LinkedHashMap();
	    		tempj = (LinkedHashMap)jsonValues.get(i);
	    		String groupName_current = tempj.get(groupByKey).toString().substring(0, 3);
	    		if (!(tempj.get("demographicsPageUrl").toString().toLowerCase().contains("-county/")))
	    		{
	    			
		    		if (!jsonValuesGroupBykey.containsKey(groupName_current))
		    		{
		    			jsonArray_itemsValue.add(tempj);
		    		}
		    		else
		    		{
		    			jsonArray_itemsValue= jsonValuesGroupBykey.get(groupName_current);
		    			jsonArray_itemsValue.add(tempj);
		    		}
		    		
	    		
	    			jsonValuesGroupBykey.put(groupName_current, jsonArray_itemsValue);
	    			
	    		}
    			
    			//System.out.println(i);
		    }
	    	jsonValuesGroupBykey.forEach((k,v) ->
			{
	    		JSONObject jsonObject = new JSONObject();
				jsonObject.put("items", v);
				jsonObject.put("zipcodePatern", k);
				jsonObject.put("stateName", ((LinkedHashMap)v.get(0)).get("stateName"));
	    		groups.add(jsonObject);
		});
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return SortJSONArray(groups,"zipcodePatern").toJSONString();
	}
	
	public static String GroupByLetters(String json,String groupByKey)
	{

		JSONArray groups= new JSONArray();
		try
		{
		    JSONArray jsonValues = JsonPath.read(json,"$");
	
		   int index=0;	
		   for(char alphabet = 'A'; alphabet <= 'Z';alphabet++) 
		   {
	
			    JSONArray jsonArray_itemsValue = new JSONArray();
	    		String upperAlphabet = Character.toString(alphabet);
	    		Object stateName=null;
		    	for (int i=0;i< jsonValues.size();i++)
		    	{
		    		LinkedHashMap tempj = new LinkedHashMap();
		    		tempj = (LinkedHashMap)jsonValues.get(i);
	    			if (tempj.get(groupByKey).toString().startsWith(upperAlphabet) )//&& (!(tempj.get("cityStatisticsPageUrl").toString().contains("-county/")))
		    		{
	    				jsonArray_itemsValue.add(tempj);
	    				stateName=tempj.get("stateName");
		    		}
			    }
				if (jsonArray_itemsValue.size()>=1)
				{
	
					JSONObject jsonObject = new JSONObject();
					JSONArray itemsValue = new JSONArray();
					itemsValue = jsonArray_itemsValue;
					jsonObject.put("items", itemsValue);
					jsonObject.put("letter", upperAlphabet);
					jsonObject.put("stateName", stateName);
		    		groups.add(jsonObject);
		    		//System.out.println(index);
		    		
				}
				index++;
		    }
	    
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return groups.toJSONString();
	}
	
	public static String GroupByLettersExcludingCounty(String json,String groupByKey)
	{
		JSONArray groups= new JSONArray();
	    JSONArray jsonArray = JsonPath.read(json,"$");
	    //System.out.println(json);
	    JSONArray jsonArrayExcludingCounty= (JSONArray)jsonArray.clone();
    	for (int i=0;i< jsonArray.size();i++)
    	{
    		LinkedHashMap tempj = new LinkedHashMap();
    		tempj = (LinkedHashMap)jsonArray.get(i);
    		JSONArray itemsJsonArray = (JSONArray)tempj.get("items");
		    JSONArray itemsJsonArrayExcludingCounty= (JSONArray)itemsJsonArray.clone();
			for (int j=0;j<itemsJsonArray.size();j++)
			{			
				LinkedHashMap itemJSON = new LinkedHashMap();
				itemJSON = (LinkedHashMap)itemsJsonArray.get(j);
				if (itemJSON.get("cityStatisticsPageUrl").toString().contains("-county/")) 
				{
					itemsJsonArrayExcludingCounty.remove(itemJSON);
				}
				
			}
			
			((LinkedHashMap)jsonArrayExcludingCounty.get(i)).put("items", itemsJsonArrayExcludingCounty);
		}
				
		return jsonArrayExcludingCounty.toJSONString();
	}
	
	public static String AddBoroughs(String initailAPIresponse,String rebuildAPIInitialResponse) throws FileNotFoundException
	{
		String combinedAPI= (initailAPIresponse+rebuildAPIInitialResponse).replace("][", ",");
		
		return combinedAPI;
	}
	
  	public static String GetAPI(Object api) throws Exception {
  		LinkedHashMap searchedJSONObject=GetsearchedJSONObject(api);
  		String apiResponse = RestAPI.GetResponse(searchedJSONObject.get("URL").toString(), (LinkedHashMap)(searchedJSONObject.get("Headers")));
  		return apiResponse;
	}
  	
  	public static String GetAPI(Object api,Map<String,Object> postBody) throws Exception {
  		LinkedHashMap searchedJSONObject=GetsearchedJSONObject(api);
  		String apiResponse = RestAPI.GetResponse(searchedJSONObject.get("URL").toString(), (LinkedHashMap)(searchedJSONObject.get("Headers")),postBody);
  		return apiResponse;
	}
  	
  	public static LinkedHashMap GetsearchedJSONObject(Object api) throws Exception
  	{
  		LinkedHashMap apiMap= new LinkedHashMap();
  		apiMap = (LinkedHashMap)api;
  		Object apiMethod= null;
  		if (apiMap.containsKey("inputParameter4API")) apiMethod=apiMap.get("inputParameter4API");
  		LinkedHashMap searchedJSONObject = RestAPI.GetAPIInfo(apiMap.get("apiName").toString(),apiMethod);
  		return searchedJSONObject;
  	}
  	

	
}
