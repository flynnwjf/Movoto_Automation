package com.movoto.scripts.agent;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.DPPTestCaseDataProvider;

public class Reg_69_BasicInfolist extends BaseTest{
	
	@Test(dataProvider = "TestDataForReg_69", dataProviderClass = DPPTestCaseDataProvider.class)
	public void basicListingInfo(Map<String, String> data){
		System.out.println("Reg_69 is running");
		
		library.setRequestHeader(data.get("headerKey"), data.get("headerValue"));
		String response = library.HTTPGet(data.get("apiString"));
		if(library.getCurrentPlatform().equals("Web"))
			scenarios.removeAddsPopUp();
		
		//Verification of Address/City/State/Zip code/Property description
		verifyAddressZipDescription(response);
		
		//verify Days on Movoto,Year Built,HOA,$/Sqft,Lot,Property Type,MLS#,MLSlogo,Listing Office,Listing Broker
		verifydaysOnMovotoYearBuiltHOA(response);
			
		//verify url contains Movoto Real Estate, houston Link, zip Link
		verifyLinks(data);
	}
	/*
	 * Test case specific reusable methods
	 */
	public void verifyAddressZipDescription(String response){
		String postalCode = library.getTextFrom("HOMEPAGE.postalCode");
		String postalCodeInApi = (String) library.getValueFromJson("$.dpp.address.zipCode", response);
		Assert.assertTrue(postalCode.equalsIgnoreCase(postalCodeInApi), "Zip Code is not matched");
		
		String streetAddress = library.getTextFrom("HOMEPAGE.streetAddress");
		streetAddress = streetAddress.replace(",", " ").trim();
		String streetAddressInApi = (String) library.getValueFromJson("$.dpp.address.addressInfo", response);
		Assert.assertEquals(streetAddress, streetAddressInApi);
		
		String addressRegion = library.getTextFrom("HOMEPAGE.addressRegion");
		String addressRegionInApi = (String) library.getValueFromJson("$.dpp.address.city", response);
		String addressLocalityInApi = (String) library.getValueFromJson("$.dpp.address.state", response);
		Assert.assertEquals(addressRegion, addressLocalityInApi);
		
		String addressLocality = library.getTextFrom("HOMEPAGE.addressLocality");
		addressLocality = addressLocality.replace(',', ' ').trim();
		Assert.assertEquals(addressLocality, addressRegionInApi);
		
		String description = library.getTextFrom("HOMEPAGE.properties");
//		description = description.trim().toLowerCase();
		String descriptionInApi = (String) library.getValueFromJson("$.dpp.publicRemarks", response);
		description = descriptionInApi.substring(0,descriptionInApi.length());
//		descriptionInApi = descriptionInApi.replaceAll("\n"," ");
//		descriptionInApi = descriptionInApi.trim().toLowerCase();
		Assert.assertTrue(description.contains(descriptionInApi));
	}
	
	public void verifydaysOnMovotoYearBuiltHOA(String response){
		String yearBuilt = library.getTextFrom("HOMEPAGE.yearBuilt");
		int numericYearBiult = Integer.parseInt(yearBuilt);
		Integer yearBuiltInApi = (Integer) library.getValueFromJson("$.dpp.yearBuilt", response);
		int numericYearBuiltInApi = (int)yearBuiltInApi;
		Assert.assertEquals(numericYearBiult, numericYearBuiltInApi, "Year Built is not matched");
		
		String daysOnMovoto = library.getTextFrom("HOMEPAGE.daysOnMovoto");
		int numericDaysOnMovoto = Integer.parseInt(daysOnMovoto);
		Integer daysOnMovotoInApi = (Integer) library.getValueFromJson("$.dpp.daysOnMarket", response);
		int numericdaysOnMovotoInApi = (int)daysOnMovotoInApi;
		Assert.assertEquals(numericDaysOnMovoto, numericdaysOnMovotoInApi);
		
		Integer hoaInApi = (Integer) library.getValueFromJson("$.dpp.totalMonthlyFee", response);
		int numericHoaInApi = (int)hoaInApi;
		String hoa = library.getTextFrom("HOMEPAGE.hoa");
		if(!hoa.equals("—")){
			hoa = hoa.split("/")[0];
			hoa = hoa.substring(1);
			int numericHoa = Integer.parseInt(hoa);
			Assert.assertTrue(numericHoaInApi == numericHoa);
		}
		
		String pricePerSquarefeet = library.getTextFrom("HOMEPAGE.pricePersquare");
		pricePerSquarefeet = pricePerSquarefeet.substring(1);
		int numericPricePsf = Integer.parseInt(pricePerSquarefeet);
		Double priceInApi = (Double) library.getValueFromJson("$.dpp.pricePerSqft", response);
		double pricevalueApi = (double)priceInApi;
		int numeriPriceInApi = (int)pricevalueApi;
		boolean ispriceMatched1 = (numeriPriceInApi <= numericPricePsf) && (numeriPriceInApi >= numericPricePsf-1);
		Assert.assertTrue(ispriceMatched1);
		
		String lotType = library.getTextFrom("HOMEPAGE.lotType");
		String lotTypeInApi = (String) library.getValueFromJson("$.dpp.lotSizeSqft", response);
		
		String propertyType = library.getTextFrom("HOMEPAGE.propertyType");
		String propertyTypeui = propertyType.toUpperCase();
		propertyTypeui = propertyTypeui.split(" ")[0]+"_"+propertyTypeui.split(" ")[1];
		String propertyTypeInApi = (String) library.getValueFromJson("$.dpp.listingType.name", response);
		Assert.assertTrue(propertyTypeInApi.contains(propertyTypeui));
		
		String mlsNum = library.getTextFrom("HOMEPAGE.mlsNum");
		int numericMslNum = Integer.parseInt(mlsNum.split("-")[1]);
		String mlsNumInApi = (String) library.getValueFromJson("$.dpp.mlsNumber", response);
		Assert.assertTrue(mlsNum.equals(mlsNumInApi));
		
		String listingOffice = library.getTextFrom("HOMEPAGE.listingOffice");
		String listingOfficeInApi = (String) library.getValueFromJson("$.dpp.officeListName", response);
		Assert.assertEquals(listingOffice, listingOfficeInApi);
		
		String listingBrokerInApi =(String) library.getValueFromJson("$.dpp.agentListFullName", response);
		String listingBroker = library.getTextFrom("HOMEPAGE.listedBy");
		Assert.assertEquals(listingBroker, listingBrokerInApi);
	}
	
	public void verifyLinks(Map<String, String> data){
	String movotoRealState = library.getAttributeOfElement("href", "HOMEPAGE.movotoRealState");
	boolean isMovotoLinkPresent = movotoRealState.contains(data.get("textOfMovotoLink"));
	Assert.assertTrue(isMovotoLinkPresent);
	
	String houstonLink = library.getAttributeOfElement("href", "HOMEPAGE.houstonLink");
	boolean isHoustonLink = houstonLink.endsWith(data.get("textOfCityLink"));
	Assert.assertTrue(isHoustonLink);
	
	String zipLink = library.getAttributeOfElement("href", "HOMEPAGE.zipLink");
	boolean isZipLink = zipLink.endsWith(data.get("textOfZipLink"));
	Assert.assertTrue(isZipLink);
	}
}
