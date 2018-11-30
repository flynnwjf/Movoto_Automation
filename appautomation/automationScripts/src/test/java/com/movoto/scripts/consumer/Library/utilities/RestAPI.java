package com.movoto.scripts.consumer.Library.utilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.testng.annotations.DataProvider;

import com.jayway.jsonpath.internal.JsonContext;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.movoto.scripts.consumer.Library.utilities.utilities.JsonManager;
public final class RestAPI {
	private static JsonManager jsonManager = new utilities.JsonManager();private static Response response;
	private static Map<String, String> header;
	private static String contentType = null;
	public static String GetResponse(String URL,Map<String,String> inputheader)
	{
		
		inputheader.forEach((k,v)->{
			SetRequestHeader(k.toString(), v.toString());
		});
		return HTTPGet(URL);
	}
	
	public static String GetResponse(String URL,Map<String,String> inputheader,Map<String,Object> postBody)
	{
		
		inputheader.forEach((k,v)->{
			SetRequestHeader(k.toString(), v.toString());
		});
		return HTTPPost(URL,postBody);
	}
	public static String HTTPGet(String URL) {

		response = getRequest().get(getURL(URL)).andReturn();
		header.clear();
		return response.asString();
	}
	
	
	public static String HTTPPost(String URL, Map<String, Object> data) {
		JSONObject jsonObject = new JSONObject(data);
		response = getRequest().body(jsonObject).post(getURL(URL)).andReturn();
		header.clear();
		return response.asString();
	}

	private static URL getURL(String url) {
		if (url != null) {
			try {
				return new URL(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	private static RequestSpecification getRequest() {
		RequestSpecification rs = RestAssured.given();
		if (contentType != null) {
			rs.and().contentType(contentType);
		} else {
			rs.and().contentType(ContentType.JSON);
		}
		if (!header.isEmpty()) {
			rs.and().headers(header);
		}
		return rs.and();
	}

	public static void SetRequestHeader(String key, String value) {
		if (header==null) {
		 header=new HashMap<String,String>();
		}
		header.put(key, value);
	}
  	public static LinkedHashMap GetAPIInfo(String apiName,Object parameters) throws Exception {
  		LinkedHashMap searchedJSONObject = GetAPIInfo(apiName);
  		//if (parameters!=null) searchedJSONObject.put("URI",FillInParametersForURI(searchedJSONObject.get("URI").toString(),parameters));  		
  		return GetAPIInfo(searchedJSONObject,parameters);
	}
  	
  	public static LinkedHashMap GetAPIInfo(LinkedHashMap intinialSearchedAPIInfo,Object parameters) throws Exception {
  		LinkedHashMap searchedJSONObject = intinialSearchedAPIInfo;
  		if (parameters!=null) {
  			searchedJSONObject.put("URI",FillInParametersForURI(searchedJSONObject.get("URI").toString(),parameters));
  			searchedJSONObject.put("URL", searchedJSONObject.get("APIDomain").toString()+searchedJSONObject.get("URI").toString());
  		}  		
  		return searchedJSONObject;
	}
  	
  	public static LinkedHashMap GetAPIInfo(String jsonFilePath,String apiName) throws Exception {
  		
  		JsonContext apis = (JsonContext)(jsonManager.GetJsonObjectByJPath(jsonFilePath));
  		LinkedHashMap searchedJSONObject = (LinkedHashMap)apis.read(String.format("$.%s", apiName));
  		LinkedHashMap commonHeaders = ((JsonContext)apis.renameKey("$", "CommonHeaders", "Headers")).read("$.Headers");
  		
  		if (!searchedJSONObject.containsKey("Headers")) {
  			searchedJSONObject.put("Headers", commonHeaders);
  		}
  		searchedJSONObject.put("APIDomain", apis.read("$.APIDomain"));
  		searchedJSONObject.put("URL", searchedJSONObject.get("APIDomain").toString()+searchedJSONObject.get("URI").toString());
  		
  		return searchedJSONObject;
	}
  	public static LinkedHashMap GetAPIInfo(String apiName) throws Exception {
  		String jsonFilePath = "config/EndPointAPIs.json";///Users/vivian/Documents/Automation/20161225/appautomation/automationScripts/src/test/java/com/movoto/scripts/consumer/
  		return GetAPIInfo(jsonFilePath,apiName);
	}
	
	public static String FillInParametersForURI(String uriFormat,Object parameters)
	{
		LinkedHashMap parametersMap = new LinkedHashMap();
		List<String> uri = new ArrayList<String>(Arrays.asList(uriFormat));
		parametersMap = (LinkedHashMap)parameters;
		parametersMap.forEach((k,v) ->
		{
			uri.set(0, uriFormat.replace(String.format("{%s}", k), ((LinkedHashMap)parameters).get(k).toString()));
		});
		return uri.get(0);
	}
}
