package com.mealam.showdown.utils.json;

public class JSONFormatUtils {

	public static String createJsonMessage(String pKey, String pValue) {
		return String.format("{\"%s\":\"%s\"}", pKey, pValue);
	}

	public static String escapeJson(String pInput) {
		return pInput
				.replace("\\", "\\\\")
				.replace("\"", "\\\"")
				.replace("\n", "\\n")
				.replace("\r", "\\r")
				.replace("\t", "\\t");
	}
}
