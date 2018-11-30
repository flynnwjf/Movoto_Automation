package com.movoto.scripts;

import java.awt.Point;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import com.movoto.fixtures.FixtureLibrary;
import com.movoto.fixtures.impl.util.FileUtil;

public class CommonScenariosIOS extends CommonScenarios {

	public CommonScenariosIOS(FixtureLibrary library) {
		super(library);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loginToAgentAppWithUsernameAndPassword(String username, String password) {
		library.clear("LOGIN.usernamefield");
		library.typeDataInto(username + "\n", "LOGIN.usernamefield");
		library.wait(3);
		library.clear("LOGIN.passwordfield");
		library.typeDataInto(password + "\n", "LOGIN.passwordfield");
	}

	@Override
	public void loginWithUsernameAndPasswordAndExpectError(String username, String password, String error) {

		library.clear("LOGIN.usernamefield");
		library.typeDataInto(username + "\n", "LOGIN.usernamefield");
		library.clear("LOGIN.passwordfield");
		library.typeDataInto(password + "\n", "LOGIN.passwordfield");

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

	@Override
	public void updateToStageForClient(String stage, String client) {

		String saveNotesLoc = "name->Save Update";
		if (stage.equalsIgnoreCase("Scheduled a Callback")) {
			saveNotesLoc = "name->Set Reminder";
		} else if (stage.equalsIgnoreCase("Scheduled a Meeting")) {
			saveNotesLoc = "name->Schedule";
		}

		openClientDetailPage(client);

		library.wait(4);
		library.verifyPageContainsElement("TRANSACTION.updatebutton");
		library.click("TRANSACTION.updatebutton");
		chooseTransactionStage(stage);
		// library.click(stageLoc);
		library.wait(4);
		library.verifyPageContainsElement("UPDATE.applybutton");
		library.click("UPDATE.applybutton");
		library.typeDataInto("Agent Automation", "NOTES.notesfield");

		library.wait(4);

		if (stage.equalsIgnoreCase("Contract Cancelled")) {
			Assert.assertTrue(library.verifyPageContainsElement(saveNotesLoc));
		}
		if (stage.equalsIgnoreCase("Scheduled a Meeting")) {
			Assert.assertTrue(library.verifyPageContainsElement(saveNotesLoc), "Save Button not enabled");
			// Assert.assertTrue(library.verifyPageContainsElement("SMUPDATE.date",
			// true), "Date field not enabled");
			Assert.assertTrue(library.verifyPageContainsElement("SMUPDATE.starttime"), "Start Date not enabled");
			Assert.assertTrue(library.verifyPageContainsElement("SMUPDATE.endtime"), "End Date not enabled");
		}
		library.verifyPageContainsElement(saveNotesLoc);
		library.click(saveNotesLoc);
		library.waitForElement("TRANSACTION.properties");
		library.wait(5);
	}

	@Override
	protected void handlePreview() {
		boolean previewExists = library.verifyPageContainsElement("HOMEPAGE.previewnextbutton");
		if (previewExists) {
			library.click("HOMEPAGE.previewnextbutton");
			library.click("HOMEPAGE.previewnextbutton");
			library.click("HOMEPAGE.previewnextbutton");
			library.click("HOMEPAGE.previewdonebutton");
		}
	}

	@Override
	public void verifyClientNameWith(String clientName) {
		library.swipeFromTo(250, 330, 250, 550);
		library.wait(3);
		super.verifyClientNameWith(clientName);
	}

	@Override
	protected String getStageLocator(String client, String stage) {
		library.swipeFromTo(250, 330, 250, 550);
		return "xpath->//UIAStaticText[@name='" + client + "']/../UIAStaticText[@name='" + stage + "']";
	}

	@Override
	protected String getStageLocator(String client) {
		library.swipeFromTo(250, 330, 250, 550);
		return "xpath->//UIAStaticText[@name='" + client + "']/../UIAStaticText[5]";
	}

	// @Override
	// public void navigateToClientListPage() {
	// library.swipeFromTo(10, 330, 270, 330);
	// // library.click("HOMEPAGE.hamburger");
	// library.click("MENU.clientlistpagelink");
	// }
	//
	@Override
	public void navigateToNotificationsListPage() {
		library.wait(3);
		library.swipeFromTo(250, 330, 250, 550);
		// library.click("HOMEPAGE.hamburger");
		library.click("MENU.notificationpagelink");

	}

	@Override
	protected void handleEditTransactionAlert() {
		library.setImplicitWaitTime(5);
		boolean duplicateExists = library.verifyPageContainsElement("MAKEOFFER.editok");
		library.setImplicitWaitTime(30);
		if (duplicateExists) {
			library.click("MAKEOFFER.editok");// alert yes
		}
	}

	@Override
	protected void chooseTransactionStage(String stage) {
		library.typeDataInto(stage, "UPDATE.list");
	}

	@Override
	protected void enterAddress(String address) {
		library.wait(3);
		library.click("MAKEOFFER.addresslabel");
		library.clear("MAKEOFFER.address");
		library.typeDataInto(address + "\n", "MAKEOFFER.address");// address
		library.wait(5);
	}

	@Override
	protected void chooseDate(String month, String day, String year) {
		/*
		 * library.typeDataInto(day, "DATE.day"); library.typeDataInto(month,
		 * "DATE.month"); library.typeDataInto(year, "DATE.year");
		 */
		library.click("DATE.okbutton");
	}

	@Override
	protected void enterNumber(String data, String loc) {
		library.click(loc);
		library.typeDataInto(data, loc);
	}

	@Override
	protected void openDateField(String field) {
		library.click(field);
	}

	@Override
	public void openMenu() {
		library.wait(3);
		library.swipeFromTo(10, 330, 270, 330);
	}

	@Override
	public void chooseStartAndEndLeaveDates(String startDate, String endDate) {
		String sdate[] = startDate.split("-");
		String edate[] = endDate.split("-");

		chooseLeaveDateiOS(sdate[0], sdate[1], sdate[2]);

		library.click("FLEAVE.enddatefield");

		chooseLeaveDateiOS(edate[0], edate[1], edate[2]);
	}

	private void chooseLeaveDateiOS(String month, String day, String year) {
		library.typeDataInto(day, "DATE.day");
		library.typeDataInto(month, "DATE.month");
		library.typeDataInto(year, "DATE.year");
	}

	@Override
	protected String getDateLocatorForIndex(int index) {
		String dateLoc = "xpath->//UIATableView/UIATableCell[" + index + "]/UIAStaticText[3]";
		return dateLoc;
	}

	@Override
	protected void verifyDate(String date) {
		date = date.split(" ")[0];
		super.verifyDate(date);
	}

	@Override
	public void verifyDateForNotificationIsValid(int index) {
		// TODO Auto-generated method stub
		// super.verifyDateForNotificationIsValid(index);
	}

	private void scrollToText(String text) {
		int count = 20;
		boolean found = false;
		while (!found && count > 0) {
			library.wait(3);
			library.swipeFromTo(250, 580, 250, 50);
			found = library.verifyPageContainsElement("name->" + text);
		}
	}

	@Override
	public void verifyN0(String message) {
		verifyNotificationExists(message);
	}

	@Override
	public void verifyFC5(String message) {
		verifyNotificationExists(message);
	}

	// Created by Priyanka on 05/24/2016 to verify Client filter
	@Override
	public void verifyClientFilter(Map<String, Object> data) {
		setRequestHeader(data);
		String contactURL = String.valueOf(data.get("contactsUrl"));
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		for (int i = 0; i < arrayCount; i++) {
			// library.wait(1);
			library.click("name->search icon");
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
			Assert.assertTrue(
					library.verifyPageContainsElement("xpath->//UIAStaticText[@name='" + LeadName + "']"));
			library.click("FLEAVE.searchicon");
		}

	}

	// Created by Priyanka to Verify lead names match as per API
	// output.
	@Override
	public void verifyLeadsName(Map<String, Object> data) {
		setRequestHeader(data);
		String contactURL = String.valueOf(data.get("contactsUrl"));
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		for (int i = 0; i < arrayCount; i++) {
			library.wait(1);
			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;
			System.out.println(LeadName);
			String LeadNameuixpath = "xpath->//UIATableCell[@name='" + LeadName + "']/UIAStaticText[1]";
			String LeadNameui = library.getTextFrom(LeadNameuixpath);
			Assert.assertEquals(LeadNameui, LeadName);//Verify lead names match as per API
			if (i != 0 && i % 3 == 0) {
				scrollClientList(80, 30);
			}
		}
	}

	// Created by Priyanka to Verify last visited date value from
	// API response matches with corresponding client last visited date field in
	// the Application
	@Override
	public void verifyLastVisitedDate(Map<String, Object> data) {

		setRequestHeader(data);
		String contactURL = String.valueOf(data.get("contactsUrl"));
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		scrollClientList(30, 70);
		scrollClientList(30, 70);
		library.wait(8);
		for (int i = 0; i < arrayCount; i++) {
			int j = i + 1;
			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;
			String lastVisitedDateTime = (String) library
					.getValueFromJson("$.clientList[" + i + "].contactInfo.lastVisitedDateTime", response);
			System.out.println(lastVisitedDateTime);
			if (lastVisitedDateTime == "null") {
				String lastVisitedDate = new String(lastVisitedDateTime.substring(0, 10));
				System.out.println(lastVisitedDate);
				String uilastVisitedDatexpath = "xpath->//UIATableCell[@name='" + LeadName + "']/UIAStaticText[3]";
				String uilastVisitedDate = library.getTextFrom(uilastVisitedDatexpath);
				// Assert.assertEquals(uilastVisitedDate, lastVisitedDate);
				Assert.assertTrue(uilastVisitedDate.contentEquals("-"), "Condition is False");//Verify last visited date value from API response matches with corresponding client last visited date field in the Application
			}
			if (lastVisitedDateTime != null) {
				String lastVisitedDate = new String(lastVisitedDateTime.substring(0, 10));
				String uilastVisitedDatexpath = "xpath->//UIATableCell[@name='" + LeadName + "']/UIAStaticText[3]";
				String uilastVisitedDate = library.getTextFrom(uilastVisitedDatexpath);
				Assert.assertEquals(uilastVisitedDate, lastVisitedDate);//Verify last visited date value from API response matches with corresponding client last visited date field in the Application
				System.out.println(lastVisitedDate);

			}
			if (j != 1 && j % 4 == 0) {
				scrollClientList(88, 30);
			}
		}

	}

	// Created by Priyanka to Stage last updated date exists
	@Override
	public void verifyStageLastUpdatedDate(Map<String, Object> data) {
		library.wait(5);
		setRequestHeader(data);
		String contactURL = String.valueOf(data.get("contactsUrl"));
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		scrollClientList(30, 70);
		scrollClientList(30, 70);
		library.wait(8);
		for (int i = 0; i < arrayCount; i++) {
			int j = i + 1;
			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;
			String StageLastUpdatedDateTime = String.valueOf(
					library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastActivityDateTime", response));
			// String StagereferralDateTime = String.valueOf(
			// library.getValueFromJson("$.clientList[" + i +
			// "].contactInfo.referralDateTime", response));
			// if (StageLastUpdatedDateTime == "null") {
			// String StagereferralDate = new
			// String(StagereferralDateTime.substring(0, 10));
			// System.out.println(StagereferralDateTime);
			// String uiStageUpdateDatexpath = "xpath->//UIATableCell[@name='" +
			// LeadName + "']/UIAStaticText[6]";
			// String uiStageUpdateDate =
			// library.getTextFrom(uiStageUpdateDatexpath);
			// Assert.assertEquals(uiStageUpdateDate, StagereferralDate);
			// }
			// if (StageLastUpdatedDateTime != "null") {
			String StageLastUpdatedDate = new String(StageLastUpdatedDateTime.substring(0, 10));
			System.out.println(StageLastUpdatedDate);
			String uiStageUpdateDatexpath = "xpath->//UIATableCell[@name='" + LeadName + "']/UIAStaticText[6]";
			String uiStageUpdateDate = library.getTextFrom(uiStageUpdateDatexpath);
			Assert.assertEquals(uiStageUpdateDate, StageLastUpdatedDate);
			// }
			if (j != 1 && j % 4 == 0) {
				scrollClientList(88, 30);
			}
		}
	}

	// Created by Priyanka to verify select sort order

	@Override
	public void verfyselectSortorder(Map<String, Object> data) {
		Map<String, Object> orderData = (Map<String, Object>) data.get("orderData");
		Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
		// String dropdown = "xpath->//button[@ng-model='selectedSort']";
		library.click("xpath->//UIAApplication[1]/UIAWindow[1]/UIAStaticText[1]");
		String order = String.valueOf(orderData.get("Order"));
		String sortOrder = "xpath->//UIAStaticText[@name='" + order + "']";
		library.getTextFrom(sortOrder);
		library.click(sortOrder);
		library.wait(2);
		Map<String, Object> accessData = getAccessTokenIds(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(apiData.get("ContentType"));
		String SortURL = String.valueOf(orderData.get("SortURL"));
		// Integer variable = Integer.valueOf((String)
		// orderData.get("variable"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);
		String response = library.HTTPGet(SortURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		scrollClientList(30, 55);
		for (int i = 0; i < arrayCount; i++) {
			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;
			System.out.println(LeadName);
			String LeadNameuixpath = "xpath->//UIATableCell[@name='" + LeadName + "']/UIAStaticText[1]";
			String LeadNameui = library.getTextFrom(LeadNameuixpath);
			Assert.assertEquals(LeadNameui, LeadName);
			if (i != 0 && i % 3 == 0) {
				scrollClientList(80, 30);
			}
		}

	}

	// Created by Priyanka to Verify email value from API response
	// matches with corresponding client email field in the Application

	public void verifyEmailFieldName(Map<String, Object> data) {
		library.wait(9);
		setRequestHeader(data);
		String contactURL = String.valueOf(data.get("contactsUrl"));
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		scrollClientList(30, 70);
		scrollClientList(30, 50);
		library.wait(8);
		for (int i = 0; i < arrayCount; i++) {
			int j = i + 1;
			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;
			String email = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.email", response);
			System.out.println(email);
			library.click("xpath->//UIATableCell[@name='" + LeadName + "']/UIAStaticText[1]");
			library.wait(25);
			library.waitForElement("CLIENTDETAILPAGE.contactinfo");
			library.wait(5);
			library.click("CLIENTDETAILPAGE.contactinfo");
			library.wait(8);
			Assert.assertTrue(library.verifyPageContainsElement("name->" + email));//Verify email value from API response matches with corresponding client email field
			library.click("CLIENT.backbutton");
			scrollClientList(30, 50);
			library.wait(5);
			if (j != 1 && j % 4 == 0) {
				scrollClientList(88, 30);

			}
		}

	}

	// Created by Priyanka to Verify primary phone value from API
	// response matches with corresponding client primary phone field in the
	// Application

	public void verifyPrimaryPhoneField(Map<String, Object> data) {
		library.wait(5);
		setRequestHeader(data);
		String contactURL = String.valueOf(data.get("contactsUrl"));
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		scrollClientList(30, 70);
		scrollClientList(30, 50);
		library.wait(8);
		for (int i = 0; i < arrayCount; i++) {
			int j = i + 1;
			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;
			String PrimaryPhone = (String) library
					.getValueFromJson("$.clientList[" + i + "].contactInfo.phone[0].phonenumber", response);
			System.out.println(PrimaryPhone);
			library.click("xpath->//UIATableCell[@name='" + LeadName + "']/UIAStaticText[1]");
			library.wait(25);
			library.waitForElement("CLIENTDETAILPAGE.contactinfo");
			library.wait(5);
			library.click("CLIENTDETAILPAGE.contactinfo");
			library.wait(8);
			Assert.assertTrue(library.verifyPageContainsElement("name->" + PrimaryPhone));//Verify primary phone value from API response matches with corresponding client primary phone field in the Application
			library.click("CLIENT.backbutton");
			scrollClientList(30, 50);
			library.wait(5);
			if (j != 1 && j % 4 == 0) {
				scrollClientList(88, 30);
			}
		}
	}

	// Created by Priyanka to Verify client stage matches with
	// opportunity stage from API response
	@Override
	public void verifyStageName(Map<String, Object> data) {
		library.wait(5);
		setRequestHeader(data);
		String contactURL = String.valueOf(data.get("contactsUrl"));
		String response = library.HTTPGet(contactURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
		scrollClientList(30, 70);
		scrollClientList(30, 70);
		library.wait(8);
		for (int i = 0; i < arrayCount; i++) {
			int j = i + 1;
			String firstName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName",
					response);
			String lastName = (String) library.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName",
					response);
			String LeadName = firstName + " " + lastName;
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
			System.out.println(opportunityStage);
			library.wait(2);
			String OpportunityStageuixpath = "xpath->//UIATableCell[@name='" + LeadName + "']/UIAStaticText[5]";
			String OpportunityStageui = library.getTextFrom(OpportunityStageuixpath);
			Assert.assertEquals(OpportunityStageui, opportunityStage);//Verify client stage matches with opportunity stage from API response
			//Verify oppurtunity stage exists in the API response
			if (j != 1 && j % 4 == 0) {
				scrollClientList(88, 30);
			}
		}
	}

	@Override
	public void verifySendEmail() {

	}

	// @Override
	// public void goToTransactionDetails() {
	// library.click("TRANSACTION.transactionlink");
	// }
	@Override
	public void updateUrgencyStageOne(Map<String, String> data) {

		String urgencyCode = data.get("urgencyCode");
		String urgencyName = data.get("urgencyName");
		String clientName = data.get("ClientName");
		String searchName = clientName.substring(0, 3);
		// openClientDetailPage(clientName);
		searchAndSelectClient(clientName);

		library.click("URGENCY.image"); // click on urgency
		// String updateLocator =
		// "xpath->//UIAApplication[1]/UIAWindow[1]/UIAPicker[1]/UIAPickerWheel[1])).sendKeys('"+urgencyName+"')";
		//// UIAApplication[1]/UIAWindow[1]/UIAPicker[1]/UIAPickerWheel[1])).sendKeys('New
		// Referral')
		// UIAApplication[1]/UIAWindow[1]/UIAPicker[1]/UIAPickerWheel[1]")).sendKeys("6-12
		// months");
		// library.typeDataInto("//UIAApplication[1]/UIAWindow[1]/UIAPicker[1]/UIAPickerWheel[1]",
		// "6-12 months");
		library.typeDataInto(urgencyName, "xpath->//UIAApplication[1]/UIAWindow[1]/UIAPicker[1]/UIAPickerWheel[1]");
		// library.click("//UIAApplication[1]/UIAWindow[1]/UIAPicker[1]/UIAPickerWheel[1]\")).sendKeys('6-12
		// months')");
		library.wait(3);
		library.isElementEnabled("URGENCY.apply", true); // verify Apply button
		library.click("URGENCY.apply");// click on apply button
		library.waitForElement("URGENCY.notes");
		library.wait(6);
		library.click("URGENCY.notes"); // click on Notes
		library.wait(6);

	}
	
	@Override
	 public void updateUrgencyStageTwo(Map<String, String> data) {

	  String urgencyCode = data.get("urgencyCode");
	  String urgencyName = data.get("urgencyName");
	  String clientName = data.get("ClientName");

	  String urgencyCodeLocator = "xpath->//UIAApplication[1]/UIAWindow[1]/UIAScrollView[1]/UIATableView[1]/UIATableCell[1]/UIAButton[1]";
	  library.setImplicitWaitTime(5);
	  // library.verifyPageContainsElement(urgencyCodeLocator, true); //
	  // incorrect urgency image

	  String noteUpdateLocator = getUrgencyNotesLocator(urgencyName);
	  library.verifyPageContainsElement(noteUpdateLocator); // Correct
	  library.setImplicitWaitTime(30);
	  // library.click("CLOSE.notes");
	  library.wait(3);
	  closeNotesField();
	  navigateToClientListPage();

	 }
	
	@Override
	public void navigateToFutureLeavePage() {
		// library.wait(2);
		openMenu();

		library.click("MENU.goonleavebutton");
		library.click("MENU.futureleavebutton");
	}

	@Override
	public void navigateToFutureLeavePages() {
		// library.wait(2);
		openMenu();
		library.click("MENU.goonleavebutton");
		// library.click("MENU.goonleavebutton");
		library.click("MENU.futureleavebutton");
	}

	// Updated By Puneet dated : - 23-may-16
	@Override
	protected String getUrgencyNotesLocator(String urgencyName) {
		String getNotesLocator = "xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[1]/UIAStaticText[2]";
		String urgencyText = library.getTextFrom(getNotesLocator);
		urgencyText = urgencyText.split(":")[1];
		urgencyText = urgencyText.trim();
		// return "xpath->//android.widget.TextView[contains(@text,'" +
		// urgencyName + "')]";
		// return urgencyText;

		return "xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[1]/UIAStaticText[2][contains(@name,'"
				+ urgencyText + "')]";
	}

	@Override
	protected String getLocatorForUrgencyCode(String urgencyCode) {

		return "xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[1]/UIAStaticText[2][contains(@name,'"
				+ urgencyCode + "')]";
	}

	@Override
	public String getLocatorForTransactionType(String transactionType) {

		String updateTransactionLocator = "xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[1]/UIAStaticText[1][contains(@name,'"
				+ transactionType + "')]";

		// UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[1]/UIAStaticText[1][contains(@name,'Add
		// a Note')]
		// String
		// updateTransactionLocatorIos=library.getTextFrom(updateTransactionLocator);
		return updateTransactionLocator;

		// TODO Auto-generated method stub
		// return "name->" + transactionType;
	}

	@Override
	public void verifyNotesResponseWithApi(Map<String, Object> data) {
		// TODO Auto-generated method stub
		// String noteDiscription = library.getTextFrom("NOTES.notePath");
		// System.out.println("\n" +noteDiscription+"\n");
		String response = getResponse((String) data.get("notesApi"));
		String noteDiscriptionofApiResponse = (String) library.getValueFromJson("$.activities[0].note", response);
		System.out.println("\n" + noteDiscriptionofApiResponse + "\n");
		String noteDetailPath = "xpath->(//UIAStaticText[contains(@name,'" + noteDiscriptionofApiResponse + "')])[1]";
		Assert.assertTrue(library.verifyPageContainsElement(noteDetailPath));
		library.click("NOTES.cancelbutton");
		// Assert.assertTrue(library.verifyPageContainsElement("xpath->.//p[contains(text(),'"
		// +noteDiscription2+ "')]", true));

	}

	@Override
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
		String pathOfAddress = "xpath->(//UIAStaticText[contains(@name,'" + addressOne + "')])[1]";

		boolean isAddressMatched = library.verifyPageContainsElement(pathOfAddress);

		Assert.assertTrue(isAddressMatched);

	}

	@Override
	public int goToLeavePageCountLeave() {

		library.waitForElement("FLEAVE.createbutton");
		library.wait(15);
		library.wait(15);
		library.wait(10);
		navigateToFutureLeavePage();
		ArrayList<Integer> count = new ArrayList<Integer>();
		int leaveCounttotal = 0;
		for (int i = 1; i <= count.size(); i++) {
			String xpath = "xpath->//UIATableView[2]/UIATableCell[" + i + "]/UIAButton[1]";

			int leaveCount = library.getElementCount(xpath);
			count.add(leaveCount);
			leaveCounttotal = leaveCounttotal + leaveCount;

		}
		return leaveCounttotal;
	}

	public int goToLeavePageCountLeaves() {

		navigateToFutureLeavePages();
		ArrayList<Integer> count = new ArrayList<Integer>();
		int leaveCounttotal = 0;
		for (int i = 1; i <= count.size(); i++) {
			String xpath = "xpath->//UIATableView[2]/UIATableCell[" + i + "]/UIAButton[1]";

			int leaveCount = library.getElementCount(xpath);
			count.add(leaveCount);
			leaveCounttotal = leaveCounttotal + leaveCount;

		}
		return leaveCounttotal;

	}

	@Override
	public void verifyLeavesWithApiResponse(String response, int leaveCount) {

		for (int i = 0; i < leaveCount; i++) {
			int j = i + 1;
			String startDate = (String) library.getValueFromJson("$.Leaves[" + i + "].startDateTime", response);
			String endDate = (String) library.getValueFromJson("$.Leaves[" + i + "].endDateTime", response);

			String[] dateElement = startDate.split(" ");
			String fromDateFromApi = dateElement[0];

			String[] dateElementt = endDate.split(" ");
			String toDateFromApi = dateElementt[0];

			String startDateui = library.getTextFrom(
					"xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[" + j + "]/UIAStaticText[1]");
			String endDateui = library.getTextFrom(
					"xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[" + j + "]/UIAStaticText[2]");

			String uiDate = startDateui + " - " + endDateui;

			String[] splitstart = uiDate.split(" ");
			String dateFromm = splitstart[1];

			String[] splitend = uiDate.split(" ");
			String dateToo = splitend[4];

			Assert.assertTrue(fromDateFromApi.equals(dateFromm));
			Assert.assertTrue(toDateFromApi.equals(dateToo));

		}

	}

	@Override
	public void deleteLeaves(int leavePosition) {
		library.click("FLEAVE.cancelleavebutton");
		library.wait(3);
		library.click("FLEAVE.deleteleavebutton");
		library.wait(2);

	}

	// created by Priyanka:-06/24/2016
	@Override
	public void goToNotificationPage() {
		library.click("xpath->//UIANavigationBar[1]/UIAButton[1]");
		library.click("xpath->//UIATableCell[@name='Notifications']/UIAStaticText[1]");
		library.wait(5);
	}

	// Created by Priyanka :- 06/23/2016
	@Override
	public void verifyFC1notification() {
		library.wait(5);
		String fc1NotificationMessgeLine1 = "Update Now";
		// String fc1NotificationMessgeLine2 = "Please update Test Client.";
		String fc1NotificationMessgeLine1xpath = "xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[3]/UIAStaticText[1]";

		// String fc1NotificationMessgeLine2xpath =
		// "xpath->////UIATableView[2]/UIAScrollView[1]/UIATableCell[1]/UIAStaticText[2]";
		String fc1NotificationMessgeLine1text = library.getTextFrom(fc1NotificationMessgeLine1xpath);
		// String fc1NotificationMessgeLine2text =
		// library.getTextFrom(fc1NotificationMessgeLine2xpath);
		Assert.assertEquals(fc1NotificationMessgeLine1, fc1NotificationMessgeLine1text);
		// Assert.assertEquals(fc1NotificationMessgeLine2,
		// fc1NotificationMessgeLine2text);
	}

	// Created by Priyanka :- 06/23/2016
	@Override
	public void verifyFC2notification() {
		String fc2NotificationMessgeLine1 = "Update Now Reminder (45min)";
		// String fc2NotificationMessgeLine2 = "Please update Test Client.";
		String fc2NotificationMessgeLine1xpath = "xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[2]/UIAStaticText[1]";
		// String fc2NotificationMessgeLine2xpath =
		// "xpath->//UIATableView[2]/UIAScrollView[1]/UIATableCell[2]/UIAStaticText[2]";
		String fc2NotificationMessgeLine1text = library.getTextFrom(fc2NotificationMessgeLine1xpath);
		// String fc2NotificationMessgeLine2text =
		// library.getTextFrom(fc2NotificationMessgeLine2xpath);
		Assert.assertEquals(fc2NotificationMessgeLine1, fc2NotificationMessgeLine1text);
		// Assert.assertEquals(fc2NotificationMessgeLine2,
		// fc2NotificationMessgeLine2text);
	}

	// Created by Priyanka :- 06/23/2016
	@Override
	public void verifyFC3notification() {
		String fc3NotificationMessgeLine1 = "Update Now Reminder (1.5hr)";
		// String fc3NotificationMessgeLine2 = "LAST REMINDER : Update Test
		// Client.";
		String fc3NotificationMessgeLine1xpath = "xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[1]/UIAStaticText[1]";
		// String fc3NotificationMessgeLine2xpath =
		// "xpath->//UIATableView[2]/UIAScrollView[1]/UIATableCell[3]/UIAStaticText[2]";
		String fc3NotificationMessgeLine1text = library.getTextFrom(fc3NotificationMessgeLine1xpath);
		// String fc3NotificationMessgeLine2text =
		// library.getTextFrom(fc3NotificationMessgeLine2xpath);
		Assert.assertEquals(fc3NotificationMessgeLine1, fc3NotificationMessgeLine1text);
		// Assert.assertEquals(fc3NotificationMessgeLine2,
		// fc3NotificationMessgeLine2text);
	}

	// Created by Priyanka :- 06/23/2016
	@Override
	public void verifyN1notification() {
		library.wait(5);
		String N1NotificationMessgeLine1 = "Call Now";
		// String fc1NotificationMessgeLine2 = "Please update Test Client.";
		String N1NotificationMessgeLine1xpath = "xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[3]/UIAStaticText[1]";
		// String fc1NotificationMessgeLine2xpath =
		// "xpath->////UIATableView[2]/UIAScrollView[1]/UIATableCell[1]/UIAStaticText[2]";
		String N1NotificationMessgeLine1text = library.getTextFrom(N1NotificationMessgeLine1xpath);
		// String fc1NotificationMessgeLine2text =
		// library.getTextFrom(fc1NotificationMessgeLine2xpath);
		Assert.assertEquals(N1NotificationMessgeLine1, N1NotificationMessgeLine1text);
		// Assert.assertEquals(fc1NotificationMessgeLine2,
		// fc1NotificationMessgeLine2text);
	}

	// Created by Priyanka :- 06/23/2016
	@Override
	public void verifyN2notification() {
		String N2NotificationMessgeLine1 = "Call Now Reminder (45min)";
		// String fc2NotificationMessgeLine2 = "Please update Test Client.";
		String N2NotificationMessgeLine1xpath = "xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[2]/UIAStaticText[1]";
		// String fc2NotificationMessgeLine2xpath =
		// "xpath->//UIATableView[2]/UIAScrollView[1]/UIATableCell[2]/UIAStaticText[2]";
		String N2NotificationMessgeLine1text = library.getTextFrom(N2NotificationMessgeLine1xpath);
		// String fc2NotificationMessgeLine2text =
		// library.getTextFrom(fc2NotificationMessgeLine2xpath);
		Assert.assertEquals(N2NotificationMessgeLine1, N2NotificationMessgeLine1text);
		// Assert.assertEquals(fc2NotificationMessgeLine2,
		// fc2NotificationMessgeLine2text);
	}

	// Created by Priyanka :- 06/23/2016
	@Override
	public void verifyN3notification() {
		String N3NotificationMessgeLine1 = "Call Now Reminder (1.5hr)";
		// String fc3NotificationMessgeLine2 = "LAST REMINDER : Update Test
		// Client.";
		String N3NotificationMessgeLine1xpath = "xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[1]/UIAStaticText[1]";
		// String fc3NotificationMessgeLine2xpath =
		// "xpath->//UIATableView[2]/UIAScrollView[1]/UIATableCell[3]/UIAStaticText[2]";
		String N3NotificationMessgeLine1text = library.getTextFrom(N3NotificationMessgeLine1xpath);
		// String fc3NotificationMessgeLine2text =
		// library.getTextFrom(fc3NotificationMessgeLine2xpath);
		Assert.assertEquals(N3NotificationMessgeLine1, N3NotificationMessgeLine1text);
		// Assert.assertEquals(fc3NotificationMessgeLine2,
		// fc3NotificationMessgeLine2text);
	}

	// Created by Akash
	@Override
	public void verifyImmediatelyLeaveSteps() {
		boolean exists = library.verifyPageContainsElement("name->Cancel");
		if (exists == true) {
			library.click("name->Cancel");
			library.wait(3);
		}

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

	// @Override
	// public void setFutureDateUsingCalander(Map<String, Object> data){
	// openMenu();
	// if(library.verifyPageContainsElement("MENU.returnNow", true))
	// library.click("MENU.returnNow");
	//
	// library.wait(2);
	// library.click("MENU.futureleavebutton");
	//
	// int numberOfLeaves =
	// library.getElementCount("FLEAVE.cancelleavebuttonCount");
	//
	// for(int i = 1; i<=numberOfLeaves; i++){
	// library.click("");
	// library.wait(2);
	//
	// }
	//
	//
	//
	// }

	@Override
	public void verifyActivityListSages() {
		library.wait(6);
		library.click("TRANSACTION.updatebutton");// Update button is enabled on
													// the client detail page
		library.wait(5);
		// Verify following activities are enabled for updates:Talked, Emailed,
		// Left Voicemail, Schedule a Callback, Schedule a Meeting, Met in
		// Person, Made an Offer, Add a Note, Return to Movoto
		Assert.assertTrue(library.verifyPageContainsElement("xpath->//UIAPickerWheel[contains(@value,'Talked')]"),
				"Element Not found");
		library.typeDataInto("Emailed", "UPDATE.list");

		Assert.assertTrue(
				library.verifyPageContainsElement("xpath->//UIAPickerWheel[contains(@value,'Emailed')]"),
				"Element Not found");
		library.typeDataInto("Left Voicemail", "UPDATE.list");
		Assert.assertTrue(
				library.verifyPageContainsElement("xpath->//UIAPickerWheel[contains(@value,'Left Voicemail')]"),
				"Element Not found");
		library.typeDataInto("Scheduled a Callback", "UPDATE.list");
		Assert.assertTrue(library.verifyPageContainsElement(
				"xpath->//UIAPickerWheel[contains(@value,'Scheduled a Callback')]"), "Element Not found");
		library.typeDataInto("Scheduled a Meeting", "UPDATE.list");
		Assert.assertTrue(library.verifyPageContainsElement(
				"xpath->//UIAPickerWheel[contains(@value,'Scheduled a Meeting')]"), "Element Not found");
		library.typeDataInto("Met in Person", "UPDATE.list");
		Assert.assertTrue(
				library.verifyPageContainsElement("xpath->//UIAPickerWheel[contains(@value,'Met in Person')]"),
				"Element Not found");
		library.typeDataInto("Made an Offer", "UPDATE.list");
		Assert.assertTrue(
				library.verifyPageContainsElement("xpath->//UIAPickerWheel[contains(@value,'Made an Offer')]"),
				"Element Not found");
		library.typeDataInto("Add a Note", "UPDATE.list");
		Assert.assertTrue(
				library.verifyPageContainsElement("xpath->//UIAPickerWheel[contains(@value,'Add a Note')]"),
				"Element Not found");
		library.typeDataInto("Return to Movoto", "UPDATE.list");
		Assert.assertTrue(
				library.verifyPageContainsElement("xpath->//UIAPickerWheel[contains(@value,'Return to Movoto')]"),
				"Element Not found");

	}

		@Override
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
	  // String webUiLocatorOne="URGENCY.image"; URGENCY.image
	  String webUiLocatorOne = library.getTextFrom("URGENCY.image");
	  // Assert.assertEquals(getOne, webUiLocatorOne);// Verify urgency code
	  // is updated to 1 in the urgency spot

	  String last = "Updated Urgency Status : " + urgency;
	  String webUiLocator = "xpath->(//UIATableView)[2]/UIATableCell[1]/UIAStaticText[2]";
	  webUiLocator = library.getTextFrom(webUiLocator);
	  Assert.assertEquals(webUiLocator, last);// Note is added as "Updated
	            // Urgency Status: 1 - Ready
	            // Now"

	  String notesTimeXpath = "xpath->(//UIATableView)[2]/UIATableCell[1]/UIAStaticText[1]";
	  String getTime = library.getTextFrom(notesTimeXpath);
	  String[] SplitGetTime = getTime.split(" ");
	  String GetDateAndTime = SplitGetTime[2] + " " + SplitGetTime[3] + " " + SplitGetTime[4];

	  String FinalValue = null;

	  // Converting time into UTC from IST and validating.
	  try {
	   DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	   formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	   Date date = formatterIST.parse(GetDateAndTime);

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
	  Assert.assertEquals(finalTime, FinalValue);// Verify the note time and date is
	             // the same as the time of
	             // the urgency updated time
	  } catch (Exception e) {
		   e.printStackTrace();
		  }

	 }
	
	
	// Created by puneet
	@Override
	public void loginVerification(Map<String, String> data) {
		openMenu();
		String agentName = data.get("Username");
		String xpathForAgentName = "xpath->//UIAStaticText[contains(@name,'Indecomm Agent Test AR')]";
		String getAgentNamefromXpath = library.getTextFrom(xpathForAgentName);
		String[] SplitAgentName = agentName.split("@");
		String Name = SplitAgentName[0];
		String xpathValue = getAgentNamefromXpath.replaceAll("\\s+", "");
		System.out.println(xpathValue);
		String loginValue = Name.replaceAll("\\s+", "");
		System.out.println(loginValue);
		boolean isMatched = xpathValue.equalsIgnoreCase(loginValue);// Verify
																	// agent is
																	// logged in
																	// successfully
																	// via
																	// checking
																	// the agent
																	// name on
																	// the menu
																	// is the
																	// same as
																	// the
																	// logged in
																	// agent's
																	// full
																	// name.
		library.wait(2);
		closeMenu();

	}

	@Override
	public void verificationupdateUrgencyStageOne(Map<String, String> data) {

		String urgencyCode = data.get("urgencyCode");
		String urgencyName = data.get("urgencyName");
		String clientName = data.get("ClientName");
		String searchName = clientName.substring(0, 3);
		// openClientDetailPage(clientName);
		searchAndSelectClient(clientName);

		String VerifyTransaction = "xpath->//UIAButton[contains(@name,'Transactions(1)')]";
		boolean got = library.verifyPageContainsElement(VerifyTransaction);

		// String verify="Talked";
		String uiTalked = "xpath->//UIAStaticText[contains(@name,'Talked')]";
		boolean gotTalked = library.verifyPageContainsElement(uiTalked);
		// String getTalked = library.getTextFrom(uiTalked);
		// System.out.println(getTalked);
		// Assert.assertTrue(verify.equals(getTalked));//Lead is in Talked stage

		library.click("URGENCY.image"); // click on urgency

		library.typeDataInto(urgencyName, "xpath->//UIAApplication[1]/UIAWindow[1]/UIAPicker[1]/UIAPickerWheel[1]");
		library.wait(3);
		library.isElementEnabled("URGENCY.apply", true); // verify Apply button
		library.click("URGENCY.apply");// click on apply button
		library.wait(3);
		library.verifyPageContainsElement("URGENCY.spotno");//To verify the number on the urgency spot
		library.waitForElement("URGENCY.notes");
		library.wait(6);
		library.click("URGENCY.notes"); // click on Notes
		library.wait(6);

	}

	@Override
	public void updateToScheduledACallbackForMetStage(String stage, String client, Map<String, Object> data) {
		searchAndSelectClient(client);
		  library.wait(5);
		     library.click("CLIENTLIST.Transaction");
		  String metStageLocator = library.getTextFrom("UPDATEPAGE.metstagelocator").split(" ")[0];
		  String metStageLocatorfromUI = metStageLocator.trim();
		  Assert.assertEquals("Met", metStageLocatorfromUI);
		  library.wait(3);
		  library.click("TRANSACTION.updatebutton");
		  chooseTransactionStage(stage);
		  library.wait(3);
		  Assert.assertTrue(library.verifyPageContainsElement("UPDATE.applybutton"));
		  library.click("UPDATE.applybutton");
		  library.wait(2);
		  Assert.assertTrue(library.verifyPageContainsElement("SC.setreminderbutton"));// Schedule
		                       // a
		                       // Reminder
		                       // page
		                       // is
		                       // opened,
		                       // Schedule
		                       // a
		                       // Reminder
		                       // is
		                       // displayed
		                       // on
		                       // the
		                       // page
		  Assert.assertTrue(library.verifyPageContainsElement("SC.schedulereminderpage"));
		  String getClientName = library.getTextFrom("SC.clientname");
		  System.out.println(getClientName);

		  Assert.assertEquals(client, getClientName);// Client Name is correct
		             // with the updated client

		  String timeInHourFromUI = library.getTextFrom("UPDATEPAGE.timeinhour");

		  String[] SplitGetTime = timeInHourFromUI.split(" | ");

		  String UIDate = ((SplitGetTime[0])); // dates as
		  String SplitDate = UIDate.replaceAll("\\.", " ");
		  Calendar cal = Calendar.getInstance();
		  SplitDate = SplitDate + " " + 00 + ":" + 00 + ":" + 00 + " " + "CST" + " " + cal.get(Calendar.YEAR);
		  String formatedDate = null;
		  String finalValue="";
		  try {
		   java.util.Date fecha = new java.util.Date(SplitDate);
		   DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
		   Date date;
		   date = (Date) formatter.parse(fecha.toString());
		   

		   
		   cal.setTime(date);
		   formatedDate = cal.get(Calendar.MONTH) + 1 + "/" + (cal.get(Calendar.DATE)) + "/" + cal.get(Calendar.YEAR);
		   String d=formatedDate;
		   String format="dd/mm/yyyy";
		       SimpleDateFormat sdf=new SimpleDateFormat(format);
		       date=sdf.parse(d);
		       finalValue =sdf.format(date);
		      //System.out.println(LocalDate.parse(formatedDate,parser));
		   System.out.println("formatedDate : " + finalValue);
		  } catch (Exception e) {
		   e.printStackTrace();
		  }

		  // String SeperateDate=SplitDate[0]+"/"+SplitDate[1]+"/"+SplitDate[2];

		  String[] b = SplitGetTime[2].split(":");
		  String uiTimeInHour = b[0];
		  String uiTimeInMin = b[1];

		  System.out.println(timeInHourFromUI);

		  // String[] time = timeInHourFromUI.split(":");
		  // String hourTime = time[0];
		  // System.out.println(hourTime);

		  // String minTimeWithAMPM = time[1];
		  // String[] minTimeArray = minTimeWithAMPM.split(" ");
		  // String minTime = minTimeArray[0];
		  // System.out.println(minTime);

		  int intTimeInHourFromUI = Integer.parseInt(uiTimeInHour);
		  int intTimeInMinFromUI = Integer.parseInt(uiTimeInMin);
		  // int intTimeInHourFromUI = Integer.parseInt(timeInHourFromUI);

		  System.out.println(metStageLocator);
		  System.out.println(timeInHourFromUI);

		  //Calendar cal = Calendar.getInstance();
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
		  Assert.assertEquals(finalValue, localSystemDate);// the default Date &
		               // Time is 10 min
		               // after the current
		               // time

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

		  library.wait(5);
	}
	@Override
	public void openNotes() {
		library.wait(8);
		library.click("CLIENTDETAIL.notesbutton");
		library.wait(10);

	}

	@Override

	public void verifyContractCancelStageTwo(Map<String, Object> data) {

		String clientName = String.valueOf(data.get("clientName"));
		String postStage = String.valueOf(data.get("postStage"));

		closeNotesField();
		navigateToClientListPage();
		waitWhileLoading();
		// verifyTransactionStageForClientWith(clientName, postStage);

	}

	// Created by puneet
	public void verifyNotesTime(Map<String, Object> data) {

		String xpathForDateAndTimeFromNotesField = library.getTextFrom("NOTES.updatedtime");
		String[] Mettime = xpathForDateAndTimeFromNotesField.split(" ");
		String MettimeSplit = Mettime[3] + " " + Mettime[4] + " " + Mettime[5];
		String FinalValue = null;

		try {
			DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
			formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			Date date = formatterIST.parse(MettimeSplit);

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
			Assert.assertEquals(finalTime, FinalValue);// Verify Notes is
																// displayed as
																// followings:Scheduled
																// a Callback
																// <activity
																// updated time
																// stamp>Callback
																// Time:
																// <scheduled
																// date and
																// time>
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

		@Override
	public String getPrestage() {

		String prestageText = library.getTextFrom("UPDATEPAGE.metstagelocator");
		return prestageText;

	}

	@Override
	public String getSysDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		String returndate = (dateFormat.format(date));
		return returndate;

	}



	public String getSysDates() {
		DateFormat dateFormat = new SimpleDateFormat("MM/d/yyyy");
		Date date = new Date();
		String returndate = (dateFormat.format(date));
		return returndate;
	}

	@Override
	public void openProperties() {
		library.wait(3);
		library.click("TRANSACTION.properties");
		library.wait(3);
	}

	

	public void verifyPropertyDetailsForContractCancel() {

		library.verifyPageNotContainsElement("CONTRACTCANCEL.escrowinfo");
		library.click("NOTES.cancelbutton");
	}

	public void navigateToClientListPage() {
		library.wait(3);
		Point start = getScreenCoordinatesForPercentInXAxis(5);
		Point end = getScreenCoordinatesForPercentInXAxis(80);
		library.swipeFromTo(start.x, start.y, end.x, end.y);
		// library.click("HOMEPAGE.hamburger");
		library.click("name->Client List");
	}

	
	@Override
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
			String totalLeadCount = library.getTextFrom("CLIENTLIST.ClientnoMsg");
			System.out.println(totalLeadCount);
			String[] splittotalLeadCount = totalLeadCount.split(" ");
			String UitotalLeadCount = splittotalLeadCount[3];
			int finalUiValue = Integer.parseInt(UitotalLeadCount);
			System.out.println(finalUiValue);
			Assert.assertEquals(finalUiValue, arrayCount);//verify leads count

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void verifyClientFilterWithMet(Map<String, Object> data) {
		library.wait(5);
		Map<String, Object> orderData = (Map<String, Object>) data.get("orderData");
		Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
		// String dropdown = "xpath->//button[@ng-model='selectedSort']";
		library.click("CLIENTLIST.newestreferred");
		library.wait(5);
		library.click("xpath->//UIAStaticText[contains(@name,'Met')]");
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
		System.out.println("Filtered Count is " + arrayCount);
		String totalLeadCount = library
				.getTextFrom("CLIENTLIST.Clientwithmet");
		System.out.println(totalLeadCount);
		String[] splittotalLeadCount = totalLeadCount.split(" ");
		String UitotalLeadCount = splittotalLeadCount[0];
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
			library.wait(5);
			Assert.assertTrue(library.verifyPageContainsElement("name->" + LeadName));//Verify the client names from API response with corresponding filter result
			if (j != 1 && j % 4 == 0) {
				scrollClientList(88, 30);
			}
		}

	}

	@Override
	public void clientListSortByFirstName(Map<String, Object> data) {
		library.wait(5);
		Map<String, Object> orderData = (Map<String, Object>) data.get("orderData");
		Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
		// String dropdown = "xpath->//button[@ng-model='selectedSort']";
		library.click("CLIENTLIST.newestreferred");
		library.wait(5);
		library.click("CLIENTLIST.sortorder");
		library.wait(5);
		library.click("CLIENTLIST.firstname");
		library.wait(5);
		/*
		 * String order = String.valueOf(orderData.get("Order")); String
		 * sortOrder = "name->" + order; library.getTextFrom(sortOrder);
		 * library.click(sortOrder); library.wait(2);
		 */
		Map<String, Object> accessData = getAccessTokenIds(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		String contentType = String.valueOf(apiData.get("ContentType"));
		String SortURL = String.valueOf(orderData.get("SortURL"));
		// Integer variable = Integer.valueOf((String)
		// orderData.get("variable"));
		library.setContentType(contentType);
		library.setRequestHeader("x-userid", xuserid);
		library.setRequestHeader("Authorization", "Bearer " + token);
		String response = library.HTTPGet(SortURL);
		int arrayCount = (int) library.getValueFromJson("$.filteredCount", response);
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
			library.wait(5);
			Assert.assertTrue(library.verifyPageContainsElement("name->" + LeadName));
			if (j != 1 && j % 4 == 0) {
				scrollClientList(88, 30);
			}
		}

	}

	@Override
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
		library.click("CLIENTLIST.contactinfo");
		library.wait(3);
		String Uiemail = library.getTextFrom("CLIENTLIST.emailfield");
		System.out.println(Uiemail);
		Assert.assertEquals(Uiemail, email);//Verify email value from API response matches with corresponding client email field
		library.click("CLIENTLIST.emailfield");
		library.wait(5);
		Assert.assertTrue(library.verifyPageContainsElement("CLIENTLIST.subjectfield"));//Verify Email Client page is opened after clicking Email icon
		library.typeDataInto("Test", "CLIENTLIST.subjectfield");
		library.wait(5);
		library.navigateBack();
		library.wait(3);
		library.typeDataInto("Test Automation", "CLIENTLIST.textfield");
		library.wait(5);
		library.navigateBack();
		library.wait(5);
		library.click("CLIENTLIST.selectpreview");
		library.wait(15);
		library.click("CLIENTLIST.selectsend");
		library.wait(20);
		library.click("CLIENTLIST.back");
		library.wait(5);
		Assert.assertTrue(library.verifyPageContainsElement("CLIENTLIST.selectclient"));//Verify the agent is led back to Client List page again.
		library.click("CLIENTLIST.selectclient");
		library.wait(5);
		library.click("CLIENTLIST.Transaction");
		library.wait(5);
		library.click("URGENCY.notes");
		library.wait(15);
		Assert.assertTrue(library.verifyPageContainsElement("CLIENTLIST.selecttext"));//Verify Email event is on top of all the other notes.
		library.wait(5);
}

	@Override
	public void goOnImmediateLeave(Map<String, Object> data) {

		library.wait(5);
		library.click("HOMEPAGE.hamburger");
		library.wait(2);
		library.click("MENU.goonleavebutton");
		library.wait(5);
		WebElement element=library.findElement("MENU.returnnow");
		if(element !=null)
		{
			library.click("MENU.returnnow");
			library.wait(10);
		}
		library.click("MENU.immediatelyleave");
		library.wait(5);
		closeMenu();
		library.click("HOMEPAGE.hamburger");
		library.wait(3);
		library.click("MENU.goonleavebutton");
		library.wait(5);
		Assert.assertTrue(library.verifyPageContainsElement("MENU.returnnow"));//Verify the text changed to "Return Now"
		closeMenu();
		Assert.assertTrue(library.verifyPageContainsElement("MAINMENU.RETURNNOW"));//Verify the blue button "Return Now" appears on top of the Client List page
		library.wait(2);
		setTokenAndUserId(data);
		String response = getResponse((String) data.get("ContactsUrl"));
		int uiValue=(int)library.getValueFromJson("$.Leaves[0].leaveType", response);
		Assert.assertEquals(uiValue, 1);//Verify leave status returned from API response is 1 (Immediately leave)
		library.wait(5);

	}

	@Override
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
		}
		library.click("MENU.returnnow");
		library.wait(5);
		closeMenu();
		library.click("HOMEPAGE.hamburger");
		library.wait(2);
		library.click("MENU.goonleavebutton");
		library.wait(3);
		Assert.assertTrue(library.verifyPageContainsElement("MENU.immediatelyleave"));//Verify the text change to "Immediately"
		closeMenu();
		Assert.assertTrue(library.verifyPageNotContainsElement("MENU.retrunnow"));//Verify the "Return Now" text disappear
		setTokenAndUserId(data);
		String response = getResponse((String) data.get("ContactsUrl"));
		System.out.println(response);
		String UIValue=(String) library.getValueFromJson("$.Leaves[0].leaveType", response);
		Assert.assertEquals(UIValue, null);

	}

	@Override
	public void addFutureLeave(Map<String, Object> data) {
		library.wait(5);
		library.click("HOMEPAGE.hamburger");
		library.wait(2);
		library.click("MENU.goonleavebutton");
		library.wait(5);
		library.click("MENU.futureleavebutton");
		library.wait(5);
		setFutureDateUsingCalanderForFunctional(data);
		int leaveCount = goToLeavePageCountLeave();
		setTokenAndUserId(data);
		String response = getResponse((String) data.get("ContactsUrl"));
		verifyLeavesWithApiResponse(response, leaveCount);
		System.out.println(response);
	}

	@Override
	public void deleteFutureLeave(Map<String, Object> data) {

		library.wait(5);
		library.click("HOMEPAGE.hamburger");
		library.wait(2);
		library.click("MENU.goonleavebutton");
		library.wait(5);
		library.click("MENU.futureleavebutton");
		library.wait(5);
		int getLeaveCountBeforeDeletion = goToLeavePageCountLeaves();
		deleteLeaves(1);
		library.wait(2);
		int leaveCount = goToLeavePageCountLeaves();
		setTokenAndUserId(data);
		String response = getResponse((String) data.get("ContactsUrl"));
		verifyLeavesWithApiResponse(response, leaveCount);

	}

	// Created by Priyanka :- 09/09/2016
	@Override
	public void verifyforFC1notification(Map<String, Object> data) {
		library.click("HOMEPAGE.hamburger");
		library.wait(4);
		String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
		int NotificationCountnum = Integer.parseInt(NotificationCount);
		library.click("MENU.notificationpagelink");
		library.wait(4);
		library.wait(5);
		String fc1NotificationMessgeLine1 = "Update Now";
		String fc1NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
		String fc1NotificationMessgeLine1text = library.getTextFrom(fc1NotificationMessgeLine1xpath);
		Assert.assertEquals(fc1NotificationMessgeLine1, fc1NotificationMessgeLine1text);//// Verify the notification title is "Update Now"
		String Name_Ios = (String) data.get("Name_Ios");
        String fc1NotificationMessgeLine2 = "Please update "+Name_Ios+".";

		String fc1NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
		String fc1NotificationMessgeLine2text = library.getTextFrom(fc1NotificationMessgeLine2xpath);
		Assert.assertEquals(fc1NotificationMessgeLine2, fc1NotificationMessgeLine2text);// Verify the notification body is  "Please update <client name>																
		library.click(fc1NotificationMessgeLine1xpath);
		library.wait(5);
		Assert.assertTrue(library.verifyPageContainsElement("TRANSACTION.updatebutton"));// Verify the agent is lead to client detail page
		library.verifyPageContainsElement("TRANSACTION.updatebutton");// Verify the agent is led to client  detail page);
		String ClientName = library.getTextFrom("NOTIFICATION.ClientName");
		Assert.assertEquals(ClientName, "Test AgentiOS");// Verify client name on client detail page is the same as that in FC1														
		library.click("HOMEPAGE.hamburger");
		library.wait(4);
		String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
		int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
		Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);// Verify the unread notification number is one less
	}

	// Created by Priyanka :- 09/09/2016
	@Override
	public void verifyforFC2notification(Map<String, Object> data) {
		library.click("HOMEPAGE.hamburger");
		library.wait(4);
		String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
		int NotificationCountnum = Integer.parseInt(NotificationCount);
		library.click("MENU.notificationpagelink");
		library.wait(4);
		String fc2NotificationMessgeLine1 = "Update Now Reminder (45min)";
		String fc2NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
		String fc2NotificationMessgeLine1text = library.getTextFrom(fc2NotificationMessgeLine1xpath);
		Assert.assertEquals(fc2NotificationMessgeLine1, fc2NotificationMessgeLine1text);// Verify the notification title is update now reminder (45 min)
        String Name = (String) data.get("Name_Ios");
        String fc2NotificationMessgeLine2 = "Please update "+Name+".";
		String fc2NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
		String fc2NotificationMessgeLine2text = library.getTextFrom(fc2NotificationMessgeLine2xpath);
		Assert.assertEquals(fc2NotificationMessgeLine2, fc2NotificationMessgeLine2text);// Verify the notofication body is "please Update <Client name>"
		library.click(fc2NotificationMessgeLine1xpath);
		library.wait(5);
		library.verifyPageContainsElement("TRANSACTION.updatebutton");// Verify the agent is led to client detail page
		String ClientName = library.getTextFrom("NOTIFICATION.ClientName");
		Assert.assertEquals(ClientName, "Test AgentiOS");// Verify client name on client detail page is the same as that in the FC2
		library.click("HOMEPAGE.hamburger");
		library.wait(4);
		String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
		int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
		Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);// Verify the unread notification number is one less
	}

	// Created by Priyanka :- 09/09/2016
	@Override
	public void verifyforFC3notification(Map<String, Object> data) {
		library.click("HOMEPAGE.hamburger");
		library.wait(4);
		String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
		int NotificationCountnum = Integer.parseInt(NotificationCount);
		library.click("MENU.notificationpagelink");
		library.wait(4);
		String fc3NotificationMessgeLine1 = "Update Now Reminder (1.5hr)";// Verify the notification title is "Update Now Reminder (1.5hr)"
		String fc3NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
		String fc3NotificationMessgeLine1text = library.getTextFrom(fc3NotificationMessgeLine1xpath);
		Assert.assertEquals(fc3NotificationMessgeLine1, fc3NotificationMessgeLine1text);
	    String Name = (String) data.get("Name_Ios");
        String fc3NotificationMessgeLine2 = "LAST REMINDER: Update "+Name+".";
		String fc3NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
		String fc3NotificationMessgeLine2text = library.getTextFrom(fc3NotificationMessgeLine2xpath);
		Assert.assertEquals(fc3NotificationMessgeLine2, fc3NotificationMessgeLine2text);// Verify yhe notification body is "LAST REMINDER: Update <client name>"
		library.click(fc3NotificationMessgeLine1xpath);
		library.wait(5);
		library.verifyPageContainsElement("TRANSACTION.updatebutton");// Verify the agent is led to client detail page
		String ClientName = library.getTextFrom("NOTIFICATION.ClientName");
		Assert.assertEquals(ClientName, "Test AgentiOS");// Client name on client detail page is the same as that in the FC3
		library.click("HOMEPAGE.hamburger");
		library.wait(4);
		String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
		int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
		Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);// Verify the unread notification number is one less
	}

	// Created by Priyanka :- 09/09/2016
	@Override
	public void verifyforN1notification(Map<String, Object> data) {
		library.click("HOMEPAGE.hamburger");
		library.wait(4);
		String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
		int NotificationCountnum = Integer.parseInt(NotificationCount);
		library.click("MENU.notificationpagelink");
		library.wait(4);
		String N1NotificationMessgeLine1 = "Update Now";
		String N1NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
		String N1NotificationMessgeLine1text = library.getTextFrom(N1NotificationMessgeLine1xpath);
		Assert.assertEquals(N1NotificationMessgeLine1, N1NotificationMessgeLine1text);// Verify the notification title is "Call Now"
        String Name = (String) data.get("Name_Ios");                    
        String Phone = (String) data.get("Phone");
        String N1NotificationMessgeLine2 = "Call "+Name+" " +Phone+"";
		String N1NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
		String N1NotificationMessgeLine2text = library.getTextFrom(N1NotificationMessgeLine2xpath);
		Assert.assertEquals(N1NotificationMessgeLine2, N1NotificationMessgeLine2text);// Verify the notification title is Call<cliet name><phone number>
		library.click(N1NotificationMessgeLine1xpath);
		library.wait(5);
		library.verifyPageContainsElement("TRANSACTION.updatebutton");// Verify the agent is led to Contact Reminde page
		String ClientName = library.getTextFrom("NOTIFICATION.ClientName");
		Assert.assertEquals(ClientName, "Test Client");// Verify client name on the page is the same as that in the N1
		library.click("FLEAVE.startdatefield");
		library.wait(10);
		library.click("HOMEPAGE.hamburger");
		library.wait(4);
		String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
		int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
		Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);// Verify the unread notification number is one less
		}

	// Created by Priyanka :- 09/09/2016
	@Override
	public void verifyforN2notification(Map<String, Object> data) {		String Address = (String) data.get("Address");
	String Price = (String) data.get("Price");
	library.click("HOMEPAGE.hamburger");
	library.wait(4);
	String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
	int NotificationCountnum = Integer.parseInt(NotificationCount);
	library.click("MENU.notificationpagelink");
	library.wait(4);
	String N2NotificationMessgeLine1 = "Call Now Reminder (45min)";
	String N2NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
	String N2NotificationMessgeLine1text = library.getTextFrom(N2NotificationMessgeLine1xpath);
	Assert.assertEquals(N2NotificationMessgeLine1, N2NotificationMessgeLine1text);// Verify the notification title is call now
	   String Name = (String) data.get("Name_Ios");                    
       String Phone = (String) data.get("Phone");
       String N2NotificationMessgeLine2 = "Call "+Name+" " +Phone+"";
	String N2NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
	String N2NotificationMessgeLine2text = library.getTextFrom(N2NotificationMessgeLine2xpath);
	Assert.assertEquals(N2NotificationMessgeLine2, N2NotificationMessgeLine2text);// Verify the notification title is Call<client name><phone number>
	library.click(N2NotificationMessgeLine1xpath);
	library.wait(5);
	String Addressui = library.getTextFrom("NOTIFACATION.address");
	Assert.assertEquals(Addressui, Address);
	String Priceui = library.getTextFrom("NOTIFACATION.address");
	Assert.assertEquals(Priceui, Price);
	library.verifyPageContainsElement("TRANSACTION.updatebutton");// Verify the agent is led to Contact New Client Reminder page
	String ClientName = library.getTextFrom("NOTIFICATION.ClientName");
	Assert.assertEquals(ClientName, "Test Client");// Verify client name on the page is the same as that in the N1
	library.click("FLEAVE.startdatefield");
	library.wait(10);
	library.click("HOMEPAGE.hamburger");
	library.wait(4);
	String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
	int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
	Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);// Verify the unread notification number is one less
	}

	// Created by Priyanka :- 09/09/2016
	@Override
	public void verifyforN3notification(Map<String, Object> data) {		String Address = (String) data.get("Address");
	String Price = (String) data.get("Price");
	library.click("HOMEPAGE.hamburger");
	library.wait(4);
	String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
	int NotificationCountnum = Integer.parseInt(NotificationCount);
	library.click("MENU.notificationpagelink");
	library.wait(4);
	String N3NotificationMessgeLine1 = "Call Now Reminder (1.5hr)";
	String N3NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
	String N3NotificationMessgeLine1text = library.getTextFrom(N3NotificationMessgeLine1xpath);
	Assert.assertEquals(N3NotificationMessgeLine1, N3NotificationMessgeLine1text);// Verify the notification title is "Call Now Reminder(1.5 hr)
	String N3NotificationMessgeLine2 = "LAST REMINDER : Call Test Client.";
	String N3NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
	String N3NotificationMessgeLine2text = library.getTextFrom(N3NotificationMessgeLine2xpath);
	Assert.assertEquals(N3NotificationMessgeLine2, N3NotificationMessgeLine2text);// Verify the notification body is "Call <client name><phone number>" 
	library.click(N3NotificationMessgeLine1xpath);
	library.wait(5);
	String Addressui = library.getTextFrom("NOTIFACATION.address");
	Assert.assertEquals(Addressui, Address);
	String Priceui = library.getTextFrom("NOTIFACATION.address");
	Assert.assertEquals(Priceui, Price);
	library.verifyPageContainsElement("TRANSACTION.updatebutton");// Verify the agent is led to contract New Client Reminder page
	String ClientName = library.getTextFrom("NOTIFICATION.ClientName");
	Assert.assertEquals(ClientName, "Test Client");// Verify client name on the page is the same as that in the N1
	library.click("FLEAVE.startdatefield");
	library.wait(10);
	library.click("HOMEPAGE.hamburger");
	library.wait(4);
	String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
	int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
	Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);// Verify the unread notification number is one less
	}

	// Created by Priyanka :- 09/06/2016
	public void verifyforFC025notification(Map<String, Object> data) {		library.click("HOMEPAGE.hamburger");
	library.wait(4);
	String NotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
	int NotificationCountnum = Integer.parseInt(NotificationCount);
	library.click("MENU.notificationpagelink");
	library.wait(4);
	String fc025NotificationMessgeLine1 = "New Client Assigned";
	String fc025NotificationMessgeLine1xpath = "NOTIFICATION.fc1NotificationMessgeLine1";
	String fc025NotificationMessgeLine1text = library.getTextFrom(fc025NotificationMessgeLine1xpath);
	Assert.assertEquals(fc025NotificationMessgeLine1, fc025NotificationMessgeLine1text);
	 String City =(String) data.get("City");
     String Price =(String) data.get("Price");
     String Zip =(String) data.get("Zip");
     String fc025NotificationMessgeLine2 = ""+Price+" in "+City+", "+Zip+".";
	String fc025NotificationMessgeLine2xpath = "NOTIFICATION.fc1NotificationMessgeLine2";
	String fc025NotificationMessgeLine2text = library.getTextFrom(fc025NotificationMessgeLine2xpath);
	Assert.assertEquals(fc025NotificationMessgeLine2, fc025NotificationMessgeLine2text);
	library.click(fc025NotificationMessgeLine1xpath);
	library.wait(5);
	library.verifyPageContainsElement("TRANSACTION.updatebutton");// Verify the agent is led to Contract New Client Reminder page
	String ClientName = library.getTextFrom("NOTIFICATION.ClientName");
	Assert.assertEquals(ClientName, "Test AgentiOS");// Verify client name on the page is the same as that in the N1
	library.click("HOMEPAGE.hamburger");
	library.wait(4);
	String ReducedNotificationCount = library.getTextFrom("NOTIFICATION.NotificationCount");
	int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
	Assert.assertEquals(NotificationCountnum - 1, ReducedNotificationCountnum);//Verify the unread notification number is one less
	}
	
	
	long diffrence=0;
	  @Override
	 public void createFC05Notification() {

	 library.wait(10);
	  SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy HH:mm a");
	    String dateInString1 = library.getTextFrom("xpath->//th[text()='Hot Lead Submission Time']/../..//tr[2]/td[4]");
	    String dateInString2 = library.getTextFrom("xpath->//th[text()='Hot Lead Submission Time']/../..//tr[3]/td[4]");
	    try
	    {
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
	    catch(ParseException e)
	    {
	     e.printStackTrace();
	    
	 }
	 }

	 
	  @Override
	  // Created by Priyanka :- 09/06/2016
	    public void verifyforFC05notification() {
	   if(diffrence>0)
	   {
	     library.click("HOMEPAGE.hamburger");
	     library.wait(4);
	           String NotificationCount = library.getTextFrom("xpath->//UIATableCell[1]/UIAStaticText[2]");
	           int NotificationCountnum = Integer.parseInt(NotificationCount);
	     library.click("MENU.notificationpagelink");
	     library.wait(4);
	     String fc05NotificationMessgeLine1 = "New Client Assigned";
	     String fc05NotificationMessgeLine1xpath = "xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[1]/UIAStaticText[1]";
	     String fc05NotificationMessgeLine1text = library.getTextFrom(fc05NotificationMessgeLine1xpath);
	     Assert.assertEquals(fc05NotificationMessgeLine1, fc05NotificationMessgeLine1text);
	     
	     String fc05NotificationMessgeLine2 = "$500,000 in Mchenry, 60050.";
	     String fc05NotificationMessgeLine2xpath = "xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[1]/UIAStaticText[2]";
	     String fc05NotificationMessgeLine2text = library.getTextFrom(fc05NotificationMessgeLine2xpath);
	     Assert.assertEquals(fc05NotificationMessgeLine2, fc05NotificationMessgeLine2text);
	         library.click(fc05NotificationMessgeLine1xpath);
	            library.wait(5);
	            library.verifyPageContainsElement("name->UPDATE");//Verify the agent is led to Contact New Client Reminder page
	            String ClientName = library.getTextFrom("xpath->//UIAApplication[1]/UIAWindow[1]/UIAStaticText[1]");
	            Assert.assertEquals(ClientName, "Test AgentiOS");//Verify client name on the page is the same as that in the N1
	         library.click("HOMEPAGE.hamburger");
	      library.wait(4);
	            String ReducedNotificationCount = library.getTextFrom("xpath->//UIATableCell[1]/UIAStaticText[2]");
	            int ReducedNotificationCountnum = Integer.parseInt(ReducedNotificationCount);
	            Assert.assertEquals(NotificationCountnum-1, ReducedNotificationCountnum);//Verify the unread notification number is one less
	   }
	    }
	  @Override
	  public void updateLeadStageToTalked(Map<String, Object> data) {
		  library.click("xpath->//UIATableCell[@name='Test Client']/UIAStaticText[1]");
		   library.wait(2);
		   library.click("TRANSACTION.updatebutton");
		   library.wait(2);
		   chooseTransactionStage("Emailed");
		   library.wait(2);
		   library.click("UPDATE.applybutton");
		   library.wait(5);
		   Assert.assertTrue(library.isElementEnabled("NOTES.notesfield", true));// Add
		                     // Note
		                     // Page
		                     // is
		                     // opened
		   library.typeDataInto("test Emailed Update", "NOTES.notesfield");
		   library.click("NOTES.savebutton");
		   library.wait(5);
		   library.click("CLIENTLIST.back");
		   library.wait(5);
		   library.click("xpath->//UIATableCell[@name='Test Client']/UIAStaticText[1]");
		   library.wait(5);
		   String TalkedStage = library
		     .getTextFrom("xpath->//UIAStaticText[contains(@name,'Buy :')]/..//UIAStaticText[2]").split(" ")[0];
		   Assert.assertEquals(TalkedStage, "Talked"); // Lead stage is changed to Talked
		   library.wait(2);
		   library.click("CLIENTDETAIL.notesbutton");
		   String text = library.getTextFrom("xpath->//UIATableView[2]/UIATableCell[1]/UIAStaticText[1]");
		   String[] tokens = text.split(" ");
		   String text1 = tokens[0];
		   Assert.assertEquals(text1, "Emailed");// Verify Notes is displayed as
		             // followings:Emailed
		   String contactURL = getApiUrlAWS(data);
		   setRequestHeader(data);
		   String response = library.HTTPGet(contactURL);
		   String xpathForDateAndTimeFromNotesField = library.getTextFrom("NOTES.updatedtime");
		   String[] Mettime = xpathForDateAndTimeFromNotesField.split(" ");
		   String MettimeSplit = Mettime[1] + " " + Mettime[2] + " " + Mettime[3];
		   String FinalValue = null;
		   try {
		    DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		    formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		    Date date = formatterIST.parse(MettimeSplit);
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
		   String text3 = library.getTextFrom("xpath->//UIATableView[2]/UIATableCell[1]/UIAStaticText[2]");
		   Assert.assertEquals(text3, "test Emailed Update");// Verify Notes is
		                // displayed as
		                // followings:test
		                // Emailed Update

	  }
	  

	  @Override
	  public void verifyMeetingTimeAndNotes(Map<String, Object> data) {
	   // TODO Auto-generated method stub
	   boolean isTalked = library.verifyPageContainsElement("OPPORTUNITY.oppotunityType");// Schedule
	                          // a
	                          // Meeting
	                          // page
	                          // is
	                          // opened,
	                          // Schedule
	                          // a
	                          // Meeting
	                          // is
	                          // displayed
	                          // on
	                          // the
	                          // page

	   Assert.assertTrue(isTalked, "Stage is not talked...");
	   library.click("TRANSACTION.updatebutton");
	   chooseTransactionStage("Scheduled a Meeting");
	   library.wait(1);
	   library.click("UPDATE.applybutton");
	   int totalMinutesOfSystemTime = getTotalMinutesOfTime();
	   library.wait(2);

	   String[] startTimeOfMeeting = library.getTextFrom("MEETINGTIME.startTime").split(":");
	   //int startTime=Integer.parseInt(startTimeOfMeeting);
	   //String startTime = startTimeOfMeeting.split(":")[0];
	   // String startTime = startTimeNew.split(" ")[0];
	   String hoursOfStartTimeConvert = startTimeOfMeeting[0];
	   int hoursOfStartTime=Integer.parseInt(hoursOfStartTimeConvert);
	   String[] minutesOfStartTimeConvert = startTimeOfMeeting[1].split(" ");
	   String minutesOfStartTimeConvertinMin=minutesOfStartTimeConvert[0];
	   int minutesOfStartTime=Integer.parseInt(minutesOfStartTimeConvertinMin);
	   int totalMinutesOfMeetingStartTime = (60 * hoursOfStartTime) + minutesOfStartTime;
	   if (startTimeOfMeeting[3].equals("PM") && hoursOfStartTime != 12)
	    totalMinutesOfMeetingStartTime = 720 + totalMinutesOfMeetingStartTime;
	   String endTimeOfMeeting = library.getTextFrom("MEETINGTIME.endTime");
	   String endTime = endTimeOfMeeting.split(" ")[1];
	   // String endTime = endTimeNew.split(" ")[0];
	   int hoursOfEndTime = Integer.parseInt(endTime.split(":")[0]);
	   int minutesOfEndTime = Integer.parseInt(endTime.split(":")[1]);
	   int totalMinutesOfMeetingEndTime = (60 * hoursOfEndTime) + minutesOfEndTime;
	   System.out.println(endTimeOfMeeting.split(" ")[1]);
	   if (endTimeOfMeeting.split(" ")[3].equals("PM") && hoursOfEndTime != 12)
	    totalMinutesOfMeetingEndTime = 720 + totalMinutesOfMeetingEndTime;

	   // boolean isStartTimeCorrect = (60 <= totalMinutesOfMeetingStartTime &&
	   // totalMinutesOfMeetingStartTime <= 65);
	   if (totalMinutesOfMeetingStartTime <= 65) {
	    Reporter.log("Day (Date) is changed", true);
	    boolean isStartTimeCorrect = (60 <= (1440 - totalMinutesOfSystemTime) + totalMinutesOfMeetingStartTime
	      && (1440 - totalMinutesOfSystemTime) + totalMinutesOfMeetingStartTime <= 65);
	    Assert.assertTrue(isStartTimeCorrect, "Meeting Start time is not 60 minutes later from the present time.");// the
	                               // default
	                               // Start
	                               // Time
	                               // is
	                               // 1
	                               // hour
	                               // after
	                               // the
	                               // current
	                               // time

	    boolean isEndTimeCorrect = ((90 <= 1440 - totalMinutesOfSystemTime + totalMinutesOfMeetingEndTime)
	      && (95 >= 7200 - totalMinutesOfSystemTime + totalMinutesOfMeetingEndTime));
	    Assert.assertTrue(isEndTimeCorrect, "Meeting End time is not 90 minutes later from the present time.");// the
	                              // default
	                              // End
	                              // Time
	                              // is
	                              // 1
	                              // and
	                              // half
	                              // an
	                              // hour
	                              // after
	                              // the
	                              // current
	                              // time

	   } else {
	    boolean isStartTimeCorrect = ((60 <= (totalMinutesOfMeetingStartTime - totalMinutesOfSystemTime))
	      && (65 >= (totalMinutesOfMeetingStartTime - totalMinutesOfSystemTime)));
	    Assert.assertTrue(isStartTimeCorrect, "Meeting Start time is not 60 minutes later from the present time.");

	    boolean isEndTimeCorrect = ((90 <= (totalMinutesOfMeetingEndTime - totalMinutesOfSystemTime))
	      && (95 >= (totalMinutesOfMeetingEndTime - totalMinutesOfSystemTime)));
	    Assert.assertTrue(isEndTimeCorrect, "Meeting End time is not 90 minutes later from the present time.");

	   }

	   library.typeDataInto("Agent Automation", "NOTES.notesfield");
	   // library.navigateBack();
	   library.wait(1);
	   library.click("MEETINGTIME.scheduleMeeting");
	   library.wait(7);
	   library.click("URGENCY.notes");
	   library.wait(5);
	   String activityTypePath = "xpath->(.//UIAStaticText[contains(@name,'Scheduled a Meeting')])[1]";
	   Assert.assertTrue(library.verifyPageContainsElement(activityTypePath));// verify
	                       // notes

	   library.wait(2);
	   Assert.assertTrue(library
	     .verifyPageContainsElement("xpath->(.//UIAStaticText[contains(@name,'Agent Automation')])[1]"));// verify
	                               // notes

	   String isStartTime = "xpath->(.//UIAStaticText[contains(@name,'" + startTimeOfMeeting + "')])[1]";
	   Assert.assertTrue(library.verifyPageContainsElement(isStartTime));// verify
	                     // notes Scheduled a Meeting <activity updated time stamp>

	   String isEndTime = "xpath->(.//UIAStaticText[contains(@name,'" + endTime + "')])[1]";
	   Assert.assertTrue(library.verifyPageContainsElement(isEndTime));// verify
	                     // notes Scheduled a Meeting <activity updated time stamp>

	   library.click("NOTES.cancelbutton");
	  }
	// Puneet
	  @Override
	  public void updateToMakeANOffer(Map<String, String> MadeOfferData) {
	   String clientName = MadeOfferData.get("ClientName");

	   searchAndSelectClient(clientName);

	   String presatge = getPrestage();

	   library.click("TRANSACTION.updatebutton");// update
	   library.wait(5);
	   library.typeDataInto("Made an Offer", "UPDATE.list");
	   library.click("UPDATE.applybutton");
	   handleEditTransactionAlert();
	   Assert.assertTrue(library.verifyPageContainsElement("MAKEOFFER.pageonepropertyaddress"));
	   Assert.assertTrue(library.verifyPageContainsElement("MAKEOFFER.pageoneclientone"));

	   enterAddress(MadeOfferData.get("Address"));

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
	   library.wait(5);
	   library.clear("MAKEOFFER.price");
	   enterNumber(MadeOfferData.get("OfferPrice"), "MAKEOFFER.price");
	   library.click("MAKEOFFER.continuebutton");// continue
	   library.wait(5);
	   String UIAddress=library.getTextFrom("xpath->//UIAStaticText[5]");
	   Assert.assertEquals(UIAddress, "1749 Lake Street, San Mateo, CA 94403"); //Verify address
	   Assert.assertTrue(library.verifyPageContainsElement("MAKEOFFER.pageopenedconfirmoffer"),
	     "Page is not opened");
	   library.wait(3);
	   String uIAddress = library.getTextFrom("MAKEOFFER.addressfield");
	   System.out.println(uIAddress);
	   // Map<String, String> madeAnOfferData = new HashMap<>();

	   String xlSheetAddress = String.valueOf(MadeOfferData.get("Address"));
	   String[] value = xlSheetAddress.split(",");
	   String finalvalue = value[0] + "," + " " + value[1] + "," + " " + value[2] + " " + value[3];
	   System.out.println(finalvalue);
	   Assert.assertEquals(finalvalue, uIAddress);

	   String uiPrice = library.getTextFrom("MAKEOFFER.pricefiled");
	   String xlPrice = String.valueOf(MadeOfferData.get("OfferPrice"));
	   String uiPriceRemoveSpecialCharaters = uiPrice.replaceAll(",", "");
	   String finalUiPrice = uiPriceRemoveSpecialCharaters.replaceAll("\\$", "");
	   System.out.println(finalUiPrice);
	   Assert.assertEquals(finalUiPrice, xlPrice);
	   String dateFromUII = library.getTextFrom("xpath->//UIAApplication[1]/UIAWindow[1]/UIAStaticText[9]");
	   Assert.assertEquals(dateFromUII, systemDate);
	   library.click("MAKEOFFER.submitOffer");
	   library.wait(3);
	   Assert.assertTrue(library.verifyPageContainsElement("UPDATEPAGE.metstagelocator"),
	     "Page is not in offered");
	  }
	  
	  @Override
	  public void verifyNotesData(Map<String, Object> data) {

	   String contactURL = getApiUrlForContractCancel(data);
	   setRequestHeaderforMadeAnOffer(data);
	   String response = library.HTTPGet(contactURL);

	   String notesData = (String) library.getValueFromJson("$.activities[0].activityType", response);

	   String notesTimeXpath = "NOTES.updatedtime";
	   String getTime = library.getTextFrom(notesTimeXpath);
	   String[] Mettime = getTime.split(" ");
	   String MettimeSplit = Mettime[3] + " " + Mettime[4] + " " + Mettime[5];

	   String FinalValue = null;
	   try {

	    DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	    formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	    Date date = formatterIST.parse(MettimeSplit);

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
	    Assert.assertEquals(finalTime, FinalValue); //Verify Made an offer time and date stamp
	    library.wait(5);
	    Assert.assertTrue(library.verifyPageContainsElement("NOTES.propertyaddress"));
	    library.click("NOTES.cancelbutton");
	    library.wait(2);

	   } catch (Exception e) {
	    e.printStackTrace();
	   }
	  }
	  
	  @Override
	  public void verifyPropertyDetails(Map<String, String> MadeOfferData) {
		  try {
			    String UIAddress = library.getTextFrom("MAKEOFFER.propertiesaddressfield");
			    System.out.println(UIAddress);
			    String xlSheetAddress = String.valueOf(MadeOfferData.get("Address"));
			    Map<String, String> test = FileUtil.readFileAsMap("config//API_Testt.properties");
			    String s = xlSheetAddress;
			    String[] ss = s.split(",");
			    String city = ss[2];
			    String addCity = (test.get(city));
			    String finalcity = ss[0] + "," + " " + ss[1] + "," + " " + addCity + " " + ss[3];
			    System.out.println(finalcity);

			    Assert.assertEquals(finalcity, UIAddress); //Verify offer address for property section

			    String systemDate = null, dateFromUI = null;
			    systemDate = getSysDates();

			    String uiPrice = library.getTextFrom("MAKEOFFER.propertiespricefield").replace("$", "").replace(",", "").trim();
			    String xlPrice = String.valueOf(MadeOfferData.get("OfferPrice"));
			       Assert.assertEquals(uiPrice, xlPrice); //Verify offer price for property section
			    String uiPriceRemoveSpecialCharaters = uiPrice.replaceAll(",", "");
			    String finalUiPrice = uiPriceRemoveSpecialCharaters.replaceAll("\\$", "");
			    System.out.println(finalUiPrice);
			    Assert.assertEquals(finalUiPrice, xlPrice);
			    dateFromUI = library.getTextFrom(
			      "xpath->//UIAApplication[1]/UIAWindow[1]/UIATableView[3]/UIATableCell[3]/UIAStaticText[2]");

			    Assert.assertEquals(dateFromUI, systemDate);
			   } catch (Exception e) {
			    e.printStackTrace();
			   }
	  }
	  
	  @Override
	  public void updateToContractAcceptedStage1(Map<String, Object> data) {
		  library.click("xpath->//UIATableCell[@name='Test Client']/UIAStaticText[1]");
		   library.wait(2);
		   library.click("TRANSACTION.updatebutton");
		   library.wait(2);
		   chooseTransactionStage("Contract Accepted");
		   library.wait(2);
		   library.click("UPDATE.applybutton");
		   library.wait(3);
		   Assert.assertTrue(library.verifyPageContainsElement("xpath->//UIAWindow[1]/UIAStaticText[1]"));// verifies
		   String ClientName = library.getTextFrom("xpath->//UIAStaticText[4]");
		   Assert.assertEquals(ClientName, "Test Client");// verify Client Name is
		   // correct with the
		   // updated client
		   String Address = library.getTextFrom("xpath->//UIAStaticText[6]");
		   Assert.assertEquals(Address, "1749 Lake Street, San Mateo, California 94403");
		   String ExpectedCloseDate = library.getTextFrom("xpath->//UIAStaticText[8]");
		   DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		   Calendar c = Calendar.getInstance();
		   c.setTime(new Date()); // Now use today date.
		   c.add(Calendar.DATE, 45); // Adding 45 days
		   String output = dateFormat.format(c.getTime());
		   Assert.assertEquals(ExpectedCloseDate, output);// verifies The default
		   library.wait(3);
		   library.click("name->Continue");// 1 continue
		   library.wait(3);
		   Assert.assertTrue(library.verifyPageContainsElement("name->Confirm Name on Contract"));
		   Assert.assertTrue(library.verifyPageContainsElement("name->Test Client"));
		   library.click("name->Yes, Name is Correct on Contract");
		   library.wait(3);
		   Assert.assertTrue(library.verifyPageContainsElement("name->Contract Accepted"));
		   Assert.assertTrue(library.verifyPageContainsElement("name->Test Client"));
		   // Assert.assertTrue(library.verifyPageContainsElement("name->'1743 Lake
		   // Street, San Mateo, California 94403'", true));
		   enterNumber("650000", "xpath->//UIATextField[1]");
		   enterNumber("3", "xpath->//UIATextField[2]");
		   library.wait(3);
		   library.click("name->Continue"); // 2 continue
		   library.wait(3);
		   Assert.assertTrue(library.isElementEnabled("xpath->//UIAButton[@name='Submit Info']", true));
		   library.wait(3);
		   library.click("xpath->//UIAButton[@name='Submit Info']");
		   library.wait(20);
		   library.click("CLIENTLIST.back");
		   library.wait(5);
		   library.click("xpath->//UIATableCell[@name='Test Client']/UIAStaticText[1]");
		   library.wait(5);
		   String[] TalkedStage = library
		     .getTextFrom("xpath->//UIAStaticText[contains(@name,'Buy :')]/..//UIAStaticText[2]").split(" ");
		   String UiTalkedStage=TalkedStage[0]+" "+TalkedStage[1].trim();
		   Assert.assertEquals(UiTalkedStage, "In Contract");
		   library.click("CLIENTDETAIL.notesbutton");
		   String text = library.getTextFrom("xpath->//UIATableView[2]/UIATableCell[1]/UIAStaticText[1]");
		   String[] tokens = text.split(" ");
		   String text1 = tokens[0] + " " + tokens[1];
		   Assert.assertEquals(text1, "Contract Accepted");// Verify Notes is
		               // displayed as
		   // followings:Contract Accepted
		   String contactURL = getApiUrlAWS(data);
		   setRequestHeader(data);
		   String response = library.HTTPGet(contactURL);
		   String xpathForDateAndTimeFromNotesField = library.getTextFrom("NOTES.updatedtime");
		   String[] Mettime = xpathForDateAndTimeFromNotesField.split(" ");
		   String MettimeSplit = Mettime[2] + " " + Mettime[3] + " " + Mettime[4];
		   String FinalValue = null;
		   try {
		    DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		    formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		    Date date = formatterIST.parse(MettimeSplit);
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

		    String text3 = library.getTextFrom("xpath->//UIATableView[2]/UIATableCell[1]/UIAStaticText[2]");
		    Assert.assertEquals(text3, "Property Address - see properties section for more details");// Verify
		                           // Notes
		                           // is
		    // displayed as
		    // followings:Property Address - see properties section for more
		    // details

		    // Verify property section on the page
		    library.click("name->Properties");
		    library.wait(3);
		    String Addresss = library.getTextFrom("MAKEOFFER.escrowinfoaddress").replace(",", "");
		    String OfferDate = library.getTextFrom("MAKEOFFER.escrowinfodate");
		    String OfferPrice = library.getTextFrom("MAKEOFFER.escrowinfoprice");
		    scrollClientList(80, 30);
		    String accepteddate = library.getTextFrom("CONTRACT.accepteddate");
		    String format="dd/mm/yyyy";
		    SimpleDateFormat sdf=new SimpleDateFormat(format);
		    Date datee=sdf.parse(accepteddate);
		    //System.out.println(sdf.format(date));
		    String Finalaccepteddate=sdf.format(datee);
		    String acceptedprice = library.getTextFrom("CONTRACT.acceptedprice");
		    String expectedclose = library.getTextFrom("CONTRACT.expectedclose");
		    String commission = library.getTextFrom("CONTRACT.commission");
		    DateFormat dateFormats = new SimpleDateFormat("MM/dd/yyyy");
		    Date dateee = new Date();
		    
		    
		    String returndate = (dateFormats.format(dateee));
		    String Addressxl = (String) data.get("Address");
		    Addressxl=Addressxl.replace(",", "");
		    String Pricexl = (String) data.get("Price");
		    String commissionxl = (String) data.get("commission") + "%";
		    Assert.assertEquals(Addresss, Addressxl);
		    Assert.assertEquals(Finalaccepteddate, returndate);
		    Assert.assertEquals(OfferPrice, Pricexl);
		    library.wait(3);
		    String formatt="dd/mm/yyyy";
		    SimpleDateFormat sdff=new SimpleDateFormat(formatt);
		    Date dateeee=sdff.parse(expectedclose);
		    System.out.println(sdf.format(dateeee));
		    String Finalexpectedclose=sdff.format(dateeee);
		    
		    SimpleDateFormat sdfff = new SimpleDateFormat("MM/dd/yyyy");
		       Calendar cc = Calendar.getInstance();
		       cc.setTime(new Date()); // Now use today date.
		       cc.add(Calendar.DATE, 45); // Adding 5 days
		       String outputt = sdfff.format(c.getTime());
		       System.out.println(outputt);
		       Assert.assertEquals(outputt, Finalexpectedclose);
		    
		    Assert.assertEquals(acceptedprice,Pricexl);
		    Assert.assertEquals(commission, commissionxl);

		   } catch (Exception e) {
		    e.printStackTrace();
		   }	  }
	  
	  public void updateToStageForContractCancel(String stage, String client) {

		  searchAndSelectClient(client);

		  library.wait(3);
		  String stageBeforeUpdate = getPrestage();
		  System.out.println(stageBeforeUpdate);
		  library.wait(3);
		  library.click("TRANSACTION.updatebutton");
		  
		  chooseTransactionStage(stage);
		  library.click("UPDATE.applybutton");
		  library.wait(5);
		  Assert.assertTrue(library.verifyPageContainsElement("name->Contract Cancelled"));//Verify contract cancell page is opened
		  library.typeDataInto("Test for Cancel", "NOTES.notesfield");
		  library.navigateBack();
		  library.wait(3);
		  library.click("name->Save Update");
		  library.wait(5);
		  Assert.assertTrue(library.verifyPageContainsElement("xpath->//UIAScrollView[1]/UIATableView[1]/UIATableCell[1]/UIAStaticText[2]"));//Verify status is changed
		 }
	  
	  
	  
	  public void verifyNotesDataForContractCancel(Map<String, Object> data) {
		  // library.navigateBack();
		    library.wait(5);
		    // library.click("xpath->//UIATableCell[@name='Test
		    // Client']/UIAStaticText[1]");
		    library.wait(5);
		    String contactURL = getApiUrlForContractCancel(data);
		    setRequestHeaderforMadeAnOffer(data);
		    String response = library.HTTPGet(contactURL);
		    Assert.assertTrue(library.verifyPageContainsElement("xpath->//UIAStaticText[contains(@name,'Contract Cancelled')]"));//Verify activity type for Contact Cancelled
		    String notesData = (String) library.getValueFromJson("$.activities[0].activityType", response);

		    String transactionType = notesData.split("-")[1];
		    transactionType = transactionType.trim();

		    String locator = getLocatorForTransactionType(transactionType);

		    library.verifyPageContainsElement(locator);
		    Assert.assertTrue(library.verifyPageContainsElement("xpath->//UIATableCell[2]"));//verify note
		    String notesLineTwo = library.getTextFrom("CONTRACTCANCEL.notesfieldlinetwo");
		    System.out.println(notesLineTwo);
		    Assert.assertEquals("Test for Cancel", notesLineTwo);
		    String xpathForDateAndTimeFromNotesField = library.getTextFrom("NOTES.updatedtime");
		    String[] Mettime = xpathForDateAndTimeFromNotesField.split(" ");
		    String MettimeSplit = Mettime[2] + " " + Mettime[3] + " " + Mettime[4];
		    String FinalValue = null;

		    try {

		     DateFormat formatterIST = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		     formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		     Date date = formatterIST.parse(MettimeSplit);

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
		     Assert.assertEquals(finalTime, FinalValue);//Verify Timestamp and date
		     library.wait(5);

		     library.click("NOTES.cancelbutton");
		     // library.waitForElement("TRANSACTION.properties");
		     library.wait(3);

		    }

		    catch (Exception e) {
		     e.printStackTrace();
		    }
		 }
	  @Override
	  public void updateToReturnToMovoto1(Map<String, String> data) {
	   library.wait(2);
	   library.click("RETURN.firstlead");
	   library.wait(3);
	   library.click("TRANSACTION.updatebutton");
	   library.wait(3);
	   chooseTransactionStage("Return to Movoto");
	   library.wait(3);
	   library.click("RETURN.apply");
	   library.wait(3);
	   library.click("RETURN.apply");
	   library.wait(2);
	   library.click("xpath->//UIAWindow[1]/UIAButton[2]");
	   library.wait(5);
	   library.click("xpath->//UIAButton[contains(@name,'search icon')]");
	   library.wait(5);
	   String EmailID= data.get("EmailID");
	   library.typeDataInto(EmailID, "xpath->//UIATextField[1]");
	   library.wait(3);
	   Assert.assertTrue(library.verifyPageContainsElement("RETURN.text")); //Verify client list is missing


	  }
	  
	  @Override
	  public void verifyClientDetailsWithApi(Map<String, Object> data) {
		  setTokenAndUserId(data);
		   String clientLeadApi = (String) data.get("clientLeadApi");
		   System.out.println(clientLeadApi);
		   library.wait(3);
		   String response = (String) getResponse(clientLeadApi);
		   System.out.println(response);

		   String mailIdFromApiResponse = (String) library.getValueFromJson("$.email", response);
		   String mailId = library.getTextFrom("DETAILS.mail");
		   Assert.assertEquals(mailId, mailIdFromApiResponse);// Verify the email
		                // matches the value
		                // from API response

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
		        "Primary number is not matched.");// Verify the
		                 // Primary Phone
		                 // matches the
		                 // value from
		                 // API response

		      contactList.add(contactType);
		      break;

		     case "mobile":
		      String mobileNumberOfApiResponse = (String) library
		        .getValueFromJson("$.phone[" + i + "].phonenumber", response);
		      String mobileNumber = library.getTextFrom("DETAILS.mobileNumber");
		      mobileNumber = mobileNumber.replaceAll("\\p{P}", "");
		      mobileNumber = mobileNumber.replaceAll(" ", "");
		      Assert.assertTrue(mobileNumber.equals(mobileNumberOfApiResponse), "Mobile number is not matched.");// Verify
		                               // phone
		                               // numbers
		                               // which
		                               // has
		                               // values
		                               // from
		                               // API
		                               // response
		                               // match
		                               // the
		                               // value
		                               // on
		                               // lead's
		                               // contact
		                               // page
		      contactList.add(contactType);
		      break;

		     case "office":
		      String officeNumberOfApiResponse = (String) library
		        .getValueFromJson("$.phone[" + i + "].phonenumber", response);
		      String officeNumber = library.getTextFrom("DETAILS.officeNumber").replace("\\p{P}", "").replace(" ", "");
		      Assert.assertTrue(officeNumber.equals(officeNumberOfApiResponse), "Office number is not matched.");// Verify
		                               // phone
		                               // numbers
		                               // which
		                               // has
		                               // values
		                               // from
		                               // API
		                               // response
		                               // match
		                               // the
		                               // value
		                               // on
		                               // lead's
		                               // contact
		                               // page
		      contactList.add(contactType);
		      break;

		     case "home":
		      String homeNumberOfApiResponse = (String) library.getValueFromJson("$.phone[" + i + "].phonenumber",
		        response);
		      String homeNumber = library.getTextFrom("DETAILS.homeNumber");
		      Assert.assertTrue(homeNumber.equals(homeNumberOfApiResponse), "Home number is not matched.");// Verify
		                              // phone
		                              // numbers
		                              // which
		                              // has
		                              // values
		                              // from
		                              // API
		                              // response
		                              // match
		                              // the
		                              // value
		                              // on
		                              // lead's
		                              // contact
		                              // page
		      contactList.add(contactType);
		      break;

		     case "other":
		      String otherNumberOfApiResponse = (String) library
		        .getValueFromJson("$.phone[" + i + "].phonenumber", response);
		      String otherNumber = library.getTextFrom("DETAILS.otherNumber").replace("\\p{P}", "").replace(" ", "");
		      Assert.assertTrue(otherNumber.equals(otherNumberOfApiResponse), "Other number is not matched.");// Verify
		                              // phone
		                              // numbers
		                              // which
		                              // has
		                              // values
		                              // from
		                              // API
		                              // response
		                              // match
		                              // the
		                              // value
		                              // on
		                              // lead's
		                              // contact
		                              // page
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
		    String value = library
		      .getTextFrom("xpath->//UIAStaticText[contains(@name,'" + contact + "')]/../UIAStaticText[2]");
		    Assert.assertEquals(value.equals("-"), true);// Verify phone numbers
		                // which has no
		                // values from API
		                // response has a
		                // "-" on the
		                // contact page

		    System.out.println(contactType);

	   }
	  }
	  
	// Created by Gopal
	  @Override
	  public void verifyOpportunitiesWithApi(Map<String, Object> data) {
		  setTokenAndUserId(data);
		   String response = getResponse((String) data.get("clientLeadApi2"));
		   System.out.println(response);
		   String opportunityTypeOfApiResponse = (String) library.getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityType", response);
		   String opportunityype=library.getTextFrom("TRANSACTION.opportunityType").split(" ")[0];
		   Assert.assertEquals(opportunityype, opportunityTypeOfApiResponse);// Lead Type matches the API response
		   String urgencyFromApi = (String) library.getValueFromJson("$.urgency", response);
		   String[] urg = urgencyFromApi.split(" ");
		   String urgencyLabelFromApi = urg[0];
		   System.out.println("\n" + urgencyLabelFromApi + "\n");
		   // Assert.assertTrue(library.verifyPageContainsElement("name->"+urgencyLabelFromApi,
		   // true));//Urgency matches the API response

		   String opportunityAreaOfApiResponse = (String) library
		     .getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityStage", response);
		   boolean isOfferedStage = library.verifyPageContainsElement(
		     "xpath->.//UIAStaticText[contains(@name,'" + opportunityAreaOfApiResponse + "')]");
		   Assert.assertTrue(isOfferedStage, "Offered stage is not matched");// Stage
		                    // is
		                    // Offered
		   //offer price does not exist for the particular API response.
	  }
	  
	  @Override // Gopal
	  public void verifyOpportunitiesOfMetStage(Map<String, Object> data) {
		  library.click("CLIENTLIST.Transaction");
			library.wait(5);
		  Assert.assertTrue(library.verifyPageContainsElement("name->Transactions (2)"));//verify transaction tab
		   String[]  splitStage1=library.getTextFrom("xpath->//UIAStaticText[contains(@name,'Met')][1]").split(" ");//
		   String[]  splitStage2=library.getTextFrom("xpath->//UIAStaticText[contains(@name,'Met')][2]").split(" ");
		   String Stage1=splitStage1[0];
		   String Stage2=splitStage2[0];
		         Assert.assertEquals(Stage1, "Met"); //verify stage is met
		         Assert.assertEquals(Stage2, "Met"); //verify stage is met
		         
		   setTokenAndUserId(data);
		   String response = getResponse((String) data.get("clientLeadApi3"));
		   System.out.println(response);
		   String opportunityTypeOfApiResponse1 = (String) library.getValueFromJson("$.opportunitiesSummary.openOpportunities[1].opportunityType", response);
		   String opportunitytype1=library.getTextFrom("//UIAStaticText[contains(@name,'Sell :')]").split(" ")[0];
		   Assert.assertEquals(opportunityTypeOfApiResponse1, opportunitytype1);//verify lead type is matched
		   String opportunityTypeOfApiResponse2 = (String) library.getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityType", response);
		   String opportunitytype2=library.getTextFrom("TRANSACTION.opportunityType").split(" ")[0];
		  Assert.assertEquals(opportunityTypeOfApiResponse2, opportunitytype2);//verify lead type is matched  
		   String urgencyFromApi = (String) library.getValueFromJson("$.urgency", response);
		   String[] urg = urgencyFromApi.split(" ");
		   String urgencyLabelFromApi = urg[0];
		   // Assert.assertTrue(library.verifyPageContainsElement("name->" +
		   // urgencyLabelFromApi, true));//Urgency of both transactions match the
		   // API response

		   String opportunityAreaOfApiResponse = (String) library
		     .getValueFromJson("$.opportunitiesSummary.openOpportunities[0].opportunityStage", response);
		   boolean isMetStage = library.verifyPageContainsElement(
		     "xpath->.//UIAStaticText[contains(@name,'" + opportunityAreaOfApiResponse + "')]");
		   Assert.assertTrue(isMetStage, "Met stage is not matched");// Stage for
		                  // both
		                  // transction
		                  // are Met
		   
		   
		   //could not find - for the price holder of the transactions
	  }
	  
	  @Override
	  public void verifyNotesForTalkedStage(Map<String, Object> data) {
		  String response = getResponse((String) data.get("notesApi"));
		   String noteDiscriptionofApiResponse = (String) library.getValueFromJson("$.activities[0].note", response);
		   System.out.println("\n" + noteDiscriptionofApiResponse + "\n");
		   String noteDetailPath = "xpath->(//UIAStaticText[contains(@name,'" + noteDiscriptionofApiResponse + "')])[1]";
		   Assert.assertTrue(library.verifyPageContainsElement(noteDetailPath));// Notes
		                      // are
		                      // displayed
		                      // in
		                      // chronological
		                      // order,
		                      // the
		                      // most
		                      // recent
		                      // note
		                      // is
		                      // on
		                      // top

		   String activityTypeOfApiResponse = (String) library.getValueFromJson("$.activities[0].activityType", response);
		   System.out.println("\n" + activityTypeOfApiResponse + "\n");
		   String[] ele = activityTypeOfApiResponse.split("-");
		   String activityTypePath = "xpath->(.//UIAStaticText[contains(@name,'" + ele[1].trim() + "')])[1]";

		   Assert.assertTrue(library.verifyPageContainsElement(activityTypePath));// The
		                       // latest
		                       // note's
		                       // activity
		                       // type
		                       // on
		                       // app
		                       // matches
		                       // the
		                       // API
		                       // response

		   library.click("NOTES.cancelbutton");


	  }
	  
	  @Override
	  public void verifyPropertiesOftalkedStage(Map<String, Object> data) {
		  library.wait(5);
		   library.click("TRANSACTION.properties");
		   library.wait(5);
		   setTokenAndUserId(data);
		   String response = getResponse((String) data.get("propertiesApi"));
		   Object propertyPriceOfApiResponse = library.getValueFromJson("$.properties[0].price", response);
		   Integer property = (Integer) propertyPriceOfApiResponse;
		   int price1 = (int) property;
		   library.wait(3);
		   // System.out.println(propertyPriceOfApiResponse);
		   String propertyPriceInApp = library.getTextFrom("MAKEOFFER.propertyPrice");
		   System.out.println(propertyPriceInApp);
		   propertyPriceInApp = propertyPriceInApp.replace("$", "");
		   propertyPriceInApp = propertyPriceInApp.replace(",", "");
		   int price2 = Integer.parseInt(propertyPriceInApp);
		   System.out.println(propertyPriceInApp);
//		   
//		   String MLSapi= (String) library.getValueFromJson("$.properties[0].mls", response);
//		 //  Assert.assertTrue(library.verifyPageContainsElement("name->MLS#: " +MLSapi+"", true)); //verify mls
//		   String leadmsgapi= (String) library.getValueFromJson("$.properties[0].message", response);
		   //Assert.assertTrue(library.verifyPageContainsElement("name->"+ leadmsgapi +"")); //verify leadmsg

		   Assert.assertTrue(price1 == price2, "Price is matched with api response");
	//
//		   Object numberOfBedsInApiResponse = (Object) library.getValueFromJson("$.properties[0].bed", response);
//		   Integer bedsInteger = (Integer) numberOfBedsInApiResponse;
//		   int numOfbedsInApiResponse = (int) (bedsInteger);
	//
//		   Object numberOfBathsInApiResponse = (Object) library.getValueFromJson("$.properties[0].bath", response);
//		   Integer bathsInteger = (Integer) numberOfBathsInApiResponse;
//		   int baths2 = (int) bathsInteger;
	//
//		   Object areaOfPropertyInApiResponse = (Object) library.getValueFromJson("$.properties[0].sqft", response);
//		   Integer areaIntger = (Integer) areaOfPropertyInApiResponse;
//		   int area2 = (int) areaIntger;
	//
//		   boolean isMatched = library.verifyPageContainsElement("xpath->.//UIAStaticText[contains(@value,'"
//		     + numOfbedsInApiResponse + " Bd | " + baths2 + " Ba | " + area2 + " Sq.Ft.')]");
//		   Assert.assertTrue(isMatched, "Number of baths, beds and area are matched with api response");
	  }
	  
	  @Override
	  public void setFutureDateUsingCalander(Map<String, Object> data) {
			
			

		}
	  
	  @Override
	  public void setFutureDateUsingCalanderForFunctional(Map<String, Object> data) {
		  
		  for (int k = 1; k <= 1; k++) {
				Calendar currentCal = Calendar.getInstance();
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				currentCal.add(Calendar.DATE, k);
				currentCal.add(Calendar.DATE, k);
				String endDate = dateFormat.format(currentCal.getTime()).split("/")[1];
				library.wait(2);
				library.click("xpath->//UIAApplication[1]/UIAWindow[1]/UIAStaticText[3]");
				library.wait(3);
				library.typeDataInto(endDate, "xpath->//UIAApplication[1]/UIAWindow[1]/UIAPicker[1]/UIAPickerWheel[1]");
				library.wait(3);
				library.click("FLEAVE.schedulebutton");
				
			}
	  }
	  
	  @Override
	  public void navigateToNotes() {
			library.wait(3);
			library.click("CLIENTLIST.Transaction");
			library.wait(5);
			library.click("URGENCY.notes");
			library.wait(2);
		}
	  
	  
	  
	  
	  
	  
	  
	  
}
