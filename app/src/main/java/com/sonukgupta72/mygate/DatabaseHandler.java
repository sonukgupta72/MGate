package com.sonukgupta72.mygate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userManager";
    private static final String KEY_ID = "id";
    private static final String TABLE_USER_DETSILS = "user";
    private static final String KEY_NAME = "name";
    private static final String KEY_UNIQ_ID = "uniqId";
    private static final String KEY_IMG_PATH = "imgPath";

    public DatabaseHandler (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USER_DETSILS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"+ KEY_IMG_PATH + " TEXT,"
                + KEY_UNIQ_ID + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETSILS);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addUser (String name, String uniqId, String filePath) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Contact Name
        values.put(KEY_UNIQ_ID, uniqId);
        values.put(KEY_IMG_PATH, filePath);

        // Inserting Row
        db.insert(TABLE_USER_DETSILS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get all contacts in a list view
    public List<UserData> getAllContacts() {
        List<UserData> userDataList = new ArrayList<UserData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER_DETSILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserData userData = new UserData();
                userData.setName(cursor.getString(0));
                userData.setFilePath(cursor.getString(1));
                userData.setUniqId(cursor.getString(2));
                // Adding contact to list
                userDataList.add(userData);
            } while (cursor.moveToNext());
        }

        // return contact list
        return userDataList;
    }
}
