package com.example.unrealmojo.test;

import java.util.Comparator;
import java.util.Map;


public class ItemComparator implements Comparator<Map<String, String>> {

	@Override
	public int compare(Map<String, String> left, Map<String, String> right) {
		if (left == null) return 1;
		if (right == null) return -1;
//		if (left.get(MainActivity.TAG_PINNED).equals("1") && right.get(MainActivity.TAG_PINNED).equals("0")) return -1;
//		if (left.get(MainActivity.TAG_PINNED).equals("0") && right.get(MainActivity.TAG_PINNED).equals("1")) return 1;
		
		int leftValue = Integer.valueOf(left.get(MainActivity.TAG_PINNED));
		int rightValue = Integer.valueOf(right.get(MainActivity.TAG_PINNED));
		int result = rightValue - leftValue;
		
		if (result != 0)
			return result;

		return (left.get(MainActivity.TAG_TITLE).toString()).compareTo(right.get(MainActivity.TAG_TITLE).toString());

	}

}
