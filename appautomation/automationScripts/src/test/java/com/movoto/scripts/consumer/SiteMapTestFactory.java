package com.movoto.scripts.consumer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.common.reflect.TypeToken;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;
import com.movoto.fixtures.FixtureLibrary;
import com.movoto.scripts.BaseTest;
import com.movoto.scripts.consumer.Library.utilities.utilities.JsonManager;
import com.movoto.scripts.data.MapSearchDataProvider;
import com.movoto.utils.JSONParserForAutomationNG;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class SiteMapTestFactory {
	//private static JsonManager jsonManager = new utilities.JsonManager();
	
	@Factory(dataProvider="getSiteMapPageURL")
	public Object[] AllSiteMapTest(LinkedHashMap data)
	{
		
		List<Object> result = new ArrayList<Object>(); 
		String domainUrl = data.get("domain").toString();
		List<String> testStates = (List<String>)data.get("testStates");
		JSONArray siteMapPages = (JSONArray)data.get("siteMapPages");
		testStates.forEach(testState->{
			siteMapPages.forEach(siteMapPage ->{
				LinkedHashMap siteMapPageInfo = (LinkedHashMap)siteMapPage;
				String pageURL=domainUrl+String.format(siteMapPageInfo.get("uri").toString(), testState);
				String pageName = siteMapPageInfo.get("pageName").toString();
				Object testResult = new SiteMapBaseTest((new JSONObject(siteMapPageInfo)).toJSONString(),pageURL,testState);
				result.add(testResult);
				
				System.out.println(pageURL);
			});
		});
		return result.toArray();
	}
	
	@DataProvider(name="getSiteMapPageURL")
	@Parameters({"siteMapPageInfoJsonFilePath"})
	public Object[][] GetSiteMapPageURL(ITestContext context, Method method)
	{
		Reporter.log("Platform: " + context.getName(), true);
		
		String siteMapPageInfoJsonFilePath= context.getCurrentXmlTest().getParameter("siteMapPageInfoJsonFilePath");
		String siteMapPageInfo = this.getData(siteMapPageInfoJsonFilePath);
		LinkedHashMap siteMapPageInfos = new LinkedHashMap<>();
		
		siteMapPageInfos = JsonPath.read(siteMapPageInfo, "$");
		
		Object[][] data = {{siteMapPageInfos}};
		return data;
		
	}
	
	public String getData(String siteMapPageInfoJsonFilePath)
	{
		JSONParserForAutomationNG dataParser = new JSONParserForAutomationNG(siteMapPageInfoJsonFilePath);
		return dataParser.getNode("siteMapTest").toJSONString();
	}

}
