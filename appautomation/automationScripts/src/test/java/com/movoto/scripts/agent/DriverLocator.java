package com.movoto.scripts.agent;

// Since we are using direct selenium driver functions so this class contains all xpaths related to selenium driver.

public class DriverLocator {
	
	
	//Reg_202
	public static String SEARCHPAGE_sortByOption = ".//*[@id='sortText']/span";
	public static String SEARCHPAGE_sortByOption_Safri = ".//a[@id='sortText']";	
	public static String SEARCHPAGE_priceLowElement = ".//*[@id='sortFilter']/li[4]/label";
	public static String SEARCHPAGE_sqft = "//label[@for='sort4']";
	
	//Reg_201
	public static String SEARCHPAGE_view    = "//a[@class='selectedView']";
	public static String SEARCHPAGE_slpitView =     "xpath->//a[@id='splitView']";
	
	public static String MOREFILTERS_resetFiltersButton = "resetFilters";
	public static String MOREFILTERS_cancelFiltersButton = "cancelFilters";
	
	public static String MOREFILTERS_applyButtonCountOnBrowser = "xpath->(//div[contains(text(),'Apply')])[1]";
	
	public static String SEARCHPAGE_dialogBody = "//div[@class='dialog-body']";
	public static String SEARCHPAGE_cancelSecondSearch = "xpath->.//*[@id='CancelSecondSearch']";
	
	public static String SEARCHPAGE_moreFiltersToggle = "xpath->//a[@class='r-morefilters-toggle']";
	
	
	//Reg_199
	public static String SEARCHPAGE_mapView = "xpath->//a[@id='mapView']";
	public static String SEARCHPAGE_gridView = "xpath->//a[@id='gridView']";
 //
	
    //Reg_83
	public static String MORTGAGE_ScrollView1= ".//*[contains(@id,'calculator')]/div[2]/div[1]/div[4]/div[1]/span";
	public static String MORTGAGE_ScrollView2 = ".//*[contains(@id,'calculator')]/div[2]/div[1]/div[3]/div[1]/span";
	public static String MORTGAGE_ScrollView3 = ".//*[contains(@id,'calculator')]/div[2]/div[1]/div[2]/div[1]/span";
	public static String MORTGAGE_ScrollView4 = ".//*[contains(@id,'calculator')]/div[2]/div[1]/div[8]/div[1]/span";
	public static String MORTGAGE_DwnPaymentRateField = "//input[@name='downPaymentRate']";
	public static String MORTGAGE_HomePriceTextField = "//input[@name='homePrice']";
	public static String MORTGAGE_InterestRateTextField = "//input[@name='interestRate']";
	public static String MORTGAGE_PieChartContainer = "(//div[@class='pie-chart-container'])[1]/div/div/div[1]/span/strong";
	public static String MORTGAGE_DwnPaymentRateText="//a[@class='toggle-range downPaymentToggle']/span";
	public static String MORTGAGE_InterestRateText = "//a[@class='toggle-range interestRateToggle']/span";
	public static String MORTGAGE_interestRateToggle = "//a[@class='toggle-range interestRateToggle']/span";
	public static String MORTGAGE_CalculatorText= "//*[text()='Mortgage Calculator']";
	
	//Reg_68
	 public static String HOMEPAGE_mapViewButton = "xpath->//table[@id='dppButtons']//i[@class='icon-map-o']";
	 
	 //Reg_70
	 public static String MAPPAGE_zoomInButton = "xpath->.//div[@title='Zoom in']//img";
	 public static String HOMEPAGE_favouriteicon = "xpath->(.//*[@id='dppButtons']//a)[4]";
	 public static String HOMEPAGE_userloginicon = "xpath->.//i[@class='icon-user']";
	 public static String HOMEPAGE_favouritehomes = "xpath->//a[text()='Favorite homes']";
	 
	 //Reg_71
	 public static String HOMEPAGE_homefeaturetext = ".//span[contains(text(),'Home Features')]";
	 public static String HOMEPAGE_overview = "//span[contains(text(),'Home Features')]/../../../div/a/span[contains(text(),'Overview')]";
	 public static String HOMEPAGE_interior = "//span[contains(text(),'Home Features')]/../../../div/a/span[contains(text(),'Interior')]";
	 public static String HOMEPAGE_exterior = "//span[contains(text(),'Home Features')]/../../../div/a/span[contains(text(),'Exterior')]";
	 public static String HOMEPAGE_amenities = "//span[contains(text(),'Home Features')]/../../../div/a/span[contains(text(),'Amenities / Utilities')]";
	 public static String HOMEPAGE_location = "//span[contains(text(),'Home Features')]/../../../div/a/span[contains(text(),'Location')]";
	 public static String HOMEPAGE_homefeatureheader = "//span[contains(text(),'Home Features')]";
	 
	 //Reg_73 
	 public static String HOMEPAGE_assignedschoolheader = "(.//span[contains(text(),'Assigned Schools')])[1]";
	 public static String HOMEPAGE_assignedschoolupkey = "xpath->//div[@section='NearbySchool']//*[@class='icon-angle-down right']";
	 public static String HOMEPAGE_studentcardcontent = "//table[@class='school-card']";
	 public static String HOMEPAGE_assignedschooldownkey = "xpath->//div[@section='NearbySchool']//*[@class='icon-angle-down right']";
	 public static String HOMEPAGE_solanabeachschoollink = "link->Solana Beach Elementary School District";
	 public static String HOMEPAGE_viewassignedschoolslink = "link->View Assigned Schools on Map";
	 
	 //Reg_74
	 public static String HOMEPAGE_addressheader = "//*[@id='soldNearBy']/..//td[contains(text(),'Address')]";
	 public static String HOMEPAGE_comparableHomeText = ".//*[@id='comparableHomesField']//span[contains(text(),'Compare Similar Homes')]";
	 
	 //Reg_76
	 public static String HOMEPAGE_nearbyhomeforsale = ".//span[contains(text(),'Nearby Homes For Sale')]";
	 public static String HOMEPAGE_numberofhousecards = "xpath->.//div[@id='nearbyProperties']//div[@class='cardone cardbox']";
	 
	 //Reg_78
	 public static String HOMEPAGE_awesomesimilarhomesheader = "//span[contains(text(),'Awesome Similar Homes')][1]";
	 
	 //Reg_79
	 public static String HOMEPAGE_newListingTest = ".//*[@id='newListingsFields']//span[contains(text(),'New Listings in')]";
	
	 //Reg_111
	 public static String HOMEPAGE_form_mortgage = "//a[@class='form-mortgage']";
	 public static String HOMEPAGE_inLineHotleadform = ".//*[@id='inlineHotleadForm']/p[1]";
	 public static String POPUP_popUpEle = ".//*[@id='body']/div[8]/div[2]/div";
	 public static String HOMEPAGE_loginWithUser = "//a[text()='Login']";
	 public static String HOMEPAGE_imageFile = ".//*[@id='dpphotleadform']/table/tbody/tr/td[1]/img";
	 public static String HOMEPAGE_logoutLInk = "//a[contains(text(),'Log out')]";
	 public static String HOMEPAGE_userIcon = "(//i[@class='icon-user'])[1]";
	 
	 //Reg-91
	 public static String HOMEPAGE_publicRecordField = ".//*[@id='publicRecordField']/div/div[2]";
	 public static String HOMEPAGE_publicRecord = "//*[contains(text(),'Public Record')]";
	 public static String HOMEPAGE_basicInfo = ".//*[@id='publicRecordInfo']/a[1]//span[contains(text(),'Basic Info')]";
	 public static String HOMEPAGE_houseFeature = ".//*[@id='publicRecordInfo']//span[contains(text(),'House Features')]";
	 
	 //Reg-94
	 public static String User_Icon_Web_Javascript="xpath->//i[@class='icon-user']";
	 
	 //Reg-88
	 public static String HOMEPPAGE_marketStatistics = ".//span[contains(text(),'Real Estate Market Statistics')]";
		
	//Reg-75
	 public static String HOMEPPAGE_poiSection= ".//div[@id='poiField']//span[contains(text(),'Points of Interest')]";
	 public static String HOMEPAGE_taxiAndCarServices = ".//div[@id='poiField']//span[normalize-space(text())='Taxi and Car Services']";
	 public static String HOMEPAGE_shoppingCentre = ".//div[@id='poiField']//span[normalize-space(text())='Shopping Center']";
	 public static String HOMEPAGE_placeOfWorship = ".//div[@id='poiField']//span[normalize-space(text())='Place of Worship']";
	 public static String HOMEPAGE_BankSection = ".//div[@id='poiField']//span[normalize-space(text())='Bank']";
	 public static String HOMEPAGE_publicTransport = ".//div[@id='poiField']//span[normalize-space(text())='Public Transportation']";
	 public static String HOMEPAGE_barSection = ".//div[@id='poiField']//span[normalize-space(text())='Bar']";
	 public static String HOMEPAGE_transportHub = ".//div[@id='poiField']//span[normalize-space(text())='Transport Hub']";
	 public static String HOMEPAGE_gasStation = ".//div[@id='poiField']//span[normalize-space(text())='Gas Station']";
	 public static String HOMEPAGE_recreation = ".//div[@id='poiField']//span[normalize-space(text())='Recreation']";
	 public static String HOMEPAGE_restaurant = ".//div[@id='poiField']//span[normalize-space(text())='Restaurant']";
	 public static String HOMEPAGE_cafeSection =".//div[@id='poiField']//span[normalize-space(text())='Cafe']";
	 public static String HOMEPAGE_grocery = ".//div[@id='poiField']//span[normalize-space(text())='Grocery']";





}
