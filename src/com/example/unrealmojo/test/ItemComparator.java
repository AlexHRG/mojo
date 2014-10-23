package com.example.unrealmojo.test;

import java.util.Comparator;
import java.util.Map;


public class ItemComparator implements Comparator<Map<String, String>> {

	@Override
	public int compare(Map<String, String> left, Map<String, String> right) {
		if (left == right) return 0;
		if (left == null) return 1;
		if (right == null) return -1;

		int result = right.get(MainActivity.TAG_PINNED).compareTo(left.get(MainActivity.TAG_PINNED));
		
		if (result != 0)
			return result;

		return (left.get(MainActivity.TAG_TITLE)).compareTo(right.get(MainActivity.TAG_TITLE));

	}

}
