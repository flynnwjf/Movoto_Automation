package com.movoto.data;

import java.util.Map;

import com.movoto.data.impl.Locator;

public interface ObjectRepository {
	
	void setAll(Map<String, Locator> objectRepository);

	void clear();

	Map<String, Locator> getAll();

	void set(String key, Locator value);

	Locator get(String key);

}
