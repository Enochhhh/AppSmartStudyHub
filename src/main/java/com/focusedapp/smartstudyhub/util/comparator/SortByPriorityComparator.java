package com.focusedapp.smartstudyhub.util.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.focusedapp.smartstudyhub.util.enumerate.EnumPriority;

public class SortByPriorityComparator implements Comparator<String> {

	@Override
    public int compare(String objectFirst, String objectSecond)
    {
		Map<String, Integer> value = new HashMap<>();
		value.put(EnumPriority.HIGH.getValue(), 0);
		value.put(EnumPriority.NORMAL.getValue(), 1);
		value.put(EnumPriority.LOW.getValue(), 2);
		value.put(EnumPriority.NONE.getValue(), 3);		       

        return value.get(objectFirst).compareTo(value.get(objectSecond));
    }
}
