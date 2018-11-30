package com.movoto.scripts.data;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import com.movoto.fixtures.FixtureLibrary;

import groovy.json.JsonException;

public class TransactionDataProvider {

	@DataProvider(name = "ContractCancelledStageTestData")
	public static Object[][] ContractCancelledStageDataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "ContractCancelledStage", "read");
		int rowCount = library.getExcelRowCount();
		library.closeExcelSheet(path, "ContractCancelledStage", "read");
		Object[][] data = new Object[rowCount-1][1];
		for (int i = 0; i < rowCount-1; i++) {
			int j = i + 1;
			library.openExcelSheet(path, "ContractCancelledStage", "read");
			String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
			String presentStage = library.getFromExcelRowAndColumn(j, "PresentStage");
			String postStage = library.getFromExcelRowAndColumn(j, "PostStage");
			String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");
			library.closeExcelSheet(path, "ContractCancelledStage", "read");
			Map<String, Object> map = new HashMap<>();

			if (updateTransactionStage.contains("Offer")) {
				Map<String, String> dataMap = getDataForMadeOfferTransaction(path, "Make Offer", library);
				map.put("MadeOfferData", dataMap);
			}

			map.put("clientName", clientName);
			map.put("presentStage", presentStage);
			map.put("updateTransactionStage", updateTransactionStage);
			map.put("postStage", postStage);
			try {
				data[i][0] = map;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return data;

	}

	private static Map<String, String> getDataForMadeOfferTransaction(String path, String sheet,
			FixtureLibrary library) {

		library.openExcelSheet(path, sheet, "read");

		String clientName = library.getFromExcelRowAndColumn(1, "ClientName");
		String address = library.getFromExcelRowAndColumn(1, "Address");
		String offerDate = library.getFromExcelRowAndColumn(1, "Offer Date");
		String offerPrice = library.getFromExcelRowAndColumn(1, "Offer Price");
		String preStage = library.getFromExcelRowAndColumn(1, "Pre-Stage");
		String postStage = library.getFromExcelRowAndColumn(1, "Post-Stage");

		library.closeExcelSheet(path, sheet, "read");

		Map<String, String> map = new HashMap<>();

		map.put("ClientName", clientName);
		map.put("Address", address);
		map.put("OfferDate", offerDate);
		map.put("OfferPrice", offerPrice);
		map.put("preStage", preStage);
		map.put("postStage", postStage);
		return map;
	}

}
