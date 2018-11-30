package com.movoto.scripts.consumer;

import java.util.List;

import com.movoto.scripts.consumer.Library.TestModels.BuildInfo;
import com.movoto.scripts.consumer.Library.TestModels.TestModel;

@BuildInfo(PageName="Listings_Home",XPATH="//div[@class='propertyList']")// and @section='BrowseByState']
@BuildInfo(PageName="Listings_Cities",XPATH="//div[@class='propertyList' and @section='Cities']")
@BuildInfo(PageName="MarketTrend_Home",XPATH="//div[@class='gs-fixed blank-section  links']")
@BuildInfo(PageName="MarketTrend_Cities",XPATH="//div[@class='gs-fixed blank-section  links' and @section='CitiesTrends']/div[@id]")
@BuildInfo(PageName="Demographics_Home",XPATH="//div[@class='propertyList']")
@BuildInfo(PageName="Demographics_Cities",XPATH="//div[@class='gs-fixed blank-section  links' and @section='CitiesTrends']/div[@id]")
@BuildInfo(PageName="Schools_Home",XPATH="//div[@class='gs-fixed blank-section  links']")
@BuildInfo(PageName="Schools_Cities",XPATH="//div[@class='gs-fixed blank-section  links' and @section='CitiesSchools']/div[@id]")

public class SiteMap extends TestModel {

	@BuildInfo(PageName="Listings_Home",XPATH="div[@class='row']/div[@class='col-xs-4']")
	@BuildInfo(PageName="Listings_Cities",XPATH="div[@class='row']/div[@class='col-xs-4']")
	@BuildInfo(PageName="MarketTrend_Home",XPATH="div[@class='row']/div[@class='col-xs-3']")
	@BuildInfo(PageName="MarketTrend_Cities",XPATH="following-sibling::div[1]/div[@class='col-xs-3']")
	@BuildInfo(PageName="Demographics_Home",XPATH="div[@class='row']/div[@class='col-xs-4']")
	@BuildInfo(PageName="Demographics_Cities",XPATH="following-sibling::div[1]/div[@class='col-xs-3']")
	@BuildInfo(PageName="Schools_Home",XPATH="div[@class='row']/div[@class='col-xs-3']")
	@BuildInfo(PageName="Schools_Cities",XPATH="following-sibling::div[1]/div[@class='col-xs-3']")
	
	public List<ListedItem> ListedItems;
	//public ListedItem ListedItems;
	
	@BuildInfo(PageName="Listings_Home",XPATH="*[contains(@class,'h')]")
	@BuildInfo(PageName="Listings_Cities",XPATH="h2")
	@BuildInfo(PageName="MarketTrend_Home",XPATH="//h2")
	@BuildInfo(PageName="MarketTrend_Cities",XPATH="self::*")
	@BuildInfo(PageName="Demographics_Home",XPATH="//h2")
	@BuildInfo(PageName="Demographics_Cities",XPATH="self::*")
	@BuildInfo(PageName="Schools_Home",XPATH="//h2")
	@BuildInfo(PageName="Schools_Cities",XPATH="self::*")
	
	public String Title;
	
	
}
