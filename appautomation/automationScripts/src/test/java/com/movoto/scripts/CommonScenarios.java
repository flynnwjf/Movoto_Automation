package com.movoto.scripts;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import com.movoto.fixtures.FixtureLibrary;
import com.movoto.fixtures.impl.util.FileUtil;
import bsh.ParseException;
import groovy.json.JsonException;

public class CommonScenarios {

	protected FixtureLibrary library;

	public CommonScenarios(FixtureLibrary library) {
		this.library = library;
	}

	public void openUrl(String url) {
		library.open(url);
	}

	public void loginToAgentAppWithUsernameAndPassword(String username, String password) {
        disableChatWindow();
		library.typeDataInto(username + "\n", "LOGIN.usernamefield");
		library.typeDataInto(password + "\n", "LOGIN.passwordfield");
		library.click("LOGIN.loginbutton");
	}

	public void verifyLoginSuccess() {
        disableChatWindow();
		handlePreview();
		Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.hamburger"), "Login was not successful!");
	}

	public void navigateToNotificationsListPage() {
		library.wait(3);
		Point start = getScreenCoordinatesForPercentInXAxis(5);
		Point end = getScreenCoordinatesForPercentInXAxis(80);
		library.swipeFromTo(start.x, start.y, end.x, end.y);
		// library.click("HOMEPAGE.hamburger");
		library.click("MENU.notificationpagelink");
	}

	public boolean verifyNotificationExists(String text) {
		// String xpath = "xpath->//android.widget.TextView[@text='" + text +
		// "']";
		String loc = "name->" + text;
		return library.verifyPageContainsElement(loc);
	}

	public void navigateToClientListPage() {
		library.wait(3);
		Point start = getScreenCoordinatesForPercentInXAxis(5);
		Point end = getScreenCoordinatesForPercentInXAxis(80);
		library.swipeFromTo(start.x, start.y, end.x, end.y);
		// library.click("HOMEPAGE.hamburger");
		library.click("name->Client List");
	}

	public void verifyClientNameWith(String clientName) {
		String xpath = "name->" + clientName;
		Assert.assertTrue(library.verifyPageContainsElement(xpath), "Client " + clientName + " not found!");
	}

	public void verifyTransactionStageForClientWith(String client, String stage) {
		String stageLocator = getStageLocator(client, stage);
		library.verifyPageContainsElement(stageLocator);
	}

	// Updated By Puneet dated : - 23-may-16
	public void updateToMadeAnOffer(Map<String, String> data) {

		String clientName = data.get("ClientName");

		openClientDetailPage(clientName);

		library.click("TRANSACTION.updatebutton");// update
		chooseTransactionStage("Made an Offer");
		library.click("UPDATE.applybutton");// apply
		handleEditTransactionAlert();

		enterAddress(data.get("Address"));

		//library.navigateBack();
		library.wait(2);
		boolean isEnabled = library.isElementEnabled("MAKEOFFER.continuebutton", true);
		Assert.assertTrue(isEnabled, "Continue button is not enabled!");
		library.click("MAKEOFFER.continuebutton");// continue

		String dateStr = data.get("OfferDate");
		String dates[] = dateStr.split("-");

		Assert.assertTrue(library.isElementEnabled("MAKEOFFER.datefield", true), "Date field is not enabled!");
		openDateField("MAKEOFFER.datefield");
		chooseDate(dates[0], dates[1], dates[2]);

		library.wait(3);
		Assert.assertTrue(library.isElementEnabled("MAKEOFFER.price", true), "Price field is not enabled!");
		library.clear("MAKEOFFER.price");
		enterNumber(data.get("OfferPrice"), "MAKEOFFER.price");
		// library.typeDataInto(data.get("OfferPrice"), "MAKEOFFER.price");//
		// offerprice
		Assert.assertTrue(library.isElementEnabled("MAKEOFFER.continuebutton", true),
				"Continue button is not enabled!");
		library.click("MAKEOFFER.continuebutton");// continue

		String offerdAddress = library.getTextFrom("MOCONFIRM.address");
		String offerPrice = library.getTextFrom("MOCONFIRM.price");
		String date = library.getTextFrom("MOCONFIRM.date");
		Reporter.log("Address: " + offerdAddress, true);
		Reporter.log("Offer Price: " + offerPrice, true);
		Reporter.log("Offered Date: " + date, true);

		library.wait(3);
		Assert.assertTrue(library.isElementEnabled("MOCONFIRM.submitbutton", true), "Continue button is not enabled!");
		library.click("MOCONFIRM.submitbutton");// submit
		library.wait(12);

	}

	protected void enterNumber(String data, String loc) {
		library.clear(loc);
		library.typeDataInto(data + "\n", loc);
	}

	protected void handleEditTransactionAlert() {
		library.setImplicitWaitTime(5);
		WebElement element=library.findElement("MAKEOFFER.editok");
		library.setImplicitWaitTime(30);
		if (element!=null) {
			library.click("MAKEOFFER.editok");// alert yes
		}
	}

	// Updated By Puneet dated : - 23-may-16
	protected void enterAddress(String address) {
		String addressList[] = address.split(",");
		if (addressList.length == 4) {
			String street = addressList[0];
			street = street.trim();

			String city = addressList[1];
			city = city.trim();
			String state = addressList[2];
			state = state.trim();
			String zip = addressList[3];
			zip = zip.trim();

			library.typeDataInto(street, "MOADDRESS.street");
			library.typeDataInto(city, "MOADDRESS.city");
			library.navigateBack();
			library.click("MOADDRESS.state");
			library.scrollTo(state);
			String loc = "name->" + state;
			library.click(loc);

			library.typeDataInto(zip, "MOADDRESS.zip");
			library.navigateBack();
			library.wait(2);
			library.typeDataInto("12345", "MO.mlsno");
			library.wait(5);
			library.navigateBack();
		}

		// library.typeDataInto(address + "\n", "MAKEOFFER.address");// address
	}

	// Updated By Puneet dated : - 23-may-16
	public void updateToContractAcceptedStage(Map<String, String> data) {

		String dateStr = data.get("contractDate");
		String dates[] = dateStr.split("-");

		String clientName = data.get("ClientName");

		library.click("name->" + clientName);// client
		library.waitForElement("TRANSACTION.properties");
		library.wait(3);
		library.click("TRANSACTION.updatebutton");// update
		chooseTransactionStage("Contract Accepted");
		library.click("UPDATE.applybutton");// apply

		//openDateField("CONTRACT.date");
		//chooseDate(dates[0], dates[1], dates[2]);
		library.wait(3);
		Assert.assertTrue(library.verifyPageContainsElement("MAKEOFFER.continuebutton"),
				"Continue button is disabled");
		library.click("MAKEOFFER.continuebutton");// 1 st continue button 
		library.wait(5);
		//Assert.assertTrue(library.verifyPageContainsElement("CONTRACT.confirmnamebutton", true),"Name is correct on contract is not there");
		library.click("CONTRACT.confirmnamebutton");// Confirm Name
		library.wait(5);
		Assert.assertTrue(library.verifyPageContainsElement("CONTRACT.commissionsmoke"),"Commision field is not enabled");
		Assert.assertTrue(library.verifyPageContainsElement("CONTRACT.price"), "MAKEOFFER price is not enabled");
		enterNumber(data.get("contractPrice"), "CONTRACT.price");
		
		//library.getDriver().findElement(By.xpath("//UIATextField[contains(@value,'Enter Contract Price')]")).sendKeys(Keys.TAB);
		enterNumber(data.get("commission"), "CONTRACT.commissionsmoke");
   /*library.wait(5);
   library.click("MAKEOFFER.price");
   library.clear("MAKEOFFER.price");
	library.typeDataInto(data.get("contractPrice") + "\n","MAKEOFFER.price");// offerprice
	library.wait(3);
	library.click("CONTRACT.commission");
	   library.clear("CONTRACT.commission");
	
		 library.typeDataInto(data.get("commission") + "\n", "CONTRACT.commission");
		library.wait(3);*/
		
		Assert.assertTrue(library.verifyPageContainsElement("CONTRACT.continuebutton"),
				"Continue button is not enabled");
		library.click("CONTRACT.continuebutton");// continue
		library.wait(2);

		Assert.assertTrue(library.verifyPageContainsElement("CONTRACT.fname"), "First name is not enabled");
		Assert.assertTrue(library.verifyPageContainsElement("CONTRACT.lname"), "Last name is not enabled");
		Assert.assertTrue(library.verifyPageContainsElement("CONTRACT.phone"), "Phone is not enabled");
		Assert.assertTrue(library.verifyPageContainsElement("CONTRACT.email"), "Email is not enabled");

		library.typeDataInto(data.get("contractFname") + "\n", "CONTRACT.fname");// First
																					// Name
		library.typeDataInto(data.get("contractLname") + "\n", "CONTRACT.lname");// Last
																					// Name
		enterNumber(data.get("phone"), "CONTRACT.phone");

		// library.typeDataInto(data.get("phone") + "\n", "CONTRACT.phone");//
		// Phone
		library.typeDataInto(data.get("email") + "\n", "CONTRACT.email");// Email
		library.wait(3);
		Assert.assertTrue(library.verifyPageContainsElement("CONTRACT.submitinfobutton"),
				"Submit button is not enabled");
		library.click("CONTRACT.submitinfobutton");// continue
		library.waitForElement("TRANSACTION.properties");
		library.wait(12);
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
		library.click("TRANSACTION.closeproperties");
		library.wait(3);
	}

	protected void openDateField(String field) {
		library.tapOnElement(field);// date field
		library.wait(4);

		Point point = getScreenCoordinatesForPercentInYAxis(20);
		library.tapOnLocation(point.x, point.y);
		library.wait(2);
		library.tapOnElement(field);// date field
	}

	protected void clearText() {
		boolean textExists = library.verifyPageContainsElement("name->Clear text");
		if (textExists) {
			library.click("name->Clear text");
		}
	}

	protected void chooseDate(String month, String day, String year) {

		library.clear("DATE.day");
		library.typeDataInto(day + "\n", "DATE.day");// day

		library.clear("DATE.year");
		library.typeDataInto(year + "\n", "DATE.year");// year

		// chooseMonth(month);

		library.click("DATE.okbutton");// ok
	}

	protected void chooseTransactionStage(String stage) {
		library.click("name->" + stage);// radio
	}

	public int getNotificationCount() {
		navigateToNotificationsListPage();
		return library.getElementCount("NOTIF.holder");
	}

	public void verifyDateForNotificationIsValid(int index) {

		library.wait(3);
		// library.scrollTo("03/31/2016");
		//
		// String date = library.getTextFrom(getDateLocatorForIndex(index));
		// Assert.assertNotNull(date, "Date is null");
		// verifyDate(date);

	}

	protected void verifyDate(String date) {
		date = date.split("/")[1];
		int dateInt = Integer.valueOf(date);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		int expDay = cal.get(Calendar.DAY_OF_MONTH);
		Assert.assertTrue(dateInt >= expDay, "Invalid Date");
	}

	protected String getDateLocatorForIndex(int index) {
		String datePath = "xpath->//android.widget.LinearLayout[@index='" + index
				+ "' and contains(@resource-id,'item_holder')]//android.widget.TextView[contains(@resource-id,'date_holder')]";
		return datePath;
	}

	public void setFutureDate(Map<String, String> data) {
		library.wait(5);
		openMenu();
		library.click("MENU.goonleavebutton");
		library.click("MENU.futureleavebutton");
		boolean leaveExists = library.verifyPageContainsElement("FLEAVE.cancelleavebutton");
		if (leaveExists) {
			library.click("FLEAVE.cancelleavebutton");
			library.click("FLEAVE.deleteleavebutton");
			library.wait(5);
			library.click("FLEAVE.createbutton");
		}

		String startDate = data.get("fromDate");
		String endDate = data.get("toDate");

		chooseStartAndEndLeaveDates(startDate, endDate);
		library.click("FLEAVE.schedulebutton");
		leaveExists = library.verifyPageContainsElement("FLEAVE.cancelleavebutton");
	}

	public void openMenu() {
		library.wait(3);
		Point start = getScreenCoordinatesForPercentInXAxis(5);
		Point end = getScreenCoordinatesForPercentInXAxis(80);
		library.swipeFromTo(start.x, start.y, end.x, end.y);

	}

	public void closeMenu() {
		library.wait(3);
		Point start = getScreenCoordinatesForPercentInXAxis(80);
		Point end = getScreenCoordinatesForPercentInXAxis(5);
		library.swipeFromTo(start.x, start.y, end.x, end.y);

	}

	public void chooseStartAndEndLeaveDates(String startDate, String endDate) {

		String sdate[] = startDate.split("-");
		String edate[] = endDate.split("-");

		library.click("FLEAVE.startdatefield");
		library.click("FLEAVE.startdatefield");
		library.click("FLEAVE.startdatefield");
		chooseLeaveDate(sdate[0], sdate[1], sdate[2]);
		library.click("FLEAVE.startdatefield");

		library.click("FLEAVE.enddatefield");
		chooseLeaveDate(edate[0], edate[1], edate[2]);
		library.click("FLEAVE.enddatefield");
	}

	protected void chooseLeaveDate(String month, String day, String year) {

		chooseMonth(month);
		chooseDay(day);
		chooseYear(year);

	}

	protected void chooseMonth(String month) {

		String fromMonth = "xpath->//android.widget.DatePicker//android.widget.NumberPicker[@index='0']/android.widget.Button[@index='2']";
		String toMonth = "xpath->//android.widget.DatePicker//android.widget.NumberPicker[@index='0']/android.widget.EditText";
		String selectedMonth = library.getTextFrom(
				"xpath->//android.widget.DatePicker//android.widget.NumberPicker[@index='0']/android.widget.EditText");
		int count = 0;
		while (!selectedMonth.equalsIgnoreCase(month) && count < 12) {
			// library.swipeFromTo(200, 650, 200, 550);
			scrollDate(fromMonth, toMonth);
			library.wait(2);
			selectedMonth = library.getTextFrom(
					"xpath->//android.widget.DatePicker//android.widget.NumberPicker[@index='0']/android.widget.EditText");
			count++;
		}

	}

	protected void scrollDate(String from, String to) {
		String fromXY = library.getElementCenterCoordinates(from);
		String toXY = library.getElementCenterCoordinates(to);

		int fromx = Integer.valueOf(fromXY.split(",")[0]);
		int fromy = Integer.valueOf(fromXY.split(",")[1]);
		int tox = Integer.valueOf(toXY.split(",")[0]);
		int toy = Integer.valueOf(toXY.split(",")[1]);

		library.swipeFromTo(fromx, fromy, tox, toy);

	}

	protected void chooseDay(String day) {
		library.clear(
				"xpath->//android.widget.DatePicker//android.widget.NumberPicker[@index='1']/android.widget.EditText");
		library.typeDataInto(day + "\n",
				"xpath->//android.widget.DatePicker//android.widget.NumberPicker[@index='1']/android.widget.EditText");// year
	}

	protected void chooseYear(String year) {
		library.clear(
				"xpath->//android.widget.DatePicker//android.widget.NumberPicker[@index='2']/android.widget.EditText");
		library.typeDataInto(year + "\n",
				"xpath->//android.widget.DatePicker//android.widget.NumberPicker[@index='2']/android.widget.EditText");// year
	}

	protected void handlePreview() {
		boolean previewExists = library.verifyPageContainsElement("HOMEPAGE.previewnextbutton");
		if (previewExists) {
			library.click("HOMEPAGE.previewnextbutton");
			library.click("HOMEPAGE.previewnextbutton");
			library.click("HOMEPAGE.previewnextbutton");
			library.click("HOMEPAGE.previewdonebutton");
		}
	}

	protected String getStageLocator(String client) {
		return "xpath->//android.widget.TextView[@text='" + client
				+ "']/../android.widget.LinearLayout[@index='1']/android.widget.RelativeLayout/android.widget.TextView[@index='1']";
	}

	protected String getStageLocator(String client, String stage) {
		return "xpath->//android.widget.TextView[@text='" + client
				+ "']/../android.widget.LinearLayout[@index='1']/android.widget.RelativeLayout/android.widget.TextView[@index='1' and @text='"
				+ stage + "']";
	}

	// Updated By Puneet dated : - 23-may-16
	public void updateUrgencyStage(Map<String, String> data) {
		String urgencyCode = data.get("urgencyCode");
		String urgencyName = data.get("urgencyName");
		String clientName = data.get("clientName");
		String searchName = clientName.substring(0, 3);
		openClientDetailPage(clientName);

		library.click("URGENCY.image"); // click on urgency
		String updateLocator = "name->" + urgencyName;
		library.click(updateLocator); // click on radio button
		library.isElementEnabled("URGENCY.apply", true); // verify Apply button
		library.click("URGENCY.apply");// click on apply button
		library.waitForElement("URGENCY.notes");
		library.wait(6);
		library.click("URGENCY.notes"); // click on Notes
		library.wait(3);

		String urgencyCodeLocator = "name->" + urgencyCode;
		library.setImplicitWaitTime(5);
		library.verifyPageContainsElement(urgencyCodeLocator);

		String noteUpdateLocator = getUrgencyNotesLocator(urgencyName);
		library.verifyPageContainsElement(noteUpdateLocator);
		library.setImplicitWaitTime(30);
		library.click("CLOSE.notes");
		library.wait(3);

	}

	public void searchForClientAndVerify(Map<String, String> data) {

		String clientName = data.get("clientName");
		String searchName = clientName;
		String clientFullName = data.get("clientFullName");
		library.click("HOMEPAGE.searchbutton"); // click on search
		library.verifyPageContainsElement("HOMEPAGE.typeintosearch");
		library.typeDataInto(searchName, "HOMEPAGE.typeintosearch");
		library.navigateBack();
		String resultLocator = "name->" + clientFullName;
		library.verifyPageContainsElement(resultLocator);// type client
																// name in
																// search field
		library.setImplicitWaitTime(2);
		boolean alertExists = library.verifyPageContainsElement("SEARCH.alert");
		boolean isMessagePresent = library.verifyPageContainsElement("SEARCH.noresult");
		library.setImplicitWaitTime(30);

		if (alertExists) {
			library.click("SEARCH.cancelalert");
		}

		library.click("SEARCH.cancel");
		library.wait(3);

	}

	public void loginWithUsernameAndPasswordAndExpectError(String username, String password, String error) {

		library.clear("LOGIN.usernamefield");
		library.typeDataInto(username + "\n", "LOGIN.usernamefield");
		library.clear("LOGIN.passwordfield");
		library.typeDataInto(password + "\n", "LOGIN.passwordfield");
		library.click("LOGIN.loginbutton");
		if (error != null && error.length() > 0) {
			String errorLoc = "name->" + error;
			boolean result = library.verifyPageContainsElement(errorLoc);
			boolean alertExists = library.verifyPageContainsElement("name->OK");
			if (result || alertExists) {
				// LOGIN.alertmessage
				library.click("name->OK");
			}

		} else {
			verifyLoginSuccess();
		}
		library.wait(3);
	}

	public String getAccessToken(Map<String, Object> data) {
		String xMdataKey = String.valueOf(data.get("XMDataKey"));
		String contentType = String.valueOf(data.get("ContentType"));
		String accessTokenUrl = String.valueOf(data.get("AccessTokenURL"));
		Map<String, Object> pData = new HashMap<>();
		library.setContentType(contentType);
		library.setRequestHeader("X-MData-Key", xMdataKey);
		String response = library.HTTPPost(accessTokenUrl, pData);
		return response;
	}

	public String sendNotification(Map<String, Object> data, String accessTokenResponse) {

		String xuserid = String.valueOf(library.getValueFromJson("$.id", accessTokenResponse));
		String accessToken = String.valueOf(library.getValueFromJson("$.token", accessTokenResponse));

		String autorization = "Bearer " + accessToken;
		String contentType = String.valueOf(data.get("ContentType"));
		String notificationUrl = String.valueOf(data.get("NotificationURL"));
		Map<String, Object> notificationData = (Map<String, Object>) data.get("NotificationData");

		library.setContentType(contentType);
		library.setRequestHeader("Authorization", autorization);
		library.setRequestHeader("x-userid", xuserid);
		String response = library.HTTPPost(notificationUrl, notificationData);
		System.out.println(response);
		Object message = library.getValueFromJson("$.sentNotifications[0].notificationInfo.notificationMessage",
				response);
		library.wait(3);
		System.out.println(message);
		return String.valueOf(message);
	}

	public void verifyN0(String message) {
		String loc = "name->" + message;
		String yesButton = "name->Yes";
		String doneButton = "name->Done";
		library.click(loc);
		Assert.assertTrue(library.verifyPageContainsElement(yesButton), "N0 cannot be opened!");
		library.navigateBack();
		library.wait(5);
	}

	public void verifyFC5(String message) {
		String loc = "name->" + message;
		String talkedButton = "name->Talked";
		String doneButton = "name->Done";
		library.click(loc);
		Assert.assertTrue(library.verifyPageContainsElement(talkedButton), "FC5 cannot be opened!");
		library.navigateBack();
		library.wait(5);
		// library.click(yesButton);
		// library.click(doneButton);

	}

	// Updated by Puneet dated:- 23-may-16
	public void updateToStageForClient(String stage, String client) {

		String saveNotesLoc = "name->Save Update";
		if (stage.equalsIgnoreCase("Scheduled a Callback")) {
			saveNotesLoc = "name->Set Reminder";
		}

		openClientDetailPage(client);
		// searchAndSelectClient(client);

		library.click("TRANSACTION.updatebutton");
		chooseTransactionStage(stage);
		// library.click(stageLoc);

		library.click("UPDATE.applybutton");
		library.typeDataInto("Agent Automation", "NOTES.notesfield");
		library.navigateBack();
		library.wait(3);
		if (stage.equalsIgnoreCase("Contract Cancelled")) {
			Assert.assertTrue(library.verifyPageContainsElement(saveNotesLoc));
		}
		if (stage.equalsIgnoreCase("Scheduled a Meeting")) {
			Assert.assertTrue(library.verifyPageContainsElement(saveNotesLoc), "Save Button not enabled");
			Assert.assertTrue(library.verifyPageContainsElement("SMUPDATE.date"), "Date field not enabled");
			Assert.assertTrue(library.verifyPageContainsElement("SMUPDATE.starttime"), "Start Date not enabled");
			Assert.assertTrue(library.verifyPageContainsElement("SMUPDATE.endtime"), "End Date not enabled");
		}
		// library.verifyPageContainsElement(saveNotesLoc, true);
		// library.verifyPageContainsElement("SMUPDATE.starttime", true);
		// library.verifyPageContainsElement("SMUPDATE.starttime", true);
		// library.verifyPageContainsElement("SMUPDATE.endtime", true);
		//

		library.click(saveNotesLoc);
		library.waitForElement("TRANSACTION.properties");
		library.wait(5);
	}

	public void selectClient(String client) {
		String loc = "name->" + client;
		library.scrollTo(client);
		library.click(loc);
	}

	// Updated By Puneet dated : - 23-may-16
	public void updateToStage(Map<String, Object> data) {
		String clientName = String.valueOf(data.get("clientName"));
		String targetStage = String.valueOf(data.get("updateTransactionStage"));

		if (targetStage.contains("Made an Offer")) {
			Map<String, String> map = (Map<String, String>) data.get("MadeOfferData");
			Assert.assertNotNull(map);
			updateToMadeAnOffer(map);
		} else if (targetStage.contains("Contract Accepted")) {
			Map<String, String> map = (Map<String, String>) data.get("contractAcceptData");
			Assert.assertNotNull(map);
			updateToContractAcceptedStage(map);
		} else {
			updateToStageForClient(targetStage, clientName);
		}

	}

	protected void openClientDetailPage(String client) {

		String clientLoc = "name->" + client;
		library.setImplicitWaitTime(4);
		boolean propertyPage = library.verifyPageContainsElement("TRANSACTION.properties");
		library.setImplicitWaitTime(30);
		boolean clientExists = library.verifyPageContainsElement(clientLoc);

		if (!propertyPage && clientExists) {
			library.click(clientLoc);// client
			library.waitForElement("TRANSACTION.properties");
			library.wait(3);
		}
	}

	// Updated By Puneet dated : - 23-may-16
	public void searchAndSelectClient(String client) {
		String searchName = client.substring(0, 3);
		library.click("HOMEPAGE.searchbutton"); // click on search
		library.verifyPageContainsElement("HOMEPAGE.typeintosearch"); // checking search button is present or not.
		library.typeDataInto(searchName, "HOMEPAGE.typeintosearch");

		String resultLocator = "name->"+client;
		System.out.println(resultLocator);
		boolean isClientVisible = library.verifyPageContainsElement(resultLocator);

		if (!isClientVisible) {
			library.scrollTo(resultLocator);
			Assert.assertTrue(library.verifyPageContainsElement(resultLocator),"Client, " + client + ", not found");
					
			library.click(resultLocator);

		} else {

			Assert.assertTrue(library.verifyPageContainsElement(resultLocator),"Client, " + client + ", not found");
					
			library.click(resultLocator);
		}

	}

	public void waitWhileLoading() {
		/*
		 * boolean isLoading =
		 * library.verifyPageContainsElement("COMMON.activityindicator", false);
		 * while (isLoading) { library.setImplicitWaitTime(1); isLoading =
		 * library.verifyPageContainsElement("COMMON.activityindicator", false);
		 * } library.setImplicitWaitTime(30);
		 */
		library.wait(7);
	}

	public Point getScreenCoordinatesForPercentInXAxis(int percent) {
		Point point = new Point(0, 0);
		String size = library.getWindowSize();
		String points[] = size.split(",");
		int width = Integer.valueOf(points[0]);
		int height = Integer.valueOf(points[1]);

		int y = height / 2;
		float xf = width * ((float) percent / 100);
		int x = (int) Math.floor(xf);

		point.setLocation(x, y);
		return point;
	}

	public Point getScreenCoordinatesForPercentInYAxis(int percent) {
		Point point = new Point(0, 0);
		String size = library.getWindowSize();
		String points[] = size.split(",");
		int width = Integer.valueOf(points[0]);
		int height = Integer.valueOf(points[1]);

		int x = width / 2;
		float yf = height * ((float) percent / 100);
		int y = (int) Math.floor(yf);

		point.setLocation(x, y);
		return point;
	}

	public void logOut() {
		library.setImplicitWaitTime(3);
		boolean isLoggedIn = library.verifyPageContainsElement("HOMEPAGE.hamburger");
		library.setImplicitWaitTime(30);
		if (isLoggedIn) {
			openMenu();
			library.click("name->Log Out");
			library.waitForElement("LOGIN.usernamefield");
		}
	}

	// Updated By Puneet dated : - 23-may-16
	protected String getUrgencyNotesLocator(String urgencyName) {
		return "xpath->//android.widget.TextView[contains(@text,'" + urgencyName + "')]";
	}

	public void verifyFutureLeaveCount(Map<String, String> data) {

		boolean leaveExists = library.verifyPageContainsElement("FLEAVE.cancelleavebutton");
		if (leaveExists) {
			library.click("FLEAVE.cancelleavebutton");
			library.click("FLEAVE.deleteleavebutton");
			library.wait(5);
			library.click("FLEAVE.createbutton");
		}

		String startDate = data.get("fromDate");
		String endDate = data.get("toDate");

		chooseStartAndEndLeaveDates(startDate, endDate);
		library.click("FLEAVE.schedulebutton");
		leaveExists = library.verifyPageContainsElement("FLEAVE.cancelleavebutton");

	}

	// Updated By Puneet dated : - 23-may-16
	public void getClientListData(Map<String, Object> data) {

		Map<String, Object> accessData = getAccessTokenId(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(data.get("ContentType"));
		String contactURL = String.valueOf(data.get("contactsUrl"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);
		String response = library.HTTPGet(contactURL);
		System.out.println(response);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		Point starts = getScreenCoordinatesForPercentInYAxis(60);
		Point ends = getScreenCoordinatesForPercentInYAxis(30);
		library.swipeFromTo(starts.x, starts.y, ends.x, ends.y);
		library.wait(1);
		for (int i = 0; i < arrayCount; i++) {
			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;
			System.out.println(LeadName);
		}
		for (int i = 0; i < arrayCount; i++) {
			String verifyopportunityStage = null;
			verifyopportunityStage = String.valueOf(library.getValueFromJson(
					"$.clientList[" + i + "].contactInfo.opportunitiesSummary.openOpportunities[0].opportunityStage",
					response));
			String opportunityStage = null;
			if (verifyopportunityStage == "null") {
				opportunityStage = "Follow-up Now";
				System.out.println(opportunityStage);
			}

			else {
				opportunityStage = (String) library.getValueFromJson("$.clientList[" + i
						+ "].contactInfo.opportunitiesSummary.openOpportunities[0].opportunityStage", response);
				System.out.println(opportunityStage);
			}

		}

	}

	// Created by Puneet
	public void searchByClientEmailIdAndVerify(Map<String, String> map) {

		String clientEmailId = map.get("clientEmail");
		String clientPhoneNumber = map.get("clientPhoneNumber");
		String clientName = map.get("clientName");

		library.click("HOMEPAGE.searchbutton");
		library.typeDataInto(clientEmailId, "HOMEPAGE.typeintosearch");
		library.wait(3);
		String resultLocator = "name->" + clientName;
		Assert.assertTrue(library.verifyPageContainsElement(resultLocator), "clientName, not found");
	}

	// Created by Puneet
	public void searchByClientPhoneNumberAndVerify(Map<String, String> map) {

		String clientEmailId = map.get("clientEmail");
		String clientPhoneNumber = map.get("clientPhoneNumber");
		String clientName = map.get("clientName");

		library.click("HOMEPAGE.searchbutton");
		library.typeDataInto(clientPhoneNumber, "HOMEPAGE.typeintosearch");
		library.wait(3);
		String resultLocator = "name->" + clientName;
		Assert.assertTrue(library.verifyPageContainsElement(resultLocator), "clientName, not found");
	}

	// Created by Puneet
	public void navigateToFutureLeavePage() {
		library.wait(5);
		openMenu();
		library.click("MENU.goonleavebutton");
		library.click("MENU.futureleavebutton");
	}

	public void navigateToImmediatelyLeavePage() {
		library.wait(5);
		openMenu();
		library.click("MENU.goonleavebutton");
		boolean present = library.verifyPageContainsElement("MENU.returnnowbutton");
		if (present == true) {
			library.click("MENU.returnnowbutton");
			library.wait(5);
			openMenu();
			library.wait(1);
			library.click("MENU.immediatelybutton");

		} else {
			library.click("MENU.goonleavebutton");
			library.wait(1);
			library.click("MENU.goonleavebutton");
			library.click("MENU.immediatelybutton");
		}

	}

	// Created by Gopal
	public void verifyImmediatelyButton() {
		library.wait(3);
		openMenu();
		library.click("MENU.goonleavebutton");
		library.verifyPageContainsElement("MENU.immediatelyleave");
	}

	// Created by Gopal
	public void verifyReturnNow() {
		library.wait(3);
		openMenu();
		// library.click("MENU.goonleavebutton");
		library.verifyPageContainsElement("MENU.retrunnow");
	}

	// Gopal
	public void deleteScheduleLeaveAndVerify() {
		library.wait(2);
		library.click("LEAVEPAGE.deletebuttonimage");
		library.wait(2);
		library.click("FLEAVE.deleteleavebutton");
		library.verifyPageContainsElement("LEAVEPAGE.deletebuttonimage");
	}

	// created by Gopal
	public void goToClientListPage() {
		library.click("HOMEPAGE.hamburger");
		library.click("MENU.clientlist");
	}

	// Created by Gopal
	public void tapSearchIconOnHeaderAndVerify() {
		library.click("FLEAVE.searchicon");
		library.verifyPageContainsElement("CLIENTPAGE.searchtextfield");
		library.click("SEARCH.cancel");
	}

	// Created by Puneet
	public void verifyTapOnCancelSteps() {
		library.wait(5);
		library.click("FLEAVE.createbutton");
		library.click("SCHEDULELEAVE.cancel");
		// library.verifyPageContainsElement("FUTURELEAVE.page",true);
		library.verifyPageContainsElement("FLEAVE.createbutton");
		boolean leaveExists = library.verifyPageContainsElement("FLEAVE.cancelleavebutton");
		if (!leaveExists) {
			library.verifyPageContainsElement("FLEAVE.createbutton");
		}
	}

	// Created by Puneet
	public void verifyContractCancelStageSteps(Map<String, Object> data) {

		if (data != null) {
			String clientName = String.valueOf(data.get("clientName"));
			String postStage = String.valueOf(data.get("postStage"));
			library.wait(3);
			searchAndSelectClient(clientName);
			waitWhileLoading();
			library.wait(3);
			updateToStage(data);
			waitWhileLoading();
			navigateToClientListPage();
			waitWhileLoading();
			verifyTransactionStageForClientWith(clientName, postStage);
		}

	}

	// Created By Gopal
	public void verifyImmediateLeaveStatus() {
		boolean isReturnEarlyPresent = library.verifyPageContainsElement("LIST.returnearly");
		if (isReturnEarlyPresent)
			library.click("LIST.returnearly");

		verifyImmediatelyButton();
		library.click("MENU.immediatelyleave");
		library.verifyPageContainsElement("LIST.returnearly");
		library.click("LIST.returnearly");
		openMenu();
		library.verifyPageContainsElement("MENU.immediatelyleave");
	}

	// Created by Gopal
	public void verifyFutureLeaveCreationCriterias(Map<String, String> data) {
		navigateToFutureLeavePage();

		boolean isCreateButtonPresent = library.verifyPageContainsElement("FLEAVE.createbutton");
		if (isCreateButtonPresent)
			library.click("FLEAVE.createbutton");

		String startDate = data.get("fromDate");
		String endDate = data.get("toDate");

		chooseStartAndEndLeaveDates(startDate, endDate);
		library.click("FLEAVE.schedulebutton");
		library.verifyPageContainsElement("SCHEDULELEAVE.cancel");
		if (library.verifyPageContainsElement("SCHEDULELEAVE.cancel"))
			library.click("SCHEDULELEAVE.cancel");

	}

	// Created by Gopal
	public void verifydeletionOfExistingLeave() {
		navigateToFutureLeavePage();
		library.wait(2);

		boolean isLeavePresent = library.verifyPageContainsElement("LEAVEPAGE.deletebuttonimage");

		if (isLeavePresent == false) {
			library.click("FLEAVE.schedulebutton");
		}

		deleteScheduleLeaveAndVerify();
	}

	// Created by Puneet

	public void verifyClientCardSteps(Map<String, String> map) {
		String clientFullName = map.get("clientFullName");
		String searchName = clientFullName.substring(0, 3);
		library.click("HOMEPAGE.searchbutton"); // click on search
		library.verifyPageContainsElement("HOMEPAGE.typeintosearch");
		library.typeDataInto(searchName, "HOMEPAGE.typeintosearch");
		library.wait(3);
		String resultLocator = "name->" + clientFullName;
		Assert.assertTrue(library.verifyPageContainsElement(resultLocator),
				"Client, " + clientFullName + ", not found");
		library.navigateBack();
		library.click("CLIENT.smsbutton");
		library.wait(3);
		// library.verifyPageContainsElement("MESSAGE.button", true);
		library.click("MESSAGE.button");
		library.verifyPageContainsElement("MESSAGE.sendicon");
		library.navigateBack();
		library.navigateBack();
		library.click(resultLocator);
		library.wait(5);
		library.verifyPageContainsElement("CLIENT.backbutton"); // verifying
																		// client
																		// detail
																		// page.
		library.click("CLIENT.contactinfo");
		library.click("CLIENT.email");
		library.verifyPageContainsElement("CLIENT.gmail");
		library.click("CLIENT.gmail");
		library.wait(3);
		library.verifyPageContainsElement("Gmail.compose");
		library.navigateBack();
		library.navigateBack();
		library.wait(5);
		library.verifyPageContainsElement("CLIENT.backbutton"); // return
																		// from
																		// Gmail
																		// page
																		// and
																		// verify
																		// client
																		// page.

	}

	// Created by Gopal
	public void verifyPreviousAndNextButton(Map<String, String> data) {
		library.wait(3);
		// scenarios.goToClientListPage();
		String clientName = data.get("clientFullName");
		library.click("name->" + clientName);
		library.wait(3);
		String clientNameInClientDetailsPage = library.getTextFrom("CLIENTDETAILPAGE.clientname");
		System.out.println(clientNameInClientDetailsPage);
		if (library.verifyPageContainsElement("CLIENTDETAILPAGE.next")) {
			library.click("CLIENTDETAILPAGE.next");
			library.wait(4);
			library.click("CLIENTDETAILPAGE.prev");
		} else {
			library.click("CLIENTDETAILPAGE.prev");
			library.wait(4);
			library.click("CLIENTDETAILPAGE.next");
		}

		library.verifyPageContainsElement("name->" + clientName);
		library.navigateBack();

		boolean isMatched = clientNameInClientDetailsPage.equals(clientName);
		Assert.assertTrue(isMatched);
	}

	// Created by Gopal
	public void verifyContactDetailsPage(Map<String, String> data) {
		String clientName = data.get("clientFullName");
		library.click("name->" + clientName);
		library.wait(3);
		library.click("CLIENTDETAILPAGE.contactinfo");
		library.verifyPageNotContainsElement("CLIENTDETAILPAGE.urgencyimage");
		library.navigateBack();
	}

	public void verifyImmediatelyLeaveSteps() {

		library.wait(5);
		navigateToImmediatelyLeavePage();
		library.wait(3);
		library.verifyPageContainsElement("ILEAVE.returnearlybutton");
		library.click("HOMEPAGE.hamburger");
		library.wait(3);
		library.verifyPageContainsElement("MENU.returnnowbutton");

		library.click("MENU.returnnowbutton");
		library.wait(3);
		library.click("HOMEPAGE.hamburger");
		// library.click("MENU.goonleavebutton");
		// library.click("");
		library.verifyPageContainsElement("MENU.immediatelybutton");

	}

	public void verifyImmediatelyLeaveWithFutureLeaveSteps(Map<String, String> data) {

		boolean isReturnNow = library.verifyPageContainsElement("ILEAVE.returnearlybutton");
		if (!isReturnNow) {
			openMenu();
			navigateToImmediatelyLeavePage();
		}

		setFutureDate(data);
		library.verifyPageContainsElement("FLEAVE.createbutton"); // verifying
																		// whether
																		// future
																		// leave
																		// added
																		// by
																		// veryfying
																		// create
																		// new
																		// button
		library.verifyPageContainsElement("ILEAVE.returnearlybutton"); // verifying
																				// after
																				// adding
																				// future
																				// leave
																				// Return
																				// early
																				// option
																				// remains
																				// or
																				// not
		library.click("FLEAVE.cancelleavebutton");
		library.click("FLEAVE.deleteleavebutton");
		library.wait(3);

	}

	// Crerated By Puneet
	public void enterSecondFutureLeaveAndExpectErrorMessage(Map<String, String> data) {

		library.wait(5);
		openMenu();
		library.click("MENU.goonleavebutton");
		library.click("MENU.futureleavebutton");
		library.wait(5);

		String startDate = data.get("fromDate");
		String endDate = data.get("toDate");

		if (library.verifyPageContainsElement("FLEAVE.createbutton"))
			library.click("FLEAVE.createbutton");

		chooseStartAndEndLeaveDates(startDate, endDate);
		library.click("FLEAVE.schedulebutton");

		boolean alertMessage = library.verifyPageContainsElement("FLEAVE.leaveoverlapmessage");
		boolean isScheduleButtonPresent = library.verifyPageContainsElement("FLEAVE.schedulebutton");
		if (alertMessage) {

			library.click("ALERT.okbutton");
			library.click("SCHEDULELEAVE.cancel");
		} else if (isScheduleButtonPresent) {

			library.verifyPageContainsElement("FLEAVE.schedulebutton");
			library.click("SCHEDULELEAVE.cancel");// Verification for failure in
													// leave creation.
		} else {
			library.verifyPageContainsElement("FLEAVE.createbutton"); // Verification
																			// for
																			// successfully
																			// Leave
																			// creation
		}
	}

	public void verifyLastActivityWith(String activity) {

	}

	// Created by Priyanka on 05/10/2016 to fetch the Access Token and xuserID

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

	// Created by Priyanka on 05/10/2016 to fetch the Access Token and xuserID

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
	// Created by Priyanka on 05/19/2016 to swipe client list page

	protected void scrollClientList(int from, int to) {
		library.wait(1);
		Point start = getScreenCoordinatesForPercentInYAxis(from);
		Point end = getScreenCoordinatesForPercentInYAxis(to);
		library.swipeFromTo(start.x, start.y, end.x, end.y);

	}

	protected void setRequestHeader(Map<String, Object> data) {
		Map<String, Object> accessData = getAccessTokenId(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(data.get("ContentType"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);

	}

	// Created by Priyanka to Verify lead names match as per API
	// output.

	public void verifyLeadsName(Map<String, Object> data) {
		setRequestHeader(data);
		  String contactURL = String.valueOf(data.get("contactsUrl"));
		  String response = library.HTTPGet(contactURL);
		  int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		  scrollClientList(30, 70);
		  library.wait(5);
		  for (int i = 0; i < 4; i++) {
		   int j = i + 1;
		   library.wait(1);
		   String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
		     response);
		   String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
		     response);
		   String LeadName = firstName + " " + lastName;
		   Assert.assertTrue(library.verifyPageContainsElement("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+j+"]/android.widget.TextView[contains(@text,'" + LeadName +"')]"));
		   if (j != 1 && j % 4 == 0) {
		    scrollClientList(88, 30);    
		   }
		  }
		     for (int i=4;i<arrayCount;i++)
		     {
		   library.wait(1);
		   String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
		     response);
		   String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
		     response);
		   String LeadName = firstName + " " + lastName;
		      Assert.assertTrue(library.verifyPageContainsElement("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+i+"]/android.widget.TextView[contains(@text,'" + LeadName +"')]"));
		     }

	}

	// Created by Priyanka to Verify client stage matches with
	// opportunity stage from API response

	public void verifyStageName(Map<String, Object> data) {
		 library.wait(5);
		  setRequestHeader(data);
		  String contactURL = String.valueOf(data.get("contactsUrl"));
		  String response = library.HTTPGet(contactURL);
		  int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		  scrollClientList(30, 70);
		  library.wait(4);
		  for (int i = 0; i < 4; i++) {
		   int j = i + 1;
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
		   //library.wait(6);
		   Assert.assertTrue(library.verifyPageContainsElement("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+j+"]//android.widget.RelativeLayout[1]/android.widget.TextView[contains(@text,'"+ opportunityStage +"')]"));//Verify client stage matches with opportunity stage from API response
		   //Verify oppurtunity stage exists in the API response

		   if (j != 1 && j % 4 == 0) {
		    scrollClientList(88, 30);
		   }
		  }
		  
		  for (int i = 4; i < arrayCount; i++) {
		   int j = i + 1;
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
		   library.wait(2);
		   Assert.assertTrue(library.verifyPageContainsElement("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+ i +"]//android.widget.RelativeLayout[1]/android.widget.TextView[contains(@text,'"+ opportunityStage +"')]"));//Verify client stage matches with opportunity stage from API response
		   //Verify oppurtunity stage exists in the API response
		   if (j != 1 && j % 4 == 0) {
		    scrollClientList(88, 30);
		   }
		 }

	}

	// Created by Priyanka  to Verify email value from API response
	// matches with corresponding client email field in the Application

	public void verifyEmailFieldName(Map<String, Object> data) {
		library.wait(5);
		  setRequestHeader(data);
		  String contactURL = String.valueOf(data.get("contactsUrl"));
		  String response = library.HTTPGet(contactURL);
		  int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		  scrollClientList(30, 70);
		  scrollClientList(30, 70);
		  library.wait(8);
		  for (int i = 0; i < 4; i++) {
		   int j = i + 1;
		   library.wait(5);
		   String email = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.email", response);
		   System.out.println(email);
		   String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
		     response);
		   String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
		     response);
		   String LeadName = firstName + " " + lastName;
		   System.out.println(LeadName);
		   library.wait(4);
		   library.click("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+j+"]/android.widget.TextView[contains(@text,'" + LeadName +"')]");
		   library.wait(6);
		   library.waitForElement("CLIENTDETAILPAGE.contactinfo");
		   library.click("CLIENTDETAILPAGE.contactinfo");
		   library.wait(5);
		   Assert.assertTrue(library.verifyPageContainsElement("name->" + email));//Verify email value from API response matches with corresponding client email field
		   library.click("CLIENT.backbutton");
		   scrollClientList(30, 50);
		   library.wait(5);
		   if (j != 1 && j % 4 == 0) {
		    scrollClientList(88, 30);

		   }
		  }
	  
		  for(int i=4;i<arrayCount;i++)
		  {
		   library.wait(5);
		   String email = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.email", response);
		   System.out.println(email);
		   String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
		     response);
		   String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
		     response);
		   String LeadName = firstName + " " + lastName;
		   System.out.println(LeadName);
		   library.wait(4);
		   library.click("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+i+"]/android.widget.TextView[contains(@text,'" + LeadName +"')]");
		   library.wait(6);
		   library.waitForElement("CLIENTDETAILPAGE.contactinfo");
		   library.click("CLIENTDETAILPAGE.contactinfo");
		   library.wait(5);
		   Assert.assertTrue(library.verifyPageContainsElement("name->" + email));//Verify email value from API response matches with corresponding client email field
		   library.click("CLIENT.backbutton");
		   scrollClientList(30, 50);
		   library.wait(5);
		 }


	}

	// Created by Priyanka  to Verify primary phone value from API
	// response matches with corresponding client primary phone field in the
	// Application

	public void verifyPrimaryPhoneField(Map<String, Object> data) {
		 library.wait(5);
		  setRequestHeader(data);
		  String contactURL = String.valueOf(data.get("contactsUrl"));
		  String response = library.HTTPGet(contactURL);
		  int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		  scrollClientList(30, 70);
		  scrollClientList(30, 70);
		  library.wait(8);
		  for (int i = 0; i < 4; i++) {
		   int j = i + 1;
		   String PrimaryPhone = (String) library
		     .getValueFromJson("$.clientList[" + i + "].contactInfo.phone[0].phonenumber", response);
		   System.out.println(PrimaryPhone);
		   String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
		     response);
		   String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
		     response);
		   String LeadName = firstName + " " + lastName;
		   System.out.println(LeadName);
		   library.wait(4);
		   library.click("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+j+"]/android.widget.TextView[contains(@text,'" + LeadName +"')]");
		   library.wait(6);
		   library.waitForElement("CLIENTDETAILPAGE.contactinfo");
		   library.click("CLIENTDETAILPAGE.contactinfo");
		   library.wait(5);
		   Assert.assertTrue(library.verifyPageContainsElement("name->" + PrimaryPhone));//Verify primary phone value from API response matches with corresponding client primary phone field in the Application
		   library.click("CLIENT.backbutton");
		   scrollClientList(30, 50);
		   library.wait(5);
		   if (j != 1 && j % 4 == 0) {
		    scrollClientList(88, 30);
		   }
		  }

		  for (int i = 4; i < arrayCount; i++) {
			   String PrimaryPhone = (String) library
					     .getValueFromJson("$.clientList[" + i + "].contactInfo.phone[0].phonenumber", response);
					   System.out.println(PrimaryPhone);
					   String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					     response);
					   String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					     response);
					   String LeadName = firstName + " " + lastName;
					   System.out.println(LeadName);
					   library.wait(4);
					   library.click("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+i+"]/android.widget.TextView[contains(@text,'" + LeadName +"')]");
					   library.wait(6);
					   library.waitForElement("CLIENTDETAILPAGE.contactinfo");
					   library.click("CLIENTDETAILPAGE.contactinfo");
					   library.wait(5);
					   Assert.assertTrue(library.verifyPageContainsElement("name->" + PrimaryPhone));//Verify primary phone value from API response matches with corresponding client primary phone field in the Application
					   library.click("CLIENT.backbutton");
					   scrollClientList(30, 50);
					   library.wait(5);
		  }}
		  
		  


	// Created by Priyanka  to Verify last visited date value from
	// API response matches with corresponding client last visited date field in
	// the Application

	public void verifyLastVisitedDate(Map<String, Object> data) {

		 setRequestHeader(data);
		  String contactURL = String.valueOf(data.get("contactsUrl"));
		  String response = library.HTTPGet(contactURL);
		  int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		  scrollClientList(30, 70);
		  scrollClientList(30, 70);
		  library.wait(8);
		  for (int i = 0; i < 4; i++) {
		   int j = i + 1;
		   String lastVisitedDateTime = (String) library
		     .getValueFromJson("$.clientList[" + i + "].contactInfo.lastVisitedDateTime", response);
		   System.out.println(lastVisitedDateTime);
		   if (lastVisitedDateTime == "null") {
		    String uilastVisitedDate = library.getTextFrom("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+j+"]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[2]");
		    Assert.assertTrue(uilastVisitedDate.contentEquals("-"), "Condition is False");//Verify last visited date value from API response matches with corresponding client last visited date field in the Application
		   }
		   if (lastVisitedDateTime != null) {
		    String lastVisitedDate = new String(lastVisitedDateTime.substring(0, 10));
		    System.out.println(lastVisitedDate);
		    Assert.assertTrue(library.verifyPageContainsElement("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+j+"]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[contains(@text,'" +lastVisitedDate+ "') ]"));//Verify last visited date value from API response matches with corresponding client last visited date field in the Application
		   }
		   if (j != 1 && j % 4 == 0) {
		    scrollClientList(88, 30);
		   }
		  }
		  for (int i = 4; i < arrayCount; i++) {
		   String lastVisitedDateTime = (String) library
		     .getValueFromJson("$.clientList[" + i + "].contactInfo.lastVisitedDateTime", response);
		   System.out.println(lastVisitedDateTime);
		   if (lastVisitedDateTime == "null") {
		    String uilastVisitedDate = library.getTextFrom("xpath->//android.widget.ListView[1]/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[2]");
		    Assert.assertTrue(uilastVisitedDate.contentEquals("-"), "Condition is False");//Verify last visited date value from API response matches with corresponding client last visited date field in the Application
		   }
		   if (lastVisitedDateTime != null) {
		    String lastVisitedDate = new String(lastVisitedDateTime.substring(0, 10));
		    System.out.println(lastVisitedDate);
		    Assert.assertTrue(library.verifyPageContainsElement("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[contains(@text,'" +lastVisitedDate+ "') ]"));//Verify last visited date value from API response matches with corresponding client last visited date field in the Application
		   }
		   }

	}

	// Created by Priyanka to Verify client list exists with
	// atleast 1 or more leads

	public void verifyClientListExistwithLeads(Map<String, Object> data) {
		setRequestHeader(data);
		String contactURL = String.valueOf(data.get("contactsUrl"));
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		Boolean Condition = arrayCount >= 1;
		Assert.assertTrue(Condition, "Condition is False");
	}

	// Created by Priyanka to verify select sort order
	public void verfyselectSortorder(Map<String, Object> data) {
		library.wait(5);
		Map<String, Object> orderData = (Map<String, Object>) data.get("orderData");
		Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
		library.click("id->com.movoto.agentfc:id/client_filter_holder");
		String order = String.valueOf(orderData.get("Order"));
		String sortOrder = "name->" + order;
		library.getTextFrom(sortOrder);
		library.click(sortOrder);
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
		scrollClientList(30, 70);
		scrollClientList(30, 70);
		for (int i = 0; i < arrayCount; i++) {
			int j = i + 1;
			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;
			System.out.println(LeadName);
			Assert.assertTrue(library.verifyPageContainsElement("name->" + LeadName));
			if (j != 1 && j % 4 == 0) {
				scrollClientList(88, 30);
			}
		}
	}

	// Created by Priyanka to Stage last updated date exists
	public void verifyStageLastUpdatedDate(Map<String, Object> data) {
		 library.wait(5);
		  setRequestHeader(data);
		  String contactURL = String.valueOf(data.get("contactsUrl"));
		  String response = library.HTTPGet(contactURL);
		  int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		  scrollClientList(30, 70);
		  scrollClientList(30, 70);
		  library.wait(8);
		  for (int i = 0; i < 4; i++) {
		   int j = i + 1;
		   String StageLastUpdatedDateTime = String.valueOf(library.getValueFromJson(
		     "$.clientList[" + i + "].contactInfo.lastActivityDateTime",
		     response));
		    String StageLastUpdatedDate = new String(StageLastUpdatedDateTime.substring(0, 10));
		    System.out.println(StageLastUpdatedDate);
		    Assert.assertTrue(library.verifyPageContainsElement("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+j+"]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.TextView[contains(@text,'" +StageLastUpdatedDate+ "') ]"));   
		   if (j != 1 && j % 4 == 0) {
		    scrollClientList(88, 30);
		   }
		  }
		  for (int i = 4; i < arrayCount; i++) {
		   String StageLastUpdatedDateTime = String.valueOf(library.getValueFromJson(
		     "$.clientList[" + i + "].contactInfo.lastActivityDateTime", response));
		    String StageLastUpdatedDate = new String(StageLastUpdatedDateTime.substring(0, 10));
		    System.out.println(StageLastUpdatedDate);
		    Assert.assertTrue(library.verifyPageContainsElement("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.TextView[contains(@text,'" +StageLastUpdatedDate+ "') ]"));
		   }

	}

	// Created by Priyanka to verify Client filter

	public void verifyClientFilter(Map<String, Object> data) {
		library.wait(5);
		setRequestHeader(data);
		String contactURL = String.valueOf(data.get("contactsUrl"));
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		for (int i = 0; i < arrayCount; i++) {
			library.click("FLEAVE.searchicon");
			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;
			System.out.println(LeadName);
			// library.wait(1);
			String Str = new String(LeadName.substring(0, 3));
			// library.wait(2);
			String target = "FLEAVE.textfieldofsearch";
			Assert.assertTrue(library.isElementEnabled(target, true));
			library.typeDataInto(Str, target);
			Assert.assertTrue(library.verifyPageContainsElement("name->" + LeadName));
			library.click("name->Cancel");
		}

	}

	// Gopal
	public Map<String, Object> getTokenAndId(Map<String, Object> data) {
		String contentType = (String) data.get("ContentType");
		String apiUrl = (String) data.get("AccessTokenURL");
		library.setContentType(contentType);
		Map<String, Object> pData = new HashMap<>();
		pData = (Map<String, Object>) data.get("NotificationData");
		String response = library.HTTPPost(apiUrl, pData);
		String token = (String) library.getValueFromJson("$.token", response);
		String userId = (String) library.getValueFromJson("$.id", response);
		// System.out.println(token);
		// System.out.println(userId);
		// System.out.println(response);
		pData.put("Token", token);
		pData.put("Id", userId);
		return pData;
	}

	// Gopal
	public void setTokenAndUserId(Map<String, Object> data) {
		Map<String, Object> rData = getTokenAndId(data);
		String token = (String) rData.get("Token");
		String userId = (String) rData.get("Id");
		String contentType = "application/json";
		String authorization = "Bearer " + token;
		library.setContentType(contentType);
		library.setRequestHeader("Authorization", authorization);
		library.setRequestHeader("x-userid", userId);

	}

	// Gopal
	public String getResponse(String urlApi) {
		// String clientLeadApi =(String)data.get("clientLeadApi");
		String clientLeadApiresponce = library.HTTPGet(urlApi);
		library.wait(5);
		return clientLeadApiresponce;
	}

	// Gopal
	public void goToClientDetailsPage(Map<String, Object> data) {
		searchAndSelectClient((String) data.get("clientName"));
		library.wait(3);
		library.click("CLIENTDETAILPAGE.contactinfo");
	}

	// Gopal
	public void verifyMailWithApiResponce(Map<String, Object> data) {
		setTokenAndUserId(data);
		String clientLeadApi = (String) data.get("clientLeadApi");
		System.out.println(clientLeadApi);
		library.wait(3);
		String mailId = library.getTextFrom("DETAILS.mail");
		System.out.println(mailId);
		String response = (String) getResponse(clientLeadApi);
		System.out.println(response);
		String mailIdOfApiResponse = (String) library.getValueFromJson("$.email", response);
		Assert.assertEquals(mailId, mailIdOfApiResponse);
	}

	// Gopal
	public void goToTransactionDetails() {
		library.click("TRANSACTION.transactionlink");
	}

	// Gopal
	public void navigateToNotes() {
		library.wait(5);
		library.click("URGENCY.notes");
		library.wait(2);
	}

	// Gopal Buyer :
	public void verifyOpportunitiesWithApiResponse(Map<String, Object> data) {
		String opportunityType = library.getTextFrom("TRANSACTION.opportunityType");
		String subString = opportunityType.split(":")[0].trim();
		subString=subString+"er";
		setTokenAndUserId(data);
		String response = getResponse((String) data.get("clientLeadApi"));
		System.out.println(response);
		String opportunityTypeOfApiResponse = (String) library
				.getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityType", response);
		Assert.assertEquals(subString, opportunityTypeOfApiResponse);
	}

	// created by Puneet
	// Puneet date: - 10/May/2016
	public String getAccessTokenIdp(Map<String, Object> data) {

		Map<String, String> map = new HashMap<>();

		String contentType = String.valueOf(data.get("ContentType"));
		String accessTokenURL = String.valueOf(data.get("AccessTokenURL"));
		library.setContentType(contentType);

		Map<String, Object> pData = new HashMap<>();

		pData = (Map<String, Object>) data.get("LoginData");
		String response = library.HTTPPost(accessTokenURL, pData);
		// library.HTTPPost(URL, pData);
		System.out.println(response);

		return response;

	}

	// Puneet date: - 10/May/2016
	public String getLeadDetails(Map<String, Object> data) {

		String accessData = (String) data.get("accessData");
		String token = (String) library.getValueFromJson("$.token", accessData);
		String id = (String) library.getValueFromJson("$.id", accessData);

		String authorization = "Bearer " + token;
		String contentType = String.valueOf(data.get("ContentType"));
		String leadDeatilsUrl = String.valueOf(data.get("LeadDetailsUrl"));

		library.setContentType(contentType);
		library.setRequestHeader("Authorization", authorization);
		library.setRequestHeader("x-userid", id);

		String response = library.HTTPGet(leadDeatilsUrl);

		return response;

	}

	// Updated By Puneet dated : - 26-may-16
	// Puneet date: - 10/May/2016
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
		String urgencyCode = urgency.split("-")[1];
		urgencyCode = urgencyCode.trim();

		String locator = getLocatorForUrgencyCode(urgencyCode);
		// locator = locator+" "+"-"+" "+urgencyName;

		library.wait(3);
		library.verifyPageContainsElement(locator);
		library.wait(3);

	}

	// Updated By Puneet dated : - 23-may-16
	// Puneet date: - 8/May/2016
	protected String getLocatorForUrgencyCode(String urgencyCode) {

		return "xpath->//android.widget.TextView[contains(@text,'" + urgencyCode + "') and @index=1]";

	}

	// Updated By Puneet dated : - 23-may-16
	// Puneet date: - 8/May/2016
	public void verifyTransactionDetails(Map<String, Object> data) {
		Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
		String accessData = getAccessTokenIdp(apiData);

		apiData.put("accessData", accessData);
		String notesApiData = getNotesDetails(apiData);

		String notesData = (String) library.getValueFromJson("$.activities[0].activityType", notesApiData);

		String transactionType = notesData.split("-")[1];
		transactionType = transactionType.trim(); // Scheduled a Callback

		String locator = getLocatorForTransactionType(transactionType);
		// String text = library.getTextFrom(locator);
		// String lastStep = text.split(":")[0].trim(); // for Web
		// String lastStep =text.trim();

		// Assert.assertEquals(lastStep, transactionType);
		// Assert.assertTrue(library.verifyPageContainsElement(locator, true),
		// "Transaction was not updated in Notes.");
		library.verifyPageContainsElement(locator);
	}

	// Puneet date: - 9/May/2016
	public String getLocatorForTransactionType(String transactionType) {
		// UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[1]/UIAStaticText[1]

		// TODO Auto-generated method stub
		return "name->" + transactionType;
	}

	// Puneet date: - 9/May/2016
	public String getNotesDetails(Map<String, Object> data) {

		String accessData = (String) data.get("accessData");
		String token = (String) library.getValueFromJson("$.token", accessData);
		String id = (String) library.getValueFromJson("$.id", accessData);

		String authorization = "Bearer " + token;
		String contentType = String.valueOf(data.get("ContentType"));
		String notesApiUrl = String.valueOf(data.get("LeadDetailsUrl")); // changed
																			// on
																			// 30
																			// may
																			// 2016
																			// LeadDetailsUrl

		library.setContentType(contentType);
		library.setRequestHeader("Authorization", authorization);
		library.setRequestHeader("x-userid", id);

		String response = library.HTTPGet(notesApiUrl);

		return response;

	}

	// Puneet date: - 9/May/2016
	public void openNotes() {
		library.wait(8);
		library.click("CLIENTDETAIL.notesbutton");
		library.wait(10);

	}

	// Updated By Puneet dated : - 23-may-16
	// Created By Puneet dated - 18-may-2016
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
			// library.click("HOMEPAGE.searchbutton");

			data.put("ClientName", clientName);

			searchAndSelectClient(clientName);
			waitWhileLoading();
			updateToStage(data);
			waitWhileLoading();
		}

	}

	// Updated By Puneet dated : - 26-may-16
	// Created By Puneet dated - 17-may-2016
	public void verifyContractCancelStageTwo(Map<String, Object> data) {

		String clientName = String.valueOf(data.get("clientName"));
		String postStage = String.valueOf(data.get("postStage"));

		closeNotesField();
		navigateToClientListPage();
		waitWhileLoading();
		// verifyTransactionStageForClientWith(clientName, postStage);

	}

	public void chooseReturnToReason(String returnReason) {

	}

	// Updated By Puneet dated : - 23-may-16
	// Created By Puneet dated - 18-may-2016
	public void updateUrgencyStageOne(Map<String, String> data) {

		String urgencyCode = data.get("urgencyCode");
		String urgencyName = data.get("urgencyName");
		String clientName = data.get("ClientName");
		String searchName = clientName.substring(0, 3);
		// openClientDetailPage(clientName);
		searchAndSelectClient(clientName);

		library.click("URGENCY.image"); // click on urgency
		String updateLocator = "name->" + urgencyName;
		library.click(updateLocator); // click on radio button
		library.wait(3);
		library.isElementEnabled("URGENCY.apply", true); // verify Apply button
		library.click("URGENCY.apply");// click on apply button
		library.waitForElement("URGENCY.notes");
		library.wait(6);
		library.click("URGENCY.notes"); // click on Notes
		library.wait(6);

	}

	// Created By Puneet dated - 26-may-2016
	public void updateUrgencyStageTwo(Map<String, String> data) {

		  String urgencyCode = data.get("urgencyCode");
		  String urgencyName = data.get("urgencyName");
		  String clientName = data.get("ClientName");

		  String urgencyCodeLocator = "name->" + urgencyCode;
		  library.setImplicitWaitTime(5);
		  library.verifyPageContainsElement(urgencyCodeLocator);

		  String noteUpdateLocator = getUrgencyNotesLocator(urgencyName);
		  library.verifyPageContainsElement(noteUpdateLocator);
		  library.setImplicitWaitTime(30);
		  // library.click("CLOSE.notes");
		  library.wait(3);
		  closeNotesField();
		  navigateToClientListPage();


		 }

	// Created by Puneet dated :- 23-may-2016
	public void getApiUrl(Map<String, Object> data) {
		Map<String, Object> loginData = (Map<String, Object>) data.get("LoginData");

		Map<String, Object> accessData = getAccessTokenId(data);
		String token = String.valueOf(accessData.get("Token"));
		String clientName = (String) data.get("clientName"); // Excel
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(data.get("ContentType"));
		String contactURL = String.valueOf(data.get("contactsUrl"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);
		String response = library.HTTPGet(contactURL);
		System.out.println(response);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);

		int index = -1;
		for (int i = 0; i <= arrayCount; i++) {

			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;

			if (LeadName.equals(clientName)) {

				System.out.println(LeadName);

				index = i;
				break;
			}
		}
		if (index != -1) {
			String baseUrl = "http://alpaca.san-mateo.movoto.net:4015/agentservice/agents";
			String urlEndPart = "activities?size=10&startIndex=0";
			String agentID = xuserid;
			String opportunityId = (String) library.getValueFromJson(
					"$.clientList[" + index + "].contactInfo.opportunitiesSummary.openOpportunities[0].opportunityId",
					response);
			System.out.println(opportunityId);
			String userId = (String) library.getValueFromJson("$.clientList[" + index + "].contactInfo.userId",
					response);
			System.out.println(userId);
			String apiUrl = baseUrl + "/" + agentID + "/" + "contacts" + "/" + userId + "/" + "opportunities" + "/"
					+ opportunityId + "/" + urlEndPart;
			System.out.println(apiUrl);

		}

	}

	// Akash
	public int goToLeavePageCountLeave() {
		library.waitForElement("FLEAVE.createbutton");
		library.wait(10);
		library.wait(10);
		navigateToFutureLeavePage();
		int leaveCount = library.getElementCount("FLEAVE.cancelleavebutton");
		// library.wait(20);
		// navigateToFutureLeavePage();
		return leaveCount;
	}

	public void verifyLeavesWithApiResponse(String response, int leaveCount) {

		for (int i = 0; i < leaveCount; i++) {
			int j = i + 1;
			String startDate = (String) library.getValueFromJson("$.Leaves[" + i + "].startDateTime", response);
			String endDate = (String) library.getValueFromJson("$.Leaves[" + i + "].endDateTime", response);

			String[] dateElement = startDate.split(" ");
			String fromDateFromApi = dateElement[0];

			String[] dateElementt = endDate.split(" ");
			String toDateFromApi = dateElementt[0];

			String[] SplitfromDateFromApi = fromDateFromApi.split("/");
			String a = SplitfromDateFromApi[1];
			int day = Integer.parseInt(a);
			if (day <= 9) {
				String fromDateFromApiAdd = 0 + a;
				String FinalFormDate = SplitfromDateFromApi[0] + "/" + fromDateFromApiAdd + "/"
						+ SplitfromDateFromApi[2];

				String[] SplittoDateFromApi = toDateFromApi.split("/");
				String aa = SplittoDateFromApi[1];
				String toDateFromApiAdd = 0 + aa;
				String FinaltoDate = SplittoDateFromApi[0] + "/" + toDateFromApiAdd + "/" + SplittoDateFromApi[2];

				String startDateui = library.getTextFrom("xpath->//android.widget.RelativeLayout[" + j
						+ "]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.TextView[2]");
				String endDateui = library.getTextFrom("xpath->//android.widget.RelativeLayout[" + j
						+ "]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[2]/android.widget.TextView[2]");

				String uiDate = startDateui + " - " + endDateui;

				String[] splitstart = uiDate.split(" ");
				String dateFromm = splitstart[1];
				String[] str = dateFromm.split("/");

				String fromDate = str[0] + "/" + str[1] + "/20" + str[2];
				String[] splitend = uiDate.split(" ");
				String dateToo = splitend[4];
				String[] str1 = dateToo.split("/");
				String toDate = str1[0] + "/" + str1[1] + "/20" + str1[2];
				Assert.assertTrue(FinalFormDate.equals(fromDate));
				Assert.assertTrue(FinaltoDate.equals(toDate));
			} else {
				String startDateui = library.getTextFrom("xpath->//android.widget.RelativeLayout[" + j
						+ "]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.TextView[2]");
				String endDateui = library.getTextFrom("xpath->//android.widget.RelativeLayout[" + j
						+ "]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[2]/android.widget.TextView[2]");

				String uiDate = startDateui + " - " + endDateui;

				String[] splitstart = uiDate.split(" ");
				String dateFromm = splitstart[1];
				String[] str = dateFromm.split("/");

				String fromDate = str[0] + "/" + str[1] + "/20" + str[2];
				String[] splitend = uiDate.split(" ");
				String dateToo = splitend[4];
				String[] str1 = dateToo.split("/");
				String toDate = str1[0] + "/" + str1[1] + "/20" + str1[2];
				Assert.assertTrue(fromDateFromApi.equals(fromDate));
				Assert.assertTrue(toDateFromApi.equals(toDate));
			}

		}

	}

	public void deleteLeaves(int leavePosition) {
		library.wait(5);
		library.click("LEAVEPAGE.deletebuttonimage");
		library.click("FLEAVE.deleteleavebutton");
		library.wait(2);

	}

	// Created By Puneet dated - 18-may-2016
	public void closeNotesField() {
		library.wait(3);
		library.click("NOTES.cancelbutton");

	}

	public void verifyNotesResponseWithApi(Map<String, Object> data) {
		String response = getResponse((String) data.get("notesApi"));
		String activityTypeOfApiResponse = (String) library.getValueFromJson("$.activities[0].activityType", response);
		System.out.println("\n" + activityTypeOfApiResponse + "\n");
		String[] ele = activityTypeOfApiResponse.split("-");
		String activityTypePath = "xpath->(.//android.widget.TextView[contains(@text,'" + ele[1].trim() + "')])[1]";
		// boolean ispresent =
		// library.verifyPageContainsElement(activityTypePath, true);
		Assert.assertTrue(library.verifyPageContainsElement(activityTypePath));
		library.click("NOTES.cancelbutton");
	}

	public void verifyPropertiesResponseWithApi(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String response = getResponse((String) data.get("propertiesApi"));
		System.out.println("\n" + response + "\n");
		String addressOne = (String) library.getValueFromJson("$.properties[0].address", response);
		addressOne = addressOne.trim();
		String addressTwo = (String) library.getValueFromJson("$.properties[0].address", response);
		addressTwo = addressTwo.trim();
		System.out.println(addressOne);
		System.out.println(addressTwo);
		String pathOfAddress = "xpath->(//android.widget.TextView[contains(@text,'" + addressTwo
				+ "') and @index=2])[1]";
		System.out.println(pathOfAddress);

		boolean isAddressMatched = library.verifyPageContainsElement(pathOfAddress);

		Assert.assertTrue(isAddressMatched);

	}

	public void verifySendEmail() {

	}

	public void navigateToFutureLeavePages() {
		// TODO Auto-generated method stub

	}

	public int goToLeavePageCountLeaves() {
		int count=0;
		count=library.getElementCount("id->com.movoto.agentfc:id/opt_area_holder");
		return count;
	}

	public void setFutureDateUsingCalander(Map<String, Object> data) {
		
		

	}

	// Puneet date: - 17/06/2016
	// This is API call, which creates new lead in MovotoFull sandbox.
	public void createLeadInSalesforce(Map<String, Object> data) {

		Map<String, Object> leadCreationData = (Map<String, Object>) data.get("leadCreationData");
		leadCreationData.get("JsonFileName");
		String LeadCreationUrl = (String) leadCreationData.get("LeadCreationUrl");
		String leadDataPath = (String) leadCreationData.get("LeadDataPath");
		String JsonFileName = (String) leadCreationData.get("JsonFileName");
		Map<String, Object> data1 = getLeadData();
		Map<String, Object> pData = new HashMap<>();
		pData = (Map<String, Object>) data1.get("JsonFile");

		library.setContentType("application/json");

		String response = library.HTTPPost(LeadCreationUrl, data1);
		System.out.println(response);

	}

	// Created By Puneet : - 20/06/2016
	public static void writeInExcelFile(Object value) {
		try {
			FileOutputStream fileOut = new FileOutputStream("data/SalesForceTest.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet worksheet = workbook.createSheet("EmailIdForSalesForce");

			// index from 0,0... cell A1 is cell(0,0)
			// HSSFRow row1 = worksheet.createRow((short) 0);
			// HSSFRow row1 = worksheet.createRow(0);
			XSSFRow row1 = worksheet.createRow(0); // creating Row

			XSSFCell cellA1 = row1.createCell(1);
			cellA1.setCellValue("LeadEmailID");

			XSSFRow row2 = worksheet.createRow(1);

			XSSFCell cellB1 = row2.createCell(1);

			cellB1.setCellValue((String) value);

			workbook.write(fileOut);

			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Created By Puneet: - 17/06/2016
	public Map<String, Object> getLeadData() {

		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader("data/notifications/LeadCreationInSalesforce.json"));

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

	// Created By Puneet: - 17/06/2016
	public static Map<String, Object> toMap(JSONObject object) throws JsonException {
		Map<String, Object> map = new HashMap<>();

		Iterator<String> keysItr = object.keySet().iterator();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (key.equalsIgnoreCase("Email")) {
				Random randomGenerator = new Random();
				for (int idx = 1; idx <= 1; ++idx) {
					int randomInt = randomGenerator.nextInt(1000000000);
					System.out.println(randomInt);
					value = "test" + randomInt + "@xyz.com";
					System.out.println(value);
					writeInExcelFile(value);

				}

				if (value instanceof JSONArray) {
					value = toList((JSONArray) value);
				}

				else if (value instanceof JSONObject) {
					value = toMap((JSONObject) value);
				}

			}
			map.put(key, value);
		}
		return map;
	}

	// Created By Puneet: - 17/06/2016
	public static List<Object> toList(JSONArray array) throws JsonException {
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	// created by Puneet :- 21-06-2016
	public void goToNotificationPage() {
		library.click("HOMEPAGE.hamburger");
		library.click("MENU.notificationpagelink");
		library.wait(5);
	}

	public void createLeadInSalesforceforIOS(Map<String, Object> data) {

		Map<String, Object> leadCreationData = (Map<String, Object>) data.get("leadCreationData");
		leadCreationData.get("JsonFileName");
		String LeadCreationUrl = (String) leadCreationData.get("LeadCreationUrl");
		String leadDataPath = (String) leadCreationData.get("LeadDataPath");
		String JsonFileName = (String) leadCreationData.get("JsonFileName");
		Map<String, Object> data1 = getLeadDataforIOS();
		Map<String, Object> pData = new HashMap<>();
		pData = (Map<String, Object>) data1.get("JsonFile");

		library.setContentType("application/json");

		String response = library.HTTPPost(LeadCreationUrl, data1);
		System.out.println(response);

	}

	// Created By Puneet: - 17/06/2016
	public Map<String, Object> getLeadDataforIOS() {

		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader("data/notifications/LeadCreationInSalesforceForAgent2.json"));

			JSONObject jsonObject = (JSONObject) obj;
			System.out.println(jsonObject);

			Map<String, Object> retMap = new HashMap<>();

			if (jsonObject != null) {
				retMap = toMapforIOS(jsonObject);
				return retMap;
			}

			System.out.println(retMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// Created By Puneet: - 17/06/2016
	public static Map<String, Object> toMapforIOS(JSONObject object) throws JsonException {
		Map<String, Object> map = new HashMap<>();

		Iterator<String> keysItr = object.keySet().iterator();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (key.equalsIgnoreCase("Email")) {
				Random randomGenerator = new Random();
				for (int idx = 1; idx <= 1; ++idx) {
					int randomInt = randomGenerator.nextInt(100000);
					System.out.println(randomInt);
					value = "test" + randomInt + "@xyz.com";
					System.out.println(value);
					writeInExcelFileForIOS(value);

				}

				if (value instanceof JSONArray) {
					value = toList((JSONArray) value);
				}

				else if (value instanceof JSONObject) {
					value = toMap((JSONObject) value);
				}

			}
			map.put(key, value);
		}
		return map;
	}

	public static void writeInExcelFileForIOS(Object value) {
		try {
			FileOutputStream fileOut = new FileOutputStream("data/SalesForceTestForAnotherAgent.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet worksheet = workbook.createSheet("EmailIdForSalesForce");

			// index from 0,0... cell A1 is cell(0,0)
			// HSSFRow row1 = worksheet.createRow((short) 0);
			// HSSFRow row1 = worksheet.createRow(0);
			XSSFRow row1 = worksheet.createRow(0); // creating Row

			XSSFCell cellA1 = row1.createCell(1);
			cellA1.setCellValue("LeadEmailID");

			XSSFRow row2 = worksheet.createRow(1);

			XSSFCell cellB1 = row2.createCell(1);

			cellB1.setCellValue((String) value);

			workbook.write(fileOut);

			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Created by Priyanka :- 06/23/2016
	public void verifyFC1notification() {
		library.wait(5);
		String fc1NotificationMessgeLine1 = "Update Now";
		// String fc1NotificationMessgeLine2 = "Please update Test Client.";
		String fc1NotificationMessgeLine1xpath = "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[3]/android.widget.TextView[1]";
		// String fc1NotificationMessgeLine2xpath =
		// "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[3]/android.widget.TextView[2]";
		String fc1NotificationMessgeLine1text = library.getTextFrom(fc1NotificationMessgeLine1xpath);
		// String fc1NotificationMessgeLine2text =
		// library.getTextFrom(fc1NotificationMessgeLine2xpath);
		Assert.assertEquals(fc1NotificationMessgeLine1, fc1NotificationMessgeLine1text);
		// Assert.assertEquals(fc1NotificationMessgeLine2,
		// fc1NotificationMessgeLine2text);
	}

	// Created by Priyanka :- 06/23/2016
	public void verifyFC2notification() {
		String fc2NotificationMessgeLine1 = "Update Now Reminder (45min)";
		// String fc2NotificationMessgeLine2 = "Please update Test Client.";
		String fc2NotificationMessgeLine1xpath = "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.TextView[1]";
		// String fc2NotificationMessgeLine2xpath =
		// "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.TextView[2]";
		String fc2NotificationMessgeLine1text = library.getTextFrom(fc2NotificationMessgeLine1xpath);
		// String fc2NotificationMessgeLine2text =
		// library.getTextFrom(fc2NotificationMessgeLine2xpath);
		Assert.assertEquals(fc2NotificationMessgeLine1, fc2NotificationMessgeLine1text);
		// Assert.assertEquals(fc2NotificationMessgeLine2,
		// fc2NotificationMessgeLine2text);
	}

	// Created by Priyanka :- 06/23/2016
	public void verifyFC3notification() {
		String fc3NotificationMessgeLine1 = "Update Now Reminder (1.5hr)";
		// String fc3NotificationMessgeLine2 = "LAST REMINDER : Update Test
		// Client.";
		String fc3NotificationMessgeLine1xpath = "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.TextView[1]";
		// String fc3NotificationMessgeLine2xpath =
		// "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.TextView[2]";
		String fc3NotificationMessgeLine1text = library.getTextFrom(fc3NotificationMessgeLine1xpath);
		// String fc3NotificationMessgeLine2text =
		// library.getTextFrom(fc3NotificationMessgeLine2xpath);
		Assert.assertEquals(fc3NotificationMessgeLine1, fc3NotificationMessgeLine1text);
		// Assert.assertEquals(fc3NotificationMessgeLine2,
		// fc3NotificationMessgeLine2text);
	}

	// Created by Priyanka :- 06/23/2016
	public void verifyN1notification() {
		library.wait(5);
		String N1NotificationMessgeLine1 = "Call Now";
		// String N1NotificationMessgeLine2 = "Please update Test Client.";
		String N1NotificationMessgeLine1xpath = "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[3]/android.widget.TextView[1]";
		// String N1NotificationMessgeLine2xpath =
		// "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[3]/android.widget.TextView[2]";
		String N1NotificationMessgeLine1text = library.getTextFrom(N1NotificationMessgeLine1xpath);
		// String N1NotificationMessgeLine2text =
		// library.getTextFrom(N1NotificationMessgeLine2xpath);
		Assert.assertEquals(N1NotificationMessgeLine1, N1NotificationMessgeLine1text);
		// Assert.assertEquals(N1NotificationMessgeLine2,
		// N1NotificationMessgeLine2text);
	}

	// Created by Priyanka :- 06/23/2016
	public void verifyN2notification() {
		String N2NotificationMessgeLine1 = "Call Now Reminder (45min)";
		// String N2NotificationMessgeLine2 = "Please update Test Client.";
		String N2NotificationMessgeLine1xpath = "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.TextView[1]";
		// String N2NotificationMessgeLine2xpath =
		// "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.TextView[2]";
		String N2NotificationMessgeLine1text = library.getTextFrom(N2NotificationMessgeLine1xpath);
		// String N2NotificationMessgeLine2text =
		// library.getTextFrom(N2NotificationMessgeLine2xpath);
		Assert.assertEquals(N2NotificationMessgeLine1, N2NotificationMessgeLine1text);
		// Assert.assertEquals(N2NotificationMessgeLine2,
		// N2NotificationMessgeLine2text);
	}

	// Created by Priyanka :- 06/23/2016
	public void verifyN3notification() {
		String N3NotificationMessgeLine1 = "Call Now Reminder (1.5hr)";
		// String N3NotificationMessgeLine2 = "LAST REMINDER : Update Test
		// Client.";
		String N3NotificationMessgeLine1xpath = "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.TextView[1]";
		// String N3NotificationMessgeLine2xpath =
		// "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.TextView[2]";
		String N3NotificationMessgeLine1text = library.getTextFrom(N3NotificationMessgeLine1xpath);
		// String N3NotificationMessgeLine2text =
		// library.getTextFrom(N3NotificationMessgeLine2xpath);
		Assert.assertEquals(N3NotificationMessgeLine1, N3NotificationMessgeLine1text);
		// Assert.assertEquals(N3NotificationMessgeLine2,
		// N3NotificationMessgeLine2text);
	}

	// Puneet date: - 28/06/2016
	// This is API call, which creates new lead in MovotoFull sandbox.
	public void createLeadInSalesforceforIOSNSeries(Map<String, Object> data) {

		Map<String, Object> leadCreationData = (Map<String, Object>) data.get("leadCreationData");
		leadCreationData.get("JsonFileName");
		String LeadCreationUrl = (String) leadCreationData.get("LeadCreationUrl");
		String leadDataPath = (String) leadCreationData.get("LeadDataPath");
		String JsonFileName = (String) leadCreationData.get("JsonFileName");
		Map<String, Object> data1 = getLeadDataForNSeriesNotificationForIOS();
		Map<String, Object> pData = new HashMap<>();
		pData = (Map<String, Object>) data1.get("JsonFile");

		library.setContentType("application/json");

		String response = library.HTTPPost(LeadCreationUrl, data1);
		System.out.println(response);

	}

	// Created By Puneet: - 17/06/2016
	public Map<String, Object> getLeadDataForNSeriesNotificationForIOS() {

		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader("data/notifications/LeadCreationInSalesforceForIOS.json"));

			JSONObject jsonObject = (JSONObject) obj;
			System.out.println(jsonObject);

			Map<String, Object> retMap = new HashMap<>();

			if (jsonObject != null) {
				retMap = toMapforNSeriesNotificationForIOS(jsonObject);
				return retMap;
			}

			System.out.println(retMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Map<String, Object> toMapforNSeriesNotificationForIOS(JSONObject object) throws JsonException {
		Map<String, Object> map = new HashMap<>();

		Iterator<String> keysItr = object.keySet().iterator();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (key.equalsIgnoreCase("Email")) {
				Random randomGenerator = new Random();
				for (int idx = 1; idx <= 1; ++idx) {
					int randomInt = randomGenerator.nextInt(100000);
					System.out.println(randomInt);
					value = "test" + randomInt + "@xyz.com";
					System.out.println(value);
					writeInExcelFileForNSeriesNotificationForIOS(value);

				}

				if (value instanceof JSONArray) {
					value = toList((JSONArray) value);
				}

				else if (value instanceof JSONObject) {
					value = toMap((JSONObject) value);
				}

			}
			map.put(key, value);
		}
		return map;
	}

	public static void writeInExcelFileForNSeriesNotificationForIOS(Object value) {
		try {
			FileOutputStream fileOut = new FileOutputStream("data/SalesForceTestForIOSNSeries.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet worksheet = workbook.createSheet("EmailIdForSalesForce");

			// index from 0,0... cell A1 is cell(0,0)
			// HSSFRow row1 = worksheet.createRow((short) 0);
			// HSSFRow row1 = worksheet.createRow(0);
			XSSFRow row1 = worksheet.createRow(0); // creating Row

			XSSFCell cellA1 = row1.createCell(1);
			cellA1.setCellValue("LeadEmailID");

			XSSFRow row2 = worksheet.createRow(1);

			XSSFCell cellB1 = row2.createCell(1);

			cellB1.setCellValue((String) value);

			workbook.write(fileOut);

			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Puneet date: - 28/06/2016
	// This is API call, which creates new lead in MovotoFull sandbox.
	public void createLeadInSalesforceForAndroid(Map<String, Object> data) {

		Map<String, Object> leadCreationData = (Map<String, Object>) data.get("leadCreationData");
		leadCreationData.get("JsonFileName");
		String LeadCreationUrl = (String) leadCreationData.get("LeadCreationUrl");
		String leadDataPath = (String) leadCreationData.get("LeadDataPath");
		String JsonFileName = (String) leadCreationData.get("JsonFileName");
		Map<String, Object> data1 = getLeadDataForNSeriesNotification();
		Map<String, Object> pData = new HashMap<>();
		pData = (Map<String, Object>) data1.get("JsonFile");

		library.setContentType("application/json");

		String response = library.HTTPPost(LeadCreationUrl, data1);
		System.out.println(response);

	}

	// Created By Puneet: - 17/06/2016
	public Map<String, Object> getLeadDataForNSeriesNotification() {

		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader("data/notifications/LeadCreationInSalesforceForAndroid.json"));

			JSONObject jsonObject = (JSONObject) obj;
			System.out.println(jsonObject);

			Map<String, Object> retMap = new HashMap<>();

			if (jsonObject != null) {
				retMap = toMapforNSeriesNotification(jsonObject);
				return retMap;
			}

			System.out.println(retMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Map<String, Object> toMapforNSeriesNotification(JSONObject object) throws JsonException {
		Map<String, Object> map = new HashMap<>();

		Iterator<String> keysItr = object.keySet().iterator();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (key.equalsIgnoreCase("Email")) {
				Random randomGenerator = new Random();
				for (int idx = 1; idx <= 1; ++idx) {
					int randomInt = randomGenerator.nextInt(100000000);
					System.out.println(randomInt);
					value = "test" + randomInt + "@xyz.com";
					System.out.println(value);
					writeInExcelFileForNSeriesNotification(value);

				}

				if (value instanceof JSONArray) {
					value = toList((JSONArray) value);
				}

				else if (value instanceof JSONObject) {
					value = toMap((JSONObject) value);
				}

			}
			map.put(key, value);
		}
		return map;
	}

	public static void writeInExcelFileForNSeriesNotification(Object value) {
		try {
			FileOutputStream fileOut = new FileOutputStream("data/SalesForceTestForAndroidNSeries.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet worksheet = workbook.createSheet("EmailIdForSalesForce");

			// index from 0,0... cell A1 is cell(0,0)
			// HSSFRow row1 = worksheet.createRow((short) 0);
			// HSSFRow row1 = worksheet.createRow(0);
			XSSFRow row1 = worksheet.createRow(0); // creating Row

			XSSFCell cellA1 = row1.createCell(1);
			cellA1.setCellValue("LeadEmailID");

			XSSFRow row2 = worksheet.createRow(1);

			XSSFCell cellB1 = row2.createCell(1);

			cellB1.setCellValue((String) value);

			workbook.write(fileOut);

			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void verifySendEmail(Map<String, Object> data) {
		// TODO Auto-generated method stub

	}

	public void LoginToDownloadLatestAppIos(String name, String pass) {
		library.typeDataInto(name, "LOGIN.namefield");
		library.typeDataInto(pass, "LOGIN.passfield");
		library.wait(5);
		library.click("xpath->.//div[@class='pull-left']/input[@class='btn btn-ha-primary']");
	}

	public void sucess() {
		Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE"));
		library.wait(5);
	}

	public void MoveApp() {
		InputStream inStream = null;
		OutputStream outStream = null;

		try {

			File afile = new File("/Users/automationng/Downloads/Agent.ipa");
			File bfile = new File("/Users/automationng/.jenkins/workspace/automationScripts/app/Agent_AWS.ipa");

			inStream = new FileInputStream(afile);
			outStream = new FileOutputStream(bfile);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {

				outStream.write(buffer, 0, length);

			}

			inStream.close();
			outStream.close();

			// delete the original file
			afile.delete();

			System.out.println("File is copied successfully!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void WeedmapLogin() {
		library.wait(3);
		library.click("id->android:id/button2");
		library.wait(10);
		library.click("xpath->//android.widget.ImageButton");
		library.wait(5);
		library.click("xpath->.//android.widget.TextView[contains(@text,'Sign in or Sign Up')]");
		library.wait(3);
		library.typeDataInto("harish", "id->com.weedmaps.app.android:id/et_login_username");
		library.wait(3);
		library.typeDataInto("harishlimba", "id->com.weedmaps.app.android:id/et_login_password");
		library.wait(3);
		library.navigateBack();
		library.wait(3);
		library.click("id->com.weedmaps.app.android:id/btn_login_submit");
		library.wait(5);
		library.verifyPageContainsElement("id->com.weedmaps.app.android:id/menu_home_search");
		library.click("id->com.weedmaps.app.android:id/menu_home_search");
		library.wait(3);
		library.typeDataInto("colorado Springs", "id->com.weedmaps.app.android:id/search_src_text");
		library.wait(8);
		library.click("xpath->//android.widget.TextView[@text='Strawberry Fields Colorado Springs - Medical ']");
		library.wait(3);
		library.verifyPageContainsElement("id->com.weedmaps.app.android:id/tv_listing_details_title");
		library.verifyPageContainsElement("xpath->//android.widget.Button[@text='Menu']");
		library.wait(3);
		library.click("xpath->//android.widget.Button[@text='Menu']");
		library.wait(3);
		library.verifyPageContainsElement("xpath->//android.widget.TextView[@text='Buddha Tahoe']");
		library.wait(3);
		library.click("xpath->//android.widget.ImageButton");
		library.wait(3);
		library.click("xpath->//android.view.View[2]/android.widget.ImageButton[1]");
		library.wait(3);
		library.click("xpath->//android.widget.ImageButton");
		library.wait(3);
		library.verifyPageContainsElement("xpath->//android.widget.TextView[@text='Doctors']");
		library.click("xpath->//android.widget.TextView[@text='Medical Cannabis Doctor']");
		library.wait(3);
		library.verifyPageContainsElement("xpath->//android.widget.TextView[@text='Medical Cannabis']");
		library.verifyPageContainsElement(
				"xpath->//android.widget.TextView[@text='440 Fair Drive, Suite S-227, Costa Mesa CA 92626']");
		library.wait(3);

	}

	public void deleteApp() {
		InputStream inStream = null;
		OutputStream outStream = null;

		try {

			File bfile = new File("/Users/automationng/.jenkins/workspace/automationScripts/app/Agent_AWS.ipa");

			outStream = new FileOutputStream(bfile);

			outStream.close();

			// delete the original file
			bfile.delete();

			System.out.println("File is Deleted successfully!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void MoveAppForAndroid() {
		InputStream inStream = null;
		OutputStream outStream = null;

		try {

			File afile = new File("/Users/automationng/Downloads/salesforce_aws.apk");
			File bfile = new File("/Users/automationng/.jenkins/workspace/automationScripts/app/salesforce_AWS.apk");

			inStream = new FileInputStream(afile);
			outStream = new FileOutputStream(bfile);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {

				outStream.write(buffer, 0, length);

			}

			inStream.close();
			outStream.close();

			// delete the original file
			afile.delete();

			System.out.println("File is copied successfully!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteAppAndroid() {
		InputStream inStream = null;
		OutputStream outStream = null;

		try {

			File bfile = new File("/Users/automationng/.jenkins/workspace/automationScripts/app/salesforce_AWS.apk");

			outStream = new FileOutputStream(bfile);

			outStream.close();

			// delete the original file
			bfile.delete();

			System.out.println("File is Deleted successfully!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void verificationupdateUrgencyStageOne(Map<String, String> data) {

		  String urgencyCode = data.get("urgencyCode");
		  String urgencyName = data.get("urgencyName");
		  String clientName = data.get("ClientName");
		  String searchName = clientName.substring(0, 3);
		  // openClientDetailPage(clientName);
		  searchAndSelectClient(clientName);

		  String VerifyTransaction = "id->com.movoto.agentfc:id/notesUpdateHolder";
		  boolean got = library.verifyPageContainsElement(VerifyTransaction); // verify transaction tab.

		  String Verify = "Talked";
		  String uiTalked = "xpath->.//android.widget.TextView[contains(@text,'Talked')]";
		  //String getTalked = library.getTextFrom(uiTalked);
		  String getTalked = getPrestage(); // get stage from UI.
		  System.out.println(getTalked);
		  Assert.assertEquals(getTalked, Verify);//verify Lead is in Talked stage

		  library.click("URGENCY.image"); // click on urgency
		  String updateLocator = "name->" + urgencyName;
		  library.click(updateLocator); // click on radio button
		  library.wait(3);
		  library.isElementEnabled("URGENCY.apply", true); // verify Apply button
		  library.click("URGENCY.apply");// click on apply button
		  library.wait(5);
		  library.verifyPageContainsElement("URGENCY.spotno");//To verify the number on the urgency spot
		  library.waitForElement("URGENCY.notes");
		  library.wait(6);
		  library.click("URGENCY.notes"); // click on Notes
		  library.wait(6);

		 }

	public void loginVerification(Map<String, String> data) {
		openMenu();
		String agentName = data.get("Username");
		String xpathForAgentName = "id->com.movoto.agentfc:id/agent_name_holder";
		String getAgentNamefromXpath = library.getTextFrom(xpathForAgentName);
		String[] SplitAgentName = agentName.split("@");
		String Name = SplitAgentName[0];
		String xpathValue = getAgentNamefromXpath.replaceAll("\\s+", "");
		System.out.println(xpathValue);
		String loginValue = Name.replaceAll("\\s+", "");
		System.out.println(loginValue);
		boolean isMatched = xpathValue.equalsIgnoreCase(loginValue);//Verify agent is logged in successfully via checking the agent name on the menu is the same as the logged in agent's full name. 
		library.wait(2);
		Assert.assertTrue(isMatched);
		closeMenu();

	}

	public void verifyUrgencyForTalkedStage(Map<String, Object> data) {

		  Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
		  Map<String, String> urgencyData = (Map<String, String>) data.get("urgencyData");
		  String urgencyName = urgencyData.get("urgencyName");
		  String accessData = getAccessTokenIdp(apiData);
		  String urgencyCode = urgencyData.get("urgencyCode");

		  apiData.put("accessData", accessData);
		  String leadData = getLeadDetails(apiData);

		  String urgency = (String) library.getValueFromJson("$.activities[0].urgencyStatus", leadData);
		  String[] splitone = urgency.split(" ");
		  String getOne = splitone[0];
		  String webUiLocatorOne = "CLIENTDETAILPAGE.urgencyimage";
		  webUiLocatorOne = library.getTextFrom(webUiLocatorOne);
		  Assert.assertEquals(getOne, webUiLocatorOne);//Verify urgency code is updated to 1 in the urgency spot

		  String last = "Updated Urgency Status: " + urgency;
		  String webUiLocator = "id->com.movoto.agentfc:id/note_holder";
		  webUiLocator = library.getTextFrom(webUiLocator);
		  Assert.assertEquals(webUiLocator, last);//validating Note is added as "Updated Urgency Status: 1 - Ready Now"

		  String notesTimeXpath = "id->com.movoto.agentfc:id/date_holder";
		  String getTime = library.getTextFrom(notesTimeXpath);
		  String FinalValue = null;
		        // converting time into UTC fro IST and validating the same.
		  try {
		   DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		   formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		   Date date = formatterIST.parse(getTime);

		   DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		   formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		   String[] SplitTime = formatterUTC.format(date).split(" ");
		   String[] Split = SplitTime[1].split(":");
		   FinalValue =SplitTime[0]+" "+Split[0]+":"+Split[1]+" "+SplitTime[2];
		   

		   System.out.println(FinalValue);

		   String ApiTime = (String) library.getValueFromJson("$.activities[0].startDateTime", leadData);
		   String[] SplitApiTime = ApiTime.split(" ");
		   String[] gotApiTime = SplitApiTime[1].split(":");
		   String finalTime =SplitApiTime[0]+" "+gotApiTime[0]+":"+gotApiTime[1]+" "+SplitApiTime[2];
		   System.out.println(finalTime);
		   Assert.assertEquals(finalTime, FinalValue);//Verify the note time and date is the same as the time of the urgency updated time

		  } catch (Exception e) {
		   e.printStackTrace();
		  }


		 }

	// Created by Gopal
	public void goToTransactionDetails(Map<String, Object> data) {
		searchAndSelectClient((String) data.get("clientName2"));
		library.click("TRANSACTION.transactionlink");
	}

	public void verifyActivityListSages() {
		library.wait(6);
		library.click("TRANSACTION.updatebutton");//Update button is enabled on the client detail page
		library.wait(5);
		
		// Verify following activities are enabled for updates:Talked, Emailed, Left Voicemail, Schedule a Callback, Schedule a Meeting, Met in Person, Made an Offer, Add a Note, Return to Movoto
		Assert.assertTrue(library.verifyPageContainsElement("name->Talked"), "Element Not found");
		Assert.assertTrue(library.verifyPageContainsElement("name->Add a Note"), "Element Not found");
		Assert.assertTrue(library.verifyPageContainsElement("name->Left Voicemail"), "Element Not found");
		Assert.assertTrue(library.verifyPageContainsElement("name->Met in Person"), "Element Not found");
		Assert.assertTrue(library.verifyPageContainsElement("name->Emailed"), "Element Not found");
		Assert.assertTrue(library.verifyPageContainsElement("name->Scheduled a Meeting"), "Element Not found");
		Assert.assertTrue(library.verifyPageContainsElement("name->Scheduled a Callback"), "Element Not found");
		Assert.assertTrue(library.verifyPageContainsElement("name->Return to Movoto"), "Element Not found");
		Assert.assertTrue(library.verifyPageContainsElement("name->Talked"), "Element Not found");
		Assert.assertTrue(library.verifyPageContainsElement("name->Made an Offer"), "Element Not found");

	}



	// Created by puneet
	public void updateToScheduledACallbackForMetStage(String stage, String client, Map<String, Object> data) {

		searchAndSelectClient(client);
		String metStageLocator = library.getTextFrom("UPDATEPAGE.metstagelocator");
		Assert.assertEquals("Met", metStageLocator);//The stage is still Met
		library.wait(3);
		library.click("TRANSACTION.updatebutton");
		chooseTransactionStage(stage);
		library.wait(3);
		Assert.assertTrue(library.verifyPageContainsElement("UPDATE.applybutton"));
		library.click("UPDATE.applybutton");
		library.wait(2);
		Assert.assertTrue(library.verifyPageContainsElement("SC.setreminderbutton"));// Schedule a Reminder page is opened, Schedule a Reminder is displayed on the page
		Assert.assertTrue(library.verifyPageContainsElement("SC.schedulereminderpage"));
		String getClientName = library.getTextFrom("SC.clientname");
		System.out.println(getClientName);

		Assert.assertEquals(client, getClientName);//Client Name is correct with the updated client

		String timeInHourFromUI = library.getTextFrom("UPDATEPAGE.timeinhour");
		String dateFromUI = library.getTextFrom("UPDATEPAGE.date");
		System.out.println(dateFromUI);
		System.out.println(timeInHourFromUI);

		String[] time = timeInHourFromUI.split(":");
		String hourTime = time[0];
		System.out.println(hourTime);

		String minTimeWithAMPM = time[1];
		String[] minTimeArray = minTimeWithAMPM.split(" ");
		String minTime = minTimeArray[0];
		System.out.println(minTime);

		int intTimeInHourFromUI = Integer.parseInt(hourTime);
		int intTimeInMinFromUI = Integer.parseInt(minTime);
		// int intTimeInHourFromUI = Integer.parseInt(timeInHourFromUI);

		System.out.println(metStageLocator);
		System.out.println(timeInHourFromUI);

		// System.out.println(dateFromUI);

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String localSystemTime = sdf.format(cal.getTime());
		System.out.println("local time" + localSystemTime);

		int localSystemMin = cal.get(Calendar.MINUTE);
		int localSystemhour = cal.get(Calendar.HOUR);
		int addoneHourToUI = localSystemhour + 1;
		String addoneHourToUIString = String.valueOf(addoneHourToUI);
		System.out.print("Add one hour " + addoneHourToUIString);

		System.out.println("Printing Local System Time.");
		System.out.println(localSystemMin);
		System.out.println(localSystemhour);

		System.out.println(localSystemTime);

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		System.out.println(dateFormat.format(date));

		String localSystemDate = dateFormat.format(date);
		System.out.println(localSystemDate);
		Assert.assertEquals(dateFromUI, localSystemDate);//the default Date & Time is 10 min after the current time

		if (localSystemMin < 50) {

			int afterAddingTenMin = localSystemMin + 10;
			boolean isCorrect = (intTimeInMinFromUI >= afterAddingTenMin);
			Assert.assertTrue(isCorrect);
			if (intTimeInMinFromUI >= afterAddingTenMin) {
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

			if (intTimeInMinFromUI >= localSystemAfterAddingMin) {

				System.out.println("UI time is greater than or equal to 10 min");
				System.out.print("Web UI showing correct time.");

			}

		}
		library.typeDataInto("Agent Automation", "NOTES.notesfield");
		library.wait(2);
		library.click("SC.setreminderbutton");
		library.waitForElement("TRANSACTION.properties");
		Assert.assertEquals("Met", metStageLocator); // Post update operation
														// check If stage is
														// still in Met stage.

		library.wait(5);
	}

	// Created by puneet
	public void verifyNotesTime(Map<String, Object> data) {

		String xpathForDateAndTimeFromNotesField = library.getTextFrom("NOTES.updatedtime");
		// String Mettime = xpathForDateAndTimeFromNotesField.split(" ")[1];
		String FinalValue = null;

		try {
			DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
			formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			Date date = formatterIST.parse(xpathForDateAndTimeFromNotesField);

			DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
			String[] SplitTime = formatterUTC.format(date).split(" ");
			   String[] Split = SplitTime[1].split(":");
			   FinalValue =SplitTime[0]+" "+Split[0]+":"+Split[1]+" "+SplitTime[2];
			   

			   System.out.println(FinalValue);

			Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
			String accessData = getAccessTokenIdp(apiData);

			apiData.put("accessData", accessData);
			String notesApiData = getNotesDetails(apiData);

			String notesData = (String) library.getValueFromJson("$.activities[0].activityType", notesApiData);
			String ApiTime = (String) library.getValueFromJson("$.activities[0].activityCreatedDateTime", notesApiData);
			String[] SplitApiTime = ApiTime.split(" ");
			   String[] gotApiTime = SplitApiTime[1].split(":");
			   String finalTime =SplitApiTime[0]+" "+gotApiTime[0]+":"+gotApiTime[1]+" "+SplitApiTime[2];
			   System.out.println(finalTime);
			String transactionType = notesData.split("-")[1];
			transactionType = transactionType.trim(); // Scheduled a Callback

			String locator = getLocatorForTransactionType(transactionType);

			library.verifyPageContainsElement(locator);
			Assert.assertEquals(finalTime, FinalValue);//verify Notes is displayed as followings:Scheduled a Callback <activity updated time and date stamp>Callback Time: <scheduled date and time>
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getSystemDate() {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String localSystemTime = sdf.format(cal.getTime());
		System.out.println("local time" + localSystemTime);
		return localSystemTime;

	}

	public String getSysDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		String returndate = (dateFormat.format(date));
		return returndate;

	}

	public String getPrestage() {
		String prestageText = library.getTextFrom("UPDATEPAGE.metstagelocator");
		return prestageText;

	}

	public void openProperties() {
		library.click("TRANSACTION.properties");
		library.wait(5);
	}


	
	
	

	public int getTotalMinutesOfTime() {
		Date date = new Date();

		String dateString = date.toString();

		String time = dateString.split(" ")[3];
		String[] timeSplit = time.split(":");
		int hrs = Integer.parseInt(timeSplit[0]);
		int minutes = Integer.parseInt(timeSplit[1]);
		int totalMinutes = (hrs * 60) + minutes;
		return totalMinutes;
	}

	



	// Created by Priyanka dated :- 21/07/2016
	public String getApiUrlAWS(Map<String, Object> data) {
		Map<String, Object> accessData = getAccessTokenId(data);
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


	public Map<String, Object> getAccessTokenIdFirst(Map<String, Object> data) {
		return data;
	}

	public Map<String, Object> getAccessTokenIdSecond(Map<String, Object> data) {
		return data;
	}

	// Created by Puneet dated :- 22/07/2016
	public String getApiUrlForMadeAnOffer(Map<String, Object> data) {

		Map<String, Object> getData = (Map<String, Object>) data.get("apiData");
		Map<String, Object> accessData = getAccessTokenId(getData);
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

	public Map<String, Object> getAccessTokenIdForMadeAnOffer(Map<String, Object> data) {

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

	public Map<String, Object> getAccessTokenIdforMadeAnOffer(Map<String, Object> data) {
		// Map<String, Object> apiData = (Map<String, Object>)
		// data.get("apiData");

		String contentType = String.valueOf(data.get("ContentType"));
		String accessTokenURL = String.valueOf(data.get("AccessTokenURL"));
		library.setContentType(contentType);

		Map<String, Object> apidata = new HashMap<>();

		Map<String, Object> apidata1 = (Map<String, Object>) data.get("apiData");
		Map<String, Object> apidata2 = (Map<String, Object>) apidata1.get("LoginData");
		// apidata = (Map<String, Object>) data.get("LoginData");
		String response = library.HTTPPost(accessTokenURL, apidata2);
		// library.HTTPPost(URL, pData);
		System.out.println(response);
		Object token = library.getValueFromJson("$.token", response);
		Object id = library.getValueFromJson("$.id", response);
		apidata.put("Token", token);
		apidata.put("Id", id);

		return apidata;

	}

	protected void setRequestHeaderforMadeAnOffer(Map<String, Object> data) {
		Map<String, Object> accessData = getAccessTokenIdforMadeAnOffer(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(data.get("ContentType"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);

	}


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

	public void verifyNotesDataforContractCancel(Map<String, Object> data) {

	}

	public void verifyPropertyDetailsForContractCancel() {
		Assert.assertTrue(library.verifyPageNotContainsElement("CONTRACTCANCEL.escrowinfo"), "Escrow Info section is deleted");
		library.click("NOTES.cancelbutton");
	}


	public String getSysDates() {
		DateFormat dateFormat = new SimpleDateFormat("MM/d/yyyy");
		Date date = new Date();
		String returndate = (dateFormat.format(date));
		return returndate;

	}
	
	// Created by Puneet dated : - 28/07/2016
	 public String getMapSearchApiResultForCity(Map<String, Object> data){
	  
	  //Get Data from MAp for API headers and City Name
	  String contentType = (String)data.get("ContentType");
	  //System.out.println(contentType);
	  String LoginDataPath = (String)data.get("LoginDataPath");
	  String acceptEncoding = (String)data.get("acceptEncoding");
	  String MapSearchApi = (String)data.get("MapSearchApi");
	  String Host = (String)data.get("Host");
	  String Connection =(String) data.get("Connection");
	  String UserAgent = (String)data.get("UserAgent");
	  String XMdatakey = (String)data.get("XMdatakey");
	  String ContentLength = (String)data.get("ContentLength");
	  String CityName = (String)data.get("CityName");
	  
	  // Setting up headers for Map search API
	  library.setContentType(contentType);
	  library.setRequestHeader("X-MData-Key", XMdatakey);
	  library.setRequestHeader("Host", Host);
	  library.setRequestHeader("Connection", Connection);
	  library.setRequestHeader("User-Agent", UserAgent);
	  library.setRequestHeader("ContentLength", ContentLength);
	  library.setRequestHeader("Accept-Encoding", acceptEncoding);
	  
	  
	  Map<String, Object> jsonData = (Map<String, Object>) data.get("JsonData");
	  //System.out.println(jsonData);
	  jsonData.put("input", CityName); // Set City Name for 
	  //System.out.println(jsonData);
	  String response = library.HTTPPost(MapSearchApi, jsonData); // hitting Map Search API
	  
	  //System.out.println(response);
	  return response;
	  
	 }
	 
	 public String getMapSearchApiResultsFromJson(JSONObject data){
		  
		  // Setting up headers for Map search API
		  library.setContentType((String)data.get("ContentType"));
		  library.setRequestHeader("X-MData-Key", (String)data.get("XMdataKey"));
		  library.setRequestHeader("Host", (String)data.get("Host"));
		  library.setRequestHeader("Connection", (String) data.get("Connection"));
		  library.setRequestHeader("User-Agent", (String)data.get("UserAgent"));
		  library.setRequestHeader("ContentLength", (String)data.get("ContentLength"));
		  library.setRequestHeader("Accept-Encoding", (String)data.get("AcceptEncoding"));
		  
		  
		  Map<String, Object> jsonData = (Map<String, Object>) data.get("httpRequest");
		  //System.out.println(jsonData);
		  jsonData.put("input", (String)data.get("CityName")); // Set City Name for 
		  //System.out.println(jsonData);
		  String response = library.HTTPPost((String)data.get("MapSearchApi"), jsonData); // hitting Map Search API
		  
		  //System.out.println(response);
		  return response;
		  
		 }
	 
	 public void searchByCityName(Map<String, Object> data){
		 
	  
	 }
	 
	 public String getCityName(Map<String, Object> data){
	  return null;
	 }

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
			String totalLeadCount=library.getTextFrom("CLIENTLIST.ClientnoMsg");
			System.out.println(totalLeadCount);
			String[] splittotalLeadCount=totalLeadCount.split(" ");
			String UitotalLeadCount=splittotalLeadCount[3];
			int finalUiValue=Integer.parseInt(UitotalLeadCount);
			System.out.println(finalUiValue);
			Assert.assertEquals(finalUiValue, arrayCount);//verify lead count
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

		
	}

	public void clientListSortByFirstName(Map<String, Object> data) {
		library.wait(5);
		  Map<String, Object> orderData = (Map<String, Object>) data.get("orderData");
		  Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
		  // String dropdown = "xpath->//button[@ng-model='selectedSort']";
		  library.click("CLIENTLIST.newestreferred");
		  library.wait(3);
		  library.click("CLIENTLIST.sortorder");
		  library.wait(3);
		  library.click("CLIENTLIST.firstname");
		  library.wait(5);
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
		  scrollClientList(30, 70);
		  scrollClientList(30, 70);
		  for (int i = 0; i < 4; i++) {
		   int j = i + 1;
		   String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
		     response);
		   String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
		     response);
		   String LeadName = firstName + " " + lastName;
		   System.out.println(LeadName);
		   Assert.assertTrue(library.verifyPageContainsElement("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+j+"]/android.widget.TextView[contains(@text,'" + LeadName +"')]"));//Verify the lead order in the UI is same as the API response 
		   if (j != 1 && j % 4 == 0) {
		    scrollClientList(88, 30);
		   }
		  }
		  for (int i = 4; i < arrayCount; i++) {
		   int j = i + 1;
		   String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
		     response);
		   String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
		     response);
		   String LeadName = firstName + " " + lastName;
		   System.out.println(LeadName);
		   Assert.assertTrue(library.verifyPageContainsElement("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+i+"]/android.widget.TextView[contains(@text,'" + LeadName +"')]"));//Verify the lead order in the UI is same as the API response 
		   if (j != 1 && j % 4 == 0) {
		    scrollClientList(88, 30);
		   }
		  }

		
	}

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
		  scrollClientList(30, 70);
		  scrollClientList(30, 70);
		  library.wait(3);
		  library.click("CLIENTLIST.selectclient");
		  library.wait(5);
		  library.verifyPageContainsElement("TRANSACTION.transactionlink");
		  library.wait(3);
		  library.click("CLIENTLIST.contactinfo");
		  library.wait(5);
		  String Uiemail = library.getTextFrom("CLIENTLIST.emailfield");
		  System.out.println(Uiemail);
		  //Verify email value from API response matches with corresponding client email field
		  Assert.assertEquals(Uiemail, email);
		  library.click("CLIENTLIST.emailfield");
		  library.wait(5);
		  Assert.assertTrue(library.verifyPageContainsElement("CLIENTLIST.subjectfield"));//Verify Email Client page is opened after clicking Email icon  library.typeDataInto("Test Mail", "CLIENTLIST.subjectfield");
		  library.wait(5);
		  library.typeDataInto("Test Automation", "CLIENTLIST.subjectfield");
		  library.wait(5);
		  library.navigateBack();
		  library.wait(5);
		  library.click("CLIENTLIST.selectpreview");
		  library.wait(15);
		  library.click("CLIENTLIST.selectsend");
		  library.wait(20);
		  library.click("CLIENTLIST.select.transaction");
		  library.wait(3);
		  library.click("CLIENTLIST.selectbutton");
		  library.wait(5);
//		  String ActivityTitle= library.getTextFrom("CLIENTLIST.activitytitle");
//		  Assert.assertEquals(ActivityTitle, "Email");
		  String EmailText=library.getTextFrom("CLIENTLIST.activitytitle");  
		  Assert.assertTrue(EmailText.contains("Thanks,"));
		  library.wait(5);

	}

	public void verifyClientFilterWithMet(Map<String, Object> data) {
		library.wait(5);
		  Map<String, Object> orderData = (Map<String, Object>) data.get("orderData");
		  Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
		  // String dropdown = "xpath->//button[@ng-model='selectedSort']";
		  library.click("CLIENTLIST.newestreferred");
		  library.wait(3);
		  library.click("CLIENTLIST.met");
		  library.wait(5);
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
		  System.out.println("Filtered Count is "+arrayCount);
		  String totalLeadCount = library.getTextFrom("CLIENTLIST.ClientnoMsg");
		  System.out.println(totalLeadCount);
		  String[] splittotalLeadCount = totalLeadCount.split(" ");
		  String UitotalLeadCount = splittotalLeadCount[2];
		  int finalUiValue = Integer.parseInt(UitotalLeadCount);
		  System.out.println(finalUiValue);
		  Assert.assertEquals(finalUiValue, arrayCount);//Verify the filter result count on UI with the response from API
		  scrollClientList(30, 70);
		  scrollClientList(30, 70);
		  for (int i = 0; i < arrayCount && i <= 10; i++) {
		   int j = i + 1;
		   String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
		     response);
		   String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
		     response);
		   String LeadName = firstName + " " + lastName;
		   System.out.println(LeadName);
		   Assert.assertTrue(library.verifyPageContainsElement("xpath->//android.widget.ListView[1]/android.widget.LinearLayout["+j+"]/android.widget.TextView[contains(@text,'" + LeadName +"')]"));//Verify the client names from API response with corresponding filter result
		   if (j != 1 && j % 4 == 0) {
		    scrollClientList(88, 30);
		   }
		  }

		
	}
	
	//22 AUG 2014 by Puneet -------------------------------------
		public void searchByZipCode(Map<String, Object> data) {
			// TODO Auto-generated method stub
			
		}

		public String getMapSearchApiResultForZipCode(Map<String, Object> data) {
			// TODO Auto-generated method stub
			return null;
		}

		public void searchByNeighborhood(Map<String, Object> data) {
			// TODO Auto-generated method stub
			
		}

		public String getMapSearchApiResultForNeighborhood(Map<String, Object> data) {
			// TODO Auto-generated method stub
			return null;
		}

		public int getTotalCountFromUI() {
			return 0;
			// TODO Auto-generated method stub
			
		}

		public int getBottomTotalCountFromUI() {
			// TODO Auto-generated method stub
			return 0;
		}
		// Created by Puneet dated : - 28/07/2016
				 public String getMapSearchApiResultForFilterFunctionality(Map<String, Object> data){
				  
				  //Get Data from MAp for API headers and City Name
				  String contentType = (String)data.get("ContentType");
				  //System.out.println(contentType);
				  String LoginDataPath = (String)data.get("LoginDataPath");
				  String acceptEncoding = (String)data.get("acceptEncoding");
				  String MapSearchApi = (String)data.get("MapSearchApi");
				  String Host = (String)data.get("Host");
				  String Connection =(String) data.get("Connection");
				  String UserAgent = (String)data.get("UserAgent");
				  String XMdatakey = (String)data.get("XMdatakey");
				  String ContentLength = (String)data.get("ContentLength");
				  String MinSqft = (String)data.get("MinSqft");
				  
				  // Setting up headers for Map search API
				  library.setContentType(contentType);
				  library.setRequestHeader("X-MData-Key", XMdatakey);
				  library.setRequestHeader("Host", Host);
				  library.setRequestHeader("Connection", Connection);
				  library.setRequestHeader("User-Agent", UserAgent);
				  library.setRequestHeader("ContentLength", ContentLength);
				  library.setRequestHeader("Accept-Encoding", acceptEncoding);
				  
				  
				  Map<String, Object> jsonData = (Map<String, Object>) data.get("JsonData");
				  //System.out.println(jsonData);
				  jsonData.put("minSqft", MinSqft); // Set sqft 
				  //System.out.println(jsonData);
				  String response = library.HTTPPost(MapSearchApi, jsonData); // hitting Map Search API
				 
				  return response;
				  
				 }

				public void verifyDefaultValuesOnSearchPage(Map<String, Object> data) {
					// TODO Auto-generated method stub
					
				}

				public void selectMinValueForPriceAndVerify(Map<String, Object> data) {
					// TODO Auto-generated method stub
					
				}

				public void selectMaxPriceValueAndVerify(Map<String, Object> data) {
					// TODO Auto-generated method stub
					
				}

				public void selectMinValueForBedAndVerify(Map<String, Object> data) {
					// TODO Auto-generated method stub
					
				}

				public void selectMinValueForBathAndVerify(Map<String, Object> data) {
					// TODO Auto-generated method stub
					
				}

			/*	public void loginAndDeleteFavouriteProperty(Map<String, Object> data) {
					// TODO Auto-generated method stub
					
				}*/
				
				public void verifyHouseCardsDisplay(Map<String, Object> data) {
					// TODO Auto-generated method stub
					
				}

				public void selectPriceLowAndVerifiHouseCards(Map<String, Object> data) {
					// TODO Auto-generated method stub
					
				}

				public void selectSquareBigAndVerifyHouseCards(Map<String, Object> data) {
					// TODO Auto-generated method stub
					
				}

				public void verifyFilterFunctionality(Map<String, Object> data) {
					// TODO Auto-generated method stub
					
				}
				
				
				//-----------------
				
				//Modified by Anupama on 30/08/2016. This method will be used to search on city, neighborhood and zipcode.
				  public String getMapSearchApiResultForCity(Map<String, Object> data, String searchType){
				   
				   //Get Data from MAp for API headers and City Name
				   String contentType = (String)data.get("ContentType");
				   //System.out.println(contentType);
				   String LoginDataPath = (String)data.get("LoginDataPath");
				   String acceptEncoding = (String)data.get("acceptEncoding");
				   String MapSearchApi = (String)data.get("MapSearchApi");
				   String Host = (String)data.get("Host");
				   String Connection =(String) data.get("Connection");
				   String UserAgent = (String)data.get("UserAgent");
				   String XMdatakey = (String)data.get("XMdatakey");
				   String ContentLength = (String)data.get("ContentLength");
				   String CityName = (String)data.get("CityName");
				   String neighborhood=(String)data.get("Neighborhood");
				   String zipCode=(String)data.get("ZipCode");
				   
				   // Setting up headers for Map search API
				   library.setContentType(contentType);
				   library.setRequestHeader("X-MData-Key", XMdatakey);
				   library.setRequestHeader("Host", Host);
				   library.setRequestHeader("Connection", Connection);
				   library.setRequestHeader("User-Agent", UserAgent);
//				   library.setRequestHeader("ContentLength", ContentLength);
				   library.setRequestHeader("Accept-Encoding", acceptEncoding);
				   
				   
				   Map<String, Object> jsonData = (Map<String, Object>) data.get("JsonData");
				   System.out.println(jsonData);
				   if(searchType.equals("CITY"))
				   {
				    jsonData.put("input", CityName);
				    jsonData.put("searchType","CITY");
				   }
				   else if (searchType.equals("NEIGHBORHOOD"))
				   {
				  jsonData.put("input",neighborhood);
				  jsonData.put("searchType","NEIGHBORHOOD");
				  jsonData.put("hasPhoto","0");
				   }
				   else
				   {
				      jsonData.put("input",zipCode);
				      jsonData.put("searchType","ZIPCODE");
				   }
				   // Set City Name for 
//				   System.out.println("JSON Data ======="+jsonData);
				   String response = library.HTTPPost(MapSearchApi, jsonData); // hitting Map Search API
//				   FileWriter file;
//				   try {
//					   file = new FileWriter("92JsonData.json");
//					   file.write(response);
//				   } catch (IOException e) {
//						e.printStackTrace();
//					}
				   
//				   System.out.println(response);
				   return response;
				   
				  }

				

				public void removeAddsPopUp(){
					System.out.println("Chrome on Android");
					
				}
				
				public String getApiResultsForHotlead(JSONObject data) {
				     
				     String contentType = (String)data.get("ContentType");
				       //System.out.println(contentType);
				       String LoginDataPath = (String)data.get("LoginDataPath");
				       String acceptEncoding = (String)data.get("AcceptEncoding");
				       String MapSearchApi = (String)data.get("MapSearchApi");
				       String Host = (String)data.get("Host");
				       String Connection =(String) data.get("Connection");
				       String UserAgent = (String)data.get("UserAgent");
				       String XMdatakey = (String)data.get("XMdataKey");
				       String ContentLength = (String)data.get("ContentLength");
				       String City = (String)data.get("CityName");
				       
				       // Setting up headers for Map search API
				       library.setContentType(contentType);
				       library.setRequestHeader("X-MData-Key", XMdatakey);
				       library.setRequestHeader("Host", Host);
				       library.setRequestHeader("Connection", Connection);
				       library.setRequestHeader("User-Agent", UserAgent);
				       library.setRequestHeader("ContentLength", ContentLength);
				       library.setRequestHeader("Accept-Encoding", acceptEncoding);
				       
				       
				       Map<String, Object> jsonData = (Map<String, Object>) data.get("httpRequest");
				        
				       jsonData.put("input",City); // Set City Name for
				       
				       String response = library.HTTPPost(MapSearchApi, jsonData); // hitting Map Search API
				      
				       return response;
				       
				      }
				public void hotLeadFunctionality(String data,JSONObject jsondata) {
					// TODO Auto-generated method stub
					
				}

				public void goOnImmediateLeave(Map<String, Object> data) {
					
					library.wait(5);
					library.click("HOMEPAGE.hamburger");
					library.wait(2);
					library.click("MENU.goonleavebutton");
					library.wait(3);
					WebElement element=library.findElement("MENU.retrunnow");
					if(element !=null)
					{
						library.click("id->com.movoto.agentfc:id/menu_sub_holder");
						library.wait(10);
						library.click("HOMEPAGE.hamburger");
						library.wait(3);
					}
					library.click("MENU.immediatelyleave");
					library.wait(5);
					library.click("HOMEPAGE.hamburger");
					library.wait(3);
					Assert.assertTrue(library.verifyPageContainsElement("MENU.retrunnow"));//Verify the text changed to "Return Now"
					closeMenu();
					Assert.assertTrue(library.verifyPageContainsElement("MENU.retrunnow"));//Verify the blue button "Return Now" appears on top of the Client List page
					library.wait(2);
					setTokenAndUserId(data);
					String response = getResponse((String) data.get("ContactsUrl"));
					int uiValue=(int)library.getValueFromJson("$.Leaves[0].leaveType", response);
					Assert.assertEquals(uiValue, 1);//Verify leave status returned from API response is 1 (Immediately leave)
					library.wait(5);
					
					
					
					

					
				}

				public void returnNow(Map<String, Object> data) {
					
					library.wait(5);
					library.click("HOMEPAGE.hamburger");
					library.wait(2);
					library.click("MENU.goonleavebutton");
					library.wait(5);
					WebElement element=library.findElement("MENU.immediatelyleave");
					if(element !=null)
					{
						library.click("MENU.immediatelyleave");
						library.wait(10);
						library.click("HOMEPAGE.hamburger");
						library.wait(3);
					}
					library.click("id->com.movoto.agentfc:id/menu_sub_holder");
					library.wait(5);
					library.click("HOMEPAGE.hamburger");
					library.wait(4);
					Assert.assertTrue(library.verifyPageContainsElement("MENU.immediatelyleave"));//Verify the text change to "Immediately"
					closeMenu();
					Assert.assertTrue(library.verifyPageNotContainsElement("MENU.retrunnow"));//Verify the "Return Now" text disappear
					setTokenAndUserId(data);
					String response = getResponse((String) data.get("ContactsUrl"));
					System.out.println(response);
					String UIValue=(String) library.getValueFromJson("$.Leaves[0].leaveType", response);
					Assert.assertEquals(UIValue, null);
					
					
				}

				public void addFutureLeave(Map<String, Object> data) {
					library.wait(5);
					library.click("HOMEPAGE.hamburger");
					library.wait(2);
					library.click("MENU.goonleavebutton");
					library.wait(5);
					library.click("MENU.futureleavebutton");
					library.wait(5);
					setFutureDateUsingCalanderForFunctional(data);
					int leaveCount =goToLeavePageCountLeave();
					setTokenAndUserId(data);
					String response =getResponse((String) data.get("ContactsUrl"));
					verifyLeavesWithApiResponse(response, leaveCount);
					System.out.println(response);
						}

				public void deleteFutureLeave(Map<String, Object> data) {
					
					library.wait(5);
					library.click("HOMEPAGE.hamburger");
					library.wait(2);
					library.click("MENU.goonleavebutton");
					library.wait(5);
					library.click("MENU.futureleavebutton");
					library.wait(5);
					int getLeaveCountBeforeDeletion =goToLeavePageCountLeaves();
					if(getLeaveCountBeforeDeletion>0)
					{
						deleteLeaves(1);
						library.wait(2);
						int leaveCount = goToLeavePageCountLeaves();
						setTokenAndUserId(data);
						String response = getResponse((String) data.get("ContactsUrl"));
						verifyLeavesWithApiResponse(response, leaveCount);
					}
					else{
						
						Assert.assertTrue(false, "There is no leaves to delete");
					}
					
					
					
				}
                // Created by Priyanka :- 09/01/2016
                public void verifyforFC1notification(Map<String, Object> data) {
                    library.wait(5);
                    String fc1NotificationMessgeLine1 = "Update Now";
                    String fc1NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
                    String fc1NotificationMessgeLine1text = library.getTextFrom(fc1NotificationMessgeLine1xpath);
                    Assert.assertEquals(fc1NotificationMessgeLine1, fc1NotificationMessgeLine1text);// Verify the notification title is "Update Now"
                    String Name = (String) data.get("Name");
                    String fc1NotificationMessgeLine2 = "Please update "+Name+".";
                    String fc1NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
                    String fc1NotificationMessgeLine2text = library.getTextFrom(fc1NotificationMessgeLine2xpath);
                    Assert.assertEquals(fc1NotificationMessgeLine2, fc1NotificationMessgeLine2text);// Verify the notification body is "Please Update <client name>"
                    String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                   int NotificationCountnum = Integer.parseInt(NotificationCount);
                    library.click(fc1NotificationMessgeLine1xpath);
                    library.wait(5);
                    Assert.assertTrue(library.verifyPageContainsElement("NOTIFICATION.clientDetailUpdateBtn"));// Verify the agent is led to client detail page
                    String ClientName = library.getTextFrom("CLIENTDETAILPAGE.clientname");
                    Assert.assertEquals(ClientName, Name);// Verify client name on client detail page is the same as that in the FC1
                String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                    int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
                Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);// Verify the unread notification number is one less

                }
                
                // Created by Priyanka :- 09/01/2016
                public void verifyforFC2notification(Map<String, Object> data) {
                    library.wait(5);
                    String fc2NotificationMessgeLine1 = "Update Now Reminder (45min)";
                    String fc2NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
                    String fc2NotificationMessgeLine1text = library.getTextFrom(fc2NotificationMessgeLine1xpath);
                    Assert.assertEquals(fc2NotificationMessgeLine1, fc2NotificationMessgeLine1text);// Verify the notification title is "Update Now Reminde
                    String Name = (String) data.get("Name");
                    String fc2NotificationMessgeLine2 = "Please update "+Name+".";
                    String fc2NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
                    String fc2NotificationMessgeLine2text = library.getTextFrom(fc2NotificationMessgeLine2xpath);
                    Assert.assertEquals(fc2NotificationMessgeLine2, fc2NotificationMessgeLine2text);// Verify the Notification body is "Please update <client name>
                    String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                    int NotificationCountnum = Integer.parseInt(NotificationCount);
                    library.click(fc2NotificationMessgeLine1xpath);
                    library.wait(5);
                    library.verifyPageContainsElement("NOTIFICATION.clientDetailUpdateBtn");// Verify the agent is lead to client detail page
                    String ClientName = library.getTextFrom("CLIENTDETAILPAGE.clientname");
                    Assert.assertEquals(ClientName, Name);// Verify the client name is the same as that on FC2
                    String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                    int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
                    Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);// Verify THE UNREAD NOTIFICATION IS ONE LESS

                }
                
                public void verifyforFC3notification(Map<String, Object> data) {
                    library.wait(5);
                    String fc3NotificationMessgeLine1 = "Update Now Reminder (1.5hr)";
                    String fc3NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
                    String fc3NotificationMessgeLine1text = library.getTextFrom(fc3NotificationMessgeLine1xpath);
                    Assert.assertEquals(fc3NotificationMessgeLine1, fc3NotificationMessgeLine1text);// Verify the notification title is "Update Now Reminder (1.5hr)
                    String Name = (String) data.get("Name");
                    String fc3NotificationMessgeLine2 = "LAST REMINDER: Update "+Name+".";
                    String fc3NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
                    String fc3NotificationMessgeLine2text = library.getTextFrom(fc3NotificationMessgeLine2xpath);
                    Assert.assertEquals(fc3NotificationMessgeLine2, fc3NotificationMessgeLine2text);// Verify the notification body is "LAST REMINDER:Update<client name>.
                    String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                    int NotificationCountnum = Integer.parseInt(NotificationCount);
                    library.click(fc3NotificationMessgeLine1xpath);
                    library.wait(5);
                    library.verifyPageContainsElement("NOTIFICATION.clientDetailUpdateBtn");// Verify the agent is lead to client detail page
                    String ClientName = library.getTextFrom("CLIENTDETAILPAGE.clientname");
                    Assert.assertEquals(ClientName, Name);// Client name on client detail page is the same as that in the FC3
                    String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                    int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
                    Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);// Verify the unread notification number is one less
                }
                
                // Created by Priyanka :- 09/02/2016
                public void verifyforN1notification(Map<String, Object> data) {
                    library.wait(5);
                    String N1NotificationMessgeLine1 = "Call Now";
                    String N1NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
                    String N1NotificationMessgeLine1text = library.getTextFrom(N1NotificationMessgeLine1xpath);
                    Assert.assertEquals(N1NotificationMessgeLine1, N1NotificationMessgeLine1text);// Verify the notification title is "Call Now"
                    String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                    int NotificationCountnum = Integer.parseInt(NotificationCount);
                    String Name = (String) data.get("Name");                    
                    String Phone = (String) data.get("Phone");
                    String N1NotificationMessgeLine2 = "Call "+Name+" " +Phone+"";
                    String N1NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
                    String N1NotificationMessgeLine2text = library.getTextFrom(N1NotificationMessgeLine2xpath);
                    Assert.assertEquals(N1NotificationMessgeLine2, N1NotificationMessgeLine2text);// Verify the notification title is "Call Now"
                    library.click(N1NotificationMessgeLine1xpath);
                    library.wait(5);
                    library.verifyPageContainsElement("NOTIFICATION.clientupdatebuttonholder");
                    String ClientName = library.getTextFrom("SC.clientname");
                    Assert.assertEquals(ClientName, Name);// Verify the notification title is Call<cliet name><phone number>

                    library.wait(5);
                    library.click("NOTIFICATION.snoozeholder");
                    library.wait(5);
                    library.click("NOTIFICATION.button");
                    library.wait(5);
                    String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                    int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
                    Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);
                }

                
    
                // Created by Priyanka :- 09/02/2016
                public void verifyforN2notification(Map<String, Object> data) {
                    String Address = (String) data.get("Address");
                    String string = Address;
                    String[] parts = string.split(",");
                    String Address1 = parts[0]; // 034556
                    String Price = (String) data.get("Price");
                    String N2NotificationMessgeLine1 = "Call Now Reminder (45min)";
                    String N2NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
                    String N2NotificationMessgeLine1text = library.getTextFrom(N2NotificationMessgeLine1xpath);
                    Assert.assertEquals(N2NotificationMessgeLine1, N2NotificationMessgeLine1text);
                    String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                    int NotificationCountnum = Integer.parseInt(NotificationCount);

                    String Name = (String) data.get("Name");                    
                    String Phone = (String) data.get("Phone");
                    String N2NotificationMessgeLine2 = "Call "+Name+" " +Phone+"";
                    String N2NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
                    String N2NotificationMessgeLine2text = library.getTextFrom(N2NotificationMessgeLine2xpath);
                    Assert.assertEquals(N2NotificationMessgeLine2, N2NotificationMessgeLine2text);// Verify the notification title is "Call Now"
                    library.click(N2NotificationMessgeLine1xpath);
                    library.wait(5);
                    library.verifyPageContainsElement("NOTIFICATION.clientupdatebuttonholder");                    
                    String ClientName = library.getTextFrom("SC.clientname");
                    Assert.assertEquals(ClientName, Name);
                    String Addressui = library.getTextFrom("MOCONFIRM.address");
                    Assert.assertTrue(Addressui.contains(Address1));
                    String Priceui = library.getTextFrom("MOCONFIRM.price");
                    Assert.assertEquals(Priceui, Price);
                    library.wait(5);
                    library.click("NOTIFICATION.snoozeholder");
                    library.wait(5);
                    library.click("NOTIFICATION.button");
                    library.wait(5);
                    String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                    int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
                    Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);
                }
                
                // Created by Priyanka :- 09/02/2016
                public void verifyforN3notification(Map<String, Object> data) {
                    String Address = (String) data.get("Address");
                    String string = Address;
                    String[] parts = string.split(",");
                    String Address1 = parts[0]; // 034556
                    Address = Address.replace(",", "");
                    String Price = (String) data.get("Price");
                    String N3NotificationMessgeLine1 = "Call Now Reminder (1.5hr)";
                    String N3NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
                    String N3NotificationMessgeLine1text = library.getTextFrom(N3NotificationMessgeLine1xpath);
                    Assert.assertEquals(N3NotificationMessgeLine1, N3NotificationMessgeLine1text);
                    String Name = (String) data.get("Name");                    
                    String N3NotificationMessgeLine2 = "LAST REMINDER: Call "+Name+".";
                    String N3NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
                    String N3NotificationMessgeLine2text = library.getTextFrom(N3NotificationMessgeLine2xpath);
                    Assert.assertEquals(N3NotificationMessgeLine2, N3NotificationMessgeLine2text);// Verify the notification title is "Call Now"
                    String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                     int NotificationCountnum = Integer.parseInt(NotificationCount);
                    library.click(N3NotificationMessgeLine1xpath);
                    library.wait(5);
                    library.verifyPageContainsElement("NOTIFICATION.clientupdatebuttonholder");
                    String ClientName = library.getTextFrom("SC.clientname");
                    Assert.assertEquals(ClientName, Name);
                    String Addressui = library.getTextFrom("id->com.movoto.agentfc:id/address_holder");
                    Assert.assertTrue(Addressui.contains(Address1));
                    String Priceui = library.getTextFrom("MOCONFIRM.price");
                    Assert.assertEquals(Priceui, Price);
                    library.wait(5);
                    library.click("NOTIFICATION.snoozeholder");
                    library.wait(5);
                    library.click("NOTIFICATION.button");
                    library.wait(5);
                    String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                    int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
                    Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);
                }
                // Created by Priyanka :- 09/06/2016
                public void verifyforFC025notification(Map<String, Object> data) {
                    library.wait(5);
                    String fc025NotificationMessgeLine1 = "New Client Assigned";
                    String fc025NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
                    String fc025NotificationMessgeLine1text = library.getTextFrom(fc025NotificationMessgeLine1xpath);
                    Assert.assertEquals(fc025NotificationMessgeLine1, fc025NotificationMessgeLine1text);//Verify the notificaiton title is " New Client Assigned"
                    String City =(String) data.get("City");
                    String Price =(String) data.get("Price");
                    String Zip =(String) data.get("Zip");
                    String Name = (String) data.get("Name"); 
                    String fc025NotificationMessgeLine2 = ""+Price+" in "+City+", "+Zip+".";
                    String fc025NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
                    String fc025NotificationMessgeLine2text = library.getTextFrom(fc025NotificationMessgeLine2xpath);
                    Assert.assertEquals(fc025NotificationMessgeLine2, fc025NotificationMessgeLine2text);//Verify the notificaiton body is "<price> in <city>, <zip>"
                    String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                    int NotificationCountnum = Integer.parseInt(NotificationCount);
                    library.click(fc025NotificationMessgeLine1xpath);
                    library.wait(5);
                    library.verifyPageContainsElement("NOTIFICATION.clientDetailUpdateBtn");//Verify the agent is lead to client detail page
                    String ClientName = library.getTextFrom("CLIENTDETAILPAGE.clientname");
                    Assert.assertEquals(ClientName, Name);//Verify the client name is the same as that on SF
                    String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
                    int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
                    Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);//Verify the unread notification number is one less than the number before clicking FC0.25.

                }
                

				public void logOutForConsumerWeb(JSONObject data) {
					// TODO Auto-generated method stub
					
				}

				public void checkIfLoggedInAndLogOut() {
					// TODO Auto-generated method stub
					
				}

				public void verifySaveSearchFunctionality(JSONObject data) {
				     // TODO Auto-generated method stub
				     
				    }
				public void robotPoweredMoveMouseToWebElementCoordinates(WebElement element)  {
					// TODO Auto-generated method stub
					
				}
				
				public void robotPoweredClick()  {
					// TODO Auto-generated method stub
					
				}
				
				public void Reg_83_Mortgage()
				{
					
				}
				
				public void Reg84_MortgageCalculator()
				{
					
				}
				
				public boolean javascriptexecutorclick(String locator) {
				       boolean flag = false;
				       String value = null;
				       WebElement element;

				       JavascriptExecutor executor = (JavascriptExecutor) library.getDriver();
				       try {
				        if (locator.contains("xpath->")) {
				         value = locator.replaceAll("xpath->", "");
				         element = library.getDriver().findElement(By.xpath(value.trim()));
				         executor.executeScript("arguments[0].click();", element);
				         flag = true;
				        }
				        if (locator.contains("id->")) {
				         value = locator.replaceAll("id->", " ");
				         element = library.getDriver().findElement(By.id(value.trim()));
				         executor.executeScript("arguments[0].click();", element);
				         flag = true;
				        }
				        if (locator.contains("name->")) {
				         value = locator.replaceAll("name->", " ");
				         element = library.getDriver().findElement(By.name(value.trim()));
				         executor.executeScript("arguments[0].click();", element);
				         flag = true;
				        }
				        if (locator.contains("class->")) {
				         value = locator.replaceAll("class->", " ");
				         element = library.getDriver().findElement(By.className(value.trim()));
				         executor.executeScript("arguments[0].click();", element);
				         flag = true;
				        }
				        if (locator.contains("link->")) {
				         value = locator.replaceAll("link->", " ");
				         element = library.getDriver().findElement(By.linkText(value.trim()));
				         executor.executeScript("arguments[0].click();", element);
				         
				         flag = true;
				        }
				        return flag;
				       } catch (Exception e) {
				        System.out.println("Invalid locator" + e);
				        return flag;
				       }

				      }
				public void DeleteTrash(JSONObject data) {
				     // TODO Auto-generated method stub
				     
				    }
				
				public boolean checkPreconditionsForTc202() {
					return false;
									}	

				public boolean checkPrecondiotionsForTC201(Map<String, Object> data) {
					// TODO Auto-generated method stub
					return false;
				}

				public boolean checkPrecondiotionsForTC200(Map<String, Object> data) {
					// TODO Auto-generated method stub
					return false;
				}
				
				
				long diffrence=0;
				  public void createFC05Notification() throws ParseException, java.text.ParseException {

				  library.wait(10);
				   SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy HH:mm a");
				      String dateInString1 = library.getTextFrom("xpath->//th[text()='Hot Lead Submission Time']/../..//tr[2]/td[4]");
				      String dateInString2 = library.getTextFrom("xpath->//th[text()='Hot Lead Submission Time']/../..//tr[3]/td[4]");
				      Date date1 = formatter.parse(dateInString1);
				      Date date2 = formatter.parse(dateInString2);
				      System.out.println(date1);
				      System.out.println(date2);
				      long days1 = date1.getTime() / 60000/60/24;
				      long days2 = date2.getTime() / 60000/60/24;
				      System.out.println(days1);
				      System.out.println(days2); 
				      diffrence= days1-days2;
				      if((diffrence)>1)
				      {
				       Assert.assertEquals(library.getTextFrom("xpath->//th[text()='Notification Type']/../..//tr[2]/td[3]"), "FC0.5");
				      }
				      else{
				       System.out.println("N0.5 notification not created");
				      }
				  }

				  public void createFC05(Map<String, Object> data) {

					  Map<String, Object> leadCreationData = (Map<String, Object>) data.get("leadCreationData");
					  leadCreationData.get("JsonFileName");
					  String LeadCreationUrl = (String) leadCreationData.get("LeadCreationUrl");
					  String leadDataPath = (String) leadCreationData.get("LeadDataPath");
					  String JsonFileName = (String) leadCreationData.get("JsonFileName");
					//  Map<String, Object> data1 = getLeadDataforIOS();
					        Map<String, Object> pData = (Map<String, Object>) data.get("JsonFile");

					  library.setContentType("application/json");

					  String response = library.HTTPPost(LeadCreationUrl, pData);
					  System.out.println(response);

					 }
				  public void verifyforFC05notification() {
				   
				   if(diffrence>0)
				   {
				       library.wait(5);
				       String fc05NotificationMessgeLine1 = "New Client Assigned";
				       String fc05NotificationMessgeLine1xpath = "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.TextView[1]";
				       String fc05NotificationMessgeLine1text = library.getTextFrom(fc05NotificationMessgeLine1xpath);
				       Assert.assertEquals(fc05NotificationMessgeLine1, fc05NotificationMessgeLine1text);
				       
				       String fc05NotificationMessgeLine2 = "$500,000 in Mchenry, 60050.";
				       String fc05NotificationMessgeLine2xpath = "xpath->//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.TextView[2]";
				       String fc05NotificationMessgeLine2text = library.getTextFrom(fc05NotificationMessgeLine2xpath);
				       Assert.assertEquals(fc05NotificationMessgeLine2, fc05NotificationMessgeLine2text);
				       
				       String NotificationCount = library.getTextFrom("id->com.movoto.agentfc:id/actionbar_badge");
				             int NotificationCountnum = Integer.parseInt(NotificationCount);
				             library.click(fc05NotificationMessgeLine1xpath);
				             library.wait(5);
				             library.verifyPageContainsElement("id->com.movoto.agentfc:id/clientDetailUpdateBtn");
				             String ClientName = library.getTextFrom("id->com.movoto.agentfc:id/clientDetailClientName");
				             Assert.assertEquals(ClientName, "Test AgentiOS");
				             String ReducedNotificationCount = library.getTextFrom("id->com.movoto.agentfc:id/actionbar_badge");
				             int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
				             Assert.assertEquals(NotificationCountnum-1, ReducedNotificationCountnum);

				      }
				   }

				// Created by Priyanka dated :- 21/07/2016
				  public void updateLeadStageToTalked(Map<String, Object> data) {
					  library.click("xpath->//android.widget.LinearLayout[@index='1']/android.widget.TextView");
					   library.wait(5);
					   library.click("TRANSACTION.updatebutton");
					   library.wait(5);
					   chooseTransactionStage("Emailed");
					   library.wait(2);
					   library.click("UPDATE.applybutton");
					   library.wait(10);
					   Assert.assertTrue(library.isElementEnabled("NOTES.notesfield", true));// Add
					                     // Note
					                     // Page
					                     // is
					                     // opened
					   library.typeDataInto("test Emailed Update", "NOTES.notesfield");
					   library.click("NOTES.savebutton");
					   Assert.assertTrue(library.verifyPageContainsElement("name->Talked"));// Lead
					                      // stage
					                      // is
					                      // changed
					                      // to
					                      // "Talked"
					   library.wait(5);
					   // library.click("UPDATE.applybutton");
					   // library.wait(2);
					   library.click("CLIENTDETAIL.notesbutton");
					   library.wait(5);
					   String text1 = library.getTextFrom("SC.notes_title_holder");
					   library.wait(5);
					   Assert.assertEquals(text1, "Emailed");// Verify Notes is displayed as
					             // followings:Emailed
					   String contactURL = getApiUrlAWS(data);
					   setRequestHeader(data);
					   String response = library.HTTPGet(contactURL);
					   String xpathForDateAndTimeFromNotesField = library.getTextFrom("NOTES.updatedtime");
					   String FinalValue = null;
					   try {
					    DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
					    formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
					    Date date = formatterIST.parse(xpathForDateAndTimeFromNotesField);
					    DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
					    formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
					    String[] SplitTime = formatterUTC.format(date).split(" ");
						   String[] Split = SplitTime[1].split(":");
						   FinalValue =SplitTime[0]+" "+Split[0]+":"+Split[1]+" "+SplitTime[2];
						   

						   System.out.println(FinalValue);
					    String notesData = (String) library.getValueFromJson("$.activities[0].activityType", response);
					    String ApiTime = (String) library.getValueFromJson("$.activities[0].activityCreatedDateTime", response);
					    String[] SplitApiTime = ApiTime.split(" ");
						   String[] gotApiTime = SplitApiTime[1].split(":");
						   String finalTime =SplitApiTime[0]+" "+gotApiTime[0]+":"+gotApiTime[1]+" "+SplitApiTime[2];
						   System.out.println(finalTime);
					    String transactionType = notesData.split("-")[1];
					    transactionType = transactionType.trim(); // Scheduled a Callback
					    String locator = getLocatorForTransactionType(transactionType);
					    library.verifyPageContainsElement(locator);
					    Assert.assertEquals(finalTime, FinalValue);// Verify Notes is
					                 // displayed as
					                 // followings:<email
					                 // update time
					                 // stamp>
					   } catch (Exception e) {
					    e.printStackTrace();
					   }
					   String text3 = library.getTextFrom("id->com.movoto.agentfc:id/note_holder");
					   Assert.assertEquals(text3, "test Emailed Update");// Verify Notes is
					                // displayed as
					                // followings:test
					                // Emailed Update
				  }
		

				  public void verifyMeetingTimeAndNotes(Map<String, Object> data) {
					  // TODO Auto-generated method stub
					    searchAndSelectClient(data.get("clientName").toString());//Client Name is correct with the updated client
					    boolean isTalked = library.verifyPageContainsElement("OPPORTUNITY.oppotunityType");//Schedule a Meeting page is opened, Schedule a Meeting is displayed on the page

					    Assert.assertTrue(isTalked, "Stage is not talked...");
					    library.click("TRANSACTION.updatebutton");
					    library.wait(2);
					    library.click("UPDATE.updateTypeScheduleMEeting");
					    library.wait(1);
					    library.click("UPDATE.applybutton");
					    int totalMinutesOfSystemTime = getTotalMinutesOfTime();
					    library.wait(2);

					    String startTimeOfMeeting = library.getTextFrom("MEETINGTIME.startTime");
					    String startTime = startTimeOfMeeting.split(" ")[0];
					    int hoursOfStartTime = Integer.parseInt(startTime.split(":")[0]);
					    int minutesOfStartTime = Integer.parseInt(startTime.split(":")[1]);
					    int totalMinutesOfMeetingStartTime = (60 * hoursOfStartTime) + minutesOfStartTime;
					    if (startTimeOfMeeting.split(" ")[1].equals("PM") && hoursOfStartTime != 12)
					     totalMinutesOfMeetingStartTime = 720 + totalMinutesOfMeetingStartTime;

					    String endTimeOfMeeting = library.getTextFrom("MEETINGTIME.endTime");
					    String endTime = endTimeOfMeeting.split(" ")[0];
					    int hoursOfEndTime = Integer.parseInt(endTime.split(":")[0]);
					    int minutesOfEndTime = Integer.parseInt(endTime.split(":")[1]);
					    int totalMinutesOfMeetingEndTime = (60 * hoursOfEndTime) + minutesOfEndTime;
					    System.out.println(endTimeOfMeeting.split(" ")[1]);
					    if (endTimeOfMeeting.split(" ")[1].equals("PM") && hoursOfEndTime != 12)
					     totalMinutesOfMeetingEndTime = 720 + totalMinutesOfMeetingEndTime;

					    // boolean isStartTimeCorrect = (60 <= totalMinutesOfMeetingStartTime &&
					    // totalMinutesOfMeetingStartTime <= 65);
					    if (totalMinutesOfMeetingStartTime <= 65) {
					     Reporter.log("Day (Date) is changed", true);
					     boolean isStartTimeCorrect = (60 <= (1440 - totalMinutesOfSystemTime) + totalMinutesOfMeetingStartTime
					       && (1440 - totalMinutesOfSystemTime) + totalMinutesOfMeetingStartTime <= 65);
					     Assert.assertTrue(isStartTimeCorrect, "Meeting Start time is not 60 minutes later from the present time.");//the default Start Time is 1 hour after the current time

					     boolean isEndTimeCorrect = ((90 <= 1440 - totalMinutesOfSystemTime + totalMinutesOfMeetingEndTime)
					       && (95 >= 7200 - totalMinutesOfSystemTime + totalMinutesOfMeetingEndTime));
					     Assert.assertTrue(isEndTimeCorrect, "Meeting End time is not 90 minutes later from the present time.");//the default End Time is 1 and half an hour after the current time

					    } else {
					     boolean isStartTimeCorrect = ((60 <= (totalMinutesOfMeetingStartTime - totalMinutesOfSystemTime))
					       && (65 >= (totalMinutesOfMeetingStartTime - totalMinutesOfSystemTime)));
					     Assert.assertTrue(isStartTimeCorrect, "Meeting Start time is not 60 minutes later from the present time.");

					     boolean isEndTimeCorrect = ((90 <= (totalMinutesOfMeetingEndTime - totalMinutesOfSystemTime))
					       && (95 >= (totalMinutesOfMeetingEndTime - totalMinutesOfSystemTime)));
					     Assert.assertTrue(isEndTimeCorrect, "Meeting End time is not 90 minutes later from the present time.");

					    }

					    library.typeDataInto("Agent Automation", "NOTES.notesfield");
					    library.navigateBack();
					    library.wait(1);
					    library.click("NOTES.savebutton");
					    library.wait(7);
					    library.click("URGENCY.notes");
					    library.wait(5);
					    setTokenAndUserId(data);
					    String response = getResponse((String) data.get("notesApi"));
					    String ActivityType=library.getTextFrom("xpath->//android.widget.TextView[contains(@text,'Scheduled a Meeting')]");
					    String notesData = (String) library.getValueFromJson("$.activities[0].activityType", response).toString().split("-")[1].trim();
					    Assert.assertEquals(ActivityType, notesData);//Verify Activity type
					    String notesTimeXpath = "xpath->(//android.widget.LinearLayout[1 and @index=2]//android.widget.TextView)[3]";
						  String[] getTime = library.getTextFrom(notesTimeXpath).split("\n")[0].split(" ");
						  String splitGetTime=getTime[2]+" "+getTime[3]+" "+getTime[4];
						  
						  
						  String FinalValue = null;
						        // converting time into UTC fro IST and validating the same.
						  try {
						   DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
						   formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
						   Date date = formatterIST.parse(splitGetTime);

						   DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
						   formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
						   String[] SplitTime = formatterUTC.format(date).split(" ");
						   String[] Split = SplitTime[1].split(":");
						   FinalValue =SplitTime[0]+" "+Split[0]+":"+Split[1]+" "+SplitTime[2];
						   

						   System.out.println(FinalValue);

						   String ApiTime = (String) library.getValueFromJson("$.activities[0].startDateTime", response);
						   String[] SplitApiTime = ApiTime.split(" ");
						   String[] gotApiTime = SplitApiTime[1].split(":");
						   String finalTime =SplitApiTime[0]+" "+gotApiTime[0]+":"+gotApiTime[1]+" "+SplitApiTime[2];
						   System.out.println(finalTime);
						   Assert.assertEquals(finalTime, FinalValue);//Verify the note time and date is the same as the time of the urgency updated time
						   
						   String isStartTime = "xpath->(.//android.widget.TextView[contains(@text,'" + startTime + "')])[1]";// Verify start time is present
							Assert.assertTrue(library.verifyPageContainsElement(isStartTime));

							String isEndTime = "xpath->(.//android.widget.TextView[contains(@text,'" + endTime + "')])[1]";// Verify end time is present
							Assert.assertTrue(library.verifyPageContainsElement(isEndTime));
						  } catch (Exception e) {
						   e.printStackTrace();
						  }

					    library.click("NOTES.cancelbutton");
					 }
				
				  
				  public void updateToMakeANOffer(Map<String, String> MadeOfferData) {
					  String clientName = MadeOfferData.get("ClientName");

					  searchAndSelectClient(clientName);

					  String presatge = getPrestage();

					  library.click("TRANSACTION.updatebutton");// update
					  library.wait(5);
					  chooseTransactionStage("Made an Offer");
					  // library.typeDataInto("Made an Offer", "UPDATE.list");
					  library.click("UPDATE.applybutton");
					  handleEditTransactionAlert();
					  Assert.assertTrue(library.verifyPageContainsElement("MAKEOFFER.pageonepropertyaddress"));
					  Assert.assertTrue(library.verifyPageContainsElement("MAKEOFFER.pageoneclientone"));

					  enterAddress(MadeOfferData.get("Address"));
					  library.wait(5);
					  library.click("MAKEOFFER.continuebutton");// continue
					  library.wait(5);
					  Assert.assertTrue(library.verifyPageContainsElement("MAKEOFFER.price"), "Element not found.");
					  String systemDate = null, dateFromUI = null;
					  systemDate = getSysDate();
					  dateFromUI = library.getTextFrom("MAKEOFFER.datefield");
					  boolean isCorrectDate = (systemDate.equals(dateFromUI));
					  System.out.println(isCorrectDate);
					  Assert.assertTrue(isCorrectDate);
					  String dateStr = MadeOfferData.get("OfferDate");
					  // library.clear("MAKEOFFER.datefield");
					  // library.typeDataInto(dateStr, "MAKEOFFER.datefield");

					  library.clear("MAKEOFFER.price");
					  enterNumber(MadeOfferData.get("OfferPrice"), "MAKEOFFER.price");
					  library.click("MAKEOFFER.continuebutton");// continue
					  library.wait(5);
					  String UIaddress=library.getTextFrom("MOCONFIRM.address");
					  Assert.assertEquals(UIaddress, "1749 Lake Street San Mateo, CA 94403"); //Verify Address is displayed on the page
					  Assert.assertTrue(library.verifyPageContainsElement("MAKEOFFER.pageopenedconfirmoffer"),
					    "Page is not opened");
					  library.wait(3);
					  String uIAddress = library.getTextFrom("MAKEOFFER.addressfield");
					  System.out.println(uIAddress);
					  // Map<String, String> madeAnOfferData = new HashMap<>();

					  String xlSheetAddress = String.valueOf(MadeOfferData.get("Address"));
					  String[] ss=xlSheetAddress.split(",");
					     String finalcity=ss[0]+" "+ss[1]+","+" "+ss[2]+" "+ss[3];
					     System.out.println(finalcity);
					  Assert.assertEquals(finalcity, uIAddress);

					  String uiPrice = library.getTextFrom("MAKEOFFER.pricefiled");
					  String xlPrice = String.valueOf(MadeOfferData.get("OfferPrice"));
					  String uiPriceRemoveSpecialCharaters = uiPrice.replaceAll(",", "");
					  String finalUiPrice = uiPriceRemoveSpecialCharaters.replaceAll("\\$", "");
					  System.out.println(finalUiPrice);
					  Assert.assertEquals(finalUiPrice, xlPrice);
					  String dateFromUII = library.getTextFrom("MOCONFIRM.date");
					  Assert.assertEquals(dateFromUII, systemDate);
					  library.click("MAKEOFFER.submitOffer");
					  library.wait(3);
					  Assert.assertTrue(library.verifyPageContainsElement("UPDATEPAGE.metstagelocator"),
					    "Page is not in offered");
					 }
				  
				  public void verifyNotesData(Map<String, Object> data) {

					  String contactURL = getApiUrlForContractCancel(data);
					        setRequestHeaderforMadeAnOffer(data);
					          String response = library.HTTPGet(contactURL);
					          
					        String notesLineTwo = library.getTextFrom("URGENCY.noteholder");
					    Assert.assertEquals("Property Address - see properties section for more details", notesLineTwo);//Property Address - see properties section for more details
					  String notesData = (String) library.getValueFromJson("$.activities[0].activityType", response);

					  String notesTimeXpath = "NOTES.updatedtime";
					  String getTime = library.getTextFrom(notesTimeXpath);
					  String[] Mettime = getTime.split(" ");
					  String MettimeSplit = Mettime[0] + " " + Mettime[1] + " " + Mettime[2];

					  String FinalValue = null;
					  try {

					   DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
					   formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
					   Date date = formatterIST.parse(MettimeSplit);

					   DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
					   formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
					   String[] SplitTime = formatterUTC.format(date).split(" ");
					   String Split = SplitTime[1];
					   String[] SplitSec = Split.split(":");
					   FinalValue = SplitSec[0] + ":" + SplitSec[1];
					   System.out.println(FinalValue);

					   String ApiTime = (String) library.getValueFromJson("$.activities[0].startDateTime", response);
					   String[] SplitApiTime = ApiTime.split(" ");
					   String[] gotApiTime = SplitApiTime[1].split(":");
					   String finalTime =gotApiTime[0]+":"+gotApiTime[1];
					   System.out.println(finalTime);
					   Assert.assertEquals(finalTime, FinalValue);
					   library.wait(5);
					   Assert.assertTrue(library.verifyPageContainsElement("NOTES.propertyaddress"));
					   library.click("NOTES.cancelbutton");
					   library.wait(2);

					  } catch (Exception e) {
					   e.printStackTrace();
					  }
					  }
				  
				  public void verifyPropertyDetails(Map<String, String> MadeOfferData) 
				  {
					  try{
					       String UIAddress = library.getTextFrom("MAKEOFFER.addressfield");
					       System.out.println(UIAddress);
					       String xlSheetAddress = String.valueOf(MadeOfferData.get("Address"));
					       Map<String ,String > test=FileUtil.readFileAsMap("config//API_Testt.properties");
					       String s=xlSheetAddress; 
					       String[] ss=s.split(",");
					       String city=ss[2];
					       String addCity=(test.get(city));
					       String finalcity=ss[0]+" "+ss[1]+","+" "+addCity+" "+ss[3];
					       System.out.println(finalcity);
					       Assert.assertEquals(finalcity, UIAddress);

					       String systemDate = null, dateFromUI = null;
					       systemDate = getSysDate();
					       
					       String uiPrice = library.getTextFrom("MAKEOFFER.pricefiled").replace("$", "").replace(",", "").trim();
					       String xlPrice = String.valueOf(MadeOfferData.get("OfferPrice"));
					       Assert.assertEquals(uiPrice, xlPrice);//Offer price displayed on the page property section
					       String uiPriceRemoveSpecialCharaters = uiPrice.replaceAll(",", "");
					       String finalUiPrice = uiPriceRemoveSpecialCharaters.replaceAll("\\$", "");
					       System.out.println(finalUiPrice);
					       Assert.assertEquals(finalUiPrice, xlPrice);
					       dateFromUI = library.getTextFrom("URGENCY.DateHolder");
					       Assert.assertEquals(dateFromUI, systemDate);
					       library.click("NOTES.cancelbutton");
					       library.wait(2);
					      }catch(Exception e)
					      {
					       e.printStackTrace();
					      }
				  }
				  
				  public void updateToContractAcceptedStage1(Map<String, Object> data) {
					  library.wait(5);
					  library.waitForElement("CONTRACTACCEPT.selectclient");
					  library.click("CONTRACTACCEPT.selectclient");
					  library.wait(5);
					  library.click("TRANSACTION.updatebutton");
					  library.wait(5);
					  chooseTransactionStage("Contract Accepted");
					  library.waitForElement("UPDATE.applybutton");
					  library.click("UPDATE.applybutton");
					  library.wait(3);
					  Assert.assertTrue(library.verifyPageContainsElement("CONTRACTACCEPT.contractaccept"));
					  library.wait(2);
					  Assert.assertTrue(library.verifyPageContainsElement("CONTRACTACCEPT.clientname"));
					  library.wait(2);
					  Assert.assertTrue(library.verifyPageContainsElement("CONTRACTACCEPT.clentaddress"));
					  library.wait(2);
					  String Excepteddate = library.getTextFrom("CONTRACTACCEPT.Excepteddate");
					  System.out.println(Excepteddate);
					  DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
					  Calendar c = Calendar.getInstance();
					  c.setTime(new Date()); // Now use today date.
					  c.add(Calendar.DATE, 45); // Adding 45 days
					  String output = dateFormat.format(c.getTime());
					  Assert.assertEquals(Excepteddate, output);
					  library.click("MAKEOFFER.continuebutton");// continue
					  library.wait(3);
					  Assert.assertTrue(library.verifyPageContainsElement("CONTRACTACCEPT.confirmpageopen"));
					  library.wait(3);
					  Assert.assertTrue(library.verifyPageContainsElement("SC.clientnamefunctional"));
					  library.wait(3);
					  library.click("CONTRACTACCEPT.yesname");
					  library.wait(3);
					  Assert.assertTrue(library.verifyPageContainsElement("CONTRACTACCEPT.contractaccept"));
					  library.wait(3);
					  Assert.assertTrue(library.verifyPageContainsElement("id->com.movoto.agentfc:id/client_name_holder"));
					  library.wait(3);
					  Assert.assertTrue(library.verifyPageContainsElement("CONTRACTACCEPT.clentaddress"));
					  library.wait(3);
					  library.typeDataInto("650000", "CONTRACTACCEPT.pricefield");
					  library.wait(3);
					  library.typeDataInto("3", "CONTRACTACCEPT.commissionfiled");
					  library.wait(3);
					  library.waitForElement("MAKEOFFER.continuebutton");
					  library.click("MAKEOFFER.continuebutton");
					  library.wait(3);
					  library.isElementEnabled("CONTRACTACCEPT.submitinfo", true);
					  library.click("CONTRACTACCEPT.submitinfo");
					  library.wait(20);
					  library.click("CLIENTDETAIL.notesbutton");
					  library.wait(5);
					  String getTime = library.getTextFrom("NOTES.updatedtime");
					  String contactURL = getApiUrlAWS(data);
					  setRequestHeader(data);
					  String response = library.HTTPGet(contactURL);
					  System.out.println(getTime);
					  String FinalValue = null;
					  try {
					   DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
					   formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
					   Date date = formatterIST.parse(getTime);

					   DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
					   formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
					   String[] SplitTime = formatterUTC.format(date).split(" ");
					   String[] Split = SplitTime[1].split(":");
					   FinalValue =SplitTime[0]+" "+Split[0]+":"+Split[1]+" "+SplitTime[2];
					   System.out.println(FinalValue);

					   String ApiTime = (String) library.getValueFromJson("$.activities[0].startDateTime", response);
					   String[] SplitApiTime = ApiTime.split(" ");
					   String[] gotApiTime = SplitApiTime[1].split(":");
					   String finalTime =SplitApiTime[0]+" "+gotApiTime[0]+":"+gotApiTime[1]+" "+SplitApiTime[2];
					   System.out.println(finalTime);
					   Assert.assertEquals(finalTime, FinalValue);
					   library.wait(5);
					   Assert.assertTrue(library.verifyPageContainsElement("NOTES.propertyaddress"));
					   library.click("NOTES.cancelbutton");
					   library.wait(2);
					   library.click("TRANSACTION.properties");
					   library.wait(5);
					   Assert.assertTrue(library.verifyPageContainsElement("CONTRACTACCEPT.clentaddress"));
					   library.wait(2);
					   String dateFromUI = library.getTextFrom("CONTRACTACCEPT.madeofferdate");
					   System.out.println(dateFromUI);
					   Assert.assertTrue(library.verifyPageContainsElement("CONTRACTACCEPT.madeofferdate"));
					   library.wait(2);
					   Assert.assertTrue(library.verifyPageContainsElement("MAKEOFFER.pricefiled"));
					   library.wait(2);
					   scrollClientList(80, 30);
					   library.wait(5);
					   String contractAccept=library.getTextFrom("CONTRACTACCEPT.accepteddate");
					   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
					   LocalDateTime now = LocalDateTime.now();
					   String systemDate=(dtf.format(now)); 
					   Assert.assertEquals(contractAccept, systemDate);//Accepted
					   library.wait(5);
					   String uiExceptedDate=library.getTextFrom("CONTRACTACCEPT.Excepteddate");
					   Assert.assertEquals(uiExceptedDate, output);//Excepted 
					   String uiprice=library.getTextFrom("CONTRACTACCEPT.acceptedprice").replace("$", "").replace(",", "");
					   String xlprice = data.get("Price").toString().replace("$", "").replace(",", "");
					   Assert.assertEquals(uiprice, xlprice);//Price
					   library.wait(5);
					   String uiCommissionValue=library.getTextFrom("CONTRACTACCEPT.commission");
					   String xlCommission=data.get("commission").toString()+"%";
					   Assert.assertEquals(uiCommissionValue, xlCommission);//Commission
					   library.wait(5);
					   library.click("NOTES.cancelbutton");
					   library.wait(2);

					  } catch (Exception e) {
					   e.printStackTrace();
					  }
					 
					 }
				  
				  public void updateToStageForContractCancel(String stage, String client) {

					  searchAndSelectClient(client);

					  library.wait(3);
					  String stageBeforeUpdate = getPrestage();
					  System.out.println(stageBeforeUpdate);
					  library.wait(3);
					  library.click("TRANSACTION.updatebutton");
					  library.wait(5);
					  Assert.assertTrue(library.isElementDisabled("MAKEOFFER.continuebutton"));//apply button is disabled
					  
					  chooseTransactionStage(stage);
					  // library.click(stageLoc);

					  library.click("UPDATE.applybutton");
					  library.wait(3);
					  Assert.assertTrue(library.verifyPageContainsElement("CONTRACTCANCEL.title_holder"));//Verify contract cancel page is opened
					  library.typeDataInto("Test for Cancel", "NOTES.notesfield");
					  library.navigateBack();
					  library.wait(3);
					  library.verifyPageContainsElement("CONTRACTCANCEL.pageone");//verify stage is changed to contract cancel
					  boolean isNotesFieldPresent = library.verifyPageContainsElement("NOTES.notesfield");
					  Assert.assertTrue(isNotesFieldPresent); // validating Notes field.
					  boolean isElementPresent = library.verifyPageContainsElement("NOTES.savebutton");
					  Assert.assertTrue(isElementPresent);
					  library.waitForElement("NOTES.savebutton");
					  library.click("NOTES.savebutton");

					  library.waitForElement("TRANSACTION.properties");
					  library.wait(5);

					 }
				  
				  public void verifyNotesDataForContractCancel(Map<String, Object> data) {

					  String contactURL = getApiUrlForContractCancel(data);
					  setRequestHeaderforMadeAnOffer(data);
					  String response = library.HTTPGet(contactURL);
					 /* Assert.assertTrue(library.verifyPageContainsElement("id->com.movoto.agentfc:id/opputinity_name_holder"));*/
					  String UIvalue=library.getTextFrom("id->com.movoto.agentfc:id/opputinity_name_holder");
					  Assert.assertEquals(UIvalue,"Contract Cancelled");//Verify activity type for Contact Cancelled
					  String notesData = (String) library.getValueFromJson("$.activities[0].activityType", response);

					  String transactionType = notesData.split("-")[1];
					  transactionType = transactionType.trim();

					  String locator = getLocatorForTransactionType(transactionType);

					  library.verifyPageContainsElement(locator);

					  String notesLineTwo = library.getTextFrom("CONTRACTCANCEL.notesfieldlinetwo");
					  System.out.println(notesLineTwo);
					  Assert.assertEquals("Test for Cancel", notesLineTwo);

					  String notesTimeXpath = "NOTES.updatedtime";
					  String getTime = library.getTextFrom(notesTimeXpath);
					  String FinalValue = null;

					  try {

					   DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
					   formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
					   Date date = formatterIST.parse(getTime);

					   DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
					   formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
					   String[] SplitTime = formatterUTC.format(date).split(" ");
					   String[] Split = SplitTime[1].split(":");
					   FinalValue =SplitTime[0]+" "+Split[0]+":"+Split[1]+" "+SplitTime[2];
					   

					   System.out.println(FinalValue);

					   String ApiTime = (String) library.getValueFromJson("$.activities[0].startDateTime", response);
					   String[] SplitApiTime = ApiTime.split(" ");
					   String[] gotApiTime = SplitApiTime[1].split(":");
					   String finalTime =SplitApiTime[0]+" "+gotApiTime[0]+":"+gotApiTime[1]+" "+SplitApiTime[2];
					   System.out.println(finalTime);
					   Assert.assertEquals(finalTime, FinalValue);//Verify timestamp and date
					   library.wait(5);

					   library.click("NOTES.cancelbutton");
					   // library.waitForElement("TRANSACTION.properties");
					   library.wait(3);

					  }

					  catch (Exception e) {
					   e.printStackTrace();
					  }

					 }
				  
				  
				// Created by Priyanka dated :- 21/07/2016
				  public void updateToReturnToMovoto1(Map<String, String> data) {
				   library.wait(2);
				   library.click("TRANSACTION.updatebutton");
				   library.wait(2);
				   library.click("RETURN.returnToMovoto");
				   library.wait(2);
				   library.click("RETURN.apply");
				   library.wait(9);
				   library.click("RETURN.return");
				   library.wait(9);
				   String EmailID= data.get("EmailID");
				   library.click("HOMEPAGE.searchbutton"); // click on search
				   library.verifyPageContainsElement("HOMEPAGE.typeintosearch");
				   library.typeDataInto(EmailID, "HOMEPAGE.typeintosearch");
				   library.wait(5);
				   Assert.assertTrue(library.verifyPageContainsElement("RETURN.text")); //Verify client list is missing

				  }
				  
				// Created by Gopal
				  public void verifyClientDetailsWithApi(Map<String, Object> data) {
					  setTokenAndUserId(data);
					   String clientLeadApi = (String) data.get("clientLeadApi");
					   System.out.println(clientLeadApi);
					   library.wait(3);
					   String response = (String) getResponse(clientLeadApi);
					   System.out.println(response);

					   String mailIdFromApiResponse = (String) library.getValueFromJson("$.email", response);
					   String mailId = library.getTextFrom("DETAILS.mail");
					   Assert.assertEquals(mailId, mailIdFromApiResponse);// Verify the email matches the value from API response

					   
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
					        "Primary number is not matched.");//verify the Primary Phone matches the value from API response
					      contactList.add(contactType);
					      break;

					     case "mobile":
					      String mobileNumberOfApiResponse = (String) library
					        .getValueFromJson("$.phone[" + i + "].phonenumber", response);
					      String mobileNumber = library.getTextFrom("DETAILS.mobileNumber");
					      Assert.assertTrue(mobileNumber.equals(mobileNumberOfApiResponse), "Mobile number is not matched.");//Verify phone numbers which has values from API response match the value on lead's contact page
					      contactList.add(contactType);
					      break;

					     case "office":
					      String officeNumberOfApiResponse = (String) library
					        .getValueFromJson("$.phone[" + i + "].phonenumber", response);
					      String officeNumber = library.getTextFrom("DETAILS.officeNumber");
					      Assert.assertTrue(officeNumber.equals(officeNumberOfApiResponse), "Office number is not matched.");//Verify phone numbers which has values from API response match the value on lead's contact page
					      contactList.add(contactType);
					      break;

					     case "home":
					      String homeNumberOfApiResponse = (String) library.getValueFromJson("$.phone[" + i + "].phonenumber",
					        response);
					      String homeNumber = library.getTextFrom("DETAILS.homeNumber");
					      Assert.assertTrue(homeNumber.equals(homeNumberOfApiResponse), "Home number is not matched.");//Verify phone numbers which has values from API response match the value on lead's contact page
					      contactList.add(contactType);
					      break;

					     case "other":
					      String otherNumberOfApiResponse = (String) library
					        .getValueFromJson("$.phone[" + i + "].phonenumber", response);
					      String otherNumber = library.getTextFrom("DETAILS.otherNumber");
					      Assert.assertTrue(otherNumber.equals(otherNumberOfApiResponse), "Other number is not matched.");//Verify phone numbers which has values from API response match the value on lead's contact page
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
					    String value = library.getTextFrom("xpath->.//android.widget.TextView[contains(@text,'"+contact+"')]/../android.widget.TextView[2]");
					    Assert.assertEquals(value.equals("-"), true);//Verify phone numbers which has no values from API response has a "-" on the contact page

					    System.out.println(contactType);

				   }
				  }	  
				  
				// Created by Gopal
				  public void verifyOpportunitiesWithApi(Map<String, Object> data) {
					  String opportunityType = library.getTextFrom("TRANSACTION.opportunityType");
					   String subString = opportunityType.split(" ")[0];
					   setTokenAndUserId(data);
					   String response = getResponse((String) data.get("clientLeadApi2"));
					   System.out.println(response);
					   setTokenAndUserId(data);
						  String response1 = getResponse((String) data.get("clientLeadApi4"));
					   String opportunityTypeOfApiResponse = (String) library
					     .getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityType", response);
					   Assert.assertEquals(subString, opportunityTypeOfApiResponse);//Lead Type matches the API response

					   String urgencyFromApi = (String) library.getValueFromJson("$.urgency", response);
					   String[] urg = urgencyFromApi.split(" ");
					   String urgencyLabelFromApi = urg[0];
					   Assert.assertTrue(library.verifyPageContainsElement("name->" + urgencyLabelFromApi));//Urgency matches the API response

					   String opportunityAreaOfApiResponse = (String) library
					     .getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityStage", response);
					   boolean isOfferedStage = library.verifyPageContainsElement(
					     "xpath->.//android.widget.TextView[contains(@text,'" + opportunityAreaOfApiResponse.trim() + "')]");
					   Assert.assertTrue(isOfferedStage, "Offered stage is not matched");//Stage is Offered
						  String OfferPrice = library.getTextFrom("PROPERTIES.offerprice").replace("$", "").replace("K", "000");
						  int Offer=Integer.parseInt(OfferPrice);
						  System.out.println(response1);
						  int OfferPriceAPI = (int)library.getValueFromJson("$.activePropertyOffer.offerPrice", response1);
						  Assert.assertEquals(Offer, OfferPriceAPI);
					   //offer price does not exist for the particular API response.
				  }
				  
				// Created by Gopal
				  public void verifyOpportunitiesOfMetStage(Map<String, Object> data) {
					  String opportunityType1 = library.getTextFrom("name->Buy");
					   String opportunityType2 = library.getTextFrom("name->Sell");
					   String subString1 = opportunityType1.split(" ")[0];
					   String subString2 = opportunityType2.split(" ")[0];
					   String Transactnum= library.getTextFrom("id->com.movoto.agentfc:id/notesUpdateHolder");
					   Assert.assertEquals(Transactnum, "Transactions (2)");//verify transaction tab
					   setTokenAndUserId(data);
					   String response = getResponse((String) data.get("clientLeadApi3"));
					   System.out.println(response);
					   String UIUrgencyImage1=library.getTextFrom("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.TextView[6]");
						  String ApiUrgencyImage1=((String) library.getValueFromJson("$.urgency", response)).split(" ")[0];
						  Assert.assertEquals(UIUrgencyImage1, ApiUrgencyImage1);
					   String UIMet1=library.getTextFrom("xpath->//android.support.v7.widget.RecyclerView[1]/android.widget.LinearLayout[1]//android.widget.TextView[@text='Met']");
						  String ApiMet1=(String) library.getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityStage", response);
						  Assert.assertEquals(ApiMet1, UIMet1);// verify stage transaction is met
						  String UIMet2=library.getTextFrom("xpath->//android.support.v7.widget.RecyclerView[1]/android.widget.LinearLayout[2]//android.widget.TextView[@text='Met']");
						  String ApiMet2=(String) library.getValueFromJson("$.opportunitiesSummary.openOpportunities[1].opportunityStage", response);
						  Assert.assertEquals(ApiMet2, UIMet2);// verify stage transaction is met
					   String opportunityTypeOfApiResponse1 = (String) library
					     .getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityType", response);
					   Assert.assertEquals(subString1, opportunityTypeOfApiResponse1);//Lead Type of both transactions matche the API response
					   
					   String opportunityTypeOfApiResponse2 = (String) library
					     .getValueFromJson("$.opportunitiesSummary.openOpportunities[1].opportunityType", response);
					   Assert.assertEquals(subString2, opportunityTypeOfApiResponse2);//Lead Type of both transactions matche the API response

					   String urgencyFromApi = (String) library.getValueFromJson("$.urgency", response);
					   String[] urg = urgencyFromApi.split(" ");
					   String urgencyLabelFromApi = urg[0];
					   Assert.assertTrue(library.verifyPageContainsElement("name->" + urgencyLabelFromApi));//Urgency of both transactions match the API response

					   String opportunityAreaOfApiResponse = (String) library
					     .getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityStage", response);
					   boolean isOfferedStage = library.verifyPageContainsElement(
					     "xpath->.//android.widget.TextView[contains(@text,'" + opportunityAreaOfApiResponse.trim() + "')]");
					   Assert.assertTrue(isOfferedStage, "Met stage is not matched");//Stage for both transction are Met
				  }
				  
				  public void verifyNotesForTalkedStage(Map<String, Object> data) {
					  try{
						  String response = getResponse((String) data.get("notesApi"));
						  String activityTypeOfApiResponse = (String) library.getValueFromJson("$.activities[0].activityType", response);
						  System.out.println("\n" + activityTypeOfApiResponse + "\n");
						  String[] ele = activityTypeOfApiResponse.split("-");
						  String activityTypePath = "xpath->(.//android.widget.TextView[contains(@text,'" + ele[1].trim() + "')])[1]";
						  Assert.assertTrue(library.verifyPageContainsElement(activityTypePath));//Notes are displayed in chronological order, the most recent note is on top

						  String noteDiscription = (String) library.getValueFromJson("$.activities[0].note", response);
						  noteDiscription = noteDiscription.trim();
						  library.wait(2);
						  Assert.assertTrue(library.verifyPageContainsElement(
						    "xpath->(.//android.widget.TextView[contains(@text,'" + noteDiscription + "')])[1]"));//The latest note's activity type on app matches the API response

						  library.click("NOTES.cancelbutton");
						  }
						  catch(Exception e)
						  {
						   e.printStackTrace();
						   System.out.println("Date format is wrong");
						  }
					 }
				  
				  public void verifyPropertiesOftalkedStage(Map<String, Object> data) throws java.text.ParseException {
					  library.click("TRANSACTION.properties");
				       setTokenAndUserId(data);
				       String response = getResponse((String) data.get("propertiesApi"));
				       Object propertyPriceOfApiResponse = (Object) library.getValueFromJson("$.properties[0].price", response);
				       Integer priceInteger = (Integer) propertyPriceOfApiResponse;
				       int price1 = (int) priceInteger;
				       System.out.println(propertyPriceOfApiResponse);
				       String propertyPriceInApp = library.getTextFrom("MAKEOFFER.propertyPrice");
				       System.out.println(propertyPriceInApp);
				       propertyPriceInApp = propertyPriceInApp.replace("$", "");
				       propertyPriceInApp = propertyPriceInApp.replace(",", "");
				       int price2 = Integer.parseInt(propertyPriceInApp);
				       System.out.println(propertyPriceInApp);
				       Assert.assertTrue(price1 == price2, "Price is matched with api response");// Property price matches the API response
				       String getTime = library.getTextFrom("id->com.movoto.agentfc:id/interested_viewed_holder");
						  String[] Mettime = getTime.split(" ");
						  String MettimeSplit = Mettime[0] + " " + Mettime[1] + " " + Mettime[2];

						  String FinalValue = null;
						  try {

						   DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
						   formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
						   Date date = formatterIST.parse(MettimeSplit);

						   DateFormat formatterUTC = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
						   formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
						   String[] SplitTime = formatterUTC.format(date).split(" ");
						   String Split = SplitTime[1];
						   String[] SplitSec = Split.split(":");
						   FinalValue = SplitSec[0] + ":" + SplitSec[1];
						   System.out.println(FinalValue);

						   String ApiTime = (String) library.getValueFromJson("$.properties[0].dateExpressedInterest", response);
						   String[] SplitApiTime = ApiTime.split(" ");
						   String[] gotApiTime = SplitApiTime[1].split(":");
						   String finalTime =gotApiTime[0]+":"+gotApiTime[1];
						   System.out.println(finalTime);
						   Assert.assertEquals(finalTime, FinalValue);
						   library.wait(2);

						  } catch (Exception e) {
						   e.printStackTrace();
						  }				       
				         String zip = (String) library.getValueFromJson("$.properties[0].zip", response);
				         String city = (String) library.getValueFromJson("$.properties[0].city", response);
				         String state = (String) library.getValueFromJson("$.properties[0].state", response);
				         String address = (String) library.getValueFromJson("$.properties[0].address", response);
				         
				         String AddressAPI = ""+address+" "+city+", "+state+" "+zip+"";
				         String AddressUI = library.getTextFrom("id->com.movoto.agentfc:id/address_holder");
				                     Assert.assertEquals(AddressAPI, AddressUI);

				         
				       
//				       String MLSapi= (String) library.getValueFromJson("$.properties[0].mls", response);
//				       //Assert.assertTrue(library.verifyPageContainsElement("name->"+ MLSapi +"", true)); //verify mls
//				       String leadmsgapi= (String) library.getValueFromJson("$.properties[0].message", response);
//				       String leadmsgui= library.getTextFrom("id->com.movoto.agentfc:id/msg_holder");
//				       //Assert.assertEquals(leadmsgapi, leadmsgui);//verify lead msg
//				       
				       Object numberOfBedsInApiResponse = (Object) library.getValueFromJson("$.properties[0].bed", response);
				       if (numberOfBedsInApiResponse!=null){
				       Integer bedsInteger = (Integer) numberOfBedsInApiResponse;   
				       int numOfbedsInApiResponse = (int) (bedsInteger);

				       Object numberOfBathsInApiResponse = (Object) library.getValueFromJson("$.properties[0].bath", response);
				       Integer bathsInteger = (Integer) numberOfBathsInApiResponse;
				       int baths2 = (int) bathsInteger;

				       Object areaOfPropertyInApiResponse = (Object) library.getValueFromJson("$.properties[0].sqft", response);
				       Integer areaIntger = (Integer) areaOfPropertyInApiResponse;
				       int area2 = (int) areaIntger;
				       boolean isMatched = library.verifyPageContainsElement("xpath->.//android.widget.TextView[contains(@text,'- Bd | " + baths2 + " Ba | " + area2 + " Sq.Ft')]");
				       Assert.assertTrue(isMatched, "Number of baths, beds and area are matched with api response");//verify propert info
				       }else{
				       boolean isMatched = library.verifyPageContainsElement("xpath->.//android.widget.TextView[contains(@text,'- Bd | - Ba | - Sq.Ft')]");
				       Assert.assertTrue(isMatched, "Number of baths, beds and area are matched with api response");//verify propert info
				       }
					 }
				  	public void loginAndDeleteFavouriteProperty(JSONObject data) {
						// TODO Auto-generated method stub
				
					}
				
					public void verifyAddFavouriteFunctionality(JSONObject data) {
						// TODO Auto-generated method stub
				
					}
				
					//Date Created 21OCT2016 BY MANAS
					public void LoginCredential(JSONObject data) {
						// TODO Auto-generated method stub
				
					}
				
					public void NavigateToFavouriteHomes(JSONObject data) {
						// TODO Auto-generated method stub
				
					}
				
					public void deleteFavouriteItem(JSONObject data) {
						// TODO Auto-generated method stub
				
					}
				
					public void SelectFavouriteProperty(JSONObject data) {
						// TODO Auto-generated method stub
						
				
					}
					public void VerifySchoolsBasicInfo(JSONObject jsonObj) {
						// TODO Auto-generated method stub

					}
	
					public void VerifyThatTestScoresSectionWorks(JSONObject jsonObj) {
						// TODO Auto-generated method stub
						
					}
	
					public void VerifyPropertiesAssignedToSchoolSectionWorks(JSONObject jsonObj) {
						// TODO Auto-generated method stub

					}
					
					public void verifySaveSearchButtonIsDisabled(){
						
					}
					
					public void verifySortingForPrice(){
						
					}
					
					public void verifySortingForSquareBigOption(){
						
					}
					public void countPropertyCardAndCheckSqft(String data){
						
					}

					public void loginfavicon(JSONObject jsonObj) {
						// TODO Auto-generated method stub
						
					}

					

					public void verifypricepropertytypebedroomsbathroomsaddresscitycardurlphoto() {
						// TODO Auto-generated method stub
						
					}

					public void verifythelasttwoproperties() {
						// TODO Auto-generated method stub
						
					}

					public void verifytheaddfavoriteiconinthecardturnsredandthedetailinfoishided() {
						// TODO Auto-generated method stub
						
					}

					public void navigatefavihome(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					
					public void verifypropertiesassignedtoschoolisdisplayed(JSONObject Data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyWsasTestScoresForSchoolNamesIsDisplayed(
							JSONObject Data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyGradeNumbersIsSameWithTestScoreApiResponse(
							JSONObject Data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyWsasTestScoresDetailInfoIsHided(
							JSONObject Data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyWsasScoringTextIsSameWithWsasApi(
							JSONObject Data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyGrade8TitleMathAndReadingAndYearsOfEachChart(
							JSONObject Data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyGrade7TitleMathAndReadingAndYearsOfEachChart(
							JSONObject Data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyGrade6TitleMathAndReadingAndYearsOfEachChart(
							JSONObject Data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyopenhouseincityisdisplayed(JSONObject Data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyopenhousepricepropertytypebedroomsbathroomsaddresscitycardurlphoto() {
						// TODO Auto-generated method stub
						
					}

					
					public void verifyopenhousefirstpropertyaddressmatchesnewlistingsnearbyapi() {
						// TODO Auto-generated method stub
						
					}

					public void loginopenhousefavicon(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					public void navigateopenhousefavihome(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					public void verifytheopenhouseaddfavoriteiconinthecardturnsred() {
						// TODO Auto-generated method stub
						
					}

					public void verifytheopenhousefavoritehouseaddressissamewithfirstcard() {
						// TODO Auto-generated method stub
						
					}

					public void verifyopenhouseupbuttonishided() {
						// TODO Auto-generated method stub
						
					}

					public void verifythefavoritehouseaddressissamewithfirstcard(
							JSONObject Data) {
						// TODO Auto-generated method stub
						
					}
					
					public void checkPreconditionFor94(){
						
					}
					public void clickNineTimesOnPropertyImageForMobilePlatform(String data){
						
					}
					public void verifyMinPrice(Map<String, Object> data){
						
					}
					
					public void verifyMaxPrice(Map<String, Object> data){
						
					}
					public void verifyMinValueForBath(Map<String, Object> data){
						
					}
					public void verifyMinValueForBed(Map<String, Object> data){
						
					}
					public String getValueFromApplyButtonJS(){
						return null;
						
					}
					
					public void setFutureDateUsingCalanderForFunctional(Map<String, Object> data) {
						
						for (int k = 1; k <= 1; k++) {
							int i=k+1;
							Calendar currentCal = Calendar.getInstance();
							DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
							currentCal.add(Calendar.DATE, k);
							String Startdatee = dateFormat.format(currentCal.getTime());
							currentCal.add(Calendar.DATE, k);
							String endDate = dateFormat.format(currentCal.getTime()).split("/")[1];
							library.wait(2);
							library.typeDataInto(Startdatee,"xpath->//android.widget.LinearLayout[1]/android.widget.RelativeLayout["+k+"]/android.widget.TextView[2]");
							library.wait(3);
							library.click("xpath->//android.widget.LinearLayout[1]/android.widget.RelativeLayout["+i+"]/android.widget.TextView[2]");
							library.wait(2);
							library.click("xpath->//android.widget.LinearLayout[1]/android.widget.NumberPicker[2]/android.widget.EditText[1]");
							library.wait(2);
							library.clear("xpath->//android.widget.LinearLayout[1]/android.widget.NumberPicker[2]/android.widget.EditText[1]");
							library.wait(2);
							library.typeDataInto(endDate,"xpath->//android.widget.LinearLayout[1]/android.widget.NumberPicker[2]/android.widget.EditText[1]");
							library.wait(3);
							library.navigateBack();
							library.click("FLEAVE.schedulebutton");
							library.wait(5);
						}
					}

					public void verifyNewListingsProperties(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyReflashicon(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyfirstpropertymatchesAPI(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyfavouriteicon(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyfirstcard(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					public void VerifyLastarticletitle(JSONObject data,String response) {
						// TODO Auto-generated method stub
						
					}

					public void VerifyFirstarticletitle(JSONObject data,String response) {
						// TODO Auto-generated method stub
						
					}

					public void VerifyTitleandUrl(JSONObject data,String response) {
						// TODO Auto-generated method stub
						
					}

					public Boolean verifyPopulationDemographics(String response) {
						// TODO Auto-generated method stub
						return null;
					}

					public Boolean verifyChartPopulationDemographics(String response) {
						// TODO Auto-generated method stub
						return null;
					}

					public Boolean verifyChartHouseholdDistribution(String response) {
						// TODO Auto-generated method stub
						return null;
					}

					public Boolean verifyChartNeighborhoodHousingCharacteristics(String response) {
						// TODO Auto-generated method stub
						return null;
					}

					public Boolean verifyChartRentalInformationFlag(String response) {
						// TODO Auto-generated method stub
						return null;
					}

					public Boolean verifyChartPopulationDemographicsIE(String response) {
						// TODO Auto-generated method stub
						return null;
					}

					public Boolean verifyChartHouseholdDistributionIE(String response) {
						// TODO Auto-generated method stub
						return null;
					}

					public Boolean verifyChartNeighborhoodHousingCharacteristicsIE(String response) {
						// TODO Auto-generated method stub
						return null;
					}

					public Boolean verifyChartRentalInformationFlagIE(String response) {
						// TODO Auto-generated method stub
						return null;
					}

					public Boolean verifyHouseholdDistribution(String response) {
						// TODO Auto-generated method stub
						return null;
					}

					public Boolean verifyNeighborhoodHousingCharacteristics(String response) {
						// TODO Auto-generated method stub
						return null;
					}

					public Boolean verifyRentalInformationFlag(String response) {
						// TODO Auto-generated method stub
						return null;
					}

					public void verifyChartWithApiResponse() {
						// TODO Auto-generated method stub
						
					}

					public void verifyUpIcon() {
						// TODO Auto-generated method stub
						
					}

					public void verifyDownIcon() {
						// TODO Auto-generated method stub
						
					}

					public void verifyGraph() {
						// TODO Auto-generated method stub
						
					}

					public void VerifyOpenhousesectionProperties (JSONObject data)
							throws org.json.simple.parser.ParseException {
						// TODO Auto-generated method stub
						
					}

					public void verifyReflashiconforopenhousesection(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyfirstpropertymatchesAPIopenhouses(JSONObject data) throws org.json.simple.parser.ParseException {
						// TODO Auto-generated method stub
						
					}

					public void verifyfavouriteiconOpenHouses(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyfirstcardOpenHouses(JSONObject data) {
						// TODO Auto-generated method stub
						
					}
					
                    
                    public long timeinSalesforce() throws ParseException, java.text.ParseException
                      {
                       String EmailID=library.getTextFrom("xpath->.//*[@id='lea11_ileinner']/a");
                        library.clear("xpath->.//*[@id='phSearchInput']");
                        library.typeDataInto(EmailID, "xpath->.//*[@id='phSearchInput']");
                        library.click("xpath->.//*[@id='phSearchButton']");
                        library.wait(15);
                        Assert.assertTrue(library.verifyPageContainsElement("xpath->//a[text()='"+ EmailID +"']"));
                        library.click("xpath->.//*[@id='Lead_body']/table/tbody/tr[2]/th/a");
                       library.wait(20);
                         String Sent_time= library.getTextFrom("xpath->//*[text()='N2']/..//td[6]");
                         System.out.println(Sent_time);
                         DateFormat df = new SimpleDateFormat("mm/dd/yyyy hh:mm a", Locale.ENGLISH);
                         Date Sent_timeSalesforce=df.parse(Sent_time);
                       long milliseconds = Sent_timeSalesforce.getTime();
                       long ISTtime= milliseconds + 48600000;
                       System.out.println(ISTtime);
                      return (long) ISTtime;
                      
                       
                      }
                    
                    public long timeinSalesforceFC3() throws ParseException, java.text.ParseException
                      {
                       String EmailID=library.getTextFrom("xpath->.//*[@id='lea11_ileinner']/a");
                        library.clear("xpath->.//*[@id='phSearchInput']");
                        library.typeDataInto(EmailID, "xpath->.//*[@id='phSearchInput']");
                        library.click("xpath->.//*[@id='phSearchButton']");
                        library.wait(15);
                        Assert.assertTrue(library.verifyPageContainsElement("xpath->//a[text()='"+ EmailID +"']"));
                        library.click("xpath->.//*[@id='Lead_body']/table/tbody/tr[2]/th/a");
                        library.wait(20);
                        library.clear("xpath->.//*[@id='phSearchInput']");
                        library.typeDataInto(EmailID, "xpath->.//*[@id='phSearchInput']");
                        library.click("xpath->.//*[@id='phSearchButton']");
                        library.wait(15);
                        Assert.assertTrue(library.verifyPageContainsElement("xpath->//a[text()='"+ EmailID +"']"));
                        library.click("xpath->.//*[@id='Lead_body']/table/tbody/tr[2]/th/a");
                       library.wait(20);
                         String Sent_time= library.getTextFrom("xpath->//*[text()='FC3']/..//td[6]");
                         System.out.println(Sent_time);
                         DateFormat df = new SimpleDateFormat("mm/dd/yyyy hh:mm a", Locale.ENGLISH);
                         Date Sent_timeSalesforce=df.parse(Sent_time);
                       long milliseconds = Sent_timeSalesforce.getTime();
                       long ISTtime= milliseconds + 48600000;
                       System.out.println(ISTtime);
                      return (long) ISTtime;
                      
                       
                      }
                    
                    public long timeinSalesforceFC025() throws ParseException, java.text.ParseException
                      {
                       String EmailID=library.getTextFrom("xpath->.//*[@id='lea11_ileinner']/a");
                        library.clear("xpath->.//*[@id='phSearchInput']");
                        library.typeDataInto(EmailID, "xpath->.//*[@id='phSearchInput']");
                        library.click("xpath->.//*[@id='phSearchButton']");
                        library.wait(15);
                        Assert.assertTrue(library.verifyPageContainsElement("xpath->//a[text()='"+ EmailID +"']"));
                        library.click("xpath->.//*[@id='Lead_body']/table/tbody/tr[2]/th/a");
                        library.wait(20);
                        library.clear("xpath->.//*[@id='phSearchInput']");
                        library.typeDataInto(EmailID, "xpath->.//*[@id='phSearchInput']");
                        library.click("xpath->.//*[@id='phSearchButton']");
                        library.wait(15);
                        Assert.assertTrue(library.verifyPageContainsElement("xpath->//a[text()='"+ EmailID +"']"));
                        library.click("xpath->.//*[@id='Lead_body']/table/tbody/tr[2]/th/a");
                       library.wait(20);
                         String Sent_time= library.getTextFrom("xpath->//*[text()='FC0.25']/..//td[6]");
                         System.out.println(Sent_time);
                         DateFormat df = new SimpleDateFormat("mm/dd/yyyy hh:mm a", Locale.ENGLISH);
                         Date Sent_timeSalesforce=df.parse(Sent_time);
                       long milliseconds = Sent_timeSalesforce.getTime();
                       long ISTtime= milliseconds + 48600000;
                       System.out.println(ISTtime);
                      return (long) ISTtime;
                      
                       
                      }
                    
                    public long timeinSalesforces() throws ParseException, java.text.ParseException
                      {
                       String EmailID=library.getTextFrom("xpath->.//*[@id='lea11_ileinner']/a");
                        library.clear("xpath->.//*[@id='phSearchInput']");
                        library.typeDataInto(EmailID, "xpath->.//*[@id='phSearchInput']");
                        library.click("xpath->.//*[@id='phSearchButton']");
                        library.wait(15);
                        Assert.assertTrue(library.verifyPageContainsElement("xpath->//a[text()='"+ EmailID +"']"));
                        library.click("xpath->.//*[@id='Lead_body']/table/tbody/tr[2]/th/a");
                       library.wait(20);
                         String Sent_time= library.getTextFrom("xpath->//*[text()='N3']/..//td[6]");
                         System.out.println(Sent_time);
                         DateFormat df = new SimpleDateFormat("mm/dd/yyyy hh:mm a", Locale.ENGLISH);
                         Date Sent_timeSalesforce=df.parse(Sent_time);
                       long milliseconds = Sent_timeSalesforce.getTime();
                       long ISTtime= milliseconds + 48600000;
                       System.out.println(ISTtime);
                      return (long) ISTtime;
                      
                       
                      }
                     public void writeInExcelFileForSalesforceTime() throws ParseException, java.text.ParseException {
                      try {
                       FileOutputStream fileOut = new FileOutputStream("data/SalesForceTime.xlsx");
                       XSSFWorkbook workbook = new XSSFWorkbook();
                       XSSFSheet worksheet = workbook.createSheet("SalesForceTime");

                       // index from 0,0... cell A1 is cell(0,0)
                       // HSSFRow row1 = worksheet.createRow((short) 0);
                       // HSSFRow row1 = worksheet.createRow(0);
                       XSSFRow row1 = worksheet.createRow(0); // creating Row

                       XSSFCell cellA1 = row1.createCell(1);
                       cellA1.setCellValue("SalesForceTime");

                       XSSFRow row2 = worksheet.createRow(1);

                       XSSFCell cellB1 = row2.createCell(1);
                       
                       Long value=timeinSalesforce();
                       String value1 = value.toString();

                       cellB1.setCellValue( value1);

                       workbook.write(fileOut);

                       fileOut.close();
                      } catch (FileNotFoundException e) {
                       e.printStackTrace();
                      } catch (IOException e) {
                       e.printStackTrace();
                      }
                     }
                     public void writeInExcelFileForSalesforceTimes() throws ParseException, java.text.ParseException {
                          try {
                           FileOutputStream fileOut = new FileOutputStream("data/SalesForceTime.xlsx");
                           XSSFWorkbook workbook = new XSSFWorkbook();
                           XSSFSheet worksheet = workbook.createSheet("SalesForceTime");

                           // index from 0,0... cell A1 is cell(0,0)
                           // HSSFRow row1 = worksheet.createRow((short) 0);
                           // HSSFRow row1 = worksheet.createRow(0);
                           XSSFRow row1 = worksheet.createRow(0); // creating Row

                           XSSFCell cellA1 = row1.createCell(1);
                           cellA1.setCellValue("SalesForceTime");

                           XSSFRow row2 = worksheet.createRow(1);

                           XSSFCell cellB1 = row2.createCell(1);
                           
                           Long value=timeinSalesforces();
                           String value1 = value.toString();

                           cellB1.setCellValue( value1);

                           workbook.write(fileOut);

                           fileOut.close();
                          } catch (FileNotFoundException e) {
                           e.printStackTrace();
                          } catch (IOException e) {
                           e.printStackTrace();
                          }
                         }
                    
                     
                     public void writeInExcelFileForSalesforceTimeFC3() throws ParseException, java.text.ParseException {
                          try {
                           FileOutputStream fileOut = new FileOutputStream("data/SalesForceTime.xlsx");
                           XSSFWorkbook workbook = new XSSFWorkbook();
                           XSSFSheet worksheet = workbook.createSheet("SalesForceTime");

                           // index from 0,0... cell A1 is cell(0,0)
                           // HSSFRow row1 = worksheet.createRow((short) 0);
                           // HSSFRow row1 = worksheet.createRow(0);
                           XSSFRow row1 = worksheet.createRow(0); // creating Row

                           XSSFCell cellA1 = row1.createCell(1);
                           cellA1.setCellValue("SalesForceTime");

                           XSSFRow row2 = worksheet.createRow(1);

                           XSSFCell cellB1 = row2.createCell(1);
                           
                           Long value=timeinSalesforceFC3();
                           String value1 = value.toString();

                           cellB1.setCellValue( value1);

                           workbook.write(fileOut);

                           fileOut.close();
                          } catch (FileNotFoundException e) {
                           e.printStackTrace();
                          } catch (IOException e) {
                           e.printStackTrace();
                          }
                         }
                     public void writeInExcelFileForSalesforceTimeFC025() throws ParseException, java.text.ParseException {
                          try {
                           FileOutputStream fileOut = new FileOutputStream("data/SalesForceTime.xlsx");
                           XSSFWorkbook workbook = new XSSFWorkbook();
                           XSSFSheet worksheet = workbook.createSheet("SalesForceTime");

                           // index from 0,0... cell A1 is cell(0,0)
                           // HSSFRow row1 = worksheet.createRow((short) 0);
                           // HSSFRow row1 = worksheet.createRow(0);
                           XSSFRow row1 = worksheet.createRow(0); // creating Row

                           XSSFCell cellA1 = row1.createCell(1);
                           cellA1.setCellValue("SalesForceTime");

                           XSSFRow row2 = worksheet.createRow(1);

                           XSSFCell cellB1 = row2.createCell(1);
                           
                           Long value=timeinSalesforceFC025();
                           String value1 = value.toString();

                           cellB1.setCellValue( value1);

                           workbook.write(fileOut);

                           fileOut.close();
                          } catch (FileNotFoundException e) {
                           e.printStackTrace();
                          } catch (IOException e) {
                           e.printStackTrace();
                          }
                         }
                    
                     public void timeinapp(Map<String, Object> data) throws ParseException, java.text.ParseException 
                     {library.wait(4);
                     String date = library.getTextFrom("id->com.movoto.agentfc:id/date_holder");
                     String time = library.getTextFrom("id->com.movoto.agentfc:id/time_holder");
                     String time1= time.replace("@", "");
                     String Notificationtime= date + " " + time1;
                     System.out.println(Notificationtime);
                     DateFormat formatter = new SimpleDateFormat("mm/dd/yyyy hh:mm a", Locale.ENGLISH);
                     Date Sent_timeNotification=formatter.parse(Notificationtime);
                     long apptimeinmilliseconds = Sent_timeNotification.getTime();
                     System.out.println(apptimeinmilliseconds);
                     String SalesforceTimeinmilliseconds= (String) data.get("SalesForceTime");
                     long SalesforceTimeinmilliseconds1 = Long.parseLong(SalesforceTimeinmilliseconds);
                     System.out.println(SalesforceTimeinmilliseconds);
                     Assert.assertTrue(apptimeinmilliseconds-SalesforceTimeinmilliseconds1<=1200000);
                     
                      
                     }

				     
				     
				     

					public void disableChatWindow() {
						// TODO Auto-generated method stub
						
					}

					public void verifytextandURL(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyzipcodeandCity(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					public void verifypopupdisappears() {
						// TODO Auto-generated method stub
						
					}

					public void verifyUpandDownIcon() {
						// TODO Auto-generated method stub
						
					}

					public void verifyNewListingPropertyWithApi(JSONObject Data, int numberofcard, String response)
							throws org.json.simple.parser.ParseException {
						// TODO Auto-generated method stub
						
					}

					public void veirfyDetailInfoIsHidedNewListing() {
						// TODO Auto-generated method stub
						
					}

					public void verifyAddfavoriteIconintheCardturnsredNewListing() {
						// TODO Auto-generated method stub
						
					}

					public void selectFirstFavouriteCardNewList(JSONObject Data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyAndClickRefreshIconNewListing() {
						// TODO Auto-generated method stub
						
					}

					public void clickonLeftIconTillVisibleNewListing() {
						// TODO Auto-generated method stub
						
					}

					public void verifyCityinNewListings(JSONObject Data) {
						// TODO Auto-generated method stub
						
					}

					public String getNewListingResponseFromApi(JSONObject Data, String apiurl) {
						// TODO Auto-generated method stub
						return null;
					}

					public String getResponseGetToKnowSectionApi(JSONObject data) {
						// TODO Auto-generated method stub
						return null;
					}

					public String getOpenhouseResponseFromApi(JSONObject Data, String apiurl) {
						// TODO Auto-generated method stub
						return null;
					}

					public void verifyCityinOpenhouse(JSONObject Data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyPricePropertyTypeBedRoomsBathRoomsAddressCityCardUrlPhotoOfOpenHouse(
							JSONObject Data, String dCheck, String response) {
						// TODO Auto-generated method stub
						
					}

					public void verifyReflashIconIsDisplayedOfOpenHouse() {
						// TODO Auto-generated method stub
						
					}

					public void loginFavIconOfOpenHouse(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyTheAddFavoriteIconInTheCardTurnsRedOfOpenHouse() {
						// TODO Auto-generated method stub
						
					}

					public void verifyUpButtonIsHidedOfOpenHouse() {
						// TODO Auto-generated method stub
						
					}

					public void verifyAndClickRefreshIconOfOpenHouse() {
						
					}
					
					public void remove_PopUp() {

						// TODO Auto-generated method stub
						
					}

					public void compareOpenHouseAndNewListingApiValue(String openHousesValue,
							String newListingValue, JSONObject Data) {
						// TODO Auto-generated method stub
					}

					public int getMinNoOfRecordsPerPageUI() {
						// TODO Auto-generated method stub
						return 0;
					}

					public void verifyFirstFavouriteUrl(JSONObject Data) {
						// TODO Auto-generated method stub
						
					}

					public void verifyTheFavoriteHouseURLIsSameWithFirstCard(JSONObject data) {
						// TODO Auto-generated method stub
						
					}

					public void VerifyTitleandUrl_API(JSONObject Data, String response) {
						// TODO Auto-generated method stub
						
					}

					public void tellUsAboutYourExperiencePopup() {
						// TODO Auto-generated method stub
						
					}

					public void openHousesSection(JSONObject jsonObj) {
						// TODO Auto-generated method stub
						
					}

					public void VerifyNewListingsSectionWorksWell(JSONObject jsonObj) {
						// TODO Auto-generated method stub
						
					}
					
					
				     

}

