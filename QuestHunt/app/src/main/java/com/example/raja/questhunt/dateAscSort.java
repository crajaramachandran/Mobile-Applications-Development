package com.example.raja.questhunt;

import java.util.Comparator;

public class dateAscSort implements Comparator<Quest>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Quest a, Quest b)
    {
        String aStr = a.getQuest_deadline();
        String bStr = b.getQuest_deadline();
        int one;
        int two;

        if(aStr.equals("Today")){
            one =0;
        }
        else{
            String aParts[] = aStr.split(" ");
            one = Integer.parseInt(aParts[0]);
        }

        if(bStr.equals("Today")){
            two=0;
        }
        else{
            String bParts[] = bStr.split(" ");
            two = Integer.parseInt(bParts[0]);
        }

        return one - two;
    }
}
