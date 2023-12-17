package com.focusedapp.smartstudyhub.util.comparator;

import java.util.Comparator;

public class SortByProjectNameComparator implements Comparator<String> {
	
	private final String exception;
	
	public SortByProjectNameComparator(String exception) {
		this.exception = exception;
	}
	
	@Override
    public int compare(String objectFirst, String objectSecond)
    {
        if (objectFirst.equals(this.exception))
        {
            if (objectSecond != exception)
            {
                return -1;
            }
        }
        else if (objectSecond.equals(this.exception))
        {
            return 1;
        }

        return objectFirst.compareTo(objectSecond);
    }
}
