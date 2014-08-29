package com.example.unrealmojo.test;

import java.util.Comparator;
import java.util.Map;


public class HamsterComparator implements Comparator<Map<String, String>> {
	private static final String TAG_TITLE = "title";
	private static final String TAG_PINNED = "pinned";

	@Override
	public int compare(Map<String, String> left, Map<String, String> right) {
		if (left == null) return 1;
		if (right == null) return -1;
		if (left.get(TAG_PINNED).equals("1") && right.get(TAG_PINNED).equals("0")) return -1;
		if (left.get(TAG_PINNED).equals("0") && right.get(TAG_PINNED).equals("1")) return 1;

		return (left.get(TAG_TITLE).toString()).compareTo(right.get(TAG_TITLE).toString());

	}

}
