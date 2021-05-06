package com.example.bankr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String USERS_TABLE = "USERS";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_BALANCE = "BALANCE";
    public static final String COLUMN_PASSWORD = "PASSWORD";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String statement = "CREATE TABLE IF NOT EXISTS " + USERS_TABLE + " ( "
                + COLUMN_USERNAME + " varchar(127) PRIMARY KEY, "
                + COLUMN_BALANCE + " REAL NOT NULL, "
                +  COLUMN_PASSWORD + " varchar(127) NOT NULL)";
        db.execSQL(statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addUser (User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_BALANCE, user.getBalance());
        cv.put(COLUMN_PASSWORD, user.getPassword());

        long insert = db.insert(USERS_TABLE, null, cv);
        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean checkIfUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryStr = "SELECT USERNAME FROM " + USERS_TABLE + " WHERE USERNAME = '" + username + "'";
        Cursor cursor = db.rawQuery(queryStr, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }
}
