package com.example.raja.questhunt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseWrapper{

    public static final String QUESTS_ROW_ID ="quests_row_id";
    public static final String QUESTS="quests";
    public static final String QUEST_NAME="quest_name";
    public static final String QUEST_XP="quest_xp";
    public static final String QUEST_DIFF="quest_diff";
    //public static final String QUEST_REP_CNT="quest_rep_cnt";
    public static final String SKILL_NAME_REF="skill_name_ref";
    public static final String QUEST_DEADLINE="quest_deadline";
    public static final String QUEST_COMPLETED_FLAG="quest_completed_flag";

    public static final int COL_QUEST_ROW_ID=0;
    public static final int COL_QUEST_NAME=1;
    public static final int COL_QUEST_XP=2;
    public static final int COL_QUEST_DIFF=3;
    //public static final int COL_QUEST_REP_CNT=3;
    public static final int COL_SKILL_NAME_REF=4;
    public static final int COL_QUEST_DEADLINE=5;
    public static final int COL_QUEST_COMPLETED=6;



    public static final String SKILLS="skills";
    public static final String SKILL_NAME="skill_name";
    public static final String SKILL_XP="skill_xp";
    public static final String SKILL_LEVEL="skill_level";
    public static final String SKILL_UID="skill_uid";

    public static final int COL_SKILL_NAME=0;
    public static final int COL_SKILL_XP=1;
    public static final int COL_SKILL_LEVEL=2;
    public static final int COL_SKILL_UID=3;

    public static final String COM_QUESTS="com_quests";
    public static final String COM_QUEST_NAME="com_quest_name";
    public static final String COM_QUEST_XP="com_quest_xp";
    public static final String COM_QUEST_DIFF="com_quest_diff";
    public static final String COM_SKILL_NAME="com_skill_name";

    public static final int COL_COM_QUEST_NAME=0;
    public static final int COL_COM_QUEST_XP=1;
    public static final int COL_COM_QUEST_DIFF=2;
    public static final int COL_COM_SKILL_NAME_REF=3;


    public static final String MASTER="master";
    public static final String MASTER_XP="master_xp";
    public static final String MASTER_LEVEL="master_level";


    public static final int COL_MASTER_XP=0;
    public static final int COL_MASTER_LEVEL=1;


    public static final String[] QUEST_ALL_KEYS=new String[]{QUEST_NAME,QUEST_XP,QUEST_DIFF,SKILL_NAME_REF};
    public static final String[] SKILL_ALL_KEYS=new String[]{SKILL_NAME,SKILL_XP,SKILL_LEVEL};
    public static final String[] COM_QUEST_ALL_KEYS=new String[]{COM_QUEST_NAME,COM_QUEST_XP,COM_QUEST_DIFF,COM_SKILL_NAME};
    public static final String[] MASTER_ALL_KEYS=new String[]{MASTER_XP,MASTER_LEVEL};


    private static final String DATABASE_NAME="EQA.db";
    private static final int DATABASE_VERSION=1;

    //TABLE CREATE STATEMENT
    private static final String QUEST_CREATE= "create table if not exists "+QUESTS
            +"("
            +QUESTS_ROW_ID+" integer,"
            +QUEST_NAME+" text not null,"
            +QUEST_XP+" integer,"
            +QUEST_DIFF+" integer,"
            //+QUEST_REP_CNT+" integer,"
            +SKILL_NAME_REF+" text,"
            +QUEST_DEADLINE+" text,"
            +QUEST_COMPLETED_FLAG+" text,"
            +"primary key("+QUESTS_ROW_ID+","+SKILL_NAME_REF+")"
            +");";

    private static final String SKILL_CREATE="create table if not exists "+SKILLS
            +"("
            +SKILL_NAME+" text not null,"
            +SKILL_XP+" integer,"
            +SKILL_LEVEL+" integer,"
            +SKILL_UID+" integer,"
            +"primary key("+SKILL_NAME+")"
            +");";


    private static final String COM_QUEST_CREATE= "create table if not exists "+COM_QUESTS
            +"("
            +COM_QUEST_NAME+" text not null,"
            +COM_QUEST_XP+" integer,"
            +COM_QUEST_DIFF+" integer,"//OR COLOR,MAYBE?WILL SEE
            +COM_SKILL_NAME+" text);";

    private static final String MASTER_CREATE="create table if not exists "+MASTER
            +"("
            +MASTER_XP+" integer,"
            +MASTER_LEVEL+" integer);";


    private final Context context;
    private DataBaseHelper myDBHelper;
    private SQLiteDatabase db;

    public DataBaseWrapper(Context ctx)
    {
        this.context=ctx;
        myDBHelper=new DataBaseHelper(context);
    }

    public DataBaseWrapper open() {
        db=myDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }

    public void testSQL()
    {
        db.execSQL("DROP TABLE IF EXISTS "+QUESTS);
        db.execSQL("DROP TABLE IF EXISTS "+SKILLS);
        db.execSQL("DROP TABLE IF EXISTS "+COM_QUESTS);
        db.execSQL("DROP TABLE IF EXISTS "+MASTER);
        db.execSQL(QUEST_CREATE);
        db.execSQL(SKILL_CREATE);
        db.execSQL(COM_QUEST_CREATE);
        db.execSQL(MASTER_CREATE);

        //quest_insert("One",5,0,"Guitar");
        //quest_insert("Two",5,0,"Workout");
        //quest_delete("Six","Guitar");
        //quest_delete("Five","Guitar");
        //db.execSQL("update skills set skill_xp=0,skill_level=0 where skill_name='Guitar';");
        //db.execSQL("delete from skills where skill_name='Professional profile'");
        //db.execSQL("delete from quests where skill_name_ref='Professional profile'");
        //db.execSQL("insert into master values(0,0);");
        //db.execSQL("update quests set quest_xp=5 where quest_xp=1");
        //db.execSQL("update master set master_xp=155");
        //db.execSQL("delete from com_quests where com_skill_name='Misc'");
        //skills_disp();
        //quest_disp();
        //master_disp();
    }

    public void master_disp() {
        System.out.println("-----MASTER-----");
        Cursor cur=db.rawQuery("select * from master",null);
        cur.moveToFirst();
        String[] master=new String[2];
        do
        {
        //skill[0]=cur.getString(COL_SKILL_NAME);
        Integer xp=cur.getInt(COL_MASTER_XP);
        Integer level=cur.getInt(COL_MASTER_LEVEL);
        master[0]=xp.toString();
        master[1]=level.toString();
        System.out.println(master[0]+"   "+master[1]);
        }while(cur.moveToNext());
        cur.close();
    }

    public void skills_disp() {
        System.out.println("-----SKILLS-----");
        Cursor cur=db.rawQuery("select * from skills",null);
        cur.moveToFirst();
        String[] skill=new String[3];
        do
        {
        skill[0]=cur.getString(COL_SKILL_NAME);
        Integer xp=cur.getInt(COL_SKILL_XP);
        Integer level=cur.getInt(COL_SKILL_LEVEL);
        skill[1]=xp.toString();
        skill[2]=level.toString();
        System.out.println(skill[0]+"   "+skill[1]+"  "+skill[2]);
        }while(cur.moveToNext());
        cur.close();
    }

    public void quest_disp() {
        System.out.println("-----QUESTS-----");
        Cursor cur=db.rawQuery("select * from quests",null);
        if(cur.getCount()>0){
            cur.moveToFirst();
            String[] quest=new String[6];
            do
            {
                Integer row_id = cur.getInt(COL_QUEST_ROW_ID);
                quest[0] = row_id.toString();
                quest[1]=cur.getString(COL_QUEST_NAME);
                Integer xp=cur.getInt(COL_QUEST_XP);
                Integer diff=cur.getInt(COL_QUEST_DIFF);
                quest[2]=xp.toString();
                quest[3]=diff.toString();
                quest[4]=cur.getString(COL_SKILL_NAME_REF);
                quest[5]=cur.getString(COL_QUEST_COMPLETED);
                System.out.println(quest[0]+"   "+quest[1]+"  "+quest[2]+"   "+quest[3]+"   "+quest[4]+"   "+quest[5]);
            }while(cur.moveToNext());
        }

        cur.close();
    }

    public void com_quests_disp() {}
    //INSERT A RECORD INTO QUEST TABLE

    public void quest_insert(int row_id, String qname, int qxp, int diff, String skill, String deadline) {
//        quest_disp();
//        System.out.println("row_id:"+row_id);
        ContentValues initialValues=new ContentValues();
        initialValues.put(QUESTS_ROW_ID,row_id);
        initialValues.put(QUEST_NAME,qname);
        initialValues.put(QUEST_XP,qxp);
        initialValues.put(QUEST_DIFF,diff);
        initialValues.put(SKILL_NAME_REF,skill);
        initialValues.put(QUEST_DEADLINE,deadline);
        initialValues.put(QUEST_COMPLETED_FLAG,"N");
        db.insertOrThrow(QUESTS,null,initialValues);
    }

    //FOR COMPLETED QUESTS
    public void com_quest_insert(String qname, int qxp, int diff, String skill) {
        System.out.println(qname);
        ContentValues initialValues=new ContentValues();
        initialValues.put(COM_QUEST_NAME,qname);
        initialValues.put(COM_QUEST_XP,qxp);
        initialValues.put(COM_QUEST_DIFF,diff);
        initialValues.put(COM_SKILL_NAME,skill);
        db.insert(COM_QUESTS,null,initialValues);
    }

    //INSERT A SKILL
    public void skill_insert(String sname) {
        ContentValues initialValues=new ContentValues();
        initialValues.put(SKILL_NAME,sname);
        initialValues.put(SKILL_XP,0);
        initialValues.put(SKILL_LEVEL,0);
        initialValues.put(SKILL_UID,0);
        db.insert(SKILLS,null,initialValues);
    }

    public int get_skill_uid(String skill){
        String query ="SELECT "+SKILL_UID+" FROM "+SKILLS+" WHERE "+SKILL_NAME+" = '"+skill+"'";
        Cursor c=db.rawQuery(query,null);
        int skill_uid = 0;
        if(c!=null) {
            c.moveToFirst();
            do{
                skill_uid = c.getInt(0);
            }while(c.moveToNext());
        }
        c.close();
        return skill_uid;
    }

    public void set_skill_uid(String skill, int skill_uid){
        String query ="UPDATE "+SKILLS+" SET "+SKILL_UID+"="+skill_uid+" WHERE "+SKILL_NAME+" = '"+skill+"'";
        Cursor c=db.rawQuery(query,null);
        c.close();
    }

    //DELETE A SKILL
    public void skill_delete(String skill) {
        String where=SKILL_NAME+"='"+skill+"'";
        db.delete(SKILLS,where,null);
        where =  SKILL_NAME_REF+"='"+skill+"'";
        db.delete(QUESTS,where,null);
    }

    //GET ALL SKILLS
    public List<Skill> getAllSkills() {

        List<Skill> tempList = new ArrayList();
        Cursor c=db.rawQuery("select * from skills", null);

        if(c!=null) {
            c.moveToFirst();
            if (c.moveToFirst()){
                do{
                    String skill_name = c.getString(COL_SKILL_NAME);
                    int skill_xp = c.getInt(COL_SKILL_XP);
                    int skill_level = c.getInt(COL_SKILL_LEVEL);
                    int skill_uid = c.getInt(COL_SKILL_UID);
                    tempList.add(new Skill(skill_name,skill_xp,skill_level,skill_uid));
                }while(c.moveToNext());
            }
            c.close();
        }
        return tempList;
    }

    //GET ALL QUESTS UNDER A PARTICULAR SKILL
    public List<Quest> getAllQuests(String skill) {
        List<Quest> tempList = new ArrayList();
        Cursor c=db.rawQuery("select * from quests where skill_name_ref="+"'"+skill+"' and "
                                 +QUEST_COMPLETED_FLAG+"='N'", null);
        if(c!=null) {
            c.moveToFirst();
            if (c.moveToFirst()){
                do{
                    int row_id = c.getInt(COL_QUEST_ROW_ID);
                    String quest_name = c.getString(COL_QUEST_NAME);
                    int quest_xp = c.getInt(COL_QUEST_XP);
                    int quest_difficulty = c.getInt(COL_QUEST_DIFF);
                    String quest_deadline = c.getString(COL_QUEST_DEADLINE);
                    tempList.add(new Quest(row_id,quest_name,quest_xp,quest_difficulty,skill,quest_deadline));
                }while(c.moveToNext());
            }
            c.close();
        }
        return tempList;

    }

    public List<Quest> getAllComQuests(String skill) {
            List<Quest> tempList = new ArrayList();
            Cursor c=db.rawQuery("select * from quests where skill_name_ref="+"'"+skill+"' and "
                    +QUEST_COMPLETED_FLAG+"='Y'", null);
            if(c!=null) {
                c.moveToFirst();
                if (c.moveToFirst()){
                    do{
                        int row_id = c.getInt(COL_QUEST_ROW_ID);
                        String quest_name = c.getString(COL_QUEST_NAME);
                        int quest_xp = c.getInt(COL_QUEST_XP);
                        int quest_difficulty = c.getInt(COL_QUEST_DIFF);
                        String quest_deadline = c.getString(COL_QUEST_DEADLINE);
                        tempList.add(new Quest(row_id,quest_name,quest_xp,quest_difficulty,skill,quest_deadline));
                    }while(c.moveToNext());
                }
                c.close();
            }
            return tempList;

    }

    public int quest_completed_update(int row_id,String skill) {
        String where = QUESTS_ROW_ID+"="+row_id+" AND "+SKILL_NAME_REF+"='"+skill+"'";
        ContentValues args = new ContentValues();
        args.put(QUEST_COMPLETED_FLAG, "Y");
        return db.update(QUESTS, args, where, null);
    }

    //DELETE FROM QUEST TABLE
    public void quest_delete(int row_id,String skill) {
        String where=QUESTS_ROW_ID+"="+row_id+" AND "+SKILL_NAME_REF+"='"+skill+"'";
        db.delete(QUESTS,where,null);
    }

    public int quest_name_update(String name, int row_id, String skill){
        String where = QUESTS_ROW_ID+"="+row_id+" AND "+SKILL_NAME_REF+"='"+skill+"'";
        ContentValues args = new ContentValues();
        args.put(QUEST_NAME, name);
        return db.update(QUESTS, args, where, null);
    }

    //GET SKILL ID

    /*public int getSkillId(String skilln)
    {
        int sid;
        Cursor cur=db.rawQuery("select skill_rid from skills where skill_name="+"'"+skilln+"'",null);
        if(cur!=null)
        {
            cur.moveToFirst();
        }
        sid=cur.getInt(COL_SKILL_RID);
        return sid;

    }*/

    public void updateProgressSkill(String skill, int qxp) {
        Cursor cur=db.rawQuery("select * from skills where skill_name='"+skill+"';",null);
        cur.moveToFirst();
        int currXp=cur.getInt(COL_SKILL_XP);
        int currLvl=cur.getInt(COL_SKILL_LEVEL);
        if (currXp+qxp>=100)
        {
            currXp=currXp+qxp-100;
            currLvl++;
        }
        else
        {
            currXp=currXp+qxp;
        }
        db.execSQL("update skills set skill_xp="+currXp+",skill_level="+currLvl+" where skill_name='"+skill+"';");
    }

    public Integer[] updateMainProgress(int qxp) {
        Integer[] mainUpd=new Integer[2];
        Cursor cur=db.rawQuery("select * from master;",null);
        cur.moveToFirst();
        int currXp=cur.getInt(COL_MASTER_XP);
        int currLvl=cur.getInt(COL_MASTER_LEVEL);
        if (currXp+qxp>=1000)
        {
            currXp=currXp+qxp-1000;
            currLvl++;
        }
        else
        {
            currXp=currXp+qxp;
        }
        db.execSQL("update master set master_xp="+currXp+",master_level="+currLvl+";");
        mainUpd[0]=currXp;
        mainUpd[1]=currLvl;
        return mainUpd;
    }


    public String[] getMainProgress() {
        String q = "Select * from master";
        Cursor cur=db.rawQuery(q,null);
        cur.moveToFirst();
        Integer xp=cur.getInt(COL_MASTER_XP);
        Integer level=cur.getInt(COL_MASTER_LEVEL);
        String ret[]={xp.toString(),level.toString()};
        return ret;
    }

    public int getProgressSkill(String title) {
        int prog=0;
        Cursor cur=db.rawQuery("select * from skills where skill_name="+"'"+title+"'",null);
        if(cur!=null)
        {
            cur.moveToFirst();
        }
        prog=cur.getInt(COL_SKILL_XP);

        return prog;
    }

    public int getQuestXp(String quest, String skill) {
        //quest=quest.replaceAll("'","'");
        String q = "Select quest_xp from quests where quest_name= ? and skill_name_ref='"+skill+"';";
        Cursor cur=db.rawQuery(q, new String[] { quest});
        //Cursor cur=db.rawQuery("Select quest_xp from quests where quest_name='"+quest+"' and skill_name_ref='"+skill+"';",null);
        System.out.println(quest);
        int xp=0;
        cur.moveToFirst();
        xp=cur.getInt(0);
        return xp;
    }

    public int getQuestDiff(String quest, String skill) {
        //quest=quest.replaceAll("'","\'");
        String q = "Select quest_diff from quests where quest_name= ? and skill_name_ref='"+skill+"';";
        Cursor cur=db.rawQuery(q, new String[] { quest});
        int diff=0;
        cur.moveToFirst();
        diff=cur.getInt(0);
        return diff;
    }

    public void updQuestName(String quest, String quest_new, String skill) {
        db.execSQL("update quests set quest_name='"+quest_new+"' where quest_name=? and skill_name_ref=?",new String[]{quest,skill});
    }

    //HELPER CLASS
    private static class DataBaseHelper extends SQLiteOpenHelper {

    DataBaseHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(QUEST_CREATE);
        db.execSQL(SKILL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + SKILLS);
        db.execSQL("DROP TABLE IF EXISTS " + COM_QUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + MASTER);

    }

    }

}
