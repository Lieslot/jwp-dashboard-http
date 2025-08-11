package org.apache.servlet.common;

import java.util.HashMap;
import java.util.Map;

public class Cookie {

	private static final String SEPARATOR = "; ";

	private final Map<String, String> properties;

	public Cookie(Map<String, String> properties) {
		this.properties = properties;
	}

	public static Cookie from(String cookieValues) {
		String[] keyValues = cookieValues.split(SEPARATOR);
		Map<String, String> properties = new HashMap<>();

		for (String value : keyValues) {
			String[] keyValue = value.split("=");

			properties.put(keyValue[0], keyValue[1]);
		}

		return new Cookie(properties);
	}

	public String get(String key) {
		return properties.get(key);
	}

}
