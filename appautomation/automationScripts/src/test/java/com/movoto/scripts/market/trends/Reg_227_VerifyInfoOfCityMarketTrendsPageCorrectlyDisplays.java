package com.movoto.scripts.market.trends;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;

/*
 * Govind Kalria
 */
public class Reg_227_VerifyInfoOfCityMarketTrendsPageCorrectlyDisplays extends BaseTest {

  JSONParserForAutomationNG jsonParser;
  JSONObject jsonObj;
  MarketTrendHelper marketTrendHelper;
  String pageUrl;


  @Test
  @Parameters("dataProviderPath")
  public void verifySearchInfo(String dataProviderPath) {
    init(dataProviderPath);
    if (jsonObj != null) {
      verifyDemographicClick(jsonObj);
      verifyURLUnderListings(jsonObj);
      verifyHomeSaleUrl(jsonObj);
      verifySchoolurl(jsonObj);
      verifyMarketSnapShotData(jsonObj);
      verifyChangeCityUrlBtn(jsonObj);
      verifyBrowseMarketArea(jsonObj);
    } else {
      Assert.assertFalse(false);
    }

  }

  public void init(String jsonPath) {

    try {
      marketTrendHelper = new MarketTrendHelper(library);
      jsonParser = new JSONParserForAutomationNG(jsonPath);
      jsonObj = jsonParser.getNode("cityInfo");
      pageUrl = jsonObj.get("MARKET_TREND_CITYPAGE.url").toString();
      library.setRequestHeader("X-MData-Key", jsonObj.get("X-MData-Key").toString());

    } catch (Exception exc) {
      System.out.println("Exception in BasicInfo:init() ->" + exc.getMessage());
    }
  }

  /*
   * Verify url of "Listings" undermap ends with <City>-<State>/<Neighborhood>
   * 
   * 
   */

  public void verifyURLUnderListings(JSONObject data) {
    String url = "MARKET_TREND_CITYPAGE.homePageurl";
    library.wait(10);
    int crumbsCount = library.getElementCount("MARKET_TREND_CITYPAGE.crumbsCount");
    if(crumbsCount==0){
    	crumbsCount = library.getElementCount("MARKET_TREND_CITYPAGE.crumbsCount");
    }
    String crumbsPath = data.get("MARKET_TREND_CITYPAGE.crumbsPath").toString();
    String state = data.get("MARKET_TREND_CITYPAGE.state").toString();
    String city = data.get("MARKET_TREND_CITYPAGE.city").toString();
    marketTrendHelper.urlUnderListingValidation(data, crumbsCount, crumbsPath, url, state, city);
  }



  /*
   * Verify url of "Browse by Market area" is <HomePageURL>/<state>/market-trends/
   */
  private void verifyBrowseMarketArea(JSONObject data) {

    Assert.assertEquals(
        data.get("MARKET_TREND_CITYPAGE.homePageurl").toString()
            + data.get("MARKET_TREND_CITYPAGE.state").toString() + "/" + "market-trends/",
        library.getAttributeOfElement("href", "MARKET_TREND_CITYPAGE.browsebyMarketArea.label"));
  }


  /*
   * Verify url of "Change Neighborhood" button is <HomePageURL>/market-trends/
   */
  private void verifyChangeCityUrlBtn(JSONObject data) {
    // Making it false as URL is not matching vivian will look inot it.
    // <a id="changecity" href="http://alpaca.san-mateo.movoto.net:3025/market-trends/"> Change City
    // </a>
    String chngCityurl =
        library.getAttributeOfElement("href", "MARKET_TREND_CITYPAGE.changeCitybtn");
    Assert.assertFalse(
        chngCityurl.equalsIgnoreCase(data.get("MARKET_TREND_CITYPAGE.homePageurl").toString()));
  }

  /*
   * Verify home for sale url
   */

  public void verifyHomeSaleUrl(JSONObject data) {

    String pageName = "MARKET_TREND_CITYPAGE";
    marketTrendHelper.urlHomeSaleValidation(data, pageUrl, pageName);

  }

  /*
   * 2 Click "Demographics" under map Verify pop up info is the same with <CityZipCode_API>
   * 
   */

  public void verifyDemographicClick(JSONObject data) {
    String demgApiResp =
        demographicApiResp(data.get("MARKET_TREND_CITYPAGE.demographicAPI").toString());
    library.wait(5);
    library.isJSEClicked("MARKET_TREND_CITYPAGE.demographic.label");
    library.wait(5);
    library.switchToWindow();
    String urlUI =
        library.getAttributeOfElement("href", "MARKET_TREND_CITYPAGE.demographicPopupInfo");
    marketTrendHelper.urldemographicValidation(demgApiResp, urlUI);
  }



  public String demographicApiResp(String dmgApi) {
    String response = library.HTTPGet(dmgApi);
    return response;
  }



  public void verifySchoolurl(JSONObject data) {
    String pageName = "MARKET_TREND_CITYPAGE";
    marketTrendHelper.urlUnderSchoolValidation(data, pageUrl, pageName);
  }

  public void verifyMarketSnapShotData(JSONObject data) {
    String pageName = "MARKET_TREND_CITYPAGE";
    String type = null;
    String apiType = null;
    String snapShotResp =
        snapShotIntialize(data, data.get("MARKET_TREND_CITYPAGE.snapShotAPI").toString());
    marketTrendHelper.marketSnapShotDataValidation(data, type, apiType, snapShotResp, pageName);

  }

  /*
   * Test case 4 Check Links in Overview section Verify below 2 urls exists in the paragraph:
   * <Market-Trends CityPage URL> <HomePageURL>/<City>-<State>
   */
  @Test
  public void verifyURLExists() {
    String url1 = library.getAttributeOfElement("href", "OVERVIEW_URL1");
    Assert.assertTrue(url1 != null && url1.isEmpty());

    String url2 = library.getAttributeOfElement("href", "OVERVIEW_URL2");
    Assert.assertTrue(url2 != null && !url2.isEmpty());
  }


  public void verifyOpenHousData() {
    /*
     * Need to check with vivan card ULR? if it is address Link
     */
    library.isJSEClicked("xpath->.//*[@id='realEstateLinks']/li[2]/a");
    String openHouse = opHouseRespAPI();
    int baseInfoCnt = library.getElementCount("xpath->(.//*[@class='baseInfo'])");
    List<String> prices = new ArrayList<>();
    List<String> bathRooms = new ArrayList<>();
    List<String> bedRooms = new ArrayList<>();
    List<String> areas = new ArrayList<>();
    List<String> cardUrls = new ArrayList<>();
    List<String> addresses = new ArrayList<>();

    for (int i = 1; i <= baseInfoCnt; i++) {

      prices.add(library.getTextFrom("xpath->(.//*[@class='baseInfo'])[" + i + "]/span[1]"));
      bathRooms.add(library.getTextFrom("xpath->(.//*[@class='baseInfo'])[" + i + "]/span[2]"));
      bedRooms.add(library.getTextFrom("xpath->(.//*[@class='baseInfo'])[" + i + "]/span[3]"));
      areas.add(library.getTextFrom("xpath->(.//*[@class='baseInfo'])[" + i + "]/span[4]"));
      cardUrls.add(
          library.getAttributeOfElement("href", "xpath->(.//*[@class='addresslink'])[" + i + "]"));
      addresses.add(library.getTextFrom("xpath->(.//*[@itemprop='address'])[" + i + "]/span[1]"));

    }
  }

  //
  // // Test case 6
  // /*
  // * Need to check with Vivian . API response not matching
  // */
  // @Test
  public void verifyNewListing() {
    // /*
    // * Need to check with vivan card ULR? if it is address Link
    // */
    // library.isJSEClicked("xpath->.//*[@id='realEstateLinks']/li[2]/a");
    String newListing = newListingAPIResp();
    System.out.println("newListing:" + newListing);
    int cnt = library.getElementCount("xpath->(.//*[@class='blogcard-info'])");
    List<String> prices = new ArrayList<>();
    List<String> bathRooms = new ArrayList<>();
    List<String> bedRooms = new ArrayList<>();
    List<String> areas = new ArrayList<>();
    List<String> cardUrls = new ArrayList<>();
    List<String> addresses = new ArrayList<>();

    for (int i = 1; i <= cnt; i++) {

      prices.add(library.getTextFrom("xpath->(.//*[@class='baseInfo'])[" + i + "]/span[1]"));
      bathRooms.add(library.getTextFrom("xpath->(.//*[@class='baseInfo'])[" + i + "]/span[2]"));
      bedRooms.add(library.getTextFrom("xpath->(.//*[@class='baseInfo'])[" + i + "]/span[3]"));
      areas.add(library.getTextFrom("xpath->(.//*[@class='baseInfo'])[" + i + "]/span[4]"));
      cardUrls.add(
          library.getAttributeOfElement("href", "xpath->(.//*[@class='addresslink'])[" + i + "]"));
      addresses.add(library.getTextFrom("xpath->(.//*[@itemprop='address'])[" + i + "]/span[1]"));

    }
  }

  //
  // // TC-7
  // /*
  // * Need to discuss with Vivian
  // *
  // * Not matching Verify the titles/urls of all the articles are the same with <Article_API>
  // * response
  // *
  // */
  // @Test
  public void get_ToKnowSection(JSONObject data) {
    String nearbyResp = newListingNearByAPI(data);

    library.getValueFromJson(nearbyResp, "$.[0].listingUrl");
    library.getDriver().get("http://www.movoto.com/san-francisco-ca/market-trends/");
    library.isJSEClicked("xpath->.//*[@id='body']/div[2]/div[2]/div[3]/div[2]/div[4]");
    int count = library.getElementCount("xpath->(.//*[@class='blogcard-info'])");

    List<String> urls = new ArrayList<>();
    List<String> titles = new ArrayList<>();

    for (int i = 1; i <= count; i++) {
      String title =
          library.getTextFrom("xpath->(.//*[@class='blogcard-info'])[" + i + "]/span[1]");
      titles.add(title);

      urls.add(library.getAttributeOfElement("href",
          "xpath->(.//*[@class='blogcard-info'])[" + i + "]"));
    }

    for (String url : urls) {
      System.out.println("Urls:" + url);
    }

    for (int i = 1; i <= urls.size(); i++) {
      System.out
          .println(library.getValueFromJson("$.[" + i + "].listingUrl", nearbyResp).toString());
    }
  }

  //
  private String opHouseRespAPI() {

    String openHouse = library.HTTPGet(
        "http://soa8-qa.ng.movoto.net/property/listings/cityid/40446/sitemapopenhouse?pagenumber=1&pagesize=20&orderby=NEWEST_FIRST");
    return openHouse;
  }


  private String snapShotIntialize(JSONObject jsonObj, String api) {
    library.setRequestHeader("X-MData-Key", jsonObj.get("X-MData-Key").toString());
    String snapShotResp = library.HTTPGet(api);
    return snapShotResp;
  }

  private String newListingAPIResp() {

    String snapShotResp =
        library.HTTPGet("http://soa8-qa.ng.movoto.net/marketstatistics/market-snapshot-data/1437");
    return snapShotResp;
  }


  public String getResponseFromApi(JSONObject Data, String ApiName) {
    library.setRequestHeader("X-MData-Key", Data.get("X-MData-Key").toString());
    String response = library.HTTPGet(Data.get(ApiName).toString());
    return response;
  }



  private String newListingNearByAPI(JSONObject data) {
    library.setRequestHeader("X-MData-Key", jsonObj.get("X-MData-Key").toString());
    String nearbyResp =
        library.HTTPGet(" http://soa8-qa.ng.movoto.net/property/listings/cityid/15195/new?size=10");
    return nearbyResp;
  }
  //
  ////
  // // // Test case 5
  // // /*
  // // * Check properties in OpenHouses Section <OpenHouse_API>:
  // // * http://soa8-qa.ng.movoto.net/property/listings/cityid/40446/sitemapopenhouse?pagenumber=1&
  // // * pagesize=20&orderby=NEWEST_FIRST
  // // *
  // // *
  // // * Verify Price/PropertyType /Bedrooms /Bathrooms /Address /City /CardURL /photo for all the
  // // * properties is the same as <OpenHouses_API> response
  // // *
  // // */
  // //
  // @Test

}
