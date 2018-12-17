package com.example.admin.clothes;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;
import static java.sql.DriverManager.println;

public class SQLiteHandlerBoard extends SQLiteOpenHelper {

    private static final String TAG = com.example.admin.clothes.SQLiteHandlerBoard.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // LoginActivity table name
    private static final String TABLE_BOARD = "boardtable";

    // LoginActivity Table Columns names
    private static final String BOARD_ID = "board_id";
    private static final String USER_NAME = "user_name";
    private static final String TITLE = "title";
    private static final String BOARD_CONTEXT = "board_context";
    private static final String BOARD_DATE = "board_date";

    public static SQLiteDatabase BoardDB;

    public SQLiteHandlerBoard(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOARD_TABLE = "CREATE TABLE " + TABLE_BOARD + " ("
                + BOARD_ID + " INTEGER PRIMARY KEY, " +USER_NAME + " TEXT FOREING KEY, " + TITLE + " TEXT, "
                + BOARD_CONTEXT + " TEXT, " + BOARD_DATE + " TEXT" + ");";
        db.execSQL(CREATE_BOARD_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase BoardDB, int oldVersion, int newVersion) {
        // Drop older table if existed
        BoardDB.execSQL("DROP TABLE IF EXISTS " + TABLE_BOARD+";");

        // Create tables again
        onCreate(BoardDB);
    }

    /**
     * Storing user details in database
     * */
    public void addBoard(String title, String board_context, String user_name, String board_date) {
        BoardDB = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, user_name); //username
        values.put(TITLE, title); // title
        values.put(BOARD_CONTEXT, board_context); //context
        values.put(BOARD_DATE, board_date); // Created At

        // Inserting Row
        long id = BoardDB.insert(TABLE_BOARD, null, values);
        BoardDB.close(); // Closing database connection

        Log.d(TAG, "New Board inserted into sqlite: " + id);
       Toast.makeText(getApplicationContext(), "게시글이 등록되었습니다.", Toast.LENGTH_LONG).show();
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUBoardDetails() {
        HashMap<String, String> board = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_BOARD+";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            board.put("user_name", cursor.getString(1));
            board.put("title", cursor.getString(2));
            board.put("board_context", cursor.getString(3));
            board.put("board_date", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching Board from Sqlite: " + board.toString());

        return board;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteBoards() {
        SQLiteDatabase BoardDB = this.getWritableDatabase();
        // Delete All Rows
        BoardDB.delete(TABLE_BOARD, null, null);
        BoardDB.close();

        Log.d(TAG, "Deleted all Board info from sqlite");
    }

    public void onOpen(SQLiteDatabase db){
        println("opened database ["+DATABASE_NAME+ "].");
    }



    public void close(){
        BoardDB.close();
    }

    public void IteminsertRecord( String username, String title, String board_context, String board_date) {
        println("inserting records using parameters.");



        BoardDB.execSQL("insert or ignore into " + TABLE_BOARD + " (username, title, board_context, board_date) " +
                "values " + "(" + username + ", " + title + ", " + board_context + ", "
                + board_date+");");
        Toast.makeText(getApplicationContext(), "게시글이 등록되었습니다.", Toast.LENGTH_LONG).show();

    }

}