package com.example.raja.questhunt;

import java.util.Comparator;

public class rowIDSort implements Comparator<Quest>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Quest a, Quest b)
    {
        int one = a .getId();
        int two = b.getId();

        return one - two;
    }
}
