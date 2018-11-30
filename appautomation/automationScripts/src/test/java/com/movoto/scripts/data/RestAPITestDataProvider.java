package com.movoto.scripts.data;

import java.io.IOException;
import java.util.Map;

import org.testng.annotations.DataProvider;

import com.movoto.common.ExcelFileManager;
import com.movoto.fixtures.impl.util.FileUtil;

public class RestAPITestDataProvider {
	
	private static final String JSON_DATA_FILE_PATH = "data/RestAPI.properties";

	/**
	 * @return
	 */
	@DataProvider(name="POST_DATA")
	public static Object[][] restAPIData() {
		
		Object[][] obj =  new Object[10][1];
		try {
//			Map map = FileUtil.readFileAsMap(JSON_DATA_FILE_PATH);
//			if (map != null) {
//				Object[][] obj = { { "1", 1 }, {"2", 2} };
//				return obj;
			
			for(int i=0;i<10;i++){
				obj[i][0] = i;
			return obj;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
