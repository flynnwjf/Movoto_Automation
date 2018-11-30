package com.movoto.data.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.movoto.fixtures.impl.util.FileUtil;

public class ORManager {

	private Map<String, Locator> locatorMap = null;

	public static ORManager getORManager() {
		return new ORManager();
	}

	public ORManager() {
		locatorMap = new HashMap<>();
	}
	
	public Map<String, Locator> populateLocators(String path) throws IOException {
		  Map<String, String> map = FileUtil.readFileAsMap(path);
		  String[] locatorStr = null;
		  String type = null, value = null;
		  if (map != null) {
			  for (String key : map.keySet()) {
				  String valueStr = map.get(key);
				  if (valueStr != null && valueStr.length()>0&&valueStr.contains("->"))
				  {
					  locatorStr = valueStr.split("->");
					  type = locatorStr[0];
					  value = locatorStr[1];
				  }
				  else
				  {
					  value=valueStr;
					  type="xpath";
				  }
				  Locator locator = new Locator();
				  locator.setName(key);
				  locator.setType(type);
				  locator.setValue(value);
				  locatorMap.put(key, locator);
			  }
		  }
		  return locatorMap;
	}

	public Map<String, Locator> getLocatorMap() {
		return locatorMap;
	}

}
