package com.movoto.scripts.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.testng.annotations.Test;
import com.movoto.utils.JSONParserForAutomationNG;
import com.movoto.api.pojo.PropertyData;
import com.movoto.scripts.BaseTest;

import com.movoto.scripts.data.MapSearchDataProvider;

import junit.framework.Assert;

public class Reg_92_MapSearch extends BaseTest
{
	JSONParserForAutomationNG jsonParser;
	WebDriver wDriver;
	JavascriptExecutor jse;
	int uiCount,noOfRecordsPerPage;
	JSONArray jsonArray;
	
	@Test(dataProvider = "MapSearchForWeb", dataProviderClass = MapSearchDataProvider.class, priority = 1)
	public void mapSearchByCity(Map<String, Object> data) 
	{
	    
		if (data != null)
		{
			wDriver=library.getDriver();
			jse=(JavascriptExecutor)library.getDriver();
			
			String cityNameForAddingInAddressInfo =(String)data.get("CityName");
			String mapSearchAPIResponse = scenarios.getMapSearchApiResultForCity(data,"CITY"); // hitting map search API with headers and json file for specific city.
			library.wait(5);
			boolean resStat = checkPreCondtions(data,"CITY");
			if(!resStat)
				Assert.assertTrue(resStat);
			
			String url=library.getDriver().getCurrentUrl();
			library.wait(5);
			try
			{
				PropertyData pData=null;
				noOfRecordsPerPage=scenarios.getMinNoOfRecordsPerPageUI();
				JSONParser jsonParser=new JSONParser();
				JSONObject jsonObject=(JSONObject)jsonParser.parse(mapSearchAPIResponse);
				jsonArray=(JSONArray)jsonObject.get("listings");
				for(int j=0; j<noOfRecordsPerPage; j++)
				{        
					pData=populateData(j,"CITY");
					for(int i=0;i<jsonArray.size();i++){
						boolean validationStatus = validate(pData,(JSONObject)jsonArray.get(i),j);
						if(validationStatus){
							jsonArray.remove(i);
							break;
						}
					}
    				if(j==1){
    					// 1. url of page contains "cape-coral-fl/@"
    					Assert.assertTrue(url.toLowerCase().contains(pData.getCity().trim().toLowerCase().replace(" ","-")+"-"+pData.getState().toLowerCase()+"/"));
    					
    					// 2. verify total house QTY on top/bottom of page is same with API response.
    					// Displayed total and api total are different.
    					String totalRec = null;
    					if(library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
    						totalRec = library.getTextFrom("xpath->.//div[@class='pagination']/div[@class='info']/b").split("of")[1].replaceAll(",", "").trim();
    					}else{
    						totalRec = library.getTextFrom("PROPERTY.uitotalpropertycount").split("of")[1].replaceAll(",", "").trim();
    					}
    					Assert.assertEquals(totalRec, jsonObject.get("totalCount").toString());
    					
    					// 3. "Cape Coral FL" is displayed in the search box.
    					String jsCode="return document.getElementById('headerSearchBox').value;";
    					String cityNameFromText=jse.executeScript(jsCode).toString();
    					Assert.assertEquals(cityNameFromText, cityNameForAddingInAddressInfo);
    					
    					//  4. Text -- "Cape Coral Real Estate & Homes for Sale in Cape Coral,FL" is displayed on the top.
    					if( library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB") ){
	    				}else{
	    					String pageText = library.getTextFrom("xpath->//h1[@class='userInput']").toString();
	    					Assert.assertEquals(pageText, data.get("CityText").toString());
	    				}
    					
    					// 5. all city/state info in the property list is "Cape Coral"/"FL"
    					String cityNameUI = pData.getCity().trim().toLowerCase();
    					String stateNameUI = pData.getState().trim().toLowerCase();
    					String diffCityState[] = cityNameForAddingInAddressInfo.split(" ");
    					Assert.assertEquals(cityNameUI, diffCityState[0].toLowerCase()+" "+diffCityState[1].toLowerCase());
    					Assert.assertEquals(stateNameUI, diffCityState[2].toLowerCase());
    					
    					//  Common verify for pagination: 
    						 
    					// 1. current page number is 1 and pagination info contains "1 - 50 of <totalqty from API>"
    					Assert.assertTrue(url.contains("p-1"));
    					String result=library.getTextFrom("SEARCHPAGE.bottomuitotalcountweb").replaceAll(",","");
    					Assert.assertTrue(result.contains(("of "+totalRec)));
    					
						// 2. Max page number is correct
//    					int lastCount = scenarios.getTotalCountFromUI();
//    					int pageCount = (int) Math.ceil(lastCount/noOfRecordsPerPage);
//    					int pageNumber = pageCount+1;  
//    					Assert.assertEquals(pageNumber, Integer.parseInt(library.getTextFrom("xpath->//a[@class='next']/preceding-sibling::a[1]").toString()));
    					
    					int lastCount = scenarios.getTotalCountFromUI();
    				    int pageCount = (int) Math.ceil(lastCount/noOfRecordsPerPage);
    				    if(Integer.parseInt(library.getTextFrom("xpath->//a[@class='next']/preceding-sibling::a[1]").toString()) == pageCount){
    				    	Assert.assertEquals(pageCount, Integer.parseInt(library.getTextFrom("xpath->//a[@class='next']/preceding-sibling::a[1]").toString()));
    					}else{
    						int pageNumber = pageCount+1;
    						Assert.assertEquals(pageNumber, Integer.parseInt(library.getTextFrom("xpath->//a[@class='next']/preceding-sibling::a[1]").toString()));
    					}
    					
    					
    					
						// 3. 50 Cards are showed on page 1
						Assert.assertTrue(noOfRecordsPerPage >= 50);  
					}
				}//j loop ends here
			}catch(Exception exe){
				System.out.println("Exception is =========="+exe.getMessage());
			}
		}
	}
	
	@Test(dataProvider = "MapSearchForWeb", dataProviderClass = MapSearchDataProvider.class, priority = 1)
	public void mapSearchByZipCode(Map<String, Object> data) 
	{
	    
		if (data != null)
		{
			wDriver=library.getDriver();
			jse=(JavascriptExecutor)library.getDriver();
			
			String cityZipCode =(String)data.get("ZipCode");
			String mapSearchAPIResponse = scenarios.getMapSearchApiResultForCity(data,"ZIPCODE"); // hitting map search API with headers and json file for specific city.
			library.wait(5);
			
			boolean resStat = checkPreCondtions(data,"ZIPCODE");
			if(!resStat)
				Assert.assertTrue(resStat);
			
			String url=library.getDriver().getCurrentUrl();
			library.wait(5);
			try
			{
				PropertyData pData=null;
				noOfRecordsPerPage=scenarios.getMinNoOfRecordsPerPageUI();
				JSONParser jsonParser=new JSONParser();
				JSONObject jsonObject=(JSONObject)jsonParser.parse(mapSearchAPIResponse);
				jsonArray=(JSONArray)jsonObject.get("listings");
				for(int j=0; j<noOfRecordsPerPage; j++)
				{        
					pData=populateData(j,"ZIPCODE");
					for(int i=0;i<jsonArray.size();i++){
						boolean validationStatus = validate(pData,(JSONObject)jsonArray.get(i),j);
						if(validationStatus){
							jsonArray.remove(i);
							break;
						}
					}
    				if(j==1){
    					// 1. url of page contains "cape-coral-fl/@"
    					Assert.assertTrue(url.toLowerCase().contains(pData.getState().toLowerCase()+"/"+cityZipCode));
    					
    					// 2. verify total house QTY on top/bottom of page is same with API response.
    					// Displayed total and api total are different.
    					String totalRec = null;
    					if(library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
    						totalRec = library.getTextFrom("xpath->.//div[@class='pagination']/div[@class='info']/b").split("of")[1].replaceAll(",", "").trim();
    					}else{
    						totalRec = library.getTextFrom("PROPERTY.uitotalpropertycount").split("of")[1].replaceAll(",", "").trim();
    					}
    					Assert.assertEquals(totalRec, jsonObject.get("totalCount").toString());
    					
    					// 3. "Zipcode" is displayed in the search box.
    					String jsCode="return document.getElementById('headerSearchBox').value;";
    					String cityNameFromText=jse.executeScript(jsCode).toString();
    					Assert.assertEquals(cityNameFromText, cityZipCode+" "+pData.getState());
    					
    					//  4. Text -- "Cape Coral Real Estate & Homes for Sale in Cape Coral,FL" is displayed on the top.
    					if( library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB") ){
	    				}else{
	    					String pageText = library.getTextFrom("xpath->//h1[@class='userInput']");
	    					Assert.assertEquals(pageText, data.get("ZipText").toString());
	    				}
    					
    					// 5. All houses' zip code on current page is "33914"
    					JSONObject zipOjb = (JSONObject)jsonArray.get(j);
    					String zipData = (String)(((JSONObject)zipOjb.get("address")).get("zipCode"));
    					Assert.assertEquals(zipData, cityZipCode);
    					
    					/*  Common verify for pagination: */
    						 
    					// 1. current page number is 1 and pagination info contains "1 - 50 of <totalqty from API>"
    					Assert.assertTrue(url.contains("p-1"));
    					String result=library.getTextFrom("SEARCHPAGE.bottomuitotalcountweb").replaceAll(",", "");
    					Assert.assertTrue(result.contains("of "+totalRec));
    					
						// 2. Max page number is correct
    					int lastCount = scenarios.getTotalCountFromUI();
    				    int pageCount = (int) Math.ceil(lastCount/noOfRecordsPerPage);
    				    if(Integer.parseInt(library.getTextFrom("xpath->//a[@class='next']/preceding-sibling::a[1]").toString()) == pageCount){
    				    	Assert.assertEquals(pageCount, Integer.parseInt(library.getTextFrom("xpath->//a[@class='next']/preceding-sibling::a[1]").toString()));
    					}else{
    						int pageNumber = pageCount+1;
    						Assert.assertEquals(pageNumber, Integer.parseInt(library.getTextFrom("xpath->//a[@class='next']/preceding-sibling::a[1]").toString()));
    					}
    				      
    					
    					
						// 3. 50 Cards are showed on page 1
						Assert.assertTrue(noOfRecordsPerPage >= 50);  
					}
				}//j loop ends here
			}catch(Exception exe){
				System.out.println("Exception is =========="+exe.getMessage());
			}
		}
	}
	
	@Test(dataProvider = "MapSearchForWeb", dataProviderClass = MapSearchDataProvider.class, priority = 1)
	public void mapSearchByNeighborhood(Map<String, Object> data) 
	{
	    
		if (data != null)
		{
			wDriver=library.getDriver();
			jse=(JavascriptExecutor)library.getDriver();
			
			String cityNeighborhood =(String)data.get("Neighborhood");
			//String cityNeighborhoodState =(String)data.get("NeighborhoodState");
			String mapSearchAPIResponse = scenarios.getMapSearchApiResultForCity(data,"NEIGHBORHOOD"); // hitting map search API with headers and json file for specific city.
			library.wait(5);
			
			boolean resStat = checkPreCondtions(data,"NEIGHBORHOOD");
			if(!resStat)
				Assert.assertTrue(resStat);
			
			String url=library.getDriver().getCurrentUrl();
			library.wait(5);
			try
			{
				PropertyData pData=null;
				noOfRecordsPerPage=scenarios.getMinNoOfRecordsPerPageUI();
				JSONParser jsonParser=new JSONParser();
				JSONObject jsonObject=(JSONObject)jsonParser.parse(mapSearchAPIResponse);
				jsonArray=(JSONArray)jsonObject.get("listings");
				for(int j=0; j<noOfRecordsPerPage; j++)
				{        
					pData=populateData(j,"NEIGHBORHOOD");
					for(int i=0;i<jsonArray.size();i++){
						boolean validationStatus = validate(pData,(JSONObject)jsonArray.get(i),j);
						if(validationStatus){
							jsonArray.remove(i);
							break;
						}
					}
    				if(j==1){
    					// 1. url of page contains "san-mateo-ca/marina-lagoon/@"
    					Assert.assertTrue(url.toLowerCase().contains(pData.getCity().trim().toLowerCase().replace(" ","-")+pData.getZipCode().toLowerCase().replace(", ","-")+"/"+pData.getState().toLowerCase().replace(" ","-")));
    					
    					// 2. verify total house QTY on top/bottom of page is same with API response.
    					String totalRec = null;
    					if(library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
    						totalRec = library.getTextFrom("xpath->.//div[@class='pagination']/div[@class='info']/b").split(" ")[4].replaceAll(",", "");
    					}else{
    						totalRec = library.getTextFrom("PROPERTY.uitotalpropertycount").split("of")[1].replaceAll(",", "").trim();
    					}
    					Assert.assertEquals(totalRec, jsonObject.get("totalCount").toString());
    					  
    					// 3. "marina lagoon" is displayed in the search box.
    					String jsCode="return document.getElementById('headerSearchBox').value;";
    					String cityNameFromText=jse.executeScript(jsCode).toString();
    					Assert.assertEquals(cityNameFromText, cityNeighborhood);
    					  
    					// 4. Text -- "Marina Lagoon San Mateo Real Estate & Homes for Sale" is displayed on the top.
    					if(library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
    					}else{
    						String pageText = library.getTextFrom("xpath->//h1[@class='userInput']");
        					Assert.assertEquals(pageText, data.get("NeighborhoodText").toString());
    					}
    					
    					/*  Common verify for pagination: */
    						 
    					// 1. current page number is 1 and pagination info contains "1 - 50 of <totalqty from API>"
    					Assert.assertTrue(url.contains("p-1"));
    					String result=library.getTextFrom("SEARCHPAGE.bottomuitotalcountweb");
    					Assert.assertTrue(result.contains("of "+totalRec));
    					
						// 2. Max page number is correct
    					int lastCount = scenarios.getTotalCountFromUI();
    				    int pageNumber = (int) Math.ceil(lastCount/noOfRecordsPerPage);
    					Assert.assertEquals(pageNumber, Integer.parseInt(library.getTextFrom("xpath->//a[@class='selected']").toString()));
    					
						// 3. 50 Cards are showed on page 1
    					// Dont have enough records to compare
						// Assert.assertTrue(noOfRecordsPerPage >= 50);  
					}
				}//j loop ends here
			}catch(Exception exe){
				System.out.println("Exception is =========="+exe.getMessage());
			}
		}
	}
	
	public boolean checkPropertyCount(){
		String totalPropertyCount=null;

		if(library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
			totalPropertyCount = library.getTextFrom("xpath->.//div[@class='pagination']/div[@class='info']/b");
		}else{
			totalPropertyCount = library.getTextFrom("xpath->.//div[@class='l count hidden-xs']");
		}
	    
		String totalPropertyCount1[] = totalPropertyCount.split("of");
	    //if total house is more than 3 digit than need to remove ,
	    if(totalPropertyCount.contains(","))
	    {
	    	totalPropertyCount = totalPropertyCount1[1].replaceAll(",", "").trim();
	    }
	    else {
	    	totalPropertyCount = totalPropertyCount1[1].trim();
	    }
	    int  totalPropertyCountInt= Integer.parseInt(totalPropertyCount);
	    if(totalPropertyCountInt > 100){
	    	return true;
	    }
	    else {
	    	return false;
	    }
	}
	
	public boolean validate(PropertyData pData, JSONObject jsonObject,int i)
	{
		jse=(JavascriptExecutor)library.getDriver();
		String bathroom=null ,bedroom=null,area=null;
		long price=0L;
		String uiData=null;
		Object apiResponseData=null;
		apiResponseData = (String)(((JSONObject)jsonObject.get("address")).get("addressInfo"));
		if(apiResponseData!=null && ((String)apiResponseData).contains("#APT ")){
			apiResponseData =((String)apiResponseData).replace("#APT ", "#");
		}
		
		if(pData.getAddrDet().equalsIgnoreCase(apiResponseData.toString())){
			Assert.assertEquals(pData.getAddrDet(), apiResponseData);
			
			uiData=pData.getPhotoCount();
			if(uiData!=null && !uiData.equals("")){
				apiResponseData= String.valueOf(jsonObject.get("photoCount"));
				Assert.assertEquals(uiData.split("/")[1], apiResponseData);
			}
			
//			uiData=pData.getStatus();
//			apiResponseData=String.valueOf(((JSONObject)jsonObject.get("listingStatus")).get("name"));
//			if(uiData!=null && !uiData.equals("")){
//				Assert.assertEquals(pData.getStatus().toUpperCase(),apiResponseData);
//			}
			
			apiResponseData =jsonObject.get("bathroomsTotal");
			bathroom=String.valueOf(apiResponseData!=null?Double.parseDouble(String.valueOf(apiResponseData)):"0.0");
			Assert.assertEquals(String.valueOf(pData.getNoOfBathRoom()), bathroom);

			apiResponseData=jsonObject.get("bedrooms");
			bedroom =String.valueOf(apiResponseData!=null?Double.parseDouble(String.valueOf(apiResponseData)):"0.0");
			Assert.assertEquals(String.valueOf(pData.getNoOfRoom()), bedroom);
			
			price = (long)jsonObject.get("listPrice"); // Price
			Assert.assertEquals(pData.getPrice(),price);// Validating price for property.
			
			apiResponseData=jsonObject.get("sqftTotal");
			area = String.valueOf(apiResponseData!=null?Double.parseDouble(String.valueOf(apiResponseData)):"0.0"); // Sqft Area 
			Assert.assertEquals(String.valueOf(pData.getArea()),area);// Validating Sqft for property.
		
			library.waitForElement("xpath->(//div[@class='fav']//a)["+(i+1)+"]");
			Assert.assertTrue((library.findElement("xpath->(//div[@class='fav']//a)["+(i+1)+"]"))!=null);
	
			if(library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
				if(i%2==0)
					library.scrollToElement("(//div[@class='fav']//a)["+(i+1)+"]");
			}else if(library.getCurrentPlatformType().equalsIgnoreCase("WEB_IExplore")){
			    if(i%3==0){
			        WebElement element = library.getDriver().findElement(By.xpath("(//div[@class='fav']//a)["+(i+1)+"]"));
			        Coordinates coordinate = ((Locatable)element).getCoordinates(); 
			        coordinate.onPage();
			        coordinate.inViewPort();
			    }
			}else{
				if(i%6==0)
					library.scrollToElement("(//div[@class='fav']//a)["+(i+1)+"]");
			}
			return true;
		}else{
			return false;
		}
	}
	
	//Preconditions from testlink as on 06-01-2017
	/*  a. at least 1 house's Price/Bedrooms/Bathrooms/Squarefootage > 0, Address != null, photo count > 2
	    b. total house qty > 100 */
	private boolean checkPreCondtions(Map<String, Object> data, String searchType) 
	{
		String url=null;
		if(searchType.equalsIgnoreCase("CITY")){
			url="http://spider.san-mateo.movoto.net:3024/cape-coral-fl/bed-1-0/bath-1-0/sqft-500-0/kw-photos/p-1/";
		}else if(searchType.equalsIgnoreCase("NEIGHBORHOOD"))
		{
			url="http://spider.san-mateo.movoto.net:3024/san-mateo-ca/marina-lagoon/bed-1-0/bath-1-0/sqft-500-0/p-1/";
			//url="http://spider.san-mateo.movoto.net:3024/pelican-ak/bed-1-0/bath-1-0/sqft-500-0/kw-photos/p-1/";
		}else
		{
			url="http://spider.san-mateo.movoto.net:3024/for-sale/fl/33914/bed-1-0/bath-1-0/sqft-500-0/kw-photos/p-1/";
		}
		
		library.get(url);
		library.wait(10);
		if(!searchType.equalsIgnoreCase("NEIGHBORHOOD")){
			return checkPropertyCount();
		}else{
			return true;
		}
		
	}
	
	//This method populates the UI data into PropertyData object.
	private PropertyData populateData(int j, String searchType) 
	{
	    System.out.println("J is "+j);
		String jsCode=null;
		PropertyData pData=new PropertyData();
		String uiData=null;
		
		//document.getElementsByClassName('lotinfo')[0].textContent
		//Price
		try
		{
			jsCode="return document.getElementsByClassName('price')["+j+"].innerText;";
			uiData=jse.executeScript(jsCode).toString();
		//	System.out.println("Price is ====="+uiData);
			pData.setPrice((uiData!=null && !uiData.equals(""))?Long.parseLong(uiData.replace("$", "").replaceAll(",", "")):0);
			//This is for farm with area specified in acres.
			//jsCode="if(document.getElementsByClassName('top-base-info')["+j+"]!=null) return (document.getElementsByClassName('top-base-info')["+j+"].children[7]); else return false;";
			//document.getElementsByClassName('icon-sqft')[0].previousElementSibling.innerHTML
			jsCode="if(document.getElementsByClassName('top-base-info')["+j+"].children[8].className=='icon-sqft') return true; else return false;";
			if((boolean)jse.executeScript(jsCode))
			{
				jsCode="var ele=document.getElementsByClassName('icon-sqft')["+j+"].previousElementSibling; if(ele=='null' || ele==null) return ''; else return document.getElementsByClassName('icon-sqft')["+j+"].previousElementSibling.innerHTML";
		        uiData=(String)jse.executeScript(jsCode);
		      //  pData.setArea(uiData)
			}
			else
			{
				jsCode="var ele= document.getElementsByClassName('top-base-info')["+j+"].childNodes[15]; if(ele=='null' || ele==null) return ''; else return ele.textContent;";//"return document.getElementsByClassName('baseInfo')["+j+"].childNodes[11].textContent";//"return document.getElementsByClassName('baseInfo')["+j+"].childNodes[15].textContent";
				uiData=jse.executeScript(jsCode).toString();
				
				
			}
			System.out.println("Area is ====="+uiData);
			pData.setArea((uiData!=null && !uiData.equals("") && !uiData.contains("—"))?Double.parseDouble(uiData.replace(",", "")):0);
			
			
		    jsCode="return (document.getElementsByClassName('top-base-info')["+j+"].childNodes[9].className=='icon-bed');";
		   
		//	System.out.println("Area for FARM is ====="+jse.executeScript(jsCode));
			if((boolean)jse.executeScript(jsCode))
			{
				//Bedroom
				jsCode="return document.getElementsByClassName('top-base-info')["+j+"].childNodes[7].textContent";
				uiData=jse.executeScript(jsCode).toString();
				pData.setNoOfRoom((uiData!=null && !uiData.equals("") && !uiData.contains("—"))?Double.parseDouble(uiData.trim()):0);
			//	System.out.println("Bedroom is ====="+uiData);
				
			}
			jsCode="return document.getElementsByClassName('top-base-info')["+j+"].childNodes[13].className=='icon-bath';";
		    
		    if((boolean)jse.executeScript(jsCode))
			{
		    	//Bathroom
		    //	System.out.println("J -BATHROOM DATA--"+j);
				jsCode="var ele= document.getElementsByClassName('top-base-info')["+j+"].childNodes[11]; if(ele=='null' || ele==null) return ''; else return ele.textContent;";//"return document.getElementsByClassName('baseInfo')["+j+"].childNodes[11].textContent";
			//	jsCode="document.getElementsByClassName('bathroom')["+j+"].textContent";
				uiData=jse.executeScript(jsCode).toString();
				pData.setNoOfBathRoom((uiData!=null && !uiData.trim().equals("") && !uiData.contains("—"))?Double.parseDouble(uiData.trim()):0);
			}
		  //  System.out.println("Bathroom is ====="+uiData);
			
			//document.getElementsByClassName('addresslink')[0].childNodes[1].childNodes[1].textContent
			//document.getElementsByClassName('address-city')[0].textContent
			//document.getElementsByClassName('addressDetail')[0].textContent
			jsCode="var ele= document.getElementsByClassName('addresslink')["+j+"].childNodes[1].childNodes[1]; if(ele=='null' || ele==null) return ''; else return ele.textContent;";
			uiData=jse.executeScript(jsCode).toString();
			pData.setAddrDet(uiData.replace(",",""));
			System.out.println("Address is ====="+uiData);
			jsCode="var ele= document.getElementsByClassName('address-city')["+j+"]; if(ele=='null' || ele==null) return ''; else return ele.textContent;";
			uiData=jse.executeScript(jsCode).toString();
			pData.setCity(uiData.replace(",",""));
			//System.out.println("City is ====="+uiData);
			//Check for Zipcode
			jsCode="return document.getElementsByClassName('addresslink')["+j+"].getElementsByClassName('addressDetail').length;";
			if((long)jse.executeScript(jsCode)>1)
			{
				jsCode=" var ele= document.getElementsByClassName('addresslink')["+j+"].getElementsByClassName('addressDetail')[0];  if(ele=='null' || ele==null) return ''; else return ele.textContent";
				uiData=((String)jse.executeScript(jsCode));
				pData.setState(uiData);
				jsCode=" var ele= document.getElementsByClassName('addresslink')["+j+"].getElementsByClassName('addressDetail')[1];  if(ele=='null' || ele==null) return ''; else return ele.textContent";
				uiData=((String)jse.executeScript(jsCode));
				pData.setZipCode(uiData);

			}
			else
			{
				jsCode="var ele= document.getElementsByClassName('addressDetail')["+j+"]; if(ele=='null' || ele==null) return ''; else return ele.textContent;";
				uiData=((String)jse.executeScript(jsCode));
				pData.setState(uiData);
			//	System.out.println("State is ======"+uiData);
			}
			//Status
			//document.getElementsByClassName("label pricechange")[0].textContent
			jsCode="var ele= document.getElementsByClassName('labels')["+j+"].childNodes[1]; if(ele=='null' || ele==null) return ''; else return ele.textContent;";
			uiData=jse.executeScript(jsCode).toString();
			pData.setStatus(uiData);
			System.out.println("Status is ====="+uiData);
			//Photocount
			jsCode="var ele= document.getElementsByClassName('page text-shadow')["+j+"]; if(ele=='null' || ele==null) return ''; else return ele.textContent;";
			uiData=jse.executeScript(jsCode).toString();
			pData.setPhotoCount(uiData);
			//System.out.println("Photocount is ====="+uiData);
		    // TODO Auto-generated method stub
		}catch(Exception exc)
		{
			System.out.println("Exception in Reg_92_MapSearch:populateData==>>"+exc.getMessage());
		}
		return pData;
	}
}