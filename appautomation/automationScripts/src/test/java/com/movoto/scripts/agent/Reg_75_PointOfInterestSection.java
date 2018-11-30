 
 
package com.movoto.scripts.agent;
 
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
 
import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;
 
public class Reg_75_PointOfInterestSection extends BaseTest {
      
       JSONParserForAutomationNG jsonParser;
       JSONObject jsonObj;
       WebDriver wDriver; 
 
 
       @Test
       @Parameters("dataProviderPath")
       public void pointOfInterestSection(String dataProviderPath) throws ParseException {
              try {
 
                     jsonParser = new JSONParserForAutomationNG(dataProviderPath);
                     jsonObj = jsonParser.getNode("PointOfInterest");
 
              } catch (Exception exc) {
                     System.out.println("Exception in Point Of Interest() ->"
                                  + exc.getMessage());
              }
              if (jsonObj != null) {
                     pointOfInterestSection(jsonObj);
              }
              else{
                     Assert.assertTrue(false, "Please check data for provided for script");
              }
       }
      
       public void pointOfInterestSection(JSONObject Data) throws ParseException {
             
              String response = getApiHeaderAndGetResponse(Data, "POI_API");
             
              library.scrollToElement("HOMEPAGE.poiSection");
              library.wait(3);
             
              // Verify "Points Of Interest" text is displayed in the title
              try {
                     boolean POItextPresent = library.verifyPageContainsElement("POIPAGE.POItext");
                     Assert.assertTrue(POItextPresent);
              } catch (NoSuchElementException e) {
                     System.out.println("POI Text Not Found");
              }
              library.wait(3);
             
              if (library.getCurrentPlatform().equals("Android") || library.getCurrentPlatform().equals("IOS_WEB")){
                     library.click("HOMEPAGE.poiSection");
              }
             
              library.wait(3);
              // Verify detail subtitles and content is the same with API response
              int iPOICount = library.getElementCount("POIPAGE.POICount");
              for (int i = 1; i <= iPOICount; i++) {
                     library.wait(2);
                     WebElement POIText = library.findElement("xpath->.//*[@id='poiListField']/a["+ i + "]/i[1]" );
                     String UIPOIText = POIText.getText();
                     String APIPOIType = library.getValueFromJson("$..poiTypeName", response).toString();
                     Assert.assertTrue(APIPOIType.contains(UIPOIText));
                     library.wait(2);
                     library.findElement("xpath->.//*[@id='poiListField']/a["+ i + "]/i[1]").click();
                     library.wait(2);
                     int iPOIMainSectionCount = library.getElementCount("xpath->.//*[@id='poiListField']/div[" + i + "]/div");
                     for (int j = 1; j <= iPOIMainSectionCount; j++) {
                           library.wait(2);
                           WebElement POISubSectionText = library.findElement("xpath->.//*[@id='poiListField']/div["+ i + "]/div["+ j + "]/div[1]/span" );
                           String UIPOISubSectionText = POISubSectionText.getText();
                           String APIPOISubType = library.getValueFromJson("$..name", response).toString();
                           Assert.assertTrue(APIPOISubType.contains(UIPOISubSectionText));
                     }
              }
              library.wait(3);
              // Click "View Nearby Points of Interest on Map" button
              library.click("POIPAGE.ViewNearByPOI");
              library.wait(3);
             
              //Verify map window pops up
              try {
                     boolean isGoogleLogoPresent = library.verifyPageContainsElement("HOMEPAGE.imgGoogleLogo");
                     Assert.assertTrue(isGoogleLogoPresent);
              } catch (NoSuchElementException e) {
                     System.out.println("Google Logo Not Found");
              }
              library.wait(3);
              //Click "X" button on the map window
              library.click("MAPPAGE.closeButton");
              library.wait(3);
              //Click UP button on this section
              library.scrollToElement("POIPAGE.POItext");
              library.wait(3);
              library.click("POIPAGE.POITextHide");
              library.wait(3);
              //Verify the detail info is hided
              boolean ishidden = library
                           .verifyPageNotContainsElement("POIPAGE.POIGrocery");
              Assert.assertTrue(ishidden);
       }
      
       // API functions
              public String getApiHeaderAndGetResponse(JSONObject Data, String apiName) {
                     library.setRequestHeader("X-MData-Key", Data.get("X-MData-Key")
                                  .toString());
                     String responsePOI = library.HTTPGet(Data.get(apiName)
                                  .toString());
                     return responsePOI;
              }
}
 
 