package com.example.bankr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.sql.PreparedStatement;

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
        db.close();
        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean checkIfUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT USERNAME FROM USERS WHERE USERNAME = ?;",
                new String[] {username});
        if (cursor.moveToFirst()) {
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }
    }

    public String checkUsernameAndPassword(String username, String password) {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT USERNAME FROM USERS WHERE USERNAME = ? AND PASSWORD = '" + password + "';" ,
                new String[]{username} );
        //db.close();
        String ret;
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            ret = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
        }
        else {
            ret = "";
        }
        db.close();
        return ret;
    }

    public BigDecimal getBalance(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT BALANCE FROM USERS WHERE USERNAME = ?;",
                new String[] {username});
        BigDecimal ret;
        if (cursor.moveToFirst()) {
            ret = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(COLUMN_BALANCE)));
        }
        else {
            ret = new BigDecimal(-1);
        }
        db.close();
        return ret;
    }

    public boolean updateUser(String username, BigDecimal balance) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE USERS SET BALANCE = ? WHERE USERNAME = ?";
        db.execSQL(sql, new String[] { String.valueOf(balance), username});
        db.close();
        return true;

    }
}
