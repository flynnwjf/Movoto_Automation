package com.movoto.data;

import java.util.Map;

public interface TestParameters {

	void setAll(Map<String, String> parameters);

	void setAll(Object[] keys, Object[] values);

	void clear();

	Map<String, String> getAll();

	void set(String key, String value);

	String get(String key);
	
}
