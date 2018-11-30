package com.movoto.data.impl;

import java.util.HashMap;
import java.util.Map;

import com.movoto.data.TestParameters;

public class TestParametersImpl implements TestParameters {

	private Map<String, String> map = new HashMap<>();

	@Override
	public void setAll(Map<String, String> parameters) {
		if (parameters != null) {
			this.map.putAll(parameters);
		}
	}

	@Override
	public void setAll(Object[] keys, Object[] values) {
		if ((keys != null && values != null) && (keys.length == values.length)) {
			for (int i = 0; i < keys.length; i++) {
				Object key = keys[i];
				Object value = values[i];
				if (key != null && value != null) {
					map.put(key.toString(), value.toString());
				}

			}
		}
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Map<String, String> getAll() {
		return this.map;
	}

	@Override
	public void set(String key, String value) {
		map.put(key, value);
	}

	@Override
	public String get(String key) {
		return map.get(key);
	}

}
