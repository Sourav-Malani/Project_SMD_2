package com.ass2.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ass2.Models.CartModel;
import com.ass2.Models.UserModel;

import java.util.ArrayList;

public class UserDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cheesybites.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_USER = "users";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_USER_PASSWORD = "user_password";
    public static final String COLUMN_USER_PHONE = "user_phone";
    public static final String COLUMN_USER_ADDRESS = "user_address";

    public static final String COLUMN_USER_IMAGE_URL = "user_image_url";

    public static final String COLUMN_USER_LOGGED_IN = "user_logged_in";
    public static final String COLUMN_DATE_REGISITERED = "date_registered";



    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create user table
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT,"
                + COLUMN_USER_PHONE + " TEXT,"
                + COLUMN_USER_ADDRESS + " TEXT,"
                + COLUMN_USER_IMAGE_URL + " TEXT,"
                + COLUMN_USER_LOGGED_IN + " BOOLEAN,"
                + COLUMN_DATE_REGISITERED + " TEXT"
                + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    public ArrayList<UserModel> getUser(String userEmail){
        ArrayList<UserModel> userModelArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + " = '" + userEmail + "'";
        Cursor cursor = db.rawQuery(query, null);


        if(cursor.moveToFirst()){
            do{
                UserModel userModel = new UserModel();
                userModel.setFullName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                userModel.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                userModel.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                userModel.setPhoneNo(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
                userModel.setDeliveryAddress(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)));
                userModel.setImageURL(cursor.getString(cursor.getColumnIndex(COLUMN_USER_IMAGE_URL)));
                userModel.setLoggedIn(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_LOGGED_IN)) > 0);
                userModel.setDateRegistered(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_REGISITERED)));
                userModelArrayList.add(userModel);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userModelArrayList;

    }

    public boolean updateUserDetails(UserModel user){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_USER + " SET "
                + COLUMN_USER_NAME + " = '" + user.getFullName() + "', "
                + COLUMN_USER_EMAIL + " = '" + user.getEmail() + "', "
                + COLUMN_USER_PASSWORD + " = '" + user.getPassword() + "', "
                + COLUMN_USER_PHONE + " = '" + user.getPhoneNo() + "', "
                + COLUMN_USER_ADDRESS + " = '" + user.getDeliveryAddress() + "', "
                + COLUMN_USER_IMAGE_URL + " = '" + user.getImageURL() + "', "
                + COLUMN_USER_LOGGED_IN + " = '" + user.getLoggedIn() + "', "
                + COLUMN_DATE_REGISITERED + " = '" + user.getDateRegistered() + "' "
                + " WHERE " + COLUMN_USER_EMAIL + " = '" + user.getEmail() + "'";
        db.execSQL(query);
        db.close();
        return true;
    }

    public boolean addUser(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getFullName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        // Add other fields as necessary

        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result != -1; // Return true if insert is successful
    }

    public long insertUser(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getFullName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_PHONE, user.getPhoneNo());
        values.put(COLUMN_USER_ADDRESS, user.getDeliveryAddress());
        values.put(COLUMN_USER_IMAGE_URL, user.getImageURL());
        values.put(COLUMN_USER_LOGGED_IN, user.getLoggedIn());
        values.put(COLUMN_DATE_REGISITERED, user.getDateRegistered());

        // Insert the user data and get the local ID
        long localID = db.insert(TABLE_USER, null, values);

        // Set the local ID in the UserModel
        user.setLocalID(localID);

        db.close();
        return localID;
    }

    public ArrayList<UserModel> getUser(String userEmail, long localID) {
        ArrayList<UserModel> userModelArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + " = '" + userEmail + "' OR " + COLUMN_ID + " = " + localID;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                UserModel userModel = new UserModel();
                userModel.setFullName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                userModel.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                userModel.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                userModel.setPhoneNo(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
                userModel.setDeliveryAddress(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)));
                userModel.setImageURL(cursor.getString(cursor.getColumnIndex(COLUMN_USER_IMAGE_URL)));
                userModel.setLoggedIn(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_LOGGED_IN)) > 0);
                userModel.setDateRegistered(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_REGISITERED)));
                userModel.setLocalID(cursor.getLong(cursor.getColumnIndex(COLUMN_ID))); // Set local ID

                userModelArrayList.add(userModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userModelArrayList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
}
