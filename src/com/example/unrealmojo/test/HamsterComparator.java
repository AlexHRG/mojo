package com.example.unrealmojo.test;

import java.util.Comparator;
import java.util.Map;


public class HamsterComparator implements Comparator<Map<String, String>> {
	private static final String TAG_TITLE = "title";

	@Override
	public int compare(Map<String, String> left, Map<String, String> right) {
		if (left == null) return 1;
		if (right == null) return -1;
		return (left.get(TAG_TITLE).toString()).compareTo(right.get(TAG_TITLE).toString());

	}

}
