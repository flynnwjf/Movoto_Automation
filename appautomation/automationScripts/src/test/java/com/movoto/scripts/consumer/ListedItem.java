package com.movoto.scripts.consumer;

import com.movoto.scripts.consumer.Library.TestModels.BuildInfo;
import com.movoto.scripts.consumer.Library.TestModels.TestModel;

//@BuildInfo(PageName="Listings",XPATH="//div[@class='propertyList' and @section='BrowseByState']//div[@class='row']/div[@class='col-xs-4']/a")
public class ListedItem extends TestModel {
	
	@BuildInfo(PageName="Listings_Home",XPATH="a")
	@BuildInfo(PageName="Listings_Cities",XPATH="self::*")
	@BuildInfo(PageName="MarketTrend_Home",XPATH="a")
	@BuildInfo(PageName="MarketTrend_Cities",XPATH="a")
	@BuildInfo(PageName="Demographics_Home",XPATH="a")
	@BuildInfo(PageName="Demographics_Cities",XPATH="a")
	@BuildInfo(PageName="Schools_Home",XPATH="a")
	@BuildInfo(PageName="Schools_Cities",XPATH="a")
	public String Text;
	
	@BuildInfo(PageName="Listings_Home",XPATH="a",ValueFrom="href")
	@BuildInfo(PageName="Listings_Cities",XPATH="a",ValueFrom="href")
	@BuildInfo(PageName="MarketTrend_Home",XPATH="a",ValueFrom="href")
	@BuildInfo(PageName="MarketTrend_Cities",XPATH="a",ValueFrom="href")
	@BuildInfo(PageName="Demographics_Home",XPATH="a",ValueFrom="href")
	@BuildInfo(PageName="Demographics_Cities",XPATH="a",ValueFrom="href")
	@BuildInfo(PageName="Schools_Home",XPATH="a",ValueFrom="href")
	@BuildInfo(PageName="Schools_Cities",XPATH="a",ValueFrom="href")
	public String URL;
}