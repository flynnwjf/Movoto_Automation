package com.movoto.data.impl;

import com.movoto.data.TestProperties;

public class TestPropertiesImpl implements TestProperties {

	private String platformName;
	private String applicationPath;
	private String operatingSystemName;
	private String testConfigurationPath;
	private String testName;
	private String objectRepositoryPath;
	private String dataSourcePath;
	private String dataBaseURL;
	private String versionID;
	private String deviceName;
	private String udid;
	private String browserName;
	private String URL;
	private String platformType;
	private String driverPath;
	private String dbConfigPath;
	private boolean stepScreenshotEnabled;
	private boolean assertScreenshotEnabled;
	private boolean autoAcceptAlertsDisabled;
	private boolean fullResetDisabled;
	private String dataFilePath;
	
	@Override
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	@Override
	public void setOperatingSystemName(String operatingSystemName) {
		// TODO Auto-generated method stub
		this.operatingSystemName = operatingSystemName;
	}

	@Override
	public String getOperatingSystemName() {
		// TODO Auto-generated method stub
		return operatingSystemName;
	}

	@Override
	public void setTestConfigurationPath(String testConfigurationPath) {
		// TODO Auto-generated method stub
		this.testConfigurationPath = testConfigurationPath;
	}

	@Override
	public String getTestConfigurationPath() {
		// TODO Auto-generated method stub
		return testConfigurationPath;
	}

	@Override
	public void setTestName(String testName) {
		// TODO Auto-generated method stub
		this.testName = testName;
	}

	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return testName;
	}

	@Override
	public void setObjectRepositoryPath(String objectRepositoryPath) {
		// TODO Auto-generated method stub
		this.objectRepositoryPath = objectRepositoryPath;
	}

	@Override
	public String getObjectRepositoryPath() {
		// TODO Auto-generated method stub
		return objectRepositoryPath;
	}

	@Override
	public void setVersionID(String version) {
		// TODO Auto-generated method stub
		this.versionID = version;
	}

	@Override
	public String getVersionID() {
		// TODO Auto-generated method stub
		return versionID;
	}

	@Override
	public void setDataSourcePath(String dataSourcePath) {
		// TODO Auto-generated method stub
		this.dataSourcePath = dataSourcePath;
	}

	@Override
	public String getDataSourcePath() {
		// TODO Auto-generated method stub
		return dataSourcePath;
	}

	@Override
	public void setDataBaseURL(String dataBaseURL) {
		// TODO Auto-generated method stub
		this.dataBaseURL = dataBaseURL;
	}

	@Override
	public String getDataBaseURL() {
		// TODO Auto-generated method stub
		return dataBaseURL;
	}

	@Override
	public String getPlatformName() {
		// TODO Auto-generated method stub
		return platformName;
	}

	@Override
	public void setApplicationPath(String applicationPath) {
		this.applicationPath = applicationPath;

	}

	@Override
	public String getApplicationPath() {
		// TODO Auto-generated method stub
		return applicationPath;
	}

	@Override
	public void setDeviceName(String deviceName) {
		// TODO Auto-generated method stub
		this.deviceName = deviceName;
	}

	@Override
	public String getDeviceName() {
		// TODO Auto-generated method stub
		return deviceName;
	}

	@Override
	public void setUDID(String udid) {
		// TODO Auto-generated method stub
		this.udid = udid;
	}

	@Override
	public String getUDID() {
		// TODO Auto-generated method stub
		return udid;
	}

	@Override
	public void setBrowserName(String browserName) {
		// TODO Auto-generated method stub
		this.browserName = browserName;
	}

	@Override
	public String getBrowserName() {
		// TODO Auto-generated method stub
		return browserName;
	}

	@Override
	public void setRemoteURL(String URL) {
		// TODO Auto-generated method stub
		this.URL =  URL;
	}

	@Override
	public String getRemoteURL() {
		// TODO Auto-generated method stub
		return URL;
	}

	@Override
	public void setPlatformType(String type) {
		// TODO Auto-generated method stub
		this.platformType = type;
	}

	@Override
	public String getPlatformType() {
		// TODO Auto-generated method stub
		return platformType;
	}

	@Override
	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}

	@Override
	public String getDriverPath() {
		// TODO Auto-generated method stub
		return driverPath;
	}

	@Override
	public void setDatabaseConfigPath(String dbConfigPath) {
		this.dbConfigPath = dbConfigPath;
		
	}

	@Override
	public String getDatabaseConfigPath() {		
		return this.dbConfigPath;
	}

	@Override
	public boolean isStepScreenshotEnabled() {
		return stepScreenshotEnabled;
	}

	@Override
	public void setStepScreenshotEnabled(boolean flag) {
		stepScreenshotEnabled = flag;		
	}

	@Override
	public boolean isAssertScreenshotEnabled() {
		return assertScreenshotEnabled;
	}

	@Override
	public void setAssertScreenshotEnabled(boolean flag) {
		assertScreenshotEnabled = flag;	
	}

	@Override
	public boolean isAutoAcceptAlertsDisabled() {
		return this.autoAcceptAlertsDisabled;
	}

	@Override
	public void setAutoAcceptAlerts(boolean flag) {
		this.autoAcceptAlertsDisabled = flag;
	}

	@Override
	public boolean isFullResetDisabled() {
		return fullResetDisabled;
	}

	@Override
	public void setFullReset(boolean flag) {
		this.fullResetDisabled = flag;
	}
	
	@Override
	public String getTestDataFilePath() {
		return dataFilePath;
	}
	
	@Override
	public void setDataFilePath(String path) {
		this.dataFilePath = path;	
	}

	@Override
	public String getCurrentOS() {
		// TODO Auto-generated method stub
		return null;
	}

}
