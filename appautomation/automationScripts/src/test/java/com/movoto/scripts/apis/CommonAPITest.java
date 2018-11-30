package com.movoto.scripts.apis;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

import org.testng.ITestContext;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.movoto.scripts.BaseTest;
import com.movoto.scripts.consumer.Library.utilities.Assertion;
import com.movoto.scripts.consumer.Library.utilities.RestAPI;
import com.movoto.scripts.data.CommonAPIDataProvider;
import com.movoto.utils.JSONParserForAutomationNG;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class CommonAPITest extends BaseTest {
	@Test(dataProvider = "getAPITestData", dataProviderClass = CommonAPIDataProvider.class)
	public void CheckAPIResponse(ITestContext context,LinkedHashMap data)
	{
		JSONArray expectResults = (JSONArray)data.get(String.format("testData_%s", context.getName()));
		expectResults.forEach(expectResult ->
		{
			LinkedHashMap expectResultMap = (LinkedHashMap)((LinkedHashMap)expectResult).get("expectedResults");
			LinkedHashMap actualResultMap = getAPIResponseMap(data.get("testAPIName").toString(),(LinkedHashMap)expectResult);
			
			LinkedHashMap results=Assertion.Comapre4Jsons(actualResultMap,expectResultMap);
			System.out.println(results.toString());
		
		});
		
	}
	
	/*@Test(dataProvider = "getAPITestData", dataProviderClass = CommonAPIDataProvider.class)
	public void CheckAPIResponse_Version2(ITestContext context,String expectResults)
	{
			String actualResultMap = getAPIResponse(context.getCurrentXmlTest().getParameter("testAPIName"),context.getCurrentXmlTest().getParameter("inputParameter4API"));
			LinkedHashMap results=Assertion.Comapre4Jsons(actualResultMap,expectResults);
			System.out.println(results.toString());
		
	}*/

	public static LinkedHashMap getAPIResponseMap(LinkedHashMap apiTestInfo)
	{
		
		return JsonPath.read(getAPIResponse(apiTestInfo), "$");
	}
	
	public static LinkedHashMap getAPIResponseMap(String apiName,LinkedHashMap dataMap)
	{
		return JsonPath.read(getAPIResponse("",apiName,dataMap), "$");
	}
	

	public static String getAPIResponse(LinkedHashMap apiTestInfo)
	{
		String apiName = apiTestInfo.get("apiName").toString();
		return getAPIResponse("",apiName,apiTestInfo);
	}
	public static String getAPIResponse(String jsonFilePath,LinkedHashMap apiTestInfo)
	{
		String apiName = apiTestInfo.get("apiName").toString();
		return getAPIResponse(jsonFilePath,apiName,apiTestInfo);
	}

	public static String getAPIResponse(String jsonFilePath,String apiName,LinkedHashMap dataMap)
	{
		LinkedHashMap apiResponse = new LinkedHashMap();
		String apiResponseString = "{}";
		try {
			LinkedHashMap searchedJSONObject = new LinkedHashMap();
			
			if (jsonFilePath =="") searchedJSONObject = RestAPI.GetAPIInfo(apiName); 
			else searchedJSONObject = RestAPI.GetAPIInfo(jsonFilePath,apiName);
			
			String requestMethod = searchedJSONObject.get("Method").toString();
			if (requestMethod.equals("GET"))
			{
				if (dataMap.containsKey("inputParameter4API")) searchedJSONObject = RestAPI.GetAPIInfo(searchedJSONObject,dataMap.get("inputParameter4API"));
				apiResponseString = RestAPI.GetResponse(searchedJSONObject.get("URL").toString(), (LinkedHashMap)(searchedJSONObject.get("Headers")));
			}
			if (requestMethod.equals("POST"))
			{
				apiResponseString = RestAPI.GetResponse(searchedJSONObject.get("URL").toString(), (LinkedHashMap)(searchedJSONObject.get("Headers")),(LinkedHashMap)dataMap.get("postBody"));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return apiResponseString;
	}
}
