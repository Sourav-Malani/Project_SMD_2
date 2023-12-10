package com.ass2.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ass2.Adapters.MainAdapter;

public class DBHelper extends SQLiteOpenHelper {

    final static String DB_NAME = "cart.db";
    final static int DB_VERSION = 1;
    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table orders " +
                        "(id integer primary key autoincrement," +
                        "name text," +
                        "email text," +
                        "price text," +
                        "image integer," +
                        "quantity integer," +
                        "description text," +
                        "foodName text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(
                "DROP table if exists orders"
        );
        onCreate(sqLiteDatabase);
    }

    public boolean insertOrder(String name, String email, String price, int image, String description, String foodName,int quantity){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("email",email);
        values.put("price",price);
        values.put("image",image);
        values.put("quantity",quantity);
        values.put("description",description);
        values.put("foodName",foodName);
        long id = database.insert("orders",null,values);
        if(id <= 0){
            return false;
        }else{
            return true;
        }
    }
}
