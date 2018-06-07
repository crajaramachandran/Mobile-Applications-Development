package com.example.raja.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StockDBAdapter extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StockDB";
    private static final String TABLE_NAME = "StockTable";
    private static final String SYMBOL = "StockSymbol";
    private static final String NAME = "StockName";
    private static final String LE = "ListingExchange";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    SYMBOL + " TEXT not null unique," +
                    NAME + " TEXT not null)";
    private SQLiteDatabase database;
    private static final String DATABASE_ALTER_TABLE_FOR_V2 = "ALTER TABLE "
            + TABLE_NAME + " ADD COLUMN " + LE + " TEXT not null";

    public StockDBAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }





    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"Table Created.");
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 2) {
            db.execSQL(DATABASE_ALTER_TABLE_FOR_V2);
        }
    }

    public ArrayList<String[]> loadStocks() {
        ArrayList<String[]> tempStocks = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_NAME,
                new String[]{SYMBOL, NAME}, null, null, null, null,SYMBOL);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getCount();

            for (int i = 0; i < count; i++) {
                String str[]= new String[2];
                str[0] = cursor.getString(0);
                str[1] = cursor.getString(1);
                tempStocks.add(str);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return tempStocks;
    }

    public void addStock(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(SYMBOL, stock.getStockSymbol());
        values.put(NAME, stock.getStockName());
        deleteStock(stock.getStockSymbol());
        long key = database.insert(TABLE_NAME, null, values);
    }

    public void deleteStock(String name) {
        int cnt = database.delete(TABLE_NAME, SYMBOL + " = ?", new String[]{name});
        Log.d(TAG,"cnt:"+cnt);
    }

    public void dumpLog() {
        Log.d(TAG, "dumpLog: vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String name = cursor.getString(1);
                Log.d(TAG, "dumpLog: " +
                        String.format("%s %-18s",SYMBOL+":",symbol)+
                        String.format("%s %-18s",NAME+":",name));
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d(TAG, "dumpLog: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

//    public void testSQL() {
//        database.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
//        database.execSQL(SQL_CREATE_TABLE);
//    }
//
//    public void checkLoadStocks(){
//        ArrayList<String[]> tempStocks = new ArrayList<>();
//        Cursor cursor = database.query(
//                TABLE_NAME,
//                new String[]{SYMBOL, NAME}, null, null, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//            int count = cursor.getCount();
//
//            for (int i = 0; i < count; i++) {
//                String str[]= new String[2];
//                str[0] = cursor.getString(0);
//                str[1] = cursor.getString(1);
//                tempStocks.add(str);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//    }


}