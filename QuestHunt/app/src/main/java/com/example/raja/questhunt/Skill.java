package com.example.raja.questhunt;

/**
 * Created by Raja on 4/7/2018.
 */

public class Skill {

    private String skill_name;
    private int skill_xp;
    private int skill_level;
    private int skill_uid;

    public Skill(String skill_name, int skill_xp, int skill_level, int skill_uid){
        this.skill_name = skill_name;
        this.skill_xp = skill_xp;
        this.skill_level = skill_level;
        this.skill_uid = skill_uid;
    }

    public String getSkill_name(){
        return skill_name;
    }

    public int getSkill_xp(){return skill_xp;}

    public int getSkill_level(){return skill_level;}

    public int getSkill_uid(){return skill_uid;}

    public void setSkill_name(String skill_name) { this.skill_name = skill_name; }

    public void setSkill_xp(int skill_xp){this.skill_xp = skill_xp;}

    public void setSkill_level(int skill_level){this.skill_level = skill_level;}

    public void setSkill_uid(int skill_uid){this.skill_uid = skill_uid;}

    @Override
    public String toString(){
        return skill_name;
    }

}
