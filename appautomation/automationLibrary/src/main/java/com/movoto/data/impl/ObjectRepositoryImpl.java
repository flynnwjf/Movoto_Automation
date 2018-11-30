package com.movoto.data.impl;

import java.util.HashMap;
import java.util.Map;
import com.movoto.data.ObjectRepository;

public class ObjectRepositoryImpl implements ObjectRepository {

	private Map<String, Locator> orMap = new HashMap<>();

	@Override
	public void setAll(Map<String, Locator> objectRepository) {
		if (objectRepository != null) {
			this.orMap.putAll(objectRepository);
		}
	}

	@Override
	public void clear() {
		this.orMap.clear();
	}

	@Override
	public Map<String, Locator> getAll() {
		return orMap;
	}

	@Override
	public void set(String key, Locator value) {
		this.orMap.put(key, value);
	}

	@Override
	public Locator get(String key) {
		return this.orMap.get(key);
	}

}
