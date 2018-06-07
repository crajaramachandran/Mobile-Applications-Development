package com.example.raja.questhunt;

/**
 * Created by Raja on 4/7/2018.
 */

public class Quest {

    private int id;
    private String quest_name;
    private int quest_xp;
    private int quest_difficulty;
    private String quest_skill;
    private String quest_deadline;

    public Quest(int id, String quest_name, int quest_xp, int quest_difficulty, String quest_skill, String quest_deadline){
        this.id = id;
        this.quest_name = quest_name;
        this.quest_difficulty = quest_difficulty;
        this.quest_xp = quest_xp;
        this.quest_skill = quest_skill;
        this.quest_deadline = quest_deadline;
    }

    public int getId(){return id;}

    public String getQuest_name(){
        return quest_name;
    }

    public void setQuest_name(String quest_name){
        this.quest_name = quest_name;
    }

    public int getQuest_xp(){return quest_xp;}

    public int getQuest_difficulty(){return quest_difficulty;}

    public String getQuest_skill(){return quest_skill;}

    public String getQuest_deadline() {return quest_deadline;}

    @Override
    public String toString(){
        return "quest_name";
    }

}
