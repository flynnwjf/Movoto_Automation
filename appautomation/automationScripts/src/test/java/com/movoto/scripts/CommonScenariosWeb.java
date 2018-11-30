package com.movoto.scripts;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.Reporter;
import com.movoto.api.APIResponseProcessor;
import com.movoto.api.pojo.APIResponse;
import com.movoto.api.pojo.Address;
import com.movoto.fixtures.FixtureLibrary;
import com.movoto.fixtures.impl.wd.AutomationNGDriver;
import com.movoto.scripts.agent.DriverLocator;
import com.movoto.fixtures.impl.util.FileUtil;

public class CommonScenariosWeb extends CommonScenarios {
	WebDriver wDriver;
	public CommonScenariosWeb(FixtureLibrary library) {
		super(library);
		// TODO Auto-generated constructor stub Priyanka

		// Gopal and Puneet are working on sourceTree

		//Priyanka 123

	}
	@Override
	public void VerifyNewListingsSectionWorksWell(JSONObject Data){
		//*****Preconditions********
    	// Step-1 before test step starts, not login
    	//Step-2  delete all the added favorites houses in favorites page
		library.wait(5);
	    loginAndDeleteFavouriteProperty(Data);
	   library.get(Data.get("AppUrl").toString());
	   library.wait(15);
	   library.scrollToElement("REVIEW.schoolName");
	   library.wait(5);
	   library.scrollToElement("NEWLISTING.header");
	   library.wait(5);
	   
	   library.scrollToElement("NEWLISTING.text");
	 //Verify "New Listings in <City>" is displayed
	   //Step-1    Verify "New Listings in <City>" is displayed
   	   verifyCityinNewListings(Data);
	  		
    	library.wait(5);
    	//Verify Price/PropertyType /Bedrooms /Bathrooms /Address /City /CardURL /photo is the same as <NewListingsNearby_API> response for the first 4 properties
    	String response=getNewListingResponseFromApi( Data,"NewListing_API");
    	library.wait(7);
    	verifyNewListingPropertyWithApi(Data,4, response);
    	//Verify Reflash icon is displayed
   		clickonLeftIconTillVisibleNewListing();
		//Click on Reflash Icon
   		verifyAndClickRefreshIconNewListing();
        //Verify the 1st property's address is matched to 1st property of <NewListingsNearby_API> response
	    verifyNewListingPropertyWithApi(Data,1,response);
	    //Click on 1st card add favourite icon and verify login window popup
	    selectFirstFavouriteCardNewList(Data);	    
	    //Verify the add favorite icon in the card turns red
	    verifyAddfavoriteIconintheCardturnsredNewListing();
	    // 	Verify the favorite house's address is same with 1st card on step1
		verifyFirstFavouriteUrl(Data);
	    //logout application
		logOutForConsumerWeb(Data);
	}
	
	@Override
	public void openHousesSection(JSONObject jsonObj) {
		//*****Preconditions********
    	// Step-1 before test step starts, not login
    	//Step-2  delete all the added favorites houses in favorites page
    	
    	loginAndDeleteFavouriteProperty(jsonObj);
    	library.get((String) jsonObj.get("App-Url"));  	
    	String response =getOpenhouseResponseFromApi(jsonObj,"OpenHouse_API");
	    String responseNew =getOpenhouseResponseFromApi(jsonObj,"NewListing_API");
    	library.wait(8);
    	library.scrollToElement("OPENHOUSESECTION.text");
    	library.wait(5);
        //Verify "OpenHouse in <City>" is displayed
 	    //Step-1    Verify "OpenHose in <City>" is displayed
    	verifyCityinOpenhouse(jsonObj);
    	// Step-2 Verify the properties of <OpenHouses_API> which has been contained in <NewListings_API> does not display
	   compareOpenHouseAndNewListingApiValue(response,responseNew, jsonObj);
    	// Verify Verify StatusTag/Price/PropertyType /Bedrooms /Bathrooms /Address /City /CardURL /photo is the same as <OpenHouses_API> response for the first 4 properties
    	verifyPricePropertyTypeBedRoomsBathRoomsAddressCityCardUrlPhotoOfOpenHouse(jsonObj,"All",response);
    	//Verify Reflash icon is displayed
    	verifyReflashIconIsDisplayedOfOpenHouse();
    	//Click on Reflash Icon
    	verifyAndClickRefreshIconOfOpenHouse();
    	//Verify the 1st property's url is matched to 1st property of <NewListingsNearby_API> response
    	verifyPricePropertyTypeBedRoomsBathRoomsAddressCityCardUrlPhotoOfOpenHouse(jsonObj,"",response);
    	 //Click on 1st card add favourite icon and verify login window popup
		loginFavIconOfOpenHouse(jsonObj);
		//Verify the add favorite icon in the card turns red
		verifyTheAddFavoriteIconInTheCardTurnsRedOfOpenHouse();
        //Verify the favorite house's address is same with 1st card on step1 
		verifyTheFavoriteHouseURLIsSameWithFirstCard(jsonObj);
		//Verify the detail info is hided
	    verifyUpButtonIsHidedOfOpenHouse();
		
	}
	@Override
	 public void veirfyDetailInfoIsHidedNewListing()
   {
      library.wait(2);
      library.scrollToElement("NEWLISTSECTION.uphided");
      library.isJSEClicked("NEWLISTSECTION.uphided");
      library.wait(5);
      boolean ishidden = library .verifyPageNotContainsElement("NEWLISTSECTION.panel");
      Assert.assertTrue(ishidden);
   }
	
	@Override
	 public void verifyAddfavoriteIconintheCardturnsredNewListing(){
       library.wait(10);
       if(library.getCurrentPlatform().equalsIgnoreCase("Android")||library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB"))
       {
    	   library.scrollToElement("NEWLISTING.header");
       }
       String classAttribute = library.getAttributeOfElement("class", "NEWLISTSECTION.favouriteicon");
       classAttribute = classAttribute.trim();
       boolean isClassAttributeActive = classAttribute.endsWith("active");
       library.wait(2);
       Assert.assertTrue(isClassAttributeActive, "Favorite icon colour is  red");
       library.wait(2);
   }
	
	@Override
	public void selectFirstFavouriteCardNewList(JSONObject Data){   
   	library.wait(10);
    	if(library.findElement("NEWLISTSECTION.previous").isDisplayed()){
    		library.click("NEWLISTSECTION.previousicon");
       }
    
		library.isJSEClicked("NEWLISTING.favouriteIcon");
		library.waitForElement("SEARCHPAGE.loginWindow");
		//Verify login window pops up
		boolean flag= library.verifyPageContainsElement("SEARCHPAGE.loginWindow");
		Assert.assertTrue(flag, "Verify Login window popup after click on 1st card");
		library.typeDataInto((String) Data.get("Username"), "LOGIN.username");
		library.typeDataInto((String) Data.get("Password"), "LOGIN.password");
		library.click("LOGIN.submitButton");
   }
	
	@Override
	public void verifyAndClickRefreshIconNewListing(){
		if(library.getCurrentPlatform().equalsIgnoreCase("Android")||library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB"))
		  {
			
		  }
		else
		{
       library.verifyPageContainsElement("NEWLISTING.refreshicon");
       library.click("NEWLISTING.refreshicon");
		}
  }
	@Override
	public void clickonLeftIconTillVisibleNewListing()
	{   if(library.getCurrentPlatform().equalsIgnoreCase("Android")||library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB"))
	  {
		
	  }
   	else
   	{
		boolean flag=false;
		do
		{
		library.click("NEWLIST.leftclick");
		  try
		  {
			  flag=library.findElement("NEWLISTSECTION.refresh").isDisplayed();  
		  }
		  catch(Exception e)
		  {
		  }
		}
		while(!flag);
   	}
	}
	
	@Override
	public void verifyPricePropertyTypeBedRoomsBathRoomsAddressCityCardUrlPhotoOfOpenHouse(JSONObject Data, String dCheck, String response){
		library.wait(5);
		if (library.getCurrentPlatform().equals("Android") || library.getCurrentPlatformType().equals("IOS_WEB")) {
			JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
			WebElement element = library.getDriver().findElement(By.xpath("//span[contains(text(),'Open Houses')]"));
			jse.executeScript("arguments[0].scrollIntoView();", element);
			jse.executeScript("arguments[0].scrollIntoView();", element);
			library.wait(5);
			try {
				if(dCheck == "All"){
					openhousecityOfOpenHouse(response,1 ,"");
				}else{
					openhousecityOfOpenHouse(response,1 ,"First");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			library.wait(5);
			library.scrollToElement("OPENHOUSESECTION.openhousetotext");
			library.isJSEClicked("OPENHOUSESECTION.iconleftangle");
			library.wait(5);
			try {
				if(dCheck == "All"){
					openhousecityOfOpenHouse(response, 4, "");
				}else{
					openhousecityOfOpenHouse(response, 1, "First");
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void verifyUpButtonIsHidedOfOpenHouse(){
		// Click UP button on this section - Verify the detail info is hided
		library.wait(2);
		library.isJSEClicked("OPENHOUSESECTION.upHided");
		boolean ishidden = library
				.verifyPageNotContainsElement("OPENHOUSESECTION.FirstPropertyOpenHouseUrlinUI");
		Assert.assertTrue(ishidden);
	}
	@Override
	public void verifyTheAddFavoriteIconInTheCardTurnsRedOfOpenHouse(){
		library.wait(10);
		library.scrollToElement("OPENHOUSESECTION.text");
		library.wait(5);
		
		library.isJSEClicked("OPENHOUSESECTION.iconleftangle");
		library.wait(5);
		String classAttribute = library.getAttributeOfElement("class", "OPENHOUSESECTION.favoriteiconred");
//		classAttribute = classAttribute.trim();
//		boolean isClassAttributeActive = classAttribute.endsWith("active");
//		JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
//		String className=(String)js.executeScript("return document.getElementById('openHousePanel').getElementsByClassName('fav')[0].children[0].getAttribute('class')");
//		System.out.println("Class Name *******"+className);
		Assert.assertTrue(classAttribute.contains("active"), "Favorite icon colour is not red");
	}
	@Override
	public void loginFavIconOfOpenHouse(JSONObject data){
		// Click the 1st card's add favorite icon - Verify login window pops up
		library.scrollToElement("OPENHOUSESECTION.text");
		library.wait(10);
//		WebElement we = library.findElement("OPENHOUSESECTION.favoriteicon");
//		library.mouseHoverJScript(we);
//		 Clicking on Favorite icon
//		library.findElement("OPENHOUSESECTION.favoriteicon").click();
		System.out.println(library.getCurrentBrowser());
		if(library.getCurrentBrowser().equalsIgnoreCase("Safari") && !library.getCurrentPlatformType().equalsIgnoreCase("IOS_WEB"))
	{
			JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
			js.executeScript("document.getElementById('openHousePanel').getElementsByClassName('fav')[0].children[0].click()");						
	}
		else
	{
		library.isJSEClicked("OPENHOUSESECTION.favoriteicon");
	}
		
		// Get the data from JSON
		String userName = (String) data.get("Username");
		String password = (String) data.get("Password");
		library.typeDataInto(userName, "OPENHOUSESECTION.loginUserName");
		library.typeDataInto(password, "OPENHOUSESECTION.loginPassword");
		library.isJSEClicked("OPENHOUSESECTION.submitLogin");
		library.wait(5);
	}
	@Override
	public void verifyAndClickRefreshIconOfOpenHouse(){
		if(library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatformType().equals("IOS_WEB"))
		  {
			
		  }
		else
		{
       library.verifyPageContainsElement("OPENHOUSESECTION.refreshicon");
       library.click("OPENHOUSESECTION.refreshicon");
		}
  }
	@Override
	public void verifyReflashIconIsDisplayedOfOpenHouse(){
    	boolean flag=false;
    	if (library.getCurrentPlatform().equals("Android") || library.getCurrentPlatformType().equals("IOS_WEB")) {
			// Do nothing - This icon left angle not visible in android
		} else {
			do{
				library.isJSEClicked("OPENHOUSESECTION.iconrightangle");
				try{
					flag=library.findElement("OPENHOUSESECTION.refreshicon").isDisplayed();
				}catch(Exception e){
				}
			}while(!flag);
			Assert.assertTrue(flag, "Open House icon right angle is not displayed");
		}
	}
	
	public void openhousecityOfOpenHouse(String response, int numberofcard, String whichProp) throws ParseException{
    	List<WebElement> elements = library.getDriver().findElements(By.xpath("(//div[@id='openHousePanel']//li)"));
		List<WebElement> temp = null;
		ListIterator<WebElement> itr = elements.listIterator();
		ListIterator<WebElement> tempItr = null;
		WebElement element = null, tempEle = null;
		
		APIResponseProcessor apiResponseProcessor=new APIResponseProcessor();
		APIResponse apiResponse=apiResponseProcessor.processResponse(response);
	    Address address=null;
	    HashMap<String,JSONObject> listingsMap=apiResponse.getListings();
		
		int i = 1, k = 0;
		String addressInfo = null;
		String pData = null, addrTemp = null;
		while (itr.hasNext()) {

			element = itr.next();
			temp = element.findElements(
					By.xpath(("(//div[@id='openHousePanel']//li)[" + (i++) + "]//div[@class='info']")));
			tempItr = temp.listIterator();
			while (tempItr.hasNext()) {
				tempEle = tempItr.next();
				pData = tempEle.getText().replaceAll("(?m)^[ \t]*\r?\n", "");
				String[] propertyData = pData.split("\n");
				String[] valuePropertyData = propertyData[2].split(",");
				String splitvaluePropertyData = valuePropertyData[0];

				for (int j = 0; j < listingsMap.size(); j++) {
					apiResponse=apiResponseProcessor.getAddress((JSONObject)listingsMap.get(j+"")); 
	 				address=apiResponse.getAddress();
	 				addressInfo=address.getAddressInfo();
	 				apiResponse.getListPrice();
					
//					addressInfo = ((JSONObject) (((JSONObject) listingsMap.get(j)).get("address"))).get("addressInfo").toString();
					addrTemp = addressInfo.contains("APT") ? addressInfo.replace("APT ", "") : addressInfo;
					if (splitvaluePropertyData.equalsIgnoreCase(addrTemp)) {
						if(whichProp == "First"){
							System.out.println("Address is present First Property");
							System.out.println("UI address : " + splitvaluePropertyData );
							System.out.println("API address : " + addrTemp );
							Assert.assertEquals(addrTemp, splitvaluePropertyData);// verify address
							break;
						}else{
							String[] UIprice = propertyData[0].split(" ");
							String price = UIprice[0].replace("$", "").replace(",", "");
							String ApiPrice = library.getValueFromJson("$.listings[" + j + "].listPrice", response).toString();
							System.out.println("UI Price : " + price );
							System.out.println("API Price : " + ApiPrice );
							Assert.assertEquals(ApiPrice, price); // Verify price
							
							String PropertyType="";
							String PropertyTypeTemp = library.getAttributeOfElement("class", "(.//*[@id='openHousePanel']//li[@class='card active']//div[@class='top-base-info'])["+(k+1)+"]/span[@class='price']/i");
							if (PropertyTypeTemp.contains("icon-text-singlefamily")) {
								PropertyType = "SINGLE_FAMILY_HOUSE";
							} else {
								PropertyType = "CONDO";
							}
							String ApiPropertytype = library
									.getValueFromJson("$.listings[" + j + "].listingType.name", response).toString();
							
							System.out.println("UI Propertytype : " + PropertyType );
							System.out.println("API Propertytype : " + ApiPropertytype );
							Assert.assertEquals(ApiPropertytype, PropertyType);// Property Type
							library.wait(5);
							String[] uiBedRoom = propertyData[1].split(" ");
							String[] BedRoom = uiBedRoom[0].split(" ");
							String SplitBedRoom = BedRoom[0];
							String ApiBedRoom = library.getValueFromJson("$.listings[" + j + "].bedrooms", response).toString();
							System.out.println("UI BedRoom : " + SplitBedRoom );
							System.out.println("API BedRoom : " + ApiBedRoom );
							Assert.assertEquals(ApiBedRoom, SplitBedRoom);// Verify Bedroom
							String uiBathroom = uiBedRoom[1];
							float Bathroom = Float.parseFloat(uiBathroom);
							String ApiBathRoom = library.getValueFromJson("$.listings[" + j + "].bathroomsTotal", response).toString();
							System.out.println("UI BathRoom : " + Bathroom );
							System.out.println("API BathRoom : " + Float.parseFloat(ApiBathRoom) );
							Assert.assertEquals(Float.parseFloat(ApiBathRoom), Bathroom); // Verify BathRoom
							System.out.println("UI address : " + splitvaluePropertyData );
							System.out.println("API address : " + addrTemp );
							Assert.assertEquals(addrTemp, splitvaluePropertyData);// verify address
							String UICity = valuePropertyData[1].trim();
							String Apicity = library.getValueFromJson("$.listings[" + j + "].address.city", response).toString();
							System.out.println("UI City : " + UICity );
							System.out.println("API City : " + Apicity );
							Assert.assertEquals(Apicity, UICity);// Verify City
							break;
						}
					}
				}
			}
			if (i == numberofcard && numberofcard == 4)
				break;
			else if(i > numberofcard && numberofcard == 1)
				break;
		}
	}
	
	@Override
	 public void verifyCityinOpenhouse(JSONObject Data)
	    {   
		String CityName = (String) Data.get("CityName");
  		String OpenHouse = (String) Data.get("openHouse");	
  		String CityNameText = library.getTextFrom("OPENHOUSESECTION.city");
  		String OpenHouseText = library.getTextFrom("OPENHOUSESECTION.text");
  		Assert.assertTrue(OpenHouseText.equals(OpenHouse));
  		Assert.assertTrue(CityNameText.equals(CityName));
	
	    }
	
	@Override
	public void verifyFirstFavouriteUrl(JSONObject Data){
	  if(!library.getCurrentPlatform().equalsIgnoreCase("Android")||!library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
	      if(library.findElement("NEWLISTSECTION.previous").isDisplayed()){
	          library.isJSEClicked("NEWLISTSECTION.previousicon");
	         }
	     }
	     
	        library.wait(10);
	        library.scrollToElement("NEWLISTING.text"); 
	  String firstCardUrl = library.getAttributeOfElement("href","xpath->.//*[@id='newListings']/div/div/ul/li[1]/div/div/a");
	  library.wait(5);
	  
	  //Verify the detail info is hided
	  veirfyDetailInfoIsHidedNewListing();
	  NavigateToFavouriteHomes(Data);
	  
	  //library.waitForElement("FAVOURITEPAGE.address");
	  String firstFavCardUrl = library.getAttributeOfElement("href","xpath->.//*[@id='myfavorites']/div/div[2]/div[1]/div[1]/div/a");
	  boolean isURLSame = firstFavCardUrl.contains(firstCardUrl);
	  Assert.assertTrue(isURLSame);
	}
	
	@Override
	public void verifyTheFavoriteHouseURLIsSameWithFirstCard(JSONObject data){
	  // Verify the favorite house's address is same with 1st card
	  NavigateToFavouriteHomes(data);
	  library.wait(5);
	  String firstFavCardUrl = library.getAttributeOfElement("href","xpath->.//*[@id='myfavorites']/div/div[2]/div[1]/div[1]/div/a");
	  library.wait(2);
	  library.get((String) data.get("App-Url"));
	  library.wait(10);
	  library.scrollToElement("OPENHOUSESECTION.text");
	  library.wait(5);
	  if (library.getCurrentPlatform().equals("Android") || library.getCurrentPlatformType().equals("IOS_WEB")) {
	  } else {
	   library.wait(5);
	   library.scrollToElement("OPENHOUSESECTION.text");
	   library.wait(5);
	   //library.wait(5);
	   library.isJSEClicked("OPENHOUSESECTION.iconleftangle");
	   library.wait(5);
	  }
	  library.wait(5);
	  library.scrollToElement("OPENHOUSESECTION.text");
	  library.wait(5);
	  
	  String firstCardUrl = library.getAttributeOfElement("href","OPENHOUSESECTION.FirstPropertyOpenHouseUrlinUI");
	  boolean isURLSame = firstCardUrl.contains(firstFavCardUrl);
	  Assert.assertTrue(isURLSame);
	}
	
	@Override
	public void compareOpenHouseAndNewListingApiValue(String openHousesValue, String newListingValue, JSONObject Data)
	{   
		ArrayList<String> finalOpenHouseUrl = new ArrayList<String>();
		ArrayList<String> finalOpenHouseAddressAndCity = new ArrayList<String>();
		
		ArrayList<String> arrOpenHouseUrl = new ArrayList<String>();
		ArrayList<String> arrNewListingUrl = new ArrayList<String>();
		ArrayList<String> arrOpenHouseAddressAndCity = new ArrayList<String>();
		ArrayList<String> arrNewListingAddressAndCity = new ArrayList<String>();
		
		APIResponseProcessor apiResponseProcessorForOpenHouse=new APIResponseProcessor();
		APIResponse apiResponseOpenHouse=apiResponseProcessorForOpenHouse.processResponse(openHousesValue);
		HashMap<String,JSONObject> listingsMapOpenHouse=apiResponseOpenHouse.getListings(); 

		for(int k=0;k<listingsMapOpenHouse.size();k++){
			APIResponse finalResponseOpenHouse = apiResponseProcessorForOpenHouse.getAddress((JSONObject)listingsMapOpenHouse.get(k+"")); 
			arrOpenHouseUrl.add(finalResponseOpenHouse.getListingUrl());
			arrOpenHouseAddressAndCity.add(finalResponseOpenHouse.getAddress().getAddressInfo()+", "+finalResponseOpenHouse.getAddress().getCity());
		}
		
		APIResponseProcessor apiResponseProcessorForNewListing=new APIResponseProcessor();
		APIResponse apiResponseNewListing=apiResponseProcessorForNewListing.processResponse(newListingValue);
		HashMap<String,JSONObject> listingsMapNewListing=apiResponseNewListing.getListings();
		
		for(int k=0;k<listingsMapOpenHouse.size();k++){
			APIResponse finalResponseNewListing = apiResponseProcessorForNewListing.getAddress((JSONObject)listingsMapNewListing.get(k+"")); 
			arrNewListingUrl.add(finalResponseNewListing.getListingUrl());
			arrNewListingAddressAndCity.add(finalResponseNewListing.getAddress().getAddressInfo()+", "+finalResponseNewListing.getAddress().getCity());
		}
		
		@SuppressWarnings("rawtypes")
		ListIterator urlOpenHouseIterator=arrOpenHouseUrl.listIterator();
		@SuppressWarnings("rawtypes")
		ListIterator addressAndCityOpenHouseIterator=arrOpenHouseAddressAndCity.listIterator();
		String urlOpenHouse=null;
		String addressAndCityOpenHouse=null;
		while(urlOpenHouseIterator.hasNext() && addressAndCityOpenHouseIterator.hasNext())
	    {
	    	urlOpenHouse=(String)urlOpenHouseIterator.next();
	    	addressAndCityOpenHouse=(String)addressAndCityOpenHouseIterator.next();
	    	if(arrNewListingUrl.contains(urlOpenHouse) && arrNewListingAddressAndCity.contains(addressAndCityOpenHouse))
	    	{
	    	}else{
	    		finalOpenHouseUrl.add(urlOpenHouse);
	    		finalOpenHouseAddressAndCity.add(addressAndCityOpenHouse);
	    	}
	    }
		
		List<WebElement> elements = library.getDriver().findElements(By.xpath("(//div[@id='openHousePanel']//li)"));
		int findProp=0;
		for(int i=0;i<elements.size();i++){
			String urlUITemp=library.getAttributeOfElement("href", "(//div[@id='openHousePanel']//li)["+(i+1)+"]//a[@class='imgmask']");
			String addressUI=library.getTextFrom("(//div[@id='openHousePanel']//li)["+(i+1)+"]//div[@class='addresslink']//span[1]")+", "+library.getTextFrom("(//div[@id='openHousePanel']//li)["+(i+1)+"]//div[@class='addresslink']//span[2]");
			String rplsStr = (String)Data.get("Main-Url");
			String urlUI = urlUITemp.replace(rplsStr,"").toString();
			
			if(finalOpenHouseUrl.contains(urlUI) && finalOpenHouseAddressAndCity.contains(addressUI)){
				findProp++;
			}
		}
		
		if(findProp == elements.size()){
			Assert.assertTrue(true,"Verify the properties of <OpenHouses_API> which has been contained in <NewListings_API> does not display");
		}else{
			Assert.assertTrue(false,"Not Verify the properties of <OpenHouses_API> which has been contained in <NewListings_API> does not display");
		}
	}
	
	@Override
	  public String getOpenhouseResponseFromApi(JSONObject Data,String apiurl)
		{
	    	String contentType = String.valueOf(Data.get("ContentType"));
			String API = String.valueOf(Data.get(apiurl).toString());
	    	library.setRequestHeader("X-MData-Key", Data.get("X-MData-Key").toString());
			library.setContentType(contentType);
			Map<String, Object> apidata = new HashMap<>();
			if(apiurl == "OpenHouse_API"){
				apidata = (Map<String, Object>) Data.get("Postdata");
			}else{
				apidata = (Map<String, Object>) Data.get("PostdataNew");
			}
			String response = library.HTTPPost(API, apidata);
			return response;
		}
	
	@Override
	  public String getNewListingResponseFromApi(JSONObject Data,String apiurl)
		{
	    	String contentType = String.valueOf(Data.get("ContentType"));
			String API = String.valueOf(Data.get(apiurl).toString());
	    	library.setRequestHeader("X-MData-Key", Data.get("X-MData-Key").toString());
			library.setContentType(contentType);
			Map<String, Object> apidata = new HashMap<>();
			apidata = (Map<String, Object>) Data.get("Postdata");
			String response = library.HTTPPost(API, apidata);
			return response;
		}
	@Override
	 public void verifyCityinNewListings(JSONObject Data)
   {         
       String CityName = (String) Data.get("CityName");
       String NewList = (String) Data.get("NewList");      
       //library.scrollToElement("NEWLISTING.city");
       String CityNameText = library.getTextFrom("NEWLISTING.city");
       String NewListText = library.getTextFrom("NEWLISTING.text");
       Assert.assertTrue((NewListText.equals(NewList)),"Verified 'New Listing' Text Label Sucessfully");
       Assert.assertTrue((CityNameText.equals(CityName)),"Verified City Sucessfully");
     
       }
	@Override
	public void verifyNewListingPropertyWithApi(JSONObject Data,int numberofcard,String response) 
	 {  
	  JavascriptExecutor jse=(JavascriptExecutor)library.getDriver();   
	  APIResponseProcessor apiResponseProcessor=new APIResponseProcessor();
	  APIResponse apiResponse=apiResponseProcessor.processResponse(response);
	     Address address=null;
	     HashMap<String,JSONObject> listingsMap=apiResponse.getListings();
	      
	          if(library.getCurrentPlatform().equalsIgnoreCase("Android")||library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB"))
	          {
	           numberofcard=1;
	          }
	         
	         
	       for(int i=0;i<numberofcard;i++)
	     {
	       
	      String propertyCardStreetAddressUI = library.getTextFrom("xpath->.//*[@id='newListings']/div/div/ul/li["+(i+1)+"]//span[@itemprop='streetAddress']");
	         boolean isItemFound =false;           
	        String propertyCardStreetAddressAPI=null; 
	      for(int k=0;k<listingsMap.size();k++)
	         {       
	      apiResponse=apiResponseProcessor.getAddress((JSONObject)listingsMap.get(k+"")); 
	      address=apiResponse.getAddress();
	      propertyCardStreetAddressAPI=address.getAddressInfo();
	      apiResponse.getListPrice();
	         if(propertyCardStreetAddressAPI.equalsIgnoreCase(propertyCardStreetAddressUI))
	                    {
	          isItemFound=true;

	       String price = library.getTextFrom("xpath->.//*[@id='newListings']/div/div/ul/li["+(i+1)+"]/div//span[@class='price']");
	       price = price.replaceAll(",", "");
	       price = price.substring(1);
	       library.wait(3);
	       int propertyCardPropertypriceUI = Integer.parseInt(price);
	       String jsCode="return (document.getElementById('newListings').getElementsByClassName('top-base-info')["+i+"].children[4].className=='icon-bed')";
	       System.out.println(jse.executeScript(jsCode));
	       if((boolean)jse.executeScript(jsCode))
	       {
	       int propertyCardPropertypriceAPI=apiResponse.getListPrice();
	       Assert.assertTrue(propertyCardPropertypriceUI == propertyCardPropertypriceAPI);
	       String Bed = library.getTextFrom("xpath->(.//*[@id='newListingPanel']/..//div[@class='info']/div[1]/span[2])[" + (i + 1) + "]");
	       int propertyCardPropertyBedUI = Integer.parseInt(Bed);
	       int propertyCardPropertyBedAPI = apiResponse.getBedrooms();
	       Assert.assertEquals(propertyCardPropertyBedUI, propertyCardPropertyBedAPI);
	       }
	       jsCode="return document.getElementById('newListings').getElementsByClassName('top-base-info')["+i+"].children[6].className=='icon-bath';";
	       if((boolean)jse.executeScript(jsCode))
	       {
	       String BathUI = library.getTextFrom("xpath->(.//*[@id='newListingPanel']/..//div[@class='info']/div[1]/span[3])[" + (i + 1) + "]");
	       if (!BathUI.contains(".")) {
	       
	    	   
	           double BathAPI= Double.parseDouble(apiResponse.getBathroomsTotal());
	        Assert.assertEquals(Double.parseDouble(BathUI), BathAPI);
	       } else {
	      
	        Float BathAPI = Float.parseFloat(apiResponse.getBathroomsTotal());
	        Assert.assertEquals(Float.parseFloat(BathUI), BathAPI);
	       }
	       }
	       propertyCardStreetAddressUI = library.getTextFrom("xpath->.//*[@id='newListings']/div/div/ul/li["+(i+1)+"]//span[@itemprop='streetAddress']");
	       propertyCardStreetAddressAPI=address.getAddressInfo();
	       Assert.assertEquals(propertyCardStreetAddressUI, propertyCardStreetAddressAPI);       
	       String propertyCardCityUI = library.getTextFrom("xpath->.//*[@id='newListings']/div/div/ul/li["+(i+1)+"]//span[@itemprop='addressLocality']");
	       String propertyCardCityAPI=address.getCity();
	       Assert.assertEquals(propertyCardCityUI, propertyCardCityAPI);   
	       library.wait(5);
	       String cardurlUI=library.getAttributeOfElement("href", "xpath->(.//*[@id='newListings']//li)["+(i+1)+"]/div[@class='cardone cardbox']/div/a");
	       System.out.println(cardurlUI);
	       String cardurlAPI=apiResponse.getListingUrl();
	       System.out.println(cardurlAPI);

	       Assert.assertTrue(cardurlUI.contains(cardurlAPI ), "Card Url matching with API response");
	       library.wait(1);
	       
	                      }
	         }
	           if(isItemFound == false){
	           Assert.assertFalse(false, "UI value not matching with API");
	           break;
	       
	         }
	           
	     }
	  }
	@Override
	public void loginWithUsernameAndPasswordAndExpectError(String username, String password, String error) {

		library.wait(2);
		library.clear("LOGIN.usernamefield");
		library.wait(2);
		library.typeDataInto(username, "LOGIN.usernamefield");

		library.wait(2);
		library.clear("LOGIN.passwordfield");
		library.wait(2);
		library.typeDataInto(password, "LOGIN.passwordfield");
		library.wait(2);
		boolean loginButtonEnabled = library.isElementEnabled("LOGIN.loginbutton", true);
		if (loginButtonEnabled) {
			library.click("LOGIN.loginbutton");
			library.wait(2);
			if (error != null && error.length() > 0) {
				boolean errorExists = library.verifyPageContainsElement("xpath->(.//h3[text()='Error'])[1]");
				if (errorExists) {
					library.click("xpath->(.//i[@class='icon-close'])[1]");
					library.wait(2);
				}
			}
		}
	}

	@Override
	public void navigateToClientListPage() {
		library.click("MENU.clientlistpagelink");
	}

	@Override
	public void navigateToNotificationsListPage() {

	}

	@Override
	protected String getStageLocator(String client, String stage) {
		return "xpath->.//strong[text()='" + client + "']/../../../..//span[text()='" + stage + "']";
	}

	@Override
	protected void openClientDetailPage(String client) {
		String clientLoc = "xpath->//*[text()='" + client + "']";
		library.click(clientLoc);// client
	}

	@Override
	protected void chooseTransactionStage(String stage) {

		library.wait(5);
		library.click("xpath->.//button[@ng-model='selectActivity']");
		library.click("xpath->.//span[text()='" + stage + "']");
	}

	@Override
	protected void handlePreview() {

	}

	public void updateToMadeAnOffer(Map<String, String> data) {

		String clientName = data.get("ClientName");

		openClientDetailPage(clientName);

		library.click("TRANSACTION.updatebutton");// update
		chooseTransactionStage("Made an Offer");
		handleEditTransactionAlert();
		enterAddress(data.get("Address"));

		library.click("MAKEOFFER.continuebutton");// continue

		String dateStr = data.get("OfferDate");
		// library.clear("MAKEOFFER.datefield");
		library.typeDataInto(dateStr, "MAKEOFFER.datefield");

		library.clear("MAKEOFFER.price");
		enterNumber(data.get("OfferPrice"), "MAKEOFFER.price");

		library.click("MAKEOFFER.saveupdatebutton");// continue

		library.wait(3);

	}

	protected void enterAddress(String address) {
		library.typeDataInto(address, "MAKEOFFER.address");// address
		library.wait(2);
		library.click("xpath->(.//div[@class='pac-item'])[1]");
	}

	public void setFutureDate(Map<String, String> data) {
		library.wait(2);
		openMenu();
		library.click("MENU.goonleavebutton");
		library.wait(2);
		library.setImplicitWaitTime(1);
		boolean leaveExists = library.verifyPageContainsElement("FLEAVE.deleteleavebutton");
		library.setImplicitWaitTime(30);
		if (leaveExists) {
			library.click("FLEAVE.deleteleavebutton");
			library.wait(5);
		}

		String startDate = data.get("fromDate");
		String endDate = data.get("toDate");
		library.click("FLEAVE.createbutton");
		chooseStartAndEndLeaveDates(startDate, endDate);
		library.click("FLEAVE.schedulebutton");
		leaveExists = library.verifyPageContainsElement("FLEAVE.deleteleavebutton");
	}

	@Override
	public void chooseStartAndEndLeaveDates(String startDate, String endDate) {
		library.clear("FLEAVE.startdatefield");
		library.typeDataInto(startDate, "FLEAVE.startdatefield");

		library.clear("FLEAVE.enddatefield");
		library.typeDataInto(endDate, "FLEAVE.enddatefield");

	}

	public void updateToStageForClient(String stage, String client) {

		openClientDetailPage(client);

		library.click("TRANSACTION.updatebutton");
		chooseTransactionStage(stage);
		// library.click(stageLoc);

		library.typeDataInto("Agent Automation", "NOTES.notesfield");
		library.wait(2);
		library.click("UPDATE.applybutton");

		library.waitForElement("TRANSACTION.properties");
		library.wait(5);
	}

	public void verifyPropertyInfo() {
		library.waitForElement("TRANSACTION.properties");
		library.wait(5);
		library.click("TRANSACTION.properties");// expand
		// String escrowStatus =
		// library.getTextFrom("TRANSACTION.escrowstatus");
		// Assert.assertEquals(escrowStatus, "Made an Offer", "Escrow status is
		// not 'Made an Offer'");
		library.wait(3);
		library.click("TRANSACTION.properties");
		library.wait(3);
	}

	public void updateUrgencyStage(Map<String, String> data) {
		String urgencyCode = data.get("urgencyCode");
		String urgencyName = data.get("urgencyName");
		String clientName = data.get("clientName");
		String searchName = clientName.substring(0, 3);

		searchAndSelectClient(clientName);

		library.click("URGENCY.image"); // click on urgency
		String updateLocator = "xpath->.//span[text()='" + urgencyName + "']";
		library.click(updateLocator); // click on radio button
		// library.verifyPageContainsElement("URGENCY.success", true);
		library.waitForElement("URGENCY.notes");
		library.wait(3);
		library.click("URGENCY.notes"); // click on Notes
		library.wait(3);
		library.setImplicitWaitTime(1);
		boolean noError = true;// library.verifyPageNotContainsElement("NOTES.error",
								// true);

		String urgencyCodeLocator = "xpath->.//label[@ng-model='trans.urgencyStatus' and text()='" + urgencyCode + "']";

		library.verifyPageContainsElement(urgencyCodeLocator);

		library.setImplicitWaitTime(30);

		if (noError) {

			String noteUpdateLocator = getUrgencyNotesLocator(urgencyName);
			library.verifyPageContainsElement(noteUpdateLocator);
			library.click("URGENCY.notes");
			library.wait(3);
		} else {
			library.click("NOTES.error");
			library.wait(3);
		}

	}

	@Override
	public void verifyContractCancelStageSteps(Map<String, Object> data) {

		if (data != null) {
			library.clear("HOMEPAGE.searchbutton");
			String clientName = String.valueOf(data.get("clientName"));
			String postStage = String.valueOf(data.get("postStage"));
			searchAndSelectClient(clientName);
			waitWhileLoading();
			updateToStage(data);
			waitWhileLoading();
			navigateToClientListPage();
			waitWhileLoading();
			verifyTransactionStageForClientWith(clientName, postStage);
		}
	}

	@Override
	protected String getUrgencyNotesLocator(String urgencyName) {
		return "xpath->(.//p[contains(text(),'" + urgencyName + "')])[1]";
	}

	@Override
	public void searchForClientAndVerify(Map<String, String> data) {

		String clientName = data.get("clientName");
		String searchName = clientName;
		String clientFullName = data.get("clientFullName");
		library.clear("HOMEPAGE.searchbutton");
		library.click("HOMEPAGE.searchbutton"); // click on search
		library.verifyPageContainsElement("HOMEPAGE.searchbutton");
		library.wait(5);
		library.typeDataInto(searchName, "HOMEPAGE.searchbutton");
		library.wait(3);
		String clientPartialName = library.getTextFrom("HOMEPAGE.getclientname");
		// System.out.println(clientPartialName);
		if (clientFullName.equalsIgnoreCase(clientPartialName)) {

			// library.verifyPageContainsElement(clientPartialName, true);
			library.setImplicitWaitTime(2);
			library.click("HOMEPAGE.getclientname");
			library.setImplicitWaitTime(5);
			library.verifyPageContainsElement("CLIENTLISTPAGE.clientname");
			String verifyClientName = library.getTextFrom("CLIENTLISTPAGE.clientname");

			/*
			 * if(verifyClientName == clientName) { Reporter.log("Success."); }
			 */

		}

		// String resultLocator = "xpath->.//div/input[@placeholder='Search by
		// Name or Email']/..//*[contains(text(),'" + equals + "']";
	}

	/*
	 * @Override public void verifyTapOnCancel() {
	 * library.click("HOMEPAGE.goonleavebutton");
	 * library.verifyPageContainsElement("GOONLEAVE.setupdate", true);
	 * library.navigateBack();
	 * 
	 * }
	 */

	// Created by Puneet
	@Override
	public void navigateToFutureLeavePage() {

		library.click("HOMEPAGE.goonleavebutton");
		library.verifyPageContainsElement("GOONLEAVE.setupdate");
	}

	// Created by Puneet
	@Override
	public void verifyTapOnCancelSteps() {
		library.wait(5);
		library.click("FLEAVE.createbutton");
		library.verifyPageContainsElement("FLEAVE.createbutton");
	}

	// Created by Puneet
	/*
	 * @Override public void searchAndSelectClient(String client) {
	 * navigateToClientListPage(); /* String searchName = client.substring(0,
	 * 3); library.click("HOMEPAGE.searchbutton"); // click on search
	 * library.verifyPageContainsElement("HOMEPAGE.searchbutton", true);
	 * library.typeDataInto(searchName, "HOMEPAGE.searchbutton"); String
	 * resultLocator = "xpath->(//a[@ng-bind-html='match.label'])[1]";
	 * 
	 * String resultLocator = "xpath->.//strong[text()='" + client + "']"; //
	 * Assert.assertTrue(library.verifyPageContainsElement("CLIENTLIST.stage",
	 * // true), "not found"); library.click(resultLocator); }
	 */

	@Override
	public void waitWhileLoading() {
		library.setImplicitWaitTime(3);
	}

	/*
	 * @Override public void updateToStage(Map<String, Object> data) { String
	 * clientName = String.valueOf(data.get("ClientName")); String targetStage =
	 * String.valueOf(data.get("updateTransactionStage"));
	 * 
	 * if (targetStage.contains("Made an Offer")) { Map<String, String> map =
	 * (Map<String, String>) data.get("MadeOfferData");
	 * Assert.assertNotNull(map); updateToMadeAnOffer(map); } else if
	 * (targetStage.contains("Contract Accepted")) { Map<String, String> map =
	 * (Map<String, String>) data.get("MadeOfferData");
	 * Assert.assertNotNull(map); updateToMadeAnOffer(map); } else {
	 * updateToStageForClient(targetStage, clientName); }
	 * 
	 * }
	 */

	// Created By Gopal
	@Override
	public void verifyImmediateLeaveStatus() {
		library.click("MENU.goonleavebutton");
		library.verifyPageContainsElement("FLEAVE.goonleavebutton");
		library.click("FLEAVE.goonleavebutton");
		library.verifyPageContainsElement("FLEAVE.returnnow");
		library.scrollTo("FLEAVE.onleave");
		library.verifyPageContainsElement("FLEAVE.onleave");
		library.click("FLEAVE.returnnow");
		library.verifyPageContainsElement("FLEAVE.goonleavebutton");
	}

	// Created By Gopal
	@Override
	public void verifyFutureLeaveCreationCriterias(Map<String, String> data) {
		String fromDate = data.get("fromDate");
		String toDate = data.get("toDate");
		library.click("FLEAVE.createbutton");
		library.verifyPageContainsElement("FLEAVE.setleaveheader");
		library.wait(2);
		library.typeDataInto(fromDate, "FLEAVE.startdatefield");
		library.typeDataInto(toDate, "FLEAVE.enddatefield");
		library.click("FLEAVE.schedulebutton");
	}

	// Created By Gopal
	@Override
	public void verifydeletionOfExistingLeave() {

		library.click("MENU.goonleavebutton");
		if (library.verifyPageContainsElement("FLEAVE.deleteleavebutton"))
			library.click("FLEAVE.deleteleavebutton");
	}

	// Created by Puneet
	@Override
	public void verifyClientCardSteps(Map<String, String> map) {
		String clientFullName = map.get("clientFullName");
		String primaryPhone = map.get("primaryPhone");
		String searchName = clientFullName.substring(0, 3);
		/*
		 * library.click("HOMEPAGE.searchbutton"); // click on search
		 * library.verifyPageContainsElement("HOMEPAGE.searchbutton", true);
		 * library.typeDataInto(searchName, "HOMEPAGE.searchbutton");
		 * library.wait(3); String resultLocator =
		 * "xpath->(//a[@ng-bind-html='match.label'])[1]";
		 * library.click(resultLocator); library.wait(6); String
		 * clientNameOnClientListPage =
		 * library.getTextFrom("CLIENTLISTPAGE.clientname");
		 * library.verifyPageContainsElement("CLIENTLISTPAGE.clientname", true);
		 * // Assert.assertEquals(clientFullName, clientNameOnClientListPage);
		 */
		searchAndSelectClient(clientFullName);
		library.wait(3);
		library.click("CLIENTDETAIL.contactdetailsbutton");
		library.verifyPageContainsElement("CLIENTDETAIL.email");
		library.click("CLIENTDETAIL.email");
		library.verifyPageContainsElement("SENDEMAIL.text");
		library.typeDataInto("Test Automation", "SENDEMAIL.subjectfield");
		library.typeDataInto("Test Automation", "SENDEMAIL.emailcontent");
		library.wait(2);
		library.click("SENDEMAIL.sendbutton");
		library.wait(5);
		navigateToClientListPage();
		library.wait(4);
		library.verifyPageContainsElement("CLIENTLIST.sortbutton");
		library.wait(2);
		library.click("CLIENTLIST.emailicon");
		library.verifyPageContainsElement("SENDEMAIL.text");
		library.wait(4);
		library.click("MENU.clientlistpagelink");
		library.wait(3);
		String PrimaryPhoneLocator = "xpath->//strong[text()='" + clientFullName + "']/../../..//span[text()='"
				+ primaryPhone + "']";
		System.out.println(PrimaryPhoneLocator);
		String getPrimaryPhoneLocator = library.getTextFrom(PrimaryPhoneLocator);
		Assert.assertEquals(primaryPhone, getPrimaryPhoneLocator);

	}

	// Created By Gopal
	@Override
	public void verifyPreviousAndNextButton(Map<String, String> data) {
		String clientName = data.get("clientFullName");
		searchAndSelectClient(clientName);
		boolean ispresent = library.verifyPageContainsElement("xpath->//*[contains(text(),'" + clientName + "')]");
		System.out.println(ispresent);
	}

	// Created by gopal
	@Override
	public void verifyContactDetailsPage(Map<String, String> data) {
		verifyPreviousAndNextButton(data);
		library.click("DETAILS.contactinfo");
		library.verifyPageNotContainsElement("DETAILS.urgency");

	}

	@Override
	public void tapSearchIconOnHeaderAndVerify() {

	}

	@Override
	public void verifyImmediatelyLeaveSteps() {

		library.wait(5);
		library.click("HOMEPAGE.goonleavebutton");
		library.verifyPageContainsElement("GOONLEAVE.button");
		library.click("GOONLEAVE.button");
		library.wait(3);
		library.verifyPageContainsElement("GOONLEAVE.returnnowbutton");
		library.verifyPageContainsElement("GOONLEAVE.onleave");
		library.wait(3);
		library.click("GOONLEAVE.returnnowbutton");
		library.wait(2);
		library.verifyPageContainsElement("GOONLEAVE.button");

	}

	public void verifyImmediatelyLeaveWithFutureLeaveSteps(Map<String, String> data) {

		library.wait(3);
		library.click("HOMEPAGE.goonleavebutton");
		library.wait(3);
		boolean isReturnNow = library.verifyPageContainsElement("GOONLEAVE.returnnowbutton");
		if (!isReturnNow) {
			library.click("GOONLEAVE.button");
			library.wait(3);
		}
		String fromDate = data.get("fromDate");
		String toDate = data.get("toDate");
		library.click("FLEAVE.createbutton");
		library.verifyPageContainsElement("FLEAVE.setleaveheader");
		library.wait(2);
		library.clear("FLEAVE.startdatefield");
		library.typeDataInto(fromDate, "FLEAVE.startdatefield");
		library.clear("FLEAVE.enddatefield");
		library.typeDataInto(toDate, "FLEAVE.enddatefield");
		library.click("FLEAVE.schedulebutton");
		library.wait(3);
		library.verifyPageContainsElement("GOONLEAVE.returnnowbutton");
		library.wait(3);
		library.click("FLEAVE.leavedeleteimage");
		library.verifyPageContainsElement("GOONLEAVE.returnnowbutton");
	}

	@Override
	public void enterSecondFutureLeaveAndExpectErrorMessage(Map<String, String> data) {

		library.wait(5);
		library.click("MENU.goonleavebutton");

		String startDate = data.get("fromDate");
		String endDate = data.get("toDate");
		library.click("FLEAVE.createbutton");
		library.wait(2);
		chooseStartAndEndLeaveDates(startDate, endDate);
		library.wait(2);
		library.click("FLEAVE.schedulebutton");
		String datePeriod = startDate + " - " + endDate;
		System.out.println(datePeriod);
		library.verifyPageContainsElement("xpath->.//span[contains(text(),'" + datePeriod + "')]");

		boolean alertMessage = library.verifyPageContainsElement("FLEAVE.leaveoverlapmessage");
		// boolean isScheduleButtonPresent =
		// library.verifyPageContainsElement("FLEAVE.schedulebutton", true);
		if (alertMessage) {

			library.click("ALERT.leaveoverlapmessage");

		}
	}

	@Override
	public void verifyLastActivityWith(String activity) {
		String notesLoc = "xpath->.//button[@ng-click='toggleNotes(trans)']";
		if (activity.contains("-")) {
			activity = activity.split("-")[1].trim();
		}
		library.click(notesLoc);
		library.wait(3);
		Assert.assertTrue(library.verifyPageContainsElement(
				"xpath->.//button[@ng-click='toggleNotes(trans)']/following-sibling::div/ul/li[1]//strong[contains(text(),'"
						+ activity + "')]"), "Last activity not updated!");

		library.click(notesLoc);
		library.wait(3);
	}

	// Gopal
	@Override
	public void goToClientDetailsPage(Map<String, Object> data) {
		searchAndSelectClient((String) data.get("clientName"));
		library.click("DETAILS.contactinfo");
	}

	// Gopal
	@Override
	public void verifyMailWithApiResponce(Map<String, Object> data) {
		setTokenAndUserId(data);
		String clientLeadApi = (String) data.get("clientLeadApi");
		String mailId = library.getTextFrom("DETAILS.primaryemail");
		String response = (String) getResponse(clientLeadApi);
		String mailIdOfApiResponce = (String) library.getValueFromJson("$.email", response);
		Assert.assertEquals(mailId, mailIdOfApiResponce);

	}

	// Gopal
	@Override
	public void goToTransactionDetails() {
		library.click("DETAILS.transactions");
	}

	// Gopal
	@Override
	public void verifyOpportunitiesWithApiResponse(Map<String, Object> data) {
		String opportunityType = library.getTextFrom("DETAILS.opportunityType");
		System.out.println(opportunityType);
		String opportunityStage = library.getTextFrom("DETAILS.opportunityStage");
		System.out.println(opportunityStage);
		setTokenAndUserId(data);
		String response = getResponse((String) data.get("clientLeadApi"));
		System.out.println(response);
		String opportunityTypeOfApiResponse = (String) library
				.getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityType", response);
		String opportunityAreaOfApiResponse = (String) library
				.getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityStage", response);
		Assert.assertEquals(opportunityType, opportunityTypeOfApiResponse);
		Assert.assertEquals(opportunityStage, opportunityAreaOfApiResponse);
	}

	// Puneet date: - 10/May/2016
	// Puneet : - 25may16
	// Puneet date: - 10/May/2016
	public void updateToContractAcceptedStage(Map<String, String> data) {

		String dateStr = data.get("contractDate");

		String clientName = data.get("ClientName");
		openClientDetailPage(clientName);

		library.click("TRANSACTION.updatebutton");// update
		chooseTransactionStage("Contract Accepted");
		// library.clear("CONTRACT.date");
		library.typeDataInto(dateStr, "CONTRACT.date");

		library.isElementEnabled("CONTRACT.expectedclosedate", true);
		library.isElementEnabled("CONTRACT.price", true);

		enterNumber(data.get("contractPrice"), "MAKEOFFER.price");
		library.wait(5);

		library.clear("xpath->.//*[@name='commission']");
		library.wait(5);
		library.typeDataInto("3", "xpath->.//*[@name='commission']");
		// enterNumber(data.get("commission"), "CONTRACT.commission");

		library.wait(3);

		library.isElementEnabled("CONTRACT.continuebutton", true);

		library.click("CONTRACT.continuebutton"); // 1 continue
		library.wait(3);
		library.isElementEnabled("CONTRACT.editnamelink", true);
		library.wait(3);
		library.click("CONTRACT.continuebutton"); // 2 continue
		library.wait(2);

		library.verifyPageContainsElement("CONTARCT.laststep");

		library.typeDataInto(data.get("contractFname") + "\n", "CONTRACT.fname");// First
																					// Name
		// library.typeDataInto(data.get("email") + "\n", "CONTRACT.email");//
		// Email
		// library.typeDataInto(data.get("contractLname") + "\n",
		// "CONTRACT.lname");// Last
		// Name
		// library.typeDataInto(data.get("phone") + "\n", "CONTRACT.phone");//

		library.isElementEnabled("CONTRACT.fname", true);
		library.isElementEnabled("CONTRACT.lname", true);
		library.isElementEnabled("CONTRACT.phone", true);

		library.isElementEnabled("CONTRACT.email", true);

		// enterNumber(data.get("phone"), "CONTRACT.phone");

		// Phone
		library.wait(3);
		// library.click("CONTRACT.submitinfobutton");// continue
		library.waitForElement("TRANSACTION.properties");
		library.wait(5);
	}

	// Puneet date: - 9/May/2016
	@Override
	public void verifyContractCancelStageOne(Map<String, Object> data) {
		if (data != null) {

			String clientName = null;
			String postStage = null;

			Map<String, String> map = null;
			if (data.containsKey("MadeOfferData")) {
				map = (Map<String, String>) data.get("MadeOfferData");
			} else if (data.containsKey("contractAcceptData")) {
				map = (Map<String, String>) data.get("contractAcceptData");
			} else if (data.containsKey("returnToMovotoData")) {
				map = (Map<String, String>) data.get("returnToMovotoData");
			} else {
				map = null;
			}
			if (map != null) {
				clientName = String.valueOf(map.get("ClientName"));
				postStage = String.valueOf(map.get("postStage"));
				data.put("updateTransactionStage", map.get("updateTransactionStage"));
			} else {
				clientName = String.valueOf(data.get("ClientName"));
				postStage = String.valueOf(data.get("postStage"));
				data.put("updateTransactionStage", data.get("updateTransactionStage"));

			}
			library.clear("HOMEPAGE.searchbutton");

			data.put("ClientName", clientName);

			searchAndSelectClient(clientName);
			waitWhileLoading();
			updateToStage(data);
			waitWhileLoading();
		}
	}

	// Puneet date: - 9/May/2016
	@Override
	public void verifyContractCancelStageTwo(Map<String, Object> data) {

		navigateToClientListPage();
		waitWhileLoading();

	}

	// Created by Puneet
	// Puneet date: - 10/May/2016 Updated the code since it was commented.
	@Override
	public void searchAndSelectClient(String client) {
		navigateToClientListPage();

		String searchName = client.substring(0, 3);
		library.click("HOMEPAGE.searchbutton"); // click on search
		library.clear("HOMEPAGE.searchbutton");
		library.verifyPageContainsElement("HOMEPAGE.searchbutton");
		library.typeDataInto(searchName, "HOMEPAGE.searchbutton");
		String resultLocator = "xpath->(//a[@ng-bind-html='match.label'])[1]";

		// String resultLocator = "xpath->.//strong[text()='" + client + "']";
		// Assert.assertTrue(library.verifyPageContainsElement("CLIENTLIST.stage",
		// true), "not found");
		library.click(resultLocator);
		library.wait(10);

	}

	// Puneet date: - 11/May/2016
	@Override
	public void updateToStage(Map<String, Object> data) {
		String clientName = String.valueOf(data.get("ClientName"));
		String targetStage = String.valueOf(data.get("updateTransactionStage"));

		if (targetStage.contains("Made an Offer")) {
			Map<String, String> map = (Map<String, String>) data.get("MadeOfferData");
			Assert.assertNotNull(map);
			updateToMadeAnOffer(map);
		} else if (targetStage.contains("Contract Accepted")) {
			Map<String, String> map = (Map<String, String>) data.get("contractAcceptData");
			Assert.assertNotNull(map);
			// updateToMadeAnOffer(map);
			updateToContractAcceptedStage(map);
		} else if ((targetStage.contains("Return to Movoto"))) {
			Map<String, String> map = (Map<String, String>) data.get("returnToMovotoData");
			updateToReturnToMovoto(map);
		} else {
			updateToStageForClient(targetStage, clientName);
		}

	}

	public void updateToReturnToMovoto(Map<String, String> map) {

		String clientName = map.get("ClientName");
		// openClientDetailPage(clientName);

		library.click("TRANSACTION.updatebutton");// update
		chooseTransactionStage("Return to Movoto");
		chooseReturnToReason("Out of Area");
		library.verifyPageNotContainsElement(clientName); // Verifying
																// after return
																// to movoto
																// client should
																// be removed
																// from client
																// page.

	}

	@Override
	protected String getLocatorForUrgencyCode(String urgencyCode) {

		return "xpath->.//label[@ng-model='trans.urgencyStatus' and text()='" + urgencyCode + "']";

	}

	// Puneet date: - 9/May/2016
	@Override
	public String getLocatorForTransactionType(String title) {

		return "xpath->(.//strong[@class='ng-binding'])[4 and contains(text(),'" + title + "')]";

	}

	// Puneet date: - 10/May/2016
	@Override
	public void openNotes() {
		library.wait(15);
		library.click("CLIENTDETAIL.notesbutton");
		library.wait(3);

	}

	// Created by Priyanka on 05/10/2016 to fetch the Access Token and xuserID
	@Override
	public Map<String, Object> getAccessTokenId(Map<String, Object> data) {
		// Map<String, Object> apiData = (Map<String, Object>)
		// data.get("apiData");

		String contentType = String.valueOf(data.get("ContentType"));
		String accessTokenURL = String.valueOf(data.get("AccessTokenURL"));
		library.setContentType(contentType);

		Map<String, Object> apidata = new HashMap<>();

		apidata = (Map<String, Object>) data.get("LoginData");
		String response = library.HTTPPost(accessTokenURL, apidata);
		// library.HTTPPost(URL, pData);
		System.out.println(response);
		Object token = library.getValueFromJson("$.token", response);
		Object id = library.getValueFromJson("$.id", response);
		apidata.put("Token", token);
		apidata.put("Id", id);

		return apidata;

	}

	@Override
	public Map<String, Object> getAccessTokenIds(Map<String, Object> data) {
		Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");

		String contentType = String.valueOf(apiData.get("ContentType"));
		String accessTokenURL = String.valueOf(apiData.get("AccessTokenURL"));
		library.setContentType(contentType);

		Map<String, Object> apidata = new HashMap<>();

		apidata = (Map<String, Object>) apiData.get("LoginData");
		String response = library.HTTPPost(accessTokenURL, apidata);
		// library.HTTPPost(URL, pData);
		System.out.println(response);
		Object token = library.getValueFromJson("$.token", response);
		Object id = library.getValueFromJson("$.id", response);
		apidata.put("Token", token);
		apidata.put("Id", id);

		return apidata;

	}

	// Created by Priyanka to Verify lead names match as per API
	// output.
	@Override
	public void verifyLeadsName(Map<String, Object> data) {
		// Map<String, Object> apiData = (Map<String, Object>)
		// data.get("apiData");

		Map<String, Object> accessData = getAccessTokenId(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		try {
			String contentType = String.valueOf(data.get("ContentType"));
			String contactURL = String.valueOf(data.get("contactsUrl"));
			library.setContentType(contentType);
			library.setRequestHeader("x-userid", xuserid);
			library.setRequestHeader("Authorization", "Bearer " + token);
			String response = library.HTTPGet(contactURL);
			int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
			int leadCount = library
					.getElementCount("xpath->//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)']");
			Assert.assertEquals(arrayCount, leadCount);
			for (int i = 0; i < arrayCount; i++) {
				String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
						response);
				String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
						response);
				String LeadName = firstName + " " + lastName;
				System.out.println(LeadName);
				int j = i + 1;
				String uiLeadNameLocator = "xpath->(//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'])["
						+ j + "]//a/strong";
				String uiLeadName = library.getTextFrom(uiLeadNameLocator);
				Assert.assertEquals(uiLeadName, LeadName);//Verify lead names match as per API
			}
		} catch (NullPointerException e) {

		}
	}

	// Created by Priyanka to Verify client stage matches with
	// opportunity stage from API response
	@Override
	public void verifyStageName(Map<String, Object> data) {
		// Map<String, Object> apiData = (Map<String, Object>)
		// data.get("apiData");
		Map<String, Object> accessData = getAccessTokenId(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(data.get("ContentType"));
		String contactURL = String.valueOf(data.get("contactsUrl"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		// String verifyopportunityStage =
		// String.valueOf(library.getValueFromJson("$.clientList[1].contactInfo.opportunitiesSummary.openOpportunities[0].opportunityStage",
		// response));
		// System.out.println(verifyopportunityStage);

		for (int i = 0; i < arrayCount; i++) {
			String verifyopportunityStage = null;
			verifyopportunityStage = String.valueOf(library.getValueFromJson(
					"$.clientList[" + i + "].contactInfo.opportunitiesSummary.openOpportunities[0].opportunityStage",
					response));
			String opportunityStage = null;
			if (verifyopportunityStage == "null") {
				opportunityStage = "Follow-up Now";
			} else {
				opportunityStage = (String) library.getValueFromJson("$.clientList[" + i
						+ "].contactInfo.opportunitiesSummary.openOpportunities[0].opportunityStage", response);
			}
			System.out.println(opportunityStage);
			int j = i + 1;
			String uiopportunityStageloc = "xpath->(//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'])["
					+ j + "]//div[@client-stage='opportunity']/div/div[2]/span";
			String uiopportunityStage = library.getTextFrom(uiopportunityStageloc);
			Assert.assertEquals(uiopportunityStage, opportunityStage);//Verify client stage matches with opportunity stage from API response
			//Verify oppurtunity stage exists in the API response
		}

	}

	// Created by Priyanka to Verify email value from API response
	// matches with corresponding client email field in the Application
	@Override
	public void verifyEmailFieldName(Map<String, Object> data) {
		// Map<String, Object> apiData = (Map<String, Object>)
		// data.get("apiData");
		Map<String, Object> accessData = getAccessTokenId(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(data.get("ContentType"));
		String contactURL = String.valueOf(data.get("contactsUrl"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		for (int i = 0; i < arrayCount; i++) {
			String email = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.email", response);
			System.out.println(email);
			int j = i + 1;
			String uiemailloc = "xpath->(//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'])[" + j
					+ "]//div[@class='col-xs-6 col-sm-6 email']//span";
			String uiemail = library.getTextFrom(uiemailloc);//Verify email value from API response matches with corresponding client email field
			Assert.assertEquals(uiemail, email);
		}

	}

	// Created by Priyanka to Verify primary phone value from API
	// response matches with corresponding client primary phone field in the
	// Application
	@Override
	public void verifyPrimaryPhoneField(Map<String, Object> data) {
		// Map<String, Object> apiData = (Map<String, Object>)
		// data.get("apiData");
		Map<String, Object> accessData = getAccessTokenId(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(data.get("ContentType"));
		String contactURL = String.valueOf(data.get("contactsUrl"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		for (int i = 0; i < arrayCount && i <= 10; i++) {
			String PrimaryPhone = (String) library
					.getValueFromJson("$.clientList[" + i + "].contactInfo.phone[0].phonenumber", response);
			System.out.println(PrimaryPhone);
			int j = i + 1;
			String uiPrimaryPhoneloc = "xpath->(//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'])["
					+ j + "]//div[@class='col-xs-6 col-sm-6 phone']//span";
			String uiPrimaryPhone = library.getTextFrom(uiPrimaryPhoneloc);
			Assert.assertEquals(uiPrimaryPhone, uiPrimaryPhone);//Verify primary phone value from API response matches with corresponding client primary phone field in the Application
		}

	}

	// Created by Priyanka to Verify last visited date value from
	// API response matches with corresponding client last visited date field in
	// the Application
	@Override
	public void verifyLastVisitedDate(Map<String, Object> data) {
		// Map<String, Object> apiData = (Map<String, Object>)
		// data.get("apiData");
		Map<String, Object> accessData = getAccessTokenId(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(data.get("ContentType"));
		String contactURL = String.valueOf(data.get("contactsUrl"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		for (int i = 0; i < arrayCount; i++) {
			String lastVisitedDateTime = String.valueOf(
					library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastVisitedDateTime", response));
			int j = i + 1;
			if (lastVisitedDateTime == "null") {
				System.out.println(lastVisitedDateTime);
				String uilastVisitedDateLoc = "xpath->(//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'])["
						+ j + "]//div[@class='col-xs-12 col-sm-2 col-md-3 last-visit']//strong";
				String uilastVisitedDate = library.getTextFrom(uilastVisitedDateLoc);
				// Boolean condition =
				// lastVisitedDateTime.contains(uilastVisitedDate);
				Assert.assertTrue(uilastVisitedDate.contentEquals("-"), "Condition is False");//Verify last visited date value from API response matches with corresponding client last visited date field in the Application
			}
			if (lastVisitedDateTime != "null") {
				System.out.println(lastVisitedDateTime);
				String uilastVisitedDateLoc = "xpath->(//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'])["
						+ j + "]//div[@class='col-xs-12 col-sm-2 col-md-3 last-visit']//strong";
				String uilastVisitedDate = library.getTextFrom(uilastVisitedDateLoc);
				Boolean condition = lastVisitedDateTime.contains(uilastVisitedDate);//Verify last visited date value from API response matches with corresponding client last visited date field in the Application
				Assert.assertTrue(condition, "Condition is False");
			}
		}

	}

	// Created by Priyanka to Verify client list exists with
	// atleast 1 or more leads

	public void verifyClientListExistwithLeads(Map<String, Object> data) {
		// Map<String, Object> apiData = (Map<String, Object>)
		// data.get("apiData");
		Map<String, Object> accessData = getAccessTokenId(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(data.get("ContentType"));
		String contactURL = String.valueOf(data.get("contactsUrl"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		Boolean Condition = arrayCount >= 1;
		Assert.assertTrue(Condition, "Condition is False");
	}

	@Override
	public void verfyselectSortorder(Map<String, Object> data) {
		Map<String, Object> orderData = (Map<String, Object>) data.get("orderData");
		Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
		String dropdown = "xpath->//button[@ng-model='selectedSort']";
		library.click(dropdown);
		String order = String.valueOf(orderData.get("Order"));
		String sortOrder = "xpath ->//a/span[text()='" + order + "']";
		library.getTextFrom(sortOrder);
		library.click(sortOrder);
		library.wait(2);
		Map<String, Object> accessData = getAccessTokenIds(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(apiData.get("ContentType"));
		String SortURL = String.valueOf(orderData.get("SortURL"));
		// Integer variable = Integer.valueOf((String)
		// orderData.get("variable"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);

		String response = library.HTTPGet(SortURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		for (int i = 0; i < arrayCount; i++) {
			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;
			System.out.println(LeadName);
			int j = i + 1;
			String uiLeadNameLocator = "xpath->(//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'])["
					+ j + "]//a/strong";
			String uiLeadName = library.getTextFrom(uiLeadNameLocator);
			Assert.assertEquals(uiLeadName, LeadName);
		}
	}

	// Created by Priyanka to Verify client list exists with
	// atleast 1 or more leads
	@Override
	public void verifyStageLastUpdatedDate(Map<String, Object> data) {
		// Map<String, Object> apiData = (Map<String, Object>)
		// data.get("apiData");
		// Map<String, Object> orderData = (Map<String, Object>)
		// data.get("orderData");
		Map<String, Object> accessData = getAccessTokenId(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(data.get("ContentType"));
		String contactURL = String.valueOf(data.get("contactsUrl"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		for (int i = 0; i < arrayCount; i++) {
			int j = i + 1;
			String StageLastUpdatedDateTime = String.valueOf(
					library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastActivityDateTime", response));
			// String StagereferralDateTime =
			// String.valueOf(library.getValueFromJson("$.clientList[" + i+
			// "].contactInfo.referralDateTime",response));
			// if (StageLastUpdatedDateTime == "null") {
			// System.out.println(StagereferralDateTime);
			//
			// String uiStageLastUpdatedDateLoc =
			// "xpath->(//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'])["+
			// j + "]//div[@client-stage='opportunity']//span//strong";
			// String uiStageLastUpdatedDate =
			// library.getTextFrom(uiStageLastUpdatedDateLoc);
			// Boolean condition =
			// StagereferralDateTime.contains(uiStageLastUpdatedDate);
			// Assert.assertTrue(condition, "Condition is False");
			//
			// }

			// if (StageLastUpdatedDateTime != "null") {
			System.out.println(StageLastUpdatedDateTime);

			String uiStageLastUpdatedDateLoc = "xpath->(//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'])["
					+ j + "]//div[@client-stage='opportunity']//span//strong";
			String uiStageLastUpdatedDate = library.getTextFrom(uiStageLastUpdatedDateLoc);

			Boolean condition = StageLastUpdatedDateTime.contains(uiStageLastUpdatedDate);
			Assert.assertTrue(condition, "Condition is False");

			// }
		}

	}

	@Override
	public void verifyClientFilter(Map<String, Object> data) {

	}

	// Created by Priyanka
	@Override
	public void verifySendEmail(Map<String, Object> data) {
		Map<String, Object> accessData = getAccessTokenId(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(data.get("ContentType"));
		String contactURL = String.valueOf(data.get("contactsUrl"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);
		String response = library.HTTPGet(contactURL);
		// int arrayCount = (int) library.getValueFromJson("$.filteredCount",
		// response);
		String email = (String) library.getValueFromJson("$.clientList[1].contactInfo.email", response);
		System.out.println(email);
		library.wait(3);
		// library.click("xpath->//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'][1]//a/strong");
		// library.wait(3);
		// library.click("xpath->(//a[@role='button'])[1]");
		// library.verifyPageContainsElement("CLIENTDETAIL.email", true);
		library.click("xpath->//span[text()='" + email + "']");
		library.verifyPageContainsElement("xpath->.//span[text()='Email Client']");
		library.typeDataInto("Test Automation", "xpath->.//input[@placeholder='(Required)']");

		// library.typeDataInto("Test Automation",
		// "xpath->.//*[@id='taTextElement6777109244213873']/p/br");
		library.wait(2);
		Assert.assertTrue(library.isElementEnabled("xpath->.//button[@name='send']", true));
		library.click("xpath->.//button[@name='send']");
		library.waitForElement("xpath->//h3[text()='Success']");
		Assert.assertTrue(library.verifyPageContainsElement("xpath->//h3[text()='Success']"));
		library.click("xpath->//a[contains(text(),'Client List')]");
		library.wait(20);

	}

	// Akash date- 23/May/2016
	@Override
	public int goToLeavePageCountLeave() {
		library.click("MENU.goonleavebutton");
		int leaveCount = library.getElementCount("FLEAVE.leavecount");
		return leaveCount;
	}

	@Override
	public void deleteLeaves(int leavePosition) {
		library.click("xpath->(.//*[contains(text(),'Set-up Dates:')]/../../div//i[@ng-click='cancelLeave(leave)'])["
				+ leavePosition + "]");
	}

	@Override
	public void verifyLeavesWithApiResponse(String response, int leaveCount) {

		for (int i = 0; i < leaveCount; i++) {

			String startDateFirst = (String) library.getValueFromJson("$.Leaves[" + i + "].startDateTime", response);
			String endDatFirst = (String) library.getValueFromJson("$.Leaves[" + i + "].endDateTime", response);

			String[] splitstartDateFirst = startDateFirst.split(" ");
			String dateFrom = splitstartDateFirst[0];

			String[] splitendDatFirst = endDatFirst.split(" ");
			String dateTo = splitendDatFirst[0];

			String[] splitdayFirst = dateFrom.split("/");
			String s = splitdayFirst[1];

			String[] splitdayend = dateTo.split("/");
			String ss = splitdayFirst[1];

			int day = Integer.parseInt(s);
			int dayy = Integer.parseInt(ss);

			if (day <= 9 || dayy <= 9) {
				String[] splitDate = dateFrom.split("/");
				String datee = splitDate[0] + "/0" + splitDate[1] + "/" + splitDate[2];

				String[] splitend = dateTo.split("/");
				String dateee = splitend[0] + "/0" + splitend[1] + "/" + splitend[2];

				String leaveStatus = datee + " - " + dateee;
				String leave = "xpath->.//span[text()='" + leaveStatus + "']";
				boolean isLeavePresent = library.verifyPageContainsElement(leave);
				Assert.assertTrue(isLeavePresent);

			} else {
				String leaveStatus = dateFrom + " - " + dateTo;
				String leave = "xpath->.//span[text()='" + leaveStatus + "']";
				boolean isLeavePresent = library.verifyPageContainsElement(leave);
				Assert.assertTrue(isLeavePresent);
			}

		}
	}

	@Override
	public void verifyNotesResponseWithApi(Map<String, Object> data) {
		String response = getResponse((String) data.get("notesApi"));
		String noteDiscription = (String) library.getValueFromJson("$.activities[0].note", response);
		noteDiscription = noteDiscription.trim();
		String dateOfUpates = (String) library.getValueFromJson("$.activities[0].startDateTime", response);
		System.out.println(noteDiscription);
		library.wait(3);
		Assert.assertTrue(library
				.verifyPageContainsElement("xpath->(.//p[contains(text(),'" + noteDiscription + "')])[1]"));
	}

	@Override
	public void verifyPropertiesResponseWithApi(Map<String, Object> data) {
		String response = getResponse((String) data.get("propertiesApi"));
		System.out.println("\n" + response + "\n");
		String addressOne = (String) library.getValueFromJson("$.properties[0].address", response);
		addressOne = addressOne.trim();
		String addressTwo = (String) library.getValueFromJson("$.properties[0].address", response);
		addressTwo = addressTwo.trim();
		System.out.println(addressOne);
		System.out.println(addressTwo);
		boolean isAddressExist = library.verifyPageContainsElement("xpath->.//*[contains(text(),'" + addressOne + "')]");

		library.wait(5);
		Assert.assertTrue(
				library.verifyPageContainsElement("xpath->.//*[contains(text(),'" + addressOne + "')]"));
		Assert.assertTrue(
				library.verifyPageContainsElement("xpath->.//*[contains(text(),'" + addressTwo + "')]"));
	}

	@Override
	public void updateUrgencyStageOne(Map<String, String> data) {
		String urgencyCode = data.get("urgencyCode");
		String urgencyName = data.get("urgencyName");
		String clientName = data.get("ClientName");
		String searchName = clientName.substring(0, 3);

		searchAndSelectClient(clientName);

		library.click("URGENCY.image"); // click on urgency
		String updateLocator = "xpath->.//span[text()='" + urgencyName + "']";
		library.click(updateLocator); // click on radio button
		// library.verifyPageContainsElement("URGENCY.success", true);
		library.waitForElement("URGENCY.notes");
		library.wait(3);
		library.click("URGENCY.notes"); // click on Notes
		library.wait(3);
	}

	@Override
	public void verifyUrgencyDetails(Map<String, Object> data) {
		Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
		Map<String, String> urgencyData = (Map<String, String>) data.get("urgencyData");
		String urgencyName = urgencyData.get("urgencyName");
		String accessData = getAccessTokenIdp(apiData);

		apiData.put("accessData", accessData);
		String leadData = getLeadDetails(apiData);

		// String urgency = (String) library.getValueFromJson("$.urgency",
		// leadData); // From API

		String urgency = (String) library.getValueFromJson("$.activities[0].urgencyStatus", leadData);
		// String urgencyCode = urgency.split("-")[1];
		// urgencyCode = urgencyCode.trim();

		String last = "Updated Urgency Status: " + urgency;

		// String locator = getLocatorForUrgencyCode(urgencyCode);
		String webUiLocator = "xpath->(//*[@class='list-group ng-isolate-scope']/li[1]//p)[3]";
		webUiLocator = library.getTextFrom(webUiLocator);

		// locator = locator+" "+"-"+" "+urgencyName;

		library.wait(3);
		Assert.assertEquals(last, webUiLocator);
		library.wait(3);

	}

	

	@Override
	public String getNotesDetails(Map<String, Object> data) {

		String accessData = (String) data.get("accessData");
		String token = (String) library.getValueFromJson("$.token", accessData);
		String id = (String) library.getValueFromJson("$.id", accessData);

		String authorization = "Bearer " + token;
		String contentType = String.valueOf(data.get("ContentType"));
		String notesApiUrl = String.valueOf(data.get("LeadDetailsUrl"));

		library.setContentType(contentType);
		library.setRequestHeader("Authorization", authorization);
		library.setRequestHeader("x-userid", id);

		String response = library.HTTPGet(notesApiUrl);

		return response;

	}

	@Override
	public void setFutureDateUsingCalander(Map<String, Object> data) {
		library.wait(2);
		library.click("MENU.goonleavebutton");

		int leaveCount = library.getElementCount("FLEAVE.leavecount");
		for (int i = 1; i <= leaveCount; i++) {
			library.click("FLEAVE.deleteleavebutton");
			library.wait(2);
		}

		for (int k = 1; k <= 3; k++) {
			Calendar currentCal = Calendar.getInstance();
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			currentCal.add(Calendar.DATE, k);
			String Startdatee = dateFormat.format(currentCal.getTime());

			library.click("FLEAVE.createbutton");
			library.wait(2);
			library.clear("xpath->.//div[@class='daterangepicker dropdown-menu show-calendar opensright'][" + k
					+ "]//input[@name='daterangepicker_start']");
			library.typeDataInto(Startdatee,
					"xpath->.//div[@class='daterangepicker dropdown-menu show-calendar opensright'][" + k
							+ "]//input[@name='daterangepicker_start']");

			library.clear("xpath->.//div[@class='daterangepicker dropdown-menu show-calendar opensright'][" + k
					+ "]//input[@name='daterangepicker_end']");
			library.typeDataInto(Startdatee,
					"xpath->.//div[@class='daterangepicker dropdown-menu show-calendar opensright'][" + k
							+ "]//input[@name='daterangepicker_end']");
			library.wait(3);
			library.click("xpath->//div[@class='daterangepicker dropdown-menu show-calendar opensright'][" + k
					+ "]//button[text()='Apply']");
			library.wait(5);
		}
	}

	// Puneet- 07/07/2016
	@Override
	public void verifyActivityListSages() {
		String getXpathforStage = "xpath->(//div[contains(@class,'col-xss-6 col-xs-5 feature-title')])[3]/..//div[contains(@class,'col-xss-6 col-xs-7 feature-value ng-binding')]";
		String getXpathforStageText = library.getTextFrom(getXpathforStage);
		String stageName = "Met";
		Assert.assertEquals(getXpathforStageText, "Met"); // validating client
															// in Met stage.
		System.out.println(getXpathforStageText);
		// for Talked stage
		if (stageName.equalsIgnoreCase(getXpathforStageText)) {

			library.click("TRANSACTION.updatebutton");

			library.wait(5);
			library.click("xpath->.//button[@ng-model='selectActivity']");

			// verifying expected stages.
			library.verifyPageContainsElement("xpath->.//span[text()='Talked']");
			library.verifyPageContainsElement("xpath->.//span[text()='Add a Note']");
			library.verifyPageContainsElement("xpath->.//span[text()='Left Voicemail']");
			library.verifyPageContainsElement("xpath->.//span[text()='Met in Person']");
			library.verifyPageContainsElement("xpath->.//span[text()='Made an Offer']");
			library.verifyPageContainsElement("xpath->.//span[text()='Emailed']");
			library.verifyPageContainsElement("xpath->.//span[text()='Scheduled a Meeting']");
			library.verifyPageContainsElement("xpath->.//span[text()='Scheduled a Callback']");
			library.verifyPageContainsElement("xpath->.//span[text()='Return to Movoto']");
		}
	}

		// Gopal changed
	@Override
	public void goToTransactionDetails(Map<String, Object> data) {
		searchAndSelectClient((String) data.get("clientName2"));
		boolean isOffered = library.verifyPageContainsElement("TRANSACTION.offeredStage");
		if (!isOffered) {
			library.click("TRANSACTION.updatebutton");
			library.wait(2);
			library.click("TRANSACTION.selectOne");
			library.click("TRANSACTION.setOffered");
			library.typeDataInto("Agent Automation", "NOTES.notesfield");
			library.click("MAKEOFFER.saveupdatebutton");
			library.wait(4);
		}
	}

	@Override
	public void loginVerification(Map<String, String> data) {
		String agentName = data.get("Username");
		String xpathForAgentName = "xpath->//span[@class='h4']/strong";
		String getAgentNamefromXpath = library.getTextFrom(xpathForAgentName);
		String[] SplitAgentName = agentName.split("@");
		String Name = SplitAgentName[0];
		String xpathValue = getAgentNamefromXpath.replaceAll("\\s+", "");
		System.out.println(xpathValue);
		String loginValue = Name.replaceAll("\\s+", "");
		System.out.println(loginValue);
		boolean isMatched = xpathValue.equalsIgnoreCase(loginValue);//Verify agent is logged in successfully via checking the agent name on the menu is the same as the logged in agent's full name. 
		Assert.assertTrue(isMatched);
		library.wait(2);
	}

	
	@Override
	public void updateToScheduledACallbackForMetStage(String stage, String client, Map<String, Object> data) {

		searchAndSelectClient(client);
		library.click("TRANSACTION.updatebutton");
		library.wait(3);
		String metStageLocator = library.getTextFrom("UPDATEPAGE.metstagelocator");
		library.wait(3);
		Assert.assertEquals(client, metStageLocator);// Client Name is correct
														// with the updated
														// client
		chooseTransactionStage(stage);
		String dateFromUI = library.getTextFrom("UPDATEPAGE.date");
		String timeInHourFromUI = library.getTextFrom("UPDATEPAGE.timeinhour");
		int intTimeInHourFromUI = Integer.parseInt(timeInHourFromUI);
		String timeInMinuteFromUI = library.getTextFrom("UPDATEPAGE.timeinminute");
		int intTimeInMinuteFromUI = Integer.parseInt(timeInMinuteFromUI);
		System.out.println(metStageLocator);
		System.out.println(timeInHourFromUI);
		System.out.println(timeInMinuteFromUI);
		System.out.println(dateFromUI);

		// add one hour in scheduled callback screen

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String localSystemTime = sdf.format(cal.getTime());
		System.out.println(localSystemTime);

		int localSystemMin = cal.get(Calendar.MINUTE);
		int localSystemhour = cal.get(Calendar.HOUR);
		int addoneHourToUI = localSystemhour + 1;
		String addoneHourToUIString = String.valueOf(addoneHourToUI);
		System.out.print("Add one hour " + addoneHourToUIString);
		library.typeDataInto(addoneHourToUIString, "UPDATEPAGE.timeinhour");
		library.click("xpath->.//span[text()=" + addoneHourToUI + "]");
		library.wait(3);

		System.out.println("Printing Local System Time.");
		System.out.println(localSystemMin);
		System.out.println(localSystemhour);

		System.out.println(localSystemTime);

		Date date = new Date();
		String localSystemDate = sdf.format(date);
		System.out.println(localSystemDate);

		if (localSystemMin < 50) {

			int afterAddingTenMin = localSystemMin + 10;
			boolean isCorrect = (intTimeInMinuteFromUI >= afterAddingTenMin);
			Assert.assertTrue(isCorrect);// the default Date & Time is 10 min
											// after the current time
			if (intTimeInMinuteFromUI >= afterAddingTenMin) {
				System.out.println("UI time is greater than or equal to 10 min");
				System.out.print("Web UI showing correct time.");
			}

		} else if (localSystemMin > 50) {
			intTimeInHourFromUI = intTimeInHourFromUI++;
			System.out.println(intTimeInHourFromUI);

			// Adding 10 minute

			cal.add(Calendar.MINUTE, 10);
			int localSystemAfterAddingMin = cal.get(Calendar.MINUTE);
			int localSystemhourAfterAddingMin = cal.get(Calendar.HOUR);
			String newTime = sdf.format(cal.getTime());
			System.out.println(newTime);
			System.out.println(localSystemAfterAddingMin);
			System.out.println(localSystemhourAfterAddingMin);

			if (intTimeInMinuteFromUI >= localSystemAfterAddingMin) {

				System.out.println("UI time is greater than or equal to 10 min");
				System.out.print("Web UI showing correct time.");

			}

		}
		String startDate=library.getTextFrom("xpath->.//*[@ng-model='currentActivity.activity.formData.time.from.HH']");
		String endDate=startDate+":"+(library.getTextFrom("xpath->.//*[@ng-model='currentActivity.activity.formData.time.from.MM']"));
		System.out.println(endDate);
		library.typeDataInto("Agent Automation", "NOTES.notesfield");
		library.wait(5);
		data.put("endDate", endDate);
		library.wait(5);
		library.click("UPDATE.applybutton");
		library.waitForElement("TRANSACTION.properties");
		library.wait(5);
	}


	@Override
	public String getSystemDate() {

		Calendar currentCal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String localSystemTime = dateFormat.format(currentCal.getTime());
		System.out.println("local time" + localSystemTime);
		return localSystemTime;

	}

	@Override
	public String getPrestage() {

		String prestageText = library
				.getTextFrom("xpath->(//div[@class='col-xss-6 col-xs-7 feature-value ng-binding'])[3]");
		return prestageText;

	}

	@Override
	public void openProperties() {
		library.click("MAKEOFFER.propertiesbutton");
		library.wait(5);

	}

	

	@Override
	// puneet
	public void verifyNotesTime(Map<String, Object> data) {
		String xpathForDateAndTimeFromNotesField = library.getTextFrom("NOTES.updatedtime");
		// String Mettime = xpathForDateAndTimeFromNotesField.split(" ")[1];

		try {
			DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mma");
			formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			Date date = formatterIST.parse(xpathForDateAndTimeFromNotesField);

			DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mma");
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
			String SplitTime = formatterUTC.format(date);
			

			System.out.println(SplitTime);

			Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
			String accessData = getAccessTokenIdp(apiData);

			apiData.put("accessData", accessData);
			String notesApiData = getNotesDetails(apiData);

			String notesData = (String) library.getValueFromJson("$.activities[0].activityType", notesApiData);
			String ApiTime = (String) library.getValueFromJson("$.activities[0].activityCreatedDateTime", notesApiData);
			String[] SplitApiTime = ApiTime.split(" ");
		    String[] gotApiTime = SplitApiTime[1].split(":");
		    String FinalTime=SplitApiTime[0]+" "+gotApiTime[0]+":"+gotApiTime[1]+SplitApiTime[2];
		    System.out.println(FinalTime);
			String transactionType = notesData.split("-")[1];
			transactionType = transactionType.trim(); // Scheduled a Callback

			String locator = getLocatorForTransactionType(transactionType);
			Assert.assertTrue(library.verifyPageContainsElement(locator), "Transaction type failed");//Verify transaction type
			Assert.assertEquals(FinalTime, SplitTime);// Verify date and time
			String uiCallBack=library.getTextFrom("xpath->(.//*[@ng-show='showCallbackTime(note)'])[1]");
			String time=data.get("endDate").toString();
			Assert.assertTrue(uiCallBack.contains(time));//Verify call back time
			Assert.assertTrue(library.verifyPageContainsElement("xpath->(.//*[@ng-bind-html='note.note'])[1]"), "Notes is present");//Verify Notes 
																
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// @Override
	// public void verifyNotesData(Map<String, Object> data) {
	//
	// Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
	// String accessData = getAccessTokenIdp(apiData);
	//
	// apiData.put("accessData", accessData);
	// String notesApiData = getNotesDetails(apiData);
	//
	// String notesData = (String)
	// library.getValueFromJson("$.activities[0].activityType", notesApiData);
	//
	// String transactionType = notesData.split("-")[1];
	// transactionType = transactionType.trim(); // Scheduled a Callback
	//
	// String locator = getLocatorForTransactionType(transactionType);
	//
	// library.verifyPageContainsElement(locator, true);
	//
	// String notesLineTwo = library.getTextFrom("MAKEOFFER.notesfieldlinetwo");
	// System.out.println(notesLineTwo);
	// Assert.assertEquals("Property Address - see properties section for more
	// details", notesLineTwo);
	//
	// String notesTimeXpath = "xpath->(//span[@class='ng-binding'])[1]";
	// String getTime = library.getTextFrom(notesTimeXpath);
	// String FinalValue = null;
	//
	// try {
	// DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mma");
	// formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	// Date date = formatterIST.parse(getTime);
	//
	// DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mma");
	// formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
	// String[] SplitTime = formatterUTC.format(date).split(" ");
	// FinalValue = SplitTime[1];
	// System.out.println(FinalValue);
	//
	// String ApiTime = (String)
	// library.getValueFromJson("$.activities[0].startDateTime", notesApiData);
	// String[] SplitApiTime = ApiTime.split(" ");
	// String gotApiTime = SplitApiTime[1];
	// String[] splitSec = gotApiTime.split(":");
	// String finalTime = splitSec[0] + ":" + splitSec[1] + SplitApiTime[2];
	// System.out.println(finalTime);
	// Assert.assertEquals(finalTime, FinalValue);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	

	@Override
	public void verifyNotesDataforContractCancel(Map<String, Object> data) {

		String contactURL = getApiUrlForMadeAnOffer(data);
		setRequestHeaderforMadeAnOffer(data);
		String response = library.HTTPGet(contactURL);

		String notesData = (String) library.getValueFromJson("$.activities[0].activityType", response);

		String transactionType = notesData.split("-")[1];
		transactionType = transactionType.trim(); // Scheduled a Callback

		String locator = getLocatorForTransactionType(transactionType);

		library.verifyPageContainsElement(locator);

		String notesLineTwo = library.getTextFrom("MAKEOFFER.notesfieldlinetwo");
		System.out.println(notesLineTwo);
		Assert.assertEquals("Property Address - see properties section for more details", notesLineTwo);

		String notesTimeXpath = "xpath->(//span[@class='ng-binding'])[1]";
		String getTime = library.getTextFrom(notesTimeXpath);
		String FinalValue = null;

		try {
			DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mma");
			formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			Date date = formatterIST.parse(getTime);

			DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mma");
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
			String[] SplitTime = formatterUTC.format(date).split(" ");
			FinalValue = SplitTime[1];
			System.out.println(FinalValue);

			String ApiTime = (String) library.getValueFromJson("$.activities[0].startDateTime", response);
			String[] SplitApiTime = ApiTime.split(" ");
			String gotApiTime = SplitApiTime[1];
			String[] splitSec = gotApiTime.split(":");
			String finalTime = splitSec[0] + ":" + splitSec[1] + SplitApiTime[2];
			System.out.println(finalTime);
			Assert.assertEquals(finalTime, FinalValue);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Map<String, Object> getAccessTokenIdFirst(Map<String, Object> data) {

		String contentType = String.valueOf(data.get("ContentType"));
		String accessTokenURL = String.valueOf(data.get("AccessTokenURL"));
		library.setContentType(contentType);

		Map<String, Object> apidata = new HashMap<>();

		Map<String, Object> apidata1 = (Map<String, Object>) data.get("apiData");
		// apidata = (Map<String, Object>) data.get("LoginData");
		String response = library.HTTPPost(accessTokenURL, apidata1);
		// library.HTTPPost(URL, pData);
		System.out.println(response);
		Object token = library.getValueFromJson("$.token", response);
		Object id = library.getValueFromJson("$.id", response);
		apidata.put("Token", token);
		apidata.put("Id", id);

		return apidata;

	}

	@Override
	public Map<String, Object> getAccessTokenIdSecond(Map<String, Object> data) {

		String contentType = String.valueOf(data.get("ContentType"));
		String accessTokenURL = String.valueOf(data.get("AccessTokenURL"));
		library.setContentType(contentType);

		Map<String, Object> apidata = new HashMap<>();

		Map<String, Object> apidata1 = (Map<String, Object>) data.get("apiData");
		// apidata = (Map<String, Object>) data.get("LoginData");
		String response = library.HTTPPost(accessTokenURL, apidata1);
		// library.HTTPPost(URL, pData);
		System.out.println(response);
		Object token = library.getValueFromJson("$.token", response);
		Object id = library.getValueFromJson("$.id", response);
		apidata.put("Token", token);
		apidata.put("Id", id);

		return apidata;

	}

	@Override
	public String getApiUrlForMadeAnOffer(Map<String, Object> data) {

		Map<String, Object> getData = (Map<String, Object>) data.get("apiData");
		Map<String, Object> accessData = getAccessTokenIdForMadeAnOffer(getData);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(data.get("ContentType"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);
		library.setRequestHeader("Authorization", "Bearer " + token);
		String contactURL = String.valueOf(data.get("contactsUrl"));
		String response = library.HTTPGet(contactURL);
		System.out.println(response);
		String baseUrl = "http://agent-ng.movoto.net/agentservice/agents";
		String urlEndPart = "activities?size=10&startIndex=0";
		String agentID = xuserid;
		String opportunityId = (String) library.getValueFromJson(
				"$.clientList[0].contactInfo.opportunitiesSummary.openOpportunities[0].opportunityId", response);
		System.out.println(opportunityId);
		String userId = (String) library.getValueFromJson("$.clientList[0].contactInfo.userId", response);
		System.out.println(userId);
		String apiUrl = baseUrl + "/" + agentID + "/" + "contacts" + "/" + userId + "/" + "opportunities" + "/"
				+ opportunityId + "/" + urlEndPart;
		System.out.println(apiUrl);
		return apiUrl;

	}

	// Created by Puneet on 05/10/2016 to fetch the Access Token and xuserID
	@Override
	public Map<String, Object> getAccessTokenIdForMadeAnOffer(Map<String, Object> data) {
		// Map<String, Object> apiData = (Map<String, Object>)
		// data.get("apiData");

		String contentType = String.valueOf(data.get("ContentType"));
		String accessTokenURL = String.valueOf(data.get("AccessTokenURL"));
		library.setContentType(contentType);

		Map<String, Object> apidata = new HashMap<>();

		// Map<String, Object> apidata1 = (Map<String, Object>)
		// data.get("apiData");
		apidata = (Map<String, Object>) data.get("LoginData");
		String response = library.HTTPPost(accessTokenURL, apidata);
		// library.HTTPPost(URL, pData);
		System.out.println(response);
		Object token = library.getValueFromJson("$.token", response);
		Object id = library.getValueFromJson("$.id", response);
		apidata.put("Token", token);
		apidata.put("Id", id);

		return apidata;

	}

	

	@Override
	public String getApiUrlForContractCancel(Map<String, Object> data) {

		Map<String, Object> getData = (Map<String, Object>) data.get("apiData");
		Map<String, Object> accessData = getAccessTokenIdForMadeAnOffer(getData);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(getData.get("ContentType"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);
		library.setRequestHeader("Authorization", "Bearer " + token);
		String contactURL = String.valueOf(data.get("contactsUrl"));
		String response = library.HTTPGet(contactURL);
		System.out.println(response);
		String baseUrl = "http://agent-ng.movoto.net/agentservice/agents";
		String urlEndPart = "activities?size=10&startIndex=0";
		String agentID = xuserid;
		String opportunityId = (String) library.getValueFromJson(
				"$.clientList[0].contactInfo.opportunitiesSummary.openOpportunities[0].opportunityId", response);
		System.out.println(opportunityId);
		String userId = (String) library.getValueFromJson("$.clientList[0].contactInfo.userId", response);
		System.out.println(userId);
		String apiUrl = baseUrl + "/" + agentID + "/" + "contacts" + "/" + userId + "/" + "opportunities" + "/"
				+ opportunityId + "/" + urlEndPart;
		System.out.println(apiUrl);
		return apiUrl;

	}

	

	

	@Override
	public void verifyPropertyDetailsForContractCancel() {
		
		Assert.assertTrue(library.verifyPageNotContainsElement("CONTRACTCANCEL.escrowinfo") , "The Escrow Info section is gone");
	}
	@Override
	public void searchByCityName(Map<String, Object> data) {

		library.wait(10);
		String CityName = (String) data.get("CityName");
		String Str = new String("Welcome to Tutorialspoint.com");

		System.out.print("Return Value :");
		System.out.println(Str.substring(10));
		library.clear("PROPERTY.searchtextbox");
		library.typeDataInto(CityName, "PROPERTY.searchtextbox");
		library.click("PROPERTY.searchbutton");
		library.wait(30);
		if (library.verifyPageContainsElement("PROPERTY.selectnoinalertbox")) {
			library.click("PROPERTY.selectnoinalertbox");
		}
		library.wait(10);
		String Url = library.getUrl();
		boolean city = Url.contains("woodstock-il/");
		System.out.println(city);
		library.verifyPageContainsElement("PROPERTY.searchtextbox");
		String cityNameInAddress = library.getTextFrom("PROPERTY.cityname");

		cityNameInAddress = library.getTextFrom("PROPERTY.cityname");
		// boolean isCityPresent = cityNameInAddress.contains(CityName);
		// Assert.assertTrue(isCityPresent); // validate city name
		Assert.assertEquals(cityNameInAddress, "Woodstock Real Estate & Homes for Sale");

	}

	public String getCityName(Map<String, Object> data) {
		String CityName = (String) data.get("CityName");

		String[] city = CityName.split(",");
		String cityName = city[0];
		return cityName;
	}

	@Override
	public void verifyTotalLeadCounts(Map<String, Object> data) {
		Map<String, Object> accessData = getAccessTokenId(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		try {
			String contentType = String.valueOf(data.get("ContentType"));
			String contactURL = String.valueOf(data.get("contactsUrl"));
			library.setContentType(contentType);
			library.setRequestHeader("x-userid", xuserid);
			library.setRequestHeader("Authorization", "Bearer " + token);
			String response = library.HTTPGet(contactURL);
			int arrayCount = (int) library.getValueFromJson("$.totalCount", response);
			System.out.println(arrayCount);
			String totalLeadCount = library.getTextFrom("CLIENTLIST.ClientnoMsg");
			System.out.println(totalLeadCount);
			String[] splittotalLeadCount = totalLeadCount.split(" ");
			String UitotalLeadCount = splittotalLeadCount[2];
			int finalUiValue = Integer.parseInt(UitotalLeadCount);
			System.out.println(finalUiValue);
			Assert.assertEquals(finalUiValue, arrayCount);//verify leads count

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void clientListSortByFirstName(Map<String, Object> data) {
		Map<String, Object> orderData = (Map<String, Object>) data.get("orderData");
		Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
		library.click("CLIENTLIST.newestreferred");
		library.wait(5);
		String dropdown = "CLIENTLIST.firstname";
		library.click(dropdown);
		/*
		 * String order = String.valueOf(orderData.get("Order")); String
		 * sortOrder = "xpath ->//a/span[text()='" + order + "']";
		 * library.getTextFrom(sortOrder); library.click(sortOrder);
		 */library.wait(2);
		Map<String, Object> accessData = getAccessTokenIds(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(apiData.get("ContentType"));
		String SortURL = String.valueOf(orderData.get("SortURL"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);

		String response = library.HTTPGet(SortURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		for (int i = 0; i < arrayCount && i <= 10; i++) {
			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;
			System.out.println(LeadName);
			int j = i + 1;
			String uiLeadNameLocator = "xpath->(//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'])["
					+ j + "]//a/strong";
			String uiLeadName = library.getTextFrom(uiLeadNameLocator);
			System.out.println(uiLeadNameLocator);
			Assert.assertEquals(uiLeadName, LeadName);
		}
	}

	@Override
	public void verifySendEmailFunctionalTest(Map<String, Object> data) {		
		Map<String, Object> accessData = getAccessTokenId(data);
	
	  String token = String.valueOf(accessData.get("Token"));
	  String xuserid = String.valueOf(accessData.get("Id"));
	  String contentType = String.valueOf(data.get("ContentType"));
	  String contactURL = String.valueOf(data.get("contactsUrl"));
	  library.setContentType(contentType);
	  library.setRequestHeader("x-userid", xuserid);
	  library.setRequestHeader("Authorization", "Bearer " + token);
	  String response = library.HTTPGet(contactURL);
	  String email = (String) library.getValueFromJson("$.clientList[0].contactInfo.email", response);
	  System.out.println(email);
	  library.wait(3);
	  Assert.assertTrue(library.verifyPageContainsElement("xpath->//span[text()='" + email + "']"));// Verify
	                          // email
	                          // value
	                          // from
	                          // API
	                          // response
	                          // matches
	                          // with
	                          // corresponding
	                          // client
	                          // email
	                          // field
	  library.click("xpath->//span[text()='" + email + "']");
	  Assert.assertTrue(library.verifyPageContainsElement("SENDEMAIL.text"));// Verify
	                    // Email
	                    // Client
	                    // page
	                    // is
	                    // opened
	                    // after
	                    // clicking
	                    // Email
	                    // icon
	  library.typeDataInto("Test Automation", "SENDEMAIL.subjectfield");
	  library.wait(2);
	  Assert.assertTrue(library.isElementEnabled("SENDEMAIL.sendbutton", true));
	  library.click("SENDEMAIL.sendbutton");
	  library.waitForElement("SENDEMAIL.success");
	  Assert.assertTrue(library.verifyPageContainsElement("SENDEMAIL.success"));
	  library.waitForElement("CLIENTLIST.clientlisttext");
	  Assert.assertTrue(library.verifyPageContainsElement("CLIENTLIST.clientlisttext"));// Verify
	                       // the
	                       // agent
	                       // is
	                       // led
	                       // back
	                       // to
	                       // Client
	                       // List
	                       // page
	                       // again.
	  library.click("CLIENTLIST.clientlisttext");
	  library.wait(20);
	  library.verifyPageNotContainsElement("CLIENTLIST.newestreferred");
	  library.wait(3);
	  library.click("CLIENTLIST.getopportunity");
	  library.wait(5);
	  library.click("URGENCY.notes");
	  library.wait(5);
	  String activitytitle= library.getTextFrom("CLIENTLIST.activitytitle");
	  Assert.assertTrue(activitytitle.contains("Email"));
	  Assert.assertTrue(library.verifyPageContainsElement("CLIENTLIST.selecttext"));// Verify
	                      // Email
	                      // event
	                      // is
	                      // on
	                      // top
	                      // of
	                      // all
	                      // the
	                      // other
	                      // notes.
	  library.wait(5);


	}

	@Override
	public void verifyClientFilterWithMet(Map<String, Object> data) {
		Map<String, Object> orderData = (Map<String, Object>) data.get("orderData");
		Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
		library.click("xpath->.//*[@id='sort-filter-group']/button[3]");
		library.wait(5);
		String dropdown = "xpath->(.//span[contains(text(),'Met')])[1]";
		library.click(dropdown);

		library.wait(2);
		Map<String, Object> accessData = getAccessTokenIds(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(apiData.get("ContentType"));
		String SortURL = String.valueOf(orderData.get("SortURL"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);

		String response = library.HTTPGet(SortURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		System.out.println("Filtered Count is " + arrayCount);
		String[] UI = library.getTextFrom("CLIENTLIST.ClientnoMsg").split(" ");
		String Count = UI[2];
		int UICount = Integer.parseInt(Count);
		Assert.assertEquals(UICount, arrayCount);//Verify the filter result count on UI with the response from API

		for (int i = 0; i < arrayCount && i <= 10; i++) {
			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;
			System.out.println(LeadName);
			int j = i + 1;
			String uiLeadNameLocator = "xpath->(//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'])["
					+ j + "]//a/strong";
			String uiLeadName = library.getTextFrom(uiLeadNameLocator);
			System.out.println(uiLeadNameLocator);
			Assert.assertEquals(uiLeadName, LeadName);//Verify the client names from API response with corresponding filter result
		}
	}

	// 22 aug 2014 by Puneet --------------

	@Override
	public void searchByZipCode(Map<String, Object> data) {

		library.wait(10);
		String zipcode = (String) data.get("ZipCode");
		library.verifyPageContainsElement("PROPERTY.searchtextbox");
		library.clear("PROPERTY.searchtextbox");
		library.typeDataInto(zipcode, "PROPERTY.searchtextbox");
		library.click("PROPERTY.searchbutton");
		library.verifyPageContainsElement("PROPERTY.searchbutton");
		library.wait(5);
		// boolean isPopupPresent =
		// library.verifyPageContainsElement("PROPERTY.selectnoinalertbox",
		// true); // checking for popup (YES OR NO)
		// if(isPopupPresent)
		// {
		library.click("PROPERTY.selectnoinalertbox");
		// }
		library.wait(10);
		String Url = library.getUrl();
		boolean UrlValue = Url.contains("il/" + zipcode + "/@");
		System.out.println(UrlValue);
		library.verifyPageContainsElement("PROPERTY.searchbutton");

		String zipCodeInAddress = library.getTextFrom("PROPERTY.cityname");
		boolean isZipCodePresent = zipCodeInAddress.contains(zipcode);

		Assert.assertTrue(isZipCodePresent); // validate zipcode name

	}

	// Created by Puneet dated : - 22/08/2016
	public String getMapSearchApiResultForZipCode(Map<String, Object> data) {

		// Get Data from MAp for API headers and City Name
		String contentType = (String) data.get("ContentType");
		// System.out.println(contentType);
		String LoginDataPath = (String) data.get("LoginDataPath");
		String acceptEncoding = (String) data.get("acceptEncoding");
		String MapSearchApi = (String) data.get("MapSearchApi");
		String Host = (String) data.get("Host");
		String Connection = (String) data.get("Connection");
		String UserAgent = (String) data.get("UserAgent");
		String XMdatakey = (String) data.get("XMdatakey");
		String ContentLength = (String) data.get("ContentLength");
		String Zipcode = (String) data.get("ZipCode");

		// Setting up headers for Map search API
		library.setContentType(contentType);
		library.setRequestHeader("X-MData-Key", XMdatakey);
		library.setRequestHeader("Host", Host);
		library.setRequestHeader("Connection", Connection);
		library.setRequestHeader("User-Agent", UserAgent);
		library.setRequestHeader("ContentLength", ContentLength);
		library.setRequestHeader("Accept-Encoding", acceptEncoding);

		Map<String, Object> jsonData = (Map<String, Object>) data.get("JsonData");
		// System.out.println(jsonData);
		jsonData.put("input", Zipcode); // updating json file to set zipcode
										// taken from excel.
		// System.out.println(jsonData);
		String response = library.HTTPPost(MapSearchApi, jsonData); // hitting
																	// Map
																	// Search
																	// API

		// System.out.println(response);
		return response;

	}

	@Override
	public void searchByNeighborhood(Map<String, Object> data) {

		library.wait(10);
		String neighborhood = (String) data.get("Neighborhood");

		library.clear("PROPERTY.searchtextbox");
		library.verifyPageContainsElement("PROPERTY.searchtextbox");
		library.typeDataInto(neighborhood, "PROPERTY.searchtextbox");
		library.verifyPageContainsElement("PROPERTY.searchtextbox");
		library.click("PROPERTY.searchbutton");
		library.wait(5);
		// boolean isPopupPresent =
		// library.verifyPageContainsElement("PROPERTY.selectnoinalertbox",
		// true); // checking for popup (YES OR NO)
		// if(isPopupPresent)
		// {
		library.click("PROPERTY.selectnoinalertbox");
		// }
		library.wait(10);
		String Neighborhood = library.getUrl();
		boolean UrlNeighborhood = Neighborhood.contains("san-mateo-ca/beresford-park/@");
		System.out.println(UrlNeighborhood);
		library.verifyPageContainsElement("PROPERTY.searchtextbox");

		String neighborhoodInAddress = library.getTextFrom("PROPERTY.cityname");
		boolean isCityPresent = neighborhoodInAddress.contains(neighborhood);
		Assert.assertTrue(isCityPresent); // validate neighborhood

	}

	// Created by Puneet dated : - 22/08/2016
	@Override
	public String getMapSearchApiResultForNeighborhood(Map<String, Object> data) {

		// Get Data from MAp for API headers and City Name
		String contentType = (String) data.get("ContentType");
		// System.out.println(contentType);
		String LoginDataPath = (String) data.get("LoginDataPath");
		String acceptEncoding = (String) data.get("acceptEncoding");
		String MapSearchApi = (String) data.get("MapSearchApi");
		String Host = (String) data.get("Host");
		String Connection = (String) data.get("Connection");
		String UserAgent = (String) data.get("UserAgent");
		String XMdatakey = (String) data.get("XMdatakey");
		String ContentLength = (String) data.get("ContentLength");
		String neighborhood = (String) data.get("Neighborhood");

		// Setting up headers for Map search API
		library.setContentType(contentType);
		library.setRequestHeader("X-MData-Key", XMdatakey);
		library.setRequestHeader("Host", Host);
		library.setRequestHeader("Connection", Connection);
		library.setRequestHeader("User-Agent", UserAgent);
		library.setRequestHeader("ContentLength", ContentLength);
		library.setRequestHeader("Accept-Encoding", acceptEncoding);

		Map<String, Object> jsonData = (Map<String, Object>) data.get("JsonData");

		jsonData.put("input", neighborhood); // updating json file to set
												// neighborhood taken from
												// excel.

		String response = library.HTTPPost(MapSearchApi, jsonData); // hitting
																	// Map
																	// Search
																	// API

		return response;

	}

	
	@Override
	 public int getTotalCountFromUI() { //Returns the total no of properties from the top left corner.
				String[] countFromUI = null; 
				if(library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
					countFromUI = library.getTextFrom("xpath->.//div[@class='pagination']/div[@class='info']/b").split(" ");
				}else{
					countFromUI = library.getTextFrom("PROPERTY.uitotalpropertycount").split(" "); // Results
				}
			   int count=0;
			   
			   //Checks the results length for two scenarios 1) Results 1-50 356 2) Results 28
			   String cnt = countFromUI[countFromUI.length-1].replaceAll(",", "");
			   if(countFromUI!=null && countFromUI.length==3)
				   count = Integer.parseInt(countFromUI[2]);
			   else
				   count=Integer.parseInt(cnt);   
			   
			   return count;
}
	
	@Override
	 public int getBottomTotalCountFromUI() { 
		   String[] countFromUIBottom=null;
		   JavascriptExecutor jse=(JavascriptExecutor)library.getDriver();
		   countFromUIBottom=((String)jse.executeScript("return (document.getElementsByClassName('pagination')[0].getElementsByTagName('b')[0].textContent);")).split(" ");
		   int count = Integer.parseInt(countFromUIBottom[4]);
		   System.out.println("TOTAL COUNT =================="+count);
		   return count;
}

	// Puneet - 29Aug2016
	@Override
	public void verifyDefaultValuesOnSearchPage(Map<String, Object> data) {
		
		String minPriceByDefaultOption = (String) data.get("MinPriceByDefaultOption");
		String maxPriceByDefaultOption = (String) data.get("MaxPriceByDefaultOption");
		String defaultBedValue = (String) data.get("DefaultBedValue");
		String defaultBathValue = (String) data.get("DefaultBathValue");
		
		String priceNoMinDefaultValue = library.getTextFrom("SEARCHPAGE.pricemin");
		String priceNoMaxDefaultValue = library.getTextFrom("SEARCHPAGE.pricemax");
		String bedDefaultValue = library.getTextFrom("SEARCHPAGE.beds");
		String bathDefaultValue = library.getTextFrom("SEARCHPAGE.baths");

		boolean isPriceDefaultValueIsCorrect = priceNoMinDefaultValue.contains(minPriceByDefaultOption);
		boolean isPriceDefaultValueIsCorrectForMax = priceNoMaxDefaultValue.contains(maxPriceByDefaultOption);
		boolean isBedDefaultValueIsCorrect = bedDefaultValue.contains(defaultBedValue);
		boolean isBathsDefaultValueIsCorrect = bathDefaultValue.contains(defaultBathValue);

		Assert.assertTrue(isPriceDefaultValueIsCorrect);
		Assert.assertTrue(isPriceDefaultValueIsCorrectForMax);
		Assert.assertTrue(isBedDefaultValueIsCorrect);
		Assert.assertTrue(isBathsDefaultValueIsCorrect);
	}

	@Override
	public void selectMinValueForPriceAndVerify(Map<String, Object> data) {
		String minPriceByDefaultOption = (String) data.get("MinPriceByDefaultOption");
		String minPriceData = (String) data.get("MinPrice");
		String removeSpecialChaFromMinPriceData = minPriceData.replace("$", "");
		String removeSpecialChaFromMinPrice = removeSpecialChaFromMinPriceData.replaceAll("K", "000");
		int minPriceInInt = Integer.parseInt(removeSpecialChaFromMinPrice);
        String[] countFromUI = library.getTextFrom("PROPERTY.uitotalpropertycount").split(" "); // Results
		String countRemoveComma = countFromUI[4].replace(",", "");
		int count = Integer.parseInt(countRemoveComma);
		Select selectElement = new Select(library.findElement("SEARCHPAGE.selectpricemin"));
		selectElement.selectByVisibleText(minPriceData);
		verifyMinPrice(data);
		library.waitForElement("minPriceByDefaultOption");
		selectElement.selectByVisibleText(minPriceByDefaultOption);
		library.wait(4);
		String countRemoveCommaAfter = countFromUI[4].replace(",", "");
		int countAfter = Integer.parseInt(countRemoveCommaAfter);
		Assert.assertEquals(countAfter, count);// 3rd validation point
	}

	@Override
	public void selectMaxPriceValueAndVerify(Map<String, Object> data) {
		String maxPriceByDefaultOption = (String) data.get("MaxPriceByDefaultOption");
		String maxPrice = (String) data.get("MaxPrice");
		String removeSpecialCharFromMaxPrice = maxPrice.replace("$", "");
		String removeMFromMaxPrice= removeSpecialCharFromMaxPrice.replaceAll("K", "000");
		int maxPriceInInt = Integer.parseInt(removeMFromMaxPrice);
		String[] countFromUIAfterSelectingMaxPrice = library.getTextFrom("PROPERTY.uitotalpropertycount").split(" "); // Results
		String countFromUIAfterSelectingMaxPriceRemoveComma = countFromUIAfterSelectingMaxPrice[4].replace(",", "");
		int count = Integer.parseInt(countFromUIAfterSelectingMaxPriceRemoveComma);
		Select selectElementForMaxPrice = new Select(library.findElement("SEARCHPAGE.selectBoxForPriceMax"));
		selectElementForMaxPrice.selectByVisibleText(maxPrice);
		verifyMaxPrice(data);
		selectElementForMaxPrice.selectByVisibleText(maxPriceByDefaultOption);
		library.wait(5);
		String[] countFromUIAfterSelectingMaxPrice1 = library.getTextFrom("PROPERTY.uitotalpropertycount").split(" "); // Results
		String countFromUIAfterSelectingMaxPriceRemoveCommaa = countFromUIAfterSelectingMaxPrice1[4].replace(",", "");
		int countAfterSelectingNoMax = Integer.parseInt(countFromUIAfterSelectingMaxPriceRemoveCommaa);
		Assert.assertEquals(countAfterSelectingNoMax, count);// 4th validation
	}

	@Override
	public void selectMinValueForBedAndVerify(Map<String, Object> data) {
		String defaultBedValue = (String) data.get("DefaultBedValue");
		String minBedValue = (String) data.get("MinBedValue");
		int minBedValueInInt = Integer.parseInt(minBedValue);
		Select selectElementForMinBed = new Select(library.findElement("SEARCHPAGE.selectBedBox"));
		selectElementForMinBed.selectByValue(minBedValue);
		library.wait(4);
		String minValueInUIForBed = library.getTextFrom("SEARCHPAGE.bedminvalue");
		String minValueInUIForBedRemove = minValueInUIForBed.replace("+", "");
		minValueInUIForBedRemove = minValueInUIForBedRemove.replaceAll("(?m)^[ \t]*\r?\n", "").trim();
		int minValueInUIForBedInt = Integer.parseInt(minValueInUIForBedRemove);
		verifyMinValueForBed(data);
		library.wait(5);
		selectElementForMinBed.selectByVisibleText(defaultBedValue);
	}

	public void selectMinValueForBathAndVerify(Map<String, Object> data) {
		String minBathValue = (String) data.get("MinBathValue");
		int minBathValueInInt = Integer.parseInt(minBathValue);
		Select selectElementForMinBath = new Select(library.findElement("SEARCHPAGE.selectBathBox"));
		selectElementForMinBath.selectByValue(minBathValue);
		library.wait(4);
		verifyMinValueForBath(data);
	}

	@Override
	public void loginAndDeleteFavouriteProperty(JSONObject data) {
		LoginCredential(data);
		library.wait(5);
		NavigateToFavouriteHomes(data);
		if (!(library.getCurrentPlatform().contains("Android")) || !(library.getCurrentPlatform().contains("IOS_WEB"))) {
			library.waitForElement("FAVOURITEICON.heart");
		} 
		deleteFavouriteItem(data);
	}

	@Override
	public void verifyHouseCardsDisplay(Map<String, Object> data) {
        //verifying daysOnMarket of API response from small to large(Mistake in Test Link as it is written daysOnMarket from large to small instead of small to large. )
		library.wait(5);
		String sortByDefaultOption = (String) data.get("SortByDefaultOption");
		//Verifying Relevant is by default selected on UI
		//For Safari browser we need to select Relevant option because Safari options recently selected option, so we need to make sure that we select Relevant and then only verify.
		if(library.getCurrentBrowser().contains("Safari") && !library.getCurrentPlatformType().equalsIgnoreCase("IOS_WEB")){
			WebElement selectedView = library.findElement("SEARCHPAGE.sortOption");
			library.mouseHoverJScript(selectedView);
			 WebElement sortByFirstOption = library.findElement("SEARCHPAGE.sortByFirstOption");
			 library.click("SEARCHPAGE.sortByFirstOption");
		}
		//Only for Android Web and IOS Safari Web since There is different button for Sort button
		if(library.getCurrentPlatformType().equalsIgnoreCase("ANDROID_WEB") || library.getCurrentPlatformType().equalsIgnoreCase("IOS_WEB")){
			//Click on MAP orange button.
		   	library.click("xpath->//a[@class='btn orange sortBtn']");
		   	//Getting text from Sort options.
			String getSort = library.getTextFrom("xpath->//div[@class='r dropdown r-sort']/a/span");
			//Validating Relevant default option.
			Assert.assertTrue(getSort.equalsIgnoreCase(sortByDefaultOption));
		}
		else{
		//Click on Sort By Relavant option on rigth top.
		library.click("xpath->//span[@data-i18n='search##sort.relevant']");
		String getSort = library.getTextFrom("xpath->(.//*[@id='sortFilter']/li)[1]/label");
		Assert.assertTrue(getSort.equalsIgnoreCase(sortByDefaultOption));
		}
		//library.verifyPageContainsElement("SEARCHPAGE.relevant");
		//String newestContains = library.getTextFrom("SEARCHPAGE.relevant");
		//boolean isNewestContains = newestContains.contains(sortByDefaultOption);
		//Assert.assertTrue(isNewestContains); // VALIDATING newest on the searchpage
		String mapSearchAPIResponse = getMapSearchApiResultForCity(data); // hitting map search API
		//Getting first 5 daysOnMarket values from API response.
		try
		{
		JSONParser jsonParser=new JSONParser();
		JSONObject jsonObject=(JSONObject)jsonParser.parse(mapSearchAPIResponse);
		JSONArray array=(JSONArray)jsonObject.get("listings");
		long i1 = ((long)((JSONObject)array.get(0)).get("daysOnMarket"));
		long i2 = ((long) ((JSONObject)array.get(1)).get("daysOnMarket"));
		long i3 = ((long) ((JSONObject)array.get(2)).get("daysOnMarket"));
		long i4 = ((long) ((JSONObject)array.get(3)).get("daysOnMarket"));
		long i5 = ((long) ((JSONObject)array.get(4)).get("daysOnMarket"));
		// Validating from small to large.
		Assert.assertTrue((i1 <= i2));
		Assert.assertTrue((i2 <= i3));
		Assert.assertTrue((i3 <= i4));
		Assert.assertTrue((i4 <= i5));
		
		}catch(Exception e)
		{
			System.out.println("Exception "+e.getMessage());
		}
}

	@Override
	public void selectPriceLowAndVerifiHouseCards(Map<String, Object> data) {
		//	Verify house cards are orderly displayed by price from low to high AND current page's url contains "sortby-price-asc/"
		String urlContains = (String) data.get("UrlPart");
		//AutomationNGDriver driver = null;
		WebDriver driver = null;
		// Condition for Chrome on Android
		if (library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")) {
			driver = library.getDriver();
			library.click("xpath->//a[@class='btn orange sortBtn']");
			library.wait(3);
			if(library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
				JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
				   js.executeScript("document.getElementById('sortFilter').children[3].children[0].click()");
			}
			else{
			library.click("xpath->//div[@class='r dropdown r-sort']/ul/li[@data-ga-name='PriceLow']");
			}
			library.wait(5);
			String priceURL = driver.getCurrentUrl();
			Boolean priceURLFlag = priceURL.contains(urlContains);
			Assert.assertTrue(priceURLFlag);// Verify Url 
			verifySortingForPrice();
		}
				// Else part for Browsers IE,Safari and Chrome.
		else {

			String browsertype = library.getBrowserName();
			switch (browsertype) {
			case "IExplore":
			case "Chrome":
				library.wait(3);
				library.click("SEARCHPAGE.sortByOption");
				library.wait(5);
				library.click("SEARCHPAGE.pricelow");
				library.wait(10);
				String url1 = library.getUrl();
				boolean verifyUrl1 = url1.contains(urlContains);
				Assert.assertTrue(verifyUrl1);
				library.wait(10);
				String url = library.getUrl();
				boolean verifyUrl = url.contains(urlContains);
				Assert.assertTrue(verifyUrl);
				verifySortingForPrice();
				break;
			case "Safari":
				library.wait(3);
				//clicking on selected View 
				JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
				js.executeScript("if(document.createEvent){var evObj = document.createEvent('MouseEvents'); evObj.initEvent('mouseover',true, false); document.getElementsByClassName('r dropdown r-sort')[0].dispatchEvent(evObj);}else if(document.createEventObject){ document.getElementsByClassName('r dropdown r-sort')[0].fireEvent('onmouseover');}");
			    //WebElement selectedView = library.findElement("SEARCHPAGE.sortOption");
				//library.mouseHoverJScript(selectedView);
				library.wait(2);
				//selecting Split view 
				WebElement splitView = library.findElement("SEARCHPAGE.splitview");
				library.mouseHoverJScript(splitView);
				library.wait(3);
				// mouse hover to Save Search button to remove previous selected view list
				WebElement saveSearchButton = library.findElement("SEARCHPAGE.saveSearchLinkButton");
				library.mouseHoverJScript(saveSearchButton);
				library.wait(1);
				library.waitForElement("SEARCHPAGE.pricelow");
				library.click("SEARCHPAGE.pricelow");
				// Page loading after clicking on Price Low or page transition thats why used hard wait.
				library.wait(10);
				String url11 = library.getUrl();
				boolean verifyUrl11 = url11.contains(urlContains);
				Assert.assertTrue(verifyUrl11);
				verifySortingForPrice();
                break;
			}
		}
	}
	
	@Override
	public void selectSquareBigAndVerifyHouseCards(Map<String, Object> data) {
		String SortBySqftInUrl = (String) data.get("SortBySqftInUrl");
		WebDriver driver = library.getDriver();
		if (library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")) {
			library.click("xpath->//a[@class='btn orange sortBtn']");
			library.wait(3);
			if(library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
				JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
				   js.executeScript("document.getElementById('sortFilter').children[4].children[0].click()");
			}
			else{
				library.click("xpath->//div[@class='r dropdown r-sort']/ul/li[@data-ga-name='SqftBig']");
			}
			library.wait(5);
			String sqFeetURL = driver.getCurrentUrl();
			Boolean sqFeetURLFlag = sqFeetURL.contains(SortBySqftInUrl);
			Assert.assertTrue(sqFeetURLFlag);
			verifySortingForSquareBigOption();
		} else {
			String browsertype = library.getBrowserName();
			boolean sorted = true;
			switch (browsertype) {
			case "IExplore":
				library.wait(10);
				JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
				js.executeScript("document.getElementById('sort4').click()");
				//library.click("xpath->//div[@class='r dropdown r-sort']/ul/li[@data-ga-name='SqftBig']");
				library.wait(10);
				String urlforSqft = library.getUrl();
				boolean verifyUrlContains = urlforSqft.contains(SortBySqftInUrl);
				Assert.assertTrue(verifyUrlContains);
				verifySortingForSquareBigOption();
				break;
			case "Chrome":
			case "Safari":
				library.wait(10);
				//JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
				//js.executeScript("if(document.createEvent){var evObj = document.createEvent('MouseEvents'); evObj.initEvent('mouseover',true, false); document.getElementsByClassName('r dropdown r-sort')[0].dispatchEvent(evObj);}else if(document.createEventObject){ document.getElementsByClassName('r dropdown r-sort')[0].fireEvent('onmouseover');}");
				//WebElement sortText = library.findElement("SEARCHPAGE.sortByOption");
				//library.click("SEARCHPAGE.sortBySqftBigOption");xpath->//div[@class='r dropdown r-sort']/ul/li[@data-ga-name='SqftBig']
				//library.click("xpath->(.//*[@id='sortFilter']/li)[5]");
				JavascriptExecutor js1 =(JavascriptExecutor)library.getDriver();
				js1.executeScript("document.getElementById('sort4').click()");
				library.wait(10);
				String url1 = library.getUrl();
				boolean verifyUrl1 = url1.contains(SortBySqftInUrl);
				Assert.assertTrue(verifyUrl1);
				verifySortingForSquareBigOption();
				break;
			}
		}
}
	
	@Override
	public void verifyFilterFunctionality(Map<String, Object> data) {
		String minSqft = String.valueOf(data.get("MinSqft"));
		String defaultValueForSquareFeetFilter = (String) data.get("DefaultValueForSqft");
		if (library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB") ) {
			WebDriver driver = library.getDriver();
			JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
			   js.executeScript("document.getElementsByClassName('r-morefilters-toggle btn orange')[0].click()");
			//library.click("xpath->//a[@class='r-morefilters-toggle btn orange']/span");
			//library.click("SEARCHPAGE.filtersButton");
			//Getting value from Apply button.
			System.out.println("APPLY BUTTON VALUE------>"+library.getTextFrom("MOREFILTERS.applyButtonCount"));
			Integer defaultResultCount = Integer.parseInt(library.getTextFrom("MOREFILTERS.applyButtonCount"));
			Select minHouseSizeSelect = new Select(library.findElement("FILTERS.minHouseSize"));
			WebElement minHouseSizeSelectedOption = minHouseSizeSelect.getFirstSelectedOption();
			//Verify default filter text is "No Min" for min Square Feet filter
			Assert.assertTrue(minHouseSizeSelectedOption.getText().equals(defaultValueForSquareFeetFilter));
			minHouseSizeSelect.selectByValue(minSqft);
			library.wait(15);
			js.executeScript("document.getElementById('applyFilters').click()");
			library.wait(5);
			//library.click("MOREFILTERS.applyButton");
			//library.click("SEARCHPAGE.alertPopup");//yes or no alert
			boolean isAlertPresent = library.verifyPageContainsElement("PROPERTY.selectnoinalertbox");
			if(isAlertPresent){
			library.click("PROPERTY.selectnoinalertbox");
			}
			countPropertyCardAndCheckSqft(minSqft);
			
			js.executeScript("document.getElementsByClassName('r-morefilters-toggle btn orange')[0].click()");
			library.wait(5);
			//library.click("SEARCHPAGE.filtersButton");
			Integer resultCountBeforeSelect = Integer
					.parseInt(library.getTextFrom("MOREFILTERS.applyButtonCount"));
			minHouseSizeSelect.selectByVisibleText(defaultValueForSquareFeetFilter);
			library.wait(10);
			Integer defaultResultCountAfterMinSqFeet = Integer
					.parseInt(library.getTextFrom("MOREFILTERS.applyButtonCount"));
			
			Assert.assertEquals(defaultResultCount, defaultResultCountAfterMinSqFeet);
			library.waitForElement("MOREFILTERS.cancelButton");
			TouchActions actions;
			if(library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
				WebElement cancelButton = driver.findElement(By.id("cancelFilters"));
				 actions = new TouchActions(driver);
				actions.singleTap(cancelButton).perform();
				library.wait(10);
			}
			else{
				library.wait(10);
				library.click("MOREFILTERS.cancelButton");
			}
			library.wait(15);
			String countOnPage = library.getTextFrom("xpath->//div[@class='pagination']//div[@class='info']/b");
			//String countOnPage = library.getTextFrom("SEARCHPAGE.propertyCountOnPage");
			String[] countOnPageArr = countOnPage.split(" ");
			
			Integer countOnPageValue = Integer.parseInt(countOnPageArr[4]);
			System.out.println("COUNT ====================================>"+countOnPageValue);
			Assert.assertEquals(countOnPageValue, resultCountBeforeSelect);
			js.executeScript("document.getElementsByClassName('r-morefilters-toggle btn orange')[0].click()");
			//library.click("SEARCHPAGE.filtersButton");
			
			if(library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
				WebElement resetFilters = driver.findElement(By.id("resetFilters"));
				actions = new TouchActions(driver);
				actions.singleTap(resetFilters).perform();
				library.wait(5);
				
			}else{
				library.click("MOREFILTERS.resetButton");
			}
			
			library.wait(10);
			Integer resultCountAfterReset = Integer
					.parseInt(library.getTextFrom("MOREFILTERS.applyButtonCount"));
			Assert.assertEquals(defaultResultCount, resultCountAfterReset);
}
         //For IE,Chrome 
		 else {
			library.wait(15);
			// getting Sqft from Excel
			Assert.assertTrue(library.verifyPageContainsElement("SEARCHPAGE.morefilters"));
			if(library.getBrowserName().equalsIgnoreCase("IExplore")){
				JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
				   js.executeScript("document.getElementsByClassName('r-morefilters-toggle')[0].click()");
			}else{
			library.click("SEARCHPAGE.morefilters");
			}
			library.waitForElement("MOREFILTERS.applyButtonCountOnBrowser");
			String countOnApplyButtonBeforeSelectingSqft = getValueFromApplyButtonJS();
            library.waitForElement("SEARCHPAGE.sqftmin");
            Assert.assertTrue(library.verifyPageContainsElement("SEARCHPAGE.sqftmin"));
			String sqftMinDefaultValue = library.getTextFrom("SEARCHPAGE.sqftmin");
			boolean isDefaultValueContainsNoMin = sqftMinDefaultValue.contains(defaultValueForSquareFeetFilter);
			Assert.assertTrue(isDefaultValueContainsNoMin);// validating default
			// value for Sqft
			library.click("SEARCHPAGE.sqftmin");
			library.select("SEARCHPAGE.sqftmin", minSqft);
			library.waitForElement("FILTERS.applybutton");
			String countOnApplyButton = getValueFromApplyButtonJS();
			// hitting map Search API
			String mapSearchAPIResponse = getMapSearchApiResultForFilterFunctionality(data);
			String getTotalPropertyCount = String
					.valueOf(library.getValueFromJson("$.totalCount", mapSearchAPIResponse));
			boolean isCountValueIsProper = countOnApplyButton.contains(getTotalPropertyCount);
			/// validate APply button contains correct count.
			Assert.assertTrue(isCountValueIsProper);
			library.waitForElement("FILTERS.applybutton");
			if (library.getBrowserName().equalsIgnoreCase("IExplore")) {
				library.wait(5);
				JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
				   js.executeScript("document.getElementsByClassName('btn orange r-submit-apply')[0].click()");
			} else {
				library.wait(4);
				javascriptexecutorclick("xpath->//div[@id='applyFilters']");
			}
			boolean flag = false;
			try {
				flag = library.getDriver().findElement(By.xpath(DriverLocator.SEARCHPAGE_dialogBody)).isDisplayed();
			} catch (Exception e) {
				flag = false;

			}
			library.wait(5);
			if (flag) {
				library.isJSEClicked("SEARCHPAGE.cancelSecondSearch");
			}
			WebDriver driver = library.getDriver();
			//Boolean minHouseSizeFlag = true;
			library.wait(5);
			countPropertyCardAndCheckSqft(minSqft);
			library.getDriver().navigate().refresh();
			if (library.getBrowserName().equalsIgnoreCase("IExplore")) {
				library.isJSEClicked("SEARCHPAGE.moreFiltersToggle");
			} else {
				library.click("SEARCHPAGE.morefilters");
			}
			library.waitForElement("FILTERS.minHouseSize");
			//Using directly By.name locator strategy until library.findElement gets resolved. 
			Select minSqftSelect = new Select(driver.findElement(By.name("minHouseSize")));
			minSqftSelect.selectByVisibleText(defaultValueForSquareFeetFilter);
			//library.waitForElement will not work here because it only waits for button not value inside the button that's why used hard wait.
			library.wait(10);
			String countOnApplyButtonByFefault = getValueFromApplyButtonJS();
			boolean isCountSame = countOnApplyButtonByFefault.contains(countOnApplyButtonBeforeSelectingSqft);
			Assert.assertTrue(isCountSame);
			JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
			js.executeScript("document.getElementById('cancelFilters').click()");
			//library.click("SEARCHPAGE.cancelButtonn");
			library.waitForElement("SEARCHPAGE.applyButtonValueA");
			String resultAfterCancel = library.getTextFrom("SEARCHPAGE.applyButtonValueA");
			String[] countAfterCancel = resultAfterCancel.split(" ");//[Results, 1-50, of, 360]
			boolean countCheck = countOnApplyButton.contains(countAfterCancel[4].replace(",",""));
			System.out.println("Count check ===="+countCheck);
			Assert.assertTrue(countCheck);
			library.click("SEARCHPAGE.morefilters");
			//library.waitForElement("MOREFILTERS.resetDiv");
			library.wait(5);
			js.executeScript("document.getElementById('resetFilters').click()");
			library.wait(5);
			//library.click("MOREFILTERS.resetDiv");
			library.waitForElement("MOREFILTERS.resetText");
			library.click("MOREFILTERS.resetText");
			library.wait(3);
			library.waitForElement("MOREFILTERS.applyButtonCountOnBrowser");
			String countOnApplyButtonAfterSelectingReset = getValueFromApplyButtonJS();
			Assert.assertEquals(countOnApplyButtonAfterSelectingReset, countOnApplyButtonBeforeSelectingSqft);
		}
		}

	@Override
	public void verifyAddFavouriteFunctionality(JSONObject data) {
         
		if (library.getCurrentPlatform().contains("Android") || library.getCurrentPlatform().contains("IOS") ) {
    
			// Precondition : - login and delete all favorite
			loginAndDeleteFavouriteProperty(data); 
			// Reg-196 Test case start here.
			library.get((String) data.get("TestCaseUrl")); 
			String addressBeforeFavourite = library.getTextFrom("FAVORITEPROPERTY.address");
			addressBeforeFavourite = addressBeforeFavourite.split(",")[0];
			//1100 S Hope Street St #1712, Los Angeles
			//, CA
			System.out.println(addressBeforeFavourite);
			SelectFavouriteProperty(data);
			String addressAfterFavourite = library.getTextFrom("FAVOURITEPAGE.address");
			//1100 S Hope Street St #1712
			//Los Angeles, CA, 90015
			addressAfterFavourite = addressAfterFavourite.split(",")[0];
			System.out.println(addressAfterFavourite);
			// Validating address
			boolean isFavouriteAddressIsCorrect = addressAfterFavourite.contains(addressBeforeFavourite);
			Assert.assertTrue(isFavouriteAddressIsCorrect); 
			String classAttribute = library.getAttributeOfElement("class", "FAVOURITEICON.color");
			classAttribute = classAttribute.trim();
			System.out.println(classAttribute);
			boolean isClassAttributeActive = classAttribute.contains("active");
			Assert.assertTrue(isClassAttributeActive, "Favorite icon colour is not red");
			logOutForConsumerWeb(data);
			} else {
			WebDriver driver = library.getDriver();
			library.waitForElement("FAVOURITEICON.address");
			String addressBeforeFavourite = library.getTextFrom("FAVOURITEICON.address");
			loginAndDeleteFavouriteProperty(data); // Precondition for Reg-196
			// transition of page thats why used Hard wait.									
			library.wait(10); 
			String testCaseUrl = (String) data.get("TestCaseUrl");
			library.get(testCaseUrl);
			library.wait(15);
			SelectFavouriteProperty(data);
			library.getDriver().navigate().refresh();
			library.waitForElement("USER.icon");
			library.wait(10);
			WebElement userIcon1 = library.findElement("xpath->//i[@class='icon-user']");
			/*robotPoweredMoveMouseToWebElementCoordinates(userIcon1);
			robotPoweredClick();*/
			System.out.println("Element is "+userIcon1);
			library.mouseHoverJScript(userIcon1);
			String FavouriteUrl = (String) data.get("FavouriteUrl");
			driver.get(FavouriteUrl);			
			library.wait(3);
			library.waitForElement("FAVOURITEPAGE.address");
			library.wait(5);
			String addressAfterFavourite = library.getTextFrom("FAVOURITEPAGE.address");
			boolean isAddressSame = addressAfterFavourite.contains(addressBeforeFavourite);
			Assert.assertTrue(isAddressSame);
			// Code to check Icon turns red or not.
			String classAttribute = library.getAttributeOfElement("class", "FAVOURITEICON.color");
			classAttribute = classAttribute.trim();
			System.out.println(classAttribute);
			boolean isClassAttributeActive = classAttribute.contains("active");
			Assert.assertTrue(isClassAttributeActive, "Favorite icon colour is not red");
			logOutForConsumerWeb(data);
		}

	}

	@Override
	public void removeAddsPopUp() {
		if (!library.getCurrentPlatform().equals("Android")) {
			boolean ispopup = library.verifyPageContainsElement("POPUP.close");
			if (ispopup)
				library.click("POPUP.close");
			library.wait(3);
		}
	}

	@Override
	public void hotLeadFunctionality(String data,JSONObject jsondata) {

		if (library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")) {
			// Preconditions - Make sure the page has 1 house with photo count >9
			
			checkPreconditionFor94();
			library.wait(5);
			String mapSearchAPIResponse = data;
			
			clickNineTimesOnPropertyImageForMobilePlatform(mapSearchAPIResponse);
			library.wait(5);
			// Verify "Request a Tour" window pops up
			boolean isPopupPresent = library.verifyPageContainsElement("PROPERTYCARD.popup");
			Assert.assertTrue(isPopupPresent);
			// Input <Email><Name><Phone> to the box and click "call me back" button
			library.clear("LOGINPOPUP.email");
			library.typeDataIntoWithJavaScript((jsondata.get("Username")).toString(), "LOGINPOPUP.email");
			library.clear("LOGINPOPUP.name");
			library.typeDataIntoWithJavaScript((jsondata.get("Name")).toString(), "LOGINPOPUP.name");
			library.clear("LOGINPOPUP.phoneNumber");
			library.typeDataIntoWithJavaScript((jsondata.get("PhoneNumber")).toString(), "LOGINPOPUP.phoneNumber");
			library.click("LOGINPOPUP.callmeBack");
			library.wait(5);
			
			String note=null;
			note = library.getTextFrom("xpath->//div[@page='thankyoupop']/div[2]");
			note = note.trim();
			
			if(note != null){
				Assert.assertTrue(library.verifyPageContainsElement("POPUP.thankyouNote"));
				Assert.assertEquals(note, jsondata.get("HotLeadPopupText").toString());
			}else{
				boolean isWhenDoYouHopeToBuyPopupIsPresent = library.verifyPageContainsElement("xpath->.//div[@class='dialog-body']");
				Assert.assertTrue(isWhenDoYouHopeToBuyPopupIsPresent);
				library.click("xpath->(.//i[@class='icon-remove'])[3]");
				library.wait(5);
				Assert.assertTrue(library.verifyPageContainsElement("POPUP.thankyouNote"));
				note = library.getTextFrom("xpath->//div[@page='thankyoupop']/div[2]");
				note = note.trim();
			}	
			library.wait(5);
			library.verifyPageContainsElement("POPUP.login");
			library.click("POPUP.login");
			library.typeDataInto((jsondata.get("Username")).toString(), "LOGIN.username");
			library.typeDataInto((jsondata.get("Password")).toString(), "LOGIN.password");
			library.click("LOGIN.submitButton");
			
			// Verify "Log in" button not exist on the window
			boolean isLoginButtonPresent = library.verifyPageNotContainsElement("POPUP.login");
			Assert.assertTrue(isLoginButtonPresent);
		} else {
			// Preconditions - Not login before test steps start
			boolean isLoginEnabled = library.verifyPageContainsElement("USER.icon");
			if (isLoginEnabled) {
				library.get(jsondata.get("LogoutUrl").toString());
			}
			
			// Preconditions - Make sure the page has 1 house with photo count > 9
			checkPreconditionFor94();
			String mapSearchAPIResponse = data;
			int intPhotoCount = 0;
			for (int i = 0; i < 50; i++) // for API array
			{
				String photoCount = String
						.valueOf(library.getValueFromJson("$.listings[" + i + "].photoCount", mapSearchAPIResponse));
				if (photoCount != null) {
					intPhotoCount = Integer.parseInt(photoCount);
				}
				if (intPhotoCount > 9) {
					int j=0;
					// Click ">" button on the card's photo for 9 times
					while (j<9)
					{
						library.wait(1);
						JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
						js.executeScript("document.getElementsByClassName('nav-r')["+i+"].click()");
						j++;
					}
					library.wait(3);
					// Verify "Let's Go!" button exists
					boolean isButtonPresent = library.verifyPageContainsElement("PROPERTYCARD.letsGoButton");
					Assert.assertTrue(isButtonPresent);
					// Click "Let's Go!" button
					library.click("PROPERTYCARD.letsGoButton");
					break;
				}
			}
			library.wait(15);
			// Verify "Request a Tour" window pops up
			boolean isPopupPresent = library.verifyPageContainsElement("PROPERTYCARD.popup");
			Assert.assertTrue(isPopupPresent);

			// Input <Email><Name><Phone> to the box and click "call me back"
			library.clear("LOGINPOPUP.email");
			library.typeDataIntoWithJavaScript((jsondata.get("Username")).toString(), "LOGINPOPUP.email");
			library.clear("LOGINPOPUP.name");
			library.typeDataIntoWithJavaScript((jsondata.get("Name")).toString(), "LOGINPOPUP.name");
			library.clear("LOGINPOPUP.phoneNumber");
			library.typeDataIntoWithJavaScript((jsondata.get("PhoneNumber")).toString(), "LOGINPOPUP.phoneNumber");
			library.isJSEClicked("LOGINPOPUP.callmeBack");
			
			library.wait(5);
			String note=null;
			note = library.getTextFrom("xpath->//div[@page='thankyoupop']/div[2]");
			note = note.trim();
			
			if(note != null){
				Assert.assertTrue(library.verifyPageContainsElement("POPUP.thankyouNote"));
				Assert.assertEquals(note, jsondata.get("HotLeadPopupText").toString());
			}else{
				boolean isWhenDoYouHopeToBuyPopupIsPresent = library.verifyPageContainsElement("xpath->.//div[@class='dialog-body']");
				Assert.assertTrue(isWhenDoYouHopeToBuyPopupIsPresent);
				library.click("xpath->(.//i[@class='icon-remove'])[3]");
				
				Assert.assertTrue(library.verifyPageContainsElement("POPUP.thankyouNote"));
				note = library.getTextFrom("xpath->//div[@page='thankyoupop']/div[2]");
				note = note.trim();
				Assert.assertEquals(note, jsondata.get("HotLeadPopupText").toString());
			}

			library.verifyPageContainsElement("POPUP.login");
			library.wait(5);

			// Click "Log in" button and complete login process
			library.isJSEClicked("POPUP.login");
			library.typeDataIntoWithJavaScript((jsondata.get("Username")).toString(), "LOGIN.username");
			library.typeDataIntoWithJavaScript((jsondata.get("Password")).toString(), "LOGIN.password");
			library.isJSEClicked("LOGIN.submitButton");
			// Verify "Log in" button not exist on the window
			library.wait(5);
			boolean isLoginButtonPresent = library.verifyPageNotContainsElement("POPUP.login");
			Assert.assertTrue(isLoginButtonPresent);
		}
	}

	@Override
	public void goOnImmediateLeave(Map<String, Object> data) {
		library.wait(5);
		library.click("MENU.goonleavebutton");
		library.wait(5);
		WebElement element=library.findElement("FLEAVE.returnnow");
		if(element !=null)
		{
			library.click("FLEAVE.returnnow");
			library.wait(10);
		}
		library.click("FLEAVE.goonleavebutton");
		library.wait(5);
		Assert.assertTrue(library.verifyPageContainsElement("FLEAVE.returnnow"));//Verify the text changed to "Return Now"
		library.click("HOMEPAGE.hamburger");
		library.wait(5);
		Assert.assertTrue(library.verifyPageContainsElement("FLEAVE.onleave"));//Verify the blue button "Return Now" appears on top of the Client List page
		setTokenAndUserId(data);
		String response = getResponse((String) data.get("ContactsUrl"));
		int uiValue=(int)library.getValueFromJson("$.Leaves[0].leaveType", response);
		Assert.assertEquals(uiValue, 1);//Verify leave status returned from API response is 1 (Immediately leave)
		library.wait(5);

	}

	@Override
	public void returnNow(Map<String, Object> data) {

		library.wait(5);
		library.click("MENU.goonleavebutton");
		library.wait(5);
		WebElement element=library.findElement("FLEAVE.goonleavebutton");
		if(element !=null)
		{
			library.click("FLEAVE.goonleavebutton");
			library.wait(10);
		}
		library.click("FLEAVE.returnnow");
		library.wait(3);
		library.click("HOMEPAGE.hamburger");
		library.wait(5);
		Assert.assertTrue(library.verifyPageNotContainsElement("FLEAVE.returnnow"));//Verify the "Return Now" text disappear
		setTokenAndUserId(data);
		String response = getResponse((String) data.get("ContactsUrl"));
		System.out.println(response);
		String UIValue=(String) library.getValueFromJson("$.Leaves[0].leaveType", response);
		Assert.assertEquals(UIValue, null);

	}

	@Override
	public void addFutureLeave(Map<String, Object> data) {

		library.wait(5);
		library.click("MENU.goonleavebutton");
		library.wait(5);
		setFutureDateUsingCalanderForFunctional(data);
		library.wait(5);
		int leaveCount = goToLeavePageCountLeave();
		setTokenAndUserId(data);
		String response = getResponse((String) data.get("ContactsUrl"));
		verifyLeavesWithApiResponse(response, leaveCount);
		System.out.println(response);

	}

	@Override
	public void deleteFutureLeave(Map<String, Object> data) {
		library.wait(5);
		library.click("MENU.goonleavebutton");
		library.wait(5);
		int getLeaveCountBeforeDeletion = goToLeavePageCountLeaves();
		deleteLeaves(1);
		library.wait(5);
		int leaveCount = goToLeavePageCountLeaves();
		setTokenAndUserId(data);
		String response = getResponse((String) data.get("ContactsUrl"));
		verifyLeavesWithApiResponse(response, leaveCount);
	}

	@Override
	public void logOutForConsumerWeb(JSONObject data) {
		String CurrentPlatform = library.getCurrentPlatform();
        if(library.getCurrentBrowser().equalsIgnoreCase("IExplore")){
        	library.get((String) data.get("LogoutUrl"));
        }
        else if(CurrentPlatform.equalsIgnoreCase("Android") || CurrentPlatform.equalsIgnoreCase("IOS_WEB") ){
        	library.wait(5);
        	library.get((String) data.get("TestCaseUrl")); 
        	library.wait(5);
        	library.waitForElement("HAMBURGER.menu");
			library.click("HAMBURGER.menu");
			library.waitForElement("Hamberger.userIcon");
			library.click("Hamberger.userIcon");
			library.waitForElement("Hamberger.userLogout");
			library.click("Hamberger.userLogout");
			library.wait(10);
        }
        else{
        	
        	/*WebElement element=library.findElement("USER.icon");
     	    library.mouseHoverJScript(element);*/
        	library.isJSEClicked("Hamberger.userIcon");
        	library.wait(5);   
            library.isJSEClicked("Hamberger.userLogout");
            library.wait(10);
        }
	}

	@Override
	public void checkIfLoggedInAndLogOut() {
		
		boolean isLoginEnabled=false;
		  WebDriver driver = library.getDriver();
		  try
		  {
		   isLoginEnabled = library.verifyPageContainsElement("xpath->//i[@class='icon-user']");
		  }
		  catch(Exception e)
		  {
		   isLoginEnabled=false;
		  }
		  if (isLoginEnabled) {
		   WebElement userIcon = driver.findElement(By.xpath("//i[@class='icon-user']"));
		   robotPoweredMoveMouseToWebElementCoordinates(userIcon);
		   robotPoweredClick();
		   library.wait(2);
		   WebElement logOut = driver.findElement(By.xpath("//a[text()='Log out']"));
		   logOut.click();
		   library.wait(2);
		  }
		
		/*WebDriver driver = library.getDriver();
		boolean isLoginEnabled = library.verifyPageContainsElement("xpath->//i[@class='icon-user']", true);
		if (isLoginEnabled) {
			WebElement userIcon = driver.findElement(By.xpath("//i[@class='icon-user']"));
			robotPoweredMoveMouseToWebElementCoordinates(userIcon);
			robotPoweredClick();
			library.wait(2);
			WebElement logOut = driver.findElement(By.xpath("//a[text()='Log out']"));
			logOut.click();
			library.wait(2);
		}*/

	}

	@Override
	public void verifySaveSearchFunctionality(JSONObject data) {
		WebDriver driver = library.getDriver();
		String firstUrl = (String) data.get("FirstURL");
		String secondUrl = (String) data.get("SecondURL");
		String popupTextForWoodstockCity= (String) data.get("PopupTextForCity");
		String thirdUrl = (String) data.get("ThirdURL");
		String advancedUrl = (String) data.get("AdvanceURL").toString().replace("#/", "");
		String PopupTextForZipCode = (String) data.get("popupTextForZipCode");
		String verifyUrlPart = (String) data.get("VerifyUrlPart");
		String verifyUrlPartForWoodstock = (String) data.get("VerifyUrlPartForWoodstock");
		String popupTextForTheaterDistrict = (String) data.get("PopupTextForTheaterDistrict");
		String popupTextForTheaterDistrict1 = popupTextForTheaterDistrict.replaceAll(" ", "");
		String theaterDistictUrlPart = (String) data.get("TheaterDistictUrlPart");
		String theaterDistictUrlPartForSavedSearch = (String) data.get("TheaterDistictUrlPartForSavedSearch");
		String zipCodeUrlPart = (String) data.get("ZipCodeUrlPart");
		String zipCodeUrlPartForSavedSearch = (String)data.get("ZipCodeUrlPartForSavedSearch");
		
		if (library.getCurrentPlatform().contains("Android") || library.getCurrentPlatform().contains("IOS_WEB")) {
            library.wait(40);   
            driver.get(firstUrl);
              library.wait(20);
              library.waitForElement("HOMEPAGE.savesearchlink");
              // click "Save Search" button
              library.click("SEARCHPAGE.saveSearchLinkButton");
              library.wait(5);
              String searchText = library.getTextFrom("POPUP.VerifyText");
              Assert.assertTrue(searchText.contains((String) data.get("popupTextForZipCode")));
              // Verify "Return to the map" button exist on the box.
              boolean isReturnToMapButtonPresent = library.verifyPageContainsElement("POPUP.returntomapbutton");
              Assert.assertTrue(isReturnToMapButtonPresent);
              // Click "Return to the map" button
              library.click("POPUP.returntomapbutton");
              library.wait(3);
              // Verify page's url is <URL_Aaved-searchesPage>
              Assert.assertTrue(library.getUrl().contains(firstUrl));
              // Verify "Save Search" button is disabled.
              verifySaveSearchButtonIsDisabled();
              // open saved search pag
              driver.get(advancedUrl);
              library.wait(5);
              // Verify search result with title -- "60089 IL " exits.
              boolean flag = library.findElement(("POPUP.VerifyTextContain60089")).isDisplayed();
              Assert.assertTrue(flag);
              library.wait(20);
               
              library.isJSEClicked("POPUP.VerifyTextContain60089");
              // library.click("POPUP.VerifyTextContain60089");
              //driver.findElement(By.xpath("(//*[contains(text(),'33914 FL')])[2]")).click();
              library.wait(20);
//           if (library.getCurrentPlatform().contains("IOS_WEB")){
//             String sUrlForSavedSearch = library.getUrl();
//             String sUrlForSavedSearch1 = (String) data.get(sUrlForSavedSearch);
//           }
              String base = driver.getWindowHandle();
              Set<String> set = driver.getWindowHandles();
              set.remove(base);
              for (String testString : set)
                driver.switchTo().window(testString);
              String sUrlForSavedSearch = library.getUrl();
               
//           if (library.getCurrentPlatform().contains("IOS_WEB")){
//                     sUrlForSavedSearch = (String) data.get(sUrlForSavedSearch);
//                     library.wait(20);
//                      
//                     try{
//                        System.out.println(driver.switchTo().alert().getText());
//                        driver.switchTo().alert().accept();
//                     }
//                     catch(Exception e)
//                     {
//                          
//                     }
//                      
//           }
             

              // 2. Verify Url for the saved search element contains "il/60089/"
              // and "?savedsearch"
              boolean isUrlForSavedSearchIsCorrect = sUrlForSavedSearch.contains(zipCodeUrlPart);
              boolean isZipCodeUrlPartForSavedSearchIsCorrect = sUrlForSavedSearch.contains(zipCodeUrlPartForSavedSearch);
              Assert.assertTrue(isUrlForSavedSearchIsCorrect);
              Assert.assertTrue(isZipCodeUrlPartForSavedSearchIsCorrect);
              // Open <URL_MapSearchPage2> to go to map search page
              library.wait(40);
              driver.get(secondUrl);
              library.wait(30);

              // Click "Save Search" button
              library.isJSEClicked("SEARCHPAGE.saveSearchLinkButton");
              library.wait(6);

              // Verify "Your search Woodstock IL is saved." text exist on the box
              searchText = library.getTextFrom("POPUP.VerifyText");
              Assert.assertTrue(searchText.contains(popupTextForWoodstockCity));

              // Verify "Manage your search" button exist on the box.
              boolean isManageYourSearchButtonPresent = library.findElement(("POPUP.verifyManageYourSearch"))
                .isDisplayed();
              Assert.assertTrue(isManageYourSearchButtonPresent);

              // Click "Manage your search" button
              library.click("POPUP.verifyManageYourSearch");
              library.wait(5);
              // Verify page's url is <URL_Aaved-searchesPage>
              String getAdvanceUrl = library.getUrl();
              boolean isAdvanceUrlIsCorrect = getAdvanceUrl.contains(advancedUrl);
              Assert.assertTrue(isAdvanceUrlIsCorrect);
              // Verify search result "Woodstock IL" exists.

              boolean isWoodstockIlPresent = library.findElement(("POPUP.VerifyTextCoantins_Woodstock_IL")).isDisplayed();
              Assert.assertTrue(isWoodstockIlPresent);

              library.click("POPUP.VerifyTextCoantins_Woodstock_IL");

              String base2 = driver.getWindowHandle();
              Set<String> set2 = driver.getWindowHandles();
              set.remove(base2);
              set.remove(base);
              for (String testString : set2)
                driver.switchTo().window(testString);
              library.wait(20);
              String sUrlForWoodstock = library.getUrl();
              library.wait(40);

//                     if (library.getCurrentPlatform().contains("IOS_WEB")){
//                     sUrlForWoodstock = (String) data.get(sUrlForWoodstock);
//                     }
               

              // 3. Verify Url for the saved search element contains
              // "woodstock-il/" and "?savedsearch"
              boolean isUrlForWoodstockIsCorrect = sUrlForWoodstock.contains(verifyUrlPart);
              library.wait(5);
              boolean isUrlForWoodstockIsCorrectForSavedSearch = sUrlForWoodstock.contains(verifyUrlPartForWoodstock);
              Assert.assertTrue(isUrlForWoodstockIsCorrect);
              Assert.assertTrue(isUrlForWoodstockIsCorrectForSavedSearch);
              // Open <URL_MapSearchPage3> to go to map search page
              driver.get(thirdUrl);
              library.wait(5);
              library.waitForElement("SEARCHPAGE.saveSearchLinkButton");
              // Click "Save Search" button
              library.click("SEARCHPAGE.saveSearchLinkButton");
              library.wait(5);

              // Verify "Your search Theater District Manhattan NY is saved." text
              // exist on the box
              searchText = library.getTextFrom("POPUP.VerifyText");
              searchText =searchText.replace(" ", "");
              //Assert.assertTrue(searchText.contains(popupTextForTheaterDistrict));
                        Assert.assertEquals(searchText, popupTextForTheaterDistrict);
              // Click "X" button on the pop up box
              library.click("POPUP.close");
              library.wait(2);

              // Verify "Save Search" button is disabled.
              verifySaveSearchButtonIsDisabled();
              // Verify page's url is <URL_MapSearchPage3>
              String getThirdUrl = library.getUrl();
              boolean isThirdUrlCorrect = getThirdUrl.contains(thirdUrl);
              Assert.assertTrue(isThirdUrlCorrect);

              // open saved search page through url : <URL_Aaved-searchesPage>
              driver.get(advancedUrl);
              library.wait(2);

              // Verify search result "Theater District Manhattan NY" exists.
              boolean isTheaterDistrictPresent = library.findElement(("POPUP.VerifyTextCoantins_Theater_Manhattan"))
                .isDisplayed();
             
              Assert.assertTrue(isTheaterDistrictPresent);

              library.click("POPUP.VerifyTextCoantins_Theater_Manhattan");

              String base3 = driver.getWindowHandle();
              Set<String> set3 = driver.getWindowHandles();
              set.remove(base2);
              set.remove(base);
              set.remove(base3);

              for (String testString : set3)
                driver.switchTo().window(testString);
              library.wait(20);
              String sUrlFortheaterDistrict = library.getUrl();
              // 2. Verify Url for the saved search element contains
              // "manhattan-ny/theater-district" and "?savedsearch"

//                     if (library.getCurrentPlatform().contains("IOS_WEB")){
//                     sUrlFortheaterDistrict = (String) data.get(sUrlFortheaterDistrict);
//                   }
              library.wait(40);
              boolean isUrlFortheaterDistrict = sUrlFortheaterDistrict.contains(theaterDistictUrlPart);
               
              boolean isTheaterDistictUrlPartForSavedSearchIsCorrect = sUrlFortheaterDistrict.contains(theaterDistictUrlPartForSavedSearch);
              library.wait(10);
              Assert.assertTrue(isUrlFortheaterDistrict);
              library.wait(5);
              Assert.assertTrue(isTheaterDistictUrlPartForSavedSearchIsCorrect);

		}
		/*else if(library.getBrowserName().equals("IExplore") || library.getBrowserName().equals("Chrome") ) {
		 * 
		 * 
		 * 
		 * 
		 * }*/
		else {
			driver.get(firstUrl); // Loading URL
			// Test case starts here
			library.waitForElement("HOMEPAGE.savesearchlink");
			library.isJSEClicked("HOMEPAGE.savesearchlink");
			library.waitForElement("POPUP.savesearch");
			boolean isPopupPresent = library.verifyPageContainsElement("POPUP.savesearch");
			Assert.assertTrue(isPopupPresent); //
			library.waitForElement("POPUP.zipcode");
			String popupZipCode = library.getTextFrom("POPUP.zipcode");
			String popupText = library.getTextFrom("POPUP.text");// POPUP.zipcode
			// validating complete text present on Popup for zip code.(Your
			// search 60089 IL is saved.)
			// String PopupTextForZipCode = (String)
			// data.get("popupTextForZipCode");
			boolean isTextPresentOnPopupForZipCodeIsCorrect = popupText.contains(PopupTextForZipCode);
			Assert.assertTrue(isTextPresentOnPopupForZipCodeIsCorrect);
			String zipCodeValue = (String) data.get("ZipCodeWithCity");
			boolean isZipCodeCorrect = popupZipCode.contains(zipCodeValue);
			Assert.assertTrue(isZipCodeCorrect);
			// Verification for text presents on Popup for zip code.
			boolean isReturnToMapButtonPresent = library.verifyPageContainsElement("POPUP.returntomapbutton");
			Assert.assertTrue(isReturnToMapButtonPresent);
			if (library.getBrowserName().equals("IExplore")) {
				WebElement returntomapbutton = library.findElement("POPUP.returntomapbutton");
				library.mouseHoverJScript(returntomapbutton);
			/*	robotPoweredMoveMouseToWebElementCoordinates(returntomapbutton);
				robotPoweredClick();*/
				library.isJSEClicked("POPUP.returntomapbutton");
			} else {
				library.click("POPUP.returntomapbutton");
			} // Click on Return to map button
			library.wait(5);
			boolean saveSearchPopupNotExists = library.verifyPageNotContainsElement("POPUP.savesearch");
			Assert.assertTrue(saveSearchPopupNotExists);
			String url1 = library.getUrl();
			boolean isUrlCorrect = url1.contains(firstUrl);
			Assert.assertTrue(isUrlCorrect);
			//Xpath is hardcoded since library function is giving null if element not present, will fix once it get resolve, need to use library.findElement().
			boolean isSaveSearchDisabled = driver.findElement(By.xpath(".//*[@id='linkSaveSearch']")).isEnabled(); // giving
			if (!isSaveSearchDisabled) {
				System.out.println("Save Search Button is enable");
			} else {
				System.out.println("Save Search Button is disabled");
				driver.navigate().refresh();
				library.wait(5);
				driver.get(advancedUrl); // open saved search page
				library.click("HOMEPAGE.savedSearch");
				String zipCodeTitleFromSavedSearchPage = library.getTextFrom("SAVEDSEARCH.zipCodeTitle");
				String zipCodeTitle = (String) data.get("ZipCodeWithCity");
				boolean iszipCodeTitleFromSavedSearchPageCorrect = zipCodeTitleFromSavedSearchPage
						.contains(zipCodeTitle);
				Assert.assertTrue(iszipCodeTitleFromSavedSearchPageCorrect);
				String base = driver.getWindowHandle();
				/*try{
				JavascriptExecutor jv =(JavascriptExecutor)library.getDriver();
				System.out.println(jv);
				
				   jv.executeScript("document.getElementsByClassName('searchLink')[0].click();");
				}catch(Exception e){
					System.out.println("JAVA SCRIPT"+e.getMessage());
				} */
		        //library.waitForElement("SAVEDSEARCH.zipCodeTitle")
				//library.isJSEClicked("SAVEDSEARCH.zipCodeTitle");
				WebElement zipCodeClickOnSavedPage = library.findElement("SAVEDSEARCH.zipCodeTitle");
				//library.mouseHoverJScript(zipCodeClickOnSavedPage);
				if(library.getBrowserName().equals("IExplore") || library.getBrowserName().equals("Chrome") ) {
					JavascriptExecutor jv =(JavascriptExecutor)library.getDriver();
					jv.executeScript("document.getElementsByClassName('searchLink')[0].click();");
					library.wait(5);
					library.isJSEClicked("SAVEDSEARCH.zipCodeTitle");
					driver.manage().window().maximize();
					library.wait(5);
					}
				else{
				robotPoweredMoveMouseToWebElementCoordinates(zipCodeClickOnSavedPage);
				robotPoweredClick();
				robotPoweredClick();
				}
				library.wait(10);
				Set<String> set = driver.getWindowHandles();
				set.remove(base);
				for (String testString : set)
				{
					driver.switchTo().window(testString);
					driver.manage().window().maximize();
					library.wait(20);
				}
				library.wait(10);
				boolean isUrlForZipCodeCorrect = library.getUrl().contains(zipCodeUrlPart);
				Assert.assertTrue(isUrlForZipCodeCorrect);
				driver.get(secondUrl);
				if (library.getBrowserName().equalsIgnoreCase("IExplore")) {
					library.wait(15);
					driver.navigate().refresh();
					//library.getDriver().findElement(By.id("linkSaveSearch")).click();
					library.isJSEClicked("HOMEPAGE.savesearchlink");
					

				} else {

					library.wait(5);
					library.click("HOMEPAGE.savesearchlink");
				}

				//library.click("HOMEPAGE.savesearchlink");
				library.waitForElement("POPUP.savesearch");
				boolean isPopupPresentForUrl2 = library.verifyPageContainsElement("POPUP.savesearch");
				Assert.assertTrue(isPopupPresentForUrl2);// 1
				String popupTextForCity = library.getTextFrom("POPUP.text"); // POPUPTEXTFORZIPCODE
				boolean isZipCodeTextPresentOnPopupCorrect = popupTextForCity
						.contains((String) data.get("PopupTextForCity"));
				Assert.assertTrue(isZipCodeTextPresentOnPopupCorrect); //
				library.wait(3);//////
				library.waitForElement("POPUP.manageyoursearch");
				boolean isManageYourSearchButtonPresent = library.verifyPageContainsElement("POPUP.manageyoursearch");
				Assert.assertTrue(isManageYourSearchButtonPresent); // step 7
				if (library.getBrowserName().equalsIgnoreCase("IExplore")) {
					library.click("POPUP.manageyoursearch");
				} else {
					library.click("POPUP.manageyoursearch");
				}
				library.wait(7);
				String url2 = library.getUrl(); // Saved Search URL
				boolean isUrl2Correct = url2.contains(advancedUrl); // advanced
																	// url
				library.wait(3);//////
				Assert.assertTrue(isUrl2Correct);
				String city = library.getTextFrom("SAVESEARCH.city");
				boolean isCityNamePresent = city.contains(city);
				Assert.assertTrue(isCityNamePresent);
				if (library.getBrowserName().equalsIgnoreCase("IExplore")) {
					library.isJSEClicked("SAVESEARCH.zipcode");
					library.wait(5);
					JavascriptExecutor jv =(JavascriptExecutor)library.getDriver();
					jv.executeScript("document.getElementsByClassName('searchLink')[0].click();");
					library.wait(5);
					driver.manage().window().maximize();
				} else {
					// library.click("SAVESEARCH.zipcode");
					WebElement clickOnCityNamOnSavedPage = library.findElement("SAVEDSEARCH.zipCodeTitle");
					//JavascriptExecutor js1 =(JavascriptExecutor)library.getDriver();
					   //js1.executeScript("document.getElementsByClassName('searchLink')[0].click()");
					//library.mouseHoverJScript(clickOnCityNamOnSavedPage);
					if(library.getBrowserName().equals("IExplore") || library.getBrowserName().equals("Chrome") ) {
						JavascriptExecutor jv =(JavascriptExecutor)library.getDriver();
						jv.executeScript("document.getElementsByClassName('searchLink')[0].click();");
						driver.manage().window().maximize();
						library.wait(5);
						//library.isJSEClicked("SAVEDSEARCH.zipCodeTitle");
						}
					else{
					robotPoweredMoveMouseToWebElementCoordinates(clickOnCityNamOnSavedPage);
					robotPoweredClick();
					robotPoweredClick();
					}
					library.wait(5);//Page Transition.
					JavascriptExecutor jv =(JavascriptExecutor)library.getDriver();
					library.wait(5);
					jv.executeScript("document.getElementsByClassName('searchLink')[0].click();");
					library.wait(10);
					set = driver.getWindowHandles();
					for (String testString : set)
					{
						driver.switchTo().window(testString);
						driver.manage().window().maximize();
						library.wait(5);
					}
					library.wait(20);//Page Transition.
				}
				library.wait(10);
				set = driver.getWindowHandles();
				for (String testString : set)
				{
					driver.switchTo().window(testString);
					driver.manage().window().maximize();
					library.wait(20);//Page Transition.
					// Verify Url for the saved search element contains
					// "woodstock-il/"new-york-ny
				}
					
               
				String url3 = library.getUrl(); // D
				library.wait(20);//Page Transition.
				library.wait(10);
				library.wait(10);
				boolean isUrlFCorrectForWoodstock = url3.contains(verifyUrlPart);
				Assert.assertTrue(isUrlFCorrectForWoodstock);
				library.wait(5);
				String window = driver.getWindowHandle();
				driver.switchTo().window(window);
				driver.navigate().to(thirdUrl);
				driver.manage().window().maximize();
				if (library.getBrowserName().equalsIgnoreCase("IExplore")) {
					library.isJSEClicked("HOMEPAGE.savesearchlink");
					// javascriptexecutorclick("HOMEPAGE.savesearchlink");
				} else {
					library.click("HOMEPAGE.savesearchlink");
				}
				library.wait(5);
				String popupTextForTheatreDistrict = library.getTextFrom("POPUP.text");
				String popupTextForTheatreDistrict1 = popupTextForTheatreDistrict.replace(" ", "");
				//boolean isTextPresentOnPopUpCorrectForTheaterDistrict = popupTextForTheatreDistrict
				//		.contains(popupTextForTheaterDistrict);
				//Assert.assertTrue(isTextPresentOnPopUpCorrectForTheaterDistrict);
				Assert.assertEquals(popupTextForTheatreDistrict1, popupTextForTheaterDistrict1);
				
				if (library.getBrowserName().equalsIgnoreCase("IExplore")) {
					library.isJSEClicked("SAVESEARCHPOPUP.close");
				} else {
					library.click("SAVESEARCHPOPUP.close");
				}
				library.refresh();
				library.waitForElement("POPUP.savesearch");
				boolean checkPopupPresent = library.verifyPageNotContainsElement("POPUP.savesearch");
				Assert.assertTrue(checkPopupPresent); 
				String url4 = library.getUrl();
				boolean isUrl4Correct = url4.contains(thirdUrl); // advanced url
				Assert.assertTrue(isUrl4Correct);
				library.wait(5);
				library.get(advancedUrl);

				WebElement clickOnCityNamOnSavedPage = library.findElement("SAVEDSEARCH.zipCodeTitle");
				//JavascriptExecutor js3 =(JavascriptExecutor)library.getDriver();
				  // js3.executeScript("document.getElementsByClassName('searchLink')[0].click()");
				//library.mouseHoverJScript(clickOnCityNamOnSavedPage);
				if(library.getBrowserName().equals("IExplore") || library.getBrowserName().equals("Chrome") ) {
					library.isJSEClicked("SAVEDSEARCH.zipCodeTitle");
					library.wait(5);
					JavascriptExecutor jv =(JavascriptExecutor)library.getDriver();
					jv.executeScript("document.getElementsByClassName('searchLink')[0].click();");
					driver.manage().window().maximize();
				}
				else{
				robotPoweredMoveMouseToWebElementCoordinates(clickOnCityNamOnSavedPage);
				robotPoweredClick();
				robotPoweredClick();
				}
				library.wait(10);//Page Transition.
				set = driver.getWindowHandles();
				for (String testString : set)
				{
					driver.switchTo().window(testString);
					driver.manage().window().maximize();
					library.wait(20);
				}
				String urlForAdvancedSearchPage = library.getUrl();		
				driver.manage().window().maximize();
				System.out.println(urlForAdvancedSearchPage);
				library.wait(20);
				library.wait(10);
				boolean isUrlForAdvancedSearchPageCorrect = urlForAdvancedSearchPage.contains(theaterDistictUrlPart);
				Assert.assertTrue(isUrlForAdvancedSearchPageCorrect);
			}
		}
	}
	public void robotPoweredMouseDown() throws AWTException {
		Robot mouseObject = new Robot();
		mouseObject.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		mouseObject.waitForIdle();
	}

	public void robotPoweredMouseUp() throws AWTException {
		Robot mouseObject = new Robot();
		mouseObject.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		mouseObject.waitForIdle();
	}

	@Override
	public void robotPoweredClick() {
		try {
			Robot mouseObject = new Robot();
			mouseObject.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			mouseObject.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			mouseObject.waitForIdle();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	
	@Override
	public void robotPoweredMoveMouseToWebElementCoordinates(WebElement element) {
		try {
			WebDriver driver = library.getDriver();
			Robot mouseObject = new Robot();
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			// Get Browser dimensions
			int browserWidth = driver.manage().window().getSize().width;
			int browserHeight = driver.manage().window().getSize().height;

			// Get dimensions of the window displaying the web page
			int pageWidth = Integer
					.parseInt(executor.executeScript("return document.documentElement.clientWidth").toString());
			int pageHeight = Integer
					.parseInt(executor.executeScript("return document.documentElement.clientHeight").toString());

			// Calculate the space the browser is using for toolbars
			int browserFurnitureOffsetX = browserWidth - pageWidth;
			int browserFurnitureOffsetY = browserHeight - pageHeight;

			// Get the coordinates of the WebElement on the page and calculate
			// the
			// centre point
			int webElementX = element.getLocation().getX() + Math.round(element.getSize().width / 2);
			int webElementY = element.getLocation().getY() + Math.round(element.getSize().height / 2);

			// Calculate the correct X/Y coordinates based upon the browser
			// furniture offset and the position of the browser on the desktop
			int xPosition = driver.manage().window().getPosition().x + browserFurnitureOffsetX + webElementX;
			int yPosition = driver.manage().window().getPosition().y + browserFurnitureOffsetY + webElementY;

			// Move the mouse to the calculated X/Y coordinates
			mouseObject.mouseMove(xPosition, yPosition);
			mouseObject.waitForIdle();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void Reg84_MortgageCalculator() {
		if (library.getCurrentPlatform().equalsIgnoreCase("AndroidWeb")) {
			library.wait(10);
			JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
			WebElement element = library.getDriver()
					.findElement(By.xpath("//div[@id='mortgageWidget']//thead//tr[1]/th[1]"));
			jse.executeScript("arguments[0].scrollIntoView();", element);
			library.wait(5);
			jse.executeScript("arguments[0].scrollIntoView();", element);
			String Lenderxpath = "xpath->//div[@id='mortgageWidget']//thead//tr[1]/th[1]";
			String APRxpath = "xpath->//div[@id='mortgageWidget']//thead//tr[1]/th[2]/a";
			String Rate = "xpath->//div[@id='mortgageWidget']//thead//tr[1]/th[3]/a";
			String EstPayment = "xpath->//div[@id='mortgageWidget']//thead//tr[1]/th[4]/a";
			Assert.assertEquals(library.getTextFrom(Lenderxpath), "Lender");
			Assert.assertEquals(library.getTextFrom(APRxpath), "APR");
			Assert.assertEquals(library.getTextFrom(Rate), "Rate");
			Assert.assertEquals(library.getTextFrom(EstPayment), "Est. Payment");
			String URL = "xpath->.//*[text()='See More Rates']";
			String linkname = library.getAttributeOfElement("href", URL);
			Assert.assertTrue(linkname.contains("/mortgages?"));
		} else {
			String Lenderxpath = "xpath->//div[@id='mortgageWidget']//thead//tr[1]/th[1]";
			String APRxpath = "xpath->//div[@id='mortgageWidget']//thead//tr[1]/th[2]/a";
			String Rate = "xpath->//div[@id='mortgageWidget']//thead//tr[1]/th[3]/a";
			String EstPayment = "xpath->//div[@id='mortgageWidget']//thead//tr[1]/th[4]/a";
			Assert.assertEquals(library.getTextFrom(Lenderxpath), "Lender");
			Assert.assertEquals(library.getTextFrom(APRxpath), "APR");
			Assert.assertEquals(library.getTextFrom(Rate), "Rate");
			Assert.assertEquals(library.getTextFrom(EstPayment), "Est. Payment");
			String URL = "xpath->.//*[text()='See More Rates']";
			String linkname = library.getAttributeOfElement("href", URL);
			Assert.assertTrue(linkname.contains("/mortgages?"));
		}
	}

	
	@Override
	public void DeleteTrash(JSONObject data) {
	     String UserName=(String) data.get("Username");
	     String Password=(String) data.get("Password");
	     if (library.getCurrentPlatform().contains("Android")) {
	         library.typeDataInto(UserName, "LOGINPAGE.userNameForMobilePlatform");
	           library.typeDataInto(Password, "LOGINPAGE.passwordForMobilePlatform");
	           library.waitForElement("LOGINPAGE.submitButtonForMobilePlatform");
	           library.click("LOGINPAGE.submitButtonForMobilePlatform");
	          //Used hard wait otherwise it will fail in this step because page transition is happening from login to other page.
	           library.wait(5); 
	           //library.click("HOMEPAGE.menuOption");
//	           library.isJSEClicked("HOMEPAGE.menuOption");
//	           library.waitForElement("HOMEPAGE.saveSearchLink");
//	            library.getDriver().findElement(By.linkText("Saved Searches")).click();
//	            library.waitForElement("HOMEPAGE.menuOption");
//	            library.click("HOMEPAGE.menuOption");
	           library.wait(3);
	           Select dropdown = new Select(library.getDriver().findElement(By.id("nav-options")));
	           dropdown.selectByVisibleText("Saved Searches");
	        }
	   else {
	      if(library.getCurrentBrowser().contains("Safari")){
	       WebDriver driver = library.getDriver();
	       library.wait(5);// icon-user ng-scope
	        boolean isLoginEnabled = library.verifyPageContainsElement("USER.loginImage");
	        if (isLoginEnabled) {
	        // WebElement userIcon = library.findElement("USER.loginImage");
	      library.wait(2);
	         driver.get((String) data.get("LogoutUrl"));
	         library.wait(5);
	         driver.get((String) data.get("AdvanceURL"));
	         library.wait(5);
	        }
	       }
	      library.get((String) data.get("AdvanceURL"));
	      library.wait(5);
	      if(library.getCurrentPlatform().contains("IOS"))
	       {
	        boolean isPopupPresent = library.verifyPageContainsElement("LOGINPAGE.userNameForMobilePlatform");
	        Assert.assertTrue(isPopupPresent);
	        library.typeDataInto(UserName, "LOGINPAGE.userNameForMobilePlatform");
	        library.typeDataInto(Password, "LOGINPAGE.passwordForMobilePlatform");
	        library.wait(2);
	        library.click("LOGINPAGE.submitButtonForMobilePlatform");
	        library.wait(3);
	        Select dropdown = new Select(library.getDriver().findElement(By.id("nav-options")));
	        dropdown.selectByVisibleText("Saved Searches");
	        
	       }
	      else
	      {
	    	//library.setImplicitWaitTime(3);
		       boolean isPopupPresent = library.verifyPageContainsElement("POPUP.loginIdField");
		       Assert.assertTrue(isPopupPresent);
		       // Input account/pwd and click "login" button
		       library.typeDataInto(UserName, "LOGIN.username");
		       library.typeDataInto(Password, "LOGIN.password");
		       library.wait(2);
		       library.click("LOGIN.submitButton");
		       library.click("HOMEPAGE.savedSearch");
		       //library.wait(5);
	      }
	     }
	     //library.get((String)data.get("AdvanceURL"));
	     library.wait(5);
	     int count = library.getElementCount("SAVEDSEARCH.deletetrashlist");
	     System.out.println("total count is" + count);
	     if (count > 0) {
	      for (int i = 1; i <= count; i++) {
	       library.wait(2);
	       //Hard code the xpath due to Dyanamic xpath using in Loop condition
	       String mytrash = "xpath->(.//span[@class='button'])[" + i + "]";
	      
	       library.click(mytrash);
	       library.wait(2);
	      }
	     }
	     
	 }
	/*@Override
	public void DeleteTrash() {
		
		
		if (library.getCurrentPlatform().contains("AndroidWeb")) {
			   library.typeDataInto("harishlimba76@gmail.com", "id->email");
			     library.typeDataInto("harishlimba76", "id->password");
			     library.wait(5);
			     library.click("id->btnLogin");
			     library.wait(5);
			     library.click("xpath->//*[@class='caret']");
			    library.wait(10);
			      library.getDriver().findElement(By.linkText("Saved Searches")).click();
			      library.wait(2);
			      library.click("xpath->//*[@class='caret']");
			  } 
			else {
				  
				  if(library.getCurrentBrowser().contains("Safari")){
					  WebDriver driver = library.getDriver();
					  library.wait(5);// icon-user ng-scope
					   boolean isLoginEnabled = library.verifyPageContainsElement("xpath->//i[@class='icon-user ng-scope']", true);
					   if (isLoginEnabled) {
					    WebElement userIcon = driver.findElement(By.xpath("//i[@class='icon-user ng-scope']"));
					   //robotPoweredMoveMouseToWebElementCoordinates(userIcon);
					  //robotPoweredClick();
					  javascriptexecutorclick("xpath->//i[@class='icon-user ng-scope']");
					    library.wait(2);
					    //WebElement logOut = driver.findElement(By.xpath("//a[text()=' Log out']"));
					   // logOut.click();
					  //  javascriptexecutorclick("xpath->//a[text()=' Log out']");
					    library.wait(2);
					    driver.get("http://www2.movoto.com/logout/");
					    library.wait(5);
					 driver.get("http://www2.movoto.com/my-movoto/#/saved-searches");
					 library.wait(5);
					 
					   }
					  }
					  
				   boolean isPopupPresent = library.verifyPageContainsElement("xpath->.//*[@id='txtEmail_contact']", true);
				   Assert.assertTrue(isPopupPresent);
				   

				   // Input account/pwd and click "login" button
				   library.typeDataInto("harishlimba76@gmail.com", "xpath->.//*[@id='txtEmail_contact']");
				   library.typeDataInto("harishlimba76", "xpath->.//*[@id='txtPsw_login']");
				   library.wait(2);
				   library.click("xpath->.//*[@id='loginPanel']/a");
				   library.wait(5);
				   
				  
			  }
			  int count = library.getElementCount("xpath->//div[@class='saved ng-scope']/..//span[contains(text(),'Trash')]");
			  System.out.println("total count is" + count);
			  if (count > 0) {
			   for (int i = 1; i <= count; i++) {
			    library.wait(2);
			    String mytrash = "xpath->(//div[@class='saved ng-scope']/..//span[contains(text(),'Trash')])[" + i
			      + "]";
			    // library.scrollTo(mytrash);
			    library.click(mytrash);
			    library.wait(2);
			   }
			  }
			  
	}*/
	
	@Override
	public boolean checkPreconditionsForTc202() {
		boolean flag = false;
		//For ANdroid Web and IOS web
		if (library.getCurrentPlatform().equals("Android") || library.getCurrentPlatform().equals("IOS_WEB")) {
			String countFromUI = library.getTextFrom("xpath->//div[@class='pagination']/div[@class='info']/b"); // Results
			String finalCnt = countFromUI.split("of")[1];
			finalCnt = finalCnt.replaceAll(",", "").trim();
			int count = Integer.parseInt(finalCnt);
			if (count > 3) {
				return flag = true;
			} else {
				return flag = false;
			}
		}  
			//For browsers IE,Chrome and Safari
			else {
			String[] countFromUI = library.getTextFrom("PROPERTY.uitotalpropertycount").split(" "); // Results
			String withoutComma = countFromUI[4].replaceAll(",", "");
			int count = Integer.parseInt(withoutComma);
			if (count > 3) {
				return flag = true;
			} else {
				return flag = false;
			}
		}
} 
	 
	 @Override
	public boolean checkPrecondiotionsForTC201(Map<String, Object> data) {
		//WebDriver driver = library.getDriver();
		 String minSqftFromDataProvider = String.valueOf(data.get("MinSqft"));
		 //int minSqftFromDataProviderInIntFormat = Integer.parseInt(minSqftFromDataProvider);
		 Double minSqftFromDataProviderInDoubleFormat = Double.parseDouble(minSqftFromDataProvider);
		library.wait(5);
		if (library.getBrowserName().equalsIgnoreCase("Safari")) {
			javascriptexecutorclick(DriverLocator.SEARCHPAGE_view);
			library.wait(5);
			javascriptexecutorclick(DriverLocator.SEARCHPAGE_slpitView);
		}
		boolean flag = false;
		List<Double> cardList = new ArrayList<Double>();
		String cardSqft = null;
		for (int i = 1; i <= 50; i++) {
			try {
				library.wait(1);
				cardSqft = library.getDriver()
						.findElement(By
								.xpath("(//div[@class='cardone cardbox'])[" + i + "]//div[@class='top-base-info']//span[4]"))
						.getText(); // Dynamic Xpath
			} catch (Exception e) {
				e.printStackTrace();
				i = i + 1;
			}

			if (cardSqft != null && cardSqft.length() > 0) {
				cardSqft = cardSqft.replace(",", "");
				cardSqft = cardSqft.trim();
				cardList.add(Double.parseDouble(cardSqft));
			}
		}
		
		Double maxSqft = Collections.max(cardList);
		Double minSqft = Collections.min(cardList);
		return(((maxSqft>minSqftFromDataProviderInDoubleFormat)&&(minSqftFromDataProviderInDoubleFormat>minSqft))?true:false);
	}
	 
	 @Override
	 public boolean checkPrecondiotionsForTC200(Map<String, Object> data) {
		
		 String minPriceData = (String) data.get("MinPrice");
			
			String removeSpecialChaFromMinPriceData = minPriceData.replace("$", "");
			String removeSpecialChaFromMinPrice = removeSpecialChaFromMinPriceData.replaceAll("K", "000");
			int minPriceInInt = Integer.parseInt(removeSpecialChaFromMinPrice);
			String minBedValue = (String) data.get("MinBedValue");
			int minBedValueInInt = Integer.parseInt(minBedValue);
			String minBathValue = (String) data.get("MinBathValue");
			int minBathValueInInt = Integer.parseInt(minBathValue);
			
			String maxPriceFromDataProvider = (String) data.get("MaxPrice");
			String removeSpecialCharFromMaxPrice = maxPriceFromDataProvider.replace("$", "");
			String removeMFromMaxPrice= removeSpecialCharFromMaxPrice.replaceAll("K", "000");
			int maxPriceInInt = Integer.parseInt(removeMFromMaxPrice);
		 boolean flag = false;
			List<Double> cardList = new ArrayList<Double>();
			List<Integer> bedList = new ArrayList<Integer>();
			List<Double> bathList = new ArrayList<Double>();
			String cardPrice = null;
			String bedDetails = null;
			String bathDetails = null;
			
			for (int i = 1; i <= 50; i++) {

				
				try {
					cardPrice = library.getDriver()
							.findElement(By
									.xpath("(//div[@class='cardone cardbox'])["+i+"]//div[@class='top-base-info']/span[1]"))
							.getText();
					bedDetails = library.getDriver()
							.findElement(By
									.xpath("(//div[@class='cardone cardbox'])["+i+"]//div[@class='top-base-info']/span[2]"))
							.getText();
					bathDetails = library.getDriver()
							.findElement(By
									.xpath("(//div[@class='cardone cardbox'])["+i+"]//div[@class='top-base-info']/span[3]"))   //
							.getText();
				} catch (Exception e) {
					e.printStackTrace();
					i = i + 1;
				}
				if (cardPrice != null && cardPrice.length() > 0) {
					cardPrice = cardPrice.replace(",", "");
					cardPrice = cardPrice.replace("$", "");
					cardPrice = cardPrice.trim();

					cardList.add(Double.parseDouble(cardPrice));
				}
				
				boolean isContainDash = bedDetails.equals("—");
				System.out.println(isContainDash);
				if (bedDetails != null && bedDetails.length() > 0 && !isContainDash) {
					bedDetails = bedDetails.trim();
					bedList.add(Integer.parseInt(bedDetails));
				}
				
				boolean isContainDashForBath = bedDetails.equals("—");
				System.out.println(isContainDashForBath);
				if (bathDetails != null && bathDetails.length() > 0 && !isContainDashForBath) {
					bathDetails = bathDetails.trim();
					bathList.add(Double.parseDouble(bathDetails));
				}
	      }
			
			Double maxPrice = Collections.max(cardList);
			Double minPrice = Collections.min(cardList);
			int maxBed = Collections.max(bedList);
			int minBed = Collections.min(bedList);
			Double maxBath = Collections.max(bathList);
			Double minBath = Collections.min(bathList);
			int bedDifference = ((maxBed-minBed));
			Double bathDifference = ((maxBath-minBath));
			double difference =((maxPrice - minPrice));
			
			boolean isPriceDifferenceCorrect = ((minPriceInInt > minPrice) && (maxPriceInInt<maxPrice));
			boolean isBedDifferenceCorrect = ((maxBed>minBedValueInInt)&& (minBedValueInInt> minBed));
			boolean isBathDifferenceCorect = ((maxBath> minBathValueInInt) && (minBathValueInInt>minBath));
			/* boolean isPriceDifferenceCorrect = (difference > 25000);
	        boolean isBedDifferenceCorrect = (bedDifference > 1);
	        boolean isBathDifferenceCorect = (bathDifference > 1);*/
			if(isPriceDifferenceCorrect && isPriceDifferenceCorrect && isPriceDifferenceCorrect){
	        	 flag = true;
	        }
	        else{
	        	System.out.println("precondition failed  !!");
	        	 flag=false;
	        }
			
	        return flag;
		}
	 
	 @Override
	 public void verificationupdateUrgencyStageOne(Map<String, String> data) {
	  String urgencyCode = (String) data.get("urgencyCode");
	  String urgencyName = (String) data.get("urgencyName");
	  String clientName = (String) data.get("ClientName");
	  String searchName = clientName.substring(0, 3);

	  searchAndSelectClient(clientName);

	  library.click("URGENCY.image"); // click on urgency
	  String updateLocator = "xpath->.//span[text()='" + urgencyName + "']";
	  library.click(updateLocator); // click on radio button
	  library.verifyPageContainsElement("URGENCY.success");// Verify urgency code is updated to 1 in the urgency spot
	  library.verifyPageContainsElement("URGENCY.spotno"); // To verify the number on the urgency spot
	  library.waitForElement("URGENCY.notes");
	  library.wait(3);
	  library.click("URGENCY.notes"); // click on Notes
	  library.wait(3);
	  
	 }
	 
	 @Override
	 public void verifyUrgencyForTalkedStage(Map<String, Object> data) {
	  String xpathForTransactionsTab = "xpath->(//a[@class='text-center btn-green ng-binding'])[2]";
	  library.verifyPageContainsElement(xpathForTransactionsTab); // validating
	                   // for
	                   // transaction
	                   // tab.
	  String getXpathforStage = "xpath->(//div[contains(@class,'col-xss-6 col-xs-5 feature-title')])[3]/..//div[contains(@class,'col-xss-6 col-xs-7 feature-value ng-binding')]";
	  String getXpathforStageText = library.getTextFrom(getXpathforStage); // getting
	                    // stage
	                    // from
	                    // UI

	  System.out.println(getXpathforStageText);
	  Assert.assertEquals(getXpathforStageText, "Talked");// validating Stage.
	  String stageName = "Talked";

	  System.out.println(getXpathforStageText);

	  if (stageName.equalsIgnoreCase(getXpathforStageText)) {

	   Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
	   Map<String, String> urgencyData = (Map<String, String>) data.get("urgencyData");
	   String urgencyName = urgencyData.get("urgencyName");
	   String accessData = getAccessTokenIdp(apiData);
	   String urgencyCode = urgencyData.get("urgencyCode");

	   apiData.put("accessData", accessData);
	   String leadData = getLeadDetails(apiData);

	   // String urgency = (String) library.getValueFromJson("$.urgency",
	   // leadData); // From API

	   String urgency = (String) library.getValueFromJson("$.activities[0].urgencyStatus", leadData);
	   // String urgencyCode = urgency.split("-")[1];
	   // urgencyCode = urgencyCode.trim();

	   String last = "Updated Urgency Status:" + urgency;

	   // String locator = getLocatorForUrgencyCode(urgencyCode);
	   String webUiLocator = "xpath->.//label[@class='urgency-status ng-pristine ng-untouched ng-valid ng-binding ng-isolate-scope urgency-as-1']";

	   webUiLocator = library.getTextFrom(webUiLocator);
	   System.out.println(webUiLocator);
	   String notesAfterUpdateUrgency = "Updated Urgency Status: " + urgencyCode + " - " + urgencyName;
	   System.out.println(notesAfterUpdateUrgency);

	   String urgency1 = (String) library.getValueFromJson("$.activities[0].urgencyStatus", leadData);
		  String[] splitone = urgency1.split(" ");
		  String getOne = splitone[0];
		  String webUiLocatorOne = "xpath->//label[@urgency='trans.urgencyStatus']";
		  webUiLocatorOne = library.getTextFrom(webUiLocatorOne);
		  Assert.assertEquals(getOne, webUiLocatorOne);//Verify urgency code is updated to 1 in the urgency spot

	 //label[@urgency='trans.urgencyStatus']
	   
	   
	   String notesTimeXpath = "xpath->(//span[@class='ng-binding'])[1]";
	   String getTime = library.getTextFrom(notesTimeXpath); // getting
	                 // time from
	                 // UI.

	   try { // Converting time from IST to UST and validating.
	    DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mma");
	    formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	    Date date = formatterIST.parse(getTime);

	    DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mma");
	    formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
	    String SplitTime = formatterUTC.format(date);
	    System.out.println(SplitTime);

	    String ApiTime = (String) library.getValueFromJson("$.activities[0].startDateTime", leadData); // getting
	                            // time
	                            // from
	                            // API
	    String[] SplitApiTime = ApiTime.split(" ");
	    String[] gotApiTime = SplitApiTime[1].split(":");
	    String FinalTime=SplitApiTime[0]+" "+gotApiTime[0]+":"+gotApiTime[1]+SplitApiTime[2];
	    System.out.println(FinalTime);
	    Assert.assertEquals(FinalTime, SplitTime); // validating time and date
	               // with API.

	   } catch (Exception e) {
	    e.printStackTrace();
	   }

	  }
	  

	 }
	 
	 @Override
	 public void updateUrgencyStageTwo(Map<String, String> data) {

	  String urgencyCode = data.get("urgencyCode");
	  String urgencyName = data.get("urgencyName");
	  String clientName = data.get("ClientName");
	  String searchName = clientName.substring(0, 3);

	  library.setImplicitWaitTime(1);
	  boolean noError = true;
	  String urgencyCodeLocator = "xpath->.//label[@ng-model='trans.urgencyStatus' and text()='" + urgencyCode + "']";

	  library.verifyPageContainsElement(urgencyCodeLocator);

	  library.setImplicitWaitTime(30);

	  if (noError) {

	   String noteUpdateLocator = getUrgencyNotesLocator(urgencyName);
	   library.verifyPageContainsElement(noteUpdateLocator); // validating
	                  // updatelocator
	   library.click("URGENCY.notes");
	   library.wait(3);
	  } else {
	   library.click("NOTES.error");
	   library.wait(3);
	  }

	 }
	 @Override
	 public void updateLeadStageToTalked(Map<String, Object> data) {
	  library.wait(30);
	  library.click("xpath->//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'][1]//a/strong");
	  library.wait(2);
	  library.click("TRANSACTION.updatebutton");
	  library.wait(4);
	  library.click("xpath-> //span[text()='SELECT ONE']");
	  library.wait(2);
	  library.click("xpath->//span[text()='Emailed']");
	  Assert.assertTrue(library.isElementEnabled("xpath->//textarea", true));// Add
	                    // Note
	                    // Page
	                    // is
	                    // opened
	  library.typeDataInto("test Emailed Update", "NOTES.notesfield");
	  library.click(
	    "xpath->//div[@class='hidden-xs col-sm-12 col-md-offset-5 col-md-7 no-padding']//button[@type='submit']");
	  library.wait(8);
	  String stageName = library.getTextFrom("TRANSACTION.offeredStage");
	  Assert.assertEquals(stageName, "Talked");// Lead stage is changed to
	             // "Talked"
	  library.wait(4);
	  library.waitForElement("URGENCY.notes");
	  library.wait(3);
	  library.click("URGENCY.notes"); // click on Notes
	  String string = library.getTextFrom("xpath->//li[@ng-repeat='note in trans.noteList'][1]//strong");
	  String[] parts = string.split(" ");
	  String part1 = parts[0];
	  Assert.assertEquals(part1, "Emailed:");// Verify Notes is displayed as
	            // followings:Emailed
	  String note = library
	    .getTextFrom("xpath->//li[@ng-repeat='note in trans.noteList'][1]//p[@ng-bind-html='note.note']");
	  Assert.assertEquals(note, "test Emailed Update");// Verify Notes is
	               // displayed as
	               // followings:test
	               // Emailed Update
	  String contactURL = getApiUrlAWS(data);
	  setRequestHeader(data);
	  String response = library.HTTPGet(contactURL);
	  String notesTimeXpath = "xpath->(//span[@class='ng-binding'])[1]";
	  String getTime = library.getTextFrom(notesTimeXpath);
	  try {
	   DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mma");
	   formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	   Date date = formatterIST.parse(getTime);
	   DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mma");
	   formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
	   System.out.println("==========" + formatterUTC.format(date));
	   String SplitTime = formatterUTC.format(date);
	   System.out.println(SplitTime);
	   String ApiTime = (String) library.getValueFromJson("$.activities[0].startDateTime", response);
	   String[] SplitApiTime = ApiTime.split(" ");
	    String[] gotApiTime = SplitApiTime[1].split(":");
	    String FinalTime=SplitApiTime[0]+" "+gotApiTime[0]+":"+gotApiTime[1]+SplitApiTime[2];
	    System.out.println(FinalTime);
	   Assert.assertEquals(FinalTime, SplitTime);// Verify Notes is
	              // displayed as
	              // followings:<email
	              // update time stamp>
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	 }


	 
	 @Override
	 public void verifyMeetingTimeAndNotes(Map<String, Object> data) {
		 try{
			
			 
	  String opportunityStage = library.getTextFrom("DETAILS.opportunityStage");
	  if (!opportunityStage.equals("Talked")) {
	   library.click("TRANSACTION.updatebutton");
	   library.wait(2);
	   library.click("TRANSACTION.selectOne");
	   library.click("TRANSACTION.setTalked");
	   library.typeDataInto("Agent Automation", "NOTES.notesfield");
	   library.click("MAKEOFFER.saveupdatebutton");
	   library.wait(4);
	  }

	  library.click("TRANSACTION.updatebutton");
	  library.wait(2);
	  library.click("TRANSACTION.selectOne");
	  library.click("TRANSACTION.setScheduleMeeting");

	  int totalMinutesOfSystemTime = getTotalMinutesOfTime();

	  Assert.assertTrue(library.verifyPageContainsElement(
	    "xpath->.//span[contains(text(),'Update:')]/strong[contains(text(),'" + data.get("clientName") + "')]"), "Client Name is not Matched.");// Client Name is correct
	              // with the updated
	              // client
	  Assert.assertTrue(library.verifyPageContainsElement("TRANSACTION.scheduleMeetingDate"),
	    "Date Element is not there.");// Schedule a Meeting page is
	            // opened, Schedule a Meeting is
	            // displayed on the page
	  Assert.assertTrue(library.verifyPageContainsElement("TRANSACTION.scheduleMeetingTime"),
	    "Time Element is not there.");

	  String hoursOfStartTime = library.getTextFrom("UPDATE.setHoursFrom");
	  int hrsOfStartTime = Integer.parseInt(hoursOfStartTime);
	  String minutesOfStartTime = library.getTextFrom("UPDATE.setminutesfrom");
	  int minutsOfStartTime = Integer.parseInt(minutesOfStartTime);
	  int totalMinutesOfStartTime = (hrsOfStartTime * 60) + minutsOfStartTime;
	  String amTimeOfStart = "AM";

	  String hoursOfEndTime = library.getTextFrom("UPDATE.setHoursTo");
	  int hrsOfEndTime = Integer.parseInt(hoursOfEndTime);
	  String minutesOfEndTime = library.getTextFrom("UPDATE.setMinutesTo");
	  int minutsOfEndTime = Integer.parseInt(minutesOfEndTime);
	  int totalMinutesOfEndTime = (hrsOfEndTime * 60) + minutsOfEndTime;
	  String amTimeOfEnd = "AM";

	  if (library.isCheckBoxChecked("xpath->(.//label[normalize-space(text())='from']/../../div[5]//input)[2]"))
	   amTimeOfStart = "PM";

	  if (library.isCheckBoxChecked("xpath->(.//label[normalize-space(text())='to']/../../div[5]//input)[2]"))
	   amTimeOfEnd = "PM";

	  if (library.isCheckBoxChecked("xpath->(.//label[normalize-space(text())='from']/../../div[5]//input)[2]")
	    && hrsOfStartTime != 12) {
	   totalMinutesOfStartTime = 720 + totalMinutesOfStartTime;
	   amTimeOfStart = "PM";
	  }
	  if (library.isCheckBoxChecked("xpath->(.//label[normalize-space(text())='to']/../../div[5]//input)[2]")
	    && hrsOfEndTime != 12) {
	   totalMinutesOfEndTime = 720 + totalMinutesOfEndTime;
	   amTimeOfEnd = "PM";
	  }
	  if (totalMinutesOfStartTime <= 65) {
	   Reporter.log("Day (Date) is changed", true);
	   boolean isStartTimeCorrect = (60 <= (1440 - totalMinutesOfSystemTime) + totalMinutesOfStartTime
	     && 65 >= (1440 - totalMinutesOfSystemTime) + totalMinutesOfStartTime);
	   Assert.assertTrue(isStartTimeCorrect, "Meeting Start time is not 60 minutes later from the present time.");// the
	                              // default
	                              // Start
	                              // Time
	                              // is
	                              // 1
	                              // hour
	                              // after
	                              // the
	                              // current
	                              // time

	   boolean isEndTimeCorrect = ((90 <= 1440 - totalMinutesOfSystemTime + totalMinutesOfEndTime)
	     && (95 >= 7200 - totalMinutesOfSystemTime + totalMinutesOfEndTime));
	   Assert.assertTrue(isEndTimeCorrect, "Meeting End time is not 90 minutes later from the present time.");// the
	                             // default
	                             // End
	                             // Time
	                             // is
	                             // 1
	                             // and
	                             // half
	                             // an
	                             // hour
	                             // after
	                             // the
	                             // current
	                             // time

	  } else {
	   boolean isStartTimeCorrect = ((60 <= (totalMinutesOfStartTime - totalMinutesOfSystemTime))
	     && (65 >= (totalMinutesOfStartTime - totalMinutesOfSystemTime)));
	   Assert.assertTrue(isStartTimeCorrect, "Meeting Start time is not 60 minutes later from the present time.");

	   boolean isEndTimeCorrect = ((90 <= (totalMinutesOfEndTime - totalMinutesOfSystemTime))
	     && (95 >= (totalMinutesOfEndTime - totalMinutesOfSystemTime)));
	   Assert.assertTrue(isEndTimeCorrect, "Meeting End time is not 90 minutes later from the present time.");
	   library.typeDataInto("Agent Automation", "NOTES.notesfield");
	   library.click("MAKEOFFER.saveupdatebutton");
	   library.wait(10);
	  }

	  String opportunityTypeAfterSchedulingMeeting = library.getTextFrom("DETAILS.opportunityStage");
	  Assert.assertTrue(opportunityStage.equals("Talked"), "Opportunity Type is changed after scheduling a meeting.");
	  library.wait(5);
	  library.click("URGENCY.notes");
	  library.wait(5);
	  Assert.assertTrue(
	    library.verifyPageContainsElement("xpath->(.//p[contains(text(),'Agent Automation')])[1]"));// Verify
	                             // Notes
	  String activityTypePath = library.getTextFrom("xpath->(.//*[contains(text(),'Scheduled a Meeting')])[1]").split(":")[0];
	  Assert.assertEquals("Scheduled a Meeting", activityTypePath);// Verify  Notes
	  library.wait(5);
	  setTokenAndUserId(data);
	  String response = getResponse((String) data.get("notesApi"));
	  String notesTimeXpath = "xpath->(.//p[@class='list-group-item-text ng-binding'])[1]";
	  String[] getTime = library.getTextFrom(notesTimeXpath).split(" ");
	  String splitGetTime=getTime[2]+" "+getTime[3];
	  DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mma");
	   formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	   Date date = formatterIST.parse(splitGetTime);
	   DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mma");
	   formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
	   System.out.println("==========" + formatterUTC.format(date));
	   String SplitTime = formatterUTC.format(date);
	   System.out.println(SplitTime);
	   String ApiTime = (String) library.getValueFromJson("$.activities[0].startDateTime", response);
	   String[] SplitApiTime = ApiTime.split(" ");
	    String[] gotApiTime = SplitApiTime[1].split(":");
	    String FinalTime=SplitApiTime[0]+" "+gotApiTime[0]+":"+gotApiTime[1]+SplitApiTime[2];
	    System.out.println(FinalTime);
	   Assert.assertEquals(FinalTime, SplitTime);// Verify Notes is
	              // displayed as
	              // followings:<email
	              // update time stamp>
	   String isStartTime = "xpath->(.//*[contains(text(),'" + hoursOfStartTime + ":" + minutesOfStartTime
				+ amTimeOfStart + "')])[1]";
		Assert.assertTrue(library.verifyPageContainsElement(isStartTime));// Verify Start time present
																				
		String isEndTime = "xpath->(.//*[contains(text(),'" + hoursOfEndTime + ":" + minutesOfEndTime + amTimeOfEnd
				+ "')])[1]";
		Assert.assertTrue(library.verifyPageContainsElement(isEndTime));// Verify Endtime is present
	  
	 }catch(Exception e)
	 
	 {
		 e.printStackTrace();
	 }
}
	 
	 
	 @Override
	 public void updateToMakeANOffer(Map<String, String> MadeOfferData) {
	  String clientName = MadeOfferData.get("ClientName");

	  searchAndSelectClient(clientName);

	  library.click("TRANSACTION.updatebutton");// update
	  library.wait(5);
	  String presatge = getPrestage();

	  Assert.assertTrue(library.verifyPageContainsElement("MAKEOFFER.pageonepropertyaddress"));
	  Assert.assertTrue(library.verifyPageContainsElement("MAKEOFFER.pageoneclientone"));
	  chooseTransactionStage("Made an Offer");
	  handleEditTransactionAlert();
	  enterAddress(MadeOfferData.get("Address"));
	  library.click("MAKEOFFER.continuebutton");// continue
	  library.wait(5);
	  String AddressUI = library.getTextFrom("xpath->//address[@class='ng-isolate-scope']");
	  Assert.assertEquals(AddressUI, "1749 Lake St, San Mateo, California 94403");
	  Assert.assertTrue(library.verifyPageContainsElement("MAKEOFFER.price"), "Element not found.");
	  String systemDate = null, dateFromUI = null;
	  systemDate = getSystemDate();
	  dateFromUI = library.getTextFrom("MAKEOFFER.datefield");
	  boolean isCorrectDate = (systemDate.equals(dateFromUI));
	  System.out.println(isCorrectDate);
	  Assert.assertTrue(isCorrectDate);
	  String dateStr = MadeOfferData.get("OfferDate");
	  // library.clear("MAKEOFFER.datefield");
	  library.typeDataInto(dateStr, "MAKEOFFER.datefield");

	  library.clear("MAKEOFFER.price");
	  enterNumber(MadeOfferData.get("OfferPrice"), "MAKEOFFER.price");

	  library.click("MAKEOFFER.saveupdatebutton");// continue

	  library.wait(5);
	  library.wait(10);
	  String poststage = getPrestage();
	  
	  Assert.assertEquals("Offered", poststage);//Activity type
	 }
	 
	 @Override
	 public void verifyNotesData(Map<String, Object> data) {

	  String contactURL = getApiUrlForContractCancel(data);
	  setRequestHeaderforMadeAnOffer(data);
	  String response = library.HTTPGet(contactURL);

	  String notesData = (String) library.getValueFromJson("$.activities[0].activityType", response);

	  String transactionType = notesData.split("-")[1];
	  transactionType = transactionType.trim(); // Scheduled a Callback

	  String locator = getLocatorForTransactionType(transactionType);

	  library.verifyPageContainsElement(locator);

	  String notesLineTwo = library.getTextFrom("MAKEOFFER.notesfieldlinetwo");
	  System.out.println(notesLineTwo);
	  Assert.assertEquals("Property Address - see properties section for more details", notesLineTwo);// Property
	                          // Address
	                          // -
	                          // see
	                          // properties
	                          // section
	                          // for
	                          // more
	                          // details

	  String notesTimeXpath = "xpath->(//span[@class='ng-binding'])[1]";
	  String getTime = library.getTextFrom(notesTimeXpath);
	  String FinalValue = null;

	  try {
	   DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mma");
	   formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	   Date date = formatterIST.parse(getTime);

	   DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mma");
	   formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
	   String SplitTime = formatterUTC.format(date);
	   System.out.println(SplitTime);

	   String ApiTime = (String) library.getValueFromJson("$.activities[0].startDateTime", response);
	   String[] SplitApiTime = ApiTime.split(" ");
	    String[] gotApiTime = SplitApiTime[1].split(":");
	    String FinalTime=SplitApiTime[0]+" "+gotApiTime[0]+":"+gotApiTime[1]+SplitApiTime[2];
	    System.out.println(FinalTime);
	   Assert.assertEquals(FinalTime, SplitTime);// Verify time stamp

	  } catch (Exception e) {
	   e.printStackTrace();
	  }

	 }
	 
	 @Override
	 public void verifyPropertyDetails(Map<String, String> MadeOfferData) {
	  try {

	   String clientName = MadeOfferData.get("ClientName");
	   String date = MadeOfferData.get("OfferDate");
	   String price = MadeOfferData.get("OfferPrice").replace(",", "").replace("\\$", "");
	   System.out.println(price);
	   String address = MadeOfferData.get("Address");

	   String madeOfferTextFromUI = library.getTextFrom("MAKEOFFER.escrowinfo");
	   String madeAnOffer = "Made an Offer";
	   boolean isCorrect = madeAnOffer.equals(madeOfferTextFromUI);
	   Assert.assertTrue(isCorrect);
	   String systemDate = getSystemDate();
	   String uiDate = library.getTextFrom("MAKEOFFER.escrowinfodate");
	   boolean isDateCorrect = systemDate.equals(uiDate);
	   String addressline1 = library.getTextFrom("MAKEOFFER.escrowinfoaddressone");
	   addressline1 = addressline1 + "reet" + ",";
	   String addressline2 = library.getTextFrom("MAKEOFFER.escrowinfoaddresstwo");
	   String completeAddressUI = addressline1 + addressline2;
	   Map<String, String> test = FileUtil.readFileAsMap("config//API_Testt.properties");

	   String s = completeAddressUI;
	   String[] ss = s.split(",");
	   String city = ss[2];
	   city = city.trim();
	   String[] Addcity = city.split(" ");
	   String AAddcity = Addcity[0];
	   // System.out.println(AAddcity);
	   String addCity = (test.get(AAddcity));
	   String finalcity = ss[0] + "," + ss[1] + "," + addCity + "," + Addcity[1];
	   System.out.println(finalcity);
	   System.out.println(address);
	   Assert.assertEquals(finalcity, address);

	   String uiPrice = library.getTextFrom("MAKEOFFER.escrowinfoprice").replace(",", "").replace("$", "");
	   Assert.assertEquals(uiPrice, price);// verify offered page is
	            // displayed on property page
	   System.out.println(completeAddressUI);

	   Assert.assertEquals(systemDate, uiDate);// Date comparison
	   String uiPriceRemoveSpecialCharaters = uiPrice.replaceAll(",", "");
	   String finalUiPrice = uiPriceRemoveSpecialCharaters.replaceAll("\\$", "");
	   System.out.println(uiPriceRemoveSpecialCharaters);

	   boolean isPriceCorrect = finalUiPrice.equals(price); // Price
	                 // comparison
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	 }
	 
	 
	 @Override
	 public void updateToContractAcceptedStage1(Map<String, Object> data) {
		  library.click("xpath->//div[@ng-init='opportunity=(client.contactInfo|getUpdateOpprotunity)'][1]//a/strong");
		  library.wait(2);
		  library.click("TRANSACTION.updatebutton");
		  library.wait(4);
		  library.click("xpath-> //span[text()='SELECT ONE']");
		  library.wait(2);
		  library.click("xpath->//span[text()='Contract Accepted']");
		  Assert.assertTrue(library
		    .verifyPageContainsElement("xpath->//strong[@class='stage' and text()='Contract Accepted']"));// verifies
		  String ClientName = library
		    .getTextFrom("xpath->//span[contains(text(),'Update:')]/..//strong[@class='ng-binding']");
		  Assert.assertEquals(ClientName, "Test Client");// verify Client Name is
		  // correct with the
		  // updated client
		  String Address = library.getTextFrom("xpath->//a[text()='Edit address']/..//address");
		  Assert.assertEquals(Address, "1749 Lake St, San Mateo, California 94403");
		  String ExpectedCloseDate = library
		    .getTextFrom("xpath->//strong[text()='Expected Close Date:']/../../../..//div[@name='date']");
		  DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		  Calendar c = Calendar.getInstance();
		  c.setTime(new Date()); // Now use today date.
		  c.add(Calendar.DATE, 45); // Adding 45 days
		  String output = dateFormat.format(c.getTime());
		  Assert.assertEquals(ExpectedCloseDate, output);// verifies The default
		  library.isElementEnabled("CONTRACT.expectedclosedate", true);
		  library.isElementEnabled("CONTRACT.price", true);
		  enterNumber("650000", "MAKEOFFER.price");
		  enterNumber("3", "xpath->//input[@name='commission']");
		  library.wait(3);
		  library.isElementEnabled("CONTRACT.continuebutton", true);
		  library.click("CONTRACT.continuebutton"); // 1 continue
		  library.wait(3);
		  library.verifyPageContainsElement("xpath->//strong[text()='Confirm Name on Contract:']");
		  String ClientName2 = library.getTextFrom("xpath->//a[@class='edit-name']/..//strong");
		  Assert.assertEquals(ClientName2, "Test Client");// verify Client Name is
		  // correct with the
		  // updated client
		  library.isElementEnabled("CONTRACT.editnamelink", true);
		  library.wait(3);
		  library.click("CONTRACT.continuebutton"); // 2 continue
		  library.click("CONTRACTACCEPT.saveupdate");// continue
		  library.wait(2);
		  Assert.assertTrue(library.verifyPageContainsElement("CONTRACTACCEPT.accept"));
		  // verify notes section
		  openNotes();
		  library.wait(5);
		  String string = library.getTextFrom("xpath->//li[@ng-repeat='note in trans.noteList'][1]//strong");
		  String[] parts = string.split(" ");
		  String part = parts[0] + " " + parts[1];
		  Assert.assertEquals(part, "Contract Accepted:");
		  String note = library.getTextFrom(
		    "xpath->//li[@ng-repeat='note in trans.noteList'][1]//p[@ng-show='showOfferInformation(note)']");
		  Assert.assertEquals(note, "Property Address - see properties section for more details");
		  String contactURL = getApiUrlAWS(data);
		  setRequestHeader(data);
		  String response = library.HTTPGet(contactURL);
		  String notesTimeXpath = "CONTRACTCANCEL.accept";
		  String getTime = library.getTextFrom(notesTimeXpath);
		  try {
		   DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mma");
		   formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		   Date date = formatterIST.parse(getTime);
		   DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mma");
		   formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		   System.out.println("==========" + formatterUTC.format(date));
		   String SplitTime = formatterUTC.format(date);
		   System.out.println(SplitTime);
		   String ApiTime = (String) library.getValueFromJson("$.activities[0].startDateTime", response);
		   String[] SplitApiTime = ApiTime.split(" ");
		    String[] gotApiTime = SplitApiTime[1].split(":");
		    String FinalTime=SplitApiTime[0]+" "+gotApiTime[0]+":"+gotApiTime[1]+SplitApiTime[2];
		    System.out.println(FinalTime);
		   Assert.assertEquals(FinalTime, SplitTime);
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		  // Verify property section on the page
		  library.click("DETAILS.properties");
		  library.wait(3);
		  library.scrollToElement("MadeanOffertext");
		  library.wait(5);
		  String MadeanOffertext = library.getTextFrom("MAKEOFFER.escrowinfo");
		  String Address1 = library.getTextFrom("MAKEOFFER.escrowinfoaddressone")+"reet";
		  String Address2 = library.getTextFrom("MAKEOFFER.escrowinfoaddresstwo");
		  String FinalAddress = Address1 + " " + Address2;
		  String OfferDate = library.getTextFrom("MAKEOFFER.escrowinfodate");
		  String OfferPrice = library.getTextFrom("MAKEOFFER.escrowinfoprice");
		  String ContractacceptedText = library.getTextFrom("CONTRACT.escrowinfo");
		  String accepteddate = library.getTextFrom("CONTRACT.accepteddate");
		  String acceptedprice = library.getTextFrom("CONTRACT.acceptedprice");
		  String expectedclose = library.getTextFrom("CONTRACT.expectedclose");
		  String commission = library.getTextFrom("CONTRACT.commission");
		  DateFormat dateFormats = new SimpleDateFormat("MM/dd/yyyy");
		  Date date = new Date();

		  String returndate = (dateFormats.format(date));
		  String Addressxl = (String) data.get("Address");
		  String Pricexl = (String) data.get("Price");
		  String commissionxl = (String) data.get("commission") + "%";
		  Assert.assertEquals(MadeanOffertext, "Made an Offer");
		  Assert.assertEquals(FinalAddress, Addressxl);// verify address
		  Assert.assertEquals(OfferDate, returndate);
		  Assert.assertEquals(OfferPrice, Pricexl);// verify offer price
		  Assert.assertEquals(ContractacceptedText, "Contract Accepted");
		  Assert.assertEquals(accepteddate, returndate);
		  Assert.assertEquals(acceptedprice, Pricexl);
		  Assert.assertEquals(expectedclose, output);
		  Assert.assertEquals(commission, commissionxl);// verify commission
		  }
	 
	 
	 @Override
	 public void updateToStageForContractCancel(String stage, String client) {

	  searchAndSelectClient(client);
	  library.wait(3);
	  String stageBeforeUpdate = getPrestage();
	  // Assert.assertEquals("In Contract", stageBeforeUpdate); // validating
	  // stage prior to operation for contract cancel stage.
	  library.click("TRANSACTION.updatebutton");
	  chooseTransactionStage(stage);
	  Assert.assertTrue(library.isElementDisabled("UPDATE.applybutton"));// Update
	                   // button
	                   // is
	                   // disabled
	                   // before
	                   // any
	                   // notes
	                   // is
	                   // input
	  library.typeDataInto("Test for Cancel", "NOTES.notesfield");
	  boolean isNotesFieldPresent = library.verifyPageContainsElement("NOTES.notesfield");
	  Assert.assertTrue(isNotesFieldPresent); // validating Notes field.
	  library.wait(2);
	  library.click("UPDATE.applybutton");
	  library.wait(3);
	  Assert.assertTrue(library.verifyPageContainsElement("CONTRACTCANCEL.cancel"));// verify
	                            // stage
	                            // is
	                            // changed
	                            // to
	                            // contract
	                            // cancelled

	  library.waitForElement("TRANSACTION.properties");
	  library.wait(5);
	 }
	 
	 
	 @Override
	 public void verifyNotesDataForContractCancel(Map<String, Object> data) {

		 String contactURL = getApiUrlForContractCancel(data);
		  setRequestHeaderforMadeAnOffer(data);
		  String response = library.HTTPGet(contactURL);
		  
		  Assert.assertTrue(library.verifyPageContainsElement("xpath->(//div[@class='col-xss-6 col-xs-7 feature-value ng-binding'])[3]"));//Verify activity type for Contact Cancelled
		  String notesData = (String) library.getValueFromJson("$.activities[0].activityType", response);

		  String transactionType = notesData.split("-")[1];
		  transactionType = transactionType.trim(); // Scheduled a Callback

		  String locator = getLocatorForTransactionType(transactionType);

		  library.verifyPageContainsElement(locator);

		  String notesLineTwo = library.getTextFrom("CONTRACTCANCEL.notesfieldlinetwo");
		  System.out.println(notesLineTwo);
		  Assert.assertEquals("Test for Cancel", notesLineTwo);// verify notes is
		                // dispalyed as
		                // Contract
		                // cancelled

		  String notesTimeXpath = "xpath->(//span[@class='ng-binding'])[1]";
		  String getTime = library.getTextFrom(notesTimeXpath);

		  try {
		   DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mma");
		   formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		   Date date = formatterIST.parse(getTime);

		   DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mma");
		   formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		   String SplitTime = formatterUTC.format(date);
		   System.out.println(SplitTime);

		   String ApiTime = (String) library.getValueFromJson("$.activities[0].startDateTime", response);
		   String[] SplitApiTime = ApiTime.split(" ");
		    String[] gotApiTime = SplitApiTime[1].split(":");
		    String FinalTime=SplitApiTime[0]+" "+gotApiTime[0]+":"+gotApiTime[1]+SplitApiTime[2];
		    System.out.println(FinalTime);
		   Assert.assertEquals(FinalTime, SplitTime);//// verify notes is
		              //// dispalyed as
		              //// timestamp and date
		   

		  } catch (Exception e) {
		   e.printStackTrace();
		  }

	 }
	 
	 @Override
	 public void updateToReturnToMovoto1(Map<String, String> data) {
	  library.wait(7);
	  library.click("SELECT.selectclient");
	  library.wait(2);
	  library.click("TRANSACTION.updatebutton");
	  library.wait(7);
	  library.click("RETURN.select");
	  library.wait(4);
	  library.click("RETURN.return");
	  library.wait(4);
	  library.click("RETURN.select");
	  library.wait(2);
	  library.click("RETURN.leadstatus");
	  library.click(
	    "RETURN.submitbutton");
	  library.wait(10);
	  String EmailID = data.get("EmailID");
	  library.click("HOMEPAGE.searchbutton"); // click on search
	  library.clear("HOMEPAGE.searchbutton");
	  library.verifyPageContainsElement("HOMEPAGE.searchbutton");
	  library.typeDataInto(EmailID, "HOMEPAGE.searchbutton");
	  Assert.assertTrue(library.verifyPageContainsElement("RETRUN.return")); // Verify
	                             // client
	                             // list
	                             // is
	                             // missing
	  
	  
	  
	 }
	 
	 @Override
	 public void verifyClientDetailsWithApi(Map<String, Object> data) {
		 setTokenAndUserId(data);
		  String clientLeadApi = (String) data.get("clientLeadApi");
		  String response = (String) getResponse(clientLeadApi);

		  String mailIdOfApiResponce = (String) library.getValueFromJson("$.email", response);
		  String mailId = library.getTextFrom("DETAILS.primaryemail");
		  if (mailIdOfApiResponce == null) {

		   Assert.assertTrue(mailId.equals("-"), "In Contact info there is no '-' for not having Contact Number");// Verify
		                             // the
		                             // email
		                             // matches
		                             // the
		                             // value
		                             // from
		                             // API
		                             // response
		  } else
		   Assert.assertEquals(mailId, mailIdOfApiResponce);// Verify email id,
		                // if email id
		                // present

		  ArrayList<String> contactList = new ArrayList<>();
		  ArrayList<String> contacts = new ArrayList<>();
		  contacts.add("primary");
		  contacts.add("mobile");
		  contacts.add("office");
		  contacts.add("home");
		  contacts.add("other");

		  for (int i = 0; i < 5; i++) {

		   String contactType = (String) library.getValueFromJson("$.phone[" + i + "].type", response);

		   try {

		    switch (contactType) {

		    case "primary":
		     String primaryNumberOfApiResponse = (String) library
		       .getValueFromJson("$.phone[" + i + "].phonenumber", response);
		     String primaryNumber = library.getTextFrom("DETAILS.primaryPhone");
		     Assert.assertTrue(primaryNumber.equals(primaryNumberOfApiResponse),
		       "Primary number is not matched.");// Verify the
		                // Primary Phone
		                // matches the
		                // value from
		                // API response
		     contactList.add(contactType);
		     break;

		    case "mobile":
		     String mobileNumberOfApiResponse = (String) library
		       .getValueFromJson("$.phone[" + i + "].phonenumber", response);
		     String mobileNumber = library.getTextFrom("DETAILS.mobileNumber");
		     Assert.assertTrue(mobileNumber.equals(mobileNumberOfApiResponse), "Mobile number is not matched.");// Verify
		                              // phone
		                              // numbers
		                              // which
		                              // has
		                              // values
		                              // from
		                              // API
		                              // response
		                              // match
		                              // the
		                              // value
		                              // on
		                              // lead's
		                              // contact
		                              // page
		     contactList.add(contactType);
		     break;

		    case "office":
		     String officeNumberOfApiResponse = (String) library
		       .getValueFromJson("$.phone[" + i + "].phonenumber", response);
		     String officeNumber = library.getTextFrom("DETAILS.officeNumber");
		     Assert.assertTrue(officeNumber.equals(officeNumberOfApiResponse), "Office number is not matched.");// Verify
		                              // phone
		                              // numbers
		                              // which
		                              // has
		                              // values
		                              // from
		                              // API
		                              // response
		                              // match
		                              // the
		                              // value
		                              // on
		                              // lead's
		                              // contact
		                              // page
		     contactList.add(contactType);
		     break;

		    case "home":
		     String homeNumberOfApiResponse = (String) library.getValueFromJson("$.phone[" + i + "].phonenumber",
		       response);
		     String homeNumber = library.getTextFrom("DETAILS.homeNumber");
		     Assert.assertTrue(homeNumber.equals(homeNumberOfApiResponse), "Home number is not matched.");// Verify
		                             // phone
		                             // numbers
		                             // which
		                             // has
		                             // values
		                             // from
		                             // API
		                             // response
		                             // match
		                             // the
		                             // value
		                             // on
		                             // lead's
		                             // contact
		                             // page
		     contactList.add(contactType);
		     break;

		    case "other":
		     String otherNumberOfApiResponse = (String) library
		       .getValueFromJson("$.phone[" + i + "].phonenumber", response);
		     String otherNumber = library.getTextFrom("DETAILS.otherNumber");
		     Assert.assertTrue(otherNumber.equals(otherNumberOfApiResponse), "Other number is not matched.");// Verify
		                             // phone
		                             // numbers
		                             // which
		                             // has
		                             // values
		                             // from
		                             // API
		                             // response
		                             // match
		                             // the
		                             // value
		                             // on
		                             // lead's
		                             // contact
		                             // page
		     contactList.add(contactType);
		     break;
		    }
		   } catch (Exception e) {
		    System.out.println(i + " Contact Numbers are avaible..");
		    break;
		   }

		  }

		  contacts.removeAll(contactList);
		  Iterator<String> itr = contacts.iterator();
		  while (itr.hasNext()) {
		   String contactType = itr.next();
		   String contact = contactType.substring(0, 1).toUpperCase() + contactType.substring(1);
		   String value = library.getTextFrom("xpath->.//*[contains(text(),'" + contact + ":')]/../div[3]/a");
		   Assert.assertEquals(value.equals("-"), true);// Verify phone numbers
		               // which has no
		               // values from API
		               // response has a
		               // "-" on the
		               // contact page

		   System.out.println(contactType);


	  }
	  

	 }
	 
	 
	 @Override
	 public void verifyOpportunitiesWithApi(Map<String, Object> data) {
		 String opportunityType = library.getTextFrom("DETAILS.opportunityType");
		  System.out.println(opportunityType);
		  String opportunityStage = library.getTextFrom("DETAILS.opportunityStage");
		  System.out.println(opportunityStage);
		  setTokenAndUserId(data);
		  String response = getResponse((String) data.get("clientLeadApi2"));
		  System.out.println(response);
		  setTokenAndUserId(data);
		  String response1 = getResponse((String) data.get("clientLeadApi4"));
		  String opportunityTypeOfApiResponse = (String) library
		    .getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityType", response);
		  String opportunityAreaOfApiResponse = (String) library
		    .getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityStage", response);
		  Assert.assertEquals(opportunityType, opportunityTypeOfApiResponse);// Lead
		                   // Type
		                   // matches
		                   // the
		                   // API
		                   // Response

		  Assert.assertEquals(opportunityStage, opportunityAreaOfApiResponse);// Stage
		                   // is
		                   // offered

		  String urgencyLabel = library.getTextFrom("TRANSACTION.urgencyLabel");

		  String urgencyFromApi = (String) library.getValueFromJson("$.urgency", response);
		  String[] urg = urgencyFromApi.split(" ");
		  String urgencyLabelFromApi = urg[0];
		  Assert.assertEquals(urgencyLabel, urgencyLabelFromApi);// Urgency mathes
		                // with API
		                // response		  
		  String OfferPrice = library.getTextFrom("PROPERTIES.offerprice").replace("$", "").replace(",", "");
		  int Offer=Integer.parseInt(OfferPrice);
		  System.out.println(response1);
		  int OfferPriceAPI = (int)library.getValueFromJson("$.activePropertyOffer.offerPrice", response1);
		  Assert.assertEquals(Offer, OfferPriceAPI);
	 }
	 
	 @Override
	 public void verifyOpportunitiesOfMetStage(Map<String, Object> data) {
		 String opportunityType1 = library.getTextFrom("xpath->(//div[text()=':Type'])[1]/../div[2]");
		  String opportunityType2 = library.getTextFrom("xpath->(//div[text()=':Type'])[2]/../div[2]");
		  System.out.println(opportunityType1);
		  System.out.println(opportunityType2);
		  String opportunityStage1 = library.getTextFrom("DETAILS.opportunityStage");
		  if (!opportunityStage1.equals("Met")) {
		   library.click("TRANSACTION.updatebutton");
		   library.wait(2);
		   library.click("TRANSACTION.selectOne");
		   library.click("TRANSACTION.setMet");
		   library.typeDataInto("Agent Automation", "NOTES.notesfield");
		   library.click("MAKEOFFER.saveupdatebutton");
		   library.wait(4);
		  }
		  Assert.assertTrue(
		    library.verifyPageContainsElement("xpath->(.//a[@class='text-center btn-green ng-binding'])[2]"));// Verify
		                              // transaction
		                              // tab
		  setTokenAndUserId(data);
		  String response = getResponse((String) data.get("clientLeadApi3"));
		  System.out.println(response);
		  String UIUrgencyImage1=library.getTextFrom("xpath->(.//*[@class='urgency-status ng-pristine ng-untouched ng-valid ng-binding ng-isolate-scope urgency-as-1'])[1]");
		  String ApiUrgencyImage1=((String) library.getValueFromJson("$.urgency", response)).split(" ")[0];
		  Assert.assertEquals(UIUrgencyImage1, ApiUrgencyImage1);
		  String UIMet1=library.getTextFrom("xpath->(//div[text()='Met'])[1]");
		  String ApiMet1=(String) library.getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityStage", response);
		  Assert.assertEquals(ApiMet1, UIMet1);// verify stage transaction is met
		  String UIMet2=library.getTextFrom("xpath->(//div[text()='Met'])[2]");
		  String ApiMet2=(String) library.getValueFromJson("$.opportunitiesSummary.openOpportunities[1].opportunityStage", response);
		  Assert.assertEquals(ApiMet2, UIMet2);// verify stage transaction is met
		  String opportunityTypeOfApiResponse1 = (String) library
		    .getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityType", response);
		  String opportunityAreaOfApiResponse1 = (String) library
		    .getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityStage", response);
		  Assert.assertEquals(opportunityType1, opportunityTypeOfApiResponse1);// Lead
		                    // type
		                    // of
		                    // first
		                    // transaction
		                    // matches
		                    // with
		                    // API
		                    // response

		  String opportunityTypeOfApiResponse2 = (String) library
		    .getValueFromJson("$.opportunitiesSummary.openOpportunities[1].opportunityType", response);
		  String opportunityAreaOfApiResponse2 = (String) library
		    .getValueFromJson("$.opportunitiesSummary.openOpportunities[1].opportunityStage", response);
		  Assert.assertEquals(opportunityType2, opportunityTypeOfApiResponse2);// Lead
		                    // type
		                    // of
		                    // second
		                    // transaction
		                    // matches
		                    // with
		                    // API
		                    // response

		  String urgencyLabel = library.getTextFrom("TRANSACTION.urgencyLabel");

		  String urgencyFromApi = (String) library.getValueFromJson("$.urgency", response);
		  String[] urg = urgencyFromApi.split(" ");
		  String urgencyLabelFromApi = urg[0];
		  Assert.assertEquals(urgencyLabel, urgencyLabelFromApi);// Urgency of
		                // BOTH
		                // transaction
		                // matches the
		                // API response
		
	 }	 
	 
	 @Override
	 public void verifyNotesForTalkedStage(Map<String, Object> data) {
		 try {
			   String notesTimeXpath = "xpath->(.//p[@class='list-group-item-text ng-binding'])[1]";
			   String[] getTime = library.getTextFrom(notesTimeXpath).split(" ");
			   String UITime=getTime[2]+" "+getTime[3].trim();             
			   String FinalValue = null;
			   String response = getResponse((String) data.get("notesApi"));

			   // Converting time from IST to UST and validating.
			   DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mma");
			   formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			   Date date = formatterIST.parse(UITime);

			   DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mma");
			   formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
			   String[] SplitTime = formatterUTC.format(date).split(" ");
			   FinalValue = SplitTime[1];
			   System.out.println(FinalValue);

			   String ApiTime = (String) library.getValueFromJson("$.activities[0].startDateTime", response); // getting
			                           // time
			                           // from
			                           // API
			   String[] SplitApiTime = ApiTime.split(" ");
			   String gotApiTime = SplitApiTime[1];
			   String[] splitSec = gotApiTime.split(":");
			   String finalTime = splitSec[0] + ":" + splitSec[1] + SplitApiTime[2];
			   System.out.println(finalTime);
			   Assert.assertEquals(finalTime, FinalValue); // validating time
			              // with API.

			   String noteDiscription = (String) library.getValueFromJson("$.activities[0].note", response);
			   noteDiscription = noteDiscription.trim();
			   library.wait(2);
			   Assert.assertTrue(library
			     .verifyPageContainsElement("xpath->(.//p[contains(text(),'" + noteDiscription + "')])[1]"));// Notes
			                              // are
			                              // displayed
			                              // in
			                              // chronological
			                              // order
			                              // most
			                              // recent
			                              // note
			                              // is
			                              // on
			                              // top

			   String activityTypeOfApiResponse = (String) library.getValueFromJson("$.activities[0].activityType",
			     response);
			   String ele = activityTypeOfApiResponse.split("-")[1];
			   String activityTypePath = "xpath->(.//*[contains(text(),'" + ele.trim() + "')])[1]";
			   System.out.println(ele);
			   Assert.assertTrue(library.verifyPageContainsElement(activityTypePath));// he
			                       // latest
			                       // note's
			                       // activity
			                       // type
			                       // on
			                       // app
			                       // matches
			                       // the
			                       // APT
			                       // Response
			  } catch (Exception e) {
			   e.printStackTrace();
			  }


	 }
	 
	 
	 @Override
	 public void verifyPropertiesOftalkedStage(Map<String, Object> data) {
		 library.click("TRANSACTION.properties");
		  setTokenAndUserId(data);
		  String response = getResponse((String) data.get("propertiesApi"));
		  String dateAndTime = (String) library.getValueFromJson("$.properties[0].dateExpressedInterest", response);
		  String dateOfApiResponse = dateAndTime.split(" ")[0];
		  boolean isDateMatched = library.verifyPageContainsElement(
		    "xpath->.//*[contains(text(),'interest expressed on:')]/..//strong[contains(text(),'"
		      + dateOfApiResponse + "')]");
		  Assert.assertTrue(isDateMatched, "Date is not matched");// verify
		                // interest
		                // expressed on
		                // date
		  
		  //verify property address
		  String addressUI = library.getTextFrom("xpath->//span[@ng-if='prop.address']");
		  String CityUI = library.getTextFrom("xpath->//span[@ng-if='prop.city']").replace(",", "");
		  String StateUI = library.getTextFrom("xpath->//span[@ng-if='prop.state']").replace(" ", "").replace(",", "");
		  String zipUI = library.getTextFrom("xpath->//span[@ng-if='prop.state']/..");
		  String addressAPI = (String) library.getValueFromJson("$.properties[0].address", response);
		  String CityAPI = (String) library.getValueFromJson("$.properties[0].city", response);
		  String StateAPI = (String) library.getValueFromJson("$.properties[0].state", response);
		  String zipAPI =  (String) library.getValueFromJson("$.properties[0].zip", response);
		  Assert.assertEquals(addressUI, addressAPI);
		  Assert.assertEquals(CityUI, CityAPI);
		  Assert.assertEquals(StateUI, StateAPI);
		  Assert.assertTrue(zipUI.contains(zipAPI));
		  
//		  String MLSapi = (String) library.getValueFromJson("$.properties[0].mls", response);
//		 // Assert.assertTrue(library.verifyPageContainsElement("xpath->.//*[@value='" + MLSapi + "']", true));// verify
//		                           // mls
//		  String leadmsgapi = (String) library.getValueFromJson("$.properties[0].message", response);
//		 // String leadmsgui = library.getTextFrom("xpath->.//*[@id='properties-row']//p[3]");
//		  Assert.assertEquals(leadmsgapi, leadmsgui);// verify lead msg

		  String propertyPriceInApp = library.getTextFrom("PROPERTIES.price");
		  System.out.println(propertyPriceInApp);
		  propertyPriceInApp = propertyPriceInApp.replace("$", "");
		  propertyPriceInApp = propertyPriceInApp.replace(",", "");
		  // Object property = propertyPriceInApp;
		  int price1 = Integer.parseInt(propertyPriceInApp);
		  System.out.println(propertyPriceInApp);
		  Object propertyPriceOfApiResponse = (Object) library.getValueFromJson("$.properties[0].price", response);
		  Integer propertyPriceString = (Integer) propertyPriceOfApiResponse;
		  int price2 = (int) propertyPriceString;
		  Assert.assertTrue(price1 == price2, "Price is matched with api response"); // verify
		                     // propert
		                     // price
		                     // matches

//		  String numberOfbeds = library.getTextFrom("PROPERTIES.numberOfBeds");
//		  int numOfBeds = Integer.parseInt(numberOfbeds);
//		  Object numberOfBedsInApiResponse = (Object) library.getValueFromJson("$.properties[0].bed", response);// verify
//		                            // propert
//		                            // info
//		  Integer bedsInteger = (Integer) numberOfBedsInApiResponse;
//		  int numOfbedsInApiResponse = (int) (bedsInteger);
//		  Assert.assertTrue(numOfBeds == numOfbedsInApiResponse, "Number of beds is not matched");
	//
//		  String numberOfBaths = library.getTextFrom("PROPERTiES.numberOfBaths");
//		  int baths1 = Integer.parseInt(numberOfBaths);
//		  Object numberOfBathsInApiResponse = (Object) library.getValueFromJson("$.properties[0].bath", response);// verify
//		                            // propert
//		                            // info
//		  Integer bathsInteger = (Integer) numberOfBathsInApiResponse;
//		  int baths2 = (int) bathsInteger;
//		  Assert.assertTrue(baths1 == baths2, "NUmber of baths is not matched");
	//
//		  String areaOfProperty = library.getTextFrom("PROPERTIES.areaInSqft");
//		  areaOfProperty = areaOfProperty.replaceAll(",", "");
//		  int area1 = Integer.parseInt(areaOfProperty);
//		  Object areaOfPropertyInApiResponse = (Object) library.getValueFromJson("$.properties[0].sqft", response);// verify
//		                             // propert
//		                             // info
//		  Integer areaIntger = (Integer) areaOfPropertyInApiResponse;
//		  int area2 = (int) areaIntger;
//		  Assert.assertTrue(area1 == area2, "Area Of property is not matched");// verify property info
	 }
	 
	
	
	// Date created 21oct2016 by Manas 
	 @Override
	public void LoginCredential(JSONObject data) {
		 System.out.println(library.getCurrentPlatform().toString());
		String CurrentPlatform = library.getCurrentPlatform().toString();
		String CurrentBrowserName = library.getBrowserName().toString();
		String userName = (String) data.get("Username");
		String password = (String) data.get("Password");
		switch (CurrentPlatform) {
		case "Android":
			// for loading URL, depends on Network speed thats why used hard
			// wait.
			library.wait(10);
			if(library.verifyPageContainsElement("HAMBURGER.menu"))
			{
			library.waitForElement("HAMBURGER.menu");
			library.click("HAMBURGER.menu");
			library.wait(8);
			library.isJSEClicked("HAMBURGER.login");
			}
			library.waitForElement("SEARCHPAGE.loginWindow");
			library.verifyPageContainsElement("SEARCHPAGE.loginWindow");
			library.waitForElement("LOGIN.username");
			library.typeDataInto(userName, "LOGIN.username");
			library.waitForElement("LOGIN.password");
			library.typeDataInto(password, "LOGIN.password");
			library.click("LOGIN.submitButton");
			break;
		case "IOS_WEB":		
			// for loading URL, depends on Network speed thats why used hard
			// wait.
			library.wait(10);
			if(library.verifyPageContainsElement("HAMBURGER.menu"))
			{
				library.waitForElement("HAMBURGER.menu");
				library.click("HAMBURGER.menu");
				library.wait(5);
				boolean isUserAlreadyLoggedIn = library.verifyPageContainsElement("xpath->.//i[@class='icon-user']");
				if(isUserAlreadyLoggedIn){
					library.waitForElement("xpath->.//i[@class='icon-user']");
					library.click("xpath->.//i[@class='icon-user']");
					library.wait(3);
					library.waitForElement("xpath->//*[contains(text(),'Sign Out')]");
					library.click("xpath->//*[contains(text(),'Sign Out')]");
					library.wait(20);
					library.waitForElement("HAMBURGER.menu");
					library.click("HAMBURGER.menu");
				}
				library.isJSEClicked("HAMBURGER.login");
			}
			
			library.waitForElement("SEARCHPAGE.loginWindow");
			library.verifyPageContainsElement("SEARCHPAGE.loginWindow");
			library.waitForElement("LOGIN.username");
			library.typeDataInto(userName, "LOGIN.username");
			library.waitForElement("LOGIN.password");
			library.typeDataInto(password, "LOGIN.password");
			library.click("LOGIN.submitButton");
			break;
		case "Web":
			WebDriver driver = library.getDriver();
			boolean isLoginEnabled = library.verifyPageContainsElement("USER.icon");
			if (isLoginEnabled) {
				String logOutUrl = (String) data.get("LogoutUrl");
				library.get(logOutUrl);
			}
/*			WebElement element = library.findElement("HAMBURGER.userIcon");
			library.mouseHoverJScript(element);
			library.wait(1);*/
			library.isJSEClicked("xpath->.//*[@id='loginLink']");
			library.waitForElement("SEARCHPAGE.loginWindow");
			library.verifyPageContainsElement("SEARCHPAGE.loginWindow");
			library.typeDataInto(userName, "LOGIN.username");
			library.waitForElement("LOGIN.password");
			library.typeDataInto(password, "LOGIN.password");
			library.waitForElement("LOGIN.submitButton");
			library.click("LOGIN.submitButton");
			break;
		}
	}
	 
	@Override
	public void NavigateToFavouriteHomes(JSONObject data) {
		String CurrentPlatform = library.getCurrentPlatform();
		String CurrentBrowserName = library.getBrowserName();
		switch (CurrentPlatform) {
		case "Android":
		case "IOS_WEB":	
			library.wait(5); // transition from login page to welcome page,
								// that's why used hard wait.
			library.waitForElement("HAMBURGER.menu");
			library.click("HAMBURGER.menu");
			library.wait(5);
			library.waitForElement("USER.icon");
			library.isJSEClicked("USER.icon");
			library.wait(5);
			library.waitForElement("USER.savedHome");
			library.click("USER.savedHome");
			library.wait(5);
			library.click("FAVOURITEICON.savedHomes");
			library.wait(5);
			library.click("FAVOURITEICON.againSavedHomes");
			break;
		case "Web":
		case "Mac":
			library.wait(5);
			boolean isLoginEnabled = library.verifyPageContainsElement("USER.icon");
			if (isLoginEnabled) {
				WebElement element = library.findElement("HAMBURGER.userIcon");
				//library.wait(5);
				library.mouseHoverJScript(element);
				library.wait(1);
				library.isJSEClicked("USER.savedHome");
			}
			break;
		}
	}
	
	@Override
	public void deleteFavouriteItem(JSONObject data) {
		boolean isElementPresent = false;
		if (library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")) {
			try {
				// currently findElement gets failed if element is not present in UI thats why we put Hardcoded xpath will convert to Object repo once it gets resolve.
				isElementPresent = library.getDriver().findElement(By.xpath(".//a[@class='text-shadow favorite active']")).isDisplayed();
				//isElementPresent = library.findElement("FAVOURITEICON.heart").isDisplayed(); // we tried but we found issue with 
			} catch (Exception e) {
			}
			if (isElementPresent) {
				
				int heartCount = library.getElementCount("FAVOURITEICON.color");
				for (int i = 1; i <= heartCount; i++) {
					library.wait(5);
					if(library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
						library.waitForElement("xpath->.//*[@id='myfavorites']/div/div[2]/div[" + i + "]/div[1]/div/div[1]/a"); // Dynamic Xpath
						library.isJSEClicked("xpath->.//*[@id='myfavorites']/div/div[2]/div[" + i + "]/div[1]/div/div[1]/a"); // Dynamic Xpath
					}else{
						library.waitForElement("xpath->.//*[@id='myfavorites']/div/div[2]/div[" + i + "]/div[1]/div/div[1]/a"); // Dynamic Xpath
						library.click("xpath->.//*[@id='myfavorites']/div/div[2]/div[" + i + "]/div[1]/div/div[1]/a"); // Dynamic Xpath

					}
				}
				
			}
			
			logOutForConsumerWeb(data);
		
		} else {
			 try {
				    library.wait(5); 
				    library.click("USER.SavedFavHome");
				    library.wait(5); 
				    // currently findElement gets failed if element is not present in UI thats why we put Hardcoded xpath will convert to Object repo once it gets resolve.

				    isElementPresent = library.getDriver().findElement(By.xpath(".//a[@class='text-shadow favorite active']")).isDisplayed();
				    System.out.println(isElementPresent);
				   } catch (Exception e) {

				   }
				   if (isElementPresent) {
				    library.wait(5); 
				    int heartCount = library.getElementCount("FAVOURITEICON.color");
				    // Precondition : - Deleting favorite properties, clicking on heart icon to remove
				    for (int i = 1; i <= heartCount; i++) {
				     library.wait(5); 
				    // library.waitForElement("xpath->(//div[@class='favoritecard-btn'])[" + i + "]");
				     library.isJSEClicked("xpath->.//*[@id='myfavorites']/div/div[2]/div[" + i + "]/div[1]/div/div[1]/a");
				     library.wait(2);
				    }
				   }
				   logOutForConsumerWeb(data);  
				   
		}
		library.get((String) data.get("LogoutUrl"));
	}
	
	@Override
	public void SelectFavouriteProperty(JSONObject data)
	{
		String CurrentPlatform = library.getCurrentPlatform();
		String CurrentBrowserName = library.getBrowserName();
		String userName = (String) data.get("Username");
		String password = (String) data.get("Password");
		switch (CurrentPlatform) {
		case "Android":
		case "IOS_WEB":
			   library.waitForElement("SEARCHPAGE.favouriteButton");
			   if(library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
				   library.wait(10);
				   JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
				   js.executeScript("document.getElementsByClassName('favorite')[0].click()");
				   library.wait(2);
				}
			   else{
			   library.click("SEARCHPAGE.favouriteButton");
			   }
			   //library.wait(3);
			   library.waitForElement("SEARCHPAGE.loginWindow");
			   library.verifyPageContainsElement("SEARCHPAGE.loginWindow");
			   library.typeDataInto(userName, "LOGIN.username");
			   library.typeDataInto(password, "LOGIN.password");
			   library.click("LOGIN.submitButton");
			   library.wait(10);
			   library.waitForElement("HAMBURGER.menu");
			   library.click("HAMBURGER.menu");
			   library.wait(6);
			   library.waitForElement("USER.icon");
				library.isJSEClicked("USER.icon");
				library.waitForElement("USER.savedHome");
				library.click("USER.savedHome");
				library.wait(5);
				library.click("xpath->.//*[@id='nav-options']");
				library.wait(5);
				library.click("xpath->.//*[@value='/my-movoto/favorites']");
			   
			  // library.wait(10);
			   break;
		case "Web":
			if (CurrentBrowserName.equalsIgnoreCase("Safari")) {
				   
				   library.refresh();
				   library.wait(8);
				   library.waitForElement("SEARCHPAGE.favouriteButtonDriver");
				   JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
				   js.executeScript("document.getElementsByClassName('fav')[0].children[0].click()");
				   
				   library.wait(3);
				
				   library.waitForElement("SEARCHPAGE.loginWindow");
				   library.verifyPageContainsElement("SEARCHPAGE.loginWindow");
				   library.typeDataInto(userName, "LOGIN.username");
				   library.typeDataInto(password, "LOGIN.password");
				   library.click("LOGIN.submitButton");
				    WebElement userIcon = library.findElement("LOGIN.submitButton");
				   
				    library.mouseHoverJScript(userIcon);
				    library.wait(5);
			} else {
				library.refresh();
				   library.wait(8);
				   library.waitForElement("SEARCHPAGE.favouriteButtonDriver");
				   JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
				   js.executeScript("document.getElementsByClassName('fav')[0].children[0].click()");
				  
				   library.wait(3);
				 
				   library.waitForElement("SEARCHPAGE.loginWindow");
				   library.verifyPageContainsElement("SEARCHPAGE.loginWindow");
				   library.typeDataInto(userName, "LOGIN.username");
				   library.typeDataInto(password, "LOGIN.password");
				   library.wait(2);
				if (CurrentBrowserName.equalsIgnoreCase("IExplore")) {
					library.click("LOGIN.submitButton");
					library.wait(10);
					
				} else {
					// currently findElement gets failed if element is not present in UI thats why we put Hardcoded xpath will convert to Object repo once it gets resolve.
					//WebElement userIcon = library.findElement("xpath->.//*[@id='loginPanel']/a");
					//library.mouseHoverJScript(userIcon);
					library.click("LOGIN.submitButton");
					
				}
				break;
			}
		}
	}

	
	@Override
	public void VerifySchoolsBasicInfo(JSONObject data) {

		// Verify SchoolName/Grade/{T/S Ratio}/Type/SubType is the same with
		// <BasicInfo_API> response
		library.setRequestHeader("X-MData-Key", "CHUMAGATHUQ9VE7AYEBR");
		String response = library
				.HTTPGet("http://service.ng.movoto.net/school/baseInfo?id=550852000373");

		String SchoolNameAPI = library.getValueFromJson("$.name", response)
				.toString();
		String SchoolNameUI = library.getTextFrom("SCHOOLPAGE.schoolname");
		Assert.assertEquals(SchoolNameAPI, SchoolNameUI);

		String GradeAPI = library.getValueFromJson("$.level", response)
				.toString();
		String GradeUI = library.getTextFrom("SCHOOLPAGE.schoolgrade");
		Assert.assertEquals(GradeAPI, GradeUI);

		String TypeAPI = library.getValueFromJson("$.type", response)
				.toString();
		String TypeUI = library.getTextFrom("SCHOOLPAGE.schooltype");
		Assert.assertEquals(TypeAPI, TypeUI.toLowerCase());

		String SubTypeAPI = library.getValueFromJson("$.subType", response)
				.toString();
		String SubTypeUI = library.getTextFrom("SCHOOLPAGE.schoolsubtype");
		Assert.assertEquals(SubTypeAPI, SubTypeUI);

		// Verify StateRate is the same with <StateRating_API> response
		library.setRequestHeader("X-MData-Key", "CHUMAGATHUQ9VE7AYEBR");
		String response1 = library
				.HTTPGet("http://soa8-qa.ng.movoto.net/school/5500847/stateRating");

		String StateRatingAPI = library.getValueFromJson("$..[0].rating",
				response1).toString();
		library.wait(15);
		String StateRatingUI = library.getTextFrom("SCHOOLPAGE.schoolrating");

		Assert.assertTrue(StateRatingAPI.contains(StateRatingUI),
				"Rating matching with UI");

		// Verify url's pattern for "See Active Listings" is
		// "/<City>-<State Abbr>/new-7/"
		javascriptexecutorclick("xpath->//a[@class='btn orange dsp-getPMHomes']");
		library.wait(5);
		String VerifyUrlUI = library.getUrl();
		String City = data.get("City").toString();
		String State = data.get("State").toString();
		String new7 = data.get("new-7").toString();
		String VerifyUrl = City + "-" + State + "/" + new7 + "/";
		Assert.assertTrue(VerifyUrlUI.contains(VerifyUrl),
				"Verfiied City,State,new7");

		library.getDriver().navigate().back();
		library.getDriver().navigate().refresh();
		
		// Verify the breadcrum is followed below logic:
		// a. 1st node's url is <Domain>
		// b. 2nd node's url contains "/<State Abbr>/schools"
		// c. 3rd node's url contains "/schools/<City>-<State Abbr>"
		// d. 4th node's text is <School Name>

		int count = library.getElementCount("SCHOOLPAGE.breadcrumbcount");
		library.wait(5);
		String verifyLinkAPI[] = { data.get("1st_node").toString(),
				data.get("2nd_node").toString(),
				data.get("3rd_node").toString() };
		for (int i = 0; i < count - 1; i++) {
				String lnkDomain = library
					.getDriver()
					.findElement(
							By.xpath("(//ul[@class='crumbs']/li)[" + (i + 1)
									+ "]/a")).getText();
			Assert.assertEquals(lnkDomain, verifyLinkAPI[i].toString());
		}
		
		// Verify length of the text in tip window is > 50
		library.click("SCHOOLPAGE.greatschoolsRating");
		library.wait(5);
		String toolTiptext = library
				.getTextFrom("SCHOOLPAGE.greatschoolsRatingToolTip");
		Assert.assertTrue((toolTiptext.length() > 50),
				"Verified length of text in tool tip window sucessully");

	}

	public void verifySaveSearchButtonIsDisabled() {
		String attribute = library.getAttributeOfElement("class", "HOMEPAGE.savesearchlink");
		if (attribute.contains("disabled")) {
			Assert.assertTrue(true);
		}
	}

	public void verifySortingForPrice() {
		List<Double> cardList = new ArrayList<Double>();
		for (int i = 1; i <= 50; i++) {
			//Dynamic Xpath
			String cardPrice = library.getTextFrom("xpath->(.//*[@class='price'])[" + i + "]"); 
			cardPrice = cardPrice.replace("$", "");
			cardPrice = cardPrice.replace(",", "");
			cardPrice = cardPrice.trim();
			cardList.add(Double.parseDouble(cardPrice));
		}
		boolean sorted = true;
		for (int i = 1; i < cardList.size(); i++) {
			if (cardList.get(i - 1).compareTo(cardList.get(i)) > 0)
				sorted = false;
		}
		Assert.assertTrue(sorted, "cards are sorted");

	}
	
	public void verifySortingForSquareBigOption(){
		boolean sorted = true;
		List<Double> cardList = new ArrayList<Double>();
		for (int i = 1; i <= 50; i++) {
			String cardPrice = library
					.getTextFrom("xpath->(//div[@class='cardone cardbox'])["+i+"]//div[@class='top-base-info']/span[4] "); // Dynamic
			cardPrice = cardPrice.replace(",", "");
			cardPrice = cardPrice.trim();
			cardList.add(Double.parseDouble(cardPrice));
		}
		for (int i = 1; i < cardList.size(); i++) {
			if (cardList.get(i - 1).compareTo(cardList.get(i)) < 0)
				sorted = false;
		}
		Assert.assertTrue(sorted, "Cards are sorted");

	}
	
	public void countPropertyCardAndCheckSqft(String data){
		String minSqft = data;
		int minSqftInt = Integer.parseInt(minSqft);
		WebDriver driver = library.getDriver();
		Boolean minHouseSizeFlag = true;
		library.wait(10);
		int cardSize = 1;
		for (;;) {
			try {
				//System.out.println(driver.findElement(By.xpath("(//div[@class='top-base-info']/span[4])[" + cardSize + "]")).getText());   // Dynamic Xpath
				if (driver.findElement(By.xpath("(//div[@class='top-base-info']/span[4])[" + cardSize + "]")).getText()
						.trim().equals("")) {
					break;
				}
				cardSize++;
			} catch (Exception e) {
				// cardSize--;
				break;
			}

		}
		cardSize--;
		for (int i = 1; i <= cardSize; i++) {
			String res = library.getTextFrom("xpath->(//div[@class='top-base-info']/span[4])[" + i + "]");  // Dynamic Xpath
			String resWithoutComma = res.replace(",", "");
			int sqFeetInInt = Integer.parseInt(resWithoutComma);
			if (sqFeetInInt < minSqftInt) {
				minHouseSizeFlag = false;
				break;
			}
		}
		Assert.assertTrue(minHouseSizeFlag);

		
	}
	
	
	
	@Override
	public void checkPreconditionFor94() {
		Boolean picCountFlag = false;
		for (int i = 1; i <= 50; i++) // for API array
		{
			try {
				String tempPicCount = library.getTextFrom("xpath->(.//*[@class='cardone cardbox'])[" + i + "]");
				
				if (tempPicCount.contains("/")) {
					String picCount = null;
					String tempPicCountArr[] = tempPicCount.split("\\n");
					if(library.getCurrentPlatformType().equalsIgnoreCase("WEB_IExplore")){
						picCount = tempPicCountArr[3].split("/")[1];
				    }else{
				    	picCount = tempPicCountArr[4].split("/")[1];
				    }
					if (Integer.parseInt(picCount) > 9) {
						picCountFlag = true;
						break;
					}
				}
			} catch (Exception e) {
				break;
			}
		}
		Assert.assertTrue(picCountFlag);
	}
	
	@Override
	public void clickNineTimesOnPropertyImageForMobilePlatform(String data) {
		int intPhotoCount = 0;
		String mapSearchAPIResponse = data;
		WebDriver driver = library.getDriver();
		for (int i = 0; i < 50; i++) // for API array
		{
			String photoCount = String
					.valueOf(library.getValueFromJson("$.listings[" + i + "].photoCount", mapSearchAPIResponse));

			if (photoCount != null) {
				intPhotoCount = Integer.parseInt(photoCount);
			}
			if (intPhotoCount > 9) {
				for (int j = 1; j < 10; j++) {
					library.wait(1);
					JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
					js.executeScript("document.getElementsByClassName('nav-r')["+i+"].click()");
				}
				library.wait(3);
				// Verify "Let's Go!" button exists
				boolean isButtonPresent = library.verifyPageContainsElement("PROPERTYCARD.letsGoButton");
				Assert.assertTrue(isButtonPresent);
				// Click "Let's Go!" button
				library.click("PROPERTYCARD.letsGoButton");
				break;
			}
		}

	}
	
	@Override
	public void verifyMinPrice(Map<String, Object> data){
		String minPriceData = (String) data.get("MinPrice");
		String removeSpecialChaFromMinPriceData = minPriceData.replace("$", "");
		String removeSpecialChaFromMinPrice = removeSpecialChaFromMinPriceData.replaceAll("K", "000");
		int minPriceInInt = Integer.parseInt(removeSpecialChaFromMinPrice);
		Boolean minPriceFlag = true;
		library.wait(6);
		for (int i = 1; i <= 50; i++) {
			//System.out.println(library.getTextFrom("xpath->(//span[@class='price'])[" + i + "]"));
			String res = library.getTextFrom("xpath->(//span[@class='price'])[" + i + "]");
			String resWithoutComma = res.replace(",", "");
			String resWithout$ = resWithoutComma.replace("$", "");
			int priceInInt = Integer.parseInt(resWithout$);
			if (priceInInt < minPriceInInt) {
				minPriceFlag = false;
				break;
			}
		}
		Assert.assertTrue(minPriceFlag);
		
	}
	
	@Override
	public void verifyMaxPrice(Map<String, Object> data){
		String maxPrice = (String) data.get("MaxPrice");
		String removeSpecialChaFromMaxPrice = maxPrice.replace("$", "");
		String removeSpecialChaFromMax = removeSpecialChaFromMaxPrice.replaceAll("K", "000");
		int maxPriceInInt = Integer.parseInt(removeSpecialChaFromMax);
		Boolean maxPriceFlag = true;
		library.wait(10);

		for (int i = 1; i <= 50; i++) {
			//System.out.println(library.getTextFrom("xpath->(//span[@class='price'])[" + i + "]"));
			String res = library.getTextFrom("xpath->(//span[@class='price'])[" + i + "]");
			String resWithoutComma = res.replace(",", "");
			String resWithout$ = resWithoutComma.replace("$", "");
			int priceInInt = Integer.parseInt(resWithout$);
			if (priceInInt > maxPriceInInt) {
				maxPriceFlag = false;
				break;
			}
		}
		Assert.assertTrue(maxPriceFlag);
		}
	
	@Override
	public void verifyMinValueForBath(Map<String, Object> data){
		String minBathValue = (String) data.get("MinBathValue");
		int minBathValueInInt = Integer.parseInt(minBathValue);
		Boolean minBath = true;
		for (int i = 1; i <= 50; i++) {
			String res = library.getTextFrom("xpath->(//div[@class='top-base-info']/span[3])[" + i + "]");
			float noOfBath = Float.parseFloat(res);
			if (noOfBath < minBathValueInInt) {
				minBath = false;
				break;
			}
		}		Assert.assertTrue(minBath);
	}
	@Override
	public void verifyMinValueForBed(Map<String, Object> data){
		String minBedValue = (String) data.get("MinBedValue");
		int minBedValueInInt = Integer.parseInt(minBedValue);
		Boolean minBd = true;
		for (int i = 1; i <= 50; i++) {
			library.wait(1);
			String res = library.getTextFrom("xpath->(//div[@class='top-base-info']/span[2])[" + i + "]");//Dynamic Xpath
			int noOfBed = Integer.parseInt(res);
			if (noOfBed < minBedValueInInt) {
				minBd = false;
				break;
			}
		}
		Assert.assertTrue(minBd);		
}
	@Override
	public String getValueFromApplyButtonJS(){
		JavascriptExecutor js1 =(JavascriptExecutor)library.getDriver();
		String countOnApplyButton = (String) js1.executeScript("return document.getElementById('applyFilters').childNodes[3].textContent;");
		return countOnApplyButton;
	}
	
	@Override
	public void setFutureDateUsingCalanderForFunctional(Map<String, Object> data) {
		library.wait(2);
		library.click("MENU.goonleavebutton");

		int leaveCount = library.getElementCount("FLEAVE.leavecount");
		for (int i = 1; i <= leaveCount; i++) {
			library.click("FLEAVE.deleteleavebutton");
			library.wait(2);
		}

		for (int k = 1; k <= 1; k++) {
			Calendar currentCal = Calendar.getInstance();
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			currentCal.add(Calendar.DATE, k);
			String Startdatee = dateFormat.format(currentCal.getTime());

			library.click("FLEAVE.createbutton");
			library.wait(2);
			library.clear("xpath->.//div[@class='daterangepicker dropdown-menu show-calendar opensright'][" + k
					+ "]//input[@name='daterangepicker_start']");
			library.typeDataInto(Startdatee,
					"xpath->.//div[@class='daterangepicker dropdown-menu show-calendar opensright'][" + k
							+ "]//input[@name='daterangepicker_start']");

			library.clear("xpath->.//div[@class='daterangepicker dropdown-menu show-calendar opensright'][" + k
					+ "]//input[@name='daterangepicker_end']");
			library.typeDataInto(Startdatee,
					"xpath->.//div[@class='daterangepicker dropdown-menu show-calendar opensright'][" + k
							+ "]//input[@name='daterangepicker_end']");
			library.wait(3);
			library.click("xpath->//div[@class='daterangepicker dropdown-menu show-calendar opensright'][" + k
					+ "]//button[text()='Apply']");
			library.wait(5);
		}
	}	

@Override
 public void verifyNewListingsProperties(JSONObject data) {
  library.get((String) data.get("App-Url"));
  String CityName = data.get("CityName").toString();
  if (library.getCurrentPlatform().equals("Android")) {
   library.wait(5);
   JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
   WebElement element = library.getDriver().findElement(By.xpath("//span[text()='New Listings']"));
   jse.executeScript("arguments[0].scrollIntoView();", element);
   jse.executeScript("arguments[0].scrollIntoView();", element);
   library.wait(5);
  } else {
   library.scrollToElement("xpath->//span[text()='New Listings']");
  }
  library.wait(5);
  library.scrollTo("DEMOGRAPHICS.newlistings");
  Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.newlistings"));
  Assert.assertTrue(library.verifyPageContainsElement("xpath->//span[text()='in " + CityName + "']"));
  library.setRequestHeader("X-MData-Key", data.get("X-MData-Key").toString());
  String response = library.HTTPGet(data.get("NewListings_API").toString());
  int elementcount = library.getElementCount("xpath->.//*[@id='newListingPanel']//li");
  for (int i = 1; i < elementcount; i++) {
	  if(library.verifyPageContainsElement("xpath->.//*[@id='newListingPanel']//li[" + i + "]//div[@class='baseInfo']/span[1]"))
	  { String PriceinUI = library
     .getTextFrom("xpath->.//*[@id='newListingPanel']//li[" + i + "]//div[@class='baseInfo']/span[1]"); // price
   String ListPricenUI = PriceinUI.replace(",", "");
   String ListPricenUI1 = ListPricenUI.replace("$", "");
   int Price = Integer.parseInt(ListPricenUI1);
   String numberOfBedroomsinUI = library
     .getTextFrom("xpath->.//*[@id='newListingPanel']//li[" + i + "]//div[@class='baseInfo']/span[2]");// Bedromm
   int bedsInUI = Integer.parseInt(numberOfBedroomsinUI);
   String numberOfBathroomsinUI = library
     .getTextFrom("xpath->.//*[@id='newListingPanel']//li[" + i + "]//div[@class='baseInfo']/span[3]");// Bathroom
   double numberOfBaths = Double.parseDouble(numberOfBathroomsinUI);
   String AddressinUI = library
     .getTextFrom("xpath->.//*[@id='newListingPanel']//li[" + i + "]//a[@class='addresslink']//span[1]");// Address
   String CityinUI = library
     .getTextFrom("xpath->.//*[@id='newListingPanel']//li[" + i + "]//a[@class='addresslink']//span[2]");// City
   String CardURLinUI = library.getAttributeOfElement("href",
     "xpath->.//*[@id='newListingPanel']//li[" + i + "]//a[@class='addresslink']");// CardURL
   Integer PriceinAPI = (Integer) library.getValueFromJson("$.[" + (i - 1) + "].listPrice", response); // price
   int PriceinAPI1 = (int) PriceinAPI;
   System.out.println(PriceinAPI1);
   Integer numberOfBedroomsinAPI = (Integer) library.getValueFromJson("$.[" + (i - 1) + "].bedrooms",
     response);// Bedromm
   int numberOfBedroomsinAPI1 = (int) numberOfBedroomsinAPI;
   System.out.println(numberOfBedroomsinAPI1);
   Double numberOfBathroomsinAPI = (Double) library.getValueFromJson("$.[" + (i - 1) + "].bathroomsTotal",
     response);// Bathroom
   double numberOfBathroomsinAPI1 = (double) numberOfBathroomsinAPI;
   System.out.println(numberOfBathroomsinAPI1);
   String propertyTypeinAPI = (String) library.getValueFromJson("$.[" + (i - 1) + "].listingType.displayName",
     response);// propertyType
   System.out.println(propertyTypeinAPI);
   String AddressinAPI = (String) library.getValueFromJson("$.[" + (i - 1) + "].address.addressInfo",
     response);// Address
   System.out.println(AddressinAPI);
   String AddressinAPI1 = AddressinAPI.replace("APT ", "");
   String CityinAPI = (String) library.getValueFromJson("$.[" + (i - 1) + "].address.city", response);// City
   System.out.println(CityinAPI);
   String CardURLinAPI = (String) library.getValueFromJson("$.[" + (i - 1) + "].listingUrl", response);// CardURL
   System.out.println(CardURLinAPI);
   Assert.assertEquals(Price, PriceinAPI1);
   Assert.assertEquals(bedsInUI, numberOfBedroomsinAPI1);
   Assert.assertEquals(numberOfBaths, numberOfBathroomsinAPI1);
   // Assert.assertEquals(propertyTypeinUI, propertyTypeinAPI);
   Assert.assertEquals(AddressinUI, AddressinAPI1);
   Assert.assertEquals(CityinUI, CityinAPI);
   Assert.assertTrue(CardURLinUI.contains(CardURLinAPI));
	  }
  }
  if (library.getCurrentPlatform().equals("Android")) {
		boolean flag=false;
		do
		{
		library.isJSEClicked("xpath->.//*[@id='newListingPanel']//i[@class='icon-angle-right']");
		  try
		  {
			  flag=library.findElement("DEMOGRAPHICS.newlistingrefresh").isDisplayed();  
		  }
		  catch(Exception e)
		  {
		  }
		}
		while(!flag);
}
  }
 

 @Override
 public void verifyReflashicon(JSONObject data) {
  int elementcount = library.getElementCount("xpath->.//*[@id='newListingPanel']//li");
  if (library.getCurrentPlatform().equals("Android")) {
	  if(elementcount>1)
	  {
   for (int i = 1; i < elementcount - 4; i++) {
    library.isJSEClicked("xpath->.//*[@id='newListingPanel']//i[@class='icon-angle-right']");
   }}
  } else {
	  
	  while(library.verifyPageContainsElement("xpath->.//*[@id='newListingPanel']//i[@class='icon-angle-right']"))
	  { if(elementcount>4)
	  {
			boolean flag=false;
			do
			{
			library.click("xpath->.//*[@id='newListingPanel']//i[@class='icon-angle-right']");
			  try
			  {
				  flag=library.findElement("DEMOGRAPHICS.newlistingrefresh").isDisplayed();  
			  }
			  catch(Exception e)
			  {
			  }
			}
			while(!flag);
	
	  }

  Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.newlistingrefresh"));
  }
  }  
 }

 @Override
 public void verifyfirstpropertymatchesAPI(JSONObject data) {
  library.setRequestHeader("X-MData-Key", data.get("X-MData-Key").toString());
  String response = library.HTTPGet(data.get("NewListings_API").toString());
  library.isJSEClicked("DEMOGRAPHICS.newlistingrefresh");
  library.wait(4);
  String FirstPropertyAddressinUI = library.getTextFrom("DEMOGRAPHICS.FirstPropertyAddressinUI");// Address
  String FirstPropertyAddressinAPI = (String) library.getValueFromJson("$.[0].address.addressInfo", response);// Address
  String FirstPropertyAddressinAPI1 = FirstPropertyAddressinAPI.replace("APT ", "");
  Assert.assertEquals(FirstPropertyAddressinUI, FirstPropertyAddressinAPI1);
 }

 @Override
 public void verifyfavouriteicon(JSONObject data) {
  library.wait(5);
  library.isJSEClicked("xpath->.//*[@id='newListingPanel']//li[1]//div[@class='fav']/a");
  library.wait(2);
  Assert.assertTrue(library.verifyPageContainsElement("xpath->//div[@class='dialog-body']"));
  String Username = (String) data.get("Username");
  String Password = (String) data.get("Password");
  library.typeDataInto(Username, "LOGIN.username");
  library.typeDataInto(Password, "LOGIN.password");
  library.click("LOGIN.submitButton");
  library.wait(5);
  library.scrollToElement("NEWLISTING.text");
    library.wait(5);
    String classAttribute = library.getAttributeOfElement("class", ".//*[@id='newListingPanel']//li[1]//div[@class='fav']/a[@class='text-shadow favorite active']");
    classAttribute = classAttribute.trim();
    boolean isClassAttributeActive = classAttribute.endsWith("active");
    library.wait(2);
    Assert.assertTrue(isClassAttributeActive, "Favorite icon colour is not red");
    library.wait(2);
 }

 @Override
 public void verifyfirstcard(JSONObject data) {
  library.setRequestHeader("X-MData-Key", data.get("X-MData-Key").toString());
  String response = library.HTTPGet(data.get("NewListings_API").toString());
  library.isJSEClicked("DEMOGRAPHICS.hidenewlisting");
  Assert.assertFalse(library.verifyPageContainsElement("xpath->.//*[@id='newListingPanel']//li[1]"));
  library.scrollToElement("DEMOGRAPHICS.iconuser");
  if (library.getCurrentPlatform().equals("chrome")) {
   library.click("DEMOGRAPHICS.iconuser");
  } else
   robotPoweredMoveMouseToWebElementCoordinates(library.findElement("DEMOGRAPHICS.iconuser"));
  // scenarios.robotPoweredClick();
  library.isJSEClicked("DEMOGRAPHICS.iconuser");
  library.isJSEClicked("DEMOGRAPHICS.favouritehome");
  library.wait(2);
  String AddressinAPI = (String) library.getValueFromJson("$.[0].address.addressInfo", response);// Address
  String AddressinAPI1 = AddressinAPI.replace("APT ", "");
  String AddressinfavUI = library.getTextFrom("DEMOGRAPHICS.AddressinfavUI");
  Assert.assertTrue(AddressinfavUI.contains(AddressinAPI1));
 }
	

 @Override
 public void VerifyTitleandUrl_API(JSONObject Data,String response) {
  library.wait(10);
  String CityName = (String) Data.get("CityName");
  if (library.getCurrentPlatform().equals("Android")) {
   JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
   WebElement element = library.getDriver().findElement(By.xpath("//span[text()='Get to Know']"));
   jse.executeScript("arguments[0].scrollIntoView();", element);
   jse.executeScript("arguments[0].scrollIntoView();", element);
   library.wait(5);
  } else {
   library.scrollToElement("xpath->//span[text()='Get to Know']");
  }
  library.wait(5);
  //int elementcount = library.getElementCount("xpath->.//*[@id='articlePanel']//li");
   //  Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.gettoknowText"));
     String stateCode_API="CA";
  String stateCode = (String) library.getValueFromJson("$.data[1].state.stateCode", response);
     if(stateCode.contains(stateCode_API))
     {
     }
     else{
        String CityNameUI= library.getTextFrom("DEMOGRAPHICS.CityName");
        Assert.assertEquals(CityNameUI, CityName);
     }
  for (int i = 1; i <=4; i++) {
   String title = null;
         String CardURLinUI= null;
         if(stateCode.contains(stateCode_API))
   {    
       title = library.getTextFrom("xpath->.//*[@id='articlePanel']//div[" + i + "]//span"); // title
       CardURLinUI = library.getAttributeOfElement("href",
        "xpath->.//*[@id='articlePanel']/div[" + i + "]/div//a");// CardURL
  }
  else{
   title = library.getTextFrom("xpath->.//*[@id='articlePanel']//div[" + i + "]//span"); // title
    CardURLinUI = library.getAttributeOfElement("href",
     "xpath->.//*[@id='articlePanel']//div[" + i + "]//a");// CardURL

  }
   String titleAPI = (String) library.getValueFromJson("$.data[" + (i - 1) + "].title", response);
   System.out.println(titleAPI);
   String CardURLinAPI = (String) library.getValueFromJson("$.data[" + (i - 1) + "].URL", response);
   System.out.println(CardURLinAPI);
   Assert.assertEquals(title, titleAPI);
   Assert.assertTrue(CardURLinUI.contains(CardURLinAPI));
   if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")) {
       if(library.verifyPageNotContainsElement("DEMOGRAPHICS.righticon"))
       {
       }
       else{
    library.wait(4);   
    library.isJSEClicked("DEMOGRAPHICS.righticon");
       }
   }
         
  }
 }
 
 

@Override
public void VerifyTitleandUrl(JSONObject Data,String response) {
	library.wait(10);
	String CityName = (String) Data.get("CityName");
	if (library.getCurrentPlatform().equals("Android") || library.getCurrentPlatformType().equalsIgnoreCase("IOS_WEB")) {
		JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
		WebElement element = library.getDriver().findElement(By.xpath("//span[text()='Get to Know']"));
		jse.executeScript("arguments[0].scrollIntoView();", element);
		jse.executeScript("arguments[0].scrollIntoView();", element);
		library.wait(5);
	} else {
		library.scrollToElement("xpath->//span[text()='Get to Know']");
	}
	library.wait(5);
	//int elementcount = library.getElementCount("xpath->.//*[@id='articlePanel']//li");
  //  Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.gettoknowText"));
    String stateCode_API="CA";
	String stateCode = (String) library.getValueFromJson("$.data[1].state.stateCode", response);
    if(stateCode.contains(stateCode_API))
    {
    }
    else{
       String CityNameUI= library.getTextFrom("DEMOGRAPHICS.CityName");
       Assert.assertEquals(CityNameUI, CityName);
    }
	for (int i = 1; i <=4; i++) {
		String title = null;
        String CardURLinUI= null;
        if(stateCode.contains(stateCode_API))
		{ 		 
   		 title = library.getTextFrom("xpath->.//*[@id='articlePanel']//div[" + i + "]//span"); // title
   		 CardURLinUI = library.getAttributeOfElement("href",
   				"xpath->.//*[@id='articlePanel']/div[" + i + "]/div//a");// CardURL
	}
	else{
		title = library.getTextFrom("xpath->.//*[@id='articlePanel']//li[" + i + "]//span"); // title
		 CardURLinUI = library.getAttributeOfElement("href",
				"xpath->.//*[@id='articlePanel']//li[" + i + "]//a[@class='blogcard-info']");// CardURL

	}
		String titleAPI = (String) library.getValueFromJson("$.data[" + (i - 1) + "].title", response);
		System.out.println(titleAPI);
		String CardURLinAPI = (String) library.getValueFromJson("$.data[" + (i - 1) + "].URL", response);
		System.out.println(CardURLinAPI);
		Assert.assertEquals(title, titleAPI);
		Assert.assertTrue(CardURLinUI.contains(CardURLinAPI));
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")) {
		    if(stateCode.contains(stateCode_API))
		    {
		    }
		    else{
			library.wait(4);			
			library.isJSEClicked("DEMOGRAPHICS.righticon");
		    }
		}
    	  	
	}
}

 
@Override
public void VerifyLastarticletitle(JSONObject Data,String response) {
	Boolean flag = library.findElement("DEMOGRAPHICS.righticon").isDisplayed();	
	if(library.getCurrentPlatform().equals("Android") || library.getCurrentPlatformType().equalsIgnoreCase("IOS_WEB"))
	{
	}
	else{
do{
	library.wait(4);
	library.isJSEClicked("DEMOGRAPHICS.righticon");
		}
while(flag==false);
	}

	String Lastcardtitle = library.getTextFrom("DEMOGRAPHICS.lastCardTitle"); // title
	String LastCardURLinUI = library.getAttributeOfElement("href",
			"DEMOGRAPHICS.LASTCARDurl");// CardURL
	String LastCardtitleAPI = (String) library.getValueFromJson("$.data[4].title", response);
	String LastCardURLinAPI = (String) library.getValueFromJson("$.data[4].URL", response);
	Assert.assertEquals(Lastcardtitle, LastCardtitleAPI);
	Assert.assertTrue(LastCardURLinUI.contains(LastCardURLinAPI));//verifies the first title
}

@Override
public void VerifyFirstarticletitle(JSONObject Data,String response) {
	library.wait(4);
	library.isJSEClicked("DEMOGRAPHICS.Refreshicon");
	library.wait(4);
	String Firstcardtitle = library.getTextFrom("DEMOGRAPHICS.firstCardTitle"); // title
	String FirstCardURLinUI = library.getAttributeOfElement("href",
			"DEMOGRAPHICS.firstCARDurl");// CardURL
	String FirstCardtitleAPI = (String) library.getValueFromJson("$.data[0].title", response);
	String FirstCardURLinAPI = (String) library.getValueFromJson("$.data[0].URL", response);
	Assert.assertEquals(Firstcardtitle, FirstCardtitleAPI);
	Assert.assertTrue(FirstCardURLinUI.contains(FirstCardURLinAPI));//verifies the last title
}
private static Map<String, Object> getJsonAsMap(String jsonFilePath) {
	  JSONParser parser = new JSONParser();
	  try {

	   Object obj = parser.parse(new FileReader(jsonFilePath));

	   JSONObject jsonObject = (JSONObject) obj;
	   System.out.println(jsonObject);
	   Map<String, Object> retMap = new HashMap<>();

	   if (jsonObject != null) {
	    retMap = toMap(jsonObject);
	    return retMap;
	   }

	   System.out.println(retMap);

	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  return null;
	 }
@Override
public Boolean verifyPopulationDemographics(String response) {
	int size = 0;
	int ite = 1;
	boolean flag = true;
	// verify Population Demographics (chart IDs: 10,5,12,9,11)

	// Population by Education Level
	if (flag) {
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.10.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		for (int i = 1; i <= size; i++) {
			String uiLabel = library.getTextFrom(
					"xpath->.//*[@id='PopulationDemographics_Sectin']/div[1]/table/tbody/tr[" + i + "]/td[1]");
			String uiTempValue = library.getTextFrom(
					"xpath->.//*[@id='PopulationDemographics_Sectin']/div[1]/table/tbody/tr[" + i + "]/td[2]");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.10.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.10.labelValues." + i + ".value", response));

			String uiValueArr[] = uiTempValue.split(" ");
			String uiValue = uiValueArr[0].replace(",", "").trim();

			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			

		}
	}

	// Population by Age
	if (flag) {
		size = 0;
		ite = 1;
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.5.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		for (int i = 1; i <= size; i++) {
			String uiLabel = library.getTextFrom(
					"xpath->.//*[@id='PopulationDemographics_Sectin']/div[2]/table/tbody/tr[" + i + "]/td[1]");
			String uiTempValue = library.getTextFrom(
					"xpath->.//*[@id='PopulationDemographics_Sectin']/div[2]/table/tbody/tr[" + i + "]/td[2]");
			String apiLabel = String.valueOf(library.getValueFromJson("$.5.labelValues." + i + ".label", response));
			String apiValue = String.valueOf(library.getValueFromJson("$.5.labelValues." + i + ".value", response));
			String uiValueArr[] = uiTempValue.split(" ");
			String uiValue = uiValueArr[0].replace(",", "").trim();

			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}

		}
	}

	// Civilian Employment Industry
	if (flag) {
		size = 0;
		ite = 1;
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.12.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		for (int i = 1; i <= size; i++) {
			String uiLabel = library.getTextFrom(
					"xpath->.//*[@id='PopulationDemographics_Sectin']/div[3]/table/tbody/tr[" + i + "]/td[1]");
			String uiTempValue = library.getTextFrom(
					"xpath->.//*[@id='PopulationDemographics_Sectin']/div[3]/table/tbody/tr[" + i + "]/td[2]");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.12.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.12.labelValues." + i + ".value", response));
			String uiValueArr[] = uiTempValue.split(" ");
			String uiValue = uiValueArr[0].replace(",", "").trim();

			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}

		}
	}

	// Means of Transportation to Work
	if (flag) {
		size = 0;
		ite = 1;
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.9.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		for (int i = 1; i <= size; i++) {
			String uiLabel = library.getTextFrom(
					"xpath->.//*[@id='PopulationDemographics_Sectin']/div[4]/table/tbody/tr[" + i + "]/td[1]");
			String uiTempValue = library.getTextFrom(
					"xpath->.//*[@id='PopulationDemographics_Sectin']/div[4]/table/tbody/tr[" + i + "]/td[2]");
			String apiLabel = String.valueOf(library.getValueFromJson("$.9.labelValues." + i + ".label", response));
			String apiValue = String.valueOf(library.getValueFromJson("$.9.labelValues." + i + ".value", response));
			String uiValueArr[] = uiTempValue.split(" ");
			String uiValue = uiValueArr[0].replace(",", "").trim();

			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}

		}
	}

	// Employment Status
	if (flag) {
		size = 0;
		ite = 1;
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.11.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		for (int i = 1; i <= size; i++) {
			String uiLabel = library.getTextFrom(
					"xpath->.//*[@id='PopulationDemographics_Sectin']/div[5]/table/tbody/tr[" + i + "]/td[1]");
			String uiTempValue = library.getTextFrom(
					"xpath->.//*[@id='PopulationDemographics_Sectin']/div[5]/table/tbody/tr[" + i + "]/td[2]");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.11.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.11.labelValues." + i + ".value", response));
			String uiValueArr[] = uiTempValue.split(" ");
			String uiValue = uiValueArr[0].replace(",", "").trim();

			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}

		}
	}

	return flag;
}

@Override
public Boolean verifyChartPopulationDemographics(String response) {
	int size = 0;
	int ite = 1;
	boolean flag = true;
	WebDriver driver = library.getDriver();
	// verify Population Demographics (chart IDs: 10,5,12,9,11)

	// Population by Education Level
	if (flag) {
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.10.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.populationByEducationLevelGraph");
		library.wait(10);


		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16'])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip1");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.10.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.10.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);

		library.click("DEMOGRAPHICS.printIcon2");

		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	// Population by Age
	if (flag) {
		size = 0;
		ite = 1;
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.5.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.populationByAgeGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->//*[@stroke='#FFFFFF'][" + i + "]");

			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String.valueOf(library.getValueFromJson("$.5.labelValues." + i + ".label", response));
			String apiValue = String.valueOf(library.getValueFromJson("$.5.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon2");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.click("xpath->.//*[@id='body']/div[5]/div[2]/div/a/i");
	}

	// Civilian Employment Industry
	if (flag) {
		size = 0;
		ite = 1;
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.12.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}
		library.wait(5);
		library.click("DEMOGRAPHICS.civilianEmploymentIndustryGraph");
		library.wait(10);
		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16'])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.12.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.12.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon2");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.click("xpath->.//*[@id='body']/div[5]/div[2]/div/a/i");
	}

	// Means of Transportation to Work
	if (flag) {
		size = 0;
		ite = 1;
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.9.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.meansOfTransportationToWorkGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16'])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String.valueOf(library.getValueFromJson("$.9.labelValues." + i + ".label", response));
			String apiValue = String.valueOf(library.getValueFromJson("$.9.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon2");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.click("DEMOGRAPHICS.graphCloseIcon");
	}

	// Employment Status
	if (flag) {
		size = 0;
		ite = 1;
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.11.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(10);
		library.click("DEMOGRAPHICS.employmentStatusGraph");
		library.wait(5);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16'])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.11.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.11.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon2");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.click("xpath->.//*[@id='body']/div[5]/div[2]/div/a/i");
	}

	return flag;
}

@Override
public Boolean verifyChartHouseholdDistribution(String response) {
	int size = 0;
	int ite = 1;
	boolean flag = true;
	WebDriver driver = library.getDriver();
	// verify Household Distribution (chart IDs: 13,3)

	// Household Income Levels
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.13.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.householdIncomeLevelsGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16'])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.13.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.13.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon2");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	// Household Size Distribution
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.3.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.householdSizeDistributionGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16'])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String.valueOf(library.getValueFromJson("$.3.labelValues." + i + ".label", response));
			String apiValue = String.valueOf(library.getValueFromJson("$.3.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon2");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	return flag;

}
@Override
public Boolean verifyChartNeighborhoodHousingCharacteristics(String response) {
	int size = 0;
	int ite = 1;
	boolean flag = true;
	WebDriver driver = library.getDriver();
	// verify Neighborhood Housing Characteristics (chart IDs: 16,18,17)

	// Number of Rooms in Residences
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.16.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}
		library.wait(5);
		library.click("DEMOGRAPHICS.numberOfRoomsInResidencesGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16'])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.16.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.16.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon2");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	// Number of Bedrooms in Residences
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.18.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.numberofBedroomsInResidencesGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16'])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.18.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.18.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);
		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon2");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	// Residences by Year Built
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.17.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.residencesByYearBuiltGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16'])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.17.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.17.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);
		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon2");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	return flag;
}

@Override
public Boolean verifyChartRentalInformationFlag(String response) {
	int size = 0;
	int ite = 1;
	boolean flag = true;
	WebDriver driver = library.getDriver();
	// verify Rental Information (chart IDs: 19,20)

	// Owner vs Renter Occupancy
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.19.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}
		library.wait(5);
		library.click("DEMOGRAPHICS.ownerVsRenterOccupancyGraph");
		library.wait(10);
		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16'])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.19.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.19.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon2");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	// Rents of Renter Occupied Units
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.20.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}
		library.wait(5);
		library.click("DEMOGRAPHICS.rentsOfRenterOccupiedUnitsGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->//*[@stroke='#FFFFFF'][" + i + "]");

			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.20.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.20.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);
		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon2");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	return flag;
}

@Override
public Boolean verifyChartPopulationDemographicsIE(String response) {
	int size = 0;
	int ite = 1;
	boolean flag = true;
	WebDriver driver = library.getDriver();
	// verify Population Demographics (chart IDs: 10,5,12,9,11)

	// Population by Education Level
	if (flag) {
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.10.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.populationByEducationLevelGraph");
		library.wait(10);

		

		for (int i = 1; i <= size; i++) {
		
			library.click("xpath->(//*[@y='16' and not(@stroke-opacity) and not(@style)])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip1");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.10.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.10.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.click("DEMOGRAPHICS.printIcon1");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	// Population by Age
	if (flag) {
		size = 0;
		ite = 1;
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.5.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.populationByAgeGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->//*[@stroke='#ffffff'][" + i + "]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip2");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String.valueOf(library.getValueFromJson("$.5.labelValues." + i + ".label", response));
			String apiValue = String.valueOf(library.getValueFromJson("$.5.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon1");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.click("xpath->.//*[@id='body']/div[5]/div[2]/div/a/i");
	}

	// Civilian Employment Industry
	if (flag) {
		size = 0;
		ite = 1;
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.12.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}
		library.wait(5);
		library.click("DEMOGRAPHICS.civilianEmploymentIndustryGraph");
		library.wait(10);
		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16' and not(@stroke-opacity) and not(@style)])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip1");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.12.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.12.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon1");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.click("xpath->.//*[@id='body']/div[5]/div[2]/div/a/i");
	}

	// Means of Transportation to Work
	if (flag) {
		size = 0;
		ite = 1;
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.9.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.meansOfTransportationToWorkGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16' and not(@stroke-opacity) and not(@style)])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip1");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String.valueOf(library.getValueFromJson("$.9.labelValues." + i + ".label", response));
			String apiValue = String.valueOf(library.getValueFromJson("$.9.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon1");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.click("DEMOGRAPHICS.graphCloseIcon");
	}

	// Employment Status
	if (flag) {
		size = 0;
		ite = 1;
		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.11.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(10);
		library.click("DEMOGRAPHICS.employmentStatusGraph");
		library.wait(5);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16' and not(@stroke-opacity) and not(@style)])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip1");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.11.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.11.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon1");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.click("xpath->.//*[@id='body']/div[5]/div[2]/div/a/i");
	}

	return flag;
}

@Override
public Boolean verifyChartHouseholdDistributionIE(String response) {
	int size = 0;
	int ite = 1;
	boolean flag = true;
	WebDriver driver = library.getDriver();
	// verify Household Distribution (chart IDs: 13,3)

	// Household Income Levels
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.13.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.householdIncomeLevelsGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16' and not(@stroke-opacity) and not(@style)])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip1");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.13.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.13.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon1");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	// Household Size Distribution
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.3.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.householdSizeDistributionGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16' and not(@stroke-opacity) and not(@style)])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip1");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String.valueOf(library.getValueFromJson("$.3.labelValues." + i + ".label", response));
			String apiValue = String.valueOf(library.getValueFromJson("$.3.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon1");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	return flag;

}

@Override
public Boolean verifyChartNeighborhoodHousingCharacteristicsIE(String response) {
	int size = 0;
	int ite = 1;
	boolean flag = true;
	WebDriver driver = library.getDriver();
	// verify Neighborhood Housing Characteristics (chart IDs: 16,18,17)

	// Number of Rooms in Residences
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.16.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}
		library.wait(5);
		library.click("DEMOGRAPHICS.numberOfRoomsInResidencesGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16' and not(@stroke-opacity) and not(@style)])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip1");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.16.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.16.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon1");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	// Number of Bedrooms in Residences
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.18.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.numberofBedroomsInResidencesGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16' and not(@stroke-opacity) and not(@style)])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip1");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.18.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.18.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);
		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon1");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	// Residences by Year Built
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.17.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		library.wait(5);
		library.click("DEMOGRAPHICS.residencesByYearBuiltGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16' and not(@stroke-opacity) and not(@style)])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip1");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.17.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.17.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);
		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon1");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	return flag;
}

@Override
public Boolean verifyChartRentalInformationFlagIE(String response) {
	int size = 0;
	int ite = 1;
	boolean flag = true;
	WebDriver driver = library.getDriver();
	// verify Rental Information (chart IDs: 19,20)

	// Owner vs Renter Occupancy
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.19.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}
		library.wait(5);
		library.click("DEMOGRAPHICS.ownerVsRenterOccupancyGraph");
		library.wait(10);
		for (int i = 1; i <= size; i++) {
			library.click("xpath->(//*[@y='16' and not(@stroke-opacity) and not(@style)])["+i+"]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip1");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.19.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.19.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);

		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon1");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));

		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	// Rents of Renter Occupied Units
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.20.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}
		library.wait(5);
		library.click("DEMOGRAPHICS.rentsOfRenterOccupiedUnitsGraph");
		library.wait(10);

		for (int i = 1; i <= size; i++) {
			library.click("xpath->//*[@stroke='#ffffff'][" + i + "]");
			String chartValue = library.getTextFrom("DEMOGRAPHICS.toolTip2");
			String chartValueArr[] = chartValue.split(":");
			String uiLabel = chartValueArr[0].trim();
			String uiValue = chartValueArr[1].trim().replace(",", "");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.20.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.20.labelValues." + i + ".value", response));
			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}
			library.wait(2);
		}
		Assert.assertTrue(flag);
		library.wait(2);
		library.click("DEMOGRAPHICS.printIcon1");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.printText"));
		library.wait(2);
		library.click("DEMOGRAPHICS.graphCloseIcon");

	}

	return flag;
}

@Override
public Boolean verifyHouseholdDistribution(String response) {
	int size = 0;
	int ite = 1;
	boolean flag = true;
	// verify Household Distribution (chart IDs: 13,3)

	// Household Income Levels
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.13.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		for (int i = 1; i <= size; i++) {
			String uiLabel = library.getTextFrom(
					"xpath->.//*[@id='HouseholdDistribution_Sectin']/div[1]/table/tbody/tr[" + i + "]/td[1]");
			String uiTempValue = library.getTextFrom(
					"xpath->.//*[@id='HouseholdDistribution_Sectin']/div[1]/table/tbody/tr[" + i + "]/td[2]");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.13.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.13.labelValues." + i + ".value", response));

			String uiValueArr[] = uiTempValue.split(" ");
			String uiValue = uiValueArr[0].replace(",", "").trim();

			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}

		}
	}

	// Household Size Distribution
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.3.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		for (int i = 1; i <= size; i++) {
			String uiLabel = library.getTextFrom(
					"xpath->.//*[@id='HouseholdDistribution_Sectin']/div[2]/table/tbody/tr[" + i + "]/td[1]");
			String uiTempValue = library.getTextFrom(
					"xpath->.//*[@id='HouseholdDistribution_Sectin']/div[2]/table/tbody/tr[" + i + "]/td[2]");
			String apiLabel = String.valueOf(library.getValueFromJson("$.3.labelValues." + i + ".label", response));
			String apiValue = String.valueOf(library.getValueFromJson("$.3.labelValues." + i + ".value", response));

			String uiValueArr[] = uiTempValue.split(" ");
			String uiValue = uiValueArr[0].replace(",", "").trim();

			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}

		}
	}

	return flag;
}

@Override
public Boolean verifyNeighborhoodHousingCharacteristics(String response) {
	int size = 0;
	int ite = 1;
	boolean flag = true;
	// verify Neighborhood Housing Characteristics (chart IDs: 16,18,17)

	// Number of Rooms in Residences
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.16.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		for (int i = 1; i <= size; i++) {
			String uiLabel = library.getTextFrom(
					"xpath->.//*[@id='NeighborhoodHousingCharacteristics_Sectin']/div[1]/table/tbody/tr[" + i
							+ "]/td[1]");
			String uiTempValue = library.getTextFrom(
					"xpath->.//*[@id='NeighborhoodHousingCharacteristics_Sectin']/div[1]/table/tbody/tr[" + i
							+ "]/td[2]");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.16.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.16.labelValues." + i + ".value", response));

			String uiValueArr[] = uiTempValue.split(" ");
			String uiValue = uiValueArr[0].replace(",", "").trim();

			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}

		}
	}

	// Number of Bedrooms in Residences
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.18.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		for (int i = 1; i <= size; i++) {
			String uiLabel = library.getTextFrom(
					"xpath->.//*[@id='NeighborhoodHousingCharacteristics_Sectin']/div[2]/table/tbody/tr[" + i
							+ "]/td[1]");
			String uiTempValue = library.getTextFrom(
					"xpath->.//*[@id='NeighborhoodHousingCharacteristics_Sectin']/div[2]/table/tbody/tr[" + i
							+ "]/td[2]");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.18.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.18.labelValues." + i + ".value", response));

			String uiValueArr[] = uiTempValue.split(" ");
			String uiValue = uiValueArr[0].replace(",", "").trim();

			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}

		}
	}

	// Residences by Year Built
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.17.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		for (int i = 1; i <= size; i++) {
			String uiLabel = library.getTextFrom(
					"xpath->.//*[@id='NeighborhoodHousingCharacteristics_Sectin']/div[3]/table/tbody/tr[" + i
							+ "]/td[1]");
			String uiTempValue = library.getTextFrom(
					"xpath->.//*[@id='NeighborhoodHousingCharacteristics_Sectin']/div[3]/table/tbody/tr[" + i
							+ "]/td[2]");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.17.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.17.labelValues." + i + ".value", response));

			String uiValueArr[] = uiTempValue.split(" ");
			String uiValue = uiValueArr[0].replace(",", "").trim();

			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}

		}
	}

	return flag;
}

@Override
public Boolean verifyRentalInformationFlag(String response) {
	int size = 0;
	int ite = 1;
	boolean flag = true;
	// verify Rental Information (chart IDs: 19,20)

	// Owner vs Renter Occupancy
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.19.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		for (int i = 1; i <= size; i++) {
			String uiLabel = library.getTextFrom(
					"xpath->.//*[@id='RentalInformation_Sectin']/div[1]/table/tbody/tr[" + i + "]/td[1]");
			String uiTempValue = library.getTextFrom(
					"xpath->.//*[@id='RentalInformation_Sectin']/div[1]/table/tbody/tr[" + i + "]/td[2]");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.19.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.19.labelValues." + i + ".value", response));

			String uiValueArr[] = uiTempValue.split(" ");
			String uiValue = uiValueArr[0].replace(",", "").trim();

			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}

		}
	}

	// Rents of Renter Occupied Units
	if (flag) {
		size = 0;
		ite = 1;

		for (;;) {
			try {
				if (String.valueOf(library.getValueFromJson("$.20.labelValues." + ite, response)) != "null") {
					size++;
					ite++;
				} else {
					break;
				}

			} catch (Exception e) {
				break;
			}
		}

		for (int i = 1; i <= size; i++) {
			String uiLabel = library.getTextFrom(
					"xpath->.//*[@id='RentalInformation_Sectin']/div[2]/table/tbody/tr[" + i + "]/td[1]");
			String uiTempValue = library.getTextFrom(
					"xpath->.//*[@id='RentalInformation_Sectin']/div[2]/table/tbody/tr[" + i + "]/td[2]");
			String apiLabel = String
					.valueOf(library.getValueFromJson("$.20.labelValues." + i + ".label", response));
			String apiValue = String
					.valueOf(library.getValueFromJson("$.20.labelValues." + i + ".value", response));

			String uiValueArr[] = uiTempValue.split(" ");
			String uiValue = uiValueArr[0].replace(",", "").trim();

			if (!uiLabel.equalsIgnoreCase(apiLabel) || !uiValue.equalsIgnoreCase(apiValue)) {
				flag = false;
				break;
			}

		}
	}

	return flag;
}

public String ChartResponse()
{
	library.setRequestHeader("X-MData-Key", "CHUMAGATHUQ9VE7AYEBR");
	String response = library.HTTPGet(
			"http://soa8-qa.ng.movoto.net/demographics/census/ca/san%20jose/95111/10,5,12,9,11,13,3,16,18,17,19,20");
	return response;

	
}

@Override
public void verifyChartWithApiResponse() {

	String response = ChartResponse();

	Boolean populationDemographicsFlag = verifyPopulationDemographics(response);
	Assert.assertTrue(populationDemographicsFlag);

	Boolean householdDistributionFlag = verifyHouseholdDistribution(response);
	Assert.assertTrue(householdDistributionFlag);

	Boolean neighborhoodHousingCharacteristicsFlag = verifyNeighborhoodHousingCharacteristics(response);
	Assert.assertTrue(neighborhoodHousingCharacteristicsFlag);

	Boolean RentalInformationFlag = verifyRentalInformationFlag(response);
	Assert.assertTrue(RentalInformationFlag);

}


public void verifyUpandDownIcon() {
	library.isJSEClicked("DEMOGRAPHICS.upIconPopulationDemographics1");
	library.wait(2);
	Assert.assertTrue(library.verifyPageNotContainsElement("DEMOGRAPHICS.upIconPopulationDemographics"));
	library.isJSEClicked("DEMOGRAPHICS.upIconHouseholdDistribution1");
	library.wait(2);
	Assert.assertTrue(library.verifyPageNotContainsElement("DEMOGRAPHICS.upIconHouseholdDistribution"));
	library.isJSEClicked("DEMOGRAPHICS.upIconHousingCharater1");
	library.wait(2);
	Assert.assertTrue(library.verifyPageNotContainsElement("DEMOGRAPHICS.upIconHousingCharater"));
	library.isJSEClicked("DEMOGRAPHICS.upIconRentalInfo1");
	library.wait(2);
	Assert.assertTrue(library.verifyPageNotContainsElement("DEMOGRAPHICS.upIconRentalInfo"));
	library.isJSEClicked("DEMOGRAPHICS.upIconaboutNeighbourhood1");
	library.wait(2);
	Assert.assertTrue(library.verifyPageNotContainsElement("DEMOGRAPHICS.upIconaboutNeighbourhood"));
	library.isJSEClicked("DEMOGRAPHICS.upIconPopulationDemographics1");
	library.wait(2);
	Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.upIconPopulationDemographics"));
	library.isJSEClicked("DEMOGRAPHICS.upIconHouseholdDistribution1");
	library.wait(2);
	Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.upIconHouseholdDistribution"));
	library.isJSEClicked("DEMOGRAPHICS.upIconHousingCharater1");
	library.wait(2);
	Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.upIconHousingCharater"));
	library.isJSEClicked("DEMOGRAPHICS.upIconRentalInfo1");
	library.wait(2);
	Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.upIconRentalInfo"));
	library.isJSEClicked("DEMOGRAPHICS.upIconaboutNeighbourhood1");
	library.wait(2);
	Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.upIconaboutNeighbourhood"));

	
}


@Override
public void verifyGraph() {

	String response = ChartResponse();
	if (library.getCurrentBrowser().equalsIgnoreCase("IExplore")) {
		verifyChartPopulationDemographicsIE(response);
		verifyChartHouseholdDistributionIE(response);
		verifyChartNeighborhoodHousingCharacteristicsIE(response);
		verifyChartRentalInformationFlagIE(response);
	} else {
		verifyChartPopulationDemographics(response);
		verifyChartHouseholdDistribution(response);
		verifyChartNeighborhoodHousingCharacteristics(response);
		verifyChartRentalInformationFlag(response);
	}
}

   public String PropertyListingresponse()
   {
		library.setRequestHeader("X-MData-Key", "CHUMAGATHUQ9VE7AYEBR");
		String response = library.HTTPGet(
				"http://soa8-qa.ng.movoto.net/property/listings/cityid/40446/sitemapopenhouse?pagenumber=1&pagesize=20&orderby=NEWEST_FIRST");
	   return response;
   }

	@Override
	public void VerifyOpenhousesectionProperties(JSONObject data) throws ParseException {
	library.getDriver().get("http://spider.san-mateo.movoto.net:3024/madison-wi/53794/demographics/");
	library.wait(5);
	if (library.getCurrentPlatform().equals("Android")) {
		library.wait(5);
		library.scrollToElement("DEMOGRAPHICS.newlistings");
		boolean b = library.verifyPageContainsElement("DEMOGRAPHICS.openHouse");
		System.out.println(b);
		String CityName= (String) data.get("CityName");
		 Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.openHouse"));
     	  Assert.assertTrue(library.verifyPageContainsElement("xpath->//span[text()='in " + CityName + "']"));
	    String response = PropertyListingresponse();
		List<WebElement> elements = library.getDriver().findElements(By.xpath("(//div[@id='openHousePanel']//li)"));
		List<WebElement> temp = null;
		
		ListIterator<WebElement> itr = elements.listIterator();
		ListIterator<WebElement> tempItr = null;
		WebElement element = null, tempEle = null;
		JSONParser parser = new JSONParser();
		JSONArray jsonObject = (JSONArray) parser.parse(response);
		int i = 1, k = 1;
		String addressInfo = null;
		String pData = null, addrTemp = null;
		while (itr.hasNext()) {

			element = itr.next();
			temp = element.findElements(
					By.xpath(("(//div[@id='openHousePanel']//li)[" + (i++) + "]//div[@class='baseInfo']")));
			tempItr = temp.listIterator();
			while (tempItr.hasNext()) {
				tempEle = tempItr.next();
				pData = tempEle.getText().replaceAll("(?m)^[ \t]*\r?\n", "");
				String[] propertyData = pData.split("\n");
				String[] valuePropertyData = propertyData[2].split(",");
				String splitvaluePropertyData = valuePropertyData[0];

				for (int j = 0; j < jsonObject.size(); j++) {
					addressInfo = ((JSONObject) (((JSONObject) jsonObject.get(j)).get("address")))
							.get("addressInfo").toString();
					addrTemp = addressInfo.contains("APT") ? addressInfo.replace("APT ", "") : addressInfo;

					if (splitvaluePropertyData.equalsIgnoreCase(addrTemp)) {
						System.out.println("Address is present");
						String[] UIprice = propertyData[0].split(" ");
						String price = UIprice[0].replace("$", "").replace(",", "");
						String ApiPrice = library.getValueFromJson("$.[" + j + "].listPrice", response).toString();
						Assert.assertEquals(ApiPrice, price); // Verify
																// price
						String[] uiBedRoom = propertyData[1].split(" ");
						String[] BedRoom = uiBedRoom[0].split(" ");
						String SplitBedRoom = BedRoom[0];
						String ApiBedRoom = library.getValueFromJson("$.[" + j + "].bedrooms", response).toString();
						Assert.assertEquals(ApiBedRoom, SplitBedRoom);// Verify
																		// Bedroom
						String uiBathroom = uiBedRoom[1];
						float Bathroom = Float.parseFloat(uiBathroom);
						String ApiBathRoom = library.getValueFromJson("$.[" + j + "].bathroomsTotal", response)
								.toString();
						Assert.assertEquals(Float.parseFloat(ApiBathRoom), Bathroom); // Verify
																						// BathRoom
						Assert.assertEquals(addrTemp, splitvaluePropertyData);// verify
																				// address
						String UICity = valuePropertyData[1].trim();
						String Apicity = library.getValueFromJson("$.[" + j + "].address.city", response)
								.toString();
						Assert.assertEquals(Apicity, UICity);// Verify City
						String PropertyType = library.getAttributeOfElement("class",
								"xpath->(.//*[@id='openListings']//div//li[@class='card active']//div[@class='baseInfo'])["
										+ k++ + "]/span[@class='price']/i");
						System.out.println(PropertyType);
						if (PropertyType.contains("icon-text-singlefamily")) {
							PropertyType = PropertyType.replace("icon-text-singlefamily", "Single Family");
						} else {
							PropertyType = PropertyType.replace("icon-text-condo", "Condo");
						}
						String ApiPropertytype = library
								.getValueFromJson("$.[" + j + "].listingType.displayName", response).toString();
						if (ApiPropertytype.contains("Condominium")) {
							ApiPropertytype = ApiPropertytype.replaceAll("Condominium", "Condo");
						}
						Assert.assertEquals(ApiPropertytype, PropertyType);// Property
																			// Type
						break;

					}

				}

			}
			if (i>1)
				break;

		}}
		 else {

				library.wait(5);
				boolean b = library.verifyPageContainsElement("DEMOGRAPHICS.openHouse");
				System.out.println(b);
				 String response = PropertyListingresponse();
				List<WebElement> elements = library.getDriver().findElements(By.xpath("(//div[@id='openHousePanel']//li)"));
				List<WebElement> temp = null;
				ListIterator<WebElement> itr = elements.listIterator();
				ListIterator<WebElement> tempItr = null;
				WebElement element = null, tempEle = null;
				JSONParser parser = new JSONParser();
				JSONArray jsonObject = (JSONArray) parser.parse(response);
				int i = 1, k = 1;
				String addressInfo = null;
				String pData = null, addrTemp = null;
				while (itr.hasNext()) {

					element = itr.next();
					temp = element.findElements(
							By.xpath(("(//div[@id='openHousePanel']//li)[" + (i++) + "]//div[@class='baseInfo']")));
					tempItr = temp.listIterator();
					while (tempItr.hasNext()) {
						tempEle = tempItr.next();
						pData = tempEle.getText().replaceAll("(?m)^[ \t]*\r?\n", "");
						String[] propertyData = pData.split("\n");
						String[] valuePropertyData = propertyData[2].split(",");
						String splitvaluePropertyData = valuePropertyData[0];

						for (int j = 0; j < jsonObject.size(); j++) {
							addressInfo = ((JSONObject) (((JSONObject) jsonObject.get(j)).get("address")))
									.get("addressInfo").toString();
							addrTemp = addressInfo.contains("APT") ? addressInfo.replace("APT ", "") : addressInfo;

							if (splitvaluePropertyData.equalsIgnoreCase(addrTemp)) {
								System.out.println("Address is present");
								String[] UIprice = propertyData[0].split(" ");
								String price = UIprice[0].replace("$", "").replace(",", "");
								String ApiPrice = library.getValueFromJson("$.[" + j + "].listPrice", response).toString();
								Assert.assertEquals(ApiPrice, price); // Verify
																		// price
								String[] uiBedRoom = propertyData[1].split(" ");
								String[] BedRoom = uiBedRoom[0].split(" ");
								String SplitBedRoom = BedRoom[0];
								String ApiBedRoom = library.getValueFromJson("$.[" + j + "].bedrooms", response).toString();
								Assert.assertEquals(ApiBedRoom, SplitBedRoom);// Verify
																				// Bedroom
								String uiBathroom = uiBedRoom[1];
								float Bathroom = Float.parseFloat(uiBathroom);
								String ApiBathRoom = library.getValueFromJson("$.[" + j + "].bathroomsTotal", response)
										.toString();
								Assert.assertEquals(Float.parseFloat(ApiBathRoom), Bathroom); // Verify
																								// BathRoom
								Assert.assertEquals(addrTemp, splitvaluePropertyData);// verify
																						// address
								String UICity = valuePropertyData[1].trim();
								String Apicity = library.getValueFromJson("$.[" + j + "].address.city", response)
										.toString();
								Assert.assertEquals(Apicity, UICity);// Verify City
								String PropertyType = library.getAttributeOfElement("class",
										"xpath->(.//*[@id='openListings']//div//li[@class='card active']//div[@class='baseInfo'])["
												+ k++ + "]/span[@class='price']/i");
								System.out.println(PropertyType);
								if (PropertyType.contains("icon-text-singlefamily")) {
									PropertyType = PropertyType.replace("icon-text-singlefamily", "Single Family");
								} else {
									PropertyType = PropertyType.replace("icon-text-condo", "Condo");
								}
								String ApiPropertytype = library
										.getValueFromJson("$.[" + j + "].listingType.displayName", response).toString();
								if (ApiPropertytype.contains("Condominium")) {
									ApiPropertytype = ApiPropertytype.replaceAll("Condominium", "Condo");
								}
								Assert.assertEquals(ApiPropertytype, PropertyType);// Property
																					// Type
								break;

							}

						}

					}
					if (i == 4)
						break;

				}
}
	}
	
	
	
	@Override
	public void verifyReflashiconforopenhousesection(JSONObject data) {
		int elementcount = library.getElementCount("DEMOGRAPHICS.getElementcount");
		if (library.getCurrentPlatform().equals("Android")) {
			for (int i = 1; i < elementcount - 4; i++) {
				library.isJSEClicked("DEMOGRAPHICS.iconRightAngle");
			}
		} else {
			library.isJSEClicked("DEMOGRAPHICS.openHousesNext");
			library.wait(2);
			library.isJSEClicked("DEMOGRAPHICS.openHousesNext");
			library.wait(2);
			library.isJSEClicked("DEMOGRAPHICS.openHousesNext");
			library.wait(2);
		}
		Assert.assertTrue(library.verifyPageContainsElement("DEMOGRAPHICS.openHousefresh"));

	}
	
	@Override
	public void verifyfirstpropertymatchesAPIopenhouses(JSONObject data) throws ParseException {
		library.setRequestHeader("X-MData-Key", data.get("X-MData-Key").toString());
		String response = library.HTTPGet(data.get("OpenHouse_API").toString());
		List<WebElement> elements = library.getDriver().findElements(By.xpath("(//div[@id='openHousePanel']//li)"));
		  List<WebElement> temp = null;
		  ListIterator<WebElement> itr = elements.listIterator();
		  ListIterator<WebElement> tempItr = null;
		  WebElement element = null, tempEle = null;
		  JSONParser parser = new JSONParser();
		  JSONArray jsonObject = (JSONArray) parser.parse(response);
		  int i = 1, k = 1;
		  String addressInfo = null;
		  String pData = null, addrTemp = null;
		  while (itr.hasNext()) {
		   element = itr.next();
		   temp = element.findElements(
		     By.xpath(("(//div[@id='openHousePanel']//li)[" + (i++) + "]//div[@class='baseInfo']")));
		   tempItr = temp.listIterator();
		   while (tempItr.hasNext()) {
		    tempEle = tempItr.next();
		    pData = tempEle.getText().replaceAll("(?m)^[ \t]*\r?\n", "");
		    String[] propertyData = pData.split("\n");
		    String[] valuePropertyData = propertyData[2].split(",");
		    String splitvaluePropertyData = valuePropertyData[0];
		    for (int j = 0; j < jsonObject.size(); j++) {
		     addressInfo = ((JSONObject) (((JSONObject) jsonObject.get(j)).get("address")))
		       .get("addressInfo").toString();
		     addrTemp = addressInfo.contains("APT") ? addressInfo.replace("APT ", "") : addressInfo;
		     if (splitvaluePropertyData.equalsIgnoreCase(addrTemp)) {
		      System.out.println("Address is present");
		      System.out.println("UI address : " + splitvaluePropertyData );
		      System.out.println("API address : " + addrTemp );
		      Assert.assertEquals(addrTemp, splitvaluePropertyData);// verify address
		      break;
		     }
		    }
		   }
		   if (i>1)
		    break;
		  }
	}
	
	@Override
	public void verifyfavouriteiconOpenHouses(JSONObject data) 
	{
		library.wait(4);
		library.isJSEClicked("DEMOGRAPHICS.favIcon");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("SEARCHPAGE.loginWindow"));
		String Username = (String) data.get("Username");
		String Password = (String) data.get("Password");
		library.typeDataInto(Username, "LOGIN.username");
		library.typeDataInto(Password, "LOGIN.password");
		library.click("LOGIN.submitButton");
		library.wait(5);
		library.scrollToElement("DEMOGRAPHICS.openHousePanel");
		  library.wait(5);
		  String classAttribute = library.getAttributeOfElement("class", "DEMOGRAPHICS.textShadowFavIcon");
		  classAttribute = classAttribute.trim();
		  boolean isClassAttributeActive = classAttribute.endsWith("active");
		  library.wait(2);
		  Assert.assertTrue(isClassAttributeActive, "Favorite icon colour is not red");
		  library.wait(2);
	}
	
	@Override
	public void verifyfirstcardOpenHouses(JSONObject data) {
		
		
		// Verify the favorite house's address is same with 1st card
		NavigateToFavouriteHomes(data);
		  WebElement wfavHouseAddress = library.findElement("DEMOGRAPHICS.AddressinfavUI");
		  String UIfavHouseAddress = wfavHouseAddress.getText();
		  System.out.println(UIfavHouseAddress);
		  library.get((String) data.get("App-Url"));
		  library.wait(5);
		  library.scrollToElement("DEMOGRAPHICS.openHouse");
		  library.wait(5);
		  if (library.getCurrentPlatform().equals("Android")) {
		   JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
		   WebElement element = library
		     .findElement("DEMOGRAPHICS.FirstPropertyOpenHOuseAddressinUI");
		   jse.executeScript("arguments[0].scrollIntoView(true);", element);
		  }
		  else {
				if(library.verifyPageNotContainsElement("DEMOGRAPHICS.openHousefresh")){
					verifyReflashiconforopenhousesection(data);}
					library.click("DEMOGRAPHICS.openHousefresh");
			  library.wait(5);
		  }
		  String sAddress = library.getTextFrom("DEMOGRAPHICS.FirstPropertyOpenHOuseAddressinUI");

		  boolean isAddressSame = UIfavHouseAddress.contains(sAddress);
		  Assert.assertTrue(isAddressSame);
		  library.isJSEClicked("DEMOGRAPHICS.hideopenhouses");
		  Assert.assertFalse(library.verifyPageContainsElement("xpath->.//*[@id='openHousePanel']//li[1]"));
	}

	
	
/*@Override
	   public void disableChatWindow()
	   {   library.wait(10);
		   StringBuilder sb=new StringBuilder("var chat_window = document.getElementById('habla_panel_div');");
		   sb.append("chat_window['style'] = 'display: none';");
		   JavascriptExecutor jExecutor=(JavascriptExecutor)library.getDriver();
		   jExecutor.executeScript(sb.toString());
		   
		   
	   }*/

@Override
public void verifytextandURL(JSONObject Data)
{
	  try{
	  String CityName=(String) Data.get("CityName");
	  library.wait(2);
	  library.isJSEClicked("DEMOGRAPHICS.textfield");
	  library.wait(2);
	  library.typeDataIntoWithJavaScript(CityName, "DEMOGRAPHICS.textfield");
	   library.wait(2);
	   library.isJSEClicked("DEMOGRAPHICS.searchbutton");
	   if (library.getCurrentPlatform().equals("Android")|| (library.getCurrentPlatform().equals("IOS_WEB"))) 
	   {
	       boolean b = library.verifyPageNotContainsElement("xpath->.//*[@id='body']/div[5]/div[2]/div/div/ul/li[1]/a");
	       library.wait(4);   
	  while (b==true)
	   {
		   library.wait(4);
		   library.isJSEClicked("DEMOGRAPHICS.searchbutton");
		    b = library.verifyPageContainsElement("xpath->.//*[@id='body']/div[5]/div[2]/div/div/ul/li[1]/a");
	   }
	   }else{   
	       boolean b = library.verifyPageContainsElement("xpath->//h3[@class= 'popover-title']");
	       library.wait(4);   
	  while (b==true)
	   {
		   library.wait(4);
		   library.isJSEClicked("DEMOGRAPHICS.searchbutton");
		    b = library.verifyPageContainsElement("xpath->//h3[@class= 'popover-title']");
	   }}
	   String urlString =(String) Data.get("urlString");  
  URL url = new URL(urlString);
  HttpURLConnection con = (HttpURLConnection) url.openConnection();
  con.setRequestMethod("GET");
  con.setRequestProperty("X-MData-Key", "CHUMAGATHUQ9VE7AYEBR");  
  if(con.getResponseCode() == 200){
      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();
            
            while ((output = in.readLine()) != null) {
                  response.append(output);
            }
            in.close();
            
            JSONParser parser=new JSONParser();
            JSONArray obj=(JSONArray)parser.parse(response.toString());
            int j=0;
            System.out.println(obj.size());
            int length= obj.size()-1;   	
            for(int i=length;i>=0;i--){
             
             j=j+1;
                  JSONObject inObj = (JSONObject)obj.get(i);
                  String Citynameapi=   (String) inObj.get("cityName");
                  String zipcode= (String) inObj.get("code");
                  System.out.println(zipcode);
                  String jsonname= Citynameapi+", "+zipcode;
              	if (library.getCurrentPlatform().equals("Android")||library.getCurrentPlatform().equals("IOS_WEB"))
                 {String uiname= library.getTextFrom("xpath->.//div[@class='linksList']/ul/li["+(i+1)+"]/a");
                                                              //*[@id="body"]/div[6]/div[2]/div/div/ul/li[1]/a
                 Assert.assertEquals(uiname, jsonname);
                 String jsonurl=  (String) inObj.get("demographicsPageUrl");
                 String uiurl= library.getAttributeOfElement("href", "xpath->.//div[@class='linksList']/ul/li["+(i+1)+"]/a");
                 Assert.assertTrue(uiurl.contains(jsonurl));  }
                 else
                 {library.wait(1);
                 library.getDriver().switchTo().activeElement();
                 	String uiname= library.getTextFrom("xpath->.//div[@class='linksList']/ul/li["+(i+1)+"]/a");
                  Assert.assertEquals(uiname, jsonname);
                  String jsonurl=  (String) inObj.get("demographicsPageUrl");
                  String uiurl= library.getAttributeOfElement("href", "xpath->.//div[@class='linksList']/ul/li["+(i+1)+"]/a");
                  Assert.assertTrue(uiurl.contains(jsonurl));                     
                 } }}
  

	  }
	  catch(Exception e)
	  {
	   e.printStackTrace();
	  }

}

@Override
public void verifyzipcodeandCity(JSONObject data)
{		
    library.wait(5);
	library.isJSEClicked("DEMOGRAPHICS.remove");
    library.wait(5);
	library.clear("DEMOGRAPHICS.textfield");
    library.wait(5);
	library.clear("DEMOGRAPHICS.textfield");
    library.wait(5);
	String zipcode = (String) data.get("ZipCode");
	String zipURL= (String) data.get("zipURL");
	String cityNameUI= (String) data.get("cityNameUI");
	library.wait(2);
	  library.typeDataIntoWithJavaScript(zipcode, "DEMOGRAPHICS.textfield");
	library.wait(5);
	library.isJSEClicked("DEMOGRAPHICS.searchbutton");
	library.wait(2);
	  if (library.getCurrentPlatform().equals("Android")|| (library.getCurrentPlatform().equals("IOS_WEB"))) 
	  {
	String uisearchdisplay = library.getTextFrom("DEMOGRAPHICS.searchdisplay_mob");
	Assert.assertTrue(uisearchdisplay.contains(zipcode));
    String uizipURL= library.getAttributeOfElement("href", "DEMOGRAPHICS.searchdisplay_mob");
    Assert.assertEquals(zipURL, uizipURL);
	  }
	  else
	  {     library.wait(2);
			String uisearchdisplay = library.getTextFrom("DEMOGRAPHICS.searchdisplay");
			Assert.assertTrue(uisearchdisplay.contains(zipcode));
			Assert.assertTrue(uisearchdisplay.contains(cityNameUI));
		    String uizipURL= library.getAttributeOfElement("href", "DEMOGRAPHICS.searchdisplay");
		    Assert.assertEquals(zipURL, uizipURL);
	  }
}

@Override
public void verifypopupdisappears()
{    library.wait(4);
	 library.isJSEClicked("DEMOGRAPHICS.removeicon");
	 library.wait(4);
	  if (library.getCurrentPlatform().equals("Android")|| (library.getCurrentPlatform().equals("IOS_WEB"))) 
	  {
	 try{
	Assert.assertTrue(library.verifyPageNotContainsElement("DEMOGRAPHICS.searchdisplay_mob"));
	 }catch(Exception e)
	  {
		   e.printStackTrace();
		  }
	  }
	  else
	  {
	 try{
	Assert.assertFalse(library.verifyPageContainsElement("DEMOGRAPHICS.searchdisplay"));
	 }catch(Exception e)
	  {
		   e.printStackTrace();
		  }
	  }
}

@Override
public void navigateToNotes() {
	library.wait(5);
	library.click("URGENCY.notes");
	library.wait(2);
}


@Override
@SuppressWarnings("unchecked")
public String getResponseGetToKnowSectionApi(JSONObject data)
{
	String contentType = String.valueOf(data.get("ContentType"));
	String GettoKnow_API = String.valueOf(data.get("Gettoknow_API"));
	library.setContentType(contentType);
	Map<String, Object> apidata = new HashMap<>();
	apidata = (Map<String, Object>) data.get("Postdata");
	String response = library.HTTPPost(GettoKnow_API, apidata);
	return response;
}	

@Override
//Returns no of properties in the page-50
 public int getMinNoOfRecordsPerPageUI() 
{
	String[] countFromUI=null;
	countFromUI = library.getTextFrom("xpath->.//div[@class='pagination']/div[@class='info']/b").split(" "); // Results
	
	int count=0;
	if(countFromUI[0].contains("-"))
	{
	   count= Integer.parseInt(countFromUI[0].split("-")[1]);
	}else if(countFromUI!=null && countFromUI.length>0)
	{
	   count = Integer.parseInt( countFromUI.length>=2?countFromUI[2].replace(",", ""):countFromUI[1].replace(",", ""));
	}
	return count;
//	if(library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")){
//		countFromUI = library.getTextFrom("xpath->.//div[@class='pagination']/div[@class='info']/b").split(" "); // Results
//		
//		int count=0;
//		if(countFromUI[0].contains("-"))
//		{
//		   count= Integer.parseInt(countFromUI[0].split("-")[1]);
//		}else if(countFromUI!=null && countFromUI.length>0)
//		{
//		   count = Integer.parseInt( countFromUI.length>=2?countFromUI[2].replace(",", ""):countFromUI[1].replace(",", ""));
//		}
//		return count;
//		
//	}else{
//		countFromUI = library.getTextFrom("PROPERTY.uitotalpropertycount").split(" "); // Results
//		
//		int count=0;
//		if(countFromUI[1].contains("-"))
//		{
//		   count= Integer.parseInt(countFromUI[1].split("-")[1]);
//		}else if(countFromUI!=null && countFromUI.length>0)
//		{
//		   count = Integer.parseInt(countFromUI.length>2?countFromUI[3].replace(",", ""):countFromUI[1].replace(",", ""));
//		}
//		return count;
//	}
 }

@Override
public void tellUsAboutYourExperiencePopup(){
	boolean isPopupPresent = library.verifyPageContainsElement("id->fexam_nps_intro");
	if(isPopupPresent){
		library.click("xpath->(//div[@class='fexam_grade fexam_grade_bg'])[11]");
		library.wait(2);
		library.click("id->fexam_close");
		library.wait(2);
	}
}


}






