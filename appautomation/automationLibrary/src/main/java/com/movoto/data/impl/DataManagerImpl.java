package com.movoto.data.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import com.movoto.data.DataManager;
import com.movoto.data.ObjectRepository;
import com.movoto.data.TestDTO;
import com.movoto.data.TestParameters;
import com.movoto.data.TestProperties;
import com.movoto.fixtures.FileType;
import com.movoto.fixtures.impl.util.FileUtil;

public class DataManagerImpl extends DataManager {

	private TestDTO dto;
	private ObjectRepository or;
	private String testName;
	private String configFilePath;
	private TestProperties properties;
	private TestParameters parameters;

	@Override
	public synchronized TestDTO populateTestDataForTest(String testName) {
		if (testName != null) {
			this.testName = testName;
			try {
				populateTestProperties();
				populateObjectRepository();
				populateTestDataObject();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DataManager.setData(dto);
		return dto;
	}

	private void populateTestProperties() throws IOException {
		String userDir = System.getProperty("user.dir");
		System.out.println("User Dir: " + userDir);
		
		configFilePath = userDir + "/config/COMMON_CONFIGURATION" + "." + FileType.FILE_TYPE_PROPERTIES;
		Map<String, String> pMap = FileUtil.readPropertiesFile(configFilePath);

		String testConfigFilePath = userDir + "/config/" + testName + "." + FileType.FILE_TYPE_PROPERTIES;
		File file = new File(testConfigFilePath);

		if (file.exists()) {
			Map<String, String> tcMap = FileUtil.readPropertiesFile(testConfigFilePath);
			if (tcMap != null) {
				for (String key : tcMap.keySet()) {
					String value = tcMap.get(key);
					if (value != null && value.length() > 0) {
						pMap.put(key, value);
					} 
				}
			}
		}

		if (pMap != null) {
			properties = new TestPropertiesImpl();
			properties.setDataSourcePath(pMap.get(TestProperties.TEST_DATA_SOURCE_PATH));
			properties.setObjectRepositoryPath(pMap.get(TestProperties.TEST_OBJECT_REPO_PATH));
			properties.setTestConfigurationPath(configFilePath);
			properties.setOperatingSystemName(TestProperties.TEST_OS);
			properties.setTestName(testName);
			properties.setDataBaseURL(pMap.get(TestProperties.TEST_DB_URL));
			properties.setApplicationPath(pMap.get(TestProperties.TEST_APPLICATION_PATH));
			properties.setBrowserName(pMap.get(TestProperties.TEST_BROWSER_NAME));
			properties.setDeviceName(pMap.get(TestProperties.TEST_DEVICE_NAME));
			properties.setPlatformName(pMap.get(TestProperties.TEST_PLATFORM_NAME));
			properties.setPlatformType(pMap.get(TestProperties.TEST_PLATFORM_TYPE));
			properties.setRemoteURL(pMap.get(TestProperties.TEST_REMOTE_URL));
			properties.setUDID(pMap.get(TestProperties.TEST_UDID));
			properties.setVersionID(pMap.get(TestProperties.TEST_PLATFORM_VERSION));
			properties.setDriverPath(pMap.get(TestProperties.TEST_DRIVER_PATH));
			properties.setStepScreenshotEnabled(getBooleanValueFor(pMap.get(TestProperties.TEST_STEP_SCREENSHOT_ENABLED)));
			properties.setAssertScreenshotEnabled(getBooleanValueFor(pMap.get(TestProperties.TEST_ASSERT_SCREENSHOT_ENABLED)));
			properties.setAutoAcceptAlerts(getBooleanValueFor(pMap.get(TestProperties.TEST_DISABLE_AUTO_ACCEPT_ALERTS_IOS)));
			properties.setFullReset(getBooleanValueFor(pMap.get(TestProperties.TEST_DISABLE_FULL_RESET)));
			properties.setDataFilePath(pMap.get(TestProperties.TEST_DATA_PATH));
		}
	}

	private void populateObjectRepository() throws IOException {
		if (properties != null) {
			Map<String, Locator> uiMap = ORManager.getORManager().populateLocators(properties.getObjectRepositoryPath());
			or = new ObjectRepositoryImpl();
			or.setAll(uiMap);
		}
	}

	private void populateTestDataObject() {
		if (properties != null) {
			dto = new TestDTOImpl();
			dto.setTestProperties(properties);
			if (parameters != null) {
				dto.setTestParameters(parameters);
			}
			if (or != null) {
				dto.setObjectRepository(or);
			}
		}
	}

	private boolean getBooleanValueFor(String value) {
		if (value != null) {
			try {
				boolean b = Boolean.valueOf(value);
				return b;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

}
