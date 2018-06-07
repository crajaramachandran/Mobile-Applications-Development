package com.example.raja.questhunt;

import java.util.Comparator;

public class difficultyAscSort implements Comparator<Quest>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Quest a, Quest b)
    {

        int one = a .getQuest_difficulty();
        int two = b.getQuest_difficulty();
        int res = one - two;
        if(res == 0){
            return deadlineCompare(a.getQuest_deadline(),b.getQuest_deadline());
        }

        return one - two;
    }

    public int deadlineCompare(String a, String b){
        int one;
        int two;
        if(a.equals("Today")){
            one = 0;
        }
        else{
            String aParts[] = a.split(" ");
            one = Integer.parseInt(aParts[0]);
        }
        if(b.equals("Today")){
            two = 0;
        }
        else{
            String bParts[] = b.split(" ");
            two = Integer.parseInt(bParts[0]);
        }

        return two - one;
    }
}
