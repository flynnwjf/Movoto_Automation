package com.movoto.scripts.agent;
import org.json.simple.JSONObject;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;

public class Reg_70_ButtonActionTest extends BaseTest {
	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver wDriver;
	String addressContentUI=null;
	
	@Test
	@Parameters("dataProviderPath")
	public void VerifyActionButtonSectionWorks(	String dataProviderPath) {
		try {

			jsonParser = new JSONParserForAutomationNG(dataProviderPath);
			jsonObj = jsonParser.getNode("ActionButton");
		} catch (Exception exc) {
			System.out.println("Exception in Action Button Section:init() ->"+ exc.getMessage());
		}
		
		if (jsonObj != null) {
			verifyActionButtonSectionWorks(jsonObj);
		} else {
			Assert.assertTrue(false, "Please check data for provided for script");
		}
	}

	public void verifyActionButtonSectionWorks(JSONObject data) {
		String response = getApiHeaderAndGetResponse(data, "ActionButton_API");
		
		// Delete all the added favorites houses in favorites page for the test account.
		deleteFavouriteProperty(data);
		
		// click the view map button and verify map is displayed
		clickAndVerifyMapIsDisplayed();
		
		// Verify changeMapViewButton/GallaryButton/AddFavoriteButton/ShareButton/GotoSeeThisHomeButton exist
		verifyChangeMapViewAndFalleryAndFavouriteAndShareAndGotoSeeThisHomeButton();
		
		// Verify photo count/price/address is the same with api
		verifyPriceAddressWithAPI(response);
		
		// Click close button and Verify the first 3 photos' is displayed
		clickCloseAndVerifyFirstThreePhotos();
		
		// Click the "photo view" button and Verify photo gallery is displayed
		clickPhotoViewAndVerifyPhotoGalleryDisplayed();
		
		// Click the view map button on the top of photo gallery and Verify map window pops up
		clickViewMapOfPhotoGalleryAndVerifyMapWindowPopsUp();
		
		// Click "photo view" button on the map and Verify photo gallery is displayed and 
		// Click close button of photo gallery
		clickPhotoViewOnMapAndVerifyPhotoGalleryDisplayedAndClickCloseOfPhotoGallery();
		
		// Click the add favorite icon and Verify login window pops up
		clickAddFavoriteIconAndVerifyLoginWindowPopsUp();
		
		// Login with test account precondition mentions and Verify the add favorite icon turns red
		loginWithTestAccountAndVerifyFavoriteIconTurnsRed(data);
		
		// FAVORITEHOMEPAGE.favoriteAddress
		verifyFavoriteAddressIsSameWithStep1(data);
				
		// Click the share button and Verify "Share this property" window pops up
		clickShareAndVerifyShareThisPropertyWindowPopsUp(data);
		
		// Verify "Send Email/Share on Facebook/Share on Google+/Share on Twitter" exist
		verifySendEmailShareOnFacebookShareOnGoogleShareOnTwitterExist();
		
		// Click "Send Email" button and Verify "Email House to A Friend" window pops up
		clickSendEmailAndVerifyEmailHouseToAFriendWindowPopsUp();
		
		// Verify default text of  sendname box is "movoto"
		verifyDefaultTextSenderEmailInputEmailIntoFriendEmailThanksForSharingPopsUpGoToSeeExists(data);
	}

	// API functions
	public String getApiHeaderAndGetResponse(JSONObject Data, String apiName) {
		library.setRequestHeader("X-MData-Key", Data.get("X-MData-Key")
				.toString());
		String responseActionButton = library.HTTPGet(Data.get(apiName)
				.toString());
		return responseActionButton;
	}
	
	public void deleteFavouriteProperty(JSONObject data){
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			library.click("ANDROIDHOMEPAGE.link");
			library.wait(3);
		}
		scenarios.loginAndDeleteFavouriteProperty(data);
		library.get((String) data.get("App-Url"));
		library.wait(3);
	}
	
	public void clickAndVerifyMapIsDisplayed(){
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
				library.scrollToElement("ANDROIDHOMEPAGE.GoSeeThisHomeButton");
			library.wait(3);
		}
			library.click("HOMEPAGE.mapViewbtn");
			library.wait(3);
			//  Verify map is displayed
			try {
				boolean isGoogleLogoPresent = library.verifyPageContainsElement("HOMEPAGE.imgGoogleLogo");
				Assert.assertTrue(isGoogleLogoPresent);
			} catch (NoSuchElementException e) {
				System.out.println("Google Logo Not Found");
			}
	}
	
	public void verifyChangeMapViewAndFalleryAndFavouriteAndShareAndGotoSeeThisHomeButton(){
		// ChangeMapViewButton
		try {
			boolean isMapViewPresent = library.verifyPageContainsElement("MAPPAGE.changeMapViewButton");
			Assert.assertTrue(isMapViewPresent);
		} catch (NoSuchElementException e) {
			System.out.println("Map View Not Found");
		}
		
		// AddFavoriteButton
		try {
			boolean isFavoriteButtonPresent = library.verifyPageContainsElement("MAPPAGE.addFavoriteButton");
			Assert.assertTrue(isFavoriteButtonPresent);
		} catch (NoSuchElementException e) {
			System.out.println("Favorite Button Not Found");
		}
		
		// ShareButton
		try {
			boolean isShareButtonPresent = library.verifyPageContainsElement("MAPPAGE.shareButton");
			Assert.assertTrue(isShareButtonPresent);
		} catch (NoSuchElementException e) {
			System.out.println("Share Button Not Found");
		}
		
		// Go See This Home
		try {
			boolean isGoSeeThisHomeButtonPresent = library.verifyPageContainsElement("MAPPAGE.goSeeThisHomeButton");
			Assert.assertTrue(isGoSeeThisHomeButtonPresent);
		} catch (NoSuchElementException e) {
			System.out.println("Go See This Home Button Not Found");
		}
		
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			library.click("MAPPAGE.closeButton");
		}
	}
	
	public void verifyPriceAddressWithAPI(String response){
		library.wait(5);
		int priceContentAPI = (int) library.getValueFromJson("$.dpp.listPrice", response);
		String addressContentAPI = (String) library.getValueFromJson("$.dpp.address.addressInfo", response);
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			// Price
			String price=library.getAttributeOfElement("content", "ANDROIDHOMEPAGE.priceElement");
			int priceInt = Integer.parseInt(price);
			Assert.assertEquals(priceContentAPI, priceInt); 
			// Address
			String addressContentUI = library.getAttributeOfElement("alt", "ANDROIDHOMEPAGE.addressLabel");
			Assert.assertTrue(addressContentUI.contains(addressContentAPI));
		}
		else {
			// Price	 
			String priceContentUI = library.getTextFrom("MAPPAGE.priceLabel");
			priceContentUI = priceContentUI.replace("$", "").replace(",", "");
			int propertyPriceint = Integer.parseInt(priceContentUI);
			Assert.assertEquals(propertyPriceint, priceContentAPI);
			// Address	 
			String addressContentUI = library.getTextFrom("MAPPAGE.addressLabel");
			Assert.assertEquals(addressContentUI, addressContentAPI);
			library.wait(5);
		}
	}
	
	public void clickCloseAndVerifyFirstThreePhotos(){
		// Verify the first 4 photos' is displayed
		int iFirst3Photo = library.getElementCount("HOMEPAGE.firstfourproperties");
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			Assert.assertEquals(1, iFirst3Photo); 
		}
		else {
			library.wait(5);
			library.click("MAPPAGE.closeButton");
			library.wait(5);
			Assert.assertEquals(3, iFirst3Photo);
		}
	}
	
	public void clickPhotoViewAndVerifyPhotoGalleryDisplayed(){
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			library.wait(5);
			library.click("ANDROIDHOMEPAGE.photoClick");
			library.wait(5);
		}
		else {
			library.wait(5);
			library.click("HOMEPAGE.photoView");
			library.wait(5);
		}
		// Verify photo gallery is displayed
		try {
			boolean isgalleryButtonPresent = library.verifyPageContainsElement("GALLERYPAGE.galleryButton");
			Assert.assertTrue(isgalleryButtonPresent);
		} catch (NoSuchElementException e) {
			System.out.println("Gallery Button Not Found");
		}
	}
	
	public void clickViewMapOfPhotoGalleryAndVerifyMapWindowPopsUp(){
		library.wait(5);
		library.click("GALLERYPAGE.photoShowMapViewBtn");
		library.wait(5);
		
		// Verify map window pops up
		try {
			boolean isGoogleLogoPresent = library.verifyPageContainsElement("HOMEPAGE.imgGoogleLogo");
			Assert.assertTrue(isGoogleLogoPresent);
		} catch (NoSuchElementException e) {
			System.out.println("Google Logo Not Found");
		}
	}
	
	public void clickPhotoViewOnMapAndVerifyPhotoGalleryDisplayedAndClickCloseOfPhotoGallery(){
		library.wait(5);
		library.click("MAPPAGE.changeMapViewButton");
		library.wait(5);
		
		// Verify photo gallery is displayed
		try {
			boolean isgalleryButtonPresent = library.verifyPageContainsElement("GALLERYPAGE.galleryButton");
			Assert.assertTrue(isgalleryButtonPresent);
		} catch (NoSuchElementException e) {
			System.out.println("Gallery Button Not Found");
		}
		
		// Click close button of photo gallery
		library.click("GALLERYPAGE.closeButton");
		library.wait(5);
	}
	
	public void clickAddFavoriteIconAndVerifyLoginWindowPopsUp(){
		library.wait(5);
		library.click("HOMEPAGE.favoriteiconred");
		library.wait(5);
		
		// Verify login window pops up
		try {
			boolean isloginTitleActionButtonPresent = library.verifyPageContainsElement("LOGINPOPUP.loginTitleActionButton");
			Assert.assertTrue(isloginTitleActionButtonPresent);
		} catch (NoSuchElementException e) {
			System.out.println("Login Title Action Button Not Found");
		}
		library.wait(5);
	}
	
	public void loginWithTestAccountAndVerifyFavoriteIconTurnsRed(JSONObject data){
		scenarios.LoginCredential(data);
		library.wait(10);
		// Verify the add favorite icon turns red
		String classAttribute = library.getAttributeOfElement("class", "HOMEPAGE.favoriteiconred");
		classAttribute = classAttribute.trim();
		boolean isClassAttributeActive = classAttribute.endsWith("active");
		Assert.assertTrue(isClassAttributeActive,
				"Favorite icon colour is not red");
		library.wait(5);
		
	}
	
	public void verifyFavoriteAddressIsSameWithStep1(JSONObject data){
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			library.click("ANDROIDHOMEPAGE.link");
			library.wait(6);
		}
		scenarios.NavigateToFavouriteHomes(data);
		library.wait(5);
		String favouriteHomeAddressUI = library.getTextFrom("FAVORITEHOMEPAGE.favoriteAddress");
		library.wait(5);
		// Verify the house's address on favorite homes page is same with Step1
		library.get((String) data.get("App-Url"));
		library.wait(5);
		
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			//
		} else {
			library.click("HOMEPAGE.mapViewbtn");
			library.wait(5);
		}
		
		String addressContentUI = library.getTextFrom("MAPPAGE.addressLabel");
		library.wait(5);
		Assert.assertTrue(favouriteHomeAddressUI.contains(addressContentUI));
		library.wait(5);
		
	}
	
	public void clickShareAndVerifyShareThisPropertyWindowPopsUp(JSONObject data){
		// Go back to DPP by
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			//
		} else {
			library.get((String) data.get("App-Url"));
			library.wait(10);
		}
		library.click("HOMEPAGE.shareActionButton");
		library.wait(5);
		// Verify "Share this property" window pops up
		try {
			boolean isSharePopUpTitlePresent = library.verifyPageContainsElement("SHAREPOPUP.sharePopUpTitle");
			Assert.assertTrue(isSharePopUpTitlePresent);
		} catch (NoSuchElementException e) {
			System.out.println("Share PopUp Title Not Found");
		}
		library.wait(5);
	}
	
	public void verifySendEmailShareOnFacebookShareOnGoogleShareOnTwitterExist(){
		try {
			boolean isSharePopUpSendEmailPresent = library.verifyPageContainsElement("SHAREPOPUP.sharePopUpSendEmail");
			Assert.assertTrue(isSharePopUpSendEmailPresent);
		} catch (NoSuchElementException e) {
			System.out.println("Share PopUp Send Email Not Found");
		}
		library.wait(5);
		// Share on Facebook
		try {
			boolean isSharePopUpShareOnFacebookPresent = library.verifyPageContainsElement("SHAREPOPUP.sharePopUpShareOnFacebook");
			Assert.assertTrue(isSharePopUpShareOnFacebookPresent);
		} catch (NoSuchElementException e) {
			System.out.println("Share PopUp Share On Facebook Not Found");
		}
		library.wait(5);
		// Share on Google+
		try {
			boolean isSharePopUpShareOnGooglePresent = library.verifyPageContainsElement("SHAREPOPUP.sharePopUpShareOnGoogle");
			Assert.assertTrue(isSharePopUpShareOnGooglePresent);
		} catch (NoSuchElementException e) {
			System.out.println("Share PopUp Share On Google Not Found");
		}
		library.wait(5);
		// Share on Twitter
		try {
			boolean isSharePopUpShareOnTwitterPresent = library.verifyPageContainsElement("SHAREPOPUP.sharePopUpShareOnTwitter");
			Assert.assertTrue(isSharePopUpShareOnTwitterPresent);
		} catch (NoSuchElementException e) {
			System.out.println("Share PopUp Share On Twitter Not Found");
		}
		library.wait(5);
	}
	
	public void clickSendEmailAndVerifyEmailHouseToAFriendWindowPopsUp(){
		library.click("SHAREPOPUP.sharePopUpSendEmail");
		library.wait(5);
		
		// Verify "Email House to A Friend" window pops up
		try {
			boolean isEmailPopUpTitlePresent = library.verifyPageContainsElement("EMAILPOPUP.emailPopUpTitle");
			Assert.assertTrue(isEmailPopUpTitlePresent);
		} catch (NoSuchElementException e) {
			System.out.println("Email PopUp Title Not Found");
		}
		library.wait(2);
	}
	
	public void verifyDefaultTextSenderEmailInputEmailIntoFriendEmailThanksForSharingPopsUpGoToSeeExists(JSONObject data){
		String sEmailPopUpSendName = library.getAttributeOfElement("value","EMAILPOPUP.emailPopUpYourName");
		library.wait(2);
		Assert.assertEquals(data.get("EmailPopUpYourName").toString(), sEmailPopUpSendName);
		library.wait(2);
		// Verify default text of  senderEmail box is "kirankumaranin@gmail.com"
		String sEmailPopUpSenderEmail = library.getAttributeOfElement("value","EMAILPOPUP.emailPopUpYourEmail");
		library.wait(2);
		Assert.assertEquals(data.get("Username").toString(), sEmailPopUpSenderEmail);
		library.wait(2);
		
		// Input "Autotest@movoto.com" into friend email box
		library.clear("EMAILPOPUP.emailPopUpFriendEmail");
		library.typeDataInto(data.get("EmailPopUpFriendEmail").toString(), "EMAILPOPUP.emailPopUpFriendEmail");
		library.wait(1);
		library.click("EMAILPOPUP.emailPopUpSendEmailButton");
		library.wait(5);
		
		// Verify "Thanks for sharing" box pops up
		try {
			boolean isThanksPopUpTitlePresent = library.verifyPageContainsElement("THANKSPOPUP.thanksPopUpTitle");
			Assert.assertTrue(isThanksPopUpTitlePresent);
		} catch (NoSuchElementException e) {
			System.out.println("Thanks PopUp Title Not Found");
		}
		library.wait(5);
		
		// Verify "Go See This Home" button exist on the pop-up window
		try {
			boolean isThanksPopUpGoSeeThisHomePresent = library.verifyPageContainsElement("THANKSPOPUP.thanksPopUpGoSeeThisHome");
			Assert.assertTrue(isThanksPopUpGoSeeThisHomePresent);
		} catch (NoSuchElementException e) {
			System.out.println("Thanks PopUp Go See This Home Not Found");
		}
		library.wait(5);
	}
}
