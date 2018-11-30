package com.movoto.common;

import com.jayway.jsonpath.JsonPath;

public class JSONManager {
	
	public synchronized static Object getValueFromJson(String jsonPath, String json) {
		return JsonPath.parse(json).read(jsonPath);
	}

	public synchronized static <T> Object getValueOfTypeFromJson(String jsonPath, Class<T> type, String json) {
		return JsonPath.parse(json).read(jsonPath, type);
	}
	
}
