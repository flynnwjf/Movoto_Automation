package com.movoto.fixtures;

import java.util.Map;

public interface UtilityFixtures extends DBFixtures {
	
	public Map<String, String> loadObjectRepository(String filePath);

	public void openExcelSheet(String path, String sheet, String mode);

	public String getFromExcelRowAndColumn(int row, String header);

	public void writeToExcel(String data, int row, int column);

	public void closeExcelSheet(String path, String sheet, String mode);

	public int getExcelRowNumberForKey(String key);

	public int getExcelRowCount();

	public boolean deleteRowForKey(String key);

	public Object getValueFromJson(String jsonPath, String json);

	public boolean wait(int seconds);

	public void setCurrentTestMethod(String name);

	public String getCurrentPlatform();

	public String getCurrentDate();

	String getCurrentBrowser();

	public String getBrowserName();

	public String getCurrentOS();

	public String getCurrentPlatformType();

	public boolean checkFileInFileSystem(String filepath);

}
