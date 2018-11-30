package com.movoto.scripts.agent;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;
import com.movoto.scripts.data.DPPTestCaseDataProvider;

import junit.framework.Assert;

public class Reg_68_PhotoGallaryTest extends BaseTest{
	
	@Test(dataProvider = "TestDataForReg_68", dataProviderClass = DPPTestCaseDataProvider.class)
	public void photoGallaryOfProperty(Map<String, String> data){
		
		library.setRequestHeader(data.get("headerKey"), data.get("headerValue"));
		String response = library.HTTPGet(data.get("apiString"));
		
		if(library.getCurrentPlatform().equals("Web")){
			scenarios.removeAddsPopUp();
			//Verify first three photo is displayed
			verifyShowedImageOnUI(response);
			//verify Displayed Images, Right left navigation and back button
			verifyRightLeftNavigationAndBackButton(response);
			// Verify Address Price and image count in Image Gallery page and Share, Gallery and MapView Button
			verifyAddressPriceImageCount(response);
			scenarios.removeAddsPopUp();
			//Verify first three photo is displayed
			verifyShowedImageOnUI(response);	
		}
		
		//For AndroidChrome and IOSSafari
		else{
			verifyShowedImageOnUI(response);
			verifyScrollingOfImage();
			library.click("HOMEPAGE.firstActiveImage");
			library.wait(3);
			Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.gallerybutton"));
			Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.mapviewbutton"));
			Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.sharebutton"));
			Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.favLink"));
//			Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.phoneCallIcon"));
			Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.goToSeeHome"));
			
			String imageCountString = library.getTextFrom("IMAGEPAGE.photoCount");
			imageCountString = imageCountString.split(" ")[0];
			int imageCount = Integer.parseInt(imageCountString);
			Integer imageCountInApi = (Integer) library.getValueFromJson("$.dpp.photoCount", response);
			int imageCountValue = (int) imageCountInApi;
			Assert.assertTrue(imageCountValue == imageCount);
			
			library.click("IMAGEPAGE.closebutton");	
			verifyShowedImageOnUI(response);
		}
	}
	
	/*
	 * Test case Specific reusable methods methods
	 */
	public void verifyShowedImageOnUI(String response){
		library.wait(3);
		int activeImage = library.getElementCount("HOMEPAGE.imageCount");
		Map<String, String> activeImageIndex = new HashMap<String, String>();
		for (int i = 1; i <= activeImage; i++) {
			String indexOfImage = library.getAttributeOfElement("data-showphoto","xpath->(.//div[@class='wrap']//li[@class='active']/a)[" + i + "]");
			activeImageIndex.put("index" + i, indexOfImage);
		}
		for (int i = 1; i <= activeImage; i++) {
			String imageUrlInApi = (String) library.getValueFromJson("$.dpp.photos[" + activeImageIndex.get("index" + i) + "].url", response);
			int activeindex = Integer.parseInt(activeImageIndex.get("index" + i));
			String imageUrl = library.getAttributeOfElement("src","xpath->(.//div[@class='wrap']//img)[" + (activeindex + 1) + "]");
			String indexOfImage = library.getAttributeOfElement("data-showphoto","xpath->(.//div[@class='wrap']//li[@class='active']/a)[" +i+ "]");
			int numericValueOfIndex = Integer.parseInt(indexOfImage);
			if(numericValueOfIndex == 0){
				int lengthOfUrl = imageUrl.length();
				imageUrl = imageUrl.substring(0, lengthOfUrl - 8);
				Assert.assertEquals(imageUrlInApi.contains(imageUrl), true);
			}else
				Assert.assertEquals(imageUrlInApi.equals(imageUrl), true);
		}
	}
	
	public void verifyAddressPriceImageCount(String response){
		library.click("HOMEPAGE.firstActiveImage");
		library.wait(3);
		Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.gallerybutton"));
		Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.mapviewbutton"));
		Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.sharebutton"));
		Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.favLink"));
		Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.goToSeeHome"));
		
		String address = library.getTextFrom("IMAGEPAGE.address");
		String addressInApi = (String) library.getValueFromJson("$.dpp.address.addressInfo", response);
		Assert.assertTrue(address.equalsIgnoreCase(addressInApi));

		String price = library.getTextFrom("IMAGEPAGE.price");
		price = price.replaceAll(",", "");
		price = price.substring(1);
		int numericPrice = Integer.parseInt(price);
		Integer priceInApi = (Integer) library.getValueFromJson("$.dpp.listPrice", response);
		int numericPriceInApi = (int) priceInApi;
		Assert.assertTrue(numericPriceInApi == numericPrice);

		String imageCountString = library.getTextFrom("IMAGEPAGE.photoCount");
		imageCountString = imageCountString.split(" ")[0];
		int imageCount = Integer.parseInt(imageCountString);
		Integer imageCountInApi = (Integer) library.getValueFromJson("$.dpp.photoCount", response);
		int imageCountValue = (int) imageCountInApi;
		Assert.assertTrue(imageCountValue == imageCount);
		library.click("IMAGEPAGE.closebutton");
	}
	
	public void verifyRightLeftNavigationAndBackButton(String response){
		library.click("HOMEPAGE.rightArrow");
		verifyShowedImageOnUI(response);
		library.click("HOMEPAGE.leftAngleIcon");
		verifyShowedImageOnUI(response);
		
		/*String statusOfProperty = library.getTextFrom("HOMEPAGE.statusInfo");
		String statusOfpropertyInApi = (String) library.getValueFromJson("$.listingStatus.displayName", response);
		Assert.assertEquals(statusOfProperty.equals(statusOfpropertyInApi), true);*/
		do{
			library.click("HOMEPAGE.rightArrow");
		}while(library.verifyPageNotContainsElement("HOMEPAGE.lastActiveImage"));
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.backIconImage"));
		library.click("HOMEPAGE.backIconImage");
		verifyShowedImageOnUI(response);
		library.wait(2);
		
	}
	
	//need to delete
	public void photoGallaryVerification(Map<String, String> data) {
		if (library.getCurrentPlatform().equals("Android")) {
			int activeImage = library.getElementCount("HOMEPAGE.imageCount");
			Map<String, String> activeImageIndex = new HashMap<String, String>();
			for (int i = 1; i <= activeImage; i++) {
				String indexOfImage = library.getAttributeOfElement("data-showphoto",
						"xpath->.//div[@class='wrap']//li[" + i + "]/a");
				activeImageIndex.put("index" + i, indexOfImage);
			}

			library.setRequestHeader("X-MData-Key", "CHUMAGATHUQ9VE7AYEBR");
			String response = library.HTTPGet(
					"http://service.ng.movoto.net/property/listings/04aa35e5-c645-4587-ba75-64e452f8c087?refresh=true");
			library.wait(1);

//			int numberOfImage = library.getElementCount("xpath->.//div[@class='wrap']//img");

			for (int i = 2; i < activeImage; i++) {
				String imageUrlInApi = (String) library
						.getValueFromJson("$.photos[" + activeImageIndex.get("index" + i) + "].url", response);
				library.wait(1);
				int activeindex = Integer.parseInt(activeImageIndex.get("index" + i));

				String imageUrl = library.getAttributeOfElement("src",
						"xpath->//div[@class='wrap']//a[@data-showphoto=" + activeindex + "]/img");
				library.wait(3);

				Assert.assertEquals(imageUrl.equals(imageUrlInApi), true);

			}

			String statusOfProperty = library.getTextFrom("HOMEPAGE.statusInfo");
			String statusOfpropertyInApi = (String) library.getValueFromJson("$.listingStatus.displayName", response);
			Assert.assertEquals(statusOfProperty.equals(statusOfpropertyInApi), true);

			library.getDriver().navigate().refresh();
			library.waitForElement("HOMEPAGE.firstActiveImage");
			library.click("HOMEPAGE.firstActiveImage");
			library.wait(2);
			Assert.assertTrue(library.verifyPageContainsElement("xpath->.//a[@id='galleryButton']/i"));
			Assert.assertTrue(library.verifyPageContainsElement("xpath->.//*[@id='photoShowMapViewBtn']/i"));
			Assert.assertTrue(library
					.verifyPageContainsElement("xpath->.//table[@id='dppButtons']//i[@class='icon-share-alt']"));
		} else {
			scenarios.removeAddsPopUp();
//			library.click("HOMEPAGE.rightArrow");
			int activeImage = library.getElementCount("HOMEPAGE.imageCount");
			Map<String, String> activeImageIndex = new HashMap<String, String>();
			for (int i = 1; i <= activeImage; i++) {
				String indexOfImage = library.getAttributeOfElement("data-showphoto",
						"xpath->(.//div[@class='wrap']//li[@class='active']/a)[" + i + "]");
				activeImageIndex.put("index" + i, indexOfImage);
			}

			library.setRequestHeader("X-MData-Key", "CHUMAGATHUQ9VE7AYEBR");
			String response = library.HTTPGet(
					"http://service.ng.movoto.net/property/listings/04aa35e5-c645-4587-ba75-64e452f8c087?refresh=true");
			library.wait(1);
			for (int i = 1; i < activeImage; i++) {
				String imageUrlInApi = (String) library.getValueFromJson("$.photos[" + activeImageIndex.get("index" + i) + "].url", response);
				int activeindex = Integer.parseInt(activeImageIndex.get("index" + i));
				String imageUrl = library.getAttributeOfElement("src","xpath->(.//div[@class='wrap']//img)[" + (activeindex + 1) + "]");
				Assert.assertEquals(imageUrl.equals(imageUrlInApi), true);
			}
			
			String statusOfProperty = library.getTextFrom("HOMEPAGE.statusInfo");
			String statusOfpropertyInApi = (String) library.getValueFromJson("$.listingStatus.displayName", response);
			Assert.assertEquals(statusOfProperty.equals(statusOfpropertyInApi), true);
			library.click("HOMEPAGE.rightArrow");
			library.click("HOMEPAGE.rightArrow");
			library.click("HOMEPAGE.firstActiveImage");
			library.wait(2);
			
			Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.gallerybutton"));
			Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.mapviewbutton"));
			Assert.assertTrue(library.verifyPageContainsElement("IMAGEPAGE.sharebutton"));

			String address = library.getTextFrom("IMAGEPAGE.address");
			String addressInApi = (String) library.getValueFromJson("$.address.addressInfo", response);
			Assert.assertTrue(address.equalsIgnoreCase(addressInApi));

			String price = library.getTextFrom("IMAGEPAGE.price");
			price = price.replaceAll(",", "");
			price = price.substring(1);
			int numericPrice = Integer.parseInt(price);
			Integer priceInApi = (Integer) library.getValueFromJson("$.listPrice", response);
			int numericPriceInApi = (int) priceInApi;
			Assert.assertTrue(numericPriceInApi == numericPrice);

			String imageCountString = library.getTextFrom("IMAGEPAGE.photoCount");
			imageCountString = imageCountString.split(" ")[0];
			int imageCount = Integer.parseInt(imageCountString);
			Integer imageCountInApi = (Integer) library.getValueFromJson("$.photoCount", response);
			int imageCountValue = (int) imageCountInApi;
			Assert.assertTrue(imageCountValue == imageCount);
		}
	}
	
	public void verifyScrollingOfImage(){
		int photoViewCount = library.getElementCount(".//*[@id='dppPhotoview']/div[3]/ul/li");
		System.out.println("Total Photos of the property :  " + photoViewCount);

		for (int i=1; i<=photoViewCount; i++){
		 JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
		 WebElement element = library.getDriver().findElement(By.xpath(".//*[@id='dppPhotoview']/div[3]/ul/li[" + i + "]/a/img"));
		 jse.executeScript("arguments[0].scrollIntoView(true);", element);
		 library.wait(15);
		}
	}
	
}
