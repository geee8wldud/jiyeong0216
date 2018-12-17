package com.example.admin.clothes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static java.sql.DriverManager.println;

public class DBHelper extends SQLiteOpenHelper {

    public static DBHelper dbhelper=null;
    public static String dbname="board.db";
    public static String tablename="boardtable";
    public static Cursor c2;

    String username, title, board_context, board_date;


    private SQLiteDatabase db1;
    private String TAG;


    public DBHelper(Context context){
        super(context, tablename, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        println("creating table ["+tablename+"].");
        try{
            String DROP_SQL="drop table if exists "+tablename;
            db.execSQL(DROP_SQL);
        }catch (Exception ex){
            Log.e(TAG, "Exception in DROP_SQL",ex);
        }
        String CREATE_SQL="create table "+tablename+" ("+"boardid integer PRIMARY KEY AUTO_INCREMENT, username varchar(50), " +
                "title varchar(50), board_context varchar(500), " +
                "board_date varchar(50))";

        try{
            db.execSQL(CREATE_SQL);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }
        println("inserting records");
        try {
            IteminsertRecord("user", "Hello", "world!", "20170530");
        } catch (Exception ex) {
            Log.e(TAG, "Exception in insert SQL", ex);
        }
    }

    public void onOpen(SQLiteDatabase db){
        println("opened database ["+dbname+ "].");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS "+tablename);
        // onCreate(db);
        Log.w(TAG, "Upgrading database from version "+oldVersion+" to"+newVersion +".");
    }

    public void close(){
        db1.close();
    }

    public void IteminsertRecord(String username, String title, String board_context, String board_date) {
        println("inserting records using parameters.");

        int count=1;



        db1.execSQL("insert or ignore into " + tablename + " (username, title, board_context, board_date) " +
                "values " + "(" + username + ", " + title + ", "
                + board_context + ", " + board_date + ");");

    }

}
