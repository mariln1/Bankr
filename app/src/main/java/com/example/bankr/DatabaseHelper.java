package com.example.bankr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.math.BigDecimal;

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
        cv.put(COLUMN_BALANCE, user.getBalance().doubleValue());
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

    public boolean checkUsernameAndPassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryStr = "SELECT USERNAME FROM " + USERS_TABLE + " WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "'";
        Cursor cursor = db.rawQuery(queryStr, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }

    public BigDecimal getBalance(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryStr = "SELECT BALANCE FROM " + USERS_TABLE + " WHERE USERNAME = '" + username + "'";
        Cursor cursor = db.rawQuery(queryStr, null);
        if (cursor.moveToFirst()) {
            return BigDecimal.valueOf(cursor.getDouble(0));
        }
        else {
            return new BigDecimal(-1);
        }
    }

    public BigDecimal updateUser(String username, BigDecimal balance) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE "+USERS_TABLE +" SET " + COLUMN_BALANCE+ " = "+balance+" WHERE "+COLUMN_USERNAME+ " = '"+username +"'";
        db.execSQL(sql);
        return balance;

    }
}
