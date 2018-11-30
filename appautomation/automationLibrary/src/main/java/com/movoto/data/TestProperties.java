package com.movoto.data;

/**
 * Test Properties interface to access the thread specific test configuration details
 */

public interface TestProperties {

	String TEST_PLATFORM_TYPE = "PLATFORM_TYPE";
	String TEST_OS = "OS";
	String TEST_CONFIG_PATH = "CONFIGURATION_FILE_PATH";
	String TEST_OBJECT_REPO_PATH = "OBJECT_REPOSITORY_FILE_PATH";
	String TEST_DATA_SOURCE_PATH = "TEST_DATA_FILE_PATH";
	String TEST_DB_URL = "DATABASE_URL";
	String TEST_PLATFORM_VERSION = "PLATFORM_VERSION";
	String TEST_PLATFORM_NAME = "PLATFORM_NAME";
	String TEST_DEVICE_NAME = "DEVICE_NAME";
	String TEST_REMOTE_URL = "REMOTE_URL";
	String TEST_BROWSER_NAME = "BROWSER_NAME";
	String TEST_APPLICATION_PATH = "APPLICATION";
	String TEST_UDID = "UDID";
	String TEST_DRIVER_PATH = "DRIVER_PATH";
	String TEST_DB_CONFIG_PATH = "DB_CONFIG_PATH";
	String TEST_STEP_SCREENSHOT_ENABLED = "STEP_SCREENSHOT_ENABLED";
	String TEST_ASSERT_SCREENSHOT_ENABLED = "ASSERT_SCREENSHOT_ENABLED";
	String TEST_DISABLE_AUTO_ACCEPT_ALERTS_IOS = "DISABLE_AUTO_ACCEPT_ALERTS_IOS";
	String TEST_DISABLE_FULL_RESET = "DISABLE_FULL_RESET";
	String TEST_DATA_PATH = "TEST_DATA_FILE_PATH";

	void setPlatformName(String platformName);

	String getPlatformName();

	void setOperatingSystemName(String operatingSystemName);

	String getOperatingSystemName();

	void setTestConfigurationPath(String testConfigurationPath);

	String getTestConfigurationPath();

	void setTestName(String testName);

	String getTestName();

	void setObjectRepositoryPath(String objectRepositoryPath);

	String getObjectRepositoryPath();

	void setVersionID(String version);

	String getVersionID();

	void setDataSourcePath(String dataSourcePath);

	String getDataSourcePath();

	void setDataBaseURL(String dataBaseURL);

	String getDataBaseURL();

	void setApplicationPath(String applicationPath);

	String getApplicationPath();

	void setDeviceName(String deviceName);

	String getDeviceName();

	void setUDID(String udid);

	String getUDID();

	void setBrowserName(String browserName);

	String getBrowserName();

	void setRemoteURL(String URL);

	String getRemoteURL();

	void setPlatformType(String type);

	String getPlatformType();

	void setDriverPath(String driverPath);

	String getDriverPath();

	void setDatabaseConfigPath(String dbConfigPath);

	String getDatabaseConfigPath();

	boolean isStepScreenshotEnabled();

	void setStepScreenshotEnabled(boolean flag);

	boolean isAssertScreenshotEnabled();

	void setAssertScreenshotEnabled(boolean flag);

	boolean isAutoAcceptAlertsDisabled();

	void setAutoAcceptAlerts(boolean flag);

	boolean isFullResetDisabled();

	void setFullReset(boolean flag);
	
	String getTestDataFilePath();
	
	void setDataFilePath(String path);
	
	String getCurrentOS();	

}
