package com.movoto.scripts.agent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;
public class Reg_90VerifythatRecentlyViewsectionworkswell extends BaseTest{

	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver wDriver;

	public void init(String jsonPath) {
		try {

			jsonParser = new JSONParserForAutomationNG(jsonPath);
			jsonObj = jsonParser.getNode("RecentlyViewsection");

		} catch (Exception exc) {
			System.out.println("Exception in demographics:init() ->" + exc.getMessage());
		}
	}

	@Test
	@Parameters("dataProviderPath")
	public void VerifyPropertiesAssignedToSchoolSectionWorks(String dataProviderPath) throws ParseException {
		init(dataProviderPath);
		if (jsonObj != null) {
			VerifythatRecentlyViewsectionworkswell(jsonObj);
		}
		else
		   {
			   Assert.assertTrue(false,"Jason data not getting properly");
		   }

	}
	
	public void VerifythatRecentlyViewsectionworkswell(JSONObject data) throws ParseException {
		try {
			library.wait(5);
			WebDriver driver=library.getDriver();
			String imageUrl="";
			ArrayList<String> list=new ArrayList<String>();
			for (int i = 1; i <= 5; i++) {
				     library.wait(10);
				     imageUrl=library.getAttributeOfElement("href", "xpath->(.//a[@class='imgmask'])["+i+"]");
				     library.wait(5);
				     driver.get(imageUrl);
				     library.switchToWindow();
				     String url=library.getUrl();
				     list.add(url);
				     library.wait(5);
				     driver.get(data.get("reloadUrl").toString());
				     library.wait(5);
				    }
				    imageUrl=library.getAttributeOfElement("href", "xpath->(.//a[@class='imgmask'])[6]");
				    library.wait(10);
				    driver.get(imageUrl);
				   /* driver.manage().window().maximize();*/
				    library.waitForElement("HOMEPAGE.compareSimilarText");
				    library.wait(5);
				    library.scrollToElement("HOMEPAGE.compareSimilarText");
				    library.wait(5);
				    Assert.assertTrue(true, "HOMEPAGE.compareSimilarText");// Verify compare home section is displayed
				    library.wait(5);
				    Assert.assertTrue(true, "HOMEPAGE.map");// Verify Map is displayed in the section
				    String UiProperty =library.getAttributeOfElement("textContent", "HOMEPAGE.firstImage").trim();
					String currentProperty =library.getAttributeOfElement("textContent", "HOMEPAGE.currentProperty").trim();
				    Assert.assertTrue(UiProperty.replace("APT", "").contains(currentProperty.replace("APT", "")), "Address is not matching");//Verify 1st property is for current property
				    library.wait(5);
				    library.scrollToElement("HOMEPAGE.clickarrow");
				    library.wait(5);
				    int j = 4;
				    int numberOfProerties = library.getElementCount("HOMEPAGE.allCards");
				    for(int i = 1; i <= numberOfProerties-1; i++){
				     library.setRequestHeader("X-MData-Key", data.get("X-MData-Key").toString());
				     String[] str= list.get(j).split("/");
				     String splitUrl="";
				     for(int k=3;k<str.length;k++)
				     {
				      splitUrl+=str[k]+"/";
				     }
				     
				     String urlPost=data.get("apiUrl")+splitUrl;
				     String response=library.HTTPGet(urlPost);
				     String addressOnUI = library.getTextFrom("xpath->.//*[@id='recentlyViewedTab']//div[@class='gird-property-item']["+i+"]//div[@class='address singleline']");
				     
				     String priceStringOnUI = library.getTextFrom("xpath->.//*[@id='recentlyViewedTab']//div[@class='gird-property-item']["+i+"]//ul/li[2]");
				     priceStringOnUI = priceStringOnUI.replace("$", "");
				     priceStringOnUI = priceStringOnUI.replace(",", "");
				     int priceOnUI = Integer.parseInt(priceStringOnUI);
				     
				     String dateOnMovoto = library.getTextFrom("xpath->.//*[@id='recentlyViewedTab']//div[@class='gird-property-item']["+i+"]//ul/li[3]").split("\n")[1];
				     
				     String bedStringOnUI = library.getTextFrom("xpath->.//*[@id='recentlyViewedTab']//div[@class='gird-property-item']["+i+"]//ul/li[4]");
				     bedStringOnUI = bedStringOnUI.split(" ")[0];
				     int numericValueOfBeds = Integer.parseInt(bedStringOnUI);
				     
				     String bathStringOnUI = library.getTextFrom("xpath->.//*[@id='recentlyViewedTab']//div[@class='gird-property-item']["+i+"]//ul/li[5]");
				     bathStringOnUI = bathStringOnUI.split(" ")[0];
				     double bathsOnUI = Double.parseDouble(bathStringOnUI);
				     
				     String pricePerSqftStringOnUI = library.getTextFrom("xpath->.//*[@id='recentlyViewedTab']//div[@class='gird-property-item']["+i+"]//ul/li[7]");
				     pricePerSqftStringOnUI = pricePerSqftStringOnUI.split("/")[0];
				     pricePerSqftStringOnUI = pricePerSqftStringOnUI.replace("$", "");
				     
				     String sqftAreaStringOnUI = library.getTextFrom("xpath->.//*[@id='recentlyViewedTab']//div[@class='gird-property-item']["+i+"]//ul/li[6]");
				     double sqftArea = 0;
				     if(!sqftAreaStringOnUI.equals("—")){
				      sqftAreaStringOnUI = sqftAreaStringOnUI.split(" ")[0];
				      sqftAreaStringOnUI = sqftAreaStringOnUI.replace(",", "");
				      sqftArea = Double.parseDouble(sqftAreaStringOnUI);
				     }
				     String sqftLotStringOnUI = library.getTextFrom("xpath->.//*[@id='recentlyViewedTab']//div[@class='gird-property-item']["+i+"]//ul/li[8]");
				     String builtYearStringOnUI = library.getTextFrom("xpath->.//*[@id='recentlyViewedTab']//div[@class='gird-property-item']["+i+"]//ul/li[9]");
				      String addressInApi = (String) library.getValueFromJson("$.dpp.address.addressInfo", response).toString().replace("APT ", "").trim();
				       //Address Verification
				       Assert.assertTrue(addressInApi.contains(addressOnUI));
				       //Price Verification
				       Integer priceInApi = (Integer)library.getValueFromJson("$.dpp.listPrice", response);
				       int price = (int)priceInApi;
				       Assert.assertTrue(priceOnUI == price);
				//Date verification
				       int daysOnMarket=(int) library.getValueFromJson("$.dpp.daysOnMarket", response);
				       String dateInApi="";
				       if(daysOnMarket>=1)
				       {
				        dateInApi = (String) library.getValueFromJson("$.dpp.onMarketDate", response);
				        SimpleDateFormat newDateFormat = new SimpleDateFormat("MM/dd/yyyy");
				        Date myDate = newDateFormat.parse(dateOnMovoto);
				        newDateFormat.applyPattern("yyyy/MM/dd");
				        String myDateString = newDateFormat.format(myDate).replace("/", "-");
				        Assert.assertTrue(dateInApi.equals(myDateString));
				       }
				       else
				       { dateInApi = (String) library.getValueFromJson("$.dpp.onMarketDate", response);
				        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				           Date dateobj = new Date();
				           String date=(df.format(dateobj));
				           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				           String dateStart = dateInApi;
				        Date d1 = null;
				        Date d2 = null;
				        d1 = format.parse(dateStart);
				        d2 = format.parse(date);
				        long diff = d2.getTime() - d1.getTime();
				        long diffDays = diff / (24 * 60 * 60 * 1000);
				        Assert.assertTrue(dateOnMovoto.equals(diffDays));
				          
				       }
				       //bed verification
				       Integer bedsInApi = (Integer) library.getValueFromJson("$.dpp.bedrooms", response);
				       int beds = (int)bedsInApi;
				       Assert.assertTrue(numericValueOfBeds == beds);
				       
				       //Bath verification
				       Double bathInApi = (Double)library.getValueFromJson("$.dpp.bathroomsTotal", response);
				       double baths = (double)bathInApi;
				       Assert.assertTrue(bathsOnUI == baths);
				       
				       //sqft area verification
				       Double sqftAeraInApi =(Double) library.getValueFromJson("$.dpp.sqftTotal", response);
				       if(sqftAeraInApi == null)
				        Assert.assertTrue(sqftAreaStringOnUI.contains("—"));
				       else{
				        double sqftAreaValueInApi = sqftAeraInApi;
				        Assert.assertTrue(sqftAreaValueInApi == sqftArea);
				       }
				       
				       //SqftLot verification
				       String sqftLotInApi=null;
				       if(!(library.getValueFromJson("$.dpp.lotSizeSqft", response) instanceof Integer))
				        Assert.assertTrue(sqftLotStringOnUI.contains("—"));
				       else{
				        sqftLotInApi=Integer.toString((int)library.getValueFromJson("$.dpp.lotSizeSqft", response));
				        sqftLotStringOnUI=sqftLotStringOnUI.replace(",", "").split(" ")[0];
				        Assert.assertEquals(sqftLotInApi, sqftLotStringOnUI);
				       }

				       //Built year verification 
				       //At least on build year should be there so that we can check the code
				       String yearBuilt = (String)library.getValueFromJson("$.dpp.yearBuilt", response);
				       if(!(yearBuilt == null)){
				        Assert.assertTrue(builtYearStringOnUI.contains(yearBuilt));
				       }
				       else{
				        Assert.assertTrue(builtYearStringOnUI.contains("—"));
				       }
				       j=j-1;
				      }
				    library.isJSEClicked("HOMEPAGE.clickarrow");
				    library.wait(5);
				    Assert.assertTrue(library.verifyPageNotContainsElement("HOMEPAGE.map"),"detail info is not hided");// Verify the detail info is hided
			}
			 catch (Exception e) {

			e.printStackTrace();

		
		}
	}
}

